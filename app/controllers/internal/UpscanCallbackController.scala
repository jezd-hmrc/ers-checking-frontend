/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.internal

import controllers.ERSCheckingBaseController
import models.upscan._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc._
import services.SessionService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.SessionId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal

trait UpscanCallbackController extends ERSCheckingBaseController {

	val sessionService: SessionService

	def callbackCsv(uploadId: UploadId, sessionId: String): Action[JsValue] = Action.async(parse.json) {
		implicit request =>
			request.body.validate[UpscanCallback].fold (
				invalid = errors => {
					Logger.error(s"[UpscanController][callbackCsv] Failed to validate UpscanCallback json with errors: $errors")
					Future.successful(BadRequest)
				},
				valid = callback => {
					val uploadStatus: UploadStatus = callback match {
						case callback: UpscanReadyCallback =>
							UploadedSuccessfully(callback.uploadDetails.fileName, callback.downloadUrl.toExternalForm)
						case UpscanFailedCallback(_, details) =>
							Logger.warn(s"[UpscanController][callbackCsv] Upload id: ${uploadId.value} failed. Reason: ${details.failureReason}. Message: ${details.message}")
							Failed
					}
					Logger.info(s"[UpscanController][callbackCsv] Updating CSV callback for upload id: ${uploadId.value} to ${uploadStatus.getClass.getSimpleName}")

					(for{
						callbackList 	<- 		sessionService.getCallbackRecordCsv(sessionId)
						updatedList 	= 		UpscanCsvFilesCallback(uploadId, uploadStatus) :: callbackList.files
						_ 						<- 		sessionService.updateCallbackRecordCsv(callbackList.copy(files = updatedList), sessionId)
					} yield {
						Ok
					}) recover {
						case NonFatal(e) =>
							Logger.error(s"[UpscanController][callbackCsv] Failed to update cache after Upscan callback for UploadID: ${uploadId.value}, " +
								s"ScRef: $sessionId", e)
							InternalServerError("Exception occurred when attempting to store data")
					}
				}
			)
	}

	def callbackOds(sessionId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
		implicit val headerCarrier: HeaderCarrier = hc.copy(sessionId = Some(SessionId(sessionId)))
		request.body.validate[UpscanCallback].fold (
			invalid = errors => {
				Logger.error(s"[UpscanController][callbackOds] Failed to validate UpscanCallback json with errors: $errors")
				Future.successful(BadRequest)
			},
			valid = callback => {
				val uploadStatus = callback match {
					case callback: UpscanReadyCallback =>
						UploadedSuccessfully(callback.uploadDetails.fileName, callback.downloadUrl.toExternalForm)
					case UpscanFailedCallback(_, details) =>
						Logger.warn(s"[UpscanController][callbackOds] Callback for session id: $sessionId failed. " +
							s"Reason: ${details.failureReason}. Message: ${details.message}")
						Failed
				}
				Logger.info(s"[UpscanController][callbackOds] Updating callback for session: $sessionId to ${uploadStatus.getClass.getSimpleName}")
				sessionService.updateCallbackRecord(uploadStatus)(request, headerCarrier).map(_ => Ok) recover {
					case e: Throwable =>
						Logger.error(s"[UpscanController][callbackOds] Failed to update callback record for session: $sessionId, " +
							s"timestamp: ${System.currentTimeMillis()}.", e)
						InternalServerError("Exception occurred when attempting to update callback data")
				}
			}
		)
	}
}


object UpscanCallbackController extends UpscanCallbackController {
	override val sessionService: SessionService = SessionService
}


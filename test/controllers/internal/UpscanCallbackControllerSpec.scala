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

import java.net.URL
import java.time.Instant

import models.upscan._
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import play.api.libs.json._
import play.api.mvc.Request
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.SessionService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.test.UnitSpec
import models.upscan.UpscanCallback._

import scala.concurrent.Future

class UpscanCallbackControllerSpec extends UnitSpec with MockitoSugar
																										with OneAppPerSuite
																										with BeforeAndAfterEach {
	implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val request: Request[_] = FakeRequest()

	implicit val failedWrites: OWrites[UpscanFailedCallback] = Json.writes[UpscanFailedCallback]
		.transform((js: JsValue) => js.as[JsObject] + ("fileStatus" -> JsString("FAILED")))

	implicit val readWrites: OWrites[UpscanReadyCallback] =
		Json.writes[UpscanReadyCallback].transform((js: JsValue) => js.as[JsObject] + ("fileStatus" -> JsString("READY")))

  val mockSessionService: SessionService = mock[SessionService]
	val sessionId = "sessionId"

	val uploadDetails: UploadDetails = UploadDetails(Instant.now(), "checksum", "fileMimeType", "fileName")
	val readyCallback: UpscanReadyCallback = UpscanReadyCallback(Reference("Reference"), new URL("https://callbackUrl.com"), uploadDetails)
	val failedCallback: UpscanFailedCallback = UpscanFailedCallback(Reference("Reference"), ErrorDetails("failureReason", "message"))

  def request(body: JsValue): FakeRequest[JsValue] = FakeRequest().withBody(body)

  lazy val upscanCallbackController: UpscanCallbackController = new UpscanCallbackController {
		override val sessionService: SessionService = mockSessionService
  }

  override def beforeEach(): Unit = {
    reset(mockSessionService)
    super.beforeEach()
  }

  "callbackCsv" should {
    val uploadId: UploadId = UploadId("ID")

    "update upload status to Uploaded Successfully" when {
			"callback is UpscanReadyCallback" in {
				val uploadedSuccessfully = UploadedSuccessfully("name", "downloadUrl", noOfRows = None)
				val callbackResponse = UpscanCsvFilesCallbackList(List(UpscanCsvFilesCallback(uploadId, uploadedSuccessfully)))

				when(mockSessionService.getCallbackRecordCsv(any())(any(), any())).thenReturn(callbackResponse)
				when(mockSessionService.updateCallbackRecordCsv(any(), any())(any(), any())).thenReturn(Future.successful(mock[CacheMap]))

				val result = upscanCallbackController.callbackCsv(uploadId, sessionId)(request(Json.toJson(readyCallback)))
				status(result) shouldBe OK
				verify(mockSessionService, times(1)).getCallbackRecordCsv(any())(any(), any())
				verify(mockSessionService, times(1)).updateCallbackRecordCsv(any(), any())(any(), any())
			}
		}

    "update upload status to Failed" when {
      "callback is UpscanFailedCallback and upload is InProgress" in {
				val callbackResponse = UpscanCsvFilesCallbackList(List(UpscanCsvFilesCallback(uploadId, Failed)))
				when(mockSessionService.getCallbackRecordCsv(any())(any(), any())).thenReturn(callbackResponse)
				when(mockSessionService.updateCallbackRecordCsv(any(), any())(any(), any())).thenReturn(Future.successful(mock[CacheMap]))

				val result = upscanCallbackController.callbackCsv(uploadId, sessionId)(request(Json.toJson(failedCallback)))

				status(result) shouldBe OK
				verify(mockSessionService, times(1)).getCallbackRecordCsv(any())(any(), any())
				verify(mockSessionService, times(1)).updateCallbackRecordCsv(any(), any())(any(), any())
			}
		}

		"return InternalServerError" when {
			"updating the cache fails" in {
				val body = UpscanFailedCallback(Reference("ref"), ErrorDetails("failed", "message"))
				when(mockSessionService.getCallbackRecordCsv(any())(any(), any())).thenReturn(Future.failed(new Exception("Test exception")))
				val result = await(upscanCallbackController.callbackCsv(uploadId, sessionId)(request(Json.toJson(body))))

				status(result) shouldBe INTERNAL_SERVER_ERROR
				verify(mockSessionService, times(1)).getCallbackRecordCsv(any())(any(), any())
			}
		}

		"return a BadRequest" when {
			"callback data is not in the correct format" in {
				val jsonBody = Json.parse("""{"key":"value"}""")
				val result = await(upscanCallbackController.callbackCsv(uploadId, sessionId)(request(jsonBody)))

				status(result) shouldBe BAD_REQUEST
				verify(mockSessionService, never()).getCallbackRecordCsv(any())(any(), any())
				verify(mockSessionService, never()).updateCallbackRecordCsv(any(), any())(any(), any())
			}
		}
  }

	"callbackOds" must {

		"update callback" when {
			"Upload status is UpscanReadyCallback" in {
				when(mockSessionService.updateCallbackRecord(any())(any(), any())).thenReturn(Future.successful(mock[CacheMap]))

				val result = upscanCallbackController.callbackOds(sessionId)(request(Json.toJson(readyCallback)))
				status(result) shouldBe OK
				verify(mockSessionService, times(1)).updateCallbackRecord(any())(any(), any())
			}

			"Upload status is failed" in {
				when(mockSessionService.updateCallbackRecord(any())(any(), any())).thenReturn(Future.successful(mock[CacheMap]))
				val result = upscanCallbackController.callbackOds(sessionId)(request(Json.toJson(failedCallback)))

				status(result) shouldBe OK
				verify(mockSessionService, times(1)).updateCallbackRecord(any())(any(), any())
			}
		}

		"return InternalServerError" when {
			"updating the cache fails" in {
				when(mockSessionService.updateCallbackRecord(any())(any(), any())).thenReturn(Future.failed(new Exception("Test exception")))
				val result = await(upscanCallbackController.callbackOds(sessionId)(request(Json.toJson(readyCallback))))

				status(result) shouldBe INTERNAL_SERVER_ERROR
				verify(mockSessionService, times(1)).updateCallbackRecord(any())(any(), any())
			}
		}

		"return a BadRequest" when {
			"callback data is not in the correct format" in {
				val jsonBody = Json.parse("""{"key":"value"}""")
				val result = await(upscanCallbackController.callbackOds(sessionId)(request(jsonBody)))

				status(result) shouldBe BAD_REQUEST
				verify(mockSessionService, never()).updateCallbackRecord(any())(any(), any())
			}
		}
	}
}

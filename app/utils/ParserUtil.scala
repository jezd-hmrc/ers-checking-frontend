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

package utils

import models.SheetErrors
import models.upscan.UpscanCsvFilesCallback
import play.api.mvc.{AnyContent, Request}
import play.api.{Logger, Play}
import services.ERSTemplatesInfo
import scala.collection.mutable.ListBuffer
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import uk.gov.hmrc.http.HeaderCarrier

object ParserUtil extends ParserUtil {
  override val cacheUtil: CacheUtil = CacheUtil
}

trait ParserUtil {
  val cacheUtil: CacheUtil

  def formatDataToValidate(rowData: Seq[String], sheetName: String): Seq[String] = {
    val sheetColSize = ERSTemplatesInfo.ersSheets(sheetName.replace(".csv", "")).headerRow.size
    if(rowData.size < sheetColSize) {
      Logger.debug(s"Difference between amount of columns ${rowData.size} and amount of headers $sheetColSize")
      val additionalEmptyCells: Seq[String] = Seq.fill(sheetColSize - rowData.size)("")
      (rowData ++ additionalEmptyCells).take(sheetColSize)
    }
    else {
      rowData.take(sheetColSize)
    }
  }

  def isFileValid(errorList: ListBuffer[SheetErrors], source: String, file: Option[UpscanCsvFilesCallback] = None)
								 (implicit request: Request[AnyContent], hc: HeaderCarrier): Future[Try[Boolean]] = {
    if (isValid(errorList)) {
      Future.successful(Success(true))
    }
    else {
			val updatedErrorCount = getTotalErrorCount(errorList)
			val updatedErrorList = getSheetErrors(errorList)
			val id = if(file.isDefined) file.get.uploadId else ""

      val result = for {
				_ <- cacheUtil.cache[Long](s"${CacheUtil.SCHEME_ERROR_COUNT_CACHE}$id", updatedErrorCount )
				_ <- cacheUtil.cache[ListBuffer[SheetErrors]](s"${CacheUtil.ERROR_LIST_CACHE}$id", updatedErrorList)
			} yield Success(false)

      result recover {
        case ex: Exception => Failure(ex)
      }
    }
  }

  def isValid(schemeErrors:ListBuffer[SheetErrors]):Boolean = {
    for(sheet <- schemeErrors) {
      for(_ <- sheet.errors){
        return false
      }
    }
    true
  }

  def getTotalErrorCount(schemeErrors: ListBuffer[SheetErrors]): Long = {
    var totalErrors = 0
    if(totalErrors != schemeErrors.size)
      for(i <- schemeErrors.indices) totalErrors += schemeErrors(i).errors.length
    totalErrors
  }


  def getSheetErrors(schemeErrors: ListBuffer[SheetErrors]): ListBuffer[SheetErrors] = {
    val errorCount: Int = Play.configuration.getInt(s"errorDisplayCount").getOrElse(100)
    schemeErrors.map { schemeError =>
      SheetErrors(schemeError.sheetName,schemeError.errors.take(errorCount))
    }
  }

}

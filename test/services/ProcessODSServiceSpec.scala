/*
 * Copyright 2016 HM Revenue & Customs
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

package services

import java.io.File

import hmrc.gsi.gov.uk.services.validation._
import models.SheetErrors
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.libs.Files
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{Request, MultipartFormData}
import play.api.mvc.MultipartFormData.FilePart
import uk.gov.hmrc.play.frontend.auth.AuthContext
import uk.gov.hmrc.play.http.HeaderCarrier
import utils.CacheUtil
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import utils.UploadedFileUtil

import scala.collection.mutable.ListBuffer

class ProcessODSServiceSpec extends UnitSpec with MockitoSugar with WithFakeApplication {

  val mockFile: File = mock[File]
  when(
    mockFile.getAbsolutePath
  ).thenReturn(
      "/test/ods/EMI_template_V3.ods"
    )

  val mockTempFile: Files.TemporaryFile = mock[Files.TemporaryFile]
  when(
    mockTempFile.file
  ).thenReturn(
      mockFile
    )

  val mockUploadedFile: MultipartFormData.FilePart[Files.TemporaryFile] = mock[MultipartFormData.FilePart[Files.TemporaryFile]]
  when(
    mockUploadedFile.filename
  ).thenReturn(
      "EMI_template_V3.ods"
    )
  when(
    mockUploadedFile.ref
  ).thenReturn(
      mockTempFile
    )

  def getMockFile(isEmpty: Boolean): MultipartFormData[Files.TemporaryFile] = {
    if(isEmpty) {
      MultipartFormData(dataParts = Map(), files = Seq(), badParts = Seq(), missingFileParts = Seq())
    }
    else {
      val part = FilePart[TemporaryFile](key = "fileUpload", filename = "EMI_template_V3.ods", contentType = Some("Content-Type: multipart/form-data"), ref = mockTempFile)
      MultipartFormData(dataParts = Map(), files = Seq(part), badParts = Seq(), missingFileParts = Seq())
    }
  }


  "calling performODSUpload" should {

    def buildProcessODSService() = new ProcessODSService {
      override val uploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      override val cacheUtil:CacheUtil = mock[CacheUtil]
      override def checkFileType(uploadedFile: MultipartFormData.FilePart[Files.TemporaryFile])(implicit scheme:String, authContext: AuthContext, hc: HeaderCarrier,request: Request[_]):ListBuffer[SheetErrors] = new ListBuffer
    }

    //    "return (false, ers_check_file.no_file_error) if there isn't uploaded file" in {
    //
    //      val file = getMockFile(true)
    //      val result = await(buildProcessODSService().performODSUpload()(FakeRequest().withMultipartFormDataBody(file)))
    //      result._1 shouldBe false
    //      result._2.get shouldBe Messages("ers_check_file.no_file_error")
    //    }

    //    "return the result of checkFileType if there is an uploaded file" in {
    //
    //      val file = getMockFile(false)
    //      val result = await(buildProcessODSService().performODSUpload()(FakeRequest().withMultipartFormDataBody(file)))
    //      result._1 shouldBe true
    //      result._2.isEmpty shouldBe true
    //    }
  }

  "calling checkFileType" should {

    def buildProcessODSService(checkODSFileTypeResult: Boolean) = new ProcessODSService {
      val mockUploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      when(
        mockUploadedFileUtil.checkODSFileType(anyString())
      ).thenReturn(
          checkODSFileTypeResult
        )
      override val uploadedFileUtil: UploadedFileUtil = mockUploadedFileUtil
      override val cacheUtil:CacheUtil = mock[CacheUtil]
      override def parseOdsContent(fileName: String)(implicit scheme:String, authContext: AuthContext, hc: HeaderCarrier,request: Request[_]): ListBuffer[SheetErrors] = new ListBuffer
    }

    //    "return (false, ers_check_file.file_type_error) if uploaded file isn't .ods" in {
    //      val service = buildProcessODSService(false)
    //      val result = service.checkFileType(mockUploadedFile)
    //      result._1 shouldBe false
    //      result._2.get shouldBe Messages("ers_check_file.file_type_error")
    //    }

    //    "return the result of parseOdsContent if uploaded file is correct format" in {
    //      val service = buildProcessODSService(true)
    //      val result = service.checkFileType(mockUploadedFile)
    //      result._1 shouldBe true
    //      result._2.isEmpty shouldBe true
    //    }
  }

  "calling parseOdsContent" should {

    def buildProcessODSService(validateDataResult: Boolean) = new ProcessODSService {
      override val uploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      override val cacheUtil:CacheUtil = mock[CacheUtil]
    }

    //    "return (true, None) if validation is successful" in {
    //      val service = buildProcessODSService(true)
    //      val userDirectory = System.getProperty("user.dir")
    //       val result = service.parseOdsContent(userDirectory + "/test/ods/EMI_template_V3.ods")
    //      result._1 shouldBe true
    //      result._2.isEmpty shouldBe true
    //    }
    //
    //    "return (false, None) if validation is not successful" in {
    //      val service = buildProcessODSService(false)
    //      val userDirectory = System.getProperty("user.dir")
    //       val result = service.parseOdsContent(userDirectory + "/test/ods/EMI_template_V3.ods")
    //      result._1 shouldBe false
    //      result._2.isEmpty shouldBe true
    //    }
  }

  "calling getSheetErrors" should {

    def buildProcessODSService = new ProcessODSService {
      override val uploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      override val cacheUtil:CacheUtil = mock[CacheUtil]
    }

    val schemeErrors = new ListBuffer[SheetErrors]()

    val list1 = ValidationError(Cell("A",1,"abc"),"001", "error.1", "This entry must be 'yes' or 'no'.")
    val list2 = ValidationError(Cell("B",1,"abc"),"001", "error.1", "This entry must be 'yes' or 'no'.")
    val list3 = ValidationError(Cell("C",1,"abc"),"001", "error.1", "This entry must be 'yes' or 'no'.")

    val sheetErrors3 = new ListBuffer[ValidationError]()
    sheetErrors3 += list1
    sheetErrors3 += list2
    sheetErrors3 += list3
    schemeErrors += SheetErrors("sheet_tab_1",sheetErrors3)
    schemeErrors += SheetErrors("sheet_tab_2",sheetErrors3)

    val sheetErrors1 = new ListBuffer[ValidationError]()
    sheetErrors1 += list1
    schemeErrors += SheetErrors("sheet_tab_3",sheetErrors1)

    "return up to the first 100 errors of each sheet" in {

      val result = buildProcessODSService.getSheetErrors(schemeErrors)

      result(0).errors.size shouldBe 3
      result(1).errors.size shouldBe 3
      result(2).errors.size shouldBe 1
    }
  }

  "validating the scheme" should {

    def buildProcessODSService = new ProcessODSService {
      override val uploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      override val cacheUtil:CacheUtil = mock[CacheUtil]
    }

    val error = ValidationError(Cell("A",1,"abc"),"001", "error.1", "This entry must be 'yes' or 'no'.")
    val errors = new ListBuffer[ValidationError]
    errors ++= List(error)

    val invalidSheet = SheetErrors ("", errors)
    val validSheet = SheetErrors ("", new ListBuffer())

    "return true if no errors are found" in {
      val schemeErrors = new ListBuffer[SheetErrors]()

      schemeErrors += validSheet
      schemeErrors += validSheet
      schemeErrors += validSheet

      buildProcessODSService.isValid(schemeErrors) shouldBe true
    }

    "return false if errors are found in first sheet" in {
      val schemeErrors = new ListBuffer[SheetErrors]()

      schemeErrors += invalidSheet
      schemeErrors += validSheet
      schemeErrors += validSheet

      buildProcessODSService.isValid(schemeErrors) shouldBe false
    }

    "return false if errors are found in second sheet" in {
      val schemeErrors = new ListBuffer[SheetErrors]()

      schemeErrors += validSheet
      schemeErrors += invalidSheet
      schemeErrors += validSheet

      buildProcessODSService.isValid(schemeErrors) shouldBe false
    }

    "return false if errors are found in third sheet" in {
      val schemeErrors = new ListBuffer[SheetErrors]()

      schemeErrors += validSheet
      schemeErrors += validSheet
      schemeErrors += invalidSheet

      buildProcessODSService.isValid(schemeErrors) shouldBe false
    }

  }

  "getTotalErrorCount" should {

    def buildProcessODSService = new ProcessODSService {
      override val uploadedFileUtil: UploadedFileUtil = mock[UploadedFileUtil]
      override val cacheUtil:CacheUtil = mock[CacheUtil]
    }

    "return the total errors found over all sheets" in {
      val schemeErrors = new ListBuffer[SheetErrors]()

      val error = ValidationError(Cell("A",1,"abc"),"001", "error.1", "This entry must be 'yes' or 'no'.")
      val errors = new ListBuffer[ValidationError]
      errors ++= List(error)

      val invalidSheet = SheetErrors ("", errors)
      val validSheet = SheetErrors ("", new ListBuffer())

      schemeErrors += invalidSheet
      schemeErrors += validSheet
      schemeErrors += invalidSheet

      buildProcessODSService.getTotalErrorCount(schemeErrors) shouldBe 2
    }

  }

}
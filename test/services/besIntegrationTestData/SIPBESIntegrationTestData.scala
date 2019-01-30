/*
 * Copyright 2019 HM Revenue & Customs
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

package services.besIntegrationTestData

/**
 * Created by matt on 25/01/16.
 */
trait SIPBESIntegrationTestData {
  val sipAwardsXML = <table:table table:name="SIP_Awards_V3" table:style-name="ta1"><table:table-row table:style-name="ro6"><table:table-cell table:style-name="ce7" office:value-type="date" office:date-value="1908-10-19" calcext:value-type="date"><text:p>1908-10-19</text:p></table:table-cell><table:table-cell office:value-type="float" office:value="5" calcext:value-type="float"><text:p>5</text:p></table:table-cell><table:table-cell office:value-type="float" office:value="1" calcext:value-type="float"><text:p>1</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>2:1</text:p></table:table-cell><table:table-cell table:style-name="ce10" office:value-type="float" office:value="10.142" calcext:value-type="float"><text:p>10.1420</text:p></table:table-cell><table:table-cell table:style-name="ce11" office:value-type="float" office:value="100.01" calcext:value-type="float"><text:p>100.01</text:p></table:table-cell><table:table-cell table:style-name="ce10" office:value-type="float" office:value="10.1422" calcext:value-type="float"><text:p>10.1422</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="3" calcext:value-type="float"><text:p>3</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="4" calcext:value-type="float"><text:p>4</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="6" calcext:value-type="float"><text:p>6</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="2" calcext:value-type="float"><text:p>2</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="134" calcext:value-type="float"><text:p>134</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="32" calcext:value-type="float"><text:p>32</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce9"/><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>aa123456</text:p></table:table-cell><table:table-cell table:number-columns-repeated="995"/><table:table-cell table:style-name="ce15"/><table:table-cell table:number-columns-repeated="11"/></table:table-row></table:table>
  val sipAwardsXMLRow1 = <table:table-row table:style-name="ro6"><table:table-cell table:style-name="ce7" office:value-type="date" office:date-value="1908-10-19" calcext:value-type="date"><text:p>1908-10-19</text:p></table:table-cell><table:table-cell office:value-type="float" office:value="5" calcext:value-type="float"><text:p>5</text:p></table:table-cell><table:table-cell office:value-type="float" office:value="1" calcext:value-type="float"><text:p>1</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>2:1</text:p></table:table-cell><table:table-cell table:style-name="ce10" office:value-type="float" office:value="10.142" calcext:value-type="float"><text:p>10.1420</text:p></table:table-cell><table:table-cell table:style-name="ce11" office:value-type="float" office:value="100.01" calcext:value-type="float"><text:p>100.01</text:p></table:table-cell><table:table-cell table:style-name="ce10" office:value-type="float" office:value="10.1422" calcext:value-type="float"><text:p>10.1422</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="3" calcext:value-type="float"><text:p>3</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="4" calcext:value-type="float"><text:p>4</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="6" calcext:value-type="float"><text:p>6</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="2" calcext:value-type="float"><text:p>2</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="134" calcext:value-type="float"><text:p>134</text:p></table:table-cell><table:table-cell table:style-name="ce12" office:value-type="float" office:value="32" calcext:value-type="float"><text:p>32</text:p></table:table-cell><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce9"/><table:table-cell table:style-name="ce9" office:value-type="string" calcext:value-type="string"><text:p>aa123456</text:p></table:table-cell><table:table-cell table:number-columns-repeated="995"/><table:table-cell table:style-name="ce15"/><table:table-cell table:number-columns-repeated="11"/></table:table-row>
  val sipAwardsExpData = List("1908-10-19","5","1","yes","2:1","10.1420","100.01","10.1422","3","4","6","2","134","32","yes","","aa123456")
  val sipOutXML = <table:table table:name="SIP_Out_V3" table:style-name="ta1"><table:table-row table:style-name="ro6"><table:table-cell table:style-name="ce23" office:value-type="date" office:date-value="1980-10-19" calcext:value-type="date"><text:p>1980-10-19</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Anthony</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Michael</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Hall</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>AA123456A</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>123/XZ55555555</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.01" calcext:value-type="float"><text:p>100.01</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.02" calcext:value-type="float"><text:p>100.02</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.22" calcext:value-type="float"><text:p>100.22</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.32" calcext:value-type="float"><text:p>100.32</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="10.1422" calcext:value-type="float"><text:p>10.1422</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="10.1492" calcext:value-type="float"><text:p>10.1492</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="13.1492" calcext:value-type="float"><text:p>13.1492</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="13.1692" calcext:value-type="float"><text:p>13.1692</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce26" table:number-columns-repeated="2"/><table:table-cell table:number-columns-repeated="1007"/></table:table-row></table:table>
  val sipOutXMLRow1 = <table:table-row table:style-name="ro6"><table:table-cell table:style-name="ce23" office:value-type="date" office:date-value="1980-10-19" calcext:value-type="date"><text:p>1980-10-19</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Anthony</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Michael</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>Hall</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>AA123456A</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>123/XZ55555555</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.01" calcext:value-type="float"><text:p>100.01</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.02" calcext:value-type="float"><text:p>100.02</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.22" calcext:value-type="float"><text:p>100.22</text:p></table:table-cell><table:table-cell table:style-name="ce31" office:value-type="float" office:value="100.32" calcext:value-type="float"><text:p>100.32</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="10.1422" calcext:value-type="float"><text:p>10.1422</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="10.1492" calcext:value-type="float"><text:p>10.1492</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="13.1492" calcext:value-type="float"><text:p>13.1492</text:p></table:table-cell><table:table-cell table:style-name="ce34" office:value-type="float" office:value="13.1692" calcext:value-type="float"><text:p>13.1692</text:p></table:table-cell><table:table-cell table:style-name="ce26" office:value-type="string" calcext:value-type="string"><text:p>yes</text:p></table:table-cell><table:table-cell table:style-name="ce26" table:number-columns-repeated="2"/><table:table-cell table:number-columns-repeated="1007"/></table:table-row>
  val sipOutExpData = List("1980-10-19","Anthony","Michael","Hall","AA123456A","123/XZ55555555","100.01","100.02","100.22","100.32","10.1422","10.1492","13.1492","13.1692","yes")




}

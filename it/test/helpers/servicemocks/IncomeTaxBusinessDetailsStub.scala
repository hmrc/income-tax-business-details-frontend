/*
 * Copyright 2017 HM Revenue & Customs
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

package helpers.servicemocks

import models.createIncomeSource.{CreateIncomeSourceErrorResponse, CreateIncomeSourceResponse}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.WiremockHelper
import models.incomeSourceDetails.IncomeSourceDetailsResponse
import models.ObligationsModel
import play.api.http.Status
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.{JsValue, Json}


object  IncomeTaxBusinessDetailsStub { // scalastyle:off number.of.methods

  // Income Source Details Stubs
  // ===========================
  val incomeSourceDetailsUrl: String => String = mtditid => s"/income-tax-business-details/income-sources/$mtditid"

  def stubGetIncomeSourceDetailsResponse(mtditid: String)(status: Int, response: IncomeSourceDetailsResponse): StubMapping =
    WiremockHelper.stubGet(incomeSourceDetailsUrl(mtditid), status, response.toJson.toString)

  def verifyGetIncomeSourceDetails(mtditid: String, noOfCalls: Int = 1): Unit = {
    WiremockHelper.verifyGet(incomeSourceDetailsUrl(mtditid), noOfCalls)
  }

  // Stub CreateBusinessDetails
  def stubCreateBusinessDetailsResponse()(status: Int, response: List[CreateIncomeSourceResponse]): Unit =
    WiremockHelper.stubPost(s"/income-tax-business-details/create-income-source/business", status, Json.toJson(response).toString)

  def stubCreateBusinessDetailsErrorResponseNew()(response: List[CreateIncomeSourceErrorResponse]): Unit =
    WiremockHelper.stubPost(s"/income-tax-business-details/create-income-source/business", INTERNAL_SERVER_ERROR, Json.toJson(response).toString)

  //NextUpdates Stubs
  //=====================
  def nextUpdatesUrl(nino: String): String = s"/income-tax-obligations/$nino/open-obligations"

  def stubGetNextUpdates(nino: String, deadlines: ObligationsModel): Unit =
    WiremockHelper.stubGet(nextUpdatesUrl(nino), Status.OK, Json.toJson(deadlines).toString())

  def stubUpdateIncomeSource(status: Int, response: JsValue): StubMapping =
    WiremockHelper.stubPut("/income-tax-business-details/update-income-source", status, response.toString())

  def stubUpdateIncomeSourceError(): StubMapping = {
    stubUpdateIncomeSource(INTERNAL_SERVER_ERROR, Json.obj("failures" -> Json.arr(
      Json.obj("code" -> "500", "reason" -> "ETMP is broken :(")
    )))
  }

  def verifyUpdateIncomeSource(body: Option[String]): Unit = {
    WiremockHelper.verifyPut("/income-tax-business-details/update-income-source", body)
  }

  // Triggered Migration - Update Customer Facts
  def stubUpdateCustomerFacts(mtdId: String)(status: Int): StubMapping =
    WiremockHelper.stubPut(
      s"/income-tax-business-details/customer-facts/update/$mtdId",
      status,
      ""
    )
}

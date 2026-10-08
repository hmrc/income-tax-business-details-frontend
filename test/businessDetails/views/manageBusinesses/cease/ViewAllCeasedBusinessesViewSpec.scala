/*
 * Copyright 2026 HM Revenue & Customs
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

package businessDetails.views.manageBusinesses.cease

import businessDetails.controllers.manageBusinesses.routes as manageBusinessRoutes
import businessDetails.enums.IncomeSourceJourney.{ForeignProperty, SelfEmployment, UkProperty}
import businessDetails.models.incomeSourceDetails.viewmodels.{CeaseIncomeSourcesViewModel, CeasedBusinessDetailsViewModel}
import businessDetails.views.html.manageBusinesses.cease.ViewAllCeasedBusinessesView
import common.testConstants.BaseTestConstants.testStartDate
import common.testUtils.TestSupport
import org.jsoup.Jsoup
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}

class ViewAllCeasedBusinessesViewSpec extends TestSupport {
  val viewAllCeasedBusinesses: ViewAllCeasedBusinessesView = app.injector.instanceOf[ViewAllCeasedBusinessesView]
  val viewModel: CeaseIncomeSourcesViewModel =
    CeaseIncomeSourcesViewModel(
      soleTraderBusinesses = List.empty,
      ukProperty = None,
      foreignProperty = None,
      ceasedBusinesses = List(
        CeasedBusinessDetailsViewModel(
          tradingName = Some("Test Business 1"),
          incomeSourceType = SelfEmployment,
          tradingStartDate = Some(testStartDate),
          cessationDate = testStartDate.plusDays(30)
        ),
        CeasedBusinessDetailsViewModel(
          tradingName = Some("Test Business 2"),
          incomeSourceType = UkProperty,
          tradingStartDate = Some(testStartDate),
          cessationDate = testStartDate.plusDays(30)
        ),
        CeasedBusinessDetailsViewModel(
          tradingName = Some("Test Business 3"),
          incomeSourceType = ForeignProperty,
          tradingStartDate = Some(testStartDate),
          cessationDate = testStartDate.plusDays(30)
        )
      ),
      displayStartDate = false
  )

  class Setup(isAgent: Boolean) {
    val backUrl = if (isAgent) {
      manageBusinessRoutes.ManageYourBusinessesController.showAgent().url
    } else {
      manageBusinessRoutes.ManageYourBusinessesController.show().url
    }
    val view = viewAllCeasedBusinesses(viewModel, isAgent, backUrl)
    lazy val document = Jsoup.parse(contentAsString(view))
  }

  "The ViewAllCeasedBusinesses page" should {
    "display the correct heading" in new Setup(isAgent = false) {
      document.getElementById("heading").text() shouldBe messages("Businesses that have ceased")
    }

    "display the correct number of ceased businesses" in new Setup(isAgent = false) {
      document.select("tbody.govuk-table__body > tr.govuk-table__row").size() shouldBe viewModel.ceasedBusinesses.size
    }

    "display the correct ceased business details" in new Setup(isAgent = false) {
      document.getElementById("ceased-business-table-row-trading-name-0").text() shouldBe "Test Business 1"
      document.getElementById("ceased-business-table-row-date-ended-0").text() shouldBe "31 January 2022"
      document.getElementById("ceased-business-table-row-trading-name-1").text() shouldBe "UK property"
      document.getElementById("ceased-business-table-row-date-ended-1").text() shouldBe "31 January 2022"
      document.getElementById("ceased-business-table-row-trading-name-2").text() shouldBe "Foreign property"
      document.getElementById("ceased-business-table-row-date-ended-2").text() shouldBe "31 January 2022"
    }

    "display the correct back link" in new Setup(isAgent = false) {
      println(document)
      document.getElementById("back-fallback").attr("href") shouldBe backUrl
    }

    "display the correct back link for agents" in new Setup(isAgent = true) {
      println(document)
      document.getElementById("back-fallback").attr("href") shouldBe backUrl
    }
  }
}

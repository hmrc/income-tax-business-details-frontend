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

package common.models.incomeSourceDetails

import common.models.core.{AccountingPeriodModel, AddressModel, CessationModel}
import common.testUtils.{TestSupport, UnitSpec}

import java.time.LocalDate

class BusinessDetailsModelSpec extends UnitSpec with TestSupport {

  private val activeBusiness = BusinessDetailsModel(
    incomeSourceId = "ABC123456789",
    incomeSource = Some("Fruit Ltd"),
    accountingPeriod = Some(AccountingPeriodModel(
      start = dateService.getCurrentTaxYearStart,
      end = dateService.getAccountingPeriodEndDate(dateService.getCurrentTaxYearStart)
    )),
    tradingName = Some("business"),
    firstAccountingPeriodEndDate = Some(dateService.getAccountingPeriodEndDate(dateService.getCurrentTaxYearStart)),
    tradingStartDate = Some(dateService.getCurrentTaxYearStart),
    contextualTaxYear = None,
    cessation = None,
    address = Some(AddressModel(
      addressLine1 = Some("64 Zoo Lane"),
      addressLine2 = Some("Happy Place"),
      addressLine3 = Some("Magical Land"),
      addressLine4 = Some("England"),
      postCode = Some("ZL1 064"),
      countryCode = Some("GB")
    ))
  )

  "isActive method" when {
    "invoked on a BusinessDetailsModel object" should {
      "return true where there are no ceased or deleted businesses" in {

        activeBusiness.isActive shouldBe true
      }

      "return true where there is no deleted businesses but deleted field set to 'Some(false)'" in {

        activeBusiness.copy(deleted = Some(false)).isActive shouldBe true
      }

      "return false where there is a ceased businesses" in {

        activeBusiness.copy(cessation = Some(CessationModel(Some(dateService.getCurrentDate)))).isActive shouldBe false
      }

      "return false where there is a deleted businesses" in {

        activeBusiness.copy(deleted = Some(true)).isActive shouldBe false
      }

      "return false where there are ceased and deleted businesses" in {

        activeBusiness.copy(cessation = Some(CessationModel(Some(dateService.getCurrentDate))), deleted = Some(true)).isActive shouldBe false
      }
    }
  }
}

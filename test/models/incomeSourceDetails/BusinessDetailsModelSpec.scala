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

package models.incomeSourceDetails

import models.core.{AccountingPeriodModel, AddressModel, CessationModel}
import testUtils.{TestSupport, UnitSpec}

import java.time.LocalDate

class BusinessDetailsModelSpec extends UnitSpec with TestSupport {

  private val currentTaxYearStartDate: LocalDate = dateService.getCurrentTaxYearStart
  private val incomeSourceId: String = "ABC123456789"

  private val activeBusiness = BusinessDetailsModel(
    incomeSourceId = incomeSourceId,
    incomeSource = Some("Fruit Ltd"),
    accountingPeriod = Some(AccountingPeriodModel(
      start = currentTaxYearStartDate,
      end = dateService.getAccountingPeriodEndDate(currentTaxYearStartDate)
    )),
    tradingName = Some("business"),
    firstAccountingPeriodEndDate = Some(dateService.getAccountingPeriodEndDate(currentTaxYearStartDate)),
    tradingStartDate = Some(currentTaxYearStartDate),
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

  "isOngoingSoleTraderBusiness(id: String) method" when {
    "invoked on a BusinessDetailsModel object" should {
      "return true where there are no ceased or deleted businesses and 'incomeSourceId' is equal to 'id' parameter passed" in {

        activeBusiness.isOngoingSoleTraderBusiness(incomeSourceId) shouldBe true
      }

      "return false where the 'id' parameter passed is not matching the 'incomeSourceId' filed" in {

        activeBusiness.isOngoingSoleTraderBusiness("SomeDifferentIncomeSourceId") shouldBe false
      }

      "return false where the 'id' parameter passed as an empty String" in {

        activeBusiness.isOngoingSoleTraderBusiness("") shouldBe false
      }

      "return false where the 'id' parameter passed is a valid String but the 'incomeSourceId' filed is empty String" in {

        activeBusiness.copy(incomeSourceId = "").isOngoingSoleTraderBusiness(incomeSourceId) shouldBe false
      }

      "return false where there is a ceased businesses and incomeSourceId is equal to id parameter passed" in {

        activeBusiness.copy(cessation = Some(CessationModel(Some(dateService.getCurrentDate)))).isOngoingSoleTraderBusiness(incomeSourceId) shouldBe false
      }

      "return false where there is a deleted businesses and incomeSourceId is equal to id parameter passed" in {

        activeBusiness.copy(deleted = Some(true)).isOngoingSoleTraderBusiness(incomeSourceId) shouldBe false
      }

      "return false where there are ceased and deleted businesses and incomeSourceId is equal to id parameter passed" in {

        activeBusiness.copy(cessation = Some(CessationModel(Some(dateService.getCurrentDate))), deleted = Some(true)).isActive shouldBe false
      }
    }
  }
}

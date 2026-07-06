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

package testConstants

import enums.IncomeSourceJourney.SelfEmployment
import enums.JourneyType.IncomeSourceJourneyType
import models.UIJourneySessionData
import models.incomeSourceDetails.*
import testConstants.BaseIntegrationTestConstants.*
import testConstants.BusinessDetailsIntegrationTestConstants.*
import testConstants.PropertyDetailsIntegrationTestConstants.*

import java.time.LocalDate

object IncomeSourceIntegrationTestConstants {

  val singleBusinessResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  val singleBusinessResponse2: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1WithAddress2),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  def singleBusinessResponseInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1.copy(latencyDetails = Some(latencyDetails))),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  def singleBusinessResponseInLatencyPeriod2(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1WithAddress2.copy(latencyDetails = Some(latencyDetails))),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  def singleBusinessResponseWithUnknownsInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business3WithUnknowns.copy(latencyDetails = Some(latencyDetails))),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  def singleUKPropertyResponseInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtditid,
    businesses = List(business1.copy(latencyDetails = Some(latencyDetails))),
    properties = List(ukProperty.copy(latencyDetails = Some(latencyDetails))),
    yearOfMigration = Some("2018")
  )

  def singleUKForeignPropertyResponseInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtditid,
    businesses = List(businessWithId.copy(latencyDetails = Some(latencyDetails))),
    properties = List(ukProperty.copy(latencyDetails = Some(latencyDetails)), foreignProperty.copy(latencyDetails = Some(latencyDetails))),
    yearOfMigration = Some("2018")
  )

  def singleUKPropertyResponseWithUnknownsInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtditid,
    businesses = List(business1.copy(latencyDetails = Some(latencyDetails))),
    properties = List(ukPropertyWithUnknowns.copy(latencyDetails = Some(latencyDetails))),
    yearOfMigration = Some("2018")
  )

  def singleForeignPropertyResponseInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtditid,
    businesses = List(business1.copy(latencyDetails = Some(latencyDetails))),
    properties = List(foreignProperty.copy(latencyDetails = Some(latencyDetails))),
    yearOfMigration = Some("2018")
  )

  def singleForeignPropertyResponseWithUnknownsInLatencyPeriod(latencyDetails: LatencyDetails): IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtditid,
    businesses = List(business1.copy(latencyDetails = Some(latencyDetails))),
    properties = List(foreignPropertyWithUnknowns.copy(latencyDetails = Some(latencyDetails))),
    yearOfMigration = Some("2018")
  )

  val multipleBusinessesResponse: IncomeSourceDetailsResponse = IncomeSourceDetailsModel(
    nino = testNino,
    mtdbsa = testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = Nil,
    yearOfMigration = Some("2019")
  )

  val businessAndPropertyResponse: IncomeSourceDetailsModel =
    IncomeSourceDetailsModel(
      testNino,
      testMtdItId,
      businesses = List(business1),
      properties = List(property),
      yearOfMigration = Some("2018")
    )

  val multipleBusinessesAndPropertyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = List(property),
    yearOfMigration = Some("2018")
  )

  val multipleBusinessesAndUkProperty: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = List(ukProperty),
    yearOfMigration = Some("2018")
  )

  val multipleBusinessesWithBothPropertiesAndCeasedBusiness: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1,
      business2,
      business3
    ),
    properties = List(ukProperty, foreignProperty),
    yearOfMigration = Some("2018")
  )

  val propertyOnlyBusiness: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ukProperty, foreignProperty),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyAndCeasedBusiness: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      ceasedBusiness1
    ),
    properties = List(foreignProperty),
    yearOfMigration = Some("2018")
  )

  val allCeasedBusinesses: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      ceasedBusiness1
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val businessWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      businessWithLatencyForManageYourDetailsAudit
    ),
    properties = List(foreignProperty),
    yearOfMigration = Some("2018")
  )

  val businessOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val businessOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1.copy(latencyDetails = Some(testLatencyDetails3))
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val businessOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      ceasedBusiness1
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val ukPropertyOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ukProperty),
    yearOfMigration = Some("2018")
  )

  val ukPropertyOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ukProperty.copy(latencyDetails = Some(testLatencyDetails3))),
    yearOfMigration = Some("2018")
  )

  val ukPropertyOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ceasedUkProperty),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(foreignProperty),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(foreignProperty.copy(latencyDetails = Some(testLatencyDetails3))),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ceasedForeignProperty),
    yearOfMigration = Some("2018")
  )

  val noPropertyOrBusinessResponse: IncomeSourceDetailsResponse = IncomeSourceDetailsModel(
    testNino,
    testMtdItId, None,
    List(), Nil
  )

  val businessOnlyResponseWithUnknownAddressName: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      businessUnknownAddressName
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  lazy val completedUIJourneySessionData: IncomeSourceJourneyType => UIJourneySessionData = (incomeSources: IncomeSourceJourneyType) => {
    incomeSources.operation.operationType match {
      case "ADD" => UIJourneySessionData(testSessionId, incomeSources.toString,
        addIncomeSourceData = Some(AddIncomeSourceData(incomeSourceCreatedJourneyComplete = Some(true))))
      case "MANAGE" => if (incomeSources.businessType == SelfEmployment) UIJourneySessionData(testSessionId, incomeSources.toString,
        manageIncomeSourceData = Some(ManageIncomeSourceData(incomeSourceId = Some(testSelfEmploymentId),
          taxYear = Some(2024), reportingMethod = Some("annual"), journeyIsComplete = Some(true))))
      else UIJourneySessionData(testSessionId, incomeSources.toString,
        manageIncomeSourceData = Some(ManageIncomeSourceData(incomeSourceId = Some(testPropertyIncomeId),
          taxYear = Some(2024), reportingMethod = Some("annual"), journeyIsComplete = Some(true))))
      case "CEASE" => UIJourneySessionData(testSessionId, incomeSources.toString,
        ceaseIncomeSourceData = Some(CeaseIncomeSourceData(journeyIsComplete = Some(true))))
    }
  }

  val emptyUIJourneySessionData: IncomeSourceJourneyType => UIJourneySessionData = incomeSources => {
    incomeSources.operation.operationType match {
      case "ADD" =>
        UIJourneySessionData(
          sessionId = testSessionId,
          journeyType = incomeSources.toString,
          addIncomeSourceData = Some(AddIncomeSourceData())
        )
      case "MANAGE" =>
        UIJourneySessionData(
          sessionId = testSessionId,
          journeyType = incomeSources.toString,
          manageIncomeSourceData = Some(ManageIncomeSourceData())
        )
      case "CEASE" =>
        UIJourneySessionData(
          sessionId = testSessionId,
          journeyType = incomeSources.toString,
          ceaseIncomeSourceData = Some(CeaseIncomeSourceData())
        )
    }
  }
}



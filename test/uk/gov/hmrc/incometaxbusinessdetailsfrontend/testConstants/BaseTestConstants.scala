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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.testConstants

import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.{AffinityGroup, ConfidenceLevel, Enrolment, EnrolmentIdentifier, Enrolments}
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.testUtils.UnitSpec

object BaseTestConstants extends UnitSpec {

  val testNinoAgent: String = "AA111111A"
  val testNino: String = "AB123456C"
  val testNinoNino: String = testNino
  val testUserNino: String = testNino

  val testMtditid: String = "1234567890"
  val testUtr: String = "1234567890"
  val testSessionId: String = "session-123456"
  val testReferrerUrl: String = "/test-referrer"

  val testErrorStatus: Int = 500
  val testErrorMessage: String = "INTERNAL_SERVER_ERROR"
  val testSelfEmploymentId: String = "XA00001234"
  val testSelfEmploymentId2: String = "XA00001235"
  val testSelfEmploymentIdValidation: String = "XAIS00000000002"

  val testUserTypeIndividual: AffinityGroup = AffinityGroup.Individual
  val testUserTypeAgent: AffinityGroup = AffinityGroup.Agent
  val testUserType: AffinityGroup = testUserTypeIndividual

  val testConfidenceLevel: ConfidenceLevel = ConfidenceLevel.L250

  val ninoEnrolment: Enrolment =
    Enrolment("HMRC-NI", Seq(EnrolmentIdentifier("NINO", testNino)), "activated")

  val mtdEnrolment: Enrolment =
    Enrolment("HMRC-MTD-IT", Seq(EnrolmentIdentifier("MTDITID", testMtditid)), "activated")

  val testEnrolments: Enrolments =
    Enrolments(Set(ninoEnrolment, mtdEnrolment))

  val fakeRequest = FakeRequest("GET", "/")
}

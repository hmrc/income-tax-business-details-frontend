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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.testUtils

import config.AppConfig
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.http.{HeaderCarrier, SessionId}

trait TestSupport extends UnitSpec with GuiceOneAppPerSuite with BeforeAndAfterEach {

  implicit lazy val appConfig: AppConfig =
    app.injector.instanceOf[AppConfig]

  implicit lazy val messages: Messages =
    app.injector.instanceOf[MessagesApi].preferred(Seq(Lang("en")))

  implicit val hc: HeaderCarrier =
    HeaderCarrier(sessionId = Some(SessionId("session-123456")))

  val testNino: String = "AB123456C"
  val testMtditid: String = "1234567890"
  val testUtr: String = "1234567890"
  val testSessionId: String = "session-123456"
  val testReferrerUrl: String = "/test-referrer"

  val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", "/")

  val fakePostRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("POST", "/")

  val fakeRequestWithActiveSession: FakeRequest[AnyContentAsEmpty.type] =
    fakeRequest.withSession("authToken" -> "Bearer 123")

  val fakePostRequestWithActiveSession: FakeRequest[AnyContentAsEmpty.type] =
    fakePostRequest.withSession("authToken" -> "Bearer 123")

  def fakeRequestConfirmedClient(isSupportingAgent: Boolean = false): FakeRequest[AnyContentAsEmpty.type] =
    fakeRequestWithActiveSession.withSession(
      "clientFirstName" -> "Test",
      "clientLastName" -> "User",
      "clientUTR" -> testUtr,
      "clientMTDID" -> testMtditid,
      "clientNino" -> testNino,
      "confirmedClient" -> "true",
      "isSupportingAgent" -> isSupportingAgent.toString
    )

  def fakePostRequestConfirmedClient(isSupportingAgent: Boolean = false): FakeRequest[AnyContentAsEmpty.type] =
    fakePostRequestWithActiveSession.withSession(
      "clientFirstName" -> "Test",
      "clientLastName" -> "User",
      "clientUTR" -> testUtr,
      "clientMTDID" -> testMtditid,
      "clientNino" -> testNino,
      "confirmedClient" -> "true",
      "isSupportingAgent" -> isSupportingAgent.toString
    )
}

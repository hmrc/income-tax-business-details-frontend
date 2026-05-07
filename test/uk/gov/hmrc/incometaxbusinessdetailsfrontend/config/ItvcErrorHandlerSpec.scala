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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.config

import org.scalatest.concurrent.ScalaFutures
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.testUtils.UnitSpec

import scala.concurrent.Future

class ItvcErrorHandlerSpec extends UnitSpec with ScalaFutures {

  private lazy val app: Application = GuiceApplicationBuilder()
    .configure(
      "auth.host" -> "localhost",
      "auth.port" -> 8500,
      "auth.protocol" -> "http",
      "microservice.services.auth.host" -> "localhost",
      "microservice.services.auth.port" -> 8500,
      "microservice.services.auth.protocol" -> "http"
    ).build()

  private val request = FakeRequest("GET", "/")

  "ItvcErrorHandler" should {
    "render standard error template as html" in {
      val handler = app.injector.instanceOf[ItvcErrorHandler]
      val html = handler.standardErrorTemplate("title", "heading", "message")(request).futureValue

      html.contentType shouldBe "text/html"
      html.body should include("heading")
      html.body should include("message")
    }

    "return internal server error result" in {
      val handler = app.injector.instanceOf[ItvcErrorHandler]
      status(Future.successful(handler.showInternalServerError()(request))) shouldBe INTERNAL_SERVER_ERROR
    }

    "return bad request result" in {
      val handler = app.injector.instanceOf[ItvcErrorHandler]
      status(Future.successful(handler.showBadRequestError()(request))) shouldBe BAD_REQUEST
    }
  }
}

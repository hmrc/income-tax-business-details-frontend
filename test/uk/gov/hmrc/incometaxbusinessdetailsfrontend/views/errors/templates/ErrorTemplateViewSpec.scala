/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.views.errors.templates

import play.api.test.FakeRequest
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.testUtils.ViewSpec
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.views.html.errors.templates.ErrorTemplate

class ErrorTemplateViewSpec extends ViewSpec {
  val view: HtmlFormat.Appendable = app.injector.instanceOf[ErrorTemplate].apply(
    pageTitle = "agent-error.heading",
    heading = "agent-error.heading",
    message = "agent-error.note",
    isAgent = false
  )(FakeRequest(), messages)

  "The error page view" should {
    "display a h1" in new Setup(view) {
      document hasPageHeading messages("agent-error.heading")
      document.select("h1").text() should include(messages("agent-error.heading"))
    }
    "display the HTML title" in new Setup(view) {
      document.title() shouldBe messages("htmlTitle.errorPage", messages("agent-error.heading"))
    }
    "display the text" in new Setup(view) {
      layoutContent.selectNth("p", 1).text() shouldBe messages("agent-error.note")
    }
  }
}

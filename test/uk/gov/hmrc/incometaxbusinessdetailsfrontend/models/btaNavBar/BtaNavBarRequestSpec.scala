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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.btaNavBar

import play.api.libs.json.Json
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.testUtils.UnitSpec

class BtaNavBarRequestSpec extends UnitSpec {

  private val navLink = NavLinks(
    en = "Home",
    cy = "Hafan",
    url = "/home",
    alerts = Some(2)
  )

  "NavLinks" should {
    "round-trip through JSON format" in {
      Json.fromJson[NavLinks](Json.toJson(navLink)).get shouldBe navLink
    }
  }

  "NavContent" should {
    "round-trip through JSON format" in {
      val model = NavContent(
        home = navLink,
        account = navLink.copy(en = "Account", cy = "Cyfrif", url = "/account"),
        messages = navLink.copy(en = "Messages", cy = "Negeseuon", url = "/messages"),
        help = navLink.copy(en = "Help", cy = "Cymorth", url = "/help"),
        forms = navLink.copy(en = "Forms", cy = "Ffurflenni", url = "/forms")
      )

      Json.fromJson[NavContent](Json.toJson(model)).get shouldBe model
    }
  }

  "ListLinks" should {
    "apply default values for alerts and showBoolean" in {
      val link = ListLinks(message = "Payments", url = "/payments")
      link.alerts shouldBe None
      link.showBoolean shouldBe Some(true)
    }
  }
}


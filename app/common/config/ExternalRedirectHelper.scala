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

package common.config

import play.api.Configuration
import play.api.i18n.Messages
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait ExternalRedirectHelper {

  val servicesConfig: ServicesConfig
  val config: Configuration

  // hub routes
  lazy val hubBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.hubBaseUrl")
  lazy val hubAgentBaseUrl: String = s"$hubBaseUrl/agents"

  def individualHomeUrl: String = s"$hubBaseUrl/income-tax"

  def individualHomeUrlWithOrigin(origin: Option[String]): String =
    origin.fold(individualHomeUrl)(o => s"$individualHomeUrl?origin=$o")

  def agentHomeUrl: String = s"$hubAgentBaseUrl/client-income-tax"

  def homePageUrl(isAgent: Boolean, origin: Option[String] = None): String =
    if (isAgent) agentHomeUrl else individualHomeUrlWithOrigin(origin)

  def enterClientsUTRUrl: String = s"$hubAgentBaseUrl/client-utr"

  def confirmClientUTRUrl: String = s"$hubAgentBaseUrl/confirm-client-details"

  //Obligation routes

  lazy val obligationsBaseUrl: String = servicesConfig.getString("income-tax-obligations-frontend.baseUrl")
  lazy val obligationsAgentBaseUrl: String = s"$obligationsBaseUrl/agents"

  lazy val obligationsWaitToSignUpIndividualUrl: String = s"$obligationsBaseUrl/access-service-from-next-tax-year"

  lazy val obligationsWaitToSignUpAgentUrl: String = s"$obligationsAgentBaseUrl/view-client-from-next-tax-year"

  lazy val obligationsNextUpdatesIndividualUrl: String = s"$obligationsBaseUrl/submission-deadlines"

  lazy val obligationsNextUpdatesAgentUrl: String = s"$obligationsAgentBaseUrl/submission-deadlines"

  def obligationsNextUpdatesUrl(isAgent: Boolean): String =
    if (isAgent) obligationsNextUpdatesAgentUrl else obligationsNextUpdatesIndividualUrl

  lazy val obligationsReportingFrequencyIndividualUrl: String = s"$obligationsBaseUrl/reporting-frequency"

  lazy val obligationsReportingFrequencyAgentUrl: String = s"$obligationsAgentBaseUrl/reporting-frequency"

  def obligationsReportingFrequencyUrl(isAgent: Boolean): String =
    if (isAgent) obligationsReportingFrequencyAgentUrl else obligationsReportingFrequencyIndividualUrl

  //triggered migration routes
  import businessDetails.controllers.triggeredMigration.routes as triggeredMigrationRoutes

  def triggeredMigrationCompleteStepsUrl(isAgent: Boolean): String = {
    triggeredMigrationRoutes.CompleteStepsController.show(isAgent).url
  }

  //external links

  def findOutHowLink(implicit messages: Messages): String =
    messages.lang.code match {
      case "cy" => "https://www.gov.uk/government/collections/troi-treth-yn-ddigidol-ar-gyfer-treth-incwm.cy"
      case _ => "https://www.gov.uk/government/collections/making-tax-digital-for-income-tax"
    }
  
  

}

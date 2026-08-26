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
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait ExternalRedirectHelper {

  val servicesConfig: ServicesConfig
  val config: Configuration
  lazy val hubContextRootEnabledConfig: Boolean = servicesConfig.getBoolean("feature-switch.enable-new-hub-context-root")

  // hub routes
  lazy val vcFrontendBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.baseUrl")
  lazy val vcFrontendAgentBaseUrl: String = s"${vcFrontendBaseUrl}/agents"

  def hubBaseUrl(newHubContextRootEnabled: Boolean): String =
    if (newHubContextRootEnabled) servicesConfig.getString("income-tax-view-change-frontend.hubBaseUrl") else vcFrontendBaseUrl

  def hubAgentBaseUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubBaseUrl(newHubContextRootEnabled)}/agents"

  def individualHomeUrl(newHubContextRootEnabled: Boolean = hubContextRootEnabledConfig): String =
    s"${hubBaseUrl(newHubContextRootEnabled)}/income-tax"

  def individualHomeUrlWithOrigin(newHubContextRootEnabled: Boolean, origin: Option[String]): String =
    origin.fold(individualHomeUrl(newHubContextRootEnabled))(o => s"${individualHomeUrl(newHubContextRootEnabled)}?origin=$o")

  def agentHomeUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/client-income-tax"

  def homePageUrl(isAgent: Boolean, newHubContextRootEnabled: Boolean, origin: Option[String] = None): String =
    if (isAgent) agentHomeUrl(newHubContextRootEnabled) else individualHomeUrlWithOrigin(newHubContextRootEnabled, origin)


  def enterClientsUTRUrl(newHubContextRootEnabled: Boolean = hubContextRootEnabledConfig): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/client-utr"

  def confirmClientUTRUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/confirm-client-details"
  
  //Obligation routes

  lazy val obligationsBaseUrl: String = servicesConfig.getString("income-tax-obligations-frontend.baseUrl")
  lazy val obligationsAgentBaseUrl: String = s"$obligationsBaseUrl/agents"

  lazy val obligationsWaitToSignUpIndividualUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsBaseUrl/access-service-from-next-tax-year"
    else
      s"$vcFrontendBaseUrl/access-service-from-next-tax-year"

  lazy val obligationsWaitToSignUpAgentUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsAgentBaseUrl/view-client-from-next-tax-year"
    else
      s"$vcFrontendAgentBaseUrl/view-client-from-next-tax-year"

  lazy val obligationsNextUpdatesIndividualUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsBaseUrl/submission-deadlines"
    else
      s"$vcFrontendBaseUrl/submission-deadlines"

  lazy val obligationsNextUpdatesAgentUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsAgentBaseUrl/submission-deadlines"
    else
      s"$vcFrontendAgentBaseUrl/submission-deadlines"

  def obligationsNextUpdatesUrl(isAgent: Boolean, newObligationsEnabled: Boolean): String = {
    if (isAgent)
      obligationsNextUpdatesAgentUrl(newObligationsEnabled)
    else
      obligationsNextUpdatesIndividualUrl(newObligationsEnabled)
  }

  lazy val obligationsReportingFrequencyIndividualUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsBaseUrl/reporting-frequency"
    else
      s"$vcFrontendBaseUrl/reporting-frequency"

  lazy val obligationsReportingFrequencyAgentUrl: Boolean => String = newObligationsEnabled =>
    if (newObligationsEnabled)
      s"$obligationsAgentBaseUrl/reporting-frequency"
    else
      s"$vcFrontendAgentBaseUrl/reporting-frequency"


  def obligationsReportingFrequencyUrl(isAgent: Boolean, newObligationsEnabled: Boolean): String = {
    if (isAgent)
      obligationsReportingFrequencyAgentUrl(newObligationsEnabled)
    else
      obligationsReportingFrequencyIndividualUrl(newObligationsEnabled)
  }

}

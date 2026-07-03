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

package config

import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

//import hub.controllers.routes as hubRoutes
//import hub.controllers.agent.routes as hubAgentRoutes

trait ExternalRedirectHelper {

  val servicesConfig: ServicesConfig
  val config: Configuration
  
  // hub routes
  lazy val hubBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.baseUrl")
  lazy val hubAgentBaseUrl: String = s"${hubBaseUrl}/agents"
  
  lazy val individualHomeUrl: String =
    //hubRoutes.HomeController.show().url
    hubBaseUrl
    
  lazy val agentHomeUrl: String =
    //hubRoutes.HomeController.showAgent().url
    hubAgentBaseUrl
    
  def homePageUrl(isAgent: Boolean): String = if (isAgent) agentHomeUrl else individualHomeUrl

  lazy val enterClientsUTRUrl: String =
    //hubAgentRoutes.EnterClientsUTRController.show().url
    s"$hubAgentBaseUrl/enter-client-utr"
  lazy val confirmClientUTRUrl: String =
    //hubAgentRoutes.ConfirmClientUTRController.show().url
    s"$hubAgentBaseUrl/confirm-client-details"
  
  //Obligation routes
  
  lazy val obligationsBaseUrl: Boolean => String = newObligationsEnabled =>
    if(newObligationsEnabled)
      servicesConfig.getString("income-tax-obligations-frontend.baseUrl")
    else
      hubBaseUrl
    
  lazy val obligationsAgentBaseUrl: Boolean => String = newObligationsEnabled =>
    s"${obligationsBaseUrl(newObligationsEnabled)}/agents"
  
  
  lazy val obligationsWaitToSignUpIndividualUrl: Boolean => String = newObligationsEnabled =>
    s"${obligationsBaseUrl(newObligationsEnabled)}/access-service-from-next-tax-year"

  lazy val obligationsWaitToSignUpAgentUrl: Boolean => String = newObligationsEnabled => 
    s"${obligationsAgentBaseUrl(newObligationsEnabled)}/view-client-from-next-tax-year"

  lazy val obligationsNextUpdatesIndividualUrl: Boolean => String = newObligationsEnabled =>
    s"${obligationsBaseUrl(newObligationsEnabled)}/submission-deadlines"

  lazy val obligationsNextUpdatesAgentUrl: Boolean => String = newObligationsEnabled =>
    s"${obligationsAgentBaseUrl(newObligationsEnabled)}/submission-deadlines"

  def obligationsNextUpdatesUrl(isAgent: Boolean, newObligationsEnabled: Boolean): String = {
    if (isAgent) obligationsNextUpdatesAgentUrl(newObligationsEnabled)
    else obligationsNextUpdatesIndividualUrl(newObligationsEnabled)
  }
  
  def obligationsReportingFrequencyUrl(isAgent: Boolean, newObligationsEnabled: Boolean): String = {
    if (isAgent) s"${obligationsAgentBaseUrl(newObligationsEnabled)}/reporting-frequency"
    else s"${obligationsBaseUrl(newObligationsEnabled)}/reporting-frequency"
  }

}

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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.businessDetails.controllers.manageBusinesses.add

import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.MtdItUser
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.authV2.AuthActions
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.config.{AgentItvcErrorHandler, FrontendAppConfig, ItvcErrorHandler}
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.enums.IncomeSourceJourney.IncomeSourceType
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.services.CreateBusinessDetailsService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.utils.IncomeSourcesUtils
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.views.html.manageBusinesses.add.IncomeSourceNotAddedErrorView
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.businessDetails.controllers.triggeredMigration.routes as triggeredMigrationRoutes
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.businessDetails.controllers.manageBusinesses.routes as manageBusinessesRoutes


import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class IncomeSourceNotAddedController @Inject()(val authActions: AuthActions,
                                               val businessDetailsService: CreateBusinessDetailsService,
                                               val incomeSourceNotAddedError: IncomeSourceNotAddedErrorView,
                                               val itvcErrorHandler: ItvcErrorHandler,
                                               val itvcErrorHandlerAgent: AgentItvcErrorHandler)
                                              (implicit val appConfig: FrontendAppConfig,
                                               mcc: MessagesControllerComponents,
                                               val ec: ExecutionContext) extends FrontendController(mcc)
                                               with IncomeSourcesUtils with I18nSupport{


  private def handleRequest(isAgent: Boolean, incomeSourceType: IncomeSourceType, isTriggeredMigration: Boolean)
                   (implicit user: MtdItUser[_]): Future[Result] = {
    val incomeSourceRedirect: Call = {
      (isAgent, isTriggeredMigration) match {
        case (false, false)  => manageBusinessesRoutes.ManageYourBusinessesController.show()
        case (true, false)   => manageBusinessesRoutes.ManageYourBusinessesController.showAgent()
        case (isAgent, true) => triggeredMigrationRoutes.CheckHmrcRecordsController.show(isAgent)
      }
    }

    Future.successful(Ok(incomeSourceNotAddedError(
      isAgent,
      incomeSourceType = incomeSourceType,
      continueAction = incomeSourceRedirect
    )))
  }

  def show(incomeSourceType: IncomeSourceType, isTriggeredMigration: Boolean = false): Action[AnyContent] = authActions.asMTDIndividual().async {
    implicit user =>
      handleRequest(
        isAgent = false,
        incomeSourceType = incomeSourceType,
        isTriggeredMigration
      )
  }

  def showAgent(incomeSourceType: IncomeSourceType, isTriggeredMigration: Boolean = false): Action[AnyContent] = authActions.asMTDAgentWithConfirmedClient().async  {
    implicit mtdItUser =>
      handleRequest(
        isAgent = true,
        incomeSourceType = incomeSourceType,
        isTriggeredMigration
      )
  }
}


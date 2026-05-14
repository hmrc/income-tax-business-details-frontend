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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.businessDetails.controllers.manageBusinesses

import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.MtdItUser
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.authV2.AuthActions
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.config.featureswitch.FeatureSwitching
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.config.{AgentItvcErrorHandler, FrontendAppConfig, ItvcErrorHandler, ShowInternalServerError}
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.admin.DisplayBusinessStartDate
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.incomeSourceDetails.IncomeSourceDetailsModel
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.services.{IncomeSourceDetailsService, SessionService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.utils.IncomeSourcesUtils
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.views.html.manageBusinesses.ManageYourBusinessesView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ManageYourBusinessesController @Inject()(val manageYourBusinesses: ManageYourBusinessesView,
                                               val authActions: AuthActions,
                                               incomeSourceDetailsService: IncomeSourceDetailsService,
                                               val sessionService: SessionService)
                                              (implicit
                                               val ec: ExecutionContext,
                                               val mcc: MessagesControllerComponents,
                                               val itvcErrorHandler: ItvcErrorHandler,
                                               val itvcErrorHandlerAgent: AgentItvcErrorHandler,
                                               val appConfig: FrontendAppConfig) extends FrontendController(mcc)
  with FeatureSwitching with IncomeSourcesUtils with I18nSupport {

  def show(): Action[AnyContent] = authActions.asMTDIndividual().async { implicit user =>
    handleRequest(
      sources = user.incomeSources,
      isAgent = false,
      backUrl = uk.gov.hmrc.incometaxbusinessdetailsfrontend.controllers.routes.HomeController.show().url
    )(user, itvcErrorHandler)
  }

  def showAgent(): Action[AnyContent] = authActions.asMTDAgentWithConfirmedClient().async  { implicit user =>
    handleRequest(
      sources = user.incomeSources,
      isAgent = true,
      backUrl = uk.gov.hmrc.incometaxbusinessdetailsfrontend.controllers.routes.HomeController.showAgent().url
    )(user, itvcErrorHandlerAgent)
  }

  def handleRequest(sources: IncomeSourceDetailsModel, isAgent: Boolean, backUrl: String)
                   (implicit user: MtdItUser[_], errorHandler: ShowInternalServerError): Future[Result] = {

    incomeSourceDetailsService.getViewIncomeSourceViewModel(sources, isEnabled(DisplayBusinessStartDate)) match {
      case Right(viewModel) =>
        Future(hc.sessionId.get).flatMap { sessionId =>
          sessionService.clearSession(sessionId.value).map { _ =>
            Ok(manageYourBusinesses(
              sources = viewModel,
              isAgent = isAgent,
              backUrl = backUrl
            ))
          }
        }.recover {
          case ex: Exception =>
            Logger("application").error(
              s"Session Error: ${ex.getMessage} - ${ex.getCause}")
            errorHandler.showInternalServerError()
        }
      case Left(ex) =>
        Logger("application").error(
          s"Error: ${ex.getMessage} - ${ex.getCause}")
        Future(errorHandler.showInternalServerError())
    }
  }
}
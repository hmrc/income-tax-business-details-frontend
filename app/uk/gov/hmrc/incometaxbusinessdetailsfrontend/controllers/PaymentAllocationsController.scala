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

package uk.gov.hmrc.incometaxbusinessdetailsfrontend.controllers

import uk.gov.hmrc.incometaxbusinessdetailsfrontend.audit.AuditingService
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.audit.models.PaymentAllocationsResponseAuditModel
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.MtdItUser
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.auth.authV2.AuthActions
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.config.featureswitch._
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.config.{AgentItvcErrorHandler, FrontendAppConfig, ItvcErrorHandler, ShowInternalServerError}
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.enums.GatewayPage.GatewayPage
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.forms.utils.SessionKeys.gatewayPage
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.implicits.ImplicitDateFormatterImpl
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.admin.CreditsRefundsRepay
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.core.Nino
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.models.paymentAllocationCharges.{PaymentAllocationError, PaymentAllocationViewModel}
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import play.mvc.Http
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.services.PaymentAllocationsService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.utils.FallBackBackLinks
import uk.gov.hmrc.incometaxbusinessdetailsfrontend.views.html.PaymentAllocationView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentAllocationsController @Inject()(val paymentAllocationView: PaymentAllocationView,
                                             val authActions: AuthActions,
                                             itvcErrorHandler: ItvcErrorHandler,
                                             val itvcErrorHandlerAgent: AgentItvcErrorHandler,
                                             paymentAllocations: PaymentAllocationsService,
                                             auditingService: AuditingService)
                                            (implicit val mcc: MessagesControllerComponents,
                                             val ec: ExecutionContext,
                                             val implicitDateFormatter: ImplicitDateFormatterImpl,
                                             val appConfig: FrontendAppConfig) extends FrontendController(mcc)
  with I18nSupport with FeatureSwitching with FallBackBackLinks {

  private lazy val redirectUrlIndividual: String = uk.gov.hmrc.incometaxbusinessdetailsfrontend.controllers.errors.routes.NotFoundDocumentIDLookupController.show().url
  private lazy val redirectUrlAgent: String = uk.gov.hmrc.incometaxbusinessdetailsfrontend.controllers.agent.errors.routes.AgentNotFoundDocumentIDLookupController.show().url

  def viewPaymentAllocation(documentNumber: String, origin: Option[String] = None): Action[AnyContent] = authActions.asMTDIndividual().async {
    implicit user =>
      handleRequest(
        itvcErrorHandler = itvcErrorHandler,
        documentNumber = documentNumber,
        redirectUrl = redirectUrlIndividual,
        isAgent = false,
        origin = origin
      )

  }

  def handleRequest(itvcErrorHandler: ShowInternalServerError,
                    documentNumber: String,
                    redirectUrl: String,
                    isAgent: Boolean,
                    origin: Option[String] = None)
                   (implicit user: MtdItUser[_], hc: HeaderCarrier, ec: ExecutionContext, messages: Messages): Future[Result] = {

    val sessionGatewayPage = user.session.get(gatewayPage).map(GatewayPage(_))
    paymentAllocations.getPaymentAllocation(Nino(user.nino), documentNumber) map {
      case Right(paymentAllocations: PaymentAllocationViewModel) =>
        val taxYearOpt = paymentAllocations.originalPaymentAllocationWithClearingDate.headOption.flatMap(_.allocationDetail.flatMap(_.getTaxYearOpt))
        val backUrl = getPaymentAllocationBackUrl(sessionGatewayPage, taxYearOpt, origin, isAgent)
        auditingService.extendedAudit(PaymentAllocationsResponseAuditModel(user, paymentAllocations))
        Ok(paymentAllocationView(paymentAllocations, backUrl = backUrl, user.saUtr,
          btaNavPartial = user.btaNavPartial,
          serviceNavigationPartial = user.serviceNavigationPartial,
          isAgent = isAgent, origin = origin, gatewayPage = sessionGatewayPage,
          creditsRefundsRepayEnabled = isEnabled(CreditsRefundsRepay))(implicitly, messages))

      case Left(PaymentAllocationError(Some(Http.Status.NOT_FOUND))) =>
        Redirect(redirectUrl)
      case _ => itvcErrorHandler.showInternalServerError()
    }
  }

  def viewPaymentAllocationAgent(documentNumber: String): Action[AnyContent] = {
    authActions.asMTDPrimaryAgent().async {
      implicit mtdItUser =>
        handleRequest(
          itvcErrorHandler = itvcErrorHandlerAgent,
          documentNumber = documentNumber,
          redirectUrl = redirectUrlAgent,
          isAgent = true
        )
    }
  }
}

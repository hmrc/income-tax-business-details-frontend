/*
 * Copyright 2024 HM Revenue & Customs
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

package common.models.chargeSummary

import common.enums.GatewayPage.GatewayPage
import common.models.ChargeHistoryItem
import common.models.chargeHistory.AdjustmentHistoryModel
import common.models.financialDetails.*
import uk.gov.hmrc.govukfrontend.views.viewmodels.servicenavigation.ServiceNavigation

import java.time.LocalDate

// TODO MIPR-2779: temporary compile-only version for businessDetails migration.
// The copied V&C model pulls financials controllers, repayment history and payment allocation internals.
// Replace with the real implementation only if the migrated businessDetails flow genuinely needs charge summary behaviour.
case class ChargeSummaryViewModel(
                                   currentDate: LocalDate,
                                   chargeItem: ChargeItem,
                                   backUrl: String,
                                   paymentBreakdown: List[FinancialDetail],
                                   paymentAllocations: List[PaymentHistoryAllocations],
                                   payments: FinancialDetailsModel,
                                   chargeHistoryEnabled: Boolean,
                                   creditsRefundRepayEnabled: Boolean = true,
                                   latePaymentInterestCharge: Boolean,
                                   penaltiesEnabled: Boolean,
                                   reviewAndReconcileCredit: Option[ChargeItem],
                                   isAgent: Boolean = false,
                                   serviceNavigationPartial: Option[ServiceNavigation] = None,
                                   origin: Option[String] = None,
                                   gatewayPage: Option[GatewayPage] = None,
                                   adjustmentHistory: AdjustmentHistoryModel,
                                   poaExtraChargeLink: Option[String] = None,
                                   poaOneChargeUrl: String,
                                   poaTwoChargeUrl: String,
                                   LSPUrl: String,
                                   LPPUrl: String
                                 ) {

  val dueDate = chargeItem.dueDate
  val isCredit: Boolean = chargeItem.originalAmount < 0
  val pageTitle: String = s"chargeSummary.${chargeItem.getChargeTypeKey}"

  val hasPaymentBreakdown: Boolean = false
  val hasDunningLocks: Boolean = false
  val hasInterestLocks: Boolean = false
  val hasAccruedInterest: Boolean = false
  val isBalancingChargeZero: Boolean = false
  val chargeHistoryEnabledOrPaymentAllocationWithNoIsBalancingChargeZeroAndIsNotCredit: Boolean = false

  val sortedChargeHistoryTableEntries: List[ChargeHistoryItem] = Nil
}

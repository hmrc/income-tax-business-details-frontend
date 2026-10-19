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

package common.enums.AuditType

import scala.language.implicitConversions

sealed trait AuditType {
  val name: String

  implicit def auditTypeToString(auditType: AuditType): String = {
    auditType.name
  }
}

case object IncomeSourceDetailsResponse extends AuditType {
  val name = "incomeSourceDetailsResponse"
}

case object LowConfidenceLevelIvOutcomeFail extends AuditType {
  val name = "LowConfidenceLevelIvOutcomeFail"
}

case object LowConfidenceLevelIvOutcomeSuccess extends AuditType {
  val name = "LowConfidenceLevelIvOutcomeSuccess"
}

case object LowConfidenceLevelIvHandoff extends AuditType {
  val name = "LowConfidenceLevelIvHandoff"
}

case object CeaseIncomeSource extends AuditType {
  override val name: String = "CeaseIncomeSource"
}

case object UpdateIncomeSource extends AuditType {
  override val name: String = "UpdateIncomeSource"
}

case object CreateIncomeSource extends AuditType {
  override val name: String = "CreateIncomeSource"
}

case object AccessDeniedForSupportingAgent extends AuditType {
  val name = "AccessDeniedForSupportingAgent"
}

case object TriggeredMigrationStart extends AuditType {
  val name = "TriggeredMigrationStart"
}

case object TriggeredMigrationComplete extends AuditType {
  val name = "TriggeredMigrationComplete"
}
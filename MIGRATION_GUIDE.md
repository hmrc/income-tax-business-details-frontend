# Migration Guide: authV2 to income-tax-business-details-frontend

## Overview
Move `app/auth/authV2/` folder and dependencies to new repo. Keep entire contents unless specified to comment out. Edit only to remove unused code/imports as listed.

---

## ~~app/auth/~~

### ~~Move as-is (no changes):~~
- ~~`FrontendAuthorisedFunctions.scala`~~
- ~~`MtdItUser.scala`~~

### ~~Move authV2/ folder entirely (no changes to any files):~~
- ~~All 25 files in `app/auth/authV2/`:~~
  - ~~`AuthActions.scala`~~
  - ~~`AuthExceptions.scala`~~
  - ~~`Constants.scala`~~
  - ~~`models/AuthUserDetails.scala`~~
  - ~~`models/AgentClientDetails.scala`~~
  - ~~`models/AuthorisedUserRequest.scala`~~
  - ~~`models/ItsaStatusRetrievalActionError.scala`~~
  - ~~`actions/SessionTimeoutAction.scala`~~
  - ~~`actions/AuthoriseAndRetrieve.scala`~~
  - ~~`actions/AuthoriseAndRetrieveIndividual.scala`~~
  - ~~`actions/AuthoriseAndRetrieveAgent.scala`~~
  - ~~`actions/AuthoriseAndRetrieveIndividualForNrs.scala`~~
  - ~~`actions/AuthoriseAndRetrieveAgentForNrs.scala`~~
  - ~~`actions/AuthoriseAndRetrieveMtdAgent.scala`~~
  - ~~`actions/AuthoriseHelper.scala`~~
  - ~~`actions/AgentHasConfirmedClientAction.scala`~~
  - ~~`actions/AgentIsPrimaryAction.scala`~~
  - ~~`actions/IncomeSourceRetrievalAction.scala`~~
  - ~~`actions/ItsaStatusRetrievalAction.scala`~~
  - ~~`actions/NavBarRetrievalAction.scala`~~
  - ~~`actions/FeatureSwitchRetrievalAction.scala`~~
  - ~~`actions/RetrieveClientData.scala`~~
  - ~~`actions/TriggeredMigrationRetrievalAction.scala`~~
  - ~~`actions/SaveOriginAndRedirect.scala`~~
  - ~~`actions/RedirectIfNoIncomeSourcesAction.scala`~~

---

## app/controllers/

### Move as-is (no changes):
- `BaseController.scala`
- `timeout/SessionTimeoutController.scala`
- `SignInController.scala`
- `errors/NotEnrolledController.scala`
- `errors/UpliftFailedController.scala`
- `UpliftSuccessController.scala`
- `NoIncomeSourcesController.scala`
- `agent/AuthUtils.scala`
- `agent/EnterClientsUTRController.scala`
- `agent/ConfirmClientUTRController.scala`
- `agent/ClientRelationshipFailureController.scala`
- `agent/NoAssignmentController.scala`
- `agent/AgentErrorController.scala`
- `agent/sessionUtils/SessionKeys.scala`
- `bta/BtaNavBarController.scala`

### Heavily trim:
- **HomeController.scala**: Keep ONLY `show()` and `showAgent()` methods. Comment out all other vals and methods (keep imports intact).

---

## app/enums/

### Move as-is (no changes):
- `MTDUserRole.scala`
- `OriginEnum.scala`
- `TriggeredMigration/Channel.scala`
- `JourneyType.scala` (only `TriggeredMigrationJourney` is used, but leave all)

### Move and trim:
- **TransactionName/TransactionName.scala**: Keep ONLY case objects:
  - `LowConfidenceLevelIvHandoff` (line 69)
  - `AccessDeniedForSupportingAgent` (line 145)
  - Comment out all other case objects and their corresponding entries

- **AuditType/AuditType.scala**: Keep ONLY case objects:
  - `LowConfidenceLevelIvHandoff`
  - `AccessDeniedForSupportingAgent`
  - Comment out all others

---

## app/audit/

### Move as-is (no changes):
- `AuditingService.scala`
- `Utilities.scala`
- `models/AuditModel.scala`
- `models/ExtendedAuditModel.scala`
- `models/IvUpliftRequiredAuditModel.scala`
- `models/AccessDeniedForSupportingAgentAuditModel.scala`

---

## app/config/

### Move as-is (no changes):
- `ItvcErrorHandler.scala`
- `AgentItvcErrorHandler.scala`
- `DIModule.scala` (keep both bindings as-is)
- `featureswitch/FeatureSwitching.scala`

### Move and trim:
- **FrontendWiring.scala**: Comment out lines 34-35 (`ItvcHeaderCarrierForPartialsConverter`)

- **FrontendAppConfig.scala**: Heavy trim. Keep ONLY config values used by authV2:
  - Keep properties related to: auth URLs, timeouts, crypto config, environment
  - Comment out all business-domain specific properties (VAT, income sources displays, payment amounts, etc.)

---

## app/models/

### Move entire folder: `app/models/incomeSourceDetails/`
- `IncomeSourceDetailsResponse.scala` (contains `IncomeSourceDetailsModel` at line 81, `IncomeSourceDetailsError` at line 226)
- `BusinessDetailsModel.scala`
- `PropertyDetailsModel.scala`
- `LatencyDetails.scala`
- `QuarterTypeElection.scala`
- `TaxYear.scala`

### Move entire folder: `app/models/itsaStatus/`
- `ITSAStatusResponse.scala`
- `StatusDetail.scala`

### Move entire folder: `app/models/sessionData/`
- `SessionDataGetResponse.scala`
- `SessionDataModel.scala`
- `SessionCookieData.scala`

### Move and trim:
- **app/models/admin/FeatureSwitchName.scala**: Keep ONLY case objects:
  - `NavBarFs` (line 144)
  - `TriggeredMigration` (line 207)
  - `CY+1YouMustWaitToSignUpPageEnabled` (line 222)
  - `InvalidFS` (line 177)
  - Comment out all other case objects and their entries in `allFeatureSwitches`

- **app/models/liabilitycalculation/LiabilityCalculationResponse.scala**: Comment out lines 125-143 and remove import of `ImplicitDateFormatter`. Move:
  - `LiabilityCalculationResponse.scala`
  - `Calculation.scala`
  - `SubmissionChannel.scala`

### Additional models to move (required by authV2 actions):
- `app/models/core/AddressModel.scala`
- `app/models/core/IncomeSourceId.scala`
- `app/models/core/AccountingPeriodModel.scala`
- `app/models/core/CessationModel.scala`
- `app/models/UIJourneySessionData.scala`
- `app/models/triggeredMigration/TriggeredMigrationSessionData.scala`

---

## app/services/

### Move as-is (no changes):
- `IncomeSourceDetailsService.scala`
- `DateServiceInterface.scala` (and its implementation)
- `admin/FeatureSwitchService.scala`
- `SessionDataService.scala`
- `agent/ClientDetailsService.scala`
- `ITSAStatusService.scala`
- `CustomerFactsUpdateService.scala`
- `SessionService.scala`

---

## app/connectors/

### Move as-is (no changes):
- `ITSAStatusConnector.scala`
- `IncomeTaxCalculationConnector.scala`

---

## app/forms/

### Move as-is (no changes):
- `utils/SessionKeys.scala`

---

## conf/

### Move as-is (no changes):
- `businessDetails.routes`
- `manageBusinesses.routes`
- `obligations.routes`

### Create new files:
- **app.routes**: Create minimal version with ONLY:
  - `GET  /sign-out` → SessionTimeoutController.signOut()
  - `GET  /sign-in` → SignInController.signIn()
  - `GET  /not-enrolled` → NotEnrolledController.show()
  - `GET  /uplift-required` → UpliftFailedController.show()
  - `GET  /uplift-success` → UpliftSuccessController.show()
  - `GET  /home` → HomeController.show()
  - `GET  /` → HomeController.show()
  - `GET  /no-income-sources` → NoIncomeSourcesController.show()

- **agent.routes**: Create minimal version with ONLY:
  - `GET  /enter-clients-utr` → EnterClientsUTRController.show()
  - `GET  /confirm-client-utr` → ConfirmClientUTRController.show()
  - `GET  /relationship-failure` → ClientRelationshipFailureController.show()
  - `GET  /no-assignment` → NoAssignmentController.show()
  - `GET  /error` → AgentErrorController.show()

---

## test/

### Move authV2 test folder entirely (no changes):
- All 19 files in `test/authV2/`:
  - `AuthActionsSpecHelper.scala`
  - `AuthActionsTestData.scala`
  - `AuthoriseAndRetrieveSpec.scala`
  - `AuthoriseAndRetrieveIndividualSpec.scala`
  - `AuthoriseAndRetrieveIndividualForNrsSpec.scala`
  - `AuthoriseAndRetrieveAgentSpec.scala`
  - `AuthoriseAndRetrieveAgentForNrsSpec.scala`
  - `AuthoriseAndRetrieveMtdAgentSpec.scala`
  - `AgentHasConfirmedClientActionSpec.scala`
  - `AgentIsPrimaryActionSpec.scala`
  - `IncomeSourceRetrievalActionSpec.scala`
  - `ItsaStatusRetrievalActionSpec.scala`
  - `NavBarRetrievalActionSpec.scala`
  - `FeatureSwitchRetrievalActionSpec.scala`
  - `RetrieveClientDataSpec.scala`
  - `TriggeredMigrationRetrievalActionSpec.scala`
  - `SaveOriginAndRedirectSpec.scala`
  - `SessionTimeoutActionSpec.scala`
  - `RedirectIfNoIncomeSourcesActionSpec.scala`

### Move test utilities (no changes):
- `test/testUtils/UnitSpec.scala`
- `test/mocks/services/MockClientDetailsService.scala`
- `test/mocks/services/MockSessionService.scala`
- `test/mocks/services/MockAsyncCacheApi.scala`

### Move and trim:
- **test/testUtils/TestSupport.scala**: Heavy trim. Keep ONLY:
  - Base test class structure
  - Mocked Play components (WSClient, injector, etc.)
  - Basic request building (`FakeRequest` setup)
  - Comment out all imports and usages related to:
    - `models.financialDetails.ChargeItem`
    - `testOnly.repository.FeatureSwitchRepository`
    - `implicits.ImplicitDateFormatterImpl`
    - Business domain specific mocks

- **test/testConstants/BaseTestConstants.scala**: Keep ONLY:
  - Auth-related constants
  - MTD-related constants
  - Comment out all financial/business-specific constants

- **test/testConstants/incomeSources/IncomeSourceDetailsTestConstants.scala**: Keep ONLY constants used by authV2 tests:
  - `singleBusinessIncome`
  - `businessesAndPropertyIncome`
  - `noIncomeDetails`
  - Comment out all other constants

- **test/testConstants/BusinessDetailsTestConstants.scala**: Keep ONLY what's imported by other test files

- **test/testConstants/PropertyDetailsTestConstants.scala**: Keep ONLY what's imported by other test files

### Additional test files to move:
- `test/mocks/` (all mock files)
- `test/generators/` (all generator files)
- `test/resources/` (test resources)
- `test/valiadtion/` (test validation helpers - note: there's a typo in the folder name "valiadtion")

---

## Summary of Changes by Type

### Files to MOVE AS-IS (no edits):
- All 25 files in `app/auth/authV2/`
- All 19 files in `test/authV2/`
- All files in `app/models/incomeSourceDetails/`
- All files in `app/models/itsaStatus/`
- All files in `app/models/sessionData/`
- All files in `app/models/liabilitycalculation/` except comment out lines in Response file
- Most auth/config/audit files

### Files to MOVE AND TRIM (comment out unused):
- `HomeController.scala` - keep only `show()` and `showAgent()`
- `FrontendWiring.scala` - comment out lines 34-35
- `FrontendAppConfig.scala` - trim to auth-only properties
- `FeatureSwitchName.scala` - keep only 4 case objects
- `TransactionName.scala` - keep only 2 case objects
- `AuditType.scala` - keep only 2 case objects
- `LiabilityCalculationResponse.scala` - comment out lines 125-143
- `TestSupport.scala` - heavy trim
- Test constants files - trim to authV2 needs

### Files to CREATE NEW:
- `conf/app.routes` - minimal version with auth routes only
- `conf/agent.routes` - minimal version with agent auth routes only

---

## Order of Operations

1. Create new repo structure with folder hierarchy
2. Move all authV2 source files (no changes)
3. Move all authV2 test files (no changes)
4. Move auth/config/audit support files (apply listed trims)
5. Move models (apply listed trims)
6. Move services and connectors
7. Move test utilities and constants (apply listed trims)
8. Create new minimal routes files
9. Apply all specified trimming/commenting to files
10. Compile and resolve remaining dependencies


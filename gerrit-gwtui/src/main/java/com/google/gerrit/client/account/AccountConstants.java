begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Constants
import|;
end_import

begin_interface
DECL|interface|AccountConstants
specifier|public
interface|interface
name|AccountConstants
extends|extends
name|Constants
block|{
DECL|method|settingsHeading ()
name|String
name|settingsHeading
parameter_list|()
function_decl|;
DECL|method|fullName ()
name|String
name|fullName
parameter_list|()
function_decl|;
DECL|method|preferredEmail ()
name|String
name|preferredEmail
parameter_list|()
function_decl|;
DECL|method|registeredOn ()
name|String
name|registeredOn
parameter_list|()
function_decl|;
DECL|method|accountId ()
name|String
name|accountId
parameter_list|()
function_decl|;
DECL|method|maximumPageSizeFieldLabel ()
name|String
name|maximumPageSizeFieldLabel
parameter_list|()
function_decl|;
DECL|method|dateFormatLabel ()
name|String
name|dateFormatLabel
parameter_list|()
function_decl|;
DECL|method|contextWholeFile ()
name|String
name|contextWholeFile
parameter_list|()
function_decl|;
DECL|method|showSiteHeader ()
name|String
name|showSiteHeader
parameter_list|()
function_decl|;
DECL|method|useFlashClipboard ()
name|String
name|useFlashClipboard
parameter_list|()
function_decl|;
DECL|method|copySelfOnEmails ()
name|String
name|copySelfOnEmails
parameter_list|()
function_decl|;
DECL|method|displayPatchSetsInReverseOrder ()
name|String
name|displayPatchSetsInReverseOrder
parameter_list|()
function_decl|;
DECL|method|displayPersonNameInReviewCategory ()
name|String
name|displayPersonNameInReviewCategory
parameter_list|()
function_decl|;
DECL|method|buttonSaveChanges ()
name|String
name|buttonSaveChanges
parameter_list|()
function_decl|;
DECL|method|tabAccountSummary ()
name|String
name|tabAccountSummary
parameter_list|()
function_decl|;
DECL|method|tabPreferences ()
name|String
name|tabPreferences
parameter_list|()
function_decl|;
DECL|method|tabWatchedProjects ()
name|String
name|tabWatchedProjects
parameter_list|()
function_decl|;
DECL|method|tabContactInformation ()
name|String
name|tabContactInformation
parameter_list|()
function_decl|;
DECL|method|tabSshKeys ()
name|String
name|tabSshKeys
parameter_list|()
function_decl|;
DECL|method|tabHttpAccess ()
name|String
name|tabHttpAccess
parameter_list|()
function_decl|;
DECL|method|tabWebIdentities ()
name|String
name|tabWebIdentities
parameter_list|()
function_decl|;
DECL|method|tabMyGroups ()
name|String
name|tabMyGroups
parameter_list|()
function_decl|;
DECL|method|tabAgreements ()
name|String
name|tabAgreements
parameter_list|()
function_decl|;
DECL|method|buttonShowAddSshKey ()
name|String
name|buttonShowAddSshKey
parameter_list|()
function_decl|;
DECL|method|buttonCloseAddSshKey ()
name|String
name|buttonCloseAddSshKey
parameter_list|()
function_decl|;
DECL|method|buttonDeleteSshKey ()
name|String
name|buttonDeleteSshKey
parameter_list|()
function_decl|;
DECL|method|buttonClearSshKeyInput ()
name|String
name|buttonClearSshKeyInput
parameter_list|()
function_decl|;
DECL|method|buttonOpenSshKey ()
name|String
name|buttonOpenSshKey
parameter_list|()
function_decl|;
DECL|method|buttonAddSshKey ()
name|String
name|buttonAddSshKey
parameter_list|()
function_decl|;
DECL|method|userName ()
name|String
name|userName
parameter_list|()
function_decl|;
DECL|method|password ()
name|String
name|password
parameter_list|()
function_decl|;
DECL|method|buttonSetUserName ()
name|String
name|buttonSetUserName
parameter_list|()
function_decl|;
DECL|method|buttonChangeUserName ()
name|String
name|buttonChangeUserName
parameter_list|()
function_decl|;
DECL|method|buttonClearPassword ()
name|String
name|buttonClearPassword
parameter_list|()
function_decl|;
DECL|method|buttonGeneratePassword ()
name|String
name|buttonGeneratePassword
parameter_list|()
function_decl|;
DECL|method|invalidUserName ()
name|String
name|invalidUserName
parameter_list|()
function_decl|;
DECL|method|sshKeyInvalid ()
name|String
name|sshKeyInvalid
parameter_list|()
function_decl|;
DECL|method|sshKeyAlgorithm ()
name|String
name|sshKeyAlgorithm
parameter_list|()
function_decl|;
DECL|method|sshKeyKey ()
name|String
name|sshKeyKey
parameter_list|()
function_decl|;
DECL|method|sshKeyComment ()
name|String
name|sshKeyComment
parameter_list|()
function_decl|;
DECL|method|sshKeyStatus ()
name|String
name|sshKeyStatus
parameter_list|()
function_decl|;
DECL|method|addSshKeyPanelHeader ()
name|String
name|addSshKeyPanelHeader
parameter_list|()
function_decl|;
DECL|method|addSshKeyHelp ()
name|String
name|addSshKeyHelp
parameter_list|()
function_decl|;
DECL|method|sshJavaAppletNotAvailable ()
name|String
name|sshJavaAppletNotAvailable
parameter_list|()
function_decl|;
DECL|method|invalidSshKeyError ()
name|String
name|invalidSshKeyError
parameter_list|()
function_decl|;
DECL|method|sshHostKeyTitle ()
name|String
name|sshHostKeyTitle
parameter_list|()
function_decl|;
DECL|method|sshHostKeyFingerprint ()
name|String
name|sshHostKeyFingerprint
parameter_list|()
function_decl|;
DECL|method|sshHostKeyKnownHostEntry ()
name|String
name|sshHostKeyKnownHostEntry
parameter_list|()
function_decl|;
DECL|method|webIdStatus ()
name|String
name|webIdStatus
parameter_list|()
function_decl|;
DECL|method|webIdEmail ()
name|String
name|webIdEmail
parameter_list|()
function_decl|;
DECL|method|webIdIdentity ()
name|String
name|webIdIdentity
parameter_list|()
function_decl|;
DECL|method|untrustedProvider ()
name|String
name|untrustedProvider
parameter_list|()
function_decl|;
DECL|method|buttonDeleteIdentity ()
name|String
name|buttonDeleteIdentity
parameter_list|()
function_decl|;
DECL|method|buttonLinkIdentity ()
name|String
name|buttonLinkIdentity
parameter_list|()
function_decl|;
DECL|method|buttonWatchProject ()
name|String
name|buttonWatchProject
parameter_list|()
function_decl|;
DECL|method|defaultProjectName ()
name|String
name|defaultProjectName
parameter_list|()
function_decl|;
DECL|method|defaultFilter ()
name|String
name|defaultFilter
parameter_list|()
function_decl|;
DECL|method|buttonBrowseProjects ()
name|String
name|buttonBrowseProjects
parameter_list|()
function_decl|;
DECL|method|projects ()
name|String
name|projects
parameter_list|()
function_decl|;
DECL|method|projectsClose ()
name|String
name|projectsClose
parameter_list|()
function_decl|;
DECL|method|projectListOpen ()
name|String
name|projectListOpen
parameter_list|()
function_decl|;
DECL|method|watchedProjectName ()
name|String
name|watchedProjectName
parameter_list|()
function_decl|;
DECL|method|watchedProjectFilter ()
name|String
name|watchedProjectFilter
parameter_list|()
function_decl|;
DECL|method|watchedProjectColumnEmailNotifications ()
name|String
name|watchedProjectColumnEmailNotifications
parameter_list|()
function_decl|;
DECL|method|watchedProjectColumnNewChanges ()
name|String
name|watchedProjectColumnNewChanges
parameter_list|()
function_decl|;
DECL|method|watchedProjectColumnAllComments ()
name|String
name|watchedProjectColumnAllComments
parameter_list|()
function_decl|;
DECL|method|watchedProjectColumnSubmittedChanges ()
name|String
name|watchedProjectColumnSubmittedChanges
parameter_list|()
function_decl|;
DECL|method|contactFieldFullName ()
name|String
name|contactFieldFullName
parameter_list|()
function_decl|;
DECL|method|contactFieldEmail ()
name|String
name|contactFieldEmail
parameter_list|()
function_decl|;
DECL|method|contactPrivacyDetailsHtml ()
name|String
name|contactPrivacyDetailsHtml
parameter_list|()
function_decl|;
DECL|method|contactFieldAddress ()
name|String
name|contactFieldAddress
parameter_list|()
function_decl|;
DECL|method|contactFieldCountry ()
name|String
name|contactFieldCountry
parameter_list|()
function_decl|;
DECL|method|contactFieldPhone ()
name|String
name|contactFieldPhone
parameter_list|()
function_decl|;
DECL|method|contactFieldFax ()
name|String
name|contactFieldFax
parameter_list|()
function_decl|;
DECL|method|buttonOpenRegisterNewEmail ()
name|String
name|buttonOpenRegisterNewEmail
parameter_list|()
function_decl|;
DECL|method|buttonSendRegisterNewEmail ()
name|String
name|buttonSendRegisterNewEmail
parameter_list|()
function_decl|;
DECL|method|buttonCancel ()
name|String
name|buttonCancel
parameter_list|()
function_decl|;
DECL|method|titleRegisterNewEmail ()
name|String
name|titleRegisterNewEmail
parameter_list|()
function_decl|;
DECL|method|descRegisterNewEmail ()
name|String
name|descRegisterNewEmail
parameter_list|()
function_decl|;
DECL|method|newAgreement ()
name|String
name|newAgreement
parameter_list|()
function_decl|;
DECL|method|agreementStatus ()
name|String
name|agreementStatus
parameter_list|()
function_decl|;
DECL|method|agreementName ()
name|String
name|agreementName
parameter_list|()
function_decl|;
DECL|method|agreementDescription ()
name|String
name|agreementDescription
parameter_list|()
function_decl|;
DECL|method|agreementAccepted ()
name|String
name|agreementAccepted
parameter_list|()
function_decl|;
DECL|method|agreementStatus_EXPIRED ()
name|String
name|agreementStatus_EXPIRED
parameter_list|()
function_decl|;
DECL|method|agreementStatus_NEW ()
name|String
name|agreementStatus_NEW
parameter_list|()
function_decl|;
DECL|method|agreementStatus_REJECTED ()
name|String
name|agreementStatus_REJECTED
parameter_list|()
function_decl|;
DECL|method|agreementStatus_VERIFIED ()
name|String
name|agreementStatus_VERIFIED
parameter_list|()
function_decl|;
DECL|method|newAgreementSelectTypeHeading ()
name|String
name|newAgreementSelectTypeHeading
parameter_list|()
function_decl|;
DECL|method|newAgreementNoneAvailable ()
name|String
name|newAgreementNoneAvailable
parameter_list|()
function_decl|;
DECL|method|newAgreementReviewLegalHeading ()
name|String
name|newAgreementReviewLegalHeading
parameter_list|()
function_decl|;
DECL|method|newAgreementReviewContactHeading ()
name|String
name|newAgreementReviewContactHeading
parameter_list|()
function_decl|;
DECL|method|newAgreementCompleteHeading ()
name|String
name|newAgreementCompleteHeading
parameter_list|()
function_decl|;
DECL|method|newAgreementIAGREE ()
name|String
name|newAgreementIAGREE
parameter_list|()
function_decl|;
DECL|method|newAgreementAlreadySubmitted ()
name|String
name|newAgreementAlreadySubmitted
parameter_list|()
function_decl|;
DECL|method|buttonSubmitNewAgreement ()
name|String
name|buttonSubmitNewAgreement
parameter_list|()
function_decl|;
DECL|method|welcomeToGerritCodeReview ()
name|String
name|welcomeToGerritCodeReview
parameter_list|()
function_decl|;
DECL|method|welcomeReviewContact ()
name|String
name|welcomeReviewContact
parameter_list|()
function_decl|;
DECL|method|welcomeContactFrom ()
name|String
name|welcomeContactFrom
parameter_list|()
function_decl|;
DECL|method|welcomeUsernameHeading ()
name|String
name|welcomeUsernameHeading
parameter_list|()
function_decl|;
DECL|method|welcomeSshKeyHeading ()
name|String
name|welcomeSshKeyHeading
parameter_list|()
function_decl|;
DECL|method|welcomeSshKeyText ()
name|String
name|welcomeSshKeyText
parameter_list|()
function_decl|;
DECL|method|welcomeAgreementHeading ()
name|String
name|welcomeAgreementHeading
parameter_list|()
function_decl|;
DECL|method|welcomeAgreementText ()
name|String
name|welcomeAgreementText
parameter_list|()
function_decl|;
DECL|method|welcomeAgreementLater ()
name|String
name|welcomeAgreementLater
parameter_list|()
function_decl|;
DECL|method|welcomeContinue ()
name|String
name|welcomeContinue
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


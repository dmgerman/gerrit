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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
operator|.
name|FieldName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DownloadCommand
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DownloadScheme
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AuthType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
implements|implements
name|Cloneable
block|{
DECL|field|registerUrl
specifier|protected
name|String
name|registerUrl
decl_stmt|;
DECL|field|registerText
specifier|protected
name|String
name|registerText
decl_stmt|;
DECL|field|loginUrl
specifier|protected
name|String
name|loginUrl
decl_stmt|;
DECL|field|loginText
specifier|protected
name|String
name|loginText
decl_stmt|;
DECL|field|switchAccountUrl
specifier|protected
name|String
name|switchAccountUrl
decl_stmt|;
DECL|field|httpPasswordUrl
specifier|protected
name|String
name|httpPasswordUrl
decl_stmt|;
DECL|field|reportBugUrl
specifier|protected
name|String
name|reportBugUrl
decl_stmt|;
DECL|field|reportBugText
specifier|protected
name|String
name|reportBugText
decl_stmt|;
DECL|field|httpPasswordSettingsEnabled
specifier|protected
name|boolean
name|httpPasswordSettingsEnabled
init|=
literal|true
decl_stmt|;
DECL|field|gitweb
specifier|protected
name|GitwebConfig
name|gitweb
decl_stmt|;
DECL|field|useContributorAgreements
specifier|protected
name|boolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useContactInfo
specifier|protected
name|boolean
name|useContactInfo
decl_stmt|;
DECL|field|allowRegisterNewEmail
specifier|protected
name|boolean
name|allowRegisterNewEmail
decl_stmt|;
DECL|field|authType
specifier|protected
name|AuthType
name|authType
decl_stmt|;
DECL|field|downloadSchemes
specifier|protected
name|Set
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
decl_stmt|;
DECL|field|downloadCommands
specifier|protected
name|Set
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
decl_stmt|;
DECL|field|gitDaemonUrl
specifier|protected
name|String
name|gitDaemonUrl
decl_stmt|;
DECL|field|gitHttpUrl
specifier|protected
name|String
name|gitHttpUrl
decl_stmt|;
DECL|field|sshdAddress
specifier|protected
name|String
name|sshdAddress
decl_stmt|;
DECL|field|editFullNameUrl
specifier|protected
name|String
name|editFullNameUrl
decl_stmt|;
DECL|field|wildProject
specifier|protected
name|Project
operator|.
name|NameKey
name|wildProject
decl_stmt|;
DECL|field|editableAccountFields
specifier|protected
name|Set
argument_list|<
name|Account
operator|.
name|FieldName
argument_list|>
name|editableAccountFields
decl_stmt|;
DECL|field|documentationAvailable
specifier|protected
name|boolean
name|documentationAvailable
decl_stmt|;
DECL|field|anonymousCowardName
specifier|protected
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|suggestFrom
specifier|protected
name|int
name|suggestFrom
decl_stmt|;
DECL|field|changeUpdateDelay
specifier|protected
name|int
name|changeUpdateDelay
decl_stmt|;
DECL|field|changeScreen
specifier|protected
name|AccountGeneralPreferences
operator|.
name|ChangeScreen
name|changeScreen
decl_stmt|;
DECL|field|largeChangeSize
specifier|protected
name|int
name|largeChangeSize
decl_stmt|;
DECL|field|newFeatures
specifier|protected
name|boolean
name|newFeatures
decl_stmt|;
DECL|method|getLoginUrl ()
specifier|public
name|String
name|getLoginUrl
parameter_list|()
block|{
return|return
name|loginUrl
return|;
block|}
DECL|method|setLoginUrl (final String u)
specifier|public
name|void
name|setLoginUrl
parameter_list|(
specifier|final
name|String
name|u
parameter_list|)
block|{
name|loginUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getLoginText ()
specifier|public
name|String
name|getLoginText
parameter_list|()
block|{
return|return
name|loginText
return|;
block|}
DECL|method|setLoginText (String signinText)
specifier|public
name|void
name|setLoginText
parameter_list|(
name|String
name|signinText
parameter_list|)
block|{
name|this
operator|.
name|loginText
operator|=
name|signinText
expr_stmt|;
block|}
DECL|method|getRegisterUrl ()
specifier|public
name|String
name|getRegisterUrl
parameter_list|()
block|{
return|return
name|registerUrl
return|;
block|}
DECL|method|setRegisterUrl (final String u)
specifier|public
name|void
name|setRegisterUrl
parameter_list|(
specifier|final
name|String
name|u
parameter_list|)
block|{
name|registerUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getSwitchAccountUrl ()
specifier|public
name|String
name|getSwitchAccountUrl
parameter_list|()
block|{
return|return
name|switchAccountUrl
return|;
block|}
DECL|method|setSwitchAccountUrl (String u)
specifier|public
name|void
name|setSwitchAccountUrl
parameter_list|(
name|String
name|u
parameter_list|)
block|{
name|switchAccountUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getRegisterText ()
specifier|public
name|String
name|getRegisterText
parameter_list|()
block|{
return|return
name|registerText
return|;
block|}
DECL|method|setRegisterText (final String t)
specifier|public
name|void
name|setRegisterText
parameter_list|(
specifier|final
name|String
name|t
parameter_list|)
block|{
name|registerText
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getReportBugUrl ()
specifier|public
name|String
name|getReportBugUrl
parameter_list|()
block|{
return|return
name|reportBugUrl
return|;
block|}
DECL|method|setReportBugUrl (String u)
specifier|public
name|void
name|setReportBugUrl
parameter_list|(
name|String
name|u
parameter_list|)
block|{
name|reportBugUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getReportBugText ()
specifier|public
name|String
name|getReportBugText
parameter_list|()
block|{
return|return
name|reportBugText
return|;
block|}
DECL|method|setReportBugText (String t)
specifier|public
name|void
name|setReportBugText
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|reportBugText
operator|=
name|t
expr_stmt|;
block|}
DECL|method|isHttpPasswordSettingsEnabled ()
specifier|public
name|boolean
name|isHttpPasswordSettingsEnabled
parameter_list|()
block|{
return|return
name|httpPasswordSettingsEnabled
return|;
block|}
DECL|method|setHttpPasswordSettingsEnabled (boolean httpPasswordSettingsEnabled)
specifier|public
name|void
name|setHttpPasswordSettingsEnabled
parameter_list|(
name|boolean
name|httpPasswordSettingsEnabled
parameter_list|)
block|{
name|this
operator|.
name|httpPasswordSettingsEnabled
operator|=
name|httpPasswordSettingsEnabled
expr_stmt|;
block|}
DECL|method|getEditFullNameUrl ()
specifier|public
name|String
name|getEditFullNameUrl
parameter_list|()
block|{
return|return
name|editFullNameUrl
return|;
block|}
DECL|method|setEditFullNameUrl (String u)
specifier|public
name|void
name|setEditFullNameUrl
parameter_list|(
name|String
name|u
parameter_list|)
block|{
name|editFullNameUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getHttpPasswordUrl ()
specifier|public
name|String
name|getHttpPasswordUrl
parameter_list|()
block|{
return|return
name|httpPasswordUrl
return|;
block|}
DECL|method|setHttpPasswordUrl (String url)
specifier|public
name|void
name|setHttpPasswordUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|httpPasswordUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getAuthType ()
specifier|public
name|AuthType
name|getAuthType
parameter_list|()
block|{
return|return
name|authType
return|;
block|}
DECL|method|setAuthType (final AuthType t)
specifier|public
name|void
name|setAuthType
parameter_list|(
specifier|final
name|AuthType
name|t
parameter_list|)
block|{
name|authType
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getDownloadSchemes ()
specifier|public
name|Set
argument_list|<
name|DownloadScheme
argument_list|>
name|getDownloadSchemes
parameter_list|()
block|{
return|return
name|downloadSchemes
return|;
block|}
DECL|method|setDownloadSchemes (final Set<DownloadScheme> s)
specifier|public
name|void
name|setDownloadSchemes
parameter_list|(
specifier|final
name|Set
argument_list|<
name|DownloadScheme
argument_list|>
name|s
parameter_list|)
block|{
name|downloadSchemes
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getDownloadCommands ()
specifier|public
name|Set
argument_list|<
name|DownloadCommand
argument_list|>
name|getDownloadCommands
parameter_list|()
block|{
return|return
name|downloadCommands
return|;
block|}
DECL|method|setDownloadCommands (final Set<DownloadCommand> downloadCommands)
specifier|public
name|void
name|setDownloadCommands
parameter_list|(
specifier|final
name|Set
argument_list|<
name|DownloadCommand
argument_list|>
name|downloadCommands
parameter_list|)
block|{
name|this
operator|.
name|downloadCommands
operator|=
name|downloadCommands
expr_stmt|;
block|}
DECL|method|getGitwebLink ()
specifier|public
name|GitwebConfig
name|getGitwebLink
parameter_list|()
block|{
return|return
name|gitweb
return|;
block|}
DECL|method|setGitwebLink (final GitwebConfig w)
specifier|public
name|void
name|setGitwebLink
parameter_list|(
specifier|final
name|GitwebConfig
name|w
parameter_list|)
block|{
name|gitweb
operator|=
name|w
expr_stmt|;
block|}
DECL|method|isUseContributorAgreements ()
specifier|public
name|boolean
name|isUseContributorAgreements
parameter_list|()
block|{
return|return
name|useContributorAgreements
return|;
block|}
DECL|method|setUseContributorAgreements (final boolean r)
specifier|public
name|void
name|setUseContributorAgreements
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|useContributorAgreements
operator|=
name|r
expr_stmt|;
block|}
DECL|method|isUseContactInfo ()
specifier|public
name|boolean
name|isUseContactInfo
parameter_list|()
block|{
return|return
name|useContactInfo
return|;
block|}
DECL|method|setUseContactInfo (final boolean r)
specifier|public
name|void
name|setUseContactInfo
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|useContactInfo
operator|=
name|r
expr_stmt|;
block|}
DECL|method|getGitDaemonUrl ()
specifier|public
name|String
name|getGitDaemonUrl
parameter_list|()
block|{
return|return
name|gitDaemonUrl
return|;
block|}
DECL|method|setGitDaemonUrl (String url)
specifier|public
name|void
name|setGitDaemonUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
name|gitDaemonUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getGitHttpUrl ()
specifier|public
name|String
name|getGitHttpUrl
parameter_list|()
block|{
return|return
name|gitHttpUrl
return|;
block|}
DECL|method|setGitHttpUrl (String url)
specifier|public
name|void
name|setGitHttpUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
name|gitHttpUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getSshdAddress ()
specifier|public
name|String
name|getSshdAddress
parameter_list|()
block|{
return|return
name|sshdAddress
return|;
block|}
DECL|method|setSshdAddress (final String addr)
specifier|public
name|void
name|setSshdAddress
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|sshdAddress
operator|=
name|addr
expr_stmt|;
block|}
DECL|method|getWildProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getWildProject
parameter_list|()
block|{
return|return
name|wildProject
return|;
block|}
DECL|method|setWildProject (final Project.NameKey wp)
specifier|public
name|void
name|setWildProject
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|wp
parameter_list|)
block|{
name|wildProject
operator|=
name|wp
expr_stmt|;
block|}
DECL|method|canEdit (final Account.FieldName f)
specifier|public
name|boolean
name|canEdit
parameter_list|(
specifier|final
name|Account
operator|.
name|FieldName
name|f
parameter_list|)
block|{
return|return
name|editableAccountFields
operator|.
name|contains
argument_list|(
name|f
argument_list|)
return|;
block|}
DECL|method|getEditableAccountFields ()
specifier|public
name|Set
argument_list|<
name|Account
operator|.
name|FieldName
argument_list|>
name|getEditableAccountFields
parameter_list|()
block|{
return|return
name|editableAccountFields
return|;
block|}
DECL|method|setEditableAccountFields (final Set<Account.FieldName> af)
specifier|public
name|void
name|setEditableAccountFields
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|FieldName
argument_list|>
name|af
parameter_list|)
block|{
name|editableAccountFields
operator|=
name|af
expr_stmt|;
block|}
DECL|method|isDocumentationAvailable ()
specifier|public
name|boolean
name|isDocumentationAvailable
parameter_list|()
block|{
return|return
name|documentationAvailable
return|;
block|}
DECL|method|setDocumentationAvailable (final boolean available)
specifier|public
name|void
name|setDocumentationAvailable
parameter_list|(
specifier|final
name|boolean
name|available
parameter_list|)
block|{
name|documentationAvailable
operator|=
name|available
expr_stmt|;
block|}
DECL|method|getAnonymousCowardName ()
specifier|public
name|String
name|getAnonymousCowardName
parameter_list|()
block|{
return|return
name|anonymousCowardName
return|;
block|}
DECL|method|setAnonymousCowardName (final String anonymousCowardName)
specifier|public
name|void
name|setAnonymousCowardName
parameter_list|(
specifier|final
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
DECL|method|getSuggestFrom ()
specifier|public
name|int
name|getSuggestFrom
parameter_list|()
block|{
return|return
name|suggestFrom
return|;
block|}
DECL|method|setSuggestFrom (final int suggestFrom)
specifier|public
name|void
name|setSuggestFrom
parameter_list|(
specifier|final
name|int
name|suggestFrom
parameter_list|)
block|{
name|this
operator|.
name|suggestFrom
operator|=
name|suggestFrom
expr_stmt|;
block|}
DECL|method|siteHasUsernames ()
specifier|public
name|boolean
name|siteHasUsernames
parameter_list|()
block|{
if|if
condition|(
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|CUSTOM_EXTENSION
operator|&&
name|getHttpPasswordUrl
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|canEdit
argument_list|(
name|FieldName
operator|.
name|USER_NAME
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|getChangeUpdateDelay ()
specifier|public
name|int
name|getChangeUpdateDelay
parameter_list|()
block|{
return|return
name|changeUpdateDelay
return|;
block|}
DECL|method|setChangeUpdateDelay (int seconds)
specifier|public
name|void
name|setChangeUpdateDelay
parameter_list|(
name|int
name|seconds
parameter_list|)
block|{
name|changeUpdateDelay
operator|=
name|seconds
expr_stmt|;
block|}
DECL|method|getChangeScreen ()
specifier|public
name|AccountGeneralPreferences
operator|.
name|ChangeScreen
name|getChangeScreen
parameter_list|()
block|{
return|return
name|changeScreen
return|;
block|}
DECL|method|setChangeScreen (AccountGeneralPreferences.ChangeScreen ui)
specifier|public
name|void
name|setChangeScreen
parameter_list|(
name|AccountGeneralPreferences
operator|.
name|ChangeScreen
name|ui
parameter_list|)
block|{
name|this
operator|.
name|changeScreen
operator|=
name|ui
expr_stmt|;
block|}
DECL|method|getLargeChangeSize ()
specifier|public
name|int
name|getLargeChangeSize
parameter_list|()
block|{
return|return
name|largeChangeSize
return|;
block|}
DECL|method|setLargeChangeSize (int largeChangeSize)
specifier|public
name|void
name|setLargeChangeSize
parameter_list|(
name|int
name|largeChangeSize
parameter_list|)
block|{
name|this
operator|.
name|largeChangeSize
operator|=
name|largeChangeSize
expr_stmt|;
block|}
DECL|method|getNewFeatures ()
specifier|public
name|boolean
name|getNewFeatures
parameter_list|()
block|{
return|return
name|newFeatures
return|;
block|}
DECL|method|setNewFeatures (boolean n)
specifier|public
name|void
name|setNewFeatures
parameter_list|(
name|boolean
name|n
parameter_list|)
block|{
name|newFeatures
operator|=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit


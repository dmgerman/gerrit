begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
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
name|client
operator|.
name|ui
operator|.
name|MenuScreen
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
name|common
operator|.
name|PageLinks
import|;
end_import

begin_class
DECL|class|SettingsScreen
specifier|public
specifier|abstract
class|class
name|SettingsScreen
extends|extends
name|MenuScreen
block|{
DECL|method|SettingsScreen ()
specifier|public
name|SettingsScreen
parameter_list|()
block|{
name|setRequiresSignIn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabAccountSummary
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabPreferences
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_PREFERENCES
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabWatchedProjects
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_PROJECTS
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabContactInformation
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_CONTACT
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getSshdAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabSshKeys
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_SSHKEYS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|isGitBasicAuth
argument_list|()
condition|)
block|{
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabHttpAccess
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_HTTP_PASSWORD
argument_list|)
expr_stmt|;
block|}
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabWebIdentities
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_WEBIDENT
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabMyGroups
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_MYGROUPS
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|isUseContributorAgreements
argument_list|()
condition|)
block|{
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|tabAgreements
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|SETTINGS_AGREEMENTS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|settingsHeading
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


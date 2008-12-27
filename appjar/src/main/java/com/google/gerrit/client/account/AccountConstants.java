begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|method|accountSettingsHeading ()
name|String
name|accountSettingsHeading
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
DECL|method|tabPreferences ()
name|String
name|tabPreferences
parameter_list|()
function_decl|;
DECL|method|tabSshKeys ()
name|String
name|tabSshKeys
parameter_list|()
function_decl|;
DECL|method|tabAgreements ()
name|String
name|tabAgreements
parameter_list|()
function_decl|;
DECL|method|buttonDeleteSshKey ()
name|String
name|buttonDeleteSshKey
parameter_list|()
function_decl|;
DECL|method|buttonAddSshKey ()
name|String
name|buttonAddSshKey
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
DECL|method|sshKeyLastUsed ()
name|String
name|sshKeyLastUsed
parameter_list|()
function_decl|;
DECL|method|sshKeyStored ()
name|String
name|sshKeyStored
parameter_list|()
function_decl|;
DECL|method|addSshKeyPanelHeader ()
name|String
name|addSshKeyPanelHeader
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


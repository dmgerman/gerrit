begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|Messages
import|;
end_import

begin_interface
DECL|interface|GerritMessages
specifier|public
interface|interface
name|GerritMessages
extends|extends
name|Messages
block|{
DECL|method|windowTitle1 (String hostname)
name|String
name|windowTitle1
parameter_list|(
name|String
name|hostname
parameter_list|)
function_decl|;
DECL|method|windowTitle2 (String section, String hostname)
name|String
name|windowTitle2
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|hostname
parameter_list|)
function_decl|;
DECL|method|poweredBy (String version)
name|String
name|poweredBy
parameter_list|(
name|String
name|version
parameter_list|)
function_decl|;
DECL|method|noSuchAccountMessage (String who)
name|String
name|noSuchAccountMessage
parameter_list|(
name|String
name|who
parameter_list|)
function_decl|;
DECL|method|noSuchGroupMessage (String who)
name|String
name|noSuchGroupMessage
parameter_list|(
name|String
name|who
parameter_list|)
function_decl|;
DECL|method|branchCreationFailed (String branchName, String error)
name|String
name|branchCreationFailed
parameter_list|(
name|String
name|branchName
parameter_list|,
name|String
name|error
parameter_list|)
function_decl|;
DECL|method|invalidBranchName (String branchName)
name|String
name|invalidBranchName
parameter_list|(
name|String
name|branchName
parameter_list|)
function_decl|;
DECL|method|invalidRevision (String revision)
name|String
name|invalidRevision
parameter_list|(
name|String
name|revision
parameter_list|)
function_decl|;
DECL|method|branchCreationNotAllowedUnderRefnamePrefix (String refnamePrefix)
name|String
name|branchCreationNotAllowedUnderRefnamePrefix
parameter_list|(
name|String
name|refnamePrefix
parameter_list|)
function_decl|;
DECL|method|branchAlreadyExists (String branchName)
name|String
name|branchAlreadyExists
parameter_list|(
name|String
name|branchName
parameter_list|)
function_decl|;
DECL|method|branchCreationConflict (String branchName, String existingBranchName)
name|String
name|branchCreationConflict
parameter_list|(
name|String
name|branchName
parameter_list|,
name|String
name|existingBranchName
parameter_list|)
function_decl|;
DECL|method|pluginFailed (String scriptPath)
name|String
name|pluginFailed
parameter_list|(
name|String
name|scriptPath
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
DECL|interface|AdminConstants
specifier|public
interface|interface
name|AdminConstants
extends|extends
name|Constants
block|{
DECL|method|defaultAccountName ()
name|String
name|defaultAccountName
parameter_list|()
function_decl|;
DECL|method|defaultAccountGroupName ()
name|String
name|defaultAccountGroupName
parameter_list|()
function_decl|;
DECL|method|buttonDeleteGroupMembers ()
name|String
name|buttonDeleteGroupMembers
parameter_list|()
function_decl|;
DECL|method|buttonAddGroupMember ()
name|String
name|buttonAddGroupMember
parameter_list|()
function_decl|;
DECL|method|buttonSaveDescription ()
name|String
name|buttonSaveDescription
parameter_list|()
function_decl|;
DECL|method|buttonRenameGroup ()
name|String
name|buttonRenameGroup
parameter_list|()
function_decl|;
DECL|method|buttonCreateGroup ()
name|String
name|buttonCreateGroup
parameter_list|()
function_decl|;
DECL|method|buttonChangeGroupOwner ()
name|String
name|buttonChangeGroupOwner
parameter_list|()
function_decl|;
DECL|method|buttonAddProjectRight ()
name|String
name|buttonAddProjectRight
parameter_list|()
function_decl|;
DECL|method|headingOwner ()
name|String
name|headingOwner
parameter_list|()
function_decl|;
DECL|method|headingDescription ()
name|String
name|headingDescription
parameter_list|()
function_decl|;
DECL|method|headingMembers ()
name|String
name|headingMembers
parameter_list|()
function_decl|;
DECL|method|headingCreateGroup ()
name|String
name|headingCreateGroup
parameter_list|()
function_decl|;
DECL|method|headingAccessRights ()
name|String
name|headingAccessRights
parameter_list|()
function_decl|;
DECL|method|columnMember ()
name|String
name|columnMember
parameter_list|()
function_decl|;
DECL|method|columnEmailAddress ()
name|String
name|columnEmailAddress
parameter_list|()
function_decl|;
DECL|method|columnGroupName ()
name|String
name|columnGroupName
parameter_list|()
function_decl|;
DECL|method|columnProjectName ()
name|String
name|columnProjectName
parameter_list|()
function_decl|;
DECL|method|columnGroupDescription ()
name|String
name|columnGroupDescription
parameter_list|()
function_decl|;
DECL|method|columnProjectDescription ()
name|String
name|columnProjectDescription
parameter_list|()
function_decl|;
DECL|method|columnApprovalCategory ()
name|String
name|columnApprovalCategory
parameter_list|()
function_decl|;
DECL|method|columnRightRange ()
name|String
name|columnRightRange
parameter_list|()
function_decl|;
DECL|method|groupListTitle ()
name|String
name|groupListTitle
parameter_list|()
function_decl|;
DECL|method|projectListTitle ()
name|String
name|projectListTitle
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


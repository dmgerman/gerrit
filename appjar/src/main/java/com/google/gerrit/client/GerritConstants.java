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
name|Constants
import|;
end_import

begin_interface
DECL|interface|GerritConstants
specifier|public
interface|interface
name|GerritConstants
extends|extends
name|Constants
block|{
DECL|method|menuSignIn ()
name|String
name|menuSignIn
parameter_list|()
function_decl|;
DECL|method|menuSignOut ()
name|String
name|menuSignOut
parameter_list|()
function_decl|;
DECL|method|menuSettings ()
name|String
name|menuSettings
parameter_list|()
function_decl|;
DECL|method|signInDialogTitle ()
name|String
name|signInDialogTitle
parameter_list|()
function_decl|;
DECL|method|linkIdentityDialogTitle ()
name|String
name|linkIdentityDialogTitle
parameter_list|()
function_decl|;
DECL|method|loginTypeUnsupported ()
name|String
name|loginTypeUnsupported
parameter_list|()
function_decl|;
DECL|method|errorDialogTitle ()
name|String
name|errorDialogTitle
parameter_list|()
function_decl|;
DECL|method|errorDialogClose ()
name|String
name|errorDialogClose
parameter_list|()
function_decl|;
DECL|method|notFoundTitle ()
name|String
name|notFoundTitle
parameter_list|()
function_decl|;
DECL|method|notFoundBody ()
name|String
name|notFoundBody
parameter_list|()
function_decl|;
DECL|method|nameAlreadyUsedBody ()
name|String
name|nameAlreadyUsedBody
parameter_list|()
function_decl|;
DECL|method|menuAll ()
name|String
name|menuAll
parameter_list|()
function_decl|;
DECL|method|menuAllOpen ()
name|String
name|menuAllOpen
parameter_list|()
function_decl|;
DECL|method|menuAllMerged ()
name|String
name|menuAllMerged
parameter_list|()
function_decl|;
DECL|method|menuAllAbandoned ()
name|String
name|menuAllAbandoned
parameter_list|()
function_decl|;
DECL|method|menuMine ()
name|String
name|menuMine
parameter_list|()
function_decl|;
DECL|method|menuMyChanges ()
name|String
name|menuMyChanges
parameter_list|()
function_decl|;
DECL|method|menyMyDrafts ()
name|String
name|menyMyDrafts
parameter_list|()
function_decl|;
DECL|method|menuMyStarredChanges ()
name|String
name|menuMyStarredChanges
parameter_list|()
function_decl|;
DECL|method|menuAdmin ()
name|String
name|menuAdmin
parameter_list|()
function_decl|;
DECL|method|menuPeople ()
name|String
name|menuPeople
parameter_list|()
function_decl|;
DECL|method|menuGroups ()
name|String
name|menuGroups
parameter_list|()
function_decl|;
DECL|method|menuProjects ()
name|String
name|menuProjects
parameter_list|()
function_decl|;
DECL|method|rpcStatusLoading ()
name|String
name|rpcStatusLoading
parameter_list|()
function_decl|;
DECL|method|anonymousCoward ()
name|String
name|anonymousCoward
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


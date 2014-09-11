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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
DECL|interface|UIConstants
specifier|public
interface|interface
name|UIConstants
extends|extends
name|Constants
block|{
DECL|method|commentedActionButtonSend ()
name|String
name|commentedActionButtonSend
parameter_list|()
function_decl|;
DECL|method|commentedActionButtonCancel ()
name|String
name|commentedActionButtonCancel
parameter_list|()
function_decl|;
DECL|method|projectName ()
name|String
name|projectName
parameter_list|()
function_decl|;
DECL|method|projectDescription ()
name|String
name|projectDescription
parameter_list|()
function_decl|;
DECL|method|projectItemHelp ()
name|String
name|projectItemHelp
parameter_list|()
function_decl|;
DECL|method|projectStateAbbrev ()
name|String
name|projectStateAbbrev
parameter_list|()
function_decl|;
DECL|method|projectStateHelp ()
name|String
name|projectStateHelp
parameter_list|()
function_decl|;
DECL|method|dialogCreateChangeTitle ()
name|String
name|dialogCreateChangeTitle
parameter_list|()
function_decl|;
DECL|method|dialogCreateChangeHeading ()
name|String
name|dialogCreateChangeHeading
parameter_list|()
function_decl|;
DECL|method|newChangeBranchSuggestion ()
name|String
name|newChangeBranchSuggestion
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.documentation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|documentation
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
DECL|interface|DocConstants
specifier|public
interface|interface
name|DocConstants
extends|extends
name|Constants
block|{
DECL|method|keyReloadSearch ()
name|String
name|keyReloadSearch
parameter_list|()
function_decl|;
DECL|method|docItemHelp ()
name|String
name|docItemHelp
parameter_list|()
function_decl|;
DECL|method|docTableColumnTitle ()
name|String
name|docTableColumnTitle
parameter_list|()
function_decl|;
DECL|method|docTableNone ()
name|String
name|docTableNone
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


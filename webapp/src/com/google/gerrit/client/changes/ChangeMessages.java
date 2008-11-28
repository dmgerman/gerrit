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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
DECL|interface|ChangeMessages
specifier|public
interface|interface
name|ChangeMessages
extends|extends
name|Messages
block|{
DECL|method|accountDashboardTitle (String fullName)
name|String
name|accountDashboardTitle
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|changesUploadedBy (String fullName)
name|String
name|changesUploadedBy
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|changesReviewableBy (String fullName)
name|String
name|changesReviewableBy
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


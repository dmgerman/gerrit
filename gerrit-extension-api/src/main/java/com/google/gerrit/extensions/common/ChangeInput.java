begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyInfo
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|extensions
operator|.
name|client
operator|.
name|ChangeStatus
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ChangeInput
specifier|public
class|class
name|ChangeInput
block|{
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
DECL|field|subject
specifier|public
name|String
name|subject
decl_stmt|;
DECL|field|topic
specifier|public
name|String
name|topic
decl_stmt|;
DECL|field|status
specifier|public
name|ChangeStatus
name|status
decl_stmt|;
DECL|field|isPrivate
specifier|public
name|Boolean
name|isPrivate
decl_stmt|;
DECL|field|workInProgress
specifier|public
name|Boolean
name|workInProgress
decl_stmt|;
DECL|field|baseChange
specifier|public
name|String
name|baseChange
decl_stmt|;
DECL|field|newBranch
specifier|public
name|Boolean
name|newBranch
decl_stmt|;
DECL|field|merge
specifier|public
name|MergeInput
name|merge
decl_stmt|;
DECL|method|ChangeInput ()
specifier|public
name|ChangeInput
parameter_list|()
block|{}
comment|/**    * Creates a new {@code ChangeInput} with the minimal attributes required for a successful    * creation of a new change.    *    * @param project the project name for the new change    * @param branch the branch name for the new change    * @param subject the subject (commit message) for the new change    */
DECL|method|ChangeInput (String project, String branch, String subject)
specifier|public
name|ChangeInput
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
comment|/** Who to send email notifications to after change is created. */
DECL|field|notify
specifier|public
name|NotifyHandling
name|notify
init|=
name|NotifyHandling
operator|.
name|ALL
decl_stmt|;
DECL|field|notifyDetails
specifier|public
name|Map
argument_list|<
name|RecipientType
argument_list|,
name|NotifyInfo
argument_list|>
name|notifyDetails
decl_stmt|;
block|}
end_class

end_unit


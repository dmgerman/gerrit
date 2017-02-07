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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
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
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|AccountInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Result object representing the outcome of a request to add a reviewer. */
end_comment

begin_class
DECL|class|AddReviewerResult
specifier|public
class|class
name|AddReviewerResult
block|{
comment|/** The identifier of an account or group that was to be added as a reviewer. */
DECL|field|input
specifier|public
name|String
name|input
decl_stmt|;
comment|/** If non-null, a string describing why the reviewer could not be added. */
DECL|field|error
annotation|@
name|Nullable
specifier|public
name|String
name|error
decl_stmt|;
comment|/**    * Non-null and true if the reviewer cannot be added without explicit confirmation. This may be    * the case for groups of a certain size.    */
DECL|field|confirm
annotation|@
name|Nullable
specifier|public
name|Boolean
name|confirm
decl_stmt|;
comment|/**    * List of individual reviewers added to the change. The size of this list may be greater than one    * (e.g. when a group is added). Null if no reviewers were added.    */
DECL|field|reviewers
annotation|@
name|Nullable
specifier|public
name|List
argument_list|<
name|ReviewerInfo
argument_list|>
name|reviewers
decl_stmt|;
comment|/**    * List of accounts CCed on the change. The size of this list may be greater than one (e.g. when a    * group is CCed). Null if no accounts were CCed or if reviewers is non-null.    */
DECL|field|ccs
annotation|@
name|Nullable
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|ccs
decl_stmt|;
comment|/**    * Constructs a partially initialized result for the given reviewer.    *    * @param input String identifier of an account or group, from user request    */
DECL|method|AddReviewerResult (String input)
specifier|public
name|AddReviewerResult
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|this
operator|.
name|input
operator|=
name|input
expr_stmt|;
block|}
comment|/**    * Constructs an error result for the given account.    *    * @param reviewer String identifier of an account or group    * @param error Error message    */
DECL|method|AddReviewerResult (String reviewer, String error)
specifier|public
name|AddReviewerResult
parameter_list|(
name|String
name|reviewer
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|this
argument_list|(
name|reviewer
argument_list|)
expr_stmt|;
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
comment|/**    * Constructs a needs-confirmation result for the given account.    *    * @param confirm Whether confirmation is needed.    */
DECL|method|AddReviewerResult (String reviewer, boolean confirm)
specifier|public
name|AddReviewerResult
parameter_list|(
name|String
name|reviewer
parameter_list|,
name|boolean
name|confirm
parameter_list|)
block|{
name|this
argument_list|(
name|reviewer
argument_list|)
expr_stmt|;
name|this
operator|.
name|confirm
operator|=
name|confirm
expr_stmt|;
block|}
block|}
end_class

end_unit


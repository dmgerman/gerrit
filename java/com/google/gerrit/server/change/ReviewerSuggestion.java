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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|Change
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
name|entities
operator|.
name|Project
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
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Listener to provide reviewer suggestions.  *  *<p>Invoked by Gerrit when a user clicks "Add Reviewer" on a change.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ReviewerSuggestion
specifier|public
interface|interface
name|ReviewerSuggestion
block|{
comment|/**    * Suggest reviewers to add to a change.    *    * @param project The name key of the project the suggestion is for.    * @param changeId The changeId that the suggestion is for. Can be {@code null}.    * @param query The query as typed by the user. Can be {@code null}.    * @param candidates A set of candidates for the ranking. Can be empty.    * @return Set of {@link SuggestedReviewer}s. The {@link com.google.gerrit.entities.Account.Id}s    *     listed here don't have to be included in {@code candidates}.    */
DECL|method|suggestReviewers ( Project.NameKey project, @Nullable Change.Id changeId, @Nullable String query, Set<Account.Id> candidates)
name|Set
argument_list|<
name|SuggestedReviewer
argument_list|>
name|suggestReviewers
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Nullable
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Nullable
name|String
name|query
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|candidates
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


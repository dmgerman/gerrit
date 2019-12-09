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
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|PatchSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Store for reviewed flags on changes.  *  *<p>A reviewed flag is a tuple of (patch set ID, file, account ID) and records whether the user  * has reviewed a file in a patch set. Each user can easily have thousands of reviewed flags and the  * number of reviewed flags is growing without bound. The store must be able handle this data volume  * efficiently.  *  *<p>For a cluster setups with multiple primary nodes the store must replicate the data between the  * primary servers.  */
end_comment

begin_interface
DECL|interface|AccountPatchReviewStore
specifier|public
interface|interface
name|AccountPatchReviewStore
block|{
comment|/** Represents patch set id with reviewed files. */
annotation|@
name|AutoValue
DECL|class|PatchSetWithReviewedFiles
specifier|abstract
class|class
name|PatchSetWithReviewedFiles
block|{
DECL|method|patchSetId ()
specifier|public
specifier|abstract
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|()
function_decl|;
DECL|method|files ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|files
parameter_list|()
function_decl|;
DECL|method|create (PatchSet.Id id, ImmutableSet<String> files)
specifier|public
specifier|static
name|PatchSetWithReviewedFiles
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|files
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AccountPatchReviewStore_PatchSetWithReviewedFiles
argument_list|(
name|id
argument_list|,
name|files
argument_list|)
return|;
block|}
block|}
comment|/**    * Marks the given file in the given patch set as reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param path file path    * @return {@code true} if the reviewed flag was updated, {@code false} if the reviewed flag was    *     already set    */
DECL|method|markReviewed (PatchSet.Id psId, Account.Id accountId, String path)
name|boolean
name|markReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|path
parameter_list|)
function_decl|;
comment|/**    * Marks the given files in the given patch set as reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param paths file paths    */
DECL|method|markReviewed (PatchSet.Id psId, Account.Id accountId, Collection<String> paths)
name|void
name|markReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|paths
parameter_list|)
function_decl|;
comment|/**    * Clears the reviewed flag for the given file in the given patch set for the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param path file path    */
DECL|method|clearReviewed (PatchSet.Id psId, Account.Id accountId, String path)
name|void
name|clearReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|path
parameter_list|)
function_decl|;
comment|/**    * Clears the reviewed flags for all files in the given patch set for all users.    *    * @param psId patch set ID    */
DECL|method|clearReviewed (PatchSet.Id psId)
name|void
name|clearReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
function_decl|;
comment|/**    * Clears the reviewed flags for all files in all patch sets in the given change for all users.    *    * @param changeId change ID    */
DECL|method|clearReviewed (Change.Id changeId)
name|void
name|clearReviewed
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
function_decl|;
comment|/**    * Find the latest patch set, that is smaller or equals to the given patch set, where at least,    * one file has been reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @return optionally, all files the have been reviewed by the given user that belong to the patch    *     set that is smaller or equals to the given patch set    */
DECL|method|findReviewed (PatchSet.Id psId, Account.Id accountId)
name|Optional
argument_list|<
name|PatchSetWithReviewedFiles
argument_list|>
name|findReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


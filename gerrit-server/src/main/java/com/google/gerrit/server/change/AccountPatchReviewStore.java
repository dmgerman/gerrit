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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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

begin_comment
comment|/**  * Store for reviewed flags on changes.  *  * A reviewed flag is a tuple of (patch set ID, file, account ID) and records  * whether the user has reviewed a file in a patch set. Each user can easily  * have thousands of reviewed flags and the number of reviewed flags is growing  * without bound. The store must be able handle this data volume efficiently.  *  * For a multi-master setup the store must replicate the data between the  * masters.  */
end_comment

begin_interface
DECL|interface|AccountPatchReviewStore
specifier|public
interface|interface
name|AccountPatchReviewStore
block|{
comment|/**    * Marks the given file in the given patch set as reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param path file path    * @return {@code true} if the reviewed flag was updated, {@code false} if the    *         reviewed flag was already set    * @throws OrmException thrown if updating the reviewed flag failed    */
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
throws|throws
name|OrmException
function_decl|;
comment|/**    * Marks the given files in the given patch set as reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param paths file paths    * @throws OrmException thrown if updating the reviewed flag failed    */
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
throws|throws
name|OrmException
function_decl|;
comment|/**    * Clears the reviewed flag for the given file in the given patch set for the    * given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @param path file path    * @throws OrmException thrown if clearing the reviewed flag failed    */
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
throws|throws
name|OrmException
function_decl|;
comment|/**    * Clears the reviewed flags for all files in the given patch set for all    * users.    *    * @param psId patch set ID    * @throws OrmException thrown if clearing the reviewed flags failed    */
DECL|method|clearReviewed (PatchSet.Id psId)
name|void
name|clearReviewed
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/**    * Returns the paths of all files in the given patch set the have been    * reviewed by the given user.    *    * @param psId patch set ID    * @param accountId account ID of the user    * @return the paths of all files in the given patch set the have been    *         reviewed by the given user    * @throws OrmException thrown if accessing the reviewed flags failed    */
DECL|method|findReviewed (PatchSet.Id psId, Account.Id accountId)
name|Collection
argument_list|<
name|String
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
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit


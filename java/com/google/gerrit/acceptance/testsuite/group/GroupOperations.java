begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|group
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
name|AccountGroup
import|;
end_import

begin_comment
comment|/**  * An aggregation of operations on groups for test purposes.  *  *<p>To execute the operations, no Gerrit permissions are necessary.  *  *<p><strong>Note:</strong> This interface is not implemented using the REST or extension API.  * Hence, it cannot be used for testing those APIs.  */
end_comment

begin_interface
DECL|interface|GroupOperations
specifier|public
interface|interface
name|GroupOperations
block|{
comment|/**    * Starts the fluent chain for querying or modifying a group. Please see the methods of {@link    * MoreGroupOperations} for details on possible operations.    *    * @return an aggregation of operations on a specific group    */
DECL|method|group (AccountGroup.UUID groupUuid)
name|MoreGroupOperations
name|group
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
comment|/**    * Starts the fluent chain to create a group. The returned builder can be used to specify the    * attributes of the new group. To create the group for real, {@link    * TestGroupCreation.Builder#create()} must be called.    *    *<p>Example:    *    *<pre>    * AccountGroup.UUID createdGroupUuid = groupOperations    *     .newGroup()    *     .name("verifiers")    *     .description("All verifiers of this server")    *     .create();    *</pre>    *    *<p><strong>Note:</strong> If another group with the provided name already exists, the creation    * of the group will fail.    *    * @return a builder to create the new group    */
DECL|method|newGroup ()
name|TestGroupCreation
operator|.
name|Builder
name|newGroup
parameter_list|()
function_decl|;
comment|/** An aggregation of methods on a specific group. */
DECL|interface|MoreGroupOperations
interface|interface
name|MoreGroupOperations
block|{
comment|/**      * Checks whether the group exists.      *      * @return {@code true} if the group exists      */
DECL|method|exists ()
name|boolean
name|exists
parameter_list|()
function_decl|;
comment|/**      * Retrieves the group.      *      *<p><strong>Note:</strong> This call will fail with an exception if the requested group      * doesn't exist. If you want to check for the existence of a group, use {@link #exists()}      * instead.      *      * @return the corresponding {@code TestGroup}      */
DECL|method|get ()
name|TestGroup
name|get
parameter_list|()
function_decl|;
comment|/**      * Starts the fluent chain to update a group. The returned builder can be used to specify how      * the attributes of the group should be modified. To update the group for real, {@link      * TestGroupUpdate.Builder#update()} must be called.      *      *<p>Example:      *      *<pre>      * groupOperations.forUpdate().description("Another description for this group").update();      *</pre>      *      *<p><strong>Note:</strong> The update will fail with an exception if the group to update      * doesn't exist. If you want to check for the existence of a group, use {@link #exists()}.      *      * @return a builder to update the group      */
DECL|method|forUpdate ()
name|TestGroupUpdate
operator|.
name|Builder
name|forUpdate
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit


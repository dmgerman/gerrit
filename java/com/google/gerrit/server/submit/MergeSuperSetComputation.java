begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
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
name|annotations
operator|.
name|ExtensionPoint
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
name|server
operator|.
name|CurrentUser
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
name|server
operator|.
name|permissions
operator|.
name|PermissionBackendException
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Interface to compute the merge super set to detect changes that should be submitted together.  *  *<p>E.g. to speed up performance implementations could decide to do the computation in batches in  * parallel on different server nodes.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|MergeSuperSetComputation
specifier|public
interface|interface
name|MergeSuperSetComputation
block|{
comment|/**    * Compute the set of changes that should be submitted together. As input a set of changes is    * provided for which it is known that they should be submitted together. This method should    * complete the set by including open predecessor changes that need to be submitted as well. To    * decide whether open predecessor changes should be included the method must take the submit type    * into account (e.g. for changes with submit type "Cherry-Pick" open predecessor changes must not    * be included).    *    *<p>This method is invoked iteratively while new changes to be submitted together are discovered    * by expanding the topics of the changes. This method must not do any topic expansion on its own.    *    * @param orm {@link MergeOpRepoManager} that should be used to access repositories    * @param changeSet A set of changes for which it is known that they should be submitted together    * @param user The user for which the visibility checks should be performed    * @return the completed set of changes that should be submitted together    */
DECL|method|completeWithoutTopic (MergeOpRepoManager orm, ChangeSet changeSet, CurrentUser user)
name|ChangeSet
name|completeWithoutTopic
parameter_list|(
name|MergeOpRepoManager
name|orm
parameter_list|,
name|ChangeSet
name|changeSet
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|PermissionBackendException
function_decl|;
block|}
end_interface

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
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
name|client
operator|.
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_comment
comment|/** Manages replication to other nodes. */
end_comment

begin_interface
DECL|interface|ReplicationQueue
specifier|public
interface|interface
name|ReplicationQueue
block|{
comment|/** Is replication to one or more other destinations configured? */
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**    * Schedule a full replication for a single project.    *<p>    * All remote URLs are checked to verify the are current with regards to the    * local project state. If not, they are updated by pushing new refs, updating    * existing ones which don't match, and deleting stale refs which have been    * removed from the local repository.    *    * @param project identity of the project to replicate.    * @param urlMatch substring that must appear in a URI to support replication.    */
DECL|method|scheduleFullSync (Project.NameKey project, String urlMatch)
name|void
name|scheduleFullSync
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|urlMatch
parameter_list|)
function_decl|;
comment|/**    * Schedule update of a single ref.    *<p>    * This method automatically tries to batch together multiple requests in the    * same project, to take advantage of Git's native ability to update multiple    * refs during a single push operation.    *    * @param project identity of the project to replicate.    * @param ref unique name of the ref; must start with {@code refs/}.    */
DECL|method|scheduleUpdate (Project.NameKey project, String ref)
name|void
name|scheduleUpdate
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
comment|/**    * Create new empty project at the remote sites.    *<p>    * When a new project has been created locally call this method to make sure    * that the project will be created at the remote sites as well.    *    * @param project of the project to be created.    */
DECL|method|replicateNewProject (Project.NameKey project)
name|void
name|replicateNewProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


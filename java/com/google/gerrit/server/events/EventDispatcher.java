begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|entities
operator|.
name|BranchNameKey
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
name|server
operator|.
name|permissions
operator|.
name|PermissionBackendException
import|;
end_import

begin_comment
comment|/** Interface for posting (dispatching) Events */
end_comment

begin_interface
DECL|interface|EventDispatcher
specifier|public
interface|interface
name|EventDispatcher
block|{
comment|/**    * Post a stream event that is related to a change    *    * @param change The change that the event is related to    * @param event The event to post    * @throws PermissionBackendException on failure of permission checks    */
DECL|method|postEvent (Change change, ChangeEvent event)
name|void
name|postEvent
parameter_list|(
name|Change
name|change
parameter_list|,
name|ChangeEvent
name|event
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
comment|/**    * Post a stream event that is related to a branch    *    * @param branchName The branch that the event is related to    * @param event The event to post    * @throws PermissionBackendException on failure of permission checks    */
DECL|method|postEvent (BranchNameKey branchName, RefEvent event)
name|void
name|postEvent
parameter_list|(
name|BranchNameKey
name|branchName
parameter_list|,
name|RefEvent
name|event
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
comment|/**    * Post a stream event that is related to a project.    *    * @param projectName The project that the event is related to.    * @param event The event to post.    */
DECL|method|postEvent (Project.NameKey projectName, ProjectEvent event)
name|void
name|postEvent
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|ProjectEvent
name|event
parameter_list|)
function_decl|;
comment|/**    * Post a stream event generically.    *    *<p>If you are creating a RefEvent or ChangeEvent from scratch, it is more efficient to use the    * specific postEvent methods for those use cases.    *    * @param event The event to post.    * @throws PermissionBackendException on failure of permission checks    */
DECL|method|postEvent (Event event)
name|void
name|postEvent
parameter_list|(
name|Event
name|event
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
block|}
end_interface

end_unit


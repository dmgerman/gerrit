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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|rpc
operator|.
name|SignInRequired
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
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

begin_interface
DECL|interface|ProjectAdminService
specifier|public
interface|interface
name|ProjectAdminService
extends|extends
name|RemoteJsonService
block|{
annotation|@
name|SignInRequired
DECL|method|ownedProjects (AsyncCallback<List<Project>> callback)
name|void
name|ownedProjects
parameter_list|(
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|Project
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|projectDetail (Project.Id projectId, AsyncCallback<ProjectDetail> callback)
name|void
name|projectDetail
parameter_list|(
name|Project
operator|.
name|Id
name|projectId
parameter_list|,
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|changeProjectDescription (Project.Id projectId, String description, AsyncCallback<VoidResult> callback)
name|void
name|changeProjectDescription
parameter_list|(
name|Project
operator|.
name|Id
name|projectId
parameter_list|,
name|String
name|description
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|changeProjectOwner (Project.Id projectId, String newOwnerName, AsyncCallback<VoidResult> callback)
name|void
name|changeProjectOwner
parameter_list|(
name|Project
operator|.
name|Id
name|projectId
parameter_list|,
name|String
name|newOwnerName
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


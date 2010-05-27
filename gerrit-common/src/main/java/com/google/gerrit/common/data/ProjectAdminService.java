begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|auth
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|Branch
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
name|reviewdb
operator|.
name|RefRight
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
name|RpcImpl
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
name|RpcImpl
operator|.
name|Version
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_interface
annotation|@
name|RpcImpl
argument_list|(
name|version
operator|=
name|Version
operator|.
name|V2_0
argument_list|)
DECL|interface|ProjectAdminService
specifier|public
interface|interface
name|ProjectAdminService
extends|extends
name|RemoteJsonService
block|{
DECL|method|visibleProjects (AsyncCallback<List<Project>> callback)
name|void
name|visibleProjects
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
DECL|method|projectDetail (Project.NameKey projectName, AsyncCallback<ProjectDetail> callback)
name|void
name|projectDetail
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
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
DECL|method|changeProjectSettings (Project update, AsyncCallback<ProjectDetail> callback)
name|void
name|changeProjectSettings
parameter_list|(
name|Project
name|update
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
DECL|method|deleteRight (Project.NameKey projectName, Set<RefRight.Key> ids, AsyncCallback<ProjectDetail> callback)
name|void
name|deleteRight
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Set
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
name|ids
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
DECL|method|addRight (Project.NameKey projectName, ApprovalCategory.Id categoryId, String groupName, String refName, short min, short max, AsyncCallback<ProjectDetail> callback)
name|void
name|addRight
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|ApprovalCategory
operator|.
name|Id
name|categoryId
parameter_list|,
name|String
name|groupName
parameter_list|,
name|String
name|refName
parameter_list|,
name|short
name|min
parameter_list|,
name|short
name|max
parameter_list|,
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|listBranches (Project.NameKey projectName, AsyncCallback<ListBranchesResult> callback)
name|void
name|listBranches
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|AsyncCallback
argument_list|<
name|ListBranchesResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|addBranch (Project.NameKey projectName, String branchName, String startingRevision, AsyncCallback<ListBranchesResult> callback)
name|void
name|addBranch
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|branchName
parameter_list|,
name|String
name|startingRevision
parameter_list|,
name|AsyncCallback
argument_list|<
name|ListBranchesResult
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|deleteBranch (Project.NameKey projectName, Set<Branch.NameKey> ids, AsyncCallback<Set<Branch.NameKey>> callback)
name|void
name|deleteBranch
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|ids
parameter_list|,
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


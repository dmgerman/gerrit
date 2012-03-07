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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|Project
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
DECL|interface|SuggestService
specifier|public
interface|interface
name|SuggestService
extends|extends
name|RemoteJsonService
block|{
DECL|method|suggestProjectNameKey (String query, int limit, AsyncCallback<List<Project.NameKey>> callback)
name|void
name|suggestProjectNameKey
parameter_list|(
name|String
name|query
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|suggestAccount (String query, Boolean enabled, int limit, AsyncCallback<List<AccountInfo>> callback)
name|void
name|suggestAccount
parameter_list|(
name|String
name|query
parameter_list|,
name|Boolean
name|enabled
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
DECL|method|suggestAccountGroup (String query, int limit, AsyncCallback<List<GroupReference>> callback)
name|void
name|suggestAccountGroup
parameter_list|(
name|String
name|query
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|GroupReference
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/**    * Suggests reviewers. A reviewer can be a user or a group. Inactive users,    * the system groups {@link AccountGroup#ANONYMOUS_USERS} and    * {@link AccountGroup#REGISTERED_USERS} and groups that have more than the    * configured<code>addReviewer.maxAllowed</code> members are not suggested as    * reviewers.    * @param project the project for which reviewers should be suggested    */
DECL|method|suggestReviewer (Project.NameKey project, String query, int limit, AsyncCallback<List<ReviewerInfo>> callback)
name|void
name|suggestReviewer
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|query
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|ReviewerInfo
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


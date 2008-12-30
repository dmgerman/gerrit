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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGroupMember
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
name|Set
import|;
end_import

begin_interface
DECL|interface|GroupAdminService
specifier|public
interface|interface
name|GroupAdminService
extends|extends
name|RemoteJsonService
block|{
annotation|@
name|SignInRequired
DECL|method|groupDetail (AccountGroup.Id groupId, AsyncCallback<AccountGroupDetail> callback)
name|void
name|groupDetail
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|AsyncCallback
argument_list|<
name|AccountGroupDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|changeGroupDescription (AccountGroup.Id groupId, String description, AsyncCallback<VoidResult> callback)
name|void
name|changeGroupDescription
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
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
DECL|method|renameGroup (AccountGroup.Id groupId, String newName, AsyncCallback<VoidResult> callback)
name|void
name|renameGroup
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|String
name|newName
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
DECL|method|addGroupMember (AccountGroup.Id groupId, String nameOrEmail, AsyncCallback<AccountGroupDetail> callback)
name|void
name|addGroupMember
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|String
name|nameOrEmail
parameter_list|,
name|AsyncCallback
argument_list|<
name|AccountGroupDetail
argument_list|>
name|callback
parameter_list|)
function_decl|;
annotation|@
name|SignInRequired
DECL|method|deleteGroupMembers (Set<AccountGroupMember.Key> keys, AsyncCallback<VoidResult> callback)
name|void
name|deleteGroupMembers
parameter_list|(
name|Set
argument_list|<
name|AccountGroupMember
operator|.
name|Key
argument_list|>
name|keys
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
DECL|method|changeGroupOwner (AccountGroupMember.Key key, boolean owner, AsyncCallback<VoidResult> callback)
name|void
name|changeGroupOwner
parameter_list|(
name|AccountGroupMember
operator|.
name|Key
name|key
parameter_list|,
name|boolean
name|owner
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


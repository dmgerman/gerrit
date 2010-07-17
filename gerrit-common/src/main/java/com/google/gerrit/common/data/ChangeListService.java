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
name|Change
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
name|VoidResult
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
DECL|interface|ChangeListService
specifier|public
interface|interface
name|ChangeListService
extends|extends
name|RemoteJsonService
block|{
comment|/** Get all changes which match an arbitrary query string. */
DECL|method|allQueryPrev (String query, String pos, int limit, AsyncCallback<SingleListChangeInfo> callback)
name|void
name|allQueryPrev
parameter_list|(
name|String
name|query
parameter_list|,
name|String
name|pos
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/** Get all changes which match an arbitrary query string. */
DECL|method|allQueryNext (String query, String pos, int limit, AsyncCallback<SingleListChangeInfo> callback)
name|void
name|allQueryNext
parameter_list|(
name|String
name|query
parameter_list|,
name|String
name|pos
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|SingleListChangeInfo
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/** Get the data to show AccountDashboardScreen for an account. */
DECL|method|forAccount (Account.Id id, AsyncCallback<AccountDashboardInfo> callback)
name|void
name|forAccount
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|AsyncCallback
argument_list|<
name|AccountDashboardInfo
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/** Get the ids of all changes starred by the caller. */
annotation|@
name|SignInRequired
DECL|method|myStarredChangeIds (AsyncCallback<Set<Change.Id>> callback)
name|void
name|myStarredChangeIds
parameter_list|(
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/**    * Add and/or remove changes from the set of starred changes of the caller.    *    * @param req the add and remove cluster.    */
annotation|@
name|SignInRequired
DECL|method|toggleStars (ToggleStarRequest req, AsyncCallback<VoidResult> callback)
name|void
name|toggleStars
parameter_list|(
name|ToggleStarRequest
name|req
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


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
DECL|package|com.google.gerrit.httpd.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|account
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
name|data
operator|.
name|GroupAdminService
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
name|common
operator|.
name|data
operator|.
name|GroupDetail
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
name|httpd
operator|.
name|rpc
operator|.
name|BaseServiceImplementation
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
name|server
operator|.
name|ReviewDb
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
name|IdentifiedUser
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
name|account
operator|.
name|GroupCache
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
name|common
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|GroupAdminServiceImpl
class|class
name|GroupAdminServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|GroupAdminService
block|{
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupDetailFactory
specifier|private
specifier|final
name|GroupDetailHandler
operator|.
name|Factory
name|groupDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupAdminServiceImpl (final Provider<ReviewDb> schema, final Provider<IdentifiedUser> currentUser, final GroupCache groupCache, final GroupDetailHandler.Factory groupDetailFactory)
name|GroupAdminServiceImpl
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupDetailHandler
operator|.
name|Factory
name|groupDetailFactory
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|currentUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupDetailFactory
operator|=
name|groupDetailFactory
expr_stmt|;
block|}
DECL|method|groupDetail (AccountGroup.Id groupId, AccountGroup.UUID groupUUID, AsyncCallback<GroupDetail> callback)
specifier|public
name|void
name|groupDetail
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|,
name|AsyncCallback
argument_list|<
name|GroupDetail
argument_list|>
name|callback
parameter_list|)
block|{
if|if
condition|(
name|groupId
operator|==
literal|null
operator|&&
name|groupUUID
operator|!=
literal|null
condition|)
block|{
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupUUID
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
name|groupId
operator|=
name|g
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
block|}
name|groupDetailFactory
operator|.
name|create
argument_list|(
name|groupId
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|AccountSecurity
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
name|AccountExternalId
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
name|CurrentUser
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

begin_class
DECL|class|AccountSecurityImpl
class|class
name|AccountSecurityImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|AccountSecurity
block|{
DECL|field|deleteExternalIdsFactory
specifier|private
specifier|final
name|DeleteExternalIds
operator|.
name|Factory
name|deleteExternalIdsFactory
decl_stmt|;
DECL|field|externalIdDetailFactory
specifier|private
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountSecurityImpl ( final Provider<ReviewDb> schema, final Provider<CurrentUser> currentUser, final DeleteExternalIds.Factory deleteExternalIdsFactory, final ExternalIdDetailFactory.Factory externalIdDetailFactory)
name|AccountSecurityImpl
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
name|CurrentUser
argument_list|>
name|currentUser
parameter_list|,
specifier|final
name|DeleteExternalIds
operator|.
name|Factory
name|deleteExternalIdsFactory
parameter_list|,
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
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
name|deleteExternalIdsFactory
operator|=
name|deleteExternalIdsFactory
expr_stmt|;
name|this
operator|.
name|externalIdDetailFactory
operator|=
name|externalIdDetailFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|myExternalIds (AsyncCallback<List<AccountExternalId>> callback)
specifier|public
name|void
name|myExternalIds
parameter_list|(
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|externalIdDetailFactory
operator|.
name|create
argument_list|()
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteExternalIds ( final Set<AccountExternalId.Key> keys, final AsyncCallback<Set<AccountExternalId.Key>> callback)
specifier|public
name|void
name|deleteExternalIds
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|keys
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|deleteExternalIdsFactory
operator|.
name|create
argument_list|(
name|keys
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


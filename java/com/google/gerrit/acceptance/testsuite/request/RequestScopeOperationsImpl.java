begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.request
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|request
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|acceptance
operator|.
name|AcceptanceTestRequestScope
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
name|acceptance
operator|.
name|AcceptanceTestRequestScope
operator|.
name|Context
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
name|AccountCache
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
name|AccountState
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
name|Singleton
import|;
end_import

begin_comment
comment|/**  * The implementation of {@code RequestScopeOperations}.  *  *<p>There is only one implementation of {@code RequestScopeOperations}. Nevertheless, we keep the  * separation between interface and implementation to enhance clarity.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|RequestScopeOperationsImpl
specifier|public
class|class
name|RequestScopeOperationsImpl
implements|implements
name|RequestScopeOperations
block|{
DECL|field|atrScope
specifier|private
specifier|final
name|AcceptanceTestRequestScope
name|atrScope
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|RequestScopeOperationsImpl ( AcceptanceTestRequestScope atrScope, AccountCache accountCache, IdentifiedUser.GenericFactory userFactory)
name|RequestScopeOperationsImpl
parameter_list|(
name|AcceptanceTestRequestScope
name|atrScope
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|)
block|{
name|this
operator|.
name|atrScope
operator|=
name|atrScope
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setApiUser (Account.Id accountId)
specifier|public
name|Context
name|setApiUser
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|AccountState
name|accountState
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|requireNonNull
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalArgumentException
argument_list|(
literal|"account does not exist: "
operator|+
name|accountId
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|atrScope
operator|.
name|set
argument_list|(
name|atrScope
operator|.
name|newContext
argument_list|(
literal|null
argument_list|,
name|userFactory
operator|.
name|create
argument_list|(
name|accountState
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


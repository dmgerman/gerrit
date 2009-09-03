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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|AccessPath
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
name|ssh
operator|.
name|SshScopes
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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

begin_class
annotation|@
name|Singleton
DECL|class|SshCurrentUserProvider
class|class
name|SshCurrentUserProvider
implements|implements
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
block|{
DECL|field|factory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|factory
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshCurrentUserProvider (final IdentifiedUser.RequestFactory f)
name|SshCurrentUserProvider
parameter_list|(
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|f
parameter_list|)
block|{
name|factory
operator|=
name|f
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|IdentifiedUser
name|get
parameter_list|()
block|{
specifier|final
name|Context
name|ctx
init|=
name|SshScopes
operator|.
name|getContext
argument_list|()
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|id
init|=
name|ctx
operator|.
name|session
operator|.
name|getAttribute
argument_list|(
name|SshUtil
operator|.
name|CURRENT_ACCOUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"User not yet authenticated"
argument_list|)
throw|;
block|}
return|return
name|factory
operator|.
name|create
argument_list|(
name|AccessPath
operator|.
name|SSH
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit


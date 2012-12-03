begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_comment
comment|/**  * Universal implementation of the AuthBackend that works with the injected  * set of AuthBackends.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|UniversalAuthBackend
specifier|public
specifier|final
class|class
name|UniversalAuthBackend
implements|implements
name|AuthBackend
block|{
DECL|field|authBackends
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AuthBackend
argument_list|>
name|authBackends
decl_stmt|;
annotation|@
name|Inject
DECL|method|UniversalAuthBackend (DynamicSet<AuthBackend> authBackends)
name|UniversalAuthBackend
parameter_list|(
name|DynamicSet
argument_list|<
name|AuthBackend
argument_list|>
name|authBackends
parameter_list|)
block|{
name|this
operator|.
name|authBackends
operator|=
name|authBackends
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|authenticate (final AuthRequest request)
specifier|public
name|AuthUser
name|authenticate
parameter_list|(
specifier|final
name|AuthRequest
name|request
parameter_list|)
throws|throws
name|AuthException
block|{
name|List
argument_list|<
name|AuthUser
argument_list|>
name|authUsers
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AuthException
argument_list|>
name|authExs
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|AuthBackend
name|backend
range|:
name|authBackends
control|)
block|{
try|try
block|{
name|authUsers
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|backend
operator|.
name|authenticate
argument_list|(
name|request
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingCredentialsException
name|ex
parameter_list|)
block|{
comment|// Not handled by this backend.
block|}
catch|catch
parameter_list|(
name|AuthException
name|ex
parameter_list|)
block|{
name|authExs
operator|.
name|add
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Handle the valid responses
if|if
condition|(
name|authUsers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|authUsers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|authUsers
operator|.
name|isEmpty
argument_list|()
operator|&&
name|authExs
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
throw|throw
name|authExs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|authExs
operator|.
name|isEmpty
argument_list|()
operator|&&
name|authUsers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MissingCredentialsException
argument_list|()
throw|;
block|}
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Multiple AuthBackends attempted to handle request:"
operator|+
literal|" authUsers=%s authExs=%s"
argument_list|,
name|authUsers
argument_list|,
name|authExs
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|AuthException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"UniversalAuthBackend doesn't support domain."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit


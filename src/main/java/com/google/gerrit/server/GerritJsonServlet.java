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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|rpc
operator|.
name|NotSignedInException
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
name|gson
operator|.
name|GsonBuilder
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
name|server
operator|.
name|JsonServlet
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
name|server
operator|.
name|SignedToken
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
name|Injector
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/**  * Base JSON servlet to ensure the current user is not forged.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|GerritJsonServlet
specifier|public
specifier|final
class|class
name|GerritJsonServlet
extends|extends
name|JsonServlet
argument_list|<
name|GerritCall
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getCurrentCall ()
specifier|public
specifier|static
specifier|final
name|GerritCall
name|getCurrentCall
parameter_list|()
block|{
return|return
name|JsonServlet
operator|.
expr|<
name|GerritCall
operator|>
name|getCurrentCall
argument_list|()
return|;
block|}
DECL|field|callFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|GerritCall
argument_list|>
name|callFactory
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|service
specifier|private
specifier|final
name|RemoteJsonService
name|service
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritJsonServlet (final Injector i, final GerritServer gs, final RemoteJsonService s)
name|GerritJsonServlet
parameter_list|(
specifier|final
name|Injector
name|i
parameter_list|,
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|RemoteJsonService
name|s
parameter_list|)
block|{
name|callFactory
operator|=
name|i
operator|.
name|getProvider
argument_list|(
name|GerritCall
operator|.
name|class
argument_list|)
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
name|service
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createXsrfSignedToken ()
specifier|protected
name|SignedToken
name|createXsrfSignedToken
parameter_list|()
block|{
return|return
name|server
operator|.
name|getXsrfToken
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createActiveCall (final HttpServletRequest req, final HttpServletResponse resp)
specifier|protected
name|GerritCall
name|createActiveCall
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|)
block|{
return|return
name|callFactory
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createGsonBuilder ()
specifier|protected
name|GsonBuilder
name|createGsonBuilder
parameter_list|()
block|{
specifier|final
name|GsonBuilder
name|g
init|=
name|super
operator|.
name|createGsonBuilder
argument_list|()
decl_stmt|;
name|g
operator|.
name|registerTypeAdapter
argument_list|(
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
operator|.
name|class
argument_list|,
operator|new
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|diff
operator|.
name|EditDeserializer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|g
return|;
block|}
annotation|@
name|Override
DECL|method|preInvoke (final GerritCall call)
specifier|protected
name|void
name|preInvoke
parameter_list|(
specifier|final
name|GerritCall
name|call
parameter_list|)
block|{
name|super
operator|.
name|preInvoke
argument_list|(
name|call
argument_list|)
expr_stmt|;
if|if
condition|(
name|call
operator|.
name|isComplete
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|call
operator|.
name|getMethod
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|SignInRequired
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// If SignInRequired is set on this method we must have both a
comment|// valid XSRF token *and* have the user signed in. Doing these
comment|// checks also validates that they agree on the user identity.
comment|//
if|if
condition|(
operator|!
name|call
operator|.
name|requireXsrfValid
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|call
operator|.
name|getAccountId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|call
operator|.
name|onFailure
argument_list|(
operator|new
name|NotSignedInException
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createServiceHandle ()
specifier|protected
name|Object
name|createServiceHandle
parameter_list|()
block|{
return|return
name|service
return|;
block|}
block|}
end_class

end_unit


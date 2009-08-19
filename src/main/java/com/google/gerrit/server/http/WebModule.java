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
DECL|package|com.google.gerrit.server.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|http
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Scopes
operator|.
name|SINGLETON
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
name|data
operator|.
name|GerritConfig
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
name|AuthType
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
name|RemotePeer
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
name|AccountManager
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
name|config
operator|.
name|AuthConfig
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
name|config
operator|.
name|FactoryModule
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
name|config
operator|.
name|GerritRequestModule
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
name|contact
operator|.
name|ContactStore
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
name|contact
operator|.
name|ContactStoreProvider
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
name|openid
operator|.
name|OpenIdModule
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
name|rpc
operator|.
name|UiRpcModule
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
name|SshInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|server
operator|.
name|CacheControlFilter
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
name|Key
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
name|servlet
operator|.
name|RequestScoped
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
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_class
DECL|class|WebModule
class|class
name|WebModule
extends|extends
name|FactoryModule
block|{
DECL|field|sshInfoProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|SshInfo
argument_list|>
name|sshInfoProvider
decl_stmt|;
DECL|field|loginType
specifier|private
specifier|final
name|AuthType
name|loginType
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebModule (final Provider<SshInfo> sshInfoProvider, final AuthConfig authConfig)
name|WebModule
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|SshInfo
argument_list|>
name|sshInfoProvider
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
argument_list|(
name|sshInfoProvider
argument_list|,
name|authConfig
operator|.
name|getLoginType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|WebModule (final Provider<SshInfo> sshInfoProvider, final AuthType loginType)
name|WebModule
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|SshInfo
argument_list|>
name|sshInfoProvider
parameter_list|,
specifier|final
name|AuthType
name|loginType
parameter_list|)
block|{
name|this
operator|.
name|sshInfoProvider
operator|=
name|sshInfoProvider
expr_stmt|;
name|this
operator|.
name|loginType
operator|=
name|loginType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|RequestCleanupFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|UrlRewriteFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|loginType
condition|)
block|{
case|case
name|OPENID
case|:
name|install
argument_list|(
operator|new
name|OpenIdModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|HTTP
case|:
case|case
name|HTTP_LDAP
case|:
name|install
argument_list|(
operator|new
name|HttpAuthModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
case|:
name|install
argument_list|(
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|serve
argument_list|(
literal|"/become"
argument_list|)
operator|.
name|with
argument_list|(
name|BecomeAnyAccountLoginServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Unsupported loginType: "
operator|+
name|loginType
argument_list|)
throw|;
block|}
name|install
argument_list|(
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|CacheControlFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|CacheControlFilter
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/"
argument_list|)
operator|.
name|with
argument_list|(
name|HostPageServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/Gerrit"
argument_list|)
operator|.
name|with
argument_list|(
name|LegacyGerritServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/cat/*"
argument_list|)
operator|.
name|with
argument_list|(
name|CatServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/logout"
argument_list|)
operator|.
name|with
argument_list|(
name|HttpLogoutServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/prettify/*"
argument_list|)
operator|.
name|with
argument_list|(
name|PrettifyServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/signout"
argument_list|)
operator|.
name|with
argument_list|(
name|HttpLogoutServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/ssh_info"
argument_list|)
operator|.
name|with
argument_list|(
name|SshServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/static/*"
argument_list|)
operator|.
name|with
argument_list|(
name|StaticServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|UiRpcModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GerritRequestModule
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshInfo
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|sshInfoProvider
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ContactStore
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ContactStoreProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GerritConfig
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|GerritConfigProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AccountManager
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|RemotePeer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|HttpRemotePeerProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|WebSession
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CurrentUser
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|HttpCurrentUserProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|HttpIdentifiedUserProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|common
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
name|httpd
operator|.
name|auth
operator|.
name|become
operator|.
name|BecomeAnyAccountLoginServlet
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
name|auth
operator|.
name|container
operator|.
name|HttpAuthModule
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
name|auth
operator|.
name|container
operator|.
name|HttpsClientSslCertModule
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
name|auth
operator|.
name|ldap
operator|.
name|LdapAuthModule
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
name|gitweb
operator|.
name|GitWebModule
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
name|account
operator|.
name|ChangeUserName
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
name|ClearPassword
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
name|GeneratePassword
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
name|CanonicalWebUrl
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
name|inject
operator|.
name|AbstractModule
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_class
DECL|class|WebModule
specifier|public
class|class
name|WebModule
extends|extends
name|FactoryModule
block|{
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|wantSSL
specifier|private
specifier|final
name|boolean
name|wantSSL
decl_stmt|;
DECL|field|gitWebConfig
specifier|private
specifier|final
name|GitWebConfig
name|gitWebConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebModule (final AuthConfig authConfig, @CanonicalWebUrl @Nullable final String canonicalUrl, final Injector creatingInjector)
name|WebModule
parameter_list|(
specifier|final
name|AuthConfig
name|authConfig
parameter_list|,
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
specifier|final
name|String
name|canonicalUrl
parameter_list|,
specifier|final
name|Injector
name|creatingInjector
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|wantSSL
operator|=
name|canonicalUrl
operator|!=
literal|null
operator|&&
name|canonicalUrl
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
expr_stmt|;
name|this
operator|.
name|gitWebConfig
operator|=
name|creatingInjector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|GitWebConfig
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|getInstance
argument_list|(
name|GitWebConfig
operator|.
name|class
argument_list|)
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
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|wantSSL
condition|)
block|{
name|install
argument_list|(
operator|new
name|RequireSslFilter
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
condition|)
block|{
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
name|CLIENT_SSL_CERT_LDAP
case|:
name|install
argument_list|(
operator|new
name|HttpsClientSslCertModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|LDAP
case|:
case|case
name|LDAP_BIND
case|:
name|install
argument_list|(
operator|new
name|LdapAuthModule
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
case|case
name|OPENID
case|:
comment|// OpenID support is bound in WebAppInitializer and Daemon.
case|case
name|CUSTOM_EXTENSION
case|:
break|break;
default|default:
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Unsupported loginType: "
operator|+
name|authConfig
operator|.
name|getAuthType
argument_list|()
argument_list|)
throw|;
block|}
name|install
argument_list|(
operator|new
name|UrlModule
argument_list|()
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
name|install
argument_list|(
operator|new
name|GitOverHttpServlet
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GitWebConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|gitWebConfig
argument_list|)
expr_stmt|;
if|if
condition|(
name|gitWebConfig
operator|.
name|getGitwebCGI
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|install
argument_list|(
operator|new
name|GitWebModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|GerritConfigProvider
operator|.
name|class
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
expr_stmt|;
name|bind
argument_list|(
name|AccountManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ChangeUserName
operator|.
name|CurrentUser
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeUserName
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ClearPassword
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|GeneratePassword
operator|.
name|Factory
operator|.
name|class
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
expr_stmt|;
block|}
block|}
end_class

end_unit


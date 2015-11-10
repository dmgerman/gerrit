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
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|PrivateInternals_DynamicTypes
operator|.
name|registerInParentInjectors
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
operator|.
name|WebUiPlugin
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
name|BecomeAnyAccountModule
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
name|GitwebModule
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|config
operator|.
name|GitwebCgiConfig
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
name|git
operator|.
name|AsyncReceiveCommits
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
name|util
operator|.
name|GuiceRequestScopePropagator
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
name|util
operator|.
name|RequestScopePropagator
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
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_class
DECL|class|WebModule
specifier|public
class|class
name|WebModule
extends|extends
name|LifecycleModule
block|{
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|gitwebCgiConfig
specifier|private
specifier|final
name|GitwebCgiConfig
name|gitwebCgiConfig
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|GerritOptions
name|options
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebModule (AuthConfig authConfig, GerritOptions options, GitwebCgiConfig gitwebCgiConfig)
name|WebModule
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|GerritOptions
name|options
parameter_list|,
name|GitwebCgiConfig
name|gitwebCgiConfig
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
name|options
operator|=
name|options
expr_stmt|;
name|this
operator|.
name|gitwebCgiConfig
operator|=
name|gitwebCgiConfig
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
name|bind
argument_list|(
name|RequestScopePropagator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GuiceRequestScopePropagator
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|HttpRequestContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|RunAsFilter
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|installAuthModule
argument_list|()
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|enableMasterFeatures
argument_list|()
condition|)
block|{
name|install
argument_list|(
operator|new
name|UrlModule
argument_list|(
name|options
argument_list|,
name|authConfig
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|UiRpcModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
argument_list|(
name|options
operator|.
name|enableMasterFeatures
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|gitwebCgiConfig
operator|.
name|getGitwebCgi
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|install
argument_list|(
operator|new
name|GitwebModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|WebUiPlugin
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|AsyncReceiveCommits
operator|.
name|Module
argument_list|()
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
name|ProxyProperties
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ProxyPropertiesProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|toInstance
argument_list|(
name|registerInParentInjectors
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|installAuthModule ()
specifier|private
name|void
name|installAuthModule
parameter_list|()
block|{
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
argument_list|(
name|authConfig
argument_list|)
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
name|BecomeAnyAccountModule
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|OAUTH
case|:
comment|// OAuth support is bound in WebAppInitializer and Daemon.
case|case
name|OPENID
case|:
case|case
name|OPENID_SSO
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
block|}
block|}
end_class

end_unit


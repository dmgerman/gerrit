begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.auth.openid
package|package
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
name|openid
package|;
end_package

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
name|Iterables
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
name|auth
operator|.
name|oauth
operator|.
name|OAuthServiceProvider
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
name|DynamicMap
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
comment|/** OAuth web filter uses active OAuth session to perform OAuth requests */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|OAuthWebFilterOverOpenID
class|class
name|OAuthWebFilterOverOpenID
implements|implements
name|Filter
block|{
DECL|field|GERRIT_LOGIN
specifier|static
specifier|final
name|String
name|GERRIT_LOGIN
init|=
literal|"/login"
decl_stmt|;
DECL|field|oauthSessionProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|OAuthSessionOverOpenID
argument_list|>
name|oauthSessionProvider
decl_stmt|;
DECL|field|oauthServiceProviders
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|OAuthServiceProvider
argument_list|>
name|oauthServiceProviders
decl_stmt|;
DECL|field|ssoProvider
specifier|private
name|OAuthServiceProvider
name|ssoProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|OAuthWebFilterOverOpenID ( DynamicMap<OAuthServiceProvider> oauthServiceProviders, Provider<OAuthSessionOverOpenID> oauthSessionProvider)
name|OAuthWebFilterOverOpenID
parameter_list|(
name|DynamicMap
argument_list|<
name|OAuthServiceProvider
argument_list|>
name|oauthServiceProviders
parameter_list|,
name|Provider
argument_list|<
name|OAuthSessionOverOpenID
argument_list|>
name|oauthSessionProvider
parameter_list|)
block|{
name|this
operator|.
name|oauthServiceProviders
operator|=
name|oauthServiceProviders
expr_stmt|;
name|this
operator|.
name|oauthSessionProvider
operator|=
name|oauthSessionProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig filterConfig)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|filterConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|pickSSOServiceProvider
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletRequest
name|httpRequest
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|HttpServletResponse
name|httpResponse
init|=
operator|(
name|HttpServletResponse
operator|)
name|response
decl_stmt|;
name|OAuthSessionOverOpenID
name|oauthSession
init|=
name|oauthSessionProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|OAuthServiceProvider
name|service
init|=
name|ssoProvider
operator|==
literal|null
condition|?
name|oauthSession
operator|.
name|getServiceProvider
argument_list|()
else|:
name|ssoProvider
decl_stmt|;
if|if
condition|(
name|isGerritLogin
argument_list|(
name|httpRequest
argument_list|)
operator|||
name|oauthSession
operator|.
name|isOAuthFinal
argument_list|(
name|httpRequest
argument_list|)
condition|)
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"service is unknown"
argument_list|)
throw|;
block|}
name|oauthSession
operator|.
name|setServiceProvider
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|oauthSession
operator|.
name|login
argument_list|(
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|httpRequest
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|pickSSOServiceProvider ()
specifier|private
name|void
name|pickSSOServiceProvider
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|plugins
init|=
name|oauthServiceProviders
operator|.
name|plugins
argument_list|()
decl_stmt|;
if|if
condition|(
name|plugins
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|services
init|=
name|oauthServiceProviders
operator|.
name|byPlugin
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|plugins
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|ssoProvider
operator|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|services
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|isGerritLogin (HttpServletRequest request)
specifier|private
specifier|static
name|boolean
name|isGerritLogin
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|getRequestURI
argument_list|()
operator|.
name|contains
argument_list|(
name|GERRIT_LOGIN
argument_list|)
return|;
block|}
block|}
end_class

end_unit


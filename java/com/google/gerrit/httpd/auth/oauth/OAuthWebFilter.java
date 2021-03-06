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
DECL|package|com.google.gerrit.httpd.auth.oauth
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
name|oauth
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|base
operator|.
name|MoreObjects
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
name|base
operator|.
name|Strings
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
name|common
operator|.
name|Nullable
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
name|gerrit
operator|.
name|httpd
operator|.
name|HtmlDomUtil
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
name|LoginUrlToken
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
name|template
operator|.
name|SiteHeaderFooter
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
name|Map
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
name|ServletOutputStream
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_class
annotation|@
name|Singleton
comment|/* OAuth web filter uses active OAuth session to perform OAuth requests */
DECL|class|OAuthWebFilter
class|class
name|OAuthWebFilter
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
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
DECL|field|oauthSessionProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|OAuthSession
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
DECL|field|header
specifier|private
specifier|final
name|SiteHeaderFooter
name|header
decl_stmt|;
DECL|field|ssoProvider
specifier|private
name|OAuthServiceProvider
name|ssoProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|OAuthWebFilter ( @anonicalWebUrl @ullable Provider<String> urlProvider, DynamicMap<OAuthServiceProvider> oauthServiceProviders, Provider<OAuthSession> oauthSessionProvider, SiteHeaderFooter header)
name|OAuthWebFilter
parameter_list|(
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|,
name|DynamicMap
argument_list|<
name|OAuthServiceProvider
argument_list|>
name|oauthServiceProviders
parameter_list|,
name|Provider
argument_list|<
name|OAuthSession
argument_list|>
name|oauthSessionProvider
parameter_list|,
name|SiteHeaderFooter
name|header
parameter_list|)
block|{
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
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
name|this
operator|.
name|header
operator|=
name|header
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
name|OAuthSession
name|oauthSession
init|=
name|oauthSessionProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"link"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|oauthSession
operator|.
name|setLinkMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|oauthSession
operator|.
name|setServiceProvider
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
name|provider
init|=
name|httpRequest
operator|.
name|getParameter
argument_list|(
literal|"provider"
argument_list|)
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
operator|&&
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|provider
argument_list|)
condition|)
block|{
name|selectProvider
argument_list|(
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|findService
argument_list|(
name|provider
argument_list|)
expr_stmt|;
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
DECL|method|findService (String providerId)
specifier|private
name|OAuthServiceProvider
name|findService
parameter_list|(
name|String
name|providerId
parameter_list|)
throws|throws
name|ServletException
block|{
name|Set
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
for|for
control|(
name|String
name|pluginName
range|:
name|plugins
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|m
init|=
name|oauthServiceProviders
operator|.
name|byPlugin
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|e
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|providerId
operator|.
name|equals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s_%s"
argument_list|,
name|pluginName
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"No provider found for: "
operator|+
name|providerId
argument_list|)
throw|;
block|}
DECL|method|selectProvider ( HttpServletRequest req, HttpServletResponse res, @Nullable String errorMessage)
specifier|private
name|void
name|selectProvider
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|self
init|=
name|req
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
name|String
name|cancel
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|urlProvider
operator|!=
literal|null
condition|?
name|urlProvider
operator|.
name|get
argument_list|()
else|:
literal|"/"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|cancel
operator|+=
name|LoginUrlToken
operator|.
name|getToken
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|header
operator|.
name|parse
argument_list|(
name|OAuthWebFilter
operator|.
name|class
argument_list|,
literal|"LoginForm.html"
argument_list|)
decl_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"hostName"
argument_list|)
operator|.
name|setTextContent
argument_list|(
name|req
operator|.
name|getServerName
argument_list|()
argument_list|)
expr_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"login_form"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"action"
argument_list|,
name|self
argument_list|)
expr_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"cancel_link"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|cancel
argument_list|)
expr_stmt|;
name|Element
name|emsg
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"error_message"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|errorMessage
argument_list|)
condition|)
block|{
name|emsg
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|emsg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|emsg
operator|.
name|setTextContent
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
block|}
name|Element
name|providers
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"providers"
argument_list|)
decl_stmt|;
name|Set
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
for|for
control|(
name|String
name|pluginName
range|:
name|plugins
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|m
init|=
name|oauthServiceProviders
operator|.
name|byPlugin
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|e
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|addProvider
argument_list|(
name|providers
argument_list|,
name|pluginName
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|sendHtml
argument_list|(
name|res
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
DECL|method|addProvider (Element form, String pluginName, String id, String serviceName)
specifier|private
specifier|static
name|void
name|addProvider
parameter_list|(
name|Element
name|form
parameter_list|,
name|String
name|pluginName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|serviceName
parameter_list|)
block|{
name|Element
name|div
init|=
name|form
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"div"
argument_list|)
decl_stmt|;
name|div
operator|.
name|setAttribute
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Element
name|hyperlink
init|=
name|form
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|hyperlink
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"?provider=%s_%s"
argument_list|,
name|pluginName
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|hyperlink
operator|.
name|setTextContent
argument_list|(
name|serviceName
operator|+
literal|" ("
operator|+
name|pluginName
operator|+
literal|" plugin)"
argument_list|)
expr_stmt|;
name|div
operator|.
name|appendChild
argument_list|(
name|hyperlink
argument_list|)
expr_stmt|;
name|form
operator|.
name|appendChild
argument_list|(
name|div
argument_list|)
expr_stmt|;
block|}
DECL|method|sendHtml (HttpServletResponse res, Document doc)
specifier|private
specifier|static
name|void
name|sendHtml
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bin
init|=
name|HtmlDomUtil
operator|.
name|toUTF8
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|res
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCharacterEncoding
argument_list|(
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentLength
argument_list|(
name|bin
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
init|(
name|ServletOutputStream
name|out
init|=
name|res
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|out
operator|.
name|write
argument_list|(
name|bin
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|pickSSOServiceProvider ()
specifier|private
name|void
name|pickSSOServiceProvider
parameter_list|()
throws|throws
name|ServletException
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
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"OAuth service provider wasn't installed"
argument_list|)
throw|;
block|}
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


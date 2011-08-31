begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
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
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponseWrapper
import|;
end_import

begin_comment
comment|/**  * Trust the authentication which is done by the container.  *<p>  * Check whether the container has already authenticated the user. If yes, then  * lookup the account and set the account ID in our current session.  *<p>  * This filter should only be configured to run, when authentication is  * configured to trust container authentication. This filter is intended only to  * protect the {@link ProjectServlet} and its handled URLs, which provide remote  * repository access over HTTP.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ContainerAuthFilter
class|class
name|ContainerAuthFilter
implements|implements
name|Filter
block|{
DECL|field|REALM_NAME
specifier|public
specifier|static
specifier|final
name|String
name|REALM_NAME
init|=
literal|"Gerrit Code Review"
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ContainerAuthFilter (Provider<WebSession> session, AccountCache accountCache)
name|ContainerAuthFilter
parameter_list|(
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|)
throws|throws
name|XsrfException
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
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
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|HttpServletResponseWrapper
name|rsp
init|=
operator|new
name|HttpServletResponseWrapper
argument_list|(
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|verify
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|verify (HttpServletRequest req, HttpServletResponseWrapper rsp)
specifier|private
name|boolean
name|verify
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponseWrapper
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|username
init|=
name|req
operator|.
name|getRemoteUser
argument_list|()
decl_stmt|;
specifier|final
name|AccountState
name|who
init|=
operator|(
name|username
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|accountCache
operator|.
name|getByUsername
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|who
operator|==
literal|null
operator|||
operator|!
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|session
operator|.
name|get
argument_list|()
operator|.
name|setUserAccountId
argument_list|(
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


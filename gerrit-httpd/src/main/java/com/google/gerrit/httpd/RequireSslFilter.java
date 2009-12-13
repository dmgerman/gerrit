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
name|Nullable
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

begin_comment
comment|/** Requires the connection to use SSL, redirects if not. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|RequireSslFilter
class|class
name|RequireSslFilter
implements|implements
name|Filter
block|{
DECL|class|Module
specifier|static
class|class
name|Module
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
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
name|RequireSslFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|RequireSslFilter (@anonicalWebUrl @ullable final Provider<String> urlProvider)
name|RequireSslFilter
parameter_list|(
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|)
block|{
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
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
DECL|method|doFilter (final ServletRequest request, final ServletResponse response, final FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
specifier|final
name|ServletRequest
name|request
parameter_list|,
specifier|final
name|ServletResponse
name|response
parameter_list|,
specifier|final
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
specifier|final
name|HttpServletResponse
name|rsp
init|=
operator|(
name|HttpServletResponse
operator|)
name|response
decl_stmt|;
if|if
condition|(
name|isSecure
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// If we wanted SSL, but the user didn't come to us over it,
comment|// force SSL by issuing a protocol redirect. Try to keep the
comment|// name "localhost" in case this is an SSH port tunnel.
comment|//
specifier|final
name|String
name|url
decl_stmt|;
if|if
condition|(
name|isLocalHost
argument_list|(
name|req
argument_list|)
condition|)
block|{
specifier|final
name|StringBuffer
name|b
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
decl_stmt|;
name|b
operator|.
name|replace
argument_list|(
literal|0
argument_list|,
name|b
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|url
operator|=
name|b
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
name|urlProvider
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|rsp
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_MOVED_PERMANENTLY
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Location"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isSecure (final HttpServletRequest req)
specifier|private
specifier|static
name|boolean
name|isSecure
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
literal|"https"
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|||
name|req
operator|.
name|isSecure
argument_list|()
return|;
block|}
DECL|method|isLocalHost (final HttpServletRequest req)
specifier|private
specifier|static
name|boolean
name|isLocalHost
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
literal|"localhost"
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getServerName
argument_list|()
argument_list|)
operator|||
literal|"127.0.0.1"
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getServerName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


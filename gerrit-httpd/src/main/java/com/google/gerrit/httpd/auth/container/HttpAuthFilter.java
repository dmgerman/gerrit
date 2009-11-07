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
DECL|package|com.google.gerrit.httpd.auth.container
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
name|container
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
name|WebSession
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
name|raw
operator|.
name|HostPageServlet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|server
operator|.
name|rpc
operator|.
name|RPCServletUtils
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
name|FileNotFoundException
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
name|io
operator|.
name|OutputStream
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
name|ServletContext
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
comment|/**  * Watches request for the host page and requires login if not yet signed in.  *<p>  * If HTTP authentication has been enabled on this server this filter is bound  * in front of the {@link HostPageServlet} and redirects users who are not yet  * signed in to visit {@code /login/}, so the web container can force login.  * This redirect is performed with JavaScript, such that any existing anchor  * token in the URL can be rewritten and preserved through the authentication  * process of any enterprise single sign-on solutions.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|HttpAuthFilter
class|class
name|HttpAuthFilter
implements|implements
name|Filter
block|{
DECL|field|webSession
specifier|private
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|webSession
decl_stmt|;
DECL|field|signInRaw
specifier|private
specifier|final
name|byte
index|[]
name|signInRaw
decl_stmt|;
DECL|field|signInGzip
specifier|private
specifier|final
name|byte
index|[]
name|signInGzip
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpAuthFilter (final Provider<WebSession> webSession, final ServletContext servletContext)
name|HttpAuthFilter
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|webSession
parameter_list|,
specifier|final
name|ServletContext
name|servletContext
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|webSession
operator|=
name|webSession
expr_stmt|;
specifier|final
name|String
name|pageName
init|=
literal|"LoginRedirect.html"
decl_stmt|;
specifier|final
name|String
name|doc
init|=
name|HtmlDomUtil
operator|.
name|readFile
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|pageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"No "
operator|+
name|pageName
operator|+
literal|" in webapp"
argument_list|)
throw|;
block|}
name|signInRaw
operator|=
name|doc
operator|.
name|getBytes
argument_list|(
name|HtmlDomUtil
operator|.
name|ENC
argument_list|)
expr_stmt|;
name|signInGzip
operator|=
name|HtmlDomUtil
operator|.
name|compress
argument_list|(
name|signInRaw
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|webSession
operator|.
name|get
argument_list|()
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
comment|// Not signed in yet. Since the browser state might have an anchor
comment|// token which we want to capture and carry through the auth process
comment|// we send back JavaScript now to capture that, and do the real work
comment|// of redirecting to the authentication area.
comment|//
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
specifier|final
name|byte
index|[]
name|tosend
decl_stmt|;
if|if
condition|(
name|RPCServletUtils
operator|.
name|acceptsGzipEncoding
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Content-Encoding"
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|tosend
operator|=
name|signInGzip
expr_stmt|;
block|}
else|else
block|{
name|tosend
operator|=
name|signInRaw
expr_stmt|;
block|}
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Expires"
argument_list|,
literal|"Fri, 01 Jan 1980 00:00:00 GMT"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache, must-revalidate"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
name|HtmlDomUtil
operator|.
name|ENC
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentLength
argument_list|(
name|tosend
operator|.
name|length
argument_list|)
expr_stmt|;
specifier|final
name|OutputStream
name|out
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|tosend
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Already signed in, forward the request.
comment|//
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
block|}
annotation|@
name|Override
DECL|method|init (final FilterConfig filterConfig)
specifier|public
name|void
name|init
parameter_list|(
specifier|final
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
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2011 The Android Open Source Project
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
name|common
operator|.
name|PageLinks
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
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
name|HttpServlet
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
comment|/**  * Servlet bound to {@code /login/*} to redirect after client SSL certificate  * login.  *<p>  * When using client SSL certificate one should normally never see the sign in  * dialog. However, this will happen if users session gets invalidated in some  * way. Like in other authentication types, we need to force page to fully  * reload in order to initialize a new session and create a valid xsrfKey.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|HttpsClientSslCertLoginServlet
specifier|public
class|class
name|HttpsClientSslCertLoginServlet
extends|extends
name|HttpServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
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
annotation|@
name|Inject
DECL|method|HttpsClientSslCertLoginServlet (@anonicalWebUrl @ullable final Provider<String> urlProvider)
specifier|public
name|HttpsClientSslCertLoginServlet
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
DECL|method|doGet (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|StringBuilder
name|rdr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|rdr
operator|.
name|append
argument_list|(
name|urlProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|rdr
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|rdr
operator|.
name|append
argument_list|(
name|getToken
argument_list|(
name|req
argument_list|)
argument_list|)
expr_stmt|;
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
name|sendRedirect
argument_list|(
name|rdr
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getToken (final HttpServletRequest req)
specifier|private
name|String
name|getToken
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|String
name|token
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
name|token
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|token
operator|=
name|token
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|==
literal|null
operator|||
name|token
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|token
operator|=
name|PageLinks
operator|.
name|MINE
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit


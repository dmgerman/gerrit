begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
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
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_OK
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
name|ImmutableMap
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
name|io
operator|.
name|Resources
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
name|api
operator|.
name|GerritApi
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
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|SoyFileSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|data
operator|.
name|SanitizedContent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|data
operator|.
name|UnsafeSanitizedContentOrdainer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|jbcsrc
operator|.
name|api
operator|.
name|SoySauce
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
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|function
operator|.
name|Function
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

begin_class
DECL|class|IndexServlet
specifier|public
class|class
name|IndexServlet
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
DECL|field|canonicalUrl
annotation|@
name|Nullable
specifier|private
specifier|final
name|String
name|canonicalUrl
decl_stmt|;
DECL|field|cdnPath
annotation|@
name|Nullable
specifier|private
specifier|final
name|String
name|cdnPath
decl_stmt|;
DECL|field|faviconPath
annotation|@
name|Nullable
specifier|private
specifier|final
name|String
name|faviconPath
decl_stmt|;
DECL|field|gerritApi
specifier|private
specifier|final
name|GerritApi
name|gerritApi
decl_stmt|;
DECL|field|soySauce
specifier|private
specifier|final
name|SoySauce
name|soySauce
decl_stmt|;
DECL|field|urlOrdainer
specifier|private
specifier|final
name|Function
argument_list|<
name|String
argument_list|,
name|SanitizedContent
argument_list|>
name|urlOrdainer
decl_stmt|;
DECL|method|IndexServlet ( @ullable String canonicalUrl, @Nullable String cdnPath, @Nullable String faviconPath, GerritApi gerritApi)
name|IndexServlet
parameter_list|(
annotation|@
name|Nullable
name|String
name|canonicalUrl
parameter_list|,
annotation|@
name|Nullable
name|String
name|cdnPath
parameter_list|,
annotation|@
name|Nullable
name|String
name|faviconPath
parameter_list|,
name|GerritApi
name|gerritApi
parameter_list|)
block|{
name|this
operator|.
name|canonicalUrl
operator|=
name|canonicalUrl
expr_stmt|;
name|this
operator|.
name|cdnPath
operator|=
name|cdnPath
expr_stmt|;
name|this
operator|.
name|faviconPath
operator|=
name|faviconPath
expr_stmt|;
name|this
operator|.
name|gerritApi
operator|=
name|gerritApi
expr_stmt|;
name|this
operator|.
name|soySauce
operator|=
name|SoyFileSet
operator|.
name|builder
argument_list|()
operator|.
name|add
argument_list|(
name|Resources
operator|.
name|getResource
argument_list|(
literal|"com/google/gerrit/httpd/raw/PolyGerritIndexHtml.soy"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|compileTemplates
argument_list|()
expr_stmt|;
name|this
operator|.
name|urlOrdainer
operator|=
parameter_list|(
name|s
parameter_list|)
lambda|->
name|UnsafeSanitizedContentOrdainer
operator|.
name|ordainAsSafe
argument_list|(
name|s
argument_list|,
name|SanitizedContent
operator|.
name|ContentKind
operator|.
name|TRUSTED_RESOURCE_URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|SoySauce
operator|.
name|Renderer
name|renderer
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|parameterMap
init|=
name|req
operator|.
name|getParameterMap
argument_list|()
decl_stmt|;
comment|// TODO(hiesel): Remove URL ordainer as parameter once Soy is consistent
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templateData
init|=
name|IndexHtmlUtil
operator|.
name|templateData
argument_list|(
name|gerritApi
argument_list|,
name|canonicalUrl
argument_list|,
name|cdnPath
argument_list|,
name|faviconPath
argument_list|,
name|parameterMap
argument_list|,
name|urlOrdainer
argument_list|)
decl_stmt|;
name|renderer
operator|=
name|soySauce
operator|.
name|renderTemplate
argument_list|(
literal|"com.google.gerrit.httpd.raw.Index"
argument_list|)
operator|.
name|setData
argument_list|(
name|templateData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
decl||
name|RestApiException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
name|UTF_8
operator|.
name|name
argument_list|()
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
name|setStatus
argument_list|(
name|SC_OK
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|w
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|w
operator|.
name|write
argument_list|(
name|renderer
operator|.
name|renderHtml
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


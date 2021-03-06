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
name|entities
operator|.
name|Change
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
name|entities
operator|.
name|Project
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
name|client
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
name|httpd
operator|.
name|raw
operator|.
name|AuthorizationCheckServlet
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
name|CatServlet
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
name|SshInfoServlet
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
name|ToolServlet
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
name|restapi
operator|.
name|AccessRestApiServlet
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
name|restapi
operator|.
name|AccountsRestApiServlet
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
name|restapi
operator|.
name|ChangesRestApiServlet
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
name|restapi
operator|.
name|ConfigRestApiServlet
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
name|restapi
operator|.
name|GroupsRestApiServlet
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
name|restapi
operator|.
name|ProjectsRestApiServlet
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
name|internal
operator|.
name|UniqueAnnotations
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_class
DECL|class|UrlModule
class|class
name|UrlModule
extends|extends
name|ServletModule
block|{
DECL|field|authConfig
specifier|private
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|method|UrlModule (AuthConfig authConfig)
name|UrlModule
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
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
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|!=
name|AuthType
operator|.
name|OAUTH
operator|&&
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|!=
name|AuthType
operator|.
name|OPENID
condition|)
block|{
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
block|}
name|serve
argument_list|(
literal|"/ssh_info"
argument_list|)
operator|.
name|with
argument_list|(
name|SshInfoServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/Main.class"
argument_list|)
operator|.
name|with
argument_list|(
name|notFound
argument_list|()
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/com/google/gerrit/launcher/*"
argument_list|)
operator|.
name|with
argument_list|(
name|notFound
argument_list|()
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/servlet/*"
argument_list|)
operator|.
name|with
argument_list|(
name|notFound
argument_list|()
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/all"
argument_list|)
operator|.
name|with
argument_list|(
name|query
argument_list|(
literal|"status:merged"
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/mine"
argument_list|)
operator|.
name|with
argument_list|(
name|screen
argument_list|(
name|PageLinks
operator|.
name|MINE
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/open"
argument_list|)
operator|.
name|with
argument_list|(
name|query
argument_list|(
literal|"status:open"
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/watched"
argument_list|)
operator|.
name|with
argument_list|(
name|query
argument_list|(
literal|"is:watched status:open"
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/starred"
argument_list|)
operator|.
name|with
argument_list|(
name|query
argument_list|(
literal|"is:starred"
argument_list|)
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/settings/?$"
argument_list|)
operator|.
name|with
argument_list|(
name|screen
argument_list|(
name|PageLinks
operator|.
name|SETTINGS
argument_list|)
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/register$"
argument_list|)
operator|.
name|with
argument_list|(
name|registerScreen
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/register/(.+)$"
argument_list|)
operator|.
name|with
argument_list|(
name|registerScreen
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/([1-9][0-9]*)/?$"
argument_list|)
operator|.
name|with
argument_list|(
name|NumericChangeIdRedirectServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/p/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|queryProjectNew
argument_list|()
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/r/(.+)/?$"
argument_list|)
operator|.
name|with
argument_list|(
name|DirectChangeByCommit
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/a/*"
argument_list|)
operator|.
name|through
argument_list|(
name|RequireIdentifiedUserFilter
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Must be after RequireIdentifiedUserFilter so auth happens before checking
comment|// for RunAs capability.
name|install
argument_list|(
operator|new
name|RunAsFilter
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?tools/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|ToolServlet
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Serve auth check. Mainly used by PolyGerrit for checking if a user is still logged in.
name|serveRegex
argument_list|(
literal|"^/(?:a/)?auth-check$"
argument_list|)
operator|.
name|with
argument_list|(
name|AuthorizationCheckServlet
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Bind servlets for REST root collections.
comment|// The '/plugins/' root collection is already handled by HttpPluginServlet
comment|// which is bound in HttpPluginModule. We cannot bind it here again although
comment|// this means that plugins can't add REST views on PLUGIN_KIND.
name|serveRegex
argument_list|(
literal|"^/(?:a/)?access/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|AccessRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?accounts/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|AccountsRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?changes/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|ChangesRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?config/(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|ConfigRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?groups/(.*)?$"
argument_list|)
operator|.
name|with
argument_list|(
name|GroupsRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/(?:a/)?projects/(.*)?$"
argument_list|)
operator|.
name|with
argument_list|(
name|ProjectsRestApiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/Documentation$"
argument_list|)
operator|.
name|with
argument_list|(
name|redirectDocumentation
argument_list|()
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
literal|"^/Documentation/$"
argument_list|)
operator|.
name|with
argument_list|(
name|redirectDocumentation
argument_list|()
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/Documentation/*"
argument_list|)
operator|.
name|through
argument_list|(
name|QueryDocumentationFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|notFound ()
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|notFound
parameter_list|()
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|screen (String target)
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|screen
parameter_list|(
name|String
name|target
parameter_list|)
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|toGerrit
argument_list|(
name|target
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|queryProjectNew ()
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|queryProjectNew
parameter_list|()
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|String
name|name
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|toGerrit
argument_list|(
name|PageLinks
operator|.
name|ADMIN_PROJECTS
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
return|return;
block|}
while|while
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
comment|//
name|name
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|Project
operator|.
name|NameKey
name|project
init|=
name|Project
operator|.
name|nameKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|toGerrit
argument_list|(
name|PageLinks
operator|.
name|toChangeQuery
argument_list|(
name|PageLinks
operator|.
name|projectQuery
argument_list|(
name|project
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
argument_list|)
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|query (String query)
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|toGerrit
argument_list|(
name|PageLinks
operator|.
name|toChangeQuery
argument_list|(
name|query
argument_list|)
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|key (HttpServlet servlet)
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|key
parameter_list|(
name|HttpServlet
name|servlet
parameter_list|)
block|{
specifier|final
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|srv
init|=
name|Key
operator|.
name|get
argument_list|(
name|HttpServlet
operator|.
name|class
argument_list|,
name|UniqueAnnotations
operator|.
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|srv
argument_list|)
operator|.
name|toProvider
argument_list|(
parameter_list|()
lambda|->
name|servlet
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
return|return
name|srv
return|;
block|}
DECL|method|registerScreen (final Boolean slash)
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|registerScreen
parameter_list|(
specifier|final
name|Boolean
name|slash
parameter_list|)
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|String
name|path
init|=
name|String
operator|.
name|format
argument_list|(
literal|"/register%s"
argument_list|,
name|slash
condition|?
name|req
operator|.
name|getPathInfo
argument_list|()
else|:
literal|""
argument_list|)
decl_stmt|;
name|toGerrit
argument_list|(
name|path
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|redirectDocumentation ()
specifier|private
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|redirectDocumentation
parameter_list|()
block|{
return|return
name|key
argument_list|(
operator|new
name|HttpServlet
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
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
name|String
name|path
init|=
literal|"/Documentation/index.html"
decl_stmt|;
name|toGerrit
argument_list|(
name|path
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|toGerrit (String target, HttpServletRequest req, HttpServletResponse rsp)
specifier|static
name|void
name|toGerrit
parameter_list|(
name|String
name|target
parameter_list|,
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|StringBuilder
name|url
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
name|req
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


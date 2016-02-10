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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
operator|.
name|exists
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
operator|.
name|isReadable
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
name|cache
operator|.
name|Cache
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
name|ImmutableList
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
name|GerritOptions
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
name|XsrfCookieFilter
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
name|ResourceServlet
operator|.
name|Resource
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
name|launcher
operator|.
name|GerritLauncher
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
name|cache
operator|.
name|CacheModule
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
name|GerritServerConfig
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
name|SitePaths
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
name|Provides
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
name|name
operator|.
name|Named
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
name|name
operator|.
name|Names
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
DECL|class|StaticModule
specifier|public
class|class
name|StaticModule
extends|extends
name|ServletModule
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|StaticModule
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CACHE
specifier|public
specifier|static
specifier|final
name|String
name|CACHE
init|=
literal|"static_content"
decl_stmt|;
DECL|field|POLYGERRIT_INDEX_PATHS
specifier|public
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|POLYGERRIT_INDEX_PATHS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"/"
argument_list|,
literal|"/c/*"
argument_list|,
literal|"/q/*"
argument_list|,
literal|"/x/*"
argument_list|,
literal|"/admin/*"
argument_list|,
literal|"/dashboard/*"
argument_list|,
literal|"/settings/*"
argument_list|,
comment|// TODO(dborowitz): These fragments conflict with the REST API
comment|// namespace, so they will need to use a different path.
literal|"/groups/*"
argument_list|,
literal|"/projects/*"
argument_list|)
decl_stmt|;
DECL|field|DOC_SERVLET
specifier|private
specifier|static
specifier|final
name|String
name|DOC_SERVLET
init|=
literal|"DocServlet"
decl_stmt|;
DECL|field|FAVICON_SERVLET
specifier|private
specifier|static
specifier|final
name|String
name|FAVICON_SERVLET
init|=
literal|"FaviconServlet"
decl_stmt|;
DECL|field|GWT_UI_SERVLET
specifier|private
specifier|static
specifier|final
name|String
name|GWT_UI_SERVLET
init|=
literal|"GwtUiServlet"
decl_stmt|;
DECL|field|POLYGERRIT_INDEX_SERVLET
specifier|private
specifier|static
specifier|final
name|String
name|POLYGERRIT_INDEX_SERVLET
init|=
literal|"PolyGerritUiIndexServlet"
decl_stmt|;
DECL|field|ROBOTS_TXT_SERVLET
specifier|private
specifier|static
specifier|final
name|String
name|ROBOTS_TXT_SERVLET
init|=
literal|"RobotsTxtServlet"
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|GerritOptions
name|options
decl_stmt|;
DECL|field|paths
specifier|private
name|Paths
name|paths
decl_stmt|;
annotation|@
name|Inject
DECL|method|StaticModule (GerritOptions options)
specifier|public
name|StaticModule
parameter_list|(
name|GerritOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
DECL|method|getPaths ()
specifier|private
name|Paths
name|getPaths
parameter_list|()
block|{
if|if
condition|(
name|paths
operator|==
literal|null
condition|)
block|{
name|paths
operator|=
operator|new
name|Paths
argument_list|()
expr_stmt|;
block|}
return|return
name|paths
return|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|serveRegex
argument_list|(
literal|"^/Documentation/(.+)$"
argument_list|)
operator|.
name|with
argument_list|(
name|named
argument_list|(
name|DOC_SERVLET
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/static/*"
argument_list|)
operator|.
name|with
argument_list|(
name|SiteStaticDirectoryServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|CACHE
argument_list|,
name|Path
operator|.
name|class
argument_list|,
name|Resource
operator|.
name|class
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|1
operator|<<
literal|20
argument_list|)
operator|.
name|weigher
argument_list|(
name|ResourceServlet
operator|.
name|Weigher
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
name|options
operator|.
name|enablePolyGerrit
argument_list|()
condition|)
block|{
name|install
argument_list|(
operator|new
name|CoreStaticModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|PolyGerritUiModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|.
name|enableDefaultUi
argument_list|()
condition|)
block|{
name|install
argument_list|(
operator|new
name|CoreStaticModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GwtUiModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|DOC_SERVLET
argument_list|)
DECL|method|getDocServlet (@amedCACHE) Cache<Path, Resource> cache)
name|HttpServlet
name|getDocServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
block|{
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|warFs
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|WarDocServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|warFs
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|unpackedWar
operator|!=
literal|null
operator|&&
operator|!
name|p
operator|.
name|isDev
argument_list|()
condition|)
block|{
return|return
operator|new
name|DirectoryDocServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|unpackedWar
argument_list|)
return|;
block|}
else|else
block|{
return|return
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
name|service
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
name|resp
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
return|;
block|}
block|}
DECL|class|CoreStaticModule
specifier|private
class|class
name|CoreStaticModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|public
name|void
name|configureServlets
parameter_list|()
block|{
name|serve
argument_list|(
literal|"/robots.txt"
argument_list|)
operator|.
name|with
argument_list|(
name|named
argument_list|(
name|ROBOTS_TXT_SERVLET
argument_list|)
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/favicon.ico"
argument_list|)
operator|.
name|with
argument_list|(
name|named
argument_list|(
name|FAVICON_SERVLET
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|ROBOTS_TXT_SERVLET
argument_list|)
DECL|method|getRobotsTxtServlet (@erritServerConfig Config cfg, SitePaths sitePaths, @Named(CACHE) Cache<Path, Resource> cache)
name|HttpServlet
name|getRobotsTxtServlet
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
block|{
name|Path
name|configPath
init|=
name|sitePaths
operator|.
name|resolve
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"httpd"
argument_list|,
literal|null
argument_list|,
literal|"robotsFile"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|configPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exists
argument_list|(
name|configPath
argument_list|)
operator|&&
name|isReadable
argument_list|(
name|configPath
argument_list|)
condition|)
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|configPath
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot read httpd.robotsFile, using default"
argument_list|)
expr_stmt|;
block|}
block|}
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|warFs
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|warFs
operator|.
name|getPath
argument_list|(
literal|"/robots.txt"
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|webappSourcePath
argument_list|(
literal|"robots.txt"
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|FAVICON_SERVLET
argument_list|)
DECL|method|getFaviconServlet (@amedCACHE) Cache<Path, Resource> cache)
name|HttpServlet
name|getFaviconServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
block|{
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|warFs
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|warFs
operator|.
name|getPath
argument_list|(
literal|"/favicon.ico"
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|webappSourcePath
argument_list|(
literal|"favicon.ico"
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
DECL|method|webappSourcePath (String name)
specifier|private
name|Path
name|webappSourcePath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|unpackedWar
operator|!=
literal|null
condition|)
block|{
return|return
name|p
operator|.
name|unpackedWar
operator|.
name|resolve
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
name|p
operator|.
name|buckOut
operator|.
name|resolveSibling
argument_list|(
literal|"gerrit-war"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"src"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"main"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"webapp"
argument_list|)
operator|.
name|resolve
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
DECL|class|GwtUiModule
specifier|private
class|class
name|GwtUiModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|public
name|void
name|configureServlets
parameter_list|()
block|{
name|serveRegex
argument_list|(
literal|"^/gerrit_ui/(?!rpc/)(.*)$"
argument_list|)
operator|.
name|with
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|HttpServlet
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|GWT_UI_SERVLET
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|isDev
argument_list|()
condition|)
block|{
name|filter
argument_list|(
literal|"/"
argument_list|)
operator|.
name|through
argument_list|(
operator|new
name|RecompileGwtUiFilter
argument_list|(
name|p
operator|.
name|buckOut
argument_list|,
name|p
operator|.
name|unpackedWar
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|GWT_UI_SERVLET
argument_list|)
DECL|method|getGwtUiServlet (@amedCACHE) Cache<Path, Resource> cache)
name|HttpServlet
name|getGwtUiServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|warFs
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|WarGwtUiServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|warFs
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DirectoryGwtUiServlet
argument_list|(
name|cache
argument_list|,
name|p
operator|.
name|unpackedWar
argument_list|,
name|p
operator|.
name|isDev
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
DECL|class|PolyGerritUiModule
specifier|private
class|class
name|PolyGerritUiModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|public
name|void
name|configureServlets
parameter_list|()
block|{
name|Path
name|buckOut
init|=
name|getPaths
argument_list|()
operator|.
name|buckOut
decl_stmt|;
if|if
condition|(
name|buckOut
operator|!=
literal|null
condition|)
block|{
name|serve
argument_list|(
literal|"/bower_components/*"
argument_list|)
operator|.
name|with
argument_list|(
name|BowerComponentsServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// In the war case, bower_components are either inlined by vulcanize, or
comment|// live under /polygerrit_ui in the war file, so we don't need a
comment|// separate servlet.
block|}
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|indexKey
init|=
name|named
argument_list|(
name|POLYGERRIT_INDEX_SERVLET
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|POLYGERRIT_INDEX_PATHS
control|)
block|{
name|filter
argument_list|(
name|p
argument_list|)
operator|.
name|through
argument_list|(
name|XsrfCookieFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
name|p
argument_list|)
operator|.
name|with
argument_list|(
name|indexKey
argument_list|)
expr_stmt|;
block|}
name|serve
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|with
argument_list|(
name|PolyGerritUiServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|POLYGERRIT_INDEX_SERVLET
argument_list|)
DECL|method|getPolyGerritUiIndexServlet ( @amedCACHE) Cache<Path, Resource> cache)
name|HttpServlet
name|getPolyGerritUiIndexServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
block|{
return|return
operator|new
name|SingleFileServlet
argument_list|(
name|cache
argument_list|,
name|polyGerritBasePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"index.html"
argument_list|)
argument_list|,
name|getPaths
argument_list|()
operator|.
name|isDev
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getPolyGerritUiServlet ( @amedCACHE) Cache<Path, Resource> cache)
name|PolyGerritUiServlet
name|getPolyGerritUiServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
block|{
return|return
operator|new
name|PolyGerritUiServlet
argument_list|(
name|cache
argument_list|,
name|polyGerritBasePath
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getBowerComponentsServlet ( @amedCACHE) Cache<Path, Resource> cache)
name|BowerComponentsServlet
name|getBowerComponentsServlet
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE
argument_list|)
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|BowerComponentsServlet
argument_list|(
name|cache
argument_list|,
name|getPaths
argument_list|()
operator|.
name|buckOut
argument_list|)
return|;
block|}
DECL|method|polyGerritBasePath ()
specifier|private
name|Path
name|polyGerritBasePath
parameter_list|()
block|{
name|Paths
name|p
init|=
name|getPaths
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|forcePolyGerritDev
argument_list|()
condition|)
block|{
name|checkArgument
argument_list|(
name|p
operator|.
name|buckOut
operator|!=
literal|null
argument_list|,
literal|"no buck-out directory found for PolyGerrit developer mode"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|isDev
argument_list|()
condition|)
block|{
return|return
name|p
operator|.
name|buckOut
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"polygerrit-ui"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"app"
argument_list|)
return|;
block|}
return|return
name|p
operator|.
name|warFs
operator|!=
literal|null
condition|?
name|p
operator|.
name|warFs
operator|.
name|getPath
argument_list|(
literal|"/polygerrit_ui"
argument_list|)
else|:
name|p
operator|.
name|unpackedWar
operator|.
name|resolve
argument_list|(
literal|"polygerrit_ui"
argument_list|)
return|;
block|}
block|}
DECL|class|Paths
specifier|private
class|class
name|Paths
block|{
DECL|field|warFs
specifier|private
specifier|final
name|FileSystem
name|warFs
decl_stmt|;
DECL|field|buckOut
specifier|private
specifier|final
name|Path
name|buckOut
decl_stmt|;
DECL|field|unpackedWar
specifier|private
specifier|final
name|Path
name|unpackedWar
decl_stmt|;
DECL|field|development
specifier|private
specifier|final
name|boolean
name|development
decl_stmt|;
DECL|method|Paths ()
specifier|private
name|Paths
parameter_list|()
block|{
try|try
block|{
name|File
name|launcherLoadedFrom
init|=
name|getLauncherLoadedFrom
argument_list|()
decl_stmt|;
if|if
condition|(
name|launcherLoadedFrom
operator|!=
literal|null
operator|&&
name|launcherLoadedFrom
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
comment|// Special case: unpacked war archive deployed in container.
comment|// The path is something like:
comment|//<container>/<gerrit>/WEB-INF/lib/launcher.jar
comment|// Switch to exploded war case with<container>/webapp>/<gerrit>
comment|// root directory
name|warFs
operator|=
literal|null
expr_stmt|;
name|unpackedWar
operator|=
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
operator|.
name|get
argument_list|(
name|launcherLoadedFrom
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|buckOut
operator|=
literal|null
expr_stmt|;
name|development
operator|=
literal|false
expr_stmt|;
return|return;
block|}
name|warFs
operator|=
name|getDistributionArchive
argument_list|(
name|launcherLoadedFrom
argument_list|)
expr_stmt|;
if|if
condition|(
name|warFs
operator|==
literal|null
condition|)
block|{
name|buckOut
operator|=
name|getDeveloperBuckOut
argument_list|()
expr_stmt|;
name|unpackedWar
operator|=
name|makeWarTempDir
argument_list|()
expr_stmt|;
name|development
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|options
operator|.
name|forcePolyGerritDev
argument_list|()
condition|)
block|{
name|buckOut
operator|=
name|getDeveloperBuckOut
argument_list|()
expr_stmt|;
name|unpackedWar
operator|=
literal|null
expr_stmt|;
name|development
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|buckOut
operator|=
literal|null
expr_stmt|;
name|unpackedWar
operator|=
literal|null
expr_stmt|;
name|development
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Error initializing static content paths"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getDistributionArchive (File war)
specifier|private
name|FileSystem
name|getDistributionArchive
parameter_list|(
name|File
name|war
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|war
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|GerritLauncher
operator|.
name|getZipFileSystem
argument_list|(
name|war
operator|.
name|toPath
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getLauncherLoadedFrom ()
specifier|private
name|File
name|getLauncherLoadedFrom
parameter_list|()
block|{
name|File
name|war
decl_stmt|;
try|try
block|{
name|war
operator|=
name|GerritLauncher
operator|.
name|getDistributionArchive
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|(
name|e
operator|instanceof
name|FileNotFoundException
operator|)
operator|&&
name|GerritLauncher
operator|.
name|NOT_ARCHIVED
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|ProvisionException
name|pe
init|=
operator|new
name|ProvisionException
argument_list|(
literal|"Error reading gerrit.war"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
return|return
name|war
return|;
block|}
DECL|method|isDev ()
specifier|private
name|boolean
name|isDev
parameter_list|()
block|{
return|return
name|development
return|;
block|}
DECL|method|getDeveloperBuckOut ()
specifier|private
name|Path
name|getDeveloperBuckOut
parameter_list|()
block|{
try|try
block|{
return|return
name|GerritLauncher
operator|.
name|getDeveloperBuckOut
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|makeWarTempDir ()
specifier|private
name|Path
name|makeWarTempDir
parameter_list|()
block|{
comment|// Obtain our local temporary directory, but it comes back as a file
comment|// so we have to switch it to be a directory post creation.
comment|//
try|try
block|{
name|File
name|dstwar
init|=
name|GerritLauncher
operator|.
name|createTempFile
argument_list|(
literal|"gerrit_"
argument_list|,
literal|"war"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|dstwar
operator|.
name|delete
argument_list|()
operator|||
operator|!
name|dstwar
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot mkdir "
operator|+
name|dstwar
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
comment|// Jetty normally refuses to serve out of a symlinked directory, as
comment|// a security feature. Try to resolve out any symlinks in the path.
comment|//
try|try
block|{
return|return
name|dstwar
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|toPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|dstwar
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toPath
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ProvisionException
name|pe
init|=
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot create war tempdir"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
block|}
DECL|method|named (String name)
specifier|private
specifier|static
name|Key
argument_list|<
name|HttpServlet
argument_list|>
name|named
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Key
operator|.
name|get
argument_list|(
name|HttpServlet
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


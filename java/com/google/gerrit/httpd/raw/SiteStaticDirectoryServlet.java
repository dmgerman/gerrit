begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|Path
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

begin_comment
comment|/** Sends static content from the site 's {@code static/} subdirectory. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SiteStaticDirectoryServlet
specifier|public
class|class
name|SiteStaticDirectoryServlet
extends|extends
name|ResourceServlet
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
DECL|field|staticBase
specifier|private
specifier|final
name|Path
name|staticBase
decl_stmt|;
annotation|@
name|Inject
DECL|method|SiteStaticDirectoryServlet ( SitePaths site, @GerritServerConfig Config cfg, @Named(StaticModule.CACHE) Cache<Path, Resource> cache)
name|SiteStaticDirectoryServlet
parameter_list|(
name|SitePaths
name|site
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|Named
argument_list|(
name|StaticModule
operator|.
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
name|super
argument_list|(
name|cache
argument_list|,
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"site"
argument_list|,
literal|"refreshHeaderFooter"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|p
decl_stmt|;
try|try
block|{
name|p
operator|=
name|site
operator|.
name|static_dir
operator|.
name|toRealPath
argument_list|()
operator|.
name|normalize
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|p
operator|=
name|site
operator|.
name|static_dir
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|normalize
argument_list|()
expr_stmt|;
block|}
name|staticBase
operator|=
name|p
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResourcePath (String pathInfo)
specifier|protected
name|Path
name|getResourcePath
parameter_list|(
name|String
name|pathInfo
parameter_list|)
block|{
return|return
name|staticBase
operator|.
name|resolve
argument_list|(
name|pathInfo
argument_list|)
return|;
block|}
block|}
end_class

end_unit


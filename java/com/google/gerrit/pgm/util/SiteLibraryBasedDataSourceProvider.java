begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
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
name|SiteLibraryLoaderUtil
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
name|metrics
operator|.
name|MetricMaker
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ThreadSettingsConfig
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
name|schema
operator|.
name|DataSourceProvider
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
name|schema
operator|.
name|DataSourceType
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
name|sql
operator|.
name|DataSource
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
comment|/** Loads the site library if not yet loaded. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|SiteLibraryBasedDataSourceProvider
specifier|public
class|class
name|SiteLibraryBasedDataSourceProvider
extends|extends
name|DataSourceProvider
block|{
DECL|field|libdir
specifier|private
specifier|final
name|Path
name|libdir
decl_stmt|;
DECL|field|init
specifier|private
name|boolean
name|init
decl_stmt|;
annotation|@
name|Inject
DECL|method|SiteLibraryBasedDataSourceProvider ( SitePaths site, @GerritServerConfig Config cfg, MetricMaker metrics, ThreadSettingsConfig tsc, DataSourceProvider.Context ctx, DataSourceType dst)
name|SiteLibraryBasedDataSourceProvider
parameter_list|(
name|SitePaths
name|site
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|MetricMaker
name|metrics
parameter_list|,
name|ThreadSettingsConfig
name|tsc
parameter_list|,
name|DataSourceProvider
operator|.
name|Context
name|ctx
parameter_list|,
name|DataSourceType
name|dst
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|metrics
argument_list|,
name|tsc
argument_list|,
name|ctx
argument_list|,
name|dst
argument_list|)
expr_stmt|;
name|libdir
operator|=
name|site
operator|.
name|lib_dir
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|synchronized
name|DataSource
name|get
parameter_list|()
block|{
if|if
condition|(
operator|!
name|init
condition|)
block|{
name|SiteLibraryLoaderUtil
operator|.
name|loadSiteLib
argument_list|(
name|libdir
argument_list|)
expr_stmt|;
name|init
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|super
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.pgm.init.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|api
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
name|annotations
operator|.
name|VisibleForTesting
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
name|securestore
operator|.
name|SecureStore
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
name|List
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
name|errors
operator|.
name|ConfigInvalidException
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
import|;
end_import

begin_comment
comment|/** Global variables used by the 'init' command. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitFlags
specifier|public
class|class
name|InitFlags
block|{
comment|/** Recursively delete the site path if initialization fails. */
DECL|field|deleteOnFailure
specifier|public
name|boolean
name|deleteOnFailure
decl_stmt|;
comment|/** Site is being newly created */
DECL|field|isNew
specifier|public
name|boolean
name|isNew
decl_stmt|;
comment|/** Run the daemon (and open the web UI in a browser) after initialization. */
DECL|field|autoStart
specifier|public
name|boolean
name|autoStart
decl_stmt|;
comment|/** Skip plugins */
DECL|field|skipPlugins
specifier|public
name|boolean
name|skipPlugins
decl_stmt|;
comment|/** Delete all cache files */
DECL|field|deleteCaches
specifier|public
name|boolean
name|deleteCaches
decl_stmt|;
comment|/** Dev mode */
DECL|field|dev
specifier|public
name|boolean
name|dev
decl_stmt|;
DECL|field|cfg
specifier|public
specifier|final
name|FileBasedConfig
name|cfg
decl_stmt|;
DECL|field|sec
specifier|public
specifier|final
name|SecureStore
name|sec
decl_stmt|;
DECL|field|installPlugins
specifier|public
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|installPlugins
decl_stmt|;
DECL|field|installAllPlugins
specifier|public
specifier|final
name|boolean
name|installAllPlugins
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|InitFlags ( final SitePaths site, final SecureStore secureStore, @InstallPlugins final List<String> installPlugins, @InstallAllPlugins final Boolean installAllPlugins)
specifier|public
name|InitFlags
parameter_list|(
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|SecureStore
name|secureStore
parameter_list|,
annotation|@
name|InstallPlugins
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|installPlugins
parameter_list|,
annotation|@
name|InstallAllPlugins
specifier|final
name|Boolean
name|installAllPlugins
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|sec
operator|=
name|secureStore
expr_stmt|;
name|this
operator|.
name|installPlugins
operator|=
name|installPlugins
expr_stmt|;
name|this
operator|.
name|installAllPlugins
operator|=
name|installAllPlugins
expr_stmt|;
name|cfg
operator|=
operator|new
name|FileBasedConfig
argument_list|(
name|site
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


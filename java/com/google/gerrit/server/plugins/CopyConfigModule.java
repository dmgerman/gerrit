begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
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
name|GerritPersonIdent
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
name|GerritPersonIdentProvider
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
name|AnonymousCowardName
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
name|SitePath
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
name|TrackingFooters
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
name|git
operator|.
name|GitRepositoryManager
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
name|AbstractModule
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_comment
comment|/**  * Copies critical objects from the {@code dbInjector} into a plugin.  *  *<p>Most explicit bindings are copied automatically from the cfgInjector and sysInjector to be  * made available to a plugin's private world. This module is necessary to get things bound in the  * dbInjector that are not otherwise easily available, but that a plugin author might expect to  * exist.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|CopyConfigModule
class|class
name|CopyConfigModule
extends|extends
name|AbstractModule
block|{
DECL|field|sitePath
annotation|@
name|Inject
annotation|@
name|SitePath
specifier|private
name|Path
name|sitePath
decl_stmt|;
annotation|@
name|Provides
annotation|@
name|SitePath
DECL|method|getSitePath ()
name|Path
name|getSitePath
parameter_list|()
block|{
return|return
name|sitePath
return|;
block|}
DECL|field|sitePaths
annotation|@
name|Inject
specifier|private
name|SitePaths
name|sitePaths
decl_stmt|;
annotation|@
name|Provides
DECL|method|getSitePaths ()
name|SitePaths
name|getSitePaths
parameter_list|()
block|{
return|return
name|sitePaths
return|;
block|}
DECL|field|trackingFooters
annotation|@
name|Inject
specifier|private
name|TrackingFooters
name|trackingFooters
decl_stmt|;
annotation|@
name|Provides
DECL|method|getTrackingFooters ()
name|TrackingFooters
name|getTrackingFooters
parameter_list|()
block|{
return|return
name|trackingFooters
return|;
block|}
DECL|field|gerritServerConfig
annotation|@
name|Inject
annotation|@
name|GerritServerConfig
specifier|private
name|Config
name|gerritServerConfig
decl_stmt|;
annotation|@
name|Provides
annotation|@
name|GerritServerConfig
DECL|method|getGerritServerConfig ()
name|Config
name|getGerritServerConfig
parameter_list|()
block|{
return|return
name|gerritServerConfig
return|;
block|}
DECL|field|gitRepositoryManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|gitRepositoryManager
decl_stmt|;
annotation|@
name|Provides
DECL|method|getGitRepositoryManager ()
name|GitRepositoryManager
name|getGitRepositoryManager
parameter_list|()
block|{
return|return
name|gitRepositoryManager
return|;
block|}
DECL|field|anonymousCowardName
annotation|@
name|Inject
annotation|@
name|AnonymousCowardName
specifier|private
name|String
name|anonymousCowardName
decl_stmt|;
annotation|@
name|Provides
annotation|@
name|AnonymousCowardName
DECL|method|getAnonymousCowardName ()
name|String
name|getAnonymousCowardName
parameter_list|()
block|{
return|return
name|anonymousCowardName
return|;
block|}
DECL|field|serverIdentProvider
annotation|@
name|Inject
specifier|private
name|GerritPersonIdentProvider
name|serverIdentProvider
decl_stmt|;
annotation|@
name|Provides
annotation|@
name|GerritPersonIdent
DECL|method|getServerIdent ()
name|PersonIdent
name|getServerIdent
parameter_list|()
block|{
return|return
name|serverIdentProvider
operator|.
name|get
argument_list|()
return|;
block|}
DECL|field|secureStore
annotation|@
name|Inject
specifier|private
name|SecureStore
name|secureStore
decl_stmt|;
annotation|@
name|Provides
DECL|method|getSecureStore ()
name|SecureStore
name|getSecureStore
parameter_list|()
block|{
return|return
name|secureStore
return|;
block|}
annotation|@
name|Inject
DECL|method|CopyConfigModule ()
name|CopyConfigModule
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{}
block|}
end_class

end_unit


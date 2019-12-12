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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|checkState
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|RepositoryConfig
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
comment|/**  * RepositoryManager that looks up repos stored across directories.  *  *<p>Each repository has a path configured in Gerrit server config, repository.NAME.basePath,  * indicating where the repo can be found  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|MultiBaseLocalDiskRepositoryManager
specifier|public
class|class
name|MultiBaseLocalDiskRepositoryManager
extends|extends
name|LocalDiskRepositoryManager
block|{
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MultiBaseLocalDiskRepositoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|MultiBaseLocalDiskRepositoryManager
operator|.
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|config
specifier|private
specifier|final
name|RepositoryConfig
name|config
decl_stmt|;
annotation|@
name|Inject
DECL|method|MultiBaseLocalDiskRepositoryManager ( SitePaths site, @GerritServerConfig Config cfg, RepositoryConfig config)
name|MultiBaseLocalDiskRepositoryManager
parameter_list|(
name|SitePaths
name|site
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|RepositoryConfig
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|site
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
for|for
control|(
name|Path
name|alternateBasePath
range|:
name|config
operator|.
name|getAllBasePaths
argument_list|()
control|)
block|{
name|checkState
argument_list|(
name|alternateBasePath
operator|.
name|isAbsolute
argument_list|()
argument_list|,
literal|"repository.<name>.basePath must be absolute: %s"
argument_list|,
name|alternateBasePath
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getBasePath (Project.NameKey name)
specifier|public
name|Path
name|getBasePath
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|Path
name|alternateBasePath
init|=
name|config
operator|.
name|getBasePath
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|alternateBasePath
operator|!=
literal|null
condition|?
name|alternateBasePath
else|:
name|super
operator|.
name|getBasePath
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scanProjects (ProjectVisitor visitor)
specifier|protected
name|void
name|scanProjects
parameter_list|(
name|ProjectVisitor
name|visitor
parameter_list|)
block|{
name|super
operator|.
name|scanProjects
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
for|for
control|(
name|Path
name|path
range|:
name|config
operator|.
name|getAllBasePaths
argument_list|()
control|)
block|{
name|visitor
operator|.
name|setStartFolder
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|super
operator|.
name|scanProjects
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lucene
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
name|gerrit
operator|.
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|project
operator|.
name|ProjectIndex
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
name|index
operator|.
name|AbstractIndexModule
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
name|index
operator|.
name|VersionManager
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
name|index
operator|.
name|account
operator|.
name|AccountIndex
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
name|index
operator|.
name|change
operator|.
name|ChangeIndex
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
name|index
operator|.
name|group
operator|.
name|GroupIndex
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
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanQuery
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

begin_class
DECL|class|LuceneIndexModule
specifier|public
class|class
name|LuceneIndexModule
extends|extends
name|AbstractIndexModule
block|{
DECL|method|singleVersionAllLatest (int threads, boolean slave)
specifier|public
specifier|static
name|LuceneIndexModule
name|singleVersionAllLatest
parameter_list|(
name|int
name|threads
parameter_list|,
name|boolean
name|slave
parameter_list|)
block|{
return|return
operator|new
name|LuceneIndexModule
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|,
name|threads
argument_list|,
name|slave
argument_list|)
return|;
block|}
DECL|method|singleVersionWithExplicitVersions ( Map<String, Integer> versions, int threads, boolean slave)
specifier|public
specifier|static
name|LuceneIndexModule
name|singleVersionWithExplicitVersions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|versions
parameter_list|,
name|int
name|threads
parameter_list|,
name|boolean
name|slave
parameter_list|)
block|{
return|return
operator|new
name|LuceneIndexModule
argument_list|(
name|versions
argument_list|,
name|threads
argument_list|,
name|slave
argument_list|)
return|;
block|}
DECL|method|latestVersion (boolean slave)
specifier|public
specifier|static
name|LuceneIndexModule
name|latestVersion
parameter_list|(
name|boolean
name|slave
parameter_list|)
block|{
return|return
operator|new
name|LuceneIndexModule
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
name|slave
argument_list|)
return|;
block|}
DECL|method|isInMemoryTest (Config cfg)
specifier|static
name|boolean
name|isInMemoryTest
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"index"
argument_list|,
literal|"lucene"
argument_list|,
literal|"testInmemory"
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|LuceneIndexModule (Map<String, Integer> singleVersions, int threads, boolean slave)
specifier|private
name|LuceneIndexModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|singleVersions
parameter_list|,
name|int
name|threads
parameter_list|,
name|boolean
name|slave
parameter_list|)
block|{
name|super
argument_list|(
name|singleVersions
argument_list|,
name|threads
argument_list|,
name|slave
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAccountIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|AccountIndex
argument_list|>
name|getAccountIndex
parameter_list|()
block|{
return|return
name|LuceneAccountIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getChangeIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|ChangeIndex
argument_list|>
name|getChangeIndex
parameter_list|()
block|{
return|return
name|LuceneChangeIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getGroupIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|GroupIndex
argument_list|>
name|getGroupIndex
parameter_list|()
block|{
return|return
name|LuceneGroupIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getProjectIndex ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|ProjectIndex
argument_list|>
name|getProjectIndex
parameter_list|()
block|{
return|return
name|LuceneProjectIndex
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getVersionManager ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|VersionManager
argument_list|>
name|getVersionManager
parameter_list|()
block|{
return|return
name|LuceneVersionManager
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getIndexConfig (@erritServerConfig Config cfg)
specifier|protected
name|IndexConfig
name|getIndexConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|BooleanQuery
operator|.
name|setMaxClauseCount
argument_list|(
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|"maxTerms"
argument_list|,
name|BooleanQuery
operator|.
name|getMaxClauseCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getIndexConfig
argument_list|(
name|cfg
argument_list|)
return|;
block|}
block|}
end_class

end_unit


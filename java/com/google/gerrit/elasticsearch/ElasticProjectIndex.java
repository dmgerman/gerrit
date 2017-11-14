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
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
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
name|ImmutableList
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
name|collect
operator|.
name|Lists
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
name|elasticsearch
operator|.
name|ElasticMapping
operator|.
name|MappingProperties
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
name|QueryOptions
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
name|Schema
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
name|query
operator|.
name|DataSource
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
name|query
operator|.
name|FieldBundle
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
name|query
operator|.
name|Predicate
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
name|query
operator|.
name|QueryParseException
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
name|reviewdb
operator|.
name|client
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
name|index
operator|.
name|IndexUtils
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
name|project
operator|.
name|ProjectField
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonArray
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonElement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|client
operator|.
name|JestResult
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|core
operator|.
name|Bulk
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|core
operator|.
name|Bulk
operator|.
name|Builder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|core
operator|.
name|Search
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|core
operator|.
name|search
operator|.
name|sort
operator|.
name|Sort
import|;
end_import

begin_import
import|import
name|io
operator|.
name|searchbox
operator|.
name|core
operator|.
name|search
operator|.
name|sort
operator|.
name|Sort
operator|.
name|Sorting
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|elasticsearch
operator|.
name|index
operator|.
name|query
operator|.
name|QueryBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|search
operator|.
name|builder
operator|.
name|SearchSourceBuilder
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

begin_class
DECL|class|ElasticProjectIndex
specifier|public
class|class
name|ElasticProjectIndex
extends|extends
name|AbstractElasticIndex
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectData
argument_list|>
implements|implements
name|ProjectIndex
block|{
DECL|class|ProjectMapping
specifier|static
class|class
name|ProjectMapping
block|{
DECL|field|projects
name|MappingProperties
name|projects
decl_stmt|;
DECL|method|ProjectMapping (Schema<ProjectData> schema)
name|ProjectMapping
parameter_list|(
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|projects
operator|=
name|ElasticMapping
operator|.
name|createMapping
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|PROJECTS
specifier|static
specifier|final
name|String
name|PROJECTS
init|=
literal|"projects"
decl_stmt|;
DECL|field|PROJECTS_PREFIX
specifier|static
specifier|final
name|String
name|PROJECTS_PREFIX
init|=
name|PROJECTS
operator|+
literal|"_"
decl_stmt|;
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
name|ElasticProjectIndex
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mapping
specifier|private
specifier|final
name|ProjectMapping
name|mapping
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|Provider
argument_list|<
name|ProjectCache
argument_list|>
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ElasticProjectIndex ( @erritServerConfig Config cfg, SitePaths sitePaths, Provider<ProjectCache> projectCache, JestClientBuilder clientBuilder, @Assisted Schema<ProjectData> schema)
name|ElasticProjectIndex
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|Provider
argument_list|<
name|ProjectCache
argument_list|>
name|projectCache
parameter_list|,
name|JestClientBuilder
name|clientBuilder
parameter_list|,
annotation|@
name|Assisted
name|Schema
argument_list|<
name|ProjectData
argument_list|>
name|schema
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|sitePaths
argument_list|,
name|schema
argument_list|,
name|clientBuilder
argument_list|,
name|PROJECTS_PREFIX
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|mapping
operator|=
operator|new
name|ProjectMapping
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (ProjectData projectState)
specifier|public
name|void
name|replace
parameter_list|(
name|ProjectData
name|projectState
parameter_list|)
throws|throws
name|IOException
block|{
name|Bulk
name|bulk
init|=
operator|new
name|Bulk
operator|.
name|Builder
argument_list|()
operator|.
name|defaultIndex
argument_list|(
name|indexName
argument_list|)
operator|.
name|defaultType
argument_list|(
name|PROJECTS
argument_list|)
operator|.
name|addAction
argument_list|(
name|insert
argument_list|(
name|PROJECTS
argument_list|,
name|projectState
argument_list|)
argument_list|)
operator|.
name|refresh
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|JestResult
name|result
init|=
name|client
operator|.
name|execute
argument_list|(
name|bulk
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isSucceeded
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to replace project %s in index %s: %s"
argument_list|,
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|indexName
argument_list|,
name|result
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<ProjectData> p, QueryOptions opts)
specifier|public
name|DataSource
argument_list|<
name|ProjectData
argument_list|>
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
return|return
operator|new
name|QuerySource
argument_list|(
name|p
argument_list|,
name|opts
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addActions (Builder builder, Project.NameKey nameKey)
specifier|protected
name|Builder
name|addActions
parameter_list|(
name|Builder
name|builder
parameter_list|,
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
return|return
name|builder
operator|.
name|addAction
argument_list|(
name|delete
argument_list|(
name|PROJECTS
argument_list|,
name|nameKey
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMappings ()
specifier|protected
name|String
name|getMappings
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ProjectMapping
argument_list|>
name|mappings
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"mappings"
argument_list|,
name|mapping
argument_list|)
decl_stmt|;
return|return
name|gson
operator|.
name|toJson
argument_list|(
name|mappings
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getId (ProjectData projectState)
specifier|protected
name|String
name|getId
parameter_list|(
name|ProjectData
name|projectState
parameter_list|)
block|{
return|return
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|class|QuerySource
specifier|private
class|class
name|QuerySource
implements|implements
name|DataSource
argument_list|<
name|ProjectData
argument_list|>
block|{
DECL|field|search
specifier|private
specifier|final
name|Search
name|search
decl_stmt|;
DECL|field|fields
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|fields
decl_stmt|;
DECL|method|QuerySource (Predicate<ProjectData> p, QueryOptions opts)
name|QuerySource
parameter_list|(
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|QueryBuilder
name|qb
init|=
name|queryBuilder
operator|.
name|toQueryBuilder
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|fields
operator|=
name|IndexUtils
operator|.
name|projectFields
argument_list|(
name|opts
argument_list|)
expr_stmt|;
name|SearchSourceBuilder
name|searchSource
init|=
operator|new
name|SearchSourceBuilder
argument_list|()
operator|.
name|query
argument_list|(
name|qb
argument_list|)
operator|.
name|from
argument_list|(
name|opts
operator|.
name|start
argument_list|()
argument_list|)
operator|.
name|size
argument_list|(
name|opts
operator|.
name|limit
argument_list|()
argument_list|)
operator|.
name|fields
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|fields
argument_list|)
argument_list|)
decl_stmt|;
name|Sort
name|sort
init|=
operator|new
name|Sort
argument_list|(
name|ProjectField
operator|.
name|NAME
operator|.
name|getName
argument_list|()
argument_list|,
name|Sorting
operator|.
name|ASC
argument_list|)
decl_stmt|;
name|sort
operator|.
name|setIgnoreUnmapped
argument_list|()
expr_stmt|;
name|search
operator|=
operator|new
name|Search
operator|.
name|Builder
argument_list|(
name|searchSource
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|addType
argument_list|(
name|PROJECTS
argument_list|)
operator|.
name|addIndex
argument_list|(
name|indexName
argument_list|)
operator|.
name|addSort
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|sort
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
return|return
literal|10
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|ProjectData
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
block|{
name|List
argument_list|<
name|ProjectData
argument_list|>
name|results
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|JestResult
name|result
init|=
name|client
operator|.
name|execute
argument_list|(
name|search
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isSucceeded
argument_list|()
condition|)
block|{
name|JsonObject
name|obj
init|=
name|result
operator|.
name|getJsonObject
argument_list|()
operator|.
name|getAsJsonObject
argument_list|(
literal|"hits"
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|.
name|get
argument_list|(
literal|"hits"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|JsonArray
name|json
init|=
name|obj
operator|.
name|getAsJsonArray
argument_list|(
literal|"hits"
argument_list|)
decl_stmt|;
name|results
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|json
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|json
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|toProjectData
argument_list|(
name|json
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|log
operator|.
name|error
argument_list|(
name|result
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|ProjectData
argument_list|>
name|r
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|results
argument_list|)
decl_stmt|;
return|return
operator|new
name|ResultSet
argument_list|<
name|ProjectData
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|ProjectData
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|r
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ProjectData
argument_list|>
name|toList
parameter_list|()
block|{
return|return
name|r
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do nothing.
block|}
block|}
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|readRaw ()
specifier|public
name|ResultSet
argument_list|<
name|FieldBundle
argument_list|>
name|readRaw
parameter_list|()
throws|throws
name|OrmException
block|{
comment|// TOOD(hiesel): Make a generic implementation for Lucene/ES
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|search
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toProjectData (JsonElement json)
specifier|private
name|ProjectData
name|toProjectData
parameter_list|(
name|JsonElement
name|json
parameter_list|)
block|{
name|JsonElement
name|source
init|=
name|json
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"_source"
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
name|source
operator|=
name|json
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
literal|"fields"
argument_list|)
expr_stmt|;
block|}
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|source
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
name|ProjectField
operator|.
name|NAME
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|projectCache
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
operator|.
name|toProjectData
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit


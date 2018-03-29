begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
import|import static
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
name|ChangeField
operator|.
name|APPROVAL_CODEC
import|;
end_import

begin_import
import|import static
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
name|ChangeField
operator|.
name|CHANGE_CODEC
import|;
end_import

begin_import
import|import static
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
name|ChangeField
operator|.
name|PATCH_SET_CODEC
import|;
end_import

begin_import
import|import static
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
name|ChangeIndexRewriter
operator|.
name|CLOSED_STATUSES
import|;
end_import

begin_import
import|import static
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
name|ChangeIndexRewriter
operator|.
name|OPEN_STATUSES
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
operator|.
name|decodeBase64
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
name|FluentIterable
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
name|Iterables
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
operator|.
name|Id
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|ReviewerSet
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
name|FieldDef
operator|.
name|FillArgs
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
name|server
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
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
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
name|change
operator|.
name|ChangeIndexRewriter
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
name|SubmitRuleOptions
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
name|server
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeDataSource
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
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
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
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

begin_comment
comment|/** Secondary index implementation using Elasticsearch. */
end_comment

begin_class
DECL|class|ElasticChangeIndex
class|class
name|ElasticChangeIndex
extends|extends
name|AbstractElasticIndex
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
implements|implements
name|ChangeIndex
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
name|ElasticChangeIndex
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|ChangeMapping
specifier|static
class|class
name|ChangeMapping
block|{
DECL|field|openChanges
name|MappingProperties
name|openChanges
decl_stmt|;
DECL|field|closedChanges
name|MappingProperties
name|closedChanges
decl_stmt|;
DECL|method|ChangeMapping (Schema<ChangeData> schema)
name|ChangeMapping
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
name|MappingProperties
name|mapping
init|=
name|ElasticMapping
operator|.
name|createMapping
argument_list|(
name|schema
argument_list|)
decl_stmt|;
name|this
operator|.
name|openChanges
operator|=
name|mapping
expr_stmt|;
name|this
operator|.
name|closedChanges
operator|=
name|mapping
expr_stmt|;
block|}
block|}
DECL|field|CHANGES
specifier|static
specifier|final
name|String
name|CHANGES
init|=
literal|"changes"
decl_stmt|;
DECL|field|OPEN_CHANGES
specifier|static
specifier|final
name|String
name|OPEN_CHANGES
init|=
literal|"open_"
operator|+
name|CHANGES
decl_stmt|;
DECL|field|CLOSED_CHANGES
specifier|static
specifier|final
name|String
name|CLOSED_CHANGES
init|=
literal|"closed_"
operator|+
name|CHANGES
decl_stmt|;
DECL|field|mapping
specifier|private
specifier|final
name|ChangeMapping
name|mapping
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ElasticChangeIndex ( @erritServerConfig Config cfg, Provider<ReviewDb> db, ChangeData.Factory changeDataFactory, FillArgs fillArgs, SitePaths sitePaths, JestClientBuilder clientBuilder, @Assisted Schema<ChangeData> schema)
name|ElasticChangeIndex
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|FillArgs
name|fillArgs
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|JestClientBuilder
name|clientBuilder
parameter_list|,
annotation|@
name|Assisted
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|fillArgs
argument_list|,
name|sitePaths
argument_list|,
name|schema
argument_list|,
name|clientBuilder
argument_list|,
name|CHANGES
argument_list|)
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|mapping
operator|=
operator|new
name|ChangeMapping
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (ChangeData cd)
specifier|public
name|void
name|replace
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|deleteIndex
decl_stmt|;
name|String
name|insertIndex
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|insertIndex
operator|=
name|OPEN_CHANGES
expr_stmt|;
name|deleteIndex
operator|=
name|CLOSED_CHANGES
expr_stmt|;
block|}
else|else
block|{
name|insertIndex
operator|=
name|CLOSED_CHANGES
expr_stmt|;
name|deleteIndex
operator|=
name|OPEN_CHANGES
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
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
name|CHANGES
argument_list|)
operator|.
name|addAction
argument_list|(
name|insert
argument_list|(
name|insertIndex
argument_list|,
name|cd
argument_list|)
argument_list|)
operator|.
name|addAction
argument_list|(
name|delete
argument_list|(
name|deleteIndex
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
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
literal|"Failed to replace change %s in index %s: %s"
argument_list|,
name|cd
operator|.
name|getId
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
DECL|method|getSource (Predicate<ChangeData> p, QueryOptions opts)
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Set
argument_list|<
name|Change
operator|.
name|Status
argument_list|>
name|statuses
init|=
name|ChangeIndexRewriter
operator|.
name|getPossibleStatus
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|indexes
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Sets
operator|.
name|intersection
argument_list|(
name|statuses
argument_list|,
name|OPEN_STATUSES
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|indexes
operator|.
name|add
argument_list|(
name|OPEN_CHANGES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Sets
operator|.
name|intersection
argument_list|(
name|statuses
argument_list|,
name|CLOSED_STATUSES
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|indexes
operator|.
name|add
argument_list|(
name|CLOSED_CHANGES
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QuerySource
argument_list|(
name|indexes
argument_list|,
name|p
argument_list|,
name|opts
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addActions (Builder builder, Id c)
specifier|protected
name|Builder
name|addActions
parameter_list|(
name|Builder
name|builder
parameter_list|,
name|Id
name|c
parameter_list|)
block|{
return|return
name|builder
operator|.
name|addAction
argument_list|(
name|delete
argument_list|(
name|OPEN_CHANGES
argument_list|,
name|c
argument_list|)
argument_list|)
operator|.
name|addAction
argument_list|(
name|delete
argument_list|(
name|OPEN_CHANGES
argument_list|,
name|c
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
return|return
name|gson
operator|.
name|toJson
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"mappings"
argument_list|,
name|mapping
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getId (ChangeData cd)
specifier|protected
name|String
name|getId
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|QuerySource
specifier|private
class|class
name|QuerySource
implements|implements
name|ChangeDataSource
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
DECL|method|QuerySource (List<String> types, Predicate<ChangeData> p, QueryOptions opts)
name|QuerySource
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|List
argument_list|<
name|Sort
argument_list|>
name|sorts
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|Sort
argument_list|(
name|ChangeField
operator|.
name|UPDATED
operator|.
name|getName
argument_list|()
argument_list|,
name|Sorting
operator|.
name|DESC
argument_list|)
argument_list|,
operator|new
name|Sort
argument_list|(
name|ChangeField
operator|.
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|,
name|Sorting
operator|.
name|DESC
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Sort
name|sort
range|:
name|sorts
control|)
block|{
name|sort
operator|.
name|setIgnoreUnmapped
argument_list|()
expr_stmt|;
block|}
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
name|changeFields
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
name|types
argument_list|)
operator|.
name|addSort
argument_list|(
name|sorts
argument_list|)
operator|.
name|addIndex
argument_list|(
name|indexName
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
name|ChangeData
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
name|ChangeData
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
name|toChangeData
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
name|ChangeData
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
name|ChangeData
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|ChangeData
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
name|ChangeData
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
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
literal|false
return|;
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
DECL|method|toChangeData (JsonElement json)
specifier|private
name|ChangeData
name|toChangeData
parameter_list|(
name|JsonElement
name|json
parameter_list|)
block|{
name|JsonElement
name|sourceElement
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
name|sourceElement
operator|==
literal|null
condition|)
block|{
name|sourceElement
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
name|JsonObject
name|source
init|=
name|sourceElement
operator|.
name|getAsJsonObject
argument_list|()
decl_stmt|;
name|JsonElement
name|c
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|int
name|id
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsInt
argument_list|()
decl_stmt|;
name|String
name|projectName
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsString
argument_list|()
decl_stmt|;
if|if
condition|(
name|projectName
operator|==
literal|null
condition|)
block|{
return|return
name|changeDataFactory
operator|.
name|createOnlyWhenNoteDbDisabled
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
return|return
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|,
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|CHANGE_CODEC
operator|.
name|decode
argument_list|(
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|c
operator|.
name|getAsString
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// Patch sets.
name|cd
operator|.
name|setPatchSets
argument_list|(
name|decodeProtos
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|PATCH_SET
operator|.
name|getName
argument_list|()
argument_list|,
name|PATCH_SET_CODEC
argument_list|)
argument_list|)
expr_stmt|;
comment|// Approvals.
if|if
condition|(
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|APPROVAL
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|cd
operator|.
name|setCurrentApprovals
argument_list|(
name|decodeProtos
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|APPROVAL
operator|.
name|getName
argument_list|()
argument_list|,
name|APPROVAL_CODEC
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fields
operator|.
name|contains
argument_list|(
name|ChangeField
operator|.
name|APPROVAL
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setCurrentApprovals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JsonElement
name|addedElement
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|ADDED
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|JsonElement
name|deletedElement
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|DELETED
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|addedElement
operator|!=
literal|null
operator|&&
name|deletedElement
operator|!=
literal|null
condition|)
block|{
comment|// Changed lines.
name|int
name|added
init|=
name|addedElement
operator|.
name|getAsInt
argument_list|()
decl_stmt|;
name|int
name|deleted
init|=
name|deletedElement
operator|.
name|getAsInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|added
operator|!=
literal|0
operator|&&
name|deleted
operator|!=
literal|0
condition|)
block|{
name|cd
operator|.
name|setChangedLines
argument_list|(
name|added
argument_list|,
name|deleted
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Mergeable.
name|JsonElement
name|mergeableElement
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|MERGEABLE
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mergeableElement
operator|!=
literal|null
condition|)
block|{
name|String
name|mergeable
init|=
name|mergeableElement
operator|.
name|getAsString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"1"
operator|.
name|equals
argument_list|(
name|mergeable
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setMergeable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"0"
operator|.
name|equals
argument_list|(
name|mergeable
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setMergeable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Reviewed-by.
if|if
condition|(
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|REVIEWEDBY
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|JsonArray
name|reviewedBy
init|=
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|REVIEWEDBY
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsJsonArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|reviewedBy
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|reviewedBy
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reviewedBy
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|int
name|aId
init|=
name|reviewedBy
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getAsInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|reviewedBy
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|aId
operator|==
name|ChangeField
operator|.
name|NOT_REVIEWED
condition|)
block|{
break|break;
block|}
name|accounts
operator|.
name|add
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|aId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cd
operator|.
name|setReviewedBy
argument_list|(
name|accounts
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|fields
operator|.
name|contains
argument_list|(
name|ChangeField
operator|.
name|REVIEWEDBY
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setReviewedBy
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|REVIEWER
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|cd
operator|.
name|setReviewers
argument_list|(
name|ChangeField
operator|.
name|parseReviewerFieldValues
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|source
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|REVIEWER
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsJsonArray
argument_list|()
argument_list|)
operator|.
name|transform
argument_list|(
name|JsonElement
operator|::
name|getAsString
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fields
operator|.
name|contains
argument_list|(
name|ChangeField
operator|.
name|REVIEWER
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setReviewers
argument_list|(
name|ReviewerSet
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|decodeSubmitRecords
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|STORED_SUBMIT_RECORD_STRICT
operator|.
name|getName
argument_list|()
argument_list|,
name|ChangeField
operator|.
name|SUBMIT_RULE_OPTIONS_STRICT
argument_list|,
name|cd
argument_list|)
expr_stmt|;
name|decodeSubmitRecords
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|STORED_SUBMIT_RECORD_LENIENT
operator|.
name|getName
argument_list|()
argument_list|,
name|ChangeField
operator|.
name|SUBMIT_RULE_OPTIONS_LENIENT
argument_list|,
name|cd
argument_list|)
expr_stmt|;
name|decodeUnresolvedCommentCount
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|UNRESOLVED_COMMENT_COUNT
operator|.
name|getName
argument_list|()
argument_list|,
name|cd
argument_list|)
expr_stmt|;
if|if
condition|(
name|fields
operator|.
name|contains
argument_list|(
name|ChangeField
operator|.
name|REF_STATE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setRefStates
argument_list|(
name|getByteArray
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|REF_STATE
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fields
operator|.
name|contains
argument_list|(
name|ChangeField
operator|.
name|REF_STATE_PATTERN
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cd
operator|.
name|setRefStatePatterns
argument_list|(
name|getByteArray
argument_list|(
name|source
argument_list|,
name|ChangeField
operator|.
name|REF_STATE_PATTERN
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cd
return|;
block|}
DECL|method|getByteArray (JsonObject source, String name)
specifier|private
name|Iterable
argument_list|<
name|byte
index|[]
argument_list|>
name|getByteArray
parameter_list|(
name|JsonObject
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|JsonElement
name|element
init|=
name|source
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|element
operator|!=
literal|null
condition|?
name|Iterables
operator|.
name|transform
argument_list|(
name|element
operator|.
name|getAsJsonArray
argument_list|()
argument_list|,
name|e
lambda|->
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|e
operator|.
name|getAsString
argument_list|()
argument_list|)
argument_list|)
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
DECL|method|decodeSubmitRecords ( JsonObject doc, String fieldName, SubmitRuleOptions opts, ChangeData out)
specifier|private
name|void
name|decodeSubmitRecords
parameter_list|(
name|JsonObject
name|doc
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|SubmitRuleOptions
name|opts
parameter_list|,
name|ChangeData
name|out
parameter_list|)
block|{
name|JsonArray
name|records
init|=
name|doc
operator|.
name|getAsJsonArray
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
if|if
condition|(
name|records
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ChangeField
operator|.
name|parseSubmitRecords
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|records
argument_list|)
operator|.
name|transform
argument_list|(
name|i
lambda|->
operator|new
name|String
argument_list|(
name|decodeBase64
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|,
name|opts
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|decodeUnresolvedCommentCount (JsonObject doc, String fieldName, ChangeData out)
specifier|private
name|void
name|decodeUnresolvedCommentCount
parameter_list|(
name|JsonObject
name|doc
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|ChangeData
name|out
parameter_list|)
block|{
name|JsonElement
name|count
init|=
name|doc
operator|.
name|get
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|out
operator|.
name|setUnresolvedCommentCount
argument_list|(
name|count
operator|.
name|getAsInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|account
operator|.
name|AccountField
operator|.
name|ID
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
name|elasticsearch
operator|.
name|builders
operator|.
name|QueryBuilder
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
name|builders
operator|.
name|SearchSourceBuilder
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
name|bulk
operator|.
name|BulkRequest
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
name|bulk
operator|.
name|IndexRequest
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
name|bulk
operator|.
name|UpdateRequest
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
name|server
operator|.
name|account
operator|.
name|AccountCache
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
name|account
operator|.
name|AccountState
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
name|account
operator|.
name|AccountField
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
name|gson
operator|.
name|JsonParser
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
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|StatusLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Response
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
DECL|class|ElasticAccountIndex
specifier|public
class|class
name|ElasticAccountIndex
extends|extends
name|AbstractElasticIndex
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
implements|implements
name|AccountIndex
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
name|ElasticAccountIndex
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|AccountMapping
specifier|static
class|class
name|AccountMapping
block|{
DECL|field|accounts
specifier|final
name|MappingProperties
name|accounts
decl_stmt|;
DECL|method|AccountMapping (Schema<AccountState> schema, ElasticQueryAdapter adapter)
name|AccountMapping
parameter_list|(
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
parameter_list|,
name|ElasticQueryAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|ElasticMapping
operator|.
name|createMapping
argument_list|(
name|schema
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|ACCOUNTS
specifier|private
specifier|static
specifier|final
name|String
name|ACCOUNTS
init|=
literal|"accounts"
decl_stmt|;
DECL|field|mapping
specifier|private
specifier|final
name|AccountMapping
name|mapping
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|Provider
argument_list|<
name|AccountCache
argument_list|>
name|accountCache
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ElasticAccountIndex ( ElasticConfiguration cfg, SitePaths sitePaths, Provider<AccountCache> accountCache, ElasticRestClientProvider client, @Assisted Schema<AccountState> schema)
name|ElasticAccountIndex
parameter_list|(
name|ElasticConfiguration
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|Provider
argument_list|<
name|AccountCache
argument_list|>
name|accountCache
parameter_list|,
name|ElasticRestClientProvider
name|client
parameter_list|,
annotation|@
name|Assisted
name|Schema
argument_list|<
name|AccountState
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
name|client
argument_list|,
name|ACCOUNTS
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|mapping
operator|=
operator|new
name|AccountMapping
argument_list|(
name|schema
argument_list|,
name|client
operator|.
name|adapter
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (AccountState as)
specifier|public
name|void
name|replace
parameter_list|(
name|AccountState
name|as
parameter_list|)
throws|throws
name|IOException
block|{
name|BulkRequest
name|bulk
init|=
operator|new
name|IndexRequest
argument_list|(
name|getId
argument_list|(
name|as
argument_list|)
argument_list|,
name|indexName
argument_list|,
name|type
argument_list|,
name|client
operator|.
name|adapter
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|UpdateRequest
argument_list|<>
argument_list|(
name|schema
argument_list|,
name|as
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
name|getURI
argument_list|(
name|type
argument_list|,
name|BULK
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|postRequest
argument_list|(
name|bulk
argument_list|,
name|uri
argument_list|,
name|getRefreshParam
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|statusCode
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|statusCode
operator|!=
name|HttpStatus
operator|.
name|SC_OK
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
literal|"Failed to replace account %s in index %s: %s"
argument_list|,
name|as
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|indexName
argument_list|,
name|statusCode
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<AccountState> p, QueryOptions opts)
specifier|public
name|DataSource
argument_list|<
name|AccountState
argument_list|>
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|AccountState
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
DECL|method|getDeleteActions (Account.Id a)
specifier|protected
name|String
name|getDeleteActions
parameter_list|(
name|Account
operator|.
name|Id
name|a
parameter_list|)
block|{
return|return
name|delete
argument_list|(
name|type
argument_list|,
name|a
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
name|getMappingsForSingleType
argument_list|(
name|ACCOUNTS
argument_list|,
name|mapping
operator|.
name|accounts
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getId (AccountState as)
specifier|protected
name|String
name|getId
parameter_list|(
name|AccountState
name|as
parameter_list|)
block|{
return|return
name|as
operator|.
name|getAccount
argument_list|()
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
name|DataSource
argument_list|<
name|AccountState
argument_list|>
block|{
DECL|field|search
specifier|private
specifier|final
name|String
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
DECL|method|QuerySource (Predicate<AccountState> p, QueryOptions opts)
name|QuerySource
parameter_list|(
name|Predicate
argument_list|<
name|AccountState
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
name|accountFields
argument_list|(
name|opts
argument_list|)
expr_stmt|;
name|SearchSourceBuilder
name|searchSource
init|=
operator|new
name|SearchSourceBuilder
argument_list|(
name|client
operator|.
name|adapter
argument_list|()
argument_list|)
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
name|JsonArray
name|sortArray
init|=
name|getSortArray
argument_list|(
name|AccountField
operator|.
name|ID
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|search
operator|=
name|getSearch
argument_list|(
name|searchSource
argument_list|,
name|sortArray
argument_list|)
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
name|AccountState
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
name|AccountState
argument_list|>
name|results
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|getURI
argument_list|(
name|type
argument_list|,
name|SEARCH
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|postRequest
argument_list|(
name|search
argument_list|,
name|uri
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|StatusLine
name|statusLine
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|statusLine
operator|.
name|getStatusCode
argument_list|()
operator|==
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
name|String
name|content
init|=
name|getContent
argument_list|(
name|response
argument_list|)
decl_stmt|;
name|JsonObject
name|obj
init|=
operator|new
name|JsonParser
argument_list|()
operator|.
name|parse
argument_list|(
name|content
argument_list|)
operator|.
name|getAsJsonObject
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
name|toAccountState
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
name|statusLine
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|AccountState
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
name|AccountState
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|AccountState
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
name|AccountState
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
DECL|method|toAccountState (JsonElement json)
specifier|private
name|AccountState
name|toAccountState
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
name|Account
operator|.
name|Id
name|id
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|source
operator|.
name|getAsJsonObject
argument_list|()
operator|.
name|get
argument_list|(
name|ID
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getAsInt
argument_list|()
argument_list|)
decl_stmt|;
comment|// Use the AccountCache rather than depending on any stored fields in the
comment|// document (of which there shouldn't be any). The most expensive part to
comment|// compute anyway is the effective group IDs, and we don't have a good way
comment|// to reindex when those change.
return|return
name|accountCache
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


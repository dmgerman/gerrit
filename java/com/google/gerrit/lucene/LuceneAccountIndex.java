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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
operator|.
name|getOnlyElement
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
name|account
operator|.
name|AccountField
operator|.
name|FULL_NAME
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
name|account
operator|.
name|AccountField
operator|.
name|ID
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
name|account
operator|.
name|AccountField
operator|.
name|PREFERRED_EMAIL_EXACT
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
name|exceptions
operator|.
name|StorageException
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
name|FieldDef
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
name|Schema
operator|.
name|Values
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
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
name|document
operator|.
name|Document
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
name|document
operator|.
name|NumericDocValuesField
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
name|document
operator|.
name|SortedDocValuesField
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
name|index
operator|.
name|Term
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
name|SearcherFactory
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
name|Sort
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
name|SortField
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
name|store
operator|.
name|Directory
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
name|store
operator|.
name|FSDirectory
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
name|store
operator|.
name|RAMDirectory
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
name|util
operator|.
name|BytesRef
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
DECL|class|LuceneAccountIndex
specifier|public
class|class
name|LuceneAccountIndex
extends|extends
name|AbstractLuceneIndex
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
DECL|field|ACCOUNTS
specifier|private
specifier|static
specifier|final
name|String
name|ACCOUNTS
init|=
literal|"accounts"
decl_stmt|;
DECL|field|FULL_NAME_SORT_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|FULL_NAME_SORT_FIELD
init|=
name|sortFieldName
argument_list|(
name|FULL_NAME
argument_list|)
decl_stmt|;
DECL|field|EMAIL_SORT_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|EMAIL_SORT_FIELD
init|=
name|sortFieldName
argument_list|(
name|PREFERRED_EMAIL_EXACT
argument_list|)
decl_stmt|;
DECL|field|ID_SORT_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|ID_SORT_FIELD
init|=
name|sortFieldName
argument_list|(
name|ID
argument_list|)
decl_stmt|;
DECL|method|idTerm (AccountState as)
specifier|private
specifier|static
name|Term
name|idTerm
parameter_list|(
name|AccountState
name|as
parameter_list|)
block|{
return|return
name|idTerm
argument_list|(
name|as
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|idTerm (Account.Id id)
specifier|private
specifier|static
name|Term
name|idTerm
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|QueryBuilder
operator|.
name|intTerm
argument_list|(
name|ID
operator|.
name|getName
argument_list|()
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|field|indexWriterConfig
specifier|private
specifier|final
name|GerritIndexWriterConfig
name|indexWriterConfig
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|QueryBuilder
argument_list|<
name|AccountState
argument_list|>
name|queryBuilder
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
DECL|method|dir (Schema<AccountState> schema, Config cfg, SitePaths sitePaths)
specifier|private
specifier|static
name|Directory
name|dir
parameter_list|(
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
parameter_list|,
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LuceneIndexModule
operator|.
name|isInMemoryTest
argument_list|(
name|cfg
argument_list|)
condition|)
block|{
return|return
operator|new
name|RAMDirectory
argument_list|()
return|;
block|}
name|Path
name|indexDir
init|=
name|LuceneVersionManager
operator|.
name|getDir
argument_list|(
name|sitePaths
argument_list|,
name|ACCOUNTS
argument_list|,
name|schema
argument_list|)
decl_stmt|;
return|return
name|FSDirectory
operator|.
name|open
argument_list|(
name|indexDir
argument_list|)
return|;
block|}
annotation|@
name|Inject
DECL|method|LuceneAccountIndex ( @erritServerConfig Config cfg, SitePaths sitePaths, Provider<AccountCache> accountCache, @Assisted Schema<AccountState> schema)
name|LuceneAccountIndex
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
name|AccountCache
argument_list|>
name|accountCache
parameter_list|,
annotation|@
name|Assisted
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|sitePaths
argument_list|,
name|dir
argument_list|(
name|schema
argument_list|,
name|cfg
argument_list|,
name|sitePaths
argument_list|)
argument_list|,
name|ACCOUNTS
argument_list|,
literal|null
argument_list|,
operator|new
name|GerritIndexWriterConfig
argument_list|(
name|cfg
argument_list|,
name|ACCOUNTS
argument_list|)
argument_list|,
operator|new
name|SearcherFactory
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|indexWriterConfig
operator|=
operator|new
name|GerritIndexWriterConfig
argument_list|(
name|cfg
argument_list|,
name|ACCOUNTS
argument_list|)
expr_stmt|;
name|queryBuilder
operator|=
operator|new
name|QueryBuilder
argument_list|<>
argument_list|(
name|schema
argument_list|,
name|indexWriterConfig
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (Document doc, Values<AccountState> values)
name|void
name|add
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Values
argument_list|<
name|AccountState
argument_list|>
name|values
parameter_list|)
block|{
comment|// Add separate DocValues fields for those fields needed for sorting.
name|FieldDef
argument_list|<
name|AccountState
argument_list|,
name|?
argument_list|>
name|f
init|=
name|values
operator|.
name|getField
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|==
name|ID
condition|)
block|{
name|int
name|v
init|=
operator|(
name|Integer
operator|)
name|getOnlyElement
argument_list|(
name|values
operator|.
name|getValues
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|NumericDocValuesField
argument_list|(
name|ID_SORT_FIELD
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|f
operator|==
name|FULL_NAME
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|getOnlyElement
argument_list|(
name|values
operator|.
name|getValues
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|SortedDocValuesField
argument_list|(
name|FULL_NAME_SORT_FIELD
argument_list|,
operator|new
name|BytesRef
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|f
operator|==
name|PREFERRED_EMAIL_EXACT
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|getOnlyElement
argument_list|(
name|values
operator|.
name|getValues
argument_list|()
argument_list|)
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|SortedDocValuesField
argument_list|(
name|EMAIL_SORT_FIELD
argument_list|,
operator|new
name|BytesRef
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|add
argument_list|(
name|doc
argument_list|,
name|values
argument_list|)
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
block|{
try|try
block|{
name|replace
argument_list|(
name|idTerm
argument_list|(
name|as
argument_list|)
argument_list|,
name|toDocument
argument_list|(
name|as
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|delete (Account.Id key)
specifier|public
name|void
name|delete
parameter_list|(
name|Account
operator|.
name|Id
name|key
parameter_list|)
block|{
try|try
block|{
name|delete
argument_list|(
name|idTerm
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|e
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
name|LuceneQuerySource
argument_list|(
name|opts
operator|.
name|filterFields
argument_list|(
name|IndexUtils
operator|::
name|accountFields
argument_list|)
argument_list|,
name|queryBuilder
operator|.
name|toQuery
argument_list|(
name|p
argument_list|)
argument_list|,
name|getSort
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSort ()
specifier|private
name|Sort
name|getSort
parameter_list|()
block|{
return|return
operator|new
name|Sort
argument_list|(
operator|new
name|SortField
argument_list|(
name|FULL_NAME_SORT_FIELD
argument_list|,
name|SortField
operator|.
name|Type
operator|.
name|STRING
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|SortField
argument_list|(
name|EMAIL_SORT_FIELD
argument_list|,
name|SortField
operator|.
name|Type
operator|.
name|STRING
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|SortField
argument_list|(
name|ID_SORT_FIELD
argument_list|,
name|SortField
operator|.
name|Type
operator|.
name|LONG
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|fromDocument (Document doc)
specifier|protected
name|AccountState
name|fromDocument
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
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
name|doc
operator|.
name|getField
argument_list|(
name|ID
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|numericValue
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
comment|// Use the AccountCache rather than depending on any stored fields in the document (of which
comment|// there shouldn't be any). The most expensive part to compute anyway is the effective group
comment|// IDs, and we don't have a good way to reindex when those change.
comment|// If the account doesn't exist return an empty AccountState to represent the missing account
comment|// to account the fact that the account exists in the index.
return|return
name|accountCache
operator|.
name|get
argument_list|()
operator|.
name|getEvenIfMissing
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit


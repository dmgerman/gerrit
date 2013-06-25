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
comment|// limitations under the License.package com.google.gerrit.server.git;
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
operator|.
name|FIELD_CHANGE
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
name|query
operator|.
name|change
operator|.
name|IndexRewriteImpl
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
name|query
operator|.
name|change
operator|.
name|IndexRewriteImpl
operator|.
name|OPEN_STATUSES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST_NOT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningScheduledExecutorService
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
name|extensions
operator|.
name|events
operator|.
name|LifecycleListener
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
name|FieldType
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
name|IndexPredicate
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
name|AndPredicate
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
name|NotPredicate
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
name|OrPredicate
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|IndexRewriteImpl
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
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
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
name|Field
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
name|IntField
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
name|StringField
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
name|IndexWriter
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
name|IndexWriterConfig
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
name|IndexWriterConfig
operator|.
name|OpenMode
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
name|BooleanClause
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
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|IndexSearcher
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
name|Query
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
name|ScoreDoc
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
name|SearcherManager
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
name|TermQuery
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
name|TopDocs
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
name|apache
operator|.
name|lucene
operator|.
name|util
operator|.
name|NumericUtils
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
name|Version
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Future
import|;
end_import

begin_comment
comment|/**  * Secondary index implementation using Apache Lucene.  *<p>  * Writes are managed using a single {@link IndexWriter} per process, committed  * aggressively. Reads use {@link SearcherManager} and periodically refresh,  * though there may be some lag between a committed write and it showing up to  * other threads' searchers.  */
end_comment

begin_class
DECL|class|LuceneChangeIndex
specifier|public
class|class
name|LuceneChangeIndex
implements|implements
name|ChangeIndex
implements|,
name|LifecycleListener
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
name|LuceneChangeIndex
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LUCENE_VERSION
specifier|public
specifier|static
specifier|final
name|Version
name|LUCENE_VERSION
init|=
name|Version
operator|.
name|LUCENE_43
decl_stmt|;
DECL|field|CHANGES_OPEN
specifier|public
specifier|static
specifier|final
name|String
name|CHANGES_OPEN
init|=
literal|"changes_open"
decl_stmt|;
DECL|field|CHANGES_CLOSED
specifier|public
specifier|static
specifier|final
name|String
name|CHANGES_CLOSED
init|=
literal|"changes_closed"
decl_stmt|;
DECL|method|getIndexWriterConfig (Config cfg, String name)
specifier|private
specifier|static
name|IndexWriterConfig
name|getIndexWriterConfig
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|IndexWriterConfig
name|writerConfig
init|=
operator|new
name|IndexWriterConfig
argument_list|(
name|LUCENE_VERSION
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|(
name|LUCENE_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|writerConfig
operator|.
name|setOpenMode
argument_list|(
name|OpenMode
operator|.
name|CREATE_OR_APPEND
argument_list|)
expr_stmt|;
name|double
name|m
init|=
literal|1
operator|<<
literal|20
decl_stmt|;
name|writerConfig
operator|.
name|setRAMBufferSizeMB
argument_list|(
name|cfg
operator|.
name|getLong
argument_list|(
literal|"index"
argument_list|,
name|name
argument_list|,
literal|"ramBufferSize"
argument_list|,
call|(
name|long
call|)
argument_list|(
name|IndexWriterConfig
operator|.
name|DEFAULT_RAM_BUFFER_SIZE_MB
operator|*
name|m
argument_list|)
argument_list|)
operator|/
name|m
argument_list|)
expr_stmt|;
name|writerConfig
operator|.
name|setMaxBufferedDocs
argument_list|(
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
name|name
argument_list|,
literal|"maxBufferedDocs"
argument_list|,
name|IndexWriterConfig
operator|.
name|DEFAULT_MAX_BUFFERED_DOCS
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writerConfig
return|;
block|}
DECL|field|refreshThread
specifier|private
specifier|final
name|RefreshThread
name|refreshThread
decl_stmt|;
DECL|field|fillArgs
specifier|private
specifier|final
name|FillArgs
name|fillArgs
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|readOnly
specifier|private
specifier|final
name|boolean
name|readOnly
decl_stmt|;
DECL|field|openIndex
specifier|private
specifier|final
name|SubIndex
name|openIndex
decl_stmt|;
DECL|field|closedIndex
specifier|private
specifier|final
name|SubIndex
name|closedIndex
decl_stmt|;
DECL|method|LuceneChangeIndex (Config cfg, SitePaths sitePaths, ListeningScheduledExecutorService executor, FillArgs fillArgs, boolean readOnly)
name|LuceneChangeIndex
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|ListeningScheduledExecutorService
name|executor
parameter_list|,
name|FillArgs
name|fillArgs
parameter_list|,
name|boolean
name|readOnly
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|refreshThread
operator|=
operator|new
name|RefreshThread
argument_list|()
expr_stmt|;
name|this
operator|.
name|fillArgs
operator|=
name|fillArgs
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|readOnly
operator|=
name|readOnly
expr_stmt|;
name|openIndex
operator|=
operator|new
name|SubIndex
argument_list|(
operator|new
name|File
argument_list|(
name|sitePaths
operator|.
name|index_dir
argument_list|,
name|CHANGES_OPEN
argument_list|)
argument_list|,
name|getIndexWriterConfig
argument_list|(
name|cfg
argument_list|,
literal|"changes_open"
argument_list|)
argument_list|)
expr_stmt|;
name|closedIndex
operator|=
operator|new
name|SubIndex
argument_list|(
operator|new
name|File
argument_list|(
name|sitePaths
operator|.
name|index_dir
argument_list|,
name|CHANGES_CLOSED
argument_list|)
argument_list|,
name|getIndexWriterConfig
argument_list|(
name|cfg
argument_list|,
literal|"changes_closed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|refreshThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|refreshThread
operator|.
name|halt
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|closeFutures
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|closeFutures
operator|.
name|add
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|openIndex
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|closeFutures
operator|.
name|add
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|closedIndex
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Future
argument_list|<
name|?
argument_list|>
name|future
range|:
name|closeFutures
control|)
block|{
name|Futures
operator|.
name|getUnchecked
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|insert (ChangeData cd)
specifier|public
name|void
name|insert
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
name|Term
name|id
init|=
name|idTerm
argument_list|(
name|cd
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|toDocument
argument_list|(
name|cd
argument_list|)
decl_stmt|;
if|if
condition|(
name|readOnly
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|cd
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|closedIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|openIndex
operator|.
name|insert
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|openIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|closedIndex
operator|.
name|insert
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
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
name|Term
name|id
init|=
name|idTerm
argument_list|(
name|cd
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|toDocument
argument_list|(
name|cd
argument_list|)
decl_stmt|;
if|if
condition|(
name|readOnly
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|cd
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|closedIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|openIndex
operator|.
name|replace
argument_list|(
name|id
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|openIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|closedIndex
operator|.
name|replace
argument_list|(
name|id
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|delete (ChangeData cd)
specifier|public
name|void
name|delete
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
name|Term
name|id
init|=
name|idTerm
argument_list|(
name|cd
argument_list|)
decl_stmt|;
if|if
condition|(
name|readOnly
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|cd
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|openIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|closedIndex
operator|.
name|delete
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<ChangeData> p)
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
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
name|IndexRewriteImpl
operator|.
name|getPossibleStatus
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubIndex
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
name|openIndex
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
name|closedIndex
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QuerySource
argument_list|(
name|indexes
argument_list|,
name|toQuery
argument_list|(
name|p
argument_list|)
argument_list|)
return|;
block|}
DECL|method|idTerm (ChangeData cd)
specifier|private
name|Term
name|idTerm
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|intTerm
argument_list|(
name|FIELD_CHANGE
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toQuery (Predicate<ChangeData> p)
specifier|private
name|Query
name|toQuery
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|p
operator|.
name|getClass
argument_list|()
operator|==
name|AndPredicate
operator|.
name|class
condition|)
block|{
return|return
name|booleanQuery
argument_list|(
name|p
argument_list|,
name|MUST
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|getClass
argument_list|()
operator|==
name|OrPredicate
operator|.
name|class
condition|)
block|{
return|return
name|booleanQuery
argument_list|(
name|p
argument_list|,
name|SHOULD
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|getClass
argument_list|()
operator|==
name|NotPredicate
operator|.
name|class
condition|)
block|{
return|return
name|booleanQuery
argument_list|(
name|p
argument_list|,
name|MUST_NOT
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|instanceof
name|IndexPredicate
condition|)
block|{
return|return
name|fieldQuery
argument_list|(
operator|(
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
operator|)
name|p
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"Cannot convert to index predicate: "
operator|+
name|p
argument_list|)
throw|;
block|}
block|}
DECL|method|booleanQuery (Predicate<ChangeData> p, BooleanClause.Occur o)
specifier|private
name|Query
name|booleanQuery
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|BooleanClause
operator|.
name|Occur
name|o
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
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
name|p
operator|.
name|getChildCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|q
operator|.
name|add
argument_list|(
name|toQuery
argument_list|(
name|p
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|q
return|;
block|}
DECL|method|fieldQuery (IndexPredicate<ChangeData> p)
specifier|private
name|Query
name|fieldQuery
parameter_list|(
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|p
operator|.
name|getType
argument_list|()
operator|==
name|FieldType
operator|.
name|INTEGER
condition|)
block|{
return|return
name|intQuery
argument_list|(
name|p
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|getType
argument_list|()
operator|==
name|FieldType
operator|.
name|EXACT
condition|)
block|{
return|return
name|exactQuery
argument_list|(
name|p
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
name|badFieldType
argument_list|(
name|p
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|intTerm (String name, int value)
specifier|private
name|Term
name|intTerm
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|BytesRef
name|bytes
init|=
operator|new
name|BytesRef
argument_list|(
name|NumericUtils
operator|.
name|BUF_SIZE_INT
argument_list|)
decl_stmt|;
name|NumericUtils
operator|.
name|intToPrefixCodedBytes
argument_list|(
name|value
argument_list|,
literal|0
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
return|return
operator|new
name|Term
argument_list|(
name|name
argument_list|,
name|bytes
argument_list|)
return|;
block|}
DECL|method|intQuery (IndexPredicate<ChangeData> p)
specifier|private
name|Query
name|intQuery
parameter_list|(
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|int
name|value
decl_stmt|;
try|try
block|{
comment|// Can't use IntPredicate because it and IndexPredicate are different
comment|// subclasses of OperatorPredicate.
name|value
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"not an integer: "
operator|+
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|TermQuery
argument_list|(
name|intTerm
argument_list|(
name|p
operator|.
name|getOperator
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|exactQuery (IndexPredicate<ChangeData> p)
specifier|private
name|Query
name|exactQuery
parameter_list|(
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|p
operator|.
name|getOperator
argument_list|()
argument_list|,
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|class|QuerySource
specifier|private
class|class
name|QuerySource
implements|implements
name|ChangeDataSource
block|{
comment|// TODO(dborowitz): Push limit down from predicate tree.
DECL|field|LIMIT
specifier|private
specifier|static
specifier|final
name|int
name|LIMIT
init|=
literal|1000
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|List
argument_list|<
name|SubIndex
argument_list|>
name|indexes
decl_stmt|;
DECL|field|query
specifier|private
specifier|final
name|Query
name|query
decl_stmt|;
DECL|method|QuerySource (List<SubIndex> indexes, Query query)
specifier|public
name|QuerySource
parameter_list|(
name|List
argument_list|<
name|SubIndex
argument_list|>
name|indexes
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
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
comment|// TODO(dborowitz): estimate from Lucene?
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
name|IndexSearcher
index|[]
name|searchers
init|=
operator|new
name|IndexSearcher
index|[
name|indexes
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
try|try
block|{
name|TopDocs
index|[]
name|hits
init|=
operator|new
name|TopDocs
index|[
name|indexes
operator|.
name|size
argument_list|()
index|]
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
name|indexes
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|searchers
index|[
name|i
index|]
operator|=
name|indexes
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|hits
index|[
name|i
index|]
operator|=
name|searchers
index|[
name|i
index|]
operator|.
name|search
argument_list|(
name|query
argument_list|,
name|LIMIT
argument_list|)
expr_stmt|;
block|}
name|TopDocs
name|docs
init|=
name|TopDocs
operator|.
name|merge
argument_list|(
literal|null
argument_list|,
name|LIMIT
argument_list|,
name|hits
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|docs
operator|.
name|scoreDocs
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|ScoreDoc
name|sd
range|:
name|docs
operator|.
name|scoreDocs
control|)
block|{
name|Document
name|doc
init|=
name|searchers
index|[
name|sd
operator|.
name|shardIndex
index|]
operator|.
name|doc
argument_list|(
name|sd
operator|.
name|doc
argument_list|)
decl_stmt|;
name|Number
name|v
init|=
name|doc
operator|.
name|getField
argument_list|(
name|FIELD_CHANGE
argument_list|)
operator|.
name|numericValue
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|ChangeData
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|v
operator|.
name|intValue
argument_list|()
argument_list|)
argument_list|)
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
name|result
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
finally|finally
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indexes
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|searchers
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|indexes
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|release
argument_list|(
name|searchers
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"cannot release Lucene searcher"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
DECL|method|toDocument (ChangeData cd)
specifier|private
name|Document
name|toDocument
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Document
name|result
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
for|for
control|(
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|f
range|:
name|ChangeField
operator|.
name|ALL
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|isRepeatable
argument_list|()
condition|)
block|{
name|add
argument_list|(
name|result
argument_list|,
name|f
argument_list|,
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|f
operator|.
name|get
argument_list|(
name|cd
argument_list|,
name|fillArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|val
init|=
name|f
operator|.
name|get
argument_list|(
name|cd
argument_list|,
name|fillArgs
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|add
argument_list|(
name|result
argument_list|,
name|f
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
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
block|}
DECL|method|add (Document doc, FieldDef<ChangeData, ?> f, Iterable<?> values)
specifier|private
name|void
name|add
parameter_list|(
name|Document
name|doc
parameter_list|,
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|f
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|f
operator|.
name|getType
argument_list|()
operator|==
name|FieldType
operator|.
name|INTEGER
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|IntField
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|Integer
operator|)
name|value
argument_list|,
name|store
argument_list|(
name|f
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|f
operator|.
name|getType
argument_list|()
operator|==
name|FieldType
operator|.
name|EXACT
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|StringField
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|,
name|store
argument_list|(
name|f
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
name|badFieldType
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|store (FieldDef<?, ?> f)
specifier|private
specifier|static
name|Field
operator|.
name|Store
name|store
parameter_list|(
name|FieldDef
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|f
parameter_list|)
block|{
return|return
name|f
operator|.
name|isStored
argument_list|()
condition|?
name|Field
operator|.
name|Store
operator|.
name|YES
else|:
name|Field
operator|.
name|Store
operator|.
name|NO
return|;
block|}
DECL|method|badFieldType (FieldType<?> t)
specifier|private
specifier|static
name|IllegalArgumentException
name|badFieldType
parameter_list|(
name|FieldType
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown index field type "
operator|+
name|t
argument_list|)
return|;
block|}
DECL|class|RefreshThread
specifier|private
class|class
name|RefreshThread
extends|extends
name|Thread
block|{
DECL|field|stop
specifier|private
name|boolean
name|stop
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
operator|!
name|stop
condition|)
block|{
name|openIndex
operator|.
name|maybeRefresh
argument_list|()
expr_stmt|;
name|closedIndex
operator|.
name|maybeRefresh
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
try|try
block|{
name|wait
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"error refreshing index searchers"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|halt ()
name|void
name|halt
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|stop
operator|=
literal|true
expr_stmt|;
name|notify
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|join
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"error stopping refresh thread"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


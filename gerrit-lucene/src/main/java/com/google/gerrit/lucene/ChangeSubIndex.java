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
name|lucene
operator|.
name|LuceneChangeIndex
operator|.
name|ID_SORT_FIELD
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
name|lucene
operator|.
name|LuceneChangeIndex
operator|.
name|UPDATED_SORT_FIELD
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
name|ChangeSchemaDefinitions
operator|.
name|NAME
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
name|change
operator|.
name|ChangeData
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
name|sql
operator|.
name|Timestamp
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

begin_class
DECL|class|ChangeSubIndex
specifier|public
class|class
name|ChangeSubIndex
extends|extends
name|AbstractLuceneIndex
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
DECL|method|ChangeSubIndex ( Schema<ChangeData> schema, SitePaths sitePaths, Path path, GerritIndexWriterConfig writerConfig, SearcherFactory searcherFactory)
name|ChangeSubIndex
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|Path
name|path
parameter_list|,
name|GerritIndexWriterConfig
name|writerConfig
parameter_list|,
name|SearcherFactory
name|searcherFactory
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|schema
argument_list|,
name|sitePaths
argument_list|,
name|FSDirectory
operator|.
name|open
argument_list|(
name|path
argument_list|)
argument_list|,
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|writerConfig
argument_list|,
name|searcherFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|ChangeSubIndex ( Schema<ChangeData> schema, SitePaths sitePaths, Directory dir, String subIndex, GerritIndexWriterConfig writerConfig, SearcherFactory searcherFactory)
name|ChangeSubIndex
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|Directory
name|dir
parameter_list|,
name|String
name|subIndex
parameter_list|,
name|GerritIndexWriterConfig
name|writerConfig
parameter_list|,
name|SearcherFactory
name|searcherFactory
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
argument_list|,
name|NAME
argument_list|,
name|subIndex
argument_list|,
name|writerConfig
argument_list|,
name|searcherFactory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (ChangeData obj)
specifier|public
name|void
name|replace
parameter_list|(
name|ChangeData
name|obj
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"don't use ChangeSubIndex directly"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|delete (Change.Id key)
specifier|public
name|void
name|delete
parameter_list|(
name|Change
operator|.
name|Id
name|key
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"don't use ChangeSubIndex directly"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<ChangeData> p, QueryOptions opts)
specifier|public
name|DataSource
argument_list|<
name|ChangeData
argument_list|>
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"don't use ChangeSubIndex directly"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|add (Document doc, Values<ChangeData> values)
name|void
name|add
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Values
argument_list|<
name|ChangeData
argument_list|>
name|values
parameter_list|)
block|{
comment|// Add separate DocValues fields for those fields needed for sorting.
name|FieldDef
argument_list|<
name|ChangeData
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
name|ChangeField
operator|.
name|LEGACY_ID
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
name|ChangeField
operator|.
name|UPDATED
condition|)
block|{
name|long
name|t
init|=
operator|(
operator|(
name|Timestamp
operator|)
name|getOnlyElement
argument_list|(
name|values
operator|.
name|getValues
argument_list|()
argument_list|)
operator|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|NumericDocValuesField
argument_list|(
name|UPDATED_SORT_FIELD
argument_list|,
name|t
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
block|}
end_class

end_unit


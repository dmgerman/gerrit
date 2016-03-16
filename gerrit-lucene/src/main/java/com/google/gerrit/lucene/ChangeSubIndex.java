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
DECL|method|ChangeSubIndex ( Schema<ChangeData> schema, SitePaths sitePaths, Directory dir, String name, GerritIndexWriterConfig writerConfig, SearcherFactory searcherFactory)
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
name|name
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
name|name
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
block|}
end_class

end_unit


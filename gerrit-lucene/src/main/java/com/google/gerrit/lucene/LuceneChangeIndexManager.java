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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Throwables
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
name|cache
operator|.
name|CacheBuilder
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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|LuceneChangeIndexManager
class|class
name|LuceneChangeIndexManager
implements|implements
name|ChangeIndex
operator|.
name|Manager
implements|,
name|LifecycleListener
block|{
DECL|field|indexes
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|LuceneChangeIndex
argument_list|>
name|indexes
decl_stmt|;
annotation|@
name|Inject
DECL|method|LuceneChangeIndexManager (final SitePaths sitePaths, final FillArgs fillArgs)
name|LuceneChangeIndexManager
parameter_list|(
specifier|final
name|SitePaths
name|sitePaths
parameter_list|,
specifier|final
name|FillArgs
name|fillArgs
parameter_list|)
block|{
name|indexes
operator|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|(
operator|new
name|CacheLoader
argument_list|<
name|String
argument_list|,
name|LuceneChangeIndex
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|LuceneChangeIndex
name|load
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|LuceneChangeIndex
argument_list|(
operator|new
name|File
argument_list|(
name|sitePaths
operator|.
name|index_dir
argument_list|,
name|key
argument_list|)
argument_list|,
name|fillArgs
argument_list|)
return|;
block|}
block|}
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
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
for|for
control|(
name|LuceneChangeIndex
name|index
range|:
name|indexes
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|index
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (String name)
specifier|public
name|LuceneChangeIndex
name|get
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
return|return
name|indexes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|propagateIfInstanceOf
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


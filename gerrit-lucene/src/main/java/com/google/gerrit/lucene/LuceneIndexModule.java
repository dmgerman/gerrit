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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|IndexExecutor
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
name|IndexModule
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
name|Provides
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|LuceneIndexModule
specifier|public
class|class
name|LuceneIndexModule
extends|extends
name|LifecycleModule
block|{
DECL|field|checkVersion
specifier|private
specifier|final
name|boolean
name|checkVersion
decl_stmt|;
DECL|field|threads
specifier|private
specifier|final
name|int
name|threads
decl_stmt|;
DECL|field|readOnly
specifier|private
specifier|final
name|boolean
name|readOnly
decl_stmt|;
DECL|method|LuceneIndexModule ()
specifier|public
name|LuceneIndexModule
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|LuceneIndexModule (boolean checkVersion, int threads, boolean readOnly)
specifier|public
name|LuceneIndexModule
parameter_list|(
name|boolean
name|checkVersion
parameter_list|,
name|int
name|threads
parameter_list|,
name|boolean
name|readOnly
parameter_list|)
block|{
name|this
operator|.
name|checkVersion
operator|=
name|checkVersion
expr_stmt|;
name|this
operator|.
name|threads
operator|=
name|threads
expr_stmt|;
name|this
operator|.
name|readOnly
operator|=
name|readOnly
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|IndexModule
argument_list|(
name|threads
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ChangeIndex
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|LuceneChangeIndex
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|LuceneChangeIndex
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkVersion
condition|)
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|IndexVersionCheck
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getChangeIndex (@erritServerConfig Config cfg, SitePaths sitePaths, @IndexExecutor ListeningScheduledExecutorService executor, FillArgs fillArgs)
specifier|public
name|LuceneChangeIndex
name|getChangeIndex
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
annotation|@
name|IndexExecutor
name|ListeningScheduledExecutorService
name|executor
parameter_list|,
name|FillArgs
name|fillArgs
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|LuceneChangeIndex
argument_list|(
name|cfg
argument_list|,
name|sitePaths
argument_list|,
name|executor
argument_list|,
name|fillArgs
argument_list|,
name|readOnly
argument_list|)
return|;
block|}
block|}
end_class

end_unit


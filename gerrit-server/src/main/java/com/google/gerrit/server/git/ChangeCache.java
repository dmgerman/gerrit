begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|GitReferenceUpdatedListener
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
name|cache
operator|.
name|CacheModule
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
name|SchemaFactory
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
name|Module
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|name
operator|.
name|Named
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
name|List
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
DECL|class|ChangeCache
specifier|public
class|class
name|ChangeCache
implements|implements
name|GitReferenceUpdatedListener
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
name|ChangeCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ID_CACHE
specifier|private
specifier|static
specifier|final
name|String
name|ID_CACHE
init|=
literal|"changes"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|ID_CACHE
argument_list|,
name|Project
operator|.
name|NameKey
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|1024
argument_list|)
operator|.
name|loader
argument_list|(
name|Loader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|cache
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeCache (@amedID_CACHE) LoadingCache<Project.NameKey, List<Change>> cache)
name|ChangeCache
parameter_list|(
annotation|@
name|Named
argument_list|(
name|ID_CACHE
argument_list|)
name|LoadingCache
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|get (Project.NameKey name)
name|List
argument_list|<
name|Change
argument_list|>
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|cache
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
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot fetch changes for "
operator|+
name|name
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|onGitReferenceUpdated (GitReferenceUpdatedListener.Event event)
specifier|public
name|void
name|onGitReferenceUpdated
parameter_list|(
name|GitReferenceUpdatedListener
operator|.
name|Event
name|event
parameter_list|)
block|{
for|for
control|(
name|GitReferenceUpdatedListener
operator|.
name|Update
name|u
range|:
name|event
operator|.
name|getUpdates
argument_list|()
control|)
block|{
if|if
condition|(
name|u
operator|.
name|getRefName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"refs/changes/"
argument_list|)
condition|)
block|{
name|cache
operator|.
name|invalidate
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|event
operator|.
name|getProjectName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|class|Loader
specifier|static
class|class
name|Loader
extends|extends
name|CacheLoader
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|List
argument_list|<
name|Change
argument_list|>
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
DECL|method|Loader (SchemaFactory<ReviewDb> schema)
name|Loader
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (Project.NameKey key)
specifier|public
name|List
argument_list|<
name|Change
argument_list|>
name|load
parameter_list|(
name|Project
operator|.
name|NameKey
name|key
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byProject
argument_list|(
name|key
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


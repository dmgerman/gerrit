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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|registration
operator|.
name|DynamicItem
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
name|inject
operator|.
name|AbstractModule
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
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
name|TimeUnit
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DefaultProjectNameLockManager
specifier|public
class|class
name|DefaultProjectNameLockManager
implements|implements
name|ProjectNameLockManager
block|{
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicItem
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|ProjectNameLockManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultProjectNameLockManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|lockCache
name|LoadingCache
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Lock
argument_list|>
name|lockCache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
literal|1024
argument_list|)
operator|.
name|expireAfterAccess
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
operator|.
name|build
argument_list|(
operator|new
name|CacheLoader
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Lock
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Lock
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
return|return
operator|new
name|ReentrantLock
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getLock (Project.NameKey name)
specifier|public
name|Lock
name|getLock
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
name|lockCache
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


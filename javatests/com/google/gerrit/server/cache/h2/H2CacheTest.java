begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.cache.h2
package|package
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
name|h2
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
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
name|Cache
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
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
name|h2
operator|.
name|H2CacheImpl
operator|.
name|SqlStore
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
name|h2
operator|.
name|H2CacheImpl
operator|.
name|ValueHolder
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
name|serialize
operator|.
name|StringCacheSerializer
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
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|H2CacheTest
specifier|public
class|class
name|H2CacheTest
block|{
DECL|field|KEY_TYPE
specifier|private
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|String
argument_list|>
name|KEY_TYPE
init|=
operator|new
name|TypeLiteral
argument_list|<
name|String
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|DEFAULT_VERSION
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_VERSION
init|=
literal|1234
decl_stmt|;
DECL|field|dbCnt
specifier|private
specifier|static
name|int
name|dbCnt
decl_stmt|;
DECL|method|nextDbId ()
specifier|private
specifier|static
name|int
name|nextDbId
parameter_list|()
block|{
return|return
operator|++
name|dbCnt
return|;
block|}
DECL|method|newH2CacheImpl ( int id, Cache<String, ValueHolder<String>> mem, int version)
specifier|private
specifier|static
name|H2CacheImpl
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newH2CacheImpl
parameter_list|(
name|int
name|id
parameter_list|,
name|Cache
argument_list|<
name|String
argument_list|,
name|ValueHolder
argument_list|<
name|String
argument_list|>
argument_list|>
name|mem
parameter_list|,
name|int
name|version
parameter_list|)
block|{
name|SqlStore
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|store
init|=
operator|new
name|SqlStore
argument_list|<>
argument_list|(
literal|"jdbc:h2:mem:Test_"
operator|+
name|id
argument_list|,
name|KEY_TYPE
argument_list|,
name|StringCacheSerializer
operator|.
name|INSTANCE
argument_list|,
name|StringCacheSerializer
operator|.
name|INSTANCE
argument_list|,
name|version
argument_list|,
literal|1
operator|<<
literal|20
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
operator|new
name|H2CacheImpl
argument_list|<>
argument_list|(
name|MoreExecutors
operator|.
name|directExecutor
argument_list|()
argument_list|,
name|store
argument_list|,
name|KEY_TYPE
argument_list|,
name|mem
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|get ()
specifier|public
name|void
name|get
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|Cache
argument_list|<
name|String
argument_list|,
name|ValueHolder
argument_list|<
name|String
argument_list|>
argument_list|>
name|mem
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|H2CacheImpl
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|impl
init|=
name|newH2CacheImpl
argument_list|(
name|nextDbId
argument_list|()
argument_list|,
name|mem
argument_list|,
name|DEFAULT_VERSION
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|impl
operator|.
name|getIfPresent
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|AtomicBoolean
name|called
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|impl
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
parameter_list|()
lambda|->
block|{
name|called
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|"bar"
return|;
block|}
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"Callable was called"
argument_list|)
operator|.
name|that
argument_list|(
name|called
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"in-memory value"
argument_list|)
operator|.
name|that
argument_list|(
name|impl
operator|.
name|getIfPresent
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|mem
operator|.
name|invalidate
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"persistent value"
argument_list|)
operator|.
name|that
argument_list|(
name|impl
operator|.
name|getIfPresent
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|called
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"cached value"
argument_list|)
operator|.
name|that
argument_list|(
name|impl
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
parameter_list|()
lambda|->
block|{
name|called
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|"baz"
return|;
block|}
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertWithMessage
argument_list|(
literal|"Callable was called"
argument_list|)
operator|.
name|that
argument_list|(
name|called
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|stringSerializer ()
specifier|public
name|void
name|stringSerializer
parameter_list|()
block|{
name|String
name|input
init|=
literal|"foo"
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|StringCacheSerializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|serialized
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|'f'
block|,
literal|'o'
block|,
literal|'o'
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|StringCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|version ()
specifier|public
name|void
name|version
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|id
init|=
name|nextDbId
argument_list|()
decl_stmt|;
name|H2CacheImpl
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|oldImpl
init|=
name|newH2CacheImpl
argument_list|(
name|id
argument_list|,
name|disableMemCache
argument_list|()
argument_list|,
name|DEFAULT_VERSION
argument_list|)
decl_stmt|;
name|H2CacheImpl
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newImpl
init|=
name|newH2CacheImpl
argument_list|(
name|id
argument_list|,
name|disableMemCache
argument_list|()
argument_list|,
name|DEFAULT_VERSION
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|oldImpl
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"val"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|hitCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Can't find key in cache with wrong version, but the data is still there.
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|requestCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|12
argument_list|)
expr_stmt|;
comment|// Re-putting it via the new cache works, and uses the same amount of space.
name|newImpl
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"val2"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"val2"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|hitCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|14
argument_list|)
expr_stmt|;
comment|// Now it's no longer in the old cache.
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|diskStats
argument_list|()
operator|.
name|space
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|14
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|oldImpl
operator|.
name|getIfPresent
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|disableMemCache ()
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Cache
argument_list|<
name|K
argument_list|,
name|ValueHolder
argument_list|<
name|V
argument_list|>
argument_list|>
name|disableMemCache
parameter_list|()
block|{
return|return
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
literal|0
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit


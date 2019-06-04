begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.truth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
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
name|assertAbout
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|CacheStats
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
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|Subject
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
name|common
operator|.
name|UsedAt
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
name|common
operator|.
name|UsedAt
operator|.
name|Project
import|;
end_import

begin_class
annotation|@
name|UsedAt
argument_list|(
name|Project
operator|.
name|PLUGINS_ALL
argument_list|)
DECL|class|CacheStatsSubject
specifier|public
class|class
name|CacheStatsSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (CacheStats stats)
specifier|public
specifier|static
name|CacheStatsSubject
name|assertThat
parameter_list|(
name|CacheStats
name|stats
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|CacheStatsSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|stats
argument_list|)
return|;
block|}
DECL|method|cloneStats (CacheStats other)
specifier|public
specifier|static
name|CacheStats
name|cloneStats
parameter_list|(
name|CacheStats
name|other
parameter_list|)
block|{
return|return
operator|new
name|CacheStats
argument_list|(
name|other
operator|.
name|hitCount
argument_list|()
argument_list|,
name|other
operator|.
name|missCount
argument_list|()
argument_list|,
name|other
operator|.
name|loadSuccessCount
argument_list|()
argument_list|,
name|other
operator|.
name|loadExceptionCount
argument_list|()
argument_list|,
name|other
operator|.
name|totalLoadTime
argument_list|()
argument_list|,
name|other
operator|.
name|evictionCount
argument_list|()
argument_list|)
return|;
block|}
DECL|field|stats
specifier|private
specifier|final
name|CacheStats
name|stats
decl_stmt|;
DECL|field|start
specifier|private
name|CacheStats
name|start
init|=
operator|new
name|CacheStats
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
DECL|method|CacheStatsSubject (FailureMetadata failureMetadata, CacheStats stats)
specifier|private
name|CacheStatsSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|CacheStats
name|stats
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|this
operator|.
name|stats
operator|=
name|stats
expr_stmt|;
block|}
DECL|method|since (CacheStats start)
specifier|public
name|CacheStatsSubject
name|since
parameter_list|(
name|CacheStats
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|requireNonNull
argument_list|(
name|start
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|hasHitCount (int expectedHitCount)
specifier|public
name|void
name|hasHitCount
parameter_list|(
name|int
name|expectedHitCount
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"hitCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|stats
operator|.
name|minus
argument_list|(
name|start
argument_list|)
operator|.
name|hitCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedHitCount
argument_list|)
expr_stmt|;
block|}
DECL|method|hasMissCount (int expectedMissCount)
specifier|public
name|void
name|hasMissCount
parameter_list|(
name|int
name|expectedMissCount
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"missCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|stats
operator|.
name|minus
argument_list|(
name|start
argument_list|)
operator|.
name|missCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedMissCount
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


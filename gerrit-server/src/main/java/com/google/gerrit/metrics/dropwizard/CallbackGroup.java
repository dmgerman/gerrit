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
DECL|package|com.google.gerrit.metrics.dropwizard
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|dropwizard
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
name|collect
operator|.
name|ImmutableSet
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
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * Run a user specified trigger only once every 2 seconds.  *  *<p>This allows the same Runnable trigger to be applied to several metrics. When a recorder is  * sampling the related metrics only the first access will perform recomputation. Reading other  * related metrics will rely on the already set values for the next several seconds.  */
end_comment

begin_class
DECL|class|CallbackGroup
class|class
name|CallbackGroup
implements|implements
name|Runnable
block|{
DECL|field|PERIOD
specifier|private
specifier|static
specifier|final
name|long
name|PERIOD
init|=
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toNanos
argument_list|(
literal|2
argument_list|)
decl_stmt|;
DECL|field|reloadAt
specifier|private
specifier|final
name|AtomicLong
name|reloadAt
decl_stmt|;
DECL|field|trigger
specifier|private
specifier|final
name|Runnable
name|trigger
decl_stmt|;
DECL|field|metrics
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|CallbackMetricGlue
argument_list|>
name|metrics
decl_stmt|;
DECL|field|reloadLock
specifier|private
specifier|final
name|Object
name|reloadLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|method|CallbackGroup (Runnable trigger, ImmutableSet<CallbackMetricGlue> metrics)
name|CallbackGroup
parameter_list|(
name|Runnable
name|trigger
parameter_list|,
name|ImmutableSet
argument_list|<
name|CallbackMetricGlue
argument_list|>
name|metrics
parameter_list|)
block|{
name|this
operator|.
name|reloadAt
operator|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|trigger
operator|=
name|trigger
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|reload
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|reloadLock
init|)
block|{
for|for
control|(
name|CallbackMetricGlue
name|m
range|:
name|metrics
control|)
block|{
name|m
operator|.
name|beginSet
argument_list|()
expr_stmt|;
block|}
name|trigger
operator|.
name|run
argument_list|()
expr_stmt|;
for|for
control|(
name|CallbackMetricGlue
name|m
range|:
name|metrics
control|)
block|{
name|m
operator|.
name|endSet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|reload ()
specifier|private
name|boolean
name|reload
parameter_list|()
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|long
name|now
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|long
name|next
init|=
name|reloadAt
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|>
name|now
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|reloadAt
operator|.
name|compareAndSet
argument_list|(
name|next
argument_list|,
name|now
operator|+
name|PERIOD
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
end_class

end_unit


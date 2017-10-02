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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
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
name|TimeUtil
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
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Month
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
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
comment|/** Static utility methods for dealing with dates and times in tests. */
end_comment

begin_class
DECL|class|TestTimeUtil
specifier|public
class|class
name|TestTimeUtil
block|{
DECL|field|START
specifier|public
specifier|static
specifier|final
name|Instant
name|START
init|=
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2009
argument_list|,
name|Month
operator|.
name|SEPTEMBER
argument_list|,
literal|30
argument_list|,
literal|17
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|atOffset
argument_list|(
name|ZoneOffset
operator|.
name|ofHours
argument_list|(
operator|-
literal|4
argument_list|)
argument_list|)
operator|.
name|toInstant
argument_list|()
decl_stmt|;
DECL|field|clockStepMs
specifier|private
specifier|static
name|Long
name|clockStepMs
decl_stmt|;
DECL|field|clockMs
specifier|private
specifier|static
name|AtomicLong
name|clockMs
decl_stmt|;
comment|/**    * Reset the clock to a known start point, then set the clock step.    *    *<p>The clock is initially set to 2009/09/30 17:00:00 -0400.    *    * @param clockStep amount to increment clock by on each lookup.    * @param clockStepUnit time unit for {@code clockStep}.    */
DECL|method|resetWithClockStep (long clockStep, TimeUnit clockStepUnit)
specifier|public
specifier|static
specifier|synchronized
name|void
name|resetWithClockStep
parameter_list|(
name|long
name|clockStep
parameter_list|,
name|TimeUnit
name|clockStepUnit
parameter_list|)
block|{
comment|// Set an arbitrary start point so tests are more repeatable.
name|clockMs
operator|=
operator|new
name|AtomicLong
argument_list|(
name|START
operator|.
name|toEpochMilli
argument_list|()
argument_list|)
expr_stmt|;
name|setClockStep
argument_list|(
name|clockStep
argument_list|,
name|clockStepUnit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Set the clock step used by {@link com.google.gerrit.common.TimeUtil}.    *    * @param clockStep amount to increment clock by on each lookup.    * @param clockStepUnit time unit for {@code clockStep}.    */
DECL|method|setClockStep (long clockStep, TimeUnit clockStepUnit)
specifier|public
specifier|static
specifier|synchronized
name|void
name|setClockStep
parameter_list|(
name|long
name|clockStep
parameter_list|,
name|TimeUnit
name|clockStepUnit
parameter_list|)
block|{
name|checkState
argument_list|(
name|clockMs
operator|!=
literal|null
argument_list|,
literal|"call resetWithClockStep first"
argument_list|)
expr_stmt|;
name|clockStepMs
operator|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|clockStep
argument_list|,
name|clockStepUnit
argument_list|)
expr_stmt|;
name|TimeUtil
operator|.
name|setCurrentMillisSupplier
argument_list|(
parameter_list|()
lambda|->
name|clockMs
operator|.
name|getAndAdd
argument_list|(
name|clockStepMs
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Set the clock to a specific timestamp.    *    * @param ts time to set    */
DECL|method|setClock (Timestamp ts)
specifier|public
specifier|static
specifier|synchronized
name|void
name|setClock
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
block|{
name|checkState
argument_list|(
name|clockMs
operator|!=
literal|null
argument_list|,
literal|"call resetWithClockStep first"
argument_list|)
expr_stmt|;
name|clockMs
operator|.
name|set
argument_list|(
name|ts
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Increment the clock once by a given amount.    *    * @param clockStep amount to increment clock by.    * @param clockStepUnit time unit for {@code clockStep}.    */
DECL|method|incrementClock (long clockStep, TimeUnit clockStepUnit)
specifier|public
specifier|static
specifier|synchronized
name|void
name|incrementClock
parameter_list|(
name|long
name|clockStep
parameter_list|,
name|TimeUnit
name|clockStepUnit
parameter_list|)
block|{
name|checkState
argument_list|(
name|clockMs
operator|!=
literal|null
argument_list|,
literal|"call resetWithClockStep first"
argument_list|)
expr_stmt|;
name|clockMs
operator|.
name|addAndGet
argument_list|(
name|clockStepUnit
operator|.
name|toMillis
argument_list|(
name|clockStep
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Reset the clock to use the actual system clock. */
DECL|method|useSystemTime ()
specifier|public
specifier|static
specifier|synchronized
name|void
name|useSystemTime
parameter_list|()
block|{
name|clockMs
operator|=
literal|null
expr_stmt|;
name|TimeUtil
operator|.
name|resetCurrentMillisSupplier
argument_list|()
expr_stmt|;
block|}
DECL|method|TestTimeUtil ()
specifier|private
name|TestTimeUtil
parameter_list|()
block|{}
block|}
end_class

end_unit


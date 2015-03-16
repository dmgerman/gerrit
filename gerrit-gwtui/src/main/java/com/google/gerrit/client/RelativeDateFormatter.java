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
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|changes
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Formatter to format timestamps relative to the current time using time units  * in the format defined by {@code git log --relative-date}.  */
end_comment

begin_class
DECL|class|RelativeDateFormatter
specifier|public
class|class
name|RelativeDateFormatter
block|{
DECL|field|SECOND_IN_MILLIS
specifier|static
specifier|final
name|long
name|SECOND_IN_MILLIS
init|=
literal|1000
decl_stmt|;
DECL|field|MINUTE_IN_MILLIS
specifier|static
specifier|final
name|long
name|MINUTE_IN_MILLIS
init|=
literal|60
operator|*
name|SECOND_IN_MILLIS
decl_stmt|;
DECL|field|HOUR_IN_MILLIS
specifier|static
specifier|final
name|long
name|HOUR_IN_MILLIS
init|=
literal|60
operator|*
name|MINUTE_IN_MILLIS
decl_stmt|;
DECL|field|DAY_IN_MILLIS
specifier|static
specifier|final
name|long
name|DAY_IN_MILLIS
init|=
literal|24
operator|*
name|HOUR_IN_MILLIS
decl_stmt|;
DECL|field|WEEK_IN_MILLIS
specifier|static
specifier|final
name|long
name|WEEK_IN_MILLIS
init|=
literal|7
operator|*
name|DAY_IN_MILLIS
decl_stmt|;
DECL|field|MONTH_IN_MILLIS
specifier|static
specifier|final
name|long
name|MONTH_IN_MILLIS
init|=
literal|30
operator|*
name|DAY_IN_MILLIS
decl_stmt|;
DECL|field|YEAR_IN_MILLIS
specifier|static
specifier|final
name|long
name|YEAR_IN_MILLIS
init|=
literal|365
operator|*
name|DAY_IN_MILLIS
decl_stmt|;
comment|/**    * @param when {@link Date} to format    * @return age of given {@link Date} compared to now formatted in the same    *         relative format as returned by {@code git log --relative-date}    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"boxing"
argument_list|)
DECL|method|format (Date when)
specifier|public
specifier|static
name|String
name|format
parameter_list|(
name|Date
name|when
parameter_list|)
block|{
name|long
name|ageMillis
init|=
operator|(
operator|new
name|Date
argument_list|()
operator|)
operator|.
name|getTime
argument_list|()
operator|-
name|when
operator|.
name|getTime
argument_list|()
decl_stmt|;
comment|// shouldn't happen in a perfect world
if|if
condition|(
name|ageMillis
operator|<
literal|0
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|inTheFuture
argument_list|()
return|;
block|}
comment|// seconds
if|if
condition|(
name|ageMillis
operator|<
name|upperLimit
argument_list|(
name|MINUTE_IN_MILLIS
argument_list|)
condition|)
block|{
name|long
name|seconds
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|SECOND_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|seconds
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneSecondAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|secondsAgo
argument_list|(
name|seconds
argument_list|)
return|;
block|}
block|}
comment|// minutes
if|if
condition|(
name|ageMillis
operator|<
name|upperLimit
argument_list|(
name|HOUR_IN_MILLIS
argument_list|)
condition|)
block|{
name|long
name|minutes
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|minutes
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneMinuteAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|minutesAgo
argument_list|(
name|minutes
argument_list|)
return|;
block|}
block|}
comment|// hours
if|if
condition|(
name|ageMillis
operator|<
name|upperLimit
argument_list|(
name|DAY_IN_MILLIS
argument_list|)
condition|)
block|{
name|long
name|hours
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|HOUR_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|hours
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneHourAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|hoursAgo
argument_list|(
name|hours
argument_list|)
return|;
block|}
block|}
comment|// up to 14 days use days
if|if
condition|(
name|ageMillis
operator|<
literal|14
operator|*
name|DAY_IN_MILLIS
condition|)
block|{
name|long
name|days
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|DAY_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|days
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneDayAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|daysAgo
argument_list|(
name|days
argument_list|)
return|;
block|}
block|}
comment|// up to 10 weeks use weeks
if|if
condition|(
name|ageMillis
operator|<
literal|10
operator|*
name|WEEK_IN_MILLIS
condition|)
block|{
name|long
name|weeks
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|WEEK_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|weeks
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneWeekAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|weeksAgo
argument_list|(
name|weeks
argument_list|)
return|;
block|}
block|}
comment|// months
if|if
condition|(
name|ageMillis
operator|<
name|YEAR_IN_MILLIS
condition|)
block|{
name|long
name|months
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|MONTH_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|months
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneMonthAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|monthsAgo
argument_list|(
name|months
argument_list|)
return|;
block|}
block|}
comment|// up to 5 years use "year, months" rounded to months
if|if
condition|(
name|ageMillis
operator|<
literal|5
operator|*
name|YEAR_IN_MILLIS
condition|)
block|{
name|long
name|years
init|=
name|ageMillis
operator|/
name|YEAR_IN_MILLIS
decl_stmt|;
name|String
name|yearLabel
init|=
operator|(
name|years
operator|>
literal|1
operator|)
condition|?
name|Util
operator|.
name|C
operator|.
name|years
argument_list|()
else|:
name|Util
operator|.
name|C
operator|.
name|year
argument_list|()
decl_stmt|;
name|long
name|months
init|=
name|round
argument_list|(
name|ageMillis
operator|%
name|YEAR_IN_MILLIS
argument_list|,
name|MONTH_IN_MILLIS
argument_list|)
decl_stmt|;
name|String
name|monthLabel
init|=
operator|(
name|months
operator|>
literal|1
operator|)
condition|?
name|Util
operator|.
name|C
operator|.
name|months
argument_list|()
else|:
operator|(
name|months
operator|==
literal|1
condition|?
name|Util
operator|.
name|C
operator|.
name|month
argument_list|()
else|:
literal|""
operator|)
decl_stmt|;
if|if
condition|(
name|months
operator|==
literal|0
condition|)
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|years0MonthsAgo
argument_list|(
name|years
argument_list|,
name|yearLabel
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|yearsMonthsAgo
argument_list|(
name|years
argument_list|,
name|yearLabel
argument_list|,
name|months
argument_list|,
name|monthLabel
argument_list|)
return|;
block|}
block|}
comment|// years
name|long
name|years
init|=
name|round
argument_list|(
name|ageMillis
argument_list|,
name|YEAR_IN_MILLIS
argument_list|)
decl_stmt|;
if|if
condition|(
name|years
operator|==
literal|1
condition|)
block|{
return|return
name|Util
operator|.
name|C
operator|.
name|oneYearAgo
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|yearsAgo
argument_list|(
name|years
argument_list|)
return|;
block|}
block|}
DECL|method|upperLimit (long unit)
specifier|private
specifier|static
name|long
name|upperLimit
parameter_list|(
name|long
name|unit
parameter_list|)
block|{
return|return
name|unit
operator|+
name|unit
operator|/
literal|2
return|;
block|}
DECL|method|round (long n, long unit)
specifier|private
specifier|static
name|long
name|round
parameter_list|(
name|long
name|n
parameter_list|,
name|long
name|unit
parameter_list|)
block|{
return|return
operator|(
name|n
operator|+
name|unit
operator|/
literal|2
operator|)
operator|/
name|unit
return|;
block|}
block|}
end_class

end_unit


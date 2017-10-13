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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RelativeDateFormatter
operator|.
name|DAY_IN_MILLIS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RelativeDateFormatter
operator|.
name|HOUR_IN_MILLIS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RelativeDateFormatter
operator|.
name|MINUTE_IN_MILLIS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RelativeDateFormatter
operator|.
name|SECOND_IN_MILLIS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RelativeDateFormatter
operator|.
name|YEAR_IN_MILLIS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|RelativeDateFormatterTest
specifier|public
class|class
name|RelativeDateFormatterTest
block|{
annotation|@
name|BeforeClass
DECL|method|setConstants ()
specifier|public
specifier|static
name|void
name|setConstants
parameter_list|()
block|{
name|Constants
name|c
init|=
operator|new
name|Constants
argument_list|()
decl_stmt|;
name|RelativeDateFormatter
operator|.
name|setConstants
argument_list|(
name|c
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|unsetConstants ()
specifier|public
specifier|static
name|void
name|unsetConstants
parameter_list|()
block|{
name|RelativeDateFormatter
operator|.
name|setConstants
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFormat (long ageFromNow, long timeUnit, String expectedFormat)
specifier|private
specifier|static
name|void
name|assertFormat
parameter_list|(
name|long
name|ageFromNow
parameter_list|,
name|long
name|timeUnit
parameter_list|,
name|String
name|expectedFormat
parameter_list|)
block|{
name|Date
name|d
init|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|ageFromNow
operator|*
name|timeUnit
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|RelativeDateFormatter
operator|.
name|format
argument_list|(
name|d
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedFormat
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|future ()
specifier|public
name|void
name|future
parameter_list|()
block|{
name|assertFormat
argument_list|(
operator|-
literal|100
argument_list|,
name|YEAR_IN_MILLIS
argument_list|,
literal|"in the future"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
operator|-
literal|1
argument_list|,
name|SECOND_IN_MILLIS
argument_list|,
literal|"in the future"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatSeconds ()
specifier|public
name|void
name|formatSeconds
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|1
argument_list|,
name|SECOND_IN_MILLIS
argument_list|,
literal|"1 second ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|89
argument_list|,
name|SECOND_IN_MILLIS
argument_list|,
literal|"89 seconds ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatMinutes ()
specifier|public
name|void
name|formatMinutes
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|90
argument_list|,
name|SECOND_IN_MILLIS
argument_list|,
literal|"2 minutes ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|3
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|,
literal|"3 minutes ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|60
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|,
literal|"60 minutes ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|89
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|,
literal|"89 minutes ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatHours ()
specifier|public
name|void
name|formatHours
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|90
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|,
literal|"2 hours ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|149
argument_list|,
name|MINUTE_IN_MILLIS
argument_list|,
literal|"2 hours ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|35
argument_list|,
name|HOUR_IN_MILLIS
argument_list|,
literal|"35 hours ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatDays ()
specifier|public
name|void
name|formatDays
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|36
argument_list|,
name|HOUR_IN_MILLIS
argument_list|,
literal|"2 days ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|13
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"13 days ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatWeeks ()
specifier|public
name|void
name|formatWeeks
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|14
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"2 weeks ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|69
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"10 weeks ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatMonths ()
specifier|public
name|void
name|formatMonths
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|70
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"2 months ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|75
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"3 months ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|364
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"12 months ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatYearsMonths ()
specifier|public
name|void
name|formatYearsMonths
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|366
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"1 year ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|380
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"1 year, 1 month ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|410
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"1 year, 2 months ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|2
argument_list|,
name|YEAR_IN_MILLIS
argument_list|,
literal|"2 years ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|1824
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"5 years ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|2
operator|*
literal|365
operator|-
literal|10
argument_list|,
name|DAY_IN_MILLIS
argument_list|,
literal|"2 years ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|formatYears ()
specifier|public
name|void
name|formatYears
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|5
argument_list|,
name|YEAR_IN_MILLIS
argument_list|,
literal|"5 years ago"
argument_list|)
expr_stmt|;
name|assertFormat
argument_list|(
literal|60
argument_list|,
name|YEAR_IN_MILLIS
argument_list|,
literal|"60 years ago"
argument_list|)
expr_stmt|;
block|}
DECL|class|Constants
specifier|private
specifier|static
class|class
name|Constants
implements|implements
name|CommonConstants
implements|,
name|CommonMessages
block|{
annotation|@
name|Override
DECL|method|inTheFuture ()
specifier|public
name|String
name|inTheFuture
parameter_list|()
block|{
return|return
literal|"in the future"
return|;
block|}
annotation|@
name|Override
DECL|method|month ()
specifier|public
name|String
name|month
parameter_list|()
block|{
return|return
literal|"month"
return|;
block|}
annotation|@
name|Override
DECL|method|months ()
specifier|public
name|String
name|months
parameter_list|()
block|{
return|return
literal|"months"
return|;
block|}
annotation|@
name|Override
DECL|method|year ()
specifier|public
name|String
name|year
parameter_list|()
block|{
return|return
literal|"year"
return|;
block|}
annotation|@
name|Override
DECL|method|years ()
specifier|public
name|String
name|years
parameter_list|()
block|{
return|return
literal|"years"
return|;
block|}
annotation|@
name|Override
DECL|method|oneSecondAgo ()
specifier|public
name|String
name|oneSecondAgo
parameter_list|()
block|{
return|return
literal|"1 second ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneMinuteAgo ()
specifier|public
name|String
name|oneMinuteAgo
parameter_list|()
block|{
return|return
literal|"1 minute ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneHourAgo ()
specifier|public
name|String
name|oneHourAgo
parameter_list|()
block|{
return|return
literal|"1 hour ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneDayAgo ()
specifier|public
name|String
name|oneDayAgo
parameter_list|()
block|{
return|return
literal|"1 day ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneWeekAgo ()
specifier|public
name|String
name|oneWeekAgo
parameter_list|()
block|{
return|return
literal|"1 week ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneMonthAgo ()
specifier|public
name|String
name|oneMonthAgo
parameter_list|()
block|{
return|return
literal|"1 month ago"
return|;
block|}
annotation|@
name|Override
DECL|method|oneYearAgo ()
specifier|public
name|String
name|oneYearAgo
parameter_list|()
block|{
return|return
literal|"1 year ago"
return|;
block|}
annotation|@
name|Override
DECL|method|secondsAgo (long seconds)
specifier|public
name|String
name|secondsAgo
parameter_list|(
name|long
name|seconds
parameter_list|)
block|{
return|return
name|seconds
operator|+
literal|" seconds ago"
return|;
block|}
annotation|@
name|Override
DECL|method|minutesAgo (long minutes)
specifier|public
name|String
name|minutesAgo
parameter_list|(
name|long
name|minutes
parameter_list|)
block|{
return|return
name|minutes
operator|+
literal|" minutes ago"
return|;
block|}
annotation|@
name|Override
DECL|method|hoursAgo (long hours)
specifier|public
name|String
name|hoursAgo
parameter_list|(
name|long
name|hours
parameter_list|)
block|{
return|return
name|hours
operator|+
literal|" hours ago"
return|;
block|}
annotation|@
name|Override
DECL|method|daysAgo (long days)
specifier|public
name|String
name|daysAgo
parameter_list|(
name|long
name|days
parameter_list|)
block|{
return|return
name|days
operator|+
literal|" days ago"
return|;
block|}
annotation|@
name|Override
DECL|method|weeksAgo (long weeks)
specifier|public
name|String
name|weeksAgo
parameter_list|(
name|long
name|weeks
parameter_list|)
block|{
return|return
name|weeks
operator|+
literal|" weeks ago"
return|;
block|}
annotation|@
name|Override
DECL|method|monthsAgo (long months)
specifier|public
name|String
name|monthsAgo
parameter_list|(
name|long
name|months
parameter_list|)
block|{
return|return
name|months
operator|+
literal|" months ago"
return|;
block|}
annotation|@
name|Override
DECL|method|yearsAgo (long years)
specifier|public
name|String
name|yearsAgo
parameter_list|(
name|long
name|years
parameter_list|)
block|{
return|return
name|years
operator|+
literal|" years ago"
return|;
block|}
annotation|@
name|Override
DECL|method|years0MonthsAgo (long years, String yearLabel)
specifier|public
name|String
name|years0MonthsAgo
parameter_list|(
name|long
name|years
parameter_list|,
name|String
name|yearLabel
parameter_list|)
block|{
return|return
name|years
operator|+
literal|" "
operator|+
name|yearLabel
operator|+
literal|" ago"
return|;
block|}
annotation|@
name|Override
DECL|method|yearsMonthsAgo (long years, String yearLabel, long months, String monthLabel)
specifier|public
name|String
name|yearsMonthsAgo
parameter_list|(
name|long
name|years
parameter_list|,
name|String
name|yearLabel
parameter_list|,
name|long
name|months
parameter_list|,
name|String
name|monthLabel
parameter_list|)
block|{
return|return
name|years
operator|+
literal|" "
operator|+
name|yearLabel
operator|+
literal|", "
operator|+
name|months
operator|+
literal|" "
operator|+
name|monthLabel
operator|+
literal|" ago"
return|;
block|}
block|}
block|}
end_class

end_unit


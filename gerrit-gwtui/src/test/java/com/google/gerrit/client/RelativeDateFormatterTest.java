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
name|DAY_IN_MILLIS
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RelativeDateFormatter
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
DECL|method|testFuture ()
specifier|public
name|void
name|testFuture
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
DECL|method|testFormatSeconds ()
specifier|public
name|void
name|testFormatSeconds
parameter_list|()
block|{
name|assertFormat
argument_list|(
literal|1
argument_list|,
name|SECOND_IN_MILLIS
argument_list|,
literal|"1 seconds ago"
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
DECL|method|testFormatMinutes ()
specifier|public
name|void
name|testFormatMinutes
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
DECL|method|testFormatHours ()
specifier|public
name|void
name|testFormatHours
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
DECL|method|testFormatDays ()
specifier|public
name|void
name|testFormatDays
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
DECL|method|testFormatWeeks ()
specifier|public
name|void
name|testFormatWeeks
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
DECL|method|testFormatMonths ()
specifier|public
name|void
name|testFormatMonths
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
DECL|method|testFormatYearsMonths ()
specifier|public
name|void
name|testFormatYearsMonths
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
literal|"4 years, 12 months ago"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFormatYears ()
specifier|public
name|void
name|testFormatYears
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
block|}
end_class

end_unit


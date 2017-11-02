begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

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
name|DAYS
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
name|HOURS
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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MINUTES
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
name|time
operator|.
name|ZonedDateTime
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ScheduleConfigTest
specifier|public
class|class
name|ScheduleConfigTest
block|{
comment|// Friday June 13, 2014 10:00 UTC
DECL|field|NOW
specifier|private
specifier|static
specifier|final
name|ZonedDateTime
name|NOW
init|=
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2014
argument_list|,
name|Month
operator|.
name|JUNE
argument_list|,
literal|13
argument_list|,
literal|10
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
name|UTC
argument_list|)
operator|.
name|toZonedDateTime
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|initialDelay ()
specifier|public
name|void
name|initialDelay
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"11:00"
argument_list|,
literal|"1h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|30
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"05:30"
argument_list|,
literal|"1h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|30
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"09:30"
argument_list|,
literal|"1h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|30
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"13:30"
argument_list|,
literal|"1h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|59
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"13:59"
argument_list|,
literal|"1h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"11:00"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|19
argument_list|,
name|HOURS
argument_list|)
operator|+
name|ms
argument_list|(
literal|30
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"05:30"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"11:00"
argument_list|,
literal|"1w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|7
argument_list|,
name|DAYS
argument_list|)
operator|-
name|ms
argument_list|(
literal|4
argument_list|,
name|HOURS
argument_list|)
operator|-
name|ms
argument_list|(
literal|30
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"05:30"
argument_list|,
literal|"1w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|3
argument_list|,
name|DAYS
argument_list|)
operator|+
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Mon 11:00"
argument_list|,
literal|"1w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Fri 11:00"
argument_list|,
literal|"1w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Mon 11:00"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|23
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Mon 09:00"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Mon 10:00"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|initialDelay
argument_list|(
literal|"Mon 10:00"
argument_list|,
literal|"1d"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|customKeys ()
specifier|public
name|void
name|customKeys
parameter_list|()
block|{
name|Config
name|rc
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|rc
operator|.
name|setString
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"i"
argument_list|,
literal|"1h"
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setString
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"s"
argument_list|,
literal|"01:00"
argument_list|)
expr_stmt|;
name|ScheduleConfig
name|s
init|=
operator|new
name|ScheduleConfig
argument_list|(
name|rc
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"i"
argument_list|,
literal|"s"
argument_list|,
name|NOW
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|s
operator|.
name|getInterval
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|s
operator|.
name|getInitialDelay
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
operator|new
name|ScheduleConfig
argument_list|(
name|rc
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"myInterval"
argument_list|,
literal|"myStart"
argument_list|,
name|NOW
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|getInterval
argument_list|()
argument_list|,
name|ScheduleConfig
operator|.
name|MISSING_CONFIG
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|getInitialDelay
argument_list|()
argument_list|,
name|ScheduleConfig
operator|.
name|MISSING_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|initialDelay (String startTime, String interval)
specifier|private
specifier|static
name|long
name|initialDelay
parameter_list|(
name|String
name|startTime
parameter_list|,
name|String
name|interval
parameter_list|)
block|{
return|return
operator|new
name|ScheduleConfig
argument_list|(
name|config
argument_list|(
name|startTime
argument_list|,
name|interval
argument_list|)
argument_list|,
literal|"section"
argument_list|,
literal|"subsection"
argument_list|,
name|NOW
argument_list|)
operator|.
name|getInitialDelay
argument_list|()
return|;
block|}
DECL|method|config (String startTime, String interval)
specifier|private
specifier|static
name|Config
name|config
parameter_list|(
name|String
name|startTime
parameter_list|,
name|String
name|interval
parameter_list|)
block|{
name|Config
name|rc
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|rc
operator|.
name|setString
argument_list|(
literal|"section"
argument_list|,
literal|"subsection"
argument_list|,
literal|"startTime"
argument_list|,
name|startTime
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setString
argument_list|(
literal|"section"
argument_list|,
literal|"subsection"
argument_list|,
literal|"interval"
argument_list|,
name|interval
argument_list|)
expr_stmt|;
return|return
name|rc
return|;
block|}
DECL|method|ms (int cnt, TimeUnit unit)
specifier|private
specifier|static
name|long
name|ms
parameter_list|(
name|int
name|cnt
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|cnt
argument_list|,
name|unit
argument_list|)
return|;
block|}
block|}
end_class

end_unit

begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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

begin_class
DECL|class|ConfigUtilTest
specifier|public
class|class
name|ConfigUtilTest
extends|extends
name|TestCase
block|{
DECL|method|testTimeUnit ()
specifier|public
name|void
name|testTimeUnit
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|0
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|2
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2ms"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|200
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"200 milliseconds"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|0
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"0s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|2
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|231
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"231sec"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1second"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|300
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"300 seconds"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|2
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2m"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|2
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2min"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|1
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1 minute"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|10
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"10 minutes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|5
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"5h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|5
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"5hr"
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
name|parse
argument_list|(
literal|"1hour"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|48
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"48hours"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|5
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"5 h"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|5
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"5 hr"
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
name|parse
argument_list|(
literal|"1 hour"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|48
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"48 hours"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|48
argument_list|,
name|HOURS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"48 \t \r hours"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|4
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"4d"
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
name|parse
argument_list|(
literal|"1day"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|14
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"14days"
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
argument_list|,
name|parse
argument_list|(
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
argument_list|,
name|parse
argument_list|(
literal|"1week"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|14
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|14
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2weeks"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|30
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1mon"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|30
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1month"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|60
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2mon"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|60
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2months"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|365
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|365
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"1year"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|(
literal|365
operator|*
literal|2
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|parse
argument_list|(
literal|"2years"
argument_list|)
argument_list|)
expr_stmt|;
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
DECL|method|parse (String string)
specifier|private
specifier|static
name|long
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|string
argument_list|,
literal|1
argument_list|,
name|MILLISECONDS
argument_list|)
return|;
block|}
block|}
end_class

end_unit


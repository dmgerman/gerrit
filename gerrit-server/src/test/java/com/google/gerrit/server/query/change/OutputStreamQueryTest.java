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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
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
import|import
name|java
operator|.
name|time
operator|.
name|LocalDate
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
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|OutputStreamQueryTest
specifier|public
class|class
name|OutputStreamQueryTest
block|{
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Another test doesn't clean up properly. As result, a mismatch between TimeZone.getDefault()
comment|// and the system property 'user.timezone' is left behind. Resolve that mismatch so that this
comment|// test always runs with a clean state.
name|TimeZone
name|defaultTimeZone
init|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"user.timezone"
argument_list|,
name|defaultTimeZone
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|timestampsAreFormattedAsWithJodaTime ()
specifier|public
name|void
name|timestampsAreFormattedAsWithJodaTime
parameter_list|()
block|{
name|long
name|epochSeconds
init|=
name|LocalDate
operator|.
name|of
argument_list|(
literal|2017
argument_list|,
name|Month
operator|.
name|SEPTEMBER
argument_list|,
literal|30
argument_list|)
operator|.
name|atStartOfDay
argument_list|()
operator|.
name|atZone
argument_list|(
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
operator|.
name|toInstant
argument_list|()
operator|.
name|getEpochSecond
argument_list|()
decl_stmt|;
name|String
name|formattedDateTime
init|=
name|OutputStreamQuery
operator|.
name|formatDateTime
argument_list|(
name|epochSeconds
argument_list|)
decl_stmt|;
name|DateTimeFormatter
name|jodaTimeFormatter
init|=
name|DateTimeFormat
operator|.
name|forPattern
argument_list|(
name|OutputStreamQuery
operator|.
name|TIMESTAMP_FORMAT
argument_list|)
decl_stmt|;
name|String
name|expectedDateTime
init|=
name|jodaTimeFormatter
operator|.
name|print
argument_list|(
name|epochSeconds
operator|*
literal|1000L
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|formattedDateTime
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedDateTime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


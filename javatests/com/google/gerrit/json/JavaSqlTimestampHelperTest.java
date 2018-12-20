begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 Google Inc. All Rights Reserved.
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
comment|//     http://www.apache.org/licenses/LICENSE-2.0
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
DECL|package|com.google.gerrit.json
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|json
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
name|assert_
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
name|json
operator|.
name|JavaSqlTimestampHelper
operator|.
name|parseTimestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|junit
operator|.
name|After
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
DECL|class|JavaSqlTimestampHelperTest
specifier|public
class|class
name|JavaSqlTimestampHelperTest
block|{
DECL|field|format
specifier|private
name|SimpleDateFormat
name|format
decl_stmt|;
DECL|field|systemTimeZone
specifier|private
name|TimeZone
name|systemTimeZone
decl_stmt|;
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
synchronized|synchronized
init|(
name|TimeZone
operator|.
name|class
init|)
block|{
name|systemTimeZone
operator|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT-5:00"
argument_list|)
argument_list|)
expr_stmt|;
name|format
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss.SSS Z"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|resetTimeZone ()
specifier|public
name|void
name|resetTimeZone
parameter_list|()
block|{
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|systemTimeZone
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseFullTimestamp ()
specifier|public
name|void
name|parseFullTimestamp
parameter_list|()
block|{
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02 20:04:05.789000000"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-02 15:04:05.789 -0500"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02 20:04:05"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-02 15:04:05.000 -0500"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseDateOnly ()
specifier|public
name|void
name|parseDateOnly
parameter_list|()
block|{
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-01 19:00:00.000 -0500"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseTimeZone ()
specifier|public
name|void
name|parseTimeZone
parameter_list|()
block|{
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02 15:04:05.789 -0100"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-02 11:04:05.789 -0500"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02 15:04:05.789 -0000"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-02 10:04:05.789 -0500"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|reformat
argument_list|(
literal|"2006-01-02 15:04:05.789 +0100"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2006-01-02 09:04:05.789 -0500"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseInvalidTimestamps ()
specifier|public
name|void
name|parseInvalidTimestamps
parameter_list|()
block|{
name|assertInvalid
argument_list|(
literal|"2006-01-02-15:04:05.789000000"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"2006-01-02T15:04:05.789000000"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"15:04:05"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"15:04:05.999000000"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalid (String input)
specifier|private
specifier|static
name|void
name|assertInvalid
parameter_list|(
name|String
name|input
parameter_list|)
block|{
try|try
block|{
name|parseTimestamp
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"Expected IllegalArgumentException for: "
operator|+
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Expected;
block|}
block|}
DECL|method|reformat (String input)
specifier|private
name|String
name|reformat
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|format
operator|.
name|format
argument_list|(
name|parseTimestamp
argument_list|(
name|input
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


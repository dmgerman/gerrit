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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|index
operator|.
name|query
operator|.
name|RangeUtil
operator|.
name|Range
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
DECL|class|RangeUtilTest
specifier|public
class|class
name|RangeUtilTest
block|{
annotation|@
name|Test
DECL|method|getRangeForValueOutsideOfMinMaxRange_minNotGreaterThanMax ()
specifier|public
name|void
name|getRangeForValueOutsideOfMinMaxRange_minNotGreaterThanMax
parameter_list|()
block|{
for|for
control|(
name|String
name|operator
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"="
argument_list|,
literal|">"
argument_list|,
literal|">="
argument_list|,
literal|"<"
argument_list|,
literal|"<="
argument_list|)
control|)
block|{
name|Range
name|range
init|=
name|RangeUtil
operator|.
name|getRange
argument_list|(
literal|"foo"
argument_list|,
name|operator
argument_list|,
literal|10
argument_list|,
operator|-
literal|4
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|range
operator|.
name|min
argument_list|)
operator|.
name|isAtMost
argument_list|(
name|range
operator|.
name|max
argument_list|)
expr_stmt|;
name|range
operator|=
name|RangeUtil
operator|.
name|getRange
argument_list|(
literal|"foo"
argument_list|,
name|operator
argument_list|,
operator|-
literal|10
argument_list|,
operator|-
literal|4
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|range
operator|.
name|min
argument_list|)
operator|.
name|isAtMost
argument_list|(
name|range
operator|.
name|max
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


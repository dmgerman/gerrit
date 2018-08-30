begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|LabelTypeTest
specifier|public
class|class
name|LabelTypeTest
block|{
annotation|@
name|Test
DECL|method|sortLabelValues ()
specifier|public
name|void
name|sortLabelValues
parameter_list|()
block|{
name|LabelValue
name|v0
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"Zero"
argument_list|)
decl_stmt|;
name|LabelValue
name|v1
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|"One"
argument_list|)
decl_stmt|;
name|LabelValue
name|v2
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Two"
argument_list|)
decl_stmt|;
name|LabelType
name|types
init|=
operator|new
name|LabelType
argument_list|(
literal|"Label"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|v2
argument_list|,
name|v0
argument_list|,
name|v1
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|types
operator|.
name|getValues
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|v0
argument_list|,
name|v1
argument_list|,
name|v2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|insertMissingLabelValues ()
specifier|public
name|void
name|insertMissingLabelValues
parameter_list|()
block|{
name|LabelValue
name|v0
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"Zero"
argument_list|)
decl_stmt|;
name|LabelValue
name|v2
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Two"
argument_list|)
decl_stmt|;
name|LabelValue
name|v5
init|=
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|5
argument_list|,
literal|"Five"
argument_list|)
decl_stmt|;
name|LabelType
name|types
init|=
operator|new
name|LabelType
argument_list|(
literal|"Label"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|v2
argument_list|,
name|v5
argument_list|,
name|v0
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|types
operator|.
name|getValues
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|v0
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|""
argument_list|)
argument_list|,
name|v2
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|3
argument_list|,
literal|""
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|4
argument_list|,
literal|""
argument_list|)
argument_list|,
name|v5
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

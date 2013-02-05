begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
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
name|common
operator|.
name|data
operator|.
name|ApprovalType
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
name|data
operator|.
name|ApprovalTypes
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
name|data
operator|.
name|LabelValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|GerritCommonTest
specifier|public
class|class
name|GerritCommonTest
extends|extends
name|PrologTestCase
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
specifier|final
name|ApprovalTypes
name|types
init|=
operator|new
name|ApprovalTypes
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|category
argument_list|(
literal|0
argument_list|,
literal|"CRVW"
argument_list|,
literal|"Code-Review"
argument_list|,
name|value
argument_list|(
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"I would prefer that you didn't submit this"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|2
argument_list|,
literal|"Do not submit"
argument_list|)
argument_list|)
argument_list|,
name|category
argument_list|(
literal|1
argument_list|,
literal|"VRIF"
argument_list|,
literal|"Verified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Verified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|load
argument_list|(
literal|"gerrit"
argument_list|,
literal|"gerrit_common_test.pl"
argument_list|,
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|ApprovalTypes
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|types
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|value (int value, String text)
specifier|private
specifier|static
name|LabelValue
name|value
parameter_list|(
name|int
name|value
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
name|value
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|category (int pos, String id, String name, LabelValue... values)
specifier|private
specifier|static
name|ApprovalType
name|category
parameter_list|(
name|int
name|pos
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|LabelValue
modifier|...
name|values
parameter_list|)
block|{
name|ApprovalType
name|type
init|=
operator|new
name|ApprovalType
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setPosition
argument_list|(
operator|(
name|short
operator|)
name|pos
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
block|}
end_class

end_unit


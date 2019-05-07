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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|util
operator|.
name|regex
operator|.
name|Pattern
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
DECL|class|ChangeUtilTest
specifier|public
class|class
name|ChangeUtilTest
block|{
annotation|@
name|Test
DECL|method|changeMessageUuid ()
specifier|public
name|void
name|changeMessageUuid
parameter_list|()
throws|throws
name|Exception
block|{
name|Pattern
name|pat
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[0-9a-f]{8}_[0-9a-f]{8}$"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|"abcd1234_0987fedc"
argument_list|)
operator|.
name|matches
argument_list|(
name|pat
argument_list|)
expr_stmt|;
name|String
name|id1
init|=
name|ChangeUtil
operator|.
name|messageUuid
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|id1
argument_list|)
operator|.
name|matches
argument_list|(
name|pat
argument_list|)
expr_stmt|;
name|String
name|id2
init|=
name|ChangeUtil
operator|.
name|messageUuid
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|id2
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|id1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|id2
argument_list|)
operator|.
name|matches
argument_list|(
name|pat
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


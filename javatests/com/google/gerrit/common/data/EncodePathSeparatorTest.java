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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|EncodePathSeparatorTest
specifier|public
class|class
name|EncodePathSeparatorTest
block|{
annotation|@
name|Test
DECL|method|defaultBehaviour ()
specifier|public
name|void
name|defaultBehaviour
parameter_list|()
block|{
name|assertThat
argument_list|(
operator|new
name|GitwebType
argument_list|()
operator|.
name|replacePathSeparator
argument_list|(
literal|"a/b"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"a/b"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exclamationMark ()
specifier|public
name|void
name|exclamationMark
parameter_list|()
block|{
name|GitwebType
name|gitwebType
init|=
operator|new
name|GitwebType
argument_list|()
decl_stmt|;
name|gitwebType
operator|.
name|setPathSeparator
argument_list|(
literal|'!'
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gitwebType
operator|.
name|replacePathSeparator
argument_list|(
literal|"a/b"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"a!b"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


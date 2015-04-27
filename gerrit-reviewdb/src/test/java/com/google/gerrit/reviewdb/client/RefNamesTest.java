begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
DECL|class|RefNamesTest
specifier|public
class|class
name|RefNamesTest
block|{
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1011123
argument_list|)
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|67473
argument_list|)
decl_stmt|;
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
init|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
literal|42
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|fullName ()
specifier|public
name|void
name|fullName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|RefNames
operator|.
name|fullName
argument_list|(
literal|"refs/meta/config"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RefNames
operator|.
name|fullName
argument_list|(
literal|"refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RefNames
operator|.
name|fullName
argument_list|(
literal|"master"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RefNames
operator|.
name|fullName
argument_list|(
literal|"refs/tags/v1.0"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/tags/v1.0"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refsUsers ()
specifier|public
name|void
name|refsUsers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|accountId
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/users/23/1011123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refsDraftComments ()
specifier|public
name|void
name|refsDraftComments
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|RefNames
operator|.
name|refsDraftComments
argument_list|(
name|accountId
argument_list|,
name|changeId
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/draft-comments/23/1011123-67473"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refsEdit ()
specifier|public
name|void
name|refsEdit
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|RefNames
operator|.
name|refsEdit
argument_list|(
name|accountId
argument_list|,
name|changeId
argument_list|,
name|psId
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/users/23/1011123/edit-67473/42"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


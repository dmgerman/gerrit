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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
DECL|class|AccountTest
specifier|public
class|class
name|AccountTest
block|{
annotation|@
name|Test
DECL|method|parseRefName ()
specifier|public
name|void
name|parseRefName
parameter_list|()
block|{
name|assertRef
argument_list|(
literal|1
argument_list|,
literal|"refs/users/01/1"
argument_list|)
expr_stmt|;
name|assertRef
argument_list|(
literal|1
argument_list|,
literal|"refs/users/01/1-drafts"
argument_list|)
expr_stmt|;
name|assertRef
argument_list|(
literal|1
argument_list|,
literal|"refs/users/01/1-drafts/2"
argument_list|)
expr_stmt|;
name|assertRef
argument_list|(
literal|1
argument_list|,
literal|"refs/users/01/1/edit/2"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// Invalid characters.
name|assertNotRef
argument_list|(
literal|"refs/users/01a/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/users/01/a1"
argument_list|)
expr_stmt|;
comment|// Mismatched shard.
name|assertNotRef
argument_list|(
literal|"refs/users/01/23"
argument_list|)
expr_stmt|;
comment|// Shard too short.
name|assertNotRef
argument_list|(
literal|"refs/users/1/1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseRefNameParts ()
specifier|public
name|void
name|parseRefNameParts
parameter_list|()
block|{
name|assertRefPart
argument_list|(
literal|1
argument_list|,
literal|"01/1"
argument_list|)
expr_stmt|;
name|assertRefPart
argument_list|(
literal|1
argument_list|,
literal|"01/1-drafts"
argument_list|)
expr_stmt|;
name|assertRefPart
argument_list|(
literal|1
argument_list|,
literal|"01/1-drafts/2"
argument_list|)
expr_stmt|;
name|assertNotRefPart
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotRefPart
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// This method assumes that the common prefix "refs/users/" will be removed.
name|assertNotRefPart
argument_list|(
literal|"refs/users/01/1"
argument_list|)
expr_stmt|;
comment|// Invalid characters.
name|assertNotRefPart
argument_list|(
literal|"01a/1"
argument_list|)
expr_stmt|;
name|assertNotRefPart
argument_list|(
literal|"01/a1"
argument_list|)
expr_stmt|;
comment|// Mismatched shard.
name|assertNotRefPart
argument_list|(
literal|"01/23"
argument_list|)
expr_stmt|;
comment|// Shard too short.
name|assertNotRefPart
argument_list|(
literal|"1/1"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRef (int accountId, String refName)
specifier|private
specifier|static
name|void
name|assertRef
parameter_list|(
name|int
name|accountId
parameter_list|,
name|String
name|refName
parameter_list|)
block|{
name|assertEquals
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotRef (String refName)
specifier|private
specifier|static
name|void
name|assertNotRef
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|assertNull
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRefPart (int accountId, String refName)
specifier|private
specifier|static
name|void
name|assertRefPart
parameter_list|(
name|int
name|accountId
parameter_list|,
name|String
name|refName
parameter_list|)
block|{
name|assertEquals
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|fromRefPart
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotRefPart (String refName)
specifier|private
specifier|static
name|void
name|assertNotRefPart
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|assertNull
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|fromRefPart
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


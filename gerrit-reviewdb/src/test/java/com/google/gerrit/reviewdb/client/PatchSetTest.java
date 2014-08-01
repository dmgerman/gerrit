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
name|assertFalse
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
DECL|class|PatchSetTest
specifier|public
class|class
name|PatchSetTest
block|{
annotation|@
name|Test
DECL|method|parseRefNames ()
specifier|public
name|void
name|parseRefNames
parameter_list|()
block|{
name|assertRef
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"refs/changes/01/1/1"
argument_list|)
expr_stmt|;
name|assertRef
argument_list|(
literal|1234
argument_list|,
literal|56
argument_list|,
literal|"refs/changes/34/1234/56"
argument_list|)
expr_stmt|;
comment|// Not even close.
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
name|assertNotRef
argument_list|(
literal|"01/1/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"HEAD"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/tags/v1"
argument_list|)
expr_stmt|;
comment|// Invalid characters.
name|assertNotRef
argument_list|(
literal|"refs/changes/0x/1/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/x/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/1/x"
argument_list|)
expr_stmt|;
comment|// Truncations.
name|assertNotRef
argument_list|(
literal|"refs/changes/"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/1/"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/1/1/"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01//1/1"
argument_list|)
expr_stmt|;
comment|// Leading zeroes.
name|assertNotRef
argument_list|(
literal|"refs/changes/01/01/1"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/01/1/01"
argument_list|)
expr_stmt|;
comment|// Mismatched last 2 digits.
name|assertNotRef
argument_list|(
literal|"refs/changes/35/1234/56"
argument_list|)
expr_stmt|;
comment|// Something other than patch set after change.
name|assertNotRef
argument_list|(
literal|"refs/changes/34/1234/0"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/34/1234/foo"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/34/1234|56"
argument_list|)
expr_stmt|;
name|assertNotRef
argument_list|(
literal|"refs/changes/34/1234foo"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRef (int changeId, int psId, String refName)
specifier|private
specifier|static
name|void
name|assertRef
parameter_list|(
name|int
name|changeId
parameter_list|,
name|int
name|psId
parameter_list|,
name|String
name|refName
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|PatchSet
operator|.
name|isRef
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|changeId
argument_list|)
argument_list|,
name|psId
argument_list|)
argument_list|,
name|PatchSet
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
name|assertFalse
argument_list|(
name|PatchSet
operator|.
name|isRef
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|PatchSet
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
block|}
end_class

end_unit


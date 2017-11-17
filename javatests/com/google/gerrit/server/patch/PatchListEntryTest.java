begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Patch
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
DECL|class|PatchListEntryTest
specifier|public
class|class
name|PatchListEntryTest
block|{
annotation|@
name|Test
DECL|method|empty1 ()
specifier|public
name|void
name|empty1
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
literal|"empty-file"
decl_stmt|;
specifier|final
name|PatchListEntry
name|e
init|=
name|PatchListEntry
operator|.
name|empty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Patch
operator|.
name|PatchType
operator|.
name|UNIFIED
argument_list|,
name|e
operator|.
name|getPatchType
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Patch
operator|.
name|ChangeType
operator|.
name|MODIFIED
argument_list|,
name|e
operator|.
name|getChangeType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getEdits
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


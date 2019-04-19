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
DECL|package|com.google.gerrit.server.edit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|edit
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
name|Account
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
name|Change
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
name|PatchSet
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
name|RefNames
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
name|testing
operator|.
name|GerritBaseTests
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
DECL|class|ChangeEditTest
specifier|public
class|class
name|ChangeEditTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|changeEditRef ()
specifier|public
name|void
name|changeEditRef
parameter_list|()
throws|throws
name|Exception
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|Account
operator|.
name|id
argument_list|(
literal|1000042
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|id
argument_list|(
literal|56414
argument_list|)
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|PatchSet
operator|.
name|id
argument_list|(
name|changeId
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|String
name|refName
init|=
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|"refs/users/42/1000042/edit-56414/50"
argument_list|,
name|refName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


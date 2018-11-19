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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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
DECL|class|GroupUUIDTest
specifier|public
class|class
name|GroupUUIDTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|createdUuidsForSameInputShouldBeDifferent ()
specifier|public
name|void
name|createdUuidsForSameInputShouldBeDifferent
parameter_list|()
block|{
name|String
name|groupName
init|=
literal|"Users"
decl_stmt|;
name|PersonIdent
name|personIdent
init|=
operator|new
name|PersonIdent
argument_list|(
literal|"John"
argument_list|,
literal|"john@example.com"
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|uuid1
init|=
name|GroupUUID
operator|.
name|make
argument_list|(
name|groupName
argument_list|,
name|personIdent
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|uuid2
init|=
name|GroupUUID
operator|.
name|make
argument_list|(
name|groupName
argument_list|,
name|personIdent
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|uuid2
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|uuid1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


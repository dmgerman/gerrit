begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|assertTrue
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
name|base
operator|.
name|Strings
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
name|extensions
operator|.
name|restapi
operator|.
name|Url
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|server
operator|.
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ProjectAssert
specifier|public
class|class
name|ProjectAssert
block|{
DECL|method|assertProjectInfo (Project project, ProjectInfo info)
specifier|public
specifier|static
name|void
name|assertProjectInfo
parameter_list|(
name|Project
name|project
parameter_list|,
name|ProjectInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|name
operator|!=
literal|null
condition|)
block|{
comment|// 'name' is not set if returned in a map
name|assertEquals
argument_list|(
name|project
operator|.
name|getName
argument_list|()
argument_list|,
name|info
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|project
operator|.
name|getName
argument_list|()
argument_list|,
name|Url
operator|.
name|decode
argument_list|(
name|info
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|parentName
init|=
name|project
operator|.
name|getParent
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"All-Projects"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parentName
operator|!=
literal|null
condition|?
name|parentName
operator|.
name|get
argument_list|()
else|:
literal|null
argument_list|,
name|info
operator|.
name|parent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|project
operator|.
name|getDescription
argument_list|()
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|info
operator|.
name|description
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertProjectOwners (Set<AccountGroup.UUID> expectedOwners, ProjectState state)
specifier|public
specifier|static
name|void
name|assertProjectOwners
parameter_list|(
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|expectedOwners
parameter_list|,
name|ProjectState
name|state
parameter_list|)
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|g
range|:
name|state
operator|.
name|getOwners
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
literal|"unexpected owner group "
operator|+
name|g
argument_list|,
name|expectedOwners
operator|.
name|remove
argument_list|(
name|g
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"missing owner groups: "
operator|+
name|expectedOwners
argument_list|,
name|expectedOwners
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.acceptance.rest.group
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
name|group
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|GroupAssert
specifier|public
class|class
name|GroupAssert
block|{
DECL|method|assertGroups (Iterable<String> expected, Set<String> actual)
specifier|public
specifier|static
name|void
name|assertGroups
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|expected
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|actual
parameter_list|)
block|{
for|for
control|(
name|String
name|g
range|:
name|expected
control|)
block|{
name|assertTrue
argument_list|(
literal|"missing group "
operator|+
name|g
argument_list|,
name|actual
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
literal|"unexpected groups: "
operator|+
name|actual
argument_list|,
name|actual
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertGroupInfo (AccountGroup group, GroupInfo info)
specifier|public
specifier|static
name|void
name|assertGroupInfo
parameter_list|(
name|AccountGroup
name|group
parameter_list|,
name|GroupInfo
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
name|group
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
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|info
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|group
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|info
operator|.
name|group_id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#/admin/groups/uuid-"
operator|+
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|info
operator|.
name|url
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|group
operator|.
name|isVisibleToAll
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|info
operator|.
name|options
operator|.
name|visible_to_all
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|group
operator|.
name|getDescription
argument_list|()
argument_list|,
name|info
operator|.
name|description
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|group
operator|.
name|getOwnerGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|info
operator|.
name|owner_id
argument_list|)
expr_stmt|;
block|}
DECL|method|toBoolean (Boolean b)
specifier|private
specifier|static
name|boolean
name|toBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|b
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit


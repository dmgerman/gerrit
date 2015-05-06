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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|common
operator|.
name|data
operator|.
name|Permission
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|git
operator|.
name|ProjectConfig
import|;
end_import

begin_class
DECL|class|AclUtil
specifier|public
class|class
name|AclUtil
block|{
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, GroupReference... groupList)
specifier|public
specifier|static
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|grant
argument_list|(
name|config
argument_list|,
name|section
argument_list|,
name|permission
argument_list|,
literal|false
argument_list|,
name|groupList
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, boolean force, GroupReference... groupList)
specifier|public
specifier|static
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|boolean
name|force
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|permission
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|group
range|:
name|groupList
control|)
block|{
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|r
operator|.
name|setForce
argument_list|(
name|force
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, LabelType type, int min, int max, GroupReference... groupList)
specifier|public
specifier|static
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|LabelType
name|type
parameter_list|,
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|,
name|GroupReference
modifier|...
name|groupList
parameter_list|)
block|{
name|String
name|name
init|=
name|Permission
operator|.
name|LABEL
operator|+
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|group
range|:
name|groupList
control|)
block|{
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|r
operator|.
name|setRange
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|rule (ProjectConfig config, GroupReference group)
specifier|public
specifier|static
name|PermissionRule
name|rule
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|GroupReference
name|group
parameter_list|)
block|{
return|return
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


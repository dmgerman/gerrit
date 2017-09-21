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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|Permission
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_enum
DECL|enum|ChangePermission
specifier|public
enum|enum
name|ChangePermission
implements|implements
name|ChangePermissionOrLabel
block|{
DECL|enumConstant|READ
name|READ
parameter_list|(
name|Permission
operator|.
name|READ
parameter_list|)
operator|,
DECL|enumConstant|RESTORE
constructor|RESTORE
operator|,
DECL|enumConstant|DELETE
constructor|DELETE
operator|,
DECL|enumConstant|ABANDON
constructor|ABANDON(Permission.ABANDON
block|)
enum|,
DECL|enumConstant|EDIT_ASSIGNEE
name|EDIT_ASSIGNEE
argument_list|(
name|Permission
operator|.
name|EDIT_ASSIGNEE
argument_list|)
operator|,
DECL|enumConstant|EDIT_DESCRIPTION
name|EDIT_DESCRIPTION
operator|,
DECL|enumConstant|EDIT_HASHTAGS
name|EDIT_HASHTAGS
argument_list|(
name|Permission
operator|.
name|EDIT_HASHTAGS
argument_list|)
operator|,
DECL|enumConstant|EDIT_TOPIC_NAME
name|EDIT_TOPIC_NAME
argument_list|(
name|Permission
operator|.
name|EDIT_TOPIC_NAME
argument_list|)
operator|,
DECL|enumConstant|REMOVE_REVIEWER
name|REMOVE_REVIEWER
argument_list|(
name|Permission
operator|.
name|REMOVE_REVIEWER
argument_list|)
operator|,
DECL|enumConstant|ADD_PATCH_SET
name|ADD_PATCH_SET
argument_list|(
name|Permission
operator|.
name|ADD_PATCH_SET
argument_list|)
operator|,
DECL|enumConstant|REBASE
name|REBASE
argument_list|(
name|Permission
operator|.
name|REBASE
argument_list|)
operator|,
DECL|enumConstant|SUBMIT
name|SUBMIT
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|)
operator|,
DECL|enumConstant|SUBMIT_AS
name|SUBMIT_AS
argument_list|(
name|Permission
operator|.
name|SUBMIT_AS
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|ChangePermission ()
name|ChangePermission
argument_list|()
block|{
name|name
operator|=
literal|null
block|;   }
DECL|method|ChangePermission (String name)
name|ChangePermission
argument_list|(
name|String
name|name
argument_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
block|;   }
comment|/** @return name used in {@code project.config} permissions. */
expr|@
name|Override
DECL|method|permissionName ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|permissionName
argument_list|()
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|name
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
return|return
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|' '
argument_list|)
return|;
block|}
end_function

unit|}
end_unit


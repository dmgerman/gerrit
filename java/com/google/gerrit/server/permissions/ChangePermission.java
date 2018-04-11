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
block|,
DECL|enumConstant|RESTORE
name|RESTORE
block|,
DECL|enumConstant|DELETE
name|DELETE
block|,
DECL|enumConstant|ABANDON
name|ABANDON
block|,
DECL|enumConstant|EDIT_ASSIGNEE
name|EDIT_ASSIGNEE
block|,
DECL|enumConstant|EDIT_DESCRIPTION
name|EDIT_DESCRIPTION
block|,
DECL|enumConstant|EDIT_HASHTAGS
name|EDIT_HASHTAGS
block|,
DECL|enumConstant|EDIT_TOPIC_NAME
name|EDIT_TOPIC_NAME
block|,
DECL|enumConstant|REMOVE_REVIEWER
name|REMOVE_REVIEWER
block|,
DECL|enumConstant|ADD_PATCH_SET
name|ADD_PATCH_SET
block|,
DECL|enumConstant|REBASE
name|REBASE
block|,
DECL|enumConstant|SUBMIT
name|SUBMIT
block|,
DECL|enumConstant|SUBMIT_AS
name|SUBMIT_AS
block|; }
end_enum

end_unit


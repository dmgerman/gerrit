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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|api
operator|.
name|access
operator|.
name|GerritPermission
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
block|,
comment|/**    * The change can't be restored if its current patch set is locked.    *    *<p>Before checking this permission, the caller should first verify the current patch set of the    * change is not locked by calling {@code PatchSetUtil.isPatchSetLocked}.    */
DECL|enumConstant|RESTORE
name|RESTORE
block|,
DECL|enumConstant|DELETE
name|DELETE
block|,
comment|/**    * The change can't be abandoned if its current patch set is locked.    *    *<p>Before checking this permission, the caller should first verify the current patch set of the    * change is not locked by calling {@code PatchSetUtil.isPatchSetLocked}.    */
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
comment|/**    * A new patch set can't be added if the patch set is locked for the change.    *    *<p>Before checking this permission, the caller should first verify the current patch set of the    * change is not locked by calling {@code PatchSetUtil.isPatchSetLocked}.    */
DECL|enumConstant|ADD_PATCH_SET
name|ADD_PATCH_SET
block|,
comment|/**    * The change can't be rebased if its current patch set is locked.    *    *<p>Before checking this permission, the caller should first verify the current patch set of the    * change is not locked by calling {@code PatchSetUtil.isPatchSetLocked}.    */
DECL|enumConstant|REBASE
name|REBASE
block|,
DECL|enumConstant|SUBMIT
name|SUBMIT
block|,
DECL|enumConstant|SUBMIT_AS
name|SUBMIT_AS
argument_list|(
literal|"submit on behalf of other users"
argument_list|)
block|,
DECL|enumConstant|TOGGLE_WORK_IN_PROGRESS_STATE
name|TOGGLE_WORK_IN_PROGRESS_STATE
block|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|ChangePermission ()
name|ChangePermission
parameter_list|()
block|{
name|this
operator|.
name|description
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|ChangePermission (String description)
name|ChangePermission
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|requireNonNull
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
return|return
name|description
operator|!=
literal|null
condition|?
name|description
else|:
name|GerritPermission
operator|.
name|describeEnumValue
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_enum

end_unit


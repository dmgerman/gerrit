begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb.rebuild
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|rebuild
package|;
end_package

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
name|MoreObjects
operator|.
name|ToStringHelper
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
name|PatchSetApproval
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
name|notedb
operator|.
name|ChangeUpdate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|ApprovalEvent
class|class
name|ApprovalEvent
extends|extends
name|Event
block|{
DECL|field|psa
specifier|private
name|PatchSetApproval
name|psa
decl_stmt|;
DECL|method|ApprovalEvent (PatchSetApproval psa, Timestamp changeCreatedOn)
name|ApprovalEvent
parameter_list|(
name|PatchSetApproval
name|psa
parameter_list|,
name|Timestamp
name|changeCreatedOn
parameter_list|)
block|{
name|super
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|psa
operator|.
name|getRealAccountId
argument_list|()
argument_list|,
name|psa
operator|.
name|getGranted
argument_list|()
argument_list|,
name|changeCreatedOn
argument_list|,
name|psa
operator|.
name|getTag
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|psa
operator|=
name|psa
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|canHaveTag ()
specifier|protected
name|boolean
name|canHaveTag
parameter_list|()
block|{
comment|// Legacy SUBM approvals don't have a tag field set, but the corresponding
comment|// ChangeMessage for merging the change does. We need to let these be in the
comment|// same meta commit so the SUBM approval isn't counted as post-submit.
return|return
operator|!
name|psa
operator|.
name|isLegacySubmit
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeUpdate update)
name|void
name|apply
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
block|{
name|checkUpdate
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|,
name|psa
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isPostSubmitApproval ()
specifier|protected
name|boolean
name|isPostSubmitApproval
parameter_list|()
block|{
return|return
name|psa
operator|.
name|isPostSubmit
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addToString (ToStringHelper helper)
specifier|protected
name|void
name|addToString
parameter_list|(
name|ToStringHelper
name|helper
parameter_list|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"approval"
argument_list|,
name|psa
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


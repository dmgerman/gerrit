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
DECL|package|com.google.gerrit.server.rpc.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rpc
operator|.
name|changedetail
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
name|client
operator|.
name|changes
operator|.
name|ChangeManageService
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategoryValue
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
name|client
operator|.
name|reviewdb
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|ChangeManageServiceImpl
class|class
name|ChangeManageServiceImpl
implements|implements
name|ChangeManageService
block|{
DECL|field|submitAction
specifier|private
specifier|final
name|SubmitAction
operator|.
name|Factory
name|submitAction
decl_stmt|;
DECL|field|abandonChangeFactory
specifier|private
specifier|final
name|AbandonChange
operator|.
name|Factory
name|abandonChangeFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeManageServiceImpl (final SubmitAction.Factory patchSetAction, final AbandonChange.Factory abandonChangeFactory)
name|ChangeManageServiceImpl
parameter_list|(
specifier|final
name|SubmitAction
operator|.
name|Factory
name|patchSetAction
parameter_list|,
specifier|final
name|AbandonChange
operator|.
name|Factory
name|abandonChangeFactory
parameter_list|)
block|{
name|this
operator|.
name|submitAction
operator|=
name|patchSetAction
expr_stmt|;
name|this
operator|.
name|abandonChangeFactory
operator|=
name|abandonChangeFactory
expr_stmt|;
block|}
DECL|method|patchSetAction (final ApprovalCategoryValue.Id value, final PatchSet.Id patchSetId, final AsyncCallback<VoidResult> cb)
specifier|public
name|void
name|patchSetAction
parameter_list|(
specifier|final
name|ApprovalCategoryValue
operator|.
name|Id
name|value
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|category
init|=
name|value
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|ApprovalCategory
operator|.
name|SUBMIT
operator|.
name|equals
argument_list|(
name|category
argument_list|)
operator|&&
name|value
operator|.
name|get
argument_list|()
operator|==
literal|1
condition|)
block|{
name|submitAction
operator|.
name|create
argument_list|(
name|patchSetId
argument_list|)
operator|.
name|to
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
name|value
operator|+
literal|" not supported"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|abandonChange (final PatchSet.Id patchSetId, final String message, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|abandonChange
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|abandonChangeFactory
operator|.
name|create
argument_list|(
name|patchSetId
argument_list|,
name|message
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.httpd.rpc.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|common
operator|.
name|data
operator|.
name|ApprovalDetail
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
name|ApprovalType
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
name|ApprovalTypes
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
name|PatchSetPublishDetail
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
name|PermissionRange
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
name|SubmitRecord
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|PatchLineComment
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetInfo
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
name|server
operator|.
name|ReviewDb
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
name|IdentifiedUser
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
name|account
operator|.
name|AccountInfoCacheFactory
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
name|patch
operator|.
name|PatchSetInfoFactory
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
name|patch
operator|.
name|PatchSetInfoNotAvailableException
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
name|ChangeControl
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
name|NoSuchChangeException
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
name|workflow
operator|.
name|CategoryFunction
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
name|workflow
operator|.
name|FunctionState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|PatchSetPublishDetailFactory
specifier|final
class|class
name|PatchSetPublishDetailFactory
extends|extends
name|Handler
argument_list|<
name|PatchSetPublishDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (PatchSet.Id patchSetId)
name|PatchSetPublishDetailFactory
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
function_decl|;
block|}
DECL|field|infoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|infoFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|functionState
specifier|private
specifier|final
name|FunctionState
operator|.
name|Factory
name|functionState
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|approvalTypes
specifier|private
specifier|final
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|field|aic
specifier|private
specifier|final
name|AccountInfoCacheFactory
name|aic
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
DECL|field|patchSetInfo
specifier|private
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|drafts
specifier|private
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|drafts
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetPublishDetailFactory (final PatchSetInfoFactory infoFactory, final ReviewDb db, final AccountInfoCacheFactory.Factory accountInfoCacheFactory, final FunctionState.Factory functionState, final ChangeControl.Factory changeControlFactory, final ApprovalTypes approvalTypes, final IdentifiedUser user, @Assisted final PatchSet.Id patchSetId)
name|PatchSetPublishDetailFactory
parameter_list|(
specifier|final
name|PatchSetInfoFactory
name|infoFactory
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|AccountInfoCacheFactory
operator|.
name|Factory
name|accountInfoCacheFactory
parameter_list|,
specifier|final
name|FunctionState
operator|.
name|Factory
name|functionState
parameter_list|,
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
specifier|final
name|ApprovalTypes
name|approvalTypes
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
block|{
name|this
operator|.
name|infoFactory
operator|=
name|infoFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|functionState
operator|=
name|functionState
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|approvalTypes
operator|=
name|approvalTypes
expr_stmt|;
name|this
operator|.
name|aic
operator|=
name|accountInfoCacheFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|PatchSetPublishDetail
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|PatchSetInfoNotAvailableException
throws|,
name|NoSuchChangeException
block|{
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|ChangeControl
name|control
init|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|change
operator|=
name|control
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|PatchSet
name|patchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
name|patchSetInfo
operator|=
name|infoFactory
operator|.
name|get
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|)
expr_stmt|;
name|drafts
operator|=
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draftByPatchSetAuthor
argument_list|(
name|patchSetId
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
name|aic
operator|.
name|want
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|PatchSetPublishDetail
name|detail
init|=
operator|new
name|PatchSetPublishDetail
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setPatchSetInfo
argument_list|(
name|patchSetInfo
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setChange
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setDrafts
argument_list|(
name|drafts
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PermissionRange
argument_list|>
name|allowed
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|given
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
name|patchSetId
operator|.
name|equals
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
condition|)
block|{
comment|// TODO Push this selection of labels down into the Prolog interpreter.
comment|// Ideally we discover the labels the user can apply here based on doing
comment|// a findall() over the space of labels they can apply combined against
comment|// the submit rule, thereby skipping any mutually exclusive cases. However
comment|// those are not common, so it might just be reasonable to take this
comment|// simple approach.
name|Map
argument_list|<
name|String
argument_list|,
name|PermissionRange
argument_list|>
name|rangeByName
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PermissionRange
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PermissionRange
name|r
range|:
name|control
operator|.
name|getLabelRanges
argument_list|()
control|)
block|{
if|if
condition|(
name|r
operator|.
name|isLabel
argument_list|()
condition|)
block|{
name|rangeByName
operator|.
name|put
argument_list|(
name|r
operator|.
name|getLabel
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
block|}
name|allowed
operator|=
operator|new
name|ArrayList
argument_list|<
name|PermissionRange
argument_list|>
argument_list|()
expr_stmt|;
name|given
operator|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
comment|//
operator|.
name|byPatchSetUser
argument_list|(
name|patchSetId
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
comment|//
operator|.
name|toList
argument_list|()
expr_stmt|;
name|boolean
name|couldSubmit
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
init|=
name|control
operator|.
name|canSubmit
argument_list|(
name|db
argument_list|,
name|patchSet
argument_list|)
decl_stmt|;
for|for
control|(
name|SubmitRecord
name|rec
range|:
name|submitRecords
control|)
block|{
if|if
condition|(
name|rec
operator|.
name|status
operator|==
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
condition|)
block|{
name|couldSubmit
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|rec
operator|.
name|labels
operator|!=
literal|null
condition|)
block|{
name|int
name|ok
init|=
literal|0
decl_stmt|;
for|for
control|(
name|SubmitRecord
operator|.
name|Label
name|lbl
range|:
name|rec
operator|.
name|labels
control|)
block|{
name|aic
operator|.
name|want
argument_list|(
name|lbl
operator|.
name|appliedBy
argument_list|)
expr_stmt|;
name|boolean
name|canMakeOk
init|=
literal|false
decl_stmt|;
name|PermissionRange
name|range
init|=
name|rangeByName
operator|.
name|get
argument_list|(
name|lbl
operator|.
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|range
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|allowed
operator|.
name|contains
argument_list|(
name|range
argument_list|)
condition|)
block|{
name|allowed
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
name|ApprovalType
name|at
init|=
name|approvalTypes
operator|.
name|byLabel
argument_list|(
name|lbl
operator|.
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|==
literal|null
operator|||
name|at
operator|.
name|getMax
argument_list|()
operator|.
name|getValue
argument_list|()
operator|==
name|range
operator|.
name|getMax
argument_list|()
condition|)
block|{
name|canMakeOk
operator|=
literal|true
expr_stmt|;
block|}
block|}
switch|switch
condition|(
name|lbl
operator|.
name|status
condition|)
block|{
case|case
name|OK
case|:
case|case
name|MAY
case|:
name|ok
operator|++
expr_stmt|;
break|break;
case|case
name|NEED
case|:
if|if
condition|(
name|canMakeOk
condition|)
block|{
name|ok
operator|++
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|rec
operator|.
name|status
operator|==
name|SubmitRecord
operator|.
name|Status
operator|.
name|NOT_READY
operator|&&
name|ok
operator|==
name|rec
operator|.
name|labels
operator|.
name|size
argument_list|()
condition|)
block|{
name|couldSubmit
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|couldSubmit
operator|&&
name|control
operator|.
name|getRefControl
argument_list|()
operator|.
name|canSubmit
argument_list|()
condition|)
block|{
name|detail
operator|.
name|setCanSubmit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setSubmitRecords
argument_list|(
name|submitRecords
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setLabels
argument_list|(
name|allowed
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setGiven
argument_list|(
name|given
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setAccounts
argument_list|(
name|aic
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
name|loadApprovals
argument_list|(
name|detail
argument_list|,
name|control
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
DECL|method|loadApprovals (final PatchSetPublishDetail detail, final ChangeControl control)
specifier|private
name|void
name|loadApprovals
parameter_list|(
specifier|final
name|PatchSetPublishDetail
name|detail
parameter_list|,
specifier|final
name|ChangeControl
name|control
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|allApprovals
init|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|changeId
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
specifier|final
name|FunctionState
name|fs
init|=
name|functionState
operator|.
name|create
argument_list|(
name|control
argument_list|,
name|psId
argument_list|,
name|allApprovals
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ApprovalType
name|at
range|:
name|approvalTypes
operator|.
name|getApprovalTypes
argument_list|()
control|)
block|{
name|CategoryFunction
operator|.
name|forCategory
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
argument_list|)
operator|.
name|run
argument_list|(
name|at
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|boolean
name|canRemoveReviewers
init|=
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
comment|//
operator|&&
name|control
operator|.
name|getCurrentUser
argument_list|()
operator|instanceof
name|IdentifiedUser
decl_stmt|;
specifier|final
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ApprovalDetail
argument_list|>
name|ad
init|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ApprovalDetail
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|ca
range|:
name|allApprovals
control|)
block|{
name|ApprovalDetail
name|d
init|=
name|ad
operator|.
name|get
argument_list|(
name|ca
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
operator|new
name|ApprovalDetail
argument_list|(
name|ca
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|d
operator|.
name|setCanRemove
argument_list|(
name|canRemoveReviewers
argument_list|)
expr_stmt|;
name|ad
operator|.
name|put
argument_list|(
name|d
operator|.
name|getAccount
argument_list|()
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|d
operator|.
name|canRemove
argument_list|()
condition|)
block|{
name|d
operator|.
name|setCanRemove
argument_list|(
name|control
operator|.
name|canRemoveReviewer
argument_list|(
name|ca
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ca
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|psId
argument_list|)
condition|)
block|{
name|d
operator|.
name|add
argument_list|(
name|ca
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Account
operator|.
name|Id
name|owner
init|=
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getOwner
argument_list|()
decl_stmt|;
if|if
condition|(
name|ad
operator|.
name|containsKey
argument_list|(
name|owner
argument_list|)
condition|)
block|{
comment|// Ensure the owner always sorts to the top of the table
name|ad
operator|.
name|get
argument_list|(
name|owner
argument_list|)
operator|.
name|sortFirst
argument_list|()
expr_stmt|;
block|}
name|aic
operator|.
name|want
argument_list|(
name|ad
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setApprovals
argument_list|(
name|ad
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


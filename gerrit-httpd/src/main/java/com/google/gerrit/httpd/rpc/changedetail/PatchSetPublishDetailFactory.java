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
name|AccountInfoCache
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
name|reviewdb
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
name|RefRight
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
name|CanSubmitResult
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
name|project
operator|.
name|ProjectCache
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
name|RefControl
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
name|client
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|infoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|infoFactory
decl_stmt|;
DECL|field|approvalTypes
specifier|private
specifier|final
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
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
DECL|field|accounts
specifier|private
name|AccountInfoCache
name|accounts
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
DECL|field|allowed
specifier|private
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|>
name|allowed
decl_stmt|;
DECL|field|given
specifier|private
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|given
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetPublishDetailFactory (final PatchSetInfoFactory infoFactory, final ProjectCache projectCache, final ApprovalTypes approvalTypes, final ReviewDb db, final AccountInfoCacheFactory.Factory accountInfoCacheFactory, final ChangeControl.Factory changeControlFactory, final IdentifiedUser user, @Assisted final PatchSet.Id patchSetId)
name|PatchSetPublishDetailFactory
parameter_list|(
specifier|final
name|PatchSetInfoFactory
name|infoFactory
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|ApprovalTypes
name|approvalTypes
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
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
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
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|infoFactory
operator|=
name|infoFactory
expr_stmt|;
name|this
operator|.
name|approvalTypes
operator|=
name|approvalTypes
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
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
name|patchSetInfo
operator|=
name|infoFactory
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
expr_stmt|;
name|drafts
operator|=
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draft
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
name|allowed
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|given
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|()
expr_stmt|;
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
name|computeAllowed
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|PatchSetApproval
name|a
range|:
name|db
operator|.
name|patchSetApprovals
argument_list|()
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
control|)
block|{
name|given
operator|.
name|put
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
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
name|accounts
operator|=
name|aic
operator|.
name|create
argument_list|()
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
name|setAccounts
argument_list|(
name|accounts
argument_list|)
expr_stmt|;
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
name|detail
operator|.
name|setAllowed
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
specifier|final
name|CanSubmitResult
name|canSubmitResult
init|=
name|control
operator|.
name|canSubmit
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
name|detail
operator|.
name|setSubmitAllowed
argument_list|(
name|canSubmitResult
operator|==
name|CanSubmitResult
operator|.
name|OK
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
DECL|method|computeAllowed ()
specifier|private
name|void
name|computeAllowed
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|am
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
specifier|final
name|ProjectState
name|pe
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ApprovalCategory
operator|.
name|Id
name|category
range|:
name|approvalTypes
operator|.
name|getApprovalCategories
argument_list|()
control|)
block|{
name|RefControl
name|rc
init|=
name|pe
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
operator|.
name|controlForRef
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RefRight
argument_list|>
name|categoryRights
init|=
name|rc
operator|.
name|getApplicableRights
argument_list|(
name|category
argument_list|)
decl_stmt|;
name|computeAllowed
argument_list|(
name|am
argument_list|,
name|categoryRights
argument_list|,
name|category
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|computeAllowed (final Set<AccountGroup.Id> am, final List<RefRight> list, ApprovalCategory.Id category)
specifier|private
name|void
name|computeAllowed
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|am
parameter_list|,
specifier|final
name|List
argument_list|<
name|RefRight
argument_list|>
name|list
parameter_list|,
name|ApprovalCategory
operator|.
name|Id
name|category
parameter_list|)
block|{
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|s
init|=
name|allowed
operator|.
name|get
argument_list|(
name|category
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
name|allowed
operator|.
name|put
argument_list|(
name|category
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|RefRight
name|r
range|:
name|list
control|)
block|{
if|if
condition|(
operator|!
name|am
operator|.
name|contains
argument_list|(
name|r
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
specifier|final
name|ApprovalType
name|at
init|=
name|approvalTypes
operator|.
name|getApprovalType
argument_list|(
name|r
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|short
name|m
init|=
name|r
operator|.
name|getMinValue
argument_list|()
init|;
name|m
operator|<=
name|r
operator|.
name|getMaxValue
argument_list|()
condition|;
name|m
operator|++
control|)
block|{
specifier|final
name|ApprovalCategoryValue
name|v
init|=
name|at
operator|.
name|getValue
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
name|v
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit


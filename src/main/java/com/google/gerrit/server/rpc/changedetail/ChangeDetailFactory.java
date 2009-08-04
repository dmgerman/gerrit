begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|data
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
name|client
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
name|client
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
name|client
operator|.
name|data
operator|.
name|ChangeDetail
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
name|data
operator|.
name|ChangeInfo
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
name|data
operator|.
name|GerritConfig
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
name|client
operator|.
name|reviewdb
operator|.
name|ChangeApproval
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
name|ChangeMessage
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
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|PatchSetAncestor
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
name|client
operator|.
name|reviewdb
operator|.
name|RevId
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|rpc
operator|.
name|NoSuchEntityException
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
name|Comparator
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

begin_comment
comment|/** Creates a {@link ChangeDetail} from a {@link Change}. */
end_comment

begin_class
DECL|class|ChangeDetailFactory
class|class
name|ChangeDetailFactory
extends|extends
name|Handler
argument_list|<
name|ChangeDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (Change.Id id)
name|ChangeDetailFactory
name|create
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
block|}
DECL|field|gerritConfig
specifier|private
specifier|final
name|GerritConfig
name|gerritConfig
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|functionState
specifier|private
specifier|final
name|FunctionState
operator|.
name|Factory
name|functionState
decl_stmt|;
DECL|field|patchSetDetail
specifier|private
specifier|final
name|PatchSetDetailFactory
operator|.
name|Factory
name|patchSetDetail
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|acc
specifier|private
name|AccountInfoCacheFactory
name|acc
decl_stmt|;
DECL|field|detail
specifier|private
name|ChangeDetail
name|detail
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeDetailFactory (final GerritConfig gerritConfig, final FunctionState.Factory functionState, final PatchSetDetailFactory.Factory patchSetDetail, final ReviewDb db, final ChangeControl.Factory changeControlFactory, @Assisted final Change.Id id)
name|ChangeDetailFactory
parameter_list|(
specifier|final
name|GerritConfig
name|gerritConfig
parameter_list|,
specifier|final
name|FunctionState
operator|.
name|Factory
name|functionState
parameter_list|,
specifier|final
name|PatchSetDetailFactory
operator|.
name|Factory
name|patchSetDetail
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|this
operator|.
name|gerritConfig
operator|=
name|gerritConfig
expr_stmt|;
name|this
operator|.
name|functionState
operator|=
name|functionState
expr_stmt|;
name|this
operator|.
name|patchSetDetail
operator|=
name|patchSetDetail
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
name|changeId
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ChangeDetail
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchEntityException
throws|,
name|PatchSetInfoNotAvailableException
throws|,
name|NoSuchChangeException
block|{
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
specifier|final
name|Change
name|change
init|=
name|control
operator|.
name|getChange
argument_list|()
decl_stmt|;
specifier|final
name|Project
name|proj
init|=
name|control
operator|.
name|getProject
argument_list|()
decl_stmt|;
specifier|final
name|PatchSet
name|patch
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|patch
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEntityException
argument_list|()
throw|;
block|}
name|acc
operator|=
operator|new
name|AccountInfoCacheFactory
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|acc
operator|.
name|want
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|=
operator|new
name|ChangeDetail
argument_list|()
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
name|setAllowsAnonymous
argument_list|(
name|control
operator|.
name|forAnonymousUser
argument_list|()
operator|.
name|isVisible
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCanAbandon
argument_list|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
name|control
operator|.
name|canAbandon
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setStarred
argument_list|(
name|control
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|getStarredChanges
argument_list|()
operator|.
name|contains
argument_list|(
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
name|loadPatchSets
argument_list|()
expr_stmt|;
name|loadMessages
argument_list|()
expr_stmt|;
if|if
condition|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|loadCurrentPatchSet
argument_list|()
expr_stmt|;
block|}
name|load
argument_list|()
expr_stmt|;
name|detail
operator|.
name|setAccounts
argument_list|(
name|acc
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
DECL|method|loadPatchSets ()
specifier|private
name|void
name|loadPatchSets
parameter_list|()
throws|throws
name|OrmException
block|{
name|detail
operator|.
name|setPatchSets
argument_list|(
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|changeId
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|loadMessages ()
specifier|private
name|void
name|loadMessages
parameter_list|()
throws|throws
name|OrmException
block|{
name|detail
operator|.
name|setMessages
argument_list|(
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|byChange
argument_list|(
name|changeId
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|ChangeMessage
name|m
range|:
name|detail
operator|.
name|getMessages
argument_list|()
control|)
block|{
name|acc
operator|.
name|want
argument_list|(
name|m
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|load ()
specifier|private
name|void
name|load
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|List
argument_list|<
name|ChangeApproval
argument_list|>
name|allApprovals
init|=
name|db
operator|.
name|changeApprovals
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
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|FunctionState
name|fs
init|=
name|functionState
operator|.
name|create
argument_list|(
name|detail
operator|.
name|getChange
argument_list|()
argument_list|,
name|allApprovals
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|missingApprovals
init|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|currentActions
init|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ApprovalType
name|at
range|:
name|gerritConfig
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
if|if
condition|(
operator|!
name|fs
operator|.
name|isValid
argument_list|(
name|at
argument_list|)
condition|)
block|{
name|missingApprovals
operator|.
name|add
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
specifier|final
name|ApprovalType
name|at
range|:
name|gerritConfig
operator|.
name|getActionTypes
argument_list|()
control|)
block|{
if|if
condition|(
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
name|isValid
argument_list|(
name|me
argument_list|,
name|at
argument_list|,
name|fs
argument_list|)
condition|)
block|{
name|currentActions
operator|.
name|add
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|detail
operator|.
name|setMissingApprovals
argument_list|(
name|missingApprovals
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCurrentActions
argument_list|(
name|currentActions
argument_list|)
expr_stmt|;
block|}
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
name|ChangeApproval
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
name|d
operator|.
name|add
argument_list|(
name|ca
argument_list|)
expr_stmt|;
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
comment|//
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
name|acc
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
DECL|method|loadCurrentPatchSet ()
specifier|private
name|void
name|loadCurrentPatchSet
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchEntityException
throws|,
name|PatchSetInfoNotAvailableException
throws|,
name|NoSuchChangeException
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
name|PatchSetDetailFactory
name|loader
init|=
name|patchSetDetail
operator|.
name|create
argument_list|(
name|psId
argument_list|)
decl_stmt|;
name|loader
operator|.
name|patchSet
operator|=
name|detail
operator|.
name|getCurrentPatchSet
argument_list|()
expr_stmt|;
name|detail
operator|.
name|setCurrentPatchSetDetail
argument_list|(
name|loader
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changesToGet
init|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|ancestorOrder
init|=
operator|new
name|ArrayList
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetAncestor
name|a
range|:
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|ancestorsOf
argument_list|(
name|psId
argument_list|)
control|)
block|{
for|for
control|(
name|PatchSet
name|p
range|:
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byRevision
argument_list|(
name|a
operator|.
name|getAncestorRevision
argument_list|()
argument_list|)
control|)
block|{
specifier|final
name|Change
operator|.
name|Id
name|ck
init|=
name|p
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|changesToGet
operator|.
name|add
argument_list|(
name|ck
argument_list|)
condition|)
block|{
name|ancestorOrder
operator|.
name|add
argument_list|(
name|ck
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|final
name|RevId
name|cprev
init|=
name|loader
operator|.
name|patchSet
operator|.
name|getRevision
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|descendants
init|=
name|cprev
operator|!=
literal|null
condition|?
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|descendantsOf
argument_list|(
name|cprev
argument_list|)
operator|.
name|toList
argument_list|()
else|:
name|Collections
operator|.
expr|<
name|PatchSetAncestor
operator|>
name|emptyList
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|PatchSetAncestor
name|a
range|:
name|descendants
control|)
block|{
name|changesToGet
operator|.
name|add
argument_list|(
name|a
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Change
argument_list|>
name|m
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|toMap
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changesToGet
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
name|dependsOn
init|=
operator|new
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Change
operator|.
name|Id
name|a
range|:
name|ancestorOrder
control|)
block|{
specifier|final
name|Change
name|ac
init|=
name|m
operator|.
name|get
argument_list|(
name|a
argument_list|)
decl_stmt|;
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|dependsOn
operator|.
name|add
argument_list|(
operator|new
name|ChangeInfo
argument_list|(
name|ac
argument_list|,
name|acc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
name|neededBy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|PatchSetAncestor
name|a
range|:
name|descendants
control|)
block|{
specifier|final
name|Change
name|ac
init|=
name|m
operator|.
name|get
argument_list|(
name|a
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|neededBy
operator|.
name|add
argument_list|(
operator|new
name|ChangeInfo
argument_list|(
name|ac
argument_list|,
name|acc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|neededBy
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|ChangeInfo
name|o1
parameter_list|,
specifier|final
name|ChangeInfo
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|-
name|o2
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setDependsOn
argument_list|(
name|dependsOn
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setNeededBy
argument_list|(
name|neededBy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


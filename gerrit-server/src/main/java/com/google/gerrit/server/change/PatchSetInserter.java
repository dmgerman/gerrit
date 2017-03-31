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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
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
name|ReviewerStateInternal
operator|.
name|CC
import|;
end_import

begin_import
import|import static
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
name|ReviewerStateInternal
operator|.
name|REVIEWER
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|changes
operator|.
name|NotifyHandling
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
name|changes
operator|.
name|RecipientType
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|ResourceConflictException
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
name|ApprovalCopier
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
name|ApprovalsUtil
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
name|ChangeMessagesUtil
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
name|PatchSetUtil
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
name|ReviewerSet
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
name|events
operator|.
name|CommitReceivedEvent
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
name|extensions
operator|.
name|events
operator|.
name|RevisionCreated
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
name|validators
operator|.
name|CommitValidationException
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
name|validators
operator|.
name|CommitValidators
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
name|mail
operator|.
name|send
operator|.
name|ReplacePatchSetSender
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
name|ssh
operator|.
name|NoSshInfo
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
name|update
operator|.
name|BatchUpdateOp
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
name|update
operator|.
name|ChangeContext
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
name|update
operator|.
name|Context
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
name|update
operator|.
name|RepoContext
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
name|io
operator|.
name|IOException
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|PatchSetInserter
specifier|public
class|class
name|PatchSetInserter
implements|implements
name|BatchUpdateOp
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PatchSetInserter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ChangeControl ctl, PatchSet.Id psId, RevCommit commit)
name|PatchSetInserter
name|create
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|RevCommit
name|commit
parameter_list|)
function_decl|;
block|}
comment|// Injected fields.
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|commitValidatorsFactory
specifier|private
specifier|final
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
decl_stmt|;
DECL|field|replacePatchSetFactory
specifier|private
specifier|final
name|ReplacePatchSetSender
operator|.
name|Factory
name|replacePatchSetFactory
decl_stmt|;
DECL|field|revisionCreated
specifier|private
specifier|final
name|RevisionCreated
name|revisionCreated
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|approvalCopier
specifier|private
specifier|final
name|ApprovalCopier
name|approvalCopier
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
comment|// Assisted-injected fields.
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|commit
specifier|private
specifier|final
name|RevCommit
name|commit
decl_stmt|;
comment|// Read prior to running the batch update, so must only be used during
comment|// updateRepo; updateChange and later must use the control from the
comment|// ChangeContext.
DECL|field|origCtl
specifier|private
specifier|final
name|ChangeControl
name|origCtl
decl_stmt|;
comment|// Fields exposed as setters.
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|validatePolicy
specifier|private
name|CommitValidators
operator|.
name|Policy
name|validatePolicy
init|=
name|CommitValidators
operator|.
name|Policy
operator|.
name|GERRIT
decl_stmt|;
DECL|field|checkAddPatchSetPermission
specifier|private
name|boolean
name|checkAddPatchSetPermission
init|=
literal|true
decl_stmt|;
DECL|field|draft
specifier|private
name|boolean
name|draft
decl_stmt|;
DECL|field|groups
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|field|fireRevisionCreated
specifier|private
name|boolean
name|fireRevisionCreated
init|=
literal|true
decl_stmt|;
DECL|field|notify
specifier|private
name|NotifyHandling
name|notify
init|=
name|NotifyHandling
operator|.
name|ALL
decl_stmt|;
DECL|field|accountsToNotify
specifier|private
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
init|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
decl_stmt|;
DECL|field|allowClosed
specifier|private
name|boolean
name|allowClosed
decl_stmt|;
DECL|field|copyApprovals
specifier|private
name|boolean
name|copyApprovals
init|=
literal|true
decl_stmt|;
comment|// Fields set during some phase of BatchUpdate.Op.
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|patchSet
specifier|private
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|patchSetInfo
specifier|private
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
DECL|field|changeMessage
specifier|private
name|ChangeMessage
name|changeMessage
decl_stmt|;
DECL|field|oldReviewers
specifier|private
name|ReviewerSet
name|oldReviewers
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetInserter ( ApprovalsUtil approvalsUtil, ApprovalCopier approvalCopier, ChangeMessagesUtil cmUtil, PatchSetInfoFactory patchSetInfoFactory, CommitValidators.Factory commitValidatorsFactory, ReplacePatchSetSender.Factory replacePatchSetFactory, PatchSetUtil psUtil, RevisionCreated revisionCreated, @Assisted ChangeControl ctl, @Assisted PatchSet.Id psId, @Assisted RevCommit commit)
specifier|public
name|PatchSetInserter
parameter_list|(
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|ApprovalCopier
name|approvalCopier
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
parameter_list|,
name|ReplacePatchSetSender
operator|.
name|Factory
name|replacePatchSetFactory
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|RevisionCreated
name|revisionCreated
parameter_list|,
annotation|@
name|Assisted
name|ChangeControl
name|ctl
parameter_list|,
annotation|@
name|Assisted
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
annotation|@
name|Assisted
name|RevCommit
name|commit
parameter_list|)
block|{
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|approvalCopier
operator|=
name|approvalCopier
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|commitValidatorsFactory
operator|=
name|commitValidatorsFactory
expr_stmt|;
name|this
operator|.
name|replacePatchSetFactory
operator|=
name|replacePatchSetFactory
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|revisionCreated
operator|=
name|revisionCreated
expr_stmt|;
name|this
operator|.
name|origCtl
operator|=
name|ctl
expr_stmt|;
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|commit
operator|=
name|commit
expr_stmt|;
block|}
DECL|method|getPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getPatchSetId
parameter_list|()
block|{
return|return
name|psId
return|;
block|}
DECL|method|setMessage (String message)
specifier|public
name|PatchSetInserter
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|PatchSetInserter
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setValidatePolicy (CommitValidators.Policy validate)
specifier|public
name|PatchSetInserter
name|setValidatePolicy
parameter_list|(
name|CommitValidators
operator|.
name|Policy
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validatePolicy
operator|=
name|checkNotNull
argument_list|(
name|validate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setCheckAddPatchSetPermission (boolean checkAddPatchSetPermission)
specifier|public
name|PatchSetInserter
name|setCheckAddPatchSetPermission
parameter_list|(
name|boolean
name|checkAddPatchSetPermission
parameter_list|)
block|{
name|this
operator|.
name|checkAddPatchSetPermission
operator|=
name|checkAddPatchSetPermission
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDraft (boolean draft)
specifier|public
name|PatchSetInserter
name|setDraft
parameter_list|(
name|boolean
name|draft
parameter_list|)
block|{
name|this
operator|.
name|draft
operator|=
name|draft
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setGroups (List<String> groups)
specifier|public
name|PatchSetInserter
name|setGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|groups
argument_list|,
literal|"groups may not be null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setFireRevisionCreated (boolean fireRevisionCreated)
specifier|public
name|PatchSetInserter
name|setFireRevisionCreated
parameter_list|(
name|boolean
name|fireRevisionCreated
parameter_list|)
block|{
name|this
operator|.
name|fireRevisionCreated
operator|=
name|fireRevisionCreated
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setNotify (NotifyHandling notify)
specifier|public
name|PatchSetInserter
name|setNotify
parameter_list|(
name|NotifyHandling
name|notify
parameter_list|)
block|{
name|this
operator|.
name|notify
operator|=
name|notify
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setAccountsToNotify ( ListMultimap<RecipientType, Account.Id> accountsToNotify)
specifier|public
name|PatchSetInserter
name|setAccountsToNotify
parameter_list|(
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
parameter_list|)
block|{
name|this
operator|.
name|accountsToNotify
operator|=
name|checkNotNull
argument_list|(
name|accountsToNotify
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setAllowClosed (boolean allowClosed)
specifier|public
name|PatchSetInserter
name|setAllowClosed
parameter_list|(
name|boolean
name|allowClosed
parameter_list|)
block|{
name|this
operator|.
name|allowClosed
operator|=
name|allowClosed
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setCopyApprovals (boolean copyApprovals)
specifier|public
name|PatchSetInserter
name|setCopyApprovals
parameter_list|(
name|boolean
name|copyApprovals
parameter_list|)
block|{
name|this
operator|.
name|copyApprovals
operator|=
name|copyApprovals
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
name|checkState
argument_list|(
name|change
operator|!=
literal|null
argument_list|,
literal|"getChange() only valid after executing update"
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
DECL|method|getPatchSet ()
specifier|public
name|PatchSet
name|getPatchSet
parameter_list|()
block|{
name|checkState
argument_list|(
name|patchSet
operator|!=
literal|null
argument_list|,
literal|"getPatchSet() only valid after executing update"
argument_list|)
expr_stmt|;
return|return
name|patchSet
return|;
block|}
annotation|@
name|Override
DECL|method|updateRepo (RepoContext ctx)
specifier|public
name|void
name|updateRepo
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|validate
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|addRefUpdate
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|commit
argument_list|,
name|getPatchSetId
argument_list|()
operator|.
name|toRefName
argument_list|()
argument_list|,
name|ReceiveCommand
operator|.
name|Type
operator|.
name|CREATE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateChange (ChangeContext ctx)
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|ReviewDb
name|db
init|=
name|ctx
operator|.
name|getDb
argument_list|()
decl_stmt|;
name|ChangeControl
name|ctl
init|=
name|ctx
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
decl_stmt|;
name|update
operator|.
name|setSubjectForCommit
argument_list|(
literal|"Create patch set "
operator|+
name|psId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
operator|!
name|allowClosed
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot create new patch set of change %s because it is %s"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|newGroups
init|=
name|groups
decl_stmt|;
if|if
condition|(
name|newGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PatchSet
name|prevPs
init|=
name|psUtil
operator|.
name|current
argument_list|(
name|db
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|prevPs
operator|!=
literal|null
condition|)
block|{
name|newGroups
operator|=
name|prevPs
operator|.
name|getGroups
argument_list|()
expr_stmt|;
block|}
block|}
name|patchSet
operator|=
name|psUtil
operator|.
name|insert
argument_list|(
name|db
argument_list|,
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
argument_list|,
name|psId
argument_list|,
name|commit
argument_list|,
name|draft
argument_list|,
name|newGroups
argument_list|,
literal|null
argument_list|,
name|description
argument_list|)
expr_stmt|;
if|if
condition|(
name|notify
operator|!=
name|NotifyHandling
operator|.
name|NONE
condition|)
block|{
name|oldReviewers
operator|=
name|approvalsUtil
operator|.
name|getReviewers
argument_list|(
name|db
argument_list|,
name|ctl
operator|.
name|getNotes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|changeMessage
operator|=
name|ChangeMessagesUtil
operator|.
name|newMessage
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|message
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_UPLOADED_PATCH_SET
argument_list|)
expr_stmt|;
name|changeMessage
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|patchSetInfo
operator|=
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|,
name|commit
argument_list|,
name|psId
argument_list|)
expr_stmt|;
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|!=
name|Change
operator|.
name|Status
operator|.
name|DRAFT
operator|&&
operator|!
name|allowClosed
condition|)
block|{
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
name|change
operator|.
name|setCurrentPatchSet
argument_list|(
name|patchSetInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|copyApprovals
condition|)
block|{
name|approvalCopier
operator|.
name|copy
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|,
name|patchSet
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|changeMessage
operator|!=
literal|null
condition|)
block|{
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|changeMessage
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|postUpdate (Context ctx)
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|notify
operator|!=
name|NotifyHandling
operator|.
name|NONE
operator|||
operator|!
name|accountsToNotify
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|ReplacePatchSetSender
name|cm
init|=
name|replacePatchSetFactory
operator|.
name|create
argument_list|(
name|ctx
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|ctx
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchSet
argument_list|(
name|patchSet
argument_list|,
name|patchSetInfo
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|changeMessage
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addReviewers
argument_list|(
name|oldReviewers
operator|.
name|byState
argument_list|(
name|REVIEWER
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addExtraCC
argument_list|(
name|oldReviewers
operator|.
name|byState
argument_list|(
name|CC
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setNotify
argument_list|(
name|notify
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setAccountsToNotify
argument_list|(
name|accountsToNotify
argument_list|)
expr_stmt|;
name|cm
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email for new patch set on change "
operator|+
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|fireRevisionCreated
condition|)
block|{
name|revisionCreated
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|ctx
operator|.
name|getAccount
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|notify
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|validate (RepoContext ctx)
specifier|private
name|void
name|validate
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|OrmException
block|{
if|if
condition|(
name|checkAddPatchSetPermission
operator|&&
operator|!
name|origCtl
operator|.
name|canAddPatchSet
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"cannot add patch set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|validatePolicy
operator|==
name|CommitValidators
operator|.
name|Policy
operator|.
name|NONE
condition|)
block|{
return|return;
block|}
name|String
name|refName
init|=
name|getPatchSetId
argument_list|()
operator|.
name|toRefName
argument_list|()
decl_stmt|;
try|try
init|(
name|CommitReceivedEvent
name|event
init|=
operator|new
name|CommitReceivedEvent
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|commit
operator|.
name|getId
argument_list|()
argument_list|,
name|refName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|refName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|"new"
argument_list|)
argument_list|,
name|origCtl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|origCtl
operator|.
name|getRefControl
argument_list|()
operator|.
name|getRefName
argument_list|()
argument_list|,
name|ctx
operator|.
name|getRevWalk
argument_list|()
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|commit
argument_list|,
name|ctx
operator|.
name|getIdentifiedUser
argument_list|()
argument_list|)
init|)
block|{
name|commitValidatorsFactory
operator|.
name|create
argument_list|(
name|validatePolicy
argument_list|,
name|origCtl
operator|.
name|getRefControl
argument_list|()
argument_list|,
operator|new
name|NoSshInfo
argument_list|()
argument_list|,
name|ctx
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|validate
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CommitValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getFullMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


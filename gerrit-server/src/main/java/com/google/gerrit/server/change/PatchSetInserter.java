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
name|MoreObjects
operator|.
name|firstNonNull
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
name|SetMultimap
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
name|ChangeHooks
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
name|client
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
name|ChangeUtil
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
name|git
operator|.
name|BanCommit
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
name|BatchUpdate
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
name|BatchUpdate
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
name|git
operator|.
name|BatchUpdate
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
name|git
operator|.
name|BatchUpdate
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
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GroupCollector
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
name|notedb
operator|.
name|ReviewerStateInternal
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
name|project
operator|.
name|InvalidChangeOperationException
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
name|ssh
operator|.
name|SshInfo
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
name|assistedinject
operator|.
name|Assisted
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
name|AssistedInject
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
name|notes
operator|.
name|NoteMap
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

begin_class
DECL|class|PatchSetInserter
specifier|public
class|class
name|PatchSetInserter
extends|extends
name|BatchUpdate
operator|.
name|Op
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
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (RefControl refControl, PatchSet.Id psId, RevCommit commit)
name|PatchSetInserter
name|create
parameter_list|(
name|RefControl
name|refControl
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
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
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
DECL|field|refControl
specifier|private
specifier|final
name|RefControl
name|refControl
decl_stmt|;
comment|// Fields exposed as setters.
DECL|field|sshInfo
specifier|private
name|SshInfo
name|sshInfo
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
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
DECL|field|draft
specifier|private
name|boolean
name|draft
decl_stmt|;
DECL|field|groups
specifier|private
name|Iterable
argument_list|<
name|String
argument_list|>
name|groups
decl_stmt|;
DECL|field|runHooks
specifier|private
name|boolean
name|runHooks
init|=
literal|true
decl_stmt|;
DECL|field|sendMail
specifier|private
name|boolean
name|sendMail
init|=
literal|true
decl_stmt|;
DECL|field|uploader
specifier|private
name|Account
operator|.
name|Id
name|uploader
decl_stmt|;
DECL|field|allowClosed
specifier|private
name|boolean
name|allowClosed
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
name|SetMultimap
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|oldReviewers
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|PatchSetInserter (ChangeHooks hooks, ReviewDb db, ApprovalsUtil approvalsUtil, ApprovalCopier approvalCopier, ChangeMessagesUtil cmUtil, PatchSetInfoFactory patchSetInfoFactory, CommitValidators.Factory commitValidatorsFactory, ReplacePatchSetSender.Factory replacePatchSetFactory, @Assisted RefControl refControl, @Assisted PatchSet.Id psId, @Assisted RevCommit commit)
specifier|public
name|PatchSetInserter
parameter_list|(
name|ChangeHooks
name|hooks
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
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
annotation|@
name|Assisted
name|RefControl
name|refControl
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
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
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
name|refControl
operator|=
name|refControl
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
DECL|method|setSshInfo (SshInfo sshInfo)
specifier|public
name|PatchSetInserter
name|setSshInfo
parameter_list|(
name|SshInfo
name|sshInfo
parameter_list|)
block|{
name|this
operator|.
name|sshInfo
operator|=
name|sshInfo
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
DECL|method|setGroups (Iterable<String> groups)
specifier|public
name|PatchSetInserter
name|setGroups
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
block|{
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
DECL|method|setRunHooks (boolean runHooks)
specifier|public
name|PatchSetInserter
name|setRunHooks
parameter_list|(
name|boolean
name|runHooks
parameter_list|)
block|{
name|this
operator|.
name|runHooks
operator|=
name|runHooks
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setSendMail (boolean sendMail)
specifier|public
name|PatchSetInserter
name|setSendMail
parameter_list|(
name|boolean
name|sendMail
parameter_list|)
block|{
name|this
operator|.
name|sendMail
operator|=
name|sendMail
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
DECL|method|setUploader (Account.Id uploader)
specifier|public
name|PatchSetInserter
name|setUploader
parameter_list|(
name|Account
operator|.
name|Id
name|uploader
parameter_list|)
block|{
name|this
operator|.
name|uploader
operator|=
name|uploader
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
name|ResourceConflictException
throws|,
name|IOException
block|{
name|init
argument_list|()
expr_stmt|;
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
name|void
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|OrmException
throws|,
name|InvalidChangeOperationException
throws|,
name|IOException
block|{
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
name|InvalidChangeOperationException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %s is closed"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|patchSet
operator|=
operator|new
name|PatchSet
argument_list|(
name|psId
argument_list|)
expr_stmt|;
name|patchSet
operator|.
name|setCreatedOn
argument_list|(
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|patchSet
operator|.
name|setUploader
argument_list|(
name|firstNonNull
argument_list|(
name|uploader
argument_list|,
name|ctl
operator|.
name|getChange
argument_list|()
operator|.
name|getOwner
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|patchSet
operator|.
name|setRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|commit
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|patchSet
operator|.
name|setDraft
argument_list|(
name|draft
argument_list|)
expr_stmt|;
if|if
condition|(
name|groups
operator|!=
literal|null
condition|)
block|{
name|patchSet
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|patchSet
operator|.
name|setGroups
argument_list|(
name|GroupCollector
operator|.
name|getCurrentGroups
argument_list|(
name|db
argument_list|,
name|change
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|patchSet
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|sendMail
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
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|ctl
operator|.
name|getId
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|patchSet
operator|.
name|getId
argument_list|()
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
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|db
operator|.
name|changes
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|change
argument_list|)
argument_list|)
expr_stmt|;
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
name|sendMail
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
name|getUser
argument_list|()
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
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addReviewers
argument_list|(
name|oldReviewers
operator|.
name|get
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
name|get
argument_list|(
name|CC
argument_list|)
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
name|runHooks
condition|)
block|{
name|hooks
operator|.
name|doPatchsetCreatedHook
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|init ()
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|sshInfo
operator|==
literal|null
condition|)
block|{
name|sshInfo
operator|=
operator|new
name|NoSshInfo
argument_list|()
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
name|ResourceConflictException
throws|,
name|IOException
block|{
name|CommitValidators
name|cv
init|=
name|commitValidatorsFactory
operator|.
name|create
argument_list|(
name|refControl
argument_list|,
name|sshInfo
argument_list|,
name|ctx
operator|.
name|getRepository
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|refName
init|=
name|getPatchSetId
argument_list|()
operator|.
name|toRefName
argument_list|()
decl_stmt|;
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
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|refControl
operator|.
name|getRefName
argument_list|()
argument_list|,
name|commit
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|validatePolicy
condition|)
block|{
case|case
name|RECEIVE_COMMITS
case|:
name|NoteMap
name|rejectCommits
init|=
name|BanCommit
operator|.
name|loadRejectCommitsMap
argument_list|(
name|ctx
operator|.
name|getRepository
argument_list|()
argument_list|,
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|)
decl_stmt|;
name|cv
operator|.
name|validateForReceiveCommits
argument_list|(
name|event
argument_list|,
name|rejectCommits
argument_list|)
expr_stmt|;
break|break;
case|case
name|GERRIT
case|:
name|cv
operator|.
name|validateForGerritCommits
argument_list|(
name|event
argument_list|)
expr_stmt|;
break|break;
case|case
name|NONE
case|:
break|break;
block|}
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


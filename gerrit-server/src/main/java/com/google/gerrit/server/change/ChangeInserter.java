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
name|reviewdb
operator|.
name|client
operator|.
name|Change
operator|.
name|INITIAL_PATCH_SET_ID
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
name|base
operator|.
name|MoreObjects
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
name|common
operator|.
name|FooterConstants
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
name|LabelTypes
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
name|Branch
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
name|WorkQueue
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
name|CreateChangeSender
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
name|util
operator|.
name|RequestScopePropagator
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|ChangeIdUtil
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
DECL|class|ChangeInserter
specifier|public
class|class
name|ChangeInserter
extends|extends
name|BatchUpdate
operator|.
name|InsertChangeOp
block|{
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (RefControl ctl, Change.Id cid, RevCommit rc)
name|ChangeInserter
name|create
parameter_list|(
name|RefControl
name|ctl
parameter_list|,
name|Change
operator|.
name|Id
name|cid
parameter_list|,
name|RevCommit
name|rc
parameter_list|)
function_decl|;
block|}
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
name|ChangeInserter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|createChangeSenderFactory
specifier|private
specifier|final
name|CreateChangeSender
operator|.
name|Factory
name|createChangeSenderFactory
decl_stmt|;
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|commitValidatorsFactory
specifier|private
specifier|final
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
decl_stmt|;
DECL|field|refControl
specifier|private
specifier|final
name|RefControl
name|refControl
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|patchSet
specifier|private
specifier|final
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|commit
specifier|private
specifier|final
name|RevCommit
name|commit
decl_stmt|;
comment|// Fields exposed as setters.
DECL|field|status
specifier|private
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|topic
specifier|private
name|String
name|topic
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
DECL|field|reviewers
specifier|private
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
decl_stmt|;
DECL|field|extraCC
specifier|private
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|extraCC
decl_stmt|;
DECL|field|approvals
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
decl_stmt|;
DECL|field|requestScopePropagator
specifier|private
name|RequestScopePropagator
name|requestScopePropagator
decl_stmt|;
DECL|field|runHooks
specifier|private
name|boolean
name|runHooks
decl_stmt|;
DECL|field|sendMail
specifier|private
name|boolean
name|sendMail
decl_stmt|;
DECL|field|updateRef
specifier|private
name|boolean
name|updateRef
decl_stmt|;
comment|// Fields set during the insertion process.
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|changeMessage
specifier|private
name|ChangeMessage
name|changeMessage
decl_stmt|;
DECL|field|patchSetInfo
specifier|private
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeInserter (PatchSetInfoFactory patchSetInfoFactory, ChangeHooks hooks, ApprovalsUtil approvalsUtil, ChangeMessagesUtil cmUtil, CreateChangeSender.Factory createChangeSenderFactory, WorkQueue workQueue, CommitValidators.Factory commitValidatorsFactory, @Assisted RefControl refControl, @Assisted Change.Id changeId, @Assisted RevCommit commit)
name|ChangeInserter
parameter_list|(
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|CreateChangeSender
operator|.
name|Factory
name|createChangeSenderFactory
parameter_list|,
name|WorkQueue
name|workQueue
parameter_list|,
name|CommitValidators
operator|.
name|Factory
name|commitValidatorsFactory
parameter_list|,
annotation|@
name|Assisted
name|RefControl
name|refControl
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Assisted
name|RevCommit
name|commit
parameter_list|)
block|{
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|createChangeSenderFactory
operator|=
name|createChangeSenderFactory
expr_stmt|;
name|this
operator|.
name|workQueue
operator|=
name|workQueue
expr_stmt|;
name|this
operator|.
name|commitValidatorsFactory
operator|=
name|commitValidatorsFactory
expr_stmt|;
name|this
operator|.
name|refControl
operator|=
name|refControl
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|commit
operator|=
name|commit
expr_stmt|;
name|this
operator|.
name|reviewers
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraCC
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
name|this
operator|.
name|approvals
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|runHooks
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|sendMail
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|updateRef
operator|=
literal|true
expr_stmt|;
name|user
operator|=
name|refControl
operator|.
name|getUser
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
expr_stmt|;
name|patchSet
operator|=
operator|new
name|PatchSet
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|INITIAL_PATCH_SET_ID
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
block|}
annotation|@
name|Override
DECL|method|createChange (Context ctx)
specifier|public
name|Change
name|createChange
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|IOException
block|{
name|change
operator|=
operator|new
name|Change
argument_list|(
name|getChangeKey
argument_list|(
name|commit
argument_list|)
argument_list|,
name|changeId
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|refControl
operator|.
name|getRefName
argument_list|()
argument_list|)
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setStatus
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|status
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
argument_list|)
expr_stmt|;
name|change
operator|.
name|setTopic
argument_list|(
name|topic
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
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
DECL|method|getChangeKey (RevCommit commit)
specifier|private
specifier|static
name|Change
operator|.
name|Key
name|getChangeKey
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|idList
init|=
name|commit
operator|.
name|getFooterLines
argument_list|(
name|FooterConstants
operator|.
name|CHANGE_ID
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|idList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|idList
operator|.
name|get
argument_list|(
name|idList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
name|ObjectId
name|id
init|=
name|ChangeIdUtil
operator|.
name|computeChangeId
argument_list|(
name|commit
operator|.
name|getTree
argument_list|()
argument_list|,
name|commit
argument_list|,
name|commit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|,
name|commit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|,
name|commit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|changeId
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|changeId
operator|.
name|append
argument_list|(
literal|"I"
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectId
operator|.
name|toString
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
operator|.
name|toString
argument_list|()
argument_list|)
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
literal|"getChange() only valid after creating change"
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
return|;
block|}
DECL|method|setTopic (String topic)
specifier|public
name|ChangeInserter
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|checkState
argument_list|(
name|change
operator|==
literal|null
argument_list|,
literal|"setTopic(String) only valid before creating change"
argument_list|)
expr_stmt|;
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setMessage (String message)
specifier|public
name|ChangeInserter
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
DECL|method|setValidatePolicy (CommitValidators.Policy validate)
specifier|public
name|ChangeInserter
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
DECL|method|setReviewers (Set<Account.Id> reviewers)
specifier|public
name|ChangeInserter
name|setReviewers
parameter_list|(
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
parameter_list|)
block|{
name|this
operator|.
name|reviewers
operator|=
name|reviewers
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setExtraCC (Set<Account.Id> extraCC)
specifier|public
name|ChangeInserter
name|setExtraCC
parameter_list|(
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|extraCC
parameter_list|)
block|{
name|this
operator|.
name|extraCC
operator|=
name|extraCC
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDraft (boolean draft)
specifier|public
name|ChangeInserter
name|setDraft
parameter_list|(
name|boolean
name|draft
parameter_list|)
block|{
name|checkState
argument_list|(
name|change
operator|==
literal|null
argument_list|,
literal|"setDraft(boolean) only valid before creating change"
argument_list|)
expr_stmt|;
return|return
name|setStatus
argument_list|(
name|draft
condition|?
name|Change
operator|.
name|Status
operator|.
name|DRAFT
else|:
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
return|;
block|}
DECL|method|setStatus (Change.Status status)
specifier|public
name|ChangeInserter
name|setStatus
parameter_list|(
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
name|checkState
argument_list|(
name|change
operator|==
literal|null
argument_list|,
literal|"setStatus(Change.Status) only valid before creating change"
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
if|if
condition|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
operator|.
name|equals
argument_list|(
name|status
argument_list|)
condition|)
block|{
name|patchSet
operator|.
name|setDraft
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|setGroups (Iterable<String> groups)
specifier|public
name|ChangeInserter
name|setGroups
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
block|{
name|patchSet
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setRunHooks (boolean runHooks)
specifier|public
name|ChangeInserter
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
name|ChangeInserter
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
DECL|method|setRequestScopePropagator (RequestScopePropagator r)
specifier|public
name|ChangeInserter
name|setRequestScopePropagator
parameter_list|(
name|RequestScopePropagator
name|r
parameter_list|)
block|{
name|this
operator|.
name|requestScopePropagator
operator|=
name|r
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getPatchSet ()
specifier|public
name|PatchSet
name|getPatchSet
parameter_list|()
block|{
return|return
name|patchSet
return|;
block|}
DECL|method|setApprovals (Map<String, Short> approvals)
specifier|public
name|ChangeInserter
name|setApprovals
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
parameter_list|)
block|{
name|this
operator|.
name|approvals
operator|=
name|approvals
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setUpdateRef (boolean updateRef)
specifier|public
name|ChangeInserter
name|setUpdateRef
parameter_list|(
name|boolean
name|updateRef
parameter_list|)
block|{
name|this
operator|.
name|updateRef
operator|=
name|updateRef
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getChangeMessage ()
specifier|public
name|ChangeMessage
name|getChangeMessage
parameter_list|()
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|checkState
argument_list|(
name|changeMessage
operator|!=
literal|null
argument_list|,
literal|"getChangeMessage() only valid after inserting change"
argument_list|)
expr_stmt|;
return|return
name|changeMessage
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
name|validate
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|updateRef
condition|)
block|{
return|return;
block|}
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
name|patchSet
operator|.
name|getRefName
argument_list|()
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
name|IOException
block|{
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
expr_stmt|;
comment|// Use defensive copy created by ChangeControl.
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
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setCurrentPatchSet
argument_list|(
name|patchSetInfo
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|setTopic
argument_list|(
name|change
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|patchSet
operator|.
name|getGroups
argument_list|()
operator|==
literal|null
condition|)
block|{
name|patchSet
operator|.
name|setGroups
argument_list|(
name|GroupCollector
operator|.
name|getDefaultGroups
argument_list|(
name|patchSet
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
name|ctx
operator|.
name|saveChange
argument_list|()
expr_stmt|;
comment|/* TODO: fixStatus is used here because the tests      * (byStatusClosed() in AbstractQueryChangesTest)      * insert changes that are already merged,      * and setStatus may not be used to set the Status to merged      *      * is it possible to make the tests use the merge code path,      * instead of setting the status directly?      */
name|update
operator|.
name|fixStatus
argument_list|(
name|change
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|LabelTypes
name|labelTypes
init|=
name|ctl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
decl_stmt|;
name|approvalsUtil
operator|.
name|addReviewers
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|labelTypes
argument_list|,
name|change
argument_list|,
name|patchSet
argument_list|,
name|patchSetInfo
argument_list|,
name|reviewers
argument_list|,
name|Collections
operator|.
expr|<
name|Account
operator|.
name|Id
operator|>
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|approvalsUtil
operator|.
name|addApprovals
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|labelTypes
argument_list|,
name|patchSet
argument_list|,
name|ctx
operator|.
name|getControl
argument_list|()
argument_list|,
name|approvals
argument_list|)
expr_stmt|;
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
name|change
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
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|patchSet
operator|.
name|getCreatedOn
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
name|Runnable
name|sender
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|CreateChangeSender
name|cm
init|=
name|createChangeSenderFactory
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
name|change
operator|.
name|getOwner
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
name|addReviewers
argument_list|(
name|reviewers
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addExtraCC
argument_list|(
name|extraCC
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
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email for new change "
operator|+
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"send-email newchange"
return|;
block|}
block|}
decl_stmt|;
if|if
condition|(
name|requestScopePropagator
operator|!=
literal|null
condition|)
block|{
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|submit
argument_list|(
name|requestScopePropagator
operator|.
name|wrap
argument_list|(
name|sender
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sender
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|runHooks
condition|)
block|{
name|ReviewDb
name|db
init|=
name|ctx
operator|.
name|getDb
argument_list|()
decl_stmt|;
name|hooks
operator|.
name|doPatchsetCreatedHook
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|db
argument_list|)
expr_stmt|;
if|if
condition|(
name|approvals
operator|!=
literal|null
operator|&&
operator|!
name|approvals
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|hooks
operator|.
name|doCommentAddedHook
argument_list|(
name|change
argument_list|,
name|user
operator|.
name|getAccount
argument_list|()
argument_list|,
name|patchSet
argument_list|,
literal|null
argument_list|,
name|approvals
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
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
name|IOException
throws|,
name|ResourceConflictException
block|{
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
name|CommitValidators
name|cv
init|=
name|commitValidatorsFactory
operator|.
name|create
argument_list|(
name|refControl
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
decl_stmt|;
name|String
name|refName
init|=
name|patchSet
operator|.
name|getId
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
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|commit
argument_list|,
name|user
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


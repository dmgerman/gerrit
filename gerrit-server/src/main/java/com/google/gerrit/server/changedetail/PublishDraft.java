begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|changedetail
package|;
end_package

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
name|mail
operator|.
name|MailUtil
operator|.
name|getRecipientsFromApprovals
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
name|mail
operator|.
name|MailUtil
operator|.
name|getRecipientsFromFooters
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
name|data
operator|.
name|ReviewResult
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
name|account
operator|.
name|AccountResolver
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
name|GitRepositoryManager
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
name|mail
operator|.
name|MailUtil
operator|.
name|MailRecipients
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
name|gwtorm
operator|.
name|server
operator|.
name|AtomicUpdate
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
name|lib
operator|.
name|Repository
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
name|FooterLine
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
name|revwalk
operator|.
name|RevWalk
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
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_class
DECL|class|PublishDraft
specifier|public
class|class
name|PublishDraft
implements|implements
name|Callable
argument_list|<
name|ReviewResult
argument_list|>
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
name|PublishDraft
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (PatchSet.Id patchSetId)
name|PublishDraft
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|)
function_decl|;
block|}
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|createChangeSenderFactory
specifier|private
specifier|final
name|CreateChangeSender
operator|.
name|Factory
name|createChangeSenderFactory
decl_stmt|;
DECL|field|replacePatchSetFactory
specifier|private
specifier|final
name|ReplacePatchSetSender
operator|.
name|Factory
name|replacePatchSetFactory
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
annotation|@
name|Inject
DECL|method|PublishDraft (final ChangeControl.Factory changeControlFactory, final ReviewDb db, final ChangeHooks hooks, final GitRepositoryManager repoManager, final PatchSetInfoFactory patchSetInfoFactory, final ApprovalsUtil approvalsUtil, final AccountResolver accountResolver, final CreateChangeSender.Factory createChangeSenderFactory, final ReplacePatchSetSender.Factory replacePatchSetFactory, @Assisted final PatchSet.Id patchSetId)
name|PublishDraft
parameter_list|(
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|ChangeHooks
name|hooks
parameter_list|,
specifier|final
name|GitRepositoryManager
name|repoManager
parameter_list|,
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
specifier|final
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
specifier|final
name|AccountResolver
name|accountResolver
parameter_list|,
specifier|final
name|CreateChangeSender
operator|.
name|Factory
name|createChangeSenderFactory
parameter_list|,
specifier|final
name|ReplacePatchSetSender
operator|.
name|Factory
name|replacePatchSetFactory
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
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
name|this
operator|.
name|createChangeSenderFactory
operator|=
name|createChangeSenderFactory
expr_stmt|;
name|this
operator|.
name|replacePatchSetFactory
operator|=
name|replacePatchSetFactory
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
name|ReviewResult
name|call
parameter_list|()
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|PatchSetInfoNotAvailableException
block|{
specifier|final
name|ReviewResult
name|result
init|=
operator|new
name|ReviewResult
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
name|result
operator|.
name|setChangeId
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
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
name|patchSetId
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
name|NoSuchChangeException
argument_list|(
name|changeId
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|patch
operator|.
name|isDraft
argument_list|()
condition|)
block|{
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ReviewResult
operator|.
name|Error
argument_list|(
name|ReviewResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|NOT_A_DRAFT
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
if|if
condition|(
operator|!
name|control
operator|.
name|canPublish
argument_list|(
name|db
argument_list|)
condition|)
block|{
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ReviewResult
operator|.
name|Error
argument_list|(
name|ReviewResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|PUBLISH_NOT_PERMITTED
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|PatchSet
name|updatedPatchSet
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|atomicUpdate
argument_list|(
name|patchSetId
argument_list|,
operator|new
name|AtomicUpdate
argument_list|<
name|PatchSet
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|PatchSet
name|update
parameter_list|(
name|PatchSet
name|patchset
parameter_list|)
block|{
name|patchset
operator|.
name|setDraft
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|patchset
return|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
name|Change
name|updatedChange
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|atomicUpdate
argument_list|(
name|changeId
argument_list|,
operator|new
name|AtomicUpdate
argument_list|<
name|Change
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Change
name|update
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|==
name|Change
operator|.
name|Status
operator|.
name|DRAFT
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
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|change
argument_list|)
expr_stmt|;
block|}
return|return
name|change
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|updatedPatchSet
operator|.
name|isDraft
argument_list|()
operator|||
name|updatedChange
operator|.
name|getStatus
argument_list|()
operator|==
name|Change
operator|.
name|Status
operator|.
name|NEW
condition|)
block|{
name|hooks
operator|.
name|doDraftPublishedHook
argument_list|(
name|updatedChange
argument_list|,
name|updatedPatchSet
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|sendNotifications
argument_list|(
name|control
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|,
operator|(
name|IdentifiedUser
operator|)
name|control
operator|.
name|getCurrentUser
argument_list|()
argument_list|,
name|updatedChange
argument_list|,
name|updatedPatchSet
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|sendNotifications (final boolean newChange, final IdentifiedUser currentUser, final Change updatedChange, final PatchSet updatedPatchSet)
specifier|private
name|void
name|sendNotifications
parameter_list|(
specifier|final
name|boolean
name|newChange
parameter_list|,
specifier|final
name|IdentifiedUser
name|currentUser
parameter_list|,
specifier|final
name|Change
name|updatedChange
parameter_list|,
specifier|final
name|PatchSet
name|updatedPatchSet
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|PatchSetInfoNotAvailableException
block|{
specifier|final
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|updatedChange
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|RevWalk
name|revWalk
init|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
decl_stmt|;
specifier|final
name|RevCommit
name|commit
decl_stmt|;
try|try
block|{
name|commit
operator|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|updatedPatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|revWalk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
specifier|final
name|PatchSetInfo
name|info
init|=
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|commit
argument_list|,
name|updatedPatchSet
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|FooterLine
argument_list|>
name|footerLines
init|=
name|commit
operator|.
name|getFooterLines
argument_list|()
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|currentUser
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|MailRecipients
name|recipients
init|=
name|getRecipientsFromFooters
argument_list|(
name|accountResolver
argument_list|,
name|updatedPatchSet
argument_list|,
name|footerLines
argument_list|)
decl_stmt|;
name|recipients
operator|.
name|remove
argument_list|(
name|me
argument_list|)
expr_stmt|;
if|if
condition|(
name|newChange
condition|)
block|{
name|approvalsUtil
operator|.
name|addReviewers
argument_list|(
name|db
argument_list|,
name|updatedChange
argument_list|,
name|updatedPatchSet
argument_list|,
name|info
argument_list|,
name|recipients
operator|.
name|getReviewers
argument_list|()
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
try|try
block|{
name|CreateChangeSender
name|cm
init|=
name|createChangeSenderFactory
operator|.
name|create
argument_list|(
name|updatedChange
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|me
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchSet
argument_list|(
name|updatedPatchSet
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addReviewers
argument_list|(
name|recipients
operator|.
name|getReviewers
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addExtraCC
argument_list|(
name|recipients
operator|.
name|getCcOnly
argument_list|()
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
name|updatedChange
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
specifier|final
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|patchSetApprovals
init|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|updatedChange
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
specifier|final
name|MailRecipients
name|oldRecipients
init|=
name|getRecipientsFromApprovals
argument_list|(
name|patchSetApprovals
argument_list|)
decl_stmt|;
name|approvalsUtil
operator|.
name|addReviewers
argument_list|(
name|db
argument_list|,
name|updatedChange
argument_list|,
name|updatedPatchSet
argument_list|,
name|info
argument_list|,
name|recipients
operator|.
name|getReviewers
argument_list|()
argument_list|,
name|oldRecipients
operator|.
name|getAll
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ChangeMessage
name|msg
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|updatedChange
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
name|me
argument_list|,
name|updatedPatchSet
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
name|updatedPatchSet
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setMessage
argument_list|(
literal|"Uploaded patch set "
operator|+
name|updatedPatchSet
operator|.
name|getPatchSetId
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
try|try
block|{
name|ReplacePatchSetSender
name|cm
init|=
name|replacePatchSetFactory
operator|.
name|create
argument_list|(
name|updatedChange
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|me
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchSet
argument_list|(
name|updatedPatchSet
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addReviewers
argument_list|(
name|recipients
operator|.
name|getReviewers
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|addExtraCC
argument_list|(
name|recipients
operator|.
name|getCcOnly
argument_list|()
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
literal|"Cannot send email for new patch set "
operator|+
name|updatedPatchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


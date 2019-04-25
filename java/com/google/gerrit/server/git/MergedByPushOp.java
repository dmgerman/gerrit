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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|LabelId
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
name|config
operator|.
name|SendEmailExecutor
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
name|ChangeMerged
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
name|MergedSender
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
name|Provider
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
name|concurrent
operator|.
name|ExecutorService
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
name|Future
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
name|Constants
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

begin_class
DECL|class|MergedByPushOp
specifier|public
class|class
name|MergedByPushOp
implements|implements
name|BatchUpdateOp
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ( RequestScopePropagator requestScopePropagator, PatchSet.Id psId, String refName)
name|MergedByPushOp
name|create
parameter_list|(
name|RequestScopePropagator
name|requestScopePropagator
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|String
name|refName
parameter_list|)
function_decl|;
block|}
DECL|field|requestScopePropagator
specifier|private
specifier|final
name|RequestScopePropagator
name|requestScopePropagator
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|mergedSenderFactory
specifier|private
specifier|final
name|MergedSender
operator|.
name|Factory
name|mergedSenderFactory
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|sendEmailExecutor
specifier|private
specifier|final
name|ExecutorService
name|sendEmailExecutor
decl_stmt|;
DECL|field|changeMerged
specifier|private
specifier|final
name|ChangeMerged
name|changeMerged
decl_stmt|;
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|refName
specifier|private
specifier|final
name|String
name|refName
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|correctBranch
specifier|private
name|boolean
name|correctBranch
decl_stmt|;
DECL|field|patchSetProvider
specifier|private
name|Provider
argument_list|<
name|PatchSet
argument_list|>
name|patchSetProvider
decl_stmt|;
DECL|field|patchSet
specifier|private
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|info
specifier|private
name|PatchSetInfo
name|info
decl_stmt|;
annotation|@
name|Inject
DECL|method|MergedByPushOp ( PatchSetInfoFactory patchSetInfoFactory, ChangeMessagesUtil cmUtil, MergedSender.Factory mergedSenderFactory, PatchSetUtil psUtil, @SendEmailExecutor ExecutorService sendEmailExecutor, ChangeMerged changeMerged, @Assisted RequestScopePropagator requestScopePropagator, @Assisted PatchSet.Id psId, @Assisted String refName)
name|MergedByPushOp
parameter_list|(
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|MergedSender
operator|.
name|Factory
name|mergedSenderFactory
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
annotation|@
name|SendEmailExecutor
name|ExecutorService
name|sendEmailExecutor
parameter_list|,
name|ChangeMerged
name|changeMerged
parameter_list|,
annotation|@
name|Assisted
name|RequestScopePropagator
name|requestScopePropagator
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
name|String
name|refName
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
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|mergedSenderFactory
operator|=
name|mergedSenderFactory
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|sendEmailExecutor
operator|=
name|sendEmailExecutor
expr_stmt|;
name|this
operator|.
name|changeMerged
operator|=
name|changeMerged
expr_stmt|;
name|this
operator|.
name|requestScopePropagator
operator|=
name|requestScopePropagator
expr_stmt|;
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
block|}
DECL|method|getMergedIntoRef ()
specifier|public
name|String
name|getMergedIntoRef
parameter_list|()
block|{
return|return
name|refName
return|;
block|}
DECL|method|setPatchSetProvider (Provider<PatchSet> patchSetProvider)
specifier|public
name|MergedByPushOp
name|setPatchSetProvider
parameter_list|(
name|Provider
argument_list|<
name|PatchSet
argument_list|>
name|patchSetProvider
parameter_list|)
block|{
name|this
operator|.
name|patchSetProvider
operator|=
name|requireNonNull
argument_list|(
name|patchSetProvider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
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
name|IOException
block|{
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|correctBranch
operator|=
name|refName
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|correctBranch
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|patchSetProvider
operator|!=
literal|null
condition|)
block|{
comment|// Caller might have also arranged for construction of a new patch set
comment|// that is not present in the old notes so we can't use PatchSetUtil.
name|patchSet
operator|=
name|patchSetProvider
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|patchSet
operator|=
name|requireNonNull
argument_list|(
name|psUtil
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|,
name|psId
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"patch set %s not found"
argument_list|,
name|psId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|info
operator|=
name|getPatchSetInfo
argument_list|(
name|ctx
argument_list|)
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
name|change
operator|.
name|isMerged
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|change
operator|.
name|setCurrentPatchSet
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|)
expr_stmt|;
comment|// we cannot reconstruct the submit records for when this change was
comment|// submitted, this is why we must fix the status
name|update
operator|.
name|fixStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|)
expr_stmt|;
name|update
operator|.
name|setCurrentPatchSet
argument_list|()
expr_stmt|;
name|StringBuilder
name|msgBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msgBuf
operator|.
name|append
argument_list|(
literal|"Change has been successfully pushed"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|refName
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
condition|)
block|{
name|msgBuf
operator|.
name|append
argument_list|(
literal|" into "
argument_list|)
expr_stmt|;
if|if
condition|(
name|refName
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
name|msgBuf
operator|.
name|append
argument_list|(
literal|"branch "
argument_list|)
expr_stmt|;
name|msgBuf
operator|.
name|append
argument_list|(
name|Repository
operator|.
name|shortenRefName
argument_list|(
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msgBuf
operator|.
name|append
argument_list|(
name|refName
argument_list|)
expr_stmt|;
block|}
block|}
name|msgBuf
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|ChangeMessage
name|msg
init|=
name|ChangeMessagesUtil
operator|.
name|newMessage
argument_list|(
name|psId
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
name|msgBuf
operator|.
name|toString
argument_list|()
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_MERGED
argument_list|)
decl_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|update
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|PatchSetApproval
name|submitter
init|=
name|ApprovalsUtil
operator|.
name|newApproval
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
argument_list|,
name|LabelId
operator|.
name|legacySubmit
argument_list|()
argument_list|,
literal|1
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
name|submitter
operator|.
name|getLabel
argument_list|()
argument_list|,
name|submitter
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
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
block|{
if|if
condition|(
operator|!
name|correctBranch
condition|)
block|{
return|return;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// Runnable already handles errors
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|sendEmailExecutor
operator|.
name|submit
argument_list|(
name|requestScopePropagator
operator|.
name|wrap
argument_list|(
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
name|MergedSender
name|cm
init|=
name|mergedSenderFactory
operator|.
name|create
argument_list|(
name|ctx
operator|.
name|getProject
argument_list|()
argument_list|,
name|psId
operator|.
name|changeId
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
name|info
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot send email for submitted patch set %s"
argument_list|,
name|psId
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
literal|"send-email merged"
return|;
block|}
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|changeMerged
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
name|patchSet
operator|.
name|commitId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getPatchSetInfo (ChangeContext ctx)
specifier|private
name|PatchSetInfo
name|getPatchSetInfo
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|IOException
block|{
name|RevWalk
name|rw
init|=
name|ctx
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|requireNonNull
argument_list|(
name|patchSet
argument_list|)
operator|.
name|commitId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|,
name|psId
argument_list|)
return|;
block|}
block|}
end_class

end_unit


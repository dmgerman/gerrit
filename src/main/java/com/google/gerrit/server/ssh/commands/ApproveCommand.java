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
DECL|package|com.google.gerrit.server.ssh.commands
package|package
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
name|commands
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
name|pgm
operator|.
name|CmdLineParser
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
name|mail
operator|.
name|CommentSender
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
name|EmailException
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
name|CommentSender
operator|.
name|Factory
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
name|ssh
operator|.
name|BaseCommand
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
name|gwtorm
operator|.
name|client
operator|.
name|Transaction
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|List
import|;
end_import

begin_class
DECL|class|ApproveCommand
specifier|public
class|class
name|ApproveCommand
extends|extends
name|BaseCommand
block|{
annotation|@
name|Override
DECL|method|newCmdLineParser ()
specifier|protected
specifier|final
name|CmdLineParser
name|newCmdLineParser
parameter_list|()
block|{
specifier|final
name|CmdLineParser
name|parser
init|=
name|super
operator|.
name|newCmdLineParser
argument_list|()
decl_stmt|;
for|for
control|(
name|CmdOption
name|c
range|:
name|optionList
control|)
block|{
name|parser
operator|.
name|addOption
argument_list|(
name|c
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|parser
return|;
block|}
DECL|field|CMD_ERR
specifier|private
specifier|static
specifier|final
name|int
name|CMD_ERR
init|=
literal|3
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"CHANGE,PATCHSET"
argument_list|,
name|usage
operator|=
literal|"Patch set to approve"
argument_list|)
DECL|field|patchSetId
specifier|private
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--message"
argument_list|,
name|aliases
operator|=
literal|"-m"
argument_list|,
name|usage
operator|=
literal|"Message to put on change/patchset"
argument_list|,
name|metaVar
operator|=
literal|"MESSAGE"
argument_list|)
DECL|field|changeComment
specifier|private
name|String
name|changeComment
decl_stmt|;
annotation|@
name|Inject
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|field|currentUser
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|field|commentSenderFactory
specifier|private
name|Factory
name|commentSenderFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|patchSetInfoFactory
specifier|private
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|approvalTypes
specifier|private
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
annotation|@
name|Inject
DECL|field|changeControlFactory
specifier|private
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|functionStateFactory
specifier|private
name|FunctionState
operator|.
name|Factory
name|functionStateFactory
decl_stmt|;
DECL|field|optionList
specifier|private
name|List
argument_list|<
name|CmdOption
argument_list|>
name|optionList
decl_stmt|;
annotation|@
name|Override
DECL|method|start ()
specifier|public
specifier|final
name|void
name|start
parameter_list|()
block|{
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|getApprovalNames
argument_list|()
expr_stmt|;
name|parseCommandLine
argument_list|()
expr_stmt|;
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
specifier|final
name|PatchSet
name|ps
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
name|ps
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
name|CMD_ERR
argument_list|,
literal|"Invalid patchset id"
argument_list|)
throw|;
block|}
specifier|final
name|Change
operator|.
name|Id
name|cid
init|=
name|ps
operator|.
name|getId
argument_list|()
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
name|cid
argument_list|)
decl_stmt|;
specifier|final
name|Change
name|c
init|=
name|control
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getStatus
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
name|CMD_ERR
argument_list|,
literal|"Change is closed."
argument_list|)
throw|;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Patch Set "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|patchSetId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
for|for
control|(
name|CmdOption
name|co
range|:
name|optionList
control|)
block|{
name|ApprovalCategory
operator|.
name|Id
name|category
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
name|co
operator|.
name|approvalKey
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSetApproval
operator|.
name|Key
name|psaKey
init|=
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|patchSetId
argument_list|,
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|category
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|psa
init|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|psaKey
argument_list|)
decl_stmt|;
name|Short
name|score
init|=
name|co
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|score
operator|!=
literal|null
condition|)
block|{
name|addApproval
argument_list|(
name|psaKey
argument_list|,
name|score
argument_list|,
name|c
argument_list|,
name|co
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|psa
operator|==
literal|null
condition|)
block|{
name|score
operator|=
literal|0
expr_stmt|;
name|addApproval
argument_list|(
name|psaKey
argument_list|,
name|score
argument_list|,
name|c
argument_list|,
name|co
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|score
operator|=
name|psa
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|message
init|=
name|db
operator|.
name|approvalCategoryValues
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|(
name|category
argument_list|,
name|score
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" "
operator|+
name|message
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|deleteCharAt
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|changeComment
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|changeComment
argument_list|)
expr_stmt|;
block|}
name|String
name|uuid
init|=
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|ChangeMessage
name|cm
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|cid
argument_list|,
name|uuid
argument_list|)
argument_list|,
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setMessage
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|cm
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|c
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
name|c
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|sendMail
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|cm
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMail (final Change c, final PatchSet.Id psid, final ChangeMessage message)
specifier|private
name|void
name|sendMail
parameter_list|(
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|psid
parameter_list|,
specifier|final
name|ChangeMessage
name|message
parameter_list|)
throws|throws
name|PatchSetInfoNotAvailableException
throws|,
name|EmailException
throws|,
name|OrmException
block|{
name|PatchSet
name|ps
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|psid
argument_list|)
decl_stmt|;
specifier|final
name|CommentSender
name|cm
decl_stmt|;
name|cm
operator|=
name|commentSenderFactory
operator|.
name|create
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchSet
argument_list|(
name|ps
argument_list|,
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|psid
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setReviewDb
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|cm
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
DECL|method|addApproval (final PatchSetApproval.Key psaKey, final Short score, final Change c, final CmdOption co, final Transaction txn)
specifier|private
name|void
name|addApproval
parameter_list|(
specifier|final
name|PatchSetApproval
operator|.
name|Key
name|psaKey
parameter_list|,
specifier|final
name|Short
name|score
parameter_list|,
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|CmdOption
name|co
parameter_list|,
specifier|final
name|Transaction
name|txn
parameter_list|)
throws|throws
name|OrmException
throws|,
name|UnloggedFailure
block|{
name|PatchSetApproval
name|psa
init|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|get
argument_list|(
name|psaKey
argument_list|)
decl_stmt|;
name|boolean
name|insert
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|psa
operator|==
literal|null
condition|)
block|{
name|insert
operator|=
literal|true
expr_stmt|;
name|psa
operator|=
operator|new
name|PatchSetApproval
argument_list|(
name|psaKey
argument_list|,
name|score
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|final
name|FunctionState
name|fs
init|=
name|functionStateFactory
operator|.
name|create
argument_list|(
name|c
argument_list|,
name|patchSetId
argument_list|,
name|approvals
argument_list|)
decl_stmt|;
name|psa
operator|.
name|setValue
argument_list|(
name|score
argument_list|)
expr_stmt|;
name|fs
operator|.
name|normalize
argument_list|(
name|approvalTypes
operator|.
name|getApprovalType
argument_list|(
name|psa
operator|.
name|getCategoryId
argument_list|()
argument_list|)
argument_list|,
name|psa
argument_list|)
expr_stmt|;
if|if
condition|(
name|score
operator|!=
name|psa
operator|.
name|getValue
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
name|CMD_ERR
argument_list|,
name|co
operator|.
name|name
argument_list|()
operator|+
literal|"="
operator|+
name|co
operator|.
name|value
argument_list|()
operator|+
literal|" not permitted"
argument_list|)
throw|;
block|}
name|psa
operator|.
name|setGranted
argument_list|()
expr_stmt|;
if|if
condition|(
name|insert
condition|)
block|{
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|psa
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|psa
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getApprovalNames ()
specifier|private
name|void
name|getApprovalNames
parameter_list|()
block|{
name|optionList
operator|=
operator|new
name|ArrayList
argument_list|<
name|CmdOption
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|ApprovalType
name|type
range|:
name|approvalTypes
operator|.
name|getApprovalTypes
argument_list|()
control|)
block|{
name|String
name|usage
init|=
literal|""
decl_stmt|;
specifier|final
name|ApprovalCategory
name|category
init|=
name|type
operator|.
name|getCategory
argument_list|()
decl_stmt|;
name|usage
operator|=
literal|"Score for "
operator|+
name|category
operator|.
name|getName
argument_list|()
operator|+
literal|"\n"
expr_stmt|;
for|for
control|(
name|ApprovalCategoryValue
name|v
range|:
name|type
operator|.
name|getValues
argument_list|()
control|)
block|{
name|usage
operator|+=
name|String
operator|.
name|format
argument_list|(
literal|"%3s"
argument_list|,
name|CmdOption
operator|.
name|format
argument_list|(
name|v
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
operator|+
literal|": "
operator|+
name|v
operator|.
name|getName
argument_list|()
operator|+
literal|"\n"
expr_stmt|;
block|}
name|optionList
operator|.
name|add
argument_list|(
operator|new
name|CmdOption
argument_list|(
literal|"--"
operator|+
name|category
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|replace
argument_list|(
literal|' '
argument_list|,
literal|'-'
argument_list|)
argument_list|,
name|usage
argument_list|,
name|category
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|type
operator|.
name|getMin
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|type
operator|.
name|getMax
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|category
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


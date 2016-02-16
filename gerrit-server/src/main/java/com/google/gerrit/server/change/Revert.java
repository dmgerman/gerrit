begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|TimeUtil
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
name|RevertInput
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
name|common
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|RestApiException
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
name|RestModifyView
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
name|webui
operator|.
name|UiAction
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
name|Change
operator|.
name|Status
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
name|CurrentUser
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
name|GerritPersonIdent
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
name|Sequences
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
name|git
operator|.
name|UpdateException
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
name|RevertedSender
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
name|Singleton
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
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|errors
operator|.
name|MissingObjectException
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|CommitBuilder
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
name|ObjectInserter
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
name|PersonIdent
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
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Revert
specifier|public
class|class
name|Revert
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|RevertInput
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
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
name|Revert
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|changeInserterFactory
specifier|private
specifier|final
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|changeUpdateFactory
specifier|private
specifier|final
name|ChangeUpdate
operator|.
name|Factory
name|changeUpdateFactory
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|seq
specifier|private
specifier|final
name|Sequences
name|seq
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|revertedSenderFactory
specifier|private
specifier|final
name|RevertedSender
operator|.
name|Factory
name|revertedSenderFactory
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|json
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|Revert (Provider<ReviewDb> db, GitRepositoryManager repoManager, ChangeInserter.Factory changeInserterFactory, ChangeMessagesUtil cmUtil, ChangeUpdate.Factory changeUpdateFactory, BatchUpdate.Factory updateFactory, Sequences seq, PatchSetUtil psUtil, RevertedSender.Factory revertedSenderFactory, ChangeJson.Factory json, @GerritPersonIdent PersonIdent myIdent)
name|Revert
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|ChangeUpdate
operator|.
name|Factory
name|changeUpdateFactory
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|Sequences
name|seq
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|RevertedSender
operator|.
name|Factory
name|revertedSenderFactory
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|myIdent
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|changeInserterFactory
operator|=
name|changeInserterFactory
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|changeUpdateFactory
operator|=
name|changeUpdateFactory
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|seq
operator|=
name|seq
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|revertedSenderFactory
operator|=
name|revertedSenderFactory
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource req, RevertInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|ChangeResource
name|req
parameter_list|,
name|RevertInput
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
throws|,
name|RestApiException
throws|,
name|UpdateException
throws|,
name|NoSuchChangeException
block|{
name|RefControl
name|refControl
init|=
name|req
operator|.
name|getControl
argument_list|()
operator|.
name|getRefControl
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|req
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|refControl
operator|.
name|canUpload
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"revert not permitted"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|!=
name|Status
operator|.
name|MERGED
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
name|Change
operator|.
name|Id
name|revertedChangeId
decl_stmt|;
try|try
block|{
name|revertedChangeId
operator|=
name|revert
argument_list|(
name|req
operator|.
name|getControl
argument_list|()
argument_list|,
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|message
argument_list|)
argument_list|,
operator|new
name|PersonIdent
argument_list|(
name|myIdent
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|json
operator|.
name|create
argument_list|(
name|ChangeJson
operator|.
name|NO_OPTIONS
argument_list|)
operator|.
name|format
argument_list|(
name|req
operator|.
name|getProject
argument_list|()
argument_list|,
name|revertedChangeId
argument_list|)
return|;
block|}
DECL|method|revert (ChangeControl ctl, PatchSet.Id patchSetId, String message, PersonIdent myIdent)
specifier|private
name|Change
operator|.
name|Id
name|revert
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|message
parameter_list|,
name|PersonIdent
name|myIdent
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
throws|,
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
throws|,
name|RestApiException
throws|,
name|UpdateException
block|{
name|Change
operator|.
name|Id
name|changeIdToRevert
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|PatchSet
name|patch
init|=
name|psUtil
operator|.
name|get
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|ctl
operator|.
name|getNotes
argument_list|()
argument_list|,
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
name|changeIdToRevert
argument_list|)
throw|;
block|}
name|Change
name|changeToRevert
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changeIdToRevert
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|CurrentUser
name|user
init|=
name|ctl
operator|.
name|getUser
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|RevCommit
name|commitToRevert
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|patch
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|PersonIdent
name|authorIdent
init|=
name|user
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|newCommitterIdent
argument_list|(
name|myIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|myIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|commitToRevert
operator|.
name|getParentCount
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot revert initial commit"
argument_list|)
throw|;
block|}
name|RevCommit
name|parentToCommitToRevert
init|=
name|commitToRevert
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|revWalk
operator|.
name|parseHeaders
argument_list|(
name|parentToCommitToRevert
argument_list|)
expr_stmt|;
name|CommitBuilder
name|revertCommitBuilder
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|revertCommitBuilder
operator|.
name|addParentId
argument_list|(
name|commitToRevert
argument_list|)
expr_stmt|;
name|revertCommitBuilder
operator|.
name|setTreeId
argument_list|(
name|parentToCommitToRevert
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|revertCommitBuilder
operator|.
name|setAuthor
argument_list|(
name|authorIdent
argument_list|)
expr_stmt|;
name|revertCommitBuilder
operator|.
name|setCommitter
argument_list|(
name|authorIdent
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
name|message
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|ChangeMessages
operator|.
name|get
argument_list|()
operator|.
name|revertChangeDefaultMessage
argument_list|,
name|changeToRevert
operator|.
name|getSubject
argument_list|()
argument_list|,
name|patch
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ObjectId
name|computedChangeId
init|=
name|ChangeIdUtil
operator|.
name|computeChangeId
argument_list|(
name|parentToCommitToRevert
operator|.
name|getTree
argument_list|()
argument_list|,
name|commitToRevert
argument_list|,
name|authorIdent
argument_list|,
name|myIdent
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|revertCommitBuilder
operator|.
name|setMessage
argument_list|(
name|ChangeIdUtil
operator|.
name|insertId
argument_list|(
name|message
argument_list|,
name|computedChangeId
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|RevCommit
name|revertCommit
decl_stmt|;
name|ChangeInserter
name|ins
decl_stmt|;
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|seq
operator|.
name|nextChangeId
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|ObjectInserter
name|oi
init|=
name|git
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|ObjectId
name|id
init|=
name|oi
operator|.
name|insert
argument_list|(
name|revertCommitBuilder
argument_list|)
decl_stmt|;
name|oi
operator|.
name|flush
argument_list|()
expr_stmt|;
name|revertCommit
operator|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|ins
operator|=
name|changeInserterFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|,
name|revertCommit
argument_list|,
name|ctl
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setValidatePolicy
argument_list|(
name|CommitValidators
operator|.
name|Policy
operator|.
name|GERRIT
argument_list|)
operator|.
name|setTopic
argument_list|(
name|changeToRevert
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeMessage
name|changeMessage
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|patchSetId
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|patchSetId
argument_list|)
decl_stmt|;
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
literal|"Patch Set "
argument_list|)
operator|.
name|append
argument_list|(
name|patchSetId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": Reverted"
argument_list|)
expr_stmt|;
name|msgBuf
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|msgBuf
operator|.
name|append
argument_list|(
literal|"This patchset was reverted in change: "
argument_list|)
operator|.
name|append
argument_list|(
literal|"I"
argument_list|)
operator|.
name|append
argument_list|(
name|computedChangeId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|changeMessage
operator|.
name|setMessage
argument_list|(
name|msgBuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|changeUpdateFactory
operator|.
name|create
argument_list|(
name|ctl
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|update
argument_list|,
name|changeMessage
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|ins
operator|.
name|setMessage
argument_list|(
literal|"Uploaded patch set 1."
argument_list|)
expr_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|project
argument_list|,
name|user
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|setRepository
argument_list|(
name|git
argument_list|,
name|revWalk
argument_list|,
name|oi
argument_list|)
expr_stmt|;
name|bu
operator|.
name|insertChange
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
try|try
block|{
name|RevertedSender
name|cm
init|=
name|revertedSenderFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|ins
operator|.
name|getChangeMessage
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
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email for revert change "
operator|+
name|changeId
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
return|return
name|changeId
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|changeIdToRevert
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource resource)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|resource
parameter_list|)
block|{
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Revert"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Revert the change"
argument_list|)
operator|.
name|setVisible
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|MERGED
operator|&&
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|getRefControl
argument_list|()
operator|.
name|canUpload
argument_list|()
argument_list|)
return|;
block|}
DECL|method|status (Change change)
specifier|private
specifier|static
name|String
name|status
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|!=
literal|null
condition|?
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
else|:
literal|"deleted"
return|;
block|}
block|}
end_class

end_unit


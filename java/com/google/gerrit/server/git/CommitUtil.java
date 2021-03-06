begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|common
operator|.
name|Nullable
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
name|entities
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
name|entities
operator|.
name|Account
operator|.
name|Id
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
name|entities
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
name|entities
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
name|entities
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
name|CommitInfo
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
name|CommonConverters
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
name|change
operator|.
name|ChangeInserter
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
name|change
operator|.
name|ChangeMessages
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
name|change
operator|.
name|NotifyResolver
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
name|ChangeReverted
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
name|ChangeNotes
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
name|notedb
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
name|update
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
name|UpdateException
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
name|sql
operator|.
name|Timestamp
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
name|HashSet
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
name|ConfigInvalidException
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
name|ObjectReader
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

begin_comment
comment|/** Static utilities for working with {@link RevCommit}s. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|CommitUtil
specifier|public
class|class
name|CommitUtil
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
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|seq
specifier|private
specifier|final
name|Sequences
name|seq
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|changeInserterFactory
specifier|private
specifier|final
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
decl_stmt|;
DECL|field|notifyResolver
specifier|private
specifier|final
name|NotifyResolver
name|notifyResolver
decl_stmt|;
DECL|field|revertedSenderFactory
specifier|private
specifier|final
name|RevertedSender
operator|.
name|Factory
name|revertedSenderFactory
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|changeReverted
specifier|private
specifier|final
name|ChangeReverted
name|changeReverted
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommitUtil ( GitRepositoryManager repoManager, @GerritPersonIdent Provider<PersonIdent> serverIdent, Sequences seq, ApprovalsUtil approvalsUtil, ChangeInserter.Factory changeInserterFactory, NotifyResolver notifyResolver, RevertedSender.Factory revertedSenderFactory, ChangeMessagesUtil cmUtil, ChangeReverted changeReverted, BatchUpdate.Factory updateFactory)
name|CommitUtil
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|,
name|Sequences
name|seq
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
parameter_list|,
name|NotifyResolver
name|notifyResolver
parameter_list|,
name|RevertedSender
operator|.
name|Factory
name|revertedSenderFactory
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|ChangeReverted
name|changeReverted
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|seq
operator|=
name|seq
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|changeInserterFactory
operator|=
name|changeInserterFactory
expr_stmt|;
name|this
operator|.
name|notifyResolver
operator|=
name|notifyResolver
expr_stmt|;
name|this
operator|.
name|revertedSenderFactory
operator|=
name|revertedSenderFactory
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|changeReverted
operator|=
name|changeReverted
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
block|}
DECL|method|toCommitInfo (RevCommit commit)
specifier|public
specifier|static
name|CommitInfo
name|toCommitInfo
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toCommitInfo
argument_list|(
name|commit
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|toCommitInfo (RevCommit commit, @Nullable RevWalk walk)
specifier|public
specifier|static
name|CommitInfo
name|toCommitInfo
parameter_list|(
name|RevCommit
name|commit
parameter_list|,
annotation|@
name|Nullable
name|RevWalk
name|walk
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitInfo
name|info
init|=
operator|new
name|CommitInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|commit
operator|=
name|commit
operator|.
name|getName
argument_list|()
expr_stmt|;
name|info
operator|.
name|author
operator|=
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|commit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|committer
operator|=
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|commit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|subject
operator|=
name|commit
operator|.
name|getShortMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|message
operator|=
name|commit
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|parents
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|commit
operator|.
name|getParentCount
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|commit
operator|.
name|getParentCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|RevCommit
name|p
init|=
name|walk
operator|==
literal|null
condition|?
name|commit
operator|.
name|getParent
argument_list|(
name|i
argument_list|)
else|:
name|walk
operator|.
name|parseCommit
argument_list|(
name|commit
operator|.
name|getParent
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|CommitInfo
name|parentInfo
init|=
operator|new
name|CommitInfo
argument_list|()
decl_stmt|;
name|parentInfo
operator|.
name|commit
operator|=
name|p
operator|.
name|getName
argument_list|()
expr_stmt|;
name|parentInfo
operator|.
name|subject
operator|=
name|p
operator|.
name|getShortMessage
argument_list|()
expr_stmt|;
name|info
operator|.
name|parents
operator|.
name|add
argument_list|(
name|parentInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
comment|/**    * Allows creating a revert change.    *    * @param notes ChangeNotes of the change being reverted.    * @param user Current User performing the revert.    * @param input the RevertInput entity for conducting the revert.    * @param timestamp timestamp for the created change.    * @return ObjectId that represents the newly created commit.    */
DECL|method|createRevertChange ( ChangeNotes notes, CurrentUser user, RevertInput input, Timestamp timestamp)
specifier|public
name|Change
operator|.
name|Id
name|createRevertChange
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|RevertInput
name|input
parameter_list|,
name|Timestamp
name|timestamp
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|String
name|message
init|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|message
argument_list|)
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
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|git
operator|.
name|newObjectInserter
argument_list|()
init|;
name|ObjectReader
name|reader
operator|=
name|oi
operator|.
name|newReader
argument_list|()
init|;
name|RevWalk
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
name|ObjectId
name|generatedChangeId
init|=
name|Change
operator|.
name|generateChangeId
argument_list|()
decl_stmt|;
name|ObjectId
name|revCommit
init|=
name|createRevertCommit
argument_list|(
name|message
argument_list|,
name|notes
argument_list|,
name|user
argument_list|,
name|timestamp
argument_list|,
name|oi
argument_list|,
name|revWalk
argument_list|,
name|generatedChangeId
argument_list|)
decl_stmt|;
return|return
name|createRevertChangeFromCommit
argument_list|(
name|revCommit
argument_list|,
name|input
argument_list|,
name|notes
argument_list|,
name|user
argument_list|,
name|generatedChangeId
argument_list|,
name|timestamp
argument_list|,
name|oi
argument_list|,
name|revWalk
argument_list|,
name|git
argument_list|)
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
name|ResourceNotFoundException
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Wrapper function for creating a revert Commit.    *    * @param message Commit message for the revert commit.    * @param notes ChangeNotes of the change being reverted.    * @param user Current User performing the revert.    * @param ts Timestamp of creation for the commit.    * @return ObjectId that represents the newly created commit.    */
DECL|method|createRevertCommit ( String message, ChangeNotes notes, CurrentUser user, Timestamp ts)
specifier|public
name|ObjectId
name|createRevertCommit
parameter_list|(
name|String
name|message
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Timestamp
name|ts
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|git
operator|.
name|newObjectInserter
argument_list|()
init|;
name|ObjectReader
name|reader
operator|=
name|oi
operator|.
name|newReader
argument_list|()
init|;
name|RevWalk
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
return|return
name|createRevertCommit
argument_list|(
name|message
argument_list|,
name|notes
argument_list|,
name|user
argument_list|,
name|ts
argument_list|,
name|oi
argument_list|,
name|revWalk
argument_list|,
literal|null
argument_list|)
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
name|ResourceNotFoundException
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Creates a revert commit.    *    * @param message Commit message for the revert commit.    * @param notes ChangeNotes of the change being reverted.    * @param user Current User performing the revert.    * @param ts Timestamp of creation for the commit.    * @param oi ObjectInserter for inserting the newly created commit.    * @param revWalk Used for parsing the original commit.    * @param generatedChangeId The changeId for the commit message, can be null since it is not    *     needed for commits, only for changes.    * @return ObjectId that represents the newly created commit.    * @throws ResourceConflictException Can't revert the initial commit.    * @throws IOException Thrown in case of I/O errors.    */
DECL|method|createRevertCommit ( String message, ChangeNotes notes, CurrentUser user, Timestamp ts, ObjectInserter oi, RevWalk revWalk, @Nullable ObjectId generatedChangeId)
specifier|private
name|ObjectId
name|createRevertCommit
parameter_list|(
name|String
name|message
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Timestamp
name|ts
parameter_list|,
name|ObjectInserter
name|oi
parameter_list|,
name|RevWalk
name|revWalk
parameter_list|,
annotation|@
name|Nullable
name|ObjectId
name|generatedChangeId
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|IOException
block|{
name|PatchSet
name|patch
init|=
name|notes
operator|.
name|getCurrentPatchSet
argument_list|()
decl_stmt|;
name|RevCommit
name|commitToRevert
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|patch
operator|.
name|commitId
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
name|PersonIdent
name|committerIdent
init|=
name|serverIdent
operator|.
name|get
argument_list|()
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
name|ts
argument_list|,
name|committerIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
decl_stmt|;
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
name|Change
name|changeToRevert
init|=
name|notes
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|String
name|subject
init|=
name|changeToRevert
operator|.
name|getSubject
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|.
name|length
argument_list|()
operator|>
literal|63
condition|)
block|{
name|subject
operator|=
name|subject
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|59
argument_list|)
operator|+
literal|"..."
expr_stmt|;
block|}
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
name|subject
argument_list|,
name|patch
operator|.
name|commitId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|generatedChangeId
operator|!=
literal|null
condition|)
block|{
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
name|generatedChangeId
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
return|return
name|id
return|;
block|}
DECL|method|createRevertChangeFromCommit ( ObjectId revertCommitId, RevertInput input, ChangeNotes notes, CurrentUser user, @Nullable ObjectId generatedChangeId, Timestamp ts, ObjectInserter oi, RevWalk revWalk, Repository git)
specifier|private
name|Change
operator|.
name|Id
name|createRevertChangeFromCommit
parameter_list|(
name|ObjectId
name|revertCommitId
parameter_list|,
name|RevertInput
name|input
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
annotation|@
name|Nullable
name|ObjectId
name|generatedChangeId
parameter_list|,
name|Timestamp
name|ts
parameter_list|,
name|ObjectInserter
name|oi
parameter_list|,
name|RevWalk
name|revWalk
parameter_list|,
name|Repository
name|git
parameter_list|)
throws|throws
name|IOException
throws|,
name|RestApiException
throws|,
name|UpdateException
throws|,
name|ConfigInvalidException
block|{
name|RevCommit
name|revertCommit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|revertCommitId
argument_list|)
decl_stmt|;
name|Change
name|changeToRevert
init|=
name|notes
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|id
argument_list|(
name|seq
operator|.
name|nextChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|NotifyResolver
operator|.
name|Result
name|notify
init|=
name|notifyResolver
operator|.
name|resolve
argument_list|(
name|firstNonNull
argument_list|(
name|input
operator|.
name|notify
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
argument_list|,
name|input
operator|.
name|notifyDetails
argument_list|)
decl_stmt|;
name|ChangeInserter
name|ins
init|=
name|changeInserterFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|,
name|revertCommit
argument_list|,
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
operator|.
name|setTopic
argument_list|(
name|input
operator|.
name|topic
operator|==
literal|null
condition|?
name|changeToRevert
operator|.
name|getTopic
argument_list|()
else|:
name|input
operator|.
name|topic
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|ins
operator|.
name|setMessage
argument_list|(
literal|"Uploaded patch set 1."
argument_list|)
expr_stmt|;
name|ReviewerSet
name|reviewerSet
init|=
name|approvalsUtil
operator|.
name|getReviewers
argument_list|(
name|notes
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Id
argument_list|>
name|reviewers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|reviewers
operator|.
name|add
argument_list|(
name|changeToRevert
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|reviewers
operator|.
name|addAll
argument_list|(
name|reviewerSet
operator|.
name|byState
argument_list|(
name|ReviewerStateInternal
operator|.
name|REVIEWER
argument_list|)
argument_list|)
expr_stmt|;
name|reviewers
operator|.
name|remove
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ccs
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|reviewerSet
operator|.
name|byState
argument_list|(
name|ReviewerStateInternal
operator|.
name|CC
argument_list|)
argument_list|)
decl_stmt|;
name|ccs
operator|.
name|remove
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setReviewersAndCcs
argument_list|(
name|reviewers
argument_list|,
name|ccs
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setRevertOf
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
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
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|,
name|user
argument_list|,
name|ts
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
name|setNotify
argument_list|(
name|notify
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
name|addOp
argument_list|(
name|changeId
argument_list|,
operator|new
name|NotifyOp
argument_list|(
name|changeToRevert
argument_list|,
name|ins
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|changeToRevert
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|PostRevertedMessageOp
argument_list|(
name|generatedChangeId
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|changeId
return|;
block|}
DECL|class|NotifyOp
specifier|private
class|class
name|NotifyOp
implements|implements
name|BatchUpdateOp
block|{
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|ins
specifier|private
specifier|final
name|ChangeInserter
name|ins
decl_stmt|;
DECL|method|NotifyOp (Change change, ChangeInserter ins)
name|NotifyOp
parameter_list|(
name|Change
name|change
parameter_list|,
name|ChangeInserter
name|ins
parameter_list|)
block|{
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|ins
operator|=
name|ins
expr_stmt|;
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
name|Exception
block|{
name|changeReverted
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|ins
operator|.
name|getChange
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|RevertedSender
name|cm
init|=
name|revertedSenderFactory
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
name|setNotify
argument_list|(
name|ctx
operator|.
name|getNotify
argument_list|(
name|change
operator|.
name|getId
argument_list|()
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot send email for revert change %s"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|PostRevertedMessageOp
specifier|private
class|class
name|PostRevertedMessageOp
implements|implements
name|BatchUpdateOp
block|{
DECL|field|computedChangeId
specifier|private
specifier|final
name|ObjectId
name|computedChangeId
decl_stmt|;
DECL|method|PostRevertedMessageOp (ObjectId computedChangeId)
name|PostRevertedMessageOp
parameter_list|(
name|ObjectId
name|computedChangeId
parameter_list|)
block|{
name|this
operator|.
name|computedChangeId
operator|=
name|computedChangeId
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
block|{
name|Change
name|change
init|=
name|ctx
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|patchSetId
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|ChangeMessage
name|changeMessage
init|=
name|ChangeMessagesUtil
operator|.
name|newMessage
argument_list|(
name|ctx
argument_list|,
literal|"Created a revert of this change as I"
operator|+
name|computedChangeId
operator|.
name|name
argument_list|()
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_REVERT
argument_list|)
decl_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|ctx
operator|.
name|getUpdate
argument_list|(
name|patchSetId
argument_list|)
argument_list|,
name|changeMessage
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit


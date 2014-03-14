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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
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
name|gerrit
operator|.
name|acceptance
operator|.
name|git
operator|.
name|GitUtil
operator|.
name|cloneProject
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
name|acceptance
operator|.
name|git
operator|.
name|GitUtil
operator|.
name|initSsh
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotEquals
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
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|AbstractDaemonTest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|AccountCreator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|RestResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|RestSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|SshSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|TestAccount
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
operator|.
name|GitUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
operator|.
name|PushOneCommit
import|;
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
name|Project
operator|.
name|InheritableBoolean
import|;
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
operator|.
name|SubmitType
import|;
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
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
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
name|api
operator|.
name|Git
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|diff
operator|.
name|DiffFormatter
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
DECL|class|AbstractSubmit
specifier|public
specifier|abstract
class|class
name|AbstractSubmit
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Inject
DECL|field|accounts
specifier|private
name|AccountCreator
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|field|reviewDbProvider
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|reviewDbProvider
decl_stmt|;
annotation|@
name|Inject
DECL|field|repoManager
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|session
specifier|protected
name|RestSession
name|session
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|admin
operator|=
name|accounts
operator|.
name|admin
argument_list|()
expr_stmt|;
name|session
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|initSsh
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|db
operator|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|cleanup ()
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|getSubmitType ()
specifier|protected
specifier|abstract
name|SubmitType
name|getSubmitType
parameter_list|()
function_decl|;
annotation|@
name|Test
DECL|method|submitToEmptyRepo ()
specifier|public
name|void
name|submitToEmptyRepo
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
name|Git
name|git
init|=
name|createProject
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|(
name|git
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|change
operator|.
name|getCommitId
argument_list|()
argument_list|,
name|getRemoteHead
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createProject ()
specifier|protected
name|Git
name|createProject
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
return|return
name|createProject
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|createProject (boolean emptyCommit)
specifier|private
name|Git
name|createProject
parameter_list|(
name|boolean
name|emptyCommit
parameter_list|)
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
name|SshSession
name|sshSession
init|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
decl_stmt|;
try|try
block|{
name|GitUtil
operator|.
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|,
name|emptyCommit
argument_list|)
expr_stmt|;
name|setSubmitType
argument_list|(
name|getSubmitType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloneProject
argument_list|(
name|sshSession
operator|.
name|getUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|sshSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setSubmitType (SubmitType submitType)
specifier|private
name|void
name|setSubmitType
parameter_list|(
name|SubmitType
name|submitType
parameter_list|)
throws|throws
name|IOException
block|{
name|ProjectConfigInput
name|in
init|=
operator|new
name|ProjectConfigInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|submit_type
operator|=
name|submitType
expr_stmt|;
name|in
operator|.
name|use_content_merge
operator|=
name|InheritableBoolean
operator|.
name|FALSE
expr_stmt|;
name|RestResponse
name|r
init|=
name|session
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/config"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|setUseContentMerge ()
specifier|protected
name|void
name|setUseContentMerge
parameter_list|()
throws|throws
name|IOException
block|{
name|ProjectConfigInput
name|in
init|=
operator|new
name|ProjectConfigInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|use_content_merge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|RestResponse
name|r
init|=
name|session
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/config"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|createChange (Git git)
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|Git
name|git
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
operator|new
name|PushOneCommit
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
return|;
block|}
DECL|method|createChange (Git git, String subject, String fileName, String content)
specifier|protected
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|Git
name|git
parameter_list|,
name|String
name|subject
parameter_list|,
name|String
name|fileName
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
operator|new
name|PushOneCommit
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|subject
argument_list|,
name|fileName
argument_list|,
name|content
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
return|;
block|}
DECL|method|submit (String changeId)
specifier|protected
name|void
name|submit
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
name|submit
argument_list|(
name|changeId
argument_list|,
name|HttpStatus
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
block|}
DECL|method|submitWithConflict (String changeId)
specifier|protected
name|void
name|submitWithConflict
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
name|submit
argument_list|(
name|changeId
argument_list|,
name|HttpStatus
operator|.
name|SC_CONFLICT
argument_list|)
expr_stmt|;
block|}
DECL|method|submitStatusOnly (String changeId)
specifier|protected
name|void
name|submitStatusOnly
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|approve
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|Change
name|c
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byKey
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|toList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|c
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|SUBMITTED
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
argument_list|)
expr_stmt|;
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
operator|new
name|PatchSetApproval
argument_list|(
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|admin
operator|.
name|id
argument_list|,
name|PatchSetApproval
operator|.
name|LabelId
operator|.
name|SUBMIT
argument_list|)
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|,
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|submit (String changeId, int expectedStatus)
specifier|private
name|void
name|submit
parameter_list|(
name|String
name|changeId
parameter_list|,
name|int
name|expectedStatus
parameter_list|)
throws|throws
name|IOException
block|{
name|approve
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|session
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/submit"
argument_list|,
name|SubmitInput
operator|.
name|waitForMerge
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedStatus
operator|==
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
name|ChangeInfo
name|change
init|=
operator|(
operator|new
name|Gson
argument_list|()
operator|)
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|,
name|change
operator|.
name|status
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|approve (String changeId)
specifier|private
name|void
name|approve
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/revisions/current/review"
argument_list|,
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|assertCherryPick (Git localGit, boolean contentMerge)
specifier|protected
name|void
name|assertCherryPick
parameter_list|(
name|Git
name|localGit
parameter_list|,
name|boolean
name|contentMerge
parameter_list|)
throws|throws
name|IOException
block|{
name|assertRebase
argument_list|(
name|localGit
argument_list|,
name|contentMerge
argument_list|)
expr_stmt|;
name|RevCommit
name|remoteHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|remoteHead
operator|.
name|getFooterLines
argument_list|(
literal|"Reviewed-On"
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|remoteHead
operator|.
name|getFooterLines
argument_list|(
literal|"Reviewed-By"
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRebase (Git localGit, boolean contentMerge)
specifier|protected
name|void
name|assertRebase
parameter_list|(
name|Git
name|localGit
parameter_list|,
name|boolean
name|contentMerge
parameter_list|)
throws|throws
name|IOException
block|{
name|Repository
name|repo
init|=
name|localGit
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|RevCommit
name|localHead
init|=
name|getHead
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|RevCommit
name|remoteHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
name|localHead
operator|.
name|getId
argument_list|()
argument_list|,
name|remoteHead
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|remoteHead
operator|.
name|getParentCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|contentMerge
condition|)
block|{
name|assertEquals
argument_list|(
name|getLatestDiff
argument_list|(
name|repo
argument_list|)
argument_list|,
name|getLatestRemoteDiff
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|localHead
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|remoteHead
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getHead (Repository repo)
specifier|private
name|RevCommit
name|getHead
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getHead
argument_list|(
name|repo
argument_list|,
literal|"HEAD"
argument_list|)
return|;
block|}
DECL|method|getRemoteHead ()
specifier|protected
name|RevCommit
name|getRemoteHead
parameter_list|()
throws|throws
name|IOException
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|getHead
argument_list|(
name|repo
argument_list|,
literal|"refs/heads/master"
argument_list|)
return|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getRemoteLog ()
specifier|protected
name|List
argument_list|<
name|RevCommit
argument_list|>
name|getRemoteLog
parameter_list|()
throws|throws
name|IOException
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
try|try
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|repo
operator|.
name|getRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|rw
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getHead (Repository repo, String name)
specifier|private
name|RevCommit
name|getHead
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|rw
operator|.
name|parseCommit
argument_list|(
name|repo
operator|.
name|getRef
argument_list|(
name|name
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getLatestDiff (Repository repo)
specifier|private
name|String
name|getLatestDiff
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectId
name|oldTreeId
init|=
name|repo
operator|.
name|resolve
argument_list|(
literal|"HEAD~1^{tree}"
argument_list|)
decl_stmt|;
name|ObjectId
name|newTreeId
init|=
name|repo
operator|.
name|resolve
argument_list|(
literal|"HEAD^{tree}"
argument_list|)
decl_stmt|;
return|return
name|getLatestDiff
argument_list|(
name|repo
argument_list|,
name|oldTreeId
argument_list|,
name|newTreeId
argument_list|)
return|;
block|}
DECL|method|getLatestRemoteDiff ()
specifier|private
name|String
name|getLatestRemoteDiff
parameter_list|()
throws|throws
name|IOException
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
try|try
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|ObjectId
name|oldTreeId
init|=
name|repo
operator|.
name|resolve
argument_list|(
literal|"refs/heads/master~1^{tree}"
argument_list|)
decl_stmt|;
name|ObjectId
name|newTreeId
init|=
name|repo
operator|.
name|resolve
argument_list|(
literal|"refs/heads/master^{tree}"
argument_list|)
decl_stmt|;
return|return
name|getLatestDiff
argument_list|(
name|repo
argument_list|,
name|oldTreeId
argument_list|,
name|newTreeId
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getLatestDiff (Repository repo, ObjectId oldTreeId, ObjectId newTreeId)
specifier|private
name|String
name|getLatestDiff
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ObjectId
name|oldTreeId
parameter_list|,
name|ObjectId
name|newTreeId
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DiffFormatter
name|fmt
init|=
operator|new
name|DiffFormatter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|fmt
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|format
argument_list|(
name|oldTreeId
argument_list|,
name|newTreeId
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|RefNames
operator|.
name|changeMetaRef
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|SIGNED_OFF_BY_TAG
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
name|collect
operator|.
name|Iterables
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
name|acceptance
operator|.
name|PushOneCommit
operator|.
name|Result
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ChangeApi
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
name|CherryPickInput
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
name|ReviewInput
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
name|client
operator|.
name|ChangeStatus
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
name|client
operator|.
name|GeneralPreferencesInfo
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
name|common
operator|.
name|ChangeInput
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
name|MergeInput
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
name|BadRequestException
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
name|MethodNotAllowedException
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
name|server
operator|.
name|config
operator|.
name|AnonymousCowardNameProvider
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
name|ChangeAlreadyMergedException
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
name|testutil
operator|.
name|ConfigSuite
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
name|testutil
operator|.
name|FakeEmailSender
operator|.
name|Message
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
name|testutil
operator|.
name|TestTimeUtil
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
name|Config
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
name|transport
operator|.
name|RefSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_class
DECL|class|CreateChangeIT
specifier|public
class|class
name|CreateChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|allowDraftsDisabled ()
specifier|public
specifier|static
name|Config
name|allowDraftsDisabled
parameter_list|()
block|{
return|return
name|allowDraftsDisabledConfig
argument_list|()
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|setTimeForTesting ()
specifier|public
specifier|static
name|void
name|setTimeForTesting
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|restoreTime ()
specifier|public
specifier|static
name|void
name|restoreTime
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEmptyChange_MissingBranch ()
specifier|public
name|void
name|createEmptyChange_MissingBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|ci
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|ci
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"branch must be non-empty"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEmptyChange_MissingMessage ()
specifier|public
name|void
name|createEmptyChange_MissingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|ci
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|ci
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|ci
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"commit message must be non-empty"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEmptyChange_InvalidStatus ()
specifier|public
name|void
name|createEmptyChange_InvalidStatus
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|ci
init|=
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"unsupported change status"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNewChange ()
specifier|public
name|void
name|createNewChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|notificationsOnChangeCreation ()
specifier|public
name|void
name|notificationsOnChangeCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|watch
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// check that watcher is notified
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Message
argument_list|>
name|messages
init|=
name|sender
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|m
operator|.
name|rcpt
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|user
operator|.
name|emailAddress
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|m
operator|.
name|body
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|admin
operator|.
name|fullName
operator|+
literal|" has uploaded this change for review."
argument_list|)
expr_stmt|;
comment|// check that watcher is not notified if notify=NONE
name|sender
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ChangeInput
name|input
init|=
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
decl_stmt|;
name|input
operator|.
name|notify
operator|=
name|NotifyHandling
operator|.
name|NONE
expr_stmt|;
name|assertCreateSucceeds
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNewChangeSignedOffByFooter ()
specifier|public
name|void
name|createNewChangeSignedOffByFooter
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|setSignedOffByFooter
argument_list|()
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|message
init|=
name|info
operator|.
name|revisions
operator|.
name|get
argument_list|(
name|info
operator|.
name|currentRevision
argument_list|)
operator|.
name|commit
operator|.
name|message
decl_stmt|;
name|assertThat
argument_list|(
name|message
argument_list|)
operator|.
name|contains
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%sAdministrator<%s>"
argument_list|,
name|SIGNED_OFF_BY_TAG
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNewDraftChange ()
specifier|public
name|void
name|createNewDraftChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNewDraftChangeNotAllowed ()
specifier|public
name|void
name|createNewDraftChangeNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|ChangeInput
name|ci
init|=
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|MethodNotAllowedException
operator|.
name|class
argument_list|,
literal|"draft workflow is disabled"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noteDbCommit ()
specifier|public
name|void
name|noteDbCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|readChanges
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|changeMetaRef
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|c
operator|.
name|_number
argument_list|)
argument_list|)
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Create change"
argument_list|)
expr_stmt|;
name|PersonIdent
name|expectedAuthor
init|=
name|changeNoteUtil
operator|.
name|newIdent
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|admin
operator|.
name|id
argument_list|)
operator|.
name|getAccount
argument_list|()
argument_list|,
name|c
operator|.
name|created
argument_list|,
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|AnonymousCowardNameProvider
operator|.
name|DEFAULT
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedAuthor
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|c
operator|.
name|created
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getParentCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|createMergeChange ()
specifier|public
name|void
name|createMergeChange
parameter_list|()
throws|throws
name|Exception
block|{
name|changeInTwoBranches
argument_list|(
literal|"branchA"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"branchB"
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"branchA"
argument_list|,
literal|"branchB"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertCreateSucceeds
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMergeChange_Conflicts ()
specifier|public
name|void
name|createMergeChange_Conflicts
parameter_list|()
throws|throws
name|Exception
block|{
name|changeInTwoBranches
argument_list|(
literal|"branchA"
argument_list|,
literal|"shared.txt"
argument_list|,
literal|"branchB"
argument_list|,
literal|"shared.txt"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"branchA"
argument_list|,
literal|"branchB"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|in
argument_list|,
name|RestApiException
operator|.
name|class
argument_list|,
literal|"merge conflict"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMergeChange_Conflicts_Ours ()
specifier|public
name|void
name|createMergeChange_Conflicts_Ours
parameter_list|()
throws|throws
name|Exception
block|{
name|changeInTwoBranches
argument_list|(
literal|"branchA"
argument_list|,
literal|"shared.txt"
argument_list|,
literal|"branchB"
argument_list|,
literal|"shared.txt"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"branchA"
argument_list|,
literal|"branchB"
argument_list|,
literal|"ours"
argument_list|)
decl_stmt|;
name|assertCreateSucceeds
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidSource ()
specifier|public
name|void
name|invalidSource
parameter_list|()
throws|throws
name|Exception
block|{
name|changeInTwoBranches
argument_list|(
literal|"branchA"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"branchB"
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"branchA"
argument_list|,
literal|"invalid"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|in
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"Cannot resolve 'invalid' to a commit"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidStrategy ()
specifier|public
name|void
name|invalidStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|changeInTwoBranches
argument_list|(
literal|"branchA"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"branchB"
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"branchA"
argument_list|,
literal|"branchB"
argument_list|,
literal|"octopus"
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|in
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"invalid merge strategy: octopus"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|alreadyMerged ()
specifier|public
name|void
name|alreadyMerged
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|c0
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"second commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"b contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"master"
argument_list|,
name|c0
operator|.
name|getName
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|in
argument_list|,
name|ChangeAlreadyMergedException
operator|.
name|class
argument_list|,
literal|"'"
operator|+
name|c0
operator|.
name|getName
argument_list|()
operator|+
literal|"' has already been merged"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|onlyContentMerged ()
specifier|public
name|void
name|onlyContentMerged
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
comment|// create a change, and cherrypick into master
name|PushOneCommit
operator|.
name|Result
name|cId
init|=
name|createChange
argument_list|()
decl_stmt|;
name|RevCommit
name|commitId
init|=
name|cId
operator|.
name|getCommit
argument_list|()
decl_stmt|;
name|CherryPickInput
name|cpi
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|cpi
operator|.
name|destination
operator|=
literal|"master"
expr_stmt|;
name|cpi
operator|.
name|message
operator|=
literal|"cherry pick the commit"
expr_stmt|;
name|ChangeApi
name|orig
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeApi
name|cherry
init|=
name|orig
operator|.
name|current
argument_list|()
operator|.
name|cherryPick
argument_list|(
name|cpi
argument_list|)
decl_stmt|;
name|cherry
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|cherry
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|ObjectId
name|remoteId
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|remoteId
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
name|ChangeInput
name|in
init|=
name|newMergeChangeInput
argument_list|(
literal|"master"
argument_list|,
name|commitId
operator|.
name|getName
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertCreateSucceeds
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|newChangeInput (ChangeStatus status)
specifier|private
name|ChangeInput
name|newChangeInput
parameter_list|(
name|ChangeStatus
name|status
parameter_list|)
block|{
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|in
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|in
operator|.
name|subject
operator|=
literal|"Empty change"
expr_stmt|;
name|in
operator|.
name|topic
operator|=
literal|"support-gerrit-workflow-in-browser"
expr_stmt|;
name|in
operator|.
name|status
operator|=
name|status
expr_stmt|;
return|return
name|in
return|;
block|}
DECL|method|assertCreateSucceeds (ChangeInput in)
specifier|private
name|ChangeInfo
name|assertCreateSucceeds
parameter_list|(
name|ChangeInput
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeInfo
name|out
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|project
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|project
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|branch
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|branch
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|subject
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|subject
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|topic
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|topic
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|status
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|revisions
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|submitted
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|Boolean
name|draft
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|out
operator|.
name|revisions
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|draft
decl_stmt|;
name|assertThat
argument_list|(
name|booleanToDraftStatus
argument_list|(
name|draft
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|status
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
DECL|method|assertCreateFails ( ChangeInput in, Class<? extends RestApiException> errType, String errSubstring)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|ChangeInput
name|in
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RestApiException
argument_list|>
name|errType
parameter_list|,
name|String
name|errSubstring
parameter_list|)
throws|throws
name|Exception
block|{
name|exception
operator|.
name|expect
argument_list|(
name|errType
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
name|errSubstring
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|booleanToDraftStatus (Boolean draft)
specifier|private
name|ChangeStatus
name|booleanToDraftStatus
parameter_list|(
name|Boolean
name|draft
parameter_list|)
block|{
if|if
condition|(
name|draft
operator|==
literal|null
condition|)
block|{
return|return
name|ChangeStatus
operator|.
name|NEW
return|;
block|}
return|return
name|draft
condition|?
name|ChangeStatus
operator|.
name|DRAFT
else|:
name|ChangeStatus
operator|.
name|NEW
return|;
block|}
comment|// TODO(davido): Expose setting of account preferences in the API
DECL|method|setSignedOffByFooter ()
specifier|private
name|void
name|setSignedOffByFooter
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/accounts/"
operator|+
name|admin
operator|.
name|email
operator|+
literal|"/preferences"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|GeneralPreferencesInfo
name|i
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|GeneralPreferencesInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|i
operator|.
name|signedOffBy
operator|=
literal|true
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/accounts/"
operator|+
name|admin
operator|.
name|email
operator|+
literal|"/preferences"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|GeneralPreferencesInfo
name|o
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|GeneralPreferencesInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o
operator|.
name|signedOffBy
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|newMergeChangeInput (String targetBranch, String sourceRef, String strategy)
specifier|private
name|ChangeInput
name|newMergeChangeInput
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|sourceRef
parameter_list|,
name|String
name|strategy
parameter_list|)
block|{
comment|// create a merge change from branchA to master in gerrit
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|in
operator|.
name|branch
operator|=
name|targetBranch
expr_stmt|;
name|in
operator|.
name|subject
operator|=
literal|"merge "
operator|+
name|sourceRef
operator|+
literal|" to "
operator|+
name|targetBranch
expr_stmt|;
name|in
operator|.
name|status
operator|=
name|ChangeStatus
operator|.
name|NEW
expr_stmt|;
name|MergeInput
name|mergeInput
init|=
operator|new
name|MergeInput
argument_list|()
decl_stmt|;
name|mergeInput
operator|.
name|source
operator|=
name|sourceRef
expr_stmt|;
name|in
operator|.
name|merge
operator|=
name|mergeInput
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|in
operator|.
name|merge
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
DECL|method|changeInTwoBranches (String branchA, String fileA, String branchB, String fileB)
specifier|private
name|void
name|changeInTwoBranches
parameter_list|(
name|String
name|branchA
parameter_list|,
name|String
name|fileA
parameter_list|,
name|String
name|branchB
parameter_list|,
name|String
name|fileB
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create a initial commit in master
name|Result
name|initialCommit
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
literal|"initial commit"
argument_list|,
literal|"readme.txt"
argument_list|,
literal|"initial commit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|initialCommit
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
comment|// create two new branches
name|createBranch
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
name|branchA
argument_list|)
argument_list|)
expr_stmt|;
name|createBranch
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
name|branchB
argument_list|)
argument_list|)
expr_stmt|;
comment|// create a commit in branchA
name|Result
name|changeA
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
literal|"change A"
argument_list|,
name|fileA
argument_list|,
literal|"A content"
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/heads/"
operator|+
name|branchA
argument_list|)
decl_stmt|;
name|changeA
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
comment|// create a commit in branchB
name|PushOneCommit
name|commitB
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
literal|"change B"
argument_list|,
name|fileB
argument_list|,
literal|"B content"
argument_list|)
decl_stmt|;
name|commitB
operator|.
name|setParent
argument_list|(
name|initialCommit
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|Result
name|changeB
init|=
name|commitB
operator|.
name|to
argument_list|(
literal|"refs/heads/"
operator|+
name|branchB
argument_list|)
decl_stmt|;
name|changeB
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


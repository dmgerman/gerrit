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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|ImmutableList
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
name|util
operator|.
name|RequestId
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
name|TestChanges
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
DECL|class|CommitMessageOutputTest
specifier|public
class|class
name|CommitMessageOutputTest
extends|extends
name|AbstractChangeNotesTest
block|{
annotation|@
name|Test
DECL|method|approvalsCommitFormatSimple ()
specifier|public
name|void
name|approvalsCommitFormatSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Verified"
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
literal|"Code-Review"
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|update
operator|.
name|putReviewer
argument_list|(
name|changeOwner
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|REVIEWER
argument_list|)
expr_stmt|;
name|update
operator|.
name|putReviewer
argument_list|(
name|otherUser
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|CC
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|update
operator|.
name|getRefName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/changes/01/1/meta"
argument_list|)
expr_stmt|;
name|RevCommit
name|commit
init|=
name|parseCommit
argument_list|(
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
decl_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: "
operator|+
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: "
operator|+
name|update
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Reviewer: Change Owner<1@gerrit>\n"
operator|+
literal|"CC: Other Account<2@gerrit>\n"
operator|+
literal|"Label: Code-Review=-1\n"
operator|+
literal|"Label: Verified=+1\n"
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Change Owner"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"1@gerrit"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Date
argument_list|(
name|c
operator|.
name|getCreatedOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT-7:00"
argument_list|)
argument_list|)
expr_stmt|;
name|PersonIdent
name|committer
init|=
name|commit
operator|.
name|getCommitterIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Gerrit Server"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"noreply@gerrit.com"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getWhen
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getTimeZone
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeMessageCommitFormatSimple ()
specifier|public
name|void
name|changeMessageCommitFormatSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Just a little code change.\n"
operator|+
literal|"How about a new line"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|update
operator|.
name|getRefName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/changes/01/1/meta"
argument_list|)
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Just a little code change.\n"
operator|+
literal|"How about a new line\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: "
operator|+
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: "
operator|+
name|update
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeWithRevision ()
specifier|public
name|void
name|changeWithRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Foo"
argument_list|)
expr_stmt|;
name|RevCommit
name|commit
init|=
name|tr
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"Subject"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|update
operator|.
name|setCommit
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|update
operator|.
name|getRefName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"refs/changes/01/1/meta"
argument_list|)
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Foo\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: "
operator|+
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Subject: Subject\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: "
operator|+
name|commit
operator|.
name|name
argument_list|()
operator|+
literal|"\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|approvalTombstoneCommitFormat ()
specifier|public
name|void
name|approvalTombstoneCommitFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|removeApproval
argument_list|(
literal|"Code-Review"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Label: -Code-Review\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitCommitFormat ()
specifier|public
name|void
name|submitCommitFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setSubjectForCommit
argument_list|(
literal|"Submit patch set 1"
argument_list|)
expr_stmt|;
name|RequestId
name|submissionId
init|=
name|RequestId
operator|.
name|forChange
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|update
operator|.
name|merge
argument_list|(
name|submissionId
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|submitRecord
argument_list|(
literal|"NOT_READY"
argument_list|,
literal|null
argument_list|,
name|submitLabel
argument_list|(
literal|"Verified"
argument_list|,
literal|"OK"
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|submitLabel
argument_list|(
literal|"Code-Review"
argument_list|,
literal|"NEED"
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|,
name|submitRecord
argument_list|(
literal|"NOT_READY"
argument_list|,
literal|null
argument_list|,
name|submitLabel
argument_list|(
literal|"Verified"
argument_list|,
literal|"OK"
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|submitLabel
argument_list|(
literal|"Alternative-Code-Review"
argument_list|,
literal|"NEED"
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|parseCommit
argument_list|(
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
decl_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Submit patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Status: merged\n"
operator|+
literal|"Submission-id: "
operator|+
name|submissionId
operator|.
name|toStringForStorage
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Submitted-with: NOT_READY\n"
operator|+
literal|"Submitted-with: OK: Verified: Change Owner<1@gerrit>\n"
operator|+
literal|"Submitted-with: NEED: Code-Review\n"
operator|+
literal|"Submitted-with: NOT_READY\n"
operator|+
literal|"Submitted-with: OK: Verified: Change Owner<1@gerrit>\n"
operator|+
literal|"Submitted-with: NEED: Alternative-Code-Review\n"
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Change Owner"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"1@gerrit"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|Date
argument_list|(
name|c
operator|.
name|getCreatedOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT-7:00"
argument_list|)
argument_list|)
expr_stmt|;
name|PersonIdent
name|committer
init|=
name|commit
operator|.
name|getCommitterIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Gerrit Server"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"noreply@gerrit.com"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getWhen
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|committer
operator|.
name|getTimeZone
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|author
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|anonymousUser ()
specifier|public
name|void
name|anonymousUser
parameter_list|()
throws|throws
name|Exception
block|{
name|Account
name|anon
init|=
operator|new
name|Account
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|3
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|accountCache
operator|.
name|put
argument_list|(
name|anon
argument_list|)
expr_stmt|;
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|userFactory
operator|.
name|create
argument_list|(
name|anon
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Comment on the change."
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|parseCommit
argument_list|(
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
decl_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Comment on the change.\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|,
name|commit
argument_list|)
expr_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Anonymous Coward (3)"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"3@gerrit"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitWithErrorMessage ()
specifier|public
name|void
name|submitWithErrorMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setSubjectForCommit
argument_list|(
literal|"Submit patch set 1"
argument_list|)
expr_stmt|;
name|RequestId
name|submissionId
init|=
name|RequestId
operator|.
name|forChange
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|update
operator|.
name|merge
argument_list|(
name|submissionId
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|submitRecord
argument_list|(
literal|"RULE_ERROR"
argument_list|,
literal|"Problem with patch set:\n1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Submit patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Status: merged\n"
operator|+
literal|"Submission-id: "
operator|+
name|submissionId
operator|.
name|toStringForStorage
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Submitted-with: RULE_ERROR Problem with patch set: 1\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noChangeMessage ()
specifier|public
name|void
name|noChangeMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|putReviewer
argument_list|(
name|changeOwner
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|REVIEWER
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Reviewer: Change Owner<1@gerrit>\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeMessageWithTrailingDoubleNewline ()
specifier|public
name|void
name|changeMessageWithTrailingDoubleNewline
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Testing trailing double newline\n"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Testing trailing double newline\n"
operator|+
literal|"\n"
operator|+
literal|"\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeMessageWithMultipleParagraphs ()
specifier|public
name|void
name|changeMessageWithMultipleParagraphs
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Testing paragraph 1\n"
operator|+
literal|"\n"
operator|+
literal|"Testing paragraph 2\n"
operator|+
literal|"\n"
operator|+
literal|"Testing paragraph 3"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Testing paragraph 1\n"
operator|+
literal|"\n"
operator|+
literal|"Testing paragraph 2\n"
operator|+
literal|"\n"
operator|+
literal|"Testing paragraph 3\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeMessageWithTag ()
specifier|public
name|void
name|changeMessageWithTag
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Change message with tag"
argument_list|)
expr_stmt|;
name|update
operator|.
name|setTag
argument_list|(
literal|"jenkins"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Change message with tag\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Tag: jenkins\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|leadingWhitespace ()
specifier|public
name|void
name|leadingWhitespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|c
operator|.
name|setCurrentPatchSet
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
literal|"  "
operator|+
name|c
operator|.
name|getSubject
argument_list|()
argument_list|,
name|c
operator|.
name|getOriginalSubject
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeId
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setBranch
argument_list|(
name|c
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: "
operator|+
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Subject:   Change subject\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: "
operator|+
name|update
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|changeOwner
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|setCurrentPatchSet
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
literal|"\t\t"
operator|+
name|c
operator|.
name|getSubject
argument_list|()
argument_list|,
name|c
operator|.
name|getOriginalSubject
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|changeOwner
argument_list|)
expr_stmt|;
name|update
operator|.
name|setChangeId
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setBranch
argument_list|(
name|c
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: "
operator|+
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"Subject: \t\tChange subject\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: "
operator|+
name|update
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|"\n"
argument_list|,
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|realUser ()
specifier|public
name|void
name|realUser
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
name|c
init|=
name|newChange
argument_list|()
decl_stmt|;
name|CurrentUser
name|ownerAsOtherUser
init|=
name|userFactory
operator|.
name|runAs
argument_list|(
literal|null
argument_list|,
name|otherUserId
argument_list|,
name|changeOwner
argument_list|)
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|newUpdate
argument_list|(
name|c
argument_list|,
name|ownerAsOtherUser
argument_list|)
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Message on behalf of other user"
argument_list|)
expr_stmt|;
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
name|RevCommit
name|commit
init|=
name|parseCommit
argument_list|(
name|update
operator|.
name|getResult
argument_list|()
argument_list|)
decl_stmt|;
name|PersonIdent
name|author
init|=
name|commit
operator|.
name|getAuthorIdent
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Other Account"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|author
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"2@gerrit"
argument_list|)
expr_stmt|;
name|assertBodyEquals
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"\n"
operator|+
literal|"Message on behalf of other user\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Real-user: Change Owner<1@gerrit>\n"
argument_list|,
name|commit
argument_list|)
expr_stmt|;
block|}
DECL|method|parseCommit (ObjectId id)
specifier|private
name|RevCommit
name|parseCommit
parameter_list|(
name|ObjectId
name|id
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|id
operator|instanceof
name|RevCommit
condition|)
block|{
return|return
operator|(
name|RevCommit
operator|)
name|id
return|;
block|}
try|try
init|(
name|RevWalk
name|walk
init|=
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
name|walk
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|walk
operator|.
name|parseBody
argument_list|(
name|commit
argument_list|)
expr_stmt|;
return|return
name|commit
return|;
block|}
block|}
DECL|method|assertBodyEquals (String expected, ObjectId commitId)
specifier|private
name|void
name|assertBodyEquals
parameter_list|(
name|String
name|expected
parameter_list|,
name|ObjectId
name|commitId
parameter_list|)
throws|throws
name|Exception
block|{
name|RevCommit
name|commit
init|=
name|parseCommit
argument_list|(
name|commitId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getFullMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


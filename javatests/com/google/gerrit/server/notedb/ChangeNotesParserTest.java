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
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|server
operator|.
name|notedb
operator|.
name|ChangeNotesCommit
operator|.
name|ChangeNotesRevWalk
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
name|time
operator|.
name|TimeUtil
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|junit
operator|.
name|TestRepository
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
name|revwalk
operator|.
name|RevCommit
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

begin_class
DECL|class|ChangeNotesParserTest
specifier|public
class|class
name|ChangeNotesParserTest
extends|extends
name|AbstractChangeNotesTest
block|{
DECL|field|testRepo
specifier|private
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|testRepo
decl_stmt|;
DECL|field|walk
specifier|private
name|ChangeNotesRevWalk
name|walk
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpTestRepo ()
specifier|public
name|void
name|setUpTestRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|walk
operator|=
name|ChangeNotesCommit
operator|.
name|newRevWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDownTestRepo ()
specifier|public
name|void
name|tearDownTestRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|walk
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseAuthor ()
specifier|public
name|void
name|parseAuthor
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
literal|"Update change\n\nPatch-set: 1\n"
argument_list|,
operator|new
name|PersonIdent
argument_list|(
literal|"Change Owner"
argument_list|,
literal|"owner@example.com"
argument_list|,
name|serverIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|serverIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
literal|"Update change\n\nPatch-set: 1\n"
argument_list|,
operator|new
name|PersonIdent
argument_list|(
literal|"Change Owner"
argument_list|,
literal|"x@gerrit"
argument_list|,
name|serverIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|serverIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
literal|"Update change\n\nPatch-set: 1\n"
argument_list|,
operator|new
name|PersonIdent
argument_list|(
literal|"Change\n\u1234<Owner>"
argument_list|,
literal|"\n\nx<@>\u0002gerrit"
argument_list|,
name|serverIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|serverIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseStatus ()
specifier|public
name|void
name|parseStatus
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Status: NEW\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Status: new\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nStatus: OOPS\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nStatus: NEW\nStatus: NEW\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parsePatchSetId ()
specifier|public
name|void
name|parsePatchSetId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nPatch-set: 1\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: x\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseApproval ()
specifier|public
name|void
name|parseApproval
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Label: Label1=+1\n"
operator|+
literal|"Label: Label2=1\n"
operator|+
literal|"Label: Label3=0\n"
operator|+
literal|"Label: Label4=-1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Label: -Label1\n"
operator|+
literal|"Label: -Label1 Other Account<2@gerrit>\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: Label1=X\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: Label1 = 1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: X+Y\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: Label1 Other Account<2@gerrit>\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: -Label!1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nLabel: -Label!1 Other Account<2@gerrit>\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseSubmitRecords ()
specifier|public
name|void
name|parseSubmitRecords
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
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
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nSubmitted-with: OOPS\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nSubmitted-with: NEED: X+Y\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Submitted-with: OK: X+Y: Change Owner<1@gerrit>\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Submitted-with: OK: Code-Review: 1@gerrit\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseSubmissionId ()
specifier|public
name|void
name|parseSubmissionId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
operator|+
literal|"Submission-id: 1-1453387607626-96fabc25"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Submission-id: 1-1453387607626-96fabc25\n"
operator|+
literal|"Submission-id: 1-1453387901516-5d1e2450"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseReviewer ()
specifier|public
name|void
name|parseReviewer
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Reviewer: Change Owner<1@gerrit>\n"
operator|+
literal|"CC: Other Account<2@gerrit>\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nReviewer: 1@gerrit\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseTopic ()
specifier|public
name|void
name|parseTopic
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Topic: Some Topic\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Topic:\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nTopic: Some Topic\nTopic: Other Topic"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseBranch ()
specifier|public
name|void
name|parseBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Branch: refs/heads/stable"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseChangeId ()
specifier|public
name|void
name|parseChangeId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: This is a test change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Change-id: I159532ef4844d7c18f7f3fd37a0b275590d41b1b"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseSubject ()
specifier|public
name|void
name|parseSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Some subject of a change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Subject: Some subject of a change\n"
operator|+
literal|"Subject: Some other subject\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseCommit ()
specifier|public
name|void
name|parseCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 2\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Some subject of a change\n"
operator|+
literal|"Commit: abcd1234abcd1234abcd1234abcd1234abcd1234"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 2\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Subject: Some subject of a change\n"
operator|+
literal|"Commit: abcd1234abcd1234abcd1234abcd1234abcd1234\n"
operator|+
literal|"Commit: deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update patch set 1\n"
operator|+
literal|"Uploaded patch set 1.\n"
operator|+
literal|"Patch-set: 2\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Subject: Some subject of a change\n"
operator|+
literal|"Commit: beef"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parsePatchSetState ()
specifier|public
name|void
name|parsePatchSetState
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1 (PUBLISHED)\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Some subject of a change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1 (DRAFT)\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Some subject of a change\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1 (DELETED)\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Some subject of a change\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1 (NOT A STATUS)\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Subject: Some subject of a change\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parsePatchSetGroups ()
specifier|public
name|void
name|parsePatchSetGroups
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 2\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Commit: abcd1234abcd1234abcd1234abcd1234abcd1234\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Groups: a,b,c\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 2\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Commit: abcd1234abcd1234abcd1234abcd1234abcd1234\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Groups: a,b,c\n"
operator|+
literal|"Groups: d,e,f\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseServerIdent ()
specifier|public
name|void
name|parseServerIdent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
decl_stmt|;
name|assertParseSucceeds
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
name|writeCommit
argument_list|(
name|msg
argument_list|,
name|serverIdent
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|=
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"With a message."
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
expr_stmt|;
name|assertParseSucceeds
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
name|writeCommit
argument_list|(
name|msg
argument_list|,
name|serverIdent
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|=
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Label: Label1=+1\n"
expr_stmt|;
name|assertParseSucceeds
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
name|msg
argument_list|,
name|serverIdent
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseTag ()
specifier|public
name|void
name|parseTag
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Tag:\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Tag: jenkins\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Branch: refs/heads/master\n"
operator|+
literal|"Change-id: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"Subject: Change subject\n"
operator|+
literal|"Tag: ci\n"
operator|+
literal|"Tag: jenkins\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseWorkInProgress ()
specifier|public
name|void
name|parseWorkInProgress
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Change created in WIP remains in WIP.
name|RevCommit
name|commit
init|=
name|writeCommit
argument_list|(
literal|"Update WIP change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ChangeNotesState
name|state
init|=
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|reviewStarted
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Moving change out of WIP starts review.
name|commit
operator|=
name|writeCommit
argument_list|(
literal|"New ready change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Work-in-progress: false\n"
argument_list|)
expr_stmt|;
name|state
operator|=
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|reviewStarted
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Change created not in WIP has always been in review started state.
name|state
operator|=
name|assertParseSucceeds
argument_list|(
literal|"New change that doesn't declare WIP\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|reviewStarted
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pendingReviewers ()
specifier|public
name|void
name|pendingReviewers
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Change created in WIP.
name|RevCommit
name|commit
init|=
name|writeCommit
argument_list|(
literal|"Update WIP change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ChangeNotesState
name|state
init|=
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|pendingReviewers
argument_list|()
operator|.
name|all
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|pendingReviewersByEmail
argument_list|()
operator|.
name|all
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
comment|// Reviewers added while in WIP.
name|commit
operator|=
name|writeCommit
argument_list|(
literal|"Add reviewers\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-set: 1\n"
operator|+
literal|"Reviewer: Change Owner "
operator|+
literal|"<1@gerrit>\n"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|state
operator|=
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|state
operator|.
name|pendingReviewers
argument_list|()
operator|.
name|byState
argument_list|(
name|ReviewerStateInternal
operator|.
name|REVIEWER
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|caseInsensitiveFooters ()
specifier|public
name|void
name|caseInsensitiveFooters
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"BRaNch: refs/heads/master\n"
operator|+
literal|"Change-ID: I577fb248e474018276351785930358ec0450e9f7\n"
operator|+
literal|"patcH-set: 1\n"
operator|+
literal|"subject: This is a test change\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|currentPatchSet ()
specifier|public
name|void
name|currentPatchSet
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseSucceeds
argument_list|(
literal|"Update change\n\nPatch-set: 1\nCurrent: true"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n\nPatch-set: 1\nCurrent: tRUe"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nCurrent: false"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n\nPatch-set: 1\nCurrent: blah"
argument_list|)
expr_stmt|;
block|}
DECL|method|writeCommit (String body)
specifier|private
name|RevCommit
name|writeCommit
parameter_list|(
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeNoteUtil
name|noteUtil
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ChangeNoteUtil
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|writeCommit
argument_list|(
name|body
argument_list|,
name|noteUtil
operator|.
name|newIdent
argument_list|(
name|changeOwner
operator|.
name|getAccount
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|serverIdent
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|writeCommit (String body, PersonIdent author)
specifier|private
name|RevCommit
name|writeCommit
parameter_list|(
name|String
name|body
parameter_list|,
name|PersonIdent
name|author
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|writeCommit
argument_list|(
name|body
argument_list|,
name|author
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|writeCommit (String body, boolean initWorkInProgress)
specifier|private
name|RevCommit
name|writeCommit
parameter_list|(
name|String
name|body
parameter_list|,
name|boolean
name|initWorkInProgress
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeNoteUtil
name|noteUtil
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ChangeNoteUtil
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|writeCommit
argument_list|(
name|body
argument_list|,
name|noteUtil
operator|.
name|newIdent
argument_list|(
name|changeOwner
operator|.
name|getAccount
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|serverIdent
argument_list|)
argument_list|,
name|initWorkInProgress
argument_list|)
return|;
block|}
DECL|method|writeCommit (String body, PersonIdent author, boolean initWorkInProgress)
specifier|private
name|RevCommit
name|writeCommit
parameter_list|(
name|String
name|body
parameter_list|,
name|PersonIdent
name|author
parameter_list|,
name|boolean
name|initWorkInProgress
parameter_list|)
throws|throws
name|Exception
block|{
name|Change
name|change
init|=
name|newChange
argument_list|(
name|initWorkInProgress
argument_list|)
decl_stmt|;
name|ChangeNotes
name|notes
init|=
name|newNotes
argument_list|(
name|change
argument_list|)
operator|.
name|load
argument_list|()
decl_stmt|;
try|try
init|(
name|ObjectInserter
name|ins
init|=
name|testRepo
operator|.
name|getRepository
argument_list|()
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setParentId
argument_list|(
name|notes
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|serverIdent
argument_list|,
name|author
operator|.
name|getWhen
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|testRepo
operator|.
name|tree
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|ins
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|ins
operator|.
name|flush
argument_list|()
expr_stmt|;
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
DECL|method|assertParseSucceeds (String body)
specifier|private
name|ChangeNotesState
name|assertParseSucceeds
parameter_list|(
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|assertParseSucceeds
argument_list|(
name|writeCommit
argument_list|(
name|body
argument_list|)
argument_list|)
return|;
block|}
DECL|method|assertParseSucceeds (RevCommit commit)
specifier|private
name|ChangeNotesState
name|assertParseSucceeds
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
return|;
block|}
DECL|method|assertParseFails (String body)
specifier|private
name|void
name|assertParseFails
parameter_list|(
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertParseFails (RevCommit commit)
specifier|private
name|void
name|assertParseFails
parameter_list|(
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThrows
argument_list|(
name|ConfigInvalidException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|newParser
argument_list|(
name|commit
argument_list|)
operator|.
name|parseAll
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newParser (ObjectId tip)
specifier|private
name|ChangeNotesParser
name|newParser
parameter_list|(
name|ObjectId
name|tip
parameter_list|)
throws|throws
name|Exception
block|{
name|walk
operator|.
name|reset
argument_list|()
expr_stmt|;
name|ChangeNoteJson
name|changeNoteJson
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ChangeNoteJson
operator|.
name|class
argument_list|)
decl_stmt|;
name|LegacyChangeNoteRead
name|reader
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|LegacyChangeNoteRead
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|ChangeNotesParser
argument_list|(
name|newChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|tip
argument_list|,
name|walk
argument_list|,
name|changeNoteJson
argument_list|,
name|reader
argument_list|,
name|args
operator|.
name|metrics
argument_list|)
return|;
block|}
block|}
end_class

end_unit


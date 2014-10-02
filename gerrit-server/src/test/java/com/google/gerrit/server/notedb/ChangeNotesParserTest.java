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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|RevWalk
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
operator|new
name|RevWalk
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
name|release
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
literal|"Patch-Set: 1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|writeCommit
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
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
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
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
literal|"Patch-Set: 1\n"
operator|+
literal|"Status: NEW\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Status: new\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Status: OOPS\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Status: NEW\n"
operator|+
literal|"Status: NEW\n"
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
literal|"Patch-Set: 1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Patch-Set: 1\n"
argument_list|)
expr_stmt|;
name|assertParseSucceeds
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: x\n"
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
literal|"Patch-Set: 1\n"
operator|+
literal|"Label: Label1=+1\n"
operator|+
literal|"Label: Label2=1\n"
operator|+
literal|"Label: Label3=0\n"
operator|+
literal|"Label: Label4=-1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Label: Label1=X\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Label: Label1 = 1\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Label: X+Y\n"
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
literal|"Patch-Set: 1\n"
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
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Submitted-with: OOPS\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Submitted-with: NEED: X+Y\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
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
literal|"Patch-Set: 1\n"
operator|+
literal|"Submitted-with: OK: Code-Review: 1@gerrit\n"
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
literal|"Patch-Set: 1\n"
operator|+
literal|"Reviewer: Change Owner<1@gerrit>\n"
operator|+
literal|"CC: Other Account<2@gerrit>\n"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"Update change\n"
operator|+
literal|"\n"
operator|+
literal|"Patch-Set: 1\n"
operator|+
literal|"Reviewer: 1@gerrit\n"
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
return|return
name|writeCommit
argument_list|(
name|body
argument_list|,
name|ChangeNoteUtil
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
argument_list|,
literal|"Anonymous Coward"
argument_list|)
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
decl_stmt|;
try|try
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
finally|finally
block|{
name|ins
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|assertParseSucceeds (String body)
specifier|private
name|void
name|assertParseSucceeds
parameter_list|(
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ChangeNotesParser
name|parser
init|=
name|newParser
argument_list|(
name|writeCommit
argument_list|(
name|body
argument_list|)
argument_list|)
init|)
block|{
name|parser
operator|.
name|parseAll
argument_list|()
expr_stmt|;
block|}
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
try|try
init|(
name|ChangeNotesParser
name|parser
init|=
name|newParser
argument_list|(
name|commit
argument_list|)
init|)
block|{
name|parser
operator|.
name|parseAll
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected parse to fail:\n"
operator|+
name|commit
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
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
return|return
operator|new
name|ChangeNotesParser
argument_list|(
name|newChange
argument_list|()
argument_list|,
name|tip
argument_list|,
name|walk
argument_list|,
name|repoManager
argument_list|)
return|;
block|}
block|}
end_class

end_unit


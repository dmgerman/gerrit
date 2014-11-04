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
DECL|package|com.google.gerrit.acceptance.api.revision
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|revision
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
name|assertTrue
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
name|fail
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
name|NoHttpd
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
name|api
operator|.
name|changes
operator|.
name|SubmitInput
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
name|projects
operator|.
name|BranchInput
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
name|RestApiException
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
name|IOException
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|RevisionIT
specifier|public
class|class
name|RevisionIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|admin2
specifier|private
name|TestAccount
name|admin2
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
name|admin2
operator|=
name|accounts
operator|.
name|admin2
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|reviewTriplet ()
specifier|public
name|void
name|reviewTriplet
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|reviewCurrent ()
specifier|public
name|void
name|reviewCurrent
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|reviewNumber ()
specifier|public
name|void
name|reviewNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
literal|1
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
name|updateChange
argument_list|(
name|r
argument_list|,
literal|"new content"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
literal|2
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submit ()
specifier|public
name|void
name|submit
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AuthException
operator|.
name|class
argument_list|)
DECL|method|submitOnBehalfOf ()
specifier|public
name|void
name|submitOnBehalfOf
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
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
name|SubmitInput
name|in
init|=
operator|new
name|SubmitInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|onBehalfOf
operator|=
name|admin2
operator|.
name|email
expr_stmt|;
name|in
operator|.
name|waitForMerge
operator|=
literal|true
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDraft ()
specifier|public
name|void
name|deleteDraft
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createDraft
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cherryPick ()
specifier|public
name|void
name|cherryPick
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master%topic=someTopic"
argument_list|)
decl_stmt|;
name|CherryPickInput
name|in
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|destination
operator|=
literal|"foo"
expr_stmt|;
name|in
operator|.
name|message
operator|=
literal|"it goes to stable branch"
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|in
operator|.
name|destination
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
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
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|orig
operator|.
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeApi
name|cherry
init|=
name|orig
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|cherryPick
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|orig
operator|.
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cherry
operator|.
name|get
argument_list|()
operator|.
name|subject
operator|.
name|contains
argument_list|(
name|in
operator|.
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"someTopic"
argument_list|,
name|cherry
operator|.
name|get
argument_list|()
operator|.
name|topic
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Test
DECL|method|cherryPickIdenticalTree ()
specifier|public
name|void
name|cherryPickIdenticalTree
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|CherryPickInput
name|in
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|destination
operator|=
literal|"foo"
expr_stmt|;
name|in
operator|.
name|message
operator|=
literal|"it goes to stable branch"
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|in
operator|.
name|destination
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
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
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|orig
operator|.
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeApi
name|cherry
init|=
name|orig
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|cherryPick
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|orig
operator|.
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cherry
operator|.
name|get
argument_list|()
operator|.
name|subject
operator|.
name|contains
argument_list|(
name|in
operator|.
name|message
argument_list|)
argument_list|)
expr_stmt|;
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
try|try
block|{
name|orig
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|cherryPick
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Cherry-pick identical tree error expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestApiException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cherry pick failed: identical tree"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|cherryPickConflict ()
specifier|public
name|void
name|cherryPickConflict
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|CherryPickInput
name|in
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|destination
operator|=
literal|"foo"
expr_stmt|;
name|in
operator|.
name|message
operator|=
literal|"it goes to stable branch"
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|in
operator|.
name|destination
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
literal|"another content"
argument_list|)
decl_stmt|;
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/heads/foo"
argument_list|)
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
literal|"p~master~"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|orig
operator|.
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|orig
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|cherryPick
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Cherry-pick merge conflict error expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestApiException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cherry pick failed: merge conflict"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|canRebase ()
specifier|public
name|void
name|canRebase
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|merge
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|push
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r2
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|canRebase
argument_list|()
argument_list|)
expr_stmt|;
name|merge
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|git
operator|.
name|checkout
argument_list|()
operator|.
name|setName
argument_list|(
name|r1
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|push
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r3
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r3
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r3
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|canRebase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setUnsetReviewedFlag ()
specifier|public
name|void
name|setUnsetReviewedFlag
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|setReviewed
argument_list|(
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|setReviewed
argument_list|(
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewed
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|merge (PushOneCommit.Result r)
specifier|private
name|void
name|merge
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
throws|throws
name|Exception
block|{
name|revision
argument_list|(
name|r
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|revision
argument_list|(
name|r
argument_list|)
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
DECL|method|updateChange (PushOneCommit.Result r, String content)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|updateChange
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
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
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
literal|"test commit"
argument_list|,
literal|"a.txt"
argument_list|,
name|content
argument_list|,
name|r
operator|.
name|getChangeId
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
DECL|method|createDraft ()
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createDraft
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
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
literal|"refs/drafts/master"
argument_list|)
return|;
block|}
block|}
end_class

end_unit


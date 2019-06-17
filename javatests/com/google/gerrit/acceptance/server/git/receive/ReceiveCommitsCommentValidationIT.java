begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.git.receive
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|git
operator|.
name|receive
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
name|extensions
operator|.
name|annotations
operator|.
name|Exports
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
name|DraftInput
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
name|Side
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
name|config
operator|.
name|FactoryModule
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
name|validators
operator|.
name|CommentForValidation
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
name|validators
operator|.
name|CommentForValidation
operator|.
name|CommentType
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
name|validators
operator|.
name|CommentValidator
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
name|testing
operator|.
name|TestCommentHelper
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
name|Module
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|Capture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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

begin_comment
comment|/**  * Tests for comment validation when publishing drafts via the {@code --publish-comments} option.  */
end_comment

begin_class
DECL|class|ReceiveCommitsCommentValidationIT
specifier|public
class|class
name|ReceiveCommitsCommentValidationIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|mockCommentValidator
annotation|@
name|Inject
specifier|private
name|CommentValidator
name|mockCommentValidator
decl_stmt|;
DECL|field|testCommentHelper
annotation|@
name|Inject
specifier|private
name|TestCommentHelper
name|testCommentHelper
decl_stmt|;
DECL|field|COMMENT_TEXT
specifier|private
specifier|static
specifier|final
name|String
name|COMMENT_TEXT
init|=
literal|"The comment text"
decl_stmt|;
DECL|field|capture
specifier|private
name|Capture
argument_list|<
name|ImmutableList
argument_list|<
name|CommentForValidation
argument_list|>
argument_list|>
name|capture
init|=
operator|new
name|Capture
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createModule ()
specifier|public
name|Module
name|createModule
parameter_list|()
block|{
return|return
operator|new
name|FactoryModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CommentValidator
name|mockCommentValidator
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CommentValidator
operator|.
name|class
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|CommentValidator
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
name|mockCommentValidator
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
operator|.
name|toInstance
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CommentValidator
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|resetMock ()
specifier|public
name|void
name|resetMock
parameter_list|()
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|verifyMock ()
specifier|public
name|void
name|verifyMock
parameter_list|()
block|{
name|EasyMock
operator|.
name|verify
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateComments_commentOK ()
specifier|public
name|void
name|validateComments_commentOK
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mockCommentValidator
operator|.
name|validateComments
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|CommentForValidation
operator|.
name|create
argument_list|(
name|CommentForValidation
operator|.
name|CommentType
operator|.
name|FILE_COMMENT
argument_list|,
name|COMMENT_TEXT
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|revId
init|=
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|DraftInput
name|comment
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|testCommentHelper
operator|.
name|addDraft
argument_list|(
name|changeId
argument_list|,
name|revId
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Result
name|amendResult
init|=
name|amendChange
argument_list|(
name|changeId
argument_list|,
literal|"refs/for/master%publish-comments"
argument_list|,
name|admin
argument_list|,
name|testRepo
argument_list|)
decl_stmt|;
name|amendResult
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|amendResult
operator|.
name|assertNotMessage
argument_list|(
literal|"Comment validation failure:"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateComments_commentRejected ()
specifier|public
name|void
name|validateComments_commentRejected
parameter_list|()
throws|throws
name|Exception
block|{
name|CommentForValidation
name|commentForValidation
init|=
name|CommentForValidation
operator|.
name|create
argument_list|(
name|CommentType
operator|.
name|FILE_COMMENT
argument_list|,
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mockCommentValidator
operator|.
name|validateComments
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|CommentForValidation
operator|.
name|create
argument_list|(
name|CommentForValidation
operator|.
name|CommentType
operator|.
name|FILE_COMMENT
argument_list|,
name|COMMENT_TEXT
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|commentForValidation
operator|.
name|failValidation
argument_list|(
literal|"Oh no!"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|revId
init|=
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|DraftInput
name|comment
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|testCommentHelper
operator|.
name|addDraft
argument_list|(
name|changeId
argument_list|,
name|revId
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Result
name|amendResult
init|=
name|amendChange
argument_list|(
name|changeId
argument_list|,
literal|"refs/for/master%publish-comments"
argument_list|,
name|admin
argument_list|,
name|testRepo
argument_list|)
decl_stmt|;
name|amendResult
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|amendResult
operator|.
name|assertMessage
argument_list|(
literal|"Comment validation failure:"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateComments_inlineVsFileComments_allOK ()
specifier|public
name|void
name|validateComments_inlineVsFileComments_allOK
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mockCommentValidator
operator|.
name|validateComments
argument_list|(
name|EasyMock
operator|.
name|capture
argument_list|(
name|capture
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockCommentValidator
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|revId
init|=
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|DraftInput
name|draftFile
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|testCommentHelper
operator|.
name|addDraft
argument_list|(
name|changeId
argument_list|,
name|revId
argument_list|,
name|draftFile
argument_list|)
expr_stmt|;
name|DraftInput
name|draftInline
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|result
operator|.
name|getChange
argument_list|()
operator|.
name|currentFilePaths
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|,
literal|1
argument_list|,
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|testCommentHelper
operator|.
name|addDraft
argument_list|(
name|changeId
argument_list|,
name|revId
argument_list|,
name|draftInline
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|amendChange
argument_list|(
name|changeId
argument_list|,
literal|"refs/for/master%publish-comments"
argument_list|,
name|admin
argument_list|,
name|testRepo
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|capture
operator|.
name|getValues
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|capture
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|CommentForValidation
operator|.
name|create
argument_list|(
name|CommentForValidation
operator|.
name|CommentType
operator|.
name|INLINE_COMMENT
argument_list|,
name|draftInline
operator|.
name|message
argument_list|)
argument_list|,
name|CommentForValidation
operator|.
name|create
argument_list|(
name|CommentForValidation
operator|.
name|CommentType
operator|.
name|FILE_COMMENT
argument_list|,
name|draftFile
operator|.
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


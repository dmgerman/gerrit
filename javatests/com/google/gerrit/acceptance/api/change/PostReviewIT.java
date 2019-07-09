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
DECL|package|com.google.gerrit.acceptance.api.change
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
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
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
name|ReviewInput
operator|.
name|CommentInput
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
operator|.
name|DraftHandling
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
name|common
operator|.
name|ChangeMessageInfo
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
name|server
operator|.
name|restapi
operator|.
name|change
operator|.
name|PostReview
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
name|CommentsRejectedException
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
comment|/** Tests for comment validation in {@link PostReview}. */
end_comment

begin_class
DECL|class|PostReviewIT
specifier|public
class|class
name|PostReviewIT
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
DECL|method|validateCommentsInInput_commentOK ()
specifier|public
name|void
name|validateCommentsInInput_commentOK
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|CommentInput
name|comment
init|=
name|newComment
argument_list|(
name|r
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
argument_list|)
decl_stmt|;
name|comment
operator|.
name|updated
operator|=
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|input
operator|.
name|comments
operator|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|comment
operator|.
name|path
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|comment
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
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
name|review
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
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
DECL|method|validateCommentsInInput_commentRejected ()
specifier|public
name|void
name|validateCommentsInInput_commentRejected
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|CommentInput
name|comment
init|=
name|newComment
argument_list|(
name|r
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
argument_list|)
decl_stmt|;
name|comment
operator|.
name|updated
operator|=
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|input
operator|.
name|comments
operator|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|comment
operator|.
name|path
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|comment
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|BadRequestException
name|badRequestException
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
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
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|CommentsRejectedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
operator|(
operator|(
name|CommentsRejectedException
operator|)
name|badRequestException
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCommentValidationFailures
argument_list|()
argument_list|)
operator|.
name|getComment
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|COMMENT_TEXT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Oh no!"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
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
DECL|method|validateDrafts_draftOK ()
specifier|public
name|void
name|validateDrafts_draftOK
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
name|INLINE_COMMENT
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|DraftInput
name|draft
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|r
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
name|getName
argument_list|()
argument_list|)
operator|.
name|createDraft
argument_list|(
name|draft
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|drafts
operator|=
name|DraftHandling
operator|.
name|PUBLISH
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
name|review
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
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
DECL|method|validateDrafts_draftRejected ()
specifier|public
name|void
name|validateDrafts_draftRejected
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
name|INLINE_COMMENT
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
name|INLINE_COMMENT
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|DraftInput
name|draft
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|r
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
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|draft
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|drafts
operator|=
name|DraftHandling
operator|.
name|PUBLISH
expr_stmt|;
name|BadRequestException
name|badRequestException
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
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
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|CommentsRejectedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
operator|(
operator|(
name|CommentsRejectedException
operator|)
name|badRequestException
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCommentValidationFailures
argument_list|()
argument_list|)
operator|.
name|getComment
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|draft
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Oh no!"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
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
DECL|method|validateDrafts_inlineVsFileComments_allOK ()
specifier|public
name|void
name|validateDrafts_inlineVsFileComments_allOK
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
name|DraftInput
name|draftInline
init|=
name|testCommentHelper
operator|.
name|newDraft
argument_list|(
name|r
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
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|draftInline
argument_list|)
expr_stmt|;
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
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|draftFile
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
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
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|drafts
operator|=
name|DraftHandling
operator|.
name|PUBLISH
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
name|review
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|testCommentHelper
operator|.
name|getPublishedComments
argument_list|(
name|r
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
annotation|@
name|Test
DECL|method|validateCommentsInChangeMessage_messageOK ()
specifier|public
name|void
name|validateCommentsInChangeMessage_messageOK
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
name|CommentType
operator|.
name|CHANGE_MESSAGE
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
operator|.
name|message
argument_list|(
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|int
name|numMessages
init|=
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
name|get
argument_list|()
operator|.
name|messages
operator|.
name|size
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
name|input
argument_list|)
expr_stmt|;
name|assertThat
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
name|get
argument_list|()
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
name|numMessages
operator|+
literal|1
argument_list|)
expr_stmt|;
name|ChangeMessageInfo
name|message
init|=
name|Iterables
operator|.
name|getLast
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
name|get
argument_list|()
operator|.
name|messages
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|message
argument_list|)
operator|.
name|contains
argument_list|(
name|COMMENT_TEXT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateCommentsInChangeMessage_messageRejected ()
specifier|public
name|void
name|validateCommentsInChangeMessage_messageRejected
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
name|CHANGE_MESSAGE
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
name|CommentType
operator|.
name|CHANGE_MESSAGE
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
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
operator|.
name|message
argument_list|(
name|COMMENT_TEXT
argument_list|)
decl_stmt|;
name|assertThat
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
name|get
argument_list|()
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// From the initial commit.
name|BadRequestException
name|badRequestException
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
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
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|CommentsRejectedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
operator|(
operator|(
name|CommentsRejectedException
operator|)
name|badRequestException
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCommentValidationFailures
argument_list|()
argument_list|)
operator|.
name|getComment
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|COMMENT_TEXT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|badRequestException
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Oh no!"
argument_list|)
expr_stmt|;
name|assertThat
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
name|get
argument_list|()
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Unchanged from before.
name|ChangeMessageInfo
name|message
init|=
name|Iterables
operator|.
name|getLast
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
name|get
argument_list|()
operator|.
name|messages
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|message
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|COMMENT_TEXT
argument_list|)
expr_stmt|;
block|}
DECL|method|newComment (String path)
specifier|private
specifier|static
name|CommentInput
name|newComment
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|TestCommentHelper
operator|.
name|populate
argument_list|(
operator|new
name|CommentInput
argument_list|()
argument_list|,
name|path
argument_list|,
name|PostReviewIT
operator|.
name|COMMENT_TEXT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

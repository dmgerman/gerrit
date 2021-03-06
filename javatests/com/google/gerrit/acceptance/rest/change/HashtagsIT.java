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
name|Truth
operator|.
name|assertWithMessage
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
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|allow
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
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|truth
operator|.
name|IterableSubject
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
name|UseClockStep
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
name|testsuite
operator|.
name|project
operator|.
name|ProjectOperations
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
name|testsuite
operator|.
name|request
operator|.
name|RequestScopeOperations
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
name|data
operator|.
name|Permission
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
name|HashtagsInput
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
name|BadRequestException
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
annotation|@
name|UseClockStep
DECL|class|HashtagsIT
specifier|public
class|class
name|HashtagsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
DECL|field|requestScopeOperations
annotation|@
name|Inject
specifier|private
name|RequestScopeOperations
name|requestScopeOperations
decl_stmt|;
annotation|@
name|Test
DECL|method|getNoHashtags ()
specifier|public
name|void
name|getNoHashtags
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Get on a change with no hashtags returns an empty list.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addSingleHashtag ()
specifier|public
name|void
name|addSingleHashtag
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
comment|// Adding a single hashtag returns a single hashtag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag2"
argument_list|)
expr_stmt|;
comment|// Adding another single hashtag to change that already has one hashtag
comment|// returns a sorted list of hashtags with existing and new.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addInvalidHashtag ()
specifier|public
name|void
name|addInvalidHashtag
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
name|BadRequestException
name|thrown
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"invalid,hashtag"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"hashtags may not contain commas"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addMultipleHashtags ()
specifier|public
name|void
name|addMultipleHashtags
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
comment|// Adding multiple hashtags returns a sorted list of hashtags.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag3"
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags added: tag1, tag3"
argument_list|)
expr_stmt|;
comment|// Adding multiple hashtags to change that already has hashtags returns a
comment|// sorted list of hashtags with existing and new.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags added: tag2, tag4"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addAlreadyExistingHashtag ()
specifier|public
name|void
name|addAlreadyExistingHashtag
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Adding a hashtag that already exists on the change returns a sorted list
comment|// of hashtags without duplicates.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag2"
argument_list|)
expr_stmt|;
name|ChangeMessageInfo
name|last
init|=
name|getLastMessage
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertNoNewMessageSince
argument_list|(
name|r
argument_list|,
name|last
argument_list|)
expr_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hashtagsWithPrefix ()
specifier|public
name|void
name|hashtagsWithPrefix
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
comment|// Leading # is stripped from added tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"#tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag1"
argument_list|)
expr_stmt|;
comment|// Leading # is stripped from multiple added tags.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"#tag2"
argument_list|,
literal|"#tag3"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags added: tag2, tag3"
argument_list|)
expr_stmt|;
comment|// Leading # is stripped from removed tag.
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"#tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag removed: tag2"
argument_list|)
expr_stmt|;
comment|// Leading # is stripped from multiple removed tags.
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"#tag1"
argument_list|,
literal|"#tag3"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags removed: tag1, tag3"
argument_list|)
expr_stmt|;
comment|// Leading # and space are stripped from added tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"# tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag1"
argument_list|)
expr_stmt|;
comment|// Multiple leading # are stripped from added tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"##tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag2"
argument_list|)
expr_stmt|;
comment|// Multiple leading spaces and # are stripped from added tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"# # tag3"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: tag3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeSingleHashtag ()
specifier|public
name|void
name|removeSingleHashtag
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Removing a single tag from a change that only has that tag returns an
comment|// empty list.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|)
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag removed: tag1"
argument_list|)
expr_stmt|;
comment|// Removing a single tag from a change that has multiple tags returns a
comment|// sorted list of remaining tags.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|)
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag removed: tag2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeMultipleHashtags ()
specifier|public
name|void
name|removeMultipleHashtags
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Removing multiple tags from a change that only has those tags returns an
comment|// empty list.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags removed: tag1, tag2"
argument_list|)
expr_stmt|;
comment|// Removing multiple tags from a change that has multiple tags returns a
comment|// sorted list of remaining tags.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags removed: tag2, tag4"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNotExistingHashtag ()
specifier|public
name|void
name|removeNotExistingHashtag
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Removing a single hashtag from change that has no hashtags returns an
comment|// empty list.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|ChangeMessageInfo
name|last
init|=
name|getLastMessage
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertNoNewMessageSince
argument_list|(
name|r
argument_list|,
name|last
argument_list|)
expr_stmt|;
comment|// Removing a single non-existing tag from a change that only has one other
comment|// tag returns a list of only one tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|)
expr_stmt|;
name|last
operator|=
name|getLastMessage
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|)
expr_stmt|;
name|assertNoNewMessageSince
argument_list|(
name|r
argument_list|,
name|last
argument_list|)
expr_stmt|;
comment|// Removing a single non-existing tag from a change that has multiple tags
comment|// returns a sorted list of tags without any deleted.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|)
expr_stmt|;
name|last
operator|=
name|getLastMessage
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|removeHashtags
argument_list|(
name|r
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertNoNewMessageSince
argument_list|(
name|r
argument_list|,
name|last
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addAndRemove ()
specifier|public
name|void
name|addAndRemove
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Adding and remove hashtags in a single request performs correctly.
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|HashtagsInput
name|input
init|=
operator|new
name|HashtagsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|add
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|input
operator|.
name|remove
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"tag1"
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setHashtags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag2"
argument_list|,
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtags added: tag3, tag4\nHashtag removed: tag1"
argument_list|)
expr_stmt|;
comment|// Adding and removing the same hashtag actually removes it.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|HashtagsInput
argument_list|()
expr_stmt|;
name|input
operator|.
name|add
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"tag3"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|input
operator|.
name|remove
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"tag3"
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setHashtags
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tag1"
argument_list|,
literal|"tag2"
argument_list|,
literal|"tag4"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag removed: tag3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hashtagWithMixedCase ()
specifier|public
name|void
name|hashtagWithMixedCase
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
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"MyHashtag"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"MyHashtag"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: MyHashtag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addHashtagWithoutPermissionNotAllowed ()
specifier|public
name|void
name|addHashtagWithoutPermissionNotAllowed
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
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|AuthException
name|thrown
init|=
name|assertThrows
argument_list|(
name|AuthException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"MyHashtag"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"edit hashtags not permitted"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addHashtagWithPermissionAllowed ()
specifier|public
name|void
name|addHashtagWithPermissionAllowed
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
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
argument_list|(
name|Permission
operator|.
name|EDIT_HASHTAGS
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"MyHashtag"
argument_list|)
expr_stmt|;
name|assertThatGet
argument_list|(
name|r
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"MyHashtag"
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|r
argument_list|,
literal|"Hashtag added: MyHashtag"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertThatGet (PushOneCommit.Result r)
specifier|private
name|IterableSubject
name|assertThatGet
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
throws|throws
name|Exception
block|{
return|return
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getHashtags
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addHashtags (PushOneCommit.Result r, String... toAdd)
specifier|private
name|void
name|addHashtags
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|,
name|String
modifier|...
name|toAdd
parameter_list|)
throws|throws
name|Exception
block|{
name|HashtagsInput
name|input
init|=
operator|new
name|HashtagsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|add
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|toAdd
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setHashtags
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|removeHashtags (PushOneCommit.Result r, String... toRemove)
specifier|private
name|void
name|removeHashtags
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|,
name|String
modifier|...
name|toRemove
parameter_list|)
throws|throws
name|Exception
block|{
name|HashtagsInput
name|input
init|=
operator|new
name|HashtagsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|remove
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|toRemove
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setHashtags
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessage (PushOneCommit.Result r, String expectedMessage)
specifier|private
name|void
name|assertMessage
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|,
name|String
name|expectedMessage
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|getLastMessage
argument_list|(
name|r
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoNewMessageSince (PushOneCommit.Result r, ChangeMessageInfo expected)
specifier|private
name|void
name|assertNoNewMessageSince
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|,
name|ChangeMessageInfo
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|requireNonNull
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|ChangeMessageInfo
name|last
init|=
name|getLastMessage
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|last
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|last
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|getLastMessage (PushOneCommit.Result r)
specifier|private
name|ChangeMessageInfo
name|getLastMessage
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeMessageInfo
name|lastMessage
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
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
name|lastMessage
operator|.
name|message
argument_list|)
operator|.
name|that
argument_list|(
name|lastMessage
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|lastMessage
return|;
block|}
block|}
end_class

end_unit


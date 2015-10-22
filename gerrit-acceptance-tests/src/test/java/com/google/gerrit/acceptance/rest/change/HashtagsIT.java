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
annotation|@
name|NoHttpd
DECL|class|HashtagsIT
specifier|public
class|class
name|HashtagsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|enabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetNoHashtags ()
specifier|public
name|void
name|testGetNoHashtags
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
DECL|method|testAddSingleHashtag ()
specifier|public
name|void
name|testAddSingleHashtag
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
block|}
annotation|@
name|Test
DECL|method|testAddMultipleHashtags ()
specifier|public
name|void
name|testAddMultipleHashtags
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
block|}
annotation|@
name|Test
DECL|method|testAddAlreadyExistingHashtag ()
specifier|public
name|void
name|testAddAlreadyExistingHashtag
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
block|}
annotation|@
name|Test
DECL|method|testHashtagsWithPrefix ()
specifier|public
name|void
name|testHashtagsWithPrefix
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
block|}
annotation|@
name|Test
DECL|method|testRemoveSingleHashtag ()
specifier|public
name|void
name|testRemoveSingleHashtag
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
block|}
annotation|@
name|Test
DECL|method|testRemoveMultipleHashtags ()
specifier|public
name|void
name|testRemoveMultipleHashtags
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
block|}
annotation|@
name|Test
DECL|method|testRemoveNotExistingHashtag ()
specifier|public
name|void
name|testRemoveNotExistingHashtag
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
comment|// Removing a single non-existing tag from a change that only has one other
comment|// tag returns a list of only one tag.
name|addHashtags
argument_list|(
name|r
argument_list|,
literal|"tag1"
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
block|}
annotation|@
name|Test
DECL|method|testAddAndRemove ()
specifier|public
name|void
name|testAddAndRemove
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
block|}
specifier|private
name|IterableSubject
argument_list|<
name|?
extends|extends
name|IterableSubject
argument_list|<
name|?
argument_list|,
name|String
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|,
name|String
argument_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
DECL|method|assertThatGet (PushOneCommit.Result r)
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
block|}
end_class

end_unit


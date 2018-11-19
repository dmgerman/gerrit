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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|assertTrue
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
name|GerritBaseTests
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
DECL|class|MostSpecificComparatorTest
specifier|public
class|class
name|MostSpecificComparatorTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|cmp
specifier|private
name|MostSpecificComparator
name|cmp
decl_stmt|;
annotation|@
name|Test
DECL|method|shorterDistanceWins ()
specifier|public
name|void
name|shorterDistanceWins
parameter_list|()
block|{
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"refs/heads/master2"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"refs/heads/maste"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"^refs/heads/.*"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"refs/heads/master"
argument_list|,
literal|"^refs/heads/master.*"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assuming two patterns have the same Levenshtein distance, the pattern which represents a finite    * language wins over a pattern which represents an infinite language.    */
annotation|@
name|Test
DECL|method|finiteWinsOverInfinite ()
specifier|public
name|void
name|finiteWinsOverInfinite
parameter_list|()
block|{
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/......"
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/maste."
argument_list|,
literal|"^refs/heads/maste.*"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assuming two patterns have the same Levenshtein distance and are both either finite or infinite    * the one with the higher number of state transitions (in an equivalent automaton) wins    */
annotation|@
name|Test
DECL|method|higherNumberOfTransitionsWins ()
specifier|public
name|void
name|higherNumberOfTransitionsWins
parameter_list|()
block|{
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/x"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/[a-z].*"
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
comment|// Previously there was a bug where having a '1' in a refname would cause a
comment|// glob pattern's Levenshtein distance to decrease by 1.  These two
comment|// patterns should be a Levenshtein distance of 12 from the both of the
comment|// refnames, where previously the 'branch1' refname would be a distance of
comment|// 11 from 'refs/heads/abc/*'
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/abc/spam/branch2"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/.*spam.*"
argument_list|,
literal|"refs/heads/abc/*"
argument_list|)
expr_stmt|;
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/abc/spam/branch1"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/.*spam.*"
argument_list|,
literal|"refs/heads/abc/*"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assuming the same Levenshtein distance, (in)finity and the number of transitions, the longer    * pattern wins    */
annotation|@
name|Test
DECL|method|longerPatternWins ()
specifier|public
name|void
name|longerPatternWins
parameter_list|()
block|{
name|cmp
operator|=
operator|new
name|MostSpecificComparator
argument_list|(
literal|"refs/heads/x"
argument_list|)
expr_stmt|;
name|moreSpecificFirst
argument_list|(
literal|"^refs/heads/[a-z].*"
argument_list|,
literal|"^refs/heads/..*"
argument_list|)
expr_stmt|;
block|}
DECL|method|moreSpecificFirst (String first, String second)
specifier|private
name|void
name|moreSpecificFirst
parameter_list|(
name|String
name|first
parameter_list|,
name|String
name|second
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|cmp
operator|.
name|compare
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


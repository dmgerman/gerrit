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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
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
name|collect
operator|.
name|Iterables
operator|.
name|getOnlyElement
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
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
name|ImmutableSet
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
name|server
operator|.
name|account
operator|.
name|AccountResolver
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
name|server
operator|.
name|account
operator|.
name|AccountResolver
operator|.
name|Searcher
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
name|account
operator|.
name|AccountResolver
operator|.
name|StringSearcher
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
name|account
operator|.
name|AccountResolver
operator|.
name|UnresolvableAccountException
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
name|AllUsersName
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
DECL|class|AccountResolverTest
specifier|public
class|class
name|AccountResolverTest
block|{
DECL|class|TestSearcher
specifier|private
specifier|static
class|class
name|TestSearcher
extends|extends
name|StringSearcher
block|{
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
DECL|field|shortCircuit
specifier|private
specifier|final
name|boolean
name|shortCircuit
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|AccountState
argument_list|>
name|accounts
decl_stmt|;
DECL|field|assumeVisible
specifier|private
name|boolean
name|assumeVisible
decl_stmt|;
DECL|field|filterInactive
specifier|private
name|boolean
name|filterInactive
decl_stmt|;
DECL|method|TestSearcher (String pattern, boolean shortCircuit, AccountState... accounts)
specifier|private
name|TestSearcher
parameter_list|(
name|String
name|pattern
parameter_list|,
name|boolean
name|shortCircuit
parameter_list|,
name|AccountState
modifier|...
name|accounts
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|shortCircuit
operator|=
name|shortCircuit
expr_stmt|;
name|this
operator|.
name|accounts
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|accounts
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches (String input)
specifier|protected
name|boolean
name|matches
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|matches
argument_list|(
name|pattern
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|search (String input)
specifier|public
name|Stream
argument_list|<
name|AccountState
argument_list|>
name|search
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|accounts
operator|.
name|stream
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|shortCircuitIfNoResults ()
specifier|public
name|boolean
name|shortCircuitIfNoResults
parameter_list|()
block|{
return|return
name|shortCircuit
return|;
block|}
annotation|@
name|Override
DECL|method|callerMayAssumeCandidatesAreVisible ()
specifier|public
name|boolean
name|callerMayAssumeCandidatesAreVisible
parameter_list|()
block|{
return|return
name|assumeVisible
return|;
block|}
DECL|method|setCallerMayAssumeCandidatesAreVisible ()
name|void
name|setCallerMayAssumeCandidatesAreVisible
parameter_list|()
block|{
name|this
operator|.
name|assumeVisible
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|callerShouldFilterOutInactiveCandidates ()
specifier|public
name|boolean
name|callerShouldFilterOutInactiveCandidates
parameter_list|()
block|{
return|return
name|filterInactive
return|;
block|}
DECL|method|setCallerShouldFilterOutInactiveCandidates ()
name|void
name|setCallerShouldFilterOutInactiveCandidates
parameter_list|()
block|{
name|this
operator|.
name|filterInactive
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|accounts
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|","
argument_list|,
name|pattern
operator|+
literal|"("
argument_list|,
literal|")"
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|noShortCircuit ()
specifier|public
name|void
name|noShortCircuit
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TestSearcher
argument_list|(
literal|"bar"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|2
argument_list|)
argument_list|,
name|newAccount
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|input
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|search
argument_list|(
literal|"bar"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|input
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|search
argument_list|(
literal|"baz"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|input
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shortCircuit ()
specifier|public
name|void
name|shortCircuit
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"f.*"
argument_list|,
literal|true
argument_list|)
argument_list|,
operator|new
name|TestSearcher
argument_list|(
literal|"foo|bar"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|input
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|result
operator|=
name|search
argument_list|(
literal|"bar"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|input
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|filterInvisible ()
specifier|public
name|void
name|filterInvisible
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|,
name|newAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|only
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipVisibilityCheck ()
specifier|public
name|void
name|skipVisibilityCheck
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSearcher
name|searcher
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|,
name|newAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|searcher
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|only
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|searcher
operator|.
name|setCallerMayAssumeCandidatesAreVisible
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|only
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|dontFilterInactive ()
specifier|public
name|void
name|dontFilterInactive
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newInactiveAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TestSearcher
argument_list|(
literal|"f.*"
argument_list|,
literal|false
argument_list|,
name|newInactiveAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
comment|// Searchers always short-circuit when finding a non-empty result list, and this one didn't
comment|// filter out inactive results, so the second searcher never ran.
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getOnlyElement
argument_list|(
name|result
operator|.
name|asList
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|filteredInactiveIds
argument_list|(
name|result
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|filterInactiveEventuallyFindingResults ()
specifier|public
name|void
name|filterInactiveEventuallyFindingResults
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSearcher
name|searcher1
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newInactiveAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|searcher1
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|TestSearcher
name|searcher2
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"f.*"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|searcher2
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|searcher1
argument_list|,
name|searcher2
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// No info about inactive results exposed if there was at least one active result.
name|assertThat
argument_list|(
name|filteredInactiveIds
argument_list|(
name|result
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|filterInactiveEventuallyFindingNoResults ()
specifier|public
name|void
name|filterInactiveEventuallyFindingNoResults
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSearcher
name|searcher1
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newInactiveAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|searcher1
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|TestSearcher
name|searcher2
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"f.*"
argument_list|,
literal|false
argument_list|,
name|newInactiveAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|searcher2
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|searcher1
argument_list|,
name|searcher2
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|filteredInactiveIds
argument_list|(
name|result
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|dontShortCircuitAfterFilteringInactiveCandidatesResultsInEmptyList ()
specifier|public
name|void
name|dontShortCircuitAfterFilteringInactiveCandidatesResultsInEmptyList
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountState
name|account1
init|=
name|newAccount
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|AccountState
name|account2
init|=
name|newInactiveAccount
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|TestSearcher
name|searcher1
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|account2
argument_list|)
decl_stmt|;
name|searcher1
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|TestSearcher
name|searcher2
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|account1
argument_list|,
name|account2
argument_list|)
decl_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|searcher1
argument_list|,
name|searcher2
argument_list|)
decl_stmt|;
comment|// searcher1 matched, but filtered out all candidates because account2 is inactive. Actual
comment|// result came from searcher2 instead.
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shortCircuitAfterFilteringInactiveCandidatesResultsInEmptyList ()
specifier|public
name|void
name|shortCircuitAfterFilteringInactiveCandidatesResultsInEmptyList
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountState
name|account1
init|=
name|newAccount
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|AccountState
name|account2
init|=
name|newInactiveAccount
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|TestSearcher
name|searcher1
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|true
argument_list|,
name|account2
argument_list|)
decl_stmt|;
name|searcher1
operator|.
name|setCallerShouldFilterOutInactiveCandidates
argument_list|()
expr_stmt|;
name|TestSearcher
name|searcher2
init|=
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|account1
argument_list|,
name|account2
argument_list|)
decl_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|searcher1
argument_list|,
name|searcher2
argument_list|)
decl_stmt|;
comment|// searcher1 matched and then filtered out all candidates because account2 is inactive, but
comment|// still short-circuited.
name|Result
name|result
init|=
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|asIdSet
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|filteredInactiveIds
argument_list|(
name|result
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ids
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|asUniqueWithNoResults ()
specifier|public
name|void
name|asUniqueWithNoResults
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"foo"
decl_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Predicate
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|visibilitySupplier
init|=
name|allVisible
argument_list|()
decl_stmt|;
name|UnresolvableAccountException
name|thrown
init|=
name|assertThrows
argument_list|(
name|UnresolvableAccountException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|search
argument_list|(
name|input
argument_list|,
name|searchers
argument_list|,
name|visibilitySupplier
argument_list|)
operator|.
name|asUnique
argument_list|()
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
name|isEqualTo
argument_list|(
literal|"Account 'foo' not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|asUniqueWithOneResult ()
specifier|public
name|void
name|asUniqueWithOneResult
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountState
name|account
init|=
name|newAccount
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|account
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
operator|.
name|asUnique
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|account
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|asUniqueWithMultipleResults ()
specifier|public
name|void
name|asUniqueWithMultipleResults
parameter_list|()
throws|throws
name|Exception
block|{
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|TestSearcher
argument_list|(
literal|"foo"
argument_list|,
literal|false
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|,
name|newAccount
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|UnresolvableAccountException
name|thrown
init|=
name|assertThrows
argument_list|(
name|UnresolvableAccountException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|search
argument_list|(
literal|"foo"
argument_list|,
name|searchers
argument_list|,
name|allVisible
argument_list|()
argument_list|)
operator|.
name|asUnique
argument_list|()
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
name|isEqualTo
argument_list|(
literal|"Account 'foo' is ambiguous:\n1: Anonymous Name (1)\n2: Anonymous Name (2)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceptionMessageNotFound ()
specifier|public
name|void
name|exceptionMessageNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountResolver
name|resolver
init|=
name|newAccountResolver
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
operator|new
name|UnresolvableAccountException
argument_list|(
name|resolver
operator|.
expr|new
name|Result
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Account 'foo' not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceptionMessageSelf ()
specifier|public
name|void
name|exceptionMessageSelf
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountResolver
name|resolver
init|=
name|newAccountResolver
argument_list|()
decl_stmt|;
name|UnresolvableAccountException
name|e
init|=
operator|new
name|UnresolvableAccountException
argument_list|(
name|resolver
operator|.
expr|new
name|Result
argument_list|(
literal|"self"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|isSelf
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Resolving account 'self' requires login"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceptionMessageMe ()
specifier|public
name|void
name|exceptionMessageMe
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountResolver
name|resolver
init|=
name|newAccountResolver
argument_list|()
decl_stmt|;
name|UnresolvableAccountException
name|e
init|=
operator|new
name|UnresolvableAccountException
argument_list|(
name|resolver
operator|.
expr|new
name|Result
argument_list|(
literal|"me"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|isSelf
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Resolving account 'me' requires login"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceptionMessageAmbiguous ()
specifier|public
name|void
name|exceptionMessageAmbiguous
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountResolver
name|resolver
init|=
name|newAccountResolver
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
operator|new
name|UnresolvableAccountException
argument_list|(
name|resolver
operator|.
expr|new
name|Result
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|newAccount
argument_list|(
literal|3
argument_list|)
argument_list|,
name|newAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Account 'foo' is ambiguous:\n1: Anonymous Name (1)\n3: Anonymous Name (3)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceptionMessageOnlyInactive ()
specifier|public
name|void
name|exceptionMessageOnlyInactive
parameter_list|()
throws|throws
name|Exception
block|{
name|AccountResolver
name|resolver
init|=
name|newAccountResolver
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
operator|new
name|UnresolvableAccountException
argument_list|(
name|resolver
operator|.
expr|new
name|Result
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|newInactiveAccount
argument_list|(
literal|3
argument_list|)
argument_list|,
name|newInactiveAccount
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Account 'foo' only matches inactive accounts. To use an inactive account, retry"
operator|+
literal|" with one of the following exact account IDs:\n"
operator|+
literal|"1: Anonymous Name (1)\n"
operator|+
literal|"3: Anonymous Name (3)"
argument_list|)
expr_stmt|;
block|}
DECL|method|search ( String input, ImmutableList<Searcher<?>> searchers, Supplier<Predicate<AccountState>> visibilitySupplier)
specifier|private
name|Result
name|search
parameter_list|(
name|String
name|input
parameter_list|,
name|ImmutableList
argument_list|<
name|Searcher
argument_list|<
name|?
argument_list|>
argument_list|>
name|searchers
parameter_list|,
name|Supplier
argument_list|<
name|Predicate
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|visibilitySupplier
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|newAccountResolver
argument_list|()
operator|.
name|searchImpl
argument_list|(
name|input
argument_list|,
name|searchers
argument_list|,
name|visibilitySupplier
argument_list|)
return|;
block|}
DECL|method|newAccountResolver ()
specifier|private
specifier|static
name|AccountResolver
name|newAccountResolver
parameter_list|()
block|{
return|return
operator|new
name|AccountResolver
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"Anonymous Name"
argument_list|)
return|;
block|}
DECL|method|newAccount (int id)
specifier|private
name|AccountState
name|newAccount
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|AccountState
operator|.
name|forAccount
argument_list|(
operator|new
name|AllUsersName
argument_list|(
literal|"All-Users"
argument_list|)
argument_list|,
operator|new
name|Account
argument_list|(
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newInactiveAccount (int id)
specifier|private
name|AccountState
name|newInactiveAccount
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|Account
name|a
init|=
operator|new
name|Account
argument_list|(
name|Account
operator|.
name|id
argument_list|(
name|id
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|a
operator|.
name|setActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|AccountState
operator|.
name|forAccount
argument_list|(
operator|new
name|AllUsersName
argument_list|(
literal|"All-Users"
argument_list|)
argument_list|,
name|a
argument_list|)
return|;
block|}
DECL|method|ids (int... ids)
specifier|private
specifier|static
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|(
name|int
modifier|...
name|ids
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|stream
argument_list|(
name|ids
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|Account
operator|::
name|id
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|allVisible ()
specifier|private
specifier|static
name|Supplier
argument_list|<
name|Predicate
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|allVisible
parameter_list|()
block|{
return|return
parameter_list|()
lambda|->
name|a
lambda|->
literal|true
return|;
block|}
DECL|method|only (int... ids)
specifier|private
specifier|static
name|Supplier
argument_list|<
name|Predicate
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|only
parameter_list|(
name|int
modifier|...
name|ids
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|idSet
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|ids
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|Account
operator|::
name|id
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
decl_stmt|;
return|return
parameter_list|()
lambda|->
name|a
lambda|->
name|idSet
operator|.
name|contains
argument_list|(
name|a
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|filteredInactiveIds (Result result)
specifier|private
specifier|static
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|filteredInactiveIds
parameter_list|(
name|Result
name|result
parameter_list|)
block|{
return|return
name|result
operator|.
name|filteredInactive
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


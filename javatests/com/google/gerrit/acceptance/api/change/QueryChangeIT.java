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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|block
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_OK
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
name|config
operator|.
name|GerritConfig
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
name|entities
operator|.
name|Project
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
name|ChangeInfo
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
name|TopLevelResource
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
name|QueryChanges
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
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|QueryChangeIT
specifier|public
class|class
name|QueryChangeIT
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
DECL|field|queryChangesProvider
annotation|@
name|Inject
specifier|private
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryChangesProvider
decl_stmt|;
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|multipleQueriesInOneRequestCanContainSameChange ()
specifier|public
name|void
name|multipleQueriesInOneRequestCanContainSameChange
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|cId1
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|cId2
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|int
name|numericId1
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId1
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
decl_stmt|;
name|int
name|numericId2
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId2
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId2
argument_list|)
operator|.
name|setWorkInProgress
argument_list|()
expr_stmt|;
name|QueryChanges
name|queryChanges
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"is:open repo:"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"is:wip repo:"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
operator|)
name|queryChanges
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
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
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|firstResultIds
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_number
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|_number
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|firstResultIds
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|numericId1
argument_list|,
name|numericId2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_number
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numericId2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|moreChangesIndicatorDoesNotWronglyCopyToUnrelatedChanges ()
specifier|public
name|void
name|moreChangesIndicatorDoesNotWronglyCopyToUnrelatedChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|queryWithMoreChanges
init|=
literal|"is:wip limit:1 repo:"
operator|+
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|queryWithNoMoreChanges
init|=
literal|"is:open limit:10 repo:"
operator|+
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
expr_stmt|;
name|String
name|cId2
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|cId3
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId2
argument_list|)
operator|.
name|setWorkInProgress
argument_list|()
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId3
argument_list|)
operator|.
name|setWorkInProgress
argument_list|()
expr_stmt|;
comment|// Run the capped query first
name|QueryChanges
name|queryChanges
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
name|queryWithMoreChanges
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
name|queryWithNoMoreChanges
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
operator|)
name|queryChanges
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// _moreChanges is set on the first response, but not on the second.
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_moreChanges
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertNoChangeHasMoreChangesSet
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Run the capped query second
name|QueryChanges
name|queryChanges2
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges2
operator|.
name|addQuery
argument_list|(
name|queryWithNoMoreChanges
argument_list|)
expr_stmt|;
name|queryChanges2
operator|.
name|addQuery
argument_list|(
name|queryWithMoreChanges
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|result2
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
operator|)
name|queryChanges2
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result2
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// _moreChanges is set on the second response, but not on the first.
name|assertNoChangeHasMoreChangesSet
argument_list|(
name|result2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result2
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_moreChanges
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"operator-alias.change.numberaliastest"
argument_list|,
name|value
operator|=
literal|"change"
argument_list|)
DECL|method|aliasQuery ()
specifier|public
name|void
name|aliasQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|cId1
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|cId2
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|int
name|numericId1
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId1
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
decl_stmt|;
name|int
name|numericId2
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId2
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
decl_stmt|;
name|QueryChanges
name|queryChanges
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"numberaliastest:12345"
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"numberaliastest:"
operator|+
name|numericId1
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"numberaliastest:"
operator|+
name|numericId2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
operator|)
name|queryChanges
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_number
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numericId1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_number
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numericId2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseClockStep
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|withPagedResults ()
specifier|public
name|void
name|withPagedResults
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create 4 visible changes.
name|createChange
argument_list|(
name|testRepo
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|createChange
argument_list|(
name|testRepo
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|int
name|changeId3
init|=
name|createChange
argument_list|(
name|testRepo
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|int
name|changeId4
init|=
name|createChange
argument_list|(
name|testRepo
argument_list|)
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Create hidden project.
name|Project
operator|.
name|NameKey
name|hiddenProject
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|projectOperations
operator|.
name|project
argument_list|(
name|hiddenProject
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|block
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/*"
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
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|hiddenRepo
init|=
name|cloneProject
argument_list|(
name|hiddenProject
argument_list|,
name|admin
argument_list|)
decl_stmt|;
comment|// Create 2 hidden changes.
name|createChange
argument_list|(
name|hiddenRepo
argument_list|)
expr_stmt|;
name|createChange
argument_list|(
name|hiddenRepo
argument_list|)
expr_stmt|;
comment|// Create a change query that matches all changes (visible and hidden changes).
comment|// The index returns the changes ordered by last updated timestamp:
comment|// hiddenChange2, hiddenChange1, change4, change3, change2, change1
name|QueryChanges
name|queryChanges
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"branch:master"
argument_list|)
expr_stmt|;
comment|// Set a limit on the query so that we need to paginate over the results from the index.
name|queryChanges
operator|.
name|setLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// Execute the query and verify the results.
comment|// Since the limit is set to 2, at most 2 changes are returned to user, but the index query is
comment|// executed with limit 3 (+1 so that we can populate the _more_changes field on the last
comment|// result).
comment|// This means the index query with limit 3 returns these changes:
comment|// hiddenChange2, hiddenChange1, change4
comment|// The 2 hidden changes are filtered out because they are not visible to the caller.
comment|// This means we have only one matching result (change4) but the limit (3) is not exhausted
comment|// yet. Hence the next page is loaded from the index (startIndex is 3 to skip the results
comment|// that we already processed, limit is again 3). The results for the next page are:
comment|// change3, change2, change1
comment|// change2 and change1 are dropped because they are over the limit.
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
operator|)
name|queryChanges
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|changeId3
argument_list|,
name|changeId4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|usingOutOfRangeLabelValuesDoesNotCauseError ()
specifier|public
name|void
name|usingOutOfRangeLabelValuesDoesNotCauseError
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|operator
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"="
argument_list|,
literal|">"
argument_list|,
literal|">="
argument_list|,
literal|"<"
argument_list|,
literal|"<="
argument_list|)
control|)
block|{
name|QueryChanges
name|queryChanges
init|=
name|queryChangesProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"label:Code-Review"
operator|+
name|operator
operator|+
literal|"10"
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"label:Code-Review"
operator|+
name|operator
operator|+
literal|"-10"
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"Code-Review"
operator|+
name|operator
operator|+
literal|"10"
argument_list|)
expr_stmt|;
name|queryChanges
operator|.
name|addQuery
argument_list|(
literal|"Code-Review"
operator|+
name|operator
operator|+
literal|"-10"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|queryChanges
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|statusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SC_OK
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertNoChangeHasMoreChangesSet (List<ChangeInfo> results)
specifier|private
specifier|static
name|void
name|assertNoChangeHasMoreChangesSet
parameter_list|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|results
parameter_list|)
block|{
for|for
control|(
name|ChangeInfo
name|info
range|:
name|results
control|)
block|{
name|assertThat
argument_list|(
name|info
operator|.
name|_moreChanges
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


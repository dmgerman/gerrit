begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|extensions
operator|.
name|client
operator|.
name|ListChangesOption
operator|.
name|MESSAGES
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
name|concurrent
operator|.
name|TimeUnit
operator|.
name|HOURS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|client
operator|.
name|ChangeStatus
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
name|ResourceConflictException
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
name|server
operator|.
name|CurrentUser
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
name|change
operator|.
name|AbandonUtil
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
name|ChangeCleanupConfig
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|TestTimeUtil
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
DECL|class|AbandonIT
specifier|public
class|class
name|AbandonIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|abandonUtil
annotation|@
name|Inject
specifier|private
name|AbandonUtil
name|abandonUtil
decl_stmt|;
DECL|field|cleanupConfig
annotation|@
name|Inject
specifier|private
name|ChangeCleanupConfig
name|cleanupConfig
decl_stmt|;
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
DECL|method|abandon ()
specifier|public
name|void
name|abandon
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
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|abandon
argument_list|()
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|get
argument_list|(
name|changeId
argument_list|,
name|MESSAGES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"abandoned"
argument_list|)
expr_stmt|;
name|ResourceConflictException
name|thrown
init|=
name|assertThrows
argument_list|(
name|ResourceConflictException
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
name|changeId
argument_list|)
operator|.
name|abandon
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
name|contains
argument_list|(
literal|"change is abandoned"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|batchAbandon ()
specifier|public
name|void
name|batchAbandon
parameter_list|()
throws|throws
name|Exception
block|{
name|CurrentUser
name|user
init|=
name|atrScope
operator|.
name|get
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|a
init|=
name|createChange
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|b
init|=
name|createChange
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|list
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|a
operator|.
name|getChange
argument_list|()
argument_list|,
name|b
operator|.
name|getChange
argument_list|()
argument_list|)
decl_stmt|;
name|batchAbandon
operator|.
name|batchAbandon
argument_list|(
name|batchUpdateFactory
argument_list|,
name|a
operator|.
name|getChange
argument_list|()
operator|.
name|project
argument_list|()
argument_list|,
name|user
argument_list|,
name|list
argument_list|,
literal|"deadbeef"
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|get
argument_list|(
name|a
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|MESSAGES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"abandoned"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"deadbeef"
argument_list|)
expr_stmt|;
name|info
operator|=
name|get
argument_list|(
name|b
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|MESSAGES
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"abandoned"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"deadbeef"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|batchAbandonChangeProject ()
specifier|public
name|void
name|batchAbandonChangeProject
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|project1Name
init|=
name|name
argument_list|(
literal|"Project1"
argument_list|)
decl_stmt|;
name|String
name|project2Name
init|=
name|name
argument_list|(
literal|"Project2"
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|project1Name
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|project2Name
argument_list|)
expr_stmt|;
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|project1
init|=
name|cloneProject
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|project1Name
argument_list|)
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|project2
init|=
name|cloneProject
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|project2Name
argument_list|)
argument_list|)
decl_stmt|;
name|CurrentUser
name|user
init|=
name|atrScope
operator|.
name|get
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|a
init|=
name|createChange
argument_list|(
name|project1
argument_list|,
literal|"master"
argument_list|,
literal|"x"
argument_list|,
literal|"x"
argument_list|,
literal|"x"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|b
init|=
name|createChange
argument_list|(
name|project2
argument_list|,
literal|"master"
argument_list|,
literal|"x"
argument_list|,
literal|"x"
argument_list|,
literal|"x"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|list
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|a
operator|.
name|getChange
argument_list|()
argument_list|,
name|b
operator|.
name|getChange
argument_list|()
argument_list|)
decl_stmt|;
name|ResourceConflictException
name|thrown
init|=
name|assertThrows
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|batchAbandon
operator|.
name|batchAbandon
argument_list|(
name|batchUpdateFactory
argument_list|,
name|Project
operator|.
name|nameKey
argument_list|(
name|project1Name
argument_list|)
argument_list|,
name|user
argument_list|,
name|list
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
name|String
operator|.
name|format
argument_list|(
literal|"Project name \"%s\" doesn't match \"%s\""
argument_list|,
name|project2Name
argument_list|,
name|project1Name
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"changeCleanup.abandonAfter"
argument_list|,
name|value
operator|=
literal|"1w"
argument_list|)
DECL|method|abandonInactiveOpenChanges ()
specifier|public
name|void
name|abandonInactiveOpenChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
comment|// create 2 changes which will be abandoned ...
name|int
name|id1
init|=
name|createChange
argument_list|()
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
name|id2
init|=
name|createChange
argument_list|()
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
comment|// ... because they are older than 1 week
name|TestTimeUtil
operator|.
name|incrementClock
argument_list|(
literal|7
operator|*
literal|24
argument_list|,
name|HOURS
argument_list|)
expr_stmt|;
comment|// create 1 new change that will not be abandoned
name|ChangeData
name|cd
init|=
name|createChange
argument_list|()
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|int
name|id3
init|=
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|toChangeNumbers
argument_list|(
name|query
argument_list|(
literal|"is:open"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|,
name|id3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|query
argument_list|(
literal|"is:abandoned"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|abandonUtil
operator|.
name|abandonInactiveOpenChanges
argument_list|(
name|batchUpdateFactory
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toChangeNumbers
argument_list|(
name|query
argument_list|(
literal|"is:open"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|id3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toChangeNumbers
argument_list|(
name|query
argument_list|(
literal|"is:abandoned"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeCleanupConfigDefaultAbandonMessage ()
specifier|public
name|void
name|changeCleanupConfigDefaultAbandonMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|cleanupConfig
operator|.
name|getAbandonMessage
argument_list|()
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Auto-Abandoned due to inactivity, see "
operator|+
name|canonicalWebUrl
operator|.
name|get
argument_list|()
operator|+
literal|"Documentation/user-change-cleanup.html#auto-abandon"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"changeCleanup.abandonMessage"
argument_list|,
name|value
operator|=
literal|"XX ${URL} XX"
argument_list|)
DECL|method|changeCleanupConfigCustomAbandonMessageWithUrlReplacement ()
specifier|public
name|void
name|changeCleanupConfigCustomAbandonMessageWithUrlReplacement
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|cleanupConfig
operator|.
name|getAbandonMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"XX "
operator|+
name|canonicalWebUrl
operator|.
name|get
argument_list|()
operator|+
literal|"Documentation/user-change-cleanup.html#auto-abandon XX"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"changeCleanup.abandonMessage"
argument_list|,
name|value
operator|=
literal|"XX YYY XX"
argument_list|)
DECL|method|changeCleanupConfigCustomAbandonMessageWithoutUrlReplacement ()
specifier|public
name|void
name|changeCleanupConfigCustomAbandonMessageWithoutUrlReplacement
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|cleanupConfig
operator|.
name|getAbandonMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"XX YYY XX"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|abandonNotAllowedWithoutPermission ()
specifier|public
name|void
name|abandonNotAllowedWithoutPermission
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
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|abandon
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
name|contains
argument_list|(
literal|"abandon not permitted"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|abandonAndRestoreAllowedWithPermission ()
specifier|public
name|void
name|abandonAndRestoreAllowedWithPermission
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
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
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
name|ABANDON
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|abandon
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|restore
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|restore ()
specifier|public
name|void
name|restore
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
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|abandon
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|restore
argument_list|()
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|get
argument_list|(
name|changeId
argument_list|,
name|MESSAGES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info
operator|.
name|messages
argument_list|)
operator|.
name|message
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"restored"
argument_list|)
expr_stmt|;
name|ResourceConflictException
name|thrown
init|=
name|assertThrows
argument_list|(
name|ResourceConflictException
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
name|changeId
argument_list|)
operator|.
name|restore
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
name|contains
argument_list|(
literal|"change is new"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|restoreNotAllowedWithoutPermission ()
specifier|public
name|void
name|restoreNotAllowedWithoutPermission
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
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|abandon
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
name|assertThat
argument_list|(
name|info
argument_list|(
name|changeId
argument_list|)
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|ABANDONED
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|restore
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
name|contains
argument_list|(
literal|"restore not permitted"
argument_list|)
expr_stmt|;
block|}
DECL|method|toChangeNumbers (List<ChangeInfo> changes)
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|toChangeNumbers
parameter_list|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|changes
parameter_list|)
block|{
return|return
name|changes
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
return|;
block|}
block|}
end_class

end_unit


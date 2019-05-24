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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|Change
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
name|ChangeMessagesUtil
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
name|notedb
operator|.
name|ChangeUpdate
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
name|BatchUpdate
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
name|BatchUpdateOp
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
name|ChangeContext
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
DECL|class|PrivateChangeIT
specifier|public
class|class
name|PrivateChangeIT
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
DECL|method|setPrivateByOwner ()
specifier|public
name|void
name|setPrivateByOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|userRepo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|userRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
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
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
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
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
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
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Set private"
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
name|tag
argument_list|)
operator|.
name|contains
argument_list|(
name|ChangeMessagesUtil
operator|.
name|TAG_SET_PRIVATE
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
name|setPrivate
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|info
operator|=
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
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Unset private"
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
name|tag
argument_list|)
operator|.
name|contains
argument_list|(
name|ChangeMessagesUtil
operator|.
name|TAG_UNSET_PRIVATE
argument_list|)
expr_stmt|;
name|String
name|msg
init|=
literal|"This is a security fix that must not be public."
decl_stmt|;
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
name|setPrivate
argument_list|(
literal|true
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|info
operator|=
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
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Set private\n\n"
operator|+
name|msg
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
name|tag
argument_list|)
operator|.
name|contains
argument_list|(
name|ChangeMessagesUtil
operator|.
name|TAG_SET_PRIVATE
argument_list|)
expr_stmt|;
name|msg
operator|=
literal|"After this security fix has been released we can make it public now."
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
name|setPrivate
argument_list|(
literal|false
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|info
operator|=
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
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Unset private\n\n"
operator|+
name|msg
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
name|tag
argument_list|)
operator|.
name|contains
argument_list|(
name|ChangeMessagesUtil
operator|.
name|TAG_UNSET_PRIVATE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotSetMergedChangePrivate ()
specifier|public
name|void
name|cannotSetMergedChangePrivate
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|approve
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|merge
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
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
name|setPrivate
argument_list|(
literal|true
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
literal|"cannot set merged change to private"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotSetAbandonedChangePrivate ()
specifier|public
name|void
name|cannotSetAbandonedChangePrivate
parameter_list|()
throws|throws
name|Exception
block|{
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
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
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
name|setPrivate
argument_list|(
literal|true
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
literal|"cannot set abandoned change to private"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|administratorCanSetUserChangePrivate ()
specifier|public
name|void
name|administratorCanSetUserChangePrivate
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|userRepo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|userRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
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
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
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
name|ChangeInfo
name|info
init|=
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
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotSetOtherUsersChangePrivate ()
specifier|public
name|void
name|cannotSetOtherUsersChangePrivate
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
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
literal|"not allowed to mark private"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|accessPrivate ()
specifier|public
name|void
name|accessPrivate
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|userRepo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|userRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Owner can always access its private changes.
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Add admin as a reviewer.
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|addReviewer
argument_list|(
name|admin
operator|.
name|id
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// This change should be visible for admin as a reviewer.
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|admin
operator|.
name|id
argument_list|()
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Remove admin from reviewers.
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|reviewer
argument_list|(
name|admin
operator|.
name|id
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// This change should not be visible for admin anymore.
name|ResourceNotFoundException
name|thrown
init|=
name|assertThrows
argument_list|(
name|ResourceNotFoundException
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
name|result
operator|.
name|getChangeId
argument_list|()
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
literal|"Not found: "
operator|+
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|privateChangeOfOtherUserCanBeAccessedWithPermission ()
specifier|public
name|void
name|privateChangeOfOtherUserCanBeAccessedWithPermission
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
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
name|VIEW_PRIVATE_CHANGES
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|administratorCanUnmarkPrivateAfterMerging ()
specifier|public
name|void
name|administratorCanUnmarkPrivateAfterMerging
parameter_list|()
throws|throws
name|Exception
block|{
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
name|merge
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|markMergedChangePrivate
argument_list|(
name|Change
operator|.
name|id
argument_list|(
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
name|get
argument_list|()
operator|.
name|_number
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
name|changeId
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|false
argument_list|,
literal|null
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ownerCannotMarkPrivateAfterMerging ()
specifier|public
name|void
name|ownerCannotMarkPrivateAfterMerging
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|userRepo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|userRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|merge
argument_list|(
name|result
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
name|setPrivate
argument_list|(
literal|true
argument_list|,
literal|null
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
literal|"not allowed to mark private"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mergingPrivateChangePublishesIt ()
specifier|public
name|void
name|mergingPrivateChangePublishesIt
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|(
literal|true
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|approve
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|merge
argument_list|(
name|result
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
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
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ownerCanUnmarkPrivateAfterMerging ()
specifier|public
name|void
name|ownerCanUnmarkPrivateAfterMerging
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|userRepo
init|=
name|cloneProject
argument_list|(
name|project
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|userRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|result
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
name|changeId
argument_list|)
operator|.
name|addReviewer
argument_list|(
name|admin
operator|.
name|id
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|merge
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|markMergedChangePrivate
argument_list|(
name|Change
operator|.
name|id
argument_list|(
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
name|get
argument_list|()
operator|.
name|_number
argument_list|)
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
name|setPrivate
argument_list|(
literal|false
argument_list|,
literal|null
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
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mergingPrivateChangeThroughGitPublishesIt ()
specifier|public
name|void
name|mergingPrivateChangeThroughGitPublishesIt
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
name|setPrivate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
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
name|isPrivate
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|markMergedChangePrivate (Change.Id changeId)
specifier|private
name|void
name|markMergedChangePrivate
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|BatchUpdate
name|u
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|u
operator|.
name|addOp
argument_list|(
name|changeId
argument_list|,
operator|new
name|BatchUpdateOp
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
block|{
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setLastUpdatedOn
argument_list|(
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isPrivate
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


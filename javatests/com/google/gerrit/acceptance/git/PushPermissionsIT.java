begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
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
name|git
operator|.
name|testing
operator|.
name|PushResultSubject
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
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|GlobalCapability
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
name|client
operator|.
name|InheritableBoolean
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
name|ProjectState
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
name|ChangeInput
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
name|BooleanProjectConfig
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|project
operator|.
name|ProjectConfig
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
name|project
operator|.
name|testing
operator|.
name|Util
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
name|Consumer
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
name|PushCommand
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|BlobBasedConfig
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
name|lib
operator|.
name|Config
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
name|lib
operator|.
name|ObjectId
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
name|lib
operator|.
name|Repository
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
name|transport
operator|.
name|PushResult
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
name|transport
operator|.
name|RefSpec
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
name|transport
operator|.
name|TrackingRefUpdate
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
DECL|class|PushPermissionsIT
specifier|public
class|class
name|PushPermissionsIT
extends|extends
name|AbstractDaemonTest
block|{
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
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|allProjects
argument_list|)
init|)
block|{
name|ProjectConfig
name|cfg
init|=
name|u
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|getProject
argument_list|()
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_CHANGE_ID
argument_list|,
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
comment|// Remove push-related permissions, so they can be added back individually by test methods.
name|removeAllBranchPermissions
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|ADD_PATCH_SET
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|Permission
operator|.
name|DELETE
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|Permission
operator|.
name|PUSH_MERGE
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|)
expr_stmt|;
name|removeAllGlobalCapabilities
argument_list|(
name|cfg
argument_list|,
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
comment|// Include some auxiliary permissions.
name|Util
operator|.
name|allow
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|FORGE_AUTHOR
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|FORGE_COMMITTER
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|fastForwardUpdateDenied ()
specifier|public
name|void
name|fastForwardUpdateDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"prohibited by Gerrit: ref update access denied"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasMessages
argument_list|(
literal|"Branch refs/heads/master:"
argument_list|,
literal|"You are not allowed to perform this operation."
argument_list|,
literal|"To push into this reference you need 'Push' rights."
argument_list|,
literal|"User: admin"
argument_list|,
literal|"Please read the documentation and contact an administrator"
argument_list|,
literal|"if you feel the configuration is incorrect"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonFastForwardUpdateDenied ()
specifier|public
name|void
name|nonFastForwardUpdateDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|commit
init|=
name|testRepo
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|"+"
operator|+
name|commit
operator|.
name|name
argument_list|()
operator|+
literal|":refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"need 'Force Push' privilege."
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
comment|// TODO(dborowitz): Why does this not mention refs?
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDenied ()
specifier|public
name|void
name|deleteDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|":refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"cannot delete references"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasMessages
argument_list|(
literal|"Branch refs/heads/master:"
argument_list|,
literal|"You need 'Delete Reference' rights or 'Push' rights with the "
argument_list|,
literal|"'Force Push' flag set to delete references."
argument_list|,
literal|"User: admin"
argument_list|,
literal|"Please read the documentation and contact an administrator"
argument_list|,
literal|"if you feel the configuration is incorrect"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createDenied ()
specifier|public
name|void
name|createDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|"HEAD:refs/heads/newbranch"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/newbranch"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"prohibited by Gerrit: create not permitted for refs/heads/newbranch"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupRefsByMessage ()
specifier|public
name|void
name|groupRefsByMessage
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
name|TestRepository
argument_list|<
name|?
argument_list|>
name|tr
init|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|tr
operator|.
name|branch
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|tr
operator|.
name|branch
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|":refs/heads/foo"
argument_list|,
literal|":refs/heads/bar"
argument_list|,
literal|"HEAD:refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/foo"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"cannot delete references"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/bar"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"cannot delete references"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"prohibited by Gerrit: ref update access denied"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasMessages
argument_list|(
literal|"Branches refs/heads/foo, refs/heads/bar:"
argument_list|,
literal|"You need 'Delete Reference' rights or 'Push' rights with the "
argument_list|,
literal|"'Force Push' flag set to delete references."
argument_list|,
literal|"Branch refs/heads/master:"
argument_list|,
literal|"You are not allowed to perform this operation."
argument_list|,
literal|"To push into this reference you need 'Push' rights."
argument_list|,
literal|"User: admin"
argument_list|,
literal|"Please read the documentation and contact an administrator"
argument_list|,
literal|"if you feel the configuration is incorrect"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|readOnlyProjectRejectedBeforeTestingPermissions ()
specifier|public
name|void
name|readOnlyProjectRejectedBeforeTestingPermissions
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|setState
argument_list|(
name|ProjectState
operator|.
name|READ_ONLY
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|":refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"prohibited by Gerrit: project state does not permit write"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refsMetaConfigUpdateRequiresProjectOwner ()
specifier|public
name|void
name|refsMetaConfigUpdateRequiresProjectOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/meta/config"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|forceFetch
argument_list|(
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|ObjectId
name|commit
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
name|commit
operator|.
name|name
argument_list|()
operator|+
literal|":refs/meta/config"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/meta/config"
argument_list|)
comment|// ReceiveCommits theoretically has a different message when a WRITE_CONFIG check fails, but
comment|// it never gets there, since DefaultPermissionBackend special-cases refs/meta/config and
comment|// denies UPDATE if the user is not a project owner.
operator|.
name|isRejected
argument_list|(
literal|"prohibited by Gerrit: ref update access denied"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasMessages
argument_list|(
literal|"Branch refs/meta/config:"
argument_list|,
literal|"You are not allowed to perform this operation."
argument_list|,
literal|"Configuration changes can only be pushed by project owners"
argument_list|,
literal|"who also have 'Push' rights on refs/meta/config"
argument_list|,
literal|"User: admin"
argument_list|,
literal|"Please read the documentation and contact an administrator"
argument_list|,
literal|"if you feel the configuration is incorrect"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/*"
argument_list|,
name|Permission
operator|.
name|OWNER
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
comment|// Re-fetch refs/meta/config from the server because the grant changed it, and we want a
comment|// fast-forward.
name|forceFetch
argument_list|(
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|commit
operator|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|push
argument_list|(
name|commit
operator|.
name|name
argument_list|()
operator|+
literal|":refs/meta/config"
argument_list|)
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|isOk
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeDenied ()
specifier|public
name|void
name|createChangeDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|"HEAD:refs/for/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/for/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"create change not permitted for refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasMessages
argument_list|(
literal|"Branch refs/heads/master:"
argument_list|,
literal|"You need 'Push' rights to upload code review requests."
argument_list|,
literal|"Verify that you are pushing to the right branch."
argument_list|,
literal|"User: admin"
argument_list|,
literal|"Please read the documentation and contact an administrator"
argument_list|,
literal|"if you feel the configuration is incorrect"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateBySubmitDenied ()
specifier|public
name|void
name|updateBySubmitDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/for/refs/heads/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|ObjectId
name|commit
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|push
argument_list|(
literal|"HEAD:refs/for/master"
argument_list|)
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/for/master"
argument_list|)
operator|.
name|isOk
argument_list|()
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|commit
operator|.
name|name
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
name|PushResult
name|r
init|=
name|push
argument_list|(
literal|"HEAD:refs/for/master%submit"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/for/master%submit"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"update by submit not permitted for refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addPatchSetDenied ()
specifier|public
name|void
name|addPatchSetDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/for/refs/heads/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|ChangeInput
name|ci
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|ci
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|ci
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|ci
operator|.
name|subject
operator|=
literal|"A change"
expr_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|ci
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
argument_list|)
decl_stmt|;
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|ObjectId
name|ps1Id
init|=
name|forceFetch
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|id
argument_list|,
literal|1
argument_list|)
operator|.
name|toRefName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|ps2Id
init|=
name|testRepo
operator|.
name|amend
argument_list|(
name|ps1Id
argument_list|)
operator|.
name|add
argument_list|(
literal|"file"
argument_list|,
literal|"content"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
name|ps2Id
operator|.
name|name
argument_list|()
operator|+
literal|":refs/for/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/for/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"cannot add patch set to "
operator|+
name|id
operator|.
name|get
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipValidationDenied ()
specifier|public
name|void
name|skipValidationDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/heads/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
name|c
lambda|->
name|c
operator|.
name|setPushOptions
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"skip-validation"
argument_list|)
argument_list|)
argument_list|,
literal|"HEAD:refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"skip validation not permitted for refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|accessDatabaseForNoteDbDenied ()
specifier|public
name|void
name|accessDatabaseForNoteDbDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/heads/*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
name|c
lambda|->
name|c
operator|.
name|setPushOptions
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"notedb=allow"
argument_list|)
argument_list|)
argument_list|,
literal|"HEAD:refs/changes/34/1234/meta"
argument_list|)
decl_stmt|;
comment|// Same rejection message regardless of whether NoteDb is actually enabled.
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/changes/34/1234/meta"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"NoteDb update requires access database permission"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|administrateServerForUpdateParentDenied ()
specifier|public
name|void
name|administrateServerForUpdateParentDenied
parameter_list|()
throws|throws
name|Exception
block|{
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/meta/config"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/*"
argument_list|,
name|Permission
operator|.
name|OWNER
argument_list|,
literal|false
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|String
name|project2
init|=
name|name
argument_list|(
literal|"project2"
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|project2
argument_list|)
expr_stmt|;
name|ObjectId
name|oldId
init|=
name|forceFetch
argument_list|(
literal|"refs/meta/config"
argument_list|)
decl_stmt|;
name|Config
name|cfg
init|=
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|testRepo
operator|.
name|getRepository
argument_list|()
argument_list|,
name|oldId
argument_list|,
literal|"project.config"
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"access"
argument_list|,
literal|null
argument_list|,
literal|"inheritFrom"
argument_list|,
name|project2
argument_list|)
expr_stmt|;
name|ObjectId
name|newId
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|add
argument_list|(
literal|"project.config"
argument_list|,
name|cfg
operator|.
name|toText
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|PushResult
name|r
init|=
name|push
argument_list|(
name|newId
operator|.
name|name
argument_list|()
operator|+
literal|":refs/meta/config"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|onlyRef
argument_list|(
literal|"refs/meta/config"
argument_list|)
operator|.
name|isRejected
argument_list|(
literal|"invalid project configuration: only Gerrit admin can set parent"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasNoMessages
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasProcessed
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"refs"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|removeAllBranchPermissions (ProjectConfig cfg, String... permissions)
specifier|private
specifier|static
name|void
name|removeAllBranchPermissions
parameter_list|(
name|ProjectConfig
name|cfg
parameter_list|,
name|String
modifier|...
name|permissions
parameter_list|)
block|{
name|cfg
operator|.
name|getAccessSections
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"refs/heads/"
argument_list|)
operator|||
name|s
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"refs/for/"
argument_list|)
operator|||
name|s
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"refs/*"
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|s
lambda|->
name|Arrays
operator|.
name|stream
argument_list|(
name|permissions
argument_list|)
operator|.
name|forEach
argument_list|(
name|s
operator|::
name|removePermission
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|removeAllGlobalCapabilities (ProjectConfig cfg, String... capabilities)
specifier|private
specifier|static
name|void
name|removeAllGlobalCapabilities
parameter_list|(
name|ProjectConfig
name|cfg
parameter_list|,
name|String
modifier|...
name|capabilities
parameter_list|)
block|{
name|Arrays
operator|.
name|stream
argument_list|(
name|capabilities
argument_list|)
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|cfg
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|c
argument_list|,
literal|true
argument_list|)
operator|.
name|getRules
argument_list|()
operator|.
name|clear
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|push (String... refSpecs)
specifier|private
name|PushResult
name|push
parameter_list|(
name|String
modifier|...
name|refSpecs
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|push
argument_list|(
name|c
lambda|->
block|{}
argument_list|,
name|refSpecs
argument_list|)
return|;
block|}
DECL|method|push (Consumer<PushCommand> setUp, String... refSpecs)
specifier|private
name|PushResult
name|push
parameter_list|(
name|Consumer
argument_list|<
name|PushCommand
argument_list|>
name|setUp
parameter_list|,
name|String
modifier|...
name|refSpecs
parameter_list|)
throws|throws
name|Exception
block|{
name|PushCommand
name|cmd
init|=
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|refSpecs
argument_list|)
operator|.
name|map
argument_list|(
name|RefSpec
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|setUp
operator|.
name|accept
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|Iterable
argument_list|<
name|PushResult
argument_list|>
name|results
init|=
name|cmd
operator|.
name|call
argument_list|()
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"expected 1 PushResult"
argument_list|)
operator|.
name|that
argument_list|(
name|results
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|results
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|forceFetch (String ref)
specifier|private
name|ObjectId
name|forceFetch
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|TrackingRefUpdate
name|u
init|=
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRefSpecs
argument_list|(
literal|"+"
operator|+
name|ref
operator|+
literal|":"
operator|+
name|ref
argument_list|)
operator|.
name|call
argument_list|()
operator|.
name|getTrackingRefUpdate
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|u
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|u
operator|.
name|getResult
argument_list|()
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
break|break;
case|case
name|IO_FAILURE
case|:
case|case
name|LOCK_FAILURE
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|NO_CHANGE
case|:
case|case
name|REJECTED
case|:
case|case
name|REJECTED_CURRENT_BRANCH
case|:
case|case
name|REJECTED_MISSING_OBJECT
case|:
case|case
name|REJECTED_OTHER_REASON
case|:
case|case
name|RENAMED
case|:
default|default:
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"fetch failed to update local %s: %s"
argument_list|,
name|ref
argument_list|,
name|u
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|u
operator|.
name|getNewObjectId
argument_list|()
return|;
block|}
block|}
end_class

end_unit

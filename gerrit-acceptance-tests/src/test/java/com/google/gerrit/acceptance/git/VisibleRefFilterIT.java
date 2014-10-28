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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|base
operator|.
name|CharMatcher
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
name|base
operator|.
name|Splitter
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
name|Ordering
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
name|projects
operator|.
name|BranchInput
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
name|AccountGroup
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|edit
operator|.
name|ChangeEditModifier
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
name|git
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
name|notedb
operator|.
name|NotesMigration
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
name|Util
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
name|testutil
operator|.
name|ConfigSuite
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
name|RefUpdate
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
annotation|@
name|NoHttpd
DECL|class|VisibleRefFilterIT
specifier|public
class|class
name|VisibleRefFilterIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|noteDbWriteEnabled ()
specifier|public
specifier|static
name|Config
name|noteDbWriteEnabled
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"notedb"
argument_list|,
literal|"changes"
argument_list|,
literal|"write"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Inject
DECL|field|notesMigration
specifier|private
name|NotesMigration
name|notesMigration
decl_stmt|;
annotation|@
name|Inject
DECL|field|editModifier
specifier|private
name|ChangeEditModifier
name|editModifier
decl_stmt|;
DECL|field|admins
specifier|private
name|AccountGroup
operator|.
name|UUID
name|admins
decl_stmt|;
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
name|admins
operator|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
expr_stmt|;
name|setUpChanges
argument_list|()
expr_stmt|;
name|setUpPermissions
argument_list|()
expr_stmt|;
block|}
DECL|method|setUpPermissions ()
specifier|private
name|void
name|setUpPermissions
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectConfig
name|pc
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|allProjects
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
for|for
control|(
name|AccessSection
name|sec
range|:
name|pc
operator|.
name|getAccessSections
argument_list|()
control|)
block|{
name|sec
operator|.
name|removePermission
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
name|saveProjectConfig
argument_list|(
name|allProjects
argument_list|,
name|pc
argument_list|)
expr_stmt|;
block|}
DECL|method|setUpChanges ()
specifier|private
name|void
name|setUpChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
literal|"branch"
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|admins
argument_list|,
literal|"refs/for/refs/heads/*"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|mr
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/master%submit"
argument_list|)
decl_stmt|;
name|mr
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|br
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|git
argument_list|,
literal|"refs/for/branch%submit"
argument_list|)
decl_stmt|;
name|br
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
try|try
block|{
comment|// master-tag -> master
name|RefUpdate
name|mtu
init|=
name|repo
operator|.
name|updateRef
argument_list|(
literal|"refs/tags/master-tag"
argument_list|)
decl_stmt|;
name|mtu
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|mtu
operator|.
name|setNewObjectId
argument_list|(
name|repo
operator|.
name|getRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|NEW
argument_list|,
name|mtu
operator|.
name|update
argument_list|()
argument_list|)
expr_stmt|;
comment|// branch-tag -> branch
name|RefUpdate
name|btu
init|=
name|repo
operator|.
name|updateRef
argument_list|(
literal|"refs/tags/branch-tag"
argument_list|)
decl_stmt|;
name|btu
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|btu
operator|.
name|setNewObjectId
argument_list|(
name|repo
operator|.
name|getRef
argument_list|(
literal|"refs/heads/branch"
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|NEW
argument_list|,
name|btu
operator|.
name|update
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|allRefsVisibleNoRefsMetaConfig ()
specifier|public
name|void
name|allRefsVisibleNoRefsMetaConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|READ
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
name|READ
argument_list|,
name|admins
argument_list|,
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|Util
operator|.
name|doNotInherit
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
literal|"HEAD"
argument_list|,
literal|"refs/changes/01/1/1"
argument_list|,
literal|"refs/changes/01/1/meta"
argument_list|,
literal|"refs/changes/02/2/1"
argument_list|,
literal|"refs/changes/02/2/meta"
argument_list|,
literal|"refs/heads/branch"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"refs/tags/branch-tag"
argument_list|,
literal|"refs/tags/master-tag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allRefsVisibleWithRefsMetaConfig ()
specifier|public
name|void
name|allRefsVisibleWithRefsMetaConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/meta/config"
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
literal|"HEAD"
argument_list|,
literal|"refs/changes/01/1/1"
argument_list|,
literal|"refs/changes/01/1/meta"
argument_list|,
literal|"refs/changes/02/2/1"
argument_list|,
literal|"refs/changes/02/2/meta"
argument_list|,
literal|"refs/heads/branch"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"refs/meta/config"
argument_list|,
literal|"refs/tags/branch-tag"
argument_list|,
literal|"refs/tags/master-tag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|subsetOfBranchesVisibleIncludingHead ()
specifier|public
name|void
name|subsetOfBranchesVisibleIncludingHead
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|deny
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch"
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
literal|"HEAD"
argument_list|,
literal|"refs/changes/01/1/1"
argument_list|,
literal|"refs/changes/01/1/meta"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"refs/tags/master-tag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|subsetOfBranchesVisibleNotIncludingHead ()
specifier|public
name|void
name|subsetOfBranchesVisibleNotIncludingHead
parameter_list|()
throws|throws
name|Exception
block|{
name|deny
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch"
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
literal|"refs/changes/02/2/1"
argument_list|,
literal|"refs/changes/02/2/meta"
argument_list|,
literal|"refs/heads/branch"
argument_list|,
literal|"refs/tags/branch-tag"
argument_list|,
comment|// master branch is not visible but master-tag is reachable from branch
comment|// (since PushOneCommit always bases changes on each other).
literal|"refs/tags/master-tag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|subsetOfBranchesVisibleWithEdit ()
specifier|public
name|void
name|subsetOfBranchesVisibleWithEdit
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|deny
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch"
argument_list|)
expr_stmt|;
name|Change
name|c1
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSet
name|ps1
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
comment|// Admin's edit is not visible.
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|editModifier
operator|.
name|createEdit
argument_list|(
name|c1
argument_list|,
name|ps1
argument_list|)
expr_stmt|;
comment|// User's edit is visible.
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|editModifier
operator|.
name|createEdit
argument_list|(
name|c1
argument_list|,
name|ps1
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
literal|"HEAD"
argument_list|,
literal|"refs/changes/01/1/1"
argument_list|,
literal|"refs/changes/01/1/meta"
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|"refs/tags/master-tag"
argument_list|,
literal|"refs/users/01/1000001/edit-1/1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|subsetOfRefsVisibleWithAccessDatabase ()
specifier|public
name|void
name|subsetOfRefsVisibleWithAccessDatabase
parameter_list|()
throws|throws
name|Exception
block|{
name|deny
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch"
argument_list|)
expr_stmt|;
name|allowGlobalCapability
argument_list|(
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|Change
name|c1
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSet
name|ps1
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|editModifier
operator|.
name|createEdit
argument_list|(
name|c1
argument_list|,
name|ps1
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|editModifier
operator|.
name|createEdit
argument_list|(
name|c1
argument_list|,
name|ps1
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
comment|// Change 1 is visible due to accessDatabase capability, even though
comment|// refs/heads/master is not.
literal|"refs/changes/01/1/1"
argument_list|,
literal|"refs/changes/01/1/meta"
argument_list|,
literal|"refs/changes/02/2/1"
argument_list|,
literal|"refs/changes/02/2/meta"
argument_list|,
literal|"refs/heads/branch"
argument_list|,
literal|"refs/tags/branch-tag"
argument_list|,
comment|// See comment in subsetOfBranchesVisibleNotIncludingHead.
literal|"refs/tags/master-tag"
argument_list|,
comment|// All edits are visible due to accessDatabase capability.
literal|"refs/users/00/1000000/edit-1/1"
argument_list|,
literal|"refs/users/01/1000001/edit-1/1"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert that refs seen by a non-admin user match expected.    *    * @param expected expected refs, in order. If notedb is disabled by the    *     configuration, any notedb refs (i.e. ending in "/meta") are removed    *     from the expected list before comparing to the actual results.    * @throws Exception    */
DECL|method|assertRefs (String... expected)
specifier|private
name|void
name|assertRefs
parameter_list|(
name|String
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|sshSession
operator|.
name|exec
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"gerrit ls-user-refs -p %s -u %s"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|user
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|sshSession
operator|.
name|getError
argument_list|()
argument_list|,
name|sshSession
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|filtered
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|expected
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|r
range|:
name|expected
control|)
block|{
if|if
condition|(
name|notesMigration
operator|.
name|writeChanges
argument_list|()
operator|||
operator|!
name|r
operator|.
name|endsWith
argument_list|(
name|RefNames
operator|.
name|META_SUFFIX
argument_list|)
condition|)
block|{
name|filtered
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
name|Splitter
name|s
init|=
name|Splitter
operator|.
name|on
argument_list|(
name|CharMatcher
operator|.
name|WHITESPACE
argument_list|)
operator|.
name|omitEmptyStrings
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|filtered
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|s
operator|.
name|split
argument_list|(
name|out
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


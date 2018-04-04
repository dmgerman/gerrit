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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|common
operator|.
name|data
operator|.
name|Permission
operator|.
name|READ
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
name|GroupReference
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|account
operator|.
name|AccountManager
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
name|AuthRequest
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
name|AllProjectsName
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
name|meta
operator|.
name|MetaDataUpdate
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
name|project
operator|.
name|CommitsCollection
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
name|InMemoryRepositoryManager
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
name|InMemoryTestEnvironment
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
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
name|Rule
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
comment|/** Unit tests for {@link CommitsCollection}. */
end_comment

begin_class
DECL|class|CommitsCollectionTest
specifier|public
class|class
name|CommitsCollectionTest
block|{
DECL|field|testEnvironment
annotation|@
name|Rule
specifier|public
name|InMemoryTestEnvironment
name|testEnvironment
init|=
operator|new
name|InMemoryTestEnvironment
argument_list|()
decl_stmt|;
DECL|field|accountManager
annotation|@
name|Inject
specifier|private
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|InMemoryRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|projectCache
annotation|@
name|Inject
specifier|protected
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|metaDataUpdateFactory
annotation|@
name|Inject
specifier|protected
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|allProjects
annotation|@
name|Inject
specifier|protected
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|commits
annotation|@
name|Inject
specifier|private
name|CommitsCollection
name|commits
decl_stmt|;
DECL|field|repo
specifier|private
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|repo
decl_stmt|;
DECL|field|project
specifier|private
name|ProjectConfig
name|project
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
name|setUpPermissions
argument_list|()
expr_stmt|;
name|Account
operator|.
name|Id
name|user
init|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|AuthRequest
operator|.
name|forUser
argument_list|(
literal|"user"
argument_list|)
argument_list|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|testEnvironment
operator|.
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|name
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project"
argument_list|)
decl_stmt|;
name|InMemoryRepository
name|inMemoryRepo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|project
operator|=
operator|new
name|ProjectConfig
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|project
operator|.
name|load
argument_list|(
name|inMemoryRepo
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|inMemoryRepo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canReadCommitWhenAllRefsVisible ()
specifier|public
name|void
name|canReadCommitWhenAllRefsVisible
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"master"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canReadCommitIfTwoRefsVisible ()
specifier|public
name|void
name|canReadCommitIfTwoRefsVisible
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch1"
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch2"
argument_list|)
expr_stmt|;
name|ObjectId
name|id1
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ObjectId
name|id2
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch2"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canReadCommitIfRefVisible ()
specifier|public
name|void
name|canReadCommitIfRefVisible
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch1"
argument_list|)
expr_stmt|;
name|deny
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch2"
argument_list|)
expr_stmt|;
name|ObjectId
name|id1
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ObjectId
name|id2
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch2"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canReadCommitIfReachableFromVisibleRef ()
specifier|public
name|void
name|canReadCommitIfReachableFromVisibleRef
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch1"
argument_list|)
expr_stmt|;
name|deny
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch2"
argument_list|)
expr_stmt|;
name|RevCommit
name|parent1
init|=
name|repo
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|parent1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|RevCommit
name|parent2
init|=
name|repo
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|repo
operator|.
name|branch
argument_list|(
literal|"branch2"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|parent2
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotReadAfterRollbackWithRestrictedRead ()
specifier|public
name|void
name|cannotReadAfterRollbackWithRestrictedRead
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/heads/branch1"
argument_list|)
expr_stmt|;
name|RevCommit
name|parent1
init|=
name|repo
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ObjectId
name|id1
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|parent1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|update
argument_list|(
name|parent1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canReadAfterRollbackWithAllRefsVisible ()
specifier|public
name|void
name|canReadAfterRollbackWithAllRefsVisible
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|project
argument_list|,
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|RevCommit
name|parent1
init|=
name|repo
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|ObjectId
name|id1
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|parent1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|readProjectState
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|repo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|Repository
name|r
init|=
name|repo
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|repo
operator|.
name|branch
argument_list|(
literal|"branch1"
argument_list|)
operator|.
name|update
argument_list|(
name|parent1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|commits
operator|.
name|canRead
argument_list|(
name|state
argument_list|,
name|r
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|id1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|readProjectState ()
specifier|private
name|ProjectState
name|readProjectState
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|projectCache
operator|.
name|get
argument_list|(
name|project
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|allow (ProjectConfig project, String permission, AccountGroup.UUID id, String ref)
specifier|protected
name|void
name|allow
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permission
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|Util
operator|.
name|allow
argument_list|(
name|project
argument_list|,
name|permission
argument_list|,
name|id
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
DECL|method|deny (ProjectConfig project, String permission, AccountGroup.UUID id, String ref)
specifier|protected
name|void
name|deny
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permission
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|Util
operator|.
name|deny
argument_list|(
name|project
argument_list|,
name|permission
argument_list|,
name|id
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
DECL|method|saveProjectConfig (ProjectConfig cfg)
specifier|protected
name|void
name|saveProjectConfig
parameter_list|(
name|ProjectConfig
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|cfg
operator|.
name|getName
argument_list|()
argument_list|)
init|)
block|{
name|cfg
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|cfg
operator|.
name|getProject
argument_list|()
argument_list|)
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
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|admins
init|=
name|getAdmins
argument_list|()
decl_stmt|;
comment|// Remove read permissions for all users besides admin, because by default
comment|// Anonymous user group has ALLOW READ permission in refs/*.
comment|// This method is idempotent, so is safe to call on every test setup.
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
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|admin
range|:
name|admins
control|)
block|{
name|allow
argument_list|(
name|pc
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|admin
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getAdmins ()
specifier|private
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getAdmins
parameter_list|()
block|{
name|Permission
name|adminPermission
init|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|)
operator|.
name|getPermission
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
decl_stmt|;
return|return
name|adminPermission
operator|.
name|getRules
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|PermissionRule
operator|::
name|getGroup
argument_list|)
operator|.
name|map
argument_list|(
name|GroupReference
operator|::
name|getUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|ImmutableList
operator|.
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|GitUtil
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
name|TestProjectInput
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
name|api
operator|.
name|projects
operator|.
name|ProjectInput
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
name|client
operator|.
name|SubmitType
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
name|ObjectLoader
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
name|RevObject
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
name|RevTree
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
DECL|class|ConfigChangeIT
specifier|public
class|class
name|ConfigChangeIT
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
name|project
argument_list|)
init|)
block|{
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|OWNER
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
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|REGISTERED_USERS
argument_list|,
literal|"refs/for/refs/meta/config"
argument_list|)
expr_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|REGISTERED_USERS
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|fetchRefsMetaConfig
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|cloneAs
operator|=
literal|"user"
argument_list|)
DECL|method|updateProjectConfig ()
specifier|public
name|void
name|updateProjectConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|testUpdateProjectConfig
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
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|revisions
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|cloneAs
operator|=
literal|"user"
argument_list|,
name|submitType
operator|=
name|SubmitType
operator|.
name|CHERRY_PICK
argument_list|)
DECL|method|updateProjectConfigWithCherryPick ()
specifier|public
name|void
name|updateProjectConfigWithCherryPick
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|testUpdateProjectConfig
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
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|revisions
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpdateProjectConfig ()
specifier|private
name|String
name|testUpdateProjectConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Config
name|cfg
init|=
name|readProjectConfig
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|String
name|desc
init|=
literal|"new project description"
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|,
name|desc
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createConfigChange
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|r
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
name|id
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
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
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
name|id
argument_list|)
operator|.
name|info
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
name|get
argument_list|()
operator|.
name|description
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|fetchRefsMetaConfig
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|readProjectConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"project"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|String
name|changeRev
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|currentRevision
decl_stmt|;
name|String
name|branchRev
init|=
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
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|revision
decl_stmt|;
name|assertThat
argument_list|(
name|changeRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|branchRev
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|cloneAs
operator|=
literal|"user"
argument_list|)
DECL|method|onlyAdminMayUpdateProjectParent ()
specifier|public
name|void
name|onlyAdminMayUpdateProjectParent
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|ProjectInput
name|parent
init|=
operator|new
name|ProjectInput
argument_list|()
decl_stmt|;
name|parent
operator|.
name|name
operator|=
name|name
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|parent
operator|.
name|permissionsOnly
operator|=
literal|true
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|Config
name|cfg
init|=
name|readProjectConfig
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"access"
argument_list|,
literal|null
argument_list|,
literal|"inheritFrom"
argument_list|)
argument_list|)
operator|.
name|isAnyOf
argument_list|(
literal|null
argument_list|,
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|parent
operator|.
name|name
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createConfigChange
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|r
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
name|id
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
try|try
block|{
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected submit to fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|int
name|n
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|info
argument_list|()
operator|.
name|_number
decl_stmt|;
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
literal|"Failed to submit 1 change due to the following problems:\n"
operator|+
literal|"Change "
operator|+
name|n
operator|+
literal|": Change contains a project configuration that"
operator|+
literal|" changes the parent project.\n"
operator|+
literal|"The change must be submitted by a Gerrit administrator."
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
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
name|get
argument_list|()
operator|.
name|parent
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|fetchRefsMetaConfig
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|readProjectConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"access"
argument_list|,
literal|null
argument_list|,
literal|"inheritFrom"
argument_list|)
argument_list|)
operator|.
name|isAnyOf
argument_list|(
literal|null
argument_list|,
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
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
name|id
argument_list|)
operator|.
name|info
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
name|get
argument_list|()
operator|.
name|parent
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|parent
operator|.
name|name
argument_list|)
expr_stmt|;
name|fetchRefsMetaConfig
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|readProjectConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"access"
argument_list|,
literal|null
argument_list|,
literal|"inheritFrom"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|parent
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|rejectDoubleInheritance ()
specifier|public
name|void
name|rejectDoubleInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
comment|// Create separate projects to test the config
name|Project
operator|.
name|NameKey
name|parent
init|=
name|createProjectOverAPI
argument_list|(
literal|"projectToInheritFrom"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|child
init|=
name|createProjectOverAPI
argument_list|(
literal|"projectWithMalformedConfig"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|config
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|child
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|file
argument_list|(
literal|"project.config"
argument_list|)
operator|.
name|asString
argument_list|()
decl_stmt|;
comment|// Append and push malformed project config
name|String
name|pattern
init|=
literal|"[access]\n\tinheritFrom = "
operator|+
name|allProjects
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
decl_stmt|;
name|String
name|doubleInherit
init|=
name|pattern
operator|+
literal|"\tinheritFrom = "
operator|+
name|parent
operator|.
name|get
argument_list|()
operator|+
literal|"\n"
decl_stmt|;
name|config
operator|=
name|config
operator|.
name|replace
argument_list|(
name|pattern
argument_list|,
name|doubleInherit
argument_list|)
expr_stmt|;
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|childRepo
init|=
name|cloneProject
argument_list|(
name|child
argument_list|,
name|admin
argument_list|)
decl_stmt|;
comment|// Fetch meta ref
name|GitUtil
operator|.
name|fetch
argument_list|(
name|childRepo
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
operator|+
literal|":cfg"
argument_list|)
expr_stmt|;
name|childRepo
operator|.
name|reset
argument_list|(
literal|"cfg"
argument_list|)
expr_stmt|;
name|PushOneCommit
name|push
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
argument_list|,
name|childRepo
argument_list|,
literal|"Subject"
argument_list|,
literal|"project.config"
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|res
init|=
name|push
operator|.
name|to
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|res
operator|.
name|assertErrorStatus
argument_list|()
expr_stmt|;
name|res
operator|.
name|assertMessage
argument_list|(
literal|"cannot inherit from multiple projects"
argument_list|)
expr_stmt|;
block|}
DECL|method|fetchRefsMetaConfig ()
specifier|private
name|void
name|fetchRefsMetaConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"refs/meta/config:refs/meta/config"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|readProjectConfig ()
specifier|private
name|Config
name|readProjectConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|RevWalk
name|rw
init|=
name|testRepo
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|RevTree
name|tree
init|=
name|rw
operator|.
name|parseTree
argument_list|(
name|testRepo
operator|.
name|getRepository
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"HEAD"
argument_list|)
argument_list|)
decl_stmt|;
name|RevObject
name|obj
init|=
name|rw
operator|.
name|parseAny
argument_list|(
name|testRepo
operator|.
name|get
argument_list|(
name|tree
argument_list|,
literal|"project.config"
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectLoader
name|loader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|open
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|String
name|text
init|=
operator|new
name|String
argument_list|(
name|loader
operator|.
name|getCachedBytes
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|fromText
argument_list|(
name|text
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|createConfigChange (Config cfg)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createConfigChange
parameter_list|(
name|Config
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
literal|"Update project config"
argument_list|,
literal|"project.config"
argument_list|,
name|cfg
operator|.
name|toText
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/refs/meta/config"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|allowLabel
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
name|common
operator|.
name|data
operator|.
name|Permission
operator|.
name|forLabel
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
name|server
operator|.
name|project
operator|.
name|testing
operator|.
name|TestLabels
operator|.
name|label
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
name|project
operator|.
name|testing
operator|.
name|TestLabels
operator|.
name|value
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
name|LabelType
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
name|entities
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
name|entities
operator|.
name|LabelId
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
name|PatchSetApproval
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
name|GerritApi
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|IdentifiedUser
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
name|change
operator|.
name|LabelNormalizer
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
name|GitRepositoryManager
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
name|notedb
operator|.
name|ChangeNotes
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
name|ProjectCache
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
name|schema
operator|.
name|SchemaCreator
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
name|ThreadLocalRequestContext
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
name|gerrit
operator|.
name|testing
operator|.
name|InMemoryModule
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
name|Guice
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
name|Injector
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
name|After
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

begin_comment
comment|/** Unit tests for {@link LabelNormalizer}. */
end_comment

begin_class
DECL|class|LabelNormalizerTest
specifier|public
class|class
name|LabelNormalizerTest
block|{
DECL|field|accountManager
annotation|@
name|Inject
specifier|private
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|allProjects
annotation|@
name|Inject
specifier|private
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|userFactory
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|norm
annotation|@
name|Inject
specifier|private
name|LabelNormalizer
name|norm
decl_stmt|;
DECL|field|metaDataUpdateFactory
annotation|@
name|Inject
specifier|private
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|projectCache
annotation|@
name|Inject
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|schemaCreator
annotation|@
name|Inject
specifier|private
name|SchemaCreator
name|schemaCreator
decl_stmt|;
DECL|field|requestContext
annotation|@
name|Inject
specifier|protected
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|changeNotesFactory
annotation|@
name|Inject
specifier|private
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|projectConfigFactory
annotation|@
name|Inject
specifier|private
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
decl_stmt|;
DECL|field|gApi
annotation|@
name|Inject
specifier|private
name|GerritApi
name|gApi
decl_stmt|;
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
DECL|field|lifecycle
specifier|private
name|LifecycleManager
name|lifecycle
decl_stmt|;
DECL|field|userId
specifier|private
name|Account
operator|.
name|Id
name|userId
decl_stmt|;
DECL|field|user
specifier|private
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|notes
specifier|private
name|ChangeNotes
name|notes
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpInjector ()
specifier|public
name|void
name|setUpInjector
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|lifecycle
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|lifecycle
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|lifecycle
operator|.
name|start
argument_list|()
expr_stmt|;
name|schemaCreator
operator|.
name|create
argument_list|()
expr_stmt|;
name|userId
operator|=
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
expr_stmt|;
name|user
operator|=
name|userFactory
operator|.
name|create
argument_list|(
name|userId
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|setContext
argument_list|(
parameter_list|()
lambda|->
name|user
argument_list|)
expr_stmt|;
name|configureProject
argument_list|()
expr_stmt|;
name|setUpChange
argument_list|()
expr_stmt|;
block|}
DECL|method|configureProject ()
specifier|private
name|void
name|configureProject
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectConfig
name|pc
init|=
name|loadAllProjects
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
for|for
control|(
name|String
name|label
range|:
name|pc
operator|.
name|getLabelSections
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|sec
operator|.
name|removePermission
argument_list|(
name|forLabel
argument_list|(
name|label
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|LabelType
name|lt
init|=
name|label
argument_list|(
literal|"Verified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Verified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
decl_stmt|;
name|pc
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|lt
operator|.
name|getName
argument_list|()
argument_list|,
name|lt
argument_list|)
expr_stmt|;
name|save
argument_list|(
name|pc
argument_list|)
expr_stmt|;
block|}
DECL|method|setUpChange ()
specifier|private
name|void
name|setUpChange
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|project
operator|=
name|allProjects
operator|.
name|get
argument_list|()
expr_stmt|;
name|input
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|input
operator|.
name|newBranch
operator|=
literal|true
expr_stmt|;
name|input
operator|.
name|subject
operator|=
literal|"Test change"
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|notes
operator|=
name|changeNotesFactory
operator|.
name|createChecked
argument_list|(
name|allProjects
argument_list|,
name|Change
operator|.
name|id
argument_list|(
name|info
operator|.
name|_number
argument_list|)
argument_list|)
expr_stmt|;
name|change
operator|=
name|notes
operator|.
name|getChange
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|lifecycle
operator|!=
literal|null
condition|)
block|{
name|lifecycle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|requestContext
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noNormalizeByPermission ()
specifier|public
name|void
name|noNormalizeByPermission
parameter_list|()
throws|throws
name|Exception
block|{
name|projectOperations
operator|.
name|allProjectsForUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allowLabel
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|range
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|allowLabel
argument_list|(
literal|"Verified"
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|range
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|PatchSetApproval
name|cr
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Code-Review"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|v
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Verified"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Result
operator|.
name|create
argument_list|(
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|,
name|list
argument_list|()
argument_list|,
name|list
argument_list|()
argument_list|)
argument_list|,
name|norm
operator|.
name|normalize
argument_list|(
name|notes
argument_list|,
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|normalizeByType ()
specifier|public
name|void
name|normalizeByType
parameter_list|()
throws|throws
name|Exception
block|{
name|projectOperations
operator|.
name|allProjectsForUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allowLabel
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|range
argument_list|(
operator|-
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|allowLabel
argument_list|(
literal|"Verified"
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|range
argument_list|(
operator|-
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|PatchSetApproval
name|cr
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Code-Review"
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|v
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Verified"
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Result
operator|.
name|create
argument_list|(
name|list
argument_list|()
argument_list|,
name|list
argument_list|(
name|copy
argument_list|(
name|cr
argument_list|,
literal|2
argument_list|)
argument_list|,
name|copy
argument_list|(
name|v
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|,
name|list
argument_list|()
argument_list|)
argument_list|,
name|norm
operator|.
name|normalize
argument_list|(
name|notes
argument_list|,
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyPermissionRangeKeepsResult ()
specifier|public
name|void
name|emptyPermissionRangeKeepsResult
parameter_list|()
throws|throws
name|Exception
block|{
name|PatchSetApproval
name|cr
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Code-Review"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|v
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Verified"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Result
operator|.
name|create
argument_list|(
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|,
name|list
argument_list|()
argument_list|,
name|list
argument_list|()
argument_list|)
argument_list|,
name|norm
operator|.
name|normalize
argument_list|(
name|notes
argument_list|,
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|explicitZeroVoteOnNonEmptyRangeIsPresent ()
specifier|public
name|void
name|explicitZeroVoteOnNonEmptyRangeIsPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|projectOperations
operator|.
name|allProjectsForUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allowLabel
argument_list|(
literal|"Code-Review"
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|range
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|PatchSetApproval
name|cr
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Code-Review"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|PatchSetApproval
name|v
init|=
name|psa
argument_list|(
name|userId
argument_list|,
literal|"Verified"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Result
operator|.
name|create
argument_list|(
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|,
name|list
argument_list|()
argument_list|,
name|list
argument_list|()
argument_list|)
argument_list|,
name|norm
operator|.
name|normalize
argument_list|(
name|notes
argument_list|,
name|list
argument_list|(
name|cr
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|loadAllProjects ()
specifier|private
name|ProjectConfig
name|loadAllProjects
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
name|allProjects
argument_list|)
init|)
block|{
name|ProjectConfig
name|pc
init|=
name|projectConfigFactory
operator|.
name|create
argument_list|(
name|allProjects
argument_list|)
decl_stmt|;
name|pc
operator|.
name|load
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|pc
return|;
block|}
block|}
DECL|method|save (ProjectConfig pc)
specifier|private
name|void
name|save
parameter_list|(
name|ProjectConfig
name|pc
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
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|user
argument_list|)
init|)
block|{
name|pc
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|psa (Account.Id accountId, String label, int value)
specifier|private
name|PatchSetApproval
name|psa
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|label
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|PatchSetApproval
operator|.
name|builder
argument_list|()
operator|.
name|key
argument_list|(
name|PatchSetApproval
operator|.
name|key
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|accountId
argument_list|,
name|LabelId
operator|.
name|create
argument_list|(
name|label
argument_list|)
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|)
operator|.
name|granted
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|copy (PatchSetApproval src, int newValue)
specifier|private
name|PatchSetApproval
name|copy
parameter_list|(
name|PatchSetApproval
name|src
parameter_list|,
name|int
name|newValue
parameter_list|)
block|{
return|return
name|src
operator|.
name|toBuilder
argument_list|()
operator|.
name|value
argument_list|(
name|newValue
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|list (PatchSetApproval... psas)
specifier|private
specifier|static
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|list
parameter_list|(
name|PatchSetApproval
modifier|...
name|psas
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|psas
argument_list|)
return|;
block|}
block|}
end_class

end_unit


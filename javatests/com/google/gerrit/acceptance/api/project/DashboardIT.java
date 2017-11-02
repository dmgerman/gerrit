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
DECL|package|com.google.gerrit.acceptance.api.project
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
name|project
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|DashboardInfo
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
name|DashboardSectionInfo
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
name|ProjectApi
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|DashboardsCollection
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
annotation|@
name|NoHttpd
DECL|class|DashboardIT
specifier|public
class|class
name|DashboardIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
literal|"refs/meta/dashboards/*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultDashboardDoesNotExist ()
specifier|public
name|void
name|defaultDashboardDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|dashboardDoesNotExist ()
specifier|public
name|void
name|dashboardDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
literal|"my:dashboard"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getDashboard ()
specifier|public
name|void
name|getDashboard
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|createTestDashboard
argument_list|()
decl_stmt|;
name|DashboardInfo
name|result
init|=
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertDashboardInfo
argument_list|(
name|result
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getDashboardWithNoDescription ()
specifier|public
name|void
name|getDashboardWithNoDescription
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|newDashboardInfo
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|info
operator|.
name|description
operator|=
literal|null
expr_stmt|;
name|DashboardInfo
name|created
init|=
name|createDashboard
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|description
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|DashboardInfo
name|result
init|=
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|created
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|description
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getDashboardNonDefault ()
specifier|public
name|void
name|getDashboardNonDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|createTestDashboard
argument_list|(
literal|"my"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|DashboardInfo
name|result
init|=
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertDashboardInfo
argument_list|(
name|result
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listDashboards ()
specifier|public
name|void
name|listDashboards
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|dashboards
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|DashboardInfo
name|info1
init|=
name|createTestDashboard
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test1"
argument_list|)
decl_stmt|;
name|DashboardInfo
name|info2
init|=
name|createTestDashboard
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test2"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|dashboards
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|d
lambda|->
name|d
operator|.
name|id
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
name|info1
operator|.
name|id
argument_list|,
name|info2
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setDefaultDashboard ()
specifier|public
name|void
name|setDefaultDashboard
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|createTestDashboard
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|setDefault
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|info
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setDefaultDashboardByProject ()
specifier|public
name|void
name|setDefaultDashboardByProject
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|createTestDashboard
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|info
operator|.
name|id
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|removeDefaultDashboard
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replaceDefaultDashboard ()
specifier|public
name|void
name|replaceDefaultDashboard
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|d1
init|=
name|createTestDashboard
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test1"
argument_list|)
decl_stmt|;
name|DashboardInfo
name|d2
init|=
name|createTestDashboard
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test2"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|d1
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|d2
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d1
operator|.
name|id
argument_list|)
operator|.
name|setDefault
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d1
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d2
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|d1
operator|.
name|id
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d2
operator|.
name|id
argument_list|)
operator|.
name|setDefault
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|defaultDashboard
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|d2
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d1
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|d2
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|isDefault
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotGetDashboardWithInheritedForNonDefault ()
specifier|public
name|void
name|cannotGetDashboardWithInheritedForNonDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|DashboardInfo
name|info
init|=
name|createTestDashboard
argument_list|()
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"inherited flag can only be used with default"
argument_list|)
expr_stmt|;
name|project
argument_list|()
operator|.
name|dashboard
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDashboardInfo (DashboardInfo actual, DashboardInfo expected)
specifier|private
name|void
name|assertDashboardInfo
parameter_list|(
name|DashboardInfo
name|actual
parameter_list|,
name|DashboardInfo
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|actual
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|path
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|path
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|project
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|definingProject
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|description
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|description
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|title
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|title
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|foreach
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|foreach
argument_list|)
expr_stmt|;
if|if
condition|(
name|expected
operator|.
name|sections
operator|==
literal|null
condition|)
block|{
name|assertThat
argument_list|(
name|actual
operator|.
name|sections
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|actual
operator|.
name|sections
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|sections
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|dashboards ()
specifier|private
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|dashboards
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|project
argument_list|()
operator|.
name|dashboards
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|project ()
specifier|private
name|ProjectApi
name|project
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
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
return|;
block|}
DECL|method|newDashboardInfo (String ref, String path)
specifier|private
name|DashboardInfo
name|newDashboardInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|DashboardInfo
name|info
init|=
name|DashboardsCollection
operator|.
name|newDashboardInfo
argument_list|(
name|ref
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|info
operator|.
name|title
operator|=
literal|"Reviewer"
expr_stmt|;
name|info
operator|.
name|description
operator|=
literal|"Own review requests"
expr_stmt|;
name|info
operator|.
name|foreach
operator|=
literal|"owner:self"
expr_stmt|;
name|DashboardSectionInfo
name|section
init|=
operator|new
name|DashboardSectionInfo
argument_list|()
decl_stmt|;
name|section
operator|.
name|name
operator|=
literal|"Open"
expr_stmt|;
name|section
operator|.
name|query
operator|=
literal|"is:open"
expr_stmt|;
name|info
operator|.
name|sections
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|section
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|createTestDashboard ()
specifier|private
name|DashboardInfo
name|createTestDashboard
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createTestDashboard
argument_list|(
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
argument_list|,
literal|"test"
argument_list|)
return|;
block|}
DECL|method|createTestDashboard (String ref, String path)
specifier|private
name|DashboardInfo
name|createTestDashboard
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createDashboard
argument_list|(
name|newDashboardInfo
argument_list|(
name|ref
argument_list|,
name|path
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createDashboard (DashboardInfo info)
specifier|private
name|DashboardInfo
name|createDashboard
parameter_list|(
name|DashboardInfo
name|info
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|canonicalRef
init|=
name|DashboardsCollection
operator|.
name|normalizeDashboardRef
argument_list|(
name|info
operator|.
name|ref
argument_list|)
decl_stmt|;
try|try
block|{
name|project
argument_list|()
operator|.
name|branch
argument_list|(
name|canonicalRef
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
comment|// The branch already exists if this method has already been called once.
if|if
condition|(
operator|!
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"already exists"
argument_list|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
try|try
init|(
name|Repository
name|r
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
name|Repository
argument_list|>
operator|.
name|CommitBuilder
name|cb
init|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|r
argument_list|)
operator|.
name|branch
argument_list|(
name|canonicalRef
argument_list|)
operator|.
name|commit
argument_list|()
decl_stmt|;
name|StringBuilder
name|content
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"[dashboard]\n"
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|title
operator|!=
literal|null
condition|)
block|{
name|content
operator|.
name|append
argument_list|(
literal|"title = "
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|title
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|description
operator|!=
literal|null
condition|)
block|{
name|content
operator|.
name|append
argument_list|(
literal|"description = "
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|description
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|foreach
operator|!=
literal|null
condition|)
block|{
name|content
operator|.
name|append
argument_list|(
literal|"foreach = "
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|foreach
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|sections
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DashboardSectionInfo
name|section
range|:
name|info
operator|.
name|sections
control|)
block|{
name|content
operator|.
name|append
argument_list|(
literal|"[section \""
argument_list|)
operator|.
name|append
argument_list|(
name|section
operator|.
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"\"]\n"
argument_list|)
expr_stmt|;
name|content
operator|.
name|append
argument_list|(
literal|"query = "
argument_list|)
operator|.
name|append
argument_list|(
name|section
operator|.
name|query
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|cb
operator|.
name|add
argument_list|(
name|info
operator|.
name|path
argument_list|,
name|content
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|c
init|=
name|cb
operator|.
name|create
argument_list|()
decl_stmt|;
name|project
argument_list|()
operator|.
name|commit
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

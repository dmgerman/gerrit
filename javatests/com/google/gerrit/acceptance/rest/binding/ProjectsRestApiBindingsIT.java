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
DECL|package|com.google.gerrit.acceptance.rest.binding
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
name|binding
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
name|GitUtil
operator|.
name|assertPushOk
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
name|GitUtil
operator|.
name|pushHead
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
name|rest
operator|.
name|util
operator|.
name|RestCall
operator|.
name|Method
operator|.
name|GET
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
name|entities
operator|.
name|RefNames
operator|.
name|REFS_DASHBOARDS
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
name|restapi
operator|.
name|project
operator|.
name|DashboardsCollection
operator|.
name|DEFAULT_DASHBOARD_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_METHOD_NOT_ALLOWED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_NOT_FOUND
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
name|rest
operator|.
name|util
operator|.
name|RestApiCallHelper
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
name|rest
operator|.
name|util
operator|.
name|RestCall
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
name|LabelFunction
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
name|TagInput
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests for checking the bindings of the projects REST API.  *  *<p>These tests only verify that the project REST endpoints are correctly bound, they do no test  * the functionality of the project REST endpoints.  */
end_comment

begin_class
DECL|class|ProjectsRestApiBindingsIT
specifier|public
class|class
name|ProjectsRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|PROJECT_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|PROJECT_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/description"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/description"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/projects/%s/description"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/parent"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/parent"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/config"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/config"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/HEAD"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/HEAD"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/access"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/access"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/access:review"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/check.access"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/ban"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/statistics.git"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/index"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/gc"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/create.change"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/children"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/branches"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/branches:delete"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/branches/new-branch"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/labels"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/tags"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/tags:delete"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/tags/new-tag"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|builder
argument_list|(
name|GET
argument_list|,
literal|"/projects/%s/commits"
argument_list|)
comment|// GET /projects/<project>/branches/<branch>/commits is not implemented
operator|.
name|expectedResponseCode
argument_list|(
name|SC_NOT_FOUND
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/dashboards"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/labels/new-label"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Child project REST endpoints to be tested, each URL contains placeholders for the parent    * project identifier and the child project identifier.    */
DECL|field|CHILD_PROJECT_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|CHILD_PROJECT_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/children/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Branch REST endpoints to be tested, each URL contains placeholders for the project identifier    * and the branch identifier.    */
DECL|field|BRANCH_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|BRANCH_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/branches/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/branches/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/branches/%s/mergeable"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|builder
argument_list|(
name|GET
argument_list|,
literal|"/projects/%s/branches/%s/reflog"
argument_list|)
comment|// The tests use DfsRepository which does not support getting the reflog.
operator|.
name|expectedResponseCode
argument_list|(
name|SC_METHOD_NOT_ALLOWED
argument_list|)
operator|.
name|expectedMessage
argument_list|(
literal|"reflog not supported on"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|RestCall
operator|.
name|builder
argument_list|(
name|GET
argument_list|,
literal|"/projects/%s/branches/%s/files"
argument_list|)
comment|// GET /projects/<project>/branches/<branch>/files is not implemented
operator|.
name|expectedResponseCode
argument_list|(
name|SC_NOT_FOUND
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
comment|// Branch deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/projects/%s/branches/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Branch file REST endpoints to be tested, each URL contains placeholders for the project    * identifier, the branch identifier and the file identifier.    */
DECL|field|BRANCH_FILE_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|BRANCH_FILE_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/branches/%s/files/%s/content"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Dashboard REST endpoints to be tested, each URL contains placeholders for the project    * identifier and the dashboard identifier.    */
DECL|field|DASHBOARD_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|DASHBOARD_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/dashboards/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/dashboards/%s"
argument_list|)
argument_list|,
comment|// Dashboard deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/projects/%s/dashboards/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Tag REST endpoints to be tested, each URL contains placeholders for the project identifier and    * the tag identifier.    */
DECL|field|TAG_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|TAG_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/tags/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/%s/tags/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/projects/%s/tags/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Commit REST endpoints to be tested, each URL contains placeholders for the project identifier    * and the commit identifier.    */
DECL|field|COMMIT_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|COMMIT_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/commits/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/commits/%s/in"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/commits/%s/files"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/projects/%s/commits/%s/cherrypick"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Commit file REST endpoints to be tested, each URL contains placeholders for the project    * identifier, the commit identifier and the file identifier.    */
DECL|field|COMMIT_FILE_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|COMMIT_FILE_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/commits/%s/files/%s/content"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Label REST endpoints to be tested, each URL contains placeholders for the project identifier    * and the label name.    */
DECL|field|LABEL_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|LABEL_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/%s/labels/%s"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|FILENAME
specifier|private
specifier|static
specifier|final
name|String
name|FILENAME
init|=
literal|"test.txt"
decl_stmt|;
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
annotation|@
name|Test
DECL|method|projectEndpoints ()
specifier|public
name|void
name|projectEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|PROJECT_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|childProjectEndpoints ()
specifier|public
name|void
name|childProjectEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|childProject
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|parent
argument_list|(
name|project
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|CHILD_PROJECT_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|childProject
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|branchEndpoints ()
specifier|public
name|void
name|branchEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|BRANCH_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|branchFileEndpoints ()
specifier|public
name|void
name|branchFileEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|createAndSubmitChange
argument_list|(
name|FILENAME
argument_list|)
expr_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|BRANCH_FILE_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
name|FILENAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|dashboardEndpoints ()
specifier|public
name|void
name|dashboardEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|createDefaultDashboard
argument_list|()
expr_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|DASHBOARD_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|DEFAULT_DASHBOARD_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|tagEndpoints ()
specifier|public
name|void
name|tagEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tag
init|=
literal|"test-tag"
decl_stmt|;
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
name|tag
argument_list|(
name|tag
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|TagInput
argument_list|()
argument_list|)
expr_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|TAG_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|tag
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commitEndpoints ()
specifier|public
name|void
name|commitEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commit
init|=
name|createAndSubmitChange
argument_list|(
name|FILENAME
argument_list|)
decl_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|COMMIT_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|commit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commitFileEndpoints ()
specifier|public
name|void
name|commitFileEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commit
init|=
name|createAndSubmitChange
argument_list|(
name|FILENAME
argument_list|)
decl_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|COMMIT_FILE_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|commit
argument_list|,
name|FILENAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelEndpoints ()
specifier|public
name|void
name|labelEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|label
init|=
literal|"Foo-Review"
decl_stmt|;
name|configLabel
argument_list|(
name|label
argument_list|,
name|LabelFunction
operator|.
name|NO_OP
argument_list|)
expr_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|LABEL_ENDPOINTS
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
DECL|method|createAndSubmitChange (String filename)
specifier|private
name|String
name|createAndSubmitChange
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|Exception
block|{
name|RevCommit
name|c
init|=
name|testRepo
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"A change"
argument_list|)
operator|.
name|parent
argument_list|(
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|filename
argument_list|,
literal|"content"
argument_list|)
operator|.
name|insertChangeId
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|GitUtil
operator|.
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|String
name|r
init|=
literal|"refs/for/master"
decl_stmt|;
name|PushResult
name|pr
init|=
name|pushHead
argument_list|(
name|testRepo
argument_list|,
name|r
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertPushOk
argument_list|(
name|pr
argument_list|,
name|r
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
return|return
name|c
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|createDefaultDashboard ()
specifier|private
name|void
name|createDefaultDashboard
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dashboardRef
init|=
name|REFS_DASHBOARDS
operator|+
literal|"team"
decl_stmt|;
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
name|CREATE
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/meta/*"
argument_list|)
operator|.
name|group
argument_list|(
name|adminGroupUuid
argument_list|()
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
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
name|dashboardRef
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
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
init|;
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|tr
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|r
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
name|tr
operator|.
name|branch
argument_list|(
name|dashboardRef
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
name|content
operator|.
name|append
argument_list|(
literal|"title = "
argument_list|)
operator|.
name|append
argument_list|(
literal|"Open Changes"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|content
operator|.
name|append
argument_list|(
literal|"[section \""
argument_list|)
operator|.
name|append
argument_list|(
literal|"open"
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
literal|"is:open"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|cb
operator|.
name|add
argument_list|(
literal|"overview"
argument_list|,
name|content
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
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
name|setLocalDefaultDashboard
argument_list|(
name|dashboardRef
operator|+
literal|":overview"
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


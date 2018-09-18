begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
package|package
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
name|project
operator|.
name|BranchResource
operator|.
name|BRANCH_KIND
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
name|ChildProjectResource
operator|.
name|CHILD_PROJECT_KIND
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
name|CommitResource
operator|.
name|COMMIT_KIND
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
name|DashboardResource
operator|.
name|DASHBOARD_KIND
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
name|FileResource
operator|.
name|FILE_KIND
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
name|ProjectResource
operator|.
name|PROJECT_KIND
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
name|TagResource
operator|.
name|TAG_KIND
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
name|registration
operator|.
name|DynamicMap
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
name|registration
operator|.
name|DynamicSet
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
name|RestApiModule
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
name|GerritConfigListener
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
name|RefValidationHelper
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
name|CherryPickCommit
import|;
end_import

begin_class
DECL|class|Module
specifier|public
class|class
name|Module
extends|extends
name|RestApiModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|ProjectsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DashboardsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|PROJECT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|CHILD_PROJECT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|BRANCH_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|DASHBOARD_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|FILE_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|COMMIT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|TAG_KIND
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|GerritConfigListener
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SetParent
operator|.
name|class
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|PROJECT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|CreateProject
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|PutProject
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetProject
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"description"
argument_list|)
operator|.
name|to
argument_list|(
name|GetDescription
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"description"
argument_list|)
operator|.
name|to
argument_list|(
name|PutDescription
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"description"
argument_list|)
operator|.
name|to
argument_list|(
name|PutDescription
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"access"
argument_list|)
operator|.
name|to
argument_list|(
name|GetAccess
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"access"
argument_list|)
operator|.
name|to
argument_list|(
name|SetAccess
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"access:review"
argument_list|)
operator|.
name|to
argument_list|(
name|CreateAccessChange
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"check.access"
argument_list|)
operator|.
name|to
argument_list|(
name|CheckAccess
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"check.access"
argument_list|)
operator|.
name|to
argument_list|(
name|CheckAccessReadView
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"check"
argument_list|)
operator|.
name|to
argument_list|(
name|Check
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"parent"
argument_list|)
operator|.
name|to
argument_list|(
name|GetParent
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"parent"
argument_list|)
operator|.
name|to
argument_list|(
name|SetParent
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"children"
argument_list|)
operator|.
name|to
argument_list|(
name|ChildProjectsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHILD_PROJECT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetChildProject
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"HEAD"
argument_list|)
operator|.
name|to
argument_list|(
name|GetHead
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"HEAD"
argument_list|)
operator|.
name|to
argument_list|(
name|SetHead
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"ban"
argument_list|)
operator|.
name|to
argument_list|(
name|BanCommit
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"statistics.git"
argument_list|)
operator|.
name|to
argument_list|(
name|GetStatistics
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"gc"
argument_list|)
operator|.
name|to
argument_list|(
name|GarbageCollect
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"index"
argument_list|)
operator|.
name|to
argument_list|(
name|Index
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"index.changes"
argument_list|)
operator|.
name|to
argument_list|(
name|IndexChanges
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"branches"
argument_list|)
operator|.
name|to
argument_list|(
name|BranchesCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|BRANCH_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|CreateBranch
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|BRANCH_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|PutBranch
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|BRANCH_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetBranch
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|BRANCH_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteBranch
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"branches:delete"
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteBranches
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|BRANCH_KIND
argument_list|,
literal|"mergeable"
argument_list|)
operator|.
name|to
argument_list|(
name|CheckMergeability
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|RefValidationHelper
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|BRANCH_KIND
argument_list|,
literal|"reflog"
argument_list|)
operator|.
name|to
argument_list|(
name|GetReflog
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|BRANCH_KIND
argument_list|,
literal|"files"
argument_list|)
operator|.
name|to
argument_list|(
name|FilesCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|FILE_KIND
argument_list|,
literal|"content"
argument_list|)
operator|.
name|to
argument_list|(
name|GetContent
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"commits"
argument_list|)
operator|.
name|to
argument_list|(
name|CommitsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|COMMIT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetCommit
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|COMMIT_KIND
argument_list|,
literal|"in"
argument_list|)
operator|.
name|to
argument_list|(
name|CommitIncludedIn
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|COMMIT_KIND
argument_list|,
literal|"files"
argument_list|)
operator|.
name|to
argument_list|(
name|FilesInCommitCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"tags"
argument_list|)
operator|.
name|to
argument_list|(
name|TagsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|TAG_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|CreateTag
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|TAG_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetTag
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|TAG_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|PutTag
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|TAG_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteTag
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"tags:delete"
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteTags
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"dashboards"
argument_list|)
operator|.
name|to
argument_list|(
name|DashboardsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|DASHBOARD_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|CreateDashboard
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|DASHBOARD_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetDashboard
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|DASHBOARD_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|SetDashboard
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|DASHBOARD_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteDashboard
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"config"
argument_list|)
operator|.
name|to
argument_list|(
name|GetConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PROJECT_KIND
argument_list|,
literal|"config"
argument_list|)
operator|.
name|to
argument_list|(
name|PutConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|COMMIT_KIND
argument_list|,
literal|"cherrypick"
argument_list|)
operator|.
name|to
argument_list|(
name|CherryPickCommit
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ProjectNode
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


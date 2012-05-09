begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
package|;
end_package

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
name|ListBranchesResult
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
name|ProjectAccess
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
name|ProjectAdminService
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
name|ProjectDetail
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
name|Branch
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
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|VoidResult
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
name|ObjectId
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ProjectAdminServiceImpl
class|class
name|ProjectAdminServiceImpl
implements|implements
name|ProjectAdminService
block|{
DECL|field|addBranchFactory
specifier|private
specifier|final
name|AddBranch
operator|.
name|Factory
name|addBranchFactory
decl_stmt|;
DECL|field|changeProjectAccessFactory
specifier|private
specifier|final
name|ChangeProjectAccess
operator|.
name|Factory
name|changeProjectAccessFactory
decl_stmt|;
DECL|field|reviewProjectAccessFactory
specifier|private
specifier|final
name|ReviewProjectAccess
operator|.
name|Factory
name|reviewProjectAccessFactory
decl_stmt|;
DECL|field|changeProjectSettingsFactory
specifier|private
specifier|final
name|ChangeProjectSettings
operator|.
name|Factory
name|changeProjectSettingsFactory
decl_stmt|;
DECL|field|deleteBranchesFactory
specifier|private
specifier|final
name|DeleteBranches
operator|.
name|Factory
name|deleteBranchesFactory
decl_stmt|;
DECL|field|listBranchesFactory
specifier|private
specifier|final
name|ListBranches
operator|.
name|Factory
name|listBranchesFactory
decl_stmt|;
DECL|field|visibleProjectDetailsFactory
specifier|private
specifier|final
name|VisibleProjectDetails
operator|.
name|Factory
name|visibleProjectDetailsFactory
decl_stmt|;
DECL|field|projectAccessFactory
specifier|private
specifier|final
name|ProjectAccessFactory
operator|.
name|Factory
name|projectAccessFactory
decl_stmt|;
DECL|field|createProjectHandlerFactory
specifier|private
specifier|final
name|CreateProjectHandler
operator|.
name|Factory
name|createProjectHandlerFactory
decl_stmt|;
DECL|field|projectDetailFactory
specifier|private
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectAdminServiceImpl (final AddBranch.Factory addBranchFactory, final ChangeProjectAccess.Factory changeProjectAccessFactory, final ReviewProjectAccess.Factory reviewProjectAccessFactory, final ChangeProjectSettings.Factory changeProjectSettingsFactory, final DeleteBranches.Factory deleteBranchesFactory, final ListBranches.Factory listBranchesFactory, final VisibleProjectDetails.Factory visibleProjectDetailsFactory, final ProjectAccessFactory.Factory projectAccessFactory, final ProjectDetailFactory.Factory projectDetailFactory, final CreateProjectHandler.Factory createNewProjectFactory)
name|ProjectAdminServiceImpl
parameter_list|(
specifier|final
name|AddBranch
operator|.
name|Factory
name|addBranchFactory
parameter_list|,
specifier|final
name|ChangeProjectAccess
operator|.
name|Factory
name|changeProjectAccessFactory
parameter_list|,
specifier|final
name|ReviewProjectAccess
operator|.
name|Factory
name|reviewProjectAccessFactory
parameter_list|,
specifier|final
name|ChangeProjectSettings
operator|.
name|Factory
name|changeProjectSettingsFactory
parameter_list|,
specifier|final
name|DeleteBranches
operator|.
name|Factory
name|deleteBranchesFactory
parameter_list|,
specifier|final
name|ListBranches
operator|.
name|Factory
name|listBranchesFactory
parameter_list|,
specifier|final
name|VisibleProjectDetails
operator|.
name|Factory
name|visibleProjectDetailsFactory
parameter_list|,
specifier|final
name|ProjectAccessFactory
operator|.
name|Factory
name|projectAccessFactory
parameter_list|,
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
parameter_list|,
specifier|final
name|CreateProjectHandler
operator|.
name|Factory
name|createNewProjectFactory
parameter_list|)
block|{
name|this
operator|.
name|addBranchFactory
operator|=
name|addBranchFactory
expr_stmt|;
name|this
operator|.
name|changeProjectAccessFactory
operator|=
name|changeProjectAccessFactory
expr_stmt|;
name|this
operator|.
name|reviewProjectAccessFactory
operator|=
name|reviewProjectAccessFactory
expr_stmt|;
name|this
operator|.
name|changeProjectSettingsFactory
operator|=
name|changeProjectSettingsFactory
expr_stmt|;
name|this
operator|.
name|deleteBranchesFactory
operator|=
name|deleteBranchesFactory
expr_stmt|;
name|this
operator|.
name|listBranchesFactory
operator|=
name|listBranchesFactory
expr_stmt|;
name|this
operator|.
name|visibleProjectDetailsFactory
operator|=
name|visibleProjectDetailsFactory
expr_stmt|;
name|this
operator|.
name|projectAccessFactory
operator|=
name|projectAccessFactory
expr_stmt|;
name|this
operator|.
name|projectDetailFactory
operator|=
name|projectDetailFactory
expr_stmt|;
name|this
operator|.
name|createProjectHandlerFactory
operator|=
name|createNewProjectFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|visibleProjectDetails (final AsyncCallback<List<ProjectDetail>> callback)
specifier|public
name|void
name|visibleProjectDetails
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|visibleProjectDetailsFactory
operator|.
name|create
argument_list|()
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|projectDetail (final Project.NameKey projectName, final AsyncCallback<ProjectDetail> callback)
specifier|public
name|void
name|projectDetail
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|projectDetailFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|projectAccess (final Project.NameKey projectName, final AsyncCallback<ProjectAccess> callback)
specifier|public
name|void
name|projectAccess
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectAccess
argument_list|>
name|callback
parameter_list|)
block|{
name|projectAccessFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|changeProjectSettings (final Project update, final AsyncCallback<ProjectDetail> callback)
specifier|public
name|void
name|changeProjectSettings
parameter_list|(
specifier|final
name|Project
name|update
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|changeProjectSettingsFactory
operator|.
name|create
argument_list|(
name|update
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|changeProjectAccess (Project.NameKey projectName, String baseRevision, String msg, List<AccessSection> sections, AsyncCallback<ProjectAccess> cb)
specifier|public
name|void
name|changeProjectAccess
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|baseRevision
parameter_list|,
name|String
name|msg
parameter_list|,
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|,
name|AsyncCallback
argument_list|<
name|ProjectAccess
argument_list|>
name|cb
parameter_list|)
block|{
name|ObjectId
name|base
decl_stmt|;
if|if
condition|(
name|baseRevision
operator|!=
literal|null
operator|&&
operator|!
name|baseRevision
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|base
operator|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|baseRevision
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|base
operator|=
literal|null
expr_stmt|;
block|}
name|changeProjectAccessFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|base
argument_list|,
name|sections
argument_list|,
name|msg
argument_list|)
operator|.
name|to
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reviewProjectAccess (Project.NameKey projectName, String baseRevision, String msg, List<AccessSection> sections, AsyncCallback<Change.Id> cb)
specifier|public
name|void
name|reviewProjectAccess
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|baseRevision
parameter_list|,
name|String
name|msg
parameter_list|,
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
parameter_list|,
name|AsyncCallback
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|cb
parameter_list|)
block|{
name|ObjectId
name|base
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|baseRevision
argument_list|)
decl_stmt|;
name|reviewProjectAccessFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|base
argument_list|,
name|sections
argument_list|,
name|msg
argument_list|)
operator|.
name|to
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listBranches (final Project.NameKey projectName, final AsyncCallback<ListBranchesResult> callback)
specifier|public
name|void
name|listBranches
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ListBranchesResult
argument_list|>
name|callback
parameter_list|)
block|{
name|listBranchesFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteBranch (final Project.NameKey projectName, final Set<Branch.NameKey> toRemove, final AsyncCallback<Set<Branch.NameKey>> callback)
specifier|public
name|void
name|deleteBranch
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|toRemove
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|deleteBranchesFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|toRemove
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addBranch (final Project.NameKey projectName, final String branchName, final String startingRevision, final AsyncCallback<ListBranchesResult> callback)
specifier|public
name|void
name|addBranch
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|String
name|branchName
parameter_list|,
specifier|final
name|String
name|startingRevision
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ListBranchesResult
argument_list|>
name|callback
parameter_list|)
block|{
name|addBranchFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|branchName
argument_list|,
name|startingRevision
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createNewProject (String projectName, String parentName, boolean emptyCommit, boolean permissionsOnly, AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|createNewProject
parameter_list|(
name|String
name|projectName
parameter_list|,
name|String
name|parentName
parameter_list|,
name|boolean
name|emptyCommit
parameter_list|,
name|boolean
name|permissionsOnly
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|createProjectHandlerFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|parentName
argument_list|,
name|emptyCommit
argument_list|,
name|permissionsOnly
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


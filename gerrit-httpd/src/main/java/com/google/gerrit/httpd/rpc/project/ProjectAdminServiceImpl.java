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
name|ApprovalCategory
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
name|RefRight
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
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
name|inject
operator|.
name|Inject
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
DECL|field|visibleProjectsFactory
specifier|private
specifier|final
name|VisibleProjects
operator|.
name|Factory
name|visibleProjectsFactory
decl_stmt|;
DECL|field|projectDetailFactory
specifier|private
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
decl_stmt|;
DECL|field|addRefRightFactory
specifier|private
specifier|final
name|AddRefRight
operator|.
name|Factory
name|addRefRightFactory
decl_stmt|;
DECL|field|deleteRefRightsFactory
specifier|private
specifier|final
name|DeleteRefRights
operator|.
name|Factory
name|deleteRefRightsFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectAdminServiceImpl (final AddBranch.Factory addBranchFactory, final ChangeProjectSettings.Factory changeProjectSettingsFactory, final DeleteBranches.Factory deleteBranchesFactory, final ListBranches.Factory listBranchesFactory, final VisibleProjects.Factory visibleProjectsFactory, final ProjectDetailFactory.Factory projectDetailFactory, final AddRefRight.Factory addRefRightFactory, final DeleteRefRights.Factory deleteRefRightsFactory)
name|ProjectAdminServiceImpl
parameter_list|(
specifier|final
name|AddBranch
operator|.
name|Factory
name|addBranchFactory
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
name|VisibleProjects
operator|.
name|Factory
name|visibleProjectsFactory
parameter_list|,
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
parameter_list|,
specifier|final
name|AddRefRight
operator|.
name|Factory
name|addRefRightFactory
parameter_list|,
specifier|final
name|DeleteRefRights
operator|.
name|Factory
name|deleteRefRightsFactory
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
name|visibleProjectsFactory
operator|=
name|visibleProjectsFactory
expr_stmt|;
name|this
operator|.
name|projectDetailFactory
operator|=
name|projectDetailFactory
expr_stmt|;
name|this
operator|.
name|addRefRightFactory
operator|=
name|addRefRightFactory
expr_stmt|;
name|this
operator|.
name|deleteRefRightsFactory
operator|=
name|deleteRefRightsFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|visibleProjects (final AsyncCallback<List<Project>> callback)
specifier|public
name|void
name|visibleProjects
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|Project
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|visibleProjectsFactory
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
DECL|method|deleteRight (final Project.NameKey projectName, final Set<RefRight.Key> toRemove, final AsyncCallback<ProjectDetail> callback)
specifier|public
name|void
name|deleteRight
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
name|RefRight
operator|.
name|Key
argument_list|>
name|toRemove
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|deleteRefRightsFactory
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
DECL|method|addRight (final Project.NameKey projectName, final ApprovalCategory.Id categoryId, final String groupName, final String refPattern, final short min, final short max, final AsyncCallback<ProjectDetail> callback)
specifier|public
name|void
name|addRight
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|categoryId
parameter_list|,
specifier|final
name|String
name|groupName
parameter_list|,
specifier|final
name|String
name|refPattern
parameter_list|,
specifier|final
name|short
name|min
parameter_list|,
specifier|final
name|short
name|max
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|addRefRightFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|,
name|categoryId
argument_list|,
name|groupName
argument_list|,
name|refPattern
argument_list|,
name|min
argument_list|,
name|max
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
block|}
end_class

end_unit


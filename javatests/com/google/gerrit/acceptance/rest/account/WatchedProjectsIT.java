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
DECL|package|com.google.gerrit.acceptance.rest.account
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
name|account
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|extensions
operator|.
name|client
operator|.
name|ProjectWatchInfo
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
name|UnprocessableEntityException
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
DECL|class|WatchedProjectsIT
specifier|public
class|class
name|WatchedProjectsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|NEW_PROJECT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|NEW_PROJECT_NAME
init|=
literal|"newProjectAccess"
decl_stmt|;
annotation|@
name|Test
DECL|method|setAndGetWatchedProjects ()
specifier|public
name|void
name|setAndGetWatchedProjects
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName1
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|projectName2
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
operator|+
literal|"2"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName1
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|pwi
operator|=
operator|new
name|ProjectWatchInfo
argument_list|()
expr_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName2
expr_stmt|;
name|pwi
operator|.
name|filter
operator|=
literal|"branch:master"
expr_stmt|;
name|pwi
operator|.
name|notifySubmittedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewPatchSets
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|persistedWatchedProjects
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|containsAllIn
argument_list|(
name|projectsToWatch
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setAndDeleteWatchedProjects ()
specifier|public
name|void
name|setAndDeleteWatchedProjects
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName1
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|projectName2
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
operator|+
literal|"2"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName1
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|pwi
operator|=
operator|new
name|ProjectWatchInfo
argument_list|()
expr_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName2
expr_stmt|;
name|pwi
operator|.
name|filter
operator|=
literal|"branch:master"
expr_stmt|;
name|pwi
operator|.
name|notifySubmittedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewPatchSets
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
comment|// Persist watched projects
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|d
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|pwi
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|deleteWatchedProjects
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|projectsToWatch
operator|.
name|remove
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|persistedWatchedProjects
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|getWatchedProjects
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|containsAllIn
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setConflictingWatches ()
specifier|public
name|void
name|setConflictingWatches
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|pwi
operator|=
operator|new
name|ProjectWatchInfo
argument_list|()
expr_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|notifySubmittedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewPatchSets
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
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
literal|"duplicate entry for project "
operator|+
name|projectName
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setAndGetEmptyWatch ()
specifier|public
name|void
name|setAndGetEmptyWatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|createProject
argument_list|(
name|NEW_PROJECT_NAME
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|persistedWatchedProjects
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|getWatchedProjects
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|containsAllIn
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|watchNonExistingProject ()
specifier|public
name|void
name|watchNonExistingProject
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|NEW_PROJECT_NAME
operator|+
literal|"3"
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|UnprocessableEntityException
operator|.
name|class
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteNonExistingProjectWatch ()
specifier|public
name|void
name|deleteNonExistingProjectWatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Let another user watch a project
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
comment|// Try to delete a watched project using a different user
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|d
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|pwi
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|deleteWatchedProjects
argument_list|(
name|d
argument_list|)
expr_stmt|;
comment|// Check that trying to delete a non-existing watch doesn't fail
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|deleteWatchedProjects
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|modifyProjectWatchUsingOmittedValues ()
specifier|public
name|void
name|modifyProjectWatchUsingOmittedValues
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Let another user watch a project
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
comment|// Persist a defined state
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
comment|// Omit previously set value - will set it to false on the server
comment|// The response will not carry this field then as we omit sending
comment|// false values in JSON
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|null
expr_stmt|;
comment|// Perform update
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|watchedProjects
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|getWatchedProjects
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|watchedProjects
argument_list|)
operator|.
name|containsAllIn
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setAndDeleteWatchedProjectsWithDifferentFilter ()
specifier|public
name|void
name|setAndDeleteWatchedProjectsWithDifferentFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|projectName
init|=
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|projectsToWatch
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ProjectWatchInfo
name|pwi
init|=
operator|new
name|ProjectWatchInfo
argument_list|()
decl_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|filter
operator|=
literal|"branch:stable"
expr_stmt|;
name|pwi
operator|.
name|notifyAbandonedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyAllComments
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|pwi
operator|=
operator|new
name|ProjectWatchInfo
argument_list|()
expr_stmt|;
name|pwi
operator|.
name|project
operator|=
name|projectName
expr_stmt|;
name|pwi
operator|.
name|filter
operator|=
literal|"branch:master"
expr_stmt|;
name|pwi
operator|.
name|notifySubmittedChanges
operator|=
literal|true
expr_stmt|;
name|pwi
operator|.
name|notifyNewPatchSets
operator|=
literal|true
expr_stmt|;
name|projectsToWatch
operator|.
name|add
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
comment|// Persist watched projects
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|setWatchedProjects
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|d
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|pwi
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|deleteWatchedProjects
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|projectsToWatch
operator|.
name|remove
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProjectWatchInfo
argument_list|>
name|persistedWatchedProjects
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|getWatchedProjects
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|pwi
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|persistedWatchedProjects
argument_list|)
operator|.
name|containsAllIn
argument_list|(
name|projectsToWatch
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

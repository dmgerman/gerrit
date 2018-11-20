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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectCreation
operator|.
name|Builder
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
name|config
operator|.
name|ProjectOwnerGroupsProvider
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
name|CreateProjectArgs
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
name|ProjectCreator
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
name|Collections
import|;
end_import

begin_import
import|import
name|javax
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
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|RandomStringUtils
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
name|Constants
import|;
end_import

begin_class
DECL|class|ProjectOperationsImpl
specifier|public
class|class
name|ProjectOperationsImpl
implements|implements
name|ProjectOperations
block|{
DECL|field|projectCreator
specifier|private
specifier|final
name|ProjectCreator
name|projectCreator
decl_stmt|;
DECL|field|projectOwnerGroups
specifier|private
specifier|final
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectOperationsImpl ( ProjectOwnerGroupsProvider.Factory projectOwnerGroups, ProjectCreator projectCreator)
name|ProjectOperationsImpl
parameter_list|(
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
parameter_list|,
name|ProjectCreator
name|projectCreator
parameter_list|)
block|{
name|this
operator|.
name|projectCreator
operator|=
name|projectCreator
expr_stmt|;
name|this
operator|.
name|projectOwnerGroups
operator|=
name|projectOwnerGroups
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newProject ()
specifier|public
name|Builder
name|newProject
parameter_list|()
block|{
return|return
name|TestProjectCreation
operator|.
name|builder
argument_list|(
name|this
operator|::
name|createNewProject
argument_list|)
return|;
block|}
DECL|method|createNewProject (TestProjectCreation projectCreation)
specifier|private
name|Project
operator|.
name|NameKey
name|createNewProject
parameter_list|(
name|TestProjectCreation
name|projectCreation
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|projectCreation
operator|.
name|name
argument_list|()
operator|.
name|orElse
argument_list|(
name|RandomStringUtils
operator|.
name|randomAlphabetic
argument_list|(
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|CreateProjectArgs
name|args
init|=
operator|new
name|CreateProjectArgs
argument_list|()
decl_stmt|;
name|args
operator|.
name|setProjectName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|args
operator|.
name|branch
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Constants
operator|.
name|R_HEADS
operator|+
name|Constants
operator|.
name|MASTER
argument_list|)
expr_stmt|;
name|args
operator|.
name|createEmptyCommit
operator|=
name|projectCreation
operator|.
name|createEmptyCommit
argument_list|()
operator|.
name|orElse
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|projectCreation
operator|.
name|parent
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|p
lambda|->
name|args
operator|.
name|newParent
operator|=
name|p
argument_list|)
expr_stmt|;
name|args
operator|.
name|ownerIds
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|projectOwnerGroups
operator|.
name|create
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|projectCreation
operator|.
name|submitType
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|st
lambda|->
name|args
operator|.
name|submitType
operator|=
name|st
argument_list|)
expr_stmt|;
name|projectCreator
operator|.
name|createProject
argument_list|(
name|args
argument_list|)
expr_stmt|;
return|return
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

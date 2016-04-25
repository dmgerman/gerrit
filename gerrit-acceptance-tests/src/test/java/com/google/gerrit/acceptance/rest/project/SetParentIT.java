begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
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
name|RestResponse
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
name|AllProjectsNameProvider
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
name|SetParent
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
DECL|class|SetParentIT
specifier|public
class|class
name|SetParentIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|setParent_Forbidden ()
specifier|public
name|void
name|setParent_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|parent
init|=
name|createProject
argument_list|(
literal|"parent"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|parent
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setParent ()
specifier|public
name|void
name|setParent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|parent
init|=
name|createProject
argument_list|(
literal|"parent"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|parent
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|String
name|newParent
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|newParent
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// When the parent name is not explicitly set, it should be
comment|// set to "All-Projects".
name|r
operator|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|newParent
operator|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newParent
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|AllProjectsNameProvider
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setParentForAllProjects_Conflict ()
specifier|public
name|void
name|setParentForAllProjects_Conflict
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|allProjects
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertConflict
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setInvalidParent_Conflict ()
specifier|public
name|void
name|setInvalidParent_Conflict
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertConflict
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|Project
operator|.
name|NameKey
name|child
init|=
name|createProject
argument_list|(
literal|"child"
argument_list|,
name|project
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|child
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertConflict
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|String
name|grandchild
init|=
name|createProject
argument_list|(
literal|"grandchild"
argument_list|,
name|child
argument_list|,
literal|true
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
name|grandchild
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertConflict
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setNonExistingParent_UnprocessibleEntity ()
specifier|public
name|void
name|setNonExistingParent_UnprocessibleEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/parent"
argument_list|,
name|newParentInput
argument_list|(
literal|"non-existing"
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertUnprocessableEntity
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|newParentInput (String project)
name|SetParent
operator|.
name|Input
name|newParentInput
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|SetParent
operator|.
name|Input
name|in
init|=
operator|new
name|SetParent
operator|.
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|parent
operator|=
name|project
expr_stmt|;
return|return
name|in
return|;
block|}
block|}
end_class

end_unit


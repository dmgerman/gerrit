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
name|GcAssert
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
name|acceptance
operator|.
name|UseLocalDisk
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
name|inject
operator|.
name|Inject
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
DECL|class|GarbageCollectionIT
specifier|public
class|class
name|GarbageCollectionIT
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
DECL|field|gcAssert
annotation|@
name|Inject
specifier|private
name|GcAssert
name|gcAssert
decl_stmt|;
DECL|field|project2
specifier|private
name|Project
operator|.
name|NameKey
name|project2
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
name|project2
operator|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|gcNonExistingProject_NotFound ()
specifier|public
name|void
name|gcNonExistingProject_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|POST
argument_list|(
literal|"/projects/non-existing/gc"
argument_list|)
operator|.
name|assertNotFound
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|gcNotAllowed_Forbidden ()
specifier|public
name|void
name|gcNotAllowed_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/projects/"
operator|+
name|allProjects
operator|.
name|get
argument_list|()
operator|+
literal|"/gc"
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseLocalDisk
DECL|method|testGcOneProject ()
specifier|public
name|void
name|testGcOneProject
parameter_list|()
throws|throws
name|Exception
block|{
name|POST
argument_list|(
literal|"/projects/"
operator|+
name|allProjects
operator|.
name|get
argument_list|()
operator|+
literal|"/gc"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|gcAssert
operator|.
name|assertHasPackFile
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
name|gcAssert
operator|.
name|assertHasNoPackFile
argument_list|(
name|project
argument_list|,
name|project2
argument_list|)
expr_stmt|;
block|}
DECL|method|POST (String endPoint)
specifier|private
name|RestResponse
name|POST
parameter_list|(
name|String
name|endPoint
parameter_list|)
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|post
argument_list|(
name|endPoint
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit


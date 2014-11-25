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
DECL|package|com.google.gerrit.acceptance.rest.change
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
name|change
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|ChangeStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
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
DECL|class|CreateChangeIT
specifier|public
class|class
name|CreateChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|createEmptyChange_MissingBranch ()
specifier|public
name|void
name|createEmptyChange_MissingBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|ci
init|=
operator|new
name|ChangeInfo
argument_list|()
decl_stmt|;
name|ci
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|,
name|ci
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_BAD_REQUEST
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|getEntityContent
argument_list|()
operator|.
name|contains
argument_list|(
literal|"branch must be non-empty"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEmptyChange_MissingMessage ()
specifier|public
name|void
name|createEmptyChange_MissingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|ci
init|=
operator|new
name|ChangeInfo
argument_list|()
decl_stmt|;
name|ci
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|ci
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|,
name|ci
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_BAD_REQUEST
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|getEntityContent
argument_list|()
operator|.
name|contains
argument_list|(
literal|"commit message must be non-empty"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEmptyChange_InvalidStatus ()
specifier|public
name|void
name|createEmptyChange_InvalidStatus
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|ci
init|=
name|newChangeInfo
argument_list|(
name|ChangeStatus
operator|.
name|SUBMITTED
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|,
name|ci
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_BAD_REQUEST
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|getEntityContent
argument_list|()
operator|.
name|contains
argument_list|(
literal|"unsupported change status"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNewChange ()
specifier|public
name|void
name|createNewChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertChange
argument_list|(
name|newChangeInfo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createDraftChange ()
specifier|public
name|void
name|createDraftChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assertChange
argument_list|(
name|newChangeInfo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|newChangeInfo (ChangeStatus status)
specifier|private
name|ChangeInfo
name|newChangeInfo
parameter_list|(
name|ChangeStatus
name|status
parameter_list|)
block|{
name|ChangeInfo
name|in
init|=
operator|new
name|ChangeInfo
argument_list|()
decl_stmt|;
name|in
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|in
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|in
operator|.
name|subject
operator|=
literal|"Empty change"
expr_stmt|;
name|in
operator|.
name|topic
operator|=
literal|"support-gerrit-workflow-in-browser"
expr_stmt|;
name|in
operator|.
name|status
operator|=
name|status
expr_stmt|;
return|return
name|in
return|;
block|}
DECL|method|assertChange (ChangeInfo in)
specifier|private
name|void
name|assertChange
parameter_list|(
name|ChangeInfo
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_CREATED
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
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
name|ChangeInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ChangeInfo
name|out
init|=
name|get
argument_list|(
name|info
operator|.
name|changeId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|branch
argument_list|,
name|out
operator|.
name|branch
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|subject
argument_list|,
name|out
operator|.
name|subject
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|topic
argument_list|,
name|out
operator|.
name|topic
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|status
argument_list|,
name|out
operator|.
name|status
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


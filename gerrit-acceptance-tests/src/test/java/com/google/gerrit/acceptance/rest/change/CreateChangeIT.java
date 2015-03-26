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
name|common
operator|.
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|fail
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
name|Iterables
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
name|extensions
operator|.
name|client
operator|.
name|ChangeStatus
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
name|MethodNotAllowedException
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
name|testutil
operator|.
name|ConfigSuite
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
name|Config
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
DECL|class|CreateChangeIT
specifier|public
class|class
name|CreateChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|allowDraftsDisabled ()
specifier|public
specifier|static
name|Config
name|allowDraftsDisabled
parameter_list|()
block|{
return|return
name|allowDraftsDisabledConfig
argument_list|()
return|;
block|}
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
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"branch must be non-empty"
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
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"commit message must be non-empty"
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
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"unsupported change status"
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
name|assertCreateSucceeds
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
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertCreateSucceeds
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
annotation|@
name|Test
DECL|method|createDraftChangeNotAllowed ()
specifier|public
name|void
name|createDraftChangeNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|ChangeInfo
name|ci
init|=
name|newChangeInfo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
decl_stmt|;
name|assertCreateFails
argument_list|(
name|ci
argument_list|,
name|MethodNotAllowedException
operator|.
name|class
argument_list|,
literal|"draft workflow is disabled"
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
DECL|method|assertCreateSucceeds (ChangeInfo in)
specifier|private
name|void
name|assertCreateSucceeds
parameter_list|(
name|ChangeInfo
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeInfo
name|out
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|branch
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|branch
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|subject
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|subject
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|topic
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|topic
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|status
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|out
operator|.
name|revisions
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Boolean
name|draft
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|out
operator|.
name|revisions
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|draft
decl_stmt|;
name|assertThat
argument_list|(
name|booleanToDraftStatus
argument_list|(
name|draft
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|status
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCreateFails (ChangeInfo in, Class<? extends RestApiException> errType, String errSubstring)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|ChangeInfo
name|in
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RestApiException
argument_list|>
name|errType
parameter_list|,
name|String
name|errSubstring
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected "
operator|+
name|errType
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestApiException
name|expected
parameter_list|)
block|{
name|assertThat
argument_list|(
name|expected
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|errType
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|errSubstring
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|booleanToDraftStatus (Boolean draft)
specifier|private
name|ChangeStatus
name|booleanToDraftStatus
parameter_list|(
name|Boolean
name|draft
parameter_list|)
block|{
if|if
condition|(
name|draft
operator|==
literal|null
condition|)
block|{
return|return
name|ChangeStatus
operator|.
name|NEW
return|;
block|}
return|return
name|draft
condition|?
name|ChangeStatus
operator|.
name|DRAFT
else|:
name|ChangeStatus
operator|.
name|NEW
return|;
block|}
block|}
end_class

end_unit


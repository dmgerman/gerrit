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
DECL|package|com.google.gerrit.acceptance.api.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
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
name|ConfigInfo
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
name|ConfigInput
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
name|DescriptionInput
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
name|ProjectInput
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
name|InheritableBoolean
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
name|ProjectState
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
name|SubmitType
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
name|AuthException
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
name|ResourceConflictException
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
name|RefNames
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|ProjectIT
specifier|public
class|class
name|ProjectIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|createProject ()
specifier|public
name|void
name|createProject
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|name
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|name
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|name
argument_list|)
expr_stmt|;
name|RevCommit
name|head
init|=
name|getRemoteHead
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
literal|null
argument_list|,
name|head
argument_list|)
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
literal|"refs/heads/master"
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProjectWithGitSuffix ()
specifier|public
name|void
name|createProjectWithGitSuffix
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|name
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|name
operator|+
literal|".git"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|name
argument_list|)
expr_stmt|;
name|RevCommit
name|head
init|=
name|getRemoteHead
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
literal|null
argument_list|,
name|head
argument_list|)
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
literal|"refs/heads/master"
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProjectWithInitialCommit ()
specifier|public
name|void
name|createProjectWithInitialCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|name
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|ProjectInput
name|input
init|=
operator|new
name|ProjectInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|input
operator|.
name|createEmptyCommit
operator|=
literal|true
expr_stmt|;
name|assertThat
argument_list|(
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|name
argument_list|)
expr_stmt|;
name|RevCommit
name|head
init|=
name|getRemoteHead
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
literal|null
argument_list|,
name|head
argument_list|)
expr_stmt|;
name|head
operator|=
name|getRemoteHead
argument_list|(
name|name
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|name
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|null
argument_list|,
name|head
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProjectWithMismatchedInput ()
specifier|public
name|void
name|createProjectWithMismatchedInput
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectInput
name|in
init|=
operator|new
name|ProjectInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|name
operator|=
name|name
argument_list|(
literal|"foo"
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
literal|"name must match input.name"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProjectNoNameInInput ()
specifier|public
name|void
name|createProjectNoNameInInput
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectInput
name|in
init|=
operator|new
name|ProjectInput
argument_list|()
decl_stmt|;
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
literal|"input.name is required"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProjectDuplicate ()
specifier|public
name|void
name|createProjectDuplicate
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectInput
name|in
init|=
operator|new
name|ProjectInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|name
operator|=
name|name
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Project already exists"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranch ()
specifier|public
name|void
name|createBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
literal|"refs/*"
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|ANONYMOUS_USERS
argument_list|)
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
literal|"foo"
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|descriptionChangeCausesRefUpdate ()
specifier|public
name|void
name|descriptionChangeCausesRefUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
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
name|description
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|DescriptionInput
name|in
init|=
operator|new
name|DescriptionInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|description
operator|=
literal|"new project description"
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
name|description
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
name|description
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|description
argument_list|)
expr_stmt|;
name|RevCommit
name|updatedHead
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|initialHead
argument_list|,
name|updatedHead
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|descriptionIsDeletedWhenNotSpecified ()
specifier|public
name|void
name|descriptionIsDeletedWhenNotSpecified
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
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
name|description
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|DescriptionInput
name|in
init|=
operator|new
name|DescriptionInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|description
operator|=
literal|"new project description"
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
name|description
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
name|description
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|in
operator|.
name|description
argument_list|)
expr_stmt|;
name|in
operator|.
name|description
operator|=
literal|null
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
name|description
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
name|description
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|configChangeCausesRefUpdate ()
specifier|public
name|void
name|configChangeCausesRefUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|ConfigInfo
name|info
init|=
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
name|config
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|submitType
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
argument_list|)
expr_stmt|;
name|ConfigInput
name|input
init|=
operator|new
name|ConfigInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|submitType
operator|=
name|SubmitType
operator|.
name|CHERRY_PICK
expr_stmt|;
name|info
operator|=
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
name|config
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|submitType
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SubmitType
operator|.
name|CHERRY_PICK
argument_list|)
expr_stmt|;
name|info
operator|=
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
name|config
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|submitType
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SubmitType
operator|.
name|CHERRY_PICK
argument_list|)
expr_stmt|;
name|RevCommit
name|updatedHead
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|eventRecorder
operator|.
name|assertRefUpdatedEvents
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|initialHead
argument_list|,
name|updatedHead
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setConfig ()
specifier|public
name|void
name|setConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigInput
name|input
init|=
name|createTestConfigInput
argument_list|()
decl_stmt|;
name|ConfigInfo
name|info
init|=
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
name|config
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|description
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|description
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useContributorAgreements
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|useContributorAgreements
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useContentMerge
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|useContentMerge
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useSignedOffBy
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|useSignedOffBy
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|createNewChangeForAllNotInTarget
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|requireChangeId
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|requireChangeId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|rejectImplicitMerges
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|rejectImplicitMerges
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|enableReviewerByEmail
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|enableReviewerByEmail
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|createNewChangeForAllNotInTarget
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|maxObjectSizeLimit
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|submitType
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|submitType
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|state
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|state
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setPartialConfig ()
specifier|public
name|void
name|setPartialConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigInput
name|input
init|=
name|createTestConfigInput
argument_list|()
decl_stmt|;
name|ConfigInfo
name|info
init|=
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
name|config
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|ConfigInput
name|partialInput
init|=
operator|new
name|ConfigInput
argument_list|()
decl_stmt|;
name|partialInput
operator|.
name|useContributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|FALSE
expr_stmt|;
name|info
operator|=
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
name|config
argument_list|(
name|partialInput
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|description
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useContributorAgreements
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|partialInput
operator|.
name|useContributorAgreements
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useContentMerge
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|useContentMerge
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|useSignedOffBy
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|useSignedOffBy
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|createNewChangeForAllNotInTarget
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|requireChangeId
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|requireChangeId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|rejectImplicitMerges
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|rejectImplicitMerges
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|enableReviewerByEmail
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|enableReviewerByEmail
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|createNewChangeForAllNotInTarget
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|maxObjectSizeLimit
operator|.
name|configuredValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|submitType
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|submitType
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|state
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|state
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonOwnerCannotSetConfig ()
specifier|public
name|void
name|nonOwnerCannotSetConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigInput
name|input
init|=
name|createTestConfigInput
argument_list|()
decl_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"write config not permitted"
argument_list|)
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
name|config
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestConfigInput ()
specifier|private
name|ConfigInput
name|createTestConfigInput
parameter_list|()
block|{
name|ConfigInput
name|input
init|=
operator|new
name|ConfigInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|description
operator|=
literal|"some description"
expr_stmt|;
name|input
operator|.
name|useContributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|useContentMerge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|useSignedOffBy
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|createNewChangeForAllNotInTarget
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|requireChangeId
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|rejectImplicitMerges
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|enableReviewerByEmail
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|createNewChangeForAllNotInTarget
operator|=
name|InheritableBoolean
operator|.
name|TRUE
expr_stmt|;
name|input
operator|.
name|maxObjectSizeLimit
operator|=
literal|"5m"
expr_stmt|;
name|input
operator|.
name|submitType
operator|=
name|SubmitType
operator|.
name|CHERRY_PICK
expr_stmt|;
name|input
operator|.
name|state
operator|=
name|ProjectState
operator|.
name|HIDDEN
expr_stmt|;
return|return
name|input
return|;
block|}
block|}
end_class

end_unit


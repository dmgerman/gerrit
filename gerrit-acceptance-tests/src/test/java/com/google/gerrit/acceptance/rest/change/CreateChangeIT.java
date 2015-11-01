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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|SIGNED_OFF_BY_TAG
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
name|client
operator|.
name|GeneralPreferencesInfo
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
name|ChangeInput
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
name|server
operator|.
name|config
operator|.
name|AnonymousCowardNameProvider
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
name|notedb
operator|.
name|ChangeNoteUtil
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
operator|.
name|TestTimeUtil
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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
name|Repository
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
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|BeforeClass
DECL|method|setTimeForTesting ()
specifier|public
specifier|static
name|void
name|setTimeForTesting
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|restoreTime ()
specifier|public
specifier|static
name|void
name|restoreTime
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
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
name|ChangeInput
name|ci
init|=
operator|new
name|ChangeInput
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
name|ChangeInput
name|ci
init|=
operator|new
name|ChangeInput
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
name|ChangeInput
name|ci
init|=
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
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
name|newChangeInput
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
DECL|method|createNewChangeSignedOffByFooter ()
specifier|public
name|void
name|createNewChangeSignedOffByFooter
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
name|setSignedOffByFooter
argument_list|()
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|message
init|=
name|info
operator|.
name|revisions
operator|.
name|get
argument_list|(
name|info
operator|.
name|currentRevision
argument_list|)
operator|.
name|commit
operator|.
name|message
decl_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|contains
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s Adminitrstaor %s"
argument_list|,
name|SIGNED_OFF_BY_TAG
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
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
name|newChangeInput
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
name|ChangeInput
name|ci
init|=
name|newChangeInput
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
annotation|@
name|Test
DECL|method|notedbCommit ()
specifier|public
name|void
name|notedbCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|enabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|assertCreateSucceeds
argument_list|(
name|newChangeInput
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openMetadataRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ChangeNoteUtil
operator|.
name|changeRefName
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|c
operator|.
name|_number
argument_list|)
argument_list|)
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Create change"
argument_list|)
expr_stmt|;
name|PersonIdent
name|expectedAuthor
init|=
name|ChangeNoteUtil
operator|.
name|newIdent
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|admin
operator|.
name|id
argument_list|)
operator|.
name|getAccount
argument_list|()
argument_list|,
name|c
operator|.
name|created
argument_list|,
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|AnonymousCowardNameProvider
operator|.
name|DEFAULT
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedAuthor
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|c
operator|.
name|created
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commit
operator|.
name|getParentCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|newChangeInput (ChangeStatus status)
specifier|private
name|ChangeInput
name|newChangeInput
parameter_list|(
name|ChangeStatus
name|status
parameter_list|)
block|{
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
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
DECL|method|assertCreateSucceeds (ChangeInput in)
specifier|private
name|ChangeInfo
name|assertCreateSucceeds
parameter_list|(
name|ChangeInput
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
name|assertThat
argument_list|(
name|out
operator|.
name|submitted
argument_list|)
operator|.
name|isNull
argument_list|()
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
return|return
name|out
return|;
block|}
DECL|method|assertCreateFails (ChangeInput in, Class<? extends RestApiException> errType, String errSubstring)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|ChangeInput
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
name|exception
operator|.
name|expect
argument_list|(
name|errType
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
name|errSubstring
argument_list|)
expr_stmt|;
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
comment|// TODO(davido): Expose setting of account preferences in the API
DECL|method|setSignedOffByFooter ()
specifier|private
name|void
name|setSignedOffByFooter
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/accounts/"
operator|+
name|admin
operator|.
name|email
operator|+
literal|"/preferences"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|GeneralPreferencesInfo
name|i
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
name|GeneralPreferencesInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|i
operator|.
name|signedOffBy
operator|=
literal|true
expr_stmt|;
name|r
operator|=
name|adminSession
operator|.
name|put
argument_list|(
literal|"/accounts/"
operator|+
name|admin
operator|.
name|email
operator|+
literal|"/preferences"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|GeneralPreferencesInfo
name|o
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
name|GeneralPreferencesInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o
operator|.
name|signedOffBy
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


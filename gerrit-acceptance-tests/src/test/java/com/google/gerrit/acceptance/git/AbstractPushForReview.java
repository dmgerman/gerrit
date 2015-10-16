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
DECL|package|com.google.gerrit.acceptance.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|git
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
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
name|MILLISECONDS
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|GitUtil
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
name|PushOneCommit
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
name|TestAccount
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
name|EditInfo
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
name|LabelInfo
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
name|notedb
operator|.
name|NotesMigration
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
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTimeUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTimeUtils
operator|.
name|MillisProvider
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
name|Before
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_class
DECL|class|AbstractPushForReview
specifier|public
specifier|abstract
class|class
name|AbstractPushForReview
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|noteDbEnabled ()
specifier|public
specifier|static
name|Config
name|noteDbEnabled
parameter_list|()
block|{
return|return
name|NotesMigration
operator|.
name|allEnabledConfig
argument_list|()
return|;
block|}
annotation|@
name|Inject
DECL|field|notesMigration
specifier|private
name|NotesMigration
name|notesMigration
decl_stmt|;
DECL|enum|Protocol
specifier|protected
enum|enum
name|Protocol
block|{
comment|// TODO(dborowitz): TEST.
DECL|enumConstant|SSH
DECL|enumConstant|HTTP
name|SSH
block|,
name|HTTP
block|}
DECL|field|sshUrl
specifier|private
name|String
name|sshUrl
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setTimeForTesting ()
specifier|public
specifier|static
name|void
name|setTimeForTesting
parameter_list|()
block|{
specifier|final
name|long
name|clockStepMs
init|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
decl_stmt|;
specifier|final
name|AtomicLong
name|clockMs
init|=
operator|new
name|AtomicLong
argument_list|(
operator|new
name|DateTime
argument_list|(
literal|2009
argument_list|,
literal|9
argument_list|,
literal|30
argument_list|,
literal|17
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|getMillis
argument_list|()
argument_list|)
decl_stmt|;
name|DateTimeUtils
operator|.
name|setCurrentMillisProvider
argument_list|(
operator|new
name|MillisProvider
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|long
name|getMillis
parameter_list|()
block|{
return|return
name|clockMs
operator|.
name|getAndAdd
argument_list|(
name|clockStepMs
argument_list|)
return|;
block|}
block|}
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
name|DateTimeUtils
operator|.
name|setCurrentMillisSystem
argument_list|()
expr_stmt|;
block|}
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
name|sshUrl
operator|=
name|sshSession
operator|.
name|getUrl
argument_list|()
expr_stmt|;
block|}
DECL|method|selectProtocol (Protocol p)
specifier|protected
name|void
name|selectProtocol
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
decl_stmt|;
switch|switch
condition|(
name|p
condition|)
block|{
case|case
name|SSH
case|:
name|url
operator|=
name|sshUrl
expr_stmt|;
break|break;
case|case
name|HTTP
case|:
name|url
operator|=
name|admin
operator|.
name|getHttpUrl
argument_list|(
name|server
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unexpected protocol: "
operator|+
name|p
argument_list|)
throw|;
block|}
name|testRepo
operator|=
name|GitUtil
operator|.
name|cloneProject
argument_list|(
name|project
argument_list|,
name|url
operator|+
literal|"/"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMaster ()
specifier|public
name|void
name|testPushForMaster
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithTopic ()
specifier|public
name|void
name|testPushForMasterWithTopic
parameter_list|()
throws|throws
name|Exception
block|{
comment|// specify topic in ref
name|String
name|topic
init|=
literal|"my/topic"
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|)
expr_stmt|;
comment|// specify topic as option
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master%topic="
operator|+
name|topic
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithCc ()
specifier|public
name|void
name|testPushForMasterWithCc
parameter_list|()
throws|throws
name|Exception
block|{
comment|// cc one user
name|String
name|topic
init|=
literal|"my/topic"
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%cc="
operator|+
name|user
operator|.
name|email
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|)
expr_stmt|;
comment|// cc several users
name|TestAccount
name|user2
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"another-user"
argument_list|,
literal|"another.user@example.com"
argument_list|,
literal|"Another User"
argument_list|)
decl_stmt|;
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%cc="
operator|+
name|admin
operator|.
name|email
operator|+
literal|",cc="
operator|+
name|user
operator|.
name|email
operator|+
literal|",cc="
operator|+
name|user2
operator|.
name|email
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|)
expr_stmt|;
comment|// cc non-existing user
name|String
name|nonExistingEmail
init|=
literal|"non.existing@example.com"
decl_stmt|;
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%cc="
operator|+
name|admin
operator|.
name|email
operator|+
literal|",cc="
operator|+
name|nonExistingEmail
operator|+
literal|",cc="
operator|+
name|user
operator|.
name|email
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"user \""
operator|+
name|nonExistingEmail
operator|+
literal|"\" not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithReviewer ()
specifier|public
name|void
name|testPushForMasterWithReviewer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add one reviewer
name|String
name|topic
init|=
literal|"my/topic"
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%r="
operator|+
name|user
operator|.
name|email
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|,
name|user
argument_list|)
expr_stmt|;
comment|// add several reviewers
name|TestAccount
name|user2
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"another-user"
argument_list|,
literal|"another.user@example.com"
argument_list|,
literal|"Another User"
argument_list|)
decl_stmt|;
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%r="
operator|+
name|admin
operator|.
name|email
operator|+
literal|",r="
operator|+
name|user
operator|.
name|email
operator|+
literal|",r="
operator|+
name|user2
operator|.
name|email
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
comment|// admin is the owner of the change and should not appear as reviewer
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|topic
argument_list|,
name|user
argument_list|,
name|user2
argument_list|)
expr_stmt|;
comment|// add non-existing user as reviewer
name|String
name|nonExistingEmail
init|=
literal|"non.existing@example.com"
decl_stmt|;
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master/"
operator|+
name|topic
operator|+
literal|"%r="
operator|+
name|admin
operator|.
name|email
operator|+
literal|",r="
operator|+
name|nonExistingEmail
operator|+
literal|",r="
operator|+
name|user
operator|.
name|email
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"user \""
operator|+
name|nonExistingEmail
operator|+
literal|"\" not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterAsDraft ()
specifier|public
name|void
name|testPushForMasterAsDraft
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create draft by pushing to 'refs/drafts/'
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/drafts/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// create draft by using 'draft' option
name|r
operator|=
name|pushTo
argument_list|(
literal|"refs/for/master%draft"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterAsEdit ()
specifier|public
name|void
name|testPushForMasterAsEdit
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|EditInfo
name|edit
init|=
name|getEdit
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|edit
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
comment|// specify edit as option
name|r
operator|=
name|amendChange
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|"refs/for/master%edit"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|edit
operator|=
name|getEdit
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|edit
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithApprovals ()
specifier|public
name|void
name|testPushForMasterWithApprovals
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/%l=Code-Review"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|ChangeInfo
name|ci
init|=
name|get
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|LabelInfo
name|cr
init|=
name|ci
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Administrator"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|value
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|ci
operator|.
name|messages
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 1: Code-Review+1."
argument_list|)
expr_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master/%l=Code-Review+2"
argument_list|)
expr_stmt|;
name|ci
operator|=
name|get
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|cr
operator|=
name|ci
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|ci
operator|.
name|messages
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 2: Code-Review+2."
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Administrator"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|value
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|push
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"c.txt"
argument_list|,
literal|"moreContent"
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master/%l=Code-Review+2"
argument_list|)
expr_stmt|;
name|ci
operator|=
name|get
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|ci
operator|.
name|messages
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 3."
argument_list|)
expr_stmt|;
block|}
comment|/**    * There was a bug that allowed a user with Forge Committer Identity access    * right to upload a commit and put *votes on behalf of another user* on it.    * This test checks that this is not possible, but that the votes that are    * specified on push are applied only on behalf of the uploader.    *    * This particular bug only occurred when there was more than one label    * defined. However to test that the votes that are specified on push are    * applied on behalf of the uploader a single label is sufficient.    */
annotation|@
name|Test
DECL|method|testPushForMasterWithApprovalsForgeCommitterButNoForgeVote ()
specifier|public
name|void
name|testPushForMasterWithApprovalsForgeCommitterButNoForgeVote
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a commit with "User" as author and committer
name|RevCommit
name|c
init|=
name|commitBuilder
argument_list|()
operator|.
name|author
argument_list|(
name|user
operator|.
name|getIdent
argument_list|()
argument_list|)
operator|.
name|committer
argument_list|(
name|user
operator|.
name|getIdent
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|,
name|PushOneCommit
operator|.
name|FILE_CONTENT
argument_list|)
operator|.
name|message
argument_list|(
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// Push this commit as "Administrator" (requires Forge Committer Identity)
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master/%l=Code-Review+1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Expected Code-Review votes:
comment|// 1. 0 from User (committer):
comment|//    When the committer is forged, the committer is automatically added as
comment|//    reviewer, hence we expect a dummy 0 vote for the committer.
comment|// 2. +1 from Administrator (uploader):
comment|//    On push Code-Review+1 was specified, hence we expect a +1 vote from
comment|//    the uploader.
name|ChangeInfo
name|ci
init|=
name|get
argument_list|(
name|GitUtil
operator|.
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|LabelInfo
name|cr
init|=
name|ci
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|int
name|indexAdmin
init|=
name|admin
operator|.
name|fullName
operator|.
name|equals
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|)
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|int
name|indexUser
init|=
name|indexAdmin
operator|==
literal|0
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
name|indexAdmin
argument_list|)
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|fullName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
name|indexAdmin
argument_list|)
operator|.
name|value
operator|.
name|intValue
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
name|indexUser
argument_list|)
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|user
operator|.
name|fullName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cr
operator|.
name|all
operator|.
name|get
argument_list|(
name|indexUser
argument_list|)
operator|.
name|value
operator|.
name|intValue
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|ci
operator|.
name|messages
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 1: Code-Review+1."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushNewPatchsetToRefsChanges ()
specifier|public
name|void
name|testPushNewPatchsetToRefsChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/changes/"
operator|+
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithApprovals_MissingLabel ()
specifier|public
name|void
name|testPushForMasterWithApprovals_MissingLabel
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/%l=Verify"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"label \"Verify\" is not a configured label"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithApprovals_ValueOutOfRange ()
specifier|public
name|void
name|testPushForMasterWithApprovals_ValueOutOfRange
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master/%l=Code-Review-3"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"label \"Code-Review\": -3 is not a valid value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForNonExistingBranch ()
specifier|public
name|void
name|testPushForNonExistingBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|branchName
init|=
literal|"non-existing"
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/"
operator|+
name|branchName
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"branch "
operator|+
name|branchName
operator|+
literal|" not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithHashtags ()
specifier|public
name|void
name|testPushForMasterWithHashtags
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Hashtags currently only work when noteDB is enabled
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
comment|// specify a single hashtag as option
name|String
name|hashtag1
init|=
literal|"tag1"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|expected
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|hashtag1
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master%hashtag=#"
operator|+
name|hashtag1
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|getHashtags
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|hashtags
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
comment|// specify a single hashtag as option in new patch set
name|String
name|hashtag2
init|=
literal|"tag2"
decl_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master/%hashtag="
operator|+
name|hashtag2
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|expected
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|hashtag1
argument_list|,
name|hashtag2
argument_list|)
expr_stmt|;
name|hashtags
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|getHashtags
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hashtags
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithMultipleHashtags ()
specifier|public
name|void
name|testPushForMasterWithMultipleHashtags
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Hashtags currently only work when noteDB is enabled
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
comment|// specify multiple hashtags as options
name|String
name|hashtag1
init|=
literal|"tag1"
decl_stmt|;
name|String
name|hashtag2
init|=
literal|"tag2"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|expected
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|hashtag1
argument_list|,
name|hashtag2
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master%hashtag=#"
operator|+
name|hashtag1
operator|+
literal|",hashtag=##"
operator|+
name|hashtag2
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|r
operator|.
name|assertChange
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|getHashtags
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|hashtags
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
comment|// specify multiple hashtags as options in new patch set
name|String
name|hashtag3
init|=
literal|"tag3"
decl_stmt|;
name|String
name|hashtag4
init|=
literal|"tag4"
decl_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master%hashtag="
operator|+
name|hashtag3
operator|+
literal|",hashtag="
operator|+
name|hashtag4
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|expected
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|hashtag1
argument_list|,
name|hashtag2
argument_list|,
name|hashtag3
argument_list|,
name|hashtag4
argument_list|)
expr_stmt|;
name|hashtags
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|getHashtags
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hashtags
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithHashtagsNoteDbDisabled ()
specifier|public
name|void
name|testPushForMasterWithHashtagsNoteDbDisabled
parameter_list|()
throws|throws
name|Exception
block|{
comment|// push with hashtags should fail when noteDb is disabled
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
name|isFalse
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|pushTo
argument_list|(
literal|"refs/for/master%hashtag=tag1"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"cannot add hashtags; noteDb is disabled"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushCommitUsingSignedOffBy ()
specifier|public
name|void
name|testPushCommitUsingSignedOffBy
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|setUseSignedOffBy
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|blockForgeCommitter
argument_list|(
name|project
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|push
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"\n\nSigned-off-by: %s<%s>"
argument_list|,
name|admin
operator|.
name|fullName
argument_list|,
name|admin
operator|.
name|email
argument_list|)
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|)
expr_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|push
operator|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"anotherContent"
argument_list|)
expr_stmt|;
name|r
operator|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"not Signed-off-by author/committer/uploader in commit message footer"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


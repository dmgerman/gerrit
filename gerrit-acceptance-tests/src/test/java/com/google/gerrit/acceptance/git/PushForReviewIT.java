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
name|gerrit
operator|.
name|acceptance
operator|.
name|git
operator|.
name|GitUtil
operator|.
name|cloneProject
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
name|git
operator|.
name|GitUtil
operator|.
name|createProject
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
name|git
operator|.
name|GitUtil
operator|.
name|initSsh
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
name|AccountCreator
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
name|SshSession
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
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
name|api
operator|.
name|Git
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|PushForReviewIT
specifier|public
class|class
name|PushForReviewIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|enum|Protocol
specifier|private
enum|enum
name|Protocol
block|{
DECL|enumConstant|SSH
DECL|enumConstant|HTTP
name|SSH
block|,
name|HTTP
block|}
annotation|@
name|Inject
DECL|field|accounts
specifier|private
name|AccountCreator
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|field|reviewDbProvider
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|reviewDbProvider
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|git
specifier|private
name|Git
name|git
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|sshUrl
specifier|private
name|String
name|sshUrl
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
name|admin
operator|=
name|accounts
operator|.
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"admin@example.com"
argument_list|,
literal|"Administrator"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|initSsh
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|SshSession
name|sshSession
init|=
operator|new
name|SshSession
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|sshUrl
operator|=
name|sshSession
operator|.
name|getUrl
argument_list|()
expr_stmt|;
name|sshSession
operator|.
name|close
argument_list|()
expr_stmt|;
name|db
operator|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
DECL|method|selectProtocol (Protocol p)
specifier|private
name|void
name|selectProtocol
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
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
argument_list|()
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
name|git
operator|=
name|cloneProject
argument_list|(
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
name|After
DECL|method|cleanup ()
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMaster_HTTP ()
specifier|public
name|void
name|testPushForMaster_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMaster
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMaster_SSH ()
specifier|public
name|void
name|testPushForMaster_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMaster
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForMaster (Protocol p)
specifier|private
name|void
name|testPushForMaster
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
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
DECL|method|testPushForMasterWithTopic_HTTP ()
specifier|public
name|void
name|testPushForMasterWithTopic_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMasterWithTopic
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithTopic_SSH ()
specifier|public
name|void
name|testPushForMasterWithTopic_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMasterWithTopic
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForMasterWithTopic (Protocol p)
specifier|private
name|void
name|testPushForMasterWithTopic
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
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
DECL|method|testPushForMasterWithCc_HTTP ()
specifier|public
name|void
name|testPushForMasterWithCc_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|testPushForMasterWithCc
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithCc_SSH ()
specifier|public
name|void
name|testPushForMasterWithCc_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|testPushForMasterWithCc
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForMasterWithCc (Protocol p)
specifier|private
name|void
name|testPushForMasterWithCc
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
comment|// cc one user
name|TestAccount
name|user
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"User"
argument_list|)
decl_stmt|;
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
DECL|method|testPushForMasterWithReviewer_HTTP ()
specifier|public
name|void
name|testPushForMasterWithReviewer_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|testPushForMasterWithReviewer
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterWithReviewer_SSH ()
specifier|public
name|void
name|testPushForMasterWithReviewer_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|testPushForMasterWithReviewer
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForMasterWithReviewer (Protocol p)
specifier|private
name|void
name|testPushForMasterWithReviewer
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|JSchException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
comment|// add one reviewer
name|TestAccount
name|user
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"User"
argument_list|)
decl_stmt|;
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
DECL|method|testPushForMasterAsDraft_HTTP ()
specifier|public
name|void
name|testPushForMasterAsDraft_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMasterAsDraft
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForMasterAsDraft_SSH ()
specifier|public
name|void
name|testPushForMasterAsDraft_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForMasterAsDraft
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForMasterAsDraft (Protocol p)
specifier|private
name|void
name|testPushForMasterAsDraft
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
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
DECL|method|testPushForNonExistingBranch_HTTP ()
specifier|public
name|void
name|testPushForNonExistingBranch_HTTP
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForNonExistingBranch
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPushForNonExistingBranch_SSH ()
specifier|public
name|void
name|testPushForNonExistingBranch_SSH
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|testPushForNonExistingBranch
argument_list|(
name|Protocol
operator|.
name|SSH
argument_list|)
expr_stmt|;
block|}
DECL|method|testPushForNonExistingBranch (Protocol p)
specifier|private
name|void
name|testPushForNonExistingBranch
parameter_list|(
name|Protocol
name|p
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|selectProtocol
argument_list|(
name|p
argument_list|)
expr_stmt|;
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
DECL|method|pushTo (String ref)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|pushTo
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
operator|new
name|PushOneCommit
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
name|gerrit
operator|.
name|acceptance
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
name|GitUtil
operator|.
name|initSsh
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
name|assertEquals
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
name|GerritConfig
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
name|GerritConfigs
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
name|RestSession
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
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|SuggestReviewers
operator|.
name|SuggestedReviewerInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SuggestReviewersIT
specifier|public
class|class
name|SuggestReviewersIT
extends|extends
name|AbstractDaemonTest
block|{
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
annotation|@
name|Inject
DECL|field|pushFactory
specifier|private
name|PushOneCommit
operator|.
name|Factory
name|pushFactory
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
decl_stmt|;
DECL|field|session
specifier|private
name|RestSession
name|session
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
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
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
name|admin
argument_list|()
expr_stmt|;
name|session
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|group
argument_list|(
literal|"users1"
argument_list|)
expr_stmt|;
name|group
argument_list|(
literal|"users2"
argument_list|)
expr_stmt|;
name|group
argument_list|(
literal|"users3"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"user1"
argument_list|,
literal|"user1@example.com"
argument_list|,
literal|"User1"
argument_list|,
literal|"users1"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"user2"
argument_list|,
literal|"user2@example.com"
argument_list|,
literal|"User2"
argument_list|,
literal|"users2"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"user3"
argument_list|,
literal|"user3@example.com"
argument_list|,
literal|"User3"
argument_list|,
literal|"users1"
argument_list|,
literal|"users2"
argument_list|)
expr_stmt|;
name|initSsh
argument_list|(
name|admin
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
name|SshSession
name|sshSession
init|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
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
name|git
operator|=
name|cloneProject
argument_list|(
name|sshSession
operator|.
name|getUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|)
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
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"suggest.accounts"
argument_list|,
name|value
operator|=
literal|"false"
argument_list|)
DECL|method|suggestReviewersNoResult1 ()
specifier|public
name|void
name|suggestReviewersNoResult1
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|reviewers
init|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"u"
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfigs
argument_list|(
block|{
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"suggest.accounts"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
block|,
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"suggest.from"
argument_list|,
name|value
operator|=
literal|"1"
argument_list|)
block|,
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"accounts.visibility"
argument_list|,
name|value
operator|=
literal|"NONE"
argument_list|)
block|}
argument_list|)
DECL|method|suggestReviewersNoResult2 ()
specifier|public
name|void
name|suggestReviewersNoResult2
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|reviewers
init|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"u"
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"suggest.from"
argument_list|,
name|value
operator|=
literal|"2"
argument_list|)
DECL|method|suggestReviewersNoResult3 ()
specifier|public
name|void
name|suggestReviewersNoResult3
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|reviewers
init|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"u"
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|suggestReviewersChange ()
specifier|public
name|void
name|suggestReviewersChange
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|(
name|admin
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|reviewers
init|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"u"
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|reviewers
operator|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"u"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|reviewers
operator|=
name|suggestReviewers
argument_list|(
name|changeId
argument_list|,
literal|"users3"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|reviewers
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|suggestReviewers (String changeId, String query, int n)
specifier|private
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|suggestReviewers
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|query
parameter_list|,
name|int
name|n
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|session
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/suggest_reviewers?q="
operator|+
name|query
operator|+
literal|"&n="
operator|+
name|n
argument_list|)
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|group (String name)
specifier|private
name|void
name|group
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|session
operator|.
name|put
argument_list|(
literal|"/groups/"
operator|+
name|name
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|createChange (TestAccount account)
specifier|private
name|String
name|createChange
parameter_list|(
name|TestAccount
name|account
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
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
name|account
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
literal|"refs/for/master"
argument_list|)
operator|.
name|getChangeId
argument_list|()
return|;
block|}
block|}
end_class

end_unit


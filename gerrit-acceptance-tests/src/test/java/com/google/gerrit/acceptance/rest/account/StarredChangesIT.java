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
DECL|package|com.google.gerrit.acceptance.rest.account
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
name|account
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|acceptance
operator|.
name|PushOneCommit
operator|.
name|Result
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
name|rest
operator|.
name|change
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
name|gson
operator|.
name|Gson
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
DECL|class|StarredChangesIT
specifier|public
class|class
name|StarredChangesIT
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
name|initSsh
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p"
argument_list|)
decl_stmt|;
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
DECL|method|starredChangeState ()
specifier|public
name|void
name|starredChangeState
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|Result
name|c1
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Result
name|c2
init|=
name|createChange
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|true
argument_list|,
name|c1
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|true
argument_list|,
name|c2
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|false
argument_list|,
name|c1
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|starChange
argument_list|(
literal|false
argument_list|,
name|c2
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c1
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getChange
argument_list|(
name|c2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|starred
argument_list|)
expr_stmt|;
block|}
DECL|method|getChange (String changeId)
specifier|private
name|ChangeInfo
name|getChange
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
literal|"/changes/?q="
operator|+
name|changeId
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|c
init|=
operator|(
operator|new
name|Gson
argument_list|()
operator|)
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|starChange (boolean on, Change.Id id)
specifier|private
name|void
name|starChange
parameter_list|(
name|boolean
name|on
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|url
init|=
literal|"/accounts/self/starred.changes/"
operator|+
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|on
condition|)
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|delete
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createChange ()
specifier|private
name|Result
name|createChange
parameter_list|()
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
literal|"refs/for/master"
argument_list|)
return|;
block|}
block|}
end_class

end_unit


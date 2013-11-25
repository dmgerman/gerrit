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
name|git
operator|.
name|GitUtil
operator|.
name|checkout
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
name|assertTrue
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
name|base
operator|.
name|Function
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
name|git
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
name|git
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
name|Set
import|;
end_import

begin_class
DECL|class|ConflictsOperatorIT
specifier|public
class|class
name|ConflictsOperatorIT
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
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|count
specifier|private
name|int
name|count
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
name|db
operator|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noConflictingChanges ()
specifier|public
name|void
name|noConflictingChanges
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
name|Git
name|git
init|=
name|createProject
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|createChange
argument_list|(
name|git
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|changes
init|=
name|queryConflictingChanges
argument_list|(
name|change
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|changes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|conflictingChanges ()
specifier|public
name|void
name|conflictingChanges
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
name|Git
name|git
init|=
name|createProject
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|conflictingChange1
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|conflictingChange2
init|=
name|createChange
argument_list|(
name|git
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|createChange
argument_list|(
name|git
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|changes
init|=
name|queryConflictingChanges
argument_list|(
name|change
argument_list|)
decl_stmt|;
name|assertChanges
argument_list|(
name|changes
argument_list|,
name|conflictingChange1
argument_list|,
name|conflictingChange2
argument_list|)
expr_stmt|;
block|}
DECL|method|createProject ()
specifier|private
name|Git
name|createProject
parameter_list|()
throws|throws
name|JSchException
throws|,
name|IOException
throws|,
name|GitAPIException
block|{
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
try|try
block|{
name|GitUtil
operator|.
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
finally|finally
block|{
name|sshSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createChange (Git git, boolean conflicting)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|Git
name|git
parameter_list|,
name|boolean
name|conflicting
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|checkout
argument_list|(
name|git
argument_list|,
literal|"origin/master"
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|conflicting
condition|?
literal|"test.txt"
else|:
literal|"test-"
operator|+
name|count
operator|+
literal|".txt"
decl_stmt|;
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
argument_list|,
literal|"Change "
operator|+
name|count
argument_list|,
name|file
argument_list|,
literal|"content "
operator|+
name|count
argument_list|)
decl_stmt|;
name|count
operator|++
expr_stmt|;
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
DECL|method|queryConflictingChanges (PushOneCommit.Result change)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|queryConflictingChanges
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|change
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
literal|"/changes/?q=conflicts:"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ChangeInfo
argument_list|>
name|changes
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
name|Set
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
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|changes
argument_list|,
operator|new
name|Function
argument_list|<
name|ChangeInfo
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|ChangeInfo
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|id
return|;
block|}
block|}
argument_list|)
argument_list|)
return|;
block|}
DECL|method|assertChanges (Set<String> actualChanges, PushOneCommit.Result... expectedChanges)
specifier|private
name|void
name|assertChanges
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|actualChanges
parameter_list|,
name|PushOneCommit
operator|.
name|Result
modifier|...
name|expectedChanges
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedChanges
operator|.
name|length
argument_list|,
name|actualChanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PushOneCommit
operator|.
name|Result
name|c
range|:
name|expectedChanges
control|)
block|{
name|assertTrue
argument_list|(
name|actualChanges
operator|.
name|contains
argument_list|(
name|id
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|id (PushOneCommit.Result change)
specifier|private
name|String
name|id
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|change
parameter_list|)
block|{
return|return
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
return|;
block|}
block|}
end_class

end_unit


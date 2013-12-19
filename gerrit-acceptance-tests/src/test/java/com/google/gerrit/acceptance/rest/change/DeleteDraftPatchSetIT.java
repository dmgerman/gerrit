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
name|PatchSet
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
DECL|class|DeleteDraftPatchSetIT
specifier|public
class|class
name|DeleteDraftPatchSetIT
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
DECL|field|user
specifier|private
name|TestAccount
name|user
decl_stmt|;
DECL|field|session
specifier|private
name|RestSession
name|session
decl_stmt|;
DECL|field|userSession
specifier|private
name|RestSession
name|userSession
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
name|user
operator|=
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
name|userSession
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|user
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
DECL|method|deletePatchSet ()
specifier|public
name|void
name|deletePatchSet
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|String
name|changeId
init|=
name|createChangeWith2PS
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|PatchSet
name|ps
init|=
name|getCurrentPatchSet
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|getChange
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"p~master~"
operator|+
name|changeId
argument_list|,
name|c
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|,
name|c
operator|.
name|status
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|deletePatchSet
argument_list|(
name|changeId
argument_list|,
name|ps
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Patch set is not a draft."
argument_list|,
name|r
operator|.
name|getEntityContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|409
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDraftPatchSetNoACL ()
specifier|public
name|void
name|deleteDraftPatchSetNoACL
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|String
name|changeId
init|=
name|createChangeWith2PS
argument_list|(
literal|"refs/drafts/master"
argument_list|)
decl_stmt|;
name|PatchSet
name|ps
init|=
name|getCurrentPatchSet
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|getChange
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"p~master~"
operator|+
name|changeId
argument_list|,
name|c
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|,
name|c
operator|.
name|status
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|deletePatchSet
argument_list|(
name|changeId
argument_list|,
name|ps
argument_list|,
name|userSession
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Not found"
argument_list|,
name|r
operator|.
name|getEntityContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|404
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDraftPatchSetAndChange ()
specifier|public
name|void
name|deleteDraftPatchSetAndChange
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|String
name|changeId
init|=
name|createChangeWith2PS
argument_list|(
literal|"refs/drafts/master"
argument_list|)
decl_stmt|;
name|PatchSet
name|ps
init|=
name|getCurrentPatchSet
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|getChange
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"p~master~"
operator|+
name|changeId
argument_list|,
name|c
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|,
name|c
operator|.
name|status
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|deletePatchSet
argument_list|(
name|changeId
argument_list|,
name|ps
argument_list|,
name|session
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
name|Change
name|change
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byKey
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ps
operator|=
name|getCurrentPatchSet
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|r
operator|=
name|deletePatchSet
argument_list|(
name|changeId
argument_list|,
name|ps
argument_list|,
name|session
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byKey
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|toList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createChangeWith2PS (String ref)
specifier|private
name|String
name|createChangeWith2PS
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
name|Result
name|result
init|=
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
name|ref
argument_list|)
decl_stmt|;
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
name|PushOneCommit
operator|.
name|SUBJECT
argument_list|,
literal|"b.txt"
argument_list|,
literal|"4711"
argument_list|,
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
name|ref
argument_list|)
operator|.
name|getChangeId
argument_list|()
return|;
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
DECL|method|getCurrentPatchSet (String changeId)
specifier|private
name|PatchSet
name|getCurrentPatchSet
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|byKey
argument_list|(
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|deletePatchSet (String changeId, PatchSet ps, RestSession s)
specifier|private
specifier|static
name|RestResponse
name|deletePatchSet
parameter_list|(
name|String
name|changeId
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|RestSession
name|s
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|s
operator|.
name|delete
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/revisions/"
operator|+
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


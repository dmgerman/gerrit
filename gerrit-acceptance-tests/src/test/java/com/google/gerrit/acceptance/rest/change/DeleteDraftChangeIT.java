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
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|ChangeJson
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

begin_class
DECL|class|DeleteDraftChangeIT
specifier|public
class|class
name|DeleteDraftChangeIT
extends|extends
name|AbstractDaemonTest
block|{
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
DECL|method|deleteChange ()
specifier|public
name|void
name|deleteChange
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
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
name|deleteChange
argument_list|(
name|changeId
argument_list|,
name|adminSession
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Change is not a draft"
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
DECL|method|deleteDraftChange ()
specifier|public
name|void
name|deleteDraftChange
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
name|createDraftChange
argument_list|()
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
name|deleteChange
argument_list|(
name|changeId
argument_list|,
name|adminSession
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
annotation|@
name|Test
DECL|method|publishDraftChange ()
specifier|public
name|void
name|publishDraftChange
parameter_list|()
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|String
name|changeId
init|=
name|createDraftChange
argument_list|()
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
name|publishChange
argument_list|(
name|changeId
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
name|c
operator|=
name|getChange
argument_list|(
name|changeId
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
block|}
annotation|@
name|Test
DECL|method|publishDraftPatchSet ()
specifier|public
name|void
name|publishDraftPatchSet
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
name|createDraftChange
argument_list|()
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
name|publishPatchSet
argument_list|(
name|changeId
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
name|c
operator|=
name|getChange
argument_list|(
name|changeId
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
block|}
DECL|method|createChange ()
specifier|private
name|String
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
operator|.
name|getChangeId
argument_list|()
return|;
block|}
DECL|method|createDraftChange ()
specifier|private
name|String
name|createDraftChange
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
literal|"refs/drafts/master"
argument_list|)
operator|.
name|getChangeId
argument_list|()
return|;
block|}
DECL|method|deleteChange (String changeId, RestSession s)
specifier|private
specifier|static
name|RestResponse
name|deleteChange
parameter_list|(
name|String
name|changeId
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
argument_list|)
return|;
block|}
DECL|method|publishChange (String changeId)
specifier|private
name|RestResponse
name|publishChange
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/publish"
argument_list|)
return|;
block|}
DECL|method|publishPatchSet (String changeId)
specifier|private
name|RestResponse
name|publishPatchSet
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|PatchSet
name|patchSet
init|=
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
decl_stmt|;
return|return
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/revisions/"
operator|+
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/publish"
argument_list|)
return|;
block|}
block|}
end_class

end_unit


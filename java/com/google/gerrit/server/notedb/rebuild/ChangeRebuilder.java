begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb.rebuild
package|package
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
name|rebuild
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ChangeBundle
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
name|NoteDbUpdateManager
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
name|NoteDbUpdateManager
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|ChangeRebuilder
specifier|public
specifier|abstract
class|class
name|ChangeRebuilder
block|{
DECL|class|NoPatchSetsException
specifier|public
specifier|static
class|class
name|NoPatchSetsException
extends|extends
name|OrmException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|NoPatchSetsException (Change.Id changeId)
name|NoPatchSetsException
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
name|super
argument_list|(
literal|"Change "
operator|+
name|changeId
operator|+
literal|" cannot be rebuilt because it has no patch sets"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|method|ChangeRebuilder (SchemaFactory<ReviewDb> schemaFactory)
specifier|protected
name|ChangeRebuilder
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
block|}
DECL|method|rebuildAsync ( Change.Id id, ListeningExecutorService executor)
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|Result
argument_list|>
name|rebuildAsync
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|ListeningExecutorService
name|executor
parameter_list|)
block|{
return|return
name|executor
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schemaFactory
operator|.
name|open
argument_list|()
init|)
block|{
return|return
name|rebuild
argument_list|(
name|db
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Rebuild ReviewDb contents by copying from NoteDb.    *    *<p>Requires NoteDb to be the primary storage for the change.    */
DECL|method|rebuildReviewDb (ReviewDb db, Project.NameKey project, Change.Id changeId)
specifier|public
specifier|abstract
name|void
name|rebuildReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|// In the following methods "rebuilding" always refers to copying the state
comment|// from ReviewDb to NoteDb, i.e. assuming ReviewDb is the primary storage.
DECL|method|rebuild (ReviewDb db, Change.Id changeId)
specifier|public
specifier|abstract
name|Result
name|rebuild
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
function_decl|;
DECL|method|rebuildEvenIfReadOnly (ReviewDb db, Change.Id changeId)
specifier|public
specifier|abstract
name|Result
name|rebuildEvenIfReadOnly
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
function_decl|;
DECL|method|rebuild (NoteDbUpdateManager manager, ChangeBundle bundle)
specifier|public
specifier|abstract
name|Result
name|rebuild
parameter_list|(
name|NoteDbUpdateManager
name|manager
parameter_list|,
name|ChangeBundle
name|bundle
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
function_decl|;
DECL|method|buildUpdates (NoteDbUpdateManager manager, ChangeBundle bundle)
specifier|public
specifier|abstract
name|void
name|buildUpdates
parameter_list|(
name|NoteDbUpdateManager
name|manager
parameter_list|,
name|ChangeBundle
name|bundle
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
function_decl|;
DECL|method|stage (ReviewDb db, Change.Id changeId)
specifier|public
specifier|abstract
name|NoteDbUpdateManager
name|stage
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
function_decl|;
DECL|method|execute (ReviewDb db, Change.Id changeId, NoteDbUpdateManager manager)
specifier|public
specifier|abstract
name|Result
name|execute
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|NoteDbUpdateManager
name|manager
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
function_decl|;
block|}
end_class

end_unit


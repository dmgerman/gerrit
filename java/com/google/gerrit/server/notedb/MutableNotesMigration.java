begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
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
name|annotations
operator|.
name|VisibleForTesting
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
name|GerritServerConfig
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
name|NoteDbChangeState
operator|.
name|PrimaryStorage
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
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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

begin_comment
comment|/**  * {@link NotesMigration} with additional methods for altering the migration state at runtime.  *  *<p>Almost all callers care only about inspecting the migration state, and for safety should not  * have access to mutation methods, which must be used with extreme care. Those callers should  * inject {@link NotesMigration}.  *  *<p>Some callers, namely the NoteDb migration pipeline and tests, do need to alter the migration  * state at runtime, and those callers are expected to take the necessary precautions such as  * keeping the in-memory and on-disk config state in sync. Those callers use this class.  *  *<p>Mutations to the {@link MutableNotesMigration} are guaranteed to be instantly visible to all  * callers that use the non-mutable {@link NotesMigration}. The current implementation accomplishes  * this by always binding {@link NotesMigration} to {@link MutableNotesMigration} in Guice, so there  * is just one {@link NotesMigration} instance process-wide.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|MutableNotesMigration
specifier|public
class|class
name|MutableNotesMigration
extends|extends
name|NotesMigration
block|{
DECL|method|newDisabled ()
specifier|public
specifier|static
name|MutableNotesMigration
name|newDisabled
parameter_list|()
block|{
return|return
operator|new
name|MutableNotesMigration
argument_list|(
operator|new
name|Config
argument_list|()
argument_list|)
return|;
block|}
DECL|method|fromConfig (Config cfg)
specifier|public
specifier|static
name|MutableNotesMigration
name|fromConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
return|return
operator|new
name|MutableNotesMigration
argument_list|(
name|cfg
argument_list|)
return|;
block|}
annotation|@
name|Inject
DECL|method|MutableNotesMigration (@erritServerConfig Config cfg)
name|MutableNotesMigration
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|super
argument_list|(
name|Snapshot
operator|.
name|create
argument_list|(
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setReadChanges (boolean readChanges)
specifier|public
name|MutableNotesMigration
name|setReadChanges
parameter_list|(
name|boolean
name|readChanges
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|b
lambda|->
name|b
operator|.
name|setReadChanges
argument_list|(
name|readChanges
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setWriteChanges (boolean writeChanges)
specifier|public
name|MutableNotesMigration
name|setWriteChanges
parameter_list|(
name|boolean
name|writeChanges
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|b
lambda|->
name|b
operator|.
name|setWriteChanges
argument_list|(
name|writeChanges
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setReadChangeSequence (boolean readChangeSequence)
specifier|public
name|MutableNotesMigration
name|setReadChangeSequence
parameter_list|(
name|boolean
name|readChangeSequence
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|b
lambda|->
name|b
operator|.
name|setReadChangeSequence
argument_list|(
name|readChangeSequence
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setChangePrimaryStorage (PrimaryStorage changePrimaryStorage)
specifier|public
name|MutableNotesMigration
name|setChangePrimaryStorage
parameter_list|(
name|PrimaryStorage
name|changePrimaryStorage
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|b
lambda|->
name|b
operator|.
name|setChangePrimaryStorage
argument_list|(
name|changePrimaryStorage
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setDisableChangeReviewDb (boolean disableChangeReviewDb)
specifier|public
name|MutableNotesMigration
name|setDisableChangeReviewDb
parameter_list|(
name|boolean
name|disableChangeReviewDb
parameter_list|)
block|{
return|return
name|set
argument_list|(
name|b
lambda|->
name|b
operator|.
name|setDisableChangeReviewDb
argument_list|(
name|disableChangeReviewDb
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Set the in-memory values returned by this instance to match the given state.    *    *<p>This method is only intended for use by tests.    *    *<p>This<em>only</em> modifies the in-memory state; if this instance was initialized from a    * file-based config, the underlying storage is not updated. Callers are responsible for managing    * the underlying storage on their own.    */
annotation|@
name|VisibleForTesting
DECL|method|setFrom (NotesMigrationState state)
specifier|public
name|MutableNotesMigration
name|setFrom
parameter_list|(
name|NotesMigrationState
name|state
parameter_list|)
block|{
name|snapshot
operator|.
name|set
argument_list|(
name|state
operator|.
name|snapshot
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|set (Function<Snapshot.Builder, Snapshot.Builder> f)
specifier|private
name|MutableNotesMigration
name|set
parameter_list|(
name|Function
argument_list|<
name|Snapshot
operator|.
name|Builder
argument_list|,
name|Snapshot
operator|.
name|Builder
argument_list|>
name|f
parameter_list|)
block|{
name|snapshot
operator|.
name|updateAndGet
argument_list|(
name|s
lambda|->
name|f
operator|.
name|apply
argument_list|(
name|s
operator|.
name|toBuilder
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit


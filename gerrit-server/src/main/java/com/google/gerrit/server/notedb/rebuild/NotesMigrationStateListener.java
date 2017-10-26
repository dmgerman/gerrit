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
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|NotesMigrationState
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

begin_comment
comment|/** Listener for state changes performed by {@link OnlineNoteDbMigrator}. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|NotesMigrationStateListener
specifier|public
interface|interface
name|NotesMigrationStateListener
block|{
comment|/**    * Invoked just before saving the new migration state.    *    * @param oldState state prior to this state change.    * @param newState state after this state change.    * @throws IOException if an error occurred, which will cause the migration to abort. Exceptions    *     that should be considered non-fatal must be caught (and ideally logged) by the    *     implementation rather than thrown.    */
DECL|method|preStateChange (NotesMigrationState oldState, NotesMigrationState newState)
name|void
name|preStateChange
parameter_list|(
name|NotesMigrationState
name|oldState
parameter_list|,
name|NotesMigrationState
name|newState
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit


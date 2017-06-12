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

begin_comment
comment|/**  * Current low-level settings of the NoteDb migration for changes.  *  *<p>This class only describes the migration state of the {@link  * com.google.gerrit.reviewdb.client.Change Change} entity group, since it is possible for a given  * site to be in different states of the Change NoteDb migration process while staying at the same  * ReviewDb schema version. It does<em>not</em> describe the migration state of non-Change tables;  * those are automatically migrated using the ReviewDb schema migration process, so the NoteDb  * migration state at a given ReviewDb schema cannot vary.  *  *<p>In many places, core Gerrit code should not directly care about the NoteDb migration state,  * and should prefer high-level APIs like {@link com.google.gerrit.server.ApprovalsUtil  * ApprovalsUtil} that don't require callers to inspect the migration state. The  *<em>implementation</em> of those utilities does care about the state, and should query the {@code  * NotesMigration} for the properties of the migration, for example, {@link #changePrimaryStorage()  * where new changes should be stored}.  *  *<p>Core Gerrit code is mostly interested in one facet of the migration at a time (reading or  * writing, say), but not all combinations of return values are supported or even make sense.  *  *<p>This class controls the state of the migration according to options in {@code gerrit.config}.  * In general, any changes to these options should only be made by adventurous administrators, who  * know what they're doing, on non-production data, for the purposes of testing the NoteDb  * implementation. Changing options quite likely requires re-running {@code RebuildNoteDb}. For  * these reasons, the options remain undocumented.  *  *<p><strong>Note:</strong> Callers should not assume the values returned by {@code  * NotesMigration}'s methods will not change in a running server.  */
end_comment

begin_class
DECL|class|NotesMigration
specifier|public
specifier|abstract
class|class
name|NotesMigration
block|{
comment|/**    * Read changes from NoteDb.    *    *<p>Change data is read from NoteDb refs, but ReviewDb is still the source of truth. If the    * loader determines NoteDb is out of date, the change data in NoteDb will be transparently    * rebuilt. This means that some code paths that look read-only may in fact attempt to write.    *    *<p>If true and {@code writeChanges() = false}, changes can still be read from NoteDb, but any    * attempts to write will generate an error.    */
DECL|method|readChanges ()
specifier|public
specifier|abstract
name|boolean
name|readChanges
parameter_list|()
function_decl|;
comment|/**    * Write changes to NoteDb.    *    *<p>This method is awkwardly named because you should be using either {@link    * #commitChangeWrites()} or {@link #failChangeWrites()} instead.    *    *<p>Updates to change data are written to NoteDb refs, but ReviewDb is still the source of    * truth. Change data will not be written unless the NoteDb refs are already up to date, and the    * write path will attempt to rebuild the change if not.    *    *<p>If false, the behavior when attempting to write depends on {@code readChanges()}. If {@code    * readChanges() = false}, writes to NoteDb are simply ignored; if {@code true}, any attempts to    * write will generate an error.    */
DECL|method|rawWriteChangesSetting ()
specifier|public
specifier|abstract
name|boolean
name|rawWriteChangesSetting
parameter_list|()
function_decl|;
comment|/**    * Read sequential change ID numbers from NoteDb.    *    *<p>If true, change IDs are read from {@code refs/sequences/changes} in All-Projects. If false,    * change IDs are read from ReviewDb's native sequences.    */
DECL|method|readChangeSequence ()
specifier|public
specifier|abstract
name|boolean
name|readChangeSequence
parameter_list|()
function_decl|;
comment|/** @return default primary storage for new changes. */
DECL|method|changePrimaryStorage ()
specifier|public
specifier|abstract
name|PrimaryStorage
name|changePrimaryStorage
parameter_list|()
function_decl|;
comment|/**    * Disable ReviewDb access for changes.    *    *<p>When set, ReviewDb operations involving the Changes table become no-ops. Lookups return no    * results; updates do nothing, as does opening, committing, or rolling back a transaction on the    * Changes table.    */
DECL|method|disableChangeReviewDb ()
specifier|public
specifier|abstract
name|boolean
name|disableChangeReviewDb
parameter_list|()
function_decl|;
comment|/**    * Fuse meta ref updates in the same batch as code updates.    *    *<p>When set, each {@link com.google.gerrit.server.update.BatchUpdate} results in a single    * {@link org.eclipse.jgit.lib.BatchRefUpdate} to update both code and meta refs atomically.    * Setting this option with a repository backend that does not support atomic multi-ref    * transactions ({@link org.eclipse.jgit.lib.RefDatabase#performsAtomicTransactions()}) is a    * configuration error, and all updates will fail at runtime.    *    *<p>Has no effect if {@link #disableChangeReviewDb()} is false.    */
DECL|method|fuseUpdates ()
specifier|public
specifier|abstract
name|boolean
name|fuseUpdates
parameter_list|()
function_decl|;
comment|/**    * Whether to fail when reading any data from NoteDb.    *    *<p>Used in conjunction with {@link #readChanges()} for tests.    */
DECL|method|failOnLoad ()
specifier|public
name|boolean
name|failOnLoad
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|commitChangeWrites ()
specifier|public
name|boolean
name|commitChangeWrites
parameter_list|()
block|{
comment|// It may seem odd that readChanges() without writeChanges() means we should
comment|// attempt to commit writes. However, this method is used by callers to know
comment|// whether or not they should short-circuit and skip attempting to read or
comment|// write NoteDb refs.
comment|//
comment|// It is possible for commitChangeWrites() to return true and
comment|// failChangeWrites() to also return true, causing an error later in the
comment|// same codepath. This specific condition is used by the auto-rebuilding
comment|// path to rebuild a change and stage the results, but not commit them due
comment|// to failChangeWrites().
return|return
name|rawWriteChangesSetting
argument_list|()
operator|||
name|readChanges
argument_list|()
return|;
block|}
DECL|method|failChangeWrites ()
specifier|public
name|boolean
name|failChangeWrites
parameter_list|()
block|{
return|return
operator|!
name|rawWriteChangesSetting
argument_list|()
operator|&&
name|readChanges
argument_list|()
return|;
block|}
DECL|method|enabled ()
specifier|public
name|boolean
name|enabled
parameter_list|()
block|{
return|return
name|rawWriteChangesSetting
argument_list|()
operator|||
name|readChanges
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
comment|/**  * Possible high-level states of the NoteDb migration for changes.  *  *<p>This class describes the series of states required to migrate a site from ReviewDb-only to  * NoteDb-only. This process has several steps, and covers only a small subset of the theoretically  * possible combinations of {@link NotesMigration} return values.  *  *<p>These states are ordered: a one-way migration from ReviewDb to NoteDb will pass through states  * in the order in which they are defined.  */
end_comment

begin_enum
DECL|enum|NotesMigrationState
specifier|public
enum|enum
name|NotesMigrationState
block|{
DECL|enumConstant|REVIEW_DB
name|REVIEW_DB
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|WRITE
name|WRITE
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|READ_WRITE_NO_SEQUENCE
name|READ_WRITE_NO_SEQUENCE
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|READ_WRITE_WITH_SEQUENCE_REVIEW_DB_PRIMARY
name|READ_WRITE_WITH_SEQUENCE_REVIEW_DB_PRIMARY
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|PrimaryStorage
operator|.
name|REVIEW_DB
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|READ_WRITE_WITH_SEQUENCE_NOTE_DB_PRIMARY
name|READ_WRITE_WITH_SEQUENCE_NOTE_DB_PRIMARY
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|PrimaryStorage
operator|.
name|NOTE_DB
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
comment|// TODO(dborowitz): This only exists as a separate state to support testing in different
comment|// NoteDbModes. Once FileRepository fuses BatchRefUpdates, we won't have separate fused/unfused
comment|// states.
DECL|enumConstant|NOTE_DB_UNFUSED
name|NOTE_DB_UNFUSED
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|PrimaryStorage
operator|.
name|NOTE_DB
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|NOTE_DB
name|NOTE_DB
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|PrimaryStorage
operator|.
name|NOTE_DB
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
block|;
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|method|NotesMigrationState ( boolean readChanges, boolean rawWriteChangesSetting, boolean readChangeSequence, PrimaryStorage changePrimaryStorage, boolean disableChangeReviewDb, boolean fuseUpdates)
name|NotesMigrationState
parameter_list|(
comment|// Arguments match abstract methods in NotesMigration.
name|boolean
name|readChanges
parameter_list|,
name|boolean
name|rawWriteChangesSetting
parameter_list|,
name|boolean
name|readChangeSequence
parameter_list|,
name|PrimaryStorage
name|changePrimaryStorage
parameter_list|,
name|boolean
name|disableChangeReviewDb
parameter_list|,
name|boolean
name|fuseUpdates
parameter_list|)
block|{
name|this
operator|.
name|migration
operator|=
operator|new
name|NotesMigration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|readChanges
parameter_list|()
block|{
return|return
name|readChanges
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|rawWriteChangesSetting
parameter_list|()
block|{
return|return
name|rawWriteChangesSetting
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|readChangeSequence
parameter_list|()
block|{
return|return
name|readChangeSequence
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrimaryStorage
name|changePrimaryStorage
parameter_list|()
block|{
return|return
name|changePrimaryStorage
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|disableChangeReviewDb
parameter_list|()
block|{
return|return
name|disableChangeReviewDb
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|fuseUpdates
parameter_list|()
block|{
return|return
name|fuseUpdates
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|migration ()
specifier|public
name|NotesMigration
name|migration
parameter_list|()
block|{
return|return
name|migration
return|;
block|}
block|}
end_enum

end_unit


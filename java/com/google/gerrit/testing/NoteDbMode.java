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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|Enums
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
name|Strings
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
name|MutableNotesMigration
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

begin_enum
DECL|enum|NoteDbMode
specifier|public
enum|enum
name|NoteDbMode
block|{
comment|/** NoteDb is disabled. */
DECL|enumConstant|OFF
name|OFF
parameter_list|(
name|NotesMigrationState
operator|.
name|REVIEW_DB
parameter_list|)
operator|,
comment|/** Writing data to NoteDb is enabled. */
DECL|enumConstant|WRITE
constructor|WRITE(NotesMigrationState.WRITE
block|)
enum|,
comment|/** Reading and writing all data to NoteDb is enabled. */
DECL|enumConstant|READ_WRITE
name|READ_WRITE
argument_list|(
name|NotesMigrationState
operator|.
name|READ_WRITE_WITH_SEQUENCE_REVIEW_DB_PRIMARY
argument_list|)
operator|,
comment|/** Changes are created with their primary storage as NoteDb. */
DECL|enumConstant|PRIMARY
name|PRIMARY
argument_list|(
name|NotesMigrationState
operator|.
name|READ_WRITE_WITH_SEQUENCE_NOTE_DB_PRIMARY
argument_list|)
operator|,
comment|/** All change tables are entirely disabled, and code/meta ref updates are fused. */
DECL|enumConstant|ON
name|ON
argument_list|(
name|NotesMigrationState
operator|.
name|NOTE_DB
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|ENV_VAR
specifier|private
specifier|static
specifier|final
name|String
name|ENV_VAR
init|=
literal|"GERRIT_NOTEDB"
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|SYS_PROP
specifier|private
specifier|static
specifier|final
name|String
name|SYS_PROP
init|=
literal|"gerrit.notedb"
decl_stmt|;
end_decl_stmt

begin_function
DECL|method|get ()
specifier|public
specifier|static
name|NoteDbMode
name|get
parameter_list|()
block|{
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|ON
return|;
block|}
name|value
operator|=
name|value
operator|.
name|toUpperCase
argument_list|()
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|NoteDbMode
name|mode
init|=
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|NoteDbMode
operator|.
name|class
argument_list|,
name|value
argument_list|)
operator|.
name|orNull
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
condition|)
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for env variable %s: %s"
argument_list|,
name|ENV_VAR
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for system property %s: %s"
argument_list|,
name|SYS_PROP
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
return|;
block|}
end_function

begin_function
DECL|method|newNotesMigrationFromEnv ()
specifier|public
specifier|static
name|MutableNotesMigration
name|newNotesMigrationFromEnv
parameter_list|()
block|{
name|MutableNotesMigration
name|m
init|=
name|MutableNotesMigration
operator|.
name|newDisabled
argument_list|()
decl_stmt|;
name|resetFromEnv
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
end_function

begin_function
DECL|method|resetFromEnv (MutableNotesMigration migration)
specifier|public
specifier|static
name|void
name|resetFromEnv
parameter_list|(
name|MutableNotesMigration
name|migration
parameter_list|)
block|{
name|migration
operator|.
name|setFrom
argument_list|(
name|get
argument_list|()
operator|.
name|state
argument_list|)
expr_stmt|;
block|}
end_function

begin_decl_stmt
DECL|field|state
specifier|private
specifier|final
name|NotesMigrationState
name|state
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|NoteDbMode (NotesMigrationState state)
specifier|private
name|NoteDbMode
parameter_list|(
name|NotesMigrationState
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
end_constructor

unit|}
end_unit


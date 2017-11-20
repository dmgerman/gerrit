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
import|import static
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
name|NoteDbTable
operator|.
name|GROUPS
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
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
operator|.
name|DISABLE_REVIEW_DB
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
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
operator|.
name|READ
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
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
operator|.
name|SECTION_NOTE_DB
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
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
operator|.
name|WRITE
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
name|inject
operator|.
name|AbstractModule
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
name|Set
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

begin_class
annotation|@
name|Singleton
DECL|class|GroupsMigration
specifier|public
class|class
name|GroupsMigration
block|{
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|GroupsMigration
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|writeToNoteDb
specifier|private
specifier|final
name|boolean
name|writeToNoteDb
decl_stmt|;
DECL|field|readFromNoteDb
specifier|private
specifier|final
name|boolean
name|readFromNoteDb
decl_stmt|;
DECL|field|disableGroupReviewDb
specifier|private
specifier|final
name|boolean
name|disableGroupReviewDb
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsMigration (@erritServerConfig Config cfg)
specifier|public
name|GroupsMigration
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
comment|// TODO(aliceks): Remove these flags when all other necessary TODOs for writing groups to
comment|// NoteDb have been addressed.
comment|// Don't flip these flags in a production setting! We only added them to spread the
comment|// implementation of groups in NoteDb among several changes which are gradually merged.
name|this
argument_list|(
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
literal|false
argument_list|)
argument_list|,
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
literal|false
argument_list|)
argument_list|,
name|cfg
operator|.
name|getBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|DISABLE_REVIEW_DB
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|GroupsMigration ( boolean writeToNoteDb, boolean readFromNoteDb, boolean disableGroupReviewDb)
specifier|public
name|GroupsMigration
parameter_list|(
name|boolean
name|writeToNoteDb
parameter_list|,
name|boolean
name|readFromNoteDb
parameter_list|,
name|boolean
name|disableGroupReviewDb
parameter_list|)
block|{
name|this
operator|.
name|writeToNoteDb
operator|=
name|writeToNoteDb
expr_stmt|;
name|this
operator|.
name|readFromNoteDb
operator|=
name|readFromNoteDb
expr_stmt|;
name|this
operator|.
name|disableGroupReviewDb
operator|=
name|disableGroupReviewDb
expr_stmt|;
block|}
DECL|method|writeToNoteDb ()
specifier|public
name|boolean
name|writeToNoteDb
parameter_list|()
block|{
return|return
name|writeToNoteDb
return|;
block|}
DECL|method|readFromNoteDb ()
specifier|public
name|boolean
name|readFromNoteDb
parameter_list|()
block|{
return|return
name|readFromNoteDb
return|;
block|}
DECL|method|disableGroupReviewDb ()
specifier|public
name|boolean
name|disableGroupReviewDb
parameter_list|()
block|{
return|return
name|disableGroupReviewDb
return|;
block|}
DECL|method|setConfigValuesIfNotSetYet (Config cfg)
specifier|public
name|void
name|setConfigValuesIfNotSetYet
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|subsections
init|=
name|cfg
operator|.
name|getSubsections
argument_list|(
name|SECTION_NOTE_DB
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|subsections
operator|.
name|contains
argument_list|(
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
name|writeToNoteDb
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
name|readFromNoteDb
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|GROUPS
operator|.
name|key
argument_list|()
argument_list|,
name|DISABLE_REVIEW_DB
argument_list|,
name|disableGroupReviewDb
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


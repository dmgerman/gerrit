begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
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
name|CHANGES
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitStep
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|Section
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
comment|/** Initialize the NoteDb in gerrit site. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitNoteDb
class|class
name|InitNoteDb
implements|implements
name|InitStep
block|{
DECL|field|noteDbChanges
specifier|private
specifier|final
name|Section
name|noteDbChanges
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitNoteDb (Section.Factory sections)
name|InitNoteDb
parameter_list|(
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|noteDbChanges
operator|=
name|sections
operator|.
name|get
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|initNoteDb
argument_list|()
expr_stmt|;
block|}
DECL|method|initNoteDb ()
specifier|private
name|void
name|initNoteDb
parameter_list|()
block|{
name|Config
name|defaultConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|NotesMigrationState
operator|.
name|FINAL
operator|.
name|setConfigValues
argument_list|(
name|defaultConfig
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|defaultConfig
operator|.
name|getNames
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|)
control|)
block|{
name|noteDbChanges
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|defaultConfig
operator|.
name|getString
argument_list|(
name|SECTION_NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


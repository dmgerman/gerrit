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
name|ACCOUNTS
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
name|NoteDbTable
operator|.
name|CHANGES
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
name|ImmutableSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_comment
comment|/**  * Implement NoteDb migration stages using {@code gerrit.config}.  *<p>  * This class controls the state of the migration according to options in  * {@code gerrit.config}. In general, any changes to these options should only  * be made by adventurous administrators, who know what they're doing, on  * non-production data, for the purposes of testing the NoteDb implementation.  * Changing options quite likely requires re-running {@code RebuildNoteDb}. For  * these reasons, the options remain undocumented.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ConfigNotesMigration
specifier|public
class|class
name|ConfigNotesMigration
extends|extends
name|NotesMigration
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
name|NotesMigration
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ConfigNotesMigration
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|NOTE_DB
specifier|private
specifier|static
specifier|final
name|String
name|NOTE_DB
init|=
literal|"noteDb"
decl_stmt|;
DECL|field|READ
specifier|private
specifier|static
specifier|final
name|String
name|READ
init|=
literal|"read"
decl_stmt|;
DECL|field|WRITE
specifier|private
specifier|static
specifier|final
name|String
name|WRITE
init|=
literal|"write"
decl_stmt|;
DECL|field|SEQUENCE
specifier|private
specifier|static
specifier|final
name|String
name|SEQUENCE
init|=
literal|"sequence"
decl_stmt|;
DECL|method|checkConfig (Config cfg)
specifier|private
specifier|static
name|void
name|checkConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|NoteDbTable
name|t
range|:
name|NoteDbTable
operator|.
name|values
argument_list|()
control|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|t
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|allowed
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|READ
argument_list|,
name|WRITE
argument_list|,
name|SEQUENCE
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|t
range|:
name|cfg
operator|.
name|getSubsections
argument_list|(
name|NOTE_DB
argument_list|)
control|)
block|{
name|checkArgument
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
name|t
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|,
literal|"invalid NoteDb table: %s"
argument_list|,
name|t
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|cfg
operator|.
name|getNames
argument_list|(
name|NOTE_DB
argument_list|,
name|t
argument_list|)
control|)
block|{
name|checkArgument
argument_list|(
name|allowed
operator|.
name|contains
argument_list|(
name|key
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|,
literal|"invalid NoteDb key: %s.%s"
argument_list|,
name|t
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|allEnabledConfig ()
specifier|public
specifier|static
name|Config
name|allEnabledConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
for|for
control|(
name|NoteDbTable
name|t
range|:
name|NoteDbTable
operator|.
name|values
argument_list|()
control|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|t
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|t
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|cfg
return|;
block|}
DECL|field|writeChanges
specifier|private
specifier|final
name|boolean
name|writeChanges
decl_stmt|;
DECL|field|readChanges
specifier|private
specifier|final
name|boolean
name|readChanges
decl_stmt|;
DECL|field|readChangeSequence
specifier|private
specifier|final
name|boolean
name|readChangeSequence
decl_stmt|;
DECL|field|writeAccounts
specifier|private
specifier|final
name|boolean
name|writeAccounts
decl_stmt|;
DECL|field|readAccounts
specifier|private
specifier|final
name|boolean
name|readAccounts
decl_stmt|;
annotation|@
name|Inject
DECL|method|ConfigNotesMigration (@erritServerConfig Config cfg)
name|ConfigNotesMigration
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|checkConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|writeChanges
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|readChanges
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Reading change sequence numbers from NoteDb is not the default even if
comment|// reading changes themselves is. Once this is enabled, it's not easy to
comment|// undo: ReviewDb might hand out numbers that have already been assigned by
comment|// NoteDb. This decision for the default may be reevaluated later.
name|readChangeSequence
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|CHANGES
operator|.
name|key
argument_list|()
argument_list|,
name|SEQUENCE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|writeAccounts
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|ACCOUNTS
operator|.
name|key
argument_list|()
argument_list|,
name|WRITE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|readAccounts
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|NOTE_DB
argument_list|,
name|ACCOUNTS
operator|.
name|key
argument_list|()
argument_list|,
name|READ
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeChanges ()
specifier|protected
name|boolean
name|writeChanges
parameter_list|()
block|{
return|return
name|writeChanges
return|;
block|}
annotation|@
name|Override
DECL|method|readChanges ()
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
DECL|method|readChangeSequence ()
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
DECL|method|writeAccounts ()
specifier|public
name|boolean
name|writeAccounts
parameter_list|()
block|{
return|return
name|writeAccounts
return|;
block|}
annotation|@
name|Override
DECL|method|readAccounts ()
specifier|public
name|boolean
name|readAccounts
parameter_list|()
block|{
return|return
name|readAccounts
return|;
block|}
block|}
end_class

end_unit


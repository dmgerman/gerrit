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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|checkNotNull
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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
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

begin_comment
comment|/** {@link NotesMigration} with bits that can be flipped live for testing. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|TestNotesMigration
specifier|public
class|class
name|TestNotesMigration
extends|extends
name|NotesMigration
block|{
DECL|field|readChanges
specifier|private
specifier|volatile
name|boolean
name|readChanges
decl_stmt|;
DECL|field|writeChanges
specifier|private
specifier|volatile
name|boolean
name|writeChanges
decl_stmt|;
DECL|field|changePrimaryStorage
specifier|private
specifier|volatile
name|PrimaryStorage
name|changePrimaryStorage
init|=
name|PrimaryStorage
operator|.
name|REVIEW_DB
decl_stmt|;
DECL|field|disableChangeReviewDb
specifier|private
specifier|volatile
name|boolean
name|disableChangeReviewDb
decl_stmt|;
DECL|field|fuseUpdates
specifier|private
specifier|volatile
name|boolean
name|fuseUpdates
decl_stmt|;
DECL|field|failOnLoad
specifier|private
specifier|volatile
name|boolean
name|failOnLoad
decl_stmt|;
DECL|method|TestNotesMigration ()
specifier|public
name|TestNotesMigration
parameter_list|()
block|{
name|resetFromEnv
argument_list|()
expr_stmt|;
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
comment|// Unlike ConfigNotesMigration, read change numbers from NoteDb by default
comment|// when reads are enabled, to improve test coverage.
return|return
name|readChanges
return|;
block|}
annotation|@
name|Override
DECL|method|changePrimaryStorage ()
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
DECL|method|disableChangeReviewDb ()
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
DECL|method|fuseUpdates ()
specifier|public
name|boolean
name|fuseUpdates
parameter_list|()
block|{
return|return
name|fuseUpdates
return|;
block|}
comment|// Increase visbility from superclass, as tests may want to check whether
comment|// NoteDb data is written in specific migration scenarios.
annotation|@
name|Override
DECL|method|rawWriteChangesSetting ()
specifier|public
name|boolean
name|rawWriteChangesSetting
parameter_list|()
block|{
return|return
name|writeChanges
return|;
block|}
annotation|@
name|Override
DECL|method|failOnLoad ()
specifier|public
name|boolean
name|failOnLoad
parameter_list|()
block|{
return|return
name|failOnLoad
return|;
block|}
DECL|method|setReadChanges (boolean readChanges)
specifier|public
name|TestNotesMigration
name|setReadChanges
parameter_list|(
name|boolean
name|readChanges
parameter_list|)
block|{
name|this
operator|.
name|readChanges
operator|=
name|readChanges
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setWriteChanges (boolean writeChanges)
specifier|public
name|TestNotesMigration
name|setWriteChanges
parameter_list|(
name|boolean
name|writeChanges
parameter_list|)
block|{
name|this
operator|.
name|writeChanges
operator|=
name|writeChanges
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setChangePrimaryStorage (PrimaryStorage changePrimaryStorage)
specifier|public
name|TestNotesMigration
name|setChangePrimaryStorage
parameter_list|(
name|PrimaryStorage
name|changePrimaryStorage
parameter_list|)
block|{
name|this
operator|.
name|changePrimaryStorage
operator|=
name|checkNotNull
argument_list|(
name|changePrimaryStorage
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDisableChangeReviewDb (boolean disableChangeReviewDb)
specifier|public
name|TestNotesMigration
name|setDisableChangeReviewDb
parameter_list|(
name|boolean
name|disableChangeReviewDb
parameter_list|)
block|{
name|this
operator|.
name|disableChangeReviewDb
operator|=
name|disableChangeReviewDb
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setFuseUpdates (boolean fuseUpdates)
specifier|public
name|TestNotesMigration
name|setFuseUpdates
parameter_list|(
name|boolean
name|fuseUpdates
parameter_list|)
block|{
name|this
operator|.
name|fuseUpdates
operator|=
name|fuseUpdates
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setFailOnLoad (boolean failOnLoad)
specifier|public
name|TestNotesMigration
name|setFailOnLoad
parameter_list|(
name|boolean
name|failOnLoad
parameter_list|)
block|{
name|this
operator|.
name|failOnLoad
operator|=
name|failOnLoad
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setAllEnabled (boolean enabled)
specifier|public
name|TestNotesMigration
name|setAllEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
return|return
name|setReadChanges
argument_list|(
name|enabled
argument_list|)
operator|.
name|setWriteChanges
argument_list|(
name|enabled
argument_list|)
return|;
block|}
DECL|method|resetFromEnv ()
specifier|public
name|TestNotesMigration
name|resetFromEnv
parameter_list|()
block|{
return|return
name|setFrom
argument_list|(
name|NoteDbMode
operator|.
name|get
argument_list|()
operator|.
name|migration
argument_list|)
return|;
block|}
DECL|method|setFrom (NotesMigration other)
specifier|public
name|TestNotesMigration
name|setFrom
parameter_list|(
name|NotesMigration
name|other
parameter_list|)
block|{
name|setWriteChanges
argument_list|(
name|other
operator|.
name|rawWriteChangesSetting
argument_list|()
argument_list|)
expr_stmt|;
name|setReadChanges
argument_list|(
name|other
operator|.
name|readChanges
argument_list|()
argument_list|)
expr_stmt|;
name|setChangePrimaryStorage
argument_list|(
name|other
operator|.
name|changePrimaryStorage
argument_list|()
argument_list|)
expr_stmt|;
name|setDisableChangeReviewDb
argument_list|(
name|other
operator|.
name|disableChangeReviewDb
argument_list|()
argument_list|)
expr_stmt|;
name|setFuseUpdates
argument_list|(
name|other
operator|.
name|fuseUpdates
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


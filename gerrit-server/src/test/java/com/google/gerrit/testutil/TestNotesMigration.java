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
DECL|field|failOnLoad
specifier|private
specifier|volatile
name|boolean
name|failOnLoad
decl_stmt|;
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
comment|// Increase visbility from superclass, as tests may want to check whether
comment|// NoteDb data is written in specific migration scenarios.
annotation|@
name|Override
DECL|method|writeChanges ()
specifier|public
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
DECL|method|readAccounts ()
specifier|public
name|boolean
name|readAccounts
parameter_list|()
block|{
return|return
literal|false
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
literal|false
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
DECL|method|setFromEnv ()
specifier|public
name|TestNotesMigration
name|setFromEnv
parameter_list|()
block|{
switch|switch
condition|(
name|NoteDbMode
operator|.
name|get
argument_list|()
condition|)
block|{
case|case
name|READ_WRITE
case|:
name|setWriteChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setReadChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|WRITE
case|:
name|setWriteChanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setReadChanges
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|CHECK
case|:
case|case
name|OFF
case|:
default|default:
name|setWriteChanges
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setReadChanges
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|this
return|;
block|}
block|}
end_class

end_unit


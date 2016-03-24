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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|config
operator|.
name|FactoryModule
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_class
DECL|class|NoteDbModule
specifier|public
class|class
name|NoteDbModule
extends|extends
name|FactoryModule
block|{
DECL|field|useTestBindings
specifier|private
specifier|final
name|boolean
name|useTestBindings
decl_stmt|;
DECL|method|forTest ()
specifier|static
name|NoteDbModule
name|forTest
parameter_list|()
block|{
return|return
operator|new
name|NoteDbModule
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|NoteDbModule ()
specifier|public
name|NoteDbModule
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|NoteDbModule (boolean useTestBindings)
specifier|private
name|NoteDbModule
parameter_list|(
name|boolean
name|useTestBindings
parameter_list|)
block|{
name|this
operator|.
name|useTestBindings
operator|=
name|useTestBindings
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|factory
argument_list|(
name|ChangeUpdate
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeDraftUpdate
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|DraftCommentNotes
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|NoteDbUpdateManager
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|useTestBindings
condition|)
block|{
name|bind
argument_list|(
name|ChangeRebuilder
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeRebuilderImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bind
argument_list|(
name|ChangeRebuilder
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|ChangeRebuilder
argument_list|(
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|NoteDbChangeState
name|rebuild
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


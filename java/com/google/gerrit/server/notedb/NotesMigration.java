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
name|Objects
import|;
end_import

begin_comment
comment|/**  * Current low-level settings of the NoteDb migration for changes.  *  *<p>This class is a stub and will be removed soon; NoteDb is the only mode.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|NotesMigration
specifier|public
class|class
name|NotesMigration
block|{
DECL|field|SECTION_NOTE_DB
specifier|public
specifier|static
specifier|final
name|String
name|SECTION_NOTE_DB
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
DECL|field|DISABLE_REVIEW_DB
specifier|private
specifier|static
specifier|final
name|String
name|DISABLE_REVIEW_DB
init|=
literal|"disableReviewDb"
decl_stmt|;
DECL|field|PRIMARY_STORAGE
specifier|private
specifier|static
specifier|final
name|String
name|PRIMARY_STORAGE
init|=
literal|"primaryStorage"
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
comment|/**    * Read changes from NoteDb.    *    *<p>Change data is read from NoteDb refs, but ReviewDb is still the source of truth. If the    * loader determines NoteDb is out of date, the change data in NoteDb will be transparently    * rebuilt. This means that some code paths that look read-only may in fact attempt to write.    *    *<p>If true and {@code writeChanges() = false}, changes can still be read from NoteDb, but any    * attempts to write will generate an error.    */
DECL|method|readChanges ()
specifier|public
specifier|final
name|boolean
name|readChanges
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|commitChangeWrites ()
specifier|public
specifier|final
name|boolean
name|commitChangeWrites
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|NotesMigration
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|()
return|;
block|}
DECL|method|NotesMigration ()
specifier|public
name|NotesMigration
parameter_list|()
block|{}
block|}
end_class

end_unit


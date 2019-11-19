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
name|checkState
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|OBJ_BLOB
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
name|ImmutableList
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
name|common
operator|.
name|UsedAt
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|errors
operator|.
name|ConfigInvalidException
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
name|ObjectId
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
name|ObjectReader
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
name|util
operator|.
name|MutableInteger
import|;
end_import

begin_comment
comment|/**  * Data stored in a note, parsed on demand. The data type to parse into is a generic list of type T.  * The source of the data is a array of raw bytes  */
end_comment

begin_class
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGIN_CHECKS
argument_list|)
DECL|class|RevisionNote
specifier|public
specifier|abstract
class|class
name|RevisionNote
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|MAX_NOTE_SZ
specifier|static
specifier|final
name|int
name|MAX_NOTE_SZ
init|=
literal|25
operator|<<
literal|20
decl_stmt|;
DECL|method|trimLeadingEmptyLines (byte[] bytes, MutableInteger p)
specifier|protected
specifier|static
name|void
name|trimLeadingEmptyLines
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|MutableInteger
name|p
parameter_list|)
block|{
while|while
condition|(
name|p
operator|.
name|value
operator|<
name|bytes
operator|.
name|length
operator|&&
name|bytes
index|[
name|p
operator|.
name|value
index|]
operator|==
literal|'\n'
condition|)
block|{
name|p
operator|.
name|value
operator|++
expr_stmt|;
block|}
block|}
DECL|field|reader
specifier|private
specifier|final
name|ObjectReader
name|reader
decl_stmt|;
DECL|field|noteId
specifier|private
specifier|final
name|ObjectId
name|noteId
decl_stmt|;
DECL|field|raw
specifier|private
name|byte
index|[]
name|raw
decl_stmt|;
DECL|field|entities
specifier|private
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|entities
decl_stmt|;
DECL|method|RevisionNote (ObjectReader reader, ObjectId noteId)
specifier|public
name|RevisionNote
parameter_list|(
name|ObjectReader
name|reader
parameter_list|,
name|ObjectId
name|noteId
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|reader
expr_stmt|;
name|this
operator|.
name|noteId
operator|=
name|noteId
expr_stmt|;
block|}
DECL|method|getRaw ()
specifier|public
name|byte
index|[]
name|getRaw
parameter_list|()
block|{
name|checkParsed
argument_list|()
expr_stmt|;
return|return
name|raw
return|;
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGIN_CHECKS
argument_list|)
DECL|method|getOnlyEntity ()
specifier|public
name|T
name|getOnlyEntity
parameter_list|()
block|{
name|checkParsed
argument_list|()
expr_stmt|;
name|checkState
argument_list|(
name|entities
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|,
literal|"expected exactly one entity"
argument_list|)
expr_stmt|;
return|return
name|entities
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|getEntities ()
specifier|public
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|getEntities
parameter_list|()
block|{
name|checkParsed
argument_list|()
expr_stmt|;
return|return
name|entities
return|;
block|}
comment|/** Reads the raw data, and delegates parsing to the {@link #parse(byte[], int)} method. */
DECL|method|parse ()
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|raw
operator|=
name|reader
operator|.
name|open
argument_list|(
name|noteId
argument_list|,
name|OBJ_BLOB
argument_list|)
operator|.
name|getCachedBytes
argument_list|(
name|MAX_NOTE_SZ
argument_list|)
expr_stmt|;
name|MutableInteger
name|p
init|=
operator|new
name|MutableInteger
argument_list|()
decl_stmt|;
name|trimLeadingEmptyLines
argument_list|(
name|raw
argument_list|,
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|value
operator|>=
name|raw
operator|.
name|length
condition|)
block|{
name|entities
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
return|return;
block|}
name|entities
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|parse
argument_list|(
name|raw
argument_list|,
name|p
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|parse (byte[] raw, int offset)
specifier|protected
specifier|abstract
name|List
argument_list|<
name|T
argument_list|>
name|parse
parameter_list|(
name|byte
index|[]
name|raw
parameter_list|,
name|int
name|offset
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
function_decl|;
DECL|method|checkParsed ()
specifier|protected
name|void
name|checkParsed
parameter_list|()
block|{
name|checkState
argument_list|(
name|raw
operator|!=
literal|null
argument_list|,
literal|"revision note not parsed yet"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


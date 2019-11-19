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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|entities
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
comment|/** Implements the parsing of comment data, handling JSON decoding and push certificates. */
end_comment

begin_class
DECL|class|ChangeRevisionNote
class|class
name|ChangeRevisionNote
extends|extends
name|RevisionNote
argument_list|<
name|Comment
argument_list|>
block|{
DECL|field|noteJson
specifier|private
specifier|final
name|ChangeNoteJson
name|noteJson
decl_stmt|;
DECL|field|status
specifier|private
specifier|final
name|Comment
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|pushCert
specifier|private
name|String
name|pushCert
decl_stmt|;
DECL|method|ChangeRevisionNote ( ChangeNoteJson noteJson, ObjectReader reader, ObjectId noteId, Comment.Status status)
name|ChangeRevisionNote
parameter_list|(
name|ChangeNoteJson
name|noteJson
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|ObjectId
name|noteId
parameter_list|,
name|Comment
operator|.
name|Status
name|status
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|,
name|noteId
argument_list|)
expr_stmt|;
name|this
operator|.
name|noteJson
operator|=
name|noteJson
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getPushCert ()
specifier|public
name|String
name|getPushCert
parameter_list|()
block|{
name|checkParsed
argument_list|()
expr_stmt|;
return|return
name|pushCert
return|;
block|}
annotation|@
name|Override
DECL|method|parse (byte[] raw, int offset)
specifier|protected
name|List
argument_list|<
name|Comment
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
block|{
name|MutableInteger
name|p
init|=
operator|new
name|MutableInteger
argument_list|()
decl_stmt|;
name|p
operator|.
name|value
operator|=
name|offset
expr_stmt|;
name|RevisionNoteData
name|data
init|=
name|parseJson
argument_list|(
name|noteJson
argument_list|,
name|raw
argument_list|,
name|p
operator|.
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|Comment
operator|.
name|Status
operator|.
name|PUBLISHED
condition|)
block|{
name|pushCert
operator|=
name|data
operator|.
name|pushCert
expr_stmt|;
block|}
else|else
block|{
name|pushCert
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|data
operator|.
name|comments
return|;
block|}
DECL|method|parseJson (ChangeNoteJson noteUtil, byte[] raw, int offset)
specifier|private
name|RevisionNoteData
name|parseJson
parameter_list|(
name|ChangeNoteJson
name|noteUtil
parameter_list|,
name|byte
index|[]
name|raw
parameter_list|,
name|int
name|offset
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|raw
argument_list|,
name|offset
argument_list|,
name|raw
operator|.
name|length
operator|-
name|offset
argument_list|)
init|;
name|Reader
name|r
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|UTF_8
argument_list|)
init|)
block|{
return|return
name|noteUtil
operator|.
name|getGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
argument_list|,
name|RevisionNoteData
operator|.
name|class
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


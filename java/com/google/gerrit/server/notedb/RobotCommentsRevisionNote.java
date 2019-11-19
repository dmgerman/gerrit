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
name|RobotComment
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

begin_comment
comment|/** Like {@link RevisionNote} but for robot comments. */
end_comment

begin_class
DECL|class|RobotCommentsRevisionNote
specifier|public
class|class
name|RobotCommentsRevisionNote
extends|extends
name|RevisionNote
argument_list|<
name|RobotComment
argument_list|>
block|{
DECL|field|noteUtil
specifier|private
specifier|final
name|ChangeNoteJson
name|noteUtil
decl_stmt|;
DECL|method|RobotCommentsRevisionNote (ChangeNoteJson noteUtil, ObjectReader reader, ObjectId noteId)
name|RobotCommentsRevisionNote
parameter_list|(
name|ChangeNoteJson
name|noteUtil
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|ObjectId
name|noteId
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
name|noteUtil
operator|=
name|noteUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (byte[] raw, int offset)
specifier|protected
name|List
argument_list|<
name|RobotComment
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
name|RobotCommentsRevisionNoteData
operator|.
name|class
argument_list|)
operator|.
name|comments
return|;
block|}
block|}
block|}
end_class

end_unit


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
name|client
operator|.
name|PatchLineComment
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|RevisionNote
class|class
name|RevisionNote
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
DECL|field|comments
specifier|final
name|ImmutableList
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
decl_stmt|;
DECL|method|RevisionNote (Change.Id changeId, ObjectReader reader, ObjectId noteId)
name|RevisionNote
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|ObjectId
name|noteId
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
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
decl_stmt|;
name|comments
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|CommentsInNotesUtil
operator|.
name|parseNote
argument_list|(
name|bytes
argument_list|,
name|changeId
argument_list|,
name|PatchLineComment
operator|.
name|Status
operator|.
name|PUBLISHED
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


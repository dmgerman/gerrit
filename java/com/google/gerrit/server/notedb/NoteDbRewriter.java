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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ObjectInserter
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
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_interface
DECL|interface|NoteDbRewriter
specifier|public
interface|interface
name|NoteDbRewriter
block|{
comment|/** Gets the name of the target ref which will be rewritten. */
DECL|method|getRefName ()
name|String
name|getRefName
parameter_list|()
function_decl|;
comment|/**    * Rewrites the commit history.    *    * @param revWalk a {@code RevWalk} instance.    * @param inserter a {@code ObjectInserter} instance.    * @param currTip the {@code ObjectId} of the ref's tip commit.    * @return the {@code ObjectId} of the ref's new tip commit.    */
DECL|method|rewriteCommitHistory (RevWalk revWalk, ObjectInserter inserter, ObjectId currTip)
name|ObjectId
name|rewriteCommitHistory
parameter_list|(
name|RevWalk
name|revWalk
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|,
name|ObjectId
name|currTip
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
function_decl|;
block|}
end_interface

end_unit


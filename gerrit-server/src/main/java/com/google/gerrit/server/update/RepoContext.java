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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_comment
comment|/** Context for performing the {@link BatchUpdateOp#updateRepo} phase. */
end_comment

begin_interface
DECL|interface|RepoContext
specifier|public
interface|interface
name|RepoContext
extends|extends
name|Context
block|{
comment|/**    * @return inserter for writing to the repo. Callers should not flush; the walk returned by {@link    *     #getRevWalk()} is able to read back objects inserted by this inserter without flushing    *     first.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|getInserter ()
name|ObjectInserter
name|getInserter
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Add a command to the pending list of commands.    *    *<p>Adding commands to the {@code RepoContext} is the only way of updating refs in the    * repository from a {@link BatchUpdateOp}.    *    * @param cmd ref update command.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|addRefUpdate (ReceiveCommand cmd)
name|void
name|addRefUpdate
parameter_list|(
name|ReceiveCommand
name|cmd
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Add a command to the pending list of commands.    *    *<p>Adding commands to the {@code RepoContext} is the only way of updating refs in the    * repository from a {@link BatchUpdateOp}.    *    * @param oldId the old object ID; must not be null. Use {@link ObjectId#zeroId()} for ref    *     creation.    * @param newId the new object ID; must not be null. Use {@link ObjectId#zeroId()} for ref    *     deletion.    * @param refName the ref name.    * @throws IOException if an error occurred opening the repo.    */
DECL|method|addRefUpdate (ObjectId oldId, ObjectId newId, String refName)
specifier|default
name|void
name|addRefUpdate
parameter_list|(
name|ObjectId
name|oldId
parameter_list|,
name|ObjectId
name|newId
parameter_list|,
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
block|{
name|addRefUpdate
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|oldId
argument_list|,
name|newId
argument_list|,
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_interface

end_unit


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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|checkArgument
import|;
end_import

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
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|BatchRefUpdate
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Collection of {@link ReceiveCommand}s that supports multiple updates per ref.  *<p>  * The underlying behavior of {@link BatchRefUpdate} is undefined (an  * implementations vary) when more than one command per ref is added. This class  * works around that limitation by allowing multiple updates per ref, as long as  * the previous new SHA-1 matches the next old SHA-1.  */
end_comment

begin_class
DECL|class|ChainedReceiveCommands
specifier|public
class|class
name|ChainedReceiveCommands
block|{
DECL|field|commands
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ReceiveCommand
argument_list|>
name|commands
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** @return true if no commands have been added. */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|commands
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**    * Add a command.    *    * @param cmd command to add. If a command has been previously added for the    *     same ref, the new SHA-1 of the most recent previous command must match    *     the old SHA-1 of this command.    */
DECL|method|add (ReceiveCommand cmd)
specifier|public
name|void
name|add
parameter_list|(
name|ReceiveCommand
name|cmd
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
name|cmd
operator|.
name|getOldId
argument_list|()
operator|.
name|equals
argument_list|(
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|)
argument_list|,
literal|"ref update is a no-op: %s"
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
name|ReceiveCommand
name|old
init|=
name|commands
operator|.
name|get
argument_list|(
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|==
literal|null
condition|)
block|{
name|commands
operator|.
name|put
argument_list|(
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
return|return;
block|}
name|checkArgument
argument_list|(
name|old
operator|.
name|getResult
argument_list|()
operator|==
name|ReceiveCommand
operator|.
name|Result
operator|.
name|NOT_ATTEMPTED
argument_list|,
literal|"cannot chain ref update %s after update %s with result %s"
argument_list|,
name|cmd
argument_list|,
name|old
argument_list|,
name|old
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|cmd
operator|.
name|getOldId
argument_list|()
operator|.
name|equals
argument_list|(
name|old
operator|.
name|getNewId
argument_list|()
argument_list|)
argument_list|,
literal|"cannot chain ref update %s after update %s with different new ID"
argument_list|,
name|cmd
argument_list|,
name|old
argument_list|)
expr_stmt|;
name|commands
operator|.
name|put
argument_list|(
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
operator|new
name|ReceiveCommand
argument_list|(
name|old
operator|.
name|getOldId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add commands from this instance to a native JGit batch update.    *<p>    * Exactly one command per ref will be added to the update. The old SHA-1 will    * be the old SHA-1 of the first command added to this instance for that ref;    * the new SHA-1 will be the new SHA-1 of the last command.    *    * @param bru batch update    */
DECL|method|addTo (BatchRefUpdate bru)
specifier|public
name|void
name|addTo
parameter_list|(
name|BatchRefUpdate
name|bru
parameter_list|)
block|{
name|checkState
argument_list|(
operator|!
name|isEmpty
argument_list|()
argument_list|,
literal|"no commands to add"
argument_list|)
expr_stmt|;
for|for
control|(
name|ReceiveCommand
name|cmd
range|:
name|commands
operator|.
name|values
argument_list|()
control|)
block|{
name|bru
operator|.
name|addCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


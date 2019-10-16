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
DECL|package|com.google.gerrit.server.git.validators
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
operator|.
name|validators
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|ImmutableMap
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
name|Project
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|server
operator|.
name|git
operator|.
name|RefCache
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
name|server
operator|.
name|update
operator|.
name|ChainedReceiveCommands
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
name|server
operator|.
name|validators
operator|.
name|ValidationException
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
name|Optional
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
name|revwalk
operator|.
name|RevWalk
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
comment|/**  * Listener to validate ref updates performed during submit operation.  *  *<p>As submit strategies may generate new commits (e.g. Cherry Pick), this listener allows  * validation of resulting new commit before destination branch is updated and new patchset ref is  * created.  *  *<p>If you only care about validating the change being submitted and not the resulting new commit,  * consider using {@link MergeValidationListener} instead.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|OnSubmitValidationListener
specifier|public
interface|interface
name|OnSubmitValidationListener
block|{
DECL|class|Arguments
class|class
name|Arguments
block|{
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|rw
specifier|private
name|RevWalk
name|rw
decl_stmt|;
DECL|field|commands
specifier|private
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ReceiveCommand
argument_list|>
name|commands
decl_stmt|;
DECL|field|refs
specifier|private
name|RefCache
name|refs
decl_stmt|;
comment|/**      * @param project project.      * @param rw revwalk that can read unflushed objects from {@code refs}.      * @param commands commands to be executed.      */
DECL|method|Arguments (Project.NameKey project, RevWalk rw, ChainedReceiveCommands commands)
name|Arguments
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ChainedReceiveCommands
name|commands
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|requireNonNull
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|requireNonNull
argument_list|(
name|rw
argument_list|)
expr_stmt|;
name|this
operator|.
name|refs
operator|=
name|requireNonNull
argument_list|(
name|commands
argument_list|)
expr_stmt|;
name|this
operator|.
name|commands
operator|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|commands
operator|.
name|getCommands
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Get the project name for this operation. */
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
comment|/**      * Get a revwalk for this operation.      *      *<p>This instance is able to read all objects mentioned in {@link #getCommands()} and {@link      * #getRef(String)}.      *      * @return open revwalk.      */
DECL|method|getRevWalk ()
specifier|public
name|RevWalk
name|getRevWalk
parameter_list|()
block|{
return|return
name|rw
return|;
block|}
comment|/**      * @return a map from ref to commands covering all ref operations to be performed on this      *     repository as part of the ongoing submit operation.      */
DECL|method|getCommands ()
specifier|public
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ReceiveCommand
argument_list|>
name|getCommands
parameter_list|()
block|{
return|return
name|commands
return|;
block|}
comment|/**      * Get a ref from the repository.      *      * @param name ref name; can be any ref, not just the ones mentioned in {@link #getCommands()}.      * @return latest value of a ref in the repository, as if all commands from {@link      *     #getCommands()} had already been applied.      * @throws IOException if an error occurred reading the ref.      */
DECL|method|getRef (String name)
specifier|public
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|getRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|refs
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
comment|/**    * Called right before branch is updated with new commit or commits as a result of submit.    *    *<p>If ValidationException is thrown, submitting is aborted.    */
DECL|method|preBranchUpdate (Arguments args)
name|void
name|preBranchUpdate
parameter_list|(
name|Arguments
name|args
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit


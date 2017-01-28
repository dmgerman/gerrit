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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
operator|.
name|NameKey
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
name|lib
operator|.
name|Repository
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
comment|/**  * Listener to validate ref updates performed during submit operation.  *  * As submit strategies may generate new commits (e.g. Cherry Pick), this  * listener allows validation of resulting new commit before destination branch  * is updated and new patchset ref is created.  *  * If you only care about validating the change being submitted and not the  * resulting new commit, consider using {@link MergeValidationListener} instead.  */
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
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
DECL|field|objectReader
specifier|private
name|ObjectReader
name|objectReader
decl_stmt|;
DECL|field|commands
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ReceiveCommand
argument_list|>
name|commands
decl_stmt|;
DECL|method|Arguments (NameKey project, Repository repository, ObjectReader objectReader, Map<String, ReceiveCommand> commands)
specifier|public
name|Arguments
parameter_list|(
name|NameKey
name|project
parameter_list|,
name|Repository
name|repository
parameter_list|,
name|ObjectReader
name|objectReader
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|objectReader
operator|=
name|objectReader
expr_stmt|;
name|this
operator|.
name|commands
operator|=
name|commands
expr_stmt|;
block|}
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
comment|/**      * @return a read only repository      */
DECL|method|getRepository ()
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
DECL|method|newRevWalk ()
specifier|public
name|RevWalk
name|newRevWalk
parameter_list|()
block|{
return|return
operator|new
name|RevWalk
argument_list|(
name|objectReader
argument_list|)
return|;
block|}
comment|/**      * @return a map from ref to op on it covering all ref ops to be performed      *         on this repository as part of ongoing submit operation.      */
DECL|method|getCommands ()
specifier|public
name|Map
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
block|}
comment|/**    * Called right before branch is updated with new commit or commits as a    * result of submit.    *    * If ValidationException is thrown, submitting is aborted.    */
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


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|project
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|acceptance
operator|.
name|testsuite
operator|.
name|ThrowingFunction
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
name|client
operator|.
name|SubmitType
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

begin_class
annotation|@
name|AutoValue
DECL|class|TestProjectCreation
specifier|public
specifier|abstract
class|class
name|TestProjectCreation
block|{
DECL|method|name ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|name
parameter_list|()
function_decl|;
DECL|method|parent ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|parent
parameter_list|()
function_decl|;
DECL|method|createEmptyCommit ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Boolean
argument_list|>
name|createEmptyCommit
parameter_list|()
function_decl|;
DECL|method|submitType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|SubmitType
argument_list|>
name|submitType
parameter_list|()
function_decl|;
DECL|method|projectCreator ()
specifier|abstract
name|ThrowingFunction
argument_list|<
name|TestProjectCreation
argument_list|,
name|Project
operator|.
name|NameKey
argument_list|>
name|projectCreator
parameter_list|()
function_decl|;
DECL|method|builder ( ThrowingFunction<TestProjectCreation, Project.NameKey> projectCreator)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|ThrowingFunction
argument_list|<
name|TestProjectCreation
argument_list|,
name|Project
operator|.
name|NameKey
argument_list|>
name|projectCreator
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestProjectCreation
operator|.
name|Builder
argument_list|()
operator|.
name|projectCreator
argument_list|(
name|projectCreator
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|name (String name)
specifier|public
specifier|abstract
name|TestProjectCreation
operator|.
name|Builder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|parent (Project.NameKey parent)
specifier|public
specifier|abstract
name|TestProjectCreation
operator|.
name|Builder
name|parent
parameter_list|(
name|Project
operator|.
name|NameKey
name|parent
parameter_list|)
function_decl|;
DECL|method|submitType (SubmitType submitType)
specifier|public
specifier|abstract
name|TestProjectCreation
operator|.
name|Builder
name|submitType
parameter_list|(
name|SubmitType
name|submitType
parameter_list|)
function_decl|;
DECL|method|createEmptyCommit (boolean value)
specifier|public
specifier|abstract
name|TestProjectCreation
operator|.
name|Builder
name|createEmptyCommit
parameter_list|(
name|boolean
name|value
parameter_list|)
function_decl|;
comment|/** Skips the empty commit on creation. This means that project's branches will not exist. */
DECL|method|noEmptyCommit ()
specifier|public
name|TestProjectCreation
operator|.
name|Builder
name|noEmptyCommit
parameter_list|()
block|{
return|return
name|createEmptyCommit
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|projectCreator ( ThrowingFunction<TestProjectCreation, Project.NameKey> projectCreator)
specifier|abstract
name|TestProjectCreation
operator|.
name|Builder
name|projectCreator
parameter_list|(
name|ThrowingFunction
argument_list|<
name|TestProjectCreation
argument_list|,
name|Project
operator|.
name|NameKey
argument_list|>
name|projectCreator
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|TestProjectCreation
name|autoBuild
parameter_list|()
function_decl|;
comment|/**      * Executes the project creation as specified.      *      * @return the name of the created project      */
DECL|method|create ()
specifier|public
name|Project
operator|.
name|NameKey
name|create
parameter_list|()
block|{
name|TestProjectCreation
name|creation
init|=
name|autoBuild
argument_list|()
decl_stmt|;
return|return
name|creation
operator|.
name|projectCreator
argument_list|()
operator|.
name|applyAndThrowSilently
argument_list|(
name|creation
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


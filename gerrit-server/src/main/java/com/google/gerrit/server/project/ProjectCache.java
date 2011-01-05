begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_comment
comment|/** Cache of project information, including access rights. */
end_comment

begin_interface
DECL|interface|ProjectCache
specifier|public
interface|interface
name|ProjectCache
block|{
comment|/**    * Get the cached data for a project by its unique name.    *    * @param projectName name of the project.    * @return the cached data; null if no such project exists.    */
DECL|method|get (Project.NameKey projectName)
specifier|public
name|ProjectState
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
function_decl|;
comment|/** Invalidate the cached information about the given project. */
DECL|method|evict (Project p)
specifier|public
name|void
name|evict
parameter_list|(
name|Project
name|p
parameter_list|)
function_decl|;
comment|/** @return sorted iteration of projects. */
DECL|method|all ()
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|all
parameter_list|()
function_decl|;
comment|/**    * Filter the set of registered project names by common prefix.    *    * @param prefix common prefix.    * @return sorted iteration of projects sharing the same prefix.    */
DECL|method|byName (String prefix)
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|byName
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
comment|/** Notify the cache that a new project was constructed. */
DECL|method|onCreateProject (Project.NameKey newProjectName)
specifier|public
name|void
name|onCreateProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|newProjectName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


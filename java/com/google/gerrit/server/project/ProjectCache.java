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
name|common
operator|.
name|collect
operator|.
name|ImmutableSortedSet
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
name|common
operator|.
name|Nullable
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
name|AccountGroup
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
name|Set
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
comment|/** @return the parent state for all projects on this server. */
DECL|method|getAllProjects ()
name|ProjectState
name|getAllProjects
parameter_list|()
function_decl|;
comment|/** @return the project state of the project storing meta data for all users. */
DECL|method|getAllUsers ()
name|ProjectState
name|getAllUsers
parameter_list|()
function_decl|;
comment|/**    * Get the cached data for a project by its unique name.    *    * @param projectName name of the project.    * @return the cached data; null if no such project exists, projectName is null or an error    *     occurred.    * @see #checkedGet(com.google.gerrit.entities.Project.NameKey)    */
DECL|method|get (@ullable Project.NameKey projectName)
name|ProjectState
name|get
parameter_list|(
annotation|@
name|Nullable
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
function_decl|;
comment|/**    * Get the cached data for a project by its unique name.    *    * @param projectName name of the project.    * @throws IOException when there was an error.    * @return the cached data; null if no such project exists or projectName is null.    */
DECL|method|checkedGet (@ullable Project.NameKey projectName)
name|ProjectState
name|checkedGet
parameter_list|(
annotation|@
name|Nullable
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Get the cached data for a project by its unique name.    *    * @param projectName name of the project.    * @param strict true when any error generates an exception    * @throws Exception in case of any error (strict = true) or only for I/O or other internal    *     errors.    * @return the cached data or null when strict = false    */
DECL|method|checkedGet (Project.NameKey projectName, boolean strict)
name|ProjectState
name|checkedGet
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|boolean
name|strict
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**    * Invalidate the cached information about the given project, and triggers reindexing for it    *    * @param p project that is being evicted    * @throws IOException thrown if the reindexing fails    */
DECL|method|evict (Project p)
name|void
name|evict
parameter_list|(
name|Project
name|p
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Invalidate the cached information about the given project, and triggers reindexing for it    *    * @param p the NameKey of the project that is being evicted    * @throws IOException thrown if the reindexing fails    */
DECL|method|evict (Project.NameKey p)
name|void
name|evict
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Remove information about the given project from the cache. It will no longer be returned from    * {@link #all()}.    */
DECL|method|remove (Project p)
name|void
name|remove
parameter_list|(
name|Project
name|p
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Remove information about the given project from the cache. It will no longer be returned from    * {@link #all()}.    */
DECL|method|remove (Project.NameKey name)
name|void
name|remove
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/** @return sorted iteration of projects. */
DECL|method|all ()
name|ImmutableSortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|all
parameter_list|()
function_decl|;
comment|/**    * @return estimated set of relevant groups extracted from hot project access rules. If the cache    *     is cold or too small for the entire project set of the server, this set may be incomplete.    */
DECL|method|guessRelevantGroupUUIDs ()
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|guessRelevantGroupUUIDs
parameter_list|()
function_decl|;
comment|/**    * Filter the set of registered project names by common prefix.    *    * @param prefix common prefix.    * @return sorted iteration of projects sharing the same prefix.    */
DECL|method|byName (String prefix)
name|ImmutableSortedSet
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
name|void
name|onCreateProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|newProjectName
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit


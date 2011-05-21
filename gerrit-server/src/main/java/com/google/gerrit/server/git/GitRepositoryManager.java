begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|RepositoryNotFoundException
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
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Manages Git repositories for the Gerrit server process.  *<p>  * Implementations of this interface should be a {@link Singleton} and  * registered in Guice so they are globally available within the server  * environment.  */
end_comment

begin_interface
DECL|interface|GitRepositoryManager
specifier|public
interface|interface
name|GitRepositoryManager
block|{
comment|/** Notes branch successful reviews are written to after being merged. */
DECL|field|REFS_NOTES_REVIEW
specifier|public
specifier|static
specifier|final
name|String
name|REFS_NOTES_REVIEW
init|=
literal|"refs/notes/review"
decl_stmt|;
comment|/** Note tree listing commits we refuse {@code refs/meta/reject-commits} */
DECL|field|REF_REJECT_COMMITS
specifier|public
specifier|static
specifier|final
name|String
name|REF_REJECT_COMMITS
init|=
literal|"refs/meta/reject-commits"
decl_stmt|;
comment|/** Configuration settings for a project {@code refs/meta/config} */
DECL|field|REF_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|REF_CONFIG
init|=
literal|"refs/meta/config"
decl_stmt|;
comment|/**    * Get (or open) a repository by name.    *    * @param name the repository name, relative to the base directory.    * @return the cached Repository instance. Caller must call {@code close()}    *         when done to decrement the resource handle.    * @throws RepositoryNotFoundException the name does not denote an existing    *         repository, or the name cannot be read as a repository.    */
DECL|method|openRepository (Project.NameKey name)
specifier|public
specifier|abstract
name|Repository
name|openRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
function_decl|;
comment|/**    * Create (and open) a repository by name.    *    * @param name the repository name, relative to the base directory.    * @return the cached Repository instance. Caller must call {@code close()}    *         when done to decrement the resource handle.    * @throws RepositoryNotFoundException the name does not denote an existing    *         repository, or the name cannot be read as a repository.    */
DECL|method|createRepository (Project.NameKey name)
specifier|public
specifier|abstract
name|Repository
name|createRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
function_decl|;
comment|/** @return set of all known projects, sorted by natural NameKey order. */
DECL|method|list ()
specifier|public
specifier|abstract
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|list
parameter_list|()
function_decl|;
comment|/**    * Read the {@code GIT_DIR/description} file for gitweb.    *<p>    * NB: This code should really be in JGit, as a member of the Repository    * object. Until it moves there, its here.    *    * @param name the repository name, relative to the base directory.    * @return description text; null if no description has been configured.    * @throws RepositoryNotFoundException the named repository does not exist.    * @throws IOException the description file exists, but is not readable by    *         this process.    */
DECL|method|getProjectDescription (Project.NameKey name)
specifier|public
specifier|abstract
name|String
name|getProjectDescription
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
function_decl|;
comment|/**    * Set the {@code GIT_DIR/description} file for gitweb.    *<p>    * NB: This code should really be in JGit, as a member of the Repository    * object. Until it moves there, its here.    *    * @param name the repository name, relative to the base directory.    * @param description new description text for the repository.    */
DECL|method|setProjectDescription (Project.NameKey name, final String description)
specifier|public
specifier|abstract
name|void
name|setProjectDescription
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|,
specifier|final
name|String
name|description
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


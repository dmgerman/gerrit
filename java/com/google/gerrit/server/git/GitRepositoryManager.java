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
name|inject
operator|.
name|ImplementedBy
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

begin_comment
comment|/**  * Manages Git repositories for the Gerrit server process.  *  *<p>Implementations of this interface should be a {@link Singleton} and registered in Guice so  * they are globally available within the server environment.  */
end_comment

begin_interface
annotation|@
name|ImplementedBy
argument_list|(
name|value
operator|=
name|LocalDiskRepositoryManager
operator|.
name|class
argument_list|)
DECL|interface|GitRepositoryManager
specifier|public
interface|interface
name|GitRepositoryManager
block|{
comment|/**    * Get (or open) a repository by name.    *    * @param name the repository name, relative to the base directory.    * @return the cached Repository instance. Caller must call {@code close()} when done to decrement    *     the resource handle.    * @throws RepositoryNotFoundException the name does not denote an existing repository.    * @throws IOException the name cannot be read as a repository.    */
DECL|method|openRepository (Project.NameKey name)
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
throws|,
name|IOException
function_decl|;
comment|/**    * Create (and open) a repository by name.    *    * @param name the repository name, relative to the base directory.    * @return the cached Repository instance. Caller must call {@code close()} when done to decrement    *     the resource handle.    * @throws RepositoryCaseMismatchException the name collides with an existing repository name, but    *     only in case of a character within the name.    * @throws RepositoryNotFoundException the name is invalid.    * @throws IOException the repository cannot be created.    */
DECL|method|createRepository (Project.NameKey name)
name|Repository
name|createRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryCaseMismatchException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
function_decl|;
comment|/** @return set of all known projects, sorted by natural NameKey order. */
DECL|method|list ()
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|list
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
package|;
end_package

begin_import
import|import
name|org
operator|.
name|spearce
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
name|File
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
name|lang
operator|.
name|ref
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/** Cache of active Git repositories being used by the manager. */
end_comment

begin_class
DECL|class|RepositoryCache
specifier|public
class|class
name|RepositoryCache
block|{
DECL|field|REPO_NAME
specifier|private
specifier|static
specifier|final
name|Pattern
name|REPO_NAME
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[A-Za-z][A-Za-z0-9/_-]+$"
argument_list|)
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|File
name|base
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Reference
argument_list|<
name|Repository
argument_list|>
argument_list|>
name|cache
decl_stmt|;
comment|/**    * Create a new cache to manage a specific base directory (and below).    *     * @param basedir top level directory that contains all repositories.    */
DECL|method|RepositoryCache (final File basedir)
specifier|public
name|RepositoryCache
parameter_list|(
specifier|final
name|File
name|basedir
parameter_list|)
block|{
name|base
operator|=
name|basedir
expr_stmt|;
name|cache
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Reference
argument_list|<
name|Repository
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**    * @return the base directory which contains all known repositories.    */
DECL|method|getBaseDirectory ()
specifier|public
name|File
name|getBaseDirectory
parameter_list|()
block|{
return|return
name|base
return|;
block|}
comment|/**    * Get (or open) a repository by name.    *     * @param name the repository name, relative to the base directory supplied    *        when the cache was created.    * @return the cached Repository instance.    * @throws InvalidRepositoryException the name does not denote an existing    *         repository, or the name cannot be read as a repository.    */
DECL|method|get (String name)
specifier|public
specifier|synchronized
name|Repository
name|get
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|InvalidRepositoryException
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|REPO_NAME
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InvalidRepositoryException
argument_list|(
name|name
argument_list|)
throw|;
block|}
specifier|final
name|Reference
argument_list|<
name|Repository
argument_list|>
name|ref
init|=
name|cache
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Repository
name|db
init|=
name|ref
operator|!=
literal|null
condition|?
name|ref
operator|.
name|get
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|db
operator|=
name|GitMetaUtil
operator|.
name|open
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidRepositoryException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidRepositoryException
argument_list|(
name|name
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|cache
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|SoftReference
argument_list|<
name|Repository
argument_list|>
argument_list|(
name|db
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|db
return|;
block|}
block|}
end_class

end_unit


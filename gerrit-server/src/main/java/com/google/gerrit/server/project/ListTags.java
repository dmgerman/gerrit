begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|ImmutableMap
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
name|Lists
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
name|common
operator|.
name|TagInfo
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
name|restapi
operator|.
name|IdString
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|restapi
operator|.
name|RestReadView
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
name|server
operator|.
name|ReviewDb
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
name|CommonConverters
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
name|ChangeCache
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
name|GitRepositoryManager
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
name|TagCache
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
name|VisibleRefFilter
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
name|Inject
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
name|Provider
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
name|MissingObjectException
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
name|Constants
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
name|Ref
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
name|RevObject
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
name|RevTag
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_class
annotation|@
name|Singleton
DECL|class|ListTags
specifier|public
class|class
name|ListTags
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|tagCache
specifier|private
specifier|final
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|changeCache
specifier|private
specifier|final
name|ChangeCache
name|changeCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListTags (GitRepositoryManager repoManager, Provider<ReviewDb> dbProvider, TagCache tagCache, ChangeCache changeCache)
specifier|public
name|ListTags
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|TagCache
name|tagCache
parameter_list|,
name|ChangeCache
name|changeCache
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|tagCache
operator|=
name|tagCache
expr_stmt|;
name|this
operator|.
name|changeCache
operator|=
name|changeCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource resource)
specifier|public
name|List
argument_list|<
name|TagInfo
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|resource
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
block|{
name|List
argument_list|<
name|TagInfo
argument_list|>
name|tags
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
name|getRepository
argument_list|(
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|all
init|=
name|visibleTags
argument_list|(
name|resource
operator|.
name|getControl
argument_list|()
argument_list|,
name|repo
argument_list|,
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|Constants
operator|.
name|R_TAGS
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|all
operator|.
name|values
argument_list|()
control|)
block|{
name|tags
operator|.
name|add
argument_list|(
name|createTagInfo
argument_list|(
name|ref
argument_list|,
name|rw
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|rw
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|tags
argument_list|,
operator|new
name|Comparator
argument_list|<
name|TagInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|TagInfo
name|a
parameter_list|,
name|TagInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|ref
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|ref
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|tags
return|;
block|}
DECL|method|get (ProjectResource resource, IdString id)
specifier|public
name|TagInfo
name|get
parameter_list|(
name|ProjectResource
name|resource
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|getRepository
argument_list|(
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|String
name|tagName
init|=
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|tagName
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_TAGS
argument_list|)
condition|)
block|{
name|tagName
operator|=
name|Constants
operator|.
name|R_TAGS
operator|+
name|tagName
expr_stmt|;
block|}
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|tagName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
operator|!
name|visibleTags
argument_list|(
name|resource
operator|.
name|getControl
argument_list|()
argument_list|,
name|repo
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|createTagInfo
argument_list|(
name|ref
argument_list|,
name|rw
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
DECL|method|getRepository (Project.NameKey project)
specifier|private
name|Repository
name|getRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
try|try
block|{
return|return
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|noGitRepository
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
block|}
DECL|method|visibleTags (ProjectControl control, Repository repo, Map<String, Ref> tags)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|visibleTags
parameter_list|(
name|ProjectControl
name|control
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|tags
parameter_list|)
block|{
return|return
operator|new
name|VisibleRefFilter
argument_list|(
name|tagCache
argument_list|,
name|changeCache
argument_list|,
name|repo
argument_list|,
name|control
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|filter
argument_list|(
name|tags
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|createTagInfo (Ref ref, RevWalk rw)
specifier|private
specifier|static
name|TagInfo
name|createTagInfo
parameter_list|(
name|Ref
name|ref
parameter_list|,
name|RevWalk
name|rw
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IOException
block|{
name|RevObject
name|object
init|=
name|rw
operator|.
name|parseAny
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|RevTag
condition|)
block|{
name|RevTag
name|tag
init|=
operator|(
name|RevTag
operator|)
name|object
decl_stmt|;
comment|// Annotated or signed tag
return|return
operator|new
name|TagInfo
argument_list|(
name|Constants
operator|.
name|R_TAGS
operator|+
name|tag
operator|.
name|getTagName
argument_list|()
argument_list|,
name|tag
operator|.
name|getName
argument_list|()
argument_list|,
name|tag
operator|.
name|getObject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|tag
operator|.
name|getFullMessage
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|tag
operator|.
name|getTaggerIdent
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// Lightweight tag
return|return
operator|new
name|TagInfo
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


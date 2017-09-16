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
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ProjectApi
operator|.
name|ListRefsRequest
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
name|api
operator|.
name|projects
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
name|common
operator|.
name|WebLinkInfo
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
name|BadRequestException
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
name|CurrentUser
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
name|WebLinks
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
name|VisibleRefFilter
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|RefPermission
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
name|ArrayList
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
name|PersonIdent
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
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
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|refFilterFactory
specifier|private
specifier|final
name|VisibleRefFilter
operator|.
name|Factory
name|refFilterFactory
decl_stmt|;
DECL|field|links
specifier|private
specifier|final
name|WebLinks
name|links
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--limit"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"maximum number of tags to list"
argument_list|)
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--start"
argument_list|,
name|aliases
operator|=
block|{
literal|"-S"
block|,
literal|"-s"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"number of tags to skip"
argument_list|)
DECL|method|setStart (int start)
specifier|public
name|void
name|setStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--match"
argument_list|,
name|aliases
operator|=
block|{
literal|"-m"
block|}
argument_list|,
name|metaVar
operator|=
literal|"MATCH"
argument_list|,
name|usage
operator|=
literal|"match tags substring"
argument_list|)
DECL|method|setMatchSubstring (String matchSubstring)
specifier|public
name|void
name|setMatchSubstring
parameter_list|(
name|String
name|matchSubstring
parameter_list|)
block|{
name|this
operator|.
name|matchSubstring
operator|=
name|matchSubstring
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--regex"
argument_list|,
name|aliases
operator|=
block|{
literal|"-r"
block|}
argument_list|,
name|metaVar
operator|=
literal|"REGEX"
argument_list|,
name|usage
operator|=
literal|"match tags regex"
argument_list|)
DECL|method|setMatchRegex (String matchRegex)
specifier|public
name|void
name|setMatchRegex
parameter_list|(
name|String
name|matchRegex
parameter_list|)
block|{
name|this
operator|.
name|matchRegex
operator|=
name|matchRegex
expr_stmt|;
block|}
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|matchSubstring
specifier|private
name|String
name|matchSubstring
decl_stmt|;
DECL|field|matchRegex
specifier|private
name|String
name|matchRegex
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListTags ( GitRepositoryManager repoManager, PermissionBackend permissionBackend, Provider<CurrentUser> user, VisibleRefFilter.Factory refFilterFactory, WebLinks webLinks)
specifier|public
name|ListTags
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|VisibleRefFilter
operator|.
name|Factory
name|refFilterFactory
parameter_list|,
name|WebLinks
name|webLinks
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
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|refFilterFactory
operator|=
name|refFilterFactory
expr_stmt|;
name|this
operator|.
name|links
operator|=
name|webLinks
expr_stmt|;
block|}
DECL|method|request (ListRefsRequest<TagInfo> request)
specifier|public
name|ListTags
name|request
parameter_list|(
name|ListRefsRequest
argument_list|<
name|TagInfo
argument_list|>
name|request
parameter_list|)
block|{
name|this
operator|.
name|setLimit
argument_list|(
name|request
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setStart
argument_list|(
name|request
operator|.
name|getStart
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setMatchSubstring
argument_list|(
name|request
operator|.
name|getSubstring
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setMatchRegex
argument_list|(
name|request
operator|.
name|getRegex
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
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
throws|,
name|BadRequestException
block|{
name|List
argument_list|<
name|TagInfo
argument_list|>
name|tags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|PermissionBackend
operator|.
name|ForProject
name|perm
init|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
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
name|getProjectState
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
name|perm
operator|.
name|ref
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ref
argument_list|,
name|rw
argument_list|,
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|links
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
operator|new
name|RefFilter
argument_list|<
name|TagInfo
argument_list|>
argument_list|(
name|Constants
operator|.
name|R_TAGS
argument_list|)
operator|.
name|start
argument_list|(
name|start
argument_list|)
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
operator|.
name|subString
argument_list|(
name|matchSubstring
argument_list|)
operator|.
name|regex
argument_list|(
name|matchRegex
argument_list|)
operator|.
name|filter
argument_list|(
name|tags
argument_list|)
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
name|getProjectState
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
name|permissionBackend
operator|.
name|user
argument_list|(
name|resource
operator|.
name|getUser
argument_list|()
argument_list|)
operator|.
name|project
argument_list|(
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|ref
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|ref
argument_list|,
name|rw
argument_list|,
name|resource
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|links
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
DECL|method|createTagInfo ( PermissionBackend.ForRef perm, Ref ref, RevWalk rw, Project.NameKey projectName, WebLinks links)
specifier|public
specifier|static
name|TagInfo
name|createTagInfo
parameter_list|(
name|PermissionBackend
operator|.
name|ForRef
name|perm
parameter_list|,
name|Ref
name|ref
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|WebLinks
name|links
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
name|boolean
name|canDelete
init|=
name|perm
operator|.
name|testOrFalse
argument_list|(
name|RefPermission
operator|.
name|DELETE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
init|=
name|links
operator|.
name|getTagLinks
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
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
comment|// Annotated or signed tag
name|RevTag
name|tag
init|=
operator|(
name|RevTag
operator|)
name|object
decl_stmt|;
name|PersonIdent
name|tagger
init|=
name|tag
operator|.
name|getTaggerIdent
argument_list|()
decl_stmt|;
return|return
operator|new
name|TagInfo
argument_list|(
name|ref
operator|.
name|getName
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
name|tagger
operator|!=
literal|null
condition|?
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|tag
operator|.
name|getTaggerIdent
argument_list|()
argument_list|)
else|:
literal|null
argument_list|,
name|canDelete
argument_list|,
name|webLinks
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|webLinks
argument_list|)
return|;
block|}
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
argument_list|,
name|canDelete
argument_list|,
name|webLinks
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|webLinks
argument_list|)
return|;
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
DECL|method|visibleTags (ProjectState state, Repository repo, Map<String, Ref> tags)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|visibleTags
parameter_list|(
name|ProjectState
name|state
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
name|refFilterFactory
operator|.
name|create
argument_list|(
name|state
argument_list|,
name|repo
argument_list|)
operator|.
name|setShowMetadata
argument_list|(
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
block|}
end_class

end_unit


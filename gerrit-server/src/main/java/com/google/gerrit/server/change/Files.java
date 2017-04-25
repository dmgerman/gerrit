begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|FileInfo
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
name|registration
operator|.
name|DynamicItem
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
name|registration
operator|.
name|DynamicMap
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
name|AuthException
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
name|CacheControl
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
name|ChildCollection
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
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|Account
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
name|PatchSet
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
name|PatchSetUtil
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
name|change
operator|.
name|AccountPatchReviewStore
operator|.
name|PatchSetWithReviewedFiles
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
name|patch
operator|.
name|PatchList
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
name|patch
operator|.
name|PatchListCache
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
name|patch
operator|.
name|PatchListNotAvailableException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|RevCommit
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
name|treewalk
operator|.
name|TreeWalk
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
name|treewalk
operator|.
name|filter
operator|.
name|PathFilterGroup
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Files
specifier|public
class|class
name|Files
implements|implements
name|ChildCollection
argument_list|<
name|RevisionResource
argument_list|,
name|FileResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListFiles
argument_list|>
name|list
decl_stmt|;
annotation|@
name|Inject
DECL|method|Files (DynamicMap<RestView<FileResource>> views, Provider<ListFiles> list)
name|Files
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListFiles
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|RevisionResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|AuthException
block|{
return|return
name|list
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (RevisionResource rev, IdString id)
specifier|public
name|FileResource
name|parse
parameter_list|(
name|RevisionResource
name|rev
parameter_list|,
name|IdString
name|id
parameter_list|)
block|{
return|return
operator|new
name|FileResource
argument_list|(
name|rev
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|class|ListFiles
specifier|public
specifier|static
specifier|final
class|class
name|ListFiles
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ListFiles
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--base"
argument_list|,
name|metaVar
operator|=
literal|"revision-id"
argument_list|)
DECL|field|base
name|String
name|base
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--parent"
argument_list|,
name|metaVar
operator|=
literal|"parent-number"
argument_list|)
DECL|field|parentNum
name|int
name|parentNum
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--reviewed"
argument_list|)
DECL|field|reviewed
name|boolean
name|reviewed
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-q"
argument_list|)
DECL|field|query
name|String
name|query
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|fileInfoJson
specifier|private
specifier|final
name|FileInfoJson
name|fileInfoJson
decl_stmt|;
DECL|field|revisions
specifier|private
specifier|final
name|Revisions
name|revisions
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|patchListCache
specifier|private
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|accountPatchReviewStore
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListFiles ( Provider<ReviewDb> db, Provider<CurrentUser> self, FileInfoJson fileInfoJson, Revisions revisions, GitRepositoryManager gitManager, PatchListCache patchListCache, PatchSetUtil psUtil, DynamicItem<AccountPatchReviewStore> accountPatchReviewStore)
name|ListFiles
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|FileInfoJson
name|fileInfoJson
parameter_list|,
name|Revisions
name|revisions
parameter_list|,
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|fileInfoJson
operator|=
name|fileInfoJson
expr_stmt|;
name|this
operator|.
name|revisions
operator|=
name|revisions
expr_stmt|;
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|accountPatchReviewStore
operator|=
name|accountPatchReviewStore
expr_stmt|;
block|}
DECL|method|setReviewed (boolean r)
specifier|public
name|ListFiles
name|setReviewed
parameter_list|(
name|boolean
name|r
parameter_list|)
block|{
name|this
operator|.
name|reviewed
operator|=
name|r
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource resource)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|PatchListNotAvailableException
block|{
name|checkOptions
argument_list|()
expr_stmt|;
if|if
condition|(
name|reviewed
condition|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|reviewed
argument_list|(
name|resource
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|query
argument_list|(
name|resource
argument_list|)
argument_list|)
return|;
block|}
name|Response
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
argument_list|>
name|r
decl_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|RevisionResource
name|baseResource
init|=
name|revisions
operator|.
name|parse
argument_list|(
name|resource
operator|.
name|getChangeResource
argument_list|()
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|base
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|(
name|fileInfoJson
operator|.
name|toFileInfoMap
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|,
name|baseResource
operator|.
name|getPatchSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parentNum
operator|>
literal|0
condition|)
block|{
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|(
name|fileInfoJson
operator|.
name|toFileInfoMap
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|,
name|parentNum
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|(
name|fileInfoJson
operator|.
name|toFileInfoMap
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resource
operator|.
name|isCacheable
argument_list|()
condition|)
block|{
name|r
operator|.
name|caching
argument_list|(
name|CacheControl
operator|.
name|PRIVATE
argument_list|(
literal|7
argument_list|,
name|TimeUnit
operator|.
name|DAYS
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|checkOptions ()
specifier|private
name|void
name|checkOptions
parameter_list|()
throws|throws
name|BadRequestException
block|{
name|int
name|supplied
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|supplied
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|parentNum
operator|>
literal|0
condition|)
block|{
name|supplied
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|reviewed
condition|)
block|{
name|supplied
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|supplied
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|supplied
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"cannot combine base, parent, reviewed, query"
argument_list|)
throw|;
block|}
block|}
DECL|method|query (RevisionResource resource)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|query
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|ObjectReader
name|or
operator|=
name|git
operator|.
name|newObjectReader
argument_list|()
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|or
argument_list|)
init|;
name|TreeWalk
name|tw
operator|=
operator|new
name|TreeWalk
argument_list|(
name|or
argument_list|)
init|)
block|{
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|tw
operator|.
name|addTree
argument_list|(
name|c
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|tw
operator|.
name|setRecursive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paths
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|tw
operator|.
name|next
argument_list|()
operator|&&
name|paths
operator|.
name|size
argument_list|()
operator|<
literal|20
condition|)
block|{
name|String
name|s
init|=
name|tw
operator|.
name|getPathString
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|paths
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|paths
return|;
block|}
block|}
DECL|method|reviewed (RevisionResource resource)
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|reviewed
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
throws|throws
name|AuthException
throws|,
name|OrmException
block|{
name|CurrentUser
name|user
init|=
name|self
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
name|Account
operator|.
name|Id
name|userId
init|=
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|PatchSet
name|patchSetId
init|=
name|resource
operator|.
name|getPatchSet
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|PatchSetWithReviewedFiles
argument_list|>
name|o
init|=
name|accountPatchReviewStore
operator|.
name|get
argument_list|()
operator|.
name|findReviewed
argument_list|(
name|patchSetId
operator|.
name|getId
argument_list|()
argument_list|,
name|userId
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|PatchSetWithReviewedFiles
name|res
init|=
name|o
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|res
operator|.
name|patchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|patchSetId
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|res
operator|.
name|files
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|copy
argument_list|(
name|res
operator|.
name|files
argument_list|()
argument_list|,
name|res
operator|.
name|patchSetId
argument_list|()
argument_list|,
name|resource
argument_list|,
name|userId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|PatchListNotAvailableException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot copy patch review flags"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
DECL|method|copy ( Set<String> paths, PatchSet.Id old, RevisionResource resource, Account.Id userId)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|copy
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|paths
parameter_list|,
name|PatchSet
operator|.
name|Id
name|old
parameter_list|,
name|RevisionResource
name|resource
parameter_list|,
name|Account
operator|.
name|Id
name|userId
parameter_list|)
throws|throws
name|IOException
throws|,
name|PatchListNotAvailableException
throws|,
name|OrmException
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|ObjectReader
name|reader
operator|=
name|git
operator|.
name|newObjectReader
argument_list|()
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|;
name|TreeWalk
name|tw
operator|=
operator|new
name|TreeWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
name|PatchList
name|oldList
init|=
name|patchListCache
operator|.
name|get
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|psUtil
operator|.
name|get
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|resource
operator|.
name|getNotes
argument_list|()
argument_list|,
name|old
argument_list|)
argument_list|)
decl_stmt|;
name|PatchList
name|curList
init|=
name|patchListCache
operator|.
name|get
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchSet
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|sz
init|=
name|paths
operator|.
name|size
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pathList
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|sz
argument_list|)
decl_stmt|;
name|tw
operator|.
name|setFilter
argument_list|(
name|PathFilterGroup
operator|.
name|createFromStrings
argument_list|(
name|paths
argument_list|)
argument_list|)
expr_stmt|;
name|tw
operator|.
name|setRecursive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|o
init|=
name|tw
operator|.
name|addTree
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|oldList
operator|.
name|getNewId
argument_list|()
argument_list|)
operator|.
name|getTree
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|c
init|=
name|tw
operator|.
name|addTree
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|curList
operator|.
name|getNewId
argument_list|()
argument_list|)
operator|.
name|getTree
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|op
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|oldList
operator|.
name|getOldId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|op
operator|=
name|tw
operator|.
name|addTree
argument_list|(
name|rw
operator|.
name|parseTree
argument_list|(
name|oldList
operator|.
name|getOldId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|int
name|cp
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|curList
operator|.
name|getOldId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cp
operator|=
name|tw
operator|.
name|addTree
argument_list|(
name|rw
operator|.
name|parseTree
argument_list|(
name|curList
operator|.
name|getOldId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|tw
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|path
init|=
name|tw
operator|.
name|getPathString
argument_list|()
decl_stmt|;
if|if
condition|(
name|tw
operator|.
name|getRawMode
argument_list|(
name|o
argument_list|)
operator|!=
literal|0
operator|&&
name|tw
operator|.
name|getRawMode
argument_list|(
name|c
argument_list|)
operator|!=
literal|0
operator|&&
name|tw
operator|.
name|idEqual
argument_list|(
name|o
argument_list|,
name|c
argument_list|)
operator|&&
name|paths
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// File exists in previously reviewed oldList and in curList.
comment|// File content is identical.
name|pathList
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|op
operator|>=
literal|0
operator|&&
name|cp
operator|>=
literal|0
operator|&&
name|tw
operator|.
name|getRawMode
argument_list|(
name|o
argument_list|)
operator|==
literal|0
operator|&&
name|tw
operator|.
name|getRawMode
argument_list|(
name|c
argument_list|)
operator|==
literal|0
operator|&&
name|tw
operator|.
name|getRawMode
argument_list|(
name|op
argument_list|)
operator|!=
literal|0
operator|&&
name|tw
operator|.
name|getRawMode
argument_list|(
name|cp
argument_list|)
operator|!=
literal|0
operator|&&
name|tw
operator|.
name|idEqual
argument_list|(
name|op
argument_list|,
name|cp
argument_list|)
operator|&&
name|paths
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// File was deleted in previously reviewed oldList and curList.
comment|// File exists in ancestor of oldList and curList.
comment|// File content is identical in ancestors.
name|pathList
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
name|accountPatchReviewStore
operator|.
name|get
argument_list|()
operator|.
name|markReviewed
argument_list|(
name|resource
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|userId
argument_list|,
name|pathList
argument_list|)
expr_stmt|;
return|return
name|pathList
return|;
block|}
block|}
DECL|method|setBase (String base)
specifier|public
name|ListFiles
name|setBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setParent (int parentNum)
specifier|public
name|ListFiles
name|setParent
parameter_list|(
name|int
name|parentNum
parameter_list|)
block|{
name|this
operator|.
name|parentNum
operator|=
name|parentNum
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit


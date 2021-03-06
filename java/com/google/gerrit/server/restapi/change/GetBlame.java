begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|ListMultimap
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
name|MultimapBuilder
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
name|common
operator|.
name|BlameInfo
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
name|RangeInfo
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
name|RestApiException
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
name|server
operator|.
name|change
operator|.
name|FileResource
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
name|config
operator|.
name|GerritServerConfig
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
name|MergeUtil
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
name|AutoMerger
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
name|project
operator|.
name|InvalidChangeOperationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gitiles
operator|.
name|blame
operator|.
name|cache
operator|.
name|BlameCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gitiles
operator|.
name|blame
operator|.
name|cache
operator|.
name|Region
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
name|List
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
name|lib
operator|.
name|Config
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
name|ObjectInserter
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
name|merge
operator|.
name|ThreeWayMergeStrategy
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|GetBlame
specifier|public
class|class
name|GetBlame
implements|implements
name|RestReadView
argument_list|<
name|FileResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|blameCache
specifier|private
specifier|final
name|BlameCache
name|blameCache
decl_stmt|;
DECL|field|mergeStrategy
specifier|private
specifier|final
name|ThreeWayMergeStrategy
name|mergeStrategy
decl_stmt|;
DECL|field|autoMerger
specifier|private
specifier|final
name|AutoMerger
name|autoMerger
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--base"
argument_list|,
name|aliases
operator|=
block|{
literal|"-b"
block|}
argument_list|,
name|usage
operator|=
literal|"whether to load the blame of the base revision (the direct"
operator|+
literal|" parent of the change) instead of the change"
argument_list|)
DECL|field|base
specifier|private
name|boolean
name|base
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetBlame ( GitRepositoryManager repoManager, BlameCache blameCache, @GerritServerConfig Config cfg, AutoMerger autoMerger)
name|GetBlame
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|BlameCache
name|blameCache
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|AutoMerger
name|autoMerger
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
name|blameCache
operator|=
name|blameCache
expr_stmt|;
name|this
operator|.
name|mergeStrategy
operator|=
name|MergeUtil
operator|.
name|getMergeStrategy
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|this
operator|.
name|autoMerger
operator|=
name|autoMerger
expr_stmt|;
block|}
DECL|method|setBase (boolean base)
specifier|public
name|GetBlame
name|setBase
parameter_list|(
name|boolean
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
annotation|@
name|Override
DECL|method|apply (FileResource resource)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|BlameInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|FileResource
name|resource
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|InvalidChangeOperationException
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|resource
operator|.
name|getRevision
argument_list|()
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
name|repository
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|ObjectInserter
name|ins
operator|=
name|repository
operator|.
name|newObjectInserter
argument_list|()
init|;
name|ObjectReader
name|reader
operator|=
name|ins
operator|.
name|newReader
argument_list|()
init|;
name|RevWalk
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
name|String
name|refName
init|=
name|resource
operator|.
name|getRevision
argument_list|()
operator|.
name|getEdit
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|?
name|resource
operator|.
name|getRevision
argument_list|()
operator|.
name|getEdit
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getRefName
argument_list|()
else|:
name|resource
operator|.
name|getRevision
argument_list|()
operator|.
name|getPatchSet
argument_list|()
operator|.
name|refName
argument_list|()
decl_stmt|;
name|Ref
name|ref
init|=
name|repository
operator|.
name|findRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"unknown ref "
operator|+
name|refName
argument_list|)
throw|;
block|}
name|ObjectId
name|objectId
init|=
name|ref
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|RevCommit
name|revCommit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|objectId
argument_list|)
decl_stmt|;
name|RevCommit
index|[]
name|parents
init|=
name|revCommit
operator|.
name|getParents
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|resource
operator|.
name|getPatchKey
argument_list|()
operator|.
name|fileName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|BlameInfo
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
operator|!
name|base
condition|)
block|{
name|result
operator|=
name|blame
argument_list|(
name|revCommit
argument_list|,
name|path
argument_list|,
name|repository
argument_list|,
name|revWalk
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parents
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"Initial commit doesn't have base"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|parents
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|result
operator|=
name|blame
argument_list|(
name|parents
index|[
literal|0
index|]
argument_list|,
name|path
argument_list|,
name|repository
argument_list|,
name|revWalk
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parents
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|ObjectId
name|automerge
init|=
name|autoMerger
operator|.
name|merge
argument_list|(
name|repository
argument_list|,
name|revWalk
argument_list|,
name|ins
argument_list|,
name|revCommit
argument_list|,
name|mergeStrategy
argument_list|)
decl_stmt|;
name|result
operator|=
name|blame
argument_list|(
name|automerge
argument_list|,
name|path
argument_list|,
name|repository
argument_list|,
name|revWalk
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"Cannot generate blame for merge commit with more than 2 parents"
argument_list|)
throw|;
block|}
name|Response
argument_list|<
name|List
argument_list|<
name|BlameInfo
argument_list|>
argument_list|>
name|r
init|=
name|Response
operator|.
name|ok
argument_list|(
name|result
argument_list|)
decl_stmt|;
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
block|}
DECL|method|blame (ObjectId id, String path, Repository repository, RevWalk revWalk)
specifier|private
name|List
argument_list|<
name|BlameInfo
argument_list|>
name|blame
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|Repository
name|repository
parameter_list|,
name|RevWalk
name|revWalk
parameter_list|)
throws|throws
name|IOException
block|{
name|ListMultimap
argument_list|<
name|BlameInfo
argument_list|,
name|RangeInfo
argument_list|>
name|ranges
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|BlameInfo
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|blameCache
operator|.
name|findLastCommit
argument_list|(
name|repository
argument_list|,
name|id
argument_list|,
name|path
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
name|List
argument_list|<
name|Region
argument_list|>
name|blameRegions
init|=
name|blameCache
operator|.
name|get
argument_list|(
name|repository
argument_list|,
name|id
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|int
name|from
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Region
name|region
range|:
name|blameRegions
control|)
block|{
name|RevCommit
name|commit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|region
operator|.
name|getSourceCommit
argument_list|()
argument_list|)
decl_stmt|;
name|BlameInfo
name|blameInfo
init|=
name|toBlameInfo
argument_list|(
name|commit
argument_list|,
name|region
operator|.
name|getSourceAuthor
argument_list|()
argument_list|)
decl_stmt|;
name|ranges
operator|.
name|put
argument_list|(
name|blameInfo
argument_list|,
operator|new
name|RangeInfo
argument_list|(
name|from
argument_list|,
name|from
operator|+
name|region
operator|.
name|getCount
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|from
operator|+=
name|region
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|BlameInfo
name|key
range|:
name|ranges
operator|.
name|keySet
argument_list|()
control|)
block|{
name|key
operator|.
name|ranges
operator|=
name|ranges
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|toBlameInfo (RevCommit commit, PersonIdent sourceAuthor)
specifier|private
specifier|static
name|BlameInfo
name|toBlameInfo
parameter_list|(
name|RevCommit
name|commit
parameter_list|,
name|PersonIdent
name|sourceAuthor
parameter_list|)
block|{
name|BlameInfo
name|blameInfo
init|=
operator|new
name|BlameInfo
argument_list|()
decl_stmt|;
name|blameInfo
operator|.
name|author
operator|=
name|sourceAuthor
operator|.
name|getName
argument_list|()
expr_stmt|;
name|blameInfo
operator|.
name|id
operator|=
name|commit
operator|.
name|getName
argument_list|()
expr_stmt|;
name|blameInfo
operator|.
name|commitMsg
operator|=
name|commit
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
name|blameInfo
operator|.
name|time
operator|=
name|commit
operator|.
name|getCommitTime
argument_list|()
expr_stmt|;
return|return
name|blameInfo
return|;
block|}
block|}
end_class

end_unit


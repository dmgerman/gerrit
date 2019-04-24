begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|ImmutableList
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
name|ImmutableSet
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
name|CommitInfo
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
name|change
operator|.
name|RevisionJson
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
name|RevisionResource
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
name|MergeListBuilder
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|GetMergeList
specifier|public
class|class
name|GetMergeList
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|RevisionJson
operator|.
name|Factory
name|json
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--parent"
argument_list|,
name|usage
operator|=
literal|"Uninteresting parent (1-based, default = 1)"
argument_list|)
DECL|field|uninterestingParent
specifier|private
name|int
name|uninterestingParent
init|=
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--links"
argument_list|,
name|usage
operator|=
literal|"Include weblinks"
argument_list|)
DECL|field|addLinks
specifier|private
name|boolean
name|addLinks
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetMergeList (GitRepositoryManager repoManager, RevisionJson.Factory json)
name|GetMergeList
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|RevisionJson
operator|.
name|Factory
name|json
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
name|json
operator|=
name|json
expr_stmt|;
block|}
DECL|method|setUninterestingParent (int uninterestingParent)
specifier|public
name|void
name|setUninterestingParent
parameter_list|(
name|int
name|uninterestingParent
parameter_list|)
block|{
name|this
operator|.
name|uninterestingParent
operator|=
name|uninterestingParent
expr_stmt|;
block|}
DECL|method|setAddLinks (boolean addLinks)
specifier|public
name|void
name|setAddLinks
parameter_list|(
name|boolean
name|addLinks
parameter_list|)
block|{
name|this
operator|.
name|addLinks
operator|=
name|addLinks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|CommitInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|p
init|=
name|rsrc
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
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|p
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
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getCommitId
argument_list|()
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|commit
argument_list|)
expr_stmt|;
if|if
condition|(
name|uninterestingParent
argument_list|<
literal|1
operator|||
name|uninterestingParent
argument_list|>
name|commit
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"No such parent: "
operator|+
name|uninterestingParent
argument_list|)
throw|;
block|}
if|if
condition|(
name|commit
operator|.
name|getParentCount
argument_list|()
operator|<
literal|2
condition|)
block|{
return|return
name|createResponse
argument_list|(
name|rsrc
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|RevCommit
argument_list|>
name|commits
init|=
name|MergeListBuilder
operator|.
name|build
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|,
name|uninterestingParent
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CommitInfo
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|commits
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|RevisionJson
name|changeJson
init|=
name|json
operator|.
name|create
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|commits
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|changeJson
operator|.
name|getCommitInfo
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|rw
argument_list|,
name|c
argument_list|,
name|addLinks
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|createResponse
argument_list|(
name|rsrc
argument_list|,
name|result
argument_list|)
return|;
block|}
block|}
DECL|method|createResponse ( RevisionResource rsrc, List<CommitInfo> result)
specifier|private
specifier|static
name|Response
argument_list|<
name|List
argument_list|<
name|CommitInfo
argument_list|>
argument_list|>
name|createResponse
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|List
argument_list|<
name|CommitInfo
argument_list|>
name|result
parameter_list|)
block|{
name|Response
argument_list|<
name|List
argument_list|<
name|CommitInfo
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
name|rsrc
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
end_class

end_unit


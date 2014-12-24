begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefDatabase
operator|.
name|ALL
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
name|Maps
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
name|Sets
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
name|client
operator|.
name|RefNames
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
name|query
operator|.
name|Predicate
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
name|query
operator|.
name|QueryParseException
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|query
operator|.
name|change
operator|.
name|QueryProcessor
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
name|util
operator|.
name|MagicBranch
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
name|Provider
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
name|transport
operator|.
name|AdvertiseRefsHook
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
name|transport
operator|.
name|BaseReceivePack
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
name|transport
operator|.
name|ServiceMayNotContinueException
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
name|transport
operator|.
name|UploadPack
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
name|Set
import|;
end_import

begin_comment
comment|/** Exposes only the non refs/changes/ reference names. */
end_comment

begin_class
DECL|class|ReceiveCommitsAdvertiseRefsHook
specifier|public
class|class
name|ReceiveCommitsAdvertiseRefsHook
implements|implements
name|AdvertiseRefsHook
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
name|ReceiveCommitsAdvertiseRefsHook
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|queryProcessor
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryProcessor
argument_list|>
name|queryProcessor
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|method|ReceiveCommitsAdvertiseRefsHook (ReviewDb db, Provider<QueryProcessor> queryProcessor, Project.NameKey projectName)
specifier|public
name|ReceiveCommitsAdvertiseRefsHook
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Provider
argument_list|<
name|QueryProcessor
argument_list|>
name|queryProcessor
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
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
name|queryProcessor
operator|=
name|queryProcessor
expr_stmt|;
name|this
operator|.
name|projectName
operator|=
name|projectName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (UploadPack us)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|UploadPack
name|us
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ReceiveCommitsAdvertiseRefsHook cannot be used for UploadPack"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (BaseReceivePack rp)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|BaseReceivePack
name|rp
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|oldRefs
init|=
name|rp
operator|.
name|getAdvertisedRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldRefs
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|oldRefs
operator|=
name|rp
operator|.
name|getRepository
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|ALL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServiceMayNotContinueException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ServiceMayNotContinueException
name|ex
init|=
operator|new
name|ServiceMayNotContinueException
argument_list|()
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|r
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|oldRefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|e
range|:
name|oldRefs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|skip
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|r
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|rp
operator|.
name|setAdvertisedRefs
argument_list|(
name|r
argument_list|,
name|advertiseHistory
argument_list|(
name|r
operator|.
name|values
argument_list|()
argument_list|,
name|rp
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|advertiseHistory ( Iterable<Ref> sending, BaseReceivePack rp)
specifier|private
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|advertiseHistory
parameter_list|(
name|Iterable
argument_list|<
name|Ref
argument_list|>
name|sending
parameter_list|,
name|BaseReceivePack
name|rp
parameter_list|)
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|toInclude
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
comment|// Advertise some recent open changes, in case a commit is based one.
specifier|final
name|int
name|limit
init|=
literal|32
decl_stmt|;
try|try
block|{
name|Set
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|toGet
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|limit
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|queryRecentChanges
argument_list|(
name|limit
argument_list|)
control|)
block|{
name|PatchSet
operator|.
name|Id
name|id
init|=
name|cd
operator|.
name|change
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|toGet
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|PatchSet
name|ps
range|:
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|toGet
argument_list|)
control|)
block|{
if|if
condition|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|!=
literal|null
operator|&&
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|toInclude
operator|.
name|add
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot list open changes of "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
comment|// Size of an additional ".have" line.
specifier|final
name|int
name|haveLineLen
init|=
literal|4
operator|+
name|Constants
operator|.
name|OBJECT_ID_STRING_LENGTH
operator|+
literal|1
operator|+
literal|5
operator|+
literal|1
decl_stmt|;
comment|// Maximum number of bytes to "waste" in the advertisement with
comment|// a peek at this repository's current reachable history.
specifier|final
name|int
name|maxExtraSize
init|=
literal|8192
decl_stmt|;
comment|// Number of recent commits to advertise immediately, hoping to
comment|// show a client a nearby merge base.
specifier|final
name|int
name|base
init|=
literal|64
decl_stmt|;
comment|// Number of commits to skip once base has already been shown.
specifier|final
name|int
name|step
init|=
literal|16
decl_stmt|;
comment|// Total number of commits to extract from the history.
specifier|final
name|int
name|max
init|=
name|maxExtraSize
operator|/
name|haveLineLen
decl_stmt|;
comment|// Scan history until the advertisement is full.
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|alreadySending
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|RevWalk
name|rw
init|=
name|rp
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|sending
control|)
block|{
try|try
block|{
if|if
condition|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|alreadySending
operator|.
name|add
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|badCommit
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|int
name|stepCnt
init|=
literal|0
decl_stmt|;
name|RevCommit
name|c
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
operator|&&
name|toInclude
operator|.
name|size
argument_list|()
operator|<
name|max
condition|)
block|{
if|if
condition|(
name|alreadySending
operator|.
name|contains
argument_list|(
name|c
argument_list|)
condition|)
block|{         }
elseif|else
if|if
condition|(
name|toInclude
operator|.
name|contains
argument_list|(
name|c
argument_list|)
condition|)
block|{         }
elseif|else
if|if
condition|(
name|c
operator|.
name|getParentCount
argument_list|()
operator|>
literal|1
condition|)
block|{         }
elseif|else
if|if
condition|(
name|toInclude
operator|.
name|size
argument_list|()
operator|<
name|base
condition|)
block|{
name|toInclude
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stepCnt
operator|=
operator|++
name|stepCnt
operator|%
name|step
expr_stmt|;
if|if
condition|(
name|stepCnt
operator|==
literal|0
condition|)
block|{
name|toInclude
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error trying to advertise history on "
operator|+
name|projectName
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|toInclude
return|;
block|}
DECL|method|queryRecentChanges (int limit)
specifier|private
name|List
argument_list|<
name|ChangeData
argument_list|>
name|queryRecentChanges
parameter_list|(
name|int
name|limit
parameter_list|)
throws|throws
name|OrmException
block|{
name|QueryProcessor
name|qp
init|=
name|queryProcessor
operator|.
name|get
argument_list|()
decl_stmt|;
name|qp
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|ChangeQueryBuilder
name|qb
init|=
name|qp
operator|.
name|getQueryBuilder
argument_list|()
decl_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
init|=
name|Predicate
operator|.
name|and
argument_list|(
name|qb
operator|.
name|project
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|qb
operator|.
name|status_open
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|qp
operator|.
name|queryChanges
argument_list|(
name|p
argument_list|)
operator|.
name|changes
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|skip (String name)
specifier|private
specifier|static
name|boolean
name|skip
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_CACHE_AUTOMERGE
argument_list|)
operator|||
name|MagicBranch
operator|.
name|isMagicBranch
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit


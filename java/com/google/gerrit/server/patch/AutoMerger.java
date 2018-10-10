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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|flogger
operator|.
name|FluentLogger
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
name|server
operator|.
name|GerritPersonIdent
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
name|UsedAt
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
name|InMemoryInserter
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|dircache
operator|.
name|DirCache
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
name|CommitBuilder
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
name|RefUpdate
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
name|ResolveMerger
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
name|RevWalk
import|;
end_import

begin_class
DECL|class|AutoMerger
specifier|public
class|class
name|AutoMerger
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|cacheAutomerge (Config cfg)
specifier|public
specifier|static
name|boolean
name|cacheAutomerge
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"cacheAutomerge"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|field|gerritIdent
specifier|private
specifier|final
name|PersonIdent
name|gerritIdent
decl_stmt|;
DECL|field|save
specifier|private
specifier|final
name|boolean
name|save
decl_stmt|;
annotation|@
name|Inject
DECL|method|AutoMerger (@erritServerConfig Config cfg, @GerritPersonIdent PersonIdent gerritIdent)
name|AutoMerger
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|gerritIdent
parameter_list|)
block|{
name|save
operator|=
name|cacheAutomerge
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|this
operator|.
name|gerritIdent
operator|=
name|gerritIdent
expr_stmt|;
block|}
comment|/**    * Perform an auto-merge of the parents of the given merge commit.    *    * @return auto-merge commit or {@code null} if an auto-merge commit couldn't be created. Headers    *     of the returned RevCommit are parsed.    */
DECL|method|merge ( Repository repo, RevWalk rw, ObjectInserter ins, RevCommit merge, ThreeWayMergeStrategy mergeStrategy)
specifier|public
name|RevCommit
name|merge
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|RevCommit
name|merge
parameter_list|,
name|ThreeWayMergeStrategy
name|mergeStrategy
parameter_list|)
throws|throws
name|IOException
block|{
name|checkArgument
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|getCreatedFromInserter
argument_list|()
operator|==
name|ins
argument_list|)
expr_stmt|;
name|InMemoryInserter
name|tmpIns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ins
operator|instanceof
name|InMemoryInserter
condition|)
block|{
comment|// Caller gave us an in-memory inserter, so ensure anything we write from
comment|// this method is visible to them.
name|tmpIns
operator|=
operator|(
name|InMemoryInserter
operator|)
name|ins
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|save
condition|)
block|{
comment|// If we don't plan on saving results, use a fully in-memory inserter.
comment|// Using just a non-flushing wrapper is not sufficient, since in
comment|// particular DfsInserter might try to write to storage after exceeding an
comment|// internal buffer size.
name|tmpIns
operator|=
operator|new
name|InMemoryInserter
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|rw
operator|.
name|parseHeaders
argument_list|(
name|merge
argument_list|)
expr_stmt|;
name|String
name|refName
init|=
name|RefNames
operator|.
name|refsCacheAutomerge
argument_list|(
name|merge
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
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
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|RevObject
name|obj
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
name|obj
operator|instanceof
name|RevCommit
condition|)
block|{
return|return
operator|(
name|RevCommit
operator|)
name|obj
return|;
block|}
return|return
name|commit
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|tmpIns
argument_list|,
name|ins
argument_list|,
name|refName
argument_list|,
name|obj
argument_list|,
name|merge
argument_list|)
return|;
block|}
name|ResolveMerger
name|m
init|=
operator|(
name|ResolveMerger
operator|)
name|mergeStrategy
operator|.
name|newMerger
argument_list|(
name|repo
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DirCache
name|dc
init|=
name|DirCache
operator|.
name|newInCore
argument_list|()
decl_stmt|;
name|m
operator|.
name|setDirCache
argument_list|(
name|dc
argument_list|)
expr_stmt|;
name|m
operator|.
name|setObjectInserter
argument_list|(
name|tmpIns
operator|==
literal|null
condition|?
operator|new
name|NonFlushingWrapper
argument_list|(
name|ins
argument_list|)
else|:
name|tmpIns
argument_list|)
expr_stmt|;
name|boolean
name|couldMerge
decl_stmt|;
try|try
block|{
name|couldMerge
operator|=
name|m
operator|.
name|merge
argument_list|(
name|merge
operator|.
name|getParents
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// It is not safe to continue further down in this method as throwing
comment|// an exception most likely means that the merge tree was not created
comment|// and m.getMergeResults() is empty. This would mean that all paths are
comment|// unmerged and Gerrit UI would show all paths in the patch list.
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Error attempting automerge %s"
argument_list|,
name|refName
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|ObjectId
name|treeId
decl_stmt|;
if|if
condition|(
name|couldMerge
condition|)
block|{
name|treeId
operator|=
name|m
operator|.
name|getResultTreeId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|treeId
operator|=
name|MergeUtil
operator|.
name|mergeWithConflicts
argument_list|(
name|rw
argument_list|,
name|ins
argument_list|,
name|dc
argument_list|,
literal|"HEAD"
argument_list|,
name|merge
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"BRANCH"
argument_list|,
name|merge
operator|.
name|getParent
argument_list|(
literal|1
argument_list|)
argument_list|,
name|m
operator|.
name|getMergeResults
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|commit
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|tmpIns
argument_list|,
name|ins
argument_list|,
name|refName
argument_list|,
name|treeId
argument_list|,
name|merge
argument_list|)
return|;
block|}
DECL|method|commit ( Repository repo, RevWalk rw, @Nullable InMemoryInserter tmpIns, ObjectInserter ins, String refName, ObjectId tree, RevCommit merge)
specifier|private
name|RevCommit
name|commit
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|InMemoryInserter
name|tmpIns
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|String
name|refName
parameter_list|,
name|ObjectId
name|tree
parameter_list|,
name|RevCommit
name|merge
parameter_list|)
throws|throws
name|IOException
block|{
name|rw
operator|.
name|parseHeaders
argument_list|(
name|merge
argument_list|)
expr_stmt|;
comment|// For maximum stability, choose a single ident using the committer time of
comment|// the input commit, using the server name and timezone.
name|PersonIdent
name|ident
init|=
operator|new
name|PersonIdent
argument_list|(
name|gerritIdent
argument_list|,
name|merge
operator|.
name|getCommitterIdent
argument_list|()
operator|.
name|getWhen
argument_list|()
argument_list|,
name|gerritIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
decl_stmt|;
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
literal|"Auto-merge of "
operator|+
name|merge
operator|.
name|name
argument_list|()
operator|+
literal|'\n'
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|p
range|:
name|merge
operator|.
name|getParents
argument_list|()
control|)
block|{
name|cb
operator|.
name|addParentId
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|save
condition|)
block|{
name|checkArgument
argument_list|(
name|tmpIns
operator|!=
literal|null
argument_list|)
expr_stmt|;
try|try
init|(
name|ObjectReader
name|tmpReader
init|=
name|tmpIns
operator|.
name|newReader
argument_list|()
init|;           RevWalk tmpRw = new RevWalk(tmpReader)
block|)
block|{
return|return
name|tmpRw
operator|.
name|parseCommit
argument_list|(
name|tmpIns
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
argument_list|)
return|;
block|}
block|}
name|checkArgument
argument_list|(
name|tmpIns
operator|==
literal|null
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
operator|(
name|ins
operator|instanceof
name|InMemoryInserter
operator|)
argument_list|)
expr_stmt|;
name|ObjectId
name|commitId
init|=
name|ins
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|ins
operator|.
name|flush
parameter_list|()
constructor_decl|;
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setNewObjectId
parameter_list|(
name|commitId
parameter_list|)
constructor_decl|;
name|ru
operator|.
name|disableRefLog
parameter_list|()
constructor_decl|;
name|ru
operator|.
name|forceUpdate
parameter_list|()
constructor_decl|;
return|return
name|rw
operator|.
name|parseCommit
argument_list|(
name|commitId
argument_list|)
return|;
block|}
end_class

begin_class
DECL|class|NonFlushingWrapper
specifier|private
specifier|static
class|class
name|NonFlushingWrapper
extends|extends
name|ObjectInserter
operator|.
name|Filter
block|{
DECL|field|ins
specifier|private
specifier|final
name|ObjectInserter
name|ins
decl_stmt|;
DECL|method|NonFlushingWrapper (ObjectInserter ins)
specifier|private
name|NonFlushingWrapper
parameter_list|(
name|ObjectInserter
name|ins
parameter_list|)
block|{
name|this
operator|.
name|ins
operator|=
name|ins
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|ObjectInserter
name|delegate
parameter_list|()
block|{
return|return
name|ins
return|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{}
block|}
end_class

unit|}
end_unit


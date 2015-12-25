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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|Multimap
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
name|data
operator|.
name|SubmitTypeRecord
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
name|client
operator|.
name|SubmitType
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
name|Branch
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
name|Change
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
name|change
operator|.
name|Submit
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
name|InternalChangeQuery
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|RevSort
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Calculates the minimal superset of changes required to be merged.  *<p>  * This includes all parents between a change and the tip of its target  * branch for the merging/rebasing submit strategies. For the cherry-pick  * strategy no additional changes are included.  *<p>  * If change.submitWholeTopic is enabled, also all changes of the topic  * and their parents are included.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|MergeSuperSet
specifier|public
class|class
name|MergeSuperSet
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
name|MergeOp
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|MergeSuperSet (@erritServerConfig Config cfg, ChangeData.Factory changeDataFactory, Provider<InternalChangeQuery> queryProvider, GitRepositoryManager repoManager)
name|MergeSuperSet
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
DECL|method|completeChangeSet (ReviewDb db, Change change)
specifier|public
name|ChangeSet
name|completeChangeSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
name|change
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Submit
operator|.
name|wholeTopicEnabled
argument_list|(
name|cfg
argument_list|)
condition|)
block|{
return|return
name|completeChangeSetIncludingTopics
argument_list|(
name|db
argument_list|,
operator|new
name|ChangeSet
argument_list|(
name|cd
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|completeChangeSetWithoutTopic
argument_list|(
name|db
argument_list|,
operator|new
name|ChangeSet
argument_list|(
name|cd
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|completeChangeSetWithoutTopic (ReviewDb db, ChangeSet changes)
specifier|private
name|ChangeSet
name|completeChangeSetWithoutTopic
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeSet
name|changes
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Multimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|pc
init|=
name|changes
operator|.
name|changesByProject
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|project
range|:
name|pc
operator|.
name|keySet
argument_list|()
control|)
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
name|CodeReviewCommit
operator|.
name|newRevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
for|for
control|(
name|Change
operator|.
name|Id
name|cId
range|:
name|pc
operator|.
name|get
argument_list|(
name|project
argument_list|)
control|)
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|cId
argument_list|)
decl_stmt|;
name|SubmitTypeRecord
name|str
init|=
name|cd
operator|.
name|submitTypeRecord
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|str
operator|.
name|isOk
argument_list|()
condition|)
block|{
name|logErrorAndThrow
argument_list|(
literal|"Failed to get submit type for "
operator|+
name|cd
operator|.
name|getId
argument_list|()
operator|+
literal|": "
operator|+
name|str
operator|.
name|errorMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|str
operator|.
name|type
operator|==
name|SubmitType
operator|.
name|CHERRY_PICK
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|cd
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// Get the underlying git commit object
name|PatchSet
name|ps
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
name|String
name|objIdStr
init|=
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|objIdStr
argument_list|)
argument_list|)
decl_stmt|;
comment|// Collect unmerged ancestors
name|Branch
operator|.
name|NameKey
name|destBranch
init|=
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
decl_stmt|;
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRef
argument_list|(
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|TOPO
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|commit
argument_list|)
expr_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|RevCommit
name|head
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|rw
operator|.
name|markUninteresting
argument_list|(
name|head
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|hashes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|rw
control|)
block|{
name|hashes
operator|.
name|add
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hashes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Merged changes are ok to exclude
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|destChanges
init|=
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byCommitsOnBranchNotMerged
argument_list|(
name|repo
argument_list|,
name|db
argument_list|,
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|,
name|hashes
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|chd
range|:
name|destChanges
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|chd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
operator|new
name|ChangeSet
argument_list|(
name|ret
argument_list|)
return|;
block|}
DECL|method|completeChangeSetIncludingTopics ( ReviewDb db, ChangeSet changes)
specifier|private
name|ChangeSet
name|completeChangeSetIncludingTopics
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeSet
name|changes
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|topicsTraversed
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|ChangeSet
name|newCs
init|=
name|completeChangeSetWithoutTopic
argument_list|(
name|db
argument_list|,
name|changes
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|chgs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|newCs
operator|.
name|changes
argument_list|()
control|)
block|{
name|chgs
operator|.
name|add
argument_list|(
name|cd
argument_list|)
expr_stmt|;
name|String
name|topic
init|=
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|topic
argument_list|)
operator|&&
operator|!
name|topicsTraversed
operator|.
name|contains
argument_list|(
name|topic
argument_list|)
condition|)
block|{
name|chgs
operator|.
name|addAll
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byTopicOpen
argument_list|(
name|topic
argument_list|)
argument_list|)
expr_stmt|;
name|done
operator|=
literal|false
expr_stmt|;
name|topicsTraversed
operator|.
name|add
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
block|}
name|changes
operator|=
operator|new
name|ChangeSet
argument_list|(
name|chgs
argument_list|)
expr_stmt|;
name|newCs
operator|=
name|completeChangeSetWithoutTopic
argument_list|(
name|db
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
return|return
name|newCs
return|;
block|}
DECL|method|logError (String msg)
specifier|private
name|void
name|logError
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isErrorEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logErrorAndThrow (String msg)
specifier|private
name|void
name|logErrorAndThrow
parameter_list|(
name|String
name|msg
parameter_list|)
throws|throws
name|OrmException
block|{
name|logError
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OrmException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
end_class

end_unit


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
name|checkNotNull
import|;
end_import

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
name|ObjectIdSerialization
operator|.
name|readNotNull
import|;
end_import

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
name|ObjectIdSerialization
operator|.
name|writeNotNull
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
name|base
operator|.
name|Objects
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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|cache
operator|.
name|Weigher
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
name|cache
operator|.
name|CacheModule
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
name|project
operator|.
name|ProjectState
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
name|Module
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
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
name|ThreeWayMerger
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
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ExecutionException
import|;
end_import

begin_comment
comment|/**  * Cache of {@link ChangeKind} per commit.  *<p>  * This is immutable conditioned on the merge strategy (unless the JGit strategy  * implementation changes, which might invalidate old entries).  */
end_comment

begin_class
DECL|class|ChangeKindCache
specifier|public
class|class
name|ChangeKindCache
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
name|ChangeKindCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ID_CACHE
specifier|private
specifier|static
specifier|final
name|String
name|ID_CACHE
init|=
literal|"change_kind"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|persist
argument_list|(
name|ID_CACHE
argument_list|,
name|Key
operator|.
name|class
argument_list|,
name|ChangeKind
operator|.
name|class
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|2
operator|<<
literal|20
argument_list|)
operator|.
name|weigher
argument_list|(
name|ChangeKindWeigher
operator|.
name|class
argument_list|)
operator|.
name|loader
argument_list|(
name|Loader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|prior
specifier|private
specifier|transient
name|ObjectId
name|prior
decl_stmt|;
DECL|field|next
specifier|private
specifier|transient
name|ObjectId
name|next
decl_stmt|;
DECL|field|strategyName
specifier|private
specifier|transient
name|String
name|strategyName
decl_stmt|;
DECL|field|repo
specifier|private
specifier|transient
name|Repository
name|repo
decl_stmt|;
comment|// Passed through to loader on miss.
DECL|method|Key (ObjectId prior, ObjectId next, String strategyName, Repository repo)
specifier|private
name|Key
parameter_list|(
name|ObjectId
name|prior
parameter_list|,
name|ObjectId
name|next
parameter_list|,
name|String
name|strategyName
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|prior
operator|=
name|prior
operator|.
name|copy
argument_list|()
expr_stmt|;
name|this
operator|.
name|next
operator|=
name|next
operator|.
name|copy
argument_list|()
expr_stmt|;
name|this
operator|.
name|strategyName
operator|=
name|strategyName
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
block|}
DECL|method|getPrior ()
specifier|public
name|ObjectId
name|getPrior
parameter_list|()
block|{
return|return
name|prior
return|;
block|}
DECL|method|getNext ()
specifier|public
name|ObjectId
name|getNext
parameter_list|()
block|{
return|return
name|next
return|;
block|}
DECL|method|getStrategyName ()
specifier|public
name|String
name|getStrategyName
parameter_list|()
block|{
return|return
name|strategyName
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Key
condition|)
block|{
name|Key
name|k
init|=
operator|(
name|Key
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|prior
argument_list|,
name|k
operator|.
name|prior
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|next
argument_list|,
name|k
operator|.
name|next
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|strategyName
argument_list|,
name|k
operator|.
name|strategyName
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|prior
argument_list|,
name|next
argument_list|,
name|strategyName
argument_list|)
return|;
block|}
DECL|method|writeObject (ObjectOutputStream out)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|prior
argument_list|)
expr_stmt|;
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|next
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeUTF
argument_list|(
name|strategyName
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (ObjectInputStream in)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|prior
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|next
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|strategyName
operator|=
name|in
operator|.
name|readUTF
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Loader
specifier|private
specifier|static
class|class
name|Loader
extends|extends
name|CacheLoader
argument_list|<
name|Key
argument_list|,
name|ChangeKind
argument_list|>
block|{
annotation|@
name|Override
DECL|method|load (Key key)
specifier|public
name|ChangeKind
name|load
parameter_list|(
name|Key
name|key
parameter_list|)
throws|throws
name|IOException
block|{
name|RevWalk
name|walk
init|=
operator|new
name|RevWalk
argument_list|(
name|key
operator|.
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
name|RevCommit
name|prior
init|=
name|walk
operator|.
name|parseCommit
argument_list|(
name|key
operator|.
name|prior
argument_list|)
decl_stmt|;
name|walk
operator|.
name|parseBody
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|RevCommit
name|next
init|=
name|walk
operator|.
name|parseCommit
argument_list|(
name|key
operator|.
name|next
argument_list|)
decl_stmt|;
name|walk
operator|.
name|parseBody
argument_list|(
name|next
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|next
operator|.
name|getFullMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|prior
operator|.
name|getFullMessage
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|next
operator|.
name|getTree
argument_list|()
operator|==
name|prior
operator|.
name|getTree
argument_list|()
operator|&&
name|isSameParents
argument_list|(
name|prior
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|ChangeKind
operator|.
name|NO_CODE_CHANGE
return|;
block|}
else|else
block|{
return|return
name|ChangeKind
operator|.
name|REWORK
return|;
block|}
block|}
if|if
condition|(
name|prior
operator|.
name|getParentCount
argument_list|()
operator|!=
literal|1
operator|||
name|next
operator|.
name|getParentCount
argument_list|()
operator|!=
literal|1
condition|)
block|{
comment|// Trivial rebases done by machine only work well on 1 parent.
return|return
name|ChangeKind
operator|.
name|REWORK
return|;
block|}
if|if
condition|(
name|next
operator|.
name|getTree
argument_list|()
operator|==
name|prior
operator|.
name|getTree
argument_list|()
operator|&&
name|isSameParents
argument_list|(
name|prior
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|ChangeKind
operator|.
name|TRIVIAL_REBASE
return|;
block|}
comment|// A trivial rebase can be detected by looking for the next commit
comment|// having the same tree as would exist when the prior commit is
comment|// cherry-picked onto the next commit's new first parent.
name|ThreeWayMerger
name|merger
init|=
name|MergeUtil
operator|.
name|newThreeWayMerger
argument_list|(
name|key
operator|.
name|repo
argument_list|,
name|MergeUtil
operator|.
name|createDryRunInserter
argument_list|()
argument_list|,
name|key
operator|.
name|strategyName
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setBase
argument_list|(
name|prior
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|merger
operator|.
name|merge
argument_list|(
name|next
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|,
name|prior
argument_list|)
operator|&&
name|merger
operator|.
name|getResultTreeId
argument_list|()
operator|.
name|equals
argument_list|(
name|next
operator|.
name|getTree
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ChangeKind
operator|.
name|TRIVIAL_REBASE
return|;
block|}
else|else
block|{
return|return
name|ChangeKind
operator|.
name|REWORK
return|;
block|}
block|}
finally|finally
block|{
name|key
operator|.
name|repo
operator|=
literal|null
expr_stmt|;
name|walk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|isSameParents (RevCommit prior, RevCommit next)
specifier|private
specifier|static
name|boolean
name|isSameParents
parameter_list|(
name|RevCommit
name|prior
parameter_list|,
name|RevCommit
name|next
parameter_list|)
block|{
if|if
condition|(
name|prior
operator|.
name|getParentCount
argument_list|()
operator|!=
name|next
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|prior
operator|.
name|getParentCount
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|prior
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|next
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|ChangeKindWeigher
specifier|public
specifier|static
class|class
name|ChangeKindWeigher
implements|implements
name|Weigher
argument_list|<
name|Key
argument_list|,
name|ChangeKind
argument_list|>
block|{
annotation|@
name|Override
DECL|method|weigh (Key key, ChangeKind changeKind)
specifier|public
name|int
name|weigh
parameter_list|(
name|Key
name|key
parameter_list|,
name|ChangeKind
name|changeKind
parameter_list|)
block|{
return|return
literal|16
operator|+
literal|2
operator|*
literal|36
operator|+
literal|2
operator|*
name|key
operator|.
name|strategyName
operator|.
name|length
argument_list|()
comment|// Size of Key, 64 bit JVM
operator|+
literal|2
operator|*
name|changeKind
operator|.
name|name
argument_list|()
operator|.
name|length
argument_list|()
return|;
comment|// Size of ChangeKind, 64 bit JVM
block|}
block|}
DECL|field|cache
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|Key
argument_list|,
name|ChangeKind
argument_list|>
name|cache
decl_stmt|;
DECL|field|useRecursiveMerge
specifier|private
specifier|final
name|boolean
name|useRecursiveMerge
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeKindCache ( @erritServerConfig Config serverConfig, @Named(ID_CACHE) LoadingCache<Key, ChangeKind> cache)
name|ChangeKindCache
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|serverConfig
parameter_list|,
annotation|@
name|Named
argument_list|(
name|ID_CACHE
argument_list|)
name|LoadingCache
argument_list|<
name|Key
argument_list|,
name|ChangeKind
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|useRecursiveMerge
operator|=
name|MergeUtil
operator|.
name|useRecursiveMerge
argument_list|(
name|serverConfig
argument_list|)
expr_stmt|;
block|}
DECL|method|getChangeKind (ProjectState project, Repository repo, ObjectId prior, ObjectId next)
specifier|public
name|ChangeKind
name|getChangeKind
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|ObjectId
name|prior
parameter_list|,
name|ObjectId
name|next
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|next
argument_list|,
literal|"next"
argument_list|)
expr_stmt|;
name|String
name|strategyName
init|=
name|MergeUtil
operator|.
name|mergeStrategyName
argument_list|(
name|project
operator|.
name|isUseContentMerge
argument_list|()
argument_list|,
name|useRecursiveMerge
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|cache
operator|.
name|get
argument_list|(
operator|new
name|Key
argument_list|(
name|prior
argument_list|,
name|next
argument_list|,
name|strategyName
argument_list|,
name|repo
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot check trivial rebase of new patch set "
operator|+
name|next
operator|.
name|name
argument_list|()
operator|+
literal|" in "
operator|+
name|project
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|ChangeKind
operator|.
name|REWORK
return|;
block|}
block|}
block|}
end_class

end_unit


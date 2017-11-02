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
DECL|package|com.google.gerrit.server.index.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|checkArgument
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Splitter
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
name|common
operator|.
name|collect
operator|.
name|SetMultimap
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
name|common
operator|.
name|collect
operator|.
name|Streams
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
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|RefState
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|NoteDbChangeState
operator|.
name|PrimaryStorage
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
name|List
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
name|regex
operator|.
name|Pattern
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
DECL|class|StalenessChecker
specifier|public
class|class
name|StalenessChecker
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
name|StalenessChecker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|FIELDS
specifier|public
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|FIELDS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|ChangeField
operator|.
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|,
name|ChangeField
operator|.
name|REF_STATE
operator|.
name|getName
argument_list|()
argument_list|,
name|ChangeField
operator|.
name|REF_STATE_PATTERN
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|ChangeIndexCollection
name|indexes
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|indexConfig
specifier|private
specifier|final
name|IndexConfig
name|indexConfig
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
annotation|@
name|Inject
DECL|method|StalenessChecker ( ChangeIndexCollection indexes, GitRepositoryManager repoManager, IndexConfig indexConfig, Provider<ReviewDb> db)
name|StalenessChecker
parameter_list|(
name|ChangeIndexCollection
name|indexes
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|indexConfig
operator|=
name|indexConfig
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
block|}
DECL|method|isStale (Change.Id id)
specifier|public
name|boolean
name|isStale
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|ChangeIndex
name|i
init|=
name|indexes
operator|.
name|getSearchIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
comment|// No index; caller couldn't do anything if it is stale.
block|}
if|if
condition|(
operator|!
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|ChangeField
operator|.
name|REF_STATE
argument_list|)
operator|||
operator|!
name|i
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|ChangeField
operator|.
name|REF_STATE_PATTERN
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
comment|// Index version not new enough for this check.
block|}
name|Optional
argument_list|<
name|ChangeData
argument_list|>
name|result
init|=
name|i
operator|.
name|get
argument_list|(
name|id
argument_list|,
name|IndexedChangeQuery
operator|.
name|createOptions
argument_list|(
name|indexConfig
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
name|FIELDS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
comment|// Not in index, but caller wants it to be.
block|}
name|ChangeData
name|cd
init|=
name|result
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|isStale
argument_list|(
name|repoManager
argument_list|,
name|id
argument_list|,
name|cd
operator|.
name|change
argument_list|()
argument_list|,
name|ChangeNotes
operator|.
name|readOneReviewDbChange
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|id
argument_list|)
argument_list|,
name|parseStates
argument_list|(
name|cd
argument_list|)
argument_list|,
name|parsePatterns
argument_list|(
name|cd
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isStale ( GitRepositoryManager repoManager, Change.Id id, Change indexChange, @Nullable Change reviewDbChange, SetMultimap<Project.NameKey, RefState> states, ListMultimap<Project.NameKey, RefStatePattern> patterns)
specifier|public
specifier|static
name|boolean
name|isStale
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|Change
name|indexChange
parameter_list|,
annotation|@
name|Nullable
name|Change
name|reviewDbChange
parameter_list|,
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|states
parameter_list|,
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|patterns
parameter_list|)
block|{
return|return
name|reviewDbChangeIsStale
argument_list|(
name|indexChange
argument_list|,
name|reviewDbChange
argument_list|)
operator|||
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|id
argument_list|,
name|states
argument_list|,
name|patterns
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|refsAreStale ( GitRepositoryManager repoManager, Change.Id id, SetMultimap<Project.NameKey, RefState> states, ListMultimap<Project.NameKey, RefStatePattern> patterns)
specifier|static
name|boolean
name|refsAreStale
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|states
parameter_list|,
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|patterns
parameter_list|)
block|{
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
init|=
name|Sets
operator|.
name|union
argument_list|(
name|states
operator|.
name|keySet
argument_list|()
argument_list|,
name|patterns
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
if|if
condition|(
name|refsAreStale
argument_list|(
name|repoManager
argument_list|,
name|id
argument_list|,
name|p
argument_list|,
name|states
argument_list|,
name|patterns
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|reviewDbChangeIsStale (Change indexChange, @Nullable Change reviewDbChange)
specifier|static
name|boolean
name|reviewDbChangeIsStale
parameter_list|(
name|Change
name|indexChange
parameter_list|,
annotation|@
name|Nullable
name|Change
name|reviewDbChange
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|indexChange
argument_list|)
expr_stmt|;
name|PrimaryStorage
name|storageFromIndex
init|=
name|PrimaryStorage
operator|.
name|of
argument_list|(
name|indexChange
argument_list|)
decl_stmt|;
name|PrimaryStorage
name|storageFromReviewDb
init|=
name|PrimaryStorage
operator|.
name|of
argument_list|(
name|reviewDbChange
argument_list|)
decl_stmt|;
if|if
condition|(
name|reviewDbChange
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|storageFromIndex
operator|==
name|PrimaryStorage
operator|.
name|REVIEW_DB
condition|)
block|{
return|return
literal|true
return|;
comment|// Index says it should have been in ReviewDb, but it wasn't.
block|}
return|return
literal|false
return|;
comment|// Not in ReviewDb, but that's ok.
block|}
name|checkArgument
argument_list|(
name|indexChange
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|reviewDbChange
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"mismatched change ID: %s != %s"
argument_list|,
name|indexChange
operator|.
name|getId
argument_list|()
argument_list|,
name|reviewDbChange
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|storageFromIndex
operator|!=
name|storageFromReviewDb
condition|)
block|{
return|return
literal|true
return|;
comment|// Primary storage differs, definitely stale.
block|}
if|if
condition|(
name|storageFromReviewDb
operator|!=
name|PrimaryStorage
operator|.
name|REVIEW_DB
condition|)
block|{
return|return
literal|false
return|;
comment|// Not a ReviewDb change, don't check rowVersion.
block|}
return|return
name|reviewDbChange
operator|.
name|getRowVersion
argument_list|()
operator|!=
name|indexChange
operator|.
name|getRowVersion
argument_list|()
return|;
block|}
DECL|method|parseStates (ChangeData cd)
specifier|private
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|parseStates
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|parseStates
argument_list|(
name|cd
operator|.
name|getRefStates
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parseStates (Iterable<byte[]> states)
specifier|public
specifier|static
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|parseStates
parameter_list|(
name|Iterable
argument_list|<
name|byte
index|[]
argument_list|>
name|states
parameter_list|)
block|{
name|RefState
operator|.
name|check
argument_list|(
name|states
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|result
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
index|[]
name|b
range|:
name|states
control|)
block|{
name|RefState
operator|.
name|check
argument_list|(
name|b
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|b
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|splitToList
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|RefState
operator|.
name|check
argument_list|(
name|parts
operator|.
name|size
argument_list|()
operator|==
literal|3
operator|&&
operator|!
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|parsePatterns (ChangeData cd)
specifier|private
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|parsePatterns
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|parsePatterns
argument_list|(
name|cd
operator|.
name|getRefStatePatterns
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parsePatterns ( Iterable<byte[]> patterns)
specifier|public
specifier|static
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|parsePatterns
parameter_list|(
name|Iterable
argument_list|<
name|byte
index|[]
argument_list|>
name|patterns
parameter_list|)
block|{
name|RefStatePattern
operator|.
name|check
argument_list|(
name|patterns
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|result
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
for|for
control|(
name|byte
index|[]
name|b
range|:
name|patterns
control|)
block|{
name|RefStatePattern
operator|.
name|check
argument_list|(
name|b
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|b
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|splitToList
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|RefStatePattern
operator|.
name|check
argument_list|(
name|parts
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|RefStatePattern
operator|.
name|create
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|refsAreStale ( GitRepositoryManager repoManager, Change.Id id, Project.NameKey project, SetMultimap<Project.NameKey, RefState> allStates, ListMultimap<Project.NameKey, RefStatePattern> allPatterns)
specifier|private
specifier|static
name|boolean
name|refsAreStale
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|allStates
parameter_list|,
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefStatePattern
argument_list|>
name|allPatterns
parameter_list|)
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
init|)
block|{
name|Set
argument_list|<
name|RefState
argument_list|>
name|states
init|=
name|allStates
operator|.
name|get
argument_list|(
name|project
argument_list|)
decl_stmt|;
for|for
control|(
name|RefState
name|state
range|:
name|states
control|)
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|match
argument_list|(
name|repo
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|RefStatePattern
name|pattern
range|:
name|allPatterns
operator|.
name|get
argument_list|(
name|project
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|pattern
operator|.
name|match
argument_list|(
name|repo
argument_list|,
name|states
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"error checking staleness of %s in %s"
argument_list|,
name|id
argument_list|,
name|project
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|/**    * Pattern for matching refs.    *    *<p>Similar to '*' syntax for native Git refspecs, but slightly more powerful: the pattern may    * contain arbitrarily many asterisks. There must be at least one '*' and the first one must    * immediately follow a '/'.    */
annotation|@
name|AutoValue
DECL|class|RefStatePattern
specifier|public
specifier|abstract
specifier|static
class|class
name|RefStatePattern
block|{
DECL|method|create (String pattern)
specifier|static
name|RefStatePattern
name|create
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|int
name|star
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
decl_stmt|;
name|check
argument_list|(
name|star
operator|>
literal|0
operator|&&
name|pattern
operator|.
name|charAt
argument_list|(
name|star
operator|-
literal|1
argument_list|)
operator|==
literal|'/'
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|star
argument_list|)
decl_stmt|;
name|check
argument_list|(
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|pattern
operator|.
name|replace
argument_list|(
literal|'*'
argument_list|,
literal|'x'
argument_list|)
argument_list|)
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
comment|// Quote everything except the '*'s, which become ".*".
name|String
name|regex
init|=
name|Streams
operator|.
name|stream
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|'*'
argument_list|)
operator|.
name|split
argument_list|(
name|pattern
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Pattern
operator|::
name|quote
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|".*"
argument_list|,
literal|"^"
argument_list|,
literal|"$"
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|AutoValue_StalenessChecker_RefStatePattern
argument_list|(
name|pattern
argument_list|,
name|prefix
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toByteArray (Project.NameKey project)
name|byte
index|[]
name|toByteArray
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
return|return
operator|(
name|project
operator|.
name|toString
argument_list|()
operator|+
literal|':'
operator|+
name|pattern
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
return|;
block|}
DECL|method|check (boolean condition, String str)
specifier|private
specifier|static
name|void
name|check
parameter_list|(
name|boolean
name|condition
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|condition
argument_list|,
literal|"invalid RefStatePattern: %s"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
DECL|method|pattern ()
specifier|abstract
name|String
name|pattern
parameter_list|()
function_decl|;
DECL|method|prefix ()
specifier|abstract
name|String
name|prefix
parameter_list|()
function_decl|;
DECL|method|regex ()
specifier|abstract
name|Pattern
name|regex
parameter_list|()
function_decl|;
DECL|method|match (String refName)
name|boolean
name|match
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
return|return
name|regex
argument_list|()
operator|.
name|matcher
argument_list|(
name|refName
argument_list|)
operator|.
name|find
argument_list|()
return|;
block|}
DECL|method|match (Repository repo, Set<RefState> expected)
specifier|private
name|boolean
name|match
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|Set
argument_list|<
name|RefState
argument_list|>
name|expected
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Ref
name|r
range|:
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|prefix
argument_list|()
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|match
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|expected
operator|.
name|contains
argument_list|(
name|RefState
operator|.
name|of
argument_list|(
name|r
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

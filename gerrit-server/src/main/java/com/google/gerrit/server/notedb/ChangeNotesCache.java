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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

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
name|cache
operator|.
name|Cache
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
name|notedb
operator|.
name|AbstractChangeNotes
operator|.
name|Args
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
name|ChangeNotesCommit
operator|.
name|ChangeNotesRevWalk
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
name|errors
operator|.
name|ConfigInvalidException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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

begin_class
annotation|@
name|Singleton
DECL|class|ChangeNotesCache
specifier|public
class|class
name|ChangeNotesCache
block|{
annotation|@
name|VisibleForTesting
DECL|field|CACHE_NAME
specifier|static
specifier|final
name|String
name|CACHE_NAME
init|=
literal|"change_notes"
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
name|bind
argument_list|(
name|ChangeNotesCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|CACHE_NAME
argument_list|,
name|Key
operator|.
name|class
argument_list|,
name|ChangeNotesState
operator|.
name|class
argument_list|)
operator|.
name|weigher
argument_list|(
name|Weigher
operator|.
name|class
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|10
operator|<<
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
block|{
DECL|method|project ()
specifier|abstract
name|Project
operator|.
name|NameKey
name|project
parameter_list|()
function_decl|;
DECL|method|changeId ()
specifier|abstract
name|Change
operator|.
name|Id
name|changeId
parameter_list|()
function_decl|;
DECL|method|id ()
specifier|abstract
name|ObjectId
name|id
parameter_list|()
function_decl|;
block|}
DECL|class|Weigher
specifier|public
specifier|static
class|class
name|Weigher
implements|implements
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Weigher
argument_list|<
name|Key
argument_list|,
name|ChangeNotesState
argument_list|>
block|{
comment|// Single object overhead.
DECL|field|O
specifier|private
specifier|static
specifier|final
name|int
name|O
init|=
literal|16
decl_stmt|;
comment|// Single pointer overhead.
DECL|field|P
specifier|private
specifier|static
specifier|final
name|int
name|P
init|=
literal|8
decl_stmt|;
comment|// Single IntKey overhead.
DECL|field|K
specifier|private
specifier|static
specifier|final
name|int
name|K
init|=
name|O
operator|+
literal|4
decl_stmt|;
comment|// Single Timestamp overhead.
DECL|field|T
specifier|private
specifier|static
specifier|final
name|int
name|T
init|=
name|O
operator|+
literal|8
decl_stmt|;
annotation|@
name|Override
DECL|method|weigh (Key key, ChangeNotesState state)
specifier|public
name|int
name|weigh
parameter_list|(
name|Key
name|key
parameter_list|,
name|ChangeNotesState
name|state
parameter_list|)
block|{
comment|// Take all columns and all collection sizes into account, but use
comment|// estimated average element sizes rather than iterating over collections.
comment|// Numbers are largely hand-wavy based on
comment|// http://stackoverflow.com/questions/258120/what-is-the-memory-consumption-of-an-object-in-java
return|return
name|K
comment|// changeId
operator|+
name|str
argument_list|(
literal|40
argument_list|)
comment|// changeKey
operator|+
name|T
comment|// createdOn
operator|+
name|T
comment|// lastUpdatedOn
operator|+
name|P
operator|+
name|K
comment|// owner
operator|+
name|P
operator|+
name|str
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|patchSetId
argument_list|()
comment|// currentPatchSetId
operator|+
name|P
operator|+
name|str
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|subject
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|str
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|topic
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|str
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|originalSubject
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|str
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|submissionId
argument_list|()
argument_list|)
operator|+
name|ptr
argument_list|(
name|state
operator|.
name|columns
argument_list|()
operator|.
name|assignee
argument_list|()
argument_list|,
name|K
argument_list|)
comment|// assignee
operator|+
name|P
comment|// status
operator|+
name|P
operator|+
name|set
argument_list|(
name|state
operator|.
name|pastAssignees
argument_list|()
argument_list|,
name|K
argument_list|)
operator|+
name|P
operator|+
name|set
argument_list|(
name|state
operator|.
name|hashtags
argument_list|()
argument_list|,
name|str
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|+
name|P
operator|+
name|list
argument_list|(
name|state
operator|.
name|patchSets
argument_list|()
argument_list|,
name|patchSet
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|list
argument_list|(
name|state
operator|.
name|allPastReviewers
argument_list|()
argument_list|,
name|approval
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|list
argument_list|(
name|state
operator|.
name|reviewerUpdates
argument_list|()
argument_list|,
literal|4
operator|*
name|O
operator|+
name|K
operator|+
name|K
operator|+
name|P
argument_list|)
operator|+
name|P
operator|+
name|list
argument_list|(
name|state
operator|.
name|submitRecords
argument_list|()
argument_list|,
name|P
operator|+
name|list
argument_list|(
literal|2
argument_list|,
name|str
argument_list|(
literal|4
argument_list|)
operator|+
name|P
operator|+
name|K
argument_list|)
operator|+
name|P
argument_list|)
operator|+
name|P
operator|+
name|list
argument_list|(
name|state
operator|.
name|allChangeMessages
argument_list|()
argument_list|,
name|changeMessage
argument_list|()
argument_list|)
comment|// Just key overhead for map, already counted messages in previous.
operator|+
name|P
operator|+
name|map
argument_list|(
name|state
operator|.
name|changeMessagesByPatchSet
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|,
name|patchSetId
argument_list|()
argument_list|)
operator|+
name|P
operator|+
name|map
argument_list|(
name|state
operator|.
name|publishedComments
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|,
name|comment
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ptr (Object o, int size)
specifier|private
specifier|static
name|int
name|ptr
parameter_list|(
name|Object
name|o
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|o
operator|!=
literal|null
condition|?
name|P
operator|+
name|size
else|:
name|P
return|;
block|}
DECL|method|str (String s)
specifier|private
specifier|static
name|int
name|str
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
name|P
return|;
block|}
return|return
name|str
argument_list|(
name|s
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
DECL|method|str (int n)
specifier|private
specifier|static
name|int
name|str
parameter_list|(
name|int
name|n
parameter_list|)
block|{
return|return
literal|8
operator|+
literal|24
operator|+
literal|2
operator|*
name|n
return|;
block|}
DECL|method|patchSetId ()
specifier|private
specifier|static
name|int
name|patchSetId
parameter_list|()
block|{
return|return
name|O
operator|+
literal|4
operator|+
name|O
operator|+
literal|4
return|;
block|}
DECL|method|set (Set<?> set, int elemSize)
specifier|private
specifier|static
name|int
name|set
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|set
parameter_list|,
name|int
name|elemSize
parameter_list|)
block|{
if|if
condition|(
name|set
operator|==
literal|null
condition|)
block|{
return|return
name|P
return|;
block|}
return|return
name|hashtable
argument_list|(
name|set
operator|.
name|size
argument_list|()
argument_list|,
name|elemSize
argument_list|)
return|;
block|}
DECL|method|map (Map<?, ?> map, int elemSize)
specifier|private
specifier|static
name|int
name|map
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|,
name|int
name|elemSize
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
name|P
return|;
block|}
return|return
name|hashtable
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|elemSize
argument_list|)
return|;
block|}
DECL|method|hashtable (int n, int elemSize)
specifier|private
specifier|static
name|int
name|hashtable
parameter_list|(
name|int
name|n
parameter_list|,
name|int
name|elemSize
parameter_list|)
block|{
comment|// Made up numbers.
name|int
name|overhead
init|=
literal|32
decl_stmt|;
name|int
name|elemOverhead
init|=
name|O
operator|+
literal|32
decl_stmt|;
return|return
name|overhead
operator|+
name|elemOverhead
operator|*
name|n
operator|*
name|elemSize
return|;
block|}
DECL|method|list (List<?> list, int elemSize)
specifier|private
specifier|static
name|int
name|list
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|,
name|int
name|elemSize
parameter_list|)
block|{
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
return|return
name|P
return|;
block|}
return|return
name|list
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|,
name|elemSize
argument_list|)
return|;
block|}
DECL|method|list (int n, int elemSize)
specifier|private
specifier|static
name|int
name|list
parameter_list|(
name|int
name|n
parameter_list|,
name|int
name|elemSize
parameter_list|)
block|{
return|return
name|O
operator|+
name|O
operator|+
name|n
operator|*
operator|(
name|P
operator|+
name|elemSize
operator|)
return|;
block|}
DECL|method|patchSet ()
specifier|private
specifier|static
name|int
name|patchSet
parameter_list|()
block|{
return|return
name|O
operator|+
name|P
operator|+
name|patchSetId
argument_list|()
operator|+
name|str
argument_list|(
literal|40
argument_list|)
comment|// revision
operator|+
name|P
operator|+
name|K
comment|// uploader
operator|+
name|P
operator|+
name|T
comment|// createdOn
operator|+
literal|1
comment|// draft
operator|+
name|str
argument_list|(
literal|40
argument_list|)
comment|// groups
operator|+
name|P
return|;
comment|// pushCertificate
block|}
DECL|method|approval ()
specifier|private
specifier|static
name|int
name|approval
parameter_list|()
block|{
return|return
name|O
operator|+
name|P
operator|+
name|patchSetId
argument_list|()
operator|+
name|P
operator|+
name|K
operator|+
name|P
operator|+
name|O
operator|+
name|str
argument_list|(
literal|10
argument_list|)
operator|+
literal|2
comment|// value
operator|+
name|P
operator|+
name|T
comment|// granted
operator|+
name|P
comment|// tag
operator|+
name|P
return|;
comment|// realAccountId
block|}
DECL|method|changeMessage ()
specifier|private
specifier|static
name|int
name|changeMessage
parameter_list|()
block|{
name|int
name|key
init|=
name|K
operator|+
name|str
argument_list|(
literal|20
argument_list|)
decl_stmt|;
return|return
name|O
operator|+
name|P
operator|+
name|key
operator|+
name|P
operator|+
name|K
comment|// author
operator|+
name|P
operator|+
name|T
comment|// writtenON
operator|+
name|str
argument_list|(
literal|64
argument_list|)
comment|// message
operator|+
name|P
operator|+
name|patchSetId
argument_list|()
operator|+
name|P
operator|+
name|P
return|;
comment|// realAuthor
block|}
DECL|method|comment ()
specifier|private
specifier|static
name|int
name|comment
parameter_list|()
block|{
name|int
name|key
init|=
name|P
operator|+
name|str
argument_list|(
literal|20
argument_list|)
operator|+
name|P
operator|+
name|str
argument_list|(
literal|32
argument_list|)
operator|+
literal|4
decl_stmt|;
name|int
name|ident
init|=
name|O
operator|+
literal|4
decl_stmt|;
return|return
name|O
operator|+
name|P
operator|+
name|key
operator|+
literal|4
comment|// lineNbr
operator|+
name|P
operator|+
name|ident
comment|// author
operator|+
name|P
operator|+
name|ident
comment|//realAuthor
operator|+
name|P
operator|+
name|T
comment|// writtenOn
operator|+
literal|2
comment|// side
operator|+
name|str
argument_list|(
literal|32
argument_list|)
comment|// message
operator|+
name|str
argument_list|(
literal|10
argument_list|)
comment|// parentUuid
operator|+
operator|(
name|P
operator|+
name|O
operator|+
literal|4
operator|+
literal|4
operator|+
literal|4
operator|+
literal|4
operator|)
operator|/
literal|2
comment|// range on 50% of comments
operator|+
name|P
comment|// tag
operator|+
name|P
operator|+
name|str
argument_list|(
literal|40
argument_list|)
comment|// revId
operator|+
name|P
operator|+
name|str
argument_list|(
literal|36
argument_list|)
return|;
comment|// serverId
block|}
block|}
annotation|@
name|AutoValue
DECL|class|Value
specifier|abstract
specifier|static
class|class
name|Value
block|{
DECL|method|state ()
specifier|abstract
name|ChangeNotesState
name|state
parameter_list|()
function_decl|;
comment|/**      * The {@link RevisionNoteMap} produced while parsing this change.      *<p>      * These instances are mutable and non-threadsafe, so it is only safe to      * return it to the caller that actually incurred the cache miss. It is only      * used as an optimization; {@link ChangeNotes} is capable of lazily loading      * it as necessary.      */
DECL|method|revisionNoteMap ()
annotation|@
name|Nullable
specifier|abstract
name|RevisionNoteMap
argument_list|<
name|ChangeRevisionNote
argument_list|>
name|revisionNoteMap
parameter_list|()
function_decl|;
block|}
DECL|class|Loader
specifier|private
class|class
name|Loader
implements|implements
name|Callable
argument_list|<
name|ChangeNotesState
argument_list|>
block|{
DECL|field|key
specifier|private
specifier|final
name|Key
name|key
decl_stmt|;
DECL|field|rw
specifier|private
specifier|final
name|ChangeNotesRevWalk
name|rw
decl_stmt|;
DECL|field|revisionNoteMap
specifier|private
name|RevisionNoteMap
argument_list|<
name|ChangeRevisionNote
argument_list|>
name|revisionNoteMap
decl_stmt|;
DECL|method|Loader (Key key, ChangeNotesRevWalk rw)
specifier|private
name|Loader
parameter_list|(
name|Key
name|key
parameter_list|,
name|ChangeNotesRevWalk
name|rw
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ChangeNotesState
name|call
parameter_list|()
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|ChangeNotesParser
name|parser
init|=
operator|new
name|ChangeNotesParser
argument_list|(
name|key
operator|.
name|changeId
argument_list|()
argument_list|,
name|key
operator|.
name|id
argument_list|()
argument_list|,
name|rw
argument_list|,
name|args
operator|.
name|noteUtil
argument_list|,
name|args
operator|.
name|metrics
argument_list|)
decl_stmt|;
name|ChangeNotesState
name|result
init|=
name|parser
operator|.
name|parseAll
argument_list|()
decl_stmt|;
comment|// This assignment only happens if call() was actually called, which only
comment|// happens when Cache#get(K, Callable<V>) incurs a cache miss.
name|revisionNoteMap
operator|=
name|parser
operator|.
name|getRevisionNoteMap
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
DECL|field|cache
specifier|private
specifier|final
name|Cache
argument_list|<
name|Key
argument_list|,
name|ChangeNotesState
argument_list|>
name|cache
decl_stmt|;
DECL|field|args
specifier|private
specifier|final
name|Args
name|args
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeNotesCache ( @amedCACHE_NAME) Cache<Key, ChangeNotesState> cache, Args args)
name|ChangeNotesCache
parameter_list|(
annotation|@
name|Named
argument_list|(
name|CACHE_NAME
argument_list|)
name|Cache
argument_list|<
name|Key
argument_list|,
name|ChangeNotesState
argument_list|>
name|cache
parameter_list|,
name|Args
name|args
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
name|args
operator|=
name|args
expr_stmt|;
block|}
DECL|method|get (Project.NameKey project, Change.Id changeId, ObjectId metaId, ChangeNotesRevWalk rw)
name|Value
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ObjectId
name|metaId
parameter_list|,
name|ChangeNotesRevWalk
name|rw
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Key
name|key
init|=
operator|new
name|AutoValue_ChangeNotesCache_Key
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|,
name|metaId
operator|.
name|copy
argument_list|()
argument_list|)
decl_stmt|;
name|Loader
name|loader
init|=
operator|new
name|Loader
argument_list|(
name|key
argument_list|,
name|rw
argument_list|)
decl_stmt|;
name|ChangeNotesState
name|s
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|,
name|loader
argument_list|)
decl_stmt|;
return|return
operator|new
name|AutoValue_ChangeNotesCache_Value
argument_list|(
name|s
argument_list|,
name|loader
operator|.
name|revisionNoteMap
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error loading %s in %s at %s"
argument_list|,
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|,
name|project
argument_list|,
name|metaId
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


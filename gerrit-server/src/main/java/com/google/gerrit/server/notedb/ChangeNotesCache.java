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
name|maximumWeight
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
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


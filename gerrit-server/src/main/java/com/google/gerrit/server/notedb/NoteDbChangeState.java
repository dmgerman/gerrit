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
operator|.
name|changeMetaRef
import|;
end_import

begin_import
import|import static
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
operator|.
name|refsDraftComments
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
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
name|Optional
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
name|Predicates
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
name|ImmutableMap
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
name|Lists
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
name|server
operator|.
name|git
operator|.
name|RefCache
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * The state of all relevant NoteDb refs across all repos corresponding to a  * given Change entity.  *<p>  * Stored serialized in the {@code Change#noteDbState} field, and used to  * determine whether the state in NoteDb is out of date.  *<p>  * Serialized in the form:  *<pre>  *   [meta-sha],[account1]=[drafts-sha],[account2]=[drafts-sha]...  *</pre>  * in numeric account ID order, with hex SHA-1s for human readability.  */
end_comment

begin_class
DECL|class|NoteDbChangeState
specifier|public
class|class
name|NoteDbChangeState
block|{
annotation|@
name|AutoValue
DECL|class|Delta
specifier|public
specifier|abstract
specifier|static
class|class
name|Delta
block|{
DECL|method|create (Change.Id changeId, Optional<ObjectId> newChangeMetaId, Map<Account.Id, ObjectId> newDraftIds)
specifier|static
name|Delta
name|create
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|newChangeMetaId
parameter_list|,
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|newDraftIds
parameter_list|)
block|{
if|if
condition|(
name|newDraftIds
operator|==
literal|null
condition|)
block|{
name|newDraftIds
operator|=
name|ImmutableMap
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_NoteDbChangeState_Delta
argument_list|(
name|changeId
argument_list|,
name|newChangeMetaId
argument_list|,
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|newDraftIds
argument_list|)
argument_list|)
return|;
block|}
DECL|method|changeId ()
specifier|abstract
name|Change
operator|.
name|Id
name|changeId
parameter_list|()
function_decl|;
DECL|method|newChangeMetaId ()
specifier|abstract
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|newChangeMetaId
parameter_list|()
function_decl|;
DECL|method|newDraftIds ()
specifier|abstract
name|ImmutableMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|newDraftIds
parameter_list|()
function_decl|;
block|}
DECL|method|parse (Change c)
specifier|public
specifier|static
name|NoteDbChangeState
name|parse
parameter_list|(
name|Change
name|c
parameter_list|)
block|{
return|return
name|parse
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|c
operator|.
name|getNoteDbState
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|parse (Change.Id id, String str)
specifier|public
specifier|static
name|NoteDbChangeState
name|parse
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
literal|','
argument_list|)
operator|.
name|splitToList
argument_list|(
name|str
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|parts
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"invalid state string for change %s: %s"
argument_list|,
name|id
argument_list|,
name|str
argument_list|)
expr_stmt|;
name|ObjectId
name|changeMetaId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|draftIds
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|parts
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|Splitter
name|s
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|parts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|p
init|=
name|parts
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|draftParts
init|=
name|s
operator|.
name|splitToList
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|draftParts
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|,
literal|"invalid draft state part for change %s: %s"
argument_list|,
name|id
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|draftIds
operator|.
name|put
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|draftParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|draftParts
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
operator|new
name|NoteDbChangeState
argument_list|(
name|id
argument_list|,
name|changeMetaId
argument_list|,
name|draftIds
argument_list|)
return|;
block|}
DECL|method|applyDelta (Change change, Delta delta)
specifier|public
specifier|static
name|NoteDbChangeState
name|applyDelta
parameter_list|(
name|Change
name|change
parameter_list|,
name|Delta
name|delta
parameter_list|)
block|{
if|if
condition|(
name|delta
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|oldStr
init|=
name|change
operator|.
name|getNoteDbState
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldStr
operator|==
literal|null
operator|&&
operator|!
name|delta
operator|.
name|newChangeMetaId
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
comment|// Neither an old nor a new meta ID was present, most likely because we
comment|// aren't writing a NoteDb graph at all for this change at this point. No
comment|// point in proceeding.
return|return
literal|null
return|;
block|}
name|NoteDbChangeState
name|oldState
init|=
name|parse
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|oldStr
argument_list|)
decl_stmt|;
name|ObjectId
name|changeMetaId
decl_stmt|;
if|if
condition|(
name|delta
operator|.
name|newChangeMetaId
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|changeMetaId
operator|=
name|delta
operator|.
name|newChangeMetaId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|changeMetaId
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|change
operator|.
name|setNoteDbState
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|changeMetaId
operator|=
name|oldState
operator|.
name|changeMetaId
expr_stmt|;
block|}
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|draftIds
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldState
operator|!=
literal|null
condition|)
block|{
name|draftIds
operator|.
name|putAll
argument_list|(
name|oldState
operator|.
name|draftIds
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|e
range|:
name|delta
operator|.
name|newDraftIds
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|draftIds
operator|.
name|remove
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|draftIds
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|NoteDbChangeState
name|state
init|=
operator|new
name|NoteDbChangeState
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|changeMetaId
argument_list|,
name|draftIds
argument_list|)
decl_stmt|;
name|change
operator|.
name|setNoteDbState
argument_list|(
name|state
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
DECL|method|isChangeUpToDate (@ullable NoteDbChangeState state, RefCache changeRepoRefs, Change.Id changeId)
specifier|public
specifier|static
name|boolean
name|isChangeUpToDate
parameter_list|(
annotation|@
name|Nullable
name|NoteDbChangeState
name|state
parameter_list|,
name|RefCache
name|changeRepoRefs
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return
operator|!
name|changeRepoRefs
operator|.
name|get
argument_list|(
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
return|;
block|}
return|return
name|state
operator|.
name|isChangeUpToDate
argument_list|(
name|changeRepoRefs
argument_list|)
return|;
block|}
DECL|method|areDraftsUpToDate (@ullable NoteDbChangeState state, RefCache draftsRepoRefs, Change.Id changeId, Account.Id accountId)
specifier|public
specifier|static
name|boolean
name|areDraftsUpToDate
parameter_list|(
annotation|@
name|Nullable
name|NoteDbChangeState
name|state
parameter_list|,
name|RefCache
name|draftsRepoRefs
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return
operator|!
name|draftsRepoRefs
operator|.
name|get
argument_list|(
name|refsDraftComments
argument_list|(
name|changeId
argument_list|,
name|accountId
argument_list|)
argument_list|)
operator|.
name|isPresent
argument_list|()
return|;
block|}
return|return
name|state
operator|.
name|areDraftsUpToDate
argument_list|(
name|draftsRepoRefs
argument_list|,
name|accountId
argument_list|)
return|;
block|}
DECL|method|toString (ObjectId changeMetaId, Map<Account.Id, ObjectId> draftIds)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ObjectId
name|changeMetaId
parameter_list|,
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|draftIds
parameter_list|)
block|{
name|List
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|draftIds
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|accountIds
argument_list|,
name|comparing
argument_list|(
name|Account
operator|.
name|Id
operator|::
name|get
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|changeMetaId
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|accountIds
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|draftIds
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|changeMetaId
specifier|private
specifier|final
name|ObjectId
name|changeMetaId
decl_stmt|;
DECL|field|draftIds
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|draftIds
decl_stmt|;
DECL|method|NoteDbChangeState (Change.Id changeId, ObjectId changeMetaId, Map<Account.Id, ObjectId> draftIds)
specifier|public
name|NoteDbChangeState
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ObjectId
name|changeMetaId
parameter_list|,
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|draftIds
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|checkNotNull
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeMetaId
operator|=
name|checkNotNull
argument_list|(
name|changeMetaId
argument_list|)
expr_stmt|;
name|this
operator|.
name|draftIds
operator|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|Maps
operator|.
name|filterValues
argument_list|(
name|draftIds
argument_list|,
name|Predicates
operator|.
name|not
argument_list|(
name|Predicates
operator|.
name|equalTo
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isChangeUpToDate (RefCache changeRepoRefs)
specifier|public
name|boolean
name|isChangeUpToDate
parameter_list|(
name|RefCache
name|changeRepoRefs
parameter_list|)
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|id
init|=
name|changeRepoRefs
operator|.
name|get
argument_list|(
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|changeMetaId
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
return|;
block|}
return|return
name|id
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|changeMetaId
argument_list|)
return|;
block|}
DECL|method|areDraftsUpToDate (RefCache draftsRepoRefs, Account.Id accountId)
specifier|public
name|boolean
name|areDraftsUpToDate
parameter_list|(
name|RefCache
name|draftsRepoRefs
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|id
init|=
name|draftsRepoRefs
operator|.
name|get
argument_list|(
name|refsDraftComments
argument_list|(
name|changeId
argument_list|,
name|accountId
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
operator|!
name|draftIds
operator|.
name|containsKey
argument_list|(
name|accountId
argument_list|)
return|;
block|}
return|return
name|id
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|draftIds
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isUpToDate (RefCache changeRepoRefs, RefCache draftsRepoRefs)
specifier|public
name|boolean
name|isUpToDate
parameter_list|(
name|RefCache
name|changeRepoRefs
parameter_list|,
name|RefCache
name|draftsRepoRefs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isChangeUpToDate
argument_list|(
name|changeRepoRefs
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|draftIds
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|areDraftsUpToDate
argument_list|(
name|draftsRepoRefs
argument_list|,
name|accountId
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
annotation|@
name|VisibleForTesting
DECL|method|getChangeId ()
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|getChangeMetaId ()
specifier|public
name|ObjectId
name|getChangeMetaId
parameter_list|()
block|{
return|return
name|changeMetaId
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|getDraftIds ()
name|ImmutableMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ObjectId
argument_list|>
name|getDraftIds
parameter_list|()
block|{
return|return
name|draftIds
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toString
argument_list|(
name|changeMetaId
argument_list|,
name|draftIds
argument_list|)
return|;
block|}
block|}
end_class

end_unit


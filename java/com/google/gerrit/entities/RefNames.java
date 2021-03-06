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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

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
name|UsedAt
import|;
end_import

begin_comment
comment|/** Constants and utilities for Gerrit-specific ref names. */
end_comment

begin_class
DECL|class|RefNames
specifier|public
class|class
name|RefNames
block|{
DECL|field|HEAD
specifier|public
specifier|static
specifier|final
name|String
name|HEAD
init|=
literal|"HEAD"
decl_stmt|;
DECL|field|REFS
specifier|public
specifier|static
specifier|final
name|String
name|REFS
init|=
literal|"refs/"
decl_stmt|;
DECL|field|REFS_HEADS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_HEADS
init|=
literal|"refs/heads/"
decl_stmt|;
DECL|field|REFS_TAGS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_TAGS
init|=
literal|"refs/tags/"
decl_stmt|;
DECL|field|REFS_CHANGES
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CHANGES
init|=
literal|"refs/changes/"
decl_stmt|;
DECL|field|REFS_META
specifier|public
specifier|static
specifier|final
name|String
name|REFS_META
init|=
literal|"refs/meta/"
decl_stmt|;
comment|/** Note tree listing commits we refuse {@code refs/meta/reject-commits} */
DECL|field|REFS_REJECT_COMMITS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_REJECT_COMMITS
init|=
literal|"refs/meta/reject-commits"
decl_stmt|;
comment|/** Configuration settings for a project {@code refs/meta/config} */
DECL|field|REFS_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CONFIG
init|=
literal|"refs/meta/config"
decl_stmt|;
comment|/** Note tree listing external IDs */
DECL|field|REFS_EXTERNAL_IDS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_EXTERNAL_IDS
init|=
literal|"refs/meta/external-ids"
decl_stmt|;
comment|/** Magic user branch in All-Users {@code refs/users/self} */
DECL|field|REFS_USERS_SELF
specifier|public
specifier|static
specifier|final
name|String
name|REFS_USERS_SELF
init|=
literal|"refs/users/self"
decl_stmt|;
comment|/** Default user preference settings */
DECL|field|REFS_USERS_DEFAULT
specifier|public
specifier|static
specifier|final
name|String
name|REFS_USERS_DEFAULT
init|=
name|RefNames
operator|.
name|REFS_USERS
operator|+
literal|"default"
decl_stmt|;
comment|/** Configurations of project-specific dashboards (canned search queries). */
DECL|field|REFS_DASHBOARDS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_DASHBOARDS
init|=
literal|"refs/meta/dashboards/"
decl_stmt|;
comment|/** Sequence counters in NoteDb. */
DECL|field|REFS_SEQUENCES
specifier|public
specifier|static
specifier|final
name|String
name|REFS_SEQUENCES
init|=
literal|"refs/sequences/"
decl_stmt|;
comment|/** NoteDb schema version number. */
DECL|field|REFS_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|REFS_VERSION
init|=
literal|"refs/meta/version"
decl_stmt|;
comment|/**    * Prefix applied to merge commit base nodes.    *    *<p>References in this directory should take the form {@code refs/cache-automerge/xx/yyyy...}    * where xx is the first two digits of the merge commit's object name, and yyyyy... is the    * remaining 38. The reference should point to a treeish that is the automatic merge result of the    * merge commit's parents.    */
DECL|field|REFS_CACHE_AUTOMERGE
specifier|public
specifier|static
specifier|final
name|String
name|REFS_CACHE_AUTOMERGE
init|=
literal|"refs/cache-automerge/"
decl_stmt|;
comment|/** Suffix of a meta ref in the NoteDb. */
DECL|field|META_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|META_SUFFIX
init|=
literal|"/meta"
decl_stmt|;
comment|/** Suffix of a ref that stores robot comments in the NoteDb. */
DECL|field|ROBOT_COMMENTS_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|ROBOT_COMMENTS_SUFFIX
init|=
literal|"/robot-comments"
decl_stmt|;
DECL|field|EDIT_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|EDIT_PREFIX
init|=
literal|"edit-"
decl_stmt|;
comment|/*    * The following refs contain an account ID and should be visible only to that account.    *    * Parsing the account ID from the ref is implemented in Account.Id#fromRef(String). This ensures    * that VisibleRefFilter hides those refs from other users.    *    * This applies to:    * - User branches (e.g. 'refs/users/23/1011123')    * - Draft comment refs (e.g. 'refs/draft-comments/73/67473/1011123')    * - Starred changes refs (e.g. 'refs/starred-changes/73/67473/1011123')    */
comment|/** Preference settings for a user {@code refs/users} */
DECL|field|REFS_USERS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_USERS
init|=
literal|"refs/users/"
decl_stmt|;
comment|/** NoteDb ref for a group {@code refs/groups} */
DECL|field|REFS_GROUPS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_GROUPS
init|=
literal|"refs/groups/"
decl_stmt|;
comment|/** NoteDb ref for the NoteMap of all group names */
DECL|field|REFS_GROUPNAMES
specifier|public
specifier|static
specifier|final
name|String
name|REFS_GROUPNAMES
init|=
literal|"refs/meta/group-names"
decl_stmt|;
comment|/**    * NoteDb ref for deleted groups {@code refs/deleted-groups}. This ref namespace is foreseen as an    * attic for deleted groups (it's reserved but not used yet)    */
DECL|field|REFS_DELETED_GROUPS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_DELETED_GROUPS
init|=
literal|"refs/deleted-groups/"
decl_stmt|;
comment|/** Draft inline comments of a user on a change */
DECL|field|REFS_DRAFT_COMMENTS
specifier|public
specifier|static
specifier|final
name|String
name|REFS_DRAFT_COMMENTS
init|=
literal|"refs/draft-comments/"
decl_stmt|;
comment|/** A change starred by a user */
DECL|field|REFS_STARRED_CHANGES
specifier|public
specifier|static
specifier|final
name|String
name|REFS_STARRED_CHANGES
init|=
literal|"refs/starred-changes/"
decl_stmt|;
DECL|method|fullName (String ref)
specifier|public
specifier|static
name|String
name|fullName
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
operator|(
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS
argument_list|)
operator|||
name|ref
operator|.
name|equals
argument_list|(
name|HEAD
argument_list|)
operator|)
condition|?
name|ref
else|:
name|REFS_HEADS
operator|+
name|ref
return|;
block|}
DECL|method|shortName (String ref)
specifier|public
specifier|static
specifier|final
name|String
name|shortName
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_HEADS
argument_list|)
condition|)
block|{
return|return
name|ref
operator|.
name|substring
argument_list|(
name|REFS_HEADS
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_TAGS
argument_list|)
condition|)
block|{
return|return
name|ref
operator|.
name|substring
argument_list|(
name|REFS_TAGS
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|ref
return|;
block|}
DECL|method|changeMetaRef (Change.Id id)
specifier|public
specifier|static
name|String
name|changeMetaRef
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|REFS_CHANGES
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|,
name|r
argument_list|)
operator|.
name|append
argument_list|(
name|META_SUFFIX
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|patchSetRef (PatchSet.Id id)
specifier|public
specifier|static
name|String
name|patchSetRef
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|REFS_CHANGES
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|id
operator|.
name|changeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|r
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
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
name|toString
argument_list|()
return|;
block|}
DECL|method|changeRefPrefix (Change.Id id)
specifier|public
specifier|static
name|String
name|changeRefPrefix
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|REFS_CHANGES
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|,
name|r
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|robotCommentsRef (Change.Id id)
specifier|public
specifier|static
name|String
name|robotCommentsRef
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|REFS_CHANGES
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|,
name|r
argument_list|)
operator|.
name|append
argument_list|(
name|ROBOT_COMMENTS_SUFFIX
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isNoteDbMetaRef (String ref)
specifier|public
specifier|static
name|boolean
name|isNoteDbMetaRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_CHANGES
argument_list|)
operator|&&
operator|(
name|ref
operator|.
name|endsWith
argument_list|(
name|META_SUFFIX
argument_list|)
operator|||
name|ref
operator|.
name|endsWith
argument_list|(
name|ROBOT_COMMENTS_SUFFIX
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_DRAFT_COMMENTS
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_STARRED_CHANGES
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** True if the provided ref is in {@code refs/changes/*}. */
DECL|method|isRefsChanges (String ref)
specifier|public
specifier|static
name|boolean
name|isRefsChanges
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_CHANGES
argument_list|)
return|;
block|}
DECL|method|refsGroups (AccountGroup.UUID groupUuid)
specifier|public
specifier|static
name|String
name|refsGroups
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
return|return
name|REFS_GROUPS
operator|+
name|shardUuid
argument_list|(
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|refsDeletedGroups (AccountGroup.UUID groupUuid)
specifier|public
specifier|static
name|String
name|refsDeletedGroups
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
return|return
name|REFS_DELETED_GROUPS
operator|+
name|shardUuid
argument_list|(
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|refsUsers (Account.Id accountId)
specifier|public
specifier|static
name|String
name|refsUsers
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|REFS_USERS
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|accountId
operator|.
name|get
argument_list|()
argument_list|,
name|r
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|refsDraftComments (Change.Id changeId, Account.Id accountId)
specifier|public
specifier|static
name|String
name|refsDraftComments
parameter_list|(
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
block|{
return|return
name|buildRefsPrefix
argument_list|(
name|REFS_DRAFT_COMMENTS
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|refsDraftCommentsPrefix (Change.Id changeId)
specifier|public
specifier|static
name|String
name|refsDraftCommentsPrefix
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
return|return
name|buildRefsPrefix
argument_list|(
name|REFS_DRAFT_COMMENTS
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|refsStarredChanges (Change.Id changeId, Account.Id accountId)
specifier|public
specifier|static
name|String
name|refsStarredChanges
parameter_list|(
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
block|{
return|return
name|buildRefsPrefix
argument_list|(
name|REFS_STARRED_CHANGES
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|refsStarredChangesPrefix (Change.Id changeId)
specifier|public
specifier|static
name|String
name|refsStarredChangesPrefix
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
return|return
name|buildRefsPrefix
argument_list|(
name|REFS_STARRED_CHANGES
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|buildRefsPrefix (String prefix, int id)
specifier|private
specifier|static
name|StringBuilder
name|buildRefsPrefix
parameter_list|(
name|String
name|prefix
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
name|newStringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
return|return
name|shard
argument_list|(
name|id
argument_list|,
name|r
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
return|;
block|}
DECL|method|refsCacheAutomerge (String hash)
specifier|public
specifier|static
name|String
name|refsCacheAutomerge
parameter_list|(
name|String
name|hash
parameter_list|)
block|{
return|return
name|REFS_CACHE_AUTOMERGE
operator|+
name|hash
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
operator|+
literal|'/'
operator|+
name|hash
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
return|;
block|}
DECL|method|shard (int id)
specifier|public
specifier|static
name|String
name|shard
parameter_list|(
name|int
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|shard
argument_list|(
name|id
argument_list|,
name|newStringBuilder
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|shard (int id, StringBuilder sb)
specifier|private
specifier|static
name|StringBuilder
name|shard
parameter_list|(
name|int
name|id
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
name|int
name|n
init|=
name|id
operator|%
literal|100
decl_stmt|;
if|if
condition|(
name|n
operator|<
literal|10
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGINS_ALL
argument_list|)
DECL|method|shardUuid (String uuid)
specifier|public
specifier|static
name|String
name|shardUuid
parameter_list|(
name|String
name|uuid
parameter_list|)
block|{
if|if
condition|(
name|uuid
operator|==
literal|null
operator|||
name|uuid
operator|.
name|length
argument_list|()
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"UUIDs must consist of at least two characters"
argument_list|)
throw|;
block|}
return|return
name|uuid
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
operator|+
literal|'/'
operator|+
name|uuid
return|;
block|}
comment|/**    * Returns reference for this change edit with sharded user and change number:    * refs/users/UU/UUUU/edit-CCCC/P.    *    * @param accountId account id    * @param changeId change number    * @param psId patch set number    * @return reference for this change edit    */
DECL|method|refsEdit (Account.Id accountId, Change.Id changeId, PatchSet.Id psId)
specifier|public
specifier|static
name|String
name|refsEdit
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
return|return
name|refsEditPrefix
argument_list|(
name|accountId
argument_list|,
name|changeId
argument_list|)
operator|+
name|psId
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Returns reference prefix for this change edit with sharded user and change number:    * refs/users/UU/UUUU/edit-CCCC/.    *    * @param accountId account id    * @param changeId change number    * @return reference prefix for this change edit    */
DECL|method|refsEditPrefix (Account.Id accountId, Change.Id changeId)
specifier|public
specifier|static
name|String
name|refsEditPrefix
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
return|return
name|refsEditPrefix
argument_list|(
name|accountId
argument_list|)
operator|+
name|changeId
operator|.
name|get
argument_list|()
operator|+
literal|'/'
return|;
block|}
DECL|method|refsEditPrefix (Account.Id accountId)
specifier|public
specifier|static
name|String
name|refsEditPrefix
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|refsUsers
argument_list|(
name|accountId
argument_list|)
operator|+
literal|'/'
operator|+
name|EDIT_PREFIX
return|;
block|}
DECL|method|isRefsEdit (String ref)
specifier|public
specifier|static
name|boolean
name|isRefsEdit
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_USERS
argument_list|)
operator|&&
name|ref
operator|.
name|contains
argument_list|(
name|EDIT_PREFIX
argument_list|)
return|;
block|}
DECL|method|isRefsUsers (String ref)
specifier|public
specifier|static
name|boolean
name|isRefsUsers
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_USERS
argument_list|)
return|;
block|}
comment|/**    * Whether the ref is a group branch that stores NoteDb data of a group. Returns {@code true} for    * all refs that start with {@code refs/groups/}.    */
DECL|method|isRefsGroups (String ref)
specifier|public
specifier|static
name|boolean
name|isRefsGroups
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_GROUPS
argument_list|)
return|;
block|}
comment|/**    * Whether the ref is a group branch that stores NoteDb data of a deleted group. Returns {@code    * true} for all refs that start with {@code refs/deleted-groups/}.    */
DECL|method|isRefsDeletedGroups (String ref)
specifier|public
specifier|static
name|boolean
name|isRefsDeletedGroups
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_DELETED_GROUPS
argument_list|)
return|;
block|}
comment|/**    * Whether the ref is used for storing group data in NoteDb. Returns {@code true} for all group    * branches, refs/meta/group-names and deleted group branches.    */
DECL|method|isGroupRef (String ref)
specifier|public
specifier|static
name|boolean
name|isGroupRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|isRefsGroups
argument_list|(
name|ref
argument_list|)
operator|||
name|isRefsDeletedGroups
argument_list|(
name|ref
argument_list|)
operator|||
name|REFS_GROUPNAMES
operator|.
name|equals
argument_list|(
name|ref
argument_list|)
return|;
block|}
comment|/** Whether the ref is the configuration branch, i.e. {@code refs/meta/config}, for a project. */
DECL|method|isConfigRef (String ref)
specifier|public
specifier|static
name|boolean
name|isConfigRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|REFS_CONFIG
operator|.
name|equals
argument_list|(
name|ref
argument_list|)
return|;
block|}
comment|/**    * Whether the ref is managed by Gerrit. Covers all Gerrit-internal refs like refs/cache-automerge    * and refs/meta as well as refs/changes. Does not cover user-created refs like branches or custom    * ref namespaces like refs/my-company.    */
DECL|method|isGerritRef (String ref)
specifier|public
specifier|static
name|boolean
name|isGerritRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_CHANGES
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_META
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_CACHE_AUTOMERGE
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_DRAFT_COMMENTS
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_DELETED_GROUPS
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_SEQUENCES
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_GROUPS
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_GROUPNAMES
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_USERS
argument_list|)
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_STARRED_CHANGES
argument_list|)
return|;
block|}
DECL|method|parseShardedRefPart (String name)
specifier|static
name|Integer
name|parseShardedRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|parts
init|=
name|name
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|parts
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|n
operator|<
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Last 2 digits.
name|int
name|le
decl_stmt|;
for|for
control|(
name|le
operator|=
literal|0
init|;
name|le
operator|<
name|parts
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
condition|;
name|le
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|parts
index|[
literal|0
index|]
operator|.
name|charAt
argument_list|(
name|le
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|le
operator|!=
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Full ID.
name|int
name|ie
decl_stmt|;
for|for
control|(
name|ie
operator|=
literal|0
init|;
name|ie
operator|<
name|parts
index|[
literal|1
index|]
operator|.
name|length
argument_list|()
condition|;
name|ie
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|parts
index|[
literal|1
index|]
operator|.
name|charAt
argument_list|(
name|ie
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|ie
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
break|break;
block|}
block|}
name|int
name|shard
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|id
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ie
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|%
literal|100
operator|!=
name|shard
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|id
return|;
block|}
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGINS_ALL
argument_list|)
DECL|method|parseShardedUuidFromRefPart (String name)
specifier|public
specifier|static
name|String
name|parseShardedUuidFromRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|parts
init|=
name|name
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|parts
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// First 2 chars.
if|if
condition|(
name|parts
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
operator|!=
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Full UUID.
name|String
name|uuid
init|=
name|parts
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|uuid
operator|.
name|startsWith
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|uuid
return|;
block|}
comment|/**    * Skips a sharded ref part at the beginning of the name.    *    *<p>E.g.: "01/1" -> "", "01/1/" -> "/", "01/1/2" -> "/2", "01/1-edit" -> "-edit"    *    * @param name ref part name    * @return the rest of the name, {@code null} if the ref name part doesn't start with a valid    *     sharded ID    */
DECL|method|skipShardedRefPart (String name)
specifier|static
name|String
name|skipShardedRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|parts
init|=
name|name
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|parts
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|n
operator|<
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Last 2 digits.
name|int
name|le
decl_stmt|;
for|for
control|(
name|le
operator|=
literal|0
init|;
name|le
operator|<
name|parts
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
condition|;
name|le
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|parts
index|[
literal|0
index|]
operator|.
name|charAt
argument_list|(
name|le
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|le
operator|!=
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Full ID.
name|int
name|ie
decl_stmt|;
for|for
control|(
name|ie
operator|=
literal|0
init|;
name|ie
operator|<
name|parts
index|[
literal|1
index|]
operator|.
name|length
argument_list|()
condition|;
name|ie
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|parts
index|[
literal|1
index|]
operator|.
name|charAt
argument_list|(
name|ie
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|ie
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
break|break;
block|}
block|}
name|int
name|shard
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|id
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ie
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|%
literal|100
operator|!=
name|shard
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|name
operator|.
name|substring
argument_list|(
literal|2
operator|+
literal|1
operator|+
name|ie
argument_list|)
return|;
comment|// 2 for the length of the shard, 1 for the '/'
block|}
comment|/**    * Parses an ID that follows a sharded ref part at the beginning of the name.    *    *<p>E.g.: "01/1/2" -> 2, "01/1/2/4" -> 2, ""01/1/2-edit" -> 2    *    * @param name ref part name    * @return ID that follows the sharded ref part at the beginning of the name, {@code null} if the    *     ref name part doesn't start with a valid sharded ID or if no valid ID follows the sharded    *     ref part    */
DECL|method|parseAfterShardedRefPart (String name)
specifier|static
name|Integer
name|parseAfterShardedRefPart
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|rest
init|=
name|skipShardedRefPart
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|rest
operator|==
literal|null
operator|||
operator|!
name|rest
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|rest
operator|=
name|rest
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|int
name|ie
decl_stmt|;
for|for
control|(
name|ie
operator|=
literal|0
init|;
name|ie
operator|<
name|rest
operator|.
name|length
argument_list|()
condition|;
name|ie
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|rest
operator|.
name|charAt
argument_list|(
name|ie
argument_list|)
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|ie
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|rest
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ie
argument_list|)
argument_list|)
return|;
block|}
DECL|method|parseRefSuffix (String name)
specifier|static
name|Integer
name|parseRefSuffix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|i
init|=
name|name
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|char
name|c
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'/'
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|i
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newStringBuilder ()
specifier|private
specifier|static
name|StringBuilder
name|newStringBuilder
parameter_list|()
block|{
comment|// Many refname types in this file are always are longer than the default of 16 chars, so
comment|// presize StringBuilders larger by default. This hurts readability less than accurate
comment|// calculations would, at a negligible cost to memory overhead.
return|return
operator|new
name|StringBuilder
argument_list|(
literal|64
argument_list|)
return|;
block|}
DECL|method|RefNames ()
specifier|private
name|RefNames
parameter_list|()
block|{}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
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
name|audit
operator|.
name|AuditService
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
name|common
operator|.
name|errors
operator|.
name|NameAlreadyUsedException
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
name|errors
operator|.
name|NoSuchGroupException
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
name|AccountGroup
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
name|AccountGroupById
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
name|AccountGroupMember
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
name|AccountGroupName
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
name|IdentifiedUser
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
name|account
operator|.
name|AccountCache
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
name|account
operator|.
name|GroupCache
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
name|account
operator|.
name|GroupIncludeCache
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
name|RenameGroupOp
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
name|assistedinject
operator|.
name|Assisted
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
name|HashSet
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
name|Future
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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

begin_comment
comment|/**  * A database accessor for write calls related to groups.  *  *<p>All calls which write group related details to the database (either ReviewDb or NoteDb) are  * gathered here. Other classes should always use this class instead of accessing the database  * directly. There are a few exceptions though: schema classes, wrapper classes, and classes  * executed during init. The latter ones should use {@code GroupsOnInit} instead.  *  *<p>If not explicitly stated, all methods of this class refer to<em>internal</em> groups.  */
end_comment

begin_class
DECL|class|GroupsUpdate
specifier|public
class|class
name|GroupsUpdate
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (@ullable IdentifiedUser currentUser)
name|GroupsUpdate
name|create
parameter_list|(
annotation|@
name|Nullable
name|IdentifiedUser
name|currentUser
parameter_list|)
function_decl|;
block|}
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupIncludeCache
specifier|private
specifier|final
name|GroupIncludeCache
name|groupIncludeCache
decl_stmt|;
DECL|field|auditService
specifier|private
specifier|final
name|AuditService
name|auditService
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|renameGroupOpFactory
specifier|private
specifier|final
name|RenameGroupOp
operator|.
name|Factory
name|renameGroupOpFactory
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|currentUser
annotation|@
name|Nullable
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
DECL|field|committerIdent
specifier|private
name|PersonIdent
name|committerIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsUpdate ( Groups groups, GroupCache groupCache, GroupIncludeCache groupIncludeCache, AuditService auditService, AccountCache accountCache, RenameGroupOp.Factory renameGroupOpFactory, @GerritPersonIdent PersonIdent serverIdent, @Assisted @Nullable IdentifiedUser currentUser)
name|GroupsUpdate
parameter_list|(
name|Groups
name|groups
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
name|GroupIncludeCache
name|groupIncludeCache
parameter_list|,
name|AuditService
name|auditService
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|RenameGroupOp
operator|.
name|Factory
name|renameGroupOpFactory
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|IdentifiedUser
name|currentUser
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupIncludeCache
operator|=
name|groupIncludeCache
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|renameGroupOpFactory
operator|=
name|renameGroupOpFactory
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|setCurrentUser
argument_list|(
name|currentUser
argument_list|)
expr_stmt|;
block|}
comment|/**    * Uses the identity of the specified user to mark database modifications executed by this {@code    * GroupsUpdate}. For NoteDb, this identity is used as author and committer for all related    * commits.    *    *<p><strong>Note</strong>: Please use this method with care and rather consider to use the    * correct annotation on the provider of this class instead.    *    * @param currentUser the user to which modifications should be attributed, or {@code null} if the    *     Gerrit server identity should be used    */
DECL|method|setCurrentUser (@ullable IdentifiedUser currentUser)
specifier|public
name|void
name|setCurrentUser
parameter_list|(
annotation|@
name|Nullable
name|IdentifiedUser
name|currentUser
parameter_list|)
block|{
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|setCommitterIdent
argument_list|(
name|currentUser
argument_list|)
expr_stmt|;
block|}
DECL|method|setCommitterIdent (@ullable IdentifiedUser currentUser)
specifier|private
name|void
name|setCommitterIdent
parameter_list|(
annotation|@
name|Nullable
name|IdentifiedUser
name|currentUser
parameter_list|)
block|{
if|if
condition|(
name|currentUser
operator|!=
literal|null
condition|)
block|{
name|committerIdent
operator|=
name|createPersonIdent
argument_list|(
name|serverIdent
argument_list|,
name|currentUser
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|committerIdent
operator|=
name|serverIdent
expr_stmt|;
block|}
block|}
DECL|method|createPersonIdent (PersonIdent ident, IdentifiedUser user)
specifier|private
specifier|static
name|PersonIdent
name|createPersonIdent
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
block|{
return|return
name|user
operator|.
name|newCommitterIdent
argument_list|(
name|ident
operator|.
name|getWhen
argument_list|()
argument_list|,
name|ident
operator|.
name|getTimeZone
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Adds/Creates the specified group for the specified members (accounts).    *    * @param db the {@code ReviewDb} instance to update    * @param group the group to add    * @param memberIds the IDs of the accounts which should be members of the created group    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the cache entry of one of the new members couldn't be invalidated, or    *     the new group couldn't be indexed    */
DECL|method|addGroup (ReviewDb db, AccountGroup group, Set<Account.Id> memberIds)
specifier|public
name|void
name|addGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
name|group
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|memberIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|addNewGroup
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|addNewGroupMembers
argument_list|(
name|db
argument_list|,
name|group
argument_list|,
name|memberIds
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|onCreateGroup
argument_list|(
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds the specified group.    *    *<p><strong>Note</strong>: This method doesn't update the index! It just adds the group to the    * database. Use this method with care.    *    * @param db the {@code ReviewDb} instance to update    * @param group the group to add    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    */
DECL|method|addNewGroup (ReviewDb db, AccountGroup group)
specifier|public
specifier|static
name|void
name|addNewGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
name|group
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroupName
name|gn
init|=
operator|new
name|AccountGroupName
argument_list|(
name|group
argument_list|)
decl_stmt|;
comment|// first insert the group name to validate that the group name hasn't
comment|// already been used to create another group
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|gn
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Updates the specified group.    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group to update    * @param groupConsumer a {@code Consumer} which performs the desired updates on the group    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the cache entry for the group couldn't be invalidated    * @throws NoSuchGroupException if the specified group doesn't exist    */
DECL|method|updateGroup ( ReviewDb db, AccountGroup.UUID groupUuid, Consumer<AccountGroup> groupConsumer)
specifier|public
name|void
name|updateGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Consumer
argument_list|<
name|AccountGroup
argument_list|>
name|groupConsumer
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|updatedGroup
init|=
name|updateGroupInDb
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|groupConsumer
argument_list|)
decl_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|updatedGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|updatedGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|updatedGroup
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|updateGroupInDb ( ReviewDb db, AccountGroup.UUID groupUuid, Consumer<AccountGroup> groupConsumer)
specifier|public
name|AccountGroup
name|updateGroupInDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Consumer
argument_list|<
name|AccountGroup
argument_list|>
name|groupConsumer
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|group
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|groupConsumer
operator|.
name|accept
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|group
return|;
block|}
comment|/**    * Renames the specified group.    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group to rename    * @param newName the new name of the group    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the cache entry for the group couldn't be invalidated    * @throws NoSuchGroupException if the specified group doesn't exist    * @throws NameAlreadyUsedException if another group has the name {@code newName}    */
DECL|method|renameGroup (ReviewDb db, AccountGroup.UUID groupUuid, AccountGroup.NameKey newName)
specifier|public
name|void
name|renameGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|AccountGroup
operator|.
name|NameKey
name|newName
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NameAlreadyUsedException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|group
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|NameKey
name|oldName
init|=
name|group
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
try|try
block|{
name|AccountGroupName
name|id
init|=
operator|new
name|AccountGroupName
argument_list|(
name|newName
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|AccountGroupName
name|other
init|=
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|get
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|other
operator|!=
literal|null
condition|)
block|{
comment|// If we are using this identity, don't report the exception.
if|if
condition|(
name|other
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// Otherwise, someone else has this identity.
throw|throw
operator|new
name|NameAlreadyUsedException
argument_list|(
literal|"group with name "
operator|+
name|newName
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
throw|throw
name|e
throw|;
block|}
name|group
operator|.
name|setNameKey
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|oldName
argument_list|)
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|,
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evictAfterRename
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|renameGroupOpFactory
operator|.
name|create
argument_list|(
name|committerIdent
argument_list|,
name|groupUuid
argument_list|,
name|oldName
operator|.
name|get
argument_list|()
argument_list|,
name|newName
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|start
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
block|}
comment|/**    * Adds an account as member to a group. The account is only added as a new member if it isn't    * already a member of the group.    *    *<p><strong>Note</strong>: This method doesn't check whether the account exists!    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group    * @param accountId the ID of the account to add    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the cache entry of the new member couldn't be invalidated    * @throws NoSuchGroupException if the specified group doesn't exist    */
DECL|method|addGroupMember (ReviewDb db, AccountGroup.UUID groupUuid, Account.Id accountId)
specifier|public
name|void
name|addGroupMember
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchGroupException
block|{
name|addGroupMembers
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|accountId
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds several accounts as members to a group. Only accounts which currently aren't members of    * the group are added.    *    *<p><strong>Note</strong>: This method doesn't check whether the accounts exist!    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group    * @param accountIds a set of IDs of accounts to add    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the group or one of the new members couldn't be indexed    * @throws NoSuchGroupException if the specified group doesn't exist    */
DECL|method|addGroupMembers (ReviewDb db, AccountGroup.UUID groupUuid, Set<Account.Id> accountIds)
specifier|public
name|void
name|addGroupMembers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|group
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|newMemberIds
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|accountIds
control|)
block|{
name|boolean
name|isMember
init|=
name|groups
operator|.
name|isMember
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|accountId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isMember
condition|)
block|{
name|newMemberIds
operator|.
name|add
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newMemberIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|addNewGroupMembers
argument_list|(
name|db
argument_list|,
name|group
argument_list|,
name|newMemberIds
argument_list|)
expr_stmt|;
block|}
DECL|method|addNewGroupMembers (ReviewDb db, AccountGroup group, Set<Account.Id> newMemberIds)
specifier|private
name|void
name|addNewGroupMembers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
name|group
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|newMemberIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|Set
argument_list|<
name|AccountGroupMember
argument_list|>
name|newMembers
init|=
name|newMemberIds
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|accountId
lambda|->
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accountId
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|AccountGroupMember
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentUser
operator|!=
literal|null
condition|)
block|{
name|auditService
operator|.
name|dispatchAddAccountsToGroup
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|newMembers
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|newMembers
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|,
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupMember
name|newMember
range|:
name|newMembers
control|)
block|{
name|accountCache
operator|.
name|evict
argument_list|(
name|newMember
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Removes several members (accounts) from a group. Only accounts which currently are members of    * the group are removed.    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group    * @param accountIds a set of IDs of accounts to remove    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the group or one of the removed members couldn't be indexed    * @throws NoSuchGroupException if the specified group doesn't exist    */
DECL|method|removeGroupMembers ( ReviewDb db, AccountGroup.UUID groupUuid, Set<Account.Id> accountIds)
specifier|public
name|void
name|removeGroupMembers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|group
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|group
operator|.
name|getId
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|AccountGroupMember
argument_list|>
name|membersToRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|accountIds
control|)
block|{
name|boolean
name|isMember
init|=
name|groups
operator|.
name|isMember
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|accountId
argument_list|)
decl_stmt|;
if|if
condition|(
name|isMember
condition|)
block|{
name|AccountGroupMember
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accountId
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|membersToRemove
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupMember
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|membersToRemove
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentUser
operator|!=
literal|null
condition|)
block|{
name|auditService
operator|.
name|dispatchDeleteAccountsFromGroup
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|membersToRemove
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|delete
argument_list|(
name|membersToRemove
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|,
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupMember
name|member
range|:
name|membersToRemove
control|)
block|{
name|accountCache
operator|.
name|evict
argument_list|(
name|member
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Adds several groups as subgroups to a group. Only groups which currently aren't subgroups of    * the group are added.    *    *<p>The parent group must be an internal group whereas the subgroups can either be internal or    * external groups.    *    *<p><strong>Note</strong>: This method doesn't check whether the subgroups exist!    *    * @param db the {@code ReviewDb} instance to update    * @param parentGroupUuid the UUID of the parent group    * @param includedGroupUuids a set of IDs of the groups to add as subgroups    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the parent group couldn't be indexed    * @throws NoSuchGroupException if the specified parent group doesn't exist    */
DECL|method|addIncludedGroups ( ReviewDb db, AccountGroup.UUID parentGroupUuid, Set<AccountGroup.UUID> includedGroupUuids)
specifier|public
name|void
name|addIncludedGroups
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|parentGroupUuid
parameter_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|includedGroupUuids
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
throws|,
name|IOException
block|{
name|AccountGroup
name|parentGroup
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|parentGroupUuid
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|parentGroupId
init|=
name|parentGroup
operator|.
name|getId
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|AccountGroupById
argument_list|>
name|newIncludedGroups
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|includedGroupUuid
range|:
name|includedGroupUuids
control|)
block|{
name|boolean
name|isIncluded
init|=
name|groups
operator|.
name|isIncluded
argument_list|(
name|db
argument_list|,
name|parentGroupUuid
argument_list|,
name|includedGroupUuid
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isIncluded
condition|)
block|{
name|AccountGroupById
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupById
operator|.
name|Key
argument_list|(
name|parentGroupId
argument_list|,
name|includedGroupUuid
argument_list|)
decl_stmt|;
name|newIncludedGroups
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupById
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newIncludedGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentUser
operator|!=
literal|null
condition|)
block|{
name|auditService
operator|.
name|dispatchAddGroupsToGroup
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|newIncludedGroups
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupById
argument_list|()
operator|.
name|insert
argument_list|(
name|newIncludedGroups
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|parentGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|parentGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|parentGroup
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupById
name|newIncludedGroup
range|:
name|newIncludedGroups
control|)
block|{
name|groupIncludeCache
operator|.
name|evictParentGroupsOf
argument_list|(
name|newIncludedGroup
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|groupIncludeCache
operator|.
name|evictSubgroupsOf
argument_list|(
name|parentGroupUuid
argument_list|)
expr_stmt|;
block|}
comment|/**    * Removes several subgroups from a parent group. Only groups which currently are subgroups of the    * group are removed.    *    *<p>The parent group must be an internal group whereas the subgroups can either be internal or    * external groups.    *    * @param db the {@code ReviewDb} instance to update    * @param parentGroupUuid the UUID of the parent group    * @param includedGroupUuids a set of IDs of the subgroups to remove from the parent group    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws IOException if the parent group couldn't be indexed    * @throws NoSuchGroupException if the specified parent group doesn't exist    */
DECL|method|deleteIncludedGroups ( ReviewDb db, AccountGroup.UUID parentGroupUuid, Set<AccountGroup.UUID> includedGroupUuids)
specifier|public
name|void
name|deleteIncludedGroups
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|parentGroupUuid
parameter_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|includedGroupUuids
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
throws|,
name|IOException
block|{
name|AccountGroup
name|parentGroup
init|=
name|groups
operator|.
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|parentGroupUuid
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|parentGroupId
init|=
name|parentGroup
operator|.
name|getId
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|AccountGroupById
argument_list|>
name|includedGroupsToRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|includedGroupUuid
range|:
name|includedGroupUuids
control|)
block|{
name|boolean
name|isIncluded
init|=
name|groups
operator|.
name|isIncluded
argument_list|(
name|db
argument_list|,
name|parentGroupUuid
argument_list|,
name|includedGroupUuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|isIncluded
condition|)
block|{
name|AccountGroupById
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupById
operator|.
name|Key
argument_list|(
name|parentGroupId
argument_list|,
name|includedGroupUuid
argument_list|)
decl_stmt|;
name|includedGroupsToRemove
operator|.
name|add
argument_list|(
operator|new
name|AccountGroupById
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includedGroupsToRemove
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentUser
operator|!=
literal|null
condition|)
block|{
name|auditService
operator|.
name|dispatchDeleteGroupsFromGroup
argument_list|(
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|includedGroupsToRemove
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroupById
argument_list|()
operator|.
name|delete
argument_list|(
name|includedGroupsToRemove
argument_list|)
expr_stmt|;
name|groupCache
operator|.
name|evict
argument_list|(
name|parentGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|parentGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|parentGroup
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupById
name|groupToRemove
range|:
name|includedGroupsToRemove
control|)
block|{
name|groupIncludeCache
operator|.
name|evictParentGroupsOf
argument_list|(
name|groupToRemove
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|groupIncludeCache
operator|.
name|evictSubgroupsOf
argument_list|(
name|parentGroupUuid
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


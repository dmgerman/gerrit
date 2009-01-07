begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|client
operator|.
name|OrmException
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
comment|/** Cache of group information, including account memberships. */
end_comment

begin_class
DECL|class|GroupCache
specifier|public
class|class
name|GroupCache
block|{
DECL|field|adminGroupId
specifier|private
name|AccountGroup
operator|.
name|Id
name|adminGroupId
decl_stmt|;
DECL|field|anonymousGroupId
specifier|private
name|AccountGroup
operator|.
name|Id
name|anonymousGroupId
decl_stmt|;
DECL|field|registeredGroupId
specifier|private
name|AccountGroup
operator|.
name|Id
name|registeredGroupId
decl_stmt|;
DECL|field|byAccount
specifier|private
specifier|final
name|LinkedHashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|>
name|byAccount
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|(
literal|16
argument_list|,
literal|0.75f
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|removeEldestEntry
parameter_list|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|>
name|eldest
parameter_list|)
block|{
return|return
literal|4096
operator|<=
name|size
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|GroupCache (final SystemConfig cfg)
specifier|public
name|GroupCache
parameter_list|(
specifier|final
name|SystemConfig
name|cfg
parameter_list|)
block|{
name|adminGroupId
operator|=
name|cfg
operator|.
name|adminGroupId
expr_stmt|;
name|anonymousGroupId
operator|=
name|cfg
operator|.
name|anonymousGroupId
expr_stmt|;
name|registeredGroupId
operator|=
name|cfg
operator|.
name|registeredGroupId
expr_stmt|;
block|}
comment|/**    * Is this group membership managed automatically by Gerrit?    *     * @param groupId the group to test.    * @return true if Gerrit handles this group membership automatically; false    *         if it can be manually managed.    */
DECL|method|isAutoGroup (final AccountGroup.Id groupId)
specifier|public
name|boolean
name|isAutoGroup
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
return|return
name|isAnonymousUsers
argument_list|(
name|groupId
argument_list|)
operator|||
name|isRegisteredUsers
argument_list|(
name|groupId
argument_list|)
return|;
block|}
comment|/**    * Does this group designate the magical 'Anonymous Users' group?    *     * @param groupId the group to test.    * @return true if this is the magical 'Anonymous' group; false otherwise.    */
DECL|method|isAnonymousUsers (final AccountGroup.Id groupId)
specifier|public
name|boolean
name|isAnonymousUsers
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
return|return
name|anonymousGroupId
operator|.
name|equals
argument_list|(
name|groupId
argument_list|)
return|;
block|}
comment|/**    * Does this group designate the magical 'Registered Users' group?    *     * @param groupId the group to test.    * @return true if this is the magical 'Registered' group; false otherwise.    */
DECL|method|isRegisteredUsers (final AccountGroup.Id groupId)
specifier|public
name|boolean
name|isRegisteredUsers
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
return|return
name|registeredGroupId
operator|.
name|equals
argument_list|(
name|groupId
argument_list|)
return|;
block|}
comment|/**    * Determine if the user is a member of the blessed administrator group.    *     * @param accountId the account to test for membership.    * @return true if the account is in the special blessed administration group;    *         false otherwise.    */
DECL|method|isAdministrator (final Account.Id accountId)
specifier|public
name|boolean
name|isAdministrator
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|isInGroup
argument_list|(
name|accountId
argument_list|,
name|adminGroupId
argument_list|)
return|;
block|}
comment|/**    * Determine if the user is a member of a specific group.    *     * @param accountId the account to test for membership.    * @param groupId the group to test for membership within.    * @return true if the account is in the group; false otherwise.    */
DECL|method|isInGroup (final Account.Id accountId, final AccountGroup.Id groupId)
specifier|public
name|boolean
name|isInGroup
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
if|if
condition|(
name|isAnonymousUsers
argument_list|(
name|groupId
argument_list|)
operator|||
name|isRegisteredUsers
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|getGroups
argument_list|(
name|accountId
argument_list|)
operator|.
name|contains
argument_list|(
name|groupId
argument_list|)
return|;
block|}
comment|/**    * Invalidate all cached information about a single user account.    *     * @param accountId the account to invalidate from the cache.    */
DECL|method|invalidate (final Account.Id accountId)
specifier|public
name|void
name|invalidate
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
name|byAccount
operator|.
name|remove
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Notify the cache that an account has become a member of a group.    *     * @param m the account-group pairing that was just inserted.    */
DECL|method|notifyGroupAdd (final AccountGroupMember m)
specifier|public
name|void
name|notifyGroupAdd
parameter_list|(
specifier|final
name|AccountGroupMember
name|m
parameter_list|)
block|{
if|if
condition|(
name|isAutoGroup
argument_list|(
name|m
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|e
init|=
name|byAccount
operator|.
name|get
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|n
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|n
operator|.
name|add
argument_list|(
name|m
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|byAccount
operator|.
name|put
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Notify the cache that an account has been removed from a group.    *     * @param m the account-group pairing that was just deleted.    */
DECL|method|notifyGroupDelete (final AccountGroupMember m)
specifier|public
name|void
name|notifyGroupDelete
parameter_list|(
specifier|final
name|AccountGroupMember
name|m
parameter_list|)
block|{
if|if
condition|(
name|isAutoGroup
argument_list|(
name|m
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|e
init|=
name|byAccount
operator|.
name|get
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|n
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|n
operator|.
name|remove
argument_list|(
name|m
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|byAccount
operator|.
name|put
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Get the groups a specific account is a member of.    *     * @param accountId the account to obtain the group list for.    * @return unmodifiable set listing the groups the account is a member of.    */
DECL|method|getGroups (final Account.Id accountId)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getGroups
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
if|if
condition|(
name|accountId
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|m
decl_stmt|;
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
name|m
operator|=
name|byAccount
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
name|m
return|;
block|}
name|m
operator|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|Common
operator|.
name|getSchemaFactory
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|AccountGroupMember
name|g
range|:
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byAccount
argument_list|(
name|accountId
argument_list|)
control|)
block|{
name|m
operator|.
name|add
argument_list|(
name|g
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|m
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|add
argument_list|(
name|anonymousGroupId
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
name|registeredGroupId
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
name|byAccount
operator|.
name|put
argument_list|(
name|accountId
argument_list|,
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|m
return|;
block|}
comment|/** Force the entire group cache to flush from memory and recompute. */
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{
synchronized|synchronized
init|(
name|byAccount
init|)
block|{
name|byAccount
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


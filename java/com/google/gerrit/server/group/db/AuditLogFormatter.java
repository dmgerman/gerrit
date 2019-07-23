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
DECL|package|com.google.gerrit.server.group.db
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
operator|.
name|db
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
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|data
operator|.
name|GroupDescription
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
name|AccountState
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
name|GroupBackend
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
name|function
operator|.
name|Function
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
comment|/**  * A formatter for entities used in an audit log which is typically represented by NoteDb commits.  *  *<p>The formatted representation of those entities must be parsable so that we can read them later  * on and map them back to their original entities. {@link AuditLogFormatter} and {@link  * com.google.gerrit.server.notedb.NoteDbUtil NoteDbUtil} contain some of those parsing/mapping  * methods.  */
end_comment

begin_class
DECL|class|AuditLogFormatter
specifier|public
class|class
name|AuditLogFormatter
block|{
DECL|field|accountRetriever
specifier|private
specifier|final
name|Function
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|Account
argument_list|>
argument_list|>
name|accountRetriever
decl_stmt|;
DECL|field|groupRetriever
specifier|private
specifier|final
name|Function
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
argument_list|>
name|groupRetriever
decl_stmt|;
DECL|field|serverId
annotation|@
name|Nullable
specifier|private
specifier|final
name|String
name|serverId
decl_stmt|;
DECL|method|createBackedBy ( AccountCache accountCache, GroupBackend groupBackend, String serverId)
specifier|public
specifier|static
name|AuditLogFormatter
name|createBackedBy
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|String
name|serverId
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|accountId
lambda|->
name|getAccount
argument_list|(
name|accountCache
argument_list|,
name|accountId
argument_list|)
argument_list|,
name|groupUuid
lambda|->
name|getGroup
argument_list|(
name|groupBackend
argument_list|,
name|groupUuid
argument_list|)
argument_list|,
name|serverId
argument_list|)
return|;
block|}
DECL|method|getAccount (AccountCache accountCache, Account.Id accountId)
specifier|private
specifier|static
name|Optional
argument_list|<
name|Account
argument_list|>
name|getAccount
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|accountCache
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
operator|.
name|map
argument_list|(
name|AccountState
operator|::
name|getAccount
argument_list|)
return|;
block|}
DECL|method|getGroup ( GroupBackend groupBackend, AccountGroup.UUID groupUuid)
specifier|private
specifier|static
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|getGroup
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|groupBackend
operator|.
name|get
argument_list|(
name|groupUuid
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createBackedBy ( ImmutableSet<Account> allAccounts, ImmutableSet<GroupDescription.Basic> allGroups, String serverId)
specifier|public
specifier|static
name|AuditLogFormatter
name|createBackedBy
parameter_list|(
name|ImmutableSet
argument_list|<
name|Account
argument_list|>
name|allAccounts
parameter_list|,
name|ImmutableSet
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|allGroups
parameter_list|,
name|String
name|serverId
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|id
lambda|->
name|getAccount
argument_list|(
name|allAccounts
argument_list|,
name|id
argument_list|)
argument_list|,
name|uuid
lambda|->
name|getGroup
argument_list|(
name|allGroups
argument_list|,
name|uuid
argument_list|)
argument_list|,
name|serverId
argument_list|)
return|;
block|}
DECL|method|getGroup ( ImmutableSet<GroupDescription.Basic> groups, AccountGroup.UUID uuid)
specifier|private
specifier|static
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|getGroup
parameter_list|(
name|ImmutableSet
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|groups
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|groups
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|group
lambda|->
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|equals
argument_list|(
name|uuid
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
return|;
block|}
DECL|method|getAccount (ImmutableSet<Account> accounts, Account.Id id)
specifier|private
specifier|static
name|Optional
argument_list|<
name|Account
argument_list|>
name|getAccount
parameter_list|(
name|ImmutableSet
argument_list|<
name|Account
argument_list|>
name|accounts
parameter_list|,
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|accounts
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|account
lambda|->
name|account
operator|.
name|id
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
return|;
block|}
DECL|method|createPartiallyWorkingFallBack ()
specifier|public
specifier|static
name|AuditLogFormatter
name|createPartiallyWorkingFallBack
parameter_list|()
block|{
return|return
operator|new
name|AuditLogFormatter
argument_list|(
name|id
lambda|->
name|Optional
operator|.
name|empty
argument_list|()
argument_list|,
name|uuid
lambda|->
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
return|;
block|}
DECL|method|create ( Function<Account.Id, Optional<Account>> accountRetriever, Function<AccountGroup.UUID, Optional<GroupDescription.Basic>> groupRetriever, String serverId)
specifier|public
specifier|static
name|AuditLogFormatter
name|create
parameter_list|(
name|Function
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|Account
argument_list|>
argument_list|>
name|accountRetriever
parameter_list|,
name|Function
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
argument_list|>
name|groupRetriever
parameter_list|,
name|String
name|serverId
parameter_list|)
block|{
return|return
operator|new
name|AuditLogFormatter
argument_list|(
name|accountRetriever
argument_list|,
name|groupRetriever
argument_list|,
name|serverId
argument_list|)
return|;
block|}
DECL|method|AuditLogFormatter ( Function<Account.Id, Optional<Account>> accountRetriever, Function<AccountGroup.UUID, Optional<GroupDescription.Basic>> groupRetriever, String serverId)
specifier|private
name|AuditLogFormatter
parameter_list|(
name|Function
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|Account
argument_list|>
argument_list|>
name|accountRetriever
parameter_list|,
name|Function
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
argument_list|>
name|groupRetriever
parameter_list|,
name|String
name|serverId
parameter_list|)
block|{
name|this
operator|.
name|accountRetriever
operator|=
name|requireNonNull
argument_list|(
name|accountRetriever
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupRetriever
operator|=
name|requireNonNull
argument_list|(
name|groupRetriever
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverId
operator|=
name|requireNonNull
argument_list|(
name|serverId
argument_list|)
expr_stmt|;
block|}
DECL|method|AuditLogFormatter ( Function<Account.Id, Optional<Account>> accountRetriever, Function<AccountGroup.UUID, Optional<GroupDescription.Basic>> groupRetriever)
specifier|private
name|AuditLogFormatter
parameter_list|(
name|Function
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|Account
argument_list|>
argument_list|>
name|accountRetriever
parameter_list|,
name|Function
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
argument_list|>
name|groupRetriever
parameter_list|)
block|{
name|this
operator|.
name|accountRetriever
operator|=
name|requireNonNull
argument_list|(
name|accountRetriever
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupRetriever
operator|=
name|requireNonNull
argument_list|(
name|groupRetriever
argument_list|)
expr_stmt|;
name|serverId
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * Creates a parsable {@code PersonIdent} for commits which are used as an audit log.    *    *<p><em>Parsable</em> means that we can unambiguously identify the original account when being    * presented with a {@code PersonIdent} of a commit.    *    *<p>We typically use the initiator of an action as the author of the commit when using those    * commits as an audit log. That's something which has to be specified by a caller of this method    * as this class doesn't create any commits itself.    *    * @param account the {@code Account} of the user who should be represented    * @param personIdent a {@code PersonIdent} which provides the timestamp for the created {@code    *     PersonIdent}    * @return a {@code PersonIdent} which can be used for the author of a commit    */
DECL|method|getParsableAuthorIdent (Account account, PersonIdent personIdent)
specifier|public
name|PersonIdent
name|getParsableAuthorIdent
parameter_list|(
name|Account
name|account
parameter_list|,
name|PersonIdent
name|personIdent
parameter_list|)
block|{
return|return
name|getParsableAuthorIdent
argument_list|(
name|account
operator|.
name|getName
argument_list|()
argument_list|,
name|account
operator|.
name|id
argument_list|()
argument_list|,
name|personIdent
argument_list|)
return|;
block|}
comment|/**    * Creates a parsable {@code PersonIdent} for commits which are used as an audit log.    *    *<p>See {@link #getParsableAuthorIdent(Account, PersonIdent)} for further details.    *    * @param accountId the ID of the account of the user who should be represented    * @param personIdent a {@code PersonIdent} which provides the timestamp for the created {@code    *     PersonIdent}    * @return a {@code PersonIdent} which can be used for the author of a commit    */
DECL|method|getParsableAuthorIdent (Account.Id accountId, PersonIdent personIdent)
specifier|public
name|PersonIdent
name|getParsableAuthorIdent
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|PersonIdent
name|personIdent
parameter_list|)
block|{
name|String
name|accountName
init|=
name|getAccountName
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
return|return
name|getParsableAuthorIdent
argument_list|(
name|accountName
argument_list|,
name|accountId
argument_list|,
name|personIdent
argument_list|)
return|;
block|}
comment|/**    * Provides a parsable representation of an account for use in e.g. commit messages.    *    * @param accountId the ID of the account of the user who should be represented    * @return the {@code String} representation of the account    */
DECL|method|getParsableAccount (Account.Id accountId)
specifier|public
name|String
name|getParsableAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|String
name|accountName
init|=
name|getAccountName
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
return|return
name|formatNameEmail
argument_list|(
name|accountName
argument_list|,
name|getEmailForAuditLog
argument_list|(
name|accountId
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Provides a parsable representation of a group for use in e.g. commit messages.    *    * @param groupUuid the UUID of the group    * @return the {@code String} representation of the group    */
DECL|method|getParsableGroup (AccountGroup.UUID groupUuid)
specifier|public
name|String
name|getParsableGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
name|String
name|uuid
init|=
name|groupUuid
operator|.
name|get
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|group
init|=
name|groupRetriever
operator|.
name|apply
argument_list|(
name|groupUuid
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|group
operator|.
name|map
argument_list|(
name|GroupDescription
operator|.
name|Basic
operator|::
name|getName
argument_list|)
operator|.
name|orElse
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
return|return
name|formatNameEmail
argument_list|(
name|name
argument_list|,
name|uuid
argument_list|)
return|;
block|}
DECL|method|getAccountName (Account.Id accountId)
specifier|private
name|String
name|getAccountName
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
name|Optional
argument_list|<
name|Account
argument_list|>
name|account
init|=
name|accountRetriever
operator|.
name|apply
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
return|return
name|account
operator|.
name|map
argument_list|(
name|Account
operator|::
name|getName
argument_list|)
comment|// Historically, the database did not enforce relational integrity, so it is
comment|// possible for groups to have non-existing members.
operator|.
name|orElse
argument_list|(
literal|"No Account for Id #"
operator|+
name|accountId
argument_list|)
return|;
block|}
DECL|method|getParsableAuthorIdent ( String accountname, Account.Id accountId, PersonIdent personIdent)
specifier|private
name|PersonIdent
name|getParsableAuthorIdent
parameter_list|(
name|String
name|accountname
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|PersonIdent
name|personIdent
parameter_list|)
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|accountname
argument_list|,
name|getEmailForAuditLog
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|personIdent
operator|.
name|getWhen
argument_list|()
argument_list|,
name|personIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getEmailForAuditLog (Account.Id accountId)
specifier|private
name|String
name|getEmailForAuditLog
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
comment|// If we ever switch to UUIDs for accounts, consider to remove the serverId and to use a similar
comment|// approach as for group UUIDs.
name|checkState
argument_list|(
name|serverId
operator|!=
literal|null
argument_list|,
literal|"serverId must be defined; fall-back AuditLogFormatter isn't sufficient"
argument_list|)
expr_stmt|;
return|return
name|accountId
operator|.
name|get
argument_list|()
operator|+
literal|"@"
operator|+
name|serverId
return|;
block|}
DECL|method|formatNameEmail (String name, String email)
specifier|private
specifier|static
name|String
name|formatNameEmail
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|email
parameter_list|)
block|{
name|StringBuilder
name|formattedResult
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|PersonIdent
operator|.
name|appendSanitized
argument_list|(
name|formattedResult
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|formattedResult
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|PersonIdent
operator|.
name|appendSanitized
argument_list|(
name|formattedResult
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|formattedResult
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|formattedResult
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


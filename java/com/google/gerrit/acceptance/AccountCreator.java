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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|server
operator|.
name|Sequences
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
name|ServerInitiated
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
name|AccountsUpdate
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
name|externalids
operator|.
name|ExternalId
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
name|group
operator|.
name|InternalGroup
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
name|group
operator|.
name|db
operator|.
name|GroupsUpdate
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
name|group
operator|.
name|db
operator|.
name|InternalGroupUpdate
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_class
annotation|@
name|Singleton
DECL|class|AccountCreator
specifier|public
class|class
name|AccountCreator
block|{
DECL|field|accounts
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TestAccount
argument_list|>
name|accounts
decl_stmt|;
DECL|field|sequences
specifier|private
specifier|final
name|Sequences
name|sequences
decl_stmt|;
DECL|field|accountsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountCreator ( Sequences sequences, @ServerInitiated Provider<AccountsUpdate> accountsUpdateProvider, GroupCache groupCache, @ServerInitiated Provider<GroupsUpdate> groupsUpdateProvider)
name|AccountCreator
parameter_list|(
name|Sequences
name|sequences
parameter_list|,
annotation|@
name|ServerInitiated
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
annotation|@
name|ServerInitiated
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
parameter_list|)
block|{
name|accounts
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|sequences
operator|=
name|sequences
expr_stmt|;
name|this
operator|.
name|accountsUpdateProvider
operator|=
name|accountsUpdateProvider
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupsUpdateProvider
operator|=
name|groupsUpdateProvider
expr_stmt|;
block|}
DECL|method|create ( @ullable String username, @Nullable String email, @Nullable String fullName, String... groupNames)
specifier|public
specifier|synchronized
name|TestAccount
name|create
parameter_list|(
annotation|@
name|Nullable
name|String
name|username
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|,
annotation|@
name|Nullable
name|String
name|fullName
parameter_list|,
name|String
modifier|...
name|groupNames
parameter_list|)
throws|throws
name|Exception
block|{
name|TestAccount
name|account
init|=
name|accounts
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
return|return
name|account
return|;
block|}
name|Account
operator|.
name|Id
name|id
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|sequences
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ExternalId
argument_list|>
name|extIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|httpPass
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|httpPass
operator|=
literal|"http-pass"
expr_stmt|;
name|extIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|createUsername
argument_list|(
name|username
argument_list|,
name|id
argument_list|,
name|httpPass
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|email
operator|!=
literal|null
condition|)
block|{
name|extIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|createEmail
argument_list|(
name|id
argument_list|,
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|insert
argument_list|(
literal|"Create Test Account"
argument_list|,
name|id
argument_list|,
name|u
lambda|->
name|u
operator|.
name|setFullName
argument_list|(
name|fullName
argument_list|)
operator|.
name|setPreferredEmail
argument_list|(
name|email
argument_list|)
operator|.
name|addExternalIds
argument_list|(
name|extIds
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupNames
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|n
range|:
name|groupNames
control|)
block|{
name|AccountGroup
operator|.
name|NameKey
name|k
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|group
operator|.
name|isPresent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|n
argument_list|)
throw|;
block|}
name|addGroupMember
argument_list|(
name|group
operator|.
name|get
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
name|account
operator|=
operator|new
name|TestAccount
argument_list|(
name|id
argument_list|,
name|username
argument_list|,
name|email
argument_list|,
name|fullName
argument_list|,
name|httpPass
argument_list|)
expr_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|accounts
operator|.
name|put
argument_list|(
name|username
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
return|return
name|account
return|;
block|}
DECL|method|create (@ullable String username, String group)
specifier|public
name|TestAccount
name|create
parameter_list|(
annotation|@
name|Nullable
name|String
name|username
parameter_list|,
name|String
name|group
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
name|username
argument_list|,
literal|null
argument_list|,
name|username
argument_list|,
name|group
argument_list|)
return|;
block|}
DECL|method|create ()
specifier|public
name|TestAccount
name|create
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|create (@ullable String username)
specifier|public
name|TestAccount
name|create
parameter_list|(
annotation|@
name|Nullable
name|String
name|username
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
name|username
argument_list|,
literal|null
argument_list|,
name|username
argument_list|,
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
return|;
block|}
DECL|method|admin ()
specifier|public
name|TestAccount
name|admin
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"admin@example.com"
argument_list|,
literal|"Administrator"
argument_list|,
literal|"Administrators"
argument_list|)
return|;
block|}
DECL|method|admin2 ()
specifier|public
name|TestAccount
name|admin2
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
literal|"admin2"
argument_list|,
literal|"admin2@example.com"
argument_list|,
literal|"Administrator2"
argument_list|,
literal|"Administrators"
argument_list|)
return|;
block|}
DECL|method|user ()
specifier|public
name|TestAccount
name|user
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"User"
argument_list|)
return|;
block|}
DECL|method|user2 ()
specifier|public
name|TestAccount
name|user2
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|create
argument_list|(
literal|"user2"
argument_list|,
literal|"user2@example.com"
argument_list|,
literal|"User2"
argument_list|)
return|;
block|}
DECL|method|get (String username)
specifier|public
name|TestAccount
name|get
parameter_list|(
name|String
name|username
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|accounts
operator|.
name|get
argument_list|(
name|username
argument_list|)
argument_list|,
literal|"No TestAccount created for %s"
argument_list|,
name|username
argument_list|)
return|;
block|}
DECL|method|evict (Collection<Account.Id> ids)
specifier|public
name|void
name|evict
parameter_list|(
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|)
block|{
name|accounts
operator|.
name|values
argument_list|()
operator|.
name|removeIf
argument_list|(
name|a
lambda|->
name|ids
operator|.
name|contains
argument_list|(
name|a
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getAll ()
specifier|public
name|ImmutableList
argument_list|<
name|TestAccount
argument_list|>
name|getAll
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|accounts
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addGroupMember (AccountGroup.UUID groupUuid, Account.Id accountId)
specifier|private
name|void
name|addGroupMember
parameter_list|(
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
throws|,
name|ConfigInvalidException
block|{
name|InternalGroupUpdate
name|groupUpdate
init|=
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
operator|.
name|setMemberModification
argument_list|(
name|memberIds
lambda|->
name|Sets
operator|.
name|union
argument_list|(
name|memberIds
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|accountId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|groupsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|updateGroup
argument_list|(
name|groupUuid
argument_list|,
name|groupUpdate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|config
operator|.
name|AllUsersName
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmDuplicateKeyException
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
name|sql
operator|.
name|Timestamp
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
name|CommitBuilder
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
name|Constants
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
name|ObjectInserter
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
name|RefUpdate
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
name|RefUpdate
operator|.
name|Result
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

begin_comment
comment|/** Updates accounts. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AccountsUpdate
specifier|public
class|class
name|AccountsUpdate
block|{
comment|/**    * Factory to create an AccountsUpdate instance for updating accounts by the Gerrit server.    *    *<p>The Gerrit server identity will be used as author and committer for all commits that update    * the accounts.    */
annotation|@
name|Singleton
DECL|class|Server
specifier|public
specifier|static
class|class
name|Server
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|Server ( GitRepositoryManager repoManager, AllUsersName allUsersName, @GerritPersonIdent Provider<PersonIdent> serverIdent)
specifier|public
name|Server
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
name|AccountsUpdate
name|create
parameter_list|()
block|{
name|PersonIdent
name|i
init|=
name|serverIdent
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
operator|new
name|AccountsUpdate
argument_list|(
name|repoManager
argument_list|,
name|allUsersName
argument_list|,
name|i
argument_list|,
name|i
argument_list|)
return|;
block|}
block|}
comment|/**    * Factory to create an AccountsUpdate instance for updating accounts by the current user.    *    *<p>The identity of the current user will be used as author for all commits that update the    * accounts. The Gerrit server identity will be used as committer.    */
annotation|@
name|Singleton
DECL|class|User
specifier|public
specifier|static
class|class
name|User
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|User ( GitRepositoryManager repoManager, AllUsersName allUsersName, @GerritPersonIdent Provider<PersonIdent> serverIdent, Provider<IdentifiedUser> identifiedUser)
specifier|public
name|User
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|identifiedUser
operator|=
name|identifiedUser
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
name|AccountsUpdate
name|create
parameter_list|()
block|{
name|PersonIdent
name|i
init|=
name|serverIdent
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
operator|new
name|AccountsUpdate
argument_list|(
name|repoManager
argument_list|,
name|allUsersName
argument_list|,
name|createPersonIdent
argument_list|(
name|i
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|i
argument_list|)
return|;
block|}
DECL|method|createPersonIdent (PersonIdent ident, IdentifiedUser user)
specifier|private
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
block|}
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|committerIdent
specifier|private
specifier|final
name|PersonIdent
name|committerIdent
decl_stmt|;
DECL|field|authorIdent
specifier|private
specifier|final
name|PersonIdent
name|authorIdent
decl_stmt|;
DECL|method|AccountsUpdate ( GitRepositoryManager repoManager, AllUsersName allUsersName, PersonIdent committerIdent, PersonIdent authorIdent)
specifier|private
name|AccountsUpdate
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
name|PersonIdent
name|committerIdent
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|checkNotNull
argument_list|(
name|repoManager
argument_list|,
literal|"repoManager"
argument_list|)
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|checkNotNull
argument_list|(
name|allUsersName
argument_list|,
literal|"allUsersName"
argument_list|)
expr_stmt|;
name|this
operator|.
name|committerIdent
operator|=
name|checkNotNull
argument_list|(
name|committerIdent
argument_list|,
literal|"committerIdent"
argument_list|)
expr_stmt|;
name|this
operator|.
name|authorIdent
operator|=
name|checkNotNull
argument_list|(
name|authorIdent
argument_list|,
literal|"authorIdent"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Inserts a new account.    *    * @throws OrmDuplicateKeyException if the account already exists    * @throws IOException if updating the user branch fails    */
DECL|method|insert (ReviewDb db, Account account)
specifier|public
name|void
name|insert
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|insert
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|createUserBranch
argument_list|(
name|account
argument_list|)
expr_stmt|;
block|}
comment|/**    * Inserts or updates an account.    *    *<p>If the account already exists, it is overwritten, otherwise it is inserted.    */
DECL|method|upsert (ReviewDb db, Account account)
specifier|public
name|void
name|upsert
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|upsert
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|createUserBranchIfNeeded
argument_list|(
name|account
argument_list|)
expr_stmt|;
block|}
comment|/** Deletes the account. */
DECL|method|delete (ReviewDb db, Account account)
specifier|public
name|void
name|delete
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|delete
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|deleteUserBranch
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Deletes the account. */
DECL|method|deleteByKey (ReviewDb db, Account.Id accountId)
specifier|public
name|void
name|deleteByKey
parameter_list|(
name|ReviewDb
name|db
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
block|{
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|accountId
argument_list|)
argument_list|)
expr_stmt|;
name|deleteUserBranch
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
DECL|method|createUserBranch (Account account)
specifier|private
name|void
name|createUserBranch
parameter_list|(
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
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
name|allUsersName
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|String
name|refName
init|=
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"User branch %s for newly created account %s already exists."
argument_list|,
name|refName
argument_list|,
name|account
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|createUserBranch
argument_list|(
name|repo
argument_list|,
name|oi
argument_list|,
name|committerIdent
argument_list|,
name|authorIdent
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createUserBranchIfNeeded (Account account)
specifier|private
name|void
name|createUserBranchIfNeeded
parameter_list|(
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
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
name|allUsersName
argument_list|)
init|;
name|ObjectInserter
name|oi
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
if|if
condition|(
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|==
literal|null
condition|)
block|{
name|createUserBranch
argument_list|(
name|repo
argument_list|,
name|oi
argument_list|,
name|committerIdent
argument_list|,
name|authorIdent
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createUserBranch ( Repository repo, ObjectInserter oi, PersonIdent committerIdent, PersonIdent authorIdent, Account account)
specifier|public
specifier|static
name|void
name|createUserBranch
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ObjectInserter
name|oi
parameter_list|,
name|PersonIdent
name|committerIdent
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectId
name|id
init|=
name|createInitialEmptyCommit
argument_list|(
name|oi
argument_list|,
name|committerIdent
argument_list|,
name|authorIdent
argument_list|,
name|account
operator|.
name|getRegisteredOn
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|refName
init|=
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogIdent
argument_list|(
name|committerIdent
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogMessage
argument_list|(
literal|"Create Account"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Result
name|result
init|=
name|ru
operator|.
name|update
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|Result
operator|.
name|NEW
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to update ref %s: %s"
argument_list|,
name|refName
argument_list|,
name|result
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|createInitialEmptyCommit ( ObjectInserter oi, PersonIdent committerIdent, PersonIdent authorIdent, Timestamp registrationDate)
specifier|private
specifier|static
name|ObjectId
name|createInitialEmptyCommit
parameter_list|(
name|ObjectInserter
name|oi
parameter_list|,
name|PersonIdent
name|committerIdent
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|,
name|Timestamp
name|registrationDate
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|emptyTree
argument_list|(
name|oi
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|committerIdent
argument_list|,
name|registrationDate
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|authorIdent
argument_list|,
name|registrationDate
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
literal|"Create Account"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|oi
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|oi
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|id
return|;
block|}
DECL|method|emptyTree (ObjectInserter oi)
specifier|private
specifier|static
name|ObjectId
name|emptyTree
parameter_list|(
name|ObjectInserter
name|oi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|oi
operator|.
name|insert
argument_list|(
name|Constants
operator|.
name|OBJ_TREE
argument_list|,
operator|new
name|byte
index|[]
block|{}
argument_list|)
return|;
block|}
DECL|method|deleteUserBranch (Account.Id accountId)
specifier|private
name|void
name|deleteUserBranch
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
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
name|allUsersName
argument_list|)
init|)
block|{
name|deleteUserBranch
argument_list|(
name|repo
argument_list|,
name|committerIdent
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deleteUserBranch ( Repository repo, PersonIdent refLogIdent, Account.Id accountId)
specifier|public
specifier|static
name|void
name|deleteUserBranch
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|PersonIdent
name|refLogIdent
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|refName
init|=
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|Ref
name|ref
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogIdent
argument_list|(
name|refLogIdent
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogMessage
argument_list|(
literal|"Delete Account"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Result
name|result
init|=
name|ru
operator|.
name|delete
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|Result
operator|.
name|FORCED
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to delete ref %s: %s"
argument_list|,
name|refName
argument_list|,
name|result
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


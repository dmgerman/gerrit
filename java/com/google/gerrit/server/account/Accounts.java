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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|toSet
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
name|extensions
operator|.
name|client
operator|.
name|GeneralPreferencesInfo
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
name|server
operator|.
name|account
operator|.
name|WatchConfig
operator|.
name|NotifyType
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
name|WatchConfig
operator|.
name|ProjectWatchKey
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
name|Objects
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
name|stream
operator|.
name|Stream
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

begin_comment
comment|/** Class to access accounts. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|Accounts
specifier|public
class|class
name|Accounts
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
name|Accounts
operator|.
name|class
argument_list|)
decl_stmt|;
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
annotation|@
name|Inject
DECL|method|Accounts (GitRepositoryManager repoManager, AllUsersName allUsersName)
name|Accounts
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsersName
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
block|}
annotation|@
name|Nullable
DECL|method|get (Account.Id accountId)
specifier|public
name|Account
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
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
return|return
name|read
argument_list|(
name|repo
argument_list|,
name|accountId
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
DECL|method|get (Collection<Account.Id> accountIds)
specifier|public
name|List
argument_list|<
name|Account
argument_list|>
name|get
parameter_list|(
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|List
argument_list|<
name|Account
argument_list|>
name|accounts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|accountIds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
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
name|read
argument_list|(
name|repo
argument_list|,
name|accountId
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|accounts
operator|::
name|add
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|accounts
return|;
block|}
comment|/**    * Returns all accounts.    *    * @return all accounts    */
DECL|method|all ()
specifier|public
name|List
argument_list|<
name|Account
argument_list|>
name|all
parameter_list|()
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
init|=
name|allIds
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Account
argument_list|>
name|accounts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|accountIds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
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
try|try
block|{
name|read
argument_list|(
name|repo
argument_list|,
name|accountId
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|accounts
operator|::
name|add
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignoring invalid account %s"
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|accounts
return|;
block|}
comment|/**    * Returns all account IDs.    *    * @return all account IDs    */
DECL|method|allIds ()
specifier|public
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|allIds
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|readUserRefs
argument_list|()
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the first n account IDs.    *    * @param n the number of account IDs that should be returned    * @return first n account IDs    */
DECL|method|firstNIds (int n)
specifier|public
name|List
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|firstNIds
parameter_list|(
name|int
name|n
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readUserRefs
argument_list|()
operator|.
name|sorted
argument_list|(
name|comparing
argument_list|(
name|id
lambda|->
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|limit
argument_list|(
name|n
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Checks if any account exists.    *    * @return {@code true} if at least one account exists, otherwise {@code false}    */
DECL|method|hasAnyAccount ()
specifier|public
name|boolean
name|hasAnyAccount
parameter_list|()
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
return|return
name|hasAnyAccount
argument_list|(
name|repo
argument_list|)
return|;
block|}
block|}
DECL|method|hasAnyAccount (Repository repo)
specifier|public
specifier|static
name|boolean
name|hasAnyAccount
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readUserRefs
argument_list|(
name|repo
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|isPresent
argument_list|()
return|;
block|}
DECL|method|getProjectWatches (Account.Id accountId)
specifier|public
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|getProjectWatches
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
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
return|return
operator|new
name|AccountConfig
argument_list|(
name|accountId
argument_list|,
name|repo
argument_list|)
operator|.
name|load
argument_list|()
operator|.
name|getProjectWatches
argument_list|()
return|;
block|}
block|}
DECL|method|getGeneralPreferences (Account.Id accountId)
specifier|public
name|GeneralPreferencesInfo
name|getGeneralPreferences
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
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
return|return
operator|new
name|AccountConfig
argument_list|(
name|accountId
argument_list|,
name|repo
argument_list|)
operator|.
name|load
argument_list|()
operator|.
name|getGeneralPreferences
argument_list|()
return|;
block|}
block|}
DECL|method|readUserRefs ()
specifier|private
name|Stream
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|readUserRefs
parameter_list|()
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
return|return
name|readUserRefs
argument_list|(
name|repo
argument_list|)
return|;
block|}
block|}
DECL|method|read (Repository allUsersRepository, Account.Id accountId)
specifier|private
name|Optional
argument_list|<
name|Account
argument_list|>
name|read
parameter_list|(
name|Repository
name|allUsersRepository
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
operator|new
name|AccountConfig
argument_list|(
name|accountId
argument_list|,
name|allUsersRepository
argument_list|)
operator|.
name|load
argument_list|()
operator|.
name|getLoadedAccount
argument_list|()
return|;
block|}
DECL|method|readUserRefs (Repository repo)
specifier|public
specifier|static
name|Stream
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|readUserRefs
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|RefNames
operator|.
name|REFS_USERS
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|r
lambda|->
name|Account
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
return|;
block|}
block|}
end_class

end_unit


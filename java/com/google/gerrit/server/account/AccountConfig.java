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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|TimeUtil
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
name|account
operator|.
name|externalids
operator|.
name|ExternalIds
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
name|MetaDataUpdate
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
name|ValidationError
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
name|VersionedMetaData
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Config
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
name|Repository
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevSort
import|;
end_import

begin_comment
comment|/**  * âaccount.configâ file in the user branch in the All-Users repository that contains the properties  * of the account.  *  *<p>The 'account.config' file is a git config file that has one 'account' section with the  * properties of the account:  *  *<pre>  *   [account]  *     active = false  *     fullName = John Doe  *     preferredEmail = john.doe@foo.com  *     status = Overloaded with reviews  *</pre>  *  *<p>All keys are optional. This means 'account.config' may not exist on the user branch if no  * properties are set.  *  *<p>Not setting a key and setting a key to an empty string are treated the same way and result in  * a {@code null} value.  *  *<p>If no value for 'active' is specified, by default the account is considered as active.  *  *<p>The commit date of the first commit on the user branch is used as registration date of the  * account. The first commit may be an empty commit (if no properties were set and 'account.config'  * doesn't exist).  */
end_comment

begin_class
DECL|class|AccountConfig
specifier|public
class|class
name|AccountConfig
extends|extends
name|VersionedMetaData
implements|implements
name|ValidationError
operator|.
name|Sink
block|{
DECL|field|ACCOUNT_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|ACCOUNT_CONFIG
init|=
literal|"account.config"
decl_stmt|;
DECL|field|ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|ACCOUNT
init|=
literal|"account"
decl_stmt|;
DECL|field|KEY_ACTIVE
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ACTIVE
init|=
literal|"active"
decl_stmt|;
DECL|field|KEY_FULL_NAME
specifier|public
specifier|static
specifier|final
name|String
name|KEY_FULL_NAME
init|=
literal|"fullName"
decl_stmt|;
DECL|field|KEY_PREFERRED_EMAIL
specifier|public
specifier|static
specifier|final
name|String
name|KEY_PREFERRED_EMAIL
init|=
literal|"preferredEmail"
decl_stmt|;
DECL|field|KEY_STATUS
specifier|public
specifier|static
specifier|final
name|String
name|KEY_STATUS
init|=
literal|"status"
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|loadedAccount
specifier|private
name|Optional
argument_list|<
name|Account
argument_list|>
name|loadedAccount
decl_stmt|;
DECL|field|externalIdsRev
specifier|private
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|externalIdsRev
decl_stmt|;
DECL|field|watchConfig
specifier|private
name|WatchConfig
name|watchConfig
decl_stmt|;
DECL|field|prefConfig
specifier|private
name|PreferencesConfig
name|prefConfig
decl_stmt|;
DECL|field|accountUpdate
specifier|private
name|Optional
argument_list|<
name|InternalAccountUpdate
argument_list|>
name|accountUpdate
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|registeredOn
specifier|private
name|Timestamp
name|registeredOn
decl_stmt|;
DECL|field|eagerParsing
specifier|private
name|boolean
name|eagerParsing
decl_stmt|;
DECL|field|validationErrors
specifier|private
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validationErrors
decl_stmt|;
DECL|method|AccountConfig (Account.Id accountId, Repository allUsersRepo)
specifier|public
name|AccountConfig
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Repository
name|allUsersRepo
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|checkNotNull
argument_list|(
name|accountId
argument_list|,
literal|"accountId"
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|checkNotNull
argument_list|(
name|allUsersRepo
argument_list|,
literal|"allUsersRepo"
argument_list|)
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
comment|/**    * Sets whether all account data should be eagerly parsed.    *    *<p>Eager parsing should only be used if the caller is interested in validation errors for all    * account data (see {@link #getValidationErrors()}.    *    * @param eagerParsing whether all account data should be eagerly parsed    * @return this AccountConfig instance for chaining    */
DECL|method|setEagerParsing (boolean eagerParsing)
specifier|public
name|AccountConfig
name|setEagerParsing
parameter_list|(
name|boolean
name|eagerParsing
parameter_list|)
block|{
name|checkState
argument_list|(
name|loadedAccount
operator|==
literal|null
argument_list|,
literal|"Account %s already loaded"
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|eagerParsing
operator|=
name|eagerParsing
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|load ()
specifier|public
name|AccountConfig
name|load
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|load
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Get the loaded account.    *    * @return the loaded account, {@link Optional#empty()} if load didn't find the account because it    *     doesn't exist    * @throws IllegalStateException if the account was not loaded yet    */
DECL|method|getLoadedAccount ()
specifier|public
name|Optional
argument_list|<
name|Account
argument_list|>
name|getLoadedAccount
parameter_list|()
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
return|return
name|loadedAccount
return|;
block|}
comment|/**    * Returns the revision of the {@code refs/meta/external-ids} branch.    *    *<p>This revision can be used to load the external IDs of the loaded account lazily via {@link    * ExternalIds#byAccount(com.google.gerrit.reviewdb.client.Account.Id, ObjectId)}.    *    * @return revision of the {@code refs/meta/external-ids} branch, {@link Optional#empty()} if no    *     {@code refs/meta/external-ids} branch exists    */
DECL|method|getExternalIdsRev ()
specifier|public
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|getExternalIdsRev
parameter_list|()
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
return|return
name|externalIdsRev
return|;
block|}
comment|/**    * Get the project watches of the loaded account.    *    * @return the project watches of the loaded account    */
DECL|method|getProjectWatches ()
specifier|public
name|ImmutableMap
argument_list|<
name|ProjectWatchKey
argument_list|,
name|ImmutableSet
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|getProjectWatches
parameter_list|()
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
return|return
name|watchConfig
operator|.
name|getProjectWatches
argument_list|()
return|;
block|}
comment|/**    * Get the general preferences of the loaded account.    *    * @return the general preferences of the loaded account    */
DECL|method|getGeneralPreferences ()
specifier|public
name|GeneralPreferencesInfo
name|getGeneralPreferences
parameter_list|()
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
return|return
name|prefConfig
operator|.
name|getGeneralPreferences
argument_list|()
return|;
block|}
comment|/**    * Sets the account. This means the loaded account will be overwritten with the given account.    *    *<p>Changing the registration date of an account is not supported.    *    * @param account account that should be set    * @throws IllegalStateException if the account was not loaded yet    */
DECL|method|setAccount (Account account)
specifier|public
name|AccountConfig
name|setAccount
parameter_list|(
name|Account
name|account
parameter_list|)
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
name|this
operator|.
name|loadedAccount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|account
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountUpdate
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|InternalAccountUpdate
operator|.
name|builder
argument_list|()
operator|.
name|setActive
argument_list|(
name|account
operator|.
name|isActive
argument_list|()
argument_list|)
operator|.
name|setFullName
argument_list|(
name|account
operator|.
name|getFullName
argument_list|()
argument_list|)
operator|.
name|setPreferredEmail
argument_list|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
operator|.
name|setStatus
argument_list|(
name|account
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|registeredOn
operator|=
name|account
operator|.
name|getRegisteredOn
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Creates a new account.    *    * @return the new account    * @throws OrmDuplicateKeyException if the user branch already exists    */
DECL|method|getNewAccount ()
specifier|public
name|Account
name|getNewAccount
parameter_list|()
throws|throws
name|OrmDuplicateKeyException
block|{
return|return
name|getNewAccount
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a new account.    *    * @return the new account    * @throws OrmDuplicateKeyException if the user branch already exists    */
DECL|method|getNewAccount (Timestamp registeredOn)
name|Account
name|getNewAccount
parameter_list|(
name|Timestamp
name|registeredOn
parameter_list|)
throws|throws
name|OrmDuplicateKeyException
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|OrmDuplicateKeyException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"account %s already exists"
argument_list|,
name|accountId
argument_list|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|registeredOn
operator|=
name|registeredOn
expr_stmt|;
name|this
operator|.
name|loadedAccount
operator|=
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|Account
argument_list|(
name|accountId
argument_list|,
name|registeredOn
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|loadedAccount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|setAccountUpdate (InternalAccountUpdate accountUpdate)
specifier|public
name|AccountConfig
name|setAccountUpdate
parameter_list|(
name|InternalAccountUpdate
name|accountUpdate
parameter_list|)
block|{
name|this
operator|.
name|accountUpdate
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|accountUpdate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|)
expr_stmt|;
name|registeredOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|rw
operator|.
name|next
argument_list|()
operator|.
name|getCommitTime
argument_list|()
operator|*
literal|1000L
argument_list|)
expr_stmt|;
name|Config
name|accountConfig
init|=
name|readConfig
argument_list|(
name|ACCOUNT_CONFIG
argument_list|)
decl_stmt|;
name|loadedAccount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|parse
argument_list|(
name|accountConfig
argument_list|,
name|revision
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|watchConfig
operator|=
operator|new
name|WatchConfig
argument_list|(
name|accountId
argument_list|,
name|readConfig
argument_list|(
name|WatchConfig
operator|.
name|WATCH_CONFIG
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|prefConfig
operator|=
operator|new
name|PreferencesConfig
argument_list|(
name|accountId
argument_list|,
name|readConfig
argument_list|(
name|PreferencesConfig
operator|.
name|PREFERENCES_CONFIG
argument_list|)
argument_list|,
name|PreferencesConfig
operator|.
name|readDefaultConfig
argument_list|(
name|repo
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|eagerParsing
condition|)
block|{
name|watchConfig
operator|.
name|parse
argument_list|()
expr_stmt|;
name|prefConfig
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|loadedAccount
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|watchConfig
operator|=
operator|new
name|WatchConfig
argument_list|(
name|accountId
argument_list|,
operator|new
name|Config
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|prefConfig
operator|=
operator|new
name|PreferencesConfig
argument_list|(
name|accountId
argument_list|,
operator|new
name|Config
argument_list|()
argument_list|,
name|PreferencesConfig
operator|.
name|readDefaultConfig
argument_list|(
name|repo
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|Ref
name|externalIdsRef
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|REFS_EXTERNAL_IDS
argument_list|)
decl_stmt|;
name|externalIdsRev
operator|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|externalIdsRef
argument_list|)
operator|.
name|map
argument_list|(
name|Ref
operator|::
name|getObjectId
argument_list|)
expr_stmt|;
block|}
DECL|method|parse (Config cfg, String metaId)
specifier|private
name|Account
name|parse
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|metaId
parameter_list|)
block|{
name|Account
name|account
init|=
operator|new
name|Account
argument_list|(
name|accountId
argument_list|,
name|registeredOn
argument_list|)
decl_stmt|;
name|account
operator|.
name|setActive
argument_list|(
name|cfg
operator|.
name|getBoolean
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFullName
argument_list|(
name|get
argument_list|(
name|cfg
argument_list|,
name|KEY_FULL_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|preferredEmail
init|=
name|get
argument_list|(
name|cfg
argument_list|,
name|KEY_PREFERRED_EMAIL
argument_list|)
decl_stmt|;
name|account
operator|.
name|setPreferredEmail
argument_list|(
name|preferredEmail
argument_list|)
expr_stmt|;
name|account
operator|.
name|setStatus
argument_list|(
name|get
argument_list|(
name|cfg
argument_list|,
name|KEY_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|account
operator|.
name|setMetaId
argument_list|(
name|metaId
argument_list|)
expr_stmt|;
return|return
name|account
return|;
block|}
annotation|@
name|Override
DECL|method|commit (MetaDataUpdate update)
specifier|public
name|RevCommit
name|commit
parameter_list|(
name|MetaDataUpdate
name|update
parameter_list|)
throws|throws
name|IOException
block|{
name|RevCommit
name|c
init|=
name|super
operator|.
name|commit
argument_list|(
name|update
argument_list|)
decl_stmt|;
name|loadedAccount
operator|.
name|get
argument_list|()
operator|.
name|setMetaId
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|Override
DECL|method|onSave (CommitBuilder commit)
specifier|protected
name|boolean
name|onSave
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|checkLoaded
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|loadedAccount
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|commit
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|commit
operator|.
name|setMessage
argument_list|(
literal|"Update account\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|commit
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|commit
operator|.
name|setMessage
argument_list|(
literal|"Create account\n"
argument_list|)
expr_stmt|;
block|}
name|commit
operator|.
name|setAuthor
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|commit
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|registeredOn
argument_list|)
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|commit
operator|.
name|getCommitter
argument_list|()
argument_list|,
name|registeredOn
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Config
name|accountConfig
init|=
name|saveAccount
argument_list|()
decl_stmt|;
name|saveProjectWatches
argument_list|()
expr_stmt|;
name|saveGeneralPreferences
argument_list|()
expr_stmt|;
comment|// metaId is set in the commit(MetaDataUpdate) method after the commit is created
name|loadedAccount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|parse
argument_list|(
name|accountConfig
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|saveAccount ()
specifier|private
name|Config
name|saveAccount
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|Config
name|accountConfig
init|=
name|readConfig
argument_list|(
name|ACCOUNT_CONFIG
argument_list|)
decl_stmt|;
if|if
condition|(
name|accountUpdate
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|writeToAccountConfig
argument_list|(
name|accountUpdate
operator|.
name|get
argument_list|()
argument_list|,
name|accountConfig
argument_list|)
expr_stmt|;
block|}
name|saveConfig
argument_list|(
name|ACCOUNT_CONFIG
argument_list|,
name|accountConfig
argument_list|)
expr_stmt|;
return|return
name|accountConfig
return|;
block|}
DECL|method|writeToAccountConfig (InternalAccountUpdate accountUpdate, Config cfg)
specifier|public
specifier|static
name|void
name|writeToAccountConfig
parameter_list|(
name|InternalAccountUpdate
name|accountUpdate
parameter_list|,
name|Config
name|cfg
parameter_list|)
block|{
name|accountUpdate
operator|.
name|getActive
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|active
lambda|->
name|setActive
argument_list|(
name|cfg
argument_list|,
name|active
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getFullName
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|fullName
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_FULL_NAME
argument_list|,
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getPreferredEmail
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|preferredEmail
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_PREFERRED_EMAIL
argument_list|,
name|preferredEmail
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getStatus
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|status
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_STATUS
argument_list|,
name|status
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|saveProjectWatches ()
specifier|private
name|void
name|saveProjectWatches
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|accountUpdate
operator|.
name|isPresent
argument_list|()
operator|&&
operator|(
operator|!
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getDeletedProjectWatches
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getUpdatedProjectWatches
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|Map
argument_list|<
name|ProjectWatchKey
argument_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|projectWatches
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|watchConfig
operator|.
name|getProjectWatches
argument_list|()
argument_list|)
decl_stmt|;
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getDeletedProjectWatches
argument_list|()
operator|.
name|forEach
argument_list|(
name|pw
lambda|->
name|projectWatches
operator|.
name|remove
argument_list|(
name|pw
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getUpdatedProjectWatches
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|pw
parameter_list|,
name|nt
parameter_list|)
lambda|->
name|projectWatches
operator|.
name|put
argument_list|(
name|pw
argument_list|,
name|nt
argument_list|)
argument_list|)
expr_stmt|;
name|saveConfig
argument_list|(
name|WatchConfig
operator|.
name|WATCH_CONFIG
argument_list|,
name|watchConfig
operator|.
name|save
argument_list|(
name|projectWatches
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|saveGeneralPreferences ()
specifier|private
name|void
name|saveGeneralPreferences
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|accountUpdate
operator|.
name|isPresent
argument_list|()
operator|&&
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getGeneralPreferences
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|saveConfig
argument_list|(
name|PreferencesConfig
operator|.
name|PREFERENCES_CONFIG
argument_list|,
name|prefConfig
operator|.
name|saveGeneralPreferences
argument_list|(
name|accountUpdate
operator|.
name|get
argument_list|()
operator|.
name|getGeneralPreferences
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Sets/Unsets {@code account.active} in the given config.    *    *<p>{@code account.active} is set to {@code false} if the account is inactive.    *    *<p>If the account is active {@code account.active} is unset since {@code true} is the default    * if this field is missing.    *    * @param cfg the config    * @param value whether the account is active    */
DECL|method|setActive (Config cfg, boolean value)
specifier|private
specifier|static
name|void
name|setActive
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|value
condition|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Sets/Unsets the given key in the given config.    *    *<p>The key unset if the value is {@code null}.    *    * @param cfg the config    * @param key the key    * @param value the value    */
DECL|method|set (Config cfg, String key, String value)
specifier|private
specifier|static
name|void
name|set
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Gets the given key from the given config.    *    *<p>Empty values are returned as {@code null}    *    * @param cfg the config    * @param key the key    * @return the value, {@code null} if key was not set or key was set to empty string    */
DECL|method|get (Config cfg, String key)
specifier|private
specifier|static
name|String
name|get
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
argument_list|)
return|;
block|}
DECL|method|checkLoaded ()
specifier|private
name|void
name|checkLoaded
parameter_list|()
block|{
name|checkState
argument_list|(
name|loadedAccount
operator|!=
literal|null
argument_list|,
literal|"Account %s not loaded yet"
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Get the validation errors, if any were discovered during parsing the account data.    *    *<p>To get validation errors for all account data request eager parsing before loading the    * account (see {@link #setEagerParsing(boolean)}).    *    * @return list of errors; empty list if there are no errors.    */
DECL|method|getValidationErrors ()
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getValidationErrors
parameter_list|()
block|{
if|if
condition|(
name|validationErrors
operator|!=
literal|null
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|validationErrors
argument_list|)
return|;
block|}
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|error (ValidationError error)
specifier|public
name|void
name|error
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
if|if
condition|(
name|validationErrors
operator|==
literal|null
condition|)
block|{
name|validationErrors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
name|validationErrors
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


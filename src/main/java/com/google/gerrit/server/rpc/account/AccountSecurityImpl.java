begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rpc
operator|.
name|account
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
name|account
operator|.
name|AccountSecurity
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
name|AccountAgreement
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
name|AccountExternalId
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
name|AccountSshKey
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
name|ContactInformation
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
name|ContributorAgreement
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
name|rpc
operator|.
name|ContactInformationStoreException
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
name|InvalidSshKeyException
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
name|client
operator|.
name|rpc
operator|.
name|NoSuchEntityException
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
name|BaseServiceImplementation
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
name|CurrentUser
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
name|AccountByEmailCache
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
name|AccountException
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
name|AccountManager
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
name|AuthRequest
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
name|Realm
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
name|AuthConfig
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
name|contact
operator|.
name|ContactStore
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
name|mail
operator|.
name|EmailException
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
name|mail
operator|.
name|RegisterNewEmailSender
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
name|ssh
operator|.
name|SshKeyCache
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
name|ssh
operator|.
name|SshUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|ValidToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Transaction
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

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchProviderException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
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
name|Set
import|;
end_import

begin_class
DECL|class|AccountSecurityImpl
class|class
name|AccountSecurityImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|AccountSecurity
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|contactStore
specifier|private
specifier|final
name|ContactStore
name|contactStore
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|registerNewEmailFactory
specifier|private
specifier|final
name|RegisterNewEmailSender
operator|.
name|Factory
name|registerNewEmailFactory
decl_stmt|;
DECL|field|sshKeyCache
specifier|private
specifier|final
name|SshKeyCache
name|sshKeyCache
decl_stmt|;
DECL|field|byEmailCache
specifier|private
specifier|final
name|AccountByEmailCache
name|byEmailCache
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|accountManager
specifier|private
specifier|final
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|useContactInfo
specifier|private
specifier|final
name|boolean
name|useContactInfo
decl_stmt|;
DECL|field|externalIdDetailFactory
specifier|private
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountSecurityImpl (final Provider<ReviewDb> schema, final Provider<CurrentUser> currentUser, final ContactStore cs, final AuthConfig ac, final Realm r, final RegisterNewEmailSender.Factory esf, final SshKeyCache skc, final AccountByEmailCache abec, final AccountCache uac, final AccountManager am, final ExternalIdDetailFactory.Factory externalIdDetailFactory)
name|AccountSecurityImpl
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
parameter_list|,
specifier|final
name|ContactStore
name|cs
parameter_list|,
specifier|final
name|AuthConfig
name|ac
parameter_list|,
specifier|final
name|Realm
name|r
parameter_list|,
specifier|final
name|RegisterNewEmailSender
operator|.
name|Factory
name|esf
parameter_list|,
specifier|final
name|SshKeyCache
name|skc
parameter_list|,
specifier|final
name|AccountByEmailCache
name|abec
parameter_list|,
specifier|final
name|AccountCache
name|uac
parameter_list|,
specifier|final
name|AccountManager
name|am
parameter_list|,
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|currentUser
argument_list|)
expr_stmt|;
name|contactStore
operator|=
name|cs
expr_stmt|;
name|authConfig
operator|=
name|ac
expr_stmt|;
name|realm
operator|=
name|r
expr_stmt|;
name|registerNewEmailFactory
operator|=
name|esf
expr_stmt|;
name|sshKeyCache
operator|=
name|skc
expr_stmt|;
name|byEmailCache
operator|=
name|abec
expr_stmt|;
name|accountCache
operator|=
name|uac
expr_stmt|;
name|accountManager
operator|=
name|am
expr_stmt|;
name|useContactInfo
operator|=
name|contactStore
operator|!=
literal|null
operator|&&
name|contactStore
operator|.
name|isEnabled
argument_list|()
expr_stmt|;
name|this
operator|.
name|externalIdDetailFactory
operator|=
name|externalIdDetailFactory
expr_stmt|;
block|}
DECL|method|mySshKeys (final AsyncCallback<List<AccountSshKey>> callback)
specifier|public
name|void
name|mySshKeys
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|byAccount
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addSshKey (final String keyText, final AsyncCallback<AccountSshKey> callback)
specifier|public
name|void
name|addSshKey
parameter_list|(
specifier|final
name|String
name|keyText
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountSshKey
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountSshKey
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
name|int
name|max
init|=
literal|0
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|getAccountId
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountSshKey
name|k
range|:
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|byAccount
argument_list|(
name|me
argument_list|)
control|)
block|{
name|max
operator|=
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|k
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|keyStr
init|=
name|keyText
decl_stmt|;
if|if
condition|(
name|keyStr
operator|.
name|startsWith
argument_list|(
literal|"---- BEGIN SSH2 PUBLIC KEY ----"
argument_list|)
condition|)
block|{
name|keyStr
operator|=
name|SshUtil
operator|.
name|toOpenSshPublicKey
argument_list|(
name|keyStr
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AccountSshKey
name|newKey
init|=
operator|new
name|AccountSshKey
argument_list|(
operator|new
name|AccountSshKey
operator|.
name|Id
argument_list|(
name|me
argument_list|,
name|max
operator|+
literal|1
argument_list|)
argument_list|,
name|keyStr
argument_list|)
decl_stmt|;
try|try
block|{
name|SshUtil
operator|.
name|parse
argument_list|(
name|newKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|InvalidSshKeyException
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidKeySpecException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|InvalidSshKeyException
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot parse SSH key"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|InvalidSshKeyException
argument_list|()
argument_list|)
throw|;
block|}
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|newKey
argument_list|)
argument_list|)
expr_stmt|;
name|uncacheSshKeys
argument_list|(
name|me
argument_list|)
expr_stmt|;
return|return
name|newKey
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteSshKeys (final Set<AccountSshKey.Id> ids, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|deleteSshKeys
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountSshKey
operator|.
name|Id
argument_list|>
name|ids
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|getAccountId
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountSshKey
operator|.
name|Id
name|keyId
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
name|me
operator|.
name|equals
argument_list|(
name|keyId
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|k
init|=
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|get
argument_list|(
name|ids
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|k
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|delete
argument_list|(
name|k
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|uncacheSshKeys
argument_list|(
name|me
argument_list|)
expr_stmt|;
block|}
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|uncacheSshKeys (final Account.Id me)
specifier|private
name|void
name|uncacheSshKeys
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|me
parameter_list|)
block|{
name|uncacheSshKeys
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|me
argument_list|)
operator|.
name|getAccount
argument_list|()
operator|.
name|getSshUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|uncacheSshKeys (final String userName)
specifier|private
name|void
name|uncacheSshKeys
parameter_list|(
specifier|final
name|String
name|userName
parameter_list|)
block|{
name|sshKeyCache
operator|.
name|evict
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|changeSshUserName (final String newName, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeSshUserName
parameter_list|(
specifier|final
name|String
name|newName
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|realm
operator|.
name|allowsEdit
argument_list|(
name|Account
operator|.
name|FieldName
operator|.
name|SSH_USER_NAME
argument_list|)
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|NameAlreadyUsedException
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|VoidResult
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Account
name|me
init|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|me
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Account
name|other
decl_stmt|;
if|if
condition|(
name|newName
operator|!=
literal|null
condition|)
block|{
name|other
operator|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|bySshUserName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|other
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|other
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|me
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NameAlreadyUsedException
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|final
name|String
name|oldName
init|=
name|me
operator|.
name|getSshUserName
argument_list|()
decl_stmt|;
name|me
operator|.
name|setSshUserName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
name|uncacheSshKeys
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|uncacheSshKeys
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|accountCache
operator|.
name|evict
argument_list|(
name|me
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|myExternalIds (AsyncCallback<List<AccountExternalId>> callback)
specifier|public
name|void
name|myExternalIds
parameter_list|(
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|externalIdDetailFactory
operator|.
name|create
argument_list|()
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteExternalIds (final Set<AccountExternalId.Key> keys, final AsyncCallback<Set<AccountExternalId.Key>> callback)
specifier|public
name|void
name|deleteExternalIds
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|keys
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
comment|// Don't permit deletes unless they are for our own account
comment|//
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|getAccountId
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
operator|.
name|Key
name|keyId
range|:
name|keys
control|)
block|{
if|if
condition|(
operator|!
name|me
operator|.
name|equals
argument_list|(
name|keyId
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
comment|// Determine the records we will allow the user to remove.
comment|//
specifier|final
name|Map
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|,
name|AccountExternalId
argument_list|>
name|all
init|=
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|toMap
argument_list|(
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byAccount
argument_list|(
name|me
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|AccountExternalId
name|mostRecent
init|=
name|AccountExternalId
operator|.
name|mostRecent
argument_list|(
name|all
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|removed
init|=
operator|new
name|HashSet
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|toDelete
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
operator|.
name|Key
name|k
range|:
name|keys
control|)
block|{
specifier|final
name|AccountExternalId
name|e
init|=
name|all
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
comment|// Its already gone, tell the client its gone
comment|//
name|removed
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|==
name|mostRecent
condition|)
block|{
comment|// Don't delete the most recently accessed identity; the
comment|// user might lock themselves out of the account.
comment|//
continue|continue;
block|}
else|else
block|{
name|toDelete
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|removed
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|toDelete
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|delete
argument_list|(
name|toDelete
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|accountCache
operator|.
name|evict
argument_list|(
name|me
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountExternalId
name|e
range|:
name|toDelete
control|)
block|{
name|byEmailCache
operator|.
name|evict
argument_list|(
name|e
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|removed
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|updateContact (final String name, final String emailAddr, final ContactInformation info, final AsyncCallback<Account> callback)
specifier|public
name|void
name|updateContact
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|emailAddr
parameter_list|,
specifier|final
name|ContactInformation
name|info
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|Account
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|Account
argument_list|>
argument_list|()
block|{
specifier|public
name|Account
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Account
name|me
init|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|oldEmail
init|=
name|me
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
if|if
condition|(
name|realm
operator|.
name|allowsEdit
argument_list|(
name|Account
operator|.
name|FieldName
operator|.
name|FULL_NAME
argument_list|)
condition|)
block|{
name|me
operator|.
name|setFullName
argument_list|(
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|isEmpty
argument_list|()
condition|?
name|name
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
name|me
operator|.
name|setPreferredEmail
argument_list|(
name|emailAddr
argument_list|)
expr_stmt|;
if|if
condition|(
name|useContactInfo
condition|)
block|{
if|if
condition|(
name|ContactInformation
operator|.
name|hasAddress
argument_list|(
name|info
argument_list|)
operator|||
operator|(
name|me
operator|.
name|isContactFiled
argument_list|()
operator|&&
name|ContactInformation
operator|.
name|hasData
argument_list|(
name|info
argument_list|)
operator|)
condition|)
block|{
name|me
operator|.
name|setContactFiled
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ContactInformation
operator|.
name|hasData
argument_list|(
name|info
argument_list|)
condition|)
block|{
try|try
block|{
name|contactStore
operator|.
name|store
argument_list|(
name|me
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContactInformationStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|eq
argument_list|(
name|oldEmail
argument_list|,
name|me
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
condition|)
block|{
name|byEmailCache
operator|.
name|evict
argument_list|(
name|oldEmail
argument_list|)
expr_stmt|;
name|byEmailCache
operator|.
name|evict
argument_list|(
name|me
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|accountCache
operator|.
name|evict
argument_list|(
name|me
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|me
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|eq (final String a, final String b)
specifier|private
specifier|static
name|boolean
name|eq
parameter_list|(
specifier|final
name|String
name|a
parameter_list|,
specifier|final
name|String
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
operator|&&
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
DECL|method|enterAgreement (final ContributorAgreement.Id id, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|enterAgreement
parameter_list|(
specifier|final
name|ContributorAgreement
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|ContributorAgreement
name|cla
init|=
name|db
operator|.
name|contributorAgreements
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cla
operator|==
literal|null
operator|||
operator|!
name|cla
operator|.
name|isActive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountAgreement
name|a
init|=
operator|new
name|AccountAgreement
argument_list|(
operator|new
name|AccountAgreement
operator|.
name|Key
argument_list|(
name|getAccountId
argument_list|()
argument_list|,
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cla
operator|.
name|isAutoVerify
argument_list|()
condition|)
block|{
name|a
operator|.
name|review
argument_list|(
name|AccountAgreement
operator|.
name|Status
operator|.
name|VERIFIED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountAgreements
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|registerEmail (final String address, final AsyncCallback<VoidResult> cb)
specifier|public
name|void
name|registerEmail
parameter_list|(
specifier|final
name|String
name|address
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
try|try
block|{
specifier|final
name|RegisterNewEmailSender
name|sender
decl_stmt|;
name|sender
operator|=
name|registerNewEmailFactory
operator|.
name|create
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|sender
operator|.
name|send
argument_list|()
expr_stmt|;
name|cb
operator|.
name|onSuccess
argument_list|(
name|VoidResult
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EmailException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email verification message to "
operator|+
name|address
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email verification message to "
operator|+
name|address
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|validateEmail (final String token, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|validateEmail
parameter_list|(
specifier|final
name|String
name|token
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ValidToken
name|t
init|=
name|authConfig
operator|.
name|getEmailRegistrationToken
argument_list|()
operator|.
name|checkToken
argument_list|(
name|token
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
operator|||
name|t
operator|.
name|getData
argument_list|()
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|t
operator|.
name|getData
argument_list|()
argument_list|)
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid token"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|String
name|newEmail
init|=
operator|new
name|String
argument_list|(
name|Base64
operator|.
name|decode
argument_list|(
name|t
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|newEmail
operator|.
name|contains
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid token"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|accountManager
operator|.
name|link
argument_list|(
name|getAccountId
argument_list|()
argument_list|,
name|AuthRequest
operator|.
name|forEmail
argument_list|(
name|newEmail
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onSuccess
argument_list|(
name|VoidResult
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


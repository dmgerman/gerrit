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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|account
operator|.
name|ExternalIdDetail
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
name|reviewdb
operator|.
name|TrustedExternalId
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
name|lib
operator|.
name|PersonIdent
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|method|AccountSecurityImpl (final GerritServer gs)
name|AccountSecurityImpl
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|)
block|{
name|server
operator|=
name|gs
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
name|Common
operator|.
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
name|Common
operator|.
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
argument_list|,
name|db
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
name|Common
operator|.
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
argument_list|,
name|db
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
DECL|method|uncacheSshKeys (final Account.Id me, final ReviewDb db)
specifier|private
name|void
name|uncacheSshKeys
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|me
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|)
block|{
specifier|final
name|Account
name|a
init|=
name|Common
operator|.
name|getAccountCache
argument_list|()
operator|.
name|get
argument_list|(
name|me
argument_list|,
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|uncacheSshKeys
argument_list|(
name|a
operator|.
name|getSshUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|userName
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|getSshKeysCache
argument_list|()
operator|.
name|remove
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|myExternalIds (AsyncCallback<ExternalIdDetail> callback)
specifier|public
name|void
name|myExternalIds
parameter_list|(
name|AsyncCallback
argument_list|<
name|ExternalIdDetail
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
name|ExternalIdDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|ExternalIdDetail
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|TrustedExternalId
argument_list|>
name|trusted
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|getTrustedExternalIds
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|myIds
init|=
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byAccount
argument_list|(
name|me
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
return|return
operator|new
name|ExternalIdDetail
argument_list|(
name|myIds
argument_list|,
name|trusted
argument_list|)
return|;
block|}
block|}
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
name|Common
operator|.
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
elseif|else
if|if
condition|(
name|e
operator|.
name|canUserDelete
argument_list|()
condition|)
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
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|invalidate
argument_list|(
name|me
argument_list|)
expr_stmt|;
block|}
return|return
name|removed
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|updateContact (final String fullName, final String emailAddr, final ContactInformation info, final AsyncCallback<Account> callback)
specifier|public
name|void
name|updateContact
parameter_list|(
specifier|final
name|String
name|fullName
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
name|Common
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|oldUser
init|=
name|me
operator|.
name|getSshUserName
argument_list|()
decl_stmt|;
name|me
operator|.
name|setFullName
argument_list|(
name|fullName
argument_list|)
expr_stmt|;
name|me
operator|.
name|setPreferredEmail
argument_list|(
name|emailAddr
argument_list|)
expr_stmt|;
if|if
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|isUseContactInfo
argument_list|()
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
name|EncryptedContactStore
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
name|oldUser
argument_list|,
name|me
operator|.
name|getSshUserName
argument_list|()
argument_list|)
condition|)
block|{
name|uncacheSshKeys
argument_list|(
name|oldUser
argument_list|)
expr_stmt|;
name|uncacheSshKeys
argument_list|(
name|me
operator|.
name|getSshUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Common
operator|.
name|getAccountCache
argument_list|()
operator|.
name|invalidate
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
name|Common
operator|.
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
specifier|final
name|PersonIdent
name|gi
init|=
name|server
operator|.
name|newGerritPersonIdent
argument_list|()
decl_stmt|;
specifier|final
name|HttpServletRequest
name|req
init|=
name|GerritJsonServlet
operator|.
name|getCurrentCall
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|RegisterNewEmailSender
name|sender
decl_stmt|;
name|sender
operator|=
operator|new
name|RegisterNewEmailSender
argument_list|(
name|server
argument_list|,
name|address
argument_list|,
name|req
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
specifier|final
name|String
name|address
decl_stmt|;
try|try
block|{
specifier|final
name|ValidToken
name|t
init|=
name|server
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
name|address
operator|=
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
expr_stmt|;
if|if
condition|(
operator|!
name|address
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
return|return;
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
specifier|public
name|VoidResult
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|exists
init|=
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byAccountEmail
argument_list|(
name|me
argument_list|,
name|address
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|exists
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
try|try
block|{
specifier|final
name|AccountExternalId
name|id
init|=
operator|new
name|AccountExternalId
argument_list|(
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|me
argument_list|,
literal|"mailto:"
operator|+
name|address
argument_list|)
argument_list|)
decl_stmt|;
name|id
operator|.
name|setEmailAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmDuplicateKeyException
name|e
parameter_list|)
block|{
comment|// Ignore a duplicate registration
block|}
specifier|final
name|Account
name|a
init|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|me
argument_list|)
decl_stmt|;
name|a
operator|.
name|setPreferredEmail
argument_list|(
name|address
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
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|Common
operator|.
name|getAccountCache
argument_list|()
operator|.
name|invalidate
argument_list|(
name|me
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
block|}
end_class

end_unit


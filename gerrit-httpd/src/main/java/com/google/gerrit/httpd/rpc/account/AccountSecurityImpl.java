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
DECL|package|com.google.gerrit.httpd.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|ChangeHooks
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
name|common
operator|.
name|data
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
name|common
operator|.
name|data
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
name|common
operator|.
name|errors
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
name|common
operator|.
name|errors
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
name|common
operator|.
name|errors
operator|.
name|PermissionDeniedException
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
name|httpd
operator|.
name|rpc
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
name|project
operator|.
name|ProjectCache
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
name|common
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
name|common
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
name|List
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
DECL|field|contactStore
specifier|private
specifier|final
name|ContactStore
name|contactStore
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
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
DECL|field|useContactInfo
specifier|private
specifier|final
name|boolean
name|useContactInfo
decl_stmt|;
DECL|field|deleteExternalIdsFactory
specifier|private
specifier|final
name|DeleteExternalIds
operator|.
name|Factory
name|deleteExternalIdsFactory
decl_stmt|;
DECL|field|externalIdDetailFactory
specifier|private
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|auditService
specifier|private
specifier|final
name|AuditService
name|auditService
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountSecurityImpl (final Provider<ReviewDb> schema, final Provider<CurrentUser> currentUser, final ContactStore cs, final Realm r, final Provider<IdentifiedUser> u, final ProjectCache pc, final AccountByEmailCache abec, final AccountCache uac, final DeleteExternalIds.Factory deleteExternalIdsFactory, final ExternalIdDetailFactory.Factory externalIdDetailFactory, final ChangeHooks hooks, final GroupCache groupCache, final AuditService auditService)
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
name|Realm
name|r
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|u
parameter_list|,
specifier|final
name|ProjectCache
name|pc
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
name|DeleteExternalIds
operator|.
name|Factory
name|deleteExternalIdsFactory
parameter_list|,
specifier|final
name|ExternalIdDetailFactory
operator|.
name|Factory
name|externalIdDetailFactory
parameter_list|,
specifier|final
name|ChangeHooks
name|hooks
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|AuditService
name|auditService
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
name|realm
operator|=
name|r
expr_stmt|;
name|user
operator|=
name|u
expr_stmt|;
name|projectCache
operator|=
name|pc
expr_stmt|;
name|byEmailCache
operator|=
name|abec
expr_stmt|;
name|accountCache
operator|=
name|uac
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
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
name|deleteExternalIdsFactory
operator|=
name|deleteExternalIdsFactory
expr_stmt|;
name|this
operator|.
name|externalIdDetailFactory
operator|=
name|externalIdDetailFactory
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
name|deleteExternalIdsFactory
operator|.
name|create
argument_list|(
name|keys
argument_list|)
operator|.
name|to
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
name|IdentifiedUser
name|self
init|=
name|user
operator|.
name|get
argument_list|()
decl_stmt|;
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
name|self
operator|.
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
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|emailAddr
argument_list|)
operator|&&
operator|!
name|self
operator|.
name|hasEmailAddress
argument_list|(
name|emailAddr
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|PermissionDeniedException
argument_list|(
literal|"Email address must be verified"
argument_list|)
argument_list|)
throw|;
block|}
name|me
operator|.
name|setPreferredEmail
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|emailAddr
argument_list|)
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
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
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
annotation|@
name|Override
DECL|method|enterAgreement (final String agreementName, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|enterAgreement
parameter_list|(
specifier|final
name|String
name|agreementName
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
annotation|@
name|Override
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
name|ContributorAgreement
name|ca
init|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getContributorAgreement
argument_list|(
name|agreementName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ca
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
if|if
condition|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"cannot enter a non-autoVerify agreement"
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|.
name|getUUID
argument_list|()
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
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
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
name|Account
name|account
init|=
name|user
operator|.
name|get
argument_list|()
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|hooks
operator|.
name|doClaSignupHook
argument_list|(
name|account
argument_list|,
name|ca
argument_list|)
expr_stmt|;
specifier|final
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
name|account
operator|.
name|getId
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|AccountGroupMember
name|m
init|=
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|AccountGroupMember
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|auditService
operator|.
name|dispatchAddAccountsToGroup
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|accountCache
operator|.
name|evict
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
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
block|}
end_class

end_unit


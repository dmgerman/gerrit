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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccountProjectWatchInfo
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
name|AccountService
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
name|AgreementInfo
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
name|InvalidQueryException
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
name|AccountDiffPreference
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
name|AccountGeneralPreferences
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
name|AccountProjectWatch
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
name|Project
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
name|project
operator|.
name|NoSuchProjectException
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
name|ProjectControl
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
name|query
operator|.
name|QueryParseException
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|Comparator
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
DECL|class|AccountServiceImpl
class|class
name|AccountServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|AccountService
block|{
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|agreementInfoFactory
specifier|private
specifier|final
name|AgreementInfoFactory
operator|.
name|Factory
name|agreementInfoFactory
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|ChangeQueryBuilder
operator|.
name|Factory
name|queryBuilder
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountServiceImpl (final Provider<ReviewDb> schema, final Provider<IdentifiedUser> identifiedUser, final AccountCache accountCache, final ProjectControl.Factory projectControlFactory, final AgreementInfoFactory.Factory agreementInfoFactory, final ChangeQueryBuilder.Factory queryBuilder)
name|AccountServiceImpl
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
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
specifier|final
name|AccountCache
name|accountCache
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|AgreementInfoFactory
operator|.
name|Factory
name|agreementInfoFactory
parameter_list|,
specifier|final
name|ChangeQueryBuilder
operator|.
name|Factory
name|queryBuilder
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|identifiedUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|identifiedUser
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|agreementInfoFactory
operator|=
name|agreementInfoFactory
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
block|}
DECL|method|myAccount (final AsyncCallback<Account> callback)
specifier|public
name|void
name|myAccount
parameter_list|(
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
block|{
return|return
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|currentUser
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|changePreferences (final AccountGeneralPreferences pref, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changePreferences
parameter_list|(
specifier|final
name|AccountGeneralPreferences
name|pref
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
name|a
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
name|a
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
name|a
operator|.
name|setGeneralPreferences
argument_list|(
name|pref
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
name|accountCache
operator|.
name|evict
argument_list|(
name|a
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
annotation|@
name|Override
DECL|method|changeDiffPreferences (final AccountDiffPreference diffPref, AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|changeDiffPreferences
parameter_list|(
specifier|final
name|AccountDiffPreference
name|diffPref
parameter_list|,
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
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|diffPref
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"diffPref.getAccountId() "
operator|+
name|diffPref
operator|.
name|getAccountId
argument_list|()
operator|+
literal|" doesn't match"
operator|+
literal|" the accountId of the signed in user "
operator|+
name|getAccountId
argument_list|()
argument_list|)
throw|;
block|}
name|db
operator|.
name|accountDiffPreferences
argument_list|()
operator|.
name|upsert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|diffPref
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
DECL|method|myProjectWatch ( final AsyncCallback<List<AccountProjectWatchInfo>> callback)
specifier|public
name|void
name|myProjectWatch
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|AccountProjectWatchInfo
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
name|AccountProjectWatchInfo
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|List
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountProjectWatch
name|w
range|:
name|db
operator|.
name|accountProjectWatches
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
control|)
block|{
specifier|final
name|ProjectControl
name|ctl
decl_stmt|;
try|try
block|{
name|ctl
operator|=
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|w
operator|.
name|getProjectNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|delete
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|w
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|r
operator|.
name|add
argument_list|(
operator|new
name|AccountProjectWatchInfo
argument_list|(
name|w
argument_list|,
name|ctl
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|r
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|AccountProjectWatchInfo
name|a
parameter_list|,
specifier|final
name|AccountProjectWatchInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addProjectWatch (final String projectName, final String filter, final AsyncCallback<AccountProjectWatchInfo> callback)
specifier|public
name|void
name|addProjectWatch
parameter_list|(
specifier|final
name|String
name|projectName
parameter_list|,
specifier|final
name|String
name|filter
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountProjectWatchInfo
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
name|AccountProjectWatchInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountProjectWatchInfo
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchProjectException
throws|,
name|InvalidQueryException
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
specifier|final
name|ProjectControl
name|ctl
init|=
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|queryBuilder
operator|.
name|create
argument_list|(
name|currentUser
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|parse
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|badFilter
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidQueryException
argument_list|(
name|badFilter
operator|.
name|getMessage
argument_list|()
argument_list|,
name|filter
argument_list|)
throw|;
block|}
block|}
name|AccountProjectWatch
name|watch
init|=
operator|new
name|AccountProjectWatch
argument_list|(
operator|new
name|AccountProjectWatch
operator|.
name|Key
argument_list|(
operator|(
operator|(
name|IdentifiedUser
operator|)
name|ctl
operator|.
name|getCurrentUser
argument_list|()
operator|)
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|nameKey
argument_list|,
name|filter
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|watch
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmDuplicateKeyException
name|alreadyHave
parameter_list|)
block|{
name|watch
operator|=
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|get
argument_list|(
name|watch
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|AccountProjectWatchInfo
argument_list|(
name|watch
argument_list|,
name|ctl
operator|.
name|getProject
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|updateProjectWatch (final AccountProjectWatch watch, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|updateProjectWatch
parameter_list|(
specifier|final
name|AccountProjectWatch
name|watch
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
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|watch
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|NoSuchEntityException
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
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|watch
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
DECL|method|deleteProjectWatches (final Set<AccountProjectWatch.Key> keys, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|deleteProjectWatches
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountProjectWatch
operator|.
name|Key
argument_list|>
name|keys
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
name|AccountProjectWatch
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
name|db
operator|.
name|accountProjectWatches
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|keys
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
DECL|method|myAgreements (final AsyncCallback<AgreementInfo> callback)
specifier|public
name|void
name|myAgreements
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|AgreementInfo
argument_list|>
name|callback
parameter_list|)
block|{
name|agreementInfoFactory
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
block|}
end_class

end_unit


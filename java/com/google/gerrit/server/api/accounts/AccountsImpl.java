begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|accounts
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|accounts
operator|.
name|AccountApi
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
name|api
operator|.
name|accounts
operator|.
name|AccountInput
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
name|api
operator|.
name|accounts
operator|.
name|Accounts
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
name|ListAccountsOption
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
name|common
operator|.
name|AccountInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|BadRequestException
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
name|restapi
operator|.
name|IdString
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
name|restapi
operator|.
name|RestApiException
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
name|restapi
operator|.
name|TopLevelResource
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
name|AccountResource
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|restapi
operator|.
name|account
operator|.
name|AccountsCollection
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
name|restapi
operator|.
name|account
operator|.
name|CreateAccount
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
name|restapi
operator|.
name|account
operator|.
name|QueryAccounts
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
name|util
operator|.
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|AccountsImpl
specifier|public
class|class
name|AccountsImpl
implements|implements
name|Accounts
block|{
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
decl_stmt|;
DECL|field|api
specifier|private
specifier|final
name|AccountApiImpl
operator|.
name|Factory
name|api
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|createAccount
specifier|private
specifier|final
name|CreateAccount
name|createAccount
decl_stmt|;
DECL|field|queryAccountsProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryAccounts
argument_list|>
name|queryAccountsProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountsImpl ( AccountsCollection accounts, AccountApiImpl.Factory api, PermissionBackend permissionBackend, Provider<CurrentUser> self, CreateAccount createAccount, Provider<QueryAccounts> queryAccountsProvider)
name|AccountsImpl
parameter_list|(
name|AccountsCollection
name|accounts
parameter_list|,
name|AccountApiImpl
operator|.
name|Factory
name|api
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|CreateAccount
name|createAccount
parameter_list|,
name|Provider
argument_list|<
name|QueryAccounts
argument_list|>
name|queryAccountsProvider
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|api
operator|=
name|api
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|createAccount
operator|=
name|createAccount
expr_stmt|;
name|this
operator|.
name|queryAccountsProvider
operator|=
name|queryAccountsProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|id (String id)
specifier|public
name|AccountApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|api
operator|.
name|create
argument_list|(
name|accounts
operator|.
name|parse
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot parse account"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|id (int id)
specifier|public
name|AccountApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|id
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|self ()
specifier|public
name|AccountApi
name|self
parameter_list|()
throws|throws
name|RestApiException
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
return|return
name|api
operator|.
name|create
argument_list|(
operator|new
name|AccountResource
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (String username)
specifier|public
name|AccountApi
name|create
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|RestApiException
block|{
name|AccountInput
name|in
init|=
operator|new
name|AccountInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|username
operator|=
name|username
expr_stmt|;
return|return
name|create
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (AccountInput in)
specifier|public
name|AccountApi
name|create
parameter_list|(
name|AccountInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
if|if
condition|(
name|requireNonNull
argument_list|(
name|in
argument_list|,
literal|"AccountInput"
argument_list|)
operator|.
name|username
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"AccountInput must specify username"
argument_list|)
throw|;
block|}
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|checkAny
argument_list|(
name|GlobalPermission
operator|.
name|fromAnnotation
argument_list|(
name|createAccount
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|AccountInfo
name|info
init|=
name|createAccount
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|in
operator|.
name|username
argument_list|)
argument_list|,
name|in
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
return|return
name|id
argument_list|(
name|info
operator|.
name|_accountId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot create account "
operator|+
name|in
operator|.
name|username
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|suggestAccounts ()
specifier|public
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
operator|new
name|SuggestAccountsRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|AccountsImpl
operator|.
name|this
operator|.
name|suggestAccounts
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|suggestAccounts (String query)
specifier|public
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|suggestAccounts
argument_list|()
operator|.
name|withQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|suggestAccounts (SuggestAccountsRequest r)
specifier|private
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|suggestAccounts
parameter_list|(
name|SuggestAccountsRequest
name|r
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|QueryAccounts
name|myQueryAccounts
init|=
name|queryAccountsProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|myQueryAccounts
operator|.
name|setSuggest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|myQueryAccounts
operator|.
name|setQuery
argument_list|(
name|r
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|myQueryAccounts
operator|.
name|setLimit
argument_list|(
name|r
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|myQueryAccounts
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot retrieve suggested accounts"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|query ()
specifier|public
name|QueryRequest
name|query
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
operator|new
name|QueryRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|AccountsImpl
operator|.
name|this
operator|.
name|query
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|query (String query)
specifier|public
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|query
argument_list|()
operator|.
name|withQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|query (QueryRequest r)
specifier|private
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|query
parameter_list|(
name|QueryRequest
name|r
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|QueryAccounts
name|myQueryAccounts
init|=
name|queryAccountsProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|myQueryAccounts
operator|.
name|setQuery
argument_list|(
name|r
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|myQueryAccounts
operator|.
name|setLimit
argument_list|(
name|r
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|myQueryAccounts
operator|.
name|setStart
argument_list|(
name|r
operator|.
name|getStart
argument_list|()
argument_list|)
expr_stmt|;
name|myQueryAccounts
operator|.
name|setSuggest
argument_list|(
name|r
operator|.
name|getSuggest
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ListAccountsOption
name|option
range|:
name|r
operator|.
name|getOptions
argument_list|()
control|)
block|{
name|myQueryAccounts
operator|.
name|addOption
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
return|return
name|myQueryAccounts
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot retrieve suggested accounts"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth.oauth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
operator|.
name|oauth
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_EXTERNAL
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
name|gerrit
operator|.
name|entities
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
name|extensions
operator|.
name|auth
operator|.
name|oauth
operator|.
name|OAuthLoginProvider
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
name|auth
operator|.
name|oauth
operator|.
name|OAuthUserInfo
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
name|AccountFieldName
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
name|registration
operator|.
name|DynamicMap
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
name|AbstractRealm
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
name|config
operator|.
name|GerritServerConfig
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
name|Collection
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
name|lib
operator|.
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|OAuthRealm
specifier|public
class|class
name|OAuthRealm
extends|extends
name|AbstractRealm
block|{
DECL|field|loginProviders
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|OAuthLoginProvider
argument_list|>
name|loginProviders
decl_stmt|;
DECL|field|editableAccountFields
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountFieldName
argument_list|>
name|editableAccountFields
decl_stmt|;
annotation|@
name|Inject
DECL|method|OAuthRealm (DynamicMap<OAuthLoginProvider> loginProviders, @GerritServerConfig Config config)
name|OAuthRealm
parameter_list|(
name|DynamicMap
argument_list|<
name|OAuthLoginProvider
argument_list|>
name|loginProviders
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|loginProviders
operator|=
name|loginProviders
expr_stmt|;
name|this
operator|.
name|editableAccountFields
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
comment|// User name should be always editable, because not all OAuth providers
comment|// expose them
name|editableAccountFields
operator|.
name|add
argument_list|(
name|AccountFieldName
operator|.
name|USER_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"oauth"
argument_list|,
literal|null
argument_list|,
literal|"allowEditFullName"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|editableAccountFields
operator|.
name|add
argument_list|(
name|AccountFieldName
operator|.
name|FULL_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"oauth"
argument_list|,
literal|null
argument_list|,
literal|"allowRegisterNewEmail"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|editableAccountFields
operator|.
name|add
argument_list|(
name|AccountFieldName
operator|.
name|REGISTER_NEW_EMAIL
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|allowsEdit (AccountFieldName field)
specifier|public
name|boolean
name|allowsEdit
parameter_list|(
name|AccountFieldName
name|field
parameter_list|)
block|{
return|return
name|editableAccountFields
operator|.
name|contains
argument_list|(
name|field
argument_list|)
return|;
block|}
comment|/**    * Authenticates with the {@link OAuthLoginProvider} specified in the authentication request.    *    *<p>{@link AccountManager} calls this method without password if authenticity of the user has    * already been established. In that case we can skip the authentication request to the {@code    * OAuthLoginService}.    *    * @param who the authentication request.    * @return the authentication request with resolved email address and display name in case the    *     authenticity of the user could be established; otherwise {@code who} is returned unchanged.    * @throws AccountException if the authentication request with the OAuth2 server failed or no    *     {@code OAuthLoginProvider} was available to handle the request.    */
annotation|@
name|Override
DECL|method|authenticate (AuthRequest who)
specifier|public
name|AuthRequest
name|authenticate
parameter_list|(
name|AuthRequest
name|who
parameter_list|)
throws|throws
name|AccountException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|who
operator|.
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|who
return|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|who
operator|.
name|getAuthPlugin
argument_list|()
argument_list|)
operator|||
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|who
operator|.
name|getAuthProvider
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AccountException
argument_list|(
literal|"Cannot authenticate"
argument_list|)
throw|;
block|}
name|OAuthLoginProvider
name|loginProvider
init|=
name|loginProviders
operator|.
name|get
argument_list|(
name|who
operator|.
name|getAuthPlugin
argument_list|()
argument_list|,
name|who
operator|.
name|getAuthProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|loginProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AccountException
argument_list|(
literal|"Cannot authenticate"
argument_list|)
throw|;
block|}
name|OAuthUserInfo
name|userInfo
decl_stmt|;
try|try
block|{
name|userInfo
operator|=
name|loginProvider
operator|.
name|login
argument_list|(
name|who
operator|.
name|getUserName
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
name|who
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AccountException
argument_list|(
literal|"Cannot authenticate"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|userInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AccountException
argument_list|(
literal|"Cannot authenticate"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|userInfo
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
operator|&&
operator|(
operator|!
name|who
operator|.
name|getUserName
argument_list|()
operator|.
name|isPresent
argument_list|()
operator|||
operator|!
name|allowsEdit
argument_list|(
name|AccountFieldName
operator|.
name|REGISTER_NEW_EMAIL
argument_list|)
operator|)
condition|)
block|{
name|who
operator|.
name|setEmailAddress
argument_list|(
name|userInfo
operator|.
name|getEmailAddress
argument_list|()
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
name|userInfo
operator|.
name|getDisplayName
argument_list|()
argument_list|)
operator|&&
operator|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|who
operator|.
name|getDisplayName
argument_list|()
argument_list|)
operator|||
operator|!
name|allowsEdit
argument_list|(
name|AccountFieldName
operator|.
name|FULL_NAME
argument_list|)
operator|)
condition|)
block|{
name|who
operator|.
name|setDisplayName
argument_list|(
name|userInfo
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|who
return|;
block|}
annotation|@
name|Override
DECL|method|onCreateAccount (AuthRequest who, Account account)
specifier|public
name|void
name|onCreateAccount
parameter_list|(
name|AuthRequest
name|who
parameter_list|,
name|Account
name|account
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|lookup (String accountName)
specifier|public
name|Account
operator|.
name|Id
name|lookup
parameter_list|(
name|String
name|accountName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|accountBelongsToRealm (Collection<ExternalId> externalIds)
specifier|public
name|boolean
name|accountBelongsToRealm
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
for|for
control|(
name|ExternalId
name|id
range|:
name|externalIds
control|)
block|{
if|if
condition|(
name|id
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|SCHEME_EXTERNAL
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit


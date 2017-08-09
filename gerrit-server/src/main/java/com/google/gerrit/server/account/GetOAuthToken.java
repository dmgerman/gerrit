begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|OAuthToken
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
name|ResourceNotFoundException
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
name|RestReadView
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
name|auth
operator|.
name|oauth
operator|.
name|OAuthTokenCache
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
name|CanonicalWebUrl
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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

begin_class
annotation|@
name|Singleton
DECL|class|GetOAuthToken
class|class
name|GetOAuthToken
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|BEARER_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|BEARER_TYPE
init|=
literal|"bearer"
decl_stmt|;
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
name|GetOAuthToken
operator|.
name|class
argument_list|)
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
DECL|field|tokenCache
specifier|private
specifier|final
name|OAuthTokenCache
name|tokenCache
decl_stmt|;
DECL|field|hostName
specifier|private
specifier|final
name|String
name|hostName
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetOAuthToken ( Provider<CurrentUser> self, OAuthTokenCache tokenCache, @CanonicalWebUrl Provider<String> urlProvider)
name|GetOAuthToken
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|OAuthTokenCache
name|tokenCache
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|tokenCache
operator|=
name|tokenCache
expr_stmt|;
name|this
operator|.
name|hostName
operator|=
name|getHostName
argument_list|(
name|urlProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|OAuthTokenInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|rsrc
operator|.
name|getUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to get access token"
argument_list|)
throw|;
block|}
name|Account
name|a
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|OAuthToken
name|accessToken
init|=
name|tokenCache
operator|.
name|get
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessToken
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|OAuthTokenInfo
name|accessTokenInfo
init|=
operator|new
name|OAuthTokenInfo
argument_list|()
decl_stmt|;
name|accessTokenInfo
operator|.
name|username
operator|=
name|a
operator|.
name|getUserName
argument_list|()
expr_stmt|;
name|accessTokenInfo
operator|.
name|resourceHost
operator|=
name|hostName
expr_stmt|;
name|accessTokenInfo
operator|.
name|accessToken
operator|=
name|accessToken
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|accessTokenInfo
operator|.
name|providerId
operator|=
name|accessToken
operator|.
name|getProviderId
argument_list|()
expr_stmt|;
name|accessTokenInfo
operator|.
name|expiresAt
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|accessToken
operator|.
name|getExpiresAt
argument_list|()
argument_list|)
expr_stmt|;
name|accessTokenInfo
operator|.
name|type
operator|=
name|BEARER_TYPE
expr_stmt|;
return|return
name|accessTokenInfo
return|;
block|}
DECL|method|getHostName (String canonicalWebUrl)
specifier|private
specifier|static
name|String
name|getHostName
parameter_list|(
name|String
name|canonicalWebUrl
parameter_list|)
block|{
if|if
condition|(
name|canonicalWebUrl
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"No canonicalWebUrl defined in gerrit.config, OAuth may not work properly"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|canonicalWebUrl
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid canonicalWebUrl '"
operator|+
name|canonicalWebUrl
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|OAuthTokenInfo
specifier|public
specifier|static
class|class
name|OAuthTokenInfo
block|{
DECL|field|username
specifier|public
name|String
name|username
decl_stmt|;
DECL|field|resourceHost
specifier|public
name|String
name|resourceHost
decl_stmt|;
DECL|field|accessToken
specifier|public
name|String
name|accessToken
decl_stmt|;
DECL|field|providerId
specifier|public
name|String
name|providerId
decl_stmt|;
DECL|field|expiresAt
specifier|public
name|String
name|expiresAt
decl_stmt|;
DECL|field|type
specifier|public
name|String
name|type
decl_stmt|;
block|}
block|}
end_class

end_unit


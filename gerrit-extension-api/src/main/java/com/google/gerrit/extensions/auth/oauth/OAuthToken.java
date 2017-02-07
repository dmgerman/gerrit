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
DECL|package|com.google.gerrit.extensions.auth.oauth
package|package
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/* OAuth token */
end_comment

begin_class
DECL|class|OAuthToken
specifier|public
class|class
name|OAuthToken
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|token
specifier|private
specifier|final
name|String
name|token
decl_stmt|;
DECL|field|secret
specifier|private
specifier|final
name|String
name|secret
decl_stmt|;
DECL|field|raw
specifier|private
specifier|final
name|String
name|raw
decl_stmt|;
comment|/**    * Time of expiration of this token, or {@code Long#MAX_VALUE} if this token never expires, or    * time of expiration is unknown.    */
DECL|field|expiresAt
specifier|private
specifier|final
name|long
name|expiresAt
decl_stmt|;
comment|/**    * The identifier of the OAuth provider that issued this token in the form    *<tt>"plugin-name:provider-name"</tt>, or {@code null}.    */
DECL|field|providerId
specifier|private
specifier|final
name|String
name|providerId
decl_stmt|;
DECL|method|OAuthToken (String token, String secret, String raw)
specifier|public
name|OAuthToken
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|secret
parameter_list|,
name|String
name|raw
parameter_list|)
block|{
name|this
argument_list|(
name|token
argument_list|,
name|secret
argument_list|,
name|raw
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|OAuthToken (String token, String secret, String raw, long expiresAt, String providerId)
specifier|public
name|OAuthToken
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|secret
parameter_list|,
name|String
name|raw
parameter_list|,
name|long
name|expiresAt
parameter_list|,
name|String
name|providerId
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
name|this
operator|.
name|raw
operator|=
name|raw
expr_stmt|;
name|this
operator|.
name|expiresAt
operator|=
name|expiresAt
expr_stmt|;
name|this
operator|.
name|providerId
operator|=
name|providerId
expr_stmt|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
DECL|method|getSecret ()
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
DECL|method|getRaw ()
specifier|public
name|String
name|getRaw
parameter_list|()
block|{
return|return
name|raw
return|;
block|}
DECL|method|getExpiresAt ()
specifier|public
name|long
name|getExpiresAt
parameter_list|()
block|{
return|return
name|expiresAt
return|;
block|}
DECL|method|isExpired ()
specifier|public
name|boolean
name|isExpired
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|>
name|expiresAt
return|;
block|}
DECL|method|getProviderId ()
specifier|public
name|String
name|getProviderId
parameter_list|()
block|{
return|return
name|providerId
return|;
block|}
block|}
end_class

end_unit


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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
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
name|common
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_comment
comment|/**  * OAuth token.  *  *<p>Only implements {@link Serializable} for backwards compatibility; new extensions should not  * depend on the serialized format.  */
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
comment|/**    * The identifier of the OAuth provider that issued this token in the form {@code    * "plugin-name:provider-name"}, or {@code null}. The empty string {@code ""} is treated the same    * as {@code null}.    */
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
DECL|method|OAuthToken ( String token, String secret, String raw, long expiresAt, @Nullable String providerId)
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
annotation|@
name|Nullable
name|String
name|providerId
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|checkNotNull
argument_list|(
name|token
argument_list|,
literal|"token"
argument_list|)
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|checkNotNull
argument_list|(
name|secret
argument_list|,
literal|"secret"
argument_list|)
expr_stmt|;
name|this
operator|.
name|raw
operator|=
name|checkNotNull
argument_list|(
name|raw
argument_list|,
literal|"raw"
argument_list|)
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
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|providerId
argument_list|)
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
annotation|@
name|Nullable
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
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|OAuthToken
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|OAuthToken
name|t
init|=
operator|(
name|OAuthToken
operator|)
name|o
decl_stmt|;
return|return
name|token
operator|.
name|equals
argument_list|(
name|t
operator|.
name|token
argument_list|)
operator|&&
name|secret
operator|.
name|equals
argument_list|(
name|t
operator|.
name|secret
argument_list|)
operator|&&
name|raw
operator|.
name|equals
argument_list|(
name|t
operator|.
name|raw
argument_list|)
operator|&&
name|expiresAt
operator|==
name|t
operator|.
name|expiresAt
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|providerId
argument_list|,
name|t
operator|.
name|providerId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|token
argument_list|,
name|secret
argument_list|,
name|raw
argument_list|,
name|expiresAt
argument_list|,
name|providerId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"token"
argument_list|,
name|token
argument_list|)
operator|.
name|add
argument_list|(
literal|"secret"
argument_list|,
name|secret
argument_list|)
operator|.
name|add
argument_list|(
literal|"raw"
argument_list|,
name|raw
argument_list|)
operator|.
name|add
argument_list|(
literal|"expiresAt"
argument_list|,
name|expiresAt
argument_list|)
operator|.
name|add
argument_list|(
literal|"providerId"
argument_list|,
name|providerId
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


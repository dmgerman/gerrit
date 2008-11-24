begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

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
name|Column
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
name|IntKey
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
name|StringKey
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

begin_comment
comment|/** Preferences and information about a single user. */
end_comment

begin_class
DECL|class|Account
specifier|public
specifier|final
class|class
name|Account
block|{
comment|/** Globally unique key to identify a user. */
DECL|class|OpenId
specifier|public
specifier|static
class|class
name|OpenId
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
annotation|@
name|Column
DECL|field|openidIdentity
specifier|protected
name|String
name|openidIdentity
decl_stmt|;
DECL|method|OpenId ()
specifier|protected
name|OpenId
parameter_list|()
block|{     }
DECL|method|OpenId (final String id)
specifier|public
name|OpenId
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
name|openidIdentity
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|openidIdentity
return|;
block|}
block|}
comment|/** Key local to Gerrit to identify a user. */
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
annotation|@
name|Column
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
annotation|@
name|Column
DECL|field|accountId
specifier|protected
name|Id
name|accountId
decl_stmt|;
comment|/** Identity from the OpenID provider the user authenticates through. */
annotation|@
name|Column
DECL|field|openidIdentity
specifier|protected
name|OpenId
name|openidIdentity
decl_stmt|;
comment|/** Date and time the user registered with the review server. */
annotation|@
name|Column
DECL|field|registeredOn
specifier|protected
name|Timestamp
name|registeredOn
decl_stmt|;
comment|/** Full name of the user ("Given-name Surname" style). */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|fullName
specifier|protected
name|String
name|fullName
decl_stmt|;
comment|/** Email address the user prefers to be contacted through. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|preferredEmail
specifier|protected
name|String
name|preferredEmail
decl_stmt|;
DECL|method|Account ()
specifier|protected
name|Account
parameter_list|()
block|{   }
comment|/**    * Create a new account.    *     * @param identity identity assigned by the OpenID provider.    * @param newId unique id, see {@link ReviewDb#nextAccountId()}.    */
DECL|method|Account (final Account.OpenId identity, final Account.Id newId)
specifier|public
name|Account
parameter_list|(
specifier|final
name|Account
operator|.
name|OpenId
name|identity
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|newId
parameter_list|)
block|{
name|openidIdentity
operator|=
name|identity
expr_stmt|;
name|accountId
operator|=
name|newId
expr_stmt|;
name|registeredOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Get local id of this account, to link with in other entities */
DECL|method|getId ()
specifier|public
name|Account
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
comment|/** Get the full name of the user ("Given-name Surname" style). */
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
comment|/** Set the full name of the user ("Given-name Surname" style). */
DECL|method|setFullName (final String name)
specifier|public
name|void
name|setFullName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|fullName
operator|=
name|name
expr_stmt|;
block|}
comment|/** Email address the user prefers to be contacted through. */
DECL|method|getPreferredEmail ()
specifier|public
name|String
name|getPreferredEmail
parameter_list|()
block|{
return|return
name|preferredEmail
return|;
block|}
comment|/** Set the email address the user prefers to be contacted through. */
DECL|method|setPreferredEmail (final String addr)
specifier|public
name|void
name|setPreferredEmail
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|preferredEmail
operator|=
name|addr
expr_stmt|;
block|}
comment|/** Get the date and time the user first registered. */
DECL|method|getRegisteredOn ()
specifier|public
name|Timestamp
name|getRegisteredOn
parameter_list|()
block|{
return|return
name|registeredOn
return|;
block|}
block|}
end_class

end_unit


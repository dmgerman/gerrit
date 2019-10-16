begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Parses/writes account properties from/to a {@link Config} file.  *  *<p>This is a low-level API. Read/write of account properties in a user branch should be done  * through {@link AccountsUpdate} or {@link AccountConfig}.  *  *<p>The config file has one 'account' section with the properties of the account:  *  *<pre>  *   [account]  *     active = false  *     fullName = John Doe  *     preferredEmail = john.doe@foo.com  *     status = Overloaded with reviews  *</pre>  *  *<p>All keys are optional.  *  *<p>Not setting a key and setting a key to an empty string are treated the same way and result in  * a {@code null} value.  *  *<p>If no value for 'active' is specified, by default the account is considered as active.  *  *<p>The account is lazily parsed.  */
end_comment

begin_class
DECL|class|AccountProperties
specifier|public
class|class
name|AccountProperties
block|{
DECL|field|ACCOUNT_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|ACCOUNT_CONFIG
init|=
literal|"account.config"
decl_stmt|;
DECL|field|ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|ACCOUNT
init|=
literal|"account"
decl_stmt|;
DECL|field|KEY_ACTIVE
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ACTIVE
init|=
literal|"active"
decl_stmt|;
DECL|field|KEY_FULL_NAME
specifier|public
specifier|static
specifier|final
name|String
name|KEY_FULL_NAME
init|=
literal|"fullName"
decl_stmt|;
DECL|field|KEY_PREFERRED_EMAIL
specifier|public
specifier|static
specifier|final
name|String
name|KEY_PREFERRED_EMAIL
init|=
literal|"preferredEmail"
decl_stmt|;
DECL|field|KEY_STATUS
specifier|public
specifier|static
specifier|final
name|String
name|KEY_STATUS
init|=
literal|"status"
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|registeredOn
specifier|private
specifier|final
name|Timestamp
name|registeredOn
decl_stmt|;
DECL|field|accountConfig
specifier|private
specifier|final
name|Config
name|accountConfig
decl_stmt|;
DECL|field|metaId
specifier|private
annotation|@
name|Nullable
name|ObjectId
name|metaId
decl_stmt|;
DECL|field|account
specifier|private
name|Account
name|account
decl_stmt|;
DECL|method|AccountProperties ( Account.Id accountId, Timestamp registeredOn, Config accountConfig, @Nullable ObjectId metaId)
name|AccountProperties
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Timestamp
name|registeredOn
parameter_list|,
name|Config
name|accountConfig
parameter_list|,
annotation|@
name|Nullable
name|ObjectId
name|metaId
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|registeredOn
operator|=
name|registeredOn
expr_stmt|;
name|this
operator|.
name|accountConfig
operator|=
name|accountConfig
expr_stmt|;
name|this
operator|.
name|metaId
operator|=
name|metaId
expr_stmt|;
block|}
DECL|method|getAccount ()
name|Account
name|getAccount
parameter_list|()
block|{
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
name|parse
argument_list|()
expr_stmt|;
block|}
return|return
name|account
return|;
block|}
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
DECL|method|setMetaId (@ullable ObjectId metaId)
name|void
name|setMetaId
parameter_list|(
annotation|@
name|Nullable
name|ObjectId
name|metaId
parameter_list|)
block|{
name|this
operator|.
name|metaId
operator|=
name|metaId
expr_stmt|;
name|this
operator|.
name|account
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|parse ()
specifier|private
name|void
name|parse
parameter_list|()
block|{
name|Account
operator|.
name|Builder
name|accountBuilder
init|=
name|Account
operator|.
name|builder
argument_list|(
name|accountId
argument_list|,
name|registeredOn
argument_list|)
decl_stmt|;
name|accountBuilder
operator|.
name|setActive
argument_list|(
name|accountConfig
operator|.
name|getBoolean
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|accountBuilder
operator|.
name|setFullName
argument_list|(
name|get
argument_list|(
name|accountConfig
argument_list|,
name|KEY_FULL_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|preferredEmail
init|=
name|get
argument_list|(
name|accountConfig
argument_list|,
name|KEY_PREFERRED_EMAIL
argument_list|)
decl_stmt|;
name|accountBuilder
operator|.
name|setPreferredEmail
argument_list|(
name|preferredEmail
argument_list|)
expr_stmt|;
name|accountBuilder
operator|.
name|setStatus
argument_list|(
name|get
argument_list|(
name|accountConfig
argument_list|,
name|KEY_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|accountBuilder
operator|.
name|setMetaId
argument_list|(
name|metaId
operator|!=
literal|null
condition|?
name|metaId
operator|.
name|name
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|account
operator|=
name|accountBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|save (InternalAccountUpdate accountUpdate)
name|Config
name|save
parameter_list|(
name|InternalAccountUpdate
name|accountUpdate
parameter_list|)
block|{
name|writeToAccountConfig
argument_list|(
name|accountUpdate
argument_list|,
name|accountConfig
argument_list|)
expr_stmt|;
return|return
name|accountConfig
return|;
block|}
DECL|method|writeToAccountConfig (InternalAccountUpdate accountUpdate, Config cfg)
specifier|public
specifier|static
name|void
name|writeToAccountConfig
parameter_list|(
name|InternalAccountUpdate
name|accountUpdate
parameter_list|,
name|Config
name|cfg
parameter_list|)
block|{
name|accountUpdate
operator|.
name|getActive
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|active
lambda|->
name|setActive
argument_list|(
name|cfg
argument_list|,
name|active
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getFullName
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|fullName
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_FULL_NAME
argument_list|,
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getPreferredEmail
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|preferredEmail
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_PREFERRED_EMAIL
argument_list|,
name|preferredEmail
argument_list|)
argument_list|)
expr_stmt|;
name|accountUpdate
operator|.
name|getStatus
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|status
lambda|->
name|set
argument_list|(
name|cfg
argument_list|,
name|KEY_STATUS
argument_list|,
name|status
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Gets the given key from the given config.    *    *<p>Empty values are returned as {@code null}    *    * @param cfg the config    * @param key the key    * @return the value, {@code null} if key was not set or key was set to empty string    */
DECL|method|get (Config cfg, String key)
specifier|private
specifier|static
name|String
name|get
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Sets/Unsets {@code account.active} in the given config.    *    *<p>{@code account.active} is set to {@code false} if the account is inactive.    *    *<p>If the account is active {@code account.active} is unset since {@code true} is the default    * if this field is missing.    *    * @param cfg the config    * @param value whether the account is active    */
DECL|method|setActive (Config cfg, boolean value)
specifier|private
specifier|static
name|void
name|setActive
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|value
condition|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|KEY_ACTIVE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Sets/Unsets the given key in the given config.    *    *<p>The key unset if the value is {@code null}.    *    * @param cfg the config    * @param key the key    * @param value the value    */
DECL|method|set (Config cfg, String key, String value)
specifier|private
specifier|static
name|void
name|set
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|ACCOUNT
argument_list|,
literal|null
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


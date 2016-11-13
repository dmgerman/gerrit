begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.info
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|info
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|core
operator|.
name|client
operator|.
name|JsArray
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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
name|impl
operator|.
name|ser
operator|.
name|JavaSqlTimestamp_JsonSerializer
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

begin_class
DECL|class|AccountInfo
specifier|public
class|class
name|AccountInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|getId ()
specifier|public
specifier|final
name|Account
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|_accountId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|_accountId ()
specifier|public
specifier|final
specifier|native
name|int
name|_accountId
parameter_list|()
comment|/*-{ return this._account_id || 0; }-*/
function_decl|;
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|email ()
specifier|public
specifier|final
specifier|native
name|String
name|email
parameter_list|()
comment|/*-{ return this.email; }-*/
function_decl|;
DECL|method|secondaryEmails ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|secondaryEmails
parameter_list|()
comment|/*-{ return this.secondary_emails; }-*/
function_decl|;
DECL|method|username ()
specifier|public
specifier|final
specifier|native
name|String
name|username
parameter_list|()
comment|/*-{ return this.username; }-*/
function_decl|;
DECL|method|registeredOn ()
specifier|public
specifier|final
name|Timestamp
name|registeredOn
parameter_list|()
block|{
name|Timestamp
name|ts
init|=
name|_getRegisteredOn
argument_list|()
decl_stmt|;
if|if
condition|(
name|ts
operator|==
literal|null
condition|)
block|{
name|ts
operator|=
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|registeredOnRaw
argument_list|()
argument_list|)
expr_stmt|;
name|_setRegisteredOn
argument_list|(
name|ts
argument_list|)
expr_stmt|;
block|}
return|return
name|ts
return|;
block|}
DECL|method|registeredOnRaw ()
specifier|private
specifier|native
name|String
name|registeredOnRaw
parameter_list|()
comment|/*-{ return this.registered_on; }-*/
function_decl|;
DECL|method|_getRegisteredOn ()
specifier|private
specifier|native
name|Timestamp
name|_getRegisteredOn
parameter_list|()
comment|/*-{ return this._cts; }-*/
function_decl|;
DECL|method|_setRegisteredOn (Timestamp ts)
specifier|private
specifier|native
name|void
name|_setRegisteredOn
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
comment|/*-{ this._cts = ts; }-*/
function_decl|;
comment|/**    * @return true if the server supplied avatar information about this account. The information may    *     be an empty list, indicating no avatars are available, such as when no plugin is installed.    *     This method returns false if the server did not check on avatars for the account.    */
DECL|method|hasAvatarInfo ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasAvatarInfo
parameter_list|()
comment|/*-{ return this.hasOwnProperty('avatars') }-*/
function_decl|;
DECL|method|avatar (int sz)
specifier|public
specifier|final
name|AvatarInfo
name|avatar
parameter_list|(
name|int
name|sz
parameter_list|)
block|{
name|JsArray
argument_list|<
name|AvatarInfo
argument_list|>
name|a
init|=
name|avatars
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|a
operator|!=
literal|null
operator|&&
name|i
operator|<
name|a
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|AvatarInfo
name|r
init|=
name|a
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|height
argument_list|()
operator|==
name|sz
condition|)
block|{
return|return
name|r
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|avatars ()
specifier|private
specifier|native
name|JsArray
argument_list|<
name|AvatarInfo
argument_list|>
name|avatars
parameter_list|()
comment|/*-{ return this.avatars }-*/
function_decl|;
DECL|method|name (String n)
specifier|public
specifier|final
specifier|native
name|void
name|name
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.name = n }-*/
function_decl|;
DECL|method|email (String e)
specifier|public
specifier|final
specifier|native
name|void
name|email
parameter_list|(
name|String
name|e
parameter_list|)
comment|/*-{ this.email = e }-*/
function_decl|;
DECL|method|username (String n)
specifier|public
specifier|final
specifier|native
name|void
name|username
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.username = n }-*/
function_decl|;
DECL|method|create (int id, String name, String email, String username)
specifier|public
specifier|static
specifier|native
name|AccountInfo
name|create
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|email
parameter_list|,
name|String
name|username
parameter_list|)
comment|/*-{     return {'_account_id': id, 'name': name, 'email': email,         'username': username};   }-*/
function_decl|;
DECL|method|AccountInfo ()
specifier|protected
name|AccountInfo
parameter_list|()
block|{}
DECL|class|AvatarInfo
specifier|public
specifier|static
class|class
name|AvatarInfo
extends|extends
name|JavaScriptObject
block|{
DECL|field|DEFAULT_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SIZE
init|=
literal|26
decl_stmt|;
DECL|method|url ()
specifier|public
specifier|final
specifier|native
name|String
name|url
parameter_list|()
comment|/*-{ return this.url }-*/
function_decl|;
DECL|method|height ()
specifier|public
specifier|final
specifier|native
name|int
name|height
parameter_list|()
comment|/*-{ return this.height || 0 }-*/
function_decl|;
DECL|method|width ()
specifier|public
specifier|final
specifier|native
name|int
name|width
parameter_list|()
comment|/*-{ return this.width || 0 }-*/
function_decl|;
DECL|method|AvatarInfo ()
specifier|protected
name|AvatarInfo
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit


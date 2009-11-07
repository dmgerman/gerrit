begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountExternalId
operator|.
name|SCHEME_GERRIT
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
import|;
end_import

begin_comment
comment|/**  * Information for {@link AccountManager#authenticate(AuthRequest)}.  *<p>  * Callers should populate this object with as much information as possible  * about the user account. For example, OpenID authentication might return  * registration information including a display name for the user, and an email  * address for them. These fields however are optional, as not all OpenID  * providers return them, and not all non-OpenID systems can use them.  */
end_comment

begin_class
DECL|class|AuthRequest
specifier|public
class|class
name|AuthRequest
block|{
comment|/** Create a request for a local username, such as from LDAP. */
DECL|method|forUser (final String username)
specifier|public
specifier|static
name|AuthRequest
name|forUser
parameter_list|(
specifier|final
name|String
name|username
parameter_list|)
block|{
specifier|final
name|AuthRequest
name|r
decl_stmt|;
name|r
operator|=
operator|new
name|AuthRequest
argument_list|(
name|SCHEME_GERRIT
operator|+
name|username
argument_list|)
expr_stmt|;
name|r
operator|.
name|setSshUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/**    * Create a request for an email address registration.    *<p>    * This type of request should be used only to attach a new email address to    * an existing user account.    */
DECL|method|forEmail (final String email)
specifier|public
specifier|static
name|AuthRequest
name|forEmail
parameter_list|(
specifier|final
name|String
name|email
parameter_list|)
block|{
specifier|final
name|AuthRequest
name|r
decl_stmt|;
name|r
operator|=
operator|new
name|AuthRequest
argument_list|(
name|SCHEME_MAILTO
operator|+
name|email
argument_list|)
expr_stmt|;
name|r
operator|.
name|setEmailAddress
argument_list|(
name|email
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|field|externalId
specifier|private
specifier|final
name|String
name|externalId
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|displayName
specifier|private
name|String
name|displayName
decl_stmt|;
DECL|field|emailAddress
specifier|private
name|String
name|emailAddress
decl_stmt|;
DECL|field|sshUserName
specifier|private
name|String
name|sshUserName
decl_stmt|;
DECL|method|AuthRequest (final String externalId)
specifier|public
name|AuthRequest
parameter_list|(
specifier|final
name|String
name|externalId
parameter_list|)
block|{
name|this
operator|.
name|externalId
operator|=
name|externalId
expr_stmt|;
block|}
DECL|method|getExternalId ()
specifier|public
name|String
name|getExternalId
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
DECL|method|isScheme (final String scheme)
specifier|public
name|boolean
name|isScheme
parameter_list|(
specifier|final
name|String
name|scheme
parameter_list|)
block|{
return|return
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|scheme
argument_list|)
return|;
block|}
DECL|method|getLocalUser ()
specifier|public
name|String
name|getLocalUser
parameter_list|()
block|{
if|if
condition|(
name|isScheme
argument_list|(
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
return|return
name|getExternalId
argument_list|()
operator|.
name|substring
argument_list|(
name|SCHEME_GERRIT
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (final String pass)
specifier|public
name|void
name|setPassword
parameter_list|(
specifier|final
name|String
name|pass
parameter_list|)
block|{
name|password
operator|=
name|pass
expr_stmt|;
block|}
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|setDisplayName (final String name)
specifier|public
name|void
name|setDisplayName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|displayName
operator|=
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|name
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getEmailAddress ()
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
name|emailAddress
return|;
block|}
DECL|method|setEmailAddress (final String email)
specifier|public
name|void
name|setEmailAddress
parameter_list|(
specifier|final
name|String
name|email
parameter_list|)
block|{
name|emailAddress
operator|=
name|email
operator|!=
literal|null
operator|&&
name|email
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|email
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getSshUserName ()
specifier|public
name|String
name|getSshUserName
parameter_list|()
block|{
return|return
name|sshUserName
return|;
block|}
DECL|method|setSshUserName (final String user)
specifier|public
name|void
name|setSshUserName
parameter_list|(
specifier|final
name|String
name|user
parameter_list|)
block|{
name|sshUserName
operator|=
name|user
expr_stmt|;
block|}
block|}
end_class

end_unit


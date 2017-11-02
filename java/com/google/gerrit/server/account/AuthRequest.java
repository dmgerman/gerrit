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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_MAILTO
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

begin_comment
comment|/**  * Information for {@link AccountManager#authenticate(AuthRequest)}.  *  *<p>Callers should populate this object with as much information as possible about the user  * account. For example, OpenID authentication might return registration information including a  * display name for the user, and an email address for them. These fields however are optional, as  * not all OpenID providers return them, and not all non-OpenID systems can use them.  */
end_comment

begin_class
DECL|class|AuthRequest
specifier|public
class|class
name|AuthRequest
block|{
comment|/** Create a request for a local username, such as from LDAP. */
DECL|method|forUser (String username)
specifier|public
specifier|static
name|AuthRequest
name|forUser
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|AuthRequest
name|r
init|=
operator|new
name|AuthRequest
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_GERRIT
argument_list|,
name|username
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/** Create a request for an external username. */
DECL|method|forExternalUser (String username)
specifier|public
specifier|static
name|AuthRequest
name|forExternalUser
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|AuthRequest
name|r
init|=
operator|new
name|AuthRequest
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_EXTERNAL
argument_list|,
name|username
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/**    * Create a request for an email address registration.    *    *<p>This type of request should be used only to attach a new email address to an existing user    * account.    */
DECL|method|forEmail (String email)
specifier|public
specifier|static
name|AuthRequest
name|forEmail
parameter_list|(
name|String
name|email
parameter_list|)
block|{
name|AuthRequest
name|r
init|=
operator|new
name|AuthRequest
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_MAILTO
argument_list|,
name|email
argument_list|)
argument_list|)
decl_stmt|;
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
name|ExternalId
operator|.
name|Key
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
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
DECL|field|skipAuthentication
specifier|private
name|boolean
name|skipAuthentication
decl_stmt|;
DECL|field|authPlugin
specifier|private
name|String
name|authPlugin
decl_stmt|;
DECL|field|authProvider
specifier|private
name|String
name|authProvider
decl_stmt|;
DECL|field|authProvidesAccountActiveStatus
specifier|private
name|boolean
name|authProvidesAccountActiveStatus
decl_stmt|;
DECL|field|active
specifier|private
name|boolean
name|active
decl_stmt|;
DECL|method|AuthRequest (ExternalId.Key externalId)
specifier|public
name|AuthRequest
parameter_list|(
name|ExternalId
operator|.
name|Key
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
DECL|method|getExternalIdKey ()
specifier|public
name|ExternalId
operator|.
name|Key
name|getExternalIdKey
parameter_list|()
block|{
return|return
name|externalId
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
name|externalId
operator|.
name|isScheme
argument_list|(
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
return|return
name|externalId
operator|.
name|id
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|setLocalUser (String localUser)
specifier|public
name|void
name|setLocalUser
parameter_list|(
name|String
name|localUser
parameter_list|)
block|{
if|if
condition|(
name|externalId
operator|.
name|isScheme
argument_list|(
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
name|externalId
operator|=
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_GERRIT
argument_list|,
name|localUser
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setPassword (String pass)
specifier|public
name|void
name|setPassword
parameter_list|(
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
DECL|method|setDisplayName (String name)
specifier|public
name|void
name|setDisplayName
parameter_list|(
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
DECL|method|setEmailAddress (String email)
specifier|public
name|void
name|setEmailAddress
parameter_list|(
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
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String user)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|userName
operator|=
name|user
expr_stmt|;
block|}
DECL|method|isSkipAuthentication ()
specifier|public
name|boolean
name|isSkipAuthentication
parameter_list|()
block|{
return|return
name|skipAuthentication
return|;
block|}
DECL|method|setSkipAuthentication (boolean skip)
specifier|public
name|void
name|setSkipAuthentication
parameter_list|(
name|boolean
name|skip
parameter_list|)
block|{
name|skipAuthentication
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|getAuthPlugin ()
specifier|public
name|String
name|getAuthPlugin
parameter_list|()
block|{
return|return
name|authPlugin
return|;
block|}
DECL|method|setAuthPlugin (String authPlugin)
specifier|public
name|void
name|setAuthPlugin
parameter_list|(
name|String
name|authPlugin
parameter_list|)
block|{
name|this
operator|.
name|authPlugin
operator|=
name|authPlugin
expr_stmt|;
block|}
DECL|method|getAuthProvider ()
specifier|public
name|String
name|getAuthProvider
parameter_list|()
block|{
return|return
name|authProvider
return|;
block|}
DECL|method|setAuthProvider (String authProvider)
specifier|public
name|void
name|setAuthProvider
parameter_list|(
name|String
name|authProvider
parameter_list|)
block|{
name|this
operator|.
name|authProvider
operator|=
name|authProvider
expr_stmt|;
block|}
DECL|method|authProvidesAccountActiveStatus ()
specifier|public
name|boolean
name|authProvidesAccountActiveStatus
parameter_list|()
block|{
return|return
name|authProvidesAccountActiveStatus
return|;
block|}
DECL|method|setAuthProvidesAccountActiveStatus (boolean authProvidesAccountActiveStatus)
specifier|public
name|void
name|setAuthProvidesAccountActiveStatus
parameter_list|(
name|boolean
name|authProvidesAccountActiveStatus
parameter_list|)
block|{
name|this
operator|.
name|authProvidesAccountActiveStatus
operator|=
name|authProvidesAccountActiveStatus
expr_stmt|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
name|active
return|;
block|}
DECL|method|setActive (Boolean isActive)
specifier|public
name|void
name|setActive
parameter_list|(
name|Boolean
name|isActive
parameter_list|)
block|{
name|this
operator|.
name|active
operator|=
name|isActive
expr_stmt|;
block|}
block|}
end_class

end_unit

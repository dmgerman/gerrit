begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|auth
operator|.
name|openid
operator|.
name|OpenIdUtil
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
name|auth
operator|.
name|openid
operator|.
name|OpenIdUrls
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
name|AuthType
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

begin_class
DECL|class|ExternalIdInfo
specifier|public
class|class
name|ExternalIdInfo
extends|extends
name|JavaScriptObject
implements|implements
name|Comparable
argument_list|<
name|ExternalIdInfo
argument_list|>
block|{
comment|/**    * Scheme used for {@link AuthType#LDAP}, {@link AuthType#CLIENT_SSL_CERT_LDAP}, {@link    * AuthType#HTTP_LDAP}, and {@link AuthType#LDAP_BIND} usernames.    *    *<p>The name {@code gerrit:} was a very poor choice.    */
DECL|field|SCHEME_GERRIT
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_GERRIT
init|=
literal|"gerrit:"
decl_stmt|;
comment|/** Scheme used to represent only an email address. */
DECL|field|SCHEME_MAILTO
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_MAILTO
init|=
literal|"mailto:"
decl_stmt|;
comment|/** Scheme for the username used to authenticate an account, e.g. over SSH. */
DECL|field|SCHEME_USERNAME
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_USERNAME
init|=
literal|"username:"
decl_stmt|;
DECL|method|identity ()
specifier|public
specifier|final
specifier|native
name|String
name|identity
parameter_list|()
comment|/*-{ return this.identity; }-*/
function_decl|;
DECL|method|emailAddress ()
specifier|public
specifier|final
specifier|native
name|String
name|emailAddress
parameter_list|()
comment|/*-{ return this.email_address; }-*/
function_decl|;
DECL|method|isTrusted ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isTrusted
parameter_list|()
comment|/*-{ return this['trusted'] ? true : false; }-*/
function_decl|;
DECL|method|canDelete ()
specifier|public
specifier|final
specifier|native
name|boolean
name|canDelete
parameter_list|()
comment|/*-{ return this['can_delete'] ? true : false; }-*/
function_decl|;
DECL|method|isUsername ()
specifier|public
specifier|final
name|boolean
name|isUsername
parameter_list|()
block|{
return|return
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
return|;
block|}
DECL|method|describe ()
specifier|public
specifier|final
name|String
name|describe
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
comment|// A local user identity should just be itself.
return|return
name|getSchemeRest
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
comment|// A local user identity should just be itself.
return|return
name|getSchemeRest
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|isScheme
argument_list|(
name|SCHEME_MAILTO
argument_list|)
condition|)
block|{
comment|// Describe a mailto address as just its email address,
comment|// which is already shown in the email address field.
return|return
literal|""
return|;
block|}
elseif|else
if|if
condition|(
name|isScheme
argument_list|(
name|OpenIdUrls
operator|.
name|URL_LAUNCHPAD
argument_list|)
condition|)
block|{
return|return
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameLaunchpad
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|isScheme
argument_list|(
name|OpenIdUrls
operator|.
name|URL_YAHOO
argument_list|)
condition|)
block|{
return|return
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameYahoo
argument_list|()
return|;
block|}
return|return
name|identity
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (ExternalIdInfo a)
specifier|public
specifier|final
name|int
name|compareTo
parameter_list|(
name|ExternalIdInfo
name|a
parameter_list|)
block|{
return|return
name|emailOf
argument_list|(
name|this
argument_list|)
operator|.
name|compareTo
argument_list|(
name|emailOf
argument_list|(
name|a
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isScheme (String scheme)
specifier|private
name|boolean
name|isScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
return|return
name|identity
argument_list|()
operator|!=
literal|null
operator|&&
name|identity
argument_list|()
operator|.
name|startsWith
argument_list|(
name|scheme
argument_list|)
return|;
block|}
DECL|method|getSchemeRest ()
specifier|private
name|String
name|getSchemeRest
parameter_list|()
block|{
name|int
name|colonIdx
init|=
name|identity
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|scheme
init|=
operator|(
name|colonIdx
operator|>
literal|0
operator|)
condition|?
name|identity
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonIdx
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|scheme
operator|!=
literal|null
condition|?
name|identity
argument_list|()
operator|.
name|substring
argument_list|(
name|scheme
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|emailOf (ExternalIdInfo a)
specifier|private
name|String
name|emailOf
parameter_list|(
name|ExternalIdInfo
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|emailAddress
argument_list|()
operator|!=
literal|null
condition|?
name|a
operator|.
name|emailAddress
argument_list|()
else|:
literal|""
return|;
block|}
DECL|method|ExternalIdInfo ()
specifier|protected
name|ExternalIdInfo
parameter_list|()
block|{}
block|}
end_class

end_unit


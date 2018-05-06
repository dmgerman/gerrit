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
name|client
operator|.
name|rpc
operator|.
name|Natives
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
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|GitBasicAuthPolicy
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|AuthInfo
specifier|public
class|class
name|AuthInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|authType ()
specifier|public
specifier|final
name|AuthType
name|authType
parameter_list|()
block|{
return|return
name|AuthType
operator|.
name|valueOf
argument_list|(
name|authTypeRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isLdap ()
specifier|public
specifier|final
name|boolean
name|isLdap
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|LDAP
operator|||
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|LDAP_BIND
return|;
block|}
DECL|method|isOpenId ()
specifier|public
specifier|final
name|boolean
name|isOpenId
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|OPENID
return|;
block|}
DECL|method|isOAuth ()
specifier|public
specifier|final
name|boolean
name|isOAuth
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|OAUTH
return|;
block|}
DECL|method|isDev ()
specifier|public
specifier|final
name|boolean
name|isDev
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
return|;
block|}
DECL|method|isClientSslCertLdap ()
specifier|public
specifier|final
name|boolean
name|isClientSslCertLdap
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|CLIENT_SSL_CERT_LDAP
return|;
block|}
DECL|method|isCustomExtension ()
specifier|public
specifier|final
name|boolean
name|isCustomExtension
parameter_list|()
block|{
return|return
name|authType
argument_list|()
operator|==
name|AuthType
operator|.
name|CUSTOM_EXTENSION
return|;
block|}
DECL|method|canEdit (AccountFieldName f)
specifier|public
specifier|final
name|boolean
name|canEdit
parameter_list|(
name|AccountFieldName
name|f
parameter_list|)
block|{
return|return
name|editableAccountFields
argument_list|()
operator|.
name|contains
argument_list|(
name|f
argument_list|)
return|;
block|}
DECL|method|editableAccountFields ()
specifier|public
specifier|final
name|List
argument_list|<
name|AccountFieldName
argument_list|>
name|editableAccountFields
parameter_list|()
block|{
name|List
argument_list|<
name|AccountFieldName
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|f
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|_editableAccountFields
argument_list|()
argument_list|)
control|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|AccountFieldName
operator|.
name|valueOf
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|fields
return|;
block|}
DECL|method|contributorAgreements ()
specifier|public
specifier|final
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|contributorAgreements
parameter_list|()
block|{
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|agreements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|JsArray
argument_list|<
name|AgreementInfo
argument_list|>
name|contributorAgreements
init|=
name|_contributorAgreements
argument_list|()
decl_stmt|;
if|if
condition|(
name|contributorAgreements
operator|!=
literal|null
condition|)
block|{
name|agreements
operator|.
name|addAll
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|contributorAgreements
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|agreements
return|;
block|}
DECL|method|siteHasUsernames ()
specifier|public
specifier|final
name|boolean
name|siteHasUsernames
parameter_list|()
block|{
if|if
condition|(
name|isCustomExtension
argument_list|()
operator|&&
name|httpPasswordUrl
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|canEdit
argument_list|(
name|AccountFieldName
operator|.
name|USER_NAME
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isHttpPasswordSettingsEnabled ()
specifier|public
specifier|final
name|boolean
name|isHttpPasswordSettingsEnabled
parameter_list|()
block|{
return|return
name|gitBasicAuthPolicy
argument_list|()
operator|==
name|GitBasicAuthPolicy
operator|.
name|HTTP
operator|||
name|gitBasicAuthPolicy
argument_list|()
operator|==
name|GitBasicAuthPolicy
operator|.
name|HTTP_LDAP
return|;
block|}
DECL|method|gitBasicAuthPolicy ()
specifier|public
specifier|final
name|GitBasicAuthPolicy
name|gitBasicAuthPolicy
parameter_list|()
block|{
return|return
name|GitBasicAuthPolicy
operator|.
name|valueOf
argument_list|(
name|gitBasicAuthPolicyRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|useContributorAgreements ()
specifier|public
specifier|final
specifier|native
name|boolean
name|useContributorAgreements
parameter_list|()
comment|/*-{ return this.use_contributor_agreements || false; }-*/
function_decl|;
DECL|method|loginUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|loginUrl
parameter_list|()
comment|/*-{ return this.login_url; }-*/
function_decl|;
DECL|method|loginText ()
specifier|public
specifier|final
specifier|native
name|String
name|loginText
parameter_list|()
comment|/*-{ return this.login_text; }-*/
function_decl|;
DECL|method|switchAccountUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|switchAccountUrl
parameter_list|()
comment|/*-{ return this.switch_account_url; }-*/
function_decl|;
DECL|method|registerUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|registerUrl
parameter_list|()
comment|/*-{ return this.register_url; }-*/
function_decl|;
DECL|method|registerText ()
specifier|public
specifier|final
specifier|native
name|String
name|registerText
parameter_list|()
comment|/*-{ return this.register_text; }-*/
function_decl|;
DECL|method|editFullNameUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|editFullNameUrl
parameter_list|()
comment|/*-{ return this.edit_full_name_url; }-*/
function_decl|;
DECL|method|httpPasswordUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|httpPasswordUrl
parameter_list|()
comment|/*-{ return this.http_password_url; }-*/
function_decl|;
DECL|method|gitBasicAuthPolicyRaw ()
specifier|private
specifier|native
name|String
name|gitBasicAuthPolicyRaw
parameter_list|()
comment|/*-{ return this.git_basic_auth_policy; }-*/
function_decl|;
DECL|method|authTypeRaw ()
specifier|private
specifier|native
name|String
name|authTypeRaw
parameter_list|()
comment|/*-{ return this.auth_type; }-*/
function_decl|;
DECL|method|_editableAccountFields ()
specifier|private
specifier|native
name|JsArrayString
name|_editableAccountFields
parameter_list|()
comment|/*-{ return this.editable_account_fields; }-*/
function_decl|;
DECL|method|_contributorAgreements ()
specifier|private
specifier|native
name|JsArray
argument_list|<
name|AgreementInfo
argument_list|>
name|_contributorAgreements
parameter_list|()
comment|/*-{ return this.contributor_agreements; }-*/
function_decl|;
DECL|method|AuthInfo ()
specifier|protected
name|AuthInfo
parameter_list|()
block|{}
block|}
end_class

end_unit


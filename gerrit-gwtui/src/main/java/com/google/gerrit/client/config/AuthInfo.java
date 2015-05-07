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
DECL|package|com.google.gerrit.client.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|config
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
name|reviewdb
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
DECL|method|canEdit (Account.FieldName f)
specifier|public
specifier|final
name|boolean
name|canEdit
parameter_list|(
name|Account
operator|.
name|FieldName
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
name|Account
operator|.
name|FieldName
argument_list|>
name|editableAccountFields
parameter_list|()
block|{
name|List
argument_list|<
name|Account
operator|.
name|FieldName
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
name|Account
operator|.
name|FieldName
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
DECL|method|useContributorAgreements ()
specifier|public
specifier|final
specifier|native
name|boolean
name|useContributorAgreements
parameter_list|()
comment|/*-{ return this.use_contributor_agreements || false; }-*/
function_decl|;
DECL|method|authTypeRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|authTypeRaw
parameter_list|()
comment|/*-{ return this.auth_type; }-*/
function_decl|;
DECL|method|_editableAccountFields ()
specifier|private
specifier|final
specifier|native
name|JsArrayString
name|_editableAccountFields
parameter_list|()
comment|/*-{ return this.editable_account_fields; }-*/
function_decl|;
DECL|method|AuthInfo ()
specifier|protected
name|AuthInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


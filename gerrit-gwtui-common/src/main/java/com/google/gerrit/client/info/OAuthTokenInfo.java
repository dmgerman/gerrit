begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|class|OAuthTokenInfo
specifier|public
class|class
name|OAuthTokenInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|OAuthTokenInfo ()
specifier|protected
name|OAuthTokenInfo
parameter_list|()
block|{   }
DECL|method|username ()
specifier|public
specifier|final
specifier|native
name|String
name|username
parameter_list|()
comment|/*-{ return this.username; }-*/
function_decl|;
DECL|method|resourceHost ()
specifier|public
specifier|final
specifier|native
name|String
name|resourceHost
parameter_list|()
comment|/*-{ return this.resource_host; }-*/
function_decl|;
DECL|method|accessToken ()
specifier|public
specifier|final
specifier|native
name|String
name|accessToken
parameter_list|()
comment|/*-{ return this.access_token; }-*/
function_decl|;
DECL|method|providerId ()
specifier|public
specifier|final
specifier|native
name|String
name|providerId
parameter_list|()
comment|/*-{ return this.provider_id; }-*/
function_decl|;
DECL|method|expiresAt ()
specifier|public
specifier|final
specifier|native
name|String
name|expiresAt
parameter_list|()
comment|/*-{ return this.expires_at; }-*/
function_decl|;
DECL|method|type ()
specifier|public
specifier|final
specifier|native
name|String
name|type
parameter_list|()
comment|/*-{ return this.type; }-*/
function_decl|;
block|}
end_class

end_unit


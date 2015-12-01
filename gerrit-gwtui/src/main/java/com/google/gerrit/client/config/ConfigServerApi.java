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
name|VoidResult
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
name|client
operator|.
name|info
operator|.
name|AccountPreferencesInfo
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
name|client
operator|.
name|info
operator|.
name|ServerInfo
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
name|client
operator|.
name|info
operator|.
name|TopMenuList
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
name|client
operator|.
name|rpc
operator|.
name|NativeMap
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
name|client
operator|.
name|rpc
operator|.
name|RestApi
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
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * A collection of static methods which work on the Gerrit REST API for server  * configuration.  */
end_comment

begin_class
DECL|class|ConfigServerApi
specifier|public
class|class
name|ConfigServerApi
block|{
comment|/** map of the server wide capabilities (core& plugins). */
DECL|method|capabilities (AsyncCallback<NativeMap<CapabilityInfo>> cb)
specifier|public
specifier|static
name|void
name|capabilities
parameter_list|(
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|CapabilityInfo
argument_list|>
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"config/server/capabilities"
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|topMenus (AsyncCallback<TopMenuList> cb)
specifier|public
specifier|static
name|void
name|topMenus
parameter_list|(
name|AsyncCallback
argument_list|<
name|TopMenuList
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"config/server/top-menus"
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|defaultPreferences (AsyncCallback<AccountPreferencesInfo> cb)
specifier|public
specifier|static
name|void
name|defaultPreferences
parameter_list|(
name|AsyncCallback
argument_list|<
name|AccountPreferencesInfo
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"config/server/preferences"
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|serverInfo (AsyncCallback<ServerInfo> cb)
specifier|public
specifier|static
name|void
name|serverInfo
parameter_list|(
name|AsyncCallback
argument_list|<
name|ServerInfo
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"config/server/info"
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|confirmEmail (String token, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|confirmEmail
parameter_list|(
name|String
name|token
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|EmailConfirmationInput
name|input
init|=
name|EmailConfirmationInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
operator|new
name|RestApi
argument_list|(
literal|"config/server/email.confirm"
argument_list|)
operator|.
name|put
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|class|EmailConfirmationInput
specifier|private
specifier|static
class|class
name|EmailConfirmationInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|setToken (String token)
specifier|final
specifier|native
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
comment|/*-{ this.token = token; }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|EmailConfirmationInput
name|create
parameter_list|()
block|{
return|return
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
return|;
block|}
DECL|method|EmailConfirmationInput ()
specifier|protected
name|EmailConfirmationInput
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit


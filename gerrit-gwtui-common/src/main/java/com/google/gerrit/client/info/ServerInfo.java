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
name|NativeString
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
name|Natives
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ServerInfo
specifier|public
class|class
name|ServerInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|auth ()
specifier|public
specifier|final
specifier|native
name|AuthInfo
name|auth
parameter_list|()
comment|/*-{ return this.auth; }-*/
function_decl|;
DECL|method|change ()
specifier|public
specifier|final
specifier|native
name|ChangeConfigInfo
name|change
parameter_list|()
comment|/*-{ return this.change; }-*/
function_decl|;
DECL|method|download ()
specifier|public
specifier|final
specifier|native
name|DownloadInfo
name|download
parameter_list|()
comment|/*-{ return this.download; }-*/
function_decl|;
DECL|method|gerrit ()
specifier|public
specifier|final
specifier|native
name|GerritInfo
name|gerrit
parameter_list|()
comment|/*-{ return this.gerrit; }-*/
function_decl|;
DECL|method|plugin ()
specifier|public
specifier|final
specifier|native
name|PluginConfigInfo
name|plugin
parameter_list|()
comment|/*-{ return this.plugin; }-*/
function_decl|;
DECL|method|sshd ()
specifier|public
specifier|final
specifier|native
name|SshdInfo
name|sshd
parameter_list|()
comment|/*-{ return this.sshd; }-*/
function_decl|;
DECL|method|suggest ()
specifier|public
specifier|final
specifier|native
name|SuggestInfo
name|suggest
parameter_list|()
comment|/*-{ return this.suggest; }-*/
function_decl|;
DECL|method|user ()
specifier|public
specifier|final
specifier|native
name|UserConfigInfo
name|user
parameter_list|()
comment|/*-{ return this.user; }-*/
function_decl|;
DECL|method|receive ()
specifier|public
specifier|final
specifier|native
name|ReceiveInfo
name|receive
parameter_list|()
comment|/*-{ return this.receive; }-*/
function_decl|;
DECL|method|urlAliases ()
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|urlAliases
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|k
range|:
name|Natives
operator|.
name|keys
argument_list|(
name|_urlAliases
argument_list|()
argument_list|)
control|)
block|{
name|urlAliases
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|urlAliasToken
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|urlAliases
return|;
block|}
DECL|method|urlAliasToken (String n)
specifier|public
specifier|final
specifier|native
name|String
name|urlAliasToken
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.url_aliases[n]; }-*/
function_decl|;
DECL|method|_urlAliases ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|_urlAliases
parameter_list|()
comment|/*-{ return this.url_aliases; }-*/
function_decl|;
DECL|method|hasSshd ()
specifier|public
specifier|final
name|boolean
name|hasSshd
parameter_list|()
block|{
return|return
name|sshd
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|method|ServerInfo ()
specifier|protected
name|ServerInfo
parameter_list|()
block|{}
DECL|class|ChangeConfigInfo
specifier|public
specifier|static
class|class
name|ChangeConfigInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|allowBlame ()
specifier|public
specifier|final
specifier|native
name|boolean
name|allowBlame
parameter_list|()
comment|/*-{ return this.allow_blame || false; }-*/
function_decl|;
DECL|method|largeChange ()
specifier|public
specifier|final
specifier|native
name|int
name|largeChange
parameter_list|()
comment|/*-{ return this.large_change || 0; }-*/
function_decl|;
DECL|method|replyLabel ()
specifier|public
specifier|final
specifier|native
name|String
name|replyLabel
parameter_list|()
comment|/*-{ return this.reply_label; }-*/
function_decl|;
DECL|method|replyTooltip ()
specifier|public
specifier|final
specifier|native
name|String
name|replyTooltip
parameter_list|()
comment|/*-{ return this.reply_tooltip; }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|showAssigneeInChangesTable ()
name|showAssigneeInChangesTable
parameter_list|()
comment|/*-{ return this.show_assignee_in_changes_table || false; }-*/
function_decl|;
DECL|method|updateDelay ()
specifier|public
specifier|final
specifier|native
name|int
name|updateDelay
parameter_list|()
comment|/*-{ return this.update_delay || 0; }-*/
function_decl|;
DECL|method|isSubmitWholeTopicEnabled ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isSubmitWholeTopicEnabled
parameter_list|()
comment|/*-{         return this.submit_whole_topic; }-*/
function_decl|;
DECL|method|ChangeConfigInfo ()
specifier|protected
name|ChangeConfigInfo
parameter_list|()
block|{}
block|}
DECL|class|PluginConfigInfo
specifier|public
specifier|static
class|class
name|PluginConfigInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|hasAvatars ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasAvatars
parameter_list|()
comment|/*-{ return this.has_avatars || false; }-*/
function_decl|;
DECL|method|jsResourcePaths ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|jsResourcePaths
parameter_list|()
comment|/*-{         return this.js_resource_paths || []; }-*/
function_decl|;
DECL|method|PluginConfigInfo ()
specifier|protected
name|PluginConfigInfo
parameter_list|()
block|{}
block|}
DECL|class|SshdInfo
specifier|public
specifier|static
class|class
name|SshdInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|SshdInfo ()
specifier|protected
name|SshdInfo
parameter_list|()
block|{}
block|}
DECL|class|SuggestInfo
specifier|public
specifier|static
class|class
name|SuggestInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|from ()
specifier|public
specifier|final
specifier|native
name|int
name|from
parameter_list|()
comment|/*-{ return this.from || 0; }-*/
function_decl|;
DECL|method|SuggestInfo ()
specifier|protected
name|SuggestInfo
parameter_list|()
block|{}
block|}
DECL|class|UserConfigInfo
specifier|public
specifier|static
class|class
name|UserConfigInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|anonymousCowardName ()
specifier|public
specifier|final
specifier|native
name|String
name|anonymousCowardName
parameter_list|()
comment|/*-{ return this.anonymous_coward_name; }-*/
function_decl|;
DECL|method|UserConfigInfo ()
specifier|protected
name|UserConfigInfo
parameter_list|()
block|{}
block|}
DECL|class|ReceiveInfo
specifier|public
specifier|static
class|class
name|ReceiveInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|enableSignedPush ()
specifier|public
specifier|final
specifier|native
name|boolean
name|enableSignedPush
parameter_list|()
comment|/*-{ return this.enable_signed_push || false; }-*/
function_decl|;
DECL|method|ReceiveInfo ()
specifier|protected
name|ReceiveInfo
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit


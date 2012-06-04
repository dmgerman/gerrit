begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|json
operator|.
name|client
operator|.
name|JSONObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|Natives
specifier|public
class|class
name|Natives
block|{
comment|/**    * Get the names of defined properties on the object. The returned set    * iterates in the native iteration order, which may match the source order.    */
DECL|method|keys (JavaScriptObject obj)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|(
name|JavaScriptObject
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|JSONObject
argument_list|(
name|obj
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|parseJSON (String json)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|T
name|parseJSON
parameter_list|(
name|String
name|json
parameter_list|)
block|{
if|if
condition|(
name|parser
operator|==
literal|null
condition|)
block|{
name|parser
operator|=
name|bestJsonParser
argument_list|()
expr_stmt|;
block|}
comment|// javac generics bug
return|return
name|Natives
operator|.
expr|<
name|T
operator|>
name|parse0
argument_list|(
name|parser
argument_list|,
name|json
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|native
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
DECL|method|parse0 (JavaScriptObject p, String s)
name|T
name|parse0
parameter_list|(
name|JavaScriptObject
name|p
parameter_list|,
name|String
name|s
parameter_list|)
comment|/*-{ return p(s); }-*/
function_decl|;
DECL|field|parser
specifier|private
specifier|static
name|JavaScriptObject
name|parser
decl_stmt|;
DECL|method|bestJsonParser ()
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|bestJsonParser
parameter_list|()
comment|/*-{     if ($wnd.JSON&& typeof $wnd.JSON.parse === 'function')       return $wnd.JSON.parse;     return function(s) { return eval('(' + s + ')'); };   }-*/
function_decl|;
DECL|method|Natives ()
specifier|private
name|Natives
parameter_list|()
block|{   }
block|}
end_class

end_unit


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
name|AbstractList
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
name|List
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
DECL|method|asList ( final JsArray<T> arr)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|asList
parameter_list|(
specifier|final
name|JsArray
argument_list|<
name|T
argument_list|>
name|arr
parameter_list|)
block|{
if|if
condition|(
name|arr
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|AbstractList
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|T
name|element
parameter_list|)
block|{
name|T
name|old
init|=
name|arr
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|arr
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
name|old
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|arr
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|arr
operator|.
name|length
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|arrayOf (T element)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|JsArray
argument_list|<
name|T
argument_list|>
name|arrayOf
parameter_list|(
name|T
name|element
parameter_list|)
block|{
name|JsArray
argument_list|<
name|T
argument_list|>
name|arr
init|=
name|JavaScriptObject
operator|.
name|createArray
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|arr
operator|.
name|push
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|arr
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|json
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|NativeString
operator|.
name|wrap
argument_list|(
name|parseString
argument_list|(
name|parser
argument_list|,
name|json
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Natives
operator|.
expr|<
name|T
operator|>
name|parseObject
argument_list|(
name|parser
argument_list|,
name|json
argument_list|)
return|;
comment|// javac generics bug
block|}
specifier|private
specifier|static
specifier|native
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
DECL|method|parseObject (JavaScriptObject p, String s)
name|T
name|parseObject
parameter_list|(
name|JavaScriptObject
name|p
parameter_list|,
name|String
name|s
parameter_list|)
comment|/*-{ return p(s); }-*/
function_decl|;
specifier|private
specifier|static
specifier|native
DECL|method|parseString (JavaScriptObject p, String s)
name|String
name|parseString
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
static|static
block|{
name|parser
operator|=
name|bestJsonParser
argument_list|()
expr_stmt|;
block|}
DECL|method|Natives ()
specifier|private
name|Natives
parameter_list|()
block|{   }
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.plugin.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|plugin
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
name|http
operator|.
name|client
operator|.
name|URL
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

begin_class
DECL|class|RestApi
specifier|public
class|class
name|RestApi
block|{
DECL|field|path
specifier|private
specifier|final
name|StringBuilder
name|path
decl_stmt|;
DECL|field|hasQueryParams
specifier|private
name|boolean
name|hasQueryParams
decl_stmt|;
DECL|method|RestApi (String name)
specifier|public
name|RestApi
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|path
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|view (String name)
specifier|public
name|RestApi
name|view
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|view (String pluginName, String name)
specifier|public
name|RestApi
name|view
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|pluginName
operator|+
literal|"~"
operator|+
name|name
argument_list|)
return|;
block|}
DECL|method|id (String id)
specifier|public
name|RestApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|URL
operator|.
name|encodePathSegment
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|id (int id)
specifier|public
name|RestApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|idRaw (String name)
specifier|public
name|RestApi
name|idRaw
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|hasQueryParams
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|charAt
argument_list|(
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
name|path
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addParameter (String name, String value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, String... value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
modifier|...
name|value
parameter_list|)
block|{
for|for
control|(
name|String
name|val
range|:
name|value
control|)
block|{
name|addParameter
argument_list|(
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|addParameterTrue (String name)
specifier|public
name|RestApi
name|addParameterTrue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, boolean value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|value
condition|?
literal|"t"
else|:
literal|"f"
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, int value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, Enum<?> value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Enum
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addParameterRaw (String name, String value)
specifier|public
name|RestApi
name|addParameterRaw
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|hasQueryParams
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|hasQueryParams
operator|=
literal|true
expr_stmt|;
block|}
name|path
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|path ()
specifier|public
name|String
name|path
parameter_list|()
block|{
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|get (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|get
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|get
argument_list|(
name|path
argument_list|()
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getString (AsyncCallback<String> cb)
specifier|public
name|void
name|getString
parameter_list|(
name|AsyncCallback
argument_list|<
name|String
argument_list|>
name|cb
parameter_list|)
block|{
name|get
argument_list|(
name|NativeString
operator|.
name|unwrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|get (String p, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|get
parameter_list|(
name|String
name|p
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.get_raw(p, r) }-*/
function_decl|;
DECL|method|put (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|put
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|put
argument_list|(
name|path
argument_list|()
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|put (String p, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|put
parameter_list|(
name|String
name|p
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.put_raw(p, r) }-*/
function_decl|;
DECL|method|put (String content, AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|put
parameter_list|(
name|String
name|content
parameter_list|,
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|put
argument_list|(
name|path
argument_list|()
argument_list|,
name|content
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|put (String p, String c, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|put
parameter_list|(
name|String
name|p
parameter_list|,
name|String
name|c
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.put_raw(p, c, r) }-*/
function_decl|;
DECL|method|put (JavaScriptObject content, AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|put
parameter_list|(
name|JavaScriptObject
name|content
parameter_list|,
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|put
argument_list|(
name|path
argument_list|()
argument_list|,
name|content
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|put (String p, JavaScriptObject c, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|put
parameter_list|(
name|String
name|p
parameter_list|,
name|JavaScriptObject
name|c
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.put_raw(p, c, r) }-*/
function_decl|;
DECL|method|post (String content, AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|post
parameter_list|(
name|String
name|content
parameter_list|,
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|post
argument_list|(
name|path
argument_list|()
argument_list|,
name|content
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|post (String p, String c, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|post
parameter_list|(
name|String
name|p
parameter_list|,
name|String
name|c
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.post_raw(p, c, r) }-*/
function_decl|;
DECL|method|post (JavaScriptObject content, AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|post
parameter_list|(
name|JavaScriptObject
name|content
parameter_list|,
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|post
argument_list|(
name|path
argument_list|()
argument_list|,
name|content
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|post (String p, JavaScriptObject c, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|post
parameter_list|(
name|String
name|p
parameter_list|,
name|JavaScriptObject
name|c
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.post_raw(p, c, r) }-*/
function_decl|;
DECL|method|delete (AsyncCallback<NoContent> cb)
specifier|public
name|void
name|delete
parameter_list|(
name|AsyncCallback
argument_list|<
name|NoContent
argument_list|>
name|cb
parameter_list|)
block|{
name|delete
argument_list|(
name|path
argument_list|()
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (String p, JavaScriptObject r)
specifier|private
specifier|static
specifier|native
name|void
name|delete
parameter_list|(
name|String
name|p
parameter_list|,
name|JavaScriptObject
name|r
parameter_list|)
comment|/*-{ $wnd.Gerrit.del_raw(p, r) }-*/
function_decl|;
DECL|method|wrap ( AsyncCallback<T> b)
specifier|private
specifier|static
specifier|native
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|JavaScriptObject
name|wrap
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|b
parameter_list|)
comment|/*-{     return function(r) {       b.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(r)     }   }-*/
function_decl|;
block|}
end_class

end_unit


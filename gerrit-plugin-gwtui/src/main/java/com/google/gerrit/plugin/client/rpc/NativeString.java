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
comment|/** Wraps a String that was returned from a JSON API. */
end_comment

begin_class
DECL|class|NativeString
specifier|public
class|class
name|NativeString
extends|extends
name|JavaScriptObject
block|{
DECL|field|TYPE
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|TYPE
init|=
name|init
argument_list|()
decl_stmt|;
DECL|method|init ()
specifier|private
specifier|static
specifier|final
specifier|native
name|JavaScriptObject
name|init
parameter_list|()
comment|/*-{ return $wnd.Gerrit.JsonString } }-*/
function_decl|;
DECL|method|asString ()
specifier|public
specifier|final
specifier|native
name|String
name|asString
parameter_list|()
comment|/*-{ return this.get(); }-*/
function_decl|;
specifier|public
specifier|static
specifier|final
DECL|method|unwrap (final AsyncCallback<String> cb)
name|AsyncCallback
argument_list|<
name|NativeString
argument_list|>
name|unwrap
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|String
argument_list|>
name|cb
parameter_list|)
block|{
return|return
operator|new
name|AsyncCallback
argument_list|<
name|NativeString
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeString
name|result
parameter_list|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
operator|!=
literal|null
condition|?
name|result
operator|.
name|asString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|is (JavaScriptObject o)
specifier|public
specifier|static
specifier|final
name|boolean
name|is
parameter_list|(
name|JavaScriptObject
name|o
parameter_list|)
block|{
return|return
name|is
argument_list|(
name|TYPE
argument_list|,
name|o
argument_list|)
return|;
block|}
DECL|method|is (JavaScriptObject T, JavaScriptObject o)
specifier|private
specifier|static
specifier|final
specifier|native
name|boolean
name|is
parameter_list|(
name|JavaScriptObject
name|T
parameter_list|,
name|JavaScriptObject
name|o
parameter_list|)
comment|/*-{ return o instanceof T }-*/
function_decl|;
DECL|method|NativeString ()
specifier|protected
name|NativeString
parameter_list|()
block|{   }
block|}
end_class

end_unit


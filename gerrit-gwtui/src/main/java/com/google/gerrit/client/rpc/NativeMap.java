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
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
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

begin_comment
comment|/** A map of native JSON objects, keyed by a string. */
end_comment

begin_class
DECL|class|NativeMap
specifier|public
class|class
name|NativeMap
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
extends|extends
name|JavaScriptObject
block|{
comment|/**    * Loop through the result map's entries and copy the key strings into the    * "name" property of the corresponding child object. This only runs on the    * top level map of the result, and requires the children to be JSON objects    * and not a JSON primitive (e.g. boolean or string).    */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|,
DECL|method|copyKeysIntoChildren ( AsyncCallback<M> callback)
name|M
extends|extends
name|NativeMap
argument_list|<
name|T
argument_list|>
parameter_list|>
name|AsyncCallback
argument_list|<
name|M
argument_list|>
name|copyKeysIntoChildren
parameter_list|(
name|AsyncCallback
argument_list|<
name|M
argument_list|>
name|callback
parameter_list|)
block|{
return|return
name|copyKeysIntoChildren
argument_list|(
literal|"name"
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/** Loop through the result map and set asProperty on the children. */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|,
DECL|method|copyKeysIntoChildren ( final String asProperty, AsyncCallback<M> callback)
name|M
extends|extends
name|NativeMap
argument_list|<
name|T
argument_list|>
parameter_list|>
name|AsyncCallback
argument_list|<
name|M
argument_list|>
name|copyKeysIntoChildren
parameter_list|(
specifier|final
name|String
name|asProperty
parameter_list|,
name|AsyncCallback
argument_list|<
name|M
argument_list|>
name|callback
parameter_list|)
block|{
return|return
operator|new
name|TransformCallback
argument_list|<
name|M
argument_list|,
name|M
argument_list|>
argument_list|(
name|callback
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|M
name|transform
parameter_list|(
name|M
name|result
parameter_list|)
block|{
name|result
operator|.
name|copyKeysIntoChildren
argument_list|(
name|asProperty
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
return|;
block|}
DECL|method|NativeMap ()
specifier|protected
name|NativeMap
parameter_list|()
block|{   }
DECL|method|keySet ()
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|Natives
operator|.
name|keys
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|values ()
specifier|public
specifier|final
specifier|native
name|NativeList
argument_list|<
name|T
argument_list|>
name|values
parameter_list|()
comment|/*-{     var s = this;     var v = [];     var i = 0;     for (var k in s) {       if (s.hasOwnProperty(k)) {         v[i++] = s[k];       }     }     return v;   }-*/
function_decl|;
DECL|method|size ()
specifier|public
specifier|final
name|int
name|size
parameter_list|()
block|{
return|return
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|isEmpty ()
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
DECL|method|containsKey (String n)
specifier|public
specifier|final
name|boolean
name|containsKey
parameter_list|(
name|String
name|n
parameter_list|)
block|{
return|return
name|get
argument_list|(
name|n
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|get (String n)
specifier|public
specifier|final
specifier|native
name|T
name|get
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this[n]; }-*/
function_decl|;
DECL|method|copyKeysIntoChildren (String p)
specifier|public
specifier|final
specifier|native
name|void
name|copyKeysIntoChildren
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{     var s = this;     for (var k in s) {       if (s.hasOwnProperty(k)) {         var c = s[k];         c[p] = k;       }     }   }-*/
function_decl|;
block|}
end_class

end_unit


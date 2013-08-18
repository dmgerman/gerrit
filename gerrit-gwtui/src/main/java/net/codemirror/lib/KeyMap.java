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
DECL|package|net.codemirror.lib
package|package
name|net
operator|.
name|codemirror
operator|.
name|lib
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

begin_comment
comment|/** Object that associates a key or key combination with a handler. */
end_comment

begin_class
DECL|class|KeyMap
specifier|public
class|class
name|KeyMap
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|KeyMap
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
DECL|method|on (String key, Runnable thunk)
specifier|public
specifier|final
specifier|native
name|KeyMap
name|on
parameter_list|(
name|String
name|key
parameter_list|,
name|Runnable
name|thunk
parameter_list|)
comment|/*-{     this[key] = function() { $entry(thunk.@java.lang.Runnable::run()()); };     return this;   }-*/
function_decl|;
DECL|method|remove (String key)
specifier|public
specifier|final
specifier|native
name|KeyMap
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
comment|/*-{ delete this[key]; }-*/
function_decl|;
DECL|method|KeyMap ()
specifier|protected
name|KeyMap
parameter_list|()
block|{   }
block|}
end_class

end_unit


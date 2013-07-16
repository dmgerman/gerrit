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
comment|/**  * Simple map-like structure to pass configuration to CodeMirror.  *  * @link http://codemirror.net/doc/manual.html#config  * @see CodeMirror#create(com.google.gwt.dom.client.Element, Configuration)  */
end_comment

begin_class
DECL|class|Configuration
specifier|public
class|class
name|Configuration
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|Configuration
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
DECL|method|set (String name, String val)
specifier|public
specifier|final
specifier|native
name|Configuration
name|set
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
comment|/*-{ this[name] = val; return this; }-*/
function_decl|;
DECL|method|set (String name, int val)
specifier|public
specifier|final
specifier|native
name|Configuration
name|set
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|val
parameter_list|)
comment|/*-{ this[name] = val; return this; }-*/
function_decl|;
DECL|method|set (String name, boolean val)
specifier|public
specifier|final
specifier|native
name|Configuration
name|set
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|val
parameter_list|)
comment|/*-{ this[name] = val; return this; }-*/
function_decl|;
DECL|method|set (String name, JavaScriptObject val)
specifier|public
specifier|final
specifier|native
name|Configuration
name|set
parameter_list|(
name|String
name|name
parameter_list|,
name|JavaScriptObject
name|val
parameter_list|)
comment|/*-{ this[name] = val; return this; }-*/
function_decl|;
DECL|method|setInfinity (String name)
specifier|public
specifier|final
specifier|native
name|Configuration
name|setInfinity
parameter_list|(
name|String
name|name
parameter_list|)
comment|/*-{ this[name] = Infinity; return this; }-*/
function_decl|;
DECL|method|Configuration ()
specifier|protected
name|Configuration
parameter_list|()
block|{   }
block|}
end_class

end_unit


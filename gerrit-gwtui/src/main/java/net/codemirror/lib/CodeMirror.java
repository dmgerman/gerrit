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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|dom
operator|.
name|client
operator|.
name|Element
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
comment|/**  * Glue to connect CodeMirror to be callable from GWT.  *  * @link http://codemirror.net/doc/manual.html#api  */
end_comment

begin_class
DECL|class|CodeMirror
specifier|public
class|class
name|CodeMirror
extends|extends
name|JavaScriptObject
block|{
DECL|method|initLibrary (AsyncCallback<Void> cb)
specifier|public
specifier|static
name|void
name|initLibrary
parameter_list|(
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|cb
parameter_list|)
block|{
name|Loader
operator|.
name|initLibrary
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|create (Element parent, Configuration cfg)
specifier|public
specifier|static
specifier|native
name|CodeMirror
name|create
parameter_list|(
name|Element
name|parent
parameter_list|,
name|Configuration
name|cfg
parameter_list|)
comment|/*-{     return $wnd.CodeMirror(parent, cfg);   }-*/
function_decl|;
DECL|method|setValue (String v)
specifier|public
specifier|final
specifier|native
name|void
name|setValue
parameter_list|(
name|String
name|v
parameter_list|)
comment|/*-{ this.setValue(v); }-*/
function_decl|;
DECL|method|setWidth (int w)
specifier|public
specifier|final
specifier|native
name|void
name|setWidth
parameter_list|(
name|int
name|w
parameter_list|)
comment|/*-{ this.setSize(w, null); }-*/
function_decl|;
DECL|method|setWidth (String w)
specifier|public
specifier|final
specifier|native
name|void
name|setWidth
parameter_list|(
name|String
name|w
parameter_list|)
comment|/*-{ this.setSize(w, null); }-*/
function_decl|;
DECL|method|setHeight (int h)
specifier|public
specifier|final
specifier|native
name|void
name|setHeight
parameter_list|(
name|int
name|h
parameter_list|)
comment|/*-{ this.setSize(null, h); }-*/
function_decl|;
DECL|method|setHeight (String h)
specifier|public
specifier|final
specifier|native
name|void
name|setHeight
parameter_list|(
name|String
name|h
parameter_list|)
comment|/*-{ this.setSize(null, h); }-*/
function_decl|;
DECL|method|refresh ()
specifier|public
specifier|final
specifier|native
name|void
name|refresh
parameter_list|()
comment|/*-{ this.refresh(); }-*/
function_decl|;
DECL|method|getWrapperElement ()
specifier|public
specifier|final
specifier|native
name|Element
name|getWrapperElement
parameter_list|()
comment|/*-{ return this.getWrapperElement(); }-*/
function_decl|;
DECL|method|CodeMirror ()
specifier|protected
name|CodeMirror
parameter_list|()
block|{   }
block|}
end_class

end_unit


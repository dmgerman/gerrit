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
comment|/** Returned by {@link CodeMirror#getScrollInfo()}. */
end_comment

begin_class
DECL|class|ScrollInfo
specifier|public
class|class
name|ScrollInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|getLeft ()
specifier|public
specifier|final
specifier|native
name|double
name|getLeft
parameter_list|()
comment|/*-{ return this.left; }-*/
function_decl|;
DECL|method|getTop ()
specifier|public
specifier|final
specifier|native
name|double
name|getTop
parameter_list|()
comment|/*-{ return this.top; }-*/
function_decl|;
comment|/**    * Pixel height of the full content being scrolled. This may only be an    * estimate given by CodeMirror. Line widgets further down in the document may    * not be measured, so line heights can be incorrect until drawn.    */
DECL|method|getHeight ()
specifier|public
specifier|final
specifier|native
name|double
name|getHeight
parameter_list|()
comment|/*-{ return this.height; }-*/
function_decl|;
DECL|method|getWidth ()
specifier|public
specifier|final
specifier|native
name|double
name|getWidth
parameter_list|()
comment|/*-{ return this.width; }-*/
function_decl|;
comment|/** Visible height of the viewport, excluding scrollbars. */
DECL|method|getClientHeight ()
specifier|public
specifier|final
specifier|native
name|double
name|getClientHeight
parameter_list|()
comment|/*-{ return this.clientHeight; }-*/
function_decl|;
DECL|method|getClientWidth ()
specifier|public
specifier|final
specifier|native
name|double
name|getClientWidth
parameter_list|()
comment|/*-{ return this.clientWidth; }-*/
function_decl|;
DECL|method|ScrollInfo ()
specifier|protected
name|ScrollInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


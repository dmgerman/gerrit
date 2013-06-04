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
comment|/** {left, top, width, height, clientWidth, clientHeight} objects returned by  * getScrollInfo(). */
end_comment

begin_class
DECL|class|ScrollInfo
specifier|public
class|class
name|ScrollInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|ScrollInfo
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
DECL|method|getLeft ()
specifier|public
specifier|final
specifier|native
name|int
name|getLeft
parameter_list|()
comment|/*-{ return this.left; }-*/
function_decl|;
DECL|method|getTop ()
specifier|public
specifier|final
specifier|native
name|int
name|getTop
parameter_list|()
comment|/*-{ return this.top; }-*/
function_decl|;
DECL|method|getWidth ()
specifier|public
specifier|final
specifier|native
name|int
name|getWidth
parameter_list|()
comment|/*-{ return this.width; }-*/
function_decl|;
DECL|method|getHeight ()
specifier|public
specifier|final
specifier|native
name|int
name|getHeight
parameter_list|()
comment|/*-{ return this.height; }-*/
function_decl|;
DECL|method|getClientWidth ()
specifier|public
specifier|final
specifier|native
name|int
name|getClientWidth
parameter_list|()
comment|/*-{ return this.clientWidth; }-*/
function_decl|;
DECL|method|getClientHeight ()
specifier|public
specifier|final
specifier|native
name|int
name|getClientHeight
parameter_list|()
comment|/*-{ return this.clientHeight; }-*/
function_decl|;
DECL|method|ScrollInfo ()
specifier|protected
name|ScrollInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


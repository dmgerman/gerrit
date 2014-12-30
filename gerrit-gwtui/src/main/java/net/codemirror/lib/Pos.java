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
comment|/** Pos (or {line, ch}) objects used within CodeMirror. */
end_comment

begin_class
DECL|class|Pos
specifier|public
class|class
name|Pos
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (int line)
specifier|public
specifier|static
specifier|final
specifier|native
name|Pos
name|create
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{     return $wnd.CodeMirror.Pos(line)   }-*/
function_decl|;
DECL|method|create (int line, int ch)
specifier|public
specifier|static
specifier|final
specifier|native
name|Pos
name|create
parameter_list|(
name|int
name|line
parameter_list|,
name|int
name|ch
parameter_list|)
comment|/*-{     return $wnd.CodeMirror.Pos(line, ch)   }-*/
function_decl|;
DECL|method|line (int l)
specifier|public
specifier|final
specifier|native
name|void
name|line
parameter_list|(
name|int
name|l
parameter_list|)
comment|/*-{ this.line = l }-*/
function_decl|;
DECL|method|ch (int c)
specifier|public
specifier|final
specifier|native
name|void
name|ch
parameter_list|(
name|int
name|c
parameter_list|)
comment|/*-{ this.ch = c }-*/
function_decl|;
DECL|method|line ()
specifier|public
specifier|final
specifier|native
name|int
name|line
parameter_list|()
comment|/*-{ return this.line }-*/
function_decl|;
DECL|method|ch ()
specifier|public
specifier|final
specifier|native
name|int
name|ch
parameter_list|()
comment|/*-{ return this.ch || 0 }-*/
function_decl|;
DECL|method|equals (Pos o)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
name|Pos
name|o
parameter_list|)
block|{
return|return
name|this
operator|==
name|o
operator|||
operator|(
name|line
argument_list|()
operator|==
name|o
operator|.
name|line
argument_list|()
operator|&&
name|ch
argument_list|()
operator|==
name|o
operator|.
name|ch
argument_list|()
operator|)
return|;
block|}
DECL|method|Pos ()
specifier|protected
name|Pos
parameter_list|()
block|{   }
block|}
end_class

end_unit


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
comment|/** {line, ch} objects used within CodeMirror. */
end_comment

begin_class
DECL|class|LineCharacter
specifier|public
class|class
name|LineCharacter
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (int line, int ch)
specifier|public
specifier|static
name|LineCharacter
name|create
parameter_list|(
name|int
name|line
parameter_list|,
name|int
name|ch
parameter_list|)
block|{
return|return
name|createImpl
argument_list|(
name|line
argument_list|,
name|ch
argument_list|)
return|;
block|}
DECL|method|create (int line)
specifier|public
specifier|static
name|LineCharacter
name|create
parameter_list|(
name|int
name|line
parameter_list|)
block|{
return|return
name|createImpl
argument_list|(
name|line
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|createImpl (int line, int ch)
specifier|private
specifier|static
name|LineCharacter
name|createImpl
parameter_list|(
name|int
name|line
parameter_list|,
name|int
name|ch
parameter_list|)
block|{
name|LineCharacter
name|lineCh
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|lineCh
operator|.
name|setLine
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|lineCh
operator|.
name|setCh
argument_list|(
name|ch
argument_list|)
expr_stmt|;
return|return
name|lineCh
return|;
block|}
DECL|method|setLine (int line)
specifier|public
specifier|final
specifier|native
name|void
name|setLine
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{ this.line = line; }-*/
function_decl|;
DECL|method|setCh (int ch)
specifier|public
specifier|final
specifier|native
name|void
name|setCh
parameter_list|(
name|int
name|ch
parameter_list|)
comment|/*-{ this.ch = ch; }-*/
function_decl|;
DECL|method|getLine ()
specifier|public
specifier|final
specifier|native
name|int
name|getLine
parameter_list|()
comment|/*-{ return this.line; }-*/
function_decl|;
DECL|method|getCh ()
specifier|public
specifier|final
specifier|native
name|int
name|getCh
parameter_list|()
comment|/*-{ return this.ch; }-*/
function_decl|;
DECL|method|LineCharacter ()
specifier|protected
name|LineCharacter
parameter_list|()
block|{   }
block|}
end_class

end_unit


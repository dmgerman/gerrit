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
comment|/** The Doc object representing the content in a CodeMirror */
end_comment

begin_class
DECL|class|CodeMirrorDoc
specifier|public
class|class
name|CodeMirrorDoc
extends|extends
name|JavaScriptObject
block|{
DECL|method|replaceRange (String replacement, LineCharacter from, LineCharacter to)
specifier|public
specifier|final
specifier|native
name|void
name|replaceRange
parameter_list|(
name|String
name|replacement
parameter_list|,
name|LineCharacter
name|from
parameter_list|,
name|LineCharacter
name|to
parameter_list|)
comment|/*-{     this.replaceRange(replacement, from, to);   }-*/
function_decl|;
DECL|method|insertText (String insertion, LineCharacter at)
specifier|public
specifier|final
specifier|native
name|void
name|insertText
parameter_list|(
name|String
name|insertion
parameter_list|,
name|LineCharacter
name|at
parameter_list|)
comment|/*-{     this.replaceRange(insertion, at);   }-*/
function_decl|;
DECL|method|CodeMirrorDoc ()
specifier|protected
name|CodeMirrorDoc
parameter_list|()
block|{   }
block|}
end_class

end_unit


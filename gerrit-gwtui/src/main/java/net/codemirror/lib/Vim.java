begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
comment|/**  * Glue around the Vim emulation for {@link CodeMirror}.  *  * As an instance {@code this} is actually the {@link CodeMirror} object. Class  * Vim is providing a new namespace for Vim related methods that are associated  * with an editor.  */
end_comment

begin_class
DECL|class|Vim
specifier|public
class|class
name|Vim
extends|extends
name|JavaScriptObject
block|{
DECL|method|initKeyMap ()
specifier|static
name|void
name|initKeyMap
parameter_list|()
block|{
name|KeyMap
name|km
init|=
name|KeyMap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"C"
block|,
literal|"I"
block|,
literal|"O"
block|,
literal|"R"
block|,
literal|"U"
block|}
control|)
block|{
name|km
operator|.
name|propagate
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|km
operator|.
name|propagate
argument_list|(
literal|"'"
operator|+
name|key
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
operator|new
name|String
index|[]
block|{
literal|"Ctrl-C"
block|,
literal|"Ctrl-O"
block|,
literal|"Ctrl-P"
block|,
literal|"Ctrl-S"
block|,
literal|"Ctrl-F"
block|,
literal|"Ctrl-B"
block|,
literal|"Ctrl-R"
block|}
control|)
block|{
name|km
operator|.
name|propagate
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|9
condition|;
name|i
operator|++
control|)
block|{
name|km
operator|.
name|propagate
argument_list|(
literal|"Ctrl-"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|km
operator|.
name|fallthrough
argument_list|(
name|CodeMirror
operator|.
name|getKeyMap
argument_list|(
literal|"vim"
argument_list|)
argument_list|)
expr_stmt|;
name|CodeMirror
operator|.
name|addKeyMap
argument_list|(
literal|"vim_ro"
argument_list|,
name|km
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"j"
argument_list|,
literal|"gj"
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"k"
argument_list|,
literal|"gk"
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"Down"
argument_list|,
literal|"gj"
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"Up"
argument_list|,
literal|"gk"
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"<PageUp>"
argument_list|,
literal|"<C-u>"
argument_list|)
expr_stmt|;
name|mapKey
argument_list|(
literal|"<PageDown>"
argument_list|,
literal|"<C-d>"
argument_list|)
expr_stmt|;
block|}
DECL|method|mapKey (String alias, String actual)
specifier|public
specifier|static
specifier|final
specifier|native
name|void
name|mapKey
parameter_list|(
name|String
name|alias
parameter_list|,
name|String
name|actual
parameter_list|)
comment|/*-{     $wnd.CodeMirror.Vim.map(alias, actual)   }-*/
function_decl|;
DECL|method|handleKey (String key)
specifier|public
specifier|final
specifier|native
name|void
name|handleKey
parameter_list|(
name|String
name|key
parameter_list|)
comment|/*-{     $wnd.CodeMirror.Vim.handleKey(this, key)   }-*/
function_decl|;
DECL|method|hasSearchHighlight ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasSearchHighlight
parameter_list|()
comment|/*-{     var v = this.state.vim;     return v&& v.searchState_&& !!v.searchState_.getOverlay();   }-*/
function_decl|;
DECL|method|Vim ()
specifier|protected
name|Vim
parameter_list|()
block|{   }
block|}
end_class

end_unit


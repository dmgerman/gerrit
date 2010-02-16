begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|common
operator|.
name|PrettyFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|common
operator|.
name|PrettyFormatter
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
name|resources
operator|.
name|client
operator|.
name|TextResource
import|;
end_import

begin_comment
comment|/** Evaluates prettify using the host browser's JavaScript engine. */
end_comment

begin_class
DECL|class|ClientSideFormatter
specifier|public
class|class
name|ClientSideFormatter
extends|extends
name|PrettyFormatter
block|{
DECL|field|FACTORY
specifier|public
specifier|static
specifier|final
name|PrettyFactory
name|FACTORY
init|=
operator|new
name|PrettyFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|PrettyFormatter
name|get
parameter_list|()
block|{
return|return
operator|new
name|ClientSideFormatter
argument_list|()
return|;
block|}
block|}
decl_stmt|;
static|static
block|{
name|Resources
operator|.
name|I
operator|.
name|prettify_css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
name|Resources
operator|.
name|I
operator|.
name|gerrit_css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|core
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_css
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_hs
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_lisp
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_lua
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_ml
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_proto
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_sql
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_vb
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|lang_wiki
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|compile (TextResource core)
specifier|private
specifier|static
name|void
name|compile
parameter_list|(
name|TextResource
name|core
parameter_list|)
block|{
name|eval
argument_list|(
name|core
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|eval (String js)
specifier|private
specifier|static
specifier|native
name|void
name|eval
parameter_list|(
name|String
name|js
parameter_list|)
comment|/*-{ eval(js); }-*/
function_decl|;
annotation|@
name|Override
DECL|method|prettify (String html, String type)
specifier|protected
name|String
name|prettify
parameter_list|(
name|String
name|html
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|go
argument_list|(
name|html
argument_list|,
name|type
argument_list|,
name|settings
operator|.
name|getTabSize
argument_list|()
argument_list|)
return|;
block|}
DECL|method|go (String srcText, String srcType, int tabSize)
specifier|private
specifier|static
specifier|native
name|String
name|go
parameter_list|(
name|String
name|srcText
parameter_list|,
name|String
name|srcType
parameter_list|,
name|int
name|tabSize
parameter_list|)
comment|/*-{      window['PR_TAB_WIDTH'] = tabSize;      return window.prettyPrintOne(srcText, srcType);   }-*/
function_decl|;
block|}
end_class

end_unit


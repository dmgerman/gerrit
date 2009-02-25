begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|client
operator|.
name|ui
operator|.
name|FancyFlexTable
operator|.
name|MyFlexTable
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
name|DOM
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
name|ui
operator|.
name|HTMLTable
import|;
end_import

begin_class
DECL|class|FancyFlexTableImplIE6
specifier|public
class|class
name|FancyFlexTableImplIE6
extends|extends
name|FancyFlexTableImpl
block|{
annotation|@
name|Override
DECL|method|resetHtml (final MyFlexTable myTable, final String bodyHtml)
specifier|public
name|void
name|resetHtml
parameter_list|(
specifier|final
name|MyFlexTable
name|myTable
parameter_list|,
specifier|final
name|String
name|bodyHtml
parameter_list|)
block|{
specifier|final
name|Element
name|oldBody
init|=
name|getBodyElement
argument_list|(
name|myTable
argument_list|)
decl_stmt|;
specifier|final
name|Element
name|newBody
init|=
name|parseBody
argument_list|(
name|bodyHtml
argument_list|)
decl_stmt|;
assert|assert
name|newBody
operator|!=
literal|null
assert|;
specifier|final
name|Element
name|tableElem
init|=
name|DOM
operator|.
name|getParent
argument_list|(
name|oldBody
argument_list|)
decl_stmt|;
name|DOM
operator|.
name|removeChild
argument_list|(
name|tableElem
argument_list|,
name|oldBody
argument_list|)
expr_stmt|;
name|setBodyElement
argument_list|(
name|myTable
argument_list|,
name|newBody
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|tableElem
argument_list|,
name|newBody
argument_list|)
expr_stmt|;
block|}
DECL|method|parseBody (final String body)
specifier|private
specifier|static
name|Element
name|parseBody
parameter_list|(
specifier|final
name|String
name|body
parameter_list|)
block|{
specifier|final
name|Element
name|div
init|=
name|DOM
operator|.
name|createDiv
argument_list|()
decl_stmt|;
name|DOM
operator|.
name|setInnerHTML
argument_list|(
name|div
argument_list|,
literal|"<table>"
operator|+
name|body
operator|+
literal|"</table>"
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|newTable
init|=
name|DOM
operator|.
name|getChild
argument_list|(
name|div
argument_list|,
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|e
init|=
name|DOM
operator|.
name|getFirstChild
argument_list|(
name|newTable
argument_list|)
init|;
name|e
operator|!=
literal|null
condition|;
name|e
operator|=
name|DOM
operator|.
name|getNextSibling
argument_list|(
name|e
argument_list|)
control|)
block|{
if|if
condition|(
literal|"tbody"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getTagName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|setBodyElement (HTMLTable myTable, Element newBody)
specifier|private
specifier|static
specifier|native
name|void
name|setBodyElement
parameter_list|(
name|HTMLTable
name|myTable
parameter_list|,
name|Element
name|newBody
parameter_list|)
comment|/*-{ myTable.@com.google.gwt.user.client.ui.HTMLTable::bodyElem = newBody; }-*/
function_decl|;
block|}
end_class

end_unit


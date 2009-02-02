begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Command
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
name|History
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
name|MenuItem
import|;
end_import

begin_comment
comment|/**  * A GWT {@link MenuItem} that uses a normal HTML link widget for its UI.  *<p>  * Using this widget instead of MenuItem permits the menu item to have the  * standard right-click "Open in new window" and "Open in new tab" feature found  * in popular browsers.  */
end_comment

begin_class
DECL|class|LinkMenuItem
specifier|public
class|class
name|LinkMenuItem
extends|extends
name|MenuItem
block|{
comment|/**    * Creates a hyperlink with its text and target history token specified.    *     * @param text the hyperlink's text    * @param targetHistoryToken the history token to which it will link    */
DECL|method|LinkMenuItem (final String text, final String targetHistoryToken)
specifier|public
name|LinkMenuItem
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|String
name|targetHistoryToken
parameter_list|)
block|{
name|super
argument_list|(
literal|""
argument_list|,
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|History
operator|.
name|newItem
argument_list|(
name|targetHistoryToken
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|a
init|=
name|DOM
operator|.
name|createAnchor
argument_list|()
decl_stmt|;
name|DOM
operator|.
name|setElementProperty
argument_list|(
name|a
argument_list|,
literal|"href"
argument_list|,
literal|"#"
operator|+
name|targetHistoryToken
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|setInnerText
argument_list|(
name|a
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


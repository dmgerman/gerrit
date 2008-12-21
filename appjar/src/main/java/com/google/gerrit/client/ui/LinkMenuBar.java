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
name|ui
operator|.
name|MenuBar
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
comment|/** A GWT MenuBar, rendering its items as through they were normal links. */
end_comment

begin_class
DECL|class|LinkMenuBar
specifier|public
class|class
name|LinkMenuBar
extends|extends
name|MenuBar
block|{
DECL|method|LinkMenuBar ()
specifier|public
name|LinkMenuBar
parameter_list|()
block|{
name|setStyleName
argument_list|(
literal|"gerrit-LinkMenuBar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addItem (final MenuItem item)
specifier|public
name|MenuItem
name|addItem
parameter_list|(
specifier|final
name|MenuItem
name|item
parameter_list|)
block|{
name|item
operator|.
name|addStyleDependentName
argument_list|(
literal|"NormalItem"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|addItem
argument_list|(
name|item
argument_list|)
return|;
block|}
comment|/**    * Add a cell to fill the screen width.    *<p>    * The glue has 100% width, forcing the browser to align out the next element    * as far right as possible. If there is exactly 1 glue in the menu bar, the    * bar is split into a left and right section. If there are 2 glues, the bar    * will be split into thirds.    */
DECL|method|addGlue ()
specifier|public
name|void
name|addGlue
parameter_list|()
block|{
name|addSeparator
argument_list|()
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-FillMenuCenter"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Mark this item as the last in its group, so it has no border.    *<p>    * Usually this is used just before {@link #addGlue()} and after the last item    * has been added.    */
DECL|method|lastInGroup ()
specifier|public
name|void
name|lastInGroup
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getItems
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|MenuItem
name|i
init|=
name|getItems
argument_list|()
operator|.
name|get
argument_list|(
name|getItems
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|i
operator|.
name|removeStyleDependentName
argument_list|(
literal|"NormalItem"
argument_list|)
expr_stmt|;
name|i
operator|.
name|addStyleDependentName
argument_list|(
literal|"LastItem"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


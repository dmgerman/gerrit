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
DECL|package|com.google.gwtexpui.user.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
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
name|Event
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
name|FocusPanel
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
name|Widget
import|;
end_import

begin_class
DECL|class|FocusPanelFactorySafari
class|class
name|FocusPanelFactorySafari
extends|extends
name|FocusPanelFactory
block|{
annotation|@
name|Override
DECL|method|wrapFocusPanel (final Widget child)
name|FocusPanel
name|wrapFocusPanel
parameter_list|(
specifier|final
name|Widget
name|child
parameter_list|)
block|{
if|if
condition|(
operator|(
name|DOM
operator|.
name|getEventsSunk
argument_list|(
name|child
operator|.
name|getElement
argument_list|()
argument_list|)
operator|&
operator|(
name|Event
operator|.
name|ONDBLCLICK
operator||
name|Event
operator|.
name|ONCLICK
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|FocusPanel
argument_list|(
name|child
argument_list|)
return|;
block|}
block|}
end_class

end_unit


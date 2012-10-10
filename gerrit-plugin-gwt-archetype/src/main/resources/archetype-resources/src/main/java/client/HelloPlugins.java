begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 Google Inc
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
package|package
name|$
block|{
package|package
block|}
end_package

begin_expr_stmt
operator|.
name|client
expr_stmt|;
end_expr_stmt

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
name|Plugin
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickHandler
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
name|Button
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
name|DialogBox
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
name|Image
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
name|RootPanel
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
name|VerticalPanel
import|;
end_import

begin_comment
comment|/**  * HelloWorld Plugins.  */
end_comment

begin_class
DECL|class|HelloPlugins
specifier|public
class|class
name|HelloPlugins
extends|extends
name|Plugin
block|{
annotation|@
name|Override
DECL|method|onModuleLoad ()
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{
name|Image
name|img
init|=
operator|new
name|Image
argument_list|(
literal|"http://code.google.com/webtoolkit/logo-185x175.png"
argument_list|)
decl_stmt|;
name|Button
name|button
init|=
operator|new
name|Button
argument_list|(
literal|"Click me"
argument_list|)
decl_stmt|;
name|VerticalPanel
name|vPanel
init|=
operator|new
name|VerticalPanel
argument_list|()
decl_stmt|;
name|vPanel
operator|.
name|setWidth
argument_list|(
literal|"100%"
argument_list|)
expr_stmt|;
name|vPanel
operator|.
name|setHorizontalAlignment
argument_list|(
name|VerticalPanel
operator|.
name|ALIGN_CENTER
argument_list|)
expr_stmt|;
name|vPanel
operator|.
name|add
argument_list|(
name|img
argument_list|)
expr_stmt|;
name|vPanel
operator|.
name|add
argument_list|(
name|button
argument_list|)
expr_stmt|;
name|RootPanel
operator|.
name|get
argument_list|()
operator|.
name|add
argument_list|(
name|vPanel
argument_list|)
expr_stmt|;
comment|// Create the dialog box
specifier|final
name|DialogBox
name|dialogBox
init|=
operator|new
name|DialogBox
argument_list|()
decl_stmt|;
comment|// The content of the dialog comes from a User specified Preference
name|dialogBox
operator|.
name|setText
argument_list|(
literal|"Hello from GWT Gerrit UI plugin"
argument_list|)
expr_stmt|;
name|dialogBox
operator|.
name|setAnimationEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Button
name|closeButton
init|=
operator|new
name|Button
argument_list|(
literal|"Close"
argument_list|)
decl_stmt|;
name|VerticalPanel
name|dialogVPanel
init|=
operator|new
name|VerticalPanel
argument_list|()
decl_stmt|;
name|dialogVPanel
operator|.
name|setWidth
argument_list|(
literal|"100%"
argument_list|)
expr_stmt|;
name|dialogVPanel
operator|.
name|setHorizontalAlignment
argument_list|(
name|VerticalPanel
operator|.
name|ALIGN_CENTER
argument_list|)
expr_stmt|;
name|dialogVPanel
operator|.
name|add
argument_list|(
name|closeButton
argument_list|)
expr_stmt|;
name|closeButton
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|dialogBox
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Set the contents of the Widget
name|dialogBox
operator|.
name|setWidget
argument_list|(
name|dialogVPanel
argument_list|)
expr_stmt|;
name|button
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|dialogBox
operator|.
name|center
argument_list|()
expr_stmt|;
name|dialogBox
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


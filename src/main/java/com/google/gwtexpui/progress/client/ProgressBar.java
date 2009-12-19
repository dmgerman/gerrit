begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.progress.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|progress
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
name|ui
operator|.
name|Composite
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
name|FlowPanel
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
name|Label
import|;
end_import

begin_comment
comment|/**  * A simple progress bar with a text label.  *<p>  * The bar is 200 pixels wide and 20 pixels high. To keep the implementation  * simple and lightweight this dimensions are fixed and shouldn't be modified by  * style overrides in client code or CSS.  */
end_comment

begin_class
DECL|class|ProgressBar
specifier|public
class|class
name|ProgressBar
extends|extends
name|Composite
block|{
static|static
block|{
name|ProgressResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
DECL|field|callerText
specifier|private
specifier|final
name|String
name|callerText
decl_stmt|;
DECL|field|bar
specifier|private
specifier|final
name|Label
name|bar
decl_stmt|;
DECL|field|msg
specifier|private
specifier|final
name|Label
name|msg
decl_stmt|;
DECL|field|value
specifier|private
name|int
name|value
decl_stmt|;
comment|/** Create a bar with no message text. */
DECL|method|ProgressBar ()
specifier|public
name|ProgressBar
parameter_list|()
block|{
name|this
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/** Create a bar displaying the specified message. */
DECL|method|ProgressBar (final String text)
specifier|public
name|ProgressBar
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|callerText
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|callerText
operator|=
name|text
operator|+
literal|" "
expr_stmt|;
block|}
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|body
operator|.
name|setStyleName
argument_list|(
name|ProgressResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|container
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|=
operator|new
name|Label
argument_list|(
name|callerText
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setStyleName
argument_list|(
name|ProgressResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|text
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|bar
operator|=
operator|new
name|Label
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setStyleName
argument_list|(
name|ProgressResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|bar
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
comment|/** @return the current value of the progress meter. */
DECL|method|getValue ()
specifier|public
name|int
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/** Update the bar's percent completion. */
DECL|method|setValue (final int pComplete)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|int
name|pComplete
parameter_list|)
block|{
assert|assert
literal|0
operator|<=
name|pComplete
operator|&&
name|pComplete
operator|<=
literal|100
assert|;
name|value
operator|=
name|pComplete
expr_stmt|;
name|bar
operator|.
name|setWidth
argument_list|(
literal|""
operator|+
operator|(
literal|2
operator|*
name|pComplete
operator|)
operator|+
literal|"px"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
name|callerText
operator|+
name|pComplete
operator|+
literal|"%"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


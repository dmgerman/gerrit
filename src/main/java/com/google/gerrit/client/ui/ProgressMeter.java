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

begin_class
DECL|class|ProgressMeter
specifier|public
class|class
name|ProgressMeter
extends|extends
name|Composite
block|{
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
DECL|method|ProgressMeter ()
specifier|public
name|ProgressMeter
parameter_list|()
block|{
name|this
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|ProgressMeter (final String text)
specifier|public
name|ProgressMeter
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
literal|"gerrit-ProgressMeter"
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
literal|"gerrit-ProgressMeterText"
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
literal|"gerrit-ProgressMeterBar"
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
DECL|method|setValue (final int v)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|int
name|v
parameter_list|)
block|{
name|bar
operator|.
name|setWidth
argument_list|(
literal|""
operator|+
operator|(
literal|2
operator|*
name|v
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
name|v
operator|+
literal|"%"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


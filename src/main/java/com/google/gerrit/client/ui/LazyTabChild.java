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
name|SimplePanel
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

begin_comment
comment|/** Panel for use within a TabPanel to create the widget only on selection. */
end_comment

begin_class
DECL|class|LazyTabChild
specifier|public
specifier|abstract
class|class
name|LazyTabChild
parameter_list|<
name|T
extends|extends
name|Widget
parameter_list|>
extends|extends
name|SimplePanel
block|{
comment|/** @return the widget to display in this panel. */
DECL|method|create ()
specifier|protected
specifier|abstract
name|T
name|create
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|setVisible (final boolean visible)
specifier|public
name|void
name|setVisible
parameter_list|(
specifier|final
name|boolean
name|visible
parameter_list|)
block|{
if|if
condition|(
name|visible
operator|&&
name|getWidget
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setWidget
argument_list|(
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setVisible
argument_list|(
name|visible
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


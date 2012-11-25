begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|HidingMenuBar
specifier|public
class|class
name|HidingMenuBar
extends|extends
name|LinkMenuBar
block|{
DECL|field|hideableItems
name|Set
argument_list|<
name|LinkMenuItem
argument_list|>
name|hideableItems
init|=
operator|new
name|HashSet
argument_list|<
name|LinkMenuItem
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|addHideableItem (final LinkMenuItem item)
specifier|public
name|void
name|addHideableItem
parameter_list|(
specifier|final
name|LinkMenuItem
name|item
parameter_list|)
block|{
name|hideableItems
operator|.
name|add
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|addItem
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
DECL|method|setHideableItemsVisible (boolean visible)
specifier|public
name|void
name|setHideableItemsVisible
parameter_list|(
name|boolean
name|visible
parameter_list|)
block|{
for|for
control|(
name|LinkMenuItem
name|item
range|:
name|hideableItems
control|)
block|{
name|item
operator|.
name|setVisible
argument_list|(
name|visible
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


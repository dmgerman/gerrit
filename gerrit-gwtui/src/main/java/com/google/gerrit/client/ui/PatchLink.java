begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|Dispatcher
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
name|reviewdb
operator|.
name|client
operator|.
name|Patch
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_class
DECL|class|PatchLink
specifier|public
class|class
name|PatchLink
extends|extends
name|InlineHyperlink
block|{
DECL|method|PatchLink (String text, String historyToken)
specifier|private
name|PatchLink
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|historyToken
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|historyToken
argument_list|)
expr_stmt|;
block|}
DECL|class|SideBySide
specifier|public
specifier|static
class|class
name|SideBySide
extends|extends
name|PatchLink
block|{
DECL|method|SideBySide (String text, PatchSet.Id base, Patch.Key id)
specifier|public
name|SideBySide
parameter_list|(
name|String
name|text
parameter_list|,
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|Dispatcher
operator|.
name|toSideBySide
argument_list|(
name|base
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Unified
specifier|public
specifier|static
class|class
name|Unified
extends|extends
name|PatchLink
block|{
DECL|method|Unified (String text, PatchSet.Id base, Patch.Key id)
specifier|public
name|Unified
parameter_list|(
name|String
name|text
parameter_list|,
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|Dispatcher
operator|.
name|toUnified
argument_list|(
name|base
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


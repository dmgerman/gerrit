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
name|client
operator|.
name|changes
operator|.
name|PatchTable
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
name|Patch
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

begin_class
DECL|class|PatchLink
specifier|public
specifier|abstract
class|class
name|PatchLink
extends|extends
name|InlineHyperlink
block|{
DECL|field|patchKey
specifier|protected
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|patchIndex
specifier|protected
name|int
name|patchIndex
decl_stmt|;
DECL|field|parentPatchTable
specifier|protected
name|PatchTable
name|parentPatchTable
decl_stmt|;
comment|/**    * @param text The text of this link    * @param patchKey The key for this patch    * @param patchIndex The index of the current patch in the patch set    * @param historyToken The history token    * @param parentPatchTable The table used to display this link    */
DECL|method|PatchLink (final String text, final Patch.Key patchKey, final int patchIndex, final String historyToken, PatchTable parentPatchTable)
specifier|public
name|PatchLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|String
name|historyToken
parameter_list|,
name|PatchTable
name|parentPatchTable
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|historyToken
argument_list|)
expr_stmt|;
name|this
operator|.
name|patchKey
operator|=
name|patchKey
expr_stmt|;
name|this
operator|.
name|patchIndex
operator|=
name|patchIndex
expr_stmt|;
name|this
operator|.
name|parentPatchTable
operator|=
name|parentPatchTable
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
name|Dispatcher
operator|.
name|patch
argument_list|(
comment|//
name|getTargetHistoryToken
argument_list|()
argument_list|,
comment|//
name|patchKey
argument_list|,
comment|//
name|patchIndex
argument_list|,
comment|//
name|parentPatchTable
comment|//
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
DECL|method|SideBySide (final String text, final Patch.Key patchKey, final int patchIndex, PatchTable parentPatchTable)
specifier|public
name|SideBySide
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
name|PatchTable
name|parentPatchTable
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|patchKey
argument_list|,
name|patchIndex
argument_list|,
name|Dispatcher
operator|.
name|toPatchSideBySide
argument_list|(
name|patchKey
argument_list|)
argument_list|,
name|parentPatchTable
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
DECL|method|Unified (final String text, final Patch.Key patchKey, final int patchIndex, PatchTable parentPatchTable)
specifier|public
name|Unified
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
name|PatchTable
name|parentPatchTable
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|patchKey
argument_list|,
name|patchIndex
argument_list|,
name|Dispatcher
operator|.
name|toPatchUnified
argument_list|(
name|patchKey
argument_list|)
argument_list|,
name|parentPatchTable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


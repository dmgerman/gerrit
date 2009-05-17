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
name|Link
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
name|client
operator|.
name|patches
operator|.
name|PatchScreen
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
name|ui
operator|.
name|Widget
import|;
end_import

begin_class
DECL|class|PatchLink
specifier|public
specifier|abstract
class|class
name|PatchLink
extends|extends
name|DirectScreenLink
block|{
DECL|field|key
specifier|protected
name|Patch
operator|.
name|Key
name|key
decl_stmt|;
DECL|method|PatchLink (final String text, final Patch.Key p, final String token)
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
name|p
parameter_list|,
specifier|final
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|key
operator|=
name|p
expr_stmt|;
block|}
DECL|method|parentPatchTable ()
specifier|protected
name|PatchTable
name|parentPatchTable
parameter_list|()
block|{
name|Widget
name|w
init|=
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|w
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|PatchTable
condition|)
block|{
return|return
operator|(
operator|(
name|PatchTable
operator|)
name|w
operator|)
return|;
block|}
name|w
operator|=
name|w
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|class|SideBySide
specifier|public
specifier|static
class|class
name|SideBySide
extends|extends
name|PatchLink
block|{
DECL|method|SideBySide (final String text, final Patch.Key p)
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
name|p
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|p
argument_list|,
name|Link
operator|.
name|toPatchSideBySide
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createScreen ()
specifier|protected
name|Screen
name|createScreen
parameter_list|()
block|{
return|return
operator|new
name|PatchScreen
operator|.
name|SideBySide
argument_list|(
name|key
argument_list|,
name|parentPatchTable
argument_list|()
argument_list|)
return|;
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
DECL|method|Unified (final String text, final Patch.Key p)
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
name|p
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|p
argument_list|,
name|Link
operator|.
name|toPatchUnified
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createScreen ()
specifier|protected
name|Screen
name|createScreen
parameter_list|()
block|{
return|return
operator|new
name|PatchScreen
operator|.
name|Unified
argument_list|(
name|key
argument_list|,
name|parentPatchTable
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


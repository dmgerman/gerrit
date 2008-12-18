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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|patches
operator|.
name|PatchUnifiedScreen
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Detail necessary to display {@link PatchUnifiedScreen}. */
end_comment

begin_class
DECL|class|UnifiedPatchDetail
specifier|public
class|class
name|UnifiedPatchDetail
block|{
DECL|field|patch
specifier|protected
name|Patch
name|patch
decl_stmt|;
DECL|field|lines
specifier|protected
name|List
argument_list|<
name|PatchLine
argument_list|>
name|lines
decl_stmt|;
DECL|method|UnifiedPatchDetail ()
specifier|protected
name|UnifiedPatchDetail
parameter_list|()
block|{   }
DECL|method|UnifiedPatchDetail (final Patch p)
specifier|public
name|UnifiedPatchDetail
parameter_list|(
specifier|final
name|Patch
name|p
parameter_list|)
block|{
name|patch
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getPatch ()
specifier|public
name|Patch
name|getPatch
parameter_list|()
block|{
return|return
name|patch
return|;
block|}
DECL|method|getLines ()
specifier|public
name|List
argument_list|<
name|PatchLine
argument_list|>
name|getLines
parameter_list|()
block|{
return|return
name|lines
return|;
block|}
DECL|method|setLines (final List<PatchLine> in)
specifier|public
name|void
name|setLines
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLine
argument_list|>
name|in
parameter_list|)
block|{
name|lines
operator|=
name|in
expr_stmt|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|PatchSetInfo
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

begin_class
DECL|class|PatchSetDetail
specifier|public
class|class
name|PatchSetDetail
block|{
DECL|field|patchSet
specifier|protected
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|info
specifier|protected
name|PatchSetInfo
name|info
decl_stmt|;
DECL|field|patches
specifier|protected
name|List
argument_list|<
name|Patch
argument_list|>
name|patches
decl_stmt|;
DECL|method|PatchSetDetail ()
specifier|public
name|PatchSetDetail
parameter_list|()
block|{   }
DECL|method|getPatchSet ()
specifier|public
name|PatchSet
name|getPatchSet
parameter_list|()
block|{
return|return
name|patchSet
return|;
block|}
DECL|method|setPatchSet (final PatchSet ps)
specifier|public
name|void
name|setPatchSet
parameter_list|(
specifier|final
name|PatchSet
name|ps
parameter_list|)
block|{
name|patchSet
operator|=
name|ps
expr_stmt|;
block|}
DECL|method|getInfo ()
specifier|public
name|PatchSetInfo
name|getInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
DECL|method|setInfo (final PatchSetInfo i)
specifier|public
name|void
name|setInfo
parameter_list|(
specifier|final
name|PatchSetInfo
name|i
parameter_list|)
block|{
name|info
operator|=
name|i
expr_stmt|;
block|}
DECL|method|getPatches ()
specifier|public
name|List
argument_list|<
name|Patch
argument_list|>
name|getPatches
parameter_list|()
block|{
return|return
name|patches
return|;
block|}
DECL|method|setPatches (final List<Patch> p)
specifier|public
name|void
name|setPatches
parameter_list|(
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|p
parameter_list|)
block|{
name|patches
operator|=
name|p
expr_stmt|;
block|}
block|}
end_class

end_unit


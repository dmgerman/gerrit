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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|reviewdb
operator|.
name|Change
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
name|gerrit
operator|.
name|client
operator|.
name|ui
operator|.
name|Screen
import|;
end_import

begin_class
DECL|class|PatchScreen
specifier|public
specifier|abstract
class|class
name|PatchScreen
extends|extends
name|Screen
block|{
DECL|field|patchId
specifier|protected
specifier|final
name|Patch
operator|.
name|Key
name|patchId
decl_stmt|;
DECL|method|PatchScreen (final Patch.Key id)
specifier|protected
name|PatchScreen
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
name|patchId
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchId
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|patchId
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|path
decl_stmt|;
specifier|final
name|int
name|last
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|>=
literal|0
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|last
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|setWindowTitle
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|patchWindowTitle
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
name|setPageTitle
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|patchPageTitle
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


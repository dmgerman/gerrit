begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|common
operator|.
name|data
operator|.
name|PatchScript
operator|.
name|PatchScriptFileInfo
import|;
end_import

begin_comment
comment|/** Contains settings for one of two sides in diff view. Each diff view has exactly 2 sides. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|DiffSide
specifier|public
specifier|abstract
class|class
name|DiffSide
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|SIDE_A
name|SIDE_A
block|,
DECL|enumConstant|SIDE_B
name|SIDE_B
block|}
DECL|method|create (PatchScriptFileInfo fileInfo, String fileName, Type type)
specifier|public
specifier|static
name|DiffSide
name|create
parameter_list|(
name|PatchScriptFileInfo
name|fileInfo
parameter_list|,
name|String
name|fileName
parameter_list|,
name|Type
name|type
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_DiffSide
argument_list|(
name|fileInfo
argument_list|,
name|fileName
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|fileInfo ()
specifier|public
specifier|abstract
name|PatchScriptFileInfo
name|fileInfo
parameter_list|()
function_decl|;
DECL|method|fileName ()
specifier|public
specifier|abstract
name|String
name|fileName
parameter_list|()
function_decl|;
DECL|method|type ()
specifier|public
specifier|abstract
name|Type
name|type
parameter_list|()
function_decl|;
block|}
end_class

end_unit


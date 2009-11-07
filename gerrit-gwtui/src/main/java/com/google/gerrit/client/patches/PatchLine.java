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

begin_class
DECL|class|PatchLine
class|class
name|PatchLine
block|{
DECL|enum|Type
specifier|static
enum|enum
name|Type
block|{
DECL|enumConstant|DELETE
DECL|enumConstant|INSERT
DECL|enumConstant|REPLACE
DECL|enumConstant|CONTEXT
name|DELETE
block|,
name|INSERT
block|,
name|REPLACE
block|,
name|CONTEXT
block|;   }
DECL|field|type
specifier|private
name|PatchLine
operator|.
name|Type
name|type
decl_stmt|;
DECL|field|lineA
specifier|private
name|int
name|lineA
decl_stmt|;
DECL|field|lineB
specifier|private
name|int
name|lineB
decl_stmt|;
DECL|method|PatchLine (final PatchLine.Type t, final int a, final int b)
name|PatchLine
parameter_list|(
specifier|final
name|PatchLine
operator|.
name|Type
name|t
parameter_list|,
specifier|final
name|int
name|a
parameter_list|,
specifier|final
name|int
name|b
parameter_list|)
block|{
name|type
operator|=
name|t
expr_stmt|;
name|lineA
operator|=
name|a
expr_stmt|;
name|lineB
operator|=
name|b
expr_stmt|;
block|}
DECL|method|getType ()
name|PatchLine
operator|.
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getLineA ()
name|int
name|getLineA
parameter_list|()
block|{
return|return
name|lineA
return|;
block|}
DECL|method|getLineB ()
name|int
name|getLineB
parameter_list|()
block|{
return|return
name|lineB
return|;
block|}
block|}
end_class

end_unit


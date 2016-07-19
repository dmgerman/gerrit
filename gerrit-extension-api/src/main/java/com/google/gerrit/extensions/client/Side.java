begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_enum
DECL|enum|Side
specifier|public
enum|enum
name|Side
block|{
DECL|enumConstant|PARENT
name|PARENT
block|,
DECL|enumConstant|REVISION
name|REVISION
block|;
DECL|method|fromShort (short s)
specifier|public
specifier|static
name|Side
name|fromShort
parameter_list|(
name|short
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|<=
literal|0
condition|)
block|{
return|return
name|PARENT
return|;
block|}
elseif|else
if|if
condition|(
name|s
operator|==
literal|1
condition|)
block|{
return|return
name|REVISION
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit


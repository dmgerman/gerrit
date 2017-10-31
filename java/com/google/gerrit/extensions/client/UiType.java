begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|enum|UiType
specifier|public
enum|enum
name|UiType
block|{
DECL|enumConstant|NONE
name|NONE
block|,
DECL|enumConstant|GWT
name|GWT
block|,
DECL|enumConstant|POLYGERRIT
name|POLYGERRIT
block|;
DECL|method|parse (String str)
specifier|public
specifier|static
name|UiType
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|UiType
name|type
range|:
name|UiType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|type
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit


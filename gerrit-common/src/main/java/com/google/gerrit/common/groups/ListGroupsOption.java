begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.groups
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|groups
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_comment
comment|/** Output options available when using {@code /groups/} RPCs. */
end_comment

begin_enum
DECL|enum|ListGroupsOption
specifier|public
enum|enum
name|ListGroupsOption
block|{
comment|/** Return information on the direct group members. */
DECL|enumConstant|MEMBERS
name|MEMBERS
argument_list|(
literal|0
argument_list|)
block|,
comment|/** Return information on the directly included groups. */
DECL|enumConstant|INCLUDES
name|INCLUDES
argument_list|(
literal|1
argument_list|)
block|;
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|ListGroupsOption (int v)
specifier|private
name|ListGroupsOption
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|v
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|int
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|fromValue (int value)
specifier|public
specifier|static
name|ListGroupsOption
name|fromValue
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|ListGroupsOption
operator|.
name|values
argument_list|()
index|[
name|value
index|]
return|;
block|}
DECL|method|fromBits (int v)
specifier|public
specifier|static
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|fromBits
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|r
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListGroupsOption
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ListGroupsOption
name|o
range|:
name|ListGroupsOption
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|v
operator|&
operator|(
literal|1
operator|<<
name|o
operator|.
name|value
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|v
operator|&=
operator|~
operator|(
literal|1
operator|<<
name|o
operator|.
name|value
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|==
literal|0
condition|)
block|{
return|return
name|r
return|;
block|}
block|}
if|if
condition|(
name|v
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown "
operator|+
name|Integer
operator|.
name|toHexString
argument_list|(
name|v
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
DECL|method|toBits (EnumSet<ListGroupsOption> set)
specifier|public
specifier|static
name|int
name|toBits
parameter_list|(
name|EnumSet
argument_list|<
name|ListGroupsOption
argument_list|>
name|set
parameter_list|)
block|{
name|int
name|r
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ListGroupsOption
name|o
range|:
name|set
control|)
block|{
name|r
operator||=
literal|1
operator|<<
name|o
operator|.
name|value
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_enum

end_unit


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

begin_comment
comment|/** Output options available when using {@code /groups/} RPCs. */
end_comment

begin_enum
DECL|enum|ListGroupsOption
specifier|public
enum|enum
name|ListGroupsOption
implements|implements
name|ListOption
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
annotation|@
name|Override
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
block|}
end_enum

end_unit


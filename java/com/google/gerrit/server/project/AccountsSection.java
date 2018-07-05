begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|PermissionRule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
DECL|class|AccountsSection
specifier|public
class|class
name|AccountsSection
block|{
DECL|field|sameGroupVisibility
specifier|protected
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|sameGroupVisibility
decl_stmt|;
DECL|method|getSameGroupVisibility ()
specifier|public
name|ImmutableList
argument_list|<
name|PermissionRule
argument_list|>
name|getSameGroupVisibility
parameter_list|()
block|{
if|if
condition|(
name|sameGroupVisibility
operator|==
literal|null
condition|)
block|{
name|sameGroupVisibility
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|sameGroupVisibility
argument_list|)
return|;
block|}
DECL|method|setSameGroupVisibility (List<PermissionRule> sameGroupVisibility)
specifier|public
name|void
name|setSameGroupVisibility
parameter_list|(
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|sameGroupVisibility
parameter_list|)
block|{
name|this
operator|.
name|sameGroupVisibility
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|sameGroupVisibility
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


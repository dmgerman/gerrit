begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|entities
operator|.
name|AccountGroup
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_class
DECL|class|SubgroupResource
specifier|public
class|class
name|SubgroupResource
extends|extends
name|GroupResource
block|{
DECL|field|SUBGROUP_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|SubgroupResource
argument_list|>
argument_list|>
name|SUBGROUP_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|SubgroupResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|member
specifier|private
specifier|final
name|GroupDescription
operator|.
name|Basic
name|member
decl_stmt|;
DECL|method|SubgroupResource (GroupResource group, GroupDescription.Basic member)
specifier|public
name|SubgroupResource
parameter_list|(
name|GroupResource
name|group
parameter_list|,
name|GroupDescription
operator|.
name|Basic
name|member
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|this
operator|.
name|member
operator|=
name|member
expr_stmt|;
block|}
DECL|method|getMember ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getMember
parameter_list|()
block|{
return|return
name|getMemberDescription
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
DECL|method|getMemberDescription ()
specifier|public
name|GroupDescription
operator|.
name|Basic
name|getMemberDescription
parameter_list|()
block|{
return|return
name|member
return|;
block|}
block|}
end_class

end_unit


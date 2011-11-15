begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|common
operator|.
name|data
operator|.
name|GroupDetail
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
DECL|class|GroupList
specifier|public
class|class
name|GroupList
block|{
DECL|field|groups
specifier|protected
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|groups
decl_stmt|;
DECL|field|canCreateGroup
specifier|protected
name|boolean
name|canCreateGroup
decl_stmt|;
DECL|method|GroupList ()
specifier|protected
name|GroupList
parameter_list|()
block|{   }
DECL|method|GroupList (final List<GroupDetail> groups, final boolean canCreateGroup)
specifier|public
name|GroupList
parameter_list|(
specifier|final
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|groups
parameter_list|,
specifier|final
name|boolean
name|canCreateGroup
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|canCreateGroup
operator|=
name|canCreateGroup
expr_stmt|;
block|}
DECL|method|getGroups ()
specifier|public
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|getGroups
parameter_list|()
block|{
return|return
name|groups
return|;
block|}
DECL|method|setGroups (List<GroupDetail> groups)
specifier|public
name|void
name|setGroups
parameter_list|(
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|groups
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
block|}
DECL|method|isCanCreateGroup ()
specifier|public
name|boolean
name|isCanCreateGroup
parameter_list|()
block|{
return|return
name|canCreateGroup
return|;
block|}
DECL|method|setCanCreateGroup (boolean set)
specifier|public
name|void
name|setCanCreateGroup
parameter_list|(
name|boolean
name|set
parameter_list|)
block|{
name|canCreateGroup
operator|=
name|set
expr_stmt|;
block|}
block|}
end_class

end_unit


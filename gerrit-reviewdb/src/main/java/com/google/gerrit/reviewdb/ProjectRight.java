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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|CompoundKey
import|;
end_import

begin_comment
comment|/** Grant to use an {@link ApprovalCategory} in the scope of a {@link Project}. */
end_comment

begin_class
DECL|class|ProjectRight
specifier|public
specifier|final
class|class
name|ProjectRight
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
DECL|field|projectName
specifier|protected
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
annotation|@
name|Column
DECL|field|categoryId
specifier|protected
name|ApprovalCategory
operator|.
name|Id
name|categoryId
decl_stmt|;
annotation|@
name|Column
DECL|field|groupId
specifier|protected
name|AccountGroup
operator|.
name|Id
name|groupId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|projectName
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|()
expr_stmt|;
name|categoryId
operator|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|()
expr_stmt|;
name|groupId
operator|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Project.NameKey p, final ApprovalCategory.Id a, final AccountGroup.Id g)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|g
parameter_list|)
block|{
name|projectName
operator|=
name|p
expr_stmt|;
name|categoryId
operator|=
name|a
expr_stmt|;
name|groupId
operator|=
name|g
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getParentKey
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
DECL|method|getProjectNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectNameKey
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|categoryId
operator|,
name|groupId
block|}
empty_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
annotation|@
name|Column
DECL|field|minValue
specifier|protected
name|short
name|minValue
decl_stmt|;
annotation|@
name|Column
DECL|field|maxValue
specifier|protected
name|short
name|maxValue
decl_stmt|;
DECL|method|ProjectRight ()
specifier|protected
name|ProjectRight
parameter_list|()
block|{   }
DECL|method|ProjectRight (final ProjectRight.Key k)
specifier|public
name|ProjectRight
parameter_list|(
specifier|final
name|ProjectRight
operator|.
name|Key
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|ProjectRight
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getProjectNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectNameKey
parameter_list|()
block|{
return|return
name|key
operator|.
name|projectName
return|;
block|}
DECL|method|getApprovalCategoryId ()
specifier|public
name|ApprovalCategory
operator|.
name|Id
name|getApprovalCategoryId
parameter_list|()
block|{
return|return
name|key
operator|.
name|categoryId
return|;
block|}
DECL|method|getAccountGroupId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getAccountGroupId
parameter_list|()
block|{
return|return
name|key
operator|.
name|groupId
return|;
block|}
DECL|method|getMinValue ()
specifier|public
name|short
name|getMinValue
parameter_list|()
block|{
return|return
name|minValue
return|;
block|}
DECL|method|setMinValue (final short m)
specifier|public
name|void
name|setMinValue
parameter_list|(
specifier|final
name|short
name|m
parameter_list|)
block|{
name|minValue
operator|=
name|m
expr_stmt|;
block|}
DECL|method|getMaxValue ()
specifier|public
name|short
name|getMaxValue
parameter_list|()
block|{
return|return
name|maxValue
return|;
block|}
DECL|method|setMaxValue (final short m)
specifier|public
name|void
name|setMaxValue
parameter_list|(
specifier|final
name|short
name|m
parameter_list|)
block|{
name|maxValue
operator|=
name|m
expr_stmt|;
block|}
block|}
end_class

end_unit


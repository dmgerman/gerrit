begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|reviewdb
operator|.
name|ApprovalCategory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ApprovalTypes
specifier|public
class|class
name|ApprovalTypes
block|{
DECL|field|approvalTypes
specifier|protected
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|approvalTypes
decl_stmt|;
DECL|field|actionTypes
specifier|protected
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|actionTypes
decl_stmt|;
DECL|field|byCategoryId
specifier|private
specifier|transient
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalType
argument_list|>
name|byCategoryId
decl_stmt|;
DECL|method|ApprovalTypes ()
specifier|protected
name|ApprovalTypes
parameter_list|()
block|{   }
DECL|method|ApprovalTypes (final List<ApprovalType> approvals, final List<ApprovalType> actions)
specifier|public
name|ApprovalTypes
parameter_list|(
specifier|final
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|approvals
parameter_list|,
specifier|final
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|actions
parameter_list|)
block|{
name|approvalTypes
operator|=
name|approvals
expr_stmt|;
name|actionTypes
operator|=
name|actions
expr_stmt|;
name|byCategory
argument_list|()
expr_stmt|;
block|}
DECL|method|getApprovalTypes ()
specifier|public
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|getApprovalTypes
parameter_list|()
block|{
return|return
name|approvalTypes
return|;
block|}
DECL|method|getActionTypes ()
specifier|public
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|getActionTypes
parameter_list|()
block|{
return|return
name|actionTypes
return|;
block|}
DECL|method|getApprovalType (final ApprovalCategory.Id id)
specifier|public
name|ApprovalType
name|getApprovalType
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|byCategory
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|byCategory ()
specifier|private
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalType
argument_list|>
name|byCategory
parameter_list|()
block|{
if|if
condition|(
name|byCategoryId
operator|==
literal|null
condition|)
block|{
name|byCategoryId
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ApprovalType
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|actionTypes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|ApprovalType
name|t
range|:
name|actionTypes
control|)
block|{
name|byCategoryId
operator|.
name|put
argument_list|(
name|t
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|approvalTypes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|ApprovalType
name|t
range|:
name|approvalTypes
control|)
block|{
name|byCategoryId
operator|.
name|put
argument_list|(
name|t
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|byCategoryId
return|;
block|}
block|}
end_class

end_unit


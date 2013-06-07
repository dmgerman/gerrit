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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
package|;
end_package

begin_class
DECL|class|BranchInfo
specifier|public
class|class
name|BranchInfo
block|{
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
DECL|field|can_delete
specifier|public
name|Boolean
name|can_delete
decl_stmt|;
DECL|method|BranchInfo ()
specifier|public
name|BranchInfo
parameter_list|()
block|{   }
DECL|method|BranchInfo (String ref, String revision, boolean canDelete)
specifier|public
name|BranchInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|revision
parameter_list|,
name|boolean
name|canDelete
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|can_delete
operator|=
name|canDelete
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
block|}
end_class

end_unit


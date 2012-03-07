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
name|client
operator|.
name|Branch
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

begin_comment
comment|/**  * It holds list of branches and boolean to indicate if it is allowed to add new  * branches.  */
end_comment

begin_class
DECL|class|ListBranchesResult
specifier|public
specifier|final
class|class
name|ListBranchesResult
block|{
DECL|field|noRepository
specifier|protected
name|boolean
name|noRepository
decl_stmt|;
DECL|field|canAdd
specifier|protected
name|boolean
name|canAdd
decl_stmt|;
DECL|field|branches
specifier|protected
name|List
argument_list|<
name|Branch
argument_list|>
name|branches
decl_stmt|;
DECL|method|ListBranchesResult ()
specifier|protected
name|ListBranchesResult
parameter_list|()
block|{   }
DECL|method|ListBranchesResult (List<Branch> branches, boolean canAdd, boolean noRepository)
specifier|public
name|ListBranchesResult
parameter_list|(
name|List
argument_list|<
name|Branch
argument_list|>
name|branches
parameter_list|,
name|boolean
name|canAdd
parameter_list|,
name|boolean
name|noRepository
parameter_list|)
block|{
name|this
operator|.
name|branches
operator|=
name|branches
expr_stmt|;
name|this
operator|.
name|canAdd
operator|=
name|canAdd
expr_stmt|;
name|this
operator|.
name|noRepository
operator|=
name|noRepository
expr_stmt|;
block|}
DECL|method|getNoRepository ()
specifier|public
name|boolean
name|getNoRepository
parameter_list|()
block|{
return|return
name|noRepository
return|;
block|}
DECL|method|getCanAdd ()
specifier|public
name|boolean
name|getCanAdd
parameter_list|()
block|{
return|return
name|canAdd
return|;
block|}
DECL|method|getBranches ()
specifier|public
name|List
argument_list|<
name|Branch
argument_list|>
name|getBranches
parameter_list|()
block|{
return|return
name|branches
return|;
block|}
block|}
end_class

end_unit


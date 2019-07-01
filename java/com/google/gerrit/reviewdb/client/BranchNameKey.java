begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_comment
comment|/** Branch name key */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|BranchNameKey
specifier|public
specifier|abstract
class|class
name|BranchNameKey
implements|implements
name|Comparable
argument_list|<
name|BranchNameKey
argument_list|>
block|{
DECL|method|create (Project.NameKey projectName, String branchName)
specifier|public
specifier|static
name|BranchNameKey
name|create
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|branchName
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_BranchNameKey
argument_list|(
name|projectName
argument_list|,
name|RefNames
operator|.
name|fullName
argument_list|(
name|branchName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (String projectName, String branchName)
specifier|public
specifier|static
name|BranchNameKey
name|create
parameter_list|(
name|String
name|projectName
parameter_list|,
name|String
name|branchName
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|projectName
argument_list|)
argument_list|,
name|branchName
argument_list|)
return|;
block|}
DECL|method|project ()
specifier|public
specifier|abstract
name|Project
operator|.
name|NameKey
name|project
parameter_list|()
function_decl|;
DECL|method|branch ()
specifier|public
specifier|abstract
name|String
name|branch
parameter_list|()
function_decl|;
DECL|method|shortName ()
specifier|public
name|String
name|shortName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|shortName
argument_list|(
name|branch
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (BranchNameKey o)
specifier|public
specifier|final
name|int
name|compareTo
parameter_list|(
name|BranchNameKey
name|o
parameter_list|)
block|{
comment|// TODO(dborowitz): Only compares branch name in order to match old StringKey behavior.
comment|// Consider comparing project name first.
return|return
name|branch
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|branch
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
name|project
argument_list|()
operator|+
literal|","
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|branch
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
name|Project
import|;
end_import

begin_class
DECL|class|ProjectDetail
specifier|public
class|class
name|ProjectDetail
block|{
DECL|field|project
specifier|public
name|Project
name|project
decl_stmt|;
DECL|field|canModifyDescription
specifier|public
name|boolean
name|canModifyDescription
decl_stmt|;
DECL|field|canModifyMergeType
specifier|public
name|boolean
name|canModifyMergeType
decl_stmt|;
DECL|field|canModifyAgreements
specifier|public
name|boolean
name|canModifyAgreements
decl_stmt|;
DECL|field|canModifyAccess
specifier|public
name|boolean
name|canModifyAccess
decl_stmt|;
DECL|method|ProjectDetail ()
specifier|public
name|ProjectDetail
parameter_list|()
block|{   }
DECL|method|setProject (final Project p)
specifier|public
name|void
name|setProject
parameter_list|(
specifier|final
name|Project
name|p
parameter_list|)
block|{
name|project
operator|=
name|p
expr_stmt|;
block|}
DECL|method|setCanModifyDescription (final boolean cmd)
specifier|public
name|void
name|setCanModifyDescription
parameter_list|(
specifier|final
name|boolean
name|cmd
parameter_list|)
block|{
name|canModifyDescription
operator|=
name|cmd
expr_stmt|;
block|}
DECL|method|setCanModifyMergeType (final boolean cmmt)
specifier|public
name|void
name|setCanModifyMergeType
parameter_list|(
specifier|final
name|boolean
name|cmmt
parameter_list|)
block|{
name|canModifyMergeType
operator|=
name|cmmt
expr_stmt|;
block|}
DECL|method|setCanModifyAgreements (final boolean cma)
specifier|public
name|void
name|setCanModifyAgreements
parameter_list|(
specifier|final
name|boolean
name|cma
parameter_list|)
block|{
name|canModifyAgreements
operator|=
name|cma
expr_stmt|;
block|}
DECL|method|setCanModifyAccess (final boolean cma)
specifier|public
name|void
name|setCanModifyAccess
parameter_list|(
specifier|final
name|boolean
name|cma
parameter_list|)
block|{
name|canModifyAccess
operator|=
name|cma
expr_stmt|;
block|}
block|}
end_class

end_unit


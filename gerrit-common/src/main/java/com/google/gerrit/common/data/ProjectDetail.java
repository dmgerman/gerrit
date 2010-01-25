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
name|reviewdb
operator|.
name|Project
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
name|reviewdb
operator|.
name|RefRight
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
DECL|field|groups
specifier|public
name|Map
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|AccountGroup
argument_list|>
name|groups
decl_stmt|;
DECL|field|rights
specifier|public
name|List
argument_list|<
name|RefRight
argument_list|>
name|rights
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
DECL|method|setGroups (final Map<AccountGroup.Id, AccountGroup> g)
specifier|public
name|void
name|setGroups
parameter_list|(
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|AccountGroup
argument_list|>
name|g
parameter_list|)
block|{
name|groups
operator|=
name|g
expr_stmt|;
block|}
DECL|method|setRights (final List<RefRight> r)
specifier|public
name|void
name|setRights
parameter_list|(
specifier|final
name|List
argument_list|<
name|RefRight
argument_list|>
name|r
parameter_list|)
block|{
name|rights
operator|=
name|r
expr_stmt|;
block|}
block|}
end_class

end_unit


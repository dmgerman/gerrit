begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.workflow
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|workflow
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
name|client
operator|.
name|data
operator|.
name|ApprovalType
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
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategoryValue
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
name|client
operator|.
name|reviewdb
operator|.
name|ChangeApproval
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
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|ProjectRight
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|OrmException
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
name|Transaction
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|RightRule
specifier|public
class|class
name|RightRule
block|{
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|method|RightRule (final ReviewDb d)
specifier|public
name|RightRule
parameter_list|(
specifier|final
name|ReviewDb
name|d
parameter_list|)
block|{
name|db
operator|=
name|d
expr_stmt|;
block|}
DECL|method|apply (final Project.NameKey projectName, final Collection<ChangeApproval> approvals)
specifier|public
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|apply
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|approvals
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Project
name|project
init|=
name|db
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
specifier|final
name|Project
operator|.
name|Id
name|projectId
init|=
name|project
operator|!=
literal|null
condition|?
name|project
operator|.
name|getId
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|apply
argument_list|(
name|projectId
argument_list|,
name|approvals
argument_list|)
return|;
block|}
DECL|method|apply (final Project.Id projectId, final Collection<ChangeApproval> approvals)
specifier|public
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|apply
parameter_list|(
specifier|final
name|Project
operator|.
name|Id
name|projectId
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|approvals
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|rights
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ChangeApproval
argument_list|>
name|max
decl_stmt|;
name|rights
operator|=
name|loadRights
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|max
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ChangeApproval
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|ChangeApproval
name|a
range|:
name|approvals
control|)
block|{
name|normalize
argument_list|(
name|rights
argument_list|,
name|a
argument_list|)
expr_stmt|;
specifier|final
name|ChangeApproval
name|m
init|=
name|max
operator|.
name|get
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
operator|||
name|m
operator|.
name|getValue
argument_list|()
operator|<
name|a
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|max
operator|.
name|put
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|missing
init|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ApprovalType
name|at
range|:
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getApprovalTypes
argument_list|()
control|)
block|{
specifier|final
name|ChangeApproval
name|m
init|=
name|max
operator|.
name|get
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ApprovalCategoryValue
name|n
init|=
name|at
operator|.
name|getMax
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
operator|||
name|n
operator|==
literal|null
operator|||
name|m
operator|.
name|getValue
argument_list|()
operator|<
name|n
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|missing
operator|.
name|add
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|missing
return|;
block|}
DECL|method|normalize (final Project.NameKey projectName, final Collection<ChangeApproval> approvals, final Transaction txn)
specifier|public
name|void
name|normalize
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|approvals
parameter_list|,
specifier|final
name|Transaction
name|txn
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Project
name|project
init|=
name|db
operator|.
name|projects
argument_list|()
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
specifier|final
name|Project
operator|.
name|Id
name|projectId
init|=
name|project
operator|!=
literal|null
condition|?
name|project
operator|.
name|getId
argument_list|()
else|:
literal|null
decl_stmt|;
name|normalize
argument_list|(
name|projectId
argument_list|,
name|approvals
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
DECL|method|normalize (final Project.Id projectId, final Collection<ChangeApproval> approvals, final Transaction txn)
specifier|public
name|void
name|normalize
parameter_list|(
specifier|final
name|Project
operator|.
name|Id
name|projectId
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|approvals
parameter_list|,
specifier|final
name|Transaction
name|txn
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|rights
decl_stmt|;
name|rights
operator|=
name|loadRights
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|ChangeApproval
name|a
range|:
name|approvals
control|)
block|{
if|if
condition|(
name|normalize
argument_list|(
name|rights
argument_list|,
name|a
argument_list|)
condition|)
block|{
name|db
operator|.
name|changeApprovals
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|normalize ( final Map<ApprovalCategory.Id, Collection<ProjectRight>> rights, final ChangeApproval a)
specifier|private
name|boolean
name|normalize
parameter_list|(
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|rights
parameter_list|,
specifier|final
name|ChangeApproval
name|a
parameter_list|)
block|{
name|short
name|min
init|=
literal|0
decl_stmt|,
name|max
init|=
literal|0
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|rights
operator|.
name|get
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|gs
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|getGroups
argument_list|(
name|a
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|r
range|:
name|l
control|)
block|{
if|if
condition|(
name|gs
operator|.
name|contains
argument_list|(
name|r
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|min
operator|=
operator|(
name|short
operator|)
name|Math
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|r
operator|.
name|getMinValue
argument_list|()
argument_list|)
expr_stmt|;
name|max
operator|=
operator|(
name|short
operator|)
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|r
operator|.
name|getMaxValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|<
name|min
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|min
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|>
name|max
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|max
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|loadRights ( final Project.Id projectId)
specifier|private
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|loadRights
parameter_list|(
specifier|final
name|Project
operator|.
name|Id
name|projectId
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|rights
init|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|projectId
operator|!=
literal|null
condition|)
block|{
name|loadRights
argument_list|(
name|rights
argument_list|,
name|projectId
argument_list|)
expr_stmt|;
block|}
name|loadRights
argument_list|(
name|rights
argument_list|,
name|ProjectRight
operator|.
name|WILD_PROJECT
argument_list|)
expr_stmt|;
return|return
name|rights
return|;
block|}
DECL|method|loadRights ( final Map<ApprovalCategory.Id, Collection<ProjectRight>> rights, final Project.Id projectId)
specifier|private
name|void
name|loadRights
parameter_list|(
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|rights
parameter_list|,
specifier|final
name|Project
operator|.
name|Id
name|projectId
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
specifier|final
name|ProjectRight
name|p
range|:
name|db
operator|.
name|projectRights
argument_list|()
operator|.
name|byProject
argument_list|(
name|projectId
argument_list|)
control|)
block|{
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|rights
operator|.
name|get
argument_list|(
name|p
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProjectRight
argument_list|>
argument_list|()
expr_stmt|;
name|rights
operator|.
name|put
argument_list|(
name|p
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.workflow
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|Account
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
name|Change
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
name|ApprovalCategory
operator|.
name|Id
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
name|server
operator|.
name|account
operator|.
name|AccountCache2
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
name|server
operator|.
name|account
operator|.
name|GroupCache
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
name|server
operator|.
name|project
operator|.
name|ProjectCache
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
name|server
operator|.
name|project
operator|.
name|ProjectState
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
name|Inject
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
name|assistedinject
operator|.
name|Assisted
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

begin_comment
comment|/** State passed through to a {@link CategoryFunction}. */
end_comment

begin_class
DECL|class|FunctionState
specifier|public
class|class
name|FunctionState
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Change c, Collection<ChangeApproval> all)
name|FunctionState
name|create
parameter_list|(
name|Change
name|c
parameter_list|,
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|all
parameter_list|)
function_decl|;
block|}
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache2
name|accountCache
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|>
name|groupCache
init|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|approvals
specifier|private
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
argument_list|>
name|approvals
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
name|ChangeApproval
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|valid
specifier|private
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Boolean
argument_list|>
name|valid
init|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectState
name|project
decl_stmt|;
DECL|field|allRights
specifier|private
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
name|allRights
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
DECL|field|projectRights
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
name|projectRights
decl_stmt|;
DECL|field|wildcardRights
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
name|wildcardRights
decl_stmt|;
DECL|field|modified
specifier|private
name|Set
argument_list|<
name|ChangeApproval
argument_list|>
name|modified
decl_stmt|;
annotation|@
name|Inject
DECL|method|FunctionState (final ProjectCache pc, final AccountCache2 ac, final GroupCache egc, @Assisted final Change c, @Assisted final Collection<ChangeApproval> all)
name|FunctionState
parameter_list|(
specifier|final
name|ProjectCache
name|pc
parameter_list|,
specifier|final
name|AccountCache2
name|ac
parameter_list|,
specifier|final
name|GroupCache
name|egc
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Change
name|c
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|all
parameter_list|)
block|{
name|projectCache
operator|=
name|pc
expr_stmt|;
name|accountCache
operator|=
name|ac
expr_stmt|;
name|change
operator|=
name|c
expr_stmt|;
name|project
operator|=
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|ChangeApproval
name|ca
range|:
name|all
control|)
block|{
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|l
init|=
name|approvals
operator|.
name|get
argument_list|(
name|ca
operator|.
name|getCategoryId
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
name|ChangeApproval
argument_list|>
argument_list|()
expr_stmt|;
name|approvals
operator|.
name|put
argument_list|(
name|ca
operator|.
name|getCategoryId
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
name|ca
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
operator|.
name|getProject
argument_list|()
return|;
block|}
DECL|method|valid (final ApprovalType at, final boolean v)
specifier|public
name|void
name|valid
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|,
specifier|final
name|boolean
name|v
parameter_list|)
block|{
name|valid
operator|.
name|put
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|isValid (final ApprovalType at)
specifier|public
name|boolean
name|isValid
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|isValid
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isValid (final ApprovalCategory.Id id)
specifier|public
name|boolean
name|isValid
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|Boolean
name|b
init|=
name|valid
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|b
operator|!=
literal|null
operator|&&
name|b
return|;
block|}
DECL|method|getApprovals (final ApprovalType at)
specifier|public
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|getApprovals
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|getApprovals
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getApprovals (final ApprovalCategory.Id id)
specifier|public
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|getApprovals
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|l
init|=
name|approvals
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|l
operator|!=
literal|null
condition|?
name|l
else|:
name|Collections
operator|.
expr|<
name|ChangeApproval
operator|>
name|emptySet
argument_list|()
return|;
block|}
DECL|method|dirty (final ChangeApproval ap)
specifier|public
name|void
name|dirty
parameter_list|(
specifier|final
name|ChangeApproval
name|ap
parameter_list|)
block|{
if|if
condition|(
name|modified
operator|==
literal|null
condition|)
block|{
name|modified
operator|=
operator|new
name|HashSet
argument_list|<
name|ChangeApproval
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|modified
operator|.
name|add
argument_list|(
name|ap
argument_list|)
expr_stmt|;
block|}
DECL|method|getDirtyChangeApprovals ()
specifier|public
name|Collection
argument_list|<
name|ChangeApproval
argument_list|>
name|getDirtyChangeApprovals
parameter_list|()
block|{
if|if
condition|(
name|modified
operator|!=
literal|null
condition|)
block|{
return|return
name|modified
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|getProjectRights (final ApprovalType at)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getProjectRights
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|getProjectRights
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getProjectRights (final ApprovalCategory.Id id)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getProjectRights
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|projectRights
operator|==
literal|null
condition|)
block|{
name|projectRights
operator|=
name|index
argument_list|(
name|project
operator|.
name|getRights
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|projectRights
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|l
operator|!=
literal|null
condition|?
name|l
else|:
name|Collections
operator|.
expr|<
name|ProjectRight
operator|>
name|emptySet
argument_list|()
return|;
block|}
DECL|method|getWildcardRights (final ApprovalType at)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getWildcardRights
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|getWildcardRights
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getWildcardRights (final ApprovalCategory.Id id)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getWildcardRights
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|wildcardRights
operator|==
literal|null
condition|)
block|{
name|wildcardRights
operator|=
name|index
argument_list|(
name|projectCache
operator|.
name|getWildcardRights
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|wildcardRights
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|l
operator|!=
literal|null
condition|?
name|l
else|:
name|Collections
operator|.
expr|<
name|ProjectRight
operator|>
name|emptySet
argument_list|()
return|;
block|}
DECL|method|getAllRights (final ApprovalType at)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getAllRights
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|getAllRights
argument_list|(
name|id
argument_list|(
name|at
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getAllRights (final ApprovalCategory.Id id)
specifier|public
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|getAllRights
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|allRights
operator|.
name|get
argument_list|(
name|id
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
name|l
operator|.
name|addAll
argument_list|(
name|getProjectRights
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|l
operator|.
name|addAll
argument_list|(
name|getWildcardRights
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|l
operator|=
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|allRights
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
DECL|method|index ( final Collection<ProjectRight> rights)
specifier|private
specifier|static
name|Map
argument_list|<
name|Id
argument_list|,
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
argument_list|>
name|index
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|rights
parameter_list|)
block|{
specifier|final
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
name|r
decl_stmt|;
name|r
operator|=
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
expr_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|pr
range|:
name|rights
control|)
block|{
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|l
init|=
name|r
operator|.
name|get
argument_list|(
name|pr
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
name|r
operator|.
name|put
argument_list|(
name|pr
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
name|pr
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|isMember (final ChangeApproval ca, final ProjectRight r)
specifier|public
name|boolean
name|isMember
parameter_list|(
specifier|final
name|ChangeApproval
name|ca
parameter_list|,
specifier|final
name|ProjectRight
name|r
parameter_list|)
block|{
return|return
name|isMember
argument_list|(
name|ca
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|r
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isMember (final Account.Id accountId, final AccountGroup.Id groupId)
specifier|public
name|boolean
name|isMember
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
return|return
name|getGroups
argument_list|(
name|accountId
argument_list|)
operator|.
name|contains
argument_list|(
name|groupId
argument_list|)
return|;
block|}
DECL|method|getGroups (final Account.Id id)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getGroups
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|==
literal|null
condition|)
block|{
name|g
operator|=
name|accountCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|getEffectiveGroups
argument_list|()
expr_stmt|;
name|groupCache
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|g
argument_list|)
expr_stmt|;
block|}
return|return
name|g
return|;
block|}
comment|/**    * Normalize the approval record down to the range permitted by the type, in    * case the type was modified since the approval was originally granted.    *<p>    * If the record's value was modified, its automatically marked as dirty.    */
DECL|method|applyTypeFloor (final ApprovalType at, final ChangeApproval a)
specifier|public
name|void
name|applyTypeFloor
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|,
specifier|final
name|ChangeApproval
name|a
parameter_list|)
block|{
specifier|final
name|ApprovalCategoryValue
name|atMin
init|=
name|at
operator|.
name|getMin
argument_list|()
decl_stmt|;
if|if
condition|(
name|atMin
operator|!=
literal|null
operator|&&
name|a
operator|.
name|getValue
argument_list|()
operator|<
name|atMin
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|atMin
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dirty
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ApprovalCategoryValue
name|atMax
init|=
name|at
operator|.
name|getMax
argument_list|()
decl_stmt|;
if|if
condition|(
name|atMax
operator|!=
literal|null
operator|&&
name|a
operator|.
name|getValue
argument_list|()
operator|>
name|atMax
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|atMax
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dirty
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Normalize the approval record to be inside the maximum range permitted by    * the ProjectRights granted to groups the account is a member of.    *<p>    * If multiple ProjectRights are matched (assigned to different groups the    * account is a member of) the lowest minValue and the highest maxValue of the    * union of them is used.    *<p>    * If the record's value was modified, its automatically marked as dirty.    */
DECL|method|applyRightFloor (final ChangeApproval a)
specifier|public
name|void
name|applyRightFloor
parameter_list|(
specifier|final
name|ChangeApproval
name|a
parameter_list|)
block|{
comment|// Find the maximal range actually granted to the user.
comment|//
name|short
name|minAllowed
init|=
literal|0
decl_stmt|,
name|maxAllowed
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|r
range|:
name|getAllRights
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|isMember
argument_list|(
name|a
argument_list|,
name|r
argument_list|)
condition|)
block|{
name|minAllowed
operator|=
operator|(
name|short
operator|)
name|Math
operator|.
name|min
argument_list|(
name|minAllowed
argument_list|,
name|r
operator|.
name|getMinValue
argument_list|()
argument_list|)
expr_stmt|;
name|maxAllowed
operator|=
operator|(
name|short
operator|)
name|Math
operator|.
name|max
argument_list|(
name|maxAllowed
argument_list|,
name|r
operator|.
name|getMaxValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Normalize the value into that range, returning true if we changed
comment|// the value.
comment|//
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|<
name|minAllowed
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|minAllowed
argument_list|)
expr_stmt|;
name|dirty
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|getValue
argument_list|()
operator|>
name|maxAllowed
condition|)
block|{
name|a
operator|.
name|setValue
argument_list|(
name|maxAllowed
argument_list|)
expr_stmt|;
name|dirty
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Run<code>applyTypeFloor</code>,<code>applyRightFloor</code>. */
DECL|method|normalize (final ApprovalType at, final ChangeApproval ca)
specifier|public
name|void
name|normalize
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|,
specifier|final
name|ChangeApproval
name|ca
parameter_list|)
block|{
name|applyTypeFloor
argument_list|(
name|at
argument_list|,
name|ca
argument_list|)
expr_stmt|;
name|applyRightFloor
argument_list|(
name|ca
argument_list|)
expr_stmt|;
block|}
DECL|method|id (final ApprovalType at)
specifier|private
specifier|static
name|Id
name|id
parameter_list|(
specifier|final
name|ApprovalType
name|at
parameter_list|)
block|{
return|return
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
end_class

end_unit


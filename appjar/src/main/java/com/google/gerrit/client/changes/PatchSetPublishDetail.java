begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|AccountInfoCache
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
name|data
operator|.
name|AccountInfoCacheFactory
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
name|data
operator|.
name|GerritConfig
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
name|data
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
name|PatchLineComment
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
name|PatchSet
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
name|PatchSetInfo
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
DECL|class|PatchSetPublishDetail
specifier|public
class|class
name|PatchSetPublishDetail
block|{
DECL|field|accounts
specifier|protected
name|AccountInfoCache
name|accounts
decl_stmt|;
DECL|field|patchSetInfo
specifier|protected
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
DECL|field|change
specifier|protected
name|Change
name|change
decl_stmt|;
DECL|field|drafts
specifier|protected
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|drafts
decl_stmt|;
DECL|field|allowed
specifier|protected
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|>
name|allowed
decl_stmt|;
DECL|field|given
specifier|protected
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|ChangeApproval
argument_list|>
name|given
decl_stmt|;
DECL|method|load (final ReviewDb db, final Change c, final PatchSet.Id psi)
specifier|public
name|void
name|load
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|psi
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|AccountInfoCacheFactory
name|acc
init|=
operator|new
name|AccountInfoCacheFactory
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|change
operator|=
name|c
expr_stmt|;
name|patchSetInfo
operator|=
name|db
operator|.
name|patchSetInfo
argument_list|()
operator|.
name|get
argument_list|(
name|psi
argument_list|)
expr_stmt|;
name|drafts
operator|=
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draft
argument_list|(
name|psi
argument_list|,
name|me
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
name|allowed
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|given
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
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|computeAllowed
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|ChangeApproval
name|a
range|:
name|db
operator|.
name|changeApprovals
argument_list|()
operator|.
name|byChangeUser
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|me
argument_list|)
control|)
block|{
name|given
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
name|acc
operator|.
name|want
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|accounts
operator|=
name|acc
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
DECL|method|computeAllowed ()
specifier|private
name|void
name|computeAllowed
parameter_list|()
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|Common
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|am
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|getGroups
argument_list|(
name|me
argument_list|)
decl_stmt|;
specifier|final
name|ProjectCache
operator|.
name|Entry
name|pe
init|=
name|Common
operator|.
name|getProjectCache
argument_list|()
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
decl_stmt|;
name|computeAllowed
argument_list|(
name|am
argument_list|,
name|pe
operator|.
name|getRights
argument_list|()
argument_list|)
expr_stmt|;
name|computeAllowed
argument_list|(
name|am
argument_list|,
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|getWildcardRights
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|computeAllowed (final Set<AccountGroup.Id> am, final Collection<ProjectRight> list)
specifier|private
name|void
name|computeAllowed
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|am
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ProjectRight
argument_list|>
name|list
parameter_list|)
block|{
specifier|final
name|GerritConfig
name|cfg
init|=
name|Common
operator|.
name|getGerritConfig
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|r
range|:
name|list
control|)
block|{
if|if
condition|(
operator|!
name|am
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
continue|continue;
block|}
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|s
init|=
name|allowed
operator|.
name|get
argument_list|(
name|r
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
operator|new
name|HashSet
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
name|allowed
operator|.
name|put
argument_list|(
name|r
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ApprovalType
name|at
init|=
name|cfg
operator|.
name|getApprovalType
argument_list|(
name|r
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|short
name|m
init|=
name|r
operator|.
name|getMinValue
argument_list|()
init|;
name|m
operator|<=
name|r
operator|.
name|getMaxValue
argument_list|()
condition|;
name|m
operator|++
control|)
block|{
specifier|final
name|ApprovalCategoryValue
name|v
init|=
name|at
operator|.
name|getValue
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
name|v
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getAccounts ()
specifier|public
name|AccountInfoCache
name|getAccounts
parameter_list|()
block|{
return|return
name|accounts
return|;
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
DECL|method|getPatchSetInfo ()
specifier|public
name|PatchSetInfo
name|getPatchSetInfo
parameter_list|()
block|{
return|return
name|patchSetInfo
return|;
block|}
DECL|method|getDrafts ()
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|getDrafts
parameter_list|()
block|{
return|return
name|drafts
return|;
block|}
DECL|method|isAllowed (final ApprovalCategory.Id id)
specifier|public
name|boolean
name|isAllowed
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|s
init|=
name|getAllowed
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|s
operator|!=
literal|null
operator|&&
operator|!
name|s
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getAllowed (final ApprovalCategory.Id id)
specifier|public
name|Set
argument_list|<
name|ApprovalCategoryValue
operator|.
name|Id
argument_list|>
name|getAllowed
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|allowed
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|getChangeApproval (final ApprovalCategory.Id id)
specifier|public
name|ChangeApproval
name|getChangeApproval
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|given
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit


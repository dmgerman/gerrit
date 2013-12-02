begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|common
operator|.
name|data
operator|.
name|GroupDetail
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
name|common
operator|.
name|errors
operator|.
name|NoSuchGroupException
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
name|client
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
name|reviewdb
operator|.
name|client
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
name|client
operator|.
name|AccountGroupById
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
name|client
operator|.
name|AccountGroupMember
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
name|client
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
name|server
operator|.
name|CurrentUser
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
name|group
operator|.
name|SystemGroupBackend
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
name|NoSuchProjectException
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
name|ProjectControl
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
name|server
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
name|io
operator|.
name|IOException
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
name|HashSet
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
DECL|class|GroupMembers
specifier|public
class|class
name|GroupMembers
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser currentUser)
name|GroupMembers
name|create
parameter_list|(
name|CurrentUser
name|currentUser
parameter_list|)
function_decl|;
block|}
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupDetailFactory
specifier|private
specifier|final
name|GroupDetailFactory
operator|.
name|Factory
name|groupDetailFactory
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControl
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|CurrentUser
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupMembers (final GroupCache groupCache, final GroupDetailFactory.Factory groupDetailFactory, final AccountCache accountCache, final ProjectControl.GenericFactory projectControl, @Assisted final CurrentUser currentUser)
name|GroupMembers
parameter_list|(
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupDetailFactory
operator|.
name|Factory
name|groupDetailFactory
parameter_list|,
specifier|final
name|AccountCache
name|accountCache
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControl
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|CurrentUser
name|currentUser
parameter_list|)
block|{
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupDetailFactory
operator|=
name|groupDetailFactory
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|projectControl
operator|=
name|projectControl
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
block|}
DECL|method|listAccounts (final AccountGroup.UUID groupUUID, final Project.NameKey project)
specifier|public
name|Set
argument_list|<
name|Account
argument_list|>
name|listAccounts
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|NoSuchGroupException
throws|,
name|NoSuchProjectException
throws|,
name|OrmException
throws|,
name|IOException
block|{
return|return
name|listAccounts
argument_list|(
name|groupUUID
argument_list|,
name|project
argument_list|,
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
DECL|method|listAccounts (final AccountGroup.UUID groupUUID, final Project.NameKey project, final Set<AccountGroup.UUID> seen)
specifier|private
name|Set
argument_list|<
name|Account
argument_list|>
name|listAccounts
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|seen
parameter_list|)
throws|throws
name|NoSuchGroupException
throws|,
name|OrmException
throws|,
name|NoSuchProjectException
throws|,
name|IOException
block|{
if|if
condition|(
name|SystemGroupBackend
operator|.
name|PROJECT_OWNERS
operator|.
name|equals
argument_list|(
name|groupUUID
argument_list|)
condition|)
block|{
return|return
name|getProjectOwners
argument_list|(
name|project
argument_list|,
name|seen
argument_list|)
return|;
block|}
else|else
block|{
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupUUID
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
return|return
name|getGroupMembers
argument_list|(
name|group
argument_list|,
name|project
argument_list|,
name|seen
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
block|}
DECL|method|getProjectOwners (final Project.NameKey project, final Set<AccountGroup.UUID> seen)
specifier|private
name|Set
argument_list|<
name|Account
argument_list|>
name|getProjectOwners
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|seen
parameter_list|)
throws|throws
name|NoSuchProjectException
throws|,
name|NoSuchGroupException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|seen
operator|.
name|add
argument_list|(
name|SystemGroupBackend
operator|.
name|PROJECT_OWNERS
argument_list|)
expr_stmt|;
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ownerGroups
init|=
name|projectControl
operator|.
name|controlFor
argument_list|(
name|project
argument_list|,
name|currentUser
argument_list|)
operator|.
name|getProjectState
argument_list|()
operator|.
name|getOwners
argument_list|()
decl_stmt|;
specifier|final
name|HashSet
argument_list|<
name|Account
argument_list|>
name|projectOwners
init|=
operator|new
name|HashSet
argument_list|<
name|Account
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|ownerGroup
range|:
name|ownerGroups
control|)
block|{
if|if
condition|(
operator|!
name|seen
operator|.
name|contains
argument_list|(
name|ownerGroup
argument_list|)
condition|)
block|{
name|projectOwners
operator|.
name|addAll
argument_list|(
name|listAccounts
argument_list|(
name|ownerGroup
argument_list|,
name|project
argument_list|,
name|seen
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|projectOwners
return|;
block|}
DECL|method|getGroupMembers (final AccountGroup group, final Project.NameKey project, final Set<AccountGroup.UUID> seen)
specifier|private
name|Set
argument_list|<
name|Account
argument_list|>
name|getGroupMembers
parameter_list|(
specifier|final
name|AccountGroup
name|group
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|seen
parameter_list|)
throws|throws
name|NoSuchGroupException
throws|,
name|OrmException
throws|,
name|NoSuchProjectException
throws|,
name|IOException
block|{
name|seen
operator|.
name|add
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|GroupDetail
name|groupDetail
init|=
name|groupDetailFactory
operator|.
name|create
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Account
argument_list|>
name|members
init|=
operator|new
name|HashSet
argument_list|<
name|Account
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupDetail
operator|.
name|members
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|AccountGroupMember
name|member
range|:
name|groupDetail
operator|.
name|members
control|)
block|{
name|members
operator|.
name|add
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|member
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|groupDetail
operator|.
name|includes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|AccountGroupById
name|groupInclude
range|:
name|groupDetail
operator|.
name|includes
control|)
block|{
specifier|final
name|AccountGroup
name|includedGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupInclude
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|includedGroup
operator|!=
literal|null
operator|&&
operator|!
name|seen
operator|.
name|contains
argument_list|(
name|includedGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
condition|)
block|{
name|members
operator|.
name|addAll
argument_list|(
name|listAccounts
argument_list|(
name|includedGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|project
argument_list|,
name|seen
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|members
return|;
block|}
block|}
end_class

end_unit


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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
operator|.
name|toImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Streams
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
name|Nullable
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
name|entities
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
name|entities
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
name|entities
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
name|group
operator|.
name|InternalGroup
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
name|InternalGroupDescription
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
name|Singleton
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
name|Optional
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
annotation|@
name|Singleton
DECL|class|GroupMembers
specifier|public
class|class
name|GroupMembers
block|{
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupMembers ( GroupCache groupCache, GroupControl.Factory groupControlFactory, AccountCache accountCache, ProjectCache projectCache)
name|GroupMembers
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|ProjectCache
name|projectCache
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
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
comment|/**    * Recursively enumerate the members of the given group. Should not be used with the    * PROJECT_OWNERS magical group.    *    *<p>Group members for which an account doesn't exist are filtered out.    */
DECL|method|listAccounts (AccountGroup.UUID groupUUID)
specifier|public
name|Set
argument_list|<
name|Account
argument_list|>
name|listAccounts
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|)
throws|throws
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
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"listAccounts called with PROJECT_OWNERS argument"
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|listAccounts
argument_list|(
name|groupUUID
argument_list|,
literal|null
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Recursively enumerate the members of the given group. The project should be specified so the    * PROJECT_OWNERS magical group can be expanded.    *    *<p>Group members for which an account doesn't exist are filtered out.    */
DECL|method|listAccounts (AccountGroup.UUID groupUUID, Project.NameKey project)
specifier|public
name|Set
argument_list|<
name|Account
argument_list|>
name|listAccounts
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|NoSuchProjectException
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
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
DECL|method|listAccounts ( final AccountGroup.UUID groupUUID, @Nullable final Project.NameKey project, final Set<AccountGroup.UUID> seen)
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
annotation|@
name|Nullable
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
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
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
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|getGroupMembers
argument_list|(
name|group
operator|.
name|get
argument_list|()
argument_list|,
name|project
argument_list|,
name|seen
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|getProjectOwners (final Project.NameKey project, Set<AccountGroup.UUID> seen)
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
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|project
argument_list|)
throw|;
block|}
specifier|final
name|HashSet
argument_list|<
name|Account
argument_list|>
name|projectOwners
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|ownerGroup
range|:
name|projectState
operator|.
name|getAllOwners
argument_list|()
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
DECL|method|getGroupMembers ( InternalGroup group, @Nullable Project.NameKey project, Set<AccountGroup.UUID> seen)
specifier|private
name|Set
argument_list|<
name|Account
argument_list|>
name|getGroupMembers
parameter_list|(
name|InternalGroup
name|group
parameter_list|,
annotation|@
name|Nullable
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
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
name|GroupControl
name|groupControl
init|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
operator|new
name|InternalGroupDescription
argument_list|(
name|group
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Account
argument_list|>
name|directMembers
init|=
name|group
operator|.
name|getMembers
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|groupControl
operator|::
name|canSeeMember
argument_list|)
operator|.
name|map
argument_list|(
name|accountCache
operator|::
name|get
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Streams
operator|::
name|stream
argument_list|)
operator|.
name|map
argument_list|(
name|AccountState
operator|::
name|account
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Account
argument_list|>
name|indirectMembers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupControl
operator|.
name|canSeeGroup
argument_list|()
condition|)
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|subgroupUuid
range|:
name|group
operator|.
name|getSubgroups
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|seen
operator|.
name|contains
argument_list|(
name|subgroupUuid
argument_list|)
condition|)
block|{
name|indirectMembers
operator|.
name|addAll
argument_list|(
name|listAccounts
argument_list|(
name|subgroupUuid
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
name|Sets
operator|.
name|union
argument_list|(
name|directMembers
argument_list|,
name|indirectMembers
argument_list|)
return|;
block|}
block|}
end_class

end_unit


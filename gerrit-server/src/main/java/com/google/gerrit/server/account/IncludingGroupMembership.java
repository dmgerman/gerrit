begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|Lists
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
name|server
operator|.
name|IdentifiedUser
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * Group membership checker for the internal group system.  *  *<p>Groups the user is directly a member of are pulled from the in-memory AccountCache by way of  * the IdentifiedUser. Transitive group memberhips are resolved on demand starting from the  * requested group and looking for a path to a group the user is a member of. Other group backends  * are supported by recursively invoking the universal GroupMembership.  */
end_comment

begin_class
DECL|class|IncludingGroupMembership
specifier|public
class|class
name|IncludingGroupMembership
implements|implements
name|GroupMembership
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (IdentifiedUser user)
name|IncludingGroupMembership
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
function_decl|;
block|}
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|includeCache
specifier|private
specifier|final
name|GroupIncludeCache
name|includeCache
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|memberOf
specifier|private
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Boolean
argument_list|>
name|memberOf
decl_stmt|;
DECL|field|knownGroups
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|knownGroups
decl_stmt|;
annotation|@
name|Inject
DECL|method|IncludingGroupMembership ( GroupCache groupCache, GroupIncludeCache includeCache, @Assisted IdentifiedUser user)
name|IncludingGroupMembership
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|GroupIncludeCache
name|includeCache
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
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
name|includeCache
operator|=
name|includeCache
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
init|=
name|user
operator|.
name|state
argument_list|()
operator|.
name|getInternalGroups
argument_list|()
decl_stmt|;
name|memberOf
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|groups
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|g
range|:
name|groups
control|)
block|{
name|memberOf
operator|.
name|put
argument_list|(
name|g
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|contains (AccountGroup.UUID id)
specifier|public
name|boolean
name|contains
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Boolean
name|b
init|=
name|memberOf
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
condition|?
name|b
else|:
name|containsAnyOf
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAnyOf (Iterable<AccountGroup.UUID> queryIds)
specifier|public
name|boolean
name|containsAnyOf
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|queryIds
parameter_list|)
block|{
comment|// Prefer lookup of a cached result over expanding includes.
name|boolean
name|tryExpanding
init|=
literal|false
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|id
range|:
name|queryIds
control|)
block|{
name|Boolean
name|b
init|=
name|memberOf
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|tryExpanding
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|tryExpanding
condition|)
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|id
range|:
name|queryIds
control|)
block|{
if|if
condition|(
name|memberOf
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
comment|// Membership was earlier proven to be false.
continue|continue;
block|}
name|memberOf
operator|.
name|put
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|group
operator|.
name|isPresent
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|search
argument_list|(
name|group
operator|.
name|get
argument_list|()
operator|.
name|getIncludes
argument_list|()
argument_list|)
condition|)
block|{
name|memberOf
operator|.
name|put
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|intersection (Iterable<AccountGroup.UUID> groupIds)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|intersection
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|r
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
name|id
range|:
name|groupIds
control|)
block|{
if|if
condition|(
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
DECL|method|search (Iterable<AccountGroup.UUID> ids)
specifier|private
name|boolean
name|search
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ids
parameter_list|)
block|{
return|return
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|ids
argument_list|)
return|;
block|}
DECL|method|computeKnownGroups ()
specifier|private
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|computeKnownGroups
parameter_list|()
block|{
name|GroupMembership
name|membership
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|direct
init|=
name|user
operator|.
name|state
argument_list|()
operator|.
name|getInternalGroups
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|direct
argument_list|)
decl_stmt|;
name|r
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|q
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|r
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|g
range|:
name|membership
operator|.
name|intersection
argument_list|(
name|includeCache
operator|.
name|allExternalMembers
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|g
operator|!=
literal|null
operator|&&
name|r
operator|.
name|add
argument_list|(
name|g
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
operator|!
name|q
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AccountGroup
operator|.
name|UUID
name|id
init|=
name|q
operator|.
name|remove
argument_list|(
name|q
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|g
range|:
name|includeCache
operator|.
name|parentGroupsOf
argument_list|(
name|id
argument_list|)
control|)
block|{
if|if
condition|(
name|g
operator|!=
literal|null
operator|&&
name|r
operator|.
name|add
argument_list|(
name|g
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|memberOf
operator|.
name|put
argument_list|(
name|g
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|r
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getKnownGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getKnownGroups
parameter_list|()
block|{
if|if
condition|(
name|knownGroups
operator|==
literal|null
condition|)
block|{
name|knownGroups
operator|=
name|computeKnownGroups
argument_list|()
expr_stmt|;
block|}
return|return
name|knownGroups
return|;
block|}
block|}
end_class

end_unit


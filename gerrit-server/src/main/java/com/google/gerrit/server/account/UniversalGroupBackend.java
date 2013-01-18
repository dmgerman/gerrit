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
import|import static
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
name|GroupBackends
operator|.
name|GROUP_REF_NAME_COMPARATOR
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
name|ArrayListMultimap
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
name|ImmutableMap
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
name|Iterables
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
name|Multimap
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|data
operator|.
name|GroupReference
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Universal implementation of the GroupBackend that works with the injected  * set of GroupBackends.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|UniversalGroupBackend
specifier|public
class|class
name|UniversalGroupBackend
implements|implements
name|GroupBackend
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UniversalGroupBackend
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|backends
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GroupBackend
argument_list|>
name|backends
decl_stmt|;
annotation|@
name|Inject
DECL|method|UniversalGroupBackend (DynamicSet<GroupBackend> backends)
name|UniversalGroupBackend
parameter_list|(
name|DynamicSet
argument_list|<
name|GroupBackend
argument_list|>
name|backends
parameter_list|)
block|{
name|this
operator|.
name|backends
operator|=
name|backends
expr_stmt|;
block|}
annotation|@
name|Nullable
DECL|method|backend (AccountGroup.UUID uuid)
specifier|private
name|GroupBackend
name|backend
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
if|if
condition|(
name|uuid
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|GroupBackend
name|g
range|:
name|backends
control|)
block|{
if|if
condition|(
name|g
operator|.
name|handles
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
name|g
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|handles (AccountGroup.UUID uuid)
specifier|public
name|boolean
name|handles
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|backend
argument_list|(
name|uuid
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.UUID uuid)
specifier|public
name|GroupDescription
operator|.
name|Basic
name|get
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|GroupBackend
name|b
init|=
name|backend
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unknown GroupBackend for UUID: "
operator|+
name|uuid
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|b
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|suggest (String name)
specifier|public
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|suggest
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|groups
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|GROUP_REF_NAME_COMPARATOR
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupBackend
name|g
range|:
name|backends
control|)
block|{
name|groups
operator|.
name|addAll
argument_list|(
name|g
operator|.
name|suggest
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
block|}
annotation|@
name|Override
DECL|method|membershipsOf (IdentifiedUser user)
specifier|public
name|GroupMembership
name|membershipsOf
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|UniversalGroupMembership
argument_list|(
name|user
argument_list|)
return|;
block|}
DECL|class|UniversalGroupMembership
specifier|private
class|class
name|UniversalGroupMembership
implements|implements
name|GroupMembership
block|{
DECL|field|memberships
specifier|private
specifier|final
name|Map
argument_list|<
name|GroupBackend
argument_list|,
name|GroupMembership
argument_list|>
name|memberships
decl_stmt|;
DECL|method|UniversalGroupMembership (IdentifiedUser user)
specifier|private
name|UniversalGroupMembership
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|GroupBackend
argument_list|,
name|GroupMembership
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|GroupBackend
name|g
range|:
name|backends
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|g
argument_list|,
name|g
operator|.
name|membershipsOf
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|memberships
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Nullable
DECL|method|membership (AccountGroup.UUID uuid)
specifier|private
name|GroupMembership
name|membership
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
if|if
condition|(
name|uuid
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|GroupBackend
argument_list|,
name|GroupMembership
argument_list|>
name|m
range|:
name|memberships
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getKey
argument_list|()
operator|.
name|handles
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
name|m
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|contains (AccountGroup.UUID uuid)
specifier|public
name|boolean
name|contains
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|GroupMembership
name|m
init|=
name|membership
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unknown GroupMembership for UUID: "
operator|+
name|uuid
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
name|m
operator|.
name|contains
argument_list|(
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAnyOf (Iterable<AccountGroup.UUID> uuids)
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
name|uuids
parameter_list|)
block|{
name|Multimap
argument_list|<
name|GroupMembership
argument_list|,
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|lookups
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|uuid
range|:
name|uuids
control|)
block|{
name|GroupMembership
name|m
init|=
name|membership
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unknown GroupMembership for UUID: "
operator|+
name|uuid
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|lookups
operator|.
name|put
argument_list|(
name|m
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|GroupMembership
argument_list|,
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|entry
range|:
name|lookups
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|GroupMembership
name|m
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ids
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|m
operator|.
name|contains
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|ids
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|m
operator|.
name|containsAnyOf
argument_list|(
name|ids
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
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
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|GroupMembership
name|m
range|:
name|memberships
operator|.
name|values
argument_list|()
control|)
block|{
name|groups
operator|.
name|addAll
argument_list|(
name|m
operator|.
name|getKnownGroups
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
block|}
block|}
block|}
end_class

end_unit


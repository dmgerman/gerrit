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
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|ImmutableList
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
name|server
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
name|server
operator|.
name|cache
operator|.
name|CacheModule
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
name|Groups
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
name|index
operator|.
name|group
operator|.
name|GroupField
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
name|index
operator|.
name|group
operator|.
name|GroupIndex
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
name|index
operator|.
name|group
operator|.
name|GroupIndexCollection
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
name|query
operator|.
name|group
operator|.
name|InternalGroupQuery
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|Module
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
name|Provider
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|name
operator|.
name|Named
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
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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

begin_comment
comment|/** Tracks group inclusions in memory for efficient access. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GroupIncludeCacheImpl
specifier|public
class|class
name|GroupIncludeCacheImpl
implements|implements
name|GroupIncludeCache
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
name|GroupIncludeCacheImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PARENT_GROUPS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PARENT_GROUPS_NAME
init|=
literal|"groups_bysubgroup"
decl_stmt|;
DECL|field|SUBGROUPS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SUBGROUPS_NAME
init|=
literal|"groups_subgroups"
decl_stmt|;
DECL|field|GROUPS_WITH_MEMBER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|GROUPS_WITH_MEMBER_NAME
init|=
literal|"groups_bymember"
decl_stmt|;
DECL|field|EXTERNAL_NAME
specifier|private
specifier|static
specifier|final
name|String
name|EXTERNAL_NAME
init|=
literal|"groups_external"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|GROUPS_WITH_MEMBER_NAME
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|GroupsWithMemberLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|PARENT_GROUPS_NAME
argument_list|,
name|AccountGroup
operator|.
name|UUID
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|ParentGroupsLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|SUBGROUPS_NAME
argument_list|,
name|AccountGroup
operator|.
name|UUID
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|SubgroupsLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|EXTERNAL_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|AllExternalLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GroupIncludeCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GroupIncludeCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GroupIncludeCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|groupsWithMember
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|groupsWithMember
decl_stmt|;
DECL|field|subgroups
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|subgroups
decl_stmt|;
DECL|field|parentGroups
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|parentGroups
decl_stmt|;
DECL|field|external
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|external
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupIncludeCacheImpl ( @amedGROUPS_WITH_MEMBER_NAME) LoadingCache<Account.Id, ImmutableSet<AccountGroup.UUID>> groupsWithMember, @Named(SUBGROUPS_NAME) LoadingCache<AccountGroup.UUID, ImmutableList<AccountGroup.UUID>> subgroups, @Named(PARENT_GROUPS_NAME) LoadingCache<AccountGroup.UUID, ImmutableList<AccountGroup.UUID>> parentGroups, @Named(EXTERNAL_NAME) LoadingCache<String, ImmutableList<AccountGroup.UUID>> external)
name|GroupIncludeCacheImpl
parameter_list|(
annotation|@
name|Named
argument_list|(
name|GROUPS_WITH_MEMBER_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|groupsWithMember
parameter_list|,
annotation|@
name|Named
argument_list|(
name|SUBGROUPS_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|subgroups
parameter_list|,
annotation|@
name|Named
argument_list|(
name|PARENT_GROUPS_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|parentGroups
parameter_list|,
annotation|@
name|Named
argument_list|(
name|EXTERNAL_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|external
parameter_list|)
block|{
name|this
operator|.
name|groupsWithMember
operator|=
name|groupsWithMember
expr_stmt|;
name|this
operator|.
name|subgroups
operator|=
name|subgroups
expr_stmt|;
name|this
operator|.
name|parentGroups
operator|=
name|parentGroups
expr_stmt|;
name|this
operator|.
name|external
operator|=
name|external
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getGroupsWithMember (Account.Id memberId)
specifier|public
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getGroupsWithMember
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
block|{
try|try
block|{
return|return
name|groupsWithMember
operator|.
name|get
argument_list|(
name|memberId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load groups containing %d as member"
argument_list|,
name|memberId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|subgroupsOf (AccountGroup.UUID groupId)
specifier|public
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|subgroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
block|{
try|try
block|{
return|return
name|subgroups
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load members of group"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|parentGroupsOf (AccountGroup.UUID groupId)
specifier|public
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|parentGroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
block|{
try|try
block|{
return|return
name|parentGroups
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load included groups"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|evictGroupsWithMember (Account.Id memberId)
specifier|public
name|void
name|evictGroupsWithMember
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
block|{
if|if
condition|(
name|memberId
operator|!=
literal|null
condition|)
block|{
name|groupsWithMember
operator|.
name|invalidate
argument_list|(
name|memberId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|evictSubgroupsOf (AccountGroup.UUID groupId)
specifier|public
name|void
name|evictSubgroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
block|{
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|subgroups
operator|.
name|invalidate
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|evictParentGroupsOf (AccountGroup.UUID groupId)
specifier|public
name|void
name|evictParentGroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
block|{
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|parentGroups
operator|.
name|invalidate
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|AccountGroup
operator|.
name|isInternalGroup
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
name|external
operator|.
name|invalidate
argument_list|(
name|EXTERNAL_NAME
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|allExternalMembers ()
specifier|public
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|allExternalMembers
parameter_list|()
block|{
try|try
block|{
return|return
name|external
operator|.
name|get
argument_list|(
name|EXTERNAL_NAME
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load set of non-internal groups"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
block|}
DECL|class|GroupsWithMemberLoader
specifier|static
class|class
name|GroupsWithMemberLoader
extends|extends
name|CacheLoader
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|groupIndexProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupIndex
argument_list|>
name|groupIndexProvider
decl_stmt|;
DECL|field|groupQueryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsWithMemberLoader ( SchemaFactory<ReviewDb> schema, GroupIndexCollection groupIndexCollection, Provider<InternalGroupQuery> groupQueryProvider, GroupCache groupCache)
name|GroupsWithMemberLoader
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
name|GroupIndexCollection
name|groupIndexCollection
parameter_list|,
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|groupIndexProvider
operator|=
name|groupIndexCollection
operator|::
name|getSearchIndex
expr_stmt|;
name|this
operator|.
name|groupQueryProvider
operator|=
name|groupQueryProvider
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (Account.Id memberId)
specifier|public
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|load
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|Stream
argument_list|<
name|InternalGroup
argument_list|>
name|internalGroupStream
decl_stmt|;
name|GroupIndex
name|groupIndex
init|=
name|groupIndexProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupIndex
operator|!=
literal|null
operator|&&
name|groupIndex
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|GroupField
operator|.
name|MEMBER
argument_list|)
condition|)
block|{
name|internalGroupStream
operator|=
name|groupQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|byMember
argument_list|(
name|memberId
argument_list|)
operator|.
name|stream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
init|)
block|{
name|internalGroupStream
operator|=
name|Groups
operator|.
name|getGroupsWithMemberFromReviewDb
argument_list|(
name|db
argument_list|,
name|memberId
argument_list|)
operator|.
name|map
argument_list|(
name|groupCache
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
expr_stmt|;
block|}
block|}
return|return
name|internalGroupStream
operator|.
name|map
argument_list|(
name|InternalGroup
operator|::
name|getGroupUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSet
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|SubgroupsLoader
specifier|static
class|class
name|SubgroupsLoader
extends|extends
name|CacheLoader
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
annotation|@
name|Inject
DECL|method|SubgroupsLoader (SchemaFactory<ReviewDb> sf, Groups groups)
name|SubgroupsLoader
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
name|Groups
name|groups
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (AccountGroup.UUID key)
specifier|public
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|load
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|key
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
init|)
block|{
return|return
name|groups
operator|.
name|getSubgroups
argument_list|(
name|db
argument_list|,
name|key
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
DECL|class|ParentGroupsLoader
specifier|static
class|class
name|ParentGroupsLoader
extends|extends
name|CacheLoader
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|groupIndexProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupIndex
argument_list|>
name|groupIndexProvider
decl_stmt|;
DECL|field|groupQueryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ParentGroupsLoader ( SchemaFactory<ReviewDb> sf, GroupIndexCollection groupIndexCollection, Provider<InternalGroupQuery> groupQueryProvider, GroupCache groupCache)
name|ParentGroupsLoader
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
name|GroupIndexCollection
name|groupIndexCollection
parameter_list|,
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|groupIndexProvider
operator|=
name|groupIndexCollection
operator|::
name|getSearchIndex
expr_stmt|;
name|this
operator|.
name|groupQueryProvider
operator|=
name|groupQueryProvider
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (AccountGroup.UUID key)
specifier|public
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|load
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|key
parameter_list|)
throws|throws
name|OrmException
block|{
name|Stream
argument_list|<
name|InternalGroup
argument_list|>
name|internalGroupStream
decl_stmt|;
if|if
condition|(
name|groupIndexProvider
operator|.
name|get
argument_list|()
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|GroupField
operator|.
name|SUBGROUP
argument_list|)
condition|)
block|{
name|internalGroupStream
operator|=
name|groupQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|bySubgroup
argument_list|(
name|key
argument_list|)
operator|.
name|stream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
init|)
block|{
name|internalGroupStream
operator|=
name|Groups
operator|.
name|getParentGroupsFromReviewDb
argument_list|(
name|db
argument_list|,
name|key
argument_list|)
operator|.
name|map
argument_list|(
name|groupCache
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
expr_stmt|;
block|}
block|}
return|return
name|internalGroupStream
operator|.
name|map
argument_list|(
name|InternalGroup
operator|::
name|getGroupUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|class|AllExternalLoader
specifier|static
class|class
name|AllExternalLoader
extends|extends
name|CacheLoader
argument_list|<
name|String
argument_list|,
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllExternalLoader (SchemaFactory<ReviewDb> sf, Groups groups)
name|AllExternalLoader
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
name|Groups
name|groups
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (String key)
specifier|public
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|load
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
init|)
block|{
return|return
name|groups
operator|.
name|getExternalGroups
argument_list|(
name|db
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit


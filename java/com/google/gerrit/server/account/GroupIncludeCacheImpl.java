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
name|flogger
operator|.
name|FluentLogger
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
name|db
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
name|logging
operator|.
name|Metadata
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
name|logging
operator|.
name|TraceContext
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
name|logging
operator|.
name|TraceContext
operator|.
name|TraceTimer
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
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
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
DECL|method|GroupIncludeCacheImpl ( @amedGROUPS_WITH_MEMBER_NAME) LoadingCache<Account.Id, ImmutableSet<AccountGroup.UUID>> groupsWithMember, @Named(PARENT_GROUPS_NAME) LoadingCache<AccountGroup.UUID, ImmutableList<AccountGroup.UUID>> parentGroups, @Named(EXTERNAL_NAME) LoadingCache<String, ImmutableList<AccountGroup.UUID>> external)
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
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot load groups containing %s as member"
argument_list|,
name|memberId
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
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot load included groups"
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
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Evict groups with member %d"
argument_list|,
name|memberId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Evict parent groups of %s"
argument_list|,
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Evict external group %s"
argument_list|,
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot load set of non-internal groups"
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
DECL|field|groupQueryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsWithMemberLoader (Provider<InternalGroupQuery> groupQueryProvider)
name|GroupsWithMemberLoader
parameter_list|(
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
parameter_list|)
block|{
name|this
operator|.
name|groupQueryProvider
operator|=
name|groupQueryProvider
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
block|{
try|try
init|(
name|TraceTimer
name|timer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Loading groups with member"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|accountId
argument_list|(
name|memberId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
return|return
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
DECL|field|groupQueryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ParentGroupsLoader (Provider<InternalGroupQuery> groupQueryProvider)
name|ParentGroupsLoader
parameter_list|(
name|Provider
argument_list|<
name|InternalGroupQuery
argument_list|>
name|groupQueryProvider
parameter_list|)
block|{
name|this
operator|.
name|groupQueryProvider
operator|=
name|groupQueryProvider
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
block|{
try|try
init|(
name|TraceTimer
name|timer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Loading parent groups"
argument_list|,
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|groupUuid
argument_list|(
name|key
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
init|)
block|{
return|return
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
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllExternalLoader (Groups groups)
name|AllExternalLoader
parameter_list|(
name|Groups
name|groups
parameter_list|)
block|{
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
name|TraceTimer
name|timer
init|=
name|TraceContext
operator|.
name|newTimer
argument_list|(
literal|"Loading all external groups"
argument_list|)
init|)
block|{
return|return
name|groups
operator|.
name|getExternalGroups
argument_list|()
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


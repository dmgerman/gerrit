begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|Optional
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
comment|/** Tracks group objects in memory for efficient access. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GroupCacheImpl
specifier|public
class|class
name|GroupCacheImpl
implements|implements
name|GroupCache
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
DECL|field|BYID_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BYID_NAME
init|=
literal|"groups"
decl_stmt|;
DECL|field|BYNAME_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BYNAME_NAME
init|=
literal|"groups_byname"
decl_stmt|;
DECL|field|BYUUID_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BYUUID_NAME
init|=
literal|"groups_byuuid"
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
name|BYID_NAME
argument_list|,
name|AccountGroup
operator|.
name|Id
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|maximumWeight
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|loader
argument_list|(
name|ByIdLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|BYNAME_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|maximumWeight
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|loader
argument_list|(
name|ByNameLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|BYUUID_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|maximumWeight
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|loader
argument_list|(
name|ByUUIDLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GroupCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GroupCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GroupCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|byId
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byId
decl_stmt|;
DECL|field|byName
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byName
decl_stmt|;
DECL|field|byUUID
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byUUID
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupCacheImpl ( @amedBYID_NAME) LoadingCache<AccountGroup.Id, Optional<InternalGroup>> byId, @Named(BYNAME_NAME) LoadingCache<String, Optional<InternalGroup>> byName, @Named(BYUUID_NAME) LoadingCache<String, Optional<InternalGroup>> byUUID)
name|GroupCacheImpl
parameter_list|(
annotation|@
name|Named
argument_list|(
name|BYID_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byId
parameter_list|,
annotation|@
name|Named
argument_list|(
name|BYNAME_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byName
parameter_list|,
annotation|@
name|Named
argument_list|(
name|BYUUID_NAME
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|byUUID
parameter_list|)
block|{
name|this
operator|.
name|byId
operator|=
name|byId
expr_stmt|;
name|this
operator|.
name|byName
operator|=
name|byName
expr_stmt|;
name|this
operator|.
name|byUUID
operator|=
name|byUUID
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.Id groupId)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
try|try
block|{
return|return
name|byId
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
literal|"Cannot load group %s"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.NameKey name)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|byName
operator|.
name|get
argument_list|(
name|name
operator|.
name|get
argument_list|()
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
literal|"Cannot look up group %s by name"
argument_list|,
name|name
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.UUID groupUuid)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
if|if
condition|(
name|groupUuid
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|byUUID
operator|.
name|get
argument_list|(
name|groupUuid
operator|.
name|get
argument_list|()
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
literal|"Cannot look up group %s by uuid"
argument_list|,
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|evict (AccountGroup.Id groupId)
specifier|public
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|Id
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
literal|"Evict group %s by ID"
argument_list|,
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|byId
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
DECL|method|evict (AccountGroup.NameKey groupName)
specifier|public
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|groupName
parameter_list|)
block|{
if|if
condition|(
name|groupName
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
literal|"Evict group '%s' by name"
argument_list|,
name|groupName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|byName
operator|.
name|invalidate
argument_list|(
name|groupName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|evict (AccountGroup.UUID groupUuid)
specifier|public
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
if|if
condition|(
name|groupUuid
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
literal|"Evict group %s by UUID"
argument_list|,
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|byUUID
operator|.
name|invalidate
argument_list|(
name|groupUuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ByIdLoader
specifier|static
class|class
name|ByIdLoader
extends|extends
name|CacheLoader
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
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
DECL|method|ByIdLoader (Provider<InternalGroupQuery> groupQueryProvider)
name|ByIdLoader
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
DECL|method|load (AccountGroup.Id key)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|load
parameter_list|(
name|AccountGroup
operator|.
name|Id
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
literal|"Loading group %s by ID"
argument_list|,
name|key
argument_list|)
init|)
block|{
return|return
name|groupQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|byId
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
block|}
DECL|class|ByNameLoader
specifier|static
class|class
name|ByNameLoader
extends|extends
name|CacheLoader
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
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
DECL|method|ByNameLoader (Provider<InternalGroupQuery> groupQueryProvider)
name|ByNameLoader
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
DECL|method|load (String name)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|load
parameter_list|(
name|String
name|name
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
literal|"Loading group '%s' by name"
argument_list|,
name|name
argument_list|)
init|)
block|{
return|return
name|groupQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|byName
argument_list|(
name|AccountGroup
operator|.
name|nameKey
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
DECL|class|ByUUIDLoader
specifier|static
class|class
name|ByUUIDLoader
extends|extends
name|CacheLoader
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|InternalGroup
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
DECL|method|ByUUIDLoader (Groups groups)
name|ByUUIDLoader
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
DECL|method|load (String uuid)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|load
parameter_list|(
name|String
name|uuid
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
literal|"Loading group %s by UUID"
argument_list|,
name|uuid
argument_list|)
init|)
block|{
return|return
name|groups
operator|.
name|getGroup
argument_list|(
name|AccountGroup
operator|.
name|uuid
argument_list|(
name|uuid
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit


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
name|base
operator|.
name|Optional
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
name|AccountGroupName
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmDuplicateKeyException
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
name|Collections
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
name|GroupCacheImpl
operator|.
name|class
argument_list|)
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
name|AccountGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
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
name|AccountGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
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
name|AccountGroup
argument_list|>
argument_list|>
argument_list|()
block|{}
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
name|AccountGroup
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
name|AccountGroup
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
name|AccountGroup
argument_list|>
argument_list|>
name|byUUID
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupCacheImpl ( @amedBYID_NAME) LoadingCache<AccountGroup.Id, Optional<AccountGroup>> byId, @Named(BYNAME_NAME) LoadingCache<String, Optional<AccountGroup>> byName, @Named(BYUUID_NAME) LoadingCache<String, Optional<AccountGroup>> byUUID, SchemaFactory<ReviewDb> schema)
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
name|AccountGroup
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
name|AccountGroup
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
name|AccountGroup
argument_list|>
argument_list|>
name|byUUID
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
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
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (final AccountGroup.Id groupId)
specifier|public
name|AccountGroup
name|get
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
try|try
block|{
name|Optional
argument_list|<
name|AccountGroup
argument_list|>
name|g
init|=
name|byId
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
return|return
name|g
operator|.
name|isPresent
argument_list|()
condition|?
name|g
operator|.
name|get
argument_list|()
else|:
name|missing
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
literal|"Cannot load group "
operator|+
name|groupId
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|missing
argument_list|(
name|groupId
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|evict (final AccountGroup group)
specifier|public
name|void
name|evict
parameter_list|(
specifier|final
name|AccountGroup
name|group
parameter_list|)
block|{
if|if
condition|(
name|group
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byId
operator|.
name|invalidate
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|.
name|getNameKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byName
operator|.
name|invalidate
argument_list|(
name|group
operator|.
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byUUID
operator|.
name|invalidate
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|evictAfterRename (final AccountGroup.NameKey oldName, final AccountGroup.NameKey newName)
specifier|public
name|void
name|evictAfterRename
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|oldName
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|newName
parameter_list|)
block|{
if|if
condition|(
name|oldName
operator|!=
literal|null
condition|)
block|{
name|byName
operator|.
name|invalidate
argument_list|(
name|oldName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newName
operator|!=
literal|null
condition|)
block|{
name|byName
operator|.
name|invalidate
argument_list|(
name|newName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.NameKey name)
specifier|public
name|AccountGroup
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
literal|null
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
operator|.
name|orNull
argument_list|()
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
literal|"Cannot lookup group %s by name"
argument_list|,
name|name
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|get (AccountGroup.UUID uuid)
specifier|public
name|AccountGroup
name|get
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
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|byUUID
operator|.
name|get
argument_list|(
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|orNull
argument_list|()
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
literal|"Cannot lookup group %s by name"
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|Iterable
argument_list|<
name|AccountGroup
argument_list|>
name|all
parameter_list|()
block|{
try|try
block|{
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot list internal groups"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|onCreateGroup (AccountGroup.NameKey newGroupName)
specifier|public
name|void
name|onCreateGroup
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|newGroupName
parameter_list|)
block|{
name|byName
operator|.
name|invalidate
argument_list|(
name|newGroupName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|missing (AccountGroup.Id key)
specifier|private
specifier|static
name|AccountGroup
name|missing
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|key
parameter_list|)
block|{
name|AccountGroup
operator|.
name|NameKey
name|name
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Deleted Group"
operator|+
name|key
argument_list|)
decl_stmt|;
return|return
operator|new
name|AccountGroup
argument_list|(
name|name
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
return|;
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
name|AccountGroup
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
annotation|@
name|Inject
DECL|method|ByIdLoader (final SchemaFactory<ReviewDb> sf)
name|ByIdLoader
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (final AccountGroup.Id key)
specifier|public
name|Optional
argument_list|<
name|AccountGroup
argument_list|>
name|load
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|key
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|Optional
operator|.
name|fromNullable
argument_list|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|AccountGroup
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
annotation|@
name|Inject
DECL|method|ByNameLoader (final SchemaFactory<ReviewDb> sf)
name|ByNameLoader
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (String name)
specifier|public
name|Optional
argument_list|<
name|AccountGroup
argument_list|>
name|load
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
name|AccountGroup
operator|.
name|NameKey
name|key
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|AccountGroupName
name|r
init|=
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|fromNullable
argument_list|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|r
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|AccountGroup
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
annotation|@
name|Inject
DECL|method|ByUUIDLoader (final SchemaFactory<ReviewDb> sf)
name|ByUUIDLoader
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (String uuid)
specifier|public
name|Optional
argument_list|<
name|AccountGroup
argument_list|>
name|load
parameter_list|(
name|String
name|uuid
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|r
decl_stmt|;
name|r
operator|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|byUUID
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|uuid
argument_list|)
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|r
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|OrmDuplicateKeyException
argument_list|(
literal|"Duplicate group UUID "
operator|+
name|uuid
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


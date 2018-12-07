begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|Key
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
name|Access
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
name|AtomicUpdate
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
name|ListResultSet
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
name|ResultSet
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

begin_class
DECL|class|AbstractDisabledAccess
specifier|abstract
class|class
name|AbstractDisabledAccess
parameter_list|<
name|T
parameter_list|,
name|K
extends|extends
name|Key
parameter_list|<
name|?
parameter_list|>
parameter_list|>
implements|implements
name|Access
argument_list|<
name|T
argument_list|,
name|K
argument_list|>
block|{
DECL|field|GONE
specifier|private
specifier|static
specifier|final
name|String
name|GONE
init|=
literal|"ReviewDb is gone"
decl_stmt|;
DECL|method|empty ()
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ResultSet
argument_list|<
name|T
argument_list|>
name|empty
parameter_list|()
block|{
return|return
operator|new
name|ListResultSet
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
DECL|method|emptyFuture ()
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CheckedFuture
argument_list|<
name|T
argument_list|,
name|OrmException
argument_list|>
name|emptyFuture
parameter_list|()
block|{
return|return
name|Futures
operator|.
name|immediateCheckedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRelationID ()
specifier|public
specifier|final
name|int
name|getRelationID
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|GONE
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getRelationName ()
specifier|public
specifier|final
name|String
name|getRelationName
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|GONE
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|primaryKey (T entity)
specifier|public
specifier|final
name|K
name|primaryKey
parameter_list|(
name|T
name|entity
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|GONE
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|toMap (Iterable<T> iterable)
specifier|public
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|T
argument_list|>
name|toMap
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|GONE
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|iterateAllEntities ()
specifier|public
specifier|final
name|ResultSet
argument_list|<
name|T
argument_list|>
name|iterateAllEntities
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|getAsync (K key)
specifier|public
specifier|final
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CheckedFuture
argument_list|<
name|T
argument_list|,
name|OrmException
argument_list|>
name|getAsync
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|emptyFuture
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (Iterable<K> keys)
specifier|public
specifier|final
name|ResultSet
argument_list|<
name|T
argument_list|>
name|get
parameter_list|(
name|Iterable
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|insert (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|insert
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|update (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|update
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|upsert (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|upsert
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|deleteKeys (Iterable<K> keys)
specifier|public
specifier|final
name|void
name|deleteKeys
parameter_list|(
name|Iterable
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|delete (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|delete
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|beginTransaction (K key)
specifier|public
specifier|final
name|void
name|beginTransaction
parameter_list|(
name|K
name|key
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|atomicUpdate (K key, AtomicUpdate<T> update)
specifier|public
specifier|final
name|T
name|atomicUpdate
parameter_list|(
name|K
name|key
parameter_list|,
name|AtomicUpdate
argument_list|<
name|T
argument_list|>
name|update
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|get (K id)
specifier|public
specifier|final
name|T
name|get
parameter_list|(
name|K
name|id
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit


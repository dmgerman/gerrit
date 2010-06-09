begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.cache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Proxy around a cache which has not yet been created. */
end_comment

begin_class
DECL|class|ProxyCache
specifier|public
specifier|final
class|class
name|ProxyCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|self
specifier|private
specifier|volatile
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|self
decl_stmt|;
DECL|method|bind (Cache<K, V> self)
specifier|public
name|void
name|bind
parameter_list|(
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|self
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
block|}
DECL|method|get (K key)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|self
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getTimeToLive (TimeUnit unit)
specifier|public
name|long
name|getTimeToLive
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|self
operator|.
name|getTimeToLive
argument_list|(
name|unit
argument_list|)
return|;
block|}
DECL|method|put (K key, V value)
specifier|public
name|void
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|self
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (K key)
specifier|public
name|void
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|self
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|removeAll ()
specifier|public
name|void
name|removeAll
parameter_list|()
block|{
name|self
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


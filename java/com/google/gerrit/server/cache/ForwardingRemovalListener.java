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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|RemovalListener
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
name|RemovalNotification
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
name|PluginName
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
name|plugincontext
operator|.
name|PluginSetContext
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

begin_comment
comment|/**  * This listener dispatches removal events to all other RemovalListeners attached via the DynamicSet  * API.  *  * @param<K>  * @param<V>  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|class|ForwardingRemovalListener
specifier|public
class|class
name|ForwardingRemovalListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String cacheName)
name|ForwardingRemovalListener
name|create
parameter_list|(
name|String
name|cacheName
parameter_list|)
function_decl|;
block|}
DECL|field|listeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|CacheRemovalListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|pluginName
specifier|private
name|String
name|pluginName
init|=
name|PluginName
operator|.
name|GERRIT
decl_stmt|;
annotation|@
name|Inject
DECL|method|ForwardingRemovalListener ( PluginSetContext<CacheRemovalListener> listeners, @Assisted String cacheName)
name|ForwardingRemovalListener
parameter_list|(
name|PluginSetContext
argument_list|<
name|CacheRemovalListener
argument_list|>
name|listeners
parameter_list|,
annotation|@
name|Assisted
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setPluginName (String name)
name|void
name|setPluginName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|name
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|onRemoval (RemovalNotification<K, V> notification)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|notification
parameter_list|)
block|{
name|listeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onRemoval
argument_list|(
name|pluginName
argument_list|,
name|cacheName
argument_list|,
name|notification
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


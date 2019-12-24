begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
package|;
end_package

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
name|events
operator|.
name|PluginEventListener
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
name|Singleton
import|;
end_import

begin_comment
comment|/** Helper class to let plugins fire a plugin-specific event. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PluginEvent
specifier|public
class|class
name|PluginEvent
block|{
DECL|field|listeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|PluginEventListener
argument_list|>
name|listeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginEvent (PluginSetContext<PluginEventListener> listeners)
name|PluginEvent
parameter_list|(
name|PluginSetContext
argument_list|<
name|PluginEventListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
DECL|method|fire (String pluginName, String type, String data)
specifier|public
name|void
name|fire
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
operator|!
name|listeners
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
name|Event
name|e
init|=
operator|new
name|Event
argument_list|(
name|pluginName
argument_list|,
name|type
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onPluginEvent
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Event to be fired by plugins. */
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
extends|extends
name|AbstractNoNotifyEvent
implements|implements
name|PluginEventListener
operator|.
name|Event
block|{
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|String
name|type
decl_stmt|;
DECL|field|data
specifier|private
specifier|final
name|String
name|data
decl_stmt|;
DECL|method|Event (String pluginName, String type, String data)
name|Event
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|data
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pluginName ()
specifier|public
name|String
name|pluginName
parameter_list|()
block|{
return|return
name|pluginName
return|;
block|}
annotation|@
name|Override
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|getData ()
specifier|public
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
block|}
block|}
end_class

end_unit


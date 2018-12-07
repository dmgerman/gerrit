begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|server
operator|.
name|config
operator|.
name|ConfigUpdatedEvent
operator|.
name|ConfigUpdateEntry
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
name|config
operator|.
name|ConfigUpdatedEvent
operator|.
name|UpdateResult
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
comment|/** Issues a configuration reload from the GerritServerConfigProvider and notify all listeners. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GerritServerConfigReloader
specifier|public
class|class
name|GerritServerConfigReloader
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
DECL|field|configProvider
specifier|private
specifier|final
name|GerritServerConfigProvider
name|configProvider
decl_stmt|;
DECL|field|configListeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|GerritConfigListener
argument_list|>
name|configListeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritServerConfigReloader ( GerritServerConfigProvider configProvider, PluginSetContext<GerritConfigListener> configListeners)
name|GerritServerConfigReloader
parameter_list|(
name|GerritServerConfigProvider
name|configProvider
parameter_list|,
name|PluginSetContext
argument_list|<
name|GerritConfigListener
argument_list|>
name|configListeners
parameter_list|)
block|{
name|this
operator|.
name|configProvider
operator|=
name|configProvider
expr_stmt|;
name|this
operator|.
name|configListeners
operator|=
name|configListeners
expr_stmt|;
block|}
comment|/**    * Reloads the Gerrit Server Configuration from disk. Synchronized to ensure that one issued    * reload is fully completed before a new one starts.    */
DECL|method|reloadConfig ()
specifier|public
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|reloadConfig
parameter_list|()
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Starting server configuration reload"
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|updates
init|=
name|fireUpdatedConfigEvent
argument_list|(
name|configProvider
operator|.
name|updateConfig
argument_list|()
argument_list|)
decl_stmt|;
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Server configuration reload completed succesfully"
argument_list|)
expr_stmt|;
return|return
name|updates
return|;
block|}
DECL|method|fireUpdatedConfigEvent ( ConfigUpdatedEvent event)
specifier|public
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|fireUpdatedConfigEvent
parameter_list|(
name|ConfigUpdatedEvent
name|event
parameter_list|)
block|{
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|updates
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|configListeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|updates
operator|.
name|putAll
argument_list|(
name|l
operator|.
name|configUpdated
argument_list|(
name|event
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|updates
return|;
block|}
block|}
end_class

end_unit


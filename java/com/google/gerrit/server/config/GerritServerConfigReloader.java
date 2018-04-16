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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|GerritServerConfigReloader
operator|.
name|class
argument_list|)
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
name|DynamicSet
argument_list|<
name|GerritConfigListener
argument_list|>
name|configListeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritServerConfigReloader ( GerritServerConfigProvider configProvider, DynamicSet<GerritConfigListener> configListeners)
name|GerritServerConfigReloader
parameter_list|(
name|GerritServerConfigProvider
name|configProvider
parameter_list|,
name|DynamicSet
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
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|reloadConfig
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting server configuration reload"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
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
name|log
operator|.
name|info
argument_list|(
literal|"Server configuration reload completed succesfully"
argument_list|)
expr_stmt|;
return|return
name|updates
return|;
block|}
DECL|method|fireUpdatedConfigEvent (ConfigUpdatedEvent event)
specifier|public
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|fireUpdatedConfigEvent
parameter_list|(
name|ConfigUpdatedEvent
name|event
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|GerritConfigListener
name|configListener
range|:
name|configListeners
control|)
block|{
name|result
operator|.
name|addAll
argument_list|(
name|configListener
operator|.
name|configUpdated
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|LifecycleListener
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
name|systemstatus
operator|.
name|ServerInformation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|AsyncAppender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Layout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_class
DECL|class|PluginLogFile
specifier|public
specifier|abstract
class|class
name|PluginLogFile
implements|implements
name|LifecycleListener
block|{
DECL|field|systemLog
specifier|private
specifier|final
name|SystemLog
name|systemLog
decl_stmt|;
DECL|field|serverInfo
specifier|private
specifier|final
name|ServerInformation
name|serverInfo
decl_stmt|;
DECL|field|logName
specifier|private
specifier|final
name|String
name|logName
decl_stmt|;
DECL|field|layout
specifier|private
specifier|final
name|Layout
name|layout
decl_stmt|;
DECL|method|PluginLogFile ( SystemLog systemLog, ServerInformation serverInfo, String logName, Layout layout)
specifier|public
name|PluginLogFile
parameter_list|(
name|SystemLog
name|systemLog
parameter_list|,
name|ServerInformation
name|serverInfo
parameter_list|,
name|String
name|logName
parameter_list|,
name|Layout
name|layout
parameter_list|)
block|{
name|this
operator|.
name|systemLog
operator|=
name|systemLog
expr_stmt|;
name|this
operator|.
name|serverInfo
operator|=
name|serverInfo
expr_stmt|;
name|this
operator|.
name|logName
operator|=
name|logName
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|AsyncAppender
name|asyncAppender
init|=
name|systemLog
operator|.
name|createAsyncAppender
argument_list|(
name|logName
argument_list|,
name|layout
argument_list|)
decl_stmt|;
name|Logger
name|logger
init|=
name|LogManager
operator|.
name|getLogger
argument_list|(
name|logName
argument_list|)
decl_stmt|;
name|logger
operator|.
name|removeAppender
argument_list|(
name|logName
argument_list|)
expr_stmt|;
name|logger
operator|.
name|addAppender
argument_list|(
name|asyncAppender
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setAdditivity
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// stop is called when plugin is unloaded or when the server shutdown.
comment|// Only clean up when the server is shutting down to prevent issue when a
comment|// plugin is reloaded. The issue is that gerrit load the new plugin and then
comment|// unload the old one so because loggers are static, the unload of the old
comment|// plugin would remove the appenders just created by the new plugin.
if|if
condition|(
name|serverInfo
operator|.
name|getState
argument_list|()
operator|==
name|ServerInformation
operator|.
name|State
operator|.
name|SHUTDOWN
condition|)
block|{
name|LogManager
operator|.
name|getLogger
argument_list|(
name|logName
argument_list|)
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|common
operator|.
name|Die
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
name|server
operator|.
name|config
operator|.
name|SitePaths
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
name|util
operator|.
name|SystemLog
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
name|ConsoleAppender
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
name|Level
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|PatternLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_class
DECL|class|ErrorLogFile
specifier|public
class|class
name|ErrorLogFile
block|{
DECL|field|LOG_NAME
specifier|static
specifier|final
name|String
name|LOG_NAME
init|=
literal|"error_log"
decl_stmt|;
DECL|method|errorOnlyConsole ()
specifier|public
specifier|static
name|void
name|errorOnlyConsole
parameter_list|()
block|{
name|LogManager
operator|.
name|resetConfiguration
argument_list|()
expr_stmt|;
specifier|final
name|PatternLayout
name|layout
init|=
operator|new
name|PatternLayout
argument_list|()
decl_stmt|;
name|layout
operator|.
name|setConversionPattern
argument_list|(
literal|"%-5p %c %x: %m%n"
argument_list|)
expr_stmt|;
specifier|final
name|ConsoleAppender
name|dst
init|=
operator|new
name|ConsoleAppender
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setTarget
argument_list|(
literal|"System.err"
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setThreshold
argument_list|(
name|Level
operator|.
name|ERROR
argument_list|)
expr_stmt|;
specifier|final
name|Logger
name|root
init|=
name|LogManager
operator|.
name|getRootLogger
argument_list|()
decl_stmt|;
name|root
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
name|root
operator|.
name|addAppender
argument_list|(
name|dst
argument_list|)
expr_stmt|;
block|}
DECL|method|start (final File sitePath)
specifier|public
specifier|static
name|LifecycleListener
name|start
parameter_list|(
specifier|final
name|File
name|sitePath
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
specifier|final
name|File
name|logdir
init|=
operator|new
name|SitePaths
argument_list|(
name|sitePath
argument_list|)
operator|.
name|logs_dir
decl_stmt|;
if|if
condition|(
operator|!
name|logdir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|logdir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Die
argument_list|(
literal|"Cannot create log directory: "
operator|+
name|logdir
argument_list|)
throw|;
block|}
if|if
condition|(
name|SystemLog
operator|.
name|shouldConfigure
argument_list|()
condition|)
block|{
name|initLogSystem
argument_list|(
name|logdir
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|LifecycleListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{       }
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|LogManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|initLogSystem (final File logdir)
specifier|private
specifier|static
name|void
name|initLogSystem
parameter_list|(
specifier|final
name|File
name|logdir
parameter_list|)
block|{
specifier|final
name|Logger
name|root
init|=
name|LogManager
operator|.
name|getRootLogger
argument_list|()
decl_stmt|;
name|root
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
name|root
operator|.
name|addAppender
argument_list|(
name|SystemLog
operator|.
name|createAppender
argument_list|(
name|logdir
argument_list|,
name|LOG_NAME
argument_list|,
operator|new
name|PatternLayout
argument_list|(
literal|"[%d] %-5p %c %x: %m%n"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


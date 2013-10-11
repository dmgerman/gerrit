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
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Appender
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
name|DailyRollingFileAppender
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
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|helpers
operator|.
name|OnlyOnceErrorHandler
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
name|spi
operator|.
name|ErrorHandler
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
name|spi
operator|.
name|LoggingEvent
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|ErrorLogFile
specifier|public
class|class
name|ErrorLogFile
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|field|LOG4J_CONFIGURATION
specifier|private
specifier|static
specifier|final
name|String
name|LOG4J_CONFIGURATION
init|=
name|LogManager
operator|.
name|DEFAULT_CONFIGURATION_KEY
decl_stmt|;
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
name|shouldConfigureLogSystem
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
DECL|method|shouldConfigureLogSystem ()
specifier|public
specifier|static
name|boolean
name|shouldConfigureLogSystem
parameter_list|()
block|{
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|LOG4J_CONFIGURATION
argument_list|)
argument_list|)
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
literal|"[%d] %-5p %c %x: %m%n"
argument_list|)
expr_stmt|;
specifier|final
name|DailyRollingFileAppender
name|dst
init|=
operator|new
name|DailyRollingFileAppender
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setName
argument_list|(
name|LOG_NAME
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|resolve
argument_list|(
name|logdir
argument_list|)
argument_list|,
name|LOG_NAME
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setImmediateFlush
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setAppend
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setThreshold
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|DieErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|activateOptions
argument_list|()
expr_stmt|;
name|dst
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|OnlyOnceErrorHandler
argument_list|()
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
DECL|method|resolve (final File logs_dir)
specifier|private
specifier|static
name|File
name|resolve
parameter_list|(
specifier|final
name|File
name|logs_dir
parameter_list|)
block|{
try|try
block|{
return|return
name|logs_dir
operator|.
name|getCanonicalFile
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|logs_dir
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
block|}
DECL|method|ErrorLogFile ()
specifier|private
name|ErrorLogFile
parameter_list|()
block|{   }
DECL|class|DieErrorHandler
specifier|private
specifier|static
specifier|final
class|class
name|DieErrorHandler
implements|implements
name|ErrorHandler
block|{
annotation|@
name|Override
DECL|method|error (String message, Exception e, int errorCode, LoggingEvent event)
specifier|public
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|Exception
name|e
parameter_list|,
name|int
name|errorCode
parameter_list|,
name|LoggingEvent
name|event
parameter_list|)
block|{
name|error
argument_list|(
name|e
operator|!=
literal|null
condition|?
name|e
operator|.
name|getMessage
argument_list|()
else|:
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|error (String message, Exception e, int errorCode)
specifier|public
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|Exception
name|e
parameter_list|,
name|int
name|errorCode
parameter_list|)
block|{
name|error
argument_list|(
name|e
operator|!=
literal|null
condition|?
name|e
operator|.
name|getMessage
argument_list|()
else|:
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|error (String message)
specifier|public
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|Die
argument_list|(
literal|"Cannot open log file: "
operator|+
name|message
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|activateOptions ()
specifier|public
name|void
name|activateOptions
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|setAppender (Appender appender)
specifier|public
name|void
name|setAppender
parameter_list|(
name|Appender
name|appender
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|setBackupAppender (Appender appender)
specifier|public
name|void
name|setBackupAppender
parameter_list|(
name|Appender
name|appender
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|setLogger (Logger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit


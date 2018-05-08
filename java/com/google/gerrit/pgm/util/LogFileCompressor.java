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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|HOURS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
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
name|common
operator|.
name|io
operator|.
name|ByteStreams
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|GerritServerConfig
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
name|git
operator|.
name|WorkQueue
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|DirectoryStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|temporal
operator|.
name|ChronoUnit
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
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Compresses the old error logs. */
end_comment

begin_class
DECL|class|LogFileCompressor
specifier|public
class|class
name|LogFileCompressor
implements|implements
name|Runnable
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
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Lifecycle
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|queue
specifier|private
specifier|final
name|WorkQueue
name|queue
decl_stmt|;
DECL|field|compressor
specifier|private
specifier|final
name|LogFileCompressor
name|compressor
decl_stmt|;
DECL|field|enabled
specifier|private
specifier|final
name|boolean
name|enabled
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (WorkQueue queue, LogFileCompressor compressor, @GerritServerConfig Config config)
name|Lifecycle
parameter_list|(
name|WorkQueue
name|queue
parameter_list|,
name|LogFileCompressor
name|compressor
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|compressor
operator|=
name|compressor
expr_stmt|;
name|this
operator|.
name|enabled
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"log"
argument_list|,
literal|"compress"
argument_list|,
literal|true
argument_list|)
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
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return;
block|}
comment|// compress log once and then schedule compression every day at 11:00pm
name|queue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|execute
argument_list|(
name|compressor
argument_list|)
expr_stmt|;
name|ZoneId
name|zone
init|=
name|ZoneId
operator|.
name|systemDefault
argument_list|()
decl_stmt|;
name|LocalDateTime
name|now
init|=
name|LocalDateTime
operator|.
name|now
argument_list|(
name|zone
argument_list|)
decl_stmt|;
name|long
name|milliSecondsUntil11pm
init|=
name|now
operator|.
name|until
argument_list|(
name|now
operator|.
name|withHour
argument_list|(
literal|23
argument_list|)
operator|.
name|withMinute
argument_list|(
literal|0
argument_list|)
operator|.
name|withSecond
argument_list|(
literal|0
argument_list|)
operator|.
name|withNano
argument_list|(
literal|0
argument_list|)
argument_list|,
name|ChronoUnit
operator|.
name|MILLIS
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|queue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|scheduleAtFixedRate
argument_list|(
name|compressor
argument_list|,
name|milliSecondsUntil11pm
argument_list|,
name|HOURS
operator|.
name|toMillis
argument_list|(
literal|24
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
decl_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{}
block|}
DECL|field|logs_dir
specifier|private
specifier|final
name|Path
name|logs_dir
decl_stmt|;
annotation|@
name|Inject
DECL|method|LogFileCompressor (SitePaths site)
name|LogFileCompressor
parameter_list|(
name|SitePaths
name|site
parameter_list|)
block|{
name|logs_dir
operator|=
name|resolve
argument_list|(
name|site
operator|.
name|logs_dir
argument_list|)
expr_stmt|;
block|}
DECL|method|resolve (Path p)
specifier|private
specifier|static
name|Path
name|resolve
parameter_list|(
name|Path
name|p
parameter_list|)
block|{
try|try
block|{
return|return
name|p
operator|.
name|toRealPath
argument_list|()
operator|.
name|normalize
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
name|p
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|normalize
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|logs_dir
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|list
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|logs_dir
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|entry
range|:
name|list
control|)
block|{
if|if
condition|(
operator|!
name|isLive
argument_list|(
name|entry
argument_list|)
operator|&&
operator|!
name|isCompressed
argument_list|(
name|entry
argument_list|)
operator|&&
name|isLogFile
argument_list|(
name|entry
argument_list|)
condition|)
block|{
name|compress
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Error listing logs to compress in %s"
argument_list|,
name|logs_dir
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to compress log files: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isLive (Path entry)
specifier|private
name|boolean
name|isLive
parameter_list|(
name|Path
name|entry
parameter_list|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|"_log"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".log"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".run"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".pid"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
return|;
block|}
DECL|method|isCompressed (Path entry)
specifier|private
name|boolean
name|isCompressed
parameter_list|(
name|Path
name|entry
parameter_list|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|".gz"
argument_list|)
comment|//
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".zip"
argument_list|)
comment|//
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".bz2"
argument_list|)
return|;
block|}
DECL|method|isLogFile (Path entry)
specifier|private
name|boolean
name|isLogFile
parameter_list|(
name|Path
name|entry
parameter_list|)
block|{
return|return
name|Files
operator|.
name|isRegularFile
argument_list|(
name|entry
argument_list|)
return|;
block|}
DECL|method|compress (Path src)
specifier|private
name|void
name|compress
parameter_list|(
name|Path
name|src
parameter_list|)
block|{
name|Path
name|dst
init|=
name|src
operator|.
name|resolveSibling
argument_list|(
name|src
operator|.
name|getFileName
argument_list|()
operator|+
literal|".gz"
argument_list|)
decl_stmt|;
name|Path
name|tmp
init|=
name|src
operator|.
name|resolveSibling
argument_list|(
literal|".tmp."
operator|+
name|src
operator|.
name|getFileName
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|src
argument_list|)
init|;
name|OutputStream
name|out
operator|=
operator|new
name|GZIPOutputStream
argument_list|(
name|Files
operator|.
name|newOutputStream
argument_list|(
name|tmp
argument_list|)
argument_list|)
init|)
block|{
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|tmp
operator|.
name|toFile
argument_list|()
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
try|try
block|{
name|Files
operator|.
name|move
argument_list|(
name|tmp
argument_list|,
name|dst
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot rename "
operator|+
name|tmp
operator|+
literal|" to "
operator|+
name|dst
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Files
operator|.
name|delete
argument_list|(
name|src
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot compress %s"
argument_list|,
name|src
argument_list|)
expr_stmt|;
try|try
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e2
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to delete temporary log file %s"
argument_list|,
name|tmp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Log File Compressor"
return|;
block|}
block|}
end_class

end_unit


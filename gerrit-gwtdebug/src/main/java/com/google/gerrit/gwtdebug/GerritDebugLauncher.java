begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.gerrit.gwtdebug
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gwtdebug
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|ServletContainer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|ServletContainerLauncher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|TreeLogger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|UnableToCompleteException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|dev
operator|.
name|shell
operator|.
name|jetty
operator|.
name|JettyNullLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|component
operator|.
name|AbstractLifeCycle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|AbstractConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|HttpFields
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|RequestLog
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|RequestLogHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppClassLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|log
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|log
operator|.
name|Logger
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_class
DECL|class|GerritDebugLauncher
specifier|public
class|class
name|GerritDebugLauncher
extends|extends
name|ServletContainerLauncher
block|{
comment|/**    * Log jetty requests/responses to TreeLogger.    */
DECL|class|JettyRequestLogger
specifier|public
specifier|static
class|class
name|JettyRequestLogger
extends|extends
name|AbstractLifeCycle
implements|implements
name|RequestLog
block|{
DECL|field|logger
specifier|private
specifier|final
name|TreeLogger
name|logger
decl_stmt|;
DECL|method|JettyRequestLogger (TreeLogger logger)
specifier|public
name|JettyRequestLogger
parameter_list|(
name|TreeLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/**      * Log an HTTP request/response to TreeLogger.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|log (Request request, Response response)
specifier|public
name|void
name|log
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|int
name|status
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|<
literal|0
condition|)
block|{
comment|// Copied from NCSARequestLog
name|status
operator|=
literal|404
expr_stmt|;
block|}
name|TreeLogger
operator|.
name|Type
name|logStatus
decl_stmt|,
name|logHeaders
decl_stmt|;
if|if
condition|(
name|status
operator|>=
literal|500
condition|)
block|{
name|logStatus
operator|=
name|TreeLogger
operator|.
name|ERROR
expr_stmt|;
name|logHeaders
operator|=
name|TreeLogger
operator|.
name|INFO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|status
operator|>=
literal|400
condition|)
block|{
name|logStatus
operator|=
name|TreeLogger
operator|.
name|WARN
expr_stmt|;
name|logHeaders
operator|=
name|TreeLogger
operator|.
name|INFO
expr_stmt|;
block|}
else|else
block|{
name|logStatus
operator|=
name|TreeLogger
operator|.
name|INFO
expr_stmt|;
name|logHeaders
operator|=
name|TreeLogger
operator|.
name|DEBUG
expr_stmt|;
block|}
name|String
name|userString
init|=
name|request
operator|.
name|getRemoteUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|userString
operator|==
literal|null
condition|)
block|{
name|userString
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|userString
operator|+=
literal|"@"
expr_stmt|;
block|}
name|String
name|bytesString
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getContentCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bytesString
operator|=
literal|" "
operator|+
name|response
operator|.
name|getContentCount
argument_list|()
operator|+
literal|" bytes"
expr_stmt|;
block|}
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|logStatus
argument_list|)
condition|)
block|{
name|TreeLogger
name|branch
init|=
name|logger
operator|.
name|branch
argument_list|(
name|logStatus
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|status
argument_list|)
operator|+
literal|" - "
operator|+
name|request
operator|.
name|getMethod
argument_list|()
operator|+
literal|' '
operator|+
name|request
operator|.
name|getUri
argument_list|()
operator|+
literal|" ("
operator|+
name|userString
operator|+
name|request
operator|.
name|getRemoteHost
argument_list|()
operator|+
literal|')'
operator|+
name|bytesString
argument_list|)
decl_stmt|;
if|if
condition|(
name|branch
operator|.
name|isLoggable
argument_list|(
name|logHeaders
argument_list|)
condition|)
block|{
comment|// Request headers
name|TreeLogger
name|headers
init|=
name|branch
operator|.
name|branch
argument_list|(
name|logHeaders
argument_list|,
literal|"Request headers"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Field
argument_list|>
name|headerFields
init|=
name|request
operator|.
name|getConnection
argument_list|()
operator|.
name|getRequestFields
argument_list|()
operator|.
name|getFields
argument_list|()
decl_stmt|;
while|while
condition|(
name|headerFields
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Field
name|headerField
init|=
name|headerFields
operator|.
name|next
argument_list|()
decl_stmt|;
name|headers
operator|.
name|log
argument_list|(
name|logHeaders
argument_list|,
name|headerField
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|headerField
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Response headers
name|headers
operator|=
name|branch
operator|.
name|branch
argument_list|(
name|logHeaders
argument_list|,
literal|"Response headers"
argument_list|)
expr_stmt|;
name|headerFields
operator|=
name|response
operator|.
name|getHttpFields
argument_list|()
operator|.
name|getFields
argument_list|()
expr_stmt|;
while|while
condition|(
name|headerFields
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Field
name|headerField
init|=
name|headerFields
operator|.
name|next
argument_list|()
decl_stmt|;
name|headers
operator|.
name|log
argument_list|(
name|logHeaders
argument_list|,
name|headerField
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|headerField
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**    * An adapter for the Jetty logging system to GWT's TreeLogger. This    * implementation class is only public to allow {@link Log} to instantiate it.    *    * The weird static data / default construction setup is a game we play with    * {@link Log}'s static initializer to prevent the initial log message from    * going to stderr.    */
DECL|class|JettyTreeLogger
specifier|public
specifier|static
class|class
name|JettyTreeLogger
implements|implements
name|Logger
block|{
DECL|field|logger
specifier|private
specifier|final
name|TreeLogger
name|logger
decl_stmt|;
DECL|method|JettyTreeLogger (TreeLogger logger)
specifier|public
name|JettyTreeLogger
parameter_list|(
name|TreeLogger
name|logger
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
DECL|method|debug (String msg, Object arg0, Object arg1)
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|,
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|SPAM
argument_list|,
name|format
argument_list|(
name|msg
argument_list|,
name|arg0
argument_list|,
name|arg1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|debug (String msg, Throwable th)
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|SPAM
argument_list|,
name|msg
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
DECL|method|getLogger (String name)
specifier|public
name|Logger
name|getLogger
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|info (String msg, Object arg0, Object arg1)
specifier|public
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|,
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
name|format
argument_list|(
name|msg
argument_list|,
name|arg0
argument_list|,
name|arg1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isDebugEnabled ()
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isLoggable
argument_list|(
name|TreeLogger
operator|.
name|SPAM
argument_list|)
return|;
block|}
DECL|method|setDebugEnabled (boolean enabled)
specifier|public
name|void
name|setDebugEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
comment|// ignored
block|}
DECL|method|warn (String msg, Object arg0, Object arg1)
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|,
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|WARN
argument_list|,
name|format
argument_list|(
name|msg
argument_list|,
name|arg0
argument_list|,
name|arg1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|warn (String msg, Throwable th)
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|WARN
argument_list|,
name|msg
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
comment|/**      * Copied from org.mortbay.log.StdErrLog.      */
DECL|method|format (String msg, Object arg0, Object arg1)
specifier|private
name|String
name|format
parameter_list|(
name|String
name|msg
parameter_list|,
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
name|int
name|i0
init|=
name|msg
operator|.
name|indexOf
argument_list|(
literal|"{}"
argument_list|)
decl_stmt|;
name|int
name|i1
init|=
name|i0
operator|<
literal|0
condition|?
operator|-
literal|1
else|:
name|msg
operator|.
name|indexOf
argument_list|(
literal|"{}"
argument_list|,
name|i0
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|arg1
operator|!=
literal|null
operator|&&
name|i1
operator|>=
literal|0
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i1
argument_list|)
operator|+
name|arg1
operator|+
name|msg
operator|.
name|substring
argument_list|(
name|i1
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arg0
operator|!=
literal|null
operator|&&
name|i0
operator|>=
literal|0
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i0
argument_list|)
operator|+
name|arg0
operator|+
name|msg
operator|.
name|substring
argument_list|(
name|i0
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
return|;
block|}
block|}
comment|/**    * The resulting {@link ServletContainer} this is launched.    */
DECL|class|JettyServletContainer
specifier|protected
specifier|static
class|class
name|JettyServletContainer
extends|extends
name|ServletContainer
block|{
DECL|field|actualPort
specifier|private
specifier|final
name|int
name|actualPort
decl_stmt|;
DECL|field|appRootDir
specifier|private
specifier|final
name|File
name|appRootDir
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|TreeLogger
name|logger
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|Server
name|server
decl_stmt|;
DECL|field|wac
specifier|private
specifier|final
name|WebAppContext
name|wac
decl_stmt|;
DECL|method|JettyServletContainer (TreeLogger logger, Server server, WebAppContext wac, int actualPort, File appRootDir)
specifier|public
name|JettyServletContainer
parameter_list|(
name|TreeLogger
name|logger
parameter_list|,
name|Server
name|server
parameter_list|,
name|WebAppContext
name|wac
parameter_list|,
name|int
name|actualPort
parameter_list|,
name|File
name|appRootDir
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|this
operator|.
name|wac
operator|=
name|wac
expr_stmt|;
name|this
operator|.
name|actualPort
operator|=
name|actualPort
expr_stmt|;
name|this
operator|.
name|appRootDir
operator|=
name|appRootDir
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|actualPort
return|;
block|}
annotation|@
name|Override
DECL|method|refresh ()
specifier|public
name|void
name|refresh
parameter_list|()
throws|throws
name|UnableToCompleteException
block|{
name|String
name|msg
init|=
literal|"Reloading web app to reflect changes in "
operator|+
name|appRootDir
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|TreeLogger
name|branch
init|=
name|logger
operator|.
name|branch
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
name|msg
argument_list|)
decl_stmt|;
comment|// Temporarily log Jetty on the branch.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|branch
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|wac
operator|.
name|stop
argument_list|()
expr_stmt|;
name|wac
operator|.
name|start
argument_list|()
expr_stmt|;
name|branch
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
literal|"Reload completed successfully"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|branch
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|ERROR
argument_list|,
literal|"Unable to restart embedded Jetty server"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnableToCompleteException
argument_list|()
throw|;
block|}
finally|finally
block|{
comment|// Reset the top-level logger.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|UnableToCompleteException
block|{
name|TreeLogger
name|branch
init|=
name|logger
operator|.
name|branch
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
literal|"Stopping Jetty server"
argument_list|)
decl_stmt|;
comment|// Temporarily log Jetty on the branch.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|branch
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|setStopAtShutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|branch
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
literal|"Stopped successfully"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|branch
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|ERROR
argument_list|,
literal|"Unable to stop embedded Jetty server"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnableToCompleteException
argument_list|()
throw|;
block|}
finally|finally
block|{
comment|// Reset the top-level logger.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * A {@link WebAppContext} tailored to GWT hosted mode. Features hot-reload    * with a new {@link WebAppClassLoader} to pick up disk changes. The default    * Jetty {@code WebAppContext} will create new instances of servlets, but it    * will not create a brand new {@link ClassLoader}. By creating a new {@code    * ClassLoader} each time, we re-read updated classes from disk.    *    * Also provides special class filtering to isolate the web app from the GWT    * hosting environment.    */
DECL|class|MyWebAppContext
specifier|protected
specifier|final
class|class
name|MyWebAppContext
extends|extends
name|WebAppContext
block|{
comment|/**      * Parent ClassLoader for the Jetty web app, which can only load JVM      * classes. We would just use<code>null</code> for the parent ClassLoader      * except this makes Jetty unhappy.      */
DECL|field|bootStrapOnlyClassLoader
specifier|private
specifier|final
name|ClassLoader
name|bootStrapOnlyClassLoader
init|=
operator|new
name|ClassLoader
argument_list|(
literal|null
argument_list|)
block|{}
decl_stmt|;
DECL|field|systemClassLoader
specifier|private
specifier|final
name|ClassLoader
name|systemClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|MyWebAppContext (String webApp, String contextPath)
specifier|private
name|MyWebAppContext
parameter_list|(
name|String
name|webApp
parameter_list|,
name|String
name|contextPath
parameter_list|)
block|{
name|super
argument_list|(
name|webApp
argument_list|,
name|contextPath
argument_list|)
expr_stmt|;
comment|// Prevent file locking on Windows; pick up file changes.
name|getInitParams
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.mortbay.jetty.servlet.Default.useFileMappedBuffer"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// Since the parent class loader is bootstrap-only, prefer it first.
name|setParentLoaderPriority
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|setClassLoader
argument_list|(
operator|new
name|MyLoader
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|setClassLoader
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|class|MyLoader
specifier|private
class|class
name|MyLoader
extends|extends
name|WebAppClassLoader
block|{
DECL|method|MyLoader ()
name|MyLoader
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|bootStrapOnlyClassLoader
argument_list|,
name|MyWebAppContext
operator|.
name|this
argument_list|)
expr_stmt|;
specifier|final
name|URLClassLoader
name|scl
init|=
operator|(
name|URLClassLoader
operator|)
name|systemClassLoader
decl_stmt|;
specifier|final
name|URL
index|[]
name|urls
init|=
name|scl
operator|.
name|getURLs
argument_list|()
decl_stmt|;
for|for
control|(
name|URL
name|u
range|:
name|urls
control|)
block|{
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|u
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|addClassPath
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isSystemPath (String name)
specifier|public
name|boolean
name|isSystemPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|name
operator|=
name|name
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|isSystemPath
argument_list|(
name|name
argument_list|)
comment|//
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"org.bouncycastle."
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|findClass (String name)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
comment|// For system path, always prefer the outside world.
if|if
condition|(
name|isSystemPath
argument_list|(
name|name
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|systemClassLoader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{           }
block|}
return|return
name|super
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
static|static
block|{
comment|// Suppress spammy Jetty log initialization.
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.mortbay.log.class"
argument_list|,
name|JettyNullLogger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Log
operator|.
name|getLog
argument_list|()
expr_stmt|;
comment|/*      * Make JDT the default Ant compiler so that JSP compilation just works      * out-of-the-box. If we don't set this, it's very, very difficult to make      * JSP compilation work.      */
name|String
name|antJavaC
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"build.compiler"
argument_list|,
literal|"org.eclipse.jdt.core.JDTCompilerAdapter"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"build.compiler"
argument_list|,
name|antJavaC
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"Gerrit.GwtDevMode"
argument_list|,
literal|""
operator|+
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|field|bindAddress
specifier|private
name|String
name|bindAddress
init|=
literal|null
decl_stmt|;
annotation|@
name|Override
DECL|method|setBindAddress (String bindAddress)
specifier|public
name|void
name|setBindAddress
parameter_list|(
name|String
name|bindAddress
parameter_list|)
block|{
name|this
operator|.
name|bindAddress
operator|=
name|bindAddress
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start (TreeLogger logger, int port, File warDir)
specifier|public
name|ServletContainer
name|start
parameter_list|(
name|TreeLogger
name|logger
parameter_list|,
name|int
name|port
parameter_list|,
name|File
name|warDir
parameter_list|)
throws|throws
name|Exception
block|{
name|TreeLogger
name|branch
init|=
name|logger
operator|.
name|branch
argument_list|(
name|TreeLogger
operator|.
name|INFO
argument_list|,
literal|"Starting Jetty on port "
operator|+
name|port
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkStartParams
argument_list|(
name|branch
argument_list|,
name|port
argument_list|,
name|warDir
argument_list|)
expr_stmt|;
comment|// Setup our branch logger during startup.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|branch
argument_list|)
argument_list|)
expr_stmt|;
comment|// Turn off XML validation.
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.mortbay.xml.XmlParser.Validating"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|AbstractConnector
name|connector
init|=
name|getConnector
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindAddress
operator|!=
literal|null
condition|)
block|{
name|connector
operator|.
name|setHost
argument_list|(
name|bindAddress
argument_list|)
expr_stmt|;
block|}
name|connector
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
comment|// Don't share ports with an existing process.
name|connector
operator|.
name|setReuseAddress
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Linux keeps the port blocked after shutdown if we don't disable this.
name|connector
operator|.
name|setSoLingerTime
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|File
name|top
decl_stmt|;
name|String
name|root
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.source_root"
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|top
operator|=
operator|new
name|File
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Under Maven warDir is "$top/gerrit-gwtui/target/gwt-hosted-mode"
name|top
operator|=
name|warDir
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
name|File
name|app
init|=
operator|new
name|File
argument_list|(
name|top
argument_list|,
literal|"gerrit-war/src/main/webapp"
argument_list|)
decl_stmt|;
name|File
name|webxml
init|=
operator|new
name|File
argument_list|(
name|app
argument_list|,
literal|"WEB-INF/web.xml"
argument_list|)
decl_stmt|;
comment|// Jetty won't start unless this directory exists.
if|if
condition|(
operator|!
name|warDir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|warDir
operator|.
name|mkdirs
argument_list|()
condition|)
name|logger
operator|.
name|branch
argument_list|(
name|TreeLogger
operator|.
name|ERROR
argument_list|,
literal|"Cannot create "
operator|+
name|warDir
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Create a new web app in the war directory.
comment|//
name|WebAppContext
name|wac
init|=
operator|new
name|MyWebAppContext
argument_list|(
name|warDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|wac
operator|.
name|setDescriptor
argument_list|(
name|webxml
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|RequestLogHandler
name|logHandler
init|=
operator|new
name|RequestLogHandler
argument_list|()
decl_stmt|;
name|logHandler
operator|.
name|setRequestLog
argument_list|(
operator|new
name|JettyRequestLogger
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
name|logHandler
operator|.
name|setHandler
argument_list|(
name|wac
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|server
operator|.
name|setStopAtShutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Now that we're started, log to the top level logger.
name|Log
operator|.
name|setLog
argument_list|(
operator|new
name|JettyTreeLogger
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|JettyServletContainer
argument_list|(
name|logger
argument_list|,
name|server
argument_list|,
name|wac
argument_list|,
name|connector
operator|.
name|getLocalPort
argument_list|()
argument_list|,
name|warDir
argument_list|)
return|;
block|}
DECL|method|getConnector ()
specifier|protected
name|AbstractConnector
name|getConnector
parameter_list|()
block|{
return|return
operator|new
name|SelectChannelConnector
argument_list|()
return|;
block|}
DECL|method|checkStartParams (TreeLogger logger, int port, File appRootDir)
specifier|private
name|void
name|checkStartParams
parameter_list|(
name|TreeLogger
name|logger
parameter_list|,
name|int
name|port
parameter_list|,
name|File
name|appRootDir
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"logger cannot be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|port
argument_list|<
literal|0
operator|||
name|port
argument_list|>
literal|65535
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"port must be either 0 (for auto) or less than 65536"
argument_list|)
throw|;
block|}
if|if
condition|(
name|appRootDir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"app root direcotry cannot be null"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


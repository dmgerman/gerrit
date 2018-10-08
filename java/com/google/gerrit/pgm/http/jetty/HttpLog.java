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
DECL|package|com.google.gerrit.pgm.http.jetty
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|http
operator|.
name|jetty
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
name|httpd
operator|.
name|GetUserFilter
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
name|httpd
operator|.
name|restapi
operator|.
name|LogRedactUtil
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
name|time
operator|.
name|TimeUtil
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
name|spi
operator|.
name|LoggingEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|RequestLog
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|component
operator|.
name|AbstractLifeCycle
import|;
end_import

begin_comment
comment|/** Writes the {@code httpd_log} file with per-request data. */
end_comment

begin_class
DECL|class|HttpLog
class|class
name|HttpLog
extends|extends
name|AbstractLifeCycle
implements|implements
name|RequestLog
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|HttpLog
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOG_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LOG_NAME
init|=
literal|"httpd_log"
decl_stmt|;
DECL|interface|HttpLogFactory
interface|interface
name|HttpLogFactory
block|{
DECL|method|get ()
name|HttpLog
name|get
parameter_list|()
function_decl|;
block|}
DECL|field|P_HOST
specifier|protected
specifier|static
specifier|final
name|String
name|P_HOST
init|=
literal|"Host"
decl_stmt|;
DECL|field|P_USER
specifier|protected
specifier|static
specifier|final
name|String
name|P_USER
init|=
literal|"User"
decl_stmt|;
DECL|field|P_METHOD
specifier|protected
specifier|static
specifier|final
name|String
name|P_METHOD
init|=
literal|"Method"
decl_stmt|;
DECL|field|P_RESOURCE
specifier|protected
specifier|static
specifier|final
name|String
name|P_RESOURCE
init|=
literal|"Resource"
decl_stmt|;
DECL|field|P_PROTOCOL
specifier|protected
specifier|static
specifier|final
name|String
name|P_PROTOCOL
init|=
literal|"Version"
decl_stmt|;
DECL|field|P_STATUS
specifier|protected
specifier|static
specifier|final
name|String
name|P_STATUS
init|=
literal|"Status"
decl_stmt|;
DECL|field|P_CONTENT_LENGTH
specifier|protected
specifier|static
specifier|final
name|String
name|P_CONTENT_LENGTH
init|=
literal|"Content-Length"
decl_stmt|;
DECL|field|P_REFERER
specifier|protected
specifier|static
specifier|final
name|String
name|P_REFERER
init|=
literal|"Referer"
decl_stmt|;
DECL|field|P_USER_AGENT
specifier|protected
specifier|static
specifier|final
name|String
name|P_USER_AGENT
init|=
literal|"User-Agent"
decl_stmt|;
DECL|field|async
specifier|private
specifier|final
name|AsyncAppender
name|async
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpLog (SystemLog systemLog)
name|HttpLog
parameter_list|(
name|SystemLog
name|systemLog
parameter_list|)
block|{
name|async
operator|=
name|systemLog
operator|.
name|createAsyncAppender
argument_list|(
name|LOG_NAME
argument_list|,
operator|new
name|HttpLogLayout
argument_list|()
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
block|{}
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
name|async
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|log (Request req, Response rsp)
specifier|public
name|void
name|log
parameter_list|(
name|Request
name|req
parameter_list|,
name|Response
name|rsp
parameter_list|)
block|{
specifier|final
name|LoggingEvent
name|event
init|=
operator|new
name|LoggingEvent
argument_list|(
comment|//
name|Logger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
comment|// fqnOfCategoryClass
name|log
argument_list|,
comment|// logger
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|,
comment|// when
name|Level
operator|.
name|INFO
argument_list|,
comment|// level
literal|""
argument_list|,
comment|// message text
literal|"HTTPD"
argument_list|,
comment|// thread name
literal|null
argument_list|,
comment|// exception information
literal|null
argument_list|,
comment|// current NDC string
literal|null
argument_list|,
comment|// caller location
literal|null
comment|// MDC properties
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
name|req
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|req
operator|.
name|getQueryString
argument_list|()
argument_list|)
condition|)
block|{
name|uri
operator|+=
literal|"?"
operator|+
name|LogRedactUtil
operator|.
name|redactQueryString
argument_list|(
name|req
operator|.
name|getQueryString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|user
init|=
operator|(
name|String
operator|)
name|req
operator|.
name|getAttribute
argument_list|(
name|GetUserFilter
operator|.
name|USER_ATTR_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|setProperty
argument_list|(
name|P_USER
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|set
argument_list|(
name|event
argument_list|,
name|P_HOST
argument_list|,
name|req
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_METHOD
argument_list|,
name|req
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_RESOURCE
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_PROTOCOL
argument_list|,
name|req
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_STATUS
argument_list|,
name|rsp
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_CONTENT_LENGTH
argument_list|,
name|rsp
operator|.
name|getContentCount
argument_list|()
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_REFERER
argument_list|,
name|req
operator|.
name|getHeader
argument_list|(
literal|"Referer"
argument_list|)
argument_list|)
expr_stmt|;
name|set
argument_list|(
name|event
argument_list|,
name|P_USER_AGENT
argument_list|,
name|req
operator|.
name|getHeader
argument_list|(
literal|"User-Agent"
argument_list|)
argument_list|)
expr_stmt|;
name|async
operator|.
name|append
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|set (LoggingEvent event, String key, String val)
specifier|private
specifier|static
name|void
name|set
parameter_list|(
name|LoggingEvent
name|event
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
operator|!
name|val
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|event
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|set (LoggingEvent event, String key, long val)
specifier|private
specifier|static
name|void
name|set
parameter_list|(
name|LoggingEvent
name|event
parameter_list|,
name|String
name|key
parameter_list|,
name|long
name|val
parameter_list|)
block|{
if|if
condition|(
literal|0
operator|<
name|val
condition|)
block|{
name|event
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


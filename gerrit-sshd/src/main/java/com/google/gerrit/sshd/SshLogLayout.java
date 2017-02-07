begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|jgit
operator|.
name|util
operator|.
name|QuotedString
import|;
end_import

begin_class
DECL|class|SshLogLayout
specifier|public
specifier|final
class|class
name|SshLogLayout
extends|extends
name|Layout
block|{
DECL|field|P_SESSION
specifier|private
specifier|static
specifier|final
name|String
name|P_SESSION
init|=
literal|"session"
decl_stmt|;
DECL|field|P_USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|P_USER_NAME
init|=
literal|"userName"
decl_stmt|;
DECL|field|P_ACCOUNT_ID
specifier|private
specifier|static
specifier|final
name|String
name|P_ACCOUNT_ID
init|=
literal|"accountId"
decl_stmt|;
DECL|field|P_WAIT
specifier|private
specifier|static
specifier|final
name|String
name|P_WAIT
init|=
literal|"queueWaitTime"
decl_stmt|;
DECL|field|P_EXEC
specifier|private
specifier|static
specifier|final
name|String
name|P_EXEC
init|=
literal|"executionTime"
decl_stmt|;
DECL|field|P_STATUS
specifier|private
specifier|static
specifier|final
name|String
name|P_STATUS
init|=
literal|"status"
decl_stmt|;
DECL|field|P_AGENT
specifier|private
specifier|static
specifier|final
name|String
name|P_AGENT
init|=
literal|"agent"
decl_stmt|;
DECL|field|calendar
specifier|private
specifier|final
name|Calendar
name|calendar
decl_stmt|;
DECL|field|lastTimeMillis
specifier|private
name|long
name|lastTimeMillis
decl_stmt|;
DECL|field|lastTimeString
specifier|private
specifier|final
name|char
index|[]
name|lastTimeString
init|=
operator|new
name|char
index|[
literal|20
index|]
decl_stmt|;
DECL|field|tzFormat
specifier|private
specifier|final
name|SimpleDateFormat
name|tzFormat
decl_stmt|;
DECL|field|timeZone
specifier|private
name|char
index|[]
name|timeZone
decl_stmt|;
DECL|method|SshLogLayout ()
specifier|public
name|SshLogLayout
parameter_list|()
block|{
specifier|final
name|TimeZone
name|tz
init|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|calendar
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|tz
argument_list|)
expr_stmt|;
name|tzFormat
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"Z"
argument_list|)
expr_stmt|;
name|tzFormat
operator|.
name|setTimeZone
argument_list|(
name|tz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format (LoggingEvent event)
specifier|public
name|String
name|format
parameter_list|(
name|LoggingEvent
name|event
parameter_list|)
block|{
specifier|final
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|formatDate
argument_list|(
name|event
operator|.
name|getTimeStamp
argument_list|()
argument_list|,
name|buf
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
name|req
argument_list|(
name|P_SESSION
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|req
argument_list|(
name|P_USER_NAME
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|req
argument_list|(
name|P_ACCOUNT_ID
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|event
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|opt
argument_list|(
name|P_WAIT
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|opt
argument_list|(
name|P_EXEC
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|opt
argument_list|(
name|P_STATUS
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|opt
argument_list|(
name|P_AGENT
argument_list|,
name|buf
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|formatDate (final long now, final StringBuffer sbuf)
specifier|private
name|void
name|formatDate
parameter_list|(
specifier|final
name|long
name|now
parameter_list|,
specifier|final
name|StringBuffer
name|sbuf
parameter_list|)
block|{
specifier|final
name|int
name|millis
init|=
call|(
name|int
call|)
argument_list|(
name|now
operator|%
literal|1000
argument_list|)
decl_stmt|;
specifier|final
name|long
name|rounded
init|=
name|now
operator|-
name|millis
decl_stmt|;
if|if
condition|(
name|rounded
operator|!=
name|lastTimeMillis
condition|)
block|{
synchronized|synchronized
init|(
name|calendar
init|)
block|{
specifier|final
name|int
name|start
init|=
name|sbuf
operator|.
name|length
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|setTimeInMillis
argument_list|(
name|rounded
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|toTwoDigits
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|toTwoDigits
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|toTwoDigits
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|toTwoDigits
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|toTwoDigits
argument_list|(
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|getChars
argument_list|(
name|start
argument_list|,
name|sbuf
operator|.
name|length
argument_list|()
argument_list|,
name|lastTimeString
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|lastTimeMillis
operator|=
name|rounded
expr_stmt|;
name|timeZone
operator|=
name|tzFormat
operator|.
name|format
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|)
operator|.
name|toCharArray
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|sbuf
operator|.
name|append
argument_list|(
name|lastTimeString
argument_list|)
expr_stmt|;
block|}
name|sbuf
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%03d"
argument_list|,
name|millis
argument_list|)
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|timeZone
argument_list|)
expr_stmt|;
block|}
DECL|method|toTwoDigits (int input)
specifier|private
name|String
name|toTwoDigits
parameter_list|(
name|int
name|input
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%02d"
argument_list|,
name|input
argument_list|)
return|;
block|}
DECL|method|req (String key, StringBuffer buf, LoggingEvent event)
specifier|private
name|void
name|req
parameter_list|(
name|String
name|key
parameter_list|,
name|StringBuffer
name|buf
parameter_list|,
name|LoggingEvent
name|event
parameter_list|)
block|{
name|Object
name|val
init|=
name|event
operator|.
name|getMDC
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|val
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|s
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|QuotedString
operator|.
name|BOURNE
operator|.
name|quote
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|opt (String key, StringBuffer buf, LoggingEvent event)
specifier|private
name|void
name|opt
parameter_list|(
name|String
name|key
parameter_list|,
name|StringBuffer
name|buf
parameter_list|,
name|LoggingEvent
name|event
parameter_list|)
block|{
name|Object
name|val
init|=
name|event
operator|.
name|getMDC
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|ignoresThrowable ()
specifier|public
name|boolean
name|ignoresThrowable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|activateOptions ()
specifier|public
name|void
name|activateOptions
parameter_list|()
block|{}
block|}
end_class

end_unit


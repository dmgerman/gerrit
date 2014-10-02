begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|annotations
operator|.
name|VisibleForTesting
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

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|LocalTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|MutableDateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|ISODateTimeFormat
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

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
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
name|TimeUnit
import|;
end_import

begin_class
DECL|class|ScheduleConfig
specifier|public
class|class
name|ScheduleConfig
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
name|ScheduleConfig
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MISSING_CONFIG
specifier|public
specifier|static
specifier|final
name|long
name|MISSING_CONFIG
init|=
operator|-
literal|1L
decl_stmt|;
DECL|field|INVALID_CONFIG
specifier|public
specifier|static
specifier|final
name|long
name|INVALID_CONFIG
init|=
operator|-
literal|2L
decl_stmt|;
DECL|field|KEY_INTERVAL
specifier|private
specifier|static
specifier|final
name|String
name|KEY_INTERVAL
init|=
literal|"interval"
decl_stmt|;
DECL|field|KEY_STARTTIME
specifier|private
specifier|static
specifier|final
name|String
name|KEY_STARTTIME
init|=
literal|"startTime"
decl_stmt|;
DECL|field|initialDelay
specifier|private
specifier|final
name|long
name|initialDelay
decl_stmt|;
DECL|field|interval
specifier|private
specifier|final
name|long
name|interval
decl_stmt|;
DECL|method|ScheduleConfig (Config rc, String section)
specifier|public
name|ScheduleConfig
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|)
block|{
name|this
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduleConfig (Config rc, String section, String subsection)
specifier|public
name|ScheduleConfig
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|)
block|{
name|this
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|DateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduleConfig (Config rc, String section, String subsection, String keyInterval, String keyStartTime)
specifier|public
name|ScheduleConfig
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|keyInterval
parameter_list|,
name|String
name|keyStartTime
parameter_list|)
block|{
name|this
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|keyInterval
argument_list|,
name|keyStartTime
argument_list|,
name|DateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|ScheduleConfig (Config rc, String section, String subsection, DateTime now)
name|ScheduleConfig
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|DateTime
name|now
parameter_list|)
block|{
name|this
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|KEY_INTERVAL
argument_list|,
name|KEY_STARTTIME
argument_list|,
name|now
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|ScheduleConfig (Config rc, String section, String subsection, String keyInterval, String keyStartTime, DateTime now)
name|ScheduleConfig
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|keyInterval
parameter_list|,
name|String
name|keyStartTime
parameter_list|,
name|DateTime
name|now
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|keyInterval
argument_list|)
expr_stmt|;
if|if
condition|(
name|interval
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|keyStartTime
argument_list|,
name|now
argument_list|,
name|interval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|initialDelay
operator|=
name|interval
expr_stmt|;
block|}
block|}
comment|/**    * Milliseconds between constructor invocation and first event time.    *<p>    * If there is any lag between the constructor invocation and queuing the    * object into an executor the event will run later, as there is no method    * to adjust for the scheduling delay.    */
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
comment|/** Number of milliseconds between events. */
DECL|method|getInterval ()
specifier|public
name|long
name|getInterval
parameter_list|()
block|{
return|return
name|interval
return|;
block|}
DECL|method|interval (Config rc, String section, String subsection, String keyInterval)
specifier|private
specifier|static
name|long
name|interval
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|keyInterval
parameter_list|)
block|{
name|long
name|interval
init|=
name|MISSING_CONFIG
decl_stmt|;
try|try
block|{
name|interval
operator|=
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|rc
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|keyInterval
argument_list|,
operator|-
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|interval
operator|==
name|MISSING_CONFIG
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"{0} schedule parameter \"{0}.{1}\" is not configured"
argument_list|,
name|section
argument_list|,
name|keyInterval
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"Invalid {0} schedule parameter \"{0}.{1}\""
argument_list|,
name|section
argument_list|,
name|keyInterval
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|interval
operator|=
name|INVALID_CONFIG
expr_stmt|;
block|}
return|return
name|interval
return|;
block|}
DECL|method|initialDelay (Config rc, String section, String subsection, String keyStartTime, DateTime now, long interval)
specifier|private
specifier|static
name|long
name|initialDelay
parameter_list|(
name|Config
name|rc
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|keyStartTime
parameter_list|,
name|DateTime
name|now
parameter_list|,
name|long
name|interval
parameter_list|)
block|{
name|long
name|delay
init|=
name|MISSING_CONFIG
decl_stmt|;
name|String
name|start
init|=
name|rc
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|keyStartTime
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|start
operator|!=
literal|null
condition|)
block|{
name|DateTimeFormatter
name|formatter
decl_stmt|;
name|MutableDateTime
name|startTime
init|=
name|now
operator|.
name|toMutableDateTime
argument_list|()
decl_stmt|;
try|try
block|{
name|formatter
operator|=
name|ISODateTimeFormat
operator|.
name|hourMinute
argument_list|()
expr_stmt|;
name|LocalTime
name|firstStartTime
init|=
name|formatter
operator|.
name|parseLocalTime
argument_list|(
name|start
argument_list|)
decl_stmt|;
name|startTime
operator|.
name|hourOfDay
argument_list|()
operator|.
name|set
argument_list|(
name|firstStartTime
operator|.
name|getHourOfDay
argument_list|()
argument_list|)
expr_stmt|;
name|startTime
operator|.
name|minuteOfHour
argument_list|()
operator|.
name|set
argument_list|(
name|firstStartTime
operator|.
name|getMinuteOfHour
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e1
parameter_list|)
block|{
name|formatter
operator|=
name|DateTimeFormat
operator|.
name|forPattern
argument_list|(
literal|"E HH:mm"
argument_list|)
expr_stmt|;
name|LocalDateTime
name|firstStartDateTime
init|=
name|formatter
operator|.
name|parseLocalDateTime
argument_list|(
name|start
argument_list|)
decl_stmt|;
name|startTime
operator|.
name|dayOfWeek
argument_list|()
operator|.
name|set
argument_list|(
name|firstStartDateTime
operator|.
name|getDayOfWeek
argument_list|()
argument_list|)
expr_stmt|;
name|startTime
operator|.
name|hourOfDay
argument_list|()
operator|.
name|set
argument_list|(
name|firstStartDateTime
operator|.
name|getHourOfDay
argument_list|()
argument_list|)
expr_stmt|;
name|startTime
operator|.
name|minuteOfHour
argument_list|()
operator|.
name|set
argument_list|(
name|firstStartDateTime
operator|.
name|getMinuteOfHour
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|startTime
operator|.
name|secondOfMinute
argument_list|()
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|startTime
operator|.
name|millisOfSecond
argument_list|()
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|long
name|s
init|=
name|startTime
operator|.
name|getMillis
argument_list|()
decl_stmt|;
name|long
name|n
init|=
name|now
operator|.
name|getMillis
argument_list|()
decl_stmt|;
name|delay
operator|=
operator|(
name|s
operator|-
name|n
operator|)
operator|%
name|interval
expr_stmt|;
if|if
condition|(
name|delay
operator|<=
literal|0
condition|)
block|{
name|delay
operator|+=
name|interval
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"{0} schedule parameter \"{0}.{1}\" is not configured"
argument_list|,
name|section
argument_list|,
name|keyStartTime
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e2
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"Invalid {0} schedule parameter \"{0}.{1}\""
argument_list|,
name|section
argument_list|,
name|keyStartTime
argument_list|)
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|delay
operator|=
name|INVALID_CONFIG
expr_stmt|;
block|}
return|return
name|delay
return|;
block|}
block|}
end_class

end_unit


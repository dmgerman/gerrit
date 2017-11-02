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
name|time
operator|.
name|DayOfWeek
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|java
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
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
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
name|Locale
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
DECL|field|rc
specifier|private
specifier|final
name|Config
name|rc
decl_stmt|;
DECL|field|section
specifier|private
specifier|final
name|String
name|section
decl_stmt|;
DECL|field|subsection
specifier|private
specifier|final
name|String
name|subsection
decl_stmt|;
DECL|field|keyInterval
specifier|private
specifier|final
name|String
name|keyInterval
decl_stmt|;
DECL|field|keyStartTime
specifier|private
specifier|final
name|String
name|keyStartTime
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
name|ZonedDateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduleConfig ( Config rc, String section, String subsection, String keyInterval, String keyStartTime)
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
name|ZonedDateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|ScheduleConfig (Config rc, String section, String subsection, ZonedDateTime now)
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
name|ZonedDateTime
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
DECL|method|ScheduleConfig ( Config rc, String section, String subsection, String keyInterval, String keyStartTime, ZonedDateTime now)
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
name|ZonedDateTime
name|now
parameter_list|)
block|{
name|this
operator|.
name|rc
operator|=
name|rc
expr_stmt|;
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
name|this
operator|.
name|subsection
operator|=
name|subsection
expr_stmt|;
name|this
operator|.
name|keyInterval
operator|=
name|keyInterval
expr_stmt|;
name|this
operator|.
name|keyStartTime
operator|=
name|keyStartTime
expr_stmt|;
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
comment|/**    * Milliseconds between constructor invocation and first event time.    *    *<p>If there is any lag between the constructor invocation and queuing the object into an    * executor the event will run later, as there is no method to adjust for the scheduling delay.    */
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
DECL|method|initialDelay ( Config rc, String section, String subsection, String keyStartTime, ZonedDateTime now, long interval)
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
name|ZonedDateTime
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
init|=
name|DateTimeFormatter
operator|.
name|ofPattern
argument_list|(
literal|"[E ]HH:mm"
argument_list|)
operator|.
name|withLocale
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|LocalTime
name|firstStartTime
init|=
name|LocalTime
operator|.
name|parse
argument_list|(
name|start
argument_list|,
name|formatter
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|startTime
init|=
name|now
operator|.
name|with
argument_list|(
name|firstStartTime
argument_list|)
decl_stmt|;
try|try
block|{
name|DayOfWeek
name|dayOfWeek
init|=
name|formatter
operator|.
name|parse
argument_list|(
name|start
argument_list|,
name|DayOfWeek
operator|::
name|from
argument_list|)
decl_stmt|;
name|startTime
operator|=
name|startTime
operator|.
name|with
argument_list|(
name|dayOfWeek
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|ignored
parameter_list|)
block|{
comment|// Day of week is an optional parameter.
block|}
name|startTime
operator|=
name|startTime
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
name|delay
operator|=
name|Duration
operator|.
name|between
argument_list|(
name|now
argument_list|,
name|startTime
argument_list|)
operator|.
name|toMillis
argument_list|()
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|formatValue
argument_list|(
name|keyInterval
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|formatValue
argument_list|(
name|keyStartTime
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|formatValue (String key)
specifier|private
name|String
name|formatValue
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|section
argument_list|)
expr_stmt|;
if|if
condition|(
name|subsection
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|subsection
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|rc
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|": NA"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

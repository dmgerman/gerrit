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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|info
operator|.
name|AccountPreferencesInfo
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
name|i18n
operator|.
name|client
operator|.
name|DateTimeFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|DateFormatter
specifier|public
class|class
name|DateFormatter
block|{
DECL|field|ONE_YEAR
specifier|private
specifier|static
specifier|final
name|long
name|ONE_YEAR
init|=
literal|182L
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
DECL|field|sTime
specifier|private
specifier|final
name|DateTimeFormat
name|sTime
decl_stmt|;
DECL|field|sDate
specifier|private
specifier|final
name|DateTimeFormat
name|sDate
decl_stmt|;
DECL|field|sdtFmt
specifier|private
specifier|final
name|DateTimeFormat
name|sdtFmt
decl_stmt|;
DECL|field|mDate
specifier|private
specifier|final
name|DateTimeFormat
name|mDate
decl_stmt|;
DECL|field|dtfmt
specifier|private
specifier|final
name|DateTimeFormat
name|dtfmt
decl_stmt|;
DECL|method|DateFormatter (AccountPreferencesInfo prefs)
specifier|public
name|DateFormatter
parameter_list|(
name|AccountPreferencesInfo
name|prefs
parameter_list|)
block|{
name|String
name|fmt_sTime
init|=
name|prefs
operator|.
name|timeFormat
argument_list|()
operator|.
name|getFormat
argument_list|()
decl_stmt|;
name|String
name|fmt_sDate
init|=
name|prefs
operator|.
name|dateFormat
argument_list|()
operator|.
name|getShortFormat
argument_list|()
decl_stmt|;
name|String
name|fmt_mDate
init|=
name|prefs
operator|.
name|dateFormat
argument_list|()
operator|.
name|getLongFormat
argument_list|()
decl_stmt|;
name|sTime
operator|=
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt_sTime
argument_list|)
expr_stmt|;
name|sDate
operator|=
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt_sDate
argument_list|)
expr_stmt|;
name|sdtFmt
operator|=
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt_sDate
operator|+
literal|" "
operator|+
name|fmt_sTime
argument_list|)
expr_stmt|;
name|mDate
operator|=
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt_mDate
argument_list|)
expr_stmt|;
name|dtfmt
operator|=
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt_mDate
operator|+
literal|" "
operator|+
name|fmt_sTime
argument_list|)
expr_stmt|;
block|}
comment|/** Format a date using a really short format. */
DECL|method|shortFormat (Date dt)
specifier|public
name|String
name|shortFormat
parameter_list|(
name|Date
name|dt
parameter_list|)
block|{
if|if
condition|(
name|dt
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|dt
operator|=
operator|new
name|Date
argument_list|(
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mDate
operator|.
name|format
argument_list|(
name|now
argument_list|)
operator|.
name|equals
argument_list|(
name|mDate
operator|.
name|format
argument_list|(
name|dt
argument_list|)
argument_list|)
condition|)
block|{
comment|// Same day as today, report only the time.
comment|//
return|return
name|sTime
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|-
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
operator|<
name|ONE_YEAR
condition|)
block|{
comment|// Within the last year, show a shorter date.
comment|//
return|return
name|sDate
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
else|else
block|{
comment|// Report only date and year, its far away from now.
comment|//
return|return
name|mDate
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
block|}
comment|/** Format a date using a really short format. */
DECL|method|shortFormatDayTime (Date dt)
specifier|public
name|String
name|shortFormatDayTime
parameter_list|(
name|Date
name|dt
parameter_list|)
block|{
if|if
condition|(
name|dt
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|dt
operator|=
operator|new
name|Date
argument_list|(
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mDate
operator|.
name|format
argument_list|(
name|now
argument_list|)
operator|.
name|equals
argument_list|(
name|mDate
operator|.
name|format
argument_list|(
name|dt
argument_list|)
argument_list|)
condition|)
block|{
comment|// Same day as today, report only the time.
comment|//
return|return
name|sTime
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|-
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
operator|<
name|ONE_YEAR
condition|)
block|{
comment|// Within the last year, show a shorter date.
comment|//
return|return
name|sdtFmt
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
else|else
block|{
comment|// Report only date and year, its far away from now.
comment|//
return|return
name|mDate
operator|.
name|format
argument_list|(
name|dt
argument_list|)
return|;
block|}
block|}
comment|/** Format a date using the locale's medium length format. */
DECL|method|mediumFormat (final Date dt)
specifier|public
name|String
name|mediumFormat
parameter_list|(
specifier|final
name|Date
name|dt
parameter_list|)
block|{
if|if
condition|(
name|dt
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|dtfmt
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|dt
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


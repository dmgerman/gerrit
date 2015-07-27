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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
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
name|Messages
import|;
end_import

begin_interface
DECL|interface|CommonMessages
specifier|public
interface|interface
name|CommonMessages
extends|extends
name|Messages
block|{
DECL|field|M
specifier|public
specifier|static
specifier|final
name|CommonMessages
name|M
init|=
name|GWT
operator|.
name|create
argument_list|(
name|CommonMessages
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|secondsAgo (long seconds)
name|String
name|secondsAgo
parameter_list|(
name|long
name|seconds
parameter_list|)
function_decl|;
DECL|method|minutesAgo (long minutes)
name|String
name|minutesAgo
parameter_list|(
name|long
name|minutes
parameter_list|)
function_decl|;
DECL|method|hoursAgo (long hours)
name|String
name|hoursAgo
parameter_list|(
name|long
name|hours
parameter_list|)
function_decl|;
DECL|method|daysAgo (long days)
name|String
name|daysAgo
parameter_list|(
name|long
name|days
parameter_list|)
function_decl|;
DECL|method|weeksAgo (long weeks)
name|String
name|weeksAgo
parameter_list|(
name|long
name|weeks
parameter_list|)
function_decl|;
DECL|method|monthsAgo (long months)
name|String
name|monthsAgo
parameter_list|(
name|long
name|months
parameter_list|)
function_decl|;
DECL|method|yearsAgo (long years)
name|String
name|yearsAgo
parameter_list|(
name|long
name|years
parameter_list|)
function_decl|;
DECL|method|years0MonthsAgo (long years, String yearLabel)
name|String
name|years0MonthsAgo
parameter_list|(
name|long
name|years
parameter_list|,
name|String
name|yearLabel
parameter_list|)
function_decl|;
DECL|method|yearsMonthsAgo (long years, String yearLabel, long months, String monthLabel)
name|String
name|yearsMonthsAgo
parameter_list|(
name|long
name|years
parameter_list|,
name|String
name|yearLabel
parameter_list|,
name|long
name|months
parameter_list|,
name|String
name|monthLabel
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


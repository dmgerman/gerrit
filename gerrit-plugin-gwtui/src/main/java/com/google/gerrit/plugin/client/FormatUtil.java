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
DECL|package|com.google.gerrit.plugin.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|plugin
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
name|AccountFormatter
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
name|client
operator|.
name|DateFormatter
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
name|client
operator|.
name|info
operator|.
name|AccountInfo
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
DECL|class|FormatUtil
specifier|public
class|class
name|FormatUtil
block|{
DECL|field|accountFormatter
specifier|private
specifier|final
specifier|static
name|AccountFormatter
name|accountFormatter
init|=
operator|new
name|AccountFormatter
argument_list|(
name|Plugin
operator|.
name|get
argument_list|()
operator|.
name|getServerInfo
argument_list|()
operator|.
name|user
argument_list|()
operator|.
name|anonymousCowardName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Format a date using a really short format. */
DECL|method|shortFormat (Date dt)
specifier|public
specifier|static
name|String
name|shortFormat
parameter_list|(
name|Date
name|dt
parameter_list|)
block|{
return|return
name|createDateFormatter
argument_list|()
operator|.
name|shortFormat
argument_list|(
name|dt
argument_list|)
return|;
block|}
comment|/** Format a date using a really short format. */
DECL|method|shortFormatDayTime (Date dt)
specifier|public
specifier|static
name|String
name|shortFormatDayTime
parameter_list|(
name|Date
name|dt
parameter_list|)
block|{
return|return
name|createDateFormatter
argument_list|()
operator|.
name|shortFormatDayTime
argument_list|(
name|dt
argument_list|)
return|;
block|}
comment|/** Format a date using the locale's medium length format. */
DECL|method|mediumFormat (Date dt)
specifier|public
specifier|static
name|String
name|mediumFormat
parameter_list|(
name|Date
name|dt
parameter_list|)
block|{
return|return
name|createDateFormatter
argument_list|()
operator|.
name|mediumFormat
argument_list|(
name|dt
argument_list|)
return|;
block|}
DECL|method|createDateFormatter ()
specifier|private
specifier|static
name|DateFormatter
name|createDateFormatter
parameter_list|()
block|{
return|return
operator|new
name|DateFormatter
argument_list|(
name|Plugin
operator|.
name|get
argument_list|()
operator|.
name|getUserPreferences
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Formats an account as a name and an email address.    *<p>    * Example output:    *<ul>    *<li>{@code A U. Thor&lt;author@example.com&gt;}: full populated</li>    *<li>{@code A U. Thor (12)}: missing email address</li>    *<li>{@code Anonymous Coward&lt;author@example.com&gt;}: missing name</li>    *<li>{@code Anonymous Coward (12)}: missing name and email address</li>    *</ul>    */
DECL|method|nameEmail (AccountInfo info)
specifier|public
specifier|static
name|String
name|nameEmail
parameter_list|(
name|AccountInfo
name|info
parameter_list|)
block|{
return|return
name|accountFormatter
operator|.
name|nameEmail
argument_list|(
name|info
argument_list|)
return|;
block|}
comment|/**    * Formats an account name.    *<p>    * If the account has a full name, it returns only the full name. Otherwise it    * returns a longer form that includes the email address.    */
DECL|method|name (AccountInfo info)
specifier|public
specifier|static
name|String
name|name
parameter_list|(
name|AccountInfo
name|info
parameter_list|)
block|{
return|return
name|accountFormatter
operator|.
name|name
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
end_class

end_unit


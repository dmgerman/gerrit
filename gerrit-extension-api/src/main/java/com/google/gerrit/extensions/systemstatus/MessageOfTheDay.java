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
DECL|package|com.google.gerrit.extensions.systemstatus
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|systemstatus
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|Date
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

begin_comment
comment|/**  * Supplies a message of the day when the page is first loaded.  *  *<pre>  * DynamicSet.bind(binder(), MessageOfTheDay.class).to(MyMessage.class);  *</pre>  */
end_comment

begin_class
annotation|@
name|ExtensionPoint
DECL|class|MessageOfTheDay
specifier|public
specifier|abstract
class|class
name|MessageOfTheDay
block|{
comment|/**    * Retrieve the message of the day as an HTML fragment.    *    * @return message as an HTML fragment; null if no message is available.    */
DECL|method|getHtmlMessage ()
specifier|public
specifier|abstract
name|String
name|getHtmlMessage
parameter_list|()
function_decl|;
comment|/**    * Unique identifier for this message.    *    *<p>Messages with the same identifier will be hidden from the user until redisplay has occurred.    *    * @return unique message identifier. This identifier should be unique within the server.    */
DECL|method|getMessageId ()
specifier|public
specifier|abstract
name|String
name|getMessageId
parameter_list|()
function_decl|;
comment|/**    * When should the message be displayed?    *    *<p>Default implementation returns {@code tomorrow at 00:00:00 GMT}.    *    * @return a future date after which the message should be redisplayed.    */
DECL|method|getRedisplay ()
specifier|public
name|Date
name|getRedisplay
parameter_list|()
block|{
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|cal
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
end_class

end_unit


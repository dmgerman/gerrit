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
DECL|package|com.google.gerrit.testutil.log
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
operator|.
name|log
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Appender
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
name|LogManager
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|LogUtil
specifier|public
class|class
name|LogUtil
block|{
comment|/**    * Change logger's setting so it only logs to a collection.    *    * @param logName Name of the logger to modify.    * @param collection The collection to log into.    * @return The logger's original settings.    */
DECL|method|logToCollection (String logName, Collection<LoggingEvent> collection)
specifier|public
specifier|static
name|LoggerSettings
name|logToCollection
parameter_list|(
name|String
name|logName
parameter_list|,
name|Collection
argument_list|<
name|LoggingEvent
argument_list|>
name|collection
parameter_list|)
block|{
name|Logger
name|logger
init|=
name|LogManager
operator|.
name|getLogger
argument_list|(
name|logName
argument_list|)
decl_stmt|;
name|LoggerSettings
name|loggerSettings
init|=
operator|new
name|LoggerSettings
argument_list|(
name|logger
argument_list|)
decl_stmt|;
name|logger
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
name|logger
operator|.
name|setAdditivity
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CollectionAppender
name|listAppender
init|=
operator|new
name|CollectionAppender
argument_list|(
name|collection
argument_list|)
decl_stmt|;
name|logger
operator|.
name|addAppender
argument_list|(
name|listAppender
argument_list|)
expr_stmt|;
return|return
name|loggerSettings
return|;
block|}
comment|/**    * Capsule for a logger's settings that get mangled by rerouting logging to a collection    */
DECL|class|LoggerSettings
specifier|public
specifier|static
class|class
name|LoggerSettings
block|{
DECL|field|additive
specifier|private
specifier|final
name|boolean
name|additive
decl_stmt|;
DECL|field|appenders
specifier|private
specifier|final
name|List
argument_list|<
name|Appender
argument_list|>
name|appenders
decl_stmt|;
comment|/**      * Read off logger settings from an instance.      *      * @param logger The logger to read the settings off from.      */
DECL|method|LoggerSettings (Logger logger)
specifier|private
name|LoggerSettings
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|additive
operator|=
name|logger
operator|.
name|getAdditivity
argument_list|()
expr_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|appenders
init|=
name|logger
operator|.
name|getAllAppenders
argument_list|()
decl_stmt|;
name|this
operator|.
name|appenders
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
while|while
condition|(
name|appenders
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|appender
init|=
name|appenders
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|appender
operator|instanceof
name|Appender
condition|)
block|{
name|this
operator|.
name|appenders
operator|.
name|add
argument_list|(
operator|(
name|Appender
operator|)
name|appender
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getAllAppenders of "
operator|+
name|logger
operator|+
literal|" contained an object that is not an Appender"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Pushes this settings back onto a logger.      *      * @param logger the logger on which to push the settings.      */
DECL|method|pushOntoLogger (Logger logger)
specifier|public
name|void
name|pushOntoLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|logger
operator|.
name|setAdditivity
argument_list|(
name|additive
argument_list|)
expr_stmt|;
name|logger
operator|.
name|removeAllAppenders
argument_list|()
expr_stmt|;
for|for
control|(
name|Appender
name|appender
range|:
name|appenders
control|)
block|{
name|logger
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


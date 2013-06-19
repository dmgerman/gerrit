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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|collect
operator|.
name|Lists
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
name|testutil
operator|.
name|log
operator|.
name|LogUtil
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
name|org
operator|.
name|junit
operator|.
name|After
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

begin_comment
comment|/**  * Testcase capturing associated logs and allowing to assert on them.  *  * For a test case SomeNameTest, the log for SomeName gets captured. Assertions  * on logs run against the coptured log events from this logger. After the  * tests, the logger are set back to their original settings.  */
end_comment

begin_class
DECL|class|LoggingMockingTestCase
specifier|public
specifier|abstract
class|class
name|LoggingMockingTestCase
extends|extends
name|MockingTestCase
block|{
DECL|field|loggerName
specifier|private
name|String
name|loggerName
decl_stmt|;
DECL|field|loggerSettings
specifier|private
name|LogUtil
operator|.
name|LoggerSettings
name|loggerSettings
decl_stmt|;
DECL|field|loggedEvents
specifier|private
name|java
operator|.
name|util
operator|.
name|Collection
argument_list|<
name|LoggingEvent
argument_list|>
name|loggedEvents
decl_stmt|;
comment|/**    * Assert a logged event with a given string.    *<p>    * If such a event is found, it is removed from the captured logs.    *    * @param needle The string to look for.    */
DECL|method|assertLogMessageContains (String needle)
specifier|protected
specifier|final
name|void
name|assertLogMessageContains
parameter_list|(
name|String
name|needle
parameter_list|)
block|{
name|LoggingEvent
name|hit
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|LoggingEvent
argument_list|>
name|iter
init|=
name|loggedEvents
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|hit
operator|==
literal|null
operator|&&
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LoggingEvent
name|event
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|getRenderedMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|needle
argument_list|)
condition|)
block|{
name|hit
operator|=
name|event
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Could not find log message containing '"
operator|+
name|needle
operator|+
literal|"'"
argument_list|,
name|hit
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Could not remove log message containing '"
operator|+
name|needle
operator|+
literal|"'"
argument_list|,
name|loggedEvents
operator|.
name|remove
argument_list|(
name|hit
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert a logged event whose throwable contains a given string    *<p>    * If such a event is found, it is removed from the captured logs.    *    * @param needle The string to look for.    */
DECL|method|assertLogThrowableMessageContains (String needle)
specifier|protected
specifier|final
name|void
name|assertLogThrowableMessageContains
parameter_list|(
name|String
name|needle
parameter_list|)
block|{
name|LoggingEvent
name|hit
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|LoggingEvent
argument_list|>
name|iter
init|=
name|loggedEvents
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|hit
operator|==
literal|null
operator|&&
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LoggingEvent
name|event
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|getThrowableInformation
argument_list|()
operator|.
name|getThrowable
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|needle
argument_list|)
condition|)
block|{
name|hit
operator|=
name|event
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Could not find log message with a Throwable containing '"
operator|+
name|needle
operator|+
literal|"'"
argument_list|,
name|hit
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Could not remove log message with a Throwable containing '"
operator|+
name|needle
operator|+
literal|"'"
argument_list|,
name|loggedEvents
operator|.
name|remove
argument_list|(
name|hit
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert that all logged events have been asserted    */
comment|// As the PowerMock runner does not pass through runTest, we inject log
comment|// verification through @After
annotation|@
name|After
DECL|method|assertNoUnassertedLogEvents ()
specifier|public
specifier|final
name|void
name|assertNoUnassertedLogEvents
parameter_list|()
block|{
if|if
condition|(
name|loggedEvents
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LoggingEvent
name|event
init|=
name|loggedEvents
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|msg
init|=
literal|"Found untreated logged events. First one is:\n"
decl_stmt|;
name|msg
operator|+=
name|event
operator|.
name|getRenderedMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|getThrowableInformation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|+=
literal|"\n"
operator|+
name|event
operator|.
name|getThrowableInformation
argument_list|()
operator|.
name|getThrowable
argument_list|()
expr_stmt|;
block|}
name|fail
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|loggedEvents
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
comment|// The logger we're interested is class name without the trailing "Test".
comment|// While this is not the most general approach it is sufficient for now,
comment|// and we can improve later to allow tests to specify which loggers are
comment|// to check.
name|loggerName
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
expr_stmt|;
name|loggerName
operator|=
name|loggerName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|loggerName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
name|loggerSettings
operator|=
name|LogUtil
operator|.
name|logToCollection
argument_list|(
name|loggerName
argument_list|,
name|loggedEvents
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|runTest ()
specifier|protected
name|void
name|runTest
parameter_list|()
throws|throws
name|Throwable
block|{
name|super
operator|.
name|runTest
argument_list|()
expr_stmt|;
comment|// Plain JUnit runner does not pick up @After, so we add it here
comment|// explicitly. Note, that we cannot put this into tearDown, as failure
comment|// to verify mocks would bail out and might leave open resources from
comment|// subclasses open.
name|assertNoUnassertedLogEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|loggerName
operator|!=
literal|null
operator|&&
name|loggerSettings
operator|!=
literal|null
condition|)
block|{
name|Logger
name|logger
init|=
name|LogManager
operator|.
name|getLogger
argument_list|(
name|loggerName
argument_list|)
decl_stmt|;
name|loggerSettings
operator|.
name|pushOntoLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


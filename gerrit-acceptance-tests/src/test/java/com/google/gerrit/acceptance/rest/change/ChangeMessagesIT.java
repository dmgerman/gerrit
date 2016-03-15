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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|extensions
operator|.
name|common
operator|.
name|ChangeInfo
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
name|extensions
operator|.
name|common
operator|.
name|ChangeMessageInfo
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
name|ConfigSuite
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
name|TestTimeUtil
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
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
DECL|class|ChangeMessagesIT
specifier|public
class|class
name|ChangeMessagesIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|systemTimeZone
specifier|private
name|String
name|systemTimeZone
decl_stmt|;
annotation|@
name|Before
DECL|method|setTimeForTesting ()
specifier|public
name|void
name|setTimeForTesting
parameter_list|()
block|{
name|systemTimeZone
operator|=
name|System
operator|.
name|setProperty
argument_list|(
literal|"user.timezone"
argument_list|,
literal|"US/Eastern"
argument_list|)
expr_stmt|;
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|resetTime ()
specifier|public
name|void
name|resetTime
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"user.timezone"
argument_list|,
name|systemTimeZone
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|messagesNotReturnedByDefault ()
specifier|public
name|void
name|messagesNotReturnedByDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|postMessage
argument_list|(
name|changeId
argument_list|,
literal|"Some nits need to be fixed."
argument_list|)
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|info
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|defaultMessage ()
specifier|public
name|void
name|defaultMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 1."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|messagesReturnedInChronologicalOrder ()
specifier|public
name|void
name|messagesReturnedInChronologicalOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|firstMessage
init|=
literal|"Some nits need to be fixed."
decl_stmt|;
name|postMessage
argument_list|(
name|changeId
argument_list|,
name|firstMessage
argument_list|)
expr_stmt|;
name|String
name|secondMessage
init|=
literal|"I like this feature."
decl_stmt|;
name|postMessage
argument_list|(
name|changeId
argument_list|,
name|secondMessage
argument_list|)
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|it
init|=
name|c
operator|.
name|messages
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 1."
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|firstMessage
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertMessage
argument_list|(
name|secondMessage
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|postMessageWithTag ()
specifier|public
name|void
name|postMessageWithTag
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|tag
init|=
literal|"jenkins"
decl_stmt|;
name|String
name|msg
init|=
literal|"Message with tag."
decl_stmt|;
name|postMessage
argument_list|(
name|changeId
argument_list|,
name|msg
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|it
init|=
name|c
operator|.
name|messages
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Uploaded patch set 1."
argument_list|)
expr_stmt|;
name|ChangeMessageInfo
name|actual
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertMessage
argument_list|(
name|msg
argument_list|,
name|actual
operator|.
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actual
operator|.
name|tag
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessage (String expected, String actual)
specifier|private
name|void
name|assertMessage
parameter_list|(
name|String
name|expected
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|assertThat
argument_list|(
name|actual
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Patch Set 1:\n\n"
operator|+
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|postMessage (String changeId, String msg)
specifier|private
name|void
name|postMessage
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
name|postMessage
argument_list|(
name|changeId
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|postMessage (String changeId, String msg, String tag)
specifier|private
name|void
name|postMessage
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|msg
parameter_list|,
name|String
name|tag
parameter_list|)
throws|throws
name|Exception
block|{
name|ReviewInput
name|in
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|message
operator|=
name|msg
expr_stmt|;
name|in
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


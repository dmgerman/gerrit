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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

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
name|ImmutableList
import|;
end_import

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
name|ImmutableMap
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
name|common
operator|.
name|errors
operator|.
name|EmailException
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
name|server
operator|.
name|git
operator|.
name|WorkQueue
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
name|server
operator|.
name|mail
operator|.
name|Address
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
name|server
operator|.
name|mail
operator|.
name|send
operator|.
name|EmailHeader
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
name|server
operator|.
name|mail
operator|.
name|send
operator|.
name|EmailSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ExecutionException
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

begin_comment
comment|/**  * Email sender implementation that records messages in memory.  *  *<p>This class is mostly threadsafe. The only exception is that not all {@link EmailHeader}  * subclasses are immutable. In particular, if a caller holds a reference to an {@code AddressList}  * and mutates it after sending, the message returned by {@link #getMessages()} may or may not  * reflect mutations.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|FakeEmailSender
specifier|public
class|class
name|FakeEmailSender
implements|implements
name|EmailSender
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
name|FakeEmailSender
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|EmailSender
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FakeEmailSender
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AutoValue
DECL|class|Message
specifier|public
specifier|abstract
specifier|static
class|class
name|Message
block|{
DECL|method|create ( Address from, Collection<Address> rcpt, Map<String, EmailHeader> headers, String body)
specifier|private
specifier|static
name|Message
name|create
parameter_list|(
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|,
name|String
name|body
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_FakeEmailSender_Message
argument_list|(
name|from
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|rcpt
argument_list|)
argument_list|,
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|headers
argument_list|)
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|from ()
specifier|public
specifier|abstract
name|Address
name|from
parameter_list|()
function_decl|;
DECL|method|rcpt ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|()
function_decl|;
DECL|method|headers ()
specifier|public
specifier|abstract
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|()
function_decl|;
DECL|method|body ()
specifier|public
specifier|abstract
name|String
name|body
parameter_list|()
function_decl|;
block|}
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|Message
argument_list|>
name|messages
decl_stmt|;
annotation|@
name|Inject
DECL|method|FakeEmailSender (WorkQueue workQueue)
name|FakeEmailSender
parameter_list|(
name|WorkQueue
name|workQueue
parameter_list|)
block|{
name|this
operator|.
name|workQueue
operator|=
name|workQueue
expr_stmt|;
name|messages
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Message
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|canEmail (String address)
specifier|public
name|boolean
name|canEmail
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|send ( Address from, Collection<Address> rcpt, Map<String, EmailHeader> headers, String body)
specifier|public
name|void
name|send
parameter_list|(
name|Address
name|from
parameter_list|,
name|Collection
argument_list|<
name|Address
argument_list|>
name|rcpt
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|EmailException
block|{
name|messages
operator|.
name|add
argument_list|(
name|Message
operator|.
name|create
argument_list|(
name|from
argument_list|,
name|rcpt
argument_list|,
name|headers
argument_list|,
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|waitForEmails
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|messages
init|)
block|{
name|messages
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getMessages ()
specifier|public
name|ImmutableList
argument_list|<
name|Message
argument_list|>
name|getMessages
parameter_list|()
block|{
name|waitForEmails
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|messages
init|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|messages
argument_list|)
return|;
block|}
block|}
DECL|method|getMessages (String changeId, String type)
specifier|public
name|List
argument_list|<
name|Message
argument_list|>
name|getMessages
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|type
parameter_list|)
block|{
specifier|final
name|String
name|idFooter
init|=
literal|"\nGerrit-Change-Id: "
operator|+
name|changeId
operator|+
literal|"\n"
decl_stmt|;
specifier|final
name|String
name|typeFooter
init|=
literal|"\nGerrit-MessageType: "
operator|+
name|type
operator|+
literal|"\n"
decl_stmt|;
return|return
name|getMessages
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|in
lambda|->
name|in
operator|.
name|body
argument_list|()
operator|.
name|contains
argument_list|(
name|idFooter
argument_list|)
operator|&&
name|in
operator|.
name|body
argument_list|()
operator|.
name|contains
argument_list|(
name|typeFooter
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|waitForEmails ()
specifier|private
name|void
name|waitForEmails
parameter_list|()
block|{
comment|// TODO(dborowitz): This is brittle; consider forcing emails to use
comment|// a single thread in tests (tricky because most callers just use the
comment|// default executor).
for|for
control|(
name|WorkQueue
operator|.
name|Task
argument_list|<
name|?
argument_list|>
name|task
range|:
name|workQueue
operator|.
name|getTasks
argument_list|()
control|)
block|{
if|if
condition|(
name|task
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"send-email"
argument_list|)
condition|)
block|{
try|try
block|{
name|task
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"error finishing email task"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit


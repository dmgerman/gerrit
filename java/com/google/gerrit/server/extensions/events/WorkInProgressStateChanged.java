begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|flogger
operator|.
name|FluentLogger
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
name|NotifyHandling
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
name|AccountInfo
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
name|events
operator|.
name|WorkInProgressStateChangedListener
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
name|registration
operator|.
name|DynamicSet
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|account
operator|.
name|AccountState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|WorkInProgressStateChanged
specifier|public
class|class
name|WorkInProgressStateChanged
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|WorkInProgressStateChangedListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|util
specifier|private
specifier|final
name|EventUtil
name|util
decl_stmt|;
annotation|@
name|Inject
DECL|method|WorkInProgressStateChanged ( DynamicSet<WorkInProgressStateChangedListener> listeners, EventUtil util)
name|WorkInProgressStateChanged
parameter_list|(
name|DynamicSet
argument_list|<
name|WorkInProgressStateChangedListener
argument_list|>
name|listeners
parameter_list|,
name|EventUtil
name|util
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
name|this
operator|.
name|util
operator|=
name|util
expr_stmt|;
block|}
DECL|method|fire (Change change, AccountState account, Timestamp when)
specifier|public
name|void
name|fire
parameter_list|(
name|Change
name|change
parameter_list|,
name|AccountState
name|account
parameter_list|,
name|Timestamp
name|when
parameter_list|)
block|{
if|if
condition|(
operator|!
name|listeners
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
name|util
operator|.
name|changeInfo
argument_list|(
name|change
argument_list|)
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|account
argument_list|)
argument_list|,
name|when
argument_list|)
decl_stmt|;
for|for
control|(
name|WorkInProgressStateChangedListener
name|l
range|:
name|listeners
control|)
block|{
try|try
block|{
name|l
operator|.
name|onWorkInProgressStateChanged
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|util
operator|.
name|logEventListenerError
argument_list|(
name|event
argument_list|,
name|l
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Couldn't fire event"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
extends|extends
name|AbstractChangeEvent
implements|implements
name|WorkInProgressStateChangedListener
operator|.
name|Event
block|{
DECL|method|Event (ChangeInfo change, AccountInfo who, Timestamp when)
specifier|protected
name|Event
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|AccountInfo
name|who
parameter_list|,
name|Timestamp
name|when
parameter_list|)
block|{
name|super
argument_list|(
name|change
argument_list|,
name|who
argument_list|,
name|when
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


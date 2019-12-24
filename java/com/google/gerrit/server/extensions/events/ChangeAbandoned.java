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
name|entities
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
name|entities
operator|.
name|PatchSet
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
name|exceptions
operator|.
name|StorageException
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
name|common
operator|.
name|RevisionInfo
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
name|ChangeAbandonedListener
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
name|GpgException
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
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchListNotAvailableException
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
name|patch
operator|.
name|PatchListObjectTooLargeException
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
name|permissions
operator|.
name|PermissionBackendException
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|io
operator|.
name|IOException
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

begin_comment
comment|/** Helper class to fire an event when a change has been abandoned. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeAbandoned
specifier|public
class|class
name|ChangeAbandoned
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
name|PluginSetContext
argument_list|<
name|ChangeAbandonedListener
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
DECL|method|ChangeAbandoned (PluginSetContext<ChangeAbandonedListener> listeners, EventUtil util)
name|ChangeAbandoned
parameter_list|(
name|PluginSetContext
argument_list|<
name|ChangeAbandonedListener
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
DECL|method|fire ( Change change, PatchSet ps, AccountState abandoner, String reason, Timestamp when, NotifyHandling notifyHandling)
specifier|public
name|void
name|fire
parameter_list|(
name|Change
name|change
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|AccountState
name|abandoner
parameter_list|,
name|String
name|reason
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|)
block|{
if|if
condition|(
name|listeners
operator|.
name|isEmpty
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
name|revisionInfo
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|ps
argument_list|)
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|abandoner
argument_list|)
argument_list|,
name|reason
argument_list|,
name|when
argument_list|,
name|notifyHandling
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onChangeAbandoned
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchListObjectTooLargeException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Couldn't fire event: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchListNotAvailableException
decl||
name|GpgException
decl||
name|IOException
decl||
name|StorageException
decl||
name|PermissionBackendException
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
comment|/** Event to be fired when a change has been abandoned. */
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
extends|extends
name|AbstractRevisionEvent
implements|implements
name|ChangeAbandonedListener
operator|.
name|Event
block|{
DECL|field|reason
specifier|private
specifier|final
name|String
name|reason
decl_stmt|;
DECL|method|Event ( ChangeInfo change, RevisionInfo revision, AccountInfo abandoner, String reason, Timestamp when, NotifyHandling notifyHandling)
name|Event
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|RevisionInfo
name|revision
parameter_list|,
name|AccountInfo
name|abandoner
parameter_list|,
name|String
name|reason
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|)
block|{
name|super
argument_list|(
name|change
argument_list|,
name|revision
argument_list|,
name|abandoner
argument_list|,
name|when
argument_list|,
name|notifyHandling
argument_list|)
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReason ()
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
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
name|common
operator|.
name|Nullable
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
name|Account
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
name|Project
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
name|CurrentUser
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
name|IdentifiedUser
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
name|change
operator|.
name|NotifyResolver
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
name|config
operator|.
name|SendEmailExecutor
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
name|MergedSender
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
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|OutOfScopeException
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
name|assistedinject
operator|.
name|Assisted
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
name|ExecutorService
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
name|Future
import|;
end_import

begin_class
DECL|class|EmailMerge
class|class
name|EmailMerge
implements|implements
name|Runnable
implements|,
name|RequestContext
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
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( Project.NameKey project, Change.Id changeId, Account.Id submitter, NotifyResolver.Result notify)
name|EmailMerge
name|create
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Account
operator|.
name|Id
name|submitter
parameter_list|,
name|NotifyResolver
operator|.
name|Result
name|notify
parameter_list|)
function_decl|;
block|}
DECL|field|sendEmailsExecutor
specifier|private
specifier|final
name|ExecutorService
name|sendEmailsExecutor
decl_stmt|;
DECL|field|mergedSenderFactory
specifier|private
specifier|final
name|MergedSender
operator|.
name|Factory
name|mergedSenderFactory
decl_stmt|;
DECL|field|requestContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|submitter
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|submitter
decl_stmt|;
DECL|field|notify
specifier|private
specifier|final
name|NotifyResolver
operator|.
name|Result
name|notify
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailMerge ( @endEmailExecutor ExecutorService executor, MergedSender.Factory mergedSenderFactory, ThreadLocalRequestContext requestContext, IdentifiedUser.GenericFactory identifiedUserFactory, @Assisted Project.NameKey project, @Assisted Change.Id changeId, @Assisted @Nullable Account.Id submitter, @Assisted NotifyResolver.Result notify)
name|EmailMerge
parameter_list|(
annotation|@
name|SendEmailExecutor
name|ExecutorService
name|executor
parameter_list|,
name|MergedSender
operator|.
name|Factory
name|mergedSenderFactory
parameter_list|,
name|ThreadLocalRequestContext
name|requestContext
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|Account
operator|.
name|Id
name|submitter
parameter_list|,
annotation|@
name|Assisted
name|NotifyResolver
operator|.
name|Result
name|notify
parameter_list|)
block|{
name|this
operator|.
name|sendEmailsExecutor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|mergedSenderFactory
operator|=
name|mergedSenderFactory
expr_stmt|;
name|this
operator|.
name|requestContext
operator|=
name|requestContext
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|submitter
operator|=
name|submitter
expr_stmt|;
name|this
operator|.
name|notify
operator|=
name|notify
expr_stmt|;
block|}
DECL|method|sendAsync ()
name|void
name|sendAsync
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|sendEmailsExecutor
operator|.
name|submit
argument_list|(
name|this
argument_list|)
decl_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|RequestContext
name|old
init|=
name|requestContext
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
decl_stmt|;
try|try
block|{
name|MergedSender
name|cm
init|=
name|mergedSenderFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|submitter
operator|!=
literal|null
condition|)
block|{
name|cm
operator|.
name|setFrom
argument_list|(
name|submitter
argument_list|)
expr_stmt|;
block|}
name|cm
operator|.
name|setNotify
argument_list|(
name|notify
argument_list|)
expr_stmt|;
name|cm
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"Cannot email merged notification for %s"
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|requestContext
operator|.
name|setContext
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"send-email merged"
return|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
if|if
condition|(
name|submitter
operator|!=
literal|null
condition|)
block|{
return|return
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|submitter
argument_list|)
operator|.
name|getRealUser
argument_list|()
return|;
block|}
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"No user on email thread"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit


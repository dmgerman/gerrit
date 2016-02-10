begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|gerrit
operator|.
name|server
operator|.
name|PatchLineCommentsUtil
operator|.
name|PLC_ORDER
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
name|reviewdb
operator|.
name|client
operator|.
name|ChangeMessage
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
name|PatchLineComment
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|git
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
name|CommentSender
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
name|notedb
operator|.
name|ChangeNotes
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
name|PatchSetInfoFactory
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|Provider
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
name|ProvisionException
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
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_class
DECL|class|EmailReviewComments
specifier|public
class|class
name|EmailReviewComments
implements|implements
name|Runnable
implements|,
name|RequestContext
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
name|EmailReviewComments
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ( NotifyHandling notify, ChangeNotes notes, PatchSet patchSet, IdentifiedUser user, ChangeMessage message, List<PatchLineComment> comments)
name|EmailReviewComments
name|create
parameter_list|(
name|NotifyHandling
name|notify
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
name|ChangeMessage
name|message
parameter_list|,
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
function_decl|;
block|}
DECL|field|sendEmailsExecutor
specifier|private
specifier|final
name|ExecutorService
name|sendEmailsExecutor
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|commentSenderFactory
specifier|private
specifier|final
name|CommentSender
operator|.
name|Factory
name|commentSenderFactory
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|requestContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|notify
specifier|private
specifier|final
name|NotifyHandling
name|notify
decl_stmt|;
DECL|field|notes
specifier|private
specifier|final
name|ChangeNotes
name|notes
decl_stmt|;
DECL|field|patchSet
specifier|private
specifier|final
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|ChangeMessage
name|message
decl_stmt|;
DECL|field|comments
specifier|private
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailReviewComments ( @endEmailExecutor ExecutorService executor, PatchSetInfoFactory patchSetInfoFactory, CommentSender.Factory commentSenderFactory, SchemaFactory<ReviewDb> schemaFactory, ThreadLocalRequestContext requestContext, @Assisted NotifyHandling notify, @Assisted ChangeNotes notes, @Assisted PatchSet patchSet, @Assisted IdentifiedUser user, @Assisted ChangeMessage message, @Assisted List<PatchLineComment> comments)
name|EmailReviewComments
parameter_list|(
annotation|@
name|SendEmailExecutor
name|ExecutorService
name|executor
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|CommentSender
operator|.
name|Factory
name|commentSenderFactory
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|ThreadLocalRequestContext
name|requestContext
parameter_list|,
annotation|@
name|Assisted
name|NotifyHandling
name|notify
parameter_list|,
annotation|@
name|Assisted
name|ChangeNotes
name|notes
parameter_list|,
annotation|@
name|Assisted
name|PatchSet
name|patchSet
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
name|ChangeMessage
name|message
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
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
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|commentSenderFactory
operator|=
name|commentSenderFactory
expr_stmt|;
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|requestContext
operator|=
name|requestContext
expr_stmt|;
name|this
operator|.
name|notify
operator|=
name|notify
expr_stmt|;
name|this
operator|.
name|notes
operator|=
name|notes
expr_stmt|;
name|this
operator|.
name|patchSet
operator|=
name|patchSet
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|comments
operator|=
name|PLC_ORDER
operator|.
name|sortedCopy
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
DECL|method|sendAsync ()
name|void
name|sendAsync
parameter_list|()
block|{
name|sendEmailsExecutor
operator|.
name|submit
argument_list|(
name|this
argument_list|)
expr_stmt|;
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
name|CommentSender
name|cm
init|=
name|commentSenderFactory
operator|.
name|create
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|,
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchSet
argument_list|(
name|patchSet
argument_list|,
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|,
name|patchSet
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setPatchLineComments
argument_list|(
name|comments
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|error
argument_list|(
literal|"Cannot email comments for "
operator|+
name|patchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|e
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
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
name|db
operator|=
literal|null
expr_stmt|;
block|}
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
literal|"send-email comments"
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
return|return
name|user
operator|.
name|getRealUser
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getReviewDbProvider ()
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
return|return
operator|new
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ReviewDb
name|get
parameter_list|()
block|{
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|db
operator|=
name|schemaFactory
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot open ReviewDb"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|db
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit


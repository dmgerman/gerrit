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
name|reviewdb
operator|.
name|client
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
name|change
operator|.
name|PostReview
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
DECL|class|EmailReviewComments
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
DECL|method|create ( NotifyHandling notify, Change change, PatchSet patchSet, Account.Id authorId, ChangeMessage message, List<PatchLineComment> comments)
name|EmailReviewComments
name|create
parameter_list|(
name|NotifyHandling
name|notify
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|Account
operator|.
name|Id
name|authorId
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
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
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
name|PostReview
operator|.
name|NotifyHandling
name|notify
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|patchSet
specifier|private
specifier|final
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|authorId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|authorId
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
DECL|method|EmailReviewComments ( WorkQueue workQueue, PatchSetInfoFactory patchSetInfoFactory, CommentSender.Factory commentSenderFactory, SchemaFactory<ReviewDb> schemaFactory, ThreadLocalRequestContext requestContext, @Assisted NotifyHandling notify, @Assisted Change change, @Assisted PatchSet patchSet, @Assisted Account.Id authorId, @Assisted ChangeMessage message, @Assisted List<PatchLineComment> comments)
name|EmailReviewComments
parameter_list|(
name|WorkQueue
name|workQueue
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
name|Change
name|change
parameter_list|,
annotation|@
name|Assisted
name|PatchSet
name|patchSet
parameter_list|,
annotation|@
name|Assisted
name|Account
operator|.
name|Id
name|authorId
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
name|workQueue
operator|=
name|workQueue
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
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|patchSet
operator|=
name|patchSet
expr_stmt|;
name|this
operator|.
name|authorId
operator|=
name|authorId
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
name|comments
expr_stmt|;
block|}
DECL|method|sendAsync ()
name|void
name|sendAsync
parameter_list|()
block|{
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
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
try|try
block|{
name|requestContext
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|comments
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|comments
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|comments
argument_list|,
operator|new
name|Comparator
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|PatchLineComment
name|a
parameter_list|,
name|PatchLineComment
name|b
parameter_list|)
block|{
name|int
name|cmp
init|=
name|path
argument_list|(
name|a
argument_list|)
operator|.
name|compareTo
argument_list|(
name|path
argument_list|(
name|b
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
block|{
return|return
name|cmp
return|;
block|}
comment|// 0 is ancestor, 1 is revision. Sort ancestor first.
name|cmp
operator|=
name|a
operator|.
name|getSide
argument_list|()
operator|-
name|b
operator|.
name|getSide
argument_list|()
expr_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
block|{
return|return
name|cmp
return|;
block|}
return|return
name|a
operator|.
name|getLine
argument_list|()
operator|-
name|b
operator|.
name|getLine
argument_list|()
return|;
block|}
specifier|private
name|String
name|path
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
return|return
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|CommentSender
name|cm
init|=
name|commentSenderFactory
operator|.
name|create
argument_list|(
name|notify
argument_list|,
name|change
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|authorId
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
name|change
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
literal|null
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
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
literal|null
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


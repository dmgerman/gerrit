begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
package|package
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|account
operator|.
name|WatchConfig
operator|.
name|NotifyType
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/** Send notice of new patch sets for reviewers. */
end_comment

begin_class
DECL|class|ReplacePatchSetSender
specifier|public
class|class
name|ReplacePatchSetSender
extends|extends
name|ReplyToChangeSender
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Project.NameKey project, Change.Id id)
name|ReplacePatchSetSender
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
name|id
parameter_list|)
function_decl|;
block|}
DECL|field|reviewers
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|extraCC
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|extraCC
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReplacePatchSetSender ( EmailArguments ea, @Assisted Project.NameKey project, @Assisted Change.Id id)
specifier|public
name|ReplacePatchSetSender
parameter_list|(
name|EmailArguments
name|ea
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
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
name|super
argument_list|(
name|ea
argument_list|,
literal|"newpatchset"
argument_list|,
name|newChangeData
argument_list|(
name|ea
argument_list|,
name|project
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addReviewers (final Collection<Account.Id> cc)
specifier|public
name|void
name|addReviewers
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|reviewers
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
DECL|method|addExtraCC (final Collection<Account.Id> cc)
specifier|public
name|void
name|addExtraCC
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|cc
parameter_list|)
block|{
name|extraCC
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
if|if
condition|(
name|fromId
operator|!=
literal|null
condition|)
block|{
comment|// Don't call yourself a reviewer of your own patch set.
comment|//
name|reviewers
operator|.
name|remove
argument_list|(
name|fromId
argument_list|)
expr_stmt|;
block|}
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|reviewers
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|extraCC
argument_list|)
expr_stmt|;
name|rcptToAuthors
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|)
expr_stmt|;
name|bccStarredBy
argument_list|()
expr_stmt|;
name|includeWatchers
argument_list|(
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|,
operator|!
name|patchSet
operator|.
name|isDraft
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|formatChange ()
specifier|protected
name|void
name|formatChange
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|textTemplate
argument_list|(
literal|"ReplacePatchSet"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|useHtml
argument_list|()
condition|)
block|{
name|appendHtml
argument_list|(
name|soyHtmlTemplate
argument_list|(
literal|"ReplacePatchSetHtml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getReviewerNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReviewerNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|reviewers
control|)
block|{
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|fromId
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|names
operator|.
name|add
argument_list|(
name|getNameFor
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|names
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|names
return|;
block|}
annotation|@
name|Override
DECL|method|setupSoyContext ()
specifier|protected
name|void
name|setupSoyContext
parameter_list|()
block|{
name|super
operator|.
name|setupSoyContext
argument_list|()
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"reviewerNames"
argument_list|,
name|getReviewerNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|supportsHtml ()
specifier|protected
name|boolean
name|supportsHtml
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


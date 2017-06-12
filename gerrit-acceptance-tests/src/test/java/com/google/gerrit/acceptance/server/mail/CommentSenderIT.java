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
DECL|package|com.google.gerrit.acceptance.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|mail
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
operator|.
name|ALL
import|;
end_import

begin_import
import|import static
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
operator|.
name|NONE
import|;
end_import

begin_import
import|import static
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
operator|.
name|OWNER
import|;
end_import

begin_import
import|import static
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
operator|.
name|OWNER_REVIEWERS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|EmailStrategy
operator|.
name|CC_ON_OWN_COMMENTS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|EmailStrategy
operator|.
name|ENABLED
import|;
end_import

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
name|account
operator|.
name|WatchConfig
operator|.
name|NotifyType
operator|.
name|ALL_COMMENTS
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
name|AbstractNotificationTest
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
name|TestAccount
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
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|EmailStrategy
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

begin_class
DECL|class|CommentSenderIT
specifier|public
class|class
name|CommentSenderIT
extends|extends
name|AbstractNotificationTest
block|{
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwner ()
specifier|public
name|void
name|commentOnReviewableChangeByOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByReviewer ()
specifier|public
name|void
name|commentOnReviewableChangeByReviewer
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerCcingSelf ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerCcingSelf
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByReviewerCcingSelf ()
specifier|public
name|void
name|commentOnReviewableChangeByReviewerCcingSelf
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOther ()
specifier|public
name|void
name|commentOnReviewableChangeByOther
parameter_list|()
throws|throws
name|Exception
block|{
name|TestAccount
name|other
init|=
name|accountCreator
operator|.
name|create
argument_list|(
literal|"other"
argument_list|,
literal|"other@example.com"
argument_list|,
literal|"other"
argument_list|)
decl_stmt|;
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|other
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|notTo
argument_list|(
name|other
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOtherCcingSelf ()
specifier|public
name|void
name|commentOnReviewableChangeByOtherCcingSelf
parameter_list|()
throws|throws
name|Exception
block|{
name|TestAccount
name|other
init|=
name|accountCreator
operator|.
name|create
argument_list|(
literal|"other"
argument_list|,
literal|"other@example.com"
argument_list|,
literal|"other"
argument_list|)
decl_stmt|;
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|other
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|,
name|other
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerNotifyOwnerReviewers ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerNotifyOwnerReviewers
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|OWNER_REVIEWERS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|notTo
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerNotifyOwner ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerNotifyOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|OWNER
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerCcingSelfNotifyOwner ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerCcingSelfNotifyOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|setEmailStrategy
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|OWNER
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
comment|// TODO(logan): Why not send to owner?
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerNotifyNone ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerNotifyNone
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|NONE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableChangeByOwnerCcingSelfNotifyNone ()
specifier|public
name|void
name|commentOnReviewableChangeByOwnerCcingSelfNotifyNone
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|setEmailStrategy
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|NONE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
comment|// TODO(logan): Why not send to owner?
block|}
annotation|@
name|Test
DECL|method|commentOnWipChangeByOwner ()
specifier|public
name|void
name|commentOnWipChangeByOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnWipChangeByOwnerCcingSelf ()
specifier|public
name|void
name|commentOnWipChangeByOwnerCcingSelf
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|CC_ON_OWN_COMMENTS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|notSent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnWipChangeByOwnerNotifyAll ()
specifier|public
name|void
name|commentOnWipChangeByOwnerNotifyAll
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|ALL
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnWipChangeByBot ()
specifier|public
name|void
name|commentOnWipChangeByBot
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|TestAccount
name|bot
init|=
name|sc
operator|.
name|testAccount
argument_list|(
literal|"bot"
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|bot
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
literal|null
argument_list|,
literal|"tag"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|notTo
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableWipChangeByBot ()
specifier|public
name|void
name|commentOnReviewableWipChangeByBot
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|TestAccount
name|bot
init|=
name|sc
operator|.
name|testAccount
argument_list|(
literal|"bot"
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|bot
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
literal|null
argument_list|,
literal|"tag"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|notTo
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableWipChangeByBotNotifyAll ()
specifier|public
name|void
name|commentOnReviewableWipChangeByBotNotifyAll
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|TestAccount
name|bot
init|=
name|sc
operator|.
name|testAccount
argument_list|(
literal|"bot"
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|bot
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|,
name|ALL
argument_list|,
literal|"tag"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|to
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|commentOnReviewableWipChangeByOwner ()
specifier|public
name|void
name|commentOnReviewableWipChangeByOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|StagedChange
name|sc
init|=
name|stageReviewableWipChange
argument_list|(
name|ALL_COMMENTS
argument_list|)
decl_stmt|;
name|review
argument_list|(
name|sc
operator|.
name|owner
argument_list|,
name|sc
operator|.
name|changeId
argument_list|,
name|ENABLED
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sender
argument_list|)
operator|.
name|sent
argument_list|(
literal|"comment"
argument_list|,
name|sc
argument_list|)
operator|.
name|notTo
argument_list|(
name|sc
operator|.
name|owner
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewer
argument_list|,
name|sc
operator|.
name|ccer
argument_list|)
operator|.
name|cc
argument_list|(
name|sc
operator|.
name|reviewerByEmail
argument_list|,
name|sc
operator|.
name|ccerByEmail
argument_list|)
operator|.
name|bcc
argument_list|(
name|sc
operator|.
name|starrer
argument_list|)
operator|.
name|bcc
argument_list|(
name|ALL_COMMENTS
argument_list|)
expr_stmt|;
block|}
DECL|method|review (TestAccount account, String changeId, EmailStrategy strategy)
specifier|private
name|void
name|review
parameter_list|(
name|TestAccount
name|account
parameter_list|,
name|String
name|changeId
parameter_list|,
name|EmailStrategy
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|review
argument_list|(
name|account
argument_list|,
name|changeId
argument_list|,
name|strategy
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|review ( TestAccount account, String changeId, EmailStrategy strategy, @Nullable NotifyHandling notify)
specifier|private
name|void
name|review
parameter_list|(
name|TestAccount
name|account
parameter_list|,
name|String
name|changeId
parameter_list|,
name|EmailStrategy
name|strategy
parameter_list|,
annotation|@
name|Nullable
name|NotifyHandling
name|notify
parameter_list|)
throws|throws
name|Exception
block|{
name|review
argument_list|(
name|account
argument_list|,
name|changeId
argument_list|,
name|strategy
argument_list|,
name|notify
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|review ( TestAccount account, String changeId, EmailStrategy strategy, @Nullable NotifyHandling notify, @Nullable String tag)
specifier|private
name|void
name|review
parameter_list|(
name|TestAccount
name|account
parameter_list|,
name|String
name|changeId
parameter_list|,
name|EmailStrategy
name|strategy
parameter_list|,
annotation|@
name|Nullable
name|NotifyHandling
name|notify
parameter_list|,
annotation|@
name|Nullable
name|String
name|tag
parameter_list|)
throws|throws
name|Exception
block|{
name|setEmailStrategy
argument_list|(
name|account
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
name|ReviewInput
name|in
init|=
name|ReviewInput
operator|.
name|recommend
argument_list|()
decl_stmt|;
name|in
operator|.
name|notify
operator|=
name|notify
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
name|revision
argument_list|(
literal|"current"
argument_list|)
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


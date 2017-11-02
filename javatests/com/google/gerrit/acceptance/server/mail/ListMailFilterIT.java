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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|GerritConfig
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
name|NoHttpd
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
name|extensions
operator|.
name|common
operator|.
name|CommentInfo
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
name|MailUtil
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
name|receive
operator|.
name|MailMessage
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
name|receive
operator|.
name|MailProcessor
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
name|java
operator|.
name|time
operator|.
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|List
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
annotation|@
name|NoHttpd
DECL|class|ListMailFilterIT
specifier|public
class|class
name|ListMailFilterIT
extends|extends
name|AbstractMailIT
block|{
DECL|field|mailProcessor
annotation|@
name|Inject
specifier|private
name|MailProcessor
name|mailProcessor
decl_stmt|;
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.mode"
argument_list|,
name|value
operator|=
literal|"OFF"
argument_list|)
DECL|method|listFilterOff ()
specifier|public
name|void
name|listFilterOff
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|changeInfo
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
comment|// Check that the comments from the email have been persisted
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.mode"
argument_list|,
name|value
operator|=
literal|"WHITELIST"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.patterns"
argument_list|,
name|values
operator|=
block|{
literal|".+ser@example\\.com"
block|,
literal|"a@b\\.com"
block|}
argument_list|)
DECL|method|listFilterWhitelistDoesNotFilterListedUser ()
specifier|public
name|void
name|listFilterWhitelistDoesNotFilterListedUser
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|changeInfo
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
comment|// Check that the comments from the email have been persisted
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.mode"
argument_list|,
name|value
operator|=
literal|"WHITELIST"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.patterns"
argument_list|,
name|values
operator|=
block|{
literal|".+@gerritcodereview\\.com"
block|,
literal|"a@b\\.com"
block|}
argument_list|)
DECL|method|listFilterWhitelistFiltersNotListedUser ()
specifier|public
name|void
name|listFilterWhitelistFiltersNotListedUser
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|changeInfo
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
comment|// Check that the comments from the email have NOT been persisted
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.mode"
argument_list|,
name|value
operator|=
literal|"BLACKLIST"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.patterns"
argument_list|,
name|values
operator|=
block|{
literal|".+@gerritcodereview\\.com"
block|,
literal|"a@b\\.com"
block|}
argument_list|)
DECL|method|listFilterBlacklistDoesNotFilterNotListedUser ()
specifier|public
name|void
name|listFilterBlacklistDoesNotFilterNotListedUser
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|changeInfo
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
comment|// Check that the comments from the email have been persisted
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.mode"
argument_list|,
name|value
operator|=
literal|"BLACKLIST"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.filter.patterns"
argument_list|,
name|values
operator|=
block|{
literal|".+@example\\.com"
block|,
literal|"a@b\\.com"
block|}
argument_list|)
DECL|method|listFilterBlacklistFiltersListedUser ()
specifier|public
name|void
name|listFilterBlacklistFiltersListedUser
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInfo
name|changeInfo
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
comment|// Check that the comments from the email have been persisted
name|Collection
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|id
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|messages
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|createChangeAndReplyByEmail ()
specifier|private
name|ChangeInfo
name|createChangeAndReplyByEmail
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChangeWithReview
argument_list|()
decl_stmt|;
name|ChangeInfo
name|changeInfo
init|=
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
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CommentInfo
argument_list|>
name|comments
init|=
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
name|commentsAsList
argument_list|()
decl_stmt|;
name|String
name|ts
init|=
name|MailUtil
operator|.
name|rfcDateformatter
operator|.
name|format
argument_list|(
name|ZonedDateTime
operator|.
name|ofInstant
argument_list|(
name|comments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|updated
operator|.
name|toInstant
argument_list|()
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// Build Message
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|messageBuilderWithDefaultFields
argument_list|()
decl_stmt|;
name|String
name|txt
init|=
name|newPlaintextBody
argument_list|(
name|canonicalWebUrl
operator|.
name|get
argument_list|()
operator|+
literal|"#/c/"
operator|+
name|changeInfo
operator|.
name|_number
operator|+
literal|"/1"
argument_list|,
literal|"Test Message"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|txt
operator|+
name|textFooterForChange
argument_list|(
name|changeInfo
operator|.
name|_number
argument_list|,
name|ts
argument_list|)
argument_list|)
expr_stmt|;
name|mailProcessor
operator|.
name|process
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|changeInfo
return|;
block|}
block|}
end_class

end_unit

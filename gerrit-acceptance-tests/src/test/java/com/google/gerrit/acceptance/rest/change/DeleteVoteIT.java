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
name|ReviewerState
operator|.
name|REVIEWER
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
name|ImmutableSet
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
name|Iterables
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
name|acceptance
operator|.
name|PushOneCommit
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
name|RestResponse
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
name|testutil
operator|.
name|FakeEmailSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|java
operator|.
name|util
operator|.
name|Map
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
DECL|class|DeleteVoteIT
specifier|public
class|class
name|DeleteVoteIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|deleteVoteOnChange ()
specifier|public
name|void
name|deleteVoteOnChange
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteVote
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteVoteOnRevision ()
specifier|public
name|void
name|deleteVoteOnRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteVote
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteVote (boolean onRevisionLevel)
specifier|private
name|void
name|deleteVote
parameter_list|(
name|boolean
name|onRevisionLevel
parameter_list|)
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|amendChange
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|recommend
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|sender
operator|.
name|clear
argument_list|()
expr_stmt|;
name|String
name|endPoint
init|=
literal|"/changes/"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
operator|+
operator|(
name|onRevisionLevel
condition|?
operator|(
literal|"/revisions/"
operator|+
name|r2
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
operator|)
else|:
literal|""
operator|)
operator|+
literal|"/reviewers/"
operator|+
name|user
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/votes/Code-Review"
decl_stmt|;
name|RestResponse
name|response
init|=
name|adminRestSession
operator|.
name|delete
argument_list|(
name|endPoint
argument_list|)
decl_stmt|;
name|response
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|FakeEmailSender
operator|.
name|Message
argument_list|>
name|messages
init|=
name|sender
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|messages
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|FakeEmailSender
operator|.
name|Message
name|msg
init|=
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|msg
operator|.
name|rcpt
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|user
operator|.
name|emailAddress
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|msg
operator|.
name|body
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|admin
operator|.
name|fullName
operator|+
literal|" has removed a vote on this change.\n"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|msg
operator|.
name|body
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Removed Code-Review+1 by "
operator|+
name|user
operator|.
name|fullName
operator|+
literal|"<"
operator|+
name|user
operator|.
name|email
operator|+
literal|">"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|endPoint
operator|=
literal|"/changes/"
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
operator|+
operator|(
name|onRevisionLevel
condition|?
operator|(
literal|"/revisions/"
operator|+
name|r2
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
operator|)
else|:
literal|""
operator|)
operator|+
literal|"/reviewers/"
operator|+
name|user
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/votes"
expr_stmt|;
name|response
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
name|endPoint
argument_list|)
expr_stmt|;
name|response
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|m
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|response
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|m
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"Code-Review"
argument_list|,
name|Short
operator|.
name|valueOf
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeInfo
name|c
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|ChangeMessageInfo
name|message
init|=
name|Iterables
operator|.
name|getLast
argument_list|(
name|c
operator|.
name|messages
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|author
operator|.
name|_accountId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|admin
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Removed Code-Review+1 by User<user@example.com>\n"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getReviewers
argument_list|(
name|c
operator|.
name|reviewers
operator|.
name|get
argument_list|(
name|REVIEWER
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|admin
operator|.
name|getId
argument_list|()
argument_list|,
name|user
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getReviewers (Collection<AccountInfo> r)
specifier|private
name|Iterable
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getReviewers
parameter_list|(
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|r
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|r
argument_list|,
name|a
lambda|->
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|a
operator|.
name|_accountId
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


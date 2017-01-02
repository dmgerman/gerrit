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
DECL|package|com.google.gerrit.server.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|changes
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|DeleteVoteInput
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
name|RevisionReviewerApi
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
name|restapi
operator|.
name|RestApiException
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
name|DeleteVote
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
name|ReviewerResource
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
name|VoteResource
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
name|Votes
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
name|UpdateException
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
name|Map
import|;
end_import

begin_class
DECL|class|RevisionReviewerApiImpl
specifier|public
class|class
name|RevisionReviewerApiImpl
implements|implements
name|RevisionReviewerApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ReviewerResource r)
name|RevisionReviewerApiImpl
name|create
parameter_list|(
name|ReviewerResource
name|r
parameter_list|)
function_decl|;
block|}
DECL|field|reviewer
specifier|private
specifier|final
name|ReviewerResource
name|reviewer
decl_stmt|;
DECL|field|listVotes
specifier|private
specifier|final
name|Votes
operator|.
name|List
name|listVotes
decl_stmt|;
DECL|field|deleteVote
specifier|private
specifier|final
name|DeleteVote
name|deleteVote
decl_stmt|;
annotation|@
name|Inject
DECL|method|RevisionReviewerApiImpl (Votes.List listVotes, DeleteVote deleteVote, @Assisted ReviewerResource reviewer)
name|RevisionReviewerApiImpl
parameter_list|(
name|Votes
operator|.
name|List
name|listVotes
parameter_list|,
name|DeleteVote
name|deleteVote
parameter_list|,
annotation|@
name|Assisted
name|ReviewerResource
name|reviewer
parameter_list|)
block|{
name|this
operator|.
name|listVotes
operator|=
name|listVotes
expr_stmt|;
name|this
operator|.
name|deleteVote
operator|=
name|deleteVote
expr_stmt|;
name|this
operator|.
name|reviewer
operator|=
name|reviewer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|votes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|votes
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|listVotes
operator|.
name|apply
argument_list|(
name|reviewer
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot list votes"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|deleteVote (String label)
specifier|public
name|void
name|deleteVote
parameter_list|(
name|String
name|label
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|deleteVote
operator|.
name|apply
argument_list|(
operator|new
name|VoteResource
argument_list|(
name|reviewer
argument_list|,
name|label
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UpdateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot delete vote"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|deleteVote (DeleteVoteInput input)
specifier|public
name|void
name|deleteVote
parameter_list|(
name|DeleteVoteInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|deleteVote
operator|.
name|apply
argument_list|(
operator|new
name|VoteResource
argument_list|(
name|reviewer
argument_list|,
name|input
operator|.
name|label
argument_list|)
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UpdateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot delete vote"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


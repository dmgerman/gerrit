begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
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
name|restapi
operator|.
name|NotImplementedException
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|ReviewerApi
specifier|public
interface|interface
name|ReviewerApi
block|{
DECL|method|votes ()
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
function_decl|;
DECL|method|deleteVote (String label)
name|void
name|deleteVote
parameter_list|(
name|String
name|label
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|deleteVote (DeleteVoteInput input)
name|void
name|deleteVote
parameter_list|(
name|DeleteVoteInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|remove ()
name|void
name|remove
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|remove (DeleteReviewerInput input)
name|void
name|remove
parameter_list|(
name|DeleteReviewerInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|ReviewerApi
block|{
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
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
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|remove (DeleteReviewerInput input)
specifier|public
name|void
name|remove
parameter_list|(
name|DeleteReviewerInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit


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
DECL|package|com.google.gerrit.server.mail
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
name|client
operator|.
name|reviewdb
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
name|server
operator|.
name|GerritServer
import|;
end_import

begin_comment
comment|/** Send notice about a change failing to merged. */
end_comment

begin_class
DECL|class|MergeFailSender
specifier|public
class|class
name|MergeFailSender
extends|extends
name|ReplyToChangeSender
block|{
DECL|method|MergeFailSender (GerritServer gs, Change c)
specifier|public
name|MergeFailSender
parameter_list|(
name|GerritServer
name|gs
parameter_list|,
name|Change
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|gs
argument_list|,
name|c
argument_list|,
literal|"comment"
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
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|ccExistingReviewers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format ()
specifier|protected
name|void
name|format
parameter_list|()
block|{
name|appendText
argument_list|(
literal|"Change "
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|patchSetInfo
operator|!=
literal|null
operator|&&
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|!=
literal|null
operator|&&
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|appendText
argument_list|(
literal|" by "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|patchSetInfo
operator|.
name|getAuthor
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|appendText
argument_list|(
literal|" FAILED to submit to "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|".\n\n"
argument_list|)
expr_stmt|;
name|formatCoverLetter
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


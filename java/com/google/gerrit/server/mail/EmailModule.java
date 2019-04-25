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
name|extensions
operator|.
name|config
operator|.
name|FactoryModule
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
name|AbandonedSender
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
name|AddKeySender
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
name|AddReviewerSender
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
name|mail
operator|.
name|send
operator|.
name|CreateChangeSender
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
name|DeleteReviewerSender
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
name|DeleteVoteSender
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
name|mail
operator|.
name|send
operator|.
name|RegisterNewEmailSender
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
name|ReplacePatchSetSender
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
name|RestoredSender
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
name|RevertedSender
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
name|SetAssigneeSender
import|;
end_import

begin_class
DECL|class|EmailModule
specifier|public
class|class
name|EmailModule
extends|extends
name|FactoryModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|factory
argument_list|(
name|AbandonedSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|AddKeySender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|AddReviewerSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|CommentSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|CreateChangeSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|DeleteReviewerSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|DeleteVoteSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|MergedSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|RegisterNewEmailSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ReplacePatchSetSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|RestoredSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|RevertedSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|SetAssigneeSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


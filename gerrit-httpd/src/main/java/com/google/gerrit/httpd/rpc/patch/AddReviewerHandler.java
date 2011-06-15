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
DECL|package|com.google.gerrit.httpd.rpc.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|patch
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
name|data
operator|.
name|ReviewerResult
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|httpd
operator|.
name|rpc
operator|.
name|changedetail
operator|.
name|ChangeDetailFactory
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
name|patch
operator|.
name|AddReviewer
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
name|Collection
import|;
end_import

begin_class
DECL|class|AddReviewerHandler
class|class
name|AddReviewerHandler
extends|extends
name|Handler
argument_list|<
name|ReviewerResult
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (Change.Id changeId, Collection<String> nameOrEmails)
name|AddReviewerHandler
name|create
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|nameOrEmails
parameter_list|)
function_decl|;
block|}
DECL|field|addReviewerFactory
specifier|private
specifier|final
name|AddReviewer
operator|.
name|Factory
name|addReviewerFactory
decl_stmt|;
DECL|field|changeDetailFactory
specifier|private
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|reviewers
specifier|private
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|reviewers
decl_stmt|;
annotation|@
name|Inject
DECL|method|AddReviewerHandler (final AddReviewer.Factory addReviewerFactory, final ChangeDetailFactory.Factory changeDetailFactory, @Assisted final Change.Id changeId, @Assisted final Collection<String> nameOrEmails)
name|AddReviewerHandler
parameter_list|(
specifier|final
name|AddReviewer
operator|.
name|Factory
name|addReviewerFactory
parameter_list|,
specifier|final
name|ChangeDetailFactory
operator|.
name|Factory
name|changeDetailFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|nameOrEmails
parameter_list|)
block|{
name|this
operator|.
name|addReviewerFactory
operator|=
name|addReviewerFactory
expr_stmt|;
name|this
operator|.
name|changeDetailFactory
operator|=
name|changeDetailFactory
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|reviewers
operator|=
name|nameOrEmails
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ReviewerResult
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|ReviewerResult
name|result
init|=
name|addReviewerFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|,
name|reviewers
argument_list|)
operator|.
name|call
argument_list|()
decl_stmt|;
name|result
operator|.
name|setChange
argument_list|(
name|changeDetailFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|)
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit


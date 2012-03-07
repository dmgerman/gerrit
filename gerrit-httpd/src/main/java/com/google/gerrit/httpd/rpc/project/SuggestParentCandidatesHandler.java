begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.httpd.rpc.project
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
name|project
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
name|project
operator|.
name|SuggestParentCandidates
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
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SuggestParentCandidatesHandler
specifier|public
class|class
name|SuggestParentCandidatesHandler
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|Project
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|SuggestParentCandidatesHandler
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|suggestParentCandidates
specifier|private
specifier|final
name|SuggestParentCandidates
name|suggestParentCandidates
decl_stmt|;
annotation|@
name|Inject
DECL|method|SuggestParentCandidatesHandler (final SuggestParentCandidates suggestParentCandidates)
name|SuggestParentCandidatesHandler
parameter_list|(
specifier|final
name|SuggestParentCandidates
name|suggestParentCandidates
parameter_list|)
block|{
name|this
operator|.
name|suggestParentCandidates
operator|=
name|suggestParentCandidates
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|Project
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|suggestParentCandidates
operator|.
name|getProjects
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchSetApproval
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/** Summarizes the approvals (or negative approvals) for a patch set.  * This will typically contain zero or one approvals for each  * category, with all of the approvals coming from a single patch set.  */
end_comment

begin_class
DECL|class|ApprovalSummary
specifier|public
class|class
name|ApprovalSummary
block|{
DECL|field|approvals
specifier|protected
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|approvals
decl_stmt|;
DECL|method|ApprovalSummary ()
specifier|protected
name|ApprovalSummary
parameter_list|()
block|{   }
DECL|method|ApprovalSummary (final Iterable<PatchSetApproval> list)
specifier|public
name|ApprovalSummary
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|list
parameter_list|)
block|{
name|approvals
operator|=
operator|new
name|HashMap
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|PatchSetApproval
name|a
range|:
name|list
control|)
block|{
name|approvals
operator|.
name|put
argument_list|(
name|a
operator|.
name|getCategoryId
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getApprovalMap ()
specifier|public
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|getApprovalMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|approvals
argument_list|)
return|;
block|}
block|}
end_class

end_unit


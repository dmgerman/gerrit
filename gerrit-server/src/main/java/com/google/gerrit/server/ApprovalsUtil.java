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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetApproval
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
name|server
operator|.
name|ReviewDb
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ApprovalsUtil
specifier|public
class|class
name|ApprovalsUtil
block|{
comment|/* Resync the changeOpen status which is cached in the approvals table for      performance reasons*/
DECL|method|syncChangeStatus (final ReviewDb db, final Change change)
specifier|public
specifier|static
name|void
name|syncChangeStatus
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Change
name|change
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
init|=
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|approvals
control|)
block|{
name|a
operator|.
name|cache
argument_list|(
name|change
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|update
argument_list|(
name|approvals
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


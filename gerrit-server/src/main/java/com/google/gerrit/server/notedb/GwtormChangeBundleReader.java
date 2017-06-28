begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|gerrit
operator|.
name|server
operator|.
name|ReviewerSet
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
name|notedb
operator|.
name|ChangeBundle
operator|.
name|Source
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
name|Singleton
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
annotation|@
name|Singleton
DECL|class|GwtormChangeBundleReader
specifier|public
class|class
name|GwtormChangeBundleReader
implements|implements
name|ChangeBundleReader
block|{
annotation|@
name|Inject
DECL|method|GwtormChangeBundleReader ()
name|GwtormChangeBundleReader
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|fromReviewDb (ReviewDb db, Change.Id id)
specifier|public
name|ChangeBundle
name|fromReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
comment|// TODO(dborowitz): Figure out how to do this more consistently, e.g. hand-written inner joins.
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
name|id
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
return|return
operator|new
name|ChangeBundle
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|,
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|byChange
argument_list|(
name|id
argument_list|)
argument_list|,
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|byChange
argument_list|(
name|id
argument_list|)
argument_list|,
name|approvals
argument_list|,
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|byChange
argument_list|(
name|id
argument_list|)
argument_list|,
name|ReviewerSet
operator|.
name|fromApprovals
argument_list|(
name|approvals
argument_list|)
argument_list|,
name|Source
operator|.
name|REVIEW_DB
argument_list|)
return|;
block|}
block|}
end_class

end_unit


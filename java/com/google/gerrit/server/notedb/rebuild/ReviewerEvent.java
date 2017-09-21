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
DECL|package|com.google.gerrit.server.notedb.rebuild
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
operator|.
name|rebuild
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|ToStringHelper
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
name|Table
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
name|server
operator|.
name|notedb
operator|.
name|ChangeUpdate
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
name|ReviewerStateInternal
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|ReviewerEvent
class|class
name|ReviewerEvent
extends|extends
name|Event
block|{
DECL|field|reviewer
specifier|private
name|Table
operator|.
name|Cell
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|,
name|Timestamp
argument_list|>
name|reviewer
decl_stmt|;
DECL|method|ReviewerEvent ( Table.Cell<ReviewerStateInternal, Account.Id, Timestamp> reviewer, Timestamp changeCreatedOn)
name|ReviewerEvent
parameter_list|(
name|Table
operator|.
name|Cell
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|,
name|Timestamp
argument_list|>
name|reviewer
parameter_list|,
name|Timestamp
name|changeCreatedOn
parameter_list|)
block|{
name|super
argument_list|(
comment|// Reviewers aren't generally associated with a particular patch set
comment|// (although as an implementation detail they were in ReviewDb). Just
comment|// use the latest patch set at the time of the event.
literal|null
argument_list|,
name|reviewer
operator|.
name|getColumnKey
argument_list|()
argument_list|,
comment|// TODO(dborowitz): Real account ID shouldn't really matter for
comment|// reviewers, but we might have to deal with this to avoid ChangeBundle
comment|// diffs when run against real data.
name|reviewer
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|reviewer
operator|.
name|getValue
argument_list|()
argument_list|,
name|changeCreatedOn
argument_list|,
literal|null
argument_list|)
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
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeUpdate update)
name|void
name|apply
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|checkUpdate
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|update
operator|.
name|putReviewer
argument_list|(
name|reviewer
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|reviewer
operator|.
name|getRowKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addToString (ToStringHelper helper)
specifier|protected
name|void
name|addToString
parameter_list|(
name|ToStringHelper
name|helper
parameter_list|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"account"
argument_list|,
name|reviewer
operator|.
name|getColumnKey
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"state"
argument_list|,
name|reviewer
operator|.
name|getRowKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


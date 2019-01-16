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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|common
operator|.
name|data
operator|.
name|SubmitRecord
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
name|exceptions
operator|.
name|StorageException
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|change
operator|.
name|ChangeField
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|SubmitRecordPredicate
specifier|public
class|class
name|SubmitRecordPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|create ( String label, SubmitRecord.Label.Status status, Set<Account.Id> accounts)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|create
parameter_list|(
name|String
name|label
parameter_list|,
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
name|status
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
parameter_list|)
block|{
name|String
name|lowerLabel
init|=
name|label
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|accounts
operator|==
literal|null
operator|||
name|accounts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|SubmitRecordPredicate
argument_list|(
name|status
operator|.
name|name
argument_list|()
operator|+
literal|','
operator|+
name|lowerLabel
argument_list|)
return|;
block|}
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|accounts
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|a
lambda|->
operator|new
name|SubmitRecordPredicate
argument_list|(
name|status
operator|.
name|name
argument_list|()
operator|+
literal|','
operator|+
name|lowerLabel
operator|+
literal|','
operator|+
name|a
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|SubmitRecordPredicate (String value)
specifier|private
name|SubmitRecordPredicate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|SUBMIT_RECORD
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData in)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|in
parameter_list|)
throws|throws
name|StorageException
block|{
return|return
name|ChangeField
operator|.
name|formatSubmitRecordValues
argument_list|(
name|in
argument_list|)
operator|.
name|contains
argument_list|(
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit


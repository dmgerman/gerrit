begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
operator|.
name|ChangeQueryBuilder
operator|.
name|Arguments
import|;
end_import

begin_class
DECL|class|EqualsFilePredicate
specifier|public
class|class
name|EqualsFilePredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|create (Arguments args, String value)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|create
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|eqPath
init|=
operator|new
name|EqualsPathPredicate
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_FILE
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|args
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|ChangeField
operator|.
name|FILE_PART
argument_list|)
condition|)
block|{
return|return
name|eqPath
return|;
block|}
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|eqPath
argument_list|,
operator|new
name|EqualsFilePredicate
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|EqualsFilePredicate (String value)
specifier|private
name|EqualsFilePredicate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|FILE_PART
argument_list|,
name|ChangeQueryBuilder
operator|.
name|FIELD_FILE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
return|return
name|ChangeField
operator|.
name|getFileParts
argument_list|(
name|object
argument_list|)
operator|.
name|contains
argument_list|(
name|value
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


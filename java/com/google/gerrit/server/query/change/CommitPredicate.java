begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|ObjectIds
operator|.
name|matchesAbbreviation
import|;
end_import

begin_import
import|import static
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
operator|.
name|COMMIT
import|;
end_import

begin_import
import|import static
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
operator|.
name|EXACT_COMMIT
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
name|git
operator|.
name|ObjectIds
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
name|FieldDef
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
name|PatchSet
import|;
end_import

begin_class
DECL|class|CommitPredicate
specifier|public
class|class
name|CommitPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|commitField (String id)
specifier|static
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|commitField
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|.
name|length
argument_list|()
operator|==
name|ObjectIds
operator|.
name|STR_LEN
condition|)
block|{
return|return
name|EXACT_COMMIT
return|;
block|}
return|return
name|COMMIT
return|;
block|}
DECL|method|CommitPredicate (String id)
specifier|public
name|CommitPredicate
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|commitField
argument_list|(
name|id
argument_list|)
argument_list|,
name|id
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
name|String
name|id
init|=
name|getValue
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|p
range|:
name|object
operator|.
name|patchSets
argument_list|()
control|)
block|{
if|if
condition|(
name|equals
argument_list|(
name|p
argument_list|,
name|id
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|equals (PatchSet p, String id)
specifier|protected
name|boolean
name|equals
parameter_list|(
name|PatchSet
name|p
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|getField
argument_list|()
operator|==
name|EXACT_COMMIT
condition|)
block|{
return|return
name|p
operator|.
name|getCommitId
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
return|;
block|}
return|return
name|matchesAbbreviation
argument_list|(
name|p
operator|.
name|getCommitId
argument_list|()
argument_list|,
name|id
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


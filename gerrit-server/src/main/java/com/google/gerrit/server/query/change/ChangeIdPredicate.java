begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|server
operator|.
name|index
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
name|index
operator|.
name|IndexPredicate
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
import|;
end_import

begin_class
DECL|class|ChangeIdPredicate
class|class
name|ChangeIdPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
block|{
DECL|field|args
specifier|private
specifier|final
name|Arguments
name|args
decl_stmt|;
DECL|method|ChangeIdPredicate (Arguments args, String id)
name|ChangeIdPredicate
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|ID
argument_list|,
name|ChangeQueryBuilder
operator|.
name|FIELD_CHANGE
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (final ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
specifier|final
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
name|change
init|=
name|cd
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|key
init|=
name|change
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|getValue
argument_list|()
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
name|Change
operator|.
name|Key
name|a
init|=
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Key
name|b
init|=
name|a
operator|.
name|max
argument_list|()
decl_stmt|;
return|return
name|ChangeDataResultSet
operator|.
name|change
argument_list|(
name|args
operator|.
name|changeDataFactory
argument_list|,
name|args
operator|.
name|db
argument_list|,
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|byKeyRange
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
literal|true
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
name|ChangeCosts
operator|.
name|cost
argument_list|(
name|ChangeCosts
operator|.
name|CHANGES_SCAN
argument_list|,
name|getCardinality
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
return|return
name|ChangeCosts
operator|.
name|CARD_KEY
return|;
block|}
block|}
end_class

end_unit


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
name|CurrentUser
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
name|IdentifiedUser
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
name|OperatorPredicate
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|IsStarredByPredicate
class|class
name|IsStarredByPredicate
extends|extends
name|OperatorPredicate
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
block|{
DECL|method|describe (CurrentUser user)
specifier|private
specifier|static
name|String
name|describe
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
if|if
condition|(
name|user
operator|instanceof
name|IdentifiedUser
condition|)
block|{
return|return
operator|(
operator|(
name|IdentifiedUser
operator|)
name|user
operator|)
operator|.
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|user
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|method|IsStarredByPredicate (Provider<ReviewDb> db, CurrentUser user)
name|IsStarredByPredicate
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_STARREDBY
argument_list|,
name|describe
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (final ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
specifier|final
name|ChangeData
name|object
parameter_list|)
block|{
return|return
name|user
operator|.
name|getStarredChanges
argument_list|()
operator|.
name|contains
argument_list|(
name|object
operator|.
name|getId
argument_list|()
argument_list|)
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
return|return
name|ChangeDataResultSet
operator|.
name|change
argument_list|(
comment|//
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|user
operator|.
name|getStarredChanges
argument_list|()
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
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
return|return
literal|10
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
name|IDS_MEMORY
argument_list|,
name|getCardinality
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


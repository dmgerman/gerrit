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
name|inject
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|SortKeyPredicate
specifier|abstract
class|class
name|SortKeyPredicate
extends|extends
name|OperatorPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|dbProvider
specifier|protected
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|method|SortKeyPredicate (Provider<ReviewDb> dbProvider, String name, String value)
name|SortKeyPredicate
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
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
DECL|class|Before
specifier|static
class|class
name|Before
extends|extends
name|SortKeyPredicate
block|{
DECL|method|Before (Provider<ReviewDb> dbProvider, String value)
name|Before
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|dbProvider
argument_list|,
literal|"sortkey_before"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
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
argument_list|(
name|dbProvider
argument_list|)
decl_stmt|;
return|return
name|change
operator|!=
literal|null
operator|&&
name|change
operator|.
name|getSortKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|getValue
argument_list|()
argument_list|)
operator|<
literal|0
return|;
block|}
block|}
DECL|class|After
specifier|static
class|class
name|After
extends|extends
name|SortKeyPredicate
block|{
DECL|method|After (Provider<ReviewDb> dbProvider, String value)
name|After
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|dbProvider
argument_list|,
literal|"sortkey_after"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
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
argument_list|(
name|dbProvider
argument_list|)
decl_stmt|;
return|return
name|change
operator|!=
literal|null
operator|&&
name|change
operator|.
name|getSortKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|getValue
argument_list|()
argument_list|)
operator|>
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit


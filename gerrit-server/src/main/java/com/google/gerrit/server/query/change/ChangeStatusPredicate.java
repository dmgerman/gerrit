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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|ImmutableBiMap
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
name|gerrit
operator|.
name|server
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * Predicate for a {@link Change.Status}.  *<p>  * The actual name of this operator can differ, it usually comes as {@code  * status:} but may also be {@code is:} to help do-what-i-meanery for end-users  * searching for changes. Either operator name has the same meaning.  */
end_comment

begin_class
DECL|class|ChangeStatusPredicate
specifier|final
class|class
name|ChangeStatusPredicate
extends|extends
name|OperatorPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|VALUES
specifier|private
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|Change
operator|.
name|Status
argument_list|,
name|String
argument_list|>
name|VALUES
decl_stmt|;
static|static
block|{
name|ImmutableBiMap
operator|.
name|Builder
argument_list|<
name|Change
operator|.
name|Status
argument_list|,
name|String
argument_list|>
name|values
init|=
name|ImmutableBiMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Status
name|s
range|:
name|Change
operator|.
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
name|values
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|s
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|VALUES
operator|=
name|values
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|open (Provider<ReviewDb> dbProvider)
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|open
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Change
operator|.
name|Status
name|e
range|:
name|Change
operator|.
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|ChangeStatusPredicate
argument_list|(
name|dbProvider
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
name|r
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|or
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|method|closed (Provider<ReviewDb> dbProvider)
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|closed
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Change
operator|.
name|Status
name|e
range|:
name|Change
operator|.
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|ChangeStatusPredicate
argument_list|(
name|dbProvider
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
name|r
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|or
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|status
specifier|private
specifier|final
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|method|ChangeStatusPredicate (Provider<ReviewDb> dbProvider, String value)
name|ChangeStatusPredicate
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
name|ChangeQueryBuilder
operator|.
name|FIELD_STATUS
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
name|status
operator|=
name|VALUES
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|status
operator|!=
literal|null
argument_list|,
literal|"invalid change status: %s"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|ChangeStatusPredicate (Provider<ReviewDb> dbProvider, Change.Status status)
name|ChangeStatusPredicate
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_STATUS
argument_list|,
name|VALUES
operator|.
name|get
argument_list|(
name|status
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getStatus ()
name|Change
operator|.
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
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
throws|throws
name|OrmException
block|{
name|Change
name|change
init|=
name|object
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
name|status
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getStatus
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
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|status
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|ChangeStatusPredicate
condition|)
block|{
specifier|final
name|ChangeStatusPredicate
name|p
init|=
operator|(
name|ChangeStatusPredicate
operator|)
name|other
decl_stmt|;
return|return
name|status
operator|.
name|equals
argument_list|(
name|p
operator|.
name|status
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getOperator
argument_list|()
operator|+
literal|":"
operator|+
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit


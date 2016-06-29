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
name|Change
operator|.
name|Status
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NavigableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Predicate for a {@link Status}.  *<p>  * The actual name of this operator can differ, it usually comes as {@code  * status:} but may also be {@code is:} to help do-what-i-meanery for end-users  * searching for changes. Either operator name has the same meaning.  *<p>  * Status names are looked up by prefix case-insensitively.  */
end_comment

begin_class
DECL|class|ChangeStatusPredicate
specifier|public
specifier|final
class|class
name|ChangeStatusPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|field|PREDICATES
specifier|private
specifier|static
specifier|final
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|PREDICATES
decl_stmt|;
DECL|field|CLOSED
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|CLOSED
decl_stmt|;
DECL|field|OPEN
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|OPEN
decl_stmt|;
static|static
block|{
name|PREDICATES
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|open
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|closed
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|ChangeStatusPredicate
name|p
init|=
operator|new
name|ChangeStatusPredicate
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|PREDICATES
operator|.
name|put
argument_list|(
name|canonicalize
argument_list|(
name|s
argument_list|)
argument_list|,
name|p
argument_list|)
expr_stmt|;
operator|(
name|s
operator|.
name|isOpen
argument_list|()
condition|?
name|open
else|:
name|closed
operator|)
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
name|CLOSED
operator|=
name|Predicate
operator|.
name|or
argument_list|(
name|closed
argument_list|)
expr_stmt|;
name|OPEN
operator|=
name|Predicate
operator|.
name|or
argument_list|(
name|open
argument_list|)
expr_stmt|;
name|PREDICATES
operator|.
name|put
argument_list|(
literal|"closed"
argument_list|,
name|CLOSED
argument_list|)
expr_stmt|;
name|PREDICATES
operator|.
name|put
argument_list|(
literal|"open"
argument_list|,
name|OPEN
argument_list|)
expr_stmt|;
name|PREDICATES
operator|.
name|put
argument_list|(
literal|"pending"
argument_list|,
name|OPEN
argument_list|)
expr_stmt|;
block|}
DECL|method|canonicalize (Change.Status status)
specifier|public
specifier|static
name|String
name|canonicalize
parameter_list|(
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
return|return
name|status
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
DECL|method|parse (String value)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|parse
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
name|lower
init|=
name|value
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|head
init|=
name|PREDICATES
operator|.
name|tailMap
argument_list|(
name|lower
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|head
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Assume no statuses share a common prefix so we can only walk one entry.
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|e
init|=
name|head
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|lower
argument_list|)
condition|)
block|{
return|return
name|e
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid change status: "
operator|+
name|value
argument_list|)
throw|;
block|}
DECL|method|open ()
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|open
parameter_list|()
block|{
return|return
name|OPEN
return|;
block|}
DECL|method|closed ()
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|closed
parameter_list|()
block|{
return|return
name|CLOSED
return|;
block|}
DECL|field|status
specifier|private
specifier|final
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|method|ChangeStatusPredicate (Change.Status status)
name|ChangeStatusPredicate
parameter_list|(
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|STATUS
argument_list|,
name|canonicalize
argument_list|(
name|status
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getStatus ()
specifier|public
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
argument_list|()
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


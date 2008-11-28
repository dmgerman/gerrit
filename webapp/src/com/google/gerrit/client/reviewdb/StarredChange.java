begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_comment
comment|/** A {@link Change} starred by an {@link Account}. */
end_comment

begin_class
DECL|class|StarredChange
specifier|public
class|class
name|StarredChange
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
implements|implements
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Column
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
DECL|field|changeId
specifier|protected
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
name|changeId
operator|=
operator|new
name|Change
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Account.Id a, final Change.Id g)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|Change
operator|.
name|Id
name|g
parameter_list|)
block|{
name|accountId
operator|=
name|a
expr_stmt|;
name|changeId
operator|=
name|g
expr_stmt|;
block|}
DECL|method|getParentKey ()
specifier|public
name|Account
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|accountId
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
name|accountId
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|changeId
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Key
operator|&&
operator|(
operator|(
name|Key
operator|)
name|o
operator|)
operator|.
name|accountId
operator|.
name|equals
argument_list|(
name|accountId
argument_list|)
operator|&&
operator|(
operator|(
name|Key
operator|)
name|o
operator|)
operator|.
name|changeId
operator|.
name|equals
argument_list|(
name|changeId
argument_list|)
return|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
DECL|method|StarredChange ()
specifier|protected
name|StarredChange
parameter_list|()
block|{   }
DECL|method|StarredChange (final StarredChange.Key k)
specifier|public
name|StarredChange
parameter_list|(
specifier|final
name|StarredChange
operator|.
name|Key
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|key
operator|.
name|accountId
return|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|key
operator|.
name|changeId
return|;
block|}
block|}
end_class

end_unit


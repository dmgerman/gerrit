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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|RefRight
import|;
end_import

begin_comment
comment|/**  * Additional data about a {@link RefRight} not normally loaded: defines if a  * right is inherited from a parent structure (e.g. a parent project).  */
end_comment

begin_class
DECL|class|InheritedRefRight
specifier|public
class|class
name|InheritedRefRight
block|{
DECL|field|right
specifier|private
name|RefRight
name|right
decl_stmt|;
DECL|field|inherited
specifier|private
name|boolean
name|inherited
decl_stmt|;
DECL|field|owner
specifier|private
name|boolean
name|owner
decl_stmt|;
comment|/**    * Creates a instance of a {@link RefRight} with data about inheritance    */
DECL|method|InheritedRefRight ()
specifier|protected
name|InheritedRefRight
parameter_list|()
block|{   }
comment|/**    * Creates a instance of a {@link RefRight} with data about inheritance    *    * @param right the right    * @param inherited true if the right is inherited, false otherwise    * @param owner true if right is owned by current user, false otherwise    */
DECL|method|InheritedRefRight (RefRight right, boolean inherited, boolean owner)
specifier|public
name|InheritedRefRight
parameter_list|(
name|RefRight
name|right
parameter_list|,
name|boolean
name|inherited
parameter_list|,
name|boolean
name|owner
parameter_list|)
block|{
name|this
operator|.
name|right
operator|=
name|right
expr_stmt|;
name|this
operator|.
name|inherited
operator|=
name|inherited
expr_stmt|;
name|this
operator|.
name|owner
operator|=
name|owner
expr_stmt|;
block|}
DECL|method|getRight ()
specifier|public
name|RefRight
name|getRight
parameter_list|()
block|{
return|return
name|right
return|;
block|}
DECL|method|isInherited ()
specifier|public
name|boolean
name|isInherited
parameter_list|()
block|{
return|return
name|inherited
return|;
block|}
DECL|method|isOwner ()
specifier|public
name|boolean
name|isOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|InheritedRefRight
condition|)
block|{
name|InheritedRefRight
name|a
init|=
name|this
decl_stmt|;
name|InheritedRefRight
name|b
init|=
operator|(
name|InheritedRefRight
operator|)
name|o
decl_stmt|;
return|return
name|a
operator|.
name|getRight
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getRight
argument_list|()
argument_list|)
operator|&&
name|a
operator|.
name|isInherited
argument_list|()
operator|==
name|b
operator|.
name|isInherited
argument_list|()
return|;
block|}
return|return
literal|false
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
name|getRight
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit


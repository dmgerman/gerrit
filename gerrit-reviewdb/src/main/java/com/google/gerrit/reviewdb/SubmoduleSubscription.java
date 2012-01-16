begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|CompoundKey
import|;
end_import

begin_comment
comment|/**  * Defining a project/branch subscription to a project/branch project.  *<p>  * This means a class instance represents a repo/branch subscription to a  * project/branch (the subscriber).  *<p>  * A subscriber operates a submodule in defined path.  */
end_comment

begin_class
DECL|class|SubmoduleSubscription
specifier|public
specifier|final
class|class
name|SubmoduleSubscription
block|{
comment|/** Subscription key */
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/**      * Indicates the super project, aka subscriber: the project owner of the      * gitlinks to the submodules.      */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|superProject
specifier|protected
name|Branch
operator|.
name|NameKey
name|superProject
decl_stmt|;
comment|/**      * Indicates the submodule, aka subscription: the project the subscriber's      * gitlink is pointed to.      */
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|submodule
specifier|protected
name|Branch
operator|.
name|NameKey
name|submodule
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|superProject
operator|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|()
expr_stmt|;
name|submodule
operator|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Branch.NameKey superProject, final Branch.NameKey submodule)
specifier|protected
name|Key
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|superProject
parameter_list|,
specifier|final
name|Branch
operator|.
name|NameKey
name|submodule
parameter_list|)
block|{
name|this
operator|.
name|superProject
operator|=
name|superProject
expr_stmt|;
name|this
operator|.
name|submodule
operator|=
name|submodule
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getParentKey
parameter_list|()
block|{
return|return
name|superProject
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
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
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
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
name|?
argument_list|>
index|[]
block|{
name|submodule
block|}
empty_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
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
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|path
specifier|protected
name|String
name|path
decl_stmt|;
DECL|method|SubmoduleSubscription ()
specifier|protected
name|SubmoduleSubscription
parameter_list|()
block|{   }
DECL|method|SubmoduleSubscription (final Branch.NameKey superProject, final Branch.NameKey submodule, final String path)
specifier|public
name|SubmoduleSubscription
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|superProject
parameter_list|,
specifier|final
name|Branch
operator|.
name|NameKey
name|submodule
parameter_list|,
specifier|final
name|String
name|path
parameter_list|)
block|{
name|key
operator|=
operator|new
name|Key
argument_list|(
name|superProject
argument_list|,
name|submodule
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
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
name|key
operator|.
name|superProject
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|" "
operator|+
name|key
operator|.
name|superProject
operator|.
name|get
argument_list|()
operator|+
literal|", "
operator|+
name|key
operator|.
name|submodule
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|" "
operator|+
name|key
operator|.
name|submodule
operator|.
name|get
argument_list|()
operator|+
literal|", "
operator|+
name|path
return|;
block|}
DECL|method|getSuperProject ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getSuperProject
parameter_list|()
block|{
return|return
name|key
operator|.
name|superProject
return|;
block|}
DECL|method|getSubmodule ()
specifier|public
name|Branch
operator|.
name|NameKey
name|getSubmodule
parameter_list|()
block|{
return|return
name|key
operator|.
name|submodule
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
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
name|SubmoduleSubscription
condition|)
block|{
return|return
name|key
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SubmoduleSubscription
operator|)
name|o
operator|)
operator|.
name|key
argument_list|)
operator|&&
name|path
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SubmoduleSubscription
operator|)
name|o
operator|)
operator|.
name|path
argument_list|)
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
name|key
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit


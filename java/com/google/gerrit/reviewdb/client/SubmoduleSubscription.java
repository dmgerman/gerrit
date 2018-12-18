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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|StringKey
import|;
end_import

begin_comment
comment|/**  * Defining a project/branch subscription to a project/branch project.  *  *<p>This means a class instance represents a repo/branch subscription to a project/branch (the  * subscriber).  *  *<p>A subscriber operates a submodule in defined path.  */
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
name|StringKey
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
comment|/**      * Indicates the super project, aka subscriber: the project owner of the gitlinks to the      * submodules.      */
DECL|field|superProject
specifier|protected
name|Branch
operator|.
name|NameKey
name|superProject
decl_stmt|;
DECL|field|submodulePath
specifier|protected
name|String
name|submodulePath
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
block|}
DECL|method|Key (Branch.NameKey superProject, String path)
specifier|protected
name|Key
parameter_list|(
name|Branch
operator|.
name|NameKey
name|superProject
parameter_list|,
name|String
name|path
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
name|submodulePath
operator|=
name|path
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
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|submodulePath
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|this
operator|.
name|submodulePath
operator|=
name|newValue
expr_stmt|;
block|}
block|}
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
DECL|field|submodule
specifier|protected
name|Branch
operator|.
name|NameKey
name|submodule
decl_stmt|;
DECL|method|SubmoduleSubscription ()
specifier|protected
name|SubmoduleSubscription
parameter_list|()
block|{}
DECL|method|SubmoduleSubscription (Branch.NameKey superProject, Branch.NameKey submodule, String path)
specifier|public
name|SubmoduleSubscription
parameter_list|(
name|Branch
operator|.
name|NameKey
name|superProject
parameter_list|,
name|Branch
operator|.
name|NameKey
name|submodule
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
operator|new
name|Key
argument_list|(
name|superProject
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|submodule
operator|=
name|submodule
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
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
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|key
operator|.
name|get
argument_list|()
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
name|submodule
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
name|submodule
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
name|submodule
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getSuperProject
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" follows "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getSubmodule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
name|AccountGroup
import|;
end_import

begin_comment
comment|/** Describes a group within a projects {@link AccessSection}s. */
end_comment

begin_class
DECL|class|GroupReference
specifier|public
class|class
name|GroupReference
implements|implements
name|Comparable
argument_list|<
name|GroupReference
argument_list|>
block|{
comment|/** @return a new reference to the given group description. */
DECL|method|forGroup (AccountGroup group)
specifier|public
specifier|static
name|GroupReference
name|forGroup
parameter_list|(
name|AccountGroup
name|group
parameter_list|)
block|{
return|return
operator|new
name|GroupReference
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|field|uuid
specifier|protected
name|String
name|uuid
decl_stmt|;
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|GroupReference ()
specifier|protected
name|GroupReference
parameter_list|()
block|{   }
DECL|method|GroupReference (AccountGroup.UUID uuid, String name)
specifier|public
name|GroupReference
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|setUUID
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getUUID
parameter_list|()
block|{
return|return
name|uuid
operator|!=
literal|null
condition|?
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|uuid
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|setUUID (AccountGroup.UUID newUUID)
specifier|public
name|void
name|setUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|newUUID
parameter_list|)
block|{
name|uuid
operator|=
name|newUUID
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String newName)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|newName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compareTo (GroupReference o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|GroupReference
name|o
parameter_list|)
block|{
return|return
name|uuid
argument_list|(
name|this
argument_list|)
operator|.
name|compareTo
argument_list|(
name|uuid
argument_list|(
name|o
argument_list|)
argument_list|)
return|;
block|}
DECL|method|uuid (GroupReference a)
specifier|private
specifier|static
name|String
name|uuid
parameter_list|(
name|GroupReference
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getUUID
argument_list|()
operator|!=
literal|null
condition|?
name|a
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
else|:
literal|"?"
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
name|uuid
argument_list|(
name|this
argument_list|)
operator|.
name|hashCode
argument_list|()
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
return|return
name|o
operator|instanceof
name|GroupReference
operator|&&
name|compareTo
argument_list|(
operator|(
name|GroupReference
operator|)
name|o
argument_list|)
operator|==
literal|0
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
literal|"Group["
operator|+
name|getName
argument_list|()
operator|+
literal|" / "
operator|+
name|getUUID
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit


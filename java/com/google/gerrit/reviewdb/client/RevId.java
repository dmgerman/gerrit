begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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

begin_comment
comment|/** A revision identifier for a file or a change. */
end_comment

begin_class
DECL|class|RevId
specifier|public
specifier|final
class|class
name|RevId
block|{
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|method|RevId ()
specifier|protected
name|RevId
parameter_list|()
block|{}
DECL|method|RevId (String str)
specifier|public
name|RevId
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|id
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return the value of this revision id. */
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|id
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
name|id
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
operator|(
name|o
operator|instanceof
name|RevId
operator|)
operator|&&
name|id
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|RevId
operator|)
name|o
operator|)
operator|.
name|id
argument_list|)
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
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"{"
operator|+
name|id
operator|+
literal|"}"
return|;
block|}
DECL|method|matches (String str)
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|id
operator|.
name|startsWith
argument_list|(
name|str
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


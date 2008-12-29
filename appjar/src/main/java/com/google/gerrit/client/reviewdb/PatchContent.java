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
name|KeyUtil
import|;
end_import

begin_comment
comment|/** Content of a single patch file */
end_comment

begin_class
DECL|class|PatchContent
specifier|public
specifier|final
class|class
name|PatchContent
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
argument_list|>
block|{
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|40
argument_list|)
DECL|field|sha1
specifier|protected
name|String
name|sha1
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{     }
DECL|method|Key (final String ps)
specifier|public
name|Key
parameter_list|(
specifier|final
name|String
name|ps
parameter_list|)
block|{
name|sha1
operator|=
name|ps
expr_stmt|;
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
name|sha1
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
name|getClass
argument_list|()
operator|==
name|o
operator|.
name|getClass
argument_list|()
operator|&&
name|sha1
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Key
operator|)
name|o
operator|)
operator|.
name|sha1
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
name|KeyUtil
operator|.
name|encode
argument_list|(
name|sha1
argument_list|)
return|;
block|}
DECL|method|getParentKey ()
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
name|getParentKey
parameter_list|()
block|{
return|return
literal|null
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
name|PatchContent
operator|.
name|Key
name|key
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|content
specifier|protected
name|String
name|content
decl_stmt|;
DECL|method|PatchContent ()
specifier|protected
name|PatchContent
parameter_list|()
block|{   }
DECL|method|PatchContent (final PatchContent.Key k, final String c)
specifier|public
name|PatchContent
parameter_list|(
specifier|final
name|PatchContent
operator|.
name|Key
name|k
parameter_list|,
specifier|final
name|String
name|c
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
name|content
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|PatchContent
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getContent ()
specifier|public
name|String
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
block|}
end_class

end_unit


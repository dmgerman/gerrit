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
name|Key
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
name|StringKey
import|;
end_import

begin_comment
comment|/** Types of approvals that can be associated with a {@link Change}. */
end_comment

begin_class
DECL|class|ApprovalCategory
specifier|public
specifier|final
class|class
name|ApprovalCategory
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|StringKey
argument_list|<
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
literal|4
argument_list|)
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final String a)
specifier|public
name|Id
parameter_list|(
specifier|final
name|String
name|a
parameter_list|)
block|{
name|id
operator|=
name|a
expr_stmt|;
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
name|id
return|;
block|}
block|}
annotation|@
name|Column
DECL|field|categoryId
specifier|protected
name|Id
name|categoryId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|20
argument_list|)
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
annotation|@
name|Column
DECL|field|position
specifier|protected
name|short
name|position
decl_stmt|;
DECL|method|ApprovalCategory ()
specifier|protected
name|ApprovalCategory
parameter_list|()
block|{   }
DECL|method|ApprovalCategory (final ApprovalCategory.Id id, final String name)
specifier|public
name|ApprovalCategory
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|categoryId
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|ApprovalCategory
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|categoryId
return|;
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
DECL|method|setName (final String n)
specifier|public
name|void
name|setName
parameter_list|(
specifier|final
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
DECL|method|getPosition ()
specifier|public
name|short
name|getPosition
parameter_list|()
block|{
return|return
name|position
return|;
block|}
DECL|method|setPosition (final short p)
specifier|public
name|void
name|setPosition
parameter_list|(
specifier|final
name|short
name|p
parameter_list|)
block|{
name|position
operator|=
name|p
expr_stmt|;
block|}
block|}
end_class

end_unit


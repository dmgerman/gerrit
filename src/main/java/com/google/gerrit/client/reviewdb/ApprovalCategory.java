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
name|gerrit
operator|.
name|client
operator|.
name|workflow
operator|.
name|CategoryFunction
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
name|client
operator|.
name|workflow
operator|.
name|MaxWithBlock
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
name|client
operator|.
name|workflow
operator|.
name|NoOpFunction
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
comment|/** Id of the special "Submit" action (and category). */
DECL|field|SUBMIT
specifier|public
specifier|static
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|SUBMIT
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
literal|"SUBM"
argument_list|)
decl_stmt|;
comment|/** Id of the special "Read" action (and category). */
DECL|field|READ
specifier|public
specifier|static
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|READ
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
literal|"READ"
argument_list|)
decl_stmt|;
comment|/** Id of the special "Push Annotated Tag" action (and category). */
DECL|field|PUSH_TAG
specifier|public
specifier|static
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|PUSH_TAG
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
literal|"pTAG"
argument_list|)
decl_stmt|;
comment|/** Id of the special "Push Branch" action (and category). */
DECL|field|PUSH_HEAD
specifier|public
specifier|static
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|PUSH_HEAD
init|=
operator|new
name|ApprovalCategory
operator|.
name|Id
argument_list|(
literal|"pHD"
argument_list|)
decl_stmt|;
DECL|field|PUSH_HEAD_UPDATE
specifier|public
specifier|static
specifier|final
name|short
name|PUSH_HEAD_UPDATE
init|=
literal|1
decl_stmt|;
DECL|field|PUSH_HEAD_CREATE
specifier|public
specifier|static
specifier|final
name|short
name|PUSH_HEAD_CREATE
init|=
literal|2
decl_stmt|;
DECL|field|PUSH_HEAD_REPLACE
specifier|public
specifier|static
specifier|final
name|short
name|PUSH_HEAD_REPLACE
init|=
literal|3
decl_stmt|;
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
name|id
operator|=
name|newValue
expr_stmt|;
block|}
block|}
comment|/** Internal short unique identifier for this category. */
annotation|@
name|Column
DECL|field|categoryId
specifier|protected
name|Id
name|categoryId
decl_stmt|;
comment|/** Unique name for this category, shown in the web interface to users. */
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
comment|/**    * Order of this category within the Approvals table when presented.    *<p>    * If< 0 (e.g. -1) this category is not shown in the Approvals table but is    * instead considered to be an action that the user might be able to perform,    * e.g. "Submit".    *<p>    * If>= 0 this category is shown in the Approvals table, sorted along with    * its siblings by<code>position, name</code>.    */
annotation|@
name|Column
DECL|field|position
specifier|protected
name|short
name|position
decl_stmt|;
comment|/** Identity of the function used to aggregate the category's value. */
annotation|@
name|Column
DECL|field|functionName
specifier|protected
name|String
name|functionName
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
name|this
operator|.
name|functionName
operator|=
name|MaxWithBlock
operator|.
name|NAME
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
DECL|method|isAction ()
specifier|public
name|boolean
name|isAction
parameter_list|()
block|{
return|return
name|position
operator|<
literal|0
return|;
block|}
DECL|method|getFunctionName ()
specifier|public
name|String
name|getFunctionName
parameter_list|()
block|{
return|return
name|functionName
return|;
block|}
DECL|method|setFunctionName (final String name)
specifier|public
name|void
name|setFunctionName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|functionName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getFunction ()
specifier|public
name|CategoryFunction
name|getFunction
parameter_list|()
block|{
specifier|final
name|CategoryFunction
name|r
init|=
name|CategoryFunction
operator|.
name|forName
argument_list|(
name|functionName
argument_list|)
decl_stmt|;
return|return
name|r
operator|!=
literal|null
condition|?
name|r
else|:
operator|new
name|NoOpFunction
argument_list|()
return|;
block|}
block|}
end_class

end_unit


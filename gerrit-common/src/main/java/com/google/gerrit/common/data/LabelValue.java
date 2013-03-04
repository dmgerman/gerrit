begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|client
operator|.
name|ApprovalCategoryValue
import|;
end_import

begin_class
DECL|class|LabelValue
specifier|public
class|class
name|LabelValue
block|{
annotation|@
name|Deprecated
DECL|method|fromApprovalCategoryValue (ApprovalCategoryValue acv)
specifier|public
specifier|static
name|LabelValue
name|fromApprovalCategoryValue
parameter_list|(
name|ApprovalCategoryValue
name|acv
parameter_list|)
block|{
return|return
operator|new
name|LabelValue
argument_list|(
name|acv
operator|.
name|getValue
argument_list|()
argument_list|,
name|acv
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|formatValue (short value)
specifier|public
specifier|static
name|String
name|formatValue
parameter_list|(
name|short
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|<
literal|0
condition|)
block|{
return|return
name|Short
operator|.
name|toString
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|==
literal|0
condition|)
block|{
return|return
literal|" 0"
return|;
block|}
else|else
block|{
return|return
literal|"+"
operator|+
name|Short
operator|.
name|toString
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
DECL|field|value
specifier|protected
name|short
name|value
decl_stmt|;
DECL|field|text
specifier|protected
name|String
name|text
decl_stmt|;
DECL|method|LabelValue (short value, String text)
specifier|public
name|LabelValue
parameter_list|(
name|short
name|value
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|LabelValue ()
specifier|protected
name|LabelValue
parameter_list|()
block|{   }
DECL|method|getValue ()
specifier|public
name|short
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|formatValue ()
specifier|public
name|String
name|formatValue
parameter_list|()
block|{
return|return
name|formatValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|format ()
specifier|public
name|String
name|format
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|formatValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|toString
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
return|return
name|format
argument_list|()
return|;
block|}
block|}
end_class

end_unit


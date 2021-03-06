begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|SubmitRequirementInfo
specifier|public
class|class
name|SubmitRequirementInfo
block|{
DECL|field|status
specifier|public
specifier|final
name|String
name|status
decl_stmt|;
DECL|field|fallbackText
specifier|public
specifier|final
name|String
name|fallbackText
decl_stmt|;
DECL|field|type
specifier|public
specifier|final
name|String
name|type
decl_stmt|;
DECL|field|data
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
decl_stmt|;
DECL|method|SubmitRequirementInfo ( String status, String fallbackText, String type, Map<String, String> data)
specifier|public
name|SubmitRequirementInfo
parameter_list|(
name|String
name|status
parameter_list|,
name|String
name|fallbackText
parameter_list|,
name|String
name|type
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|fallbackText
operator|=
name|fallbackText
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
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
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|SubmitRequirementInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|SubmitRequirementInfo
name|that
init|=
operator|(
name|SubmitRequirementInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|status
argument_list|,
name|that
operator|.
name|status
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|fallbackText
argument_list|,
name|that
operator|.
name|fallbackText
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|type
argument_list|,
name|that
operator|.
name|type
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|that
operator|.
name|data
argument_list|)
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
name|Objects
operator|.
name|hash
argument_list|(
name|status
argument_list|,
name|fallbackText
argument_list|,
name|type
argument_list|,
name|data
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
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
operator|.
name|add
argument_list|(
literal|"fallbackText"
argument_list|,
name|fallbackText
argument_list|)
operator|.
name|add
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
operator|.
name|add
argument_list|(
literal|"data"
argument_list|,
name|data
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


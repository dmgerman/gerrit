begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Result from adding a reviewer to a change. */
end_comment

begin_class
DECL|class|AddReviewerResult
specifier|public
class|class
name|AddReviewerResult
block|{
DECL|field|errors
specifier|protected
name|List
argument_list|<
name|Error
argument_list|>
name|errors
decl_stmt|;
DECL|field|change
specifier|protected
name|ChangeDetail
name|change
decl_stmt|;
DECL|method|AddReviewerResult ()
specifier|public
name|AddReviewerResult
parameter_list|()
block|{
name|errors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Error
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|addError (final Error e)
specifier|public
name|void
name|addError
parameter_list|(
specifier|final
name|Error
name|e
parameter_list|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|Error
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
DECL|method|getChange ()
specifier|public
name|ChangeDetail
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|setChange (final ChangeDetail d)
specifier|public
name|void
name|setChange
parameter_list|(
specifier|final
name|ChangeDetail
name|d
parameter_list|)
block|{
name|change
operator|=
name|d
expr_stmt|;
block|}
DECL|class|Error
specifier|public
specifier|static
class|class
name|Error
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
comment|/** Name supplied does not match to a registered account. */
DECL|enumConstant|ACCOUNT_NOT_FOUND
name|ACCOUNT_NOT_FOUND
block|,
comment|/** The account is not permitted to see the change. */
DECL|enumConstant|CHANGE_NOT_VISIBLE
name|CHANGE_NOT_VISIBLE
block|}
DECL|field|type
specifier|protected
name|Type
name|type
decl_stmt|;
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|Error ()
specifier|protected
name|Error
parameter_list|()
block|{     }
DECL|method|Error (final Type type, final String who)
specifier|public
name|Error
parameter_list|(
specifier|final
name|Type
name|type
parameter_list|,
specifier|final
name|String
name|who
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|who
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
operator|+
literal|" "
operator|+
name|name
return|;
block|}
block|}
block|}
end_class

end_unit


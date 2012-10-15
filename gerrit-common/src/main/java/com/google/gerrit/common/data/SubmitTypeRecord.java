begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|Project
import|;
end_import

begin_comment
comment|/**  * Describes the submit type for a change.  */
end_comment

begin_class
DECL|class|SubmitTypeRecord
specifier|public
class|class
name|SubmitTypeRecord
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/** The type was computed successfully */
DECL|enumConstant|OK
name|OK
block|,
comment|/** An internal server error occurred preventing computation.      *<p>      * Additional detail may be available in {@link SubmitTypeRecord#errorMessage}      */
DECL|enumConstant|RULE_ERROR
name|RULE_ERROR
block|}
DECL|method|OK (Project.SubmitType type)
specifier|public
specifier|static
name|SubmitTypeRecord
name|OK
parameter_list|(
name|Project
operator|.
name|SubmitType
name|type
parameter_list|)
block|{
name|SubmitTypeRecord
name|r
init|=
operator|new
name|SubmitTypeRecord
argument_list|()
decl_stmt|;
name|r
operator|.
name|status
operator|=
name|Status
operator|.
name|OK
expr_stmt|;
name|r
operator|.
name|type
operator|=
name|type
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|type
specifier|public
name|Project
operator|.
name|SubmitType
name|type
decl_stmt|;
DECL|field|errorMessage
specifier|public
name|String
name|errorMessage
decl_stmt|;
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
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|status
operator|==
name|Status
operator|.
name|RULE_ERROR
operator|&&
name|errorMessage
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
operator|.
name|append
argument_list|(
name|errorMessage
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|type
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
block|}
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


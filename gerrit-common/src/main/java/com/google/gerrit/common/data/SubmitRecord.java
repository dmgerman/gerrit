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
name|Account
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
comment|/**  * Describes the state required to submit a change.  */
end_comment

begin_class
DECL|class|SubmitRecord
specifier|public
class|class
name|SubmitRecord
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/** The change is ready for submission. */
DECL|enumConstant|OK
name|OK
block|,
comment|/** The change is missing a required label. */
DECL|enumConstant|NOT_READY
name|NOT_READY
block|,
comment|/** The change has been closed. */
DECL|enumConstant|CLOSED
name|CLOSED
block|,
comment|/**      * An internal server error occurred preventing computation.      *<p>      * Additional detail may be available in {@link SubmitRecord#errorMessage}.      */
DECL|enumConstant|RULE_ERROR
name|RULE_ERROR
block|;   }
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|labels
specifier|public
name|List
argument_list|<
name|Label
argument_list|>
name|labels
decl_stmt|;
DECL|field|errorMessage
specifier|public
name|String
name|errorMessage
decl_stmt|;
DECL|class|Label
specifier|public
specifier|static
class|class
name|Label
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/**        * This label provides what is necessary for submission.        *<p>        * If provided, {@link Label#appliedBy} describes the user account        * that applied this label to the change.        */
DECL|enumConstant|OK
name|OK
block|,
comment|/**        * This label prevents the change from being submitted.        *<p>        * If provided, {@link Label#appliedBy} describes the user account        * that applied this label to the change.        */
DECL|enumConstant|REJECT
name|REJECT
block|,
comment|/**        * The label is required for submission, but has not been satisfied.        */
DECL|enumConstant|NEED
name|NEED
block|,
comment|/**        * The label may be set, but it's neither necessary for submission        * nor does it block submission if set.        */
DECL|enumConstant|MAY
name|MAY
block|,
comment|/**        * The label is required for submission, but is impossible to complete.        * The likely cause is access has not been granted correctly by the        * project owner or site administrator.        */
DECL|enumConstant|IMPOSSIBLE
name|IMPOSSIBLE
block|;     }
DECL|field|label
specifier|public
name|String
name|label
decl_stmt|;
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|appliedBy
specifier|public
name|Account
operator|.
name|Id
name|appliedBy
decl_stmt|;
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
name|label
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|appliedBy
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" by "
argument_list|)
operator|.
name|append
argument_list|(
name|appliedBy
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
literal|')'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
if|if
condition|(
name|labels
operator|!=
literal|null
condition|)
block|{
name|String
name|delimiter
init|=
literal|""
decl_stmt|;
for|for
control|(
name|Label
name|label
range|:
name|labels
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|delimiter
argument_list|)
operator|.
name|append
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|delimiter
operator|=
literal|", "
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|']'
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


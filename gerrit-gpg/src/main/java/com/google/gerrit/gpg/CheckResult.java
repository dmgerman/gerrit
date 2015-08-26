begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/** Result of checking an object like a key or signature. */
end_comment

begin_class
DECL|class|CheckResult
specifier|public
class|class
name|CheckResult
block|{
DECL|field|problems
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|problems
decl_stmt|;
DECL|method|CheckResult (String... problems)
name|CheckResult
parameter_list|(
name|String
modifier|...
name|problems
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|problems
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CheckResult (List<String> problems)
name|CheckResult
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|problems
parameter_list|)
block|{
name|this
operator|.
name|problems
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|problems
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * @return whether the result is entirely ok, i.e. has passed any verification    *     or validation checks.    */
DECL|method|isOk ()
specifier|public
name|boolean
name|isOk
parameter_list|()
block|{
return|return
name|problems
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** @return any problems encountered during checking. */
DECL|method|getProblems ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProblems
parameter_list|()
block|{
return|return
name|problems
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|problems
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|problems
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


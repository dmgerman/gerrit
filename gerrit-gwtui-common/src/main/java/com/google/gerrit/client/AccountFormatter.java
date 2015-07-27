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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|info
operator|.
name|AccountInfo
import|;
end_import

begin_class
DECL|class|AccountFormatter
specifier|public
class|class
name|AccountFormatter
block|{
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|method|AccountFormatter (String anonymousCowardName)
specifier|public
name|AccountFormatter
parameter_list|(
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
comment|/**    * Formats an account as a name and an email address.    *<p>    * Example output:    *<ul>    *<li>{@code A U. Thor&lt;author@example.com&gt;}: full populated</li>    *<li>{@code A U. Thor (12)}: missing email address</li>    *<li>{@code Anonymous Coward&lt;author@example.com&gt;}: missing name</li>    *<li>{@code Anonymous Coward (12)}: missing name and email address</li>    *</ul>    */
DECL|method|nameEmail (AccountInfo info)
specifier|public
name|String
name|nameEmail
parameter_list|(
name|AccountInfo
name|info
parameter_list|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|name
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|email
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|info
operator|.
name|_accountId
argument_list|()
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|_accountId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Formats an account name.    *<p>    * If the account has a full name, it returns only the full name. Otherwise it    * returns a longer form that includes the email address.    */
DECL|method|name (AccountInfo ai)
specifier|public
name|String
name|name
parameter_list|(
name|AccountInfo
name|ai
parameter_list|)
block|{
if|if
condition|(
name|ai
operator|.
name|name
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|ai
operator|.
name|name
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ai
operator|.
name|name
argument_list|()
return|;
block|}
name|String
name|email
init|=
name|ai
operator|.
name|email
argument_list|()
decl_stmt|;
if|if
condition|(
name|email
operator|!=
literal|null
condition|)
block|{
name|int
name|at
init|=
name|email
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
return|return
literal|0
operator|<
name|at
condition|?
name|email
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|at
argument_list|)
else|:
name|email
return|;
block|}
return|return
name|nameEmail
argument_list|(
name|ai
argument_list|)
return|;
block|}
block|}
end_class

end_unit


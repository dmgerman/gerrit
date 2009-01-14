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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
package|;
end_package

begin_comment
comment|/** Result from a sign-in attempt via the LoginServlet. */
end_comment

begin_class
DECL|class|SignInResult
specifier|public
class|class
name|SignInResult
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
comment|/** The user canceled the sign-in and wasn't able to complete it */
DECL|enumConstant|SuppressWarnings
annotation|@
name|SuppressWarnings
argument_list|(
literal|"hiding"
argument_list|)
DECL|enumConstant|CANCEL
name|CANCEL
block|,
comment|/** The sign-in was successful and we have the account data */
DECL|enumConstant|SuppressWarnings
annotation|@
name|SuppressWarnings
argument_list|(
literal|"hiding"
argument_list|)
DECL|enumConstant|SUCCESS
name|SUCCESS
decl_stmt|;
block|}
comment|/** Singleton representing {@link Status#CANCEL}. */
DECL|field|CANCEL
specifier|public
specifier|static
specifier|final
name|SignInResult
name|CANCEL
init|=
operator|new
name|SignInResult
argument_list|(
name|Status
operator|.
name|CANCEL
argument_list|)
decl_stmt|;
comment|/** Singleton representing {@link Status#CANCEL}. */
DECL|field|SUCCESS
specifier|public
specifier|static
specifier|final
name|SignInResult
name|SUCCESS
init|=
operator|new
name|SignInResult
argument_list|(
name|Status
operator|.
name|SUCCESS
argument_list|)
decl_stmt|;
DECL|field|status
specifier|protected
name|Status
name|status
decl_stmt|;
DECL|method|SignInResult ()
specifier|protected
name|SignInResult
parameter_list|()
block|{   }
comment|/** Create a new result. */
DECL|method|SignInResult (final Status s)
specifier|public
name|SignInResult
parameter_list|(
specifier|final
name|Status
name|s
parameter_list|)
block|{
name|status
operator|=
name|s
expr_stmt|;
block|}
comment|/** The status of the login attempt */
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|entities
operator|.
name|Account
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
name|extensions
operator|.
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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

begin_class
annotation|@
name|Singleton
DECL|class|AccountsConsistencyChecker
specifier|public
class|class
name|AccountsConsistencyChecker
block|{
DECL|field|accounts
specifier|private
specifier|final
name|Accounts
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountsConsistencyChecker (Accounts accounts)
name|AccountsConsistencyChecker
parameter_list|(
name|Accounts
name|accounts
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
block|}
DECL|method|check ()
specifier|public
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|check
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountState
name|accountState
range|:
name|accounts
operator|.
name|all
argument_list|()
control|)
block|{
name|Account
name|account
init|=
name|accountState
operator|.
name|account
argument_list|()
decl_stmt|;
if|if
condition|(
name|account
operator|.
name|preferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|accountState
operator|.
name|externalIds
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|e
lambda|->
name|account
operator|.
name|preferredEmail
argument_list|()
operator|.
name|equals
argument_list|(
name|e
operator|.
name|email
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|addError
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Account '%s' has no external ID for its preferred email '%s'"
argument_list|,
name|account
operator|.
name|id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|account
operator|.
name|preferredEmail
argument_list|()
argument_list|)
argument_list|,
name|problems
argument_list|)
block|;         }
block|}
block|}
return|return
name|problems
return|;
block|}
DECL|method|addError (String error, List<ConsistencyProblemInfo> problems)
specifier|private
specifier|static
name|void
name|addError
parameter_list|(
name|String
name|error
parameter_list|,
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
parameter_list|)
block|{
name|problems
operator|.
name|add
argument_list|(
operator|new
name|ConsistencyProblemInfo
argument_list|(
name|ConsistencyProblemInfo
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|error
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


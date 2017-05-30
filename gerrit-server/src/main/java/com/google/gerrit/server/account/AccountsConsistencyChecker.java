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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalIds
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
name|server
operator|.
name|OrmException
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
name|Provider
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
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|externalIds
specifier|private
specifier|final
name|ExternalIds
name|externalIds
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountsConsistencyChecker (Provider<ReviewDb> dbProvider, ExternalIds externalIds)
name|AccountsConsistencyChecker
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|ExternalIds
name|externalIds
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|externalIds
operator|=
name|externalIds
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
name|OrmException
throws|,
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
name|Account
name|account
range|:
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|accounts
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
if|if
condition|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|externalIds
operator|.
name|byAccount
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
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
name|getPreferredEmail
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
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|account
operator|.
name|getPreferredEmail
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


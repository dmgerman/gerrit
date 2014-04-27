begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|accounts
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
name|common
operator|.
name|AccountInfo
import|;
end_import

begin_class
DECL|class|AccountInfoMapper
specifier|public
class|class
name|AccountInfoMapper
block|{
DECL|method|fromAcountInfo ( com.google.gerrit.server.account.AccountInfo i)
specifier|public
specifier|static
name|AccountInfo
name|fromAcountInfo
parameter_list|(
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
name|AccountInfo
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AccountInfo
name|ai
init|=
operator|new
name|AccountInfo
argument_list|()
decl_stmt|;
name|fromAccount
argument_list|(
name|i
argument_list|,
name|ai
argument_list|)
expr_stmt|;
return|return
name|ai
return|;
block|}
DECL|method|fromAccount ( com.google.gerrit.server.account.AccountInfo i, AccountInfo ai)
specifier|public
specifier|static
name|void
name|fromAccount
parameter_list|(
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
name|AccountInfo
name|i
parameter_list|,
name|AccountInfo
name|ai
parameter_list|)
block|{
name|ai
operator|.
name|_accountId
operator|=
name|i
operator|.
name|_accountId
expr_stmt|;
name|ai
operator|.
name|email
operator|=
name|i
operator|.
name|email
expr_stmt|;
name|ai
operator|.
name|name
operator|=
name|i
operator|.
name|name
expr_stmt|;
name|ai
operator|.
name|username
operator|=
name|i
operator|.
name|username
expr_stmt|;
block|}
block|}
end_class

end_unit


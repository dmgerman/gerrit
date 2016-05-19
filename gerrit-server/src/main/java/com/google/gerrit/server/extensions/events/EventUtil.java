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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|server
operator|.
name|account
operator|.
name|AccountCache
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

begin_class
DECL|class|EventUtil
specifier|public
class|class
name|EventUtil
block|{
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|EventUtil (AccountCache accountCache)
name|EventUtil
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|)
block|{
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
block|}
DECL|method|accountInfo (Account a)
specifier|public
name|AccountInfo
name|accountInfo
parameter_list|(
name|Account
name|a
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
operator|||
name|a
operator|.
name|getId
argument_list|()
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
argument_list|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|ai
operator|.
name|email
operator|=
name|a
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
name|ai
operator|.
name|name
operator|=
name|a
operator|.
name|getFullName
argument_list|()
expr_stmt|;
name|ai
operator|.
name|username
operator|=
name|a
operator|.
name|getUserName
argument_list|()
expr_stmt|;
return|return
name|ai
return|;
block|}
DECL|method|accountInfo (Account.Id accountId)
specifier|public
name|AccountInfo
name|accountInfo
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|accountInfo
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
operator|.
name|getAccount
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|exceptions
operator|.
name|InvalidSshKeyException
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
name|AccountSshKey
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
name|AbstractModule
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
name|Module
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

begin_class
annotation|@
name|Singleton
DECL|class|NoSshKeyCache
specifier|public
class|class
name|NoSshKeyCache
implements|implements
name|SshKeyCache
implements|,
name|SshKeyCreator
block|{
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|SshKeyCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|NoSshKeyCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshKeyCreator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|NoSshKeyCache
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|evict (String username)
specifier|public
name|void
name|evict
parameter_list|(
name|String
name|username
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|create (Account.Id accountId, int seq, String encoded)
specifier|public
name|AccountSshKey
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|int
name|seq
parameter_list|,
name|String
name|encoded
parameter_list|)
throws|throws
name|InvalidSshKeyException
block|{
throw|throw
operator|new
name|InvalidSshKeyException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit


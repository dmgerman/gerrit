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
name|reviewdb
operator|.
name|AccountSshKey
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
DECL|class|SshKeyList
specifier|final
class|class
name|SshKeyList
block|{
DECL|field|keys
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|keys
decl_stmt|;
DECL|method|SshKeyList ()
specifier|protected
name|SshKeyList
parameter_list|()
block|{   }
DECL|method|SshKeyList (final List<AccountSshKey> k)
specifier|public
name|SshKeyList
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountSshKey
argument_list|>
name|k
parameter_list|)
block|{
name|keys
operator|=
name|k
expr_stmt|;
block|}
block|}
end_class

end_unit


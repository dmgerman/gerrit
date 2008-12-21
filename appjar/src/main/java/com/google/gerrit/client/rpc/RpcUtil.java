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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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
name|Gerrit
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
name|client
operator|.
name|reviewdb
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_class
DECL|class|RpcUtil
specifier|public
class|class
name|RpcUtil
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|RpcConstants
name|C
decl_stmt|;
DECL|field|caImpl
specifier|private
specifier|static
name|CurrentAccountImpl
name|caImpl
decl_stmt|;
static|static
block|{
if|if
condition|(
name|GWT
operator|.
name|isClient
argument_list|()
condition|)
block|{
name|C
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|RpcConstants
operator|.
name|class
argument_list|)
expr_stmt|;
name|caImpl
operator|=
operator|new
name|CurrentAccountImpl
argument_list|()
block|{
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
specifier|final
name|Account
name|a
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
decl_stmt|;
return|return
name|a
operator|!=
literal|null
condition|?
name|a
operator|.
name|getId
argument_list|()
else|:
literal|null
return|;
block|}
block|}
expr_stmt|;
block|}
else|else
block|{
name|C
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/** Get the unique id for this account; null if there is no account. */
DECL|method|getAccountId ()
specifier|public
specifier|static
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|caImpl
operator|.
name|getAccountId
argument_list|()
return|;
block|}
DECL|method|setCurrentAccountImpl (final CurrentAccountImpl i)
specifier|public
specifier|static
name|void
name|setCurrentAccountImpl
parameter_list|(
specifier|final
name|CurrentAccountImpl
name|i
parameter_list|)
block|{
name|caImpl
operator|=
name|i
expr_stmt|;
block|}
DECL|interface|CurrentAccountImpl
specifier|public
interface|interface
name|CurrentAccountImpl
block|{
comment|/** Get the unique id for this account; null if there is no account. */
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit


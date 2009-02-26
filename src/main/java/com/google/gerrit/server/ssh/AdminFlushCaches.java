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
name|client
operator|.
name|data
operator|.
name|GroupCache
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
name|rpc
operator|.
name|Common
import|;
end_import

begin_comment
comment|/** Causes the caches to purge all entries and reload. */
end_comment

begin_class
DECL|class|AdminFlushCaches
class|class
name|AdminFlushCaches
extends|extends
name|AbstractCommand
block|{
annotation|@
name|Override
DECL|method|run (String[] args)
specifier|protected
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Failure
block|{
specifier|final
name|GroupCache
name|gc
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
decl_stmt|;
if|if
condition|(
name|gc
operator|.
name|isAdministrator
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|gc
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Common
operator|.
name|getAccountCache
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
name|SshUtil
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: Not a Gerrit administrator"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


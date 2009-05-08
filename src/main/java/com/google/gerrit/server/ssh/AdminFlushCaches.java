begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|reviewdb
operator|.
name|SystemConfig
operator|.
name|LoginType
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
DECL|field|p
name|PrintWriter
name|p
decl_stmt|;
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
throws|,
name|UnsupportedEncodingException
block|{
name|assertIsAdministrator
argument_list|()
expr_stmt|;
name|p
operator|=
name|toPrintWriter
argument_list|(
name|err
argument_list|)
expr_stmt|;
try|try
block|{
name|Common
operator|.
name|getGroupCache
argument_list|()
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
if|if
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
operator|==
name|LoginType
operator|.
name|OPENID
condition|)
block|{
name|flushCache
argument_list|(
literal|"openid"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getGerritServer
argument_list|()
operator|.
name|getDiffCache
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e1
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
literal|"warning: cannot flush cache \"diff\": "
operator|+
name|e1
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|p
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|flushCache (final String name)
specifier|private
name|void
name|flushCache
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|getGerritServer
argument_list|()
operator|.
name|getCache
argument_list|(
name|name
argument_list|)
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e1
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
literal|"warning: cannot flush cache \""
operator|+
name|name
operator|+
literal|"\": "
operator|+
name|e1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


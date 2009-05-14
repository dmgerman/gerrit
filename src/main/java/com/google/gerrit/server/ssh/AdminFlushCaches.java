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
name|rpc
operator|.
name|Common
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|constructs
operator|.
name|blocking
operator|.
name|SelfPopulatingCache
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
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
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
specifier|final
name|SelfPopulatingCache
name|diffCache
init|=
name|getGerritServer
argument_list|()
operator|.
name|getDiffCache
argument_list|()
decl_stmt|;
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
for|for
control|(
specifier|final
name|Ehcache
name|c
range|:
name|getGerritServer
argument_list|()
operator|.
name|getAllCaches
argument_list|()
control|)
block|{
specifier|final
name|String
name|name
init|=
name|c
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|diffCache
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|c
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
literal|"error: cannot flush cache \""
operator|+
name|name
operator|+
literal|"\": "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|saveToDisk
argument_list|(
name|diffCache
argument_list|)
expr_stmt|;
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
DECL|method|saveToDisk (final Ehcache c)
specifier|private
name|void
name|saveToDisk
parameter_list|(
specifier|final
name|Ehcache
name|c
parameter_list|)
block|{
try|try
block|{
name|c
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
literal|"warning: cannot save cache \""
operator|+
name|c
operator|.
name|getName
argument_list|()
operator|+
literal|"\": "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|ProjectCache
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
name|Project
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
name|ProjectRight
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|client
operator|.
name|OrmException
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_class
DECL|class|ListProjects
class|class
name|ListProjects
extends|extends
name|AbstractCommand
block|{
annotation|@
name|Override
DECL|method|run (final String[] args)
specifier|protected
name|void
name|run
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|Failure
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"usage: "
operator|+
name|getName
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|PrintWriter
name|stdout
init|=
name|toPrintWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
specifier|final
name|ReviewDb
name|db
init|=
name|openReviewDb
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ProjectCache
name|cache
init|=
name|Common
operator|.
name|getProjectCache
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Project
name|p
range|:
name|db
operator|.
name|projects
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
if|if
condition|(
name|ProjectRight
operator|.
name|WILD_PROJECT
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
comment|// This project "doesn't exist". At least not as a repository.
comment|//
continue|continue;
block|}
specifier|final
name|ProjectCache
operator|.
name|Entry
name|e
init|=
name|cache
operator|.
name|get
argument_list|(
name|p
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|canRead
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|stdout
operator|.
name|print
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: database error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


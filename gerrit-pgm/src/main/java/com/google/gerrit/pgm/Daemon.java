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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|server
operator|.
name|cache
operator|.
name|CachePool
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
name|config
operator|.
name|GerritGlobalModule
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
name|sshd
operator|.
name|SshDaemon
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
name|sshd
operator|.
name|SshModule
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
name|sshd
operator|.
name|commands
operator|.
name|MasterCommandModule
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
name|sshd
operator|.
name|commands
operator|.
name|SlaveCommandModule
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
name|Injector
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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

begin_comment
comment|/** Run only the SSH daemon portions of Gerrit. */
end_comment

begin_class
DECL|class|Daemon
specifier|public
class|class
name|Daemon
extends|extends
name|AbstractProgram
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--slave"
argument_list|,
name|usage
operator|=
literal|"support fetch only"
argument_list|)
DECL|field|slave
name|boolean
name|slave
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|sysInjector
init|=
name|GerritGlobalModule
operator|.
name|createInjector
argument_list|()
decl_stmt|;
name|Injector
name|sshInjector
init|=
name|createSshInjector
argument_list|(
name|sysInjector
argument_list|)
decl_stmt|;
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|CachePool
operator|.
name|class
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|sshInjector
operator|.
name|getInstance
argument_list|(
name|SshDaemon
operator|.
name|class
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|never
argument_list|()
return|;
block|}
DECL|method|createSshInjector (final Injector sysInjector)
specifier|private
name|Injector
name|createSshInjector
parameter_list|(
specifier|final
name|Injector
name|sysInjector
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SshModule
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|slave
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|SlaveCommandModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|MasterCommandModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
name|gerrit
operator|.
name|git
operator|.
name|PushAllProjectsOp
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
name|git
operator|.
name|ReplicationQueue
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

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Force a project to replicate, again. */
end_comment

begin_class
DECL|class|AdminReplicate
class|class
name|AdminReplicate
extends|extends
name|AbstractCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--all"
argument_list|,
name|usage
operator|=
literal|"push all known projects"
argument_list|)
DECL|field|all
specifier|private
name|boolean
name|all
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--url"
argument_list|,
name|metaVar
operator|=
literal|"PATTERN"
argument_list|,
name|usage
operator|=
literal|"pattern to match URL on"
argument_list|)
DECL|field|urlMatch
specifier|private
name|String
name|urlMatch
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"PROJECT"
argument_list|,
name|usage
operator|=
literal|"project name"
argument_list|)
DECL|field|projectNames
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|projectNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
annotation|@
name|Inject
DECL|field|pushAllOpFactory
specifier|private
name|PushAllProjectsOp
operator|.
name|Factory
name|pushAllOpFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|replication
specifier|private
name|ReplicationQueue
name|replication
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
block|{
name|assertIsAdministrator
argument_list|()
expr_stmt|;
if|if
condition|(
name|all
operator|&&
name|projectNames
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"error: cannot combine --all and PROJECT"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|replication
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"error: replication not enabled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|all
condition|)
block|{
name|pushAllOpFactory
operator|.
name|create
argument_list|(
name|urlMatch
argument_list|)
operator|.
name|start
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
specifier|final
name|String
name|name
range|:
name|projectNames
control|)
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|key
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|replication
operator|.
name|scheduleFullSync
argument_list|(
name|key
argument_list|,
name|urlMatch
argument_list|)
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
literal|"error: '"
operator|+
name|name
operator|+
literal|"': not a Gerrit project"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit


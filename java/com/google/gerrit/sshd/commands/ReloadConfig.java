begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|CommandMetaData
operator|.
name|Mode
operator|.
name|MASTER_OR_SLAVE
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|extensions
operator|.
name|annotations
operator|.
name|RequiresCapability
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
name|ConfigUpdatedEvent
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
name|ConfigUpdatedEvent
operator|.
name|UpdateResult
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
name|GerritServerConfigReloader
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
name|CommandMetaData
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
name|SshCommand
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/** Issues a reload of gerrit.config. */
end_comment

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"reload-config"
argument_list|,
name|description
operator|=
literal|"Reloads the Gerrit configuration"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ReloadConfig
specifier|public
class|class
name|ReloadConfig
extends|extends
name|SshCommand
block|{
DECL|field|gerritServerConfigReloader
annotation|@
name|Inject
specifier|private
name|GerritServerConfigReloader
name|gerritServerConfigReloader
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
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|updates
init|=
name|gerritServerConfigReloader
operator|.
name|reloadConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|updates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|stdout
operator|.
name|println
argument_list|(
literal|"No config entries updated!"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Print out UpdateResult.{ACCEPTED|REJECTED} entries grouped by their type
for|for
control|(
name|UpdateResult
name|updateResult
range|:
name|UpdateResult
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|filteredUpdates
init|=
name|filterUpdates
argument_list|(
name|updates
argument_list|,
name|updateResult
argument_list|)
decl_stmt|;
if|if
condition|(
name|filteredUpdates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|stdout
operator|.
name|println
argument_list|(
name|updateResult
operator|.
name|toString
argument_list|()
operator|+
literal|" configuration changes:"
argument_list|)
expr_stmt|;
name|filteredUpdates
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|update
lambda|->
name|update
operator|.
name|getConfigUpdates
argument_list|()
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|cfgEntry
lambda|->
name|stdout
operator|.
name|println
argument_list|(
name|cfgEntry
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|filterUpdates ( List<ConfigUpdatedEvent.Update> updates, UpdateResult result)
specifier|public
specifier|static
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|filterUpdates
parameter_list|(
name|List
argument_list|<
name|ConfigUpdatedEvent
operator|.
name|Update
argument_list|>
name|updates
parameter_list|,
name|UpdateResult
name|result
parameter_list|)
block|{
return|return
name|updates
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|update
lambda|->
name|update
operator|.
name|getResult
argument_list|()
operator|==
name|result
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


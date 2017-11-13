begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|common
operator|.
name|GroupInfo
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
name|restapi
operator|.
name|Url
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
name|AccountGroup
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
name|server
operator|.
name|group
operator|.
name|InternalGroup
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
name|group
operator|.
name|ListGroups
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
name|ioutil
operator|.
name|ColumnFormatter
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
name|gerrit
operator|.
name|util
operator|.
name|cli
operator|.
name|Options
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
name|Optional
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

begin_class
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"ls-groups"
argument_list|,
name|description
operator|=
literal|"List groups visible to the caller"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ListGroupsCommand
specifier|public
class|class
name|ListGroupsCommand
extends|extends
name|SshCommand
block|{
DECL|field|groupCache
annotation|@
name|Inject
specifier|private
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|listGroups
annotation|@
name|Inject
annotation|@
name|Options
specifier|public
name|ListGroups
name|listGroups
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--verbose"
argument_list|,
name|aliases
operator|=
block|{
literal|"-v"
block|}
argument_list|,
name|usage
operator|=
literal|"verbose output format with tab-separated columns for the "
operator|+
literal|"group name, UUID, description, owner group name, "
operator|+
literal|"owner group UUID, and whether the group is visible to all"
argument_list|)
DECL|field|verboseOutput
specifier|private
name|boolean
name|verboseOutput
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|listGroups
operator|.
name|getUser
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|listGroups
operator|.
name|getProjects
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"--user and --project options are not compatible."
argument_list|)
throw|;
block|}
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|stdout
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupInfo
name|info
range|:
name|listGroups
operator|.
name|get
argument_list|()
control|)
block|{
name|formatter
operator|.
name|addColumn
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|info
operator|.
name|name
argument_list|,
literal|"n/a"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|verboseOutput
condition|)
block|{
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|group
init|=
name|info
operator|.
name|ownerId
operator|!=
literal|null
condition|?
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|Url
operator|.
name|decode
argument_list|(
name|info
operator|.
name|ownerId
argument_list|)
argument_list|)
argument_list|)
else|:
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|Url
operator|.
name|decode
argument_list|(
name|info
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|info
operator|.
name|description
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|group
operator|.
name|map
argument_list|(
name|InternalGroup
operator|::
name|getName
argument_list|)
operator|.
name|orElse
argument_list|(
literal|"n/a"
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|group
operator|.
name|map
argument_list|(
name|g
lambda|->
name|g
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|orElse
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|info
operator|.
name|options
operator|.
name|visibleToAll
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
block|}
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


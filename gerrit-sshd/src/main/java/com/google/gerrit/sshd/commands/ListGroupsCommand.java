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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
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
name|restapi
operator|.
name|MethodNotAllowedException
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
name|ResourceNotFoundException
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
name|IdentifiedUser
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
name|GetGroups
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
name|account
operator|.
name|GroupControl
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
name|GroupJson
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
name|GroupJson
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
name|BaseCommand
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
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
name|io
operator|.
name|PrintWriter
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
name|descr
operator|=
literal|"List groups visible to the caller"
argument_list|)
DECL|class|ListGroupsCommand
specifier|public
class|class
name|ListGroupsCommand
extends|extends
name|BaseCommand
block|{
annotation|@
name|Inject
DECL|field|impl
specifier|private
name|MyListGroups
name|impl
decl_stmt|;
annotation|@
name|Override
DECL|method|start (final Environment env)
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|Environment
name|env
parameter_list|)
block|{
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|parseCommandLine
argument_list|(
name|impl
argument_list|)
expr_stmt|;
if|if
condition|(
name|impl
operator|.
name|getUser
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|impl
operator|.
name|getProjects
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
literal|"fatal: --user and --project options are not compatible."
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
try|try
block|{
name|impl
operator|.
name|display
argument_list|(
name|stdout
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|class|MyListGroups
specifier|private
specifier|static
class|class
name|MyListGroups
extends|extends
name|ListGroups
block|{
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
name|Inject
DECL|method|MyListGroups (final GroupCache groupCache, final GroupControl.Factory groupControlFactory, final GroupControl.GenericFactory genericGroupControlFactory, final Provider<IdentifiedUser> identifiedUser, final IdentifiedUser.GenericFactory userFactory, final Provider<GetGroups> accountGetGroups, final GroupJson json)
name|MyListGroups
parameter_list|(
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|GenericFactory
name|genericGroupControlFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|GetGroups
argument_list|>
name|accountGetGroups
parameter_list|,
specifier|final
name|GroupJson
name|json
parameter_list|)
block|{
name|super
argument_list|(
name|groupCache
argument_list|,
name|groupControlFactory
argument_list|,
name|genericGroupControlFactory
argument_list|,
name|identifiedUser
argument_list|,
name|userFactory
argument_list|,
name|accountGetGroups
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final PrintWriter out)
name|void
name|display
parameter_list|(
specifier|final
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|MethodNotAllowedException
throws|,
name|OrmException
block|{
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|out
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|GroupInfo
name|info
range|:
name|get
argument_list|()
control|)
block|{
name|formatter
operator|.
name|addColumn
argument_list|(
name|Objects
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
name|AccountGroup
name|o
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
literal|null
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
name|o
operator|!=
literal|null
condition|?
name|o
operator|.
name|getName
argument_list|()
else|:
literal|"n/a"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|o
operator|!=
literal|null
condition|?
name|o
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
else|:
literal|""
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
name|Objects
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
block|}
end_class

end_unit


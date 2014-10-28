begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|AccountInfo
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
name|GroupDetailFactory
operator|.
name|Factory
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
name|ListMembers
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Implements a command that allows the user to see the members of a group.  */
end_comment

begin_class
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"ls-members"
argument_list|,
name|description
operator|=
literal|"List the members of a given group"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ListMembersCommand
specifier|public
class|class
name|ListMembersCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Inject
DECL|field|impl
name|ListMembersCommandImpl
name|impl
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
name|impl
operator|.
name|display
argument_list|(
name|stdout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseCommandLine ()
specifier|protected
name|void
name|parseCommandLine
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
name|parseCommandLine
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
DECL|class|ListMembersCommandImpl
specifier|private
specifier|static
class|class
name|ListMembersCommandImpl
extends|extends
name|ListMembers
block|{
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"the name of the group"
argument_list|,
name|metaVar
operator|=
literal|"GROUPNAME"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListMembersCommandImpl (GroupCache groupCache, Factory groupDetailFactory, AccountInfo.Loader.Factory accountLoaderFactory)
specifier|protected
name|ListMembersCommandImpl
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|Factory
name|groupDetailFactory
parameter_list|,
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|)
block|{
name|super
argument_list|(
name|groupCache
argument_list|,
name|groupDetailFactory
argument_list|,
name|accountLoaderFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
block|}
DECL|method|display (PrintWriter writer)
name|void
name|display
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|errorText
init|=
literal|"Group not found or not visible\n"
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|errorText
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
init|=
name|apply
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
decl_stmt|;
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|writer
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"full name"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"email"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
for|for
control|(
name|AccountInfo
name|member
range|:
name|members
control|)
block|{
if|if
condition|(
name|member
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|formatter
operator|.
name|addColumn
argument_list|(
name|member
operator|.
name|_id
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|member
operator|.
name|username
argument_list|,
literal|"n/a"
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|member
operator|.
name|name
argument_list|)
argument_list|,
literal|"n/a"
argument_list|)
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|member
operator|.
name|email
argument_list|,
literal|"n/a"
argument_list|)
argument_list|)
expr_stmt|;
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


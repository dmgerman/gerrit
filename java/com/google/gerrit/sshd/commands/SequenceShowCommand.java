begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|notedb
operator|.
name|Sequences
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_comment
comment|/** Display sequence value. */
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
literal|"show"
argument_list|,
name|description
operator|=
literal|"Display the sequence value"
argument_list|)
DECL|class|SequenceShowCommand
specifier|final
class|class
name|SequenceShowCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"sequence name"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|sequences
annotation|@
name|Inject
name|Sequences
name|sequences
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
name|int
name|current
decl_stmt|;
switch|switch
condition|(
name|name
condition|)
block|{
case|case
literal|"changes"
case|:
name|current
operator|=
name|sequences
operator|.
name|currentChangeId
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"accounts"
case|:
name|current
operator|=
name|sequences
operator|.
name|currentAccountId
argument_list|()
expr_stmt|;
break|break;
case|case
literal|"groups"
case|:
name|current
operator|=
name|sequences
operator|.
name|currentGroupId
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
name|die
argument_list|(
literal|"Unknown sequence name: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|stdout
operator|.
name|print
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


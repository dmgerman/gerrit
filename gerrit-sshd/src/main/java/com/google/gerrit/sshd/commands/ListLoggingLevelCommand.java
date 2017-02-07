begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
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
literal|"ls-level"
argument_list|,
name|description
operator|=
literal|"list the level of loggers"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|ListLoggingLevelCommand
specifier|public
class|class
name|ListLoggingLevelCommand
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
name|required
operator|=
literal|false
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"used to match loggers"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|logs
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|Logger
argument_list|>
name|logger
init|=
name|LogManager
operator|.
name|getCurrentLoggers
argument_list|()
init|;
name|logger
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Logger
name|log
init|=
name|logger
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|log
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|logs
operator|.
name|put
argument_list|(
name|log
operator|.
name|getName
argument_list|()
argument_list|,
name|log
operator|.
name|getEffectiveLevel
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|logs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stdout
operator|.
name|println
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


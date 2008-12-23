begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
import|;
end_import

begin_comment
comment|/** Creates a command implementation based on the client input. */
end_comment

begin_class
DECL|class|GerritCommandFactory
class|class
name|GerritCommandFactory
implements|implements
name|CommandFactory
block|{
DECL|method|createCommand (final String commandLine)
specifier|public
name|Command
name|createCommand
parameter_list|(
specifier|final
name|String
name|commandLine
parameter_list|)
block|{
specifier|final
name|int
name|sp1
init|=
name|commandLine
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
name|String
name|cmd
decl_stmt|,
name|args
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|sp1
condition|)
block|{
name|cmd
operator|=
name|commandLine
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sp1
argument_list|)
expr_stmt|;
name|args
operator|=
name|commandLine
operator|.
name|substring
argument_list|(
name|sp1
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cmd
operator|=
name|commandLine
expr_stmt|;
name|args
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
literal|"git"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
operator|||
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
condition|)
block|{
name|cmd
operator|+=
literal|"-"
expr_stmt|;
specifier|final
name|int
name|sp2
init|=
name|args
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|sp2
condition|)
block|{
name|cmd
operator|+=
name|args
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sp2
argument_list|)
expr_stmt|;
name|args
operator|=
name|args
operator|.
name|substring
argument_list|(
name|sp2
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cmd
operator|+=
name|args
expr_stmt|;
name|args
operator|=
literal|""
expr_stmt|;
block|}
block|}
specifier|final
name|AbstractCommand
name|c
init|=
name|create
argument_list|(
name|cmd
argument_list|)
decl_stmt|;
name|c
operator|.
name|parseArguments
argument_list|(
name|cmd
argument_list|,
name|args
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|create (final String cmd)
specifier|private
name|AbstractCommand
name|create
parameter_list|(
specifier|final
name|String
name|cmd
parameter_list|)
block|{
if|if
condition|(
literal|"git-upload-pack"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
operator|||
literal|"gerrit-upload-pack"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
condition|)
block|{
return|return
operator|new
name|Upload
argument_list|()
return|;
block|}
if|if
condition|(
literal|"git-receive-pack"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
operator|||
literal|"gerrit-receive-pack"
operator|.
name|equals
argument_list|(
name|cmd
argument_list|)
condition|)
block|{
return|return
operator|new
name|Receive
argument_list|()
return|;
block|}
return|return
operator|new
name|AbstractCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|(
specifier|final
name|String
index|[]
name|argv
parameter_list|)
throws|throws
name|Failure
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|127
argument_list|,
literal|"gerrit: "
operator|+
name|getName
argument_list|()
operator|+
literal|": not found"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit


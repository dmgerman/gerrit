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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
operator|.
name|Command
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
name|CommandFactory
operator|.
name|ExitCallback
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
name|CommandFactory
operator|.
name|SessionAware
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
name|session
operator|.
name|ServerSession
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_class
DECL|class|BaseCommand
specifier|public
specifier|abstract
class|class
name|BaseCommand
implements|implements
name|Command
implements|,
name|SessionAware
block|{
DECL|field|in
specifier|protected
name|InputStream
name|in
decl_stmt|;
DECL|field|out
specifier|protected
name|OutputStream
name|out
decl_stmt|;
DECL|field|err
specifier|protected
name|OutputStream
name|err
decl_stmt|;
DECL|field|exit
specifier|protected
name|ExitCallback
name|exit
decl_stmt|;
DECL|field|session
specifier|protected
name|ServerSession
name|session
decl_stmt|;
comment|/** Text of the command line which lead up to invoking this instance. */
DECL|field|commandPrefix
specifier|protected
name|String
name|commandPrefix
init|=
literal|""
decl_stmt|;
comment|/** Unparsed rest of the command line. */
DECL|field|commandLine
specifier|protected
name|String
name|commandLine
decl_stmt|;
DECL|method|setInputStream (final InputStream in)
specifier|public
name|void
name|setInputStream
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
DECL|method|setOutputStream (final OutputStream out)
specifier|public
name|void
name|setOutputStream
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
DECL|method|setErrorStream (final OutputStream err)
specifier|public
name|void
name|setErrorStream
parameter_list|(
specifier|final
name|OutputStream
name|err
parameter_list|)
block|{
name|this
operator|.
name|err
operator|=
name|err
expr_stmt|;
block|}
DECL|method|setExitCallback (final ExitCallback callback)
specifier|public
name|void
name|setExitCallback
parameter_list|(
specifier|final
name|ExitCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|exit
operator|=
name|callback
expr_stmt|;
block|}
DECL|method|setSession (final ServerSession session)
specifier|public
name|void
name|setSession
parameter_list|(
specifier|final
name|ServerSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
DECL|method|setCommandPrefix (final String prefix)
specifier|public
name|void
name|setCommandPrefix
parameter_list|(
specifier|final
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|commandPrefix
operator|=
name|prefix
expr_stmt|;
block|}
comment|/**    * Set the command line to be evaluated by this command.    *<p>    * If this command is being invoked from a higher level    * {@link DispatchCommand} then only the portion after the command name (that    * is, the arguments) is supplied.    *    * @param line the command line received from the client.    */
DECL|method|setCommandLine (final String line)
specifier|public
name|void
name|setCommandLine
parameter_list|(
specifier|final
name|String
name|line
parameter_list|)
block|{
name|this
operator|.
name|commandLine
operator|=
name|line
expr_stmt|;
block|}
comment|/**    * Pass all state into the command, then run its start method.    *<p>    * This method copies all critical state, like the input and output streams,    * into the supplied command. The caller must still invoke {@code cmd.start()}    * if wants to pass control to the command.    *    * @param cmd the command that will receive the current state.    */
DECL|method|provideStateTo (final Command cmd)
specifier|protected
name|void
name|provideStateTo
parameter_list|(
specifier|final
name|Command
name|cmd
parameter_list|)
block|{
if|if
condition|(
name|cmd
operator|instanceof
name|SessionAware
condition|)
block|{
operator|(
operator|(
name|SessionAware
operator|)
name|cmd
operator|)
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
name|cmd
operator|.
name|setInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setErrorStream
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setExitCallback
argument_list|(
name|exit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|commandPrefix
operator|.
name|isEmpty
argument_list|()
condition|)
return|return
name|commandLine
return|;
else|else
return|return
name|commandPrefix
operator|+
literal|" "
operator|+
name|commandLine
return|;
block|}
block|}
end_class

end_unit


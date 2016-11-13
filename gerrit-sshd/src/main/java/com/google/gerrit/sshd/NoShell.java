begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|server
operator|.
name|ReviewDb
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
name|config
operator|.
name|CanonicalWebUrl
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
name|ssh
operator|.
name|SshInfo
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
name|SshScope
operator|.
name|Context
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
name|SchemaFactory
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
name|java
operator|.
name|io
operator|.
name|IOException
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|common
operator|.
name|Factory
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
name|Environment
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|SystemReader
import|;
end_import

begin_comment
comment|/**  * Dummy shell which prints a message and terminates.  *  *<p>This implementation is used to ensure clients who try to SSH directly to this server without  * supplying a command will get a reasonable error message, but cannot continue further.  */
end_comment

begin_class
DECL|class|NoShell
class|class
name|NoShell
implements|implements
name|Factory
argument_list|<
name|Command
argument_list|>
block|{
DECL|field|shell
specifier|private
specifier|final
name|Provider
argument_list|<
name|SendMessage
argument_list|>
name|shell
decl_stmt|;
annotation|@
name|Inject
DECL|method|NoShell (Provider<SendMessage> shell)
name|NoShell
parameter_list|(
name|Provider
argument_list|<
name|SendMessage
argument_list|>
name|shell
parameter_list|)
block|{
name|this
operator|.
name|shell
operator|=
name|shell
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|Command
name|create
parameter_list|()
block|{
return|return
name|shell
operator|.
name|get
argument_list|()
return|;
block|}
DECL|class|SendMessage
specifier|static
class|class
name|SendMessage
implements|implements
name|Command
implements|,
name|SessionAware
block|{
DECL|field|messageFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|MessageFactory
argument_list|>
name|messageFactory
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|sshScope
specifier|private
specifier|final
name|SshScope
name|sshScope
decl_stmt|;
DECL|field|in
specifier|private
name|InputStream
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|OutputStream
name|out
decl_stmt|;
DECL|field|err
specifier|private
name|OutputStream
name|err
decl_stmt|;
DECL|field|exit
specifier|private
name|ExitCallback
name|exit
decl_stmt|;
DECL|field|context
specifier|private
name|Context
name|context
decl_stmt|;
annotation|@
name|Inject
DECL|method|SendMessage ( Provider<MessageFactory> messageFactory, SchemaFactory<ReviewDb> sf, SshScope sshScope)
name|SendMessage
parameter_list|(
name|Provider
argument_list|<
name|MessageFactory
argument_list|>
name|messageFactory
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
name|SshScope
name|sshScope
parameter_list|)
block|{
name|this
operator|.
name|messageFactory
operator|=
name|messageFactory
expr_stmt|;
name|this
operator|.
name|schemaFactory
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|sshScope
operator|=
name|sshScope
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
name|SshSession
name|s
init|=
name|session
operator|.
name|getAttribute
argument_list|(
name|SshSession
operator|.
name|KEY
argument_list|)
decl_stmt|;
name|this
operator|.
name|context
operator|=
name|sshScope
operator|.
name|newContext
argument_list|(
name|schemaFactory
argument_list|,
name|s
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
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
throws|throws
name|IOException
block|{
name|Context
name|old
init|=
name|sshScope
operator|.
name|set
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|message
decl_stmt|;
try|try
block|{
name|message
operator|=
name|messageFactory
operator|.
name|get
argument_list|()
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|sshScope
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
name|err
operator|.
name|write
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|err
operator|.
name|close
argument_list|()
expr_stmt|;
name|exit
operator|.
name|onExit
argument_list|(
literal|127
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{}
block|}
DECL|class|MessageFactory
specifier|static
class|class
name|MessageFactory
block|{
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|sshInfo
specifier|private
specifier|final
name|SshInfo
name|sshInfo
decl_stmt|;
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|MessageFactory ( IdentifiedUser user, SshInfo sshInfo, @CanonicalWebUrl Provider<String> urlProvider)
name|MessageFactory
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|SshInfo
name|sshInfo
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|sshInfo
operator|=
name|sshInfo
expr_stmt|;
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
block|}
DECL|method|getMessage ()
name|String
name|getMessage
parameter_list|()
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"  ****    Welcome to Gerrit Code Review    ****\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|Account
name|account
init|=
name|user
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|account
operator|.
name|getFullName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|name
operator|=
name|user
operator|.
name|getUserName
argument_list|()
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"  Hi "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|", you have successfully connected over SSH."
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"  Unfortunately, interactive shells are disabled.\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"  To clone a hosted Git repository, use:\r\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sshInfo
operator|.
name|getHostKeys
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|host
init|=
name|sshInfo
operator|.
name|getHostKeys
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|startsWith
argument_list|(
literal|"*:"
argument_list|)
condition|)
block|{
name|host
operator|=
name|getGerritHost
argument_list|()
operator|+
name|host
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"  git clone ssh://"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|user
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"@"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"REPOSITORY_NAME.git"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getGerritHost ()
specifier|private
name|String
name|getGerritHost
parameter_list|()
block|{
name|String
name|url
init|=
name|urlProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// Ignored
block|}
block|}
return|return
name|SystemReader
operator|.
name|getInstance
argument_list|()
operator|.
name|getHostname
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit


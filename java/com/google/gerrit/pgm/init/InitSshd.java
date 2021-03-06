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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitUtil
operator|.
name|hostname
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
operator|.
name|exists
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|ConsoleUI
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitStep
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|Section
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
name|SitePaths
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
name|HostPlatform
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
name|util
operator|.
name|SocketUtil
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
name|Singleton
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
name|lang
operator|.
name|ProcessBuilder
operator|.
name|Redirect
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_comment
comment|/** Initialize the {@code sshd} configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitSshd
specifier|public
class|class
name|InitSshd
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|sshd
specifier|private
specifier|final
name|Section
name|sshd
decl_stmt|;
DECL|field|remover
specifier|private
specifier|final
name|StaleLibraryRemover
name|remover
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitSshd (ConsoleUI ui, SitePaths site, Section.Factory sections, StaleLibraryRemover remover)
name|InitSshd
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|Section
operator|.
name|Factory
name|sections
parameter_list|,
name|StaleLibraryRemover
name|remover
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|sshd
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"sshd"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|remover
operator|=
name|remover
expr_stmt|;
block|}
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
name|ui
operator|.
name|header
argument_list|(
literal|"SSH Daemon"
argument_list|)
expr_stmt|;
name|String
name|hostname
init|=
literal|"*"
decl_stmt|;
name|int
name|port
init|=
literal|29418
decl_stmt|;
name|String
name|listenAddress
init|=
name|sshd
operator|.
name|get
argument_list|(
literal|"listenAddress"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOff
argument_list|(
name|listenAddress
argument_list|)
condition|)
block|{
name|hostname
operator|=
literal|"off"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|listenAddress
operator|!=
literal|null
operator|&&
operator|!
name|listenAddress
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|InetSocketAddress
name|addr
init|=
name|SocketUtil
operator|.
name|parse
argument_list|(
name|listenAddress
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|hostname
operator|=
name|SocketUtil
operator|.
name|hostname
argument_list|(
name|addr
argument_list|)
expr_stmt|;
name|port
operator|=
name|addr
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
name|hostname
operator|=
name|ui
operator|.
name|readString
argument_list|(
name|hostname
argument_list|,
literal|"Listen on address"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isOff
argument_list|(
name|hostname
argument_list|)
condition|)
block|{
name|sshd
operator|.
name|set
argument_list|(
literal|"listenAddress"
argument_list|,
literal|"off"
argument_list|)
expr_stmt|;
return|return;
block|}
name|port
operator|=
name|ui
operator|.
name|readInt
argument_list|(
name|port
argument_list|,
literal|"Listen on port"
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|set
argument_list|(
literal|"listenAddress"
argument_list|,
name|SocketUtil
operator|.
name|format
argument_list|(
name|hostname
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|generateSshHostKeys
argument_list|()
expr_stmt|;
name|remover
operator|.
name|remove
argument_list|(
literal|"bc(pg|pkix|prov)-.*[.]jar"
argument_list|)
expr_stmt|;
block|}
DECL|method|isOff (String listenHostname)
specifier|static
name|boolean
name|isOff
parameter_list|(
name|String
name|listenHostname
parameter_list|)
block|{
return|return
literal|"off"
operator|.
name|equalsIgnoreCase
argument_list|(
name|listenHostname
argument_list|)
operator|||
literal|"none"
operator|.
name|equalsIgnoreCase
argument_list|(
name|listenHostname
argument_list|)
operator|||
literal|"no"
operator|.
name|equalsIgnoreCase
argument_list|(
name|listenHostname
argument_list|)
return|;
block|}
DECL|method|generateSshHostKeys ()
specifier|private
name|void
name|generateSshHostKeys
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_key
argument_list|)
operator|&&
operator|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_rsa
argument_list|)
operator|||
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ed25519
argument_list|)
operator|||
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_256
argument_list|)
operator|||
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_384
argument_list|)
operator|||
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_521
argument_list|)
operator|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"Generating SSH host key ..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// Generate the SSH daemon host key using ssh-keygen.
comment|//
specifier|final
name|String
name|comment
init|=
literal|"gerrit-code-review@"
operator|+
name|hostname
argument_list|()
decl_stmt|;
comment|// Workaround for JDK-6518827 - zero-length argument ignored on Win32
name|String
name|emptyPassphraseArg
init|=
name|HostPlatform
operator|.
name|isWin32
argument_list|()
condition|?
literal|"\"\""
else|:
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_rsa
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" rsa..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
operator|new
name|ProcessBuilder
argument_list|(
literal|"ssh-keygen"
argument_list|,
literal|"-q"
comment|/* quiet */
argument_list|,
literal|"-t"
argument_list|,
literal|"rsa"
argument_list|,
literal|"-N"
argument_list|,
name|emptyPassphraseArg
argument_list|,
literal|"-C"
argument_list|,
name|comment
argument_list|,
literal|"-f"
argument_list|,
name|site
operator|.
name|ssh_rsa
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|redirectError
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|redirectOutput
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|start
argument_list|()
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ed25519
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" ed25519..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|ProcessBuilder
argument_list|(
literal|"ssh-keygen"
argument_list|,
literal|"-q"
comment|/* quiet */
argument_list|,
literal|"-t"
argument_list|,
literal|"ed25519"
argument_list|,
literal|"-N"
argument_list|,
name|emptyPassphraseArg
argument_list|,
literal|"-C"
argument_list|,
name|comment
argument_list|,
literal|"-f"
argument_list|,
name|site
operator|.
name|ssh_ed25519
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|redirectError
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|redirectOutput
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|start
argument_list|()
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// continue since older hosts won't be able to generate ed25519 keys.
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" Failed to generate ed25519 key, continuing..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_256
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" ecdsa 256..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|ProcessBuilder
argument_list|(
literal|"ssh-keygen"
argument_list|,
literal|"-q"
comment|/* quiet */
argument_list|,
literal|"-t"
argument_list|,
literal|"ecdsa"
argument_list|,
literal|"-b"
argument_list|,
literal|"256"
argument_list|,
literal|"-N"
argument_list|,
name|emptyPassphraseArg
argument_list|,
literal|"-C"
argument_list|,
name|comment
argument_list|,
literal|"-f"
argument_list|,
name|site
operator|.
name|ssh_ecdsa_256
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|redirectError
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|redirectOutput
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|start
argument_list|()
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// continue since older hosts won't be able to generate ecdsa keys.
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" Failed to generate ecdsa 256 key, continuing..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_384
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" ecdsa 384..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|ProcessBuilder
argument_list|(
literal|"ssh-keygen"
argument_list|,
literal|"-q"
comment|/* quiet */
argument_list|,
literal|"-t"
argument_list|,
literal|"ecdsa"
argument_list|,
literal|"-b"
argument_list|,
literal|"384"
argument_list|,
literal|"-N"
argument_list|,
name|emptyPassphraseArg
argument_list|,
literal|"-C"
argument_list|,
name|comment
argument_list|,
literal|"-f"
argument_list|,
name|site
operator|.
name|ssh_ecdsa_384
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|redirectError
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|redirectOutput
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|start
argument_list|()
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// continue since older hosts won't be able to generate ecdsa keys.
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" Failed to generate ecdsa 384 key, continuing..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|exists
argument_list|(
name|site
operator|.
name|ssh_ecdsa_521
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" ecdsa 521..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|ProcessBuilder
argument_list|(
literal|"ssh-keygen"
argument_list|,
literal|"-q"
comment|/* quiet */
argument_list|,
literal|"-t"
argument_list|,
literal|"ecdsa"
argument_list|,
literal|"-b"
argument_list|,
literal|"521"
argument_list|,
literal|"-N"
argument_list|,
name|emptyPassphraseArg
argument_list|,
literal|"-C"
argument_list|,
name|comment
argument_list|,
literal|"-f"
argument_list|,
name|site
operator|.
name|ssh_ecdsa_521
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|redirectError
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|redirectOutput
argument_list|(
name|Redirect
operator|.
name|INHERIT
argument_list|)
operator|.
name|start
argument_list|()
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// continue since older hosts won't be able to generate ecdsa keys.
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" Failed to generate ecdsa 521 key, continuing..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|" done"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


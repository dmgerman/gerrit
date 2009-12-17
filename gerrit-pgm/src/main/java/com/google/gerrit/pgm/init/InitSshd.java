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
name|InitUtil
operator|.
name|chmod
import|;
end_import

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
name|InitUtil
operator|.
name|die
import|;
end_import

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
name|InitUtil
operator|.
name|hostname
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
name|util
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|SecurityUtils
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
name|keyprovider
operator|.
name|SimpleGeneratorHostKeyProvider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
DECL|field|libraries
specifier|private
specifier|final
name|Libraries
name|libraries
decl_stmt|;
DECL|field|sshd
specifier|private
specifier|final
name|Section
name|sshd
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitSshd (final ConsoleUI ui, final SitePaths site, final Libraries libraries, final Section.Factory sections)
name|InitSshd
parameter_list|(
specifier|final
name|ConsoleUI
name|ui
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|Libraries
name|libraries
parameter_list|,
specifier|final
name|Section
operator|.
name|Factory
name|sections
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
name|libraries
operator|=
name|libraries
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
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|site
operator|.
name|ssh_rsa
operator|.
name|exists
argument_list|()
operator|||
name|site
operator|.
name|ssh_dsa
operator|.
name|exists
argument_list|()
condition|)
block|{
name|libraries
operator|.
name|bouncyCastle
operator|.
name|downloadRequired
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|libraries
operator|.
name|bouncyCastle
operator|.
name|downloadOptional
argument_list|()
expr_stmt|;
block|}
name|generateSshHostKeys
argument_list|()
expr_stmt|;
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
name|site
operator|.
name|ssh_key
operator|.
name|exists
argument_list|()
comment|//
operator|&&
operator|!
name|site
operator|.
name|ssh_rsa
operator|.
name|exists
argument_list|()
comment|//
operator|&&
operator|!
name|site
operator|.
name|ssh_dsa
operator|.
name|exists
argument_list|()
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
if|if
condition|(
name|SecurityUtils
operator|.
name|isBouncyCastleRegistered
argument_list|()
condition|)
block|{
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
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"ssh-keygen"
block|,
comment|//
literal|"-q"
comment|/* quiet */
block|,
comment|//
literal|"-t"
block|,
literal|"rsa"
block|,
comment|//
literal|"-P"
block|,
literal|""
block|,
comment|//
literal|"-C"
block|,
name|comment
block|,
comment|//
literal|"-f"
block|,
name|site
operator|.
name|ssh_rsa
operator|.
name|getAbsolutePath
argument_list|()
comment|//
block|}
argument_list|)
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" dsa..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"ssh-keygen"
block|,
comment|//
literal|"-q"
comment|/* quiet */
block|,
comment|//
literal|"-t"
block|,
literal|"dsa"
block|,
comment|//
literal|"-P"
block|,
literal|""
block|,
comment|//
literal|"-C"
block|,
name|comment
block|,
comment|//
literal|"-f"
block|,
name|site
operator|.
name|ssh_dsa
operator|.
name|getAbsolutePath
argument_list|()
comment|//
block|}
argument_list|)
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Generate the SSH daemon host key ourselves. This is complex
comment|// because SimpleGeneratorHostKeyProvider doesn't mark the data
comment|// file as only readable by us, exposing the private key for a
comment|// short period of time. We try to reduce that risk by creating
comment|// the key within a temporary directory.
comment|//
specifier|final
name|File
name|tmpdir
init|=
operator|new
name|File
argument_list|(
name|site
operator|.
name|etc_dir
argument_list|,
literal|"tmp.sshkeygen"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmpdir
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot create directory "
operator|+
name|tmpdir
argument_list|)
throw|;
block|}
name|chmod
argument_list|(
literal|0600
argument_list|,
name|tmpdir
argument_list|)
expr_stmt|;
specifier|final
name|File
name|tmpkey
init|=
operator|new
name|File
argument_list|(
name|tmpdir
argument_list|,
name|site
operator|.
name|ssh_key
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SimpleGeneratorHostKeyProvider
name|p
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|" rsa(simple)..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
name|p
operator|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|()
expr_stmt|;
name|p
operator|.
name|setPath
argument_list|(
name|tmpkey
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setAlgorithm
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|p
operator|.
name|loadKeys
argument_list|()
expr_stmt|;
comment|// forces the key to generate.
name|chmod
argument_list|(
literal|0600
argument_list|,
name|tmpkey
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|tmpkey
operator|.
name|renameTo
argument_list|(
name|site
operator|.
name|ssh_key
argument_list|)
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot rename "
operator|+
name|tmpkey
operator|+
literal|" to "
operator|+
name|site
operator|.
name|ssh_key
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|tmpdir
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot delete "
operator|+
name|tmpdir
argument_list|)
throw|;
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


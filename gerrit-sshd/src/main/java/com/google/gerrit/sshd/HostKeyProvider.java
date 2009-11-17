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
name|server
operator|.
name|config
operator|.
name|SitePath
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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
name|KeyPairProvider
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
name|keyprovider
operator|.
name|FileKeyPairProvider
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
name|util
operator|.
name|ArrayList
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

begin_class
DECL|class|HostKeyProvider
class|class
name|HostKeyProvider
implements|implements
name|Provider
argument_list|<
name|KeyPairProvider
argument_list|>
block|{
DECL|field|sitePath
specifier|private
specifier|final
name|File
name|sitePath
decl_stmt|;
annotation|@
name|Inject
DECL|method|HostKeyProvider (@itePath final File sitePath)
name|HostKeyProvider
parameter_list|(
annotation|@
name|SitePath
specifier|final
name|File
name|sitePath
parameter_list|)
block|{
name|this
operator|.
name|sitePath
operator|=
name|sitePath
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|KeyPairProvider
name|get
parameter_list|()
block|{
specifier|final
name|File
name|etc
init|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|,
literal|"etc"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|anyKey
init|=
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"ssh_host_key"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|rsaKey
init|=
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"ssh_host_rsa_key"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|dsaKey
init|=
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"ssh_host_dsa_key"
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsaKey
operator|.
name|exists
argument_list|()
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|rsaKey
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dsaKey
operator|.
name|exists
argument_list|()
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|dsaKey
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|anyKey
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|keys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// If both formats of host key exist, we don't know which format
comment|// should be authoritative. Complain and abort.
comment|//
name|keys
operator|.
name|add
argument_list|(
name|anyKey
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Multiple host keys exist: "
operator|+
name|keys
argument_list|)
throw|;
block|}
if|if
condition|(
name|keys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No administrator created host key? Generate and save our own.
comment|//
specifier|final
name|SimpleGeneratorHostKeyProvider
name|keyp
decl_stmt|;
if|if
condition|(
operator|!
name|etc
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|etc
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot create directory "
operator|+
name|etc
argument_list|)
throw|;
block|}
name|keyp
operator|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|()
expr_stmt|;
name|keyp
operator|.
name|setPath
argument_list|(
name|anyKey
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|keyp
return|;
block|}
if|if
condition|(
operator|!
name|SecurityUtils
operator|.
name|isBouncyCastleRegistered
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Bouncy Castle Crypto not installed;"
operator|+
literal|" needed to read server host keys: "
operator|+
name|keys
operator|+
literal|""
argument_list|)
throw|;
block|}
return|return
operator|new
name|FileKeyPairProvider
argument_list|(
name|keys
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|keys
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


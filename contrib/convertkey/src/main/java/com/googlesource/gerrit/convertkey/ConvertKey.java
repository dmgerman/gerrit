begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.googlesource.gerrit.convertkey
package|package
name|com
operator|.
name|googlesource
operator|.
name|gerrit
operator|.
name|convertkey
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|HostKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
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
name|Buffer
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
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|jcajce
operator|.
name|JcaPEMWriter
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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_class
DECL|class|ConvertKey
specifier|public
class|class
name|ConvertKey
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|JSchException
throws|,
name|IOException
block|{
name|SimpleGeneratorHostKeyProvider
name|p
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: requires path to the SSH host key"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|file
operator|.
name|isFile
argument_list|()
operator|||
operator|!
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: ssh key should exist and be readable"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|p
operator|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|()
expr_stmt|;
comment|// Gerrit's SSH "simple" keys are always RSA.
name|p
operator|.
name|setPath
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|p
operator|.
name|setAlgorithm
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|Iterable
argument_list|<
name|KeyPair
argument_list|>
name|keys
init|=
name|p
operator|.
name|loadKeys
argument_list|()
decl_stmt|;
comment|// forces the key to generate.
for|for
control|(
name|KeyPair
name|k
range|:
name|keys
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Public Key ("
operator|+
name|k
operator|.
name|getPublic
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
operator|+
literal|"):"
argument_list|)
expr_stmt|;
comment|// From Gerrit's SshDaemon class; use JSch to get the public
comment|// key/type
specifier|final
name|Buffer
name|buf
init|=
operator|new
name|Buffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|putRawPublicKey
argument_list|(
name|k
operator|.
name|getPublic
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|keyBin
init|=
name|buf
operator|.
name|getCompactData
argument_list|()
decl_stmt|;
name|HostKey
name|pub
init|=
operator|new
name|HostKey
argument_list|(
literal|"localhost"
argument_list|,
name|keyBin
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pub
operator|.
name|getType
argument_list|()
operator|+
literal|" "
operator|+
name|pub
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Private Key:"
argument_list|)
expr_stmt|;
comment|// Use Bouncy Castle to write the private key back in PEM format
comment|// (PKCS#1)
comment|// http://stackoverflow.com/questions/25129822/export-rsa-public-key-to-pem-string-using-java
name|StringWriter
name|privout
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|JcaPEMWriter
name|privWriter
init|=
operator|new
name|JcaPEMWriter
argument_list|(
name|privout
argument_list|)
decl_stmt|;
name|privWriter
operator|.
name|writeObject
argument_list|(
name|k
operator|.
name|getPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|privWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|privout
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


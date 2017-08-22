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
DECL|package|com.google.gerrit.gpg.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|testutil
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
name|gpg
operator|.
name|PublicKeyStore
operator|.
name|keyIdToString
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPrivateKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPSecretKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPSecretKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcKeyFingerprintCalculator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcPBESecretKeyDecryptorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcPGPDigestCalculatorProvider
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

begin_class
DECL|class|TestKey
specifier|public
class|class
name|TestKey
block|{
DECL|field|pubArmored
specifier|private
specifier|final
name|String
name|pubArmored
decl_stmt|;
DECL|field|secArmored
specifier|private
specifier|final
name|String
name|secArmored
decl_stmt|;
DECL|field|pubRing
specifier|private
specifier|final
name|PGPPublicKeyRing
name|pubRing
decl_stmt|;
DECL|field|secRing
specifier|private
specifier|final
name|PGPSecretKeyRing
name|secRing
decl_stmt|;
DECL|method|TestKey (String pubArmored, String secArmored)
specifier|public
name|TestKey
parameter_list|(
name|String
name|pubArmored
parameter_list|,
name|String
name|secArmored
parameter_list|)
block|{
name|this
operator|.
name|pubArmored
operator|=
name|pubArmored
expr_stmt|;
name|this
operator|.
name|secArmored
operator|=
name|secArmored
expr_stmt|;
name|BcKeyFingerprintCalculator
name|fc
init|=
operator|new
name|BcKeyFingerprintCalculator
argument_list|()
decl_stmt|;
try|try
block|{
name|this
operator|.
name|pubRing
operator|=
operator|new
name|PGPPublicKeyRing
argument_list|(
name|newStream
argument_list|(
name|pubArmored
argument_list|)
argument_list|,
name|fc
argument_list|)
expr_stmt|;
name|this
operator|.
name|secRing
operator|=
operator|new
name|PGPSecretKeyRing
argument_list|(
name|newStream
argument_list|(
name|secArmored
argument_list|)
argument_list|,
name|fc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PGPException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getPublicKeyArmored ()
specifier|public
name|String
name|getPublicKeyArmored
parameter_list|()
block|{
return|return
name|pubArmored
return|;
block|}
DECL|method|getSecretKeyArmored ()
specifier|public
name|String
name|getSecretKeyArmored
parameter_list|()
block|{
return|return
name|secArmored
return|;
block|}
DECL|method|getPublicKeyRing ()
specifier|public
name|PGPPublicKeyRing
name|getPublicKeyRing
parameter_list|()
block|{
return|return
name|pubRing
return|;
block|}
DECL|method|getPublicKey ()
specifier|public
name|PGPPublicKey
name|getPublicKey
parameter_list|()
block|{
return|return
name|pubRing
operator|.
name|getPublicKey
argument_list|()
return|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|PGPSecretKey
name|getSecretKey
parameter_list|()
block|{
return|return
name|secRing
operator|.
name|getSecretKey
argument_list|()
return|;
block|}
DECL|method|getKeyId ()
specifier|public
name|long
name|getKeyId
parameter_list|()
block|{
return|return
name|getPublicKey
argument_list|()
operator|.
name|getKeyID
argument_list|()
return|;
block|}
DECL|method|getKeyIdString ()
specifier|public
name|String
name|getKeyIdString
parameter_list|()
block|{
return|return
name|keyIdToString
argument_list|(
name|getPublicKey
argument_list|()
operator|.
name|getKeyID
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getFirstUserId ()
specifier|public
name|String
name|getFirstUserId
parameter_list|()
block|{
return|return
name|getPublicKey
argument_list|()
operator|.
name|getUserIDs
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|PGPPrivateKey
name|getPrivateKey
parameter_list|()
throws|throws
name|PGPException
block|{
return|return
name|getSecretKey
argument_list|()
operator|.
name|extractPrivateKey
argument_list|(
operator|new
name|BcPBESecretKeyDecryptorBuilder
argument_list|(
operator|new
name|BcPGPDigestCalculatorProvider
argument_list|()
argument_list|)
comment|// All test keys have no passphrase.
operator|.
name|build
argument_list|(
operator|new
name|char
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newStream (String armored)
specifier|private
specifier|static
name|ArmoredInputStream
name|newStream
parameter_list|(
name|String
name|armored
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ArmoredInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|armored
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|extensions
operator|.
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
operator|.
name|BAD
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
name|extensions
operator|.
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
operator|.
name|OK
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
name|extensions
operator|.
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
operator|.
name|TRUSTED
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
name|gpg
operator|.
name|PublicKeyStore
operator|.
name|keyIdToString
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
name|gpg
operator|.
name|PublicKeyStore
operator|.
name|keyToString
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
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
name|common
operator|.
name|GpgKeyInfo
operator|.
name|Status
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
name|PGPObjectFactory
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
name|PGPPublicKeyRingCollection
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
name|PGPSignature
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
name|PGPSignatureList
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
name|bc
operator|.
name|BcPGPObjectFactory
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
name|lib
operator|.
name|Repository
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
name|transport
operator|.
name|PushCertificate
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
name|transport
operator|.
name|PushCertificate
operator|.
name|NonceStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_comment
comment|/** Checker for push certificates. */
end_comment

begin_class
DECL|class|PushCertificateChecker
specifier|public
specifier|abstract
class|class
name|PushCertificateChecker
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PushCertificateChecker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|publicKeyChecker
specifier|private
specifier|final
name|PublicKeyChecker
name|publicKeyChecker
decl_stmt|;
DECL|method|PushCertificateChecker (PublicKeyChecker publicKeyChecker)
specifier|protected
name|PushCertificateChecker
parameter_list|(
name|PublicKeyChecker
name|publicKeyChecker
parameter_list|)
block|{
name|this
operator|.
name|publicKeyChecker
operator|=
name|publicKeyChecker
expr_stmt|;
block|}
comment|/**    * Check a push certificate.    *    * @return result of the check.    */
DECL|method|check (PushCertificate cert)
specifier|public
specifier|final
name|CheckResult
name|check
parameter_list|(
name|PushCertificate
name|cert
parameter_list|)
block|{
if|if
condition|(
name|cert
operator|.
name|getNonceStatus
argument_list|()
operator|!=
name|NonceStatus
operator|.
name|OK
condition|)
block|{
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
literal|"Invalid nonce"
argument_list|)
return|;
block|}
name|List
argument_list|<
name|CheckResult
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|CheckResult
name|sigResult
init|=
literal|null
decl_stmt|;
try|try
block|{
name|PGPSignature
name|sig
init|=
name|readSignature
argument_list|(
name|cert
argument_list|)
decl_stmt|;
if|if
condition|(
name|sig
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
name|Repository
name|repo
init|=
name|getRepository
argument_list|()
decl_stmt|;
try|try
init|(
name|PublicKeyStore
name|store
init|=
operator|new
name|PublicKeyStore
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|sigResult
operator|=
name|checkSignature
argument_list|(
name|sig
argument_list|,
name|cert
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
name|checkCustom
argument_list|(
name|repo
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|shouldClose
argument_list|(
name|repo
argument_list|)
condition|)
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|results
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|bad
argument_list|(
literal|"Invalid signature format"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PGPException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Internal error checking push certificate"
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|bad
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|combine
argument_list|(
name|sigResult
argument_list|,
name|results
argument_list|)
return|;
block|}
DECL|method|combine (CheckResult sigResult, List<CheckResult> results)
specifier|private
specifier|static
name|CheckResult
name|combine
parameter_list|(
name|CheckResult
name|sigResult
parameter_list|,
name|List
argument_list|<
name|CheckResult
argument_list|>
name|results
parameter_list|)
block|{
comment|// Combine results:
comment|//  - If any input result is BAD, the final result is bad.
comment|//  - If sigResult is TRUSTED and no other result is BAD, the final result
comment|//    is TRUSTED.
comment|//  - Otherwise, the result is OK.
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|bad
init|=
literal|false
decl_stmt|;
for|for
control|(
name|CheckResult
name|result
range|:
name|results
control|)
block|{
name|problems
operator|.
name|addAll
argument_list|(
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
name|bad
operator||=
name|result
operator|.
name|getStatus
argument_list|()
operator|==
name|BAD
expr_stmt|;
block|}
name|Status
name|status
init|=
name|bad
condition|?
name|BAD
else|:
name|OK
decl_stmt|;
if|if
condition|(
name|sigResult
operator|!=
literal|null
condition|)
block|{
name|problems
operator|.
name|addAll
argument_list|(
name|sigResult
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sigResult
operator|.
name|getStatus
argument_list|()
operator|==
name|BAD
condition|)
block|{
name|status
operator|=
name|BAD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|bad
operator|&&
name|sigResult
operator|.
name|getStatus
argument_list|()
operator|==
name|TRUSTED
condition|)
block|{
name|status
operator|=
name|TRUSTED
expr_stmt|;
block|}
block|}
return|return
name|CheckResult
operator|.
name|create
argument_list|(
name|status
argument_list|,
name|problems
argument_list|)
return|;
block|}
comment|/**    * Get the repository that this checker should operate on.    *<p>    * This method is called once per call to {@link #check(PushCertificate)}.    *    * @return the repository.    * @throws IOException if an error occurred reading the repository.    */
DECL|method|getRepository ()
specifier|protected
specifier|abstract
name|Repository
name|getRepository
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * @param repo a repository previously returned by {@link #getRepository()}.    * @return whether this repository should be closed before returning from    *     {@link #check(PushCertificate)}.    */
DECL|method|shouldClose (Repository repo)
specifier|protected
specifier|abstract
name|boolean
name|shouldClose
parameter_list|(
name|Repository
name|repo
parameter_list|)
function_decl|;
comment|/**    * Perform custom checks.    *<p>    * Default implementation reports no problems, but may be overridden by    * subclasses.    *    * @param repo a repository previously returned by {@link #getRepository()}.    * @return the result of the custom check.    */
DECL|method|checkCustom (Repository repo)
specifier|protected
name|CheckResult
name|checkCustom
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
return|return
name|CheckResult
operator|.
name|ok
argument_list|()
return|;
block|}
DECL|method|readSignature (PushCertificate cert)
specifier|private
name|PGPSignature
name|readSignature
parameter_list|(
name|PushCertificate
name|cert
parameter_list|)
throws|throws
name|IOException
block|{
name|ArmoredInputStream
name|in
init|=
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
name|cert
operator|.
name|getSignature
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|PGPObjectFactory
name|factory
init|=
operator|new
name|BcPGPObjectFactory
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Object
name|obj
decl_stmt|;
while|while
condition|(
operator|(
name|obj
operator|=
name|factory
operator|.
name|nextObject
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|PGPSignatureList
condition|)
block|{
name|PGPSignatureList
name|sigs
init|=
operator|(
name|PGPSignatureList
operator|)
name|obj
decl_stmt|;
if|if
condition|(
operator|!
name|sigs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|sigs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|checkSignature (PGPSignature sig, PushCertificate cert, PublicKeyStore store)
specifier|private
name|CheckResult
name|checkSignature
parameter_list|(
name|PGPSignature
name|sig
parameter_list|,
name|PushCertificate
name|cert
parameter_list|,
name|PublicKeyStore
name|store
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
name|PGPPublicKeyRingCollection
name|keys
init|=
name|store
operator|.
name|get
argument_list|(
name|sig
operator|.
name|getKeyID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|keys
operator|.
name|getKeyRings
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
literal|"No public keys found for key ID "
operator|+
name|keyIdToString
argument_list|(
name|sig
operator|.
name|getKeyID
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
name|PGPPublicKey
name|signer
init|=
name|PublicKeyStore
operator|.
name|getSigner
argument_list|(
name|keys
argument_list|,
name|sig
argument_list|,
name|Constants
operator|.
name|encode
argument_list|(
name|cert
operator|.
name|toText
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|signer
operator|==
literal|null
condition|)
block|{
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
literal|"Signature by "
operator|+
name|keyIdToString
argument_list|(
name|sig
operator|.
name|getKeyID
argument_list|()
argument_list|)
operator|+
literal|" is not valid"
argument_list|)
return|;
block|}
name|CheckResult
name|result
init|=
name|publicKeyChecker
operator|.
name|check
argument_list|(
name|signer
argument_list|,
name|store
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|getProblems
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuilder
name|err
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Invalid public key "
argument_list|)
operator|.
name|append
argument_list|(
name|keyToString
argument_list|(
name|signer
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":\n  "
argument_list|)
operator|.
name|append
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|"\n  "
argument_list|)
operator|.
name|join
argument_list|(
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|CheckResult
operator|.
name|create
argument_list|(
name|result
operator|.
name|getStatus
argument_list|()
argument_list|,
name|err
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit


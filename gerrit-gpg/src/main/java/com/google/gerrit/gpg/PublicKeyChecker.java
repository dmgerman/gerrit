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
name|SignatureSubpacket
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
name|SignatureSubpacketTags
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_comment
comment|/** Checker for GPG public keys for use in a push certificate. */
end_comment

begin_class
DECL|class|PublicKeyChecker
specifier|public
class|class
name|PublicKeyChecker
block|{
comment|// https://tools.ietf.org/html/rfc4880#section-5.2.3.13
DECL|field|COMPLETE_TRUST
specifier|private
specifier|static
specifier|final
name|int
name|COMPLETE_TRUST
init|=
literal|120
decl_stmt|;
DECL|field|trusted
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|Fingerprint
argument_list|>
name|trusted
decl_stmt|;
DECL|field|maxTrustDepth
specifier|private
specifier|final
name|int
name|maxTrustDepth
decl_stmt|;
comment|/** Create a new checker that does not check the web of trust. */
DECL|method|PublicKeyChecker ()
specifier|public
name|PublicKeyChecker
parameter_list|()
block|{
name|this
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * @param maxTrustDepth maximum depth to search while looking for a trusted    *     key.    * @param trusted ultimately trusted key fingerprints; may not be empty. If    *     null, disable web-of-trust checks.    */
DECL|method|PublicKeyChecker (int maxTrustDepth, Collection<Fingerprint> trusted)
specifier|public
name|PublicKeyChecker
parameter_list|(
name|int
name|maxTrustDepth
parameter_list|,
name|Collection
argument_list|<
name|Fingerprint
argument_list|>
name|trusted
parameter_list|)
block|{
if|if
condition|(
name|trusted
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|maxTrustDepth
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"maxTrustDepth must be positive, got: "
operator|+
name|maxTrustDepth
argument_list|)
throw|;
block|}
if|if
condition|(
name|trusted
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"at least one trusted key required"
argument_list|)
throw|;
block|}
name|this
operator|.
name|trusted
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Fingerprint
name|fp
range|:
name|trusted
control|)
block|{
name|this
operator|.
name|trusted
operator|.
name|put
argument_list|(
name|fp
operator|.
name|getId
argument_list|()
argument_list|,
name|fp
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|trusted
operator|=
literal|null
expr_stmt|;
block|}
name|this
operator|.
name|maxTrustDepth
operator|=
name|maxTrustDepth
expr_stmt|;
block|}
comment|/**    * Check a public key, including its web of trust.    *    * @param key the public key.    * @param store a store to read public keys from for trust checks. If this    *     store is not configured for web-of-trust checks, this argument is    *     ignored.    * @return the result of the check.    */
DECL|method|check (PGPPublicKey key, PublicKeyStore store)
specifier|public
specifier|final
name|CheckResult
name|check
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|PublicKeyStore
name|store
parameter_list|)
block|{
if|if
condition|(
name|trusted
operator|==
literal|null
condition|)
block|{
return|return
name|check
argument_list|(
name|key
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|store
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"PublicKeyStore required for web of trust checks"
argument_list|)
throw|;
block|}
return|return
name|check
argument_list|(
name|key
argument_list|,
name|store
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|,
operator|new
name|HashSet
argument_list|<
name|Fingerprint
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Check only a public key, not including its web of trust.    *    * @param key the public key.    * @return the result of the check.    */
DECL|method|check (PGPPublicKey key)
specifier|public
specifier|final
name|CheckResult
name|check
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|)
block|{
return|return
name|check
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * Perform custom checks.    *<p>    * Default implementation reports no problems, but may be overridden by    * subclasses.    *    * @param key the public key.    * @param depth the depth from the initial key passed to {@link #check(    *     PGPPublicKey, PublicKeyStore)}: 0 if this was the initial key, up to a    *     maximum of {@code maxTrustDepth}.    * @return the result of the custom check.    */
DECL|method|checkCustom (PGPPublicKey key, int depth)
specifier|public
name|CheckResult
name|checkCustom
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
return|return
name|CheckResult
operator|.
name|ok
argument_list|()
return|;
block|}
DECL|method|check (PGPPublicKey key, PublicKeyStore store, int depth, boolean expand, Set<Fingerprint> seen)
specifier|private
name|CheckResult
name|check
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|PublicKeyStore
name|store
parameter_list|,
name|int
name|depth
parameter_list|,
name|boolean
name|expand
parameter_list|,
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|seen
parameter_list|)
block|{
name|CheckResult
name|basicResult
init|=
name|checkBasic
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|CheckResult
name|customResult
init|=
name|checkCustom
argument_list|(
name|key
argument_list|,
name|depth
argument_list|)
decl_stmt|;
name|CheckResult
name|trustResult
init|=
name|checkWebOfTrust
argument_list|(
name|key
argument_list|,
name|store
argument_list|,
name|depth
argument_list|,
name|seen
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expand
operator|&&
operator|!
name|trustResult
operator|.
name|isTrusted
argument_list|()
condition|)
block|{
name|trustResult
operator|=
name|CheckResult
operator|.
name|create
argument_list|(
name|trustResult
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|"Key is not trusted"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|basicResult
operator|.
name|getProblems
argument_list|()
operator|.
name|size
argument_list|()
operator|+
name|customResult
operator|.
name|getProblems
argument_list|()
operator|.
name|size
argument_list|()
operator|+
name|trustResult
operator|.
name|getProblems
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|problems
operator|.
name|addAll
argument_list|(
name|basicResult
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
name|problems
operator|.
name|addAll
argument_list|(
name|customResult
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
name|problems
operator|.
name|addAll
argument_list|(
name|trustResult
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
name|Status
name|status
decl_stmt|;
if|if
condition|(
name|basicResult
operator|.
name|getStatus
argument_list|()
operator|==
name|BAD
operator|||
name|customResult
operator|.
name|getStatus
argument_list|()
operator|==
name|BAD
operator|||
name|trustResult
operator|.
name|getStatus
argument_list|()
operator|==
name|BAD
condition|)
block|{
comment|// Any BAD result and the final result is BAD.
name|status
operator|=
name|BAD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|trustResult
operator|.
name|getStatus
argument_list|()
operator|==
name|TRUSTED
condition|)
block|{
comment|// basicResult is BAD or OK, whereas trustResult is BAD or TRUSTED. If
comment|// TRUSTED, we trust the final result.
name|status
operator|=
name|TRUSTED
expr_stmt|;
block|}
else|else
block|{
comment|// All results were OK or better, but trustResult was not TRUSTED. Don't
comment|// let subclasses bypass checkWebOfTrust by returning TRUSTED; just return
comment|// OK here.
name|status
operator|=
name|OK
expr_stmt|;
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
DECL|method|checkBasic (PGPPublicKey key)
specifier|private
name|CheckResult
name|checkBasic
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|isRevoked
argument_list|()
condition|)
block|{
comment|// TODO(dborowitz): isRevoked is overeager:
comment|// http://www.bouncycastle.org/jira/browse/BJB-45
name|problems
operator|.
name|add
argument_list|(
literal|"Key is revoked"
argument_list|)
expr_stmt|;
block|}
name|long
name|validSecs
init|=
name|key
operator|.
name|getValidSeconds
argument_list|()
decl_stmt|;
if|if
condition|(
name|validSecs
operator|!=
literal|0
condition|)
block|{
name|long
name|createdSecs
init|=
name|key
operator|.
name|getCreationTime
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
decl_stmt|;
name|long
name|nowSecs
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
decl_stmt|;
if|if
condition|(
name|nowSecs
operator|-
name|createdSecs
operator|>
name|validSecs
condition|)
block|{
name|problems
operator|.
name|add
argument_list|(
literal|"Key is expired"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|CheckResult
operator|.
name|create
argument_list|(
name|problems
argument_list|)
return|;
block|}
DECL|method|checkWebOfTrust (PGPPublicKey key, PublicKeyStore store, int depth, Set<Fingerprint> seen)
specifier|private
name|CheckResult
name|checkWebOfTrust
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|PublicKeyStore
name|store
parameter_list|,
name|int
name|depth
parameter_list|,
name|Set
argument_list|<
name|Fingerprint
argument_list|>
name|seen
parameter_list|)
block|{
if|if
condition|(
name|trusted
operator|==
literal|null
operator|||
name|store
operator|==
literal|null
condition|)
block|{
comment|// Trust checking not configured, server trusts all OK keys.
return|return
name|CheckResult
operator|.
name|trusted
argument_list|()
return|;
block|}
name|Fingerprint
name|fp
init|=
operator|new
name|Fingerprint
argument_list|(
name|key
operator|.
name|getFingerprint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|seen
operator|.
name|contains
argument_list|(
name|fp
argument_list|)
condition|)
block|{
return|return
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"Key is trusted in a cycle"
argument_list|)
return|;
block|}
name|seen
operator|.
name|add
argument_list|(
name|fp
argument_list|)
expr_stmt|;
name|Fingerprint
name|trustedFp
init|=
name|trusted
operator|.
name|get
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|trustedFp
operator|!=
literal|null
operator|&&
name|trustedFp
operator|.
name|equals
argument_list|(
name|fp
argument_list|)
condition|)
block|{
return|return
name|CheckResult
operator|.
name|trusted
argument_list|()
return|;
comment|// Directly trusted.
block|}
elseif|else
if|if
condition|(
name|depth
operator|>=
name|maxTrustDepth
condition|)
block|{
return|return
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"No path of depth<= "
operator|+
name|maxTrustDepth
operator|+
literal|" to a trusted key"
argument_list|)
return|;
block|}
name|List
argument_list|<
name|CheckResult
argument_list|>
name|signerResults
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|String
argument_list|>
name|userIds
init|=
name|key
operator|.
name|getUserIDs
argument_list|()
decl_stmt|;
while|while
condition|(
name|userIds
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|userId
init|=
name|userIds
operator|.
name|next
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|PGPSignature
argument_list|>
name|sigs
init|=
name|key
operator|.
name|getSignaturesForID
argument_list|(
name|userId
argument_list|)
decl_stmt|;
while|while
condition|(
name|sigs
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PGPSignature
name|sig
init|=
name|sigs
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// TODO(dborowitz): Handle CERTIFICATION_REVOCATION.
if|if
condition|(
name|sig
operator|.
name|getSignatureType
argument_list|()
operator|!=
name|PGPSignature
operator|.
name|DEFAULT_CERTIFICATION
operator|&&
name|sig
operator|.
name|getSignatureType
argument_list|()
operator|!=
name|PGPSignature
operator|.
name|POSITIVE_CERTIFICATION
condition|)
block|{
continue|continue;
comment|// Not a certification.
block|}
name|PGPPublicKey
name|signer
init|=
name|getSigner
argument_list|(
name|store
argument_list|,
name|sig
argument_list|,
name|userId
argument_list|,
name|key
argument_list|,
name|signerResults
argument_list|)
decl_stmt|;
comment|// TODO(dborowitz): Require self certification.
if|if
condition|(
name|signer
operator|==
literal|null
operator|||
name|Arrays
operator|.
name|equals
argument_list|(
name|signer
operator|.
name|getFingerprint
argument_list|()
argument_list|,
name|key
operator|.
name|getFingerprint
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|subpacketProblem
init|=
name|checkTrustSubpacket
argument_list|(
name|sig
argument_list|,
name|depth
argument_list|)
decl_stmt|;
if|if
condition|(
name|subpacketProblem
operator|==
literal|null
condition|)
block|{
name|CheckResult
name|signerResult
init|=
name|check
argument_list|(
name|signer
argument_list|,
name|store
argument_list|,
name|depth
operator|+
literal|1
argument_list|,
literal|false
argument_list|,
name|seen
argument_list|)
decl_stmt|;
if|if
condition|(
name|signerResult
operator|.
name|isTrusted
argument_list|()
condition|)
block|{
return|return
name|CheckResult
operator|.
name|trusted
argument_list|()
return|;
block|}
block|}
name|signerResults
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"Certification by "
operator|+
name|keyToString
argument_list|(
name|signer
argument_list|)
operator|+
literal|" is valid, but key is not trusted"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|problems
operator|.
name|add
argument_list|(
literal|"No path to a trusted key"
argument_list|)
expr_stmt|;
for|for
control|(
name|CheckResult
name|signerResult
range|:
name|signerResults
control|)
block|{
name|problems
operator|.
name|addAll
argument_list|(
name|signerResult
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|CheckResult
operator|.
name|create
argument_list|(
name|OK
argument_list|,
name|problems
argument_list|)
return|;
block|}
DECL|method|getSigner (PublicKeyStore store, PGPSignature sig, String userId, PGPPublicKey key, List<CheckResult> results)
specifier|private
specifier|static
name|PGPPublicKey
name|getSigner
parameter_list|(
name|PublicKeyStore
name|store
parameter_list|,
name|PGPSignature
name|sig
parameter_list|,
name|String
name|userId
parameter_list|,
name|PGPPublicKey
name|key
parameter_list|,
name|List
argument_list|<
name|CheckResult
argument_list|>
name|results
parameter_list|)
block|{
try|try
block|{
name|PGPPublicKeyRingCollection
name|signers
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
name|signers
operator|.
name|getKeyRings
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"Key "
operator|+
name|keyIdToString
argument_list|(
name|sig
operator|.
name|getKeyID
argument_list|()
argument_list|)
operator|+
literal|" used for certification is not in store"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|PGPPublicKey
name|signer
init|=
name|PublicKeyStore
operator|.
name|getSigner
argument_list|(
name|signers
argument_list|,
name|sig
argument_list|,
name|userId
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|signer
operator|==
literal|null
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"Certification by "
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
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|signer
return|;
block|}
catch|catch
parameter_list|(
name|PGPException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|results
operator|.
name|add
argument_list|(
name|CheckResult
operator|.
name|ok
argument_list|(
literal|"Error checking certification by "
operator|+
name|keyIdToString
argument_list|(
name|sig
operator|.
name|getKeyID
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|checkTrustSubpacket (PGPSignature sig, int depth)
specifier|private
name|String
name|checkTrustSubpacket
parameter_list|(
name|PGPSignature
name|sig
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|SignatureSubpacket
name|trustSub
init|=
name|sig
operator|.
name|getHashedSubPackets
argument_list|()
operator|.
name|getSubpacket
argument_list|(
name|SignatureSubpacketTags
operator|.
name|TRUST_SIG
argument_list|)
decl_stmt|;
if|if
condition|(
name|trustSub
operator|==
literal|null
operator|||
name|trustSub
operator|.
name|getData
argument_list|()
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
return|return
literal|"Certification is missing trust information"
return|;
block|}
name|byte
name|amount
init|=
name|trustSub
operator|.
name|getData
argument_list|()
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|amount
operator|<
name|COMPLETE_TRUST
condition|)
block|{
return|return
literal|"Certification does not fully trust key"
return|;
block|}
name|byte
name|level
init|=
name|trustSub
operator|.
name|getData
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|int
name|required
init|=
name|depth
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|level
operator|<
name|required
condition|)
block|{
return|return
literal|"Certification trusts to depth "
operator|+
name|level
operator|+
literal|", but depth "
operator|+
name|required
operator|+
literal|" is required"
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit


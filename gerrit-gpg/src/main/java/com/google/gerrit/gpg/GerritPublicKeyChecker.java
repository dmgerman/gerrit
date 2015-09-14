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
name|reviewdb
operator|.
name|client
operator|.
name|AccountExternalId
operator|.
name|SCHEME_GPGKEY
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
name|CharMatcher
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
name|MoreObjects
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
name|collect
operator|.
name|FluentIterable
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
name|collect
operator|.
name|Ordering
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
name|io
operator|.
name|BaseEncoding
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
name|common
operator|.
name|PageLinks
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
name|client
operator|.
name|AccountExternalId
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
name|config
operator|.
name|GerritServerConfig
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
name|Singleton
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
name|operator
operator|.
name|bc
operator|.
name|BcPGPContentVerifierBuilderProvider
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
name|Config
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
name|PushCertificateIdent
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
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Checker for GPG public keys including Gerrit-specific checks.  *<p>  * For Gerrit, keys must contain a self-signed user ID certification matching a  * trusted external ID in the database, or an email address thereof.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GerritPublicKeyChecker
specifier|public
class|class
name|GerritPublicKeyChecker
extends|extends
name|PublicKeyChecker
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
name|GerritPublicKeyChecker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|webUrl
specifier|private
specifier|final
name|String
name|webUrl
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|method|getTrustedFingerprints (Config cfg)
specifier|private
specifier|static
name|List
argument_list|<
name|Fingerprint
argument_list|>
name|getTrustedFingerprints
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|String
index|[]
name|strs
init|=
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"trustedKey"
argument_list|)
decl_stmt|;
if|if
condition|(
name|strs
operator|==
literal|null
operator|||
name|strs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Fingerprint
argument_list|>
name|fps
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|strs
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|str
range|:
name|strs
control|)
block|{
name|str
operator|=
name|CharMatcher
operator|.
name|WHITESPACE
operator|.
name|removeFrom
argument_list|(
name|str
argument_list|)
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
name|fps
operator|.
name|add
argument_list|(
operator|new
name|Fingerprint
argument_list|(
name|BaseEncoding
operator|.
name|base16
argument_list|()
operator|.
name|decode
argument_list|(
name|str
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|fps
return|;
block|}
annotation|@
name|Inject
DECL|method|GerritPublicKeyChecker ( @erritServerConfig Config cfg, @CanonicalWebUrl String webUrl, Provider<IdentifiedUser> userProvider)
name|GerritPublicKeyChecker
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|String
name|webUrl
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|userProvider
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
operator|.
name|getInt
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"maxTrustDepth"
argument_list|,
literal|0
argument_list|)
argument_list|,
name|getTrustedFingerprints
argument_list|(
name|cfg
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|webUrl
operator|=
name|webUrl
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|checkCustom (PGPPublicKey key)
specifier|public
name|CheckResult
name|checkCustom
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|)
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|allowedUserIds
init|=
name|getAllowedUserIds
argument_list|()
decl_stmt|;
if|if
condition|(
name|allowedUserIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
literal|"No identities found for user; check "
operator|+
name|webUrl
operator|+
literal|"#"
operator|+
name|PageLinks
operator|.
name|SETTINGS_WEBIDENT
argument_list|)
return|;
block|}
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
if|if
condition|(
name|isAllowed
argument_list|(
name|userId
argument_list|,
name|allowedUserIds
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|PGPSignature
argument_list|>
name|sigs
init|=
name|getSignaturesForId
argument_list|(
name|key
argument_list|,
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
if|if
condition|(
name|isValidCertification
argument_list|(
name|key
argument_list|,
name|sigs
operator|.
name|next
argument_list|()
argument_list|,
name|userId
argument_list|)
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
block|}
block|}
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
name|missingUserIds
argument_list|(
name|allowedUserIds
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PGPException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error checking user IDs for key"
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|msg
operator|+
literal|" "
operator|+
name|keyIdToString
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|CheckResult
operator|.
name|bad
argument_list|(
name|msg
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getSignaturesForId (PGPPublicKey key, String userId)
specifier|private
name|Iterator
argument_list|<
name|PGPSignature
argument_list|>
name|getSignaturesForId
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|String
name|userId
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|key
operator|.
name|getSignaturesForID
argument_list|(
name|userId
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyIterator
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getAllowedUserIds ()
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getAllowedUserIds
parameter_list|()
block|{
name|IdentifiedUser
name|user
init|=
name|userProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|user
operator|.
name|getEmailAddresses
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountExternalId
name|extId
range|:
name|user
operator|.
name|state
argument_list|()
operator|.
name|getExternalIds
argument_list|()
control|)
block|{
if|if
condition|(
name|extId
operator|.
name|isScheme
argument_list|(
name|SCHEME_GPGKEY
argument_list|)
condition|)
block|{
continue|continue;
comment|// Omit GPG keys.
block|}
name|result
operator|.
name|add
argument_list|(
name|extId
operator|.
name|getExternalId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|isAllowed (String userId, Set<String> allowedUserIds)
specifier|private
specifier|static
name|boolean
name|isAllowed
parameter_list|(
name|String
name|userId
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|allowedUserIds
parameter_list|)
block|{
return|return
name|allowedUserIds
operator|.
name|contains
argument_list|(
name|userId
argument_list|)
operator|||
name|allowedUserIds
operator|.
name|contains
argument_list|(
name|PushCertificateIdent
operator|.
name|parse
argument_list|(
name|userId
argument_list|)
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isValidCertification (PGPPublicKey key, PGPSignature sig, String userId)
specifier|private
specifier|static
name|boolean
name|isValidCertification
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|PGPSignature
name|sig
parameter_list|,
name|String
name|userId
parameter_list|)
throws|throws
name|PGPException
block|{
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
return|return
literal|false
return|;
block|}
if|if
condition|(
name|sig
operator|.
name|getKeyID
argument_list|()
operator|!=
name|key
operator|.
name|getKeyID
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// TODO(dborowitz): Handle certification revocations:
comment|// - Is there a revocation by either this key or another key trusted by the
comment|//   server?
comment|// - Does such a revocation postdate all other valid certifications?
name|sig
operator|.
name|init
argument_list|(
operator|new
name|BcPGPContentVerifierBuilderProvider
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|sig
operator|.
name|verifyCertification
argument_list|(
name|userId
argument_list|,
name|key
argument_list|)
return|;
block|}
DECL|method|missingUserIds (Set<String> allowedUserIds)
specifier|private
specifier|static
name|String
name|missingUserIds
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|allowedUserIds
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Key must contain a valid"
operator|+
literal|" certification for one of the following identities:\n"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|sorted
init|=
name|FluentIterable
operator|.
name|from
argument_list|(
name|allowedUserIds
argument_list|)
operator|.
name|toSortedList
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|sorted
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"  "
argument_list|)
operator|.
name|append
argument_list|(
name|sorted
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sorted
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


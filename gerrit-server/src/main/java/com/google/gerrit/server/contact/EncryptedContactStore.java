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
DECL|package|com.google.gerrit.server.contact
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|contact
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
name|common
operator|.
name|TimeUtil
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
name|errors
operator|.
name|ContactInformationStoreException
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
name|reviewdb
operator|.
name|client
operator|.
name|ContactInformation
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
name|UrlEncoded
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
name|OrmException
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
name|ProvisionException
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
name|bcpg
operator|.
name|ArmoredOutputStream
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
name|PGPCompressedData
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
name|PGPCompressedDataGenerator
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
name|PGPEncryptedData
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
name|PGPEncryptedDataGenerator
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
name|PGPLiteralData
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
name|PGPLiteralDataGenerator
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
name|PGPUtil
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
name|BcPGPDataEncryptorBuilder
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
name|BcPublicKeyKeyEncryptionMethodGenerator
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
name|ByteArrayOutputStream
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
name|FileInputStream
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchProviderException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|TimeZone
import|;
end_import

begin_comment
comment|/** Encrypts {@link ContactInformation} instances and saves them. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|EncryptedContactStore
class|class
name|EncryptedContactStore
implements|implements
name|ContactStore
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
name|EncryptedContactStore
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|UTC
specifier|private
specifier|static
specifier|final
name|TimeZone
name|UTC
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|dest
specifier|private
specifier|final
name|PGPPublicKey
name|dest
decl_stmt|;
DECL|field|prng
specifier|private
specifier|final
name|SecureRandom
name|prng
decl_stmt|;
DECL|field|storeUrl
specifier|private
specifier|final
name|URL
name|storeUrl
decl_stmt|;
DECL|field|storeAPPSEC
specifier|private
specifier|final
name|String
name|storeAPPSEC
decl_stmt|;
DECL|field|connFactory
specifier|private
specifier|final
name|ContactStoreConnection
operator|.
name|Factory
name|connFactory
decl_stmt|;
DECL|method|EncryptedContactStore (final URL storeUrl, final String storeAPPSEC, final File pubKey, final SchemaFactory<ReviewDb> schema, final ContactStoreConnection.Factory connFactory)
name|EncryptedContactStore
parameter_list|(
specifier|final
name|URL
name|storeUrl
parameter_list|,
specifier|final
name|String
name|storeAPPSEC
parameter_list|,
specifier|final
name|File
name|pubKey
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|ContactStoreConnection
operator|.
name|Factory
name|connFactory
parameter_list|)
block|{
name|this
operator|.
name|storeUrl
operator|=
name|storeUrl
expr_stmt|;
name|this
operator|.
name|storeAPPSEC
operator|=
name|storeAPPSEC
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|dest
operator|=
name|selectKey
argument_list|(
name|readPubRing
argument_list|(
name|pubKey
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|connFactory
operator|=
name|connFactory
expr_stmt|;
specifier|final
name|String
name|prngName
init|=
literal|"SHA1PRNG"
decl_stmt|;
try|try
block|{
name|prng
operator|=
name|SecureRandom
operator|.
name|getInstance
argument_list|(
name|prngName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot create "
operator|+
name|prngName
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Run a test encryption to verify the proper algorithms exist in
comment|// our JVM and we are able to invoke them. This helps to identify
comment|// a system configuration problem early at startup, rather than a
comment|// lot later during runtime.
comment|//
try|try
block|{
name|encrypt
argument_list|(
literal|"test"
argument_list|,
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"test"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"PGP encryption not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PGPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"PGP encryption not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"PGP encryption not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|readPubRing (final File pub)
specifier|private
specifier|static
name|PGPPublicKeyRingCollection
name|readPubRing
parameter_list|(
specifier|final
name|File
name|pub
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|pub
argument_list|)
init|;
name|InputStream
name|in
operator|=
name|PGPUtil
operator|.
name|getDecoderStream
argument_list|(
name|fin
argument_list|)
init|)
block|{
return|return
operator|new
name|PGPPublicKeyRingCollection
argument_list|(
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot read "
operator|+
name|pub
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PGPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot read "
operator|+
name|pub
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|selectKey (final PGPPublicKeyRingCollection rings)
specifier|private
specifier|static
name|PGPPublicKey
name|selectKey
parameter_list|(
specifier|final
name|PGPPublicKeyRingCollection
name|rings
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|ri
init|=
name|rings
operator|.
name|getKeyRings
argument_list|()
init|;
name|ri
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|PGPPublicKeyRing
name|currRing
init|=
operator|(
name|PGPPublicKeyRing
operator|)
name|ri
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|ki
init|=
name|currRing
operator|.
name|getPublicKeys
argument_list|()
init|;
name|ki
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|PGPPublicKey
name|k
init|=
operator|(
name|PGPPublicKey
operator|)
name|ki
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|isEncryptionKey
argument_list|()
condition|)
block|{
return|return
name|k
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|store (final Account account, final ContactInformation info)
specifier|public
name|void
name|store
parameter_list|(
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|ContactInformation
name|info
parameter_list|)
throws|throws
name|ContactInformationStoreException
block|{
try|try
block|{
specifier|final
name|byte
index|[]
name|plainText
init|=
name|format
argument_list|(
name|account
argument_list|,
name|info
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|fileName
init|=
literal|"account-"
operator|+
name|account
operator|.
name|getId
argument_list|()
decl_stmt|;
specifier|final
name|Date
name|fileDate
init|=
name|account
operator|.
name|getContactFiledOn
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|encText
init|=
name|encrypt
argument_list|(
name|fileName
argument_list|,
name|fileDate
argument_list|,
name|plainText
argument_list|)
decl_stmt|;
specifier|final
name|String
name|encStr
init|=
operator|new
name|String
argument_list|(
name|encText
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
specifier|final
name|Timestamp
name|filedOn
init|=
name|account
operator|.
name|getContactFiledOn
argument_list|()
decl_stmt|;
specifier|final
name|UrlEncoded
name|u
init|=
operator|new
name|UrlEncoded
argument_list|()
decl_stmt|;
if|if
condition|(
name|storeAPPSEC
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|put
argument_list|(
literal|"APPSEC"
argument_list|,
name|storeAPPSEC
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|put
argument_list|(
literal|"email"
argument_list|,
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|filedOn
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|put
argument_list|(
literal|"filed"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|filedOn
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|u
operator|.
name|put
argument_list|(
literal|"account_id"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|account
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|u
operator|.
name|put
argument_list|(
literal|"data"
argument_list|,
name|encStr
argument_list|)
expr_stmt|;
name|connFactory
operator|.
name|open
argument_list|(
name|storeUrl
argument_list|)
operator|.
name|store
argument_list|(
name|u
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot store encrypted contact information"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ContactInformationStoreException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PGPException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot store encrypted contact information"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ContactInformationStoreException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot store encrypted contact information"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ContactInformationStoreException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|cpk ()
specifier|private
specifier|final
name|PGPEncryptedDataGenerator
name|cpk
parameter_list|()
throws|throws
name|NoSuchProviderException
throws|,
name|PGPException
block|{
specifier|final
name|BcPGPDataEncryptorBuilder
name|builder
init|=
operator|new
name|BcPGPDataEncryptorBuilder
argument_list|(
name|PGPEncryptedData
operator|.
name|CAST5
argument_list|)
operator|.
name|setSecureRandom
argument_list|(
name|prng
argument_list|)
decl_stmt|;
name|PGPEncryptedDataGenerator
name|cpk
init|=
operator|new
name|PGPEncryptedDataGenerator
argument_list|(
name|builder
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|BcPublicKeyKeyEncryptionMethodGenerator
name|methodGenerator
init|=
operator|new
name|BcPublicKeyKeyEncryptionMethodGenerator
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|cpk
operator|.
name|addMethod
argument_list|(
name|methodGenerator
argument_list|)
expr_stmt|;
return|return
name|cpk
return|;
block|}
DECL|method|encrypt (final String name, final Date date, final byte[] rawText)
specifier|private
name|byte
index|[]
name|encrypt
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Date
name|date
parameter_list|,
specifier|final
name|byte
index|[]
name|rawText
parameter_list|)
throws|throws
name|NoSuchProviderException
throws|,
name|PGPException
throws|,
name|IOException
block|{
specifier|final
name|byte
index|[]
name|zText
init|=
name|compress
argument_list|(
name|name
argument_list|,
name|date
argument_list|,
name|rawText
argument_list|)
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|ArmoredOutputStream
name|aout
init|=
operator|new
name|ArmoredOutputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
specifier|final
name|OutputStream
name|cout
init|=
name|cpk
argument_list|()
operator|.
name|open
argument_list|(
name|aout
argument_list|,
name|zText
operator|.
name|length
argument_list|)
decl_stmt|;
name|cout
operator|.
name|write
argument_list|(
name|zText
argument_list|)
expr_stmt|;
name|cout
operator|.
name|close
argument_list|()
expr_stmt|;
name|aout
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|buf
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|compress (final String fileName, Date fileDate, final byte[] plainText)
specifier|private
specifier|static
name|byte
index|[]
name|compress
parameter_list|(
specifier|final
name|String
name|fileName
parameter_list|,
name|Date
name|fileDate
parameter_list|,
specifier|final
name|byte
index|[]
name|plainText
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|PGPCompressedDataGenerator
name|comdg
decl_stmt|;
specifier|final
name|int
name|len
init|=
name|plainText
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|fileDate
operator|==
literal|null
condition|)
block|{
name|fileDate
operator|=
name|PGPLiteralData
operator|.
name|NOW
expr_stmt|;
block|}
name|comdg
operator|=
operator|new
name|PGPCompressedDataGenerator
argument_list|(
name|PGPCompressedData
operator|.
name|ZIP
argument_list|)
expr_stmt|;
specifier|final
name|OutputStream
name|out
init|=
operator|new
name|PGPLiteralDataGenerator
argument_list|()
operator|.
name|open
argument_list|(
name|comdg
operator|.
name|open
argument_list|(
name|buf
argument_list|)
argument_list|,
name|PGPLiteralData
operator|.
name|BINARY
argument_list|,
name|fileName
argument_list|,
name|len
argument_list|,
name|fileDate
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|plainText
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|comdg
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|buf
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|format (final Account account, final ContactInformation info)
specifier|private
name|String
name|format
parameter_list|(
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|ContactInformation
name|info
parameter_list|)
throws|throws
name|ContactInformationStoreException
block|{
name|Timestamp
name|on
init|=
name|account
operator|.
name|getContactFiledOn
argument_list|()
decl_stmt|;
if|if
condition|(
name|on
operator|==
literal|null
condition|)
block|{
name|on
operator|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
expr_stmt|;
block|}
specifier|final
name|SimpleDateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss.SSS"
argument_list|)
decl_stmt|;
name|df
operator|.
name|setTimeZone
argument_list|(
name|UTC
argument_list|)
expr_stmt|;
specifier|final
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Account-Id"
argument_list|,
name|account
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Date"
argument_list|,
name|df
operator|.
name|format
argument_list|(
name|on
argument_list|)
operator|+
literal|" "
operator|+
name|UTC
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Full-Name"
argument_list|,
name|account
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Preferred-Email"
argument_list|,
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byAccount
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
specifier|final
name|StringBuilder
name|oistr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|e
operator|.
name|getEmailAddress
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|oistr
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|oistr
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|oistr
operator|.
name|append
argument_list|(
name|e
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
argument_list|)
condition|)
block|{
if|if
condition|(
name|oistr
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|oistr
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|oistr
operator|.
name|append
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
name|oistr
operator|.
name|append
argument_list|(
name|e
operator|.
name|getExternalId
argument_list|()
argument_list|)
expr_stmt|;
name|oistr
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
block|}
name|field
argument_list|(
name|b
argument_list|,
literal|"Identity"
argument_list|,
name|oistr
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ContactInformationStoreException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|field
argument_list|(
name|b
argument_list|,
literal|"Address"
argument_list|,
name|info
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Country"
argument_list|,
name|info
operator|.
name|getCountry
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Phone-Number"
argument_list|,
name|info
operator|.
name|getPhoneNumber
argument_list|()
argument_list|)
expr_stmt|;
name|field
argument_list|(
name|b
argument_list|,
literal|"Fax-Number"
argument_list|,
name|info
operator|.
name|getFaxNumber
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|field (final StringBuilder b, final String name, String value)
specifier|private
specifier|static
name|void
name|field
parameter_list|(
specifier|final
name|StringBuilder
name|b
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|b
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|indexOf
argument_list|(
literal|'\n'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
name|value
operator|=
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"\n\t"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n\t"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


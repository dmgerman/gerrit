begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
package|;
end_package

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
name|java
operator|.
name|security
operator|.
name|InvalidKeyException
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
name|SecureRandom
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
name|javax
operator|.
name|crypto
operator|.
name|Mac
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|ShortBufferException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
import|;
end_import

begin_comment
comment|/**  * Utility function to compute and verify XSRF tokens.  *  *<p>{@link SignedTokenEmailTokenVerifier} uses this class to verify tokens appearing in the custom  *<code>xsrfKey  *</code> JSON request property. The tokens protect against cross-site request forgery by depending  * upon the browser's security model. The classic browser security model prohibits a script from  * site A from reading any data received from site B. By sending unforgeable tokens from the server  * and asking the client to return them to us, the client script must have had read access to the  * token at some point and is therefore also from our server.  */
end_comment

begin_class
DECL|class|SignedToken
specifier|public
class|class
name|SignedToken
block|{
DECL|field|INT_SZ
specifier|private
specifier|static
specifier|final
name|int
name|INT_SZ
init|=
literal|4
decl_stmt|;
DECL|field|MAC_ALG
specifier|private
specifier|static
specifier|final
name|String
name|MAC_ALG
init|=
literal|"HmacSHA1"
decl_stmt|;
comment|/**    * Generate a random key for use with the XSRF library.    *    * @return a new private key, base 64 encoded.    */
DECL|method|generateRandomKey ()
specifier|public
specifier|static
name|String
name|generateRandomKey
parameter_list|()
block|{
specifier|final
name|byte
index|[]
name|r
init|=
operator|new
name|byte
index|[
literal|26
index|]
decl_stmt|;
operator|new
name|SecureRandom
argument_list|()
operator|.
name|nextBytes
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return
name|encodeBase64
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|field|maxAge
specifier|private
specifier|final
name|int
name|maxAge
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|SecretKeySpec
name|key
decl_stmt|;
DECL|field|rng
specifier|private
specifier|final
name|SecureRandom
name|rng
decl_stmt|;
DECL|field|tokenLength
specifier|private
specifier|final
name|int
name|tokenLength
decl_stmt|;
comment|/**    * Create a new utility, using the specific key.    *    * @param age the number of seconds a token may remain valid.    * @param keyBase64 base 64 encoded representation of the key.    * @throws XsrfException the JVM doesn't support the necessary algorithms.    */
DECL|method|SignedToken (final int age, final String keyBase64)
specifier|public
name|SignedToken
parameter_list|(
specifier|final
name|int
name|age
parameter_list|,
specifier|final
name|String
name|keyBase64
parameter_list|)
throws|throws
name|XsrfException
block|{
name|maxAge
operator|=
name|age
operator|>
literal|5
condition|?
name|age
operator|/
literal|5
else|:
name|age
expr_stmt|;
name|key
operator|=
operator|new
name|SecretKeySpec
argument_list|(
name|decodeBase64
argument_list|(
name|keyBase64
argument_list|)
argument_list|,
name|MAC_ALG
argument_list|)
expr_stmt|;
name|rng
operator|=
operator|new
name|SecureRandom
argument_list|()
expr_stmt|;
name|tokenLength
operator|=
literal|2
operator|*
name|INT_SZ
operator|+
name|newMac
argument_list|()
operator|.
name|getMacLength
argument_list|()
expr_stmt|;
block|}
comment|/**    * Generate a new signed token.    *    * @param text the text string to sign. Typically this should be some user-specific string, to    *     prevent replay attacks. The text must be safe to appear in whatever context the token    *     itself will appear, as the text is included on the end of the token.    * @return the signed token. The text passed in<code>text</code> will appear after the first ','    *     in the returned token string.    * @throws XsrfException the JVM doesn't support the necessary algorithms.    */
DECL|method|newToken (final String text)
name|String
name|newToken
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
throws|throws
name|XsrfException
block|{
specifier|final
name|int
name|q
init|=
name|rng
operator|.
name|nextInt
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|tokenLength
index|]
decl_stmt|;
name|encodeInt
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|q
argument_list|)
expr_stmt|;
name|encodeInt
argument_list|(
name|buf
argument_list|,
name|INT_SZ
argument_list|,
name|now
argument_list|()
operator|^
name|q
argument_list|)
expr_stmt|;
name|computeToken
argument_list|(
name|buf
argument_list|,
name|text
argument_list|)
expr_stmt|;
return|return
name|encodeBase64
argument_list|(
name|buf
argument_list|)
operator|+
literal|'$'
operator|+
name|text
return|;
block|}
comment|/**    * Validate a returned token.    *    * @param tokenString a token string previously created by this class.    * @param text text that must have been used during {@link #newToken(String)} in order for the    *     token to be valid. If null the text will be taken from the token string itself.    * @return true if the token is valid; false if the token is null, the empty string, has expired,    *     does not match the text supplied, or is a forged token.    * @throws XsrfException the JVM doesn't support the necessary algorithms to generate a token.    *     XSRF services are simply not available.    */
DECL|method|checkToken (final String tokenString, final String text)
name|ValidToken
name|checkToken
parameter_list|(
specifier|final
name|String
name|tokenString
parameter_list|,
specifier|final
name|String
name|text
parameter_list|)
throws|throws
name|XsrfException
block|{
if|if
condition|(
name|tokenString
operator|==
literal|null
operator|||
name|tokenString
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|int
name|s
init|=
name|tokenString
operator|.
name|indexOf
argument_list|(
literal|'$'
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|recvText
init|=
name|tokenString
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|in
decl_stmt|;
try|try
block|{
name|in
operator|=
name|decodeBase64
argument_list|(
name|tokenString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|in
operator|.
name|length
operator|!=
name|tokenLength
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|int
name|q
init|=
name|decodeInt
argument_list|(
name|in
argument_list|,
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|int
name|c
init|=
name|decodeInt
argument_list|(
name|in
argument_list|,
name|INT_SZ
argument_list|)
operator|^
name|q
decl_stmt|;
specifier|final
name|int
name|n
init|=
name|now
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxAge
operator|>
literal|0
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|c
operator|-
name|n
argument_list|)
operator|>
name|maxAge
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|byte
index|[]
name|gen
init|=
operator|new
name|byte
index|[
name|tokenLength
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|in
argument_list|,
literal|0
argument_list|,
name|gen
argument_list|,
literal|0
argument_list|,
literal|2
operator|*
name|INT_SZ
argument_list|)
expr_stmt|;
name|computeToken
argument_list|(
name|gen
argument_list|,
name|text
operator|!=
literal|null
condition|?
name|text
else|:
name|recvText
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|gen
argument_list|,
name|in
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ValidToken
argument_list|(
name|maxAge
operator|>
literal|0
operator|&&
name|c
operator|+
operator|(
name|maxAge
operator|>>
literal|1
operator|)
operator|<=
name|n
argument_list|,
name|recvText
argument_list|)
return|;
block|}
DECL|method|computeToken (final byte[] buf, final String text)
specifier|private
name|void
name|computeToken
parameter_list|(
specifier|final
name|byte
index|[]
name|buf
parameter_list|,
specifier|final
name|String
name|text
parameter_list|)
throws|throws
name|XsrfException
block|{
specifier|final
name|Mac
name|m
init|=
name|newMac
argument_list|()
decl_stmt|;
name|m
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|2
operator|*
name|INT_SZ
argument_list|)
expr_stmt|;
name|m
operator|.
name|update
argument_list|(
name|toBytes
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|m
operator|.
name|doFinal
argument_list|(
name|buf
argument_list|,
literal|2
operator|*
name|INT_SZ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ShortBufferException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XsrfException
argument_list|(
literal|"Unexpected token overflow"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|newMac ()
specifier|private
name|Mac
name|newMac
parameter_list|()
throws|throws
name|XsrfException
block|{
try|try
block|{
specifier|final
name|Mac
name|m
init|=
name|Mac
operator|.
name|getInstance
argument_list|(
name|MAC_ALG
argument_list|)
decl_stmt|;
name|m
operator|.
name|init
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XsrfException
argument_list|(
name|MAC_ALG
operator|+
literal|" not supported"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XsrfException
argument_list|(
literal|"Invalid private key"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|now ()
specifier|private
specifier|static
name|int
name|now
parameter_list|()
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|5000L
argument_list|)
return|;
block|}
DECL|method|decodeBase64 (final String s)
specifier|private
specifier|static
name|byte
index|[]
name|decodeBase64
parameter_list|(
specifier|final
name|String
name|s
parameter_list|)
block|{
return|return
name|BaseEncoding
operator|.
name|base64
argument_list|()
operator|.
name|decode
argument_list|(
name|s
argument_list|)
return|;
block|}
DECL|method|encodeBase64 (final byte[] buf)
specifier|private
specifier|static
name|String
name|encodeBase64
parameter_list|(
specifier|final
name|byte
index|[]
name|buf
parameter_list|)
block|{
return|return
name|BaseEncoding
operator|.
name|base64
argument_list|()
operator|.
name|encode
argument_list|(
name|buf
argument_list|)
return|;
block|}
DECL|method|encodeInt (final byte[] buf, final int o, final int v)
specifier|private
specifier|static
name|void
name|encodeInt
parameter_list|(
specifier|final
name|byte
index|[]
name|buf
parameter_list|,
specifier|final
name|int
name|o
parameter_list|,
specifier|final
name|int
name|v
parameter_list|)
block|{
name|int
name|_v
init|=
name|v
decl_stmt|;
name|buf
index|[
name|o
operator|+
literal|3
index|]
operator|=
operator|(
name|byte
operator|)
name|_v
expr_stmt|;
name|_v
operator|>>>=
literal|8
expr_stmt|;
name|buf
index|[
name|o
operator|+
literal|2
index|]
operator|=
operator|(
name|byte
operator|)
name|_v
expr_stmt|;
name|_v
operator|>>>=
literal|8
expr_stmt|;
name|buf
index|[
name|o
operator|+
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
name|_v
expr_stmt|;
name|_v
operator|>>>=
literal|8
expr_stmt|;
name|buf
index|[
name|o
index|]
operator|=
operator|(
name|byte
operator|)
name|_v
expr_stmt|;
block|}
DECL|method|decodeInt (final byte[] buf, final int o)
specifier|private
specifier|static
name|int
name|decodeInt
parameter_list|(
specifier|final
name|byte
index|[]
name|buf
parameter_list|,
specifier|final
name|int
name|o
parameter_list|)
block|{
name|int
name|r
init|=
name|buf
index|[
name|o
index|]
operator|<<
literal|8
decl_stmt|;
name|r
operator||=
name|buf
index|[
name|o
operator|+
literal|1
index|]
operator|&
literal|0xff
expr_stmt|;
name|r
operator|<<=
literal|8
expr_stmt|;
name|r
operator||=
name|buf
index|[
name|o
operator|+
literal|2
index|]
operator|&
literal|0xff
expr_stmt|;
return|return
operator|(
name|r
operator|<<
literal|8
operator|)
operator||
operator|(
name|buf
index|[
name|o
operator|+
literal|3
index|]
operator|&
literal|0xff
operator|)
return|;
block|}
DECL|method|toBytes (final String s)
specifier|private
specifier|static
name|byte
index|[]
name|toBytes
parameter_list|(
specifier|final
name|String
name|s
parameter_list|)
block|{
specifier|final
name|byte
index|[]
name|r
init|=
operator|new
name|byte
index|[
name|s
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
name|r
operator|.
name|length
operator|-
literal|1
init|;
name|k
operator|>=
literal|0
condition|;
name|k
operator|--
control|)
block|{
name|r
index|[
name|k
index|]
operator|=
operator|(
name|byte
operator|)
name|s
operator|.
name|charAt
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit


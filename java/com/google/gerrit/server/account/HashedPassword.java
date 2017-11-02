begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|base
operator|.
name|Preconditions
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
name|common
operator|.
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|DecoderException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|generators
operator|.
name|BCrypt
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Holds logic for salted, hashed passwords. It uses BCrypt from BouncyCastle, which truncates  * passwords at 72 bytes.  */
end_comment

begin_class
DECL|class|HashedPassword
specifier|public
class|class
name|HashedPassword
block|{
DECL|field|ALGORITHM_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|ALGORITHM_PREFIX
init|=
literal|"bcrypt:"
decl_stmt|;
DECL|field|secureRandom
specifier|private
specifier|static
specifier|final
name|SecureRandom
name|secureRandom
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
DECL|field|codec
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|codec
init|=
name|BaseEncoding
operator|.
name|base64
argument_list|()
decl_stmt|;
comment|// bcrypt uses 2^cost rounds. Since we use a generated random password, no need
comment|// for a high cost.
DECL|field|DEFAULT_COST
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_COST
init|=
literal|4
decl_stmt|;
comment|/**    * decodes a hashed password encoded with {@link #encode}.    *    * @throws DecoderException if input is malformed.    */
DECL|method|decode (String encoded)
specifier|public
specifier|static
name|HashedPassword
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
throws|throws
name|DecoderException
block|{
if|if
condition|(
operator|!
name|encoded
operator|.
name|startsWith
argument_list|(
name|ALGORITHM_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"unrecognized algorithm"
argument_list|)
throw|;
block|}
name|String
index|[]
name|fields
init|=
name|encoded
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"want 4 fields"
argument_list|)
throw|;
block|}
name|Integer
name|cost
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|fields
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|cost
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"cost parse failed"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|cost
operator|>=
literal|4
operator|&&
name|cost
operator|<
literal|32
operator|)
condition|)
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"cost should be 4..31 inclusive, got "
operator|+
name|cost
argument_list|)
throw|;
block|}
name|byte
index|[]
name|salt
init|=
name|codec
operator|.
name|decode
argument_list|(
name|fields
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|salt
operator|.
name|length
operator|!=
literal|16
condition|)
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"salt should be 16 bytes, got "
operator|+
name|salt
operator|.
name|length
argument_list|)
throw|;
block|}
return|return
operator|new
name|HashedPassword
argument_list|(
name|codec
operator|.
name|decode
argument_list|(
name|fields
index|[
literal|3
index|]
argument_list|)
argument_list|,
name|salt
argument_list|,
name|cost
argument_list|)
return|;
block|}
DECL|method|hashPassword (String password, byte[] salt, int cost)
specifier|private
specifier|static
name|byte
index|[]
name|hashPassword
parameter_list|(
name|String
name|password
parameter_list|,
name|byte
index|[]
name|salt
parameter_list|,
name|int
name|cost
parameter_list|)
block|{
name|byte
index|[]
name|pwBytes
init|=
name|password
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
return|return
name|BCrypt
operator|.
name|generate
argument_list|(
name|pwBytes
argument_list|,
name|salt
argument_list|,
name|cost
argument_list|)
return|;
block|}
DECL|method|fromPassword (String password)
specifier|public
specifier|static
name|HashedPassword
name|fromPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|byte
index|[]
name|salt
init|=
name|newSalt
argument_list|()
decl_stmt|;
return|return
operator|new
name|HashedPassword
argument_list|(
name|hashPassword
argument_list|(
name|password
argument_list|,
name|salt
argument_list|,
name|DEFAULT_COST
argument_list|)
argument_list|,
name|salt
argument_list|,
name|DEFAULT_COST
argument_list|)
return|;
block|}
DECL|method|newSalt ()
specifier|private
specifier|static
name|byte
index|[]
name|newSalt
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|16
index|]
decl_stmt|;
name|secureRandom
operator|.
name|nextBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|bytes
return|;
block|}
DECL|field|salt
specifier|private
name|byte
index|[]
name|salt
decl_stmt|;
DECL|field|hashed
specifier|private
name|byte
index|[]
name|hashed
decl_stmt|;
DECL|field|cost
specifier|private
name|int
name|cost
decl_stmt|;
DECL|method|HashedPassword (byte[] hashed, byte[] salt, int cost)
specifier|private
name|HashedPassword
parameter_list|(
name|byte
index|[]
name|hashed
parameter_list|,
name|byte
index|[]
name|salt
parameter_list|,
name|int
name|cost
parameter_list|)
block|{
name|this
operator|.
name|salt
operator|=
name|salt
expr_stmt|;
name|this
operator|.
name|hashed
operator|=
name|hashed
expr_stmt|;
name|this
operator|.
name|cost
operator|=
name|cost
expr_stmt|;
name|Preconditions
operator|.
name|checkState
argument_list|(
name|cost
operator|>=
literal|4
operator|&&
name|cost
operator|<
literal|32
argument_list|)
expr_stmt|;
comment|// salt must be 128 bit.
name|Preconditions
operator|.
name|checkState
argument_list|(
name|salt
operator|.
name|length
operator|==
literal|16
argument_list|)
expr_stmt|;
block|}
comment|/**    * Serialize the hashed password and its parameters for persistent storage.    *    * @return one-line string encoding the hash and salt.    */
DECL|method|encode ()
specifier|public
name|String
name|encode
parameter_list|()
block|{
return|return
name|ALGORITHM_PREFIX
operator|+
name|cost
operator|+
literal|":"
operator|+
name|codec
operator|.
name|encode
argument_list|(
name|salt
argument_list|)
operator|+
literal|":"
operator|+
name|codec
operator|.
name|encode
argument_list|(
name|hashed
argument_list|)
return|;
block|}
DECL|method|checkPassword (String password)
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
comment|// Constant-time comparison, because we're paranoid.
return|return
name|Arrays
operator|.
name|areEqual
argument_list|(
name|hashPassword
argument_list|(
name|password
argument_list|,
name|salt
argument_list|,
name|cost
argument_list|)
argument_list|,
name|hashed
argument_list|)
return|;
block|}
block|}
end_class

end_unit

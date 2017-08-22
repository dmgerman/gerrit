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
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
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

begin_comment
comment|/** Utility methods for Bouncy Castle. */
end_comment

begin_class
DECL|class|BouncyCastleUtil
specifier|public
class|class
name|BouncyCastleUtil
block|{
comment|/**    * Check for Bouncy Castle PGP support.    *    *<p>As a side effect, adds {@link BouncyCastleProvider} as a security provider.    *    * @return whether Bouncy Castle PGP support is enabled.    */
DECL|method|havePGP ()
specifier|public
specifier|static
name|boolean
name|havePGP
parameter_list|()
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|PGPPublicKey
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addBouncyCastleProvider
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
decl||
name|ClassNotFoundException
decl||
name|SecurityException
decl||
name|NoSuchMethodException
decl||
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
decl||
name|ClassCastException
name|noBouncyCastle
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|addBouncyCastleProvider ()
specifier|private
specifier|static
name|void
name|addBouncyCastleProvider
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|SecurityException
throws|,
name|NoSuchMethodException
throws|,
name|InstantiationException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|BouncyCastleProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|clazz
operator|.
name|getConstructor
argument_list|()
decl_stmt|;
name|Security
operator|.
name|addProvider
argument_list|(
operator|(
name|java
operator|.
name|security
operator|.
name|Provider
operator|)
name|constructor
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|BouncyCastleUtil ()
specifier|private
name|BouncyCastleUtil
parameter_list|()
block|{}
block|}
end_class

end_unit


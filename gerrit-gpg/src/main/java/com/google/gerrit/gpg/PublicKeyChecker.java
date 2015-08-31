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
comment|/** Checker for GPG public keys for use in a push certificate. */
end_comment

begin_class
DECL|class|PublicKeyChecker
specifier|public
class|class
name|PublicKeyChecker
block|{
comment|/**    * Check a public key.    *    * @param key the public key.    */
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
name|checkCustom
argument_list|(
name|key
argument_list|,
name|problems
argument_list|)
expr_stmt|;
return|return
operator|new
name|CheckResult
argument_list|(
name|problems
argument_list|)
return|;
block|}
comment|/**    * Perform custom checks.    *<p>    * Default implementation does nothing, but may be overridden by subclasses.    *    * @param key the public key.    * @param problems list to which any problems should be added.    */
DECL|method|checkCustom (PGPPublicKey key, List<String> problems)
specifier|public
name|void
name|checkCustom
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|problems
parameter_list|)
block|{
comment|// Default implementation does nothing.
block|}
block|}
end_class

end_unit


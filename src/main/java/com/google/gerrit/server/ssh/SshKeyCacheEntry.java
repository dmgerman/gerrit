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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountSshKey
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
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
name|client
operator|.
name|SchemaFactory
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
name|security
operator|.
name|PublicKey
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

begin_class
DECL|class|SshKeyCacheEntry
class|class
name|SshKeyCacheEntry
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
name|SshKeyCacheEntry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|AccountSshKey
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|publicKey
specifier|private
specifier|final
name|PublicKey
name|publicKey
decl_stmt|;
DECL|method|SshKeyCacheEntry (final AccountSshKey.Id i, final PublicKey k)
name|SshKeyCacheEntry
parameter_list|(
specifier|final
name|AccountSshKey
operator|.
name|Id
name|i
parameter_list|,
specifier|final
name|PublicKey
name|k
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
name|publicKey
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getAccount ()
name|Account
operator|.
name|Id
name|getAccount
parameter_list|()
block|{
return|return
name|id
operator|.
name|getParentKey
argument_list|()
return|;
block|}
DECL|method|match (final PublicKey inkey)
name|boolean
name|match
parameter_list|(
specifier|final
name|PublicKey
name|inkey
parameter_list|)
block|{
return|return
name|publicKey
operator|.
name|equals
argument_list|(
name|inkey
argument_list|)
return|;
block|}
DECL|method|updateLastUsed (final SchemaFactory<ReviewDb> schema)
name|void
name|updateLastUsed
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|)
block|{
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
specifier|final
name|AccountSshKey
name|k
init|=
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
condition|)
block|{
name|k
operator|.
name|setLastUsedOn
argument_list|()
expr_stmt|;
name|db
operator|.
name|accountSshKeys
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|k
argument_list|)
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
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to update \""
operator|+
name|id
operator|+
literal|"\" SSH key used"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


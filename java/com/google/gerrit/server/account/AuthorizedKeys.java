begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|annotations
operator|.
name|VisibleForTesting
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
name|AccountSshKey
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
name|Collection
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
name|Optional
import|;
end_import

begin_class
DECL|class|AuthorizedKeys
specifier|public
class|class
name|AuthorizedKeys
block|{
DECL|field|FILE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|FILE_NAME
init|=
literal|"authorized_keys"
decl_stmt|;
DECL|field|INVALID_KEY_COMMENT_PREFIX
annotation|@
name|VisibleForTesting
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_KEY_COMMENT_PREFIX
init|=
literal|"# INVALID "
decl_stmt|;
DECL|field|DELETED_KEY_COMMENT
annotation|@
name|VisibleForTesting
specifier|public
specifier|static
specifier|final
name|String
name|DELETED_KEY_COMMENT
init|=
literal|"# DELETED"
decl_stmt|;
DECL|method|parse (Account.Id accountId, String s)
specifier|public
specifier|static
name|List
argument_list|<
name|Optional
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
name|parse
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|s
parameter_list|)
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|seq
init|=
literal|1
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|s
operator|.
name|split
argument_list|(
literal|"\\r?\\n"
argument_list|)
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|INVALID_KEY_COMMENT_PREFIX
argument_list|)
condition|)
block|{
name|String
name|pub
init|=
name|line
operator|.
name|substring
argument_list|(
name|INVALID_KEY_COMMENT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|AccountSshKey
name|key
init|=
operator|new
name|AccountSshKey
argument_list|(
operator|new
name|AccountSshKey
operator|.
name|Id
argument_list|(
name|accountId
argument_list|,
name|seq
operator|++
argument_list|)
argument_list|,
name|pub
argument_list|)
decl_stmt|;
name|key
operator|.
name|setInvalid
argument_list|()
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|DELETED_KEY_COMMENT
argument_list|)
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
name|seq
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
else|else
block|{
name|AccountSshKey
name|key
init|=
operator|new
name|AccountSshKey
argument_list|(
operator|new
name|AccountSshKey
operator|.
name|Id
argument_list|(
name|accountId
argument_list|,
name|seq
operator|++
argument_list|)
argument_list|,
name|line
argument_list|)
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keys
return|;
block|}
DECL|method|serialize (Collection<Optional<AccountSshKey>> keys)
specifier|public
specifier|static
name|String
name|serialize
parameter_list|(
name|Collection
argument_list|<
name|Optional
argument_list|<
name|AccountSshKey
argument_list|>
argument_list|>
name|keys
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Optional
argument_list|<
name|AccountSshKey
argument_list|>
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
name|key
operator|.
name|isPresent
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|get
argument_list|()
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|INVALID_KEY_COMMENT_PREFIX
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|key
operator|.
name|get
argument_list|()
operator|.
name|getSshPublicKey
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
name|DELETED_KEY_COMMENT
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

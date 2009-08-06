begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|HashMap
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

begin_comment
comment|/** In-memory table of {@link AccountInfo}, indexed by {@link Account.Id}. */
end_comment

begin_class
DECL|class|AccountInfoCache
specifier|public
class|class
name|AccountInfoCache
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|AccountInfoCache
name|EMPTY
decl_stmt|;
static|static
block|{
name|EMPTY
operator|=
operator|new
name|AccountInfoCache
argument_list|()
expr_stmt|;
name|EMPTY
operator|.
name|accounts
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
comment|/** Obtain an empty cache singleton. */
DECL|method|empty ()
specifier|public
specifier|static
name|AccountInfoCache
name|empty
parameter_list|()
block|{
return|return
name|EMPTY
return|;
block|}
DECL|field|accounts
specifier|protected
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
name|accounts
decl_stmt|;
DECL|method|AccountInfoCache ()
specifier|protected
name|AccountInfoCache
parameter_list|()
block|{   }
DECL|method|AccountInfoCache (final Iterable<AccountInfo> list)
specifier|public
name|AccountInfoCache
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|AccountInfo
argument_list|>
name|list
parameter_list|)
block|{
name|accounts
operator|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|AccountInfo
name|ai
range|:
name|list
control|)
block|{
name|accounts
operator|.
name|put
argument_list|(
name|ai
operator|.
name|getId
argument_list|()
argument_list|,
name|ai
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Lookup the account summary    *<p>    * The return value can take on one of three forms:    *<ul>    *<li><code>null</code>, if<code>id == null</code>.</li>    *<li>a valid info block, if<code>id</code> was loaded.</li>    *<li>an anonymous info block, if<code>id</code> was not loaded.</li>    *</ul>    *    * @param id the id desired.    * @return info block for the account.    */
DECL|method|get (final Account.Id id)
specifier|public
name|AccountInfo
name|get
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AccountInfo
name|r
init|=
name|accounts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|AccountInfo
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** Merge the information from another cache into this one. */
DECL|method|merge (final AccountInfoCache other)
specifier|public
name|void
name|merge
parameter_list|(
specifier|final
name|AccountInfoCache
name|other
parameter_list|)
block|{
assert|assert
name|this
operator|!=
name|EMPTY
assert|;
name|accounts
operator|.
name|putAll
argument_list|(
name|other
operator|.
name|accounts
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


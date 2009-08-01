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
DECL|package|com.google.gerrit.server.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|http
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
name|Gerrit
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
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|ValidToken
import|;
end_import

begin_comment
comment|/** Data encoded into the {@link Gerrit#ACCOUNT_COOKIE} value. */
end_comment

begin_class
DECL|class|AccountCookie
class|class
name|AccountCookie
block|{
DECL|field|accountId
specifier|private
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|remember
specifier|private
name|boolean
name|remember
decl_stmt|;
DECL|method|AccountCookie (final Account.Id id, final boolean remember)
name|AccountCookie
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|boolean
name|remember
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|remember
operator|=
name|remember
expr_stmt|;
block|}
DECL|method|getAccountId ()
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|isRemember ()
name|boolean
name|isRemember
parameter_list|()
block|{
return|return
name|remember
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"."
operator|+
operator|(
name|isRemember
argument_list|()
condition|?
literal|"t"
else|:
literal|"f"
operator|)
return|;
block|}
DECL|method|parse (final ValidToken tok)
specifier|static
name|AccountCookie
name|parse
parameter_list|(
specifier|final
name|ValidToken
name|tok
parameter_list|)
block|{
if|if
condition|(
name|tok
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|str
init|=
name|tok
operator|.
name|getData
argument_list|()
decl_stmt|;
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
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
name|String
index|[]
name|parts
init|=
name|str
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|0
operator|||
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Account
operator|.
name|Id
name|accountId
init|=
name|Account
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|remember
init|=
name|parts
operator|.
name|length
operator|==
literal|2
condition|?
literal|"t"
operator|.
name|equals
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
else|:
literal|true
decl_stmt|;
return|return
operator|new
name|AccountCookie
argument_list|(
name|accountId
argument_list|,
name|remember
argument_list|)
return|;
block|}
block|}
end_class

end_unit


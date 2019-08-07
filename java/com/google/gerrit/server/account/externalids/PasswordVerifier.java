begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account.externalids
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
operator|.
name|externalids
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_USERNAME
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
name|Strings
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
name|flogger
operator|.
name|FluentLogger
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
name|Nullable
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
name|account
operator|.
name|HashedPassword
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

begin_comment
comment|/** Checks if a given username and password match a user's external IDs. */
end_comment

begin_class
DECL|class|PasswordVerifier
specifier|public
class|class
name|PasswordVerifier
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
comment|/** Returns {@code true} if there is an external ID matching both the username and password. */
DECL|method|checkPassword ( Collection<ExternalId> externalIds, String username, @Nullable String password)
specifier|public
specifier|static
name|boolean
name|checkPassword
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|,
name|String
name|username
parameter_list|,
annotation|@
name|Nullable
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|ExternalId
name|id
range|:
name|externalIds
control|)
block|{
comment|// Only process the "username:$USER" entry, which is unique.
if|if
condition|(
operator|!
name|id
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
operator|||
operator|!
name|username
operator|.
name|equals
argument_list|(
name|id
operator|.
name|key
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|hashedStr
init|=
name|id
operator|.
name|password
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|hashedStr
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|HashedPassword
operator|.
name|decode
argument_list|(
name|hashedStr
argument_list|)
operator|.
name|checkPassword
argument_list|(
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DecoderException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"DecoderException for user %s: %s "
argument_list|,
name|username
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit


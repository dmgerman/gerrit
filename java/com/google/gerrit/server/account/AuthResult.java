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
name|gerrit
operator|.
name|entities
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|ExternalId
import|;
end_import

begin_comment
comment|/** Result from {@link AccountManager#authenticate(AuthRequest)}. */
end_comment

begin_class
DECL|class|AuthResult
specifier|public
class|class
name|AuthResult
block|{
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|externalId
specifier|private
specifier|final
name|ExternalId
operator|.
name|Key
name|externalId
decl_stmt|;
DECL|field|isNew
specifier|private
specifier|final
name|boolean
name|isNew
decl_stmt|;
DECL|method|AuthResult (Account.Id accountId, ExternalId.Key externalId, boolean isNew)
specifier|public
name|AuthResult
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|ExternalId
operator|.
name|Key
name|externalId
parameter_list|,
name|boolean
name|isNew
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|externalId
operator|=
name|externalId
expr_stmt|;
name|this
operator|.
name|isNew
operator|=
name|isNew
expr_stmt|;
block|}
comment|/** Identity of the user account that was authenticated into. */
DECL|method|getAccountId ()
specifier|public
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
comment|/** External identity used to authenticate the user. */
DECL|method|getExternalId ()
specifier|public
name|ExternalId
operator|.
name|Key
name|getExternalId
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
comment|/**    * True if this account was recently created for the user.    *    *<p>New users should be redirected to the registration screen, so they can configure their new    * user account.    */
DECL|method|isNew ()
specifier|public
name|boolean
name|isNew
parameter_list|()
block|{
return|return
name|isNew
return|;
block|}
block|}
end_class

end_unit


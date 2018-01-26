begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|account
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
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|HttpPasswordInput
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|extensions
operator|.
name|restapi
operator|.
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|CurrentUser
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
name|IdentifiedUser
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
name|UserInitiated
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
name|AccountResource
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
name|AccountsUpdate
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
name|ExternalIds
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|server
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_class
DECL|class|PutHttpPassword
specifier|public
class|class
name|PutHttpPassword
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|HttpPasswordInput
argument_list|>
block|{
DECL|field|LEN
specifier|private
specifier|static
specifier|final
name|int
name|LEN
init|=
literal|31
decl_stmt|;
DECL|field|rng
specifier|private
specifier|static
specifier|final
name|SecureRandom
name|rng
decl_stmt|;
static|static
block|{
try|try
block|{
name|rng
operator|=
name|SecureRandom
operator|.
name|getInstance
argument_list|(
literal|"SHA1PRNG"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot create RNG for password generator"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|externalIds
specifier|private
specifier|final
name|ExternalIds
name|externalIds
decl_stmt|;
DECL|field|accountsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutHttpPassword ( Provider<CurrentUser> self, PermissionBackend permissionBackend, ExternalIds externalIds, @UserInitiated Provider<AccountsUpdate> accountsUpdateProvider)
name|PutHttpPassword
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ExternalIds
name|externalIds
parameter_list|,
annotation|@
name|UserInitiated
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|externalIds
operator|=
name|externalIds
expr_stmt|;
name|this
operator|.
name|accountsUpdateProvider
operator|=
name|accountsUpdateProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc, HttpPasswordInput input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|HttpPasswordInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|rsrc
operator|.
name|getUser
argument_list|()
condition|)
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|self
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|HttpPasswordInput
argument_list|()
expr_stmt|;
block|}
name|input
operator|.
name|httpPassword
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|httpPassword
argument_list|)
expr_stmt|;
name|String
name|newPassword
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|generate
condition|)
block|{
name|newPassword
operator|=
name|generate
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|input
operator|.
name|httpPassword
operator|==
literal|null
condition|)
block|{
name|newPassword
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// Only administrators can explicitly set the password.
name|permissionBackend
operator|.
name|user
argument_list|(
name|self
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
expr_stmt|;
name|newPassword
operator|=
name|input
operator|.
name|httpPassword
expr_stmt|;
block|}
return|return
name|apply
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|newPassword
argument_list|)
return|;
block|}
DECL|method|apply (IdentifiedUser user, String newPassword)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|String
name|newPassword
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|String
name|userName
init|=
name|user
operator|.
name|getUserName
argument_list|()
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|ResourceConflictException
argument_list|(
literal|"username must be set"
argument_list|)
argument_list|)
decl_stmt|;
name|ExternalId
name|extId
init|=
name|externalIds
operator|.
name|get
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_USERNAME
argument_list|,
name|userName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|extId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Set HTTP Password via API"
argument_list|,
name|extId
operator|.
name|accountId
argument_list|()
argument_list|,
name|u
lambda|->
name|u
operator|.
name|updateExternalId
argument_list|(
name|ExternalId
operator|.
name|createWithPassword
argument_list|(
name|extId
operator|.
name|key
argument_list|()
argument_list|,
name|extId
operator|.
name|accountId
argument_list|()
argument_list|,
name|extId
operator|.
name|email
argument_list|()
argument_list|,
name|newPassword
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|newPassword
argument_list|)
condition|?
name|Response
operator|.
expr|<
name|String
operator|>
name|none
argument_list|()
else|:
name|Response
operator|.
name|ok
argument_list|(
name|newPassword
argument_list|)
return|;
block|}
DECL|method|generate ()
specifier|public
specifier|static
name|String
name|generate
parameter_list|()
block|{
name|byte
index|[]
name|rand
init|=
operator|new
name|byte
index|[
name|LEN
index|]
decl_stmt|;
name|rng
operator|.
name|nextBytes
argument_list|(
name|rand
argument_list|)
expr_stmt|;
name|byte
index|[]
name|enc
init|=
name|Base64
operator|.
name|encodeBase64
argument_list|(
name|rand
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|(
name|enc
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|enc
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|enc
index|[
name|i
index|]
operator|==
literal|'='
condition|)
block|{
break|break;
block|}
name|r
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|enc
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


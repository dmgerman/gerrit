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
name|collect
operator|.
name|Lists
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
name|SshKeyInfo
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
name|RestReadView
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
name|server
operator|.
name|account
operator|.
name|VersionedAuthorizedKeys
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|util
operator|.
name|List
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
name|RepositoryNotFoundException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetSshKeys
specifier|public
class|class
name|GetSshKeys
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
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
DECL|field|authorizedKeys
specifier|private
specifier|final
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetSshKeys ( Provider<CurrentUser> self, PermissionBackend permissionBackend, VersionedAuthorizedKeys.Accessor authorizedKeys)
name|GetSshKeys
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
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
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
name|authorizedKeys
operator|=
name|authorizedKeys
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|List
argument_list|<
name|SshKeyInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|OrmException
throws|,
name|RepositoryNotFoundException
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
name|MODIFY_ACCOUNT
argument_list|)
expr_stmt|;
block|}
return|return
name|apply
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
return|;
block|}
DECL|method|apply (IdentifiedUser user)
specifier|public
name|List
argument_list|<
name|SshKeyInfo
argument_list|>
name|apply
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|authorizedKeys
operator|.
name|getKeys
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
name|GetSshKeys
operator|::
name|newSshKeyInfo
argument_list|)
return|;
block|}
DECL|method|newSshKeyInfo (AccountSshKey sshKey)
specifier|public
specifier|static
name|SshKeyInfo
name|newSshKeyInfo
parameter_list|(
name|AccountSshKey
name|sshKey
parameter_list|)
block|{
name|SshKeyInfo
name|info
init|=
operator|new
name|SshKeyInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|seq
operator|=
name|sshKey
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|info
operator|.
name|sshPublicKey
operator|=
name|sshKey
operator|.
name|getSshPublicKey
argument_list|()
expr_stmt|;
name|info
operator|.
name|encodedKey
operator|=
name|sshKey
operator|.
name|getEncodedKey
argument_list|()
expr_stmt|;
name|info
operator|.
name|algorithm
operator|=
name|sshKey
operator|.
name|getAlgorithm
argument_list|()
expr_stmt|;
name|info
operator|.
name|comment
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|sshKey
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|valid
operator|=
name|sshKey
operator|.
name|isValid
argument_list|()
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit


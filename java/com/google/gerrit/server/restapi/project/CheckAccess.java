begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
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
name|project
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_HEADS
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
name|api
operator|.
name|config
operator|.
name|AccessCheckInfo
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
name|api
operator|.
name|config
operator|.
name|AccessCheckInput
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
name|BadRequestException
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
name|RestApiException
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
name|BranchNameKey
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
name|AccountResolver
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
name|git
operator|.
name|GitRepositoryManager
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
name|DefaultPermissionMappings
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
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|ProjectPermission
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
name|RefPermission
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
name|project
operator|.
name|ProjectResource
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
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|CheckAccess
specifier|public
class|class
name|CheckAccess
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|AccessCheckInput
argument_list|>
block|{
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|gitRepositoryManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitRepositoryManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|CheckAccess ( AccountResolver resolver, PermissionBackend permissionBackend, GitRepositoryManager gitRepositoryManager)
name|CheckAccess
parameter_list|(
name|AccountResolver
name|resolver
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GitRepositoryManager
name|gitRepositoryManager
parameter_list|)
block|{
name|this
operator|.
name|accountResolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|gitRepositoryManager
operator|=
name|gitRepositoryManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc, AccessCheckInput input)
specifier|public
name|Response
argument_list|<
name|AccessCheckInfo
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|,
name|AccessCheckInput
name|input
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_ACCESS
argument_list|)
expr_stmt|;
name|rsrc
operator|.
name|getProjectState
argument_list|()
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"input is required"
argument_list|)
throw|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|account
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"input requires 'account'"
argument_list|)
throw|;
block|}
name|Account
operator|.
name|Id
name|match
init|=
name|accountResolver
operator|.
name|resolve
argument_list|(
name|input
operator|.
name|account
argument_list|)
operator|.
name|asUnique
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|.
name|id
argument_list|()
decl_stmt|;
name|AccessCheckInfo
name|info
init|=
operator|new
name|AccessCheckInfo
argument_list|()
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|match
argument_list|)
operator|.
name|project
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|ACCESS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|info
operator|.
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"user %s cannot see project %s"
argument_list|,
name|match
argument_list|,
name|rsrc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|status
operator|=
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
return|;
block|}
name|RefPermission
name|refPerm
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|permission
argument_list|)
condition|)
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"must set 'ref' when specifying 'permission'"
argument_list|)
throw|;
block|}
name|Optional
argument_list|<
name|RefPermission
argument_list|>
name|rp
init|=
name|DefaultPermissionMappings
operator|.
name|refPermission
argument_list|(
name|input
operator|.
name|permission
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rp
operator|.
name|isPresent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"'%s' is not recognized as ref permission"
argument_list|,
name|input
operator|.
name|permission
argument_list|)
argument_list|)
throw|;
block|}
name|refPerm
operator|=
name|rp
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|refPerm
operator|=
name|RefPermission
operator|.
name|READ
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|ref
argument_list|)
condition|)
block|{
try|try
block|{
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|match
argument_list|)
operator|.
name|ref
argument_list|(
name|BranchNameKey
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|input
operator|.
name|ref
argument_list|)
argument_list|)
operator|.
name|check
argument_list|(
name|refPerm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|info
operator|.
name|status
operator|=
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
expr_stmt|;
name|info
operator|.
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"user %s lacks permission %s for %s in project %s"
argument_list|,
name|match
argument_list|,
name|input
operator|.
name|permission
argument_list|,
name|input
operator|.
name|ref
argument_list|,
name|rsrc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// We say access is okay if there are no refs, but this warrants a warning,
comment|// as access denied looks the same as no branches to the user.
try|try
init|(
name|Repository
name|repo
init|=
name|gitRepositoryManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|REFS_HEADS
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|info
operator|.
name|message
operator|=
literal|"access is OK, but repository has no branches under refs/heads/"
expr_stmt|;
block|}
block|}
block|}
name|info
operator|.
name|status
operator|=
name|HttpServletResponse
operator|.
name|SC_OK
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
end_class

end_unit


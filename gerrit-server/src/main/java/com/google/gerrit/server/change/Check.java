begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|FixInput
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
name|client
operator|.
name|ListChangesOption
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
name|ChangeInfo
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
name|project
operator|.
name|NoSuchProjectException
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

begin_class
DECL|class|Check
specifier|public
class|class
name|Check
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
implements|,
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|FixInput
argument_list|>
block|{
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|jsonFactory
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|jsonFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|Check (PermissionBackend permissionBackend, Provider<CurrentUser> user, ChangeJson.Factory json)
name|Check
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|jsonFactory
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|OrmException
block|{
return|return
name|Response
operator|.
name|withMustRevalidate
argument_list|(
name|newChangeJson
argument_list|()
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, FixInput input)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|FixInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|OrmException
throws|,
name|PermissionBackendException
throws|,
name|NoSuchProjectException
throws|,
name|IOException
block|{
name|PermissionBackend
operator|.
name|WithUser
name|perm
init|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rsrc
operator|.
name|isUserOwner
argument_list|()
condition|)
block|{
try|try
block|{
name|perm
operator|.
name|project
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|READ_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|perm
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|MAINTAIN_SERVER
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Response
operator|.
name|withMustRevalidate
argument_list|(
name|newChangeJson
argument_list|()
operator|.
name|fix
argument_list|(
name|input
argument_list|)
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newChangeJson ()
specifier|private
name|ChangeJson
name|newChangeJson
parameter_list|()
block|{
return|return
name|jsonFactory
operator|.
name|create
argument_list|(
name|ListChangesOption
operator|.
name|CHECK
argument_list|)
return|;
block|}
block|}
end_class

end_unit


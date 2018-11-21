begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|CheckAccessReadView
specifier|public
class|class
name|CheckAccessReadView
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|refName
name|String
name|refName
decl_stmt|;
DECL|field|account
name|String
name|account
decl_stmt|;
DECL|field|permission
name|String
name|permission
decl_stmt|;
DECL|field|checkAccess
annotation|@
name|Inject
name|CheckAccess
name|checkAccess
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--ref"
argument_list|,
name|usage
operator|=
literal|"ref name to check permission for"
argument_list|)
DECL|method|addOption (String refName)
name|void
name|addOption
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--account"
argument_list|,
name|usage
operator|=
literal|"account to check acccess for"
argument_list|)
DECL|method|setAccount (String account)
name|void
name|setAccount
parameter_list|(
name|String
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--perm"
argument_list|,
name|usage
operator|=
literal|"permission to check; default: read of any ref."
argument_list|)
DECL|method|setPermission (String perm)
name|void
name|setPermission
parameter_list|(
name|String
name|perm
parameter_list|)
block|{
name|this
operator|.
name|permission
operator|=
name|perm
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc)
specifier|public
name|AccessCheckInfo
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
throws|,
name|PermissionBackendException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|AccessCheckInput
name|input
init|=
operator|new
name|AccessCheckInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
name|refName
expr_stmt|;
name|input
operator|.
name|account
operator|=
name|account
expr_stmt|;
name|input
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
return|return
name|checkAccess
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
return|;
block|}
block|}
end_class

end_unit


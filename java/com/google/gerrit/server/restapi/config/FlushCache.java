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
DECL|package|com.google.gerrit.server.restapi.config
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
name|config
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|FLUSH_CACHES
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
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
name|annotations
operator|.
name|RequiresAnyCapability
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
name|Input
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
name|config
operator|.
name|CacheResource
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

begin_class
annotation|@
name|RequiresAnyCapability
argument_list|(
block|{
name|FLUSH_CACHES
block|,
name|MAINTAIN_SERVER
block|}
argument_list|)
annotation|@
name|Singleton
DECL|class|FlushCache
specifier|public
class|class
name|FlushCache
implements|implements
name|RestModifyView
argument_list|<
name|CacheResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|WEB_SESSIONS
specifier|public
specifier|static
specifier|final
name|String
name|WEB_SESSIONS
init|=
literal|"web_sessions"
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|FlushCache (PermissionBackend permissionBackend)
specifier|public
name|FlushCache
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (CacheResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|CacheResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|WEB_SESSIONS
operator|.
name|equals
argument_list|(
name|rsrc
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|MAINTAIN_SERVER
argument_list|)
expr_stmt|;
block|}
name|rsrc
operator|.
name|getCache
argument_list|()
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
return|;
block|}
block|}
end_class

end_unit


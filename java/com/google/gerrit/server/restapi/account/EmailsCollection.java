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
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|AcceptsCreate
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
name|ChildCollection
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
name|IdString
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
name|RestView
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
name|AccountResource
operator|.
name|Email
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

begin_class
annotation|@
name|Singleton
DECL|class|EmailsCollection
specifier|public
class|class
name|EmailsCollection
implements|implements
name|ChildCollection
argument_list|<
name|AccountResource
argument_list|,
name|AccountResource
operator|.
name|Email
argument_list|>
implements|,
name|AcceptsCreate
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Email
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|GetEmails
name|list
decl_stmt|;
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
DECL|field|createEmailFactory
specifier|private
specifier|final
name|CreateEmail
operator|.
name|Factory
name|createEmailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailsCollection ( DynamicMap<RestView<AccountResource.Email>> views, GetEmails list, Provider<CurrentUser> self, PermissionBackend permissionBackend, CreateEmail.Factory createEmailFactory)
name|EmailsCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Email
argument_list|>
argument_list|>
name|views
parameter_list|,
name|GetEmails
name|list
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|CreateEmail
operator|.
name|Factory
name|createEmailFactory
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
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
name|createEmailFactory
operator|=
name|createEmailFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|AccountResource
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|list
return|;
block|}
annotation|@
name|Override
DECL|method|parse (AccountResource rsrc, IdString id)
specifier|public
name|AccountResource
operator|.
name|Email
name|parse
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|PermissionBackendException
throws|,
name|AuthException
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
literal|"preferred"
operator|.
name|equals
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|email
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|email
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
return|return
operator|new
name|AccountResource
operator|.
name|Email
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|email
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|hasEmailAddress
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|AccountResource
operator|.
name|Email
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|Email
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|create (AccountResource parent, IdString email)
specifier|public
name|CreateEmail
name|create
parameter_list|(
name|AccountResource
name|parent
parameter_list|,
name|IdString
name|email
parameter_list|)
block|{
return|return
name|createEmailFactory
operator|.
name|create
argument_list|(
name|email
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

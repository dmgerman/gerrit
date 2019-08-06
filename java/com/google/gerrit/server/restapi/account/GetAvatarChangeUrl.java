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
name|DynamicItem
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
name|avatar
operator|.
name|AvatarProvider
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
name|Singleton
DECL|class|GetAvatarChangeUrl
specifier|public
class|class
name|GetAvatarChangeUrl
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|avatarProvider
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|AvatarProvider
argument_list|>
name|avatarProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetAvatarChangeUrl (DynamicItem<AvatarProvider> avatarProvider)
name|GetAvatarChangeUrl
parameter_list|(
name|DynamicItem
argument_list|<
name|AvatarProvider
argument_list|>
name|avatarProvider
parameter_list|)
block|{
name|this
operator|.
name|avatarProvider
operator|=
name|avatarProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceNotFoundException
block|{
name|AvatarProvider
name|impl
init|=
name|avatarProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|impl
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
name|String
name|url
init|=
name|impl
operator|.
name|getChangeAvatarUrl
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|url
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|gerrit
operator|.
name|exceptions
operator|.
name|StorageException
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
name|AccountDetailInfo
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
name|server
operator|.
name|account
operator|.
name|AccountDirectory
operator|.
name|FillOptions
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
name|InternalAccountDirectory
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetDetail
specifier|public
class|class
name|GetDetail
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|directory
specifier|private
specifier|final
name|InternalAccountDirectory
name|directory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetDetail (InternalAccountDirectory directory)
specifier|public
name|GetDetail
parameter_list|(
name|InternalAccountDirectory
name|directory
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|directory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|AccountDetailInfo
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
throws|throws
name|StorageException
throws|,
name|PermissionBackendException
block|{
name|Account
name|a
init|=
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|AccountDetailInfo
name|info
init|=
operator|new
name|AccountDetailInfo
argument_list|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|info
operator|.
name|registeredOn
operator|=
name|a
operator|.
name|getRegisteredOn
argument_list|()
expr_stmt|;
name|info
operator|.
name|inactive
operator|=
operator|!
name|a
operator|.
name|isActive
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
name|directory
operator|.
name|fillAccountInfo
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|info
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
name|FillOptions
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit


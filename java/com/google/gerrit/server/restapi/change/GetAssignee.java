begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
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
name|AccountInfo
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
name|AccountLoader
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
name|change
operator|.
name|ChangeResource
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
name|Optional
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetAssignee
specifier|public
class|class
name|GetAssignee
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetAssignee (AccountLoader.Factory accountLoaderFactory)
name|GetAssignee
parameter_list|(
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|)
block|{
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|StorageException
throws|,
name|PermissionBackendException
block|{
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|assignee
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getAssignee
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|assignee
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
operator|.
name|fillOne
argument_list|(
name|assignee
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
end_class

end_unit


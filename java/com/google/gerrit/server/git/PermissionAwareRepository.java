begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|permissions
operator|.
name|PermissionBackend
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
name|RefDatabase
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

begin_comment
comment|/**  * Wrapper around {@link DelegateRepository} that overwrites {@link #getRefDatabase()} to return a  * {@link PermissionAwareReadOnlyRefDatabase}.  */
end_comment

begin_class
DECL|class|PermissionAwareRepository
specifier|public
class|class
name|PermissionAwareRepository
extends|extends
name|DelegateRepository
block|{
DECL|field|permissionAwareReadOnlyRefDatabase
specifier|private
specifier|final
name|PermissionAwareReadOnlyRefDatabase
name|permissionAwareReadOnlyRefDatabase
decl_stmt|;
DECL|method|PermissionAwareRepository (Repository delegate, PermissionBackend.ForProject forProject)
specifier|public
name|PermissionAwareRepository
parameter_list|(
name|Repository
name|delegate
parameter_list|,
name|PermissionBackend
operator|.
name|ForProject
name|forProject
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|permissionAwareReadOnlyRefDatabase
operator|=
operator|new
name|PermissionAwareReadOnlyRefDatabase
argument_list|(
name|delegate
argument_list|,
name|forProject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRefDatabase ()
specifier|public
name|RefDatabase
name|getRefDatabase
parameter_list|()
block|{
return|return
name|permissionAwareReadOnlyRefDatabase
return|;
block|}
block|}
end_class

end_unit


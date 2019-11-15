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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|R_HEADS
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
name|ImmutableSet
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
name|projects
operator|.
name|DeleteBranchesInput
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
name|MethodNotAllowedException
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
name|RefNames
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteBranches
specifier|public
class|class
name|DeleteBranches
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|DeleteBranchesInput
argument_list|>
block|{
DECL|field|deleteRef
specifier|private
specifier|final
name|DeleteRef
name|deleteRef
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteBranches (DeleteRef deleteRef)
name|DeleteBranches
parameter_list|(
name|DeleteRef
name|deleteRef
parameter_list|)
block|{
name|this
operator|.
name|deleteRef
operator|=
name|deleteRef
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource project, DeleteBranchesInput input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|DeleteBranchesInput
name|input
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|RestApiException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|input
operator|==
literal|null
operator|||
name|input
operator|.
name|branches
operator|==
literal|null
operator|||
name|input
operator|.
name|branches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"branches must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|branches
operator|.
name|contains
argument_list|(
name|RefNames
operator|.
name|HEAD
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"not allowed to delete HEAD"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|input
operator|.
name|branches
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|RefNames
operator|::
name|isConfigRef
argument_list|)
condition|)
block|{
comment|// Never allow to delete the meta config branch.
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"not allowed to delete branch "
operator|+
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
throw|;
block|}
name|deleteRef
operator|.
name|deleteMultipleRefs
argument_list|(
name|project
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|input
operator|.
name|branches
argument_list|)
argument_list|,
name|R_HEADS
argument_list|)
expr_stmt|;
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


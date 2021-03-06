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
name|entities
operator|.
name|RefNames
operator|.
name|isConfigRef
import|;
end_import

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
name|gerrit
operator|.
name|entities
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
name|ResourceConflictException
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
name|BranchResource
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
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
DECL|class|DeleteBranch
specifier|public
class|class
name|DeleteBranch
implements|implements
name|RestModifyView
argument_list|<
name|BranchResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
DECL|field|deleteRef
specifier|private
specifier|final
name|DeleteRef
name|deleteRef
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteBranch (Provider<InternalChangeQuery> queryProvider, DeleteRef deleteRef)
name|DeleteBranch
parameter_list|(
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
name|DeleteRef
name|deleteRef
parameter_list|)
block|{
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
name|this
operator|.
name|deleteRef
operator|=
name|deleteRef
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (BranchResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|BranchResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|RefNames
operator|.
name|HEAD
operator|.
name|equals
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|.
name|branch
argument_list|()
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
name|isConfigRef
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|.
name|branch
argument_list|()
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
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|setLimit
argument_list|(
literal|1
argument_list|)
operator|.
name|byBranchOpen
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"branch "
operator|+
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|+
literal|" has open changes"
argument_list|)
throw|;
block|}
name|deleteRef
operator|.
name|deleteSingleRef
argument_list|(
name|rsrc
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getRef
argument_list|()
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


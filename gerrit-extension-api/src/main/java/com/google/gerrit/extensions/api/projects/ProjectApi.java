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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
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
name|common
operator|.
name|ProjectInfo
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
name|NotImplementedException
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

begin_interface
DECL|interface|ProjectApi
specifier|public
interface|interface
name|ProjectApi
block|{
DECL|method|create ()
name|ProjectApi
name|create
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|create (ProjectInput in)
name|ProjectApi
name|create
parameter_list|(
name|ProjectInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|get ()
name|ProjectInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up a branch by refname.    *<p>    *<strong>Note:</strong> This method eagerly reads the branch. Methods that    * mutate the branch do not necessarily re-read the branch. Therefore, calling    * a getter method on an instance after calling a mutation method on that same    * instance is not guaranteed to reflect the mutation. It is not recommended    * to store references to {@code BranchApi} instances.    *    * @param ref branch name, with or without "refs/heads/" prefix.    * @throws RestApiException if a problem occurred reading the project.    * @return API for accessing the branch.    */
DECL|method|branch (String ref)
name|BranchApi
name|branch
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility    * when adding new methods to the interface.    **/
DECL|class|NotImplemented
specifier|public
class|class
name|NotImplemented
implements|implements
name|ProjectApi
block|{
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|ProjectApi
name|create
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|create (ProjectInput in)
specifier|public
name|ProjectApi
name|create
parameter_list|(
name|ProjectInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ProjectInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|branch (String ref)
specifier|public
name|BranchApi
name|branch
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit


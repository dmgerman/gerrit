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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|method|description ()
name|String
name|description
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|description (PutDescriptionInput in)
name|void
name|description
parameter_list|(
name|PutDescriptionInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|branches ()
name|ListBranchesRequest
name|branches
parameter_list|()
function_decl|;
DECL|class|ListBranchesRequest
specifier|public
specifier|abstract
class|class
name|ListBranchesRequest
block|{
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|substring
specifier|private
name|String
name|substring
decl_stmt|;
DECL|field|regex
specifier|private
name|String
name|regex
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|withLimit (int limit)
specifier|public
name|ListBranchesRequest
name|withLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withStart (int start)
specifier|public
name|ListBranchesRequest
name|withStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withSubstring (String substring)
specifier|public
name|ListBranchesRequest
name|withSubstring
parameter_list|(
name|String
name|substring
parameter_list|)
block|{
name|this
operator|.
name|substring
operator|=
name|substring
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withRegex (String regex)
specifier|public
name|ListBranchesRequest
name|withRegex
parameter_list|(
name|String
name|regex
parameter_list|)
block|{
name|this
operator|.
name|regex
operator|=
name|regex
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|getStart ()
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|getSubstring ()
specifier|public
name|String
name|getSubstring
parameter_list|()
block|{
return|return
name|substring
return|;
block|}
DECL|method|getRegex ()
specifier|public
name|String
name|getRegex
parameter_list|()
block|{
return|return
name|regex
return|;
block|}
block|}
DECL|method|children ()
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|children
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|children (boolean recursive)
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|children
parameter_list|(
name|boolean
name|recursive
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|child (String name)
name|ChildProjectApi
name|child
parameter_list|(
name|String
name|name
parameter_list|)
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
DECL|method|description ()
specifier|public
name|String
name|description
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
DECL|method|description (PutDescriptionInput in)
specifier|public
name|void
name|description
parameter_list|(
name|PutDescriptionInput
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
DECL|method|branches ()
specifier|public
name|ListBranchesRequest
name|branches
parameter_list|()
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|children ()
specifier|public
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|children
parameter_list|()
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|children (boolean recursive)
specifier|public
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|children
parameter_list|(
name|boolean
name|recursive
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
DECL|method|child (String name)
specifier|public
name|ChildProjectApi
name|child
parameter_list|(
name|String
name|name
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


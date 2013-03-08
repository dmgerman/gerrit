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
DECL|package|com.google.gerrit.client.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|VoidResult
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
name|client
operator|.
name|rpc
operator|.
name|RestApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|ProjectApi
specifier|public
class|class
name|ProjectApi
block|{
comment|/** Create a new project */
DECL|method|createProject (String projectName, String parent, Boolean createEmptyCcommit, Boolean permissionsOnly, AsyncCallback<VoidResult> asyncCallback)
specifier|public
specifier|static
name|void
name|createProject
parameter_list|(
name|String
name|projectName
parameter_list|,
name|String
name|parent
parameter_list|,
name|Boolean
name|createEmptyCcommit
parameter_list|,
name|Boolean
name|permissionsOnly
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|asyncCallback
parameter_list|)
block|{
name|ProjectInput
name|input
init|=
name|ProjectInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|setName
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|input
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|input
operator|.
name|setPermissionsOnly
argument_list|(
name|permissionsOnly
argument_list|)
expr_stmt|;
name|input
operator|.
name|setCreateEmptyCommit
argument_list|(
name|createEmptyCcommit
argument_list|)
expr_stmt|;
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
argument_list|)
operator|.
name|id
argument_list|(
name|projectName
argument_list|)
operator|.
name|ifNoneMatch
argument_list|()
operator|.
name|put
argument_list|(
name|input
argument_list|,
name|asyncCallback
argument_list|)
expr_stmt|;
block|}
DECL|class|ProjectInput
specifier|private
specifier|static
class|class
name|ProjectInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|static
name|ProjectInput
name|create
parameter_list|()
block|{
return|return
operator|(
name|ProjectInput
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|ProjectInput ()
specifier|protected
name|ProjectInput
parameter_list|()
block|{     }
DECL|method|setName (String n)
specifier|final
specifier|native
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ if(n)this.name=n; }-*/
function_decl|;
DECL|method|setParent (String p)
specifier|final
specifier|native
name|void
name|setParent
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ if(p)this.parent=p; }-*/
function_decl|;
DECL|method|setPermissionsOnly (boolean po)
specifier|final
specifier|native
name|void
name|setPermissionsOnly
parameter_list|(
name|boolean
name|po
parameter_list|)
comment|/*-{ if(po)this.permissions_only=po; }-*/
function_decl|;
DECL|method|setCreateEmptyCommit (boolean cc)
specifier|final
specifier|native
name|void
name|setCreateEmptyCommit
parameter_list|(
name|boolean
name|cc
parameter_list|)
comment|/*-{ if(cc)this.create_empty_commit=cc; }-*/
function_decl|;
block|}
block|}
end_class

end_unit


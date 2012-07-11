begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|rpc
operator|.
name|NativeMap
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
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
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
name|http
operator|.
name|client
operator|.
name|URL
import|;
end_import

begin_comment
comment|/** Projects available from {@code /projects/}. */
end_comment

begin_class
DECL|class|ProjectMap
specifier|public
class|class
name|ProjectMap
extends|extends
name|NativeMap
argument_list|<
name|ProjectInfo
argument_list|>
block|{
DECL|method|all (AsyncCallback<ProjectMap> callback)
specifier|public
specifier|static
name|void
name|all
parameter_list|(
name|AsyncCallback
argument_list|<
name|ProjectMap
argument_list|>
name|callback
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
argument_list|)
operator|.
name|addParameterRaw
argument_list|(
literal|"type"
argument_list|,
literal|"ALL"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"all"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"d"
argument_list|)
comment|// description
operator|.
name|send
argument_list|(
name|NativeMap
operator|.
name|copyKeysIntoChildren
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|permissions (AsyncCallback<ProjectMap> callback)
specifier|public
specifier|static
name|void
name|permissions
parameter_list|(
name|AsyncCallback
argument_list|<
name|ProjectMap
argument_list|>
name|callback
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
argument_list|)
operator|.
name|addParameterRaw
argument_list|(
literal|"type"
argument_list|,
literal|"PERMISSIONS"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"all"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"d"
argument_list|)
comment|// description
operator|.
name|send
argument_list|(
name|NativeMap
operator|.
name|copyKeysIntoChildren
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|parentCandidates (AsyncCallback<ProjectMap> callback)
specifier|public
specifier|static
name|void
name|parentCandidates
parameter_list|(
name|AsyncCallback
argument_list|<
name|ProjectMap
argument_list|>
name|callback
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
argument_list|)
operator|.
name|addParameterRaw
argument_list|(
literal|"type"
argument_list|,
literal|"PARENT_CANDIDATES"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"all"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"d"
argument_list|)
comment|// description
operator|.
name|send
argument_list|(
name|NativeMap
operator|.
name|copyKeysIntoChildren
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|suggest (String prefix, int limit, AsyncCallback<ProjectMap> cb)
specifier|public
specifier|static
name|void
name|suggest
parameter_list|(
name|String
name|prefix
parameter_list|,
name|int
name|limit
parameter_list|,
name|AsyncCallback
argument_list|<
name|ProjectMap
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
operator|+
name|URL
operator|.
name|encode
argument_list|(
name|prefix
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"[?]"
argument_list|,
literal|"%3F"
argument_list|)
argument_list|)
operator|.
name|addParameterRaw
argument_list|(
literal|"type"
argument_list|,
literal|"ALL"
argument_list|)
operator|.
name|addParameter
argument_list|(
literal|"n"
argument_list|,
name|limit
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"d"
argument_list|)
comment|// description
operator|.
name|send
argument_list|(
name|NativeMap
operator|.
name|copyKeysIntoChildren
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectMap ()
specifier|protected
name|ProjectMap
parameter_list|()
block|{   }
block|}
end_class

end_unit


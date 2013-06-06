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
DECL|package|com.google.gerrit.client.access
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|access
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|Set
import|;
end_import

begin_comment
comment|/** Access rights available from {@code /access/}. */
end_comment

begin_class
DECL|class|AccessMap
specifier|public
class|class
name|AccessMap
extends|extends
name|NativeMap
argument_list|<
name|ProjectAccessInfo
argument_list|>
block|{
DECL|method|get (Set<Project.NameKey> projects, AsyncCallback<AccessMap> callback)
specifier|public
specifier|static
name|void
name|get
parameter_list|(
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
parameter_list|,
name|AsyncCallback
argument_list|<
name|AccessMap
argument_list|>
name|callback
parameter_list|)
block|{
name|RestApi
name|api
init|=
operator|new
name|RestApi
argument_list|(
literal|"/access/"
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
name|api
operator|.
name|addParameter
argument_list|(
literal|"project"
argument_list|,
name|p
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|api
operator|.
name|get
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
DECL|method|get (final Project.NameKey project, final AsyncCallback<ProjectAccessInfo> cb)
specifier|public
specifier|static
name|void
name|get
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|ProjectAccessInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|get
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|project
argument_list|)
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|AccessMap
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|AccessMap
name|result
parameter_list|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
operator|.
name|get
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|AccessMap ()
specifier|protected
name|AccessMap
parameter_list|()
block|{   }
block|}
end_class

end_unit


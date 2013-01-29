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
DECL|package|com.google.gerrit.client.dashboards
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|dashboards
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
name|NativeList
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
name|http
operator|.
name|client
operator|.
name|URL
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

begin_comment
comment|/** Project dashboards from {@code /projects/<name>/dashboards/}. */
end_comment

begin_class
DECL|class|DashboardList
specifier|public
class|class
name|DashboardList
extends|extends
name|NativeList
argument_list|<
name|DashboardInfo
argument_list|>
block|{
DECL|method|all (Project.NameKey project, AsyncCallback<NativeList<DashboardList>> callback)
specifier|public
specifier|static
name|void
name|all
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|AsyncCallback
argument_list|<
name|NativeList
argument_list|<
name|DashboardList
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|base
argument_list|(
name|project
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"inherited"
argument_list|)
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|getDefault (Project.NameKey project, AsyncCallback<DashboardInfo> callback)
specifier|public
specifier|static
name|void
name|getDefault
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|AsyncCallback
argument_list|<
name|DashboardInfo
argument_list|>
name|callback
parameter_list|)
block|{
name|base
argument_list|(
name|project
argument_list|)
operator|.
name|view
argument_list|(
literal|"default"
argument_list|)
operator|.
name|addParameterTrue
argument_list|(
literal|"inherited"
argument_list|)
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|get (Project.NameKey project, String id, AsyncCallback<DashboardInfo> callback)
specifier|public
specifier|static
name|void
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|id
parameter_list|,
name|AsyncCallback
argument_list|<
name|DashboardInfo
argument_list|>
name|callback
parameter_list|)
block|{
name|base
argument_list|(
name|project
argument_list|)
operator|.
name|idRaw
argument_list|(
name|encodeDashboardId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|base (Project.NameKey project)
specifier|private
specifier|static
name|RestApi
name|base
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
return|return
operator|new
name|RestApi
argument_list|(
literal|"/projects/"
argument_list|)
operator|.
name|id
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|view
argument_list|(
literal|"dashboards"
argument_list|)
return|;
block|}
DECL|method|encodeDashboardId (String id)
specifier|private
specifier|static
name|String
name|encodeDashboardId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|int
name|c
init|=
name|id
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|c
condition|)
block|{
name|String
name|ref
init|=
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|id
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|id
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|ref
operator|+
literal|':'
operator|+
name|path
return|;
block|}
return|return
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|DashboardList ()
specifier|protected
name|DashboardList
parameter_list|()
block|{   }
block|}
end_class

end_unit


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
comment|/** Dashboards available from {@code /dashboards/}. */
end_comment

begin_class
DECL|class|DashboardMap
specifier|public
class|class
name|DashboardMap
extends|extends
name|NativeMap
argument_list|<
name|DashboardInfo
argument_list|>
block|{
DECL|method|allOnProject (Project.NameKey project, AsyncCallback<DashboardMap> callback)
specifier|public
specifier|static
name|void
name|allOnProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|AsyncCallback
argument_list|<
name|DashboardMap
argument_list|>
name|callback
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/dashboards/project/"
operator|+
name|URL
operator|.
name|encode
argument_list|(
name|project
operator|.
name|get
argument_list|()
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
DECL|method|projectDefault (Project.NameKey project, boolean mine, AsyncCallback<DashboardMap> callback)
specifier|public
specifier|static
name|void
name|projectDefault
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|boolean
name|mine
parameter_list|,
name|AsyncCallback
argument_list|<
name|DashboardMap
argument_list|>
name|callback
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/dashboards/project/"
operator|+
name|URL
operator|.
name|encode
argument_list|(
name|project
operator|.
name|get
argument_list|()
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
name|addParameterTrue
argument_list|(
literal|"default"
argument_list|)
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
DECL|method|DashboardMap ()
specifier|protected
name|DashboardMap
parameter_list|()
block|{   }
block|}
end_class

end_unit


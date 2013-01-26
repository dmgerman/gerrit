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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|restapi
operator|.
name|AuthException
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
name|project
operator|.
name|DeleteDashboard
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

begin_class
DECL|class|DeleteDashboard
class|class
name|DeleteDashboard
implements|implements
name|RestModifyView
argument_list|<
name|DashboardResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{
DECL|field|commitMessage
name|String
name|commitMessage
decl_stmt|;
block|}
DECL|field|defaultSetter
specifier|private
specifier|final
name|Provider
argument_list|<
name|SetDefaultDashboard
argument_list|>
name|defaultSetter
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteDashboard (Provider<SetDefaultDashboard> defaultSetter)
name|DeleteDashboard
parameter_list|(
name|Provider
argument_list|<
name|SetDefaultDashboard
argument_list|>
name|defaultSetter
parameter_list|)
block|{
name|this
operator|.
name|defaultSetter
operator|=
name|defaultSetter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (DashboardResource resource, Input input)
specifier|public
name|Object
name|apply
parameter_list|(
name|DashboardResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|Exception
block|{
if|if
condition|(
name|resource
operator|.
name|isProjectDefault
argument_list|()
condition|)
block|{
name|SetDashboard
operator|.
name|Input
name|in
init|=
operator|new
name|SetDashboard
operator|.
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|commitMessage
operator|=
name|input
operator|!=
literal|null
condition|?
name|input
operator|.
name|commitMessage
else|:
literal|null
expr_stmt|;
return|return
name|defaultSetter
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|resource
argument_list|,
name|in
argument_list|)
return|;
block|}
comment|// TODO: Implement delete of dashboards by API.
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit


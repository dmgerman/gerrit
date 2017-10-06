begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|KILL_TASK
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
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
name|annotations
operator|.
name|RequiresAnyCapability
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
name|git
operator|.
name|WorkQueue
operator|.
name|Task
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

begin_class
annotation|@
name|Singleton
annotation|@
name|RequiresAnyCapability
argument_list|(
block|{
name|KILL_TASK
block|,
name|MAINTAIN_SERVER
block|}
argument_list|)
DECL|class|DeleteTask
specifier|public
class|class
name|DeleteTask
implements|implements
name|RestModifyView
argument_list|<
name|TaskResource
argument_list|,
name|Input
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (TaskResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|TaskResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
name|Task
argument_list|<
name|?
argument_list|>
name|task
init|=
name|rsrc
operator|.
name|getTask
argument_list|()
decl_stmt|;
name|boolean
name|taskDeleted
init|=
name|task
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
decl_stmt|;
return|return
name|taskDeleted
condition|?
name|Response
operator|.
name|none
argument_list|()
else|:
name|Response
operator|.
name|withStatusCode
argument_list|(
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
literal|"Unable to kill task "
operator|+
name|task
argument_list|)
return|;
block|}
block|}
end_class

end_unit


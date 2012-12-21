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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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

begin_comment
comment|/**  * Common class for notifications that are related to a project and branch  */
end_comment

begin_class
DECL|class|NotificationEmail
specifier|public
specifier|abstract
class|class
name|NotificationEmail
extends|extends
name|OutgoingEmail
block|{
DECL|field|project
specifier|protected
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|branch
specifier|protected
name|Branch
operator|.
name|NameKey
name|branch
decl_stmt|;
DECL|method|NotificationEmail (EmailArguments ea, String anonymousCowardName, String mc, Project.NameKey project, Branch.NameKey branch)
specifier|protected
name|NotificationEmail
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|String
name|anonymousCowardName
parameter_list|,
name|String
name|mc
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|anonymousCowardName
argument_list|,
name|mc
argument_list|)
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setupVelocityContext ()
specifier|protected
name|void
name|setupVelocityContext
parameter_list|()
block|{
name|super
operator|.
name|setupVelocityContext
argument_list|()
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"projectName"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"branch"
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


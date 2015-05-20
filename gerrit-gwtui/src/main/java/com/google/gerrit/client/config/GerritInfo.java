begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|config
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_class
DECL|class|GerritInfo
specifier|public
class|class
name|GerritInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|allProjectsNameKey ()
specifier|public
specifier|final
name|Project
operator|.
name|NameKey
name|allProjectsNameKey
parameter_list|()
block|{
return|return
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|allProjects
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isAllProjects (Project.NameKey p)
specifier|public
specifier|final
name|boolean
name|isAllProjects
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
name|allProjectsNameKey
argument_list|()
operator|.
name|equals
argument_list|(
name|p
argument_list|)
return|;
block|}
DECL|method|allUsersNameKey ()
specifier|public
specifier|final
name|Project
operator|.
name|NameKey
name|allUsersNameKey
parameter_list|()
block|{
return|return
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|allUsers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isAllUsers (Project.NameKey p)
specifier|public
specifier|final
name|boolean
name|isAllUsers
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
name|allUsersNameKey
argument_list|()
operator|.
name|equals
argument_list|(
name|p
argument_list|)
return|;
block|}
DECL|method|allProjects ()
specifier|public
specifier|final
specifier|native
name|String
name|allProjects
parameter_list|()
comment|/*-{ return this.all_projects; }-*/
function_decl|;
DECL|method|allUsers ()
specifier|public
specifier|final
specifier|native
name|String
name|allUsers
parameter_list|()
comment|/*-{ return this.all_users; }-*/
function_decl|;
DECL|method|GerritInfo ()
specifier|protected
name|GerritInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


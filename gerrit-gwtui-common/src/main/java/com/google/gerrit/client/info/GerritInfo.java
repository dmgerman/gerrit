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
DECL|package|com.google.gerrit.client.info
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|info
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
name|client
operator|.
name|UiType
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
DECL|method|docSearch ()
specifier|public
specifier|final
specifier|native
name|boolean
name|docSearch
parameter_list|()
comment|/*-{ return this.doc_search; }-*/
function_decl|;
DECL|method|docUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|docUrl
parameter_list|()
comment|/*-{ return this.doc_url; }-*/
function_decl|;
DECL|method|editGpgKeys ()
specifier|public
specifier|final
specifier|native
name|boolean
name|editGpgKeys
parameter_list|()
comment|/*-{ return this.edit_gpg_keys || false; }-*/
function_decl|;
DECL|method|reportBugUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|reportBugUrl
parameter_list|()
comment|/*-{ return this.report_bug_url; }-*/
function_decl|;
DECL|method|reportBugText ()
specifier|public
specifier|final
specifier|native
name|String
name|reportBugText
parameter_list|()
comment|/*-{ return this.report_bug_text; }-*/
function_decl|;
DECL|method|_webUis ()
specifier|private
specifier|native
name|JsArrayString
name|_webUis
parameter_list|()
comment|/*-{ return this.web_uis; }-*/
function_decl|;
DECL|method|webUis ()
specifier|public
specifier|final
name|List
argument_list|<
name|UiType
argument_list|>
name|webUis
parameter_list|()
block|{
name|JsArrayString
name|webUis
init|=
name|_webUis
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|UiType
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|webUis
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|webUis
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|UiType
name|t
init|=
name|UiType
operator|.
name|parse
argument_list|(
name|webUis
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|GerritInfo ()
specifier|protected
name|GerritInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


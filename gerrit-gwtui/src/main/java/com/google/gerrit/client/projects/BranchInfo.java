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
name|WebLinkInfo
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
name|actions
operator|.
name|ActionInfo
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|JsArray
import|;
end_import

begin_class
DECL|class|BranchInfo
specifier|public
class|class
name|BranchInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|getShortName ()
specifier|public
specifier|final
name|String
name|getShortName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|shortName
argument_list|(
name|ref
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ref ()
specifier|public
specifier|final
specifier|native
name|String
name|ref
parameter_list|()
comment|/*-{ return this.ref; }-*/
function_decl|;
DECL|method|revision ()
specifier|public
specifier|final
specifier|native
name|String
name|revision
parameter_list|()
comment|/*-{ return this.revision; }-*/
function_decl|;
DECL|method|canDelete ()
specifier|public
specifier|final
specifier|native
name|boolean
name|canDelete
parameter_list|()
comment|/*-{ return this['can_delete'] ? true : false; }-*/
function_decl|;
DECL|method|actions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|()
comment|/*-{ return this.actions }-*/
function_decl|;
DECL|method|web_links ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|WebLinkInfo
argument_list|>
name|web_links
parameter_list|()
comment|/*-{ return this.web_links; }-*/
function_decl|;
DECL|method|BranchInfo ()
specifier|protected
name|BranchInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


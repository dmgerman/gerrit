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
DECL|class|GitwebTypeInfo
specifier|public
class|class
name|GitwebTypeInfo
extends|extends
name|JavaScriptObject
block|{
comment|/**    * Replace the standard path separator ('/') in a branch name or project    * name with a custom path separator configured by the property    * gitweb.pathSeparator.    * @param urlSegment The branch or project to replace the path separator in    * @return the urlSegment with the standard path separator replaced by the    * custom path separator    */
DECL|method|replacePathSeparator (String urlSegment)
specifier|public
specifier|final
name|String
name|replacePathSeparator
parameter_list|(
name|String
name|urlSegment
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"/"
operator|.
name|equals
argument_list|(
name|pathSeparator
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|urlSegment
operator|.
name|replace
argument_list|(
literal|"/"
argument_list|,
name|pathSeparator
argument_list|()
argument_list|)
return|;
block|}
return|return
name|urlSegment
return|;
block|}
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
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
DECL|method|project ()
specifier|public
specifier|final
specifier|native
name|String
name|project
parameter_list|()
comment|/*-{ return this.project; }-*/
function_decl|;
DECL|method|branch ()
specifier|public
specifier|final
specifier|native
name|String
name|branch
parameter_list|()
comment|/*-{ return this.branch; }-*/
function_decl|;
DECL|method|rootTree ()
specifier|public
specifier|final
specifier|native
name|String
name|rootTree
parameter_list|()
comment|/*-{ return this.root_tree; }-*/
function_decl|;
DECL|method|file ()
specifier|public
specifier|final
specifier|native
name|String
name|file
parameter_list|()
comment|/*-{ return this.file; }-*/
function_decl|;
DECL|method|fileHistory ()
specifier|public
specifier|final
specifier|native
name|String
name|fileHistory
parameter_list|()
comment|/*-{ return this.file_history; }-*/
function_decl|;
DECL|method|pathSeparator ()
specifier|public
specifier|final
specifier|native
name|String
name|pathSeparator
parameter_list|()
comment|/*-{ return this.path_separator; }-*/
function_decl|;
DECL|method|linkDrafts ()
specifier|public
specifier|final
specifier|native
name|boolean
name|linkDrafts
parameter_list|()
comment|/*-{ return this.link_drafts || false; }-*/
function_decl|;
DECL|method|urlEncode ()
specifier|public
specifier|final
specifier|native
name|boolean
name|urlEncode
parameter_list|()
comment|/*-{ return this.url_encode || false; }-*/
function_decl|;
DECL|method|GitwebTypeInfo ()
specifier|protected
name|GitwebTypeInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
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
name|resources
operator|.
name|client
operator|.
name|ClientBundle
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
name|resources
operator|.
name|client
operator|.
name|CssResource
import|;
end_import

begin_comment
comment|/**  * Resources used by diff.  */
end_comment

begin_interface
DECL|interface|CommentBoxResources
interface|interface
name|CommentBoxResources
extends|extends
name|ClientBundle
block|{
annotation|@
name|Source
argument_list|(
literal|"CommentBoxUi.css"
argument_list|)
DECL|method|style ()
name|Style
name|style
parameter_list|()
function_decl|;
DECL|interface|Style
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|open ()
name|String
name|open
parameter_list|()
function_decl|;
DECL|method|close ()
name|String
name|close
parameter_list|()
function_decl|;
DECL|method|commentBox ()
name|String
name|commentBox
parameter_list|()
function_decl|;
DECL|method|table ()
name|String
name|table
parameter_list|()
function_decl|;
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
DECL|method|summary ()
name|String
name|summary
parameter_list|()
function_decl|;
DECL|method|summaryText ()
name|String
name|summaryText
parameter_list|()
function_decl|;
DECL|method|date ()
name|String
name|date
parameter_list|()
function_decl|;
DECL|method|contentPanel ()
name|String
name|contentPanel
parameter_list|()
function_decl|;
DECL|method|message ()
name|String
name|message
parameter_list|()
function_decl|;
DECL|method|button ()
name|String
name|button
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit


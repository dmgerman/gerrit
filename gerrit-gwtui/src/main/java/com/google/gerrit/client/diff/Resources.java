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
name|core
operator|.
name|client
operator|.
name|GWT
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
name|ImageResource
import|;
end_import

begin_comment
comment|/** Resources used by diff. */
end_comment

begin_interface
DECL|interface|Resources
interface|interface
name|Resources
extends|extends
name|ClientBundle
block|{
DECL|field|I
specifier|static
specifier|final
name|Resources
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Resources
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|style ()
annotation|@
name|Source
argument_list|(
literal|"CommentBoxUi.css"
argument_list|)
name|Style
name|style
parameter_list|()
function_decl|;
DECL|method|goPrev ()
annotation|@
name|Source
argument_list|(
literal|"goPrev.png"
argument_list|)
name|ImageResource
name|goPrev
parameter_list|()
function_decl|;
DECL|method|goNext ()
annotation|@
name|Source
argument_list|(
literal|"goNext.png"
argument_list|)
name|ImageResource
name|goNext
parameter_list|()
function_decl|;
DECL|method|goUp ()
annotation|@
name|Source
argument_list|(
literal|"goUp.png"
argument_list|)
name|ImageResource
name|goUp
parameter_list|()
function_decl|;
DECL|interface|Style
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|commentWidgets ()
name|String
name|commentWidgets
parameter_list|()
function_decl|;
DECL|method|commentBox ()
name|String
name|commentBox
parameter_list|()
function_decl|;
DECL|method|contents ()
name|String
name|contents
parameter_list|()
function_decl|;
DECL|method|message ()
name|String
name|message
parameter_list|()
function_decl|;
DECL|method|header ()
name|String
name|header
parameter_list|()
function_decl|;
DECL|method|summary ()
name|String
name|summary
parameter_list|()
function_decl|;
DECL|method|date ()
name|String
name|date
parameter_list|()
function_decl|;
DECL|method|goPrev ()
name|String
name|goPrev
parameter_list|()
function_decl|;
DECL|method|goNext ()
name|String
name|goNext
parameter_list|()
function_decl|;
DECL|method|goUp ()
name|String
name|goUp
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit


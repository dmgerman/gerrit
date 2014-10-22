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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Anchor
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Image
import|;
end_import

begin_class
DECL|class|WebLinkInfo
specifier|public
class|class
name|WebLinkInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|imageUrl ()
specifier|public
specifier|final
specifier|native
name|String
name|imageUrl
parameter_list|()
comment|/*-{ return this.image_url; }-*/
function_decl|;
DECL|method|url ()
specifier|public
specifier|final
specifier|native
name|String
name|url
parameter_list|()
comment|/*-{ return this.url; }-*/
function_decl|;
DECL|method|target ()
specifier|public
specifier|final
specifier|native
name|String
name|target
parameter_list|()
comment|/*-{ return this.target; }-*/
function_decl|;
DECL|method|WebLinkInfo ()
specifier|protected
name|WebLinkInfo
parameter_list|()
block|{   }
DECL|method|toAnchor ()
specifier|public
specifier|final
name|Anchor
name|toAnchor
parameter_list|()
block|{
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|url
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|target
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|target
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|a
operator|.
name|setTarget
argument_list|(
name|target
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|imageUrl
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|imageUrl
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Image
name|img
init|=
operator|new
name|Image
argument_list|()
decl_stmt|;
name|img
operator|.
name|setAltText
argument_list|(
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|img
operator|.
name|setUrl
argument_list|(
name|imageUrl
argument_list|()
argument_list|)
expr_stmt|;
name|img
operator|.
name|setTitle
argument_list|(
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|img
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|a
operator|.
name|setText
argument_list|(
literal|"("
operator|+
name|name
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|a
return|;
block|}
block|}
end_class

end_unit


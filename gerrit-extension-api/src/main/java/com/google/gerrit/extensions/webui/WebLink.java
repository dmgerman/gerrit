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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
package|;
end_package

begin_interface
DECL|interface|WebLink
specifier|public
interface|interface
name|WebLink
block|{
DECL|class|Target
specifier|public
specifier|static
class|class
name|Target
block|{
DECL|field|BLANK
specifier|public
specifier|final
specifier|static
name|String
name|BLANK
init|=
literal|"_blank"
decl_stmt|;
DECL|field|SELF
specifier|public
specifier|final
specifier|static
name|String
name|SELF
init|=
literal|"_self"
decl_stmt|;
DECL|field|PARENT
specifier|public
specifier|final
specifier|static
name|String
name|PARENT
init|=
literal|"_parent"
decl_stmt|;
DECL|field|TOP
specifier|public
specifier|final
specifier|static
name|String
name|TOP
init|=
literal|"_top"
decl_stmt|;
block|}
comment|/**    * The link-name displayed in UI.    *    * @return name of link or title of the link if image URL is available.    */
DECL|method|getLinkName ()
name|String
name|getLinkName
parameter_list|()
function_decl|;
comment|/**    * URL of image to be displayed    *    * @return URL to image for link or null for using a text-only link.    * Recommended image size is 16x16.    */
DECL|method|getImageUrl ()
name|String
name|getImageUrl
parameter_list|()
function_decl|;
comment|/**    * Target window in which the link should be opened (e.g. "_blank", "_self".).    *    * @return link target, if null the link is opened in the current window    */
DECL|method|getTarget ()
name|String
name|getTarget
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


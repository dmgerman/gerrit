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
DECL|package|com.google.gerrit.server.avatar
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|avatar
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
name|annotations
operator|.
name|ExtensionPoint
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
name|NotImplementedException
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
name|IdentifiedUser
import|;
end_import

begin_comment
comment|/**  * Provide avatar URLs for specified user.  *  *<p>Invoked by Gerrit when Avatar image requests are made.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|AvatarProvider
specifier|public
interface|interface
name|AvatarProvider
block|{
comment|/**    * Get avatar URL.    *    * @param forUser The user for which to load an avatar image    * @param imageSize A requested image size, in pixels. An imageSize of 0 indicates to use whatever    *     default size the provider determines. AvatarProviders may ignore the requested image size.    *     The web interface will resize any image to match imageSize, so ideally the provider should    *     return an image sized correctly.    * @return a URL of an avatar image for the specified user. A return value of {@code null} is    *     acceptable, and results in the server responding with a 404. This will hide the avatar    *     image in the web UI.    */
DECL|method|getUrl (IdentifiedUser forUser, int imageSize)
name|String
name|getUrl
parameter_list|(
name|IdentifiedUser
name|forUser
parameter_list|,
name|int
name|imageSize
parameter_list|)
function_decl|;
comment|/**    * Gets a URL for a user to modify their avatar image.    *    * @param forUser The user wishing to change their avatar image    * @return a URL the user should visit to modify their avatar, or null if modification is not    *     possible.    */
DECL|method|getChangeAvatarUrl (IdentifiedUser forUser)
name|String
name|getChangeAvatarUrl
parameter_list|(
name|IdentifiedUser
name|forUser
parameter_list|)
function_decl|;
comment|/**    * Set the avatar image URL for specified user and specified size.    *    *<p>It is the default method (not interface method declaration) for back compatibility with old    * code.    *    * @param forUser The user for which need to change the avatar image.    * @param url The avatar image URL for the specified user.    * @param imageSize The avatar image size in pixels. If imageSize have a zero value this indicates    *     to set URL for default size that provider determines.    * @throws Exception if an error occurred.    */
DECL|method|setUrl (IdentifiedUser forUser, String url, int imageSize)
specifier|default
name|void
name|setUrl
parameter_list|(
name|IdentifiedUser
name|forUser
parameter_list|,
name|String
name|url
parameter_list|,
name|int
name|imageSize
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
comment|/**    * Indicates whether or not the provider allows to set the image URL.    *    *<p>It is the default method (not interface method declaration) for back compatibility with old    * code.    *    * @return    *<ul>    *<li>true - avatar image URL could be set.    *<li>false - avatar image URL could not be set (for example not Implemented).    *</ul>    */
DECL|method|canSetUrl ()
specifier|default
name|boolean
name|canSetUrl
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_interface

end_unit


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
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|RestApi
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
name|Account
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ErrorEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ErrorHandler
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
DECL|class|AvatarImage
specifier|public
class|class
name|AvatarImage
extends|extends
name|Image
block|{
comment|/** A default sized avatar image. */
DECL|method|AvatarImage (Account.Id account)
specifier|public
name|AvatarImage
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
block|{
name|this
argument_list|(
name|account
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**    * An avatar image for the given account using the requested size.    *    * @param account The account in which we are interested    * @param size A requested size. Note that the size can be ignored depending    *        on the avatar provider. A size<= 0 indicates to let the provider    *        decide a default size.    */
DECL|method|AvatarImage (Account.Id account, int size)
specifier|public
name|AvatarImage
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|url
argument_list|(
name|account
argument_list|,
name|size
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
comment|// If the provider does not resize the image, force it in the browser.
name|setSize
argument_list|(
name|size
operator|+
literal|"px"
argument_list|,
name|size
operator|+
literal|"px"
argument_list|)
expr_stmt|;
block|}
name|addErrorHandler
argument_list|(
operator|new
name|ErrorHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|ErrorEvent
name|event
parameter_list|)
block|{
comment|// We got a 404, don't bother showing the image. Either the user doesn't
comment|// have an avatar or there is no avatar provider plugin installed.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|url (Account.Id id, int size)
specifier|private
specifier|static
name|String
name|url
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|String
name|u
decl_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|u
operator|=
literal|"self"
expr_stmt|;
block|}
else|else
block|{
name|u
operator|=
name|id
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|RestApi
name|api
init|=
operator|new
name|RestApi
argument_list|(
literal|"/accounts/"
argument_list|)
operator|.
name|id
argument_list|(
name|u
argument_list|)
operator|.
name|view
argument_list|(
literal|"avatar"
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|api
operator|.
name|addParameter
argument_list|(
literal|"s"
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
return|return
name|api
operator|.
name|url
argument_list|()
return|;
block|}
block|}
end_class

end_unit


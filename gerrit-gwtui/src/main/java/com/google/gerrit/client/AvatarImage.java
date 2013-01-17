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
comment|/**    * An avatar image for the given account using a default size decided by the    * avatar provider    */
DECL|method|AvatarImage (Account account)
specifier|public
name|AvatarImage
parameter_list|(
name|Account
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
DECL|method|AvatarImage (Account account, int size)
specifier|public
name|AvatarImage
parameter_list|(
name|Account
name|account
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
literal|"/avatar/"
operator|+
name|account
operator|.
name|getId
argument_list|()
operator|+
operator|(
name|size
operator|>
literal|0
condition|?
operator|(
literal|"?size="
operator|+
name|size
operator|)
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit


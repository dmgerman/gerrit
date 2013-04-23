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
name|account
operator|.
name|AccountInfo
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
name|RestApi
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|MouseOutEvent
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
name|MouseOutHandler
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
name|MouseOverEvent
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
name|MouseOverHandler
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
name|Timer
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
name|UIObject
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
DECL|method|AvatarImage (AccountInfo account)
specifier|public
name|AvatarImage
parameter_list|(
name|AccountInfo
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
DECL|method|AvatarImage (AccountInfo account, int size)
specifier|public
name|AvatarImage
parameter_list|(
name|AccountInfo
name|account
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
argument_list|(
name|account
argument_list|,
name|size
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**    * An avatar image for the given account using the requested size.    *    * @param account The account in which we are interested    * @param size A requested size. Note that the size can be ignored depending    *        on the avatar provider. A size<= 0 indicates to let the provider    *        decide a default size.    * @param addPopup show avatar popup with user info on hovering over the    *        avatar image    */
DECL|method|AvatarImage (AccountInfo account, int size, boolean addPopup)
specifier|public
name|AvatarImage
parameter_list|(
name|AccountInfo
name|account
parameter_list|,
name|int
name|size
parameter_list|,
name|boolean
name|addPopup
parameter_list|)
block|{
name|super
argument_list|(
name|url
argument_list|(
name|account
operator|.
name|email
argument_list|()
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
if|if
condition|(
name|addPopup
condition|)
block|{
name|UserPopupPanel
name|userPopup
init|=
operator|new
name|UserPopupPanel
argument_list|(
name|account
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|PopupHandler
name|popupHandler
init|=
operator|new
name|PopupHandler
argument_list|(
name|userPopup
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|addMouseOverHandler
argument_list|(
name|popupHandler
argument_list|)
expr_stmt|;
name|addMouseOutHandler
argument_list|(
name|popupHandler
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|url (String email, int size)
specifier|private
specifier|static
name|String
name|url
parameter_list|(
name|String
name|email
parameter_list|,
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|email
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
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
name|email
operator|.
name|equals
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getPreferredEmail
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
name|email
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
DECL|class|PopupHandler
specifier|private
class|class
name|PopupHandler
implements|implements
name|MouseOverHandler
implements|,
name|MouseOutHandler
block|{
DECL|field|popup
specifier|private
specifier|final
name|UserPopupPanel
name|popup
decl_stmt|;
DECL|field|target
specifier|private
specifier|final
name|UIObject
name|target
decl_stmt|;
DECL|field|showTimer
specifier|private
name|Timer
name|showTimer
decl_stmt|;
DECL|field|hideTimer
specifier|private
name|Timer
name|hideTimer
decl_stmt|;
DECL|method|PopupHandler (UserPopupPanel popup, UIObject target)
specifier|public
name|PopupHandler
parameter_list|(
name|UserPopupPanel
name|popup
parameter_list|,
name|UIObject
name|target
parameter_list|)
block|{
name|this
operator|.
name|popup
operator|=
name|popup
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|popup
operator|.
name|addDomHandler
argument_list|(
operator|new
name|MouseOverHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMouseOver
parameter_list|(
name|MouseOverEvent
name|event
parameter_list|)
block|{
name|scheduleShow
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|MouseOverEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|addDomHandler
argument_list|(
operator|new
name|MouseOutHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMouseOut
parameter_list|(
name|MouseOutEvent
name|event
parameter_list|)
block|{
name|scheduleHide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|MouseOutEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMouseOver (MouseOverEvent event)
specifier|public
name|void
name|onMouseOver
parameter_list|(
name|MouseOverEvent
name|event
parameter_list|)
block|{
name|scheduleShow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMouseOut (MouseOutEvent event)
specifier|public
name|void
name|onMouseOut
parameter_list|(
name|MouseOutEvent
name|event
parameter_list|)
block|{
name|scheduleHide
argument_list|()
expr_stmt|;
block|}
DECL|method|scheduleShow ()
specifier|private
name|void
name|scheduleShow
parameter_list|()
block|{
if|if
condition|(
name|hideTimer
operator|!=
literal|null
condition|)
block|{
name|hideTimer
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|hideTimer
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|popup
operator|.
name|isShowing
argument_list|()
operator|&&
name|popup
operator|.
name|isVisible
argument_list|()
operator|)
operator|||
name|showTimer
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|showTimer
operator|=
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|popup
operator|.
name|isShowing
argument_list|()
operator|||
operator|!
name|popup
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|popup
operator|.
name|showRelativeTo
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|showTimer
operator|.
name|schedule
argument_list|(
literal|600
argument_list|)
expr_stmt|;
block|}
DECL|method|scheduleHide ()
specifier|private
name|void
name|scheduleHide
parameter_list|()
block|{
if|if
condition|(
name|showTimer
operator|!=
literal|null
condition|)
block|{
name|showTimer
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|showTimer
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|popup
operator|.
name|isShowing
argument_list|()
operator|||
operator|!
name|popup
operator|.
name|isVisible
argument_list|()
operator|||
name|hideTimer
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|hideTimer
operator|=
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|popup
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|hideTimer
operator|.
name|schedule
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


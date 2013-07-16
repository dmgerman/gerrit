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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|ErrorDialog
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
name|Gerrit
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
name|changes
operator|.
name|ChangeApi
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
name|changes
operator|.
name|ChangeInfo
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
name|NativeString
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
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
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
name|Change
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|ClickHandler
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
name|Window
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
name|rpc
operator|.
name|AsyncCallback
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
name|Button
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_class
DECL|class|ActionButton
class|class
name|ActionButton
extends|extends
name|Button
implements|implements
name|ClickHandler
block|{
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
DECL|field|action
specifier|private
specifier|final
name|ActionInfo
name|action
decl_stmt|;
DECL|method|ActionButton (Change.Id changeId, ActionInfo action)
name|ActionButton
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ActionInfo
name|action
parameter_list|)
block|{
name|this
argument_list|(
name|changeId
argument_list|,
literal|null
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
DECL|method|ActionButton (Change.Id changeId, String revision, ActionInfo action)
name|ActionButton
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|revision
parameter_list|,
name|ActionInfo
name|action
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|openDiv
argument_list|()
operator|.
name|append
argument_list|(
name|action
operator|.
name|label
argument_list|()
argument_list|)
operator|.
name|closeDiv
argument_list|()
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
name|action
operator|.
name|title
argument_list|()
argument_list|)
expr_stmt|;
name|setEnabled
argument_list|(
name|action
operator|.
name|enabled
argument_list|()
argument_list|)
expr_stmt|;
name|addClickHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClick (ClickEvent event)
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AsyncCallback
argument_list|<
name|NativeString
argument_list|>
name|cb
init|=
operator|new
name|AsyncCallback
argument_list|<
name|NativeString
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|new
name|ErrorDialog
argument_list|(
name|caught
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeString
name|msg
parameter_list|)
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
operator|&&
operator|!
name|msg
operator|.
name|asString
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO Support better UI on UiAction results.
name|Window
operator|.
name|alert
argument_list|(
name|msg
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange2
argument_list|(
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|RestApi
name|api
init|=
name|revision
operator|!=
literal|null
condition|?
name|ChangeApi
operator|.
name|revision
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|revision
argument_list|)
else|:
name|ChangeApi
operator|.
name|change
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|api
operator|.
name|view
argument_list|(
name|action
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"PUT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|action
operator|.
name|method
argument_list|()
argument_list|)
condition|)
block|{
name|api
operator|.
name|put
argument_list|(
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DELETE"
operator|.
name|equalsIgnoreCase
argument_list|(
name|action
operator|.
name|method
argument_list|()
argument_list|)
condition|)
block|{
name|api
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|api
operator|.
name|post
argument_list|(
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


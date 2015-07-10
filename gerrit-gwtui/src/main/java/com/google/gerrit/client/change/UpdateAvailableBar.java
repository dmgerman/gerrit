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
name|info
operator|.
name|ChangeInfo
operator|.
name|MessageInfo
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
name|dom
operator|.
name|client
operator|.
name|Element
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|uibinder
operator|.
name|client
operator|.
name|UiHandler
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
name|Composite
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
name|HTMLPanel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Displays the "New Message From ..." panel in bottom right on updates. */
end_comment

begin_class
DECL|class|UpdateAvailableBar
specifier|abstract
class|class
name|UpdateAvailableBar
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|UpdateAvailableBar
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|updated
specifier|private
name|Timestamp
name|updated
decl_stmt|;
DECL|field|author
annotation|@
name|UiField
name|Element
name|author
decl_stmt|;
DECL|field|show
annotation|@
name|UiField
name|Anchor
name|show
decl_stmt|;
DECL|field|ignore
annotation|@
name|UiField
name|Anchor
name|ignore
decl_stmt|;
DECL|method|UpdateAvailableBar ()
name|UpdateAvailableBar
parameter_list|()
block|{
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|set (List<MessageInfo> newMessages, Timestamp newTime)
name|void
name|set
parameter_list|(
name|List
argument_list|<
name|MessageInfo
argument_list|>
name|newMessages
parameter_list|,
name|Timestamp
name|newTime
parameter_list|)
block|{
name|HashSet
argument_list|<
name|Integer
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|MessageInfo
name|m
range|:
name|newMessages
control|)
block|{
name|int
name|a
init|=
name|m
operator|.
name|author
argument_list|()
operator|!=
literal|null
condition|?
name|m
operator|.
name|author
argument_list|()
operator|.
name|_accountId
argument_list|()
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|seen
operator|.
name|add
argument_list|(
name|a
argument_list|)
condition|)
block|{
if|if
condition|(
name|r
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|Message
operator|.
name|authorName
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|author
operator|.
name|setInnerText
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
name|newTime
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"show"
argument_list|)
DECL|method|onShow (@uppressWarningsR) ClickEvent e)
name|void
name|onShow
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|onShow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"ignore"
argument_list|)
DECL|method|onIgnore (@uppressWarningsR) ClickEvent e)
name|void
name|onIgnore
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|onIgnore
argument_list|(
name|updated
argument_list|)
expr_stmt|;
name|removeFromParent
argument_list|()
expr_stmt|;
block|}
DECL|method|onShow ()
specifier|abstract
name|void
name|onShow
parameter_list|()
function_decl|;
DECL|method|onIgnore (Timestamp newTime)
specifier|abstract
name|void
name|onIgnore
parameter_list|(
name|Timestamp
name|newTime
parameter_list|)
function_decl|;
block|}
end_class

end_unit


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
name|ReviewInput
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
name|ReviewInput
operator|.
name|DraftHandling
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
name|info
operator|.
name|ChangeInfo
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
name|info
operator|.
name|ChangeInfo
operator|.
name|LabelInfo
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
name|GerritCallback
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
name|Natives
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|JsArrayString
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

begin_comment
comment|/** Applies a label with one mouse click. */
end_comment

begin_class
DECL|class|QuickApprove
class|class
name|QuickApprove
extends|extends
name|Button
implements|implements
name|ClickHandler
block|{
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|revision
specifier|private
name|String
name|revision
decl_stmt|;
DECL|field|input
specifier|private
name|ReviewInput
name|input
decl_stmt|;
DECL|field|replyAction
specifier|private
name|ReplyAction
name|replyAction
decl_stmt|;
DECL|method|QuickApprove ()
name|QuickApprove
parameter_list|()
block|{
name|addClickHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|set (ChangeInfo info, String commit, ReplyAction action)
name|void
name|set
parameter_list|(
name|ChangeInfo
name|info
parameter_list|,
name|String
name|commit
parameter_list|,
name|ReplyAction
name|action
parameter_list|)
block|{
if|if
condition|(
operator|!
name|info
operator|.
name|hasPermittedLabels
argument_list|()
operator|||
operator|!
name|info
operator|.
name|status
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
comment|// Quick approve needs at least one label on an open change.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|info
operator|.
name|revision
argument_list|(
name|commit
argument_list|)
operator|.
name|isEdit
argument_list|()
operator|||
name|info
operator|.
name|revision
argument_list|(
name|commit
argument_list|)
operator|.
name|draft
argument_list|()
condition|)
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|qName
init|=
literal|null
decl_stmt|;
name|String
name|qValueStr
init|=
literal|null
decl_stmt|;
name|short
name|qValue
init|=
literal|0
decl_stmt|;
name|int
name|index
init|=
name|info
operator|.
name|getMissingLabelIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|LabelInfo
name|label
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|info
operator|.
name|allLabels
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|JsArrayString
name|values
init|=
name|info
operator|.
name|permittedValues
argument_list|(
name|label
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|values
operator|.
name|get
argument_list|(
name|values
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|short
name|v
init|=
name|LabelInfo
operator|.
name|parseValue
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|>
literal|0
operator|&&
name|s
operator|.
name|equals
argument_list|(
name|label
operator|.
name|maxValue
argument_list|()
argument_list|)
condition|)
block|{
name|qName
operator|=
name|label
operator|.
name|name
argument_list|()
expr_stmt|;
name|qValueStr
operator|=
name|s
expr_stmt|;
name|qValue
operator|=
name|v
expr_stmt|;
block|}
block|}
if|if
condition|(
name|qName
operator|!=
literal|null
condition|)
block|{
name|changeId
operator|=
name|info
operator|.
name|legacyId
argument_list|()
expr_stmt|;
name|project
operator|=
name|info
operator|.
name|projectNameKey
argument_list|()
expr_stmt|;
name|revision
operator|=
name|commit
expr_stmt|;
name|input
operator|=
name|ReviewInput
operator|.
name|create
argument_list|()
expr_stmt|;
name|input
operator|.
name|drafts
argument_list|(
name|DraftHandling
operator|.
name|PUBLISH_ALL_REVISIONS
argument_list|)
expr_stmt|;
name|input
operator|.
name|label
argument_list|(
name|qName
argument_list|,
name|qValue
argument_list|)
expr_stmt|;
name|replyAction
operator|=
name|action
expr_stmt|;
name|setText
argument_list|(
name|qName
operator|+
name|qValueStr
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setHTML
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
name|text
argument_list|)
operator|.
name|closeDiv
argument_list|()
argument_list|)
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
if|if
condition|(
name|replyAction
operator|!=
literal|null
operator|&&
name|replyAction
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|replyAction
operator|.
name|quickApprove
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ChangeApi
operator|.
name|revision
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|revision
argument_list|)
operator|.
name|view
argument_list|(
literal|"review"
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ReviewInput
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ReviewInput
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


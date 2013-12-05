begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
name|gerrit
operator|.
name|client
operator|.
name|AvatarImage
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
name|FormatUtil
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
name|CommentApi
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
name|CommentInfo
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
name|CommentInput
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
name|Util
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
name|patches
operator|.
name|PatchUtil
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
name|ui
operator|.
name|CommentLinkProcessor
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
name|PatchSet
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
name|Button
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
name|Widget
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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
import|;
end_import

begin_comment
comment|/** An HtmlPanel for displaying a published comment */
end_comment

begin_class
DECL|class|PublishedBox
class|class
name|PublishedBox
extends|extends
name|CommentBox
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|PublishedBox
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
DECL|interface|Style
specifier|static
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|closed ()
name|String
name|closed
parameter_list|()
function_decl|;
block|}
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|comment
specifier|private
specifier|final
name|CommentInfo
name|comment
decl_stmt|;
DECL|field|replyBox
specifier|private
name|DraftBox
name|replyBox
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
name|Style
name|style
decl_stmt|;
DECL|field|header
annotation|@
name|UiField
name|Widget
name|header
decl_stmt|;
DECL|field|name
annotation|@
name|UiField
name|Element
name|name
decl_stmt|;
DECL|field|summary
annotation|@
name|UiField
name|Element
name|summary
decl_stmt|;
DECL|field|date
annotation|@
name|UiField
name|Element
name|date
decl_stmt|;
DECL|field|message
annotation|@
name|UiField
name|Element
name|message
decl_stmt|;
DECL|field|buttons
annotation|@
name|UiField
name|Element
name|buttons
decl_stmt|;
DECL|field|reply
annotation|@
name|UiField
name|Button
name|reply
decl_stmt|;
DECL|field|done
annotation|@
name|UiField
name|Button
name|done
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|avatar
name|AvatarImage
name|avatar
decl_stmt|;
DECL|method|PublishedBox ( SideBySide2 parent, CodeMirror cm, DisplaySide side, CommentLinkProcessor clp, PatchSet.Id psId, CommentInfo info)
name|PublishedBox
parameter_list|(
name|SideBySide2
name|parent
parameter_list|,
name|CodeMirror
name|cm
parameter_list|,
name|DisplaySide
name|side
parameter_list|,
name|CommentLinkProcessor
name|clp
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|CommentInfo
name|info
parameter_list|)
block|{
name|super
argument_list|(
name|cm
argument_list|,
name|info
argument_list|,
name|side
argument_list|)
expr_stmt|;
name|setDiffScreen
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|comment
operator|=
name|info
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|author
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|avatar
operator|=
operator|new
name|AvatarImage
argument_list|(
name|info
operator|.
name|author
argument_list|()
argument_list|)
expr_stmt|;
name|avatar
operator|.
name|setSize
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|avatar
operator|=
operator|new
name|AvatarImage
argument_list|()
expr_stmt|;
block|}
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
name|header
operator|.
name|addDomHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|setOpen
argument_list|(
operator|!
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|ClickEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|.
name|setInnerText
argument_list|(
name|authorName
argument_list|(
name|info
argument_list|)
argument_list|)
expr_stmt|;
name|date
operator|.
name|setInnerText
argument_list|(
name|FormatUtil
operator|.
name|shortFormatDayTime
argument_list|(
name|info
operator|.
name|updated
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|message
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|msg
init|=
name|info
operator|.
name|message
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|summary
operator|.
name|setInnerText
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|message
operator|.
name|setInnerSafeHtml
argument_list|(
name|clp
operator|.
name|apply
argument_list|(
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|msg
argument_list|)
operator|.
name|wikify
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCommentInfo ()
name|CommentInfo
name|getCommentInfo
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
annotation|@
name|Override
DECL|method|isOpen ()
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|UIObject
operator|.
name|isVisible
argument_list|(
name|message
argument_list|)
return|;
block|}
DECL|method|setOpen (boolean open)
name|void
name|setOpen
parameter_list|(
name|boolean
name|open
parameter_list|)
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|summary
argument_list|,
operator|!
name|open
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|message
argument_list|,
name|open
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|buttons
argument_list|,
name|open
argument_list|)
expr_stmt|;
if|if
condition|(
name|open
condition|)
block|{
name|removeStyleName
argument_list|(
name|style
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addStyleName
argument_list|(
name|style
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setOpen
argument_list|(
name|open
argument_list|)
expr_stmt|;
block|}
DECL|method|registerReplyBox (DraftBox box)
name|void
name|registerReplyBox
parameter_list|(
name|DraftBox
name|box
parameter_list|)
block|{
name|replyBox
operator|=
name|box
expr_stmt|;
name|box
operator|.
name|registerReplyToBox
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|unregisterReplyBox ()
name|void
name|unregisterReplyBox
parameter_list|()
block|{
name|replyBox
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|openReplyBox ()
specifier|private
name|void
name|openReplyBox
parameter_list|()
block|{
name|replyBox
operator|.
name|setOpen
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replyBox
operator|.
name|setEdit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|addReplyBox ()
name|DraftBox
name|addReplyBox
parameter_list|()
block|{
name|DraftBox
name|box
init|=
name|getDiffScreen
argument_list|()
operator|.
name|addDraftBox
argument_list|(
name|getDiffScreen
argument_list|()
operator|.
name|createReply
argument_list|(
name|comment
argument_list|)
argument_list|,
name|getSide
argument_list|()
argument_list|)
decl_stmt|;
name|registerReplyBox
argument_list|(
name|box
argument_list|)
expr_stmt|;
return|return
name|box
return|;
block|}
DECL|method|doReply ()
name|void
name|doReply
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|doSignIn
argument_list|(
name|getDiffScreen
argument_list|()
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|replyBox
operator|==
literal|null
condition|)
block|{
name|DraftBox
name|box
init|=
name|addReplyBox
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|getCommentInfo
argument_list|()
operator|.
name|has_line
argument_list|()
condition|)
block|{
name|getDiffScreen
argument_list|()
operator|.
name|addFileCommentBox
argument_list|(
name|box
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|openReplyBox
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"reply"
argument_list|)
DECL|method|onReply (ClickEvent e)
name|void
name|onReply
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|e
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
name|doReply
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"done"
argument_list|)
DECL|method|onReplyDone (ClickEvent e)
name|void
name|onReplyDone
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|e
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|doSignIn
argument_list|(
name|getDiffScreen
argument_list|()
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|replyBox
operator|==
literal|null
condition|)
block|{
name|done
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CommentInput
name|input
init|=
name|CommentInput
operator|.
name|create
argument_list|(
name|getDiffScreen
argument_list|()
operator|.
name|createReply
argument_list|(
name|comment
argument_list|)
argument_list|)
decl_stmt|;
name|input
operator|.
name|setMessage
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|cannedReplyDone
argument_list|()
argument_list|)
expr_stmt|;
name|CommentApi
operator|.
name|createDraft
argument_list|(
name|psId
argument_list|,
name|input
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|CommentInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|CommentInfo
name|result
parameter_list|)
block|{
name|done
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DraftBox
name|box
init|=
name|getDiffScreen
argument_list|()
operator|.
name|addDraftBox
argument_list|(
name|result
argument_list|,
name|getSide
argument_list|()
argument_list|)
decl_stmt|;
name|registerReplyBox
argument_list|(
name|box
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getCommentInfo
argument_list|()
operator|.
name|has_line
argument_list|()
condition|)
block|{
name|getDiffScreen
argument_list|()
operator|.
name|addFileCommentBox
argument_list|(
name|box
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|openReplyBox
argument_list|()
expr_stmt|;
name|setOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|authorName (CommentInfo info)
specifier|private
specifier|static
name|String
name|authorName
parameter_list|(
name|CommentInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|author
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|info
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|info
operator|.
name|author
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
return|return
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getAnonymousCowardName
argument_list|()
return|;
block|}
return|return
name|Util
operator|.
name|C
operator|.
name|messageNoAuthor
argument_list|()
return|;
block|}
block|}
end_class

end_unit


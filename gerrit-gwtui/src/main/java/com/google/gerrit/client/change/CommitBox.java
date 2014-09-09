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
name|GitwebLink
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
name|WebLinkInfo
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
name|changes
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|CommitInfo
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
name|GitPerson
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
name|RevisionInfo
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
name|client
operator|.
name|ui
operator|.
name|InlineHyperlink
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
name|core
operator|.
name|client
operator|.
name|JsArray
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
name|AnchorElement
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
name|dom
operator|.
name|client
operator|.
name|TableCellElement
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
name|DOM
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
name|FlowPanel
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
name|HTML
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
name|ScrollPanel
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
name|clippy
operator|.
name|client
operator|.
name|CopyableLabel
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
DECL|class|CommitBox
class|class
name|CommitBox
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
name|CommitBox
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
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|collapsed ()
name|String
name|collapsed
parameter_list|()
function_decl|;
DECL|method|expanded ()
name|String
name|expanded
parameter_list|()
function_decl|;
DECL|method|clippy ()
name|String
name|clippy
parameter_list|()
function_decl|;
DECL|method|parentWebLink ()
name|String
name|parentWebLink
parameter_list|()
function_decl|;
block|}
DECL|field|style
annotation|@
name|UiField
name|Style
name|style
decl_stmt|;
DECL|field|authorPanel
annotation|@
name|UiField
name|FlowPanel
name|authorPanel
decl_stmt|;
DECL|field|committerPanel
annotation|@
name|UiField
name|FlowPanel
name|committerPanel
decl_stmt|;
DECL|field|mergeCommit
annotation|@
name|UiField
name|Image
name|mergeCommit
decl_stmt|;
DECL|field|commitName
annotation|@
name|UiField
name|CopyableLabel
name|commitName
decl_stmt|;
DECL|field|webLinkCell
annotation|@
name|UiField
name|TableCellElement
name|webLinkCell
decl_stmt|;
DECL|field|parents
annotation|@
name|UiField
name|Element
name|parents
decl_stmt|;
DECL|field|parentCommits
annotation|@
name|UiField
name|FlowPanel
name|parentCommits
decl_stmt|;
DECL|field|parentWebLinks
annotation|@
name|UiField
name|FlowPanel
name|parentWebLinks
decl_stmt|;
DECL|field|authorNameEmail
annotation|@
name|UiField
name|InlineHyperlink
name|authorNameEmail
decl_stmt|;
DECL|field|authorDate
annotation|@
name|UiField
name|Element
name|authorDate
decl_stmt|;
DECL|field|committerNameEmail
annotation|@
name|UiField
name|InlineHyperlink
name|committerNameEmail
decl_stmt|;
DECL|field|committerDate
annotation|@
name|UiField
name|Element
name|committerDate
decl_stmt|;
DECL|field|idText
annotation|@
name|UiField
name|CopyableLabel
name|idText
decl_stmt|;
DECL|field|text
annotation|@
name|UiField
name|HTML
name|text
decl_stmt|;
DECL|field|scroll
annotation|@
name|UiField
name|ScrollPanel
name|scroll
decl_stmt|;
DECL|field|more
annotation|@
name|UiField
name|Button
name|more
decl_stmt|;
DECL|field|expanded
specifier|private
name|boolean
name|expanded
decl_stmt|;
DECL|method|CommitBox ()
name|CommitBox
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
name|addStyleName
argument_list|(
name|style
operator|.
name|collapsed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|onShowView ()
name|void
name|onShowView
parameter_list|()
block|{
name|more
operator|.
name|setVisible
argument_list|(
name|scroll
operator|.
name|getMaximumVerticalScrollPosition
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"more"
argument_list|)
DECL|method|onMore (ClickEvent e)
name|void
name|onMore
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|expanded
condition|)
block|{
name|removeStyleName
argument_list|(
name|style
operator|.
name|expanded
argument_list|()
argument_list|)
expr_stmt|;
name|addStyleName
argument_list|(
name|style
operator|.
name|collapsed
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|removeStyleName
argument_list|(
name|style
operator|.
name|collapsed
argument_list|()
argument_list|)
expr_stmt|;
name|addStyleName
argument_list|(
name|style
operator|.
name|expanded
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|expanded
operator|=
operator|!
name|expanded
expr_stmt|;
block|}
DECL|method|set (CommentLinkProcessor commentLinkProcessor, ChangeInfo change, String revision)
name|void
name|set
parameter_list|(
name|CommentLinkProcessor
name|commentLinkProcessor
parameter_list|,
name|ChangeInfo
name|change
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|RevisionInfo
name|revInfo
init|=
name|change
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
decl_stmt|;
name|CommitInfo
name|commit
init|=
name|revInfo
operator|.
name|commit
argument_list|()
decl_stmt|;
name|commitName
operator|.
name|setText
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|idText
operator|.
name|setText
argument_list|(
literal|"Change-Id: "
operator|+
name|change
operator|.
name|change_id
argument_list|()
argument_list|)
expr_stmt|;
name|idText
operator|.
name|setPreviewText
argument_list|(
name|change
operator|.
name|change_id
argument_list|()
argument_list|)
expr_stmt|;
name|formatLink
argument_list|(
name|commit
operator|.
name|author
argument_list|()
argument_list|,
name|authorPanel
argument_list|,
name|authorNameEmail
argument_list|,
name|authorDate
argument_list|,
name|change
argument_list|)
expr_stmt|;
name|formatLink
argument_list|(
name|commit
operator|.
name|committer
argument_list|()
argument_list|,
name|committerPanel
argument_list|,
name|committerNameEmail
argument_list|,
name|committerDate
argument_list|,
name|change
argument_list|)
expr_stmt|;
name|text
operator|.
name|setHTML
argument_list|(
name|commentLinkProcessor
operator|.
name|apply
argument_list|(
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|commit
operator|.
name|message
argument_list|()
argument_list|)
operator|.
name|linkify
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setWebLinks
argument_list|(
name|change
argument_list|,
name|revision
argument_list|,
name|revInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|revInfo
operator|.
name|commit
argument_list|()
operator|.
name|parents
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|mergeCommit
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setParents
argument_list|(
name|change
operator|.
name|project
argument_list|()
argument_list|,
name|revInfo
operator|.
name|commit
argument_list|()
operator|.
name|parents
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setWebLinks (ChangeInfo change, String revision, RevisionInfo revInfo)
specifier|private
name|void
name|setWebLinks
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|String
name|revision
parameter_list|,
name|RevisionInfo
name|revInfo
parameter_list|)
block|{
name|GitwebLink
name|gw
init|=
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
decl_stmt|;
if|if
condition|(
name|gw
operator|!=
literal|null
operator|&&
name|gw
operator|.
name|canLink
argument_list|(
name|revInfo
argument_list|)
condition|)
block|{
name|addWebLink
argument_list|(
name|gw
operator|.
name|toRevision
argument_list|(
name|change
operator|.
name|project
argument_list|()
argument_list|,
name|revision
argument_list|)
argument_list|,
name|gw
operator|.
name|getLinkName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JsArray
argument_list|<
name|WebLinkInfo
argument_list|>
name|links
init|=
name|revInfo
operator|.
name|web_links
argument_list|()
decl_stmt|;
if|if
condition|(
name|links
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WebLinkInfo
name|link
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|links
argument_list|)
control|)
block|{
name|addWebLink
argument_list|(
name|link
operator|.
name|url
argument_list|()
argument_list|,
name|parenthesize
argument_list|(
name|link
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addWebLink (String href, String name)
specifier|private
name|void
name|addWebLink
parameter_list|(
name|String
name|href
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|AnchorElement
name|a
init|=
name|DOM
operator|.
name|createAnchor
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|href
argument_list|)
expr_stmt|;
name|a
operator|.
name|setInnerText
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|webLinkCell
operator|.
name|appendChild
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
DECL|method|setParents (String project, JsArray<CommitInfo> commits)
specifier|private
name|void
name|setParents
parameter_list|(
name|String
name|project
parameter_list|,
name|JsArray
argument_list|<
name|CommitInfo
argument_list|>
name|commits
parameter_list|)
block|{
name|setVisible
argument_list|(
name|parents
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|CommitInfo
name|c
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|commits
argument_list|)
control|)
block|{
name|CopyableLabel
name|copyLabel
init|=
operator|new
name|CopyableLabel
argument_list|(
name|c
operator|.
name|commit
argument_list|()
argument_list|)
decl_stmt|;
name|copyLabel
operator|.
name|setTitle
argument_list|(
name|c
operator|.
name|subject
argument_list|()
argument_list|)
expr_stmt|;
name|copyLabel
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|clippy
argument_list|()
argument_list|)
expr_stmt|;
name|parentCommits
operator|.
name|add
argument_list|(
name|copyLabel
argument_list|)
expr_stmt|;
name|GitwebLink
name|gw
init|=
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
decl_stmt|;
if|if
condition|(
name|gw
operator|!=
literal|null
condition|)
block|{
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|(
name|gw
operator|.
name|getLinkName
argument_list|()
argument_list|,
name|gw
operator|.
name|toRevision
argument_list|(
name|project
argument_list|,
name|c
operator|.
name|commit
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|a
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|parentWebLink
argument_list|()
argument_list|)
expr_stmt|;
name|parentWebLinks
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|formatLink (GitPerson person, FlowPanel p, InlineHyperlink name, Element date, ChangeInfo change)
specifier|private
specifier|static
name|void
name|formatLink
parameter_list|(
name|GitPerson
name|person
parameter_list|,
name|FlowPanel
name|p
parameter_list|,
name|InlineHyperlink
name|name
parameter_list|,
name|Element
name|date
parameter_list|,
name|ChangeInfo
name|change
parameter_list|)
block|{
comment|// only try to fetch the avatar image for author and committer if an avatar
comment|// plugin is installed, if the change owner has no avatar info assume that
comment|// no avatar plugin is installed
if|if
condition|(
name|change
operator|.
name|owner
argument_list|()
operator|.
name|has_avatar_info
argument_list|()
condition|)
block|{
name|AvatarImage
name|avatar
decl_stmt|;
if|if
condition|(
name|change
operator|.
name|owner
argument_list|()
operator|.
name|email
argument_list|()
operator|.
name|equals
argument_list|(
name|person
operator|.
name|email
argument_list|()
argument_list|)
condition|)
block|{
name|avatar
operator|=
operator|new
name|AvatarImage
argument_list|(
name|change
operator|.
name|owner
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|avatar
operator|=
operator|new
name|AvatarImage
argument_list|(
name|AccountInfo
operator|.
name|create
argument_list|(
literal|0
argument_list|,
name|person
operator|.
name|name
argument_list|()
argument_list|,
name|person
operator|.
name|email
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|insert
argument_list|(
name|avatar
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|name
operator|.
name|setText
argument_list|(
name|renderName
argument_list|(
name|person
argument_list|)
argument_list|)
expr_stmt|;
name|name
operator|.
name|setTargetHistoryToken
argument_list|(
name|PageLinks
operator|.
name|toAccountQuery
argument_list|(
name|owner
argument_list|(
name|person
argument_list|)
argument_list|,
name|change
operator|.
name|status
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|date
operator|.
name|setInnerText
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|person
operator|.
name|date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|parenthesize (String str)
specifier|private
specifier|static
name|String
name|parenthesize
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|str
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|renderName (GitPerson person)
specifier|private
specifier|static
name|String
name|renderName
parameter_list|(
name|GitPerson
name|person
parameter_list|)
block|{
return|return
name|person
operator|.
name|name
argument_list|()
operator|+
literal|"<"
operator|+
name|person
operator|.
name|email
argument_list|()
operator|+
literal|">"
return|;
block|}
DECL|method|owner (GitPerson person)
specifier|private
specifier|static
name|String
name|owner
parameter_list|(
name|GitPerson
name|person
parameter_list|)
block|{
if|if
condition|(
name|person
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|person
operator|.
name|email
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|person
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|person
operator|.
name|name
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
end_class

end_unit


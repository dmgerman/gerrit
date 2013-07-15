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
DECL|field|commitName
annotation|@
name|UiField
name|Element
name|commitName
decl_stmt|;
DECL|field|browserLink
annotation|@
name|UiField
name|AnchorElement
name|browserLink
decl_stmt|;
DECL|field|authorNameEmail
annotation|@
name|UiField
name|Element
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
name|Element
name|committerNameEmail
decl_stmt|;
DECL|field|committerDate
annotation|@
name|UiField
name|Element
name|committerDate
decl_stmt|;
DECL|field|commitMessageText
annotation|@
name|UiField
name|Element
name|commitMessageText
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
name|setInnerText
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|format
argument_list|(
name|commit
operator|.
name|author
argument_list|()
argument_list|,
name|authorNameEmail
argument_list|,
name|authorDate
argument_list|)
expr_stmt|;
name|format
argument_list|(
name|commit
operator|.
name|committer
argument_list|()
argument_list|,
name|committerNameEmail
argument_list|,
name|committerDate
argument_list|)
expr_stmt|;
name|commitMessageText
operator|.
name|setInnerSafeHtml
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
name|browserLink
operator|.
name|setInnerText
argument_list|(
name|gw
operator|.
name|getLinkName
argument_list|()
argument_list|)
expr_stmt|;
name|browserLink
operator|.
name|setHref
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
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|browserLink
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|format (GitPerson person, Element name, Element date)
specifier|private
name|void
name|format
parameter_list|(
name|GitPerson
name|person
parameter_list|,
name|Element
name|name
parameter_list|,
name|Element
name|date
parameter_list|)
block|{
name|name
operator|.
name|setInnerText
argument_list|(
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
block|}
end_class

end_unit


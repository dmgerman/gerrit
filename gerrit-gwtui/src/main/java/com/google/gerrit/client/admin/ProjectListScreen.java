begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|ADMIN_PROJECTS
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
name|Dispatcher
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
name|projects
operator|.
name|ProjectInfo
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
name|projects
operator|.
name|ProjectMap
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
name|FilteredUserInterface
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
name|HighlightingInlineHyperlink
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
name|IgnoreOutdatedFilterResultsCallbackWrapper
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
name|ProjectSearchLink
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
name|ProjectsTable
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
name|Screen
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyCodes
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
name|KeyUpEvent
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
name|KeyUpHandler
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
name|http
operator|.
name|client
operator|.
name|URL
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
name|History
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
name|HorizontalPanel
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
name|Label
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
name|globalkey
operator|.
name|client
operator|.
name|NpTextBox
import|;
end_import

begin_class
DECL|class|ProjectListScreen
specifier|public
class|class
name|ProjectListScreen
extends|extends
name|Screen
implements|implements
name|FilteredUserInterface
block|{
DECL|field|projects
specifier|private
name|ProjectsTable
name|projects
decl_stmt|;
DECL|field|filterTxt
specifier|private
name|NpTextBox
name|filterTxt
decl_stmt|;
DECL|field|subname
specifier|private
name|String
name|subname
decl_stmt|;
DECL|method|ProjectListScreen ()
specifier|public
name|ProjectListScreen
parameter_list|()
block|{   }
DECL|method|ProjectListScreen (String params)
specifier|public
name|ProjectListScreen
parameter_list|(
name|String
name|params
parameter_list|)
block|{
for|for
control|(
name|String
name|kvPair
range|:
name|params
operator|.
name|split
argument_list|(
literal|"[,;&]"
argument_list|)
control|)
block|{
name|String
index|[]
name|kv
init|=
name|kvPair
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|kv
operator|.
name|length
operator|!=
literal|2
operator|||
name|kv
index|[
literal|0
index|]
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
literal|"filter"
operator|.
name|equals
argument_list|(
name|kv
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|subname
operator|=
name|URL
operator|.
name|decodeQueryString
argument_list|(
name|kv
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|display
argument_list|()
expr_stmt|;
name|refresh
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|refresh (final boolean open)
specifier|private
name|void
name|refresh
parameter_list|(
specifier|final
name|boolean
name|open
parameter_list|)
block|{
name|setToken
argument_list|(
name|subname
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|subname
argument_list|)
condition|?
name|ADMIN_PROJECTS
else|:
name|ADMIN_PROJECTS
operator|+
literal|"?filter="
operator|+
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|subname
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectMap
operator|.
name|match
argument_list|(
name|subname
argument_list|,
operator|new
name|IgnoreOutdatedFilterResultsCallbackWrapper
argument_list|<
name|ProjectMap
argument_list|>
argument_list|(
name|this
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ProjectMap
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ProjectMap
name|result
parameter_list|)
block|{
if|if
condition|(
name|open
operator|&&
name|result
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toProject
argument_list|(
name|result
operator|.
name|values
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name_key
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|projects
operator|.
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCurrentFilter ()
specifier|public
name|String
name|getCurrentFilter
parameter_list|()
block|{
return|return
name|subname
return|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectListTitle
argument_list|()
argument_list|)
expr_stmt|;
name|initPageHeader
argument_list|()
expr_stmt|;
name|projects
operator|=
operator|new
name|ProjectsTable
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|initColumnHeaders
parameter_list|()
block|{
name|super
operator|.
name|initColumnHeaders
argument_list|()
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|ProjectsTable
operator|.
name|C_REPO_BROWSER
argument_list|,
name|Util
operator|.
name|C
operator|.
name|projectRepoBrowser
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|ProjectsTable
operator|.
name|C_REPO_BROWSER
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onOpenRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|link
argument_list|(
name|getRowItem
argument_list|(
name|row
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|link
parameter_list|(
specifier|final
name|ProjectInfo
name|item
parameter_list|)
block|{
return|return
name|Dispatcher
operator|.
name|toProject
argument_list|(
name|item
operator|.
name|name_key
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|insert
parameter_list|(
name|int
name|row
parameter_list|,
name|ProjectInfo
name|k
parameter_list|)
block|{
name|super
operator|.
name|insert
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_REPO_BROWSER
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|ProjectInfo
name|k
parameter_list|)
block|{
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|fp
operator|.
name|add
argument_list|(
operator|new
name|ProjectSearchLink
argument_list|(
name|k
operator|.
name|name_key
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
operator|new
name|HighlightingInlineHyperlink
argument_list|(
name|k
operator|.
name|name
argument_list|()
argument_list|,
name|link
argument_list|(
name|k
argument_list|)
argument_list|,
name|subname
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_NAME
argument_list|,
name|fp
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_DESCRIPTION
argument_list|,
name|k
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|GitwebLink
name|l
init|=
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_REPO_BROWSER
argument_list|,
operator|new
name|Anchor
argument_list|(
name|l
operator|.
name|getLinkName
argument_list|()
argument_list|,
literal|false
argument_list|,
name|l
operator|.
name|toProject
argument_list|(
name|k
operator|.
name|name_key
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|projects
operator|.
name|setSavePointerId
argument_list|(
name|PageLinks
operator|.
name|ADMIN_PROJECTS
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|projects
argument_list|)
expr_stmt|;
block|}
DECL|method|initPageHeader ()
specifier|private
name|void
name|initPageHeader
parameter_list|()
block|{
specifier|final
name|HorizontalPanel
name|hp
init|=
operator|new
name|HorizontalPanel
argument_list|()
decl_stmt|;
name|hp
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|projectFilterPanel
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Label
name|filterLabel
init|=
operator|new
name|Label
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectFilter
argument_list|()
argument_list|)
decl_stmt|;
name|filterLabel
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|projectFilterLabel
argument_list|()
argument_list|)
expr_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|filterLabel
argument_list|)
expr_stmt|;
name|filterTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|filterTxt
operator|.
name|setValue
argument_list|(
name|subname
argument_list|)
expr_stmt|;
name|filterTxt
operator|.
name|addKeyUpHandler
argument_list|(
operator|new
name|KeyUpHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyUp
parameter_list|(
name|KeyUpEvent
name|event
parameter_list|)
block|{
name|subname
operator|=
name|filterTxt
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|refresh
argument_list|(
name|event
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|filterTxt
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|hp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onShowView ()
specifier|public
name|void
name|onShowView
parameter_list|()
block|{
name|super
operator|.
name|onShowView
argument_list|()
expr_stmt|;
if|if
condition|(
name|subname
operator|!=
literal|null
condition|)
block|{
name|filterTxt
operator|.
name|setCursorPos
argument_list|(
name|subname
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|filterTxt
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
expr_stmt|;
name|projects
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


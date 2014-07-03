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
name|Hyperlink
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|field|prev
specifier|private
name|Hyperlink
name|prev
decl_stmt|;
DECL|field|next
specifier|private
name|Hyperlink
name|next
decl_stmt|;
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
init|=
literal|""
decl_stmt|;
DECL|field|startPosition
specifier|private
name|int
name|startPosition
decl_stmt|;
DECL|field|pageSize
specifier|private
name|int
name|pageSize
decl_stmt|;
DECL|method|ProjectListScreen ()
specifier|public
name|ProjectListScreen
parameter_list|()
block|{
name|configurePageSize
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
literal|"skip"
operator|.
name|equals
argument_list|(
name|kv
index|[
literal|0
index|]
argument_list|)
operator|&&
name|URL
operator|.
name|decodeQueryString
argument_list|(
name|kv
index|[
literal|1
index|]
argument_list|)
operator|.
name|matches
argument_list|(
literal|"^[\\d]+"
argument_list|)
condition|)
block|{
name|startPosition
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|URL
operator|.
name|decodeQueryString
argument_list|(
name|kv
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|configurePageSize
argument_list|()
expr_stmt|;
block|}
DECL|method|configurePageSize ()
specifier|private
name|void
name|configurePageSize
parameter_list|()
block|{
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
specifier|final
name|AccountGeneralPreferences
name|p
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getGeneralPreferences
argument_list|()
decl_stmt|;
specifier|final
name|short
name|m
init|=
name|p
operator|.
name|getMaximumPageSize
argument_list|()
decl_stmt|;
name|pageSize
operator|=
literal|0
operator|<
name|m
condition|?
name|m
else|:
name|AccountGeneralPreferences
operator|.
name|DEFAULT_PAGESIZE
expr_stmt|;
block|}
else|else
block|{
name|pageSize
operator|=
name|AccountGeneralPreferences
operator|.
name|DEFAULT_PAGESIZE
expr_stmt|;
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
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|refresh (final boolean open, final boolean filterModified)
specifier|private
name|void
name|refresh
parameter_list|(
specifier|final
name|boolean
name|open
parameter_list|,
specifier|final
name|boolean
name|filterModified
parameter_list|)
block|{
if|if
condition|(
name|filterModified
condition|)
block|{
name|startPosition
operator|=
literal|0
expr_stmt|;
block|}
name|setToken
argument_list|(
name|getTokenForScreen
argument_list|(
name|subname
argument_list|,
name|startPosition
argument_list|)
argument_list|)
expr_stmt|;
comment|// Retrieve one more project than page size to determine if there are more
comment|// projects to display
name|ProjectMap
operator|.
name|match
argument_list|(
name|subname
argument_list|,
name|pageSize
operator|+
literal|1
argument_list|,
name|startPosition
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
if|if
condition|(
name|result
operator|.
name|size
argument_list|()
operator|<=
name|pageSize
condition|)
block|{
name|projects
operator|.
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|next
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|projects
operator|.
name|displaySubset
argument_list|(
name|result
argument_list|,
literal|0
argument_list|,
name|result
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|setupNavigationLink
argument_list|(
name|next
argument_list|,
name|subname
argument_list|,
name|startPosition
operator|+
name|pageSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|startPosition
operator|>
literal|0
condition|)
block|{
name|setupNavigationLink
argument_list|(
name|prev
argument_list|,
name|subname
argument_list|,
name|startPosition
operator|-
name|pageSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prev
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setupNavigationLink (Hyperlink link, String filter, int skip)
specifier|private
name|void
name|setupNavigationLink
parameter_list|(
name|Hyperlink
name|link
parameter_list|,
name|String
name|filter
parameter_list|,
name|int
name|skip
parameter_list|)
block|{
name|link
operator|.
name|setTargetHistoryToken
argument_list|(
name|getTokenForScreen
argument_list|(
name|filter
argument_list|,
name|skip
argument_list|)
argument_list|)
expr_stmt|;
name|link
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|getTokenForScreen (String filter, int skip)
specifier|private
name|String
name|getTokenForScreen
parameter_list|(
name|String
name|filter
parameter_list|,
name|int
name|skip
parameter_list|)
block|{
name|String
name|token
init|=
name|ADMIN_PROJECTS
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
operator|&&
operator|!
name|filter
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|token
operator|+=
literal|"?filter="
operator|+
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skip
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|token
operator|.
name|contains
argument_list|(
literal|"?filter="
argument_list|)
condition|)
block|{
name|token
operator|+=
literal|","
expr_stmt|;
block|}
else|else
block|{
name|token
operator|+=
literal|"?"
expr_stmt|;
block|}
name|token
operator|+=
literal|"skip="
operator|+
name|skip
expr_stmt|;
block|}
return|return
name|token
return|;
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
name|prev
operator|=
operator|new
name|Hyperlink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|pagedListPrev
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|prev
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|next
operator|=
operator|new
name|Hyperlink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|pagedListNext
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|next
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
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
name|Image
name|state
init|=
operator|new
name|Image
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|k
operator|.
name|state
argument_list|()
condition|)
block|{
case|case
name|HIDDEN
case|:
name|state
operator|.
name|setResource
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|redNot
argument_list|()
argument_list|)
expr_stmt|;
name|state
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|toLongString
argument_list|(
name|k
operator|.
name|state
argument_list|()
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
name|C_STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
break|break;
case|case
name|READ_ONLY
case|:
name|state
operator|.
name|setResource
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|readOnly
argument_list|()
argument_list|)
expr_stmt|;
name|state
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|toLongString
argument_list|(
name|k
operator|.
name|state
argument_list|()
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
name|C_STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// Intentionally left blank, do not show an icon when active.
break|break;
block|}
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
name|addWebLinks
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addWebLinks
parameter_list|(
name|int
name|row
parameter_list|,
name|ProjectInfo
name|k
parameter_list|)
block|{
name|GitwebLink
name|gitWebLink
init|=
name|Gerrit
operator|.
name|getGitwebLink
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|k
operator|.
name|web_links
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|gitWebLink
operator|!=
literal|null
operator|||
operator|(
name|webLinks
operator|!=
literal|null
operator|&&
operator|!
name|webLinks
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|FlowPanel
name|p
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
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
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|gitWebLink
operator|!=
literal|null
condition|)
block|{
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|a
operator|.
name|setText
argument_list|(
name|gitWebLink
operator|.
name|getLinkName
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|gitWebLink
operator|.
name|toProject
argument_list|(
name|k
operator|.
name|name_key
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|WebLinkInfo
name|weblink
range|:
name|webLinks
control|)
block|{
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|weblink
operator|.
name|url
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|weblink
operator|.
name|imageUrl
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|weblink
operator|.
name|imageUrl
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Image
name|img
init|=
operator|new
name|Image
argument_list|()
decl_stmt|;
name|img
operator|.
name|setAltText
argument_list|(
name|weblink
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|img
operator|.
name|setUrl
argument_list|(
name|weblink
operator|.
name|imageUrl
argument_list|()
argument_list|)
expr_stmt|;
name|img
operator|.
name|setTitle
argument_list|(
name|weblink
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|img
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|a
operator|.
name|setText
argument_list|(
literal|"("
operator|+
name|weblink
operator|.
name|name
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
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
specifier|final
name|HorizontalPanel
name|buttons
init|=
operator|new
name|HorizontalPanel
argument_list|()
decl_stmt|;
name|buttons
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
name|changeTablePrevNextLinks
argument_list|()
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|prev
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|buttons
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
name|boolean
name|enterPressed
init|=
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
decl_stmt|;
name|boolean
name|filterModified
init|=
operator|!
name|filterTxt
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|subname
argument_list|)
decl_stmt|;
if|if
condition|(
name|enterPressed
operator|||
name|filterModified
condition|)
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
name|enterPressed
argument_list|,
name|filterModified
argument_list|)
expr_stmt|;
block|}
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


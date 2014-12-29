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
name|ADMIN_GROUPS
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
name|groups
operator|.
name|GroupMap
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
name|AccountScreen
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
DECL|class|GroupListScreen
specifier|public
class|class
name|GroupListScreen
extends|extends
name|AccountScreen
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
DECL|field|groups
specifier|private
name|GroupTable
name|groups
decl_stmt|;
DECL|field|filterTxt
specifier|private
name|NpTextBox
name|filterTxt
decl_stmt|;
DECL|field|pageSize
specifier|private
name|int
name|pageSize
decl_stmt|;
DECL|field|match
specifier|private
name|String
name|match
init|=
literal|""
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|query
specifier|private
name|Query
name|query
decl_stmt|;
DECL|method|GroupListScreen ()
specifier|public
name|GroupListScreen
parameter_list|()
block|{
name|configurePageSize
argument_list|()
expr_stmt|;
block|}
DECL|method|GroupListScreen (String params)
specifier|public
name|GroupListScreen
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
name|match
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
name|start
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
name|query
operator|=
operator|new
name|Query
argument_list|(
name|match
argument_list|)
operator|.
name|start
argument_list|(
name|start
argument_list|)
operator|.
name|run
argument_list|()
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
name|ADMIN_GROUPS
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
name|groupListTitle
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
name|groups
operator|=
operator|new
name|GroupTable
argument_list|(
name|PageLinks
operator|.
name|ADMIN_GROUPS
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|groups
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
name|match
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
name|Query
name|q
init|=
operator|new
name|Query
argument_list|(
name|filterTxt
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|open
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
decl_stmt|;
if|if
condition|(
name|match
operator|.
name|equals
argument_list|(
name|q
operator|.
name|qMatch
argument_list|)
condition|)
block|{
name|q
operator|.
name|start
argument_list|(
name|start
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|q
operator|.
name|open
operator|||
operator|!
name|match
operator|.
name|equals
argument_list|(
name|q
operator|.
name|qMatch
argument_list|)
condition|)
block|{
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|q
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
name|query
operator|=
name|q
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
name|match
operator|!=
literal|null
condition|)
block|{
name|filterTxt
operator|.
name|setCursorPos
argument_list|(
name|match
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
name|groups
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|class|Query
specifier|private
class|class
name|Query
block|{
DECL|field|qMatch
specifier|private
specifier|final
name|String
name|qMatch
decl_stmt|;
DECL|field|qStart
specifier|private
name|int
name|qStart
decl_stmt|;
DECL|field|open
specifier|private
name|boolean
name|open
decl_stmt|;
DECL|method|Query (String match)
name|Query
parameter_list|(
name|String
name|match
parameter_list|)
block|{
name|this
operator|.
name|qMatch
operator|=
name|match
expr_stmt|;
block|}
DECL|method|start (int start)
name|Query
name|start
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|qStart
operator|=
name|start
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|open (boolean open)
name|Query
name|open
parameter_list|(
name|boolean
name|open
parameter_list|)
block|{
name|this
operator|.
name|open
operator|=
name|open
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|run ()
name|Query
name|run
parameter_list|()
block|{
name|int
name|limit
init|=
name|open
condition|?
literal|1
else|:
name|pageSize
operator|+
literal|1
decl_stmt|;
name|GroupMap
operator|.
name|match
argument_list|(
name|qMatch
argument_list|,
name|limit
argument_list|,
name|qStart
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|GroupMap
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|GroupMap
name|result
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAttached
argument_list|()
condition|)
block|{
comment|// View has been disposed.
block|}
elseif|else
if|if
condition|(
name|query
operator|==
name|Query
operator|.
name|this
condition|)
block|{
name|query
operator|=
literal|null
expr_stmt|;
name|showMap
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|showMap (GroupMap result)
specifier|private
name|void
name|showMap
parameter_list|(
name|GroupMap
name|result
parameter_list|)
block|{
if|if
condition|(
name|open
operator|&&
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toGroup
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
name|getGroupUUID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|setToken
argument_list|(
name|getTokenForScreen
argument_list|(
name|qMatch
argument_list|,
name|qStart
argument_list|)
argument_list|)
expr_stmt|;
name|GroupListScreen
operator|.
name|this
operator|.
name|match
operator|=
name|qMatch
expr_stmt|;
name|GroupListScreen
operator|.
name|this
operator|.
name|start
operator|=
name|qStart
expr_stmt|;
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
name|groups
operator|.
name|display
argument_list|(
name|result
argument_list|,
name|qMatch
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
name|groups
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
argument_list|,
name|qMatch
argument_list|)
expr_stmt|;
name|setupNavigationLink
argument_list|(
name|next
argument_list|,
name|qMatch
argument_list|,
name|qStart
operator|+
name|pageSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|qStart
operator|>
literal|0
condition|)
block|{
name|setupNavigationLink
argument_list|(
name|prev
argument_list|,
name|qMatch
argument_list|,
name|qStart
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
if|if
condition|(
operator|!
name|isCurrentView
argument_list|()
condition|)
block|{
name|display
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


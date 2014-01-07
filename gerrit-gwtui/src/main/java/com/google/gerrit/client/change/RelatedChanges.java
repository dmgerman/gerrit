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
name|op
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
name|changes
operator|.
name|ChangeList
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
name|changes
operator|.
name|ListChangesOption
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|SelectionEvent
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
name|logical
operator|.
name|shared
operator|.
name|SelectionHandler
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
name|ClientBundle
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
name|TabBar
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
name|TabPanel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
DECL|class|RelatedChanges
specifier|public
class|class
name|RelatedChanges
extends|extends
name|TabPanel
block|{
DECL|field|R
specifier|static
specifier|final
name|RelatedChangesResources
name|R
init|=
name|GWT
operator|.
name|create
argument_list|(
name|RelatedChangesResources
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|RelatedChangesResources
interface|interface
name|RelatedChangesResources
extends|extends
name|ClientBundle
block|{
annotation|@
name|Source
argument_list|(
literal|"related_changes.css"
argument_list|)
DECL|method|css ()
name|RelatedChangesCss
name|css
parameter_list|()
function_decl|;
block|}
DECL|interface|RelatedChangesCss
interface|interface
name|RelatedChangesCss
extends|extends
name|CssResource
block|{
DECL|method|activeRow ()
name|String
name|activeRow
parameter_list|()
function_decl|;
DECL|method|current ()
name|String
name|current
parameter_list|()
function_decl|;
DECL|method|gitweb ()
name|String
name|gitweb
parameter_list|()
function_decl|;
DECL|method|indirect ()
name|String
name|indirect
parameter_list|()
function_decl|;
DECL|method|notCurrent ()
name|String
name|notCurrent
parameter_list|()
function_decl|;
DECL|method|pointer ()
name|String
name|pointer
parameter_list|()
function_decl|;
DECL|method|row ()
name|String
name|row
parameter_list|()
function_decl|;
DECL|method|subject ()
name|String
name|subject
parameter_list|()
function_decl|;
DECL|method|tabPanel ()
name|String
name|tabPanel
parameter_list|()
function_decl|;
block|}
DECL|enum|Tab
specifier|private
enum|enum
name|Tab
block|{
DECL|enumConstant|RELATED_CHANGES
DECL|enumConstant|Resources.C.relatedChanges
name|RELATED_CHANGES
argument_list|(
name|Resources
operator|.
name|C
operator|.
name|relatedChanges
argument_list|()
argument_list|,
DECL|enumConstant|Resources.C.relatedChangesTooltip
DECL|method|Resources.C.relatedChangesTooltip ()
name|Resources
operator|.
name|C
operator|.
name|relatedChangesTooltip
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|int
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|relatedChanges
argument_list|(
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|String
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|relatedChanges
argument_list|(
name|count
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|SAME_TOPIC
DECL|enumConstant|Resources.C.sameTopic
name|SAME_TOPIC
argument_list|(
name|Resources
operator|.
name|C
operator|.
name|sameTopic
argument_list|()
argument_list|,
DECL|enumConstant|Resources.C.sameTopicTooltip
DECL|method|Resources.C.sameTopicTooltip ()
name|Resources
operator|.
name|C
operator|.
name|sameTopicTooltip
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|int
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|sameTopic
argument_list|(
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|String
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|sameTopic
argument_list|(
name|count
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|CONFLICTING_CHANGES
DECL|enumConstant|Resources.C.conflictingChanges
name|CONFLICTING_CHANGES
argument_list|(
name|Resources
operator|.
name|C
operator|.
name|conflictingChanges
argument_list|()
argument_list|,
DECL|enumConstant|Resources.C.conflictingChangesTooltip
DECL|method|Resources.C.conflictingChangesTooltip ()
name|Resources
operator|.
name|C
operator|.
name|conflictingChangesTooltip
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|int
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|conflictingChanges
argument_list|(
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|String
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|conflictingChanges
argument_list|(
name|count
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|CHERRY_PICKS
DECL|enumConstant|Resources.C.cherryPicks
name|CHERRY_PICKS
argument_list|(
name|Resources
operator|.
name|C
operator|.
name|cherryPicks
argument_list|()
argument_list|,
DECL|enumConstant|Resources.C.cherryPicksTooltip
DECL|method|Resources.C.cherryPicksTooltip ()
name|Resources
operator|.
name|C
operator|.
name|cherryPicksTooltip
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|int
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|cherryPicks
argument_list|(
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|getTitle
parameter_list|(
name|String
name|count
parameter_list|)
block|{
return|return
name|Resources
operator|.
name|M
operator|.
name|cherryPicks
argument_list|(
name|count
argument_list|)
return|;
block|}
block|}
block|;
DECL|field|defaultTitle
specifier|final
name|String
name|defaultTitle
decl_stmt|;
DECL|field|tooltip
specifier|final
name|String
name|tooltip
decl_stmt|;
DECL|method|getTitle (int count)
specifier|abstract
name|String
name|getTitle
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|getTitle (String count)
specifier|abstract
name|String
name|getTitle
parameter_list|(
name|String
name|count
parameter_list|)
function_decl|;
DECL|method|Tab (String defaultTitle, String tooltip)
specifier|private
name|Tab
parameter_list|(
name|String
name|defaultTitle
parameter_list|,
name|String
name|tooltip
parameter_list|)
block|{
name|this
operator|.
name|defaultTitle
operator|=
name|defaultTitle
expr_stmt|;
name|this
operator|.
name|tooltip
operator|=
name|tooltip
expr_stmt|;
block|}
block|}
DECL|field|tabs
specifier|private
specifier|final
name|List
argument_list|<
name|RelatedChangesTab
argument_list|>
name|tabs
decl_stmt|;
DECL|field|maxHeightWithHeader
specifier|private
name|int
name|maxHeightWithHeader
decl_stmt|;
DECL|field|selectedTab
specifier|private
name|int
name|selectedTab
decl_stmt|;
DECL|field|outstandingCallbacks
specifier|private
name|int
name|outstandingCallbacks
decl_stmt|;
DECL|method|RelatedChanges ()
name|RelatedChanges
parameter_list|()
block|{
name|tabs
operator|=
operator|new
name|ArrayList
argument_list|<
name|RelatedChangesTab
argument_list|>
argument_list|(
name|Tab
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|selectedTab
operator|=
operator|-
literal|1
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addStyleName
argument_list|(
name|R
operator|.
name|css
argument_list|()
operator|.
name|tabPanel
argument_list|()
argument_list|)
expr_stmt|;
name|initTabBar
argument_list|()
expr_stmt|;
block|}
DECL|method|initTabBar ()
specifier|private
name|void
name|initTabBar
parameter_list|()
block|{
name|TabBar
name|tabBar
init|=
name|getTabBar
argument_list|()
decl_stmt|;
name|tabBar
operator|.
name|addSelectionHandler
argument_list|(
operator|new
name|SelectionHandler
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSelection
parameter_list|(
name|SelectionEvent
argument_list|<
name|Integer
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|selectedTab
operator|>=
literal|0
condition|)
block|{
name|tabs
operator|.
name|get
argument_list|(
name|selectedTab
argument_list|)
operator|.
name|registerKeys
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|selectedTab
operator|=
name|event
operator|.
name|getSelectedItem
argument_list|()
expr_stmt|;
name|tabs
operator|.
name|get
argument_list|(
name|selectedTab
argument_list|)
operator|.
name|registerKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Tab
name|tabInfo
range|:
name|Tab
operator|.
name|values
argument_list|()
control|)
block|{
name|RelatedChangesTab
name|panel
init|=
operator|new
name|RelatedChangesTab
argument_list|()
decl_stmt|;
name|add
argument_list|(
name|panel
argument_list|,
name|tabInfo
operator|.
name|defaultTitle
argument_list|)
expr_stmt|;
name|tabs
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|TabBar
operator|.
name|Tab
name|tab
init|=
name|tabBar
operator|.
name|getTab
argument_list|(
name|tabInfo
operator|.
name|ordinal
argument_list|()
argument_list|)
decl_stmt|;
name|tab
operator|.
name|setWordWrap
argument_list|(
literal|false
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Composite
operator|)
name|tab
operator|)
operator|.
name|setTitle
argument_list|(
name|tabInfo
operator|.
name|tooltip
argument_list|)
expr_stmt|;
name|setTabEnabled
argument_list|(
name|tabInfo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|getTab
argument_list|(
name|Tab
operator|.
name|RELATED_CHANGES
argument_list|)
operator|.
name|setShowIndirectAncestors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getTab
argument_list|(
name|Tab
operator|.
name|CHERRY_PICKS
argument_list|)
operator|.
name|setShowBranches
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|set (final ChangeInfo info, final String revision)
name|void
name|set
parameter_list|(
specifier|final
name|ChangeInfo
name|info
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|status
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|setForOpenChange
argument_list|(
name|info
argument_list|,
name|revision
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|cherryPicksQuery
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|cherryPicksQuery
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"project"
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cherryPicksQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"change"
argument_list|,
name|info
operator|.
name|change_id
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cherryPicksQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"-change"
argument_list|,
name|info
operator|.
name|legacy_id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeList
operator|.
name|query
argument_list|(
name|cherryPicksQuery
operator|.
name|toString
argument_list|()
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|,
name|ListChangesOption
operator|.
name|CURRENT_COMMIT
argument_list|)
argument_list|,
operator|new
name|TabChangeListCallback
argument_list|(
name|Tab
operator|.
name|CHERRY_PICKS
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|topic
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|info
operator|.
name|topic
argument_list|()
argument_list|)
condition|)
block|{
name|StringBuilder
name|topicQuery
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|topicQuery
operator|.
name|append
argument_list|(
literal|"status:open"
argument_list|)
expr_stmt|;
name|topicQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"project"
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|topicQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"branch"
argument_list|,
name|info
operator|.
name|branch
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|topicQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"topic"
argument_list|,
name|info
operator|.
name|topic
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|topicQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"-change"
argument_list|,
name|info
operator|.
name|legacy_id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeList
operator|.
name|query
argument_list|(
name|topicQuery
operator|.
name|toString
argument_list|()
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|,
name|ListChangesOption
operator|.
name|CURRENT_COMMIT
argument_list|)
argument_list|,
operator|new
name|TabChangeListCallback
argument_list|(
name|Tab
operator|.
name|SAME_TOPIC
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setForOpenChange (final ChangeInfo info, final String revision)
specifier|private
name|void
name|setForOpenChange
parameter_list|(
specifier|final
name|ChangeInfo
name|info
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|)
block|{
name|ChangeApi
operator|.
name|revision
argument_list|(
name|info
operator|.
name|legacy_id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|revision
argument_list|)
operator|.
name|view
argument_list|(
literal|"related"
argument_list|)
operator|.
name|get
argument_list|(
operator|new
name|TabCallback
argument_list|<
name|RelatedInfo
argument_list|>
argument_list|(
name|Tab
operator|.
name|RELATED_CHANGES
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|,
name|revision
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|convert
parameter_list|(
name|RelatedInfo
name|result
parameter_list|)
block|{
return|return
name|result
operator|.
name|changes
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|mergeable
argument_list|()
condition|)
block|{
name|StringBuilder
name|conflictsQuery
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|conflictsQuery
operator|.
name|append
argument_list|(
literal|"status:open"
argument_list|)
expr_stmt|;
name|conflictsQuery
operator|.
name|append
argument_list|(
literal|" is:mergeable"
argument_list|)
expr_stmt|;
name|conflictsQuery
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|op
argument_list|(
literal|"conflicts"
argument_list|,
name|info
operator|.
name|legacy_id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeList
operator|.
name|query
argument_list|(
name|conflictsQuery
operator|.
name|toString
argument_list|()
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|CURRENT_REVISION
argument_list|,
name|ListChangesOption
operator|.
name|CURRENT_COMMIT
argument_list|)
argument_list|,
operator|new
name|TabChangeListCallback
argument_list|(
name|Tab
operator|.
name|CONFLICTING_CHANGES
argument_list|,
name|info
operator|.
name|project
argument_list|()
argument_list|,
name|revision
argument_list|)
argument_list|)
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
name|R
operator|.
name|css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
DECL|method|getTab (Tab tabInfo)
specifier|private
name|RelatedChangesTab
name|getTab
parameter_list|(
name|Tab
name|tabInfo
parameter_list|)
block|{
return|return
name|tabs
operator|.
name|get
argument_list|(
name|tabInfo
operator|.
name|ordinal
argument_list|()
argument_list|)
return|;
block|}
DECL|method|setTabTitle (Tab tabInfo, String title)
specifier|private
name|void
name|setTabTitle
parameter_list|(
name|Tab
name|tabInfo
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|getTabBar
argument_list|()
operator|.
name|setTabText
argument_list|(
name|tabInfo
operator|.
name|ordinal
argument_list|()
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
DECL|method|setTabEnabled (Tab tabInfo, boolean enabled)
specifier|private
name|void
name|setTabEnabled
parameter_list|(
name|Tab
name|tabInfo
parameter_list|,
name|boolean
name|enabled
parameter_list|)
block|{
name|getTabBar
argument_list|()
operator|.
name|setTabEnabled
argument_list|(
name|tabInfo
operator|.
name|ordinal
argument_list|()
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
block|}
DECL|method|setMaxHeight (int height)
name|void
name|setMaxHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
name|maxHeightWithHeader
operator|=
name|height
expr_stmt|;
if|if
condition|(
name|isVisible
argument_list|()
condition|)
block|{
name|applyMaxHeight
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|applyMaxHeight ()
specifier|private
name|void
name|applyMaxHeight
parameter_list|()
block|{
name|int
name|header
init|=
name|getTabBar
argument_list|()
operator|.
name|getOffsetHeight
argument_list|()
operator|+
literal|2
comment|/* padding */
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getTabBar
argument_list|()
operator|.
name|getTabCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|tabs
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|setMaxHeight
argument_list|(
name|maxHeightWithHeader
operator|-
name|header
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TabCallback
specifier|private
specifier|abstract
class|class
name|TabCallback
parameter_list|<
name|T
parameter_list|>
implements|implements
name|AsyncCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|field|tabInfo
specifier|private
specifier|final
name|Tab
name|tabInfo
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|String
name|project
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
DECL|method|TabCallback (Tab tabInfo, String project, String revision)
name|TabCallback
parameter_list|(
name|Tab
name|tabInfo
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|tabInfo
operator|=
name|tabInfo
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|outstandingCallbacks
operator|++
expr_stmt|;
block|}
DECL|method|convert (T result)
specifier|protected
specifier|abstract
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|convert
parameter_list|(
name|T
name|result
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|onSuccess (T result)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|changes
init|=
name|convert
argument_list|(
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|changes
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|setTabTitle
argument_list|(
name|tabInfo
argument_list|,
name|tabInfo
operator|.
name|getTitle
argument_list|(
name|changes
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getTab
argument_list|(
name|tabInfo
argument_list|)
operator|.
name|setChanges
argument_list|(
name|project
argument_list|,
name|revision
argument_list|,
name|changes
argument_list|)
expr_stmt|;
block|}
name|onDone
argument_list|(
name|changes
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable err)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|setTabTitle
argument_list|(
name|tabInfo
argument_list|,
name|tabInfo
operator|.
name|getTitle
argument_list|(
name|Resources
operator|.
name|C
operator|.
name|notAvailable
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getTab
argument_list|(
name|tabInfo
argument_list|)
operator|.
name|setError
argument_list|(
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|onDone
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|onDone (boolean enabled)
specifier|private
name|void
name|onDone
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|setTabEnabled
argument_list|(
name|tabInfo
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
name|outstandingCallbacks
operator|--
expr_stmt|;
if|if
condition|(
name|outstandingCallbacks
operator|==
literal|0
operator|||
operator|(
name|enabled
operator|&&
name|tabInfo
operator|==
name|Tab
operator|.
name|RELATED_CHANGES
operator|)
condition|)
block|{
name|outstandingCallbacks
operator|=
literal|0
expr_stmt|;
comment|// Only execute this block once
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getTabBar
argument_list|()
operator|.
name|getTabCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|getTabBar
argument_list|()
operator|.
name|isTabEnabled
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|selectTab
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|applyMaxHeight
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
DECL|class|TabChangeListCallback
specifier|private
class|class
name|TabChangeListCallback
extends|extends
name|TabCallback
argument_list|<
name|ChangeList
argument_list|>
block|{
DECL|method|TabChangeListCallback (Tab tabInfo, String project, String revision)
name|TabChangeListCallback
parameter_list|(
name|Tab
name|tabInfo
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|super
argument_list|(
name|tabInfo
argument_list|,
name|project
argument_list|,
name|revision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|convert (ChangeList l)
specifier|protected
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|convert
parameter_list|(
name|ChangeList
name|l
parameter_list|)
block|{
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|arr
init|=
name|JavaScriptObject
operator|.
name|createArray
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeInfo
name|i
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|l
argument_list|)
control|)
block|{
if|if
condition|(
name|i
operator|.
name|current_revision
argument_list|()
operator|!=
literal|null
operator|&&
name|i
operator|.
name|revisions
argument_list|()
operator|.
name|containsKey
argument_list|(
name|i
operator|.
name|current_revision
argument_list|()
argument_list|)
condition|)
block|{
name|RevisionInfo
name|currentRevision
init|=
name|i
operator|.
name|revision
argument_list|(
name|i
operator|.
name|current_revision
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeAndCommit
name|c
init|=
name|ChangeAndCommit
operator|.
name|create
argument_list|()
decl_stmt|;
name|c
operator|.
name|set_id
argument_list|(
name|i
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|set_commit
argument_list|(
name|currentRevision
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|set_change_number
argument_list|(
name|i
operator|.
name|legacy_id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|set_revision_number
argument_list|(
name|currentRevision
operator|.
name|_number
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|set_branch
argument_list|(
name|i
operator|.
name|branch
argument_list|()
argument_list|)
expr_stmt|;
name|arr
operator|.
name|push
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|arr
return|;
block|}
block|}
DECL|class|RelatedInfo
specifier|public
specifier|static
class|class
name|RelatedInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|changes ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|ChangeAndCommit
argument_list|>
name|changes
parameter_list|()
comment|/*-{ return this.changes }-*/
function_decl|;
DECL|method|RelatedInfo ()
specifier|protected
name|RelatedInfo
parameter_list|()
block|{     }
block|}
DECL|class|ChangeAndCommit
specifier|public
specifier|static
class|class
name|ChangeAndCommit
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|static
name|ChangeAndCommit
name|create
parameter_list|()
block|{
return|return
operator|(
name|ChangeAndCommit
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|id ()
specifier|public
specifier|final
specifier|native
name|String
name|id
parameter_list|()
comment|/*-{ return this.change_id }-*/
function_decl|;
DECL|method|commit ()
specifier|public
specifier|final
specifier|native
name|CommitInfo
name|commit
parameter_list|()
comment|/*-{ return this.commit }-*/
function_decl|;
DECL|method|branch ()
specifier|final
specifier|native
name|String
name|branch
parameter_list|()
comment|/*-{ return this.branch }-*/
function_decl|;
DECL|method|set_id (String i)
specifier|final
specifier|native
name|void
name|set_id
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ if(i)this.change_id=i; }-*/
function_decl|;
DECL|method|set_commit (CommitInfo c)
specifier|final
specifier|native
name|void
name|set_commit
parameter_list|(
name|CommitInfo
name|c
parameter_list|)
comment|/*-{ if(c)this.commit=c; }-*/
function_decl|;
DECL|method|set_branch (String b)
specifier|final
specifier|native
name|void
name|set_branch
parameter_list|(
name|String
name|b
parameter_list|)
comment|/*-{ if(b)this.branch=b; }-*/
function_decl|;
DECL|method|legacy_id ()
specifier|public
specifier|final
name|Change
operator|.
name|Id
name|legacy_id
parameter_list|()
block|{
return|return
name|has_change_number
argument_list|()
condition|?
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|_change_number
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|patch_set_id ()
specifier|public
specifier|final
name|PatchSet
operator|.
name|Id
name|patch_set_id
parameter_list|()
block|{
return|return
name|has_change_number
argument_list|()
operator|&&
name|has_revision_number
argument_list|()
condition|?
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|legacy_id
argument_list|()
argument_list|,
name|_revision_number
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|has_change_number ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_change_number
parameter_list|()
comment|/*-{ return this.hasOwnProperty('_change_number') }-*/
function_decl|;
DECL|method|has_revision_number ()
specifier|final
specifier|native
name|boolean
name|has_revision_number
parameter_list|()
comment|/*-{ return this.hasOwnProperty('_revision_number') }-*/
function_decl|;
DECL|method|has_current_revision_number ()
specifier|final
specifier|native
name|boolean
name|has_current_revision_number
parameter_list|()
comment|/*-{ return this.hasOwnProperty('_current_revision_number') }-*/
function_decl|;
DECL|method|_change_number ()
specifier|final
specifier|native
name|int
name|_change_number
parameter_list|()
comment|/*-{ return this._change_number }-*/
function_decl|;
DECL|method|_revision_number ()
specifier|final
specifier|native
name|int
name|_revision_number
parameter_list|()
comment|/*-{ return this._revision_number }-*/
function_decl|;
DECL|method|_current_revision_number ()
specifier|final
specifier|native
name|int
name|_current_revision_number
parameter_list|()
comment|/*-{ return this._current_revision_number }-*/
function_decl|;
DECL|method|set_change_number (int n)
specifier|final
specifier|native
name|void
name|set_change_number
parameter_list|(
name|int
name|n
parameter_list|)
comment|/*-{ this._change_number=n; }-*/
function_decl|;
DECL|method|set_revision_number (int n)
specifier|final
specifier|native
name|void
name|set_revision_number
parameter_list|(
name|int
name|n
parameter_list|)
comment|/*-{ this._revision_number=n; }-*/
function_decl|;
DECL|method|set_current_revision_number (int n)
specifier|final
specifier|native
name|void
name|set_current_revision_number
parameter_list|(
name|int
name|n
parameter_list|)
comment|/*-{ this._current_revision_number=n; }-*/
function_decl|;
DECL|method|ChangeAndCommit ()
specifier|protected
name|ChangeAndCommit
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit


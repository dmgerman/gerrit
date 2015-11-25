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
name|info
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
name|info
operator|.
name|AccountInfo
operator|.
name|AvatarInfo
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
name|ApprovalInfo
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
name|common
operator|.
name|data
operator|.
name|LabelValue
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
name|NativeEvent
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
name|Grid
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
name|SafeHtml
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Displays a table of label and reviewer scores. */
end_comment

begin_class
DECL|class|Labels
class|class
name|Labels
extends|extends
name|Grid
block|{
DECL|field|DATA_ID
specifier|private
specifier|static
specifier|final
name|String
name|DATA_ID
init|=
literal|"data-id"
decl_stmt|;
DECL|field|DATA_VOTE
specifier|private
specifier|static
specifier|final
name|String
name|DATA_VOTE
init|=
literal|"data-vote"
decl_stmt|;
DECL|field|REMOVE_REVIEWER
specifier|private
specifier|static
specifier|final
name|String
name|REMOVE_REVIEWER
decl_stmt|;
DECL|field|REMOVE_VOTE
specifier|private
specifier|static
specifier|final
name|String
name|REMOVE_VOTE
decl_stmt|;
static|static
block|{
name|REMOVE_REVIEWER
operator|=
name|DOM
operator|.
name|createUniqueId
argument_list|()
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|REMOVE_VOTE
operator|=
name|DOM
operator|.
name|createUniqueId
argument_list|()
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|REMOVE_REVIEWER
argument_list|,
name|REMOVE_VOTE
argument_list|)
expr_stmt|;
block|}
DECL|method|init (String r, String v)
specifier|private
specifier|static
specifier|final
specifier|native
name|void
name|init
parameter_list|(
name|String
name|r
parameter_list|,
name|String
name|v
parameter_list|)
comment|/*-{     $wnd[r] = $entry(function(e) {       @com.google.gerrit.client.change.Labels::onRemoveReviewer(Lcom/google/gwt/dom/client/NativeEvent;)(e)     });     $wnd[v] = $entry(function(e) {       @com.google.gerrit.client.change.Labels::onRemoveVote(Lcom/google/gwt/dom/client/NativeEvent;)(e)     });   }-*/
function_decl|;
DECL|method|onRemoveReviewer (NativeEvent event)
specifier|private
specifier|static
name|void
name|onRemoveReviewer
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Integer
name|user
init|=
name|getDataId
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ChangeScreen
name|screen
init|=
name|ChangeScreen
operator|.
name|get
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|ChangeApi
operator|.
name|reviewer
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|user
argument_list|)
operator|.
name|delete
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JavaScriptObject
name|result
parameter_list|)
block|{
if|if
condition|(
name|screen
operator|.
name|isCurrentView
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onRemoveVote (NativeEvent event)
specifier|private
specifier|static
name|void
name|onRemoveVote
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Integer
name|user
init|=
name|getDataId
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|String
name|vote
init|=
name|getVoteId
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|vote
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ChangeScreen
name|screen
init|=
name|ChangeScreen
operator|.
name|get
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|ChangeApi
operator|.
name|vote
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|user
argument_list|,
name|vote
argument_list|)
operator|.
name|delete
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JavaScriptObject
name|result
parameter_list|)
block|{
if|if
condition|(
name|screen
operator|.
name|isCurrentView
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|screen
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDataId (NativeEvent event)
specifier|private
specifier|static
name|Integer
name|getDataId
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Element
name|e
init|=
name|event
operator|.
name|getEventTarget
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|e
operator|.
name|getAttribute
argument_list|(
name|DATA_ID
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
return|;
block|}
name|e
operator|=
name|e
operator|.
name|getParentElement
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getVoteId (NativeEvent event)
specifier|private
specifier|static
name|String
name|getVoteId
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Element
name|e
init|=
name|event
operator|.
name|getEventTarget
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|e
operator|.
name|getAttribute
argument_list|(
name|DATA_VOTE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|v
return|;
block|}
name|e
operator|=
name|e
operator|.
name|getParentElement
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|field|style
specifier|private
name|ChangeScreen
operator|.
name|Style
name|style
decl_stmt|;
DECL|method|init (ChangeScreen.Style style)
name|void
name|init
parameter_list|(
name|ChangeScreen
operator|.
name|Style
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
block|}
DECL|method|set (ChangeInfo info)
name|void
name|set
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|info
operator|.
name|labels
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|removable
init|=
name|info
operator|.
name|removableReviewerIds
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|resize
argument_list|(
name|names
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|0
init|;
name|row
operator|<
name|names
operator|.
name|size
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|String
name|name
init|=
name|names
operator|.
name|get
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|LabelInfo
name|label
init|=
name|info
operator|.
name|label
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|setText
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|label
operator|.
name|all
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|renderUsers
argument_list|(
name|label
argument_list|,
name|removable
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|style
operator|.
name|labelName
argument_list|()
argument_list|)
expr_stmt|;
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|getStyleForLabel
argument_list|(
name|label
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|renderUsers (LabelInfo label, Set<Integer> removable)
specifier|private
name|Widget
name|renderUsers
parameter_list|(
name|LabelInfo
name|label
parameter_list|,
name|Set
argument_list|<
name|Integer
argument_list|>
name|removable
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|ApprovalInfo
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|int
name|approved
init|=
literal|0
decl_stmt|;
name|int
name|rejected
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ApprovalInfo
name|ai
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|label
operator|.
name|all
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|value
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|List
argument_list|<
name|ApprovalInfo
argument_list|>
name|l
init|=
name|m
operator|.
name|get
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|ai
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|label
operator|.
name|all
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|ai
operator|.
name|value
argument_list|()
argument_list|)
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRejected
argument_list|(
name|label
argument_list|,
name|ai
argument_list|)
condition|)
block|{
name|rejected
operator|=
name|ai
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isApproved
argument_list|(
name|label
argument_list|,
name|ai
argument_list|)
condition|)
block|{
name|approved
operator|=
name|ai
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|SafeHtmlBuilder
name|html
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|v
range|:
name|sort
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
argument_list|,
name|approved
argument_list|,
name|rejected
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|html
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|html
operator|.
name|br
argument_list|()
expr_stmt|;
block|}
name|String
name|val
init|=
name|LabelValue
operator|.
name|formatValue
argument_list|(
name|v
operator|.
name|shortValue
argument_list|()
argument_list|)
decl_stmt|;
name|html
operator|.
name|openSpan
argument_list|()
expr_stmt|;
name|html
operator|.
name|setAttribute
argument_list|(
literal|"title"
argument_list|,
name|label
operator|.
name|valueText
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|.
name|intValue
argument_list|()
operator|==
name|approved
condition|)
block|{
name|html
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|label_ok
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|v
operator|.
name|intValue
argument_list|()
operator|==
name|rejected
condition|)
block|{
name|html
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|label_reject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|append
argument_list|(
name|val
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
name|formatUserList
argument_list|(
name|style
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|v
argument_list|)
argument_list|,
name|removable
argument_list|,
name|label
operator|.
name|name
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|html
operator|.
name|closeSpan
argument_list|()
expr_stmt|;
block|}
return|return
name|html
operator|.
name|toBlockWidget
argument_list|()
return|;
block|}
DECL|method|sort (Set<Integer> keySet, int a, int b)
specifier|private
specifier|static
name|List
argument_list|<
name|Integer
argument_list|>
name|sort
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|keySet
parameter_list|,
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keySet
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|r
argument_list|)
expr_stmt|;
if|if
condition|(
name|keySet
operator|.
name|contains
argument_list|(
name|a
argument_list|)
condition|)
block|{
name|r
operator|.
name|remove
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keySet
operator|.
name|contains
argument_list|(
name|b
argument_list|)
condition|)
block|{
name|r
operator|.
name|remove
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|isApproved (LabelInfo label, ApprovalInfo ai)
specifier|private
specifier|static
name|boolean
name|isApproved
parameter_list|(
name|LabelInfo
name|label
parameter_list|,
name|ApprovalInfo
name|ai
parameter_list|)
block|{
return|return
name|label
operator|.
name|approved
argument_list|()
operator|!=
literal|null
operator|&&
name|label
operator|.
name|approved
argument_list|()
operator|.
name|_accountId
argument_list|()
operator|==
name|ai
operator|.
name|_accountId
argument_list|()
return|;
block|}
DECL|method|isRejected (LabelInfo label, ApprovalInfo ai)
specifier|private
specifier|static
name|boolean
name|isRejected
parameter_list|(
name|LabelInfo
name|label
parameter_list|,
name|ApprovalInfo
name|ai
parameter_list|)
block|{
return|return
name|label
operator|.
name|rejected
argument_list|()
operator|!=
literal|null
operator|&&
name|label
operator|.
name|rejected
argument_list|()
operator|.
name|_accountId
argument_list|()
operator|==
name|ai
operator|.
name|_accountId
argument_list|()
return|;
block|}
DECL|method|getStyleForLabel (LabelInfo label)
specifier|private
name|String
name|getStyleForLabel
parameter_list|(
name|LabelInfo
name|label
parameter_list|)
block|{
switch|switch
condition|(
name|label
operator|.
name|status
argument_list|()
condition|)
block|{
case|case
name|OK
case|:
return|return
name|style
operator|.
name|label_ok
argument_list|()
return|;
case|case
name|NEED
case|:
return|return
name|style
operator|.
name|label_need
argument_list|()
return|;
case|case
name|REJECT
case|:
case|case
name|IMPOSSIBLE
case|:
return|return
name|style
operator|.
name|label_reject
argument_list|()
return|;
default|default:
case|case
name|MAY
case|:
return|return
name|style
operator|.
name|label_may
argument_list|()
return|;
block|}
block|}
DECL|method|formatUserList (ChangeScreen.Style style, Collection<? extends AccountInfo> in, Set<Integer> removable, String label, Map<Integer, VotableInfo> votable)
specifier|static
name|SafeHtml
name|formatUserList
parameter_list|(
name|ChangeScreen
operator|.
name|Style
name|style
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|AccountInfo
argument_list|>
name|in
parameter_list|,
name|Set
argument_list|<
name|Integer
argument_list|>
name|removable
parameter_list|,
name|String
name|label
parameter_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|VotableInfo
argument_list|>
name|votable
parameter_list|)
block|{
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|users
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|AccountInfo
name|a
parameter_list|,
name|AccountInfo
name|b
parameter_list|)
block|{
name|String
name|as
init|=
name|name
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|String
name|bs
init|=
name|name
argument_list|(
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
name|as
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|bs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|as
operator|.
name|compareTo
argument_list|(
name|bs
argument_list|)
return|;
block|}
specifier|private
name|String
name|name
parameter_list|(
name|AccountInfo
name|a
parameter_list|)
block|{
if|if
condition|(
name|a
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|a
operator|.
name|name
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|a
operator|.
name|email
argument_list|()
return|;
block|}
return|return
literal|""
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|SafeHtmlBuilder
name|html
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
extends|extends
name|AccountInfo
argument_list|>
name|itr
init|=
name|users
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AccountInfo
name|ai
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|AvatarInfo
name|img
init|=
name|ai
operator|.
name|avatar
argument_list|(
name|AvatarInfo
operator|.
name|DEFAULT_SIZE
argument_list|)
decl_stmt|;
name|String
name|name
decl_stmt|;
if|if
condition|(
name|ai
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|ai
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ai
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|ai
operator|.
name|email
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|ai
operator|.
name|_accountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|votableCategories
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|votable
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|s
init|=
name|votable
operator|.
name|get
argument_list|(
name|ai
operator|.
name|_accountId
argument_list|()
argument_list|)
operator|.
name|votableLabels
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|s
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|Util
operator|.
name|C
operator|.
name|votable
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|s
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|votableCategories
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
name|html
operator|.
name|openSpan
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"role"
argument_list|,
literal|"listitem"
argument_list|)
operator|.
name|setAttribute
argument_list|(
name|DATA_ID
argument_list|,
name|ai
operator|.
name|_accountId
argument_list|()
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"title"
argument_list|,
name|getTitle
argument_list|(
name|ai
argument_list|,
name|votableCategories
argument_list|)
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|label_user
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|html
operator|.
name|setAttribute
argument_list|(
name|DATA_VOTE
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|img
operator|!=
literal|null
condition|)
block|{
name|html
operator|.
name|openElement
argument_list|(
literal|"img"
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|avatar
argument_list|()
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"src"
argument_list|,
name|img
operator|.
name|url
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|img
operator|.
name|width
argument_list|()
operator|>
literal|0
condition|)
block|{
name|html
operator|.
name|setAttribute
argument_list|(
literal|"width"
argument_list|,
name|img
operator|.
name|width
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|img
operator|.
name|height
argument_list|()
operator|>
literal|0
condition|)
block|{
name|html
operator|.
name|setAttribute
argument_list|(
literal|"height"
argument_list|,
name|img
operator|.
name|height
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|closeSelf
argument_list|()
expr_stmt|;
block|}
name|html
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|removable
operator|.
name|contains
argument_list|(
name|ai
operator|.
name|_accountId
argument_list|()
argument_list|)
condition|)
block|{
name|html
operator|.
name|openElement
argument_list|(
literal|"button"
argument_list|)
expr_stmt|;
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|html
operator|.
name|setAttribute
argument_list|(
literal|"title"
argument_list|,
name|Util
operator|.
name|M
operator|.
name|removeVote
argument_list|(
name|label
argument_list|)
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"onclick"
argument_list|,
name|REMOVE_VOTE
operator|+
literal|"(event)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|html
operator|.
name|setAttribute
argument_list|(
literal|"title"
argument_list|,
name|Util
operator|.
name|M
operator|.
name|removeReviewer
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"onclick"
argument_list|,
name|REMOVE_REVIEWER
operator|+
literal|"(event)"
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|append
argument_list|(
literal|"Ã"
argument_list|)
operator|.
name|closeElement
argument_list|(
literal|"button"
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|closeSpan
argument_list|()
expr_stmt|;
if|if
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|html
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|html
return|;
block|}
DECL|method|getTitle (AccountInfo ai, String votableCategories)
specifier|private
specifier|static
name|String
name|getTitle
parameter_list|(
name|AccountInfo
name|ai
parameter_list|,
name|String
name|votableCategories
parameter_list|)
block|{
name|String
name|title
init|=
name|ai
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|?
name|ai
operator|.
name|email
argument_list|()
else|:
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|votableCategories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|title
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|title
operator|+=
literal|" "
expr_stmt|;
block|}
name|title
operator|+=
name|votableCategories
expr_stmt|;
block|}
return|return
name|title
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|NotFoundScreen
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
name|rpc
operator|.
name|ScreenLoadCallback
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
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
name|KeyCommand
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

begin_class
DECL|class|AccountDashboardScreen
specifier|public
class|class
name|AccountDashboardScreen
extends|extends
name|Screen
implements|implements
name|ChangeListScreen
block|{
DECL|field|ownerId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|ownerId
decl_stmt|;
DECL|field|mine
specifier|private
specifier|final
name|boolean
name|mine
decl_stmt|;
DECL|field|table
specifier|private
name|ChangeTable2
name|table
decl_stmt|;
DECL|field|outgoing
specifier|private
name|ChangeTable2
operator|.
name|Section
name|outgoing
decl_stmt|;
DECL|field|incoming
specifier|private
name|ChangeTable2
operator|.
name|Section
name|incoming
decl_stmt|;
DECL|field|closed
specifier|private
name|ChangeTable2
operator|.
name|Section
name|closed
decl_stmt|;
DECL|method|AccountDashboardScreen (final Account.Id id)
specifier|public
name|AccountDashboardScreen
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|ownerId
operator|=
name|id
expr_stmt|;
name|mine
operator|=
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|ownerId
operator|.
name|equals
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
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
name|table
operator|=
operator|new
name|ChangeTable2
argument_list|()
block|{
block|{
name|keysNavigation
operator|.
name|add
argument_list|(
operator|new
name|KeyCommand
argument_list|(
literal|0
argument_list|,
literal|'R'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|keyReloadSearch
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|table
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|accountDashboard
argument_list|()
argument_list|)
expr_stmt|;
name|outgoing
operator|=
operator|new
name|ChangeTable2
operator|.
name|Section
argument_list|()
expr_stmt|;
name|incoming
operator|=
operator|new
name|ChangeTable2
operator|.
name|Section
argument_list|()
expr_stmt|;
name|closed
operator|=
operator|new
name|ChangeTable2
operator|.
name|Section
argument_list|()
expr_stmt|;
name|outgoing
operator|.
name|setTitleText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|outgoingReviews
argument_list|()
argument_list|)
expr_stmt|;
name|incoming
operator|.
name|setTitleText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|incomingReviews
argument_list|()
argument_list|)
expr_stmt|;
name|incoming
operator|.
name|setHighlightUnreviewed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|closed
operator|.
name|setTitleText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|recentlyClosed
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|addSection
argument_list|(
name|outgoing
argument_list|)
expr_stmt|;
name|table
operator|.
name|addSection
argument_list|(
name|incoming
argument_list|)
expr_stmt|;
name|table
operator|.
name|addSection
argument_list|(
name|closed
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|table
operator|.
name|setSavePointerId
argument_list|(
literal|"owner:"
operator|+
name|ownerId
argument_list|)
expr_stmt|;
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
name|String
name|who
init|=
name|mine
condition|?
literal|"self"
else|:
name|ownerId
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ChangeList
operator|.
name|query
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|"is:open owner:"
operator|+
name|who
argument_list|,
literal|"is:open reviewer:"
operator|+
name|who
operator|+
literal|" -owner:"
operator|+
name|who
argument_list|,
literal|"is:closed owner:"
operator|+
name|who
operator|+
literal|" -age:4w limit:10"
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
name|table
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|display (JsArray<ChangeList> result)
specifier|private
name|void
name|display
parameter_list|(
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
name|result
parameter_list|)
block|{
if|if
condition|(
operator|!
name|mine
operator|&&
operator|!
name|hasChanges
argument_list|(
name|result
argument_list|)
condition|)
block|{
comment|// When no results are returned and the data is not for the
comment|// current user, the target user is presumed to not exist.
name|Gerrit
operator|.
name|display
argument_list|(
name|getToken
argument_list|()
argument_list|,
operator|new
name|NotFoundScreen
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|ChangeList
name|out
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ChangeList
name|in
init|=
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ChangeList
name|done
init|=
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|mine
condition|)
block|{
name|setWindowTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|myDashboardTitle
argument_list|()
argument_list|)
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|myDashboardTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// The server doesn't tell us who the dashboard is for. Try to guess
comment|// by looking at a change started by the owner and extract the name.
name|String
name|name
init|=
name|guessName
argument_list|(
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|guessName
argument_list|(
name|done
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|setWindowTitle
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|M
operator|.
name|accountDashboardTitle
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setWindowTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|unknownDashboardTitle
argument_list|()
argument_list|)
expr_stmt|;
name|setWindowTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|unknownDashboardTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|out
argument_list|)
argument_list|,
name|outComparator
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|updateColumnsForLabels
argument_list|(
name|out
argument_list|,
name|in
argument_list|,
name|done
argument_list|)
expr_stmt|;
name|outgoing
operator|.
name|display
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|incoming
operator|.
name|display
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|closed
operator|.
name|display
argument_list|(
name|done
argument_list|)
expr_stmt|;
name|table
operator|.
name|finishDisplay
argument_list|()
expr_stmt|;
block|}
DECL|method|outComparator ()
specifier|private
name|Comparator
argument_list|<
name|ChangeInfo
argument_list|>
name|outComparator
parameter_list|()
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|ChangeInfo
name|a
parameter_list|,
name|ChangeInfo
name|b
parameter_list|)
block|{
name|int
name|cmp
init|=
name|a
operator|.
name|created
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|created
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
return|return
name|cmp
return|;
return|return
name|a
operator|.
name|_number
argument_list|()
operator|-
name|b
operator|.
name|_number
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|hasChanges (JsArray<ChangeList> result)
specifier|private
name|boolean
name|hasChanges
parameter_list|(
name|JsArray
argument_list|<
name|ChangeList
argument_list|>
name|result
parameter_list|)
block|{
for|for
control|(
name|ChangeList
name|list
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|result
argument_list|)
control|)
block|{
if|if
condition|(
name|list
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|guessName (ChangeList list)
specifier|private
specifier|static
name|String
name|guessName
parameter_list|(
name|ChangeList
name|list
parameter_list|)
block|{
for|for
control|(
name|ChangeInfo
name|change
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|list
argument_list|)
control|)
block|{
if|if
condition|(
name|change
operator|.
name|owner
argument_list|()
operator|!=
literal|null
operator|&&
name|change
operator|.
name|owner
argument_list|()
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|change
operator|.
name|owner
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit


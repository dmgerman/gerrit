begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|Link
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
name|data
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
name|Event
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
name|Hyperlink
import|;
end_import

begin_class
DECL|class|ChangeLink
specifier|public
class|class
name|ChangeLink
extends|extends
name|Hyperlink
block|{
DECL|field|change
specifier|private
name|ChangeInfo
name|change
decl_stmt|;
DECL|method|ChangeLink (final String text, final ChangeInfo c)
specifier|public
name|ChangeLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|Link
operator|.
name|toChange
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|change
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onBrowserEvent (final Event event)
specifier|public
name|void
name|onBrowserEvent
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
if|if
condition|(
name|DOM
operator|.
name|eventGetType
argument_list|(
name|event
argument_list|)
operator|==
name|Event
operator|.
name|ONCLICK
condition|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|getTargetHistoryToken
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|display
argument_list|(
operator|new
name|ChangeScreen
argument_list|(
name|change
argument_list|)
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|eventPreventDefault
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


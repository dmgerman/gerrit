begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Accessibility
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
name|Widget
import|;
end_import

begin_class
DECL|class|DownloadUrlPanel
class|class
name|DownloadUrlPanel
extends|extends
name|FlowPanel
block|{
DECL|field|commandPanel
specifier|private
specifier|final
name|DownloadCommandPanel
name|commandPanel
decl_stmt|;
DECL|method|DownloadUrlPanel (final DownloadCommandPanel commandPanel)
name|DownloadUrlPanel
parameter_list|(
specifier|final
name|DownloadCommandPanel
name|commandPanel
parameter_list|)
block|{
name|this
operator|.
name|commandPanel
operator|=
name|commandPanel
expr_stmt|;
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|downloadLinkList
argument_list|()
argument_list|)
expr_stmt|;
name|Accessibility
operator|.
name|setRole
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|Accessibility
operator|.
name|ROLE_TABLIST
argument_list|)
expr_stmt|;
block|}
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|getWidgetCount
argument_list|()
operator|==
literal|0
return|;
block|}
DECL|method|select (AccountGeneralPreferences.DownloadScheme urlType)
name|void
name|select
parameter_list|(
name|AccountGeneralPreferences
operator|.
name|DownloadScheme
name|urlType
parameter_list|)
block|{
name|DownloadUrlLink
name|first
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Widget
name|w
range|:
name|this
control|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|DownloadUrlLink
condition|)
block|{
specifier|final
name|DownloadUrlLink
name|d
init|=
operator|(
name|DownloadUrlLink
operator|)
name|w
decl_stmt|;
if|if
condition|(
name|first
operator|==
literal|null
condition|)
block|{
name|first
operator|=
name|d
expr_stmt|;
block|}
if|if
condition|(
name|d
operator|.
name|urlType
operator|==
name|urlType
condition|)
block|{
name|d
operator|.
name|select
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|// If none matched the requested type, select the first in the
comment|// group as that will at least give us an initial baseline.
if|if
condition|(
name|first
operator|!=
literal|null
condition|)
block|{
name|first
operator|.
name|select
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setCurrentUrl (DownloadUrlLink link)
name|void
name|setCurrentUrl
parameter_list|(
name|DownloadUrlLink
name|link
parameter_list|)
block|{
name|commandPanel
operator|.
name|setCurrentUrl
argument_list|(
name|link
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


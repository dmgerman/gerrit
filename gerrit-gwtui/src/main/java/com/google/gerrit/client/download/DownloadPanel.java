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
DECL|package|com.google.gerrit.client.download
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|download
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
name|config
operator|.
name|DownloadInfo
operator|.
name|DownloadCommandInfo
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
name|config
operator|.
name|DownloadInfo
operator|.
name|DownloadSchemeInfo
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
name|InlineLabel
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|DownloadPanel
specifier|public
specifier|abstract
class|class
name|DownloadPanel
extends|extends
name|FlowPanel
block|{
DECL|field|project
specifier|protected
specifier|final
name|String
name|project
decl_stmt|;
DECL|field|commands
specifier|private
specifier|final
name|DownloadCommandPanel
name|commands
init|=
operator|new
name|DownloadCommandPanel
argument_list|()
decl_stmt|;
DECL|field|urls
specifier|private
specifier|final
name|DownloadUrlPanel
name|urls
init|=
operator|new
name|DownloadUrlPanel
argument_list|()
decl_stmt|;
DECL|field|copyLabel
specifier|private
specifier|final
name|CopyableLabel
name|copyLabel
init|=
operator|new
name|CopyableLabel
argument_list|(
literal|""
argument_list|)
decl_stmt|;
DECL|method|DownloadPanel (String project, boolean allowAnonymous)
specifier|public
name|DownloadPanel
parameter_list|(
name|String
name|project
parameter_list|,
name|boolean
name|allowAnonymous
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|copyLabel
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
name|downloadLinkCopyLabel
argument_list|()
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|DownloadUrlLink
operator|.
name|createDownloadUrlLinks
argument_list|(
name|allowAnonymous
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|setupWidgets
argument_list|()
expr_stmt|;
block|}
DECL|method|setupWidgets ()
specifier|private
name|void
name|setupWidgets
parameter_list|()
block|{
if|if
condition|(
operator|!
name|urls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|urls
operator|.
name|select
argument_list|(
name|Gerrit
operator|.
name|getUserPreferences
argument_list|()
operator|.
name|downloadScheme
argument_list|()
argument_list|)
expr_stmt|;
name|FlowPanel
name|p
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|p
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
name|downloadLinkHeader
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|commands
argument_list|)
expr_stmt|;
specifier|final
name|InlineLabel
name|glue
init|=
operator|new
name|InlineLabel
argument_list|()
decl_stmt|;
name|glue
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
name|downloadLinkHeaderGap
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|glue
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|urls
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|copyLabel
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateDownloadCommandLinks (DownloadSchemeInfo schemeInfo)
name|void
name|populateDownloadCommandLinks
parameter_list|(
name|DownloadSchemeInfo
name|schemeInfo
parameter_list|)
block|{
name|commands
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|DownloadCommandInfo
name|cmd
range|:
name|getCommands
argument_list|(
name|schemeInfo
argument_list|)
control|)
block|{
name|commands
operator|.
name|add
argument_list|(
operator|new
name|DownloadCommandLink
argument_list|(
name|copyLabel
argument_list|,
name|cmd
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|commands
operator|.
name|select
argument_list|()
expr_stmt|;
block|}
DECL|method|getCommands ( DownloadSchemeInfo schemeInfo)
specifier|protected
specifier|abstract
name|Set
argument_list|<
name|DownloadCommandInfo
argument_list|>
name|getCommands
parameter_list|(
name|DownloadSchemeInfo
name|schemeInfo
parameter_list|)
function_decl|;
block|}
end_class

end_unit


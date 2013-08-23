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
name|changes
operator|.
name|ChangeInfo
operator|.
name|FetchInfo
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
name|rpc
operator|.
name|NativeMap
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
DECL|class|DownloadAction
class|class
name|DownloadAction
extends|extends
name|RightSidePopdownAction
block|{
DECL|field|downloadBox
specifier|private
specifier|final
name|DownloadBox
name|downloadBox
decl_stmt|;
DECL|method|DownloadAction ( Change.Id changeId, String project, RevisionInfo revision, ChangeScreen2.Style style, UIObject relativeTo, Widget downloadButton)
name|DownloadAction
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|project
parameter_list|,
name|RevisionInfo
name|revision
parameter_list|,
name|ChangeScreen2
operator|.
name|Style
name|style
parameter_list|,
name|UIObject
name|relativeTo
parameter_list|,
name|Widget
name|downloadButton
parameter_list|)
block|{
name|super
argument_list|(
name|style
argument_list|,
name|relativeTo
argument_list|,
name|downloadButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|downloadBox
operator|=
operator|new
name|DownloadBox
argument_list|(
name|revision
operator|.
name|has_fetch
argument_list|()
condition|?
name|revision
operator|.
name|fetch
argument_list|()
else|:
name|NativeMap
operator|.
expr|<
name|FetchInfo
operator|>
name|create
argument_list|()
argument_list|,
name|revision
operator|.
name|name
argument_list|()
argument_list|,
name|project
argument_list|,
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|changeId
argument_list|,
name|revision
operator|.
name|_number
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getWidget ()
name|Widget
name|getWidget
parameter_list|()
block|{
return|return
name|downloadBox
return|;
block|}
block|}
end_class

end_unit


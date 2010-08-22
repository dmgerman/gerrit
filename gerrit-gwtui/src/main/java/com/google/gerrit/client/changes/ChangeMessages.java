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
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Messages
import|;
end_import

begin_interface
DECL|interface|ChangeMessages
specifier|public
interface|interface
name|ChangeMessages
extends|extends
name|Messages
block|{
DECL|method|accountDashboardTitle (String fullName)
name|String
name|accountDashboardTitle
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|changesStartedBy (String fullName)
name|String
name|changesStartedBy
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|changesReviewableBy (String fullName)
name|String
name|changesReviewableBy
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|changesOpenInProject (String string)
name|String
name|changesOpenInProject
parameter_list|(
name|String
name|string
parameter_list|)
function_decl|;
DECL|method|changesMergedInProject (String string)
name|String
name|changesMergedInProject
parameter_list|(
name|String
name|string
parameter_list|)
function_decl|;
DECL|method|changesAbandonedInProject (String string)
name|String
name|changesAbandonedInProject
parameter_list|(
name|String
name|string
parameter_list|)
function_decl|;
DECL|method|changeScreenTitleId (String changeId)
name|String
name|changeScreenTitleId
parameter_list|(
name|String
name|changeId
parameter_list|)
function_decl|;
DECL|method|patchSetHeader (int id)
name|String
name|patchSetHeader
parameter_list|(
name|int
name|id
parameter_list|)
function_decl|;
DECL|method|loadingPatchSet (int id)
name|String
name|loadingPatchSet
parameter_list|(
name|int
name|id
parameter_list|)
function_decl|;
DECL|method|submitPatchSet (int id)
name|String
name|submitPatchSet
parameter_list|(
name|int
name|id
parameter_list|)
function_decl|;
DECL|method|patchTableComments (@luralCount int count)
name|String
name|patchTableComments
parameter_list|(
annotation|@
name|PluralCount
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|patchTableDrafts (@luralCount int count)
name|String
name|patchTableDrafts
parameter_list|(
annotation|@
name|PluralCount
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|patchTableSize_Modify (int insertions, int deletions)
name|String
name|patchTableSize_Modify
parameter_list|(
name|int
name|insertions
parameter_list|,
name|int
name|deletions
parameter_list|)
function_decl|;
DECL|method|patchTableSize_Lines (@luralCount int insertions)
name|String
name|patchTableSize_Lines
parameter_list|(
annotation|@
name|PluralCount
name|int
name|insertions
parameter_list|)
function_decl|;
DECL|method|removeReviewer (String fullName)
name|String
name|removeReviewer
parameter_list|(
name|String
name|fullName
parameter_list|)
function_decl|;
DECL|method|messageWrittenOn (String date)
name|String
name|messageWrittenOn
parameter_list|(
name|String
name|date
parameter_list|)
function_decl|;
DECL|method|renamedFrom (String sourcePath)
name|String
name|renamedFrom
parameter_list|(
name|String
name|sourcePath
parameter_list|)
function_decl|;
DECL|method|copiedFrom (String sourcePath)
name|String
name|copiedFrom
parameter_list|(
name|String
name|sourcePath
parameter_list|)
function_decl|;
DECL|method|otherFrom (String sourcePath)
name|String
name|otherFrom
parameter_list|(
name|String
name|sourcePath
parameter_list|)
function_decl|;
DECL|method|needApproval (String categoryName, String value, String valueName)
name|String
name|needApproval
parameter_list|(
name|String
name|categoryName
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|valueName
parameter_list|)
function_decl|;
DECL|method|publishComments (String changeId, int ps)
name|String
name|publishComments
parameter_list|(
name|String
name|changeId
parameter_list|,
name|int
name|ps
parameter_list|)
function_decl|;
DECL|method|lineHeader (int line)
name|String
name|lineHeader
parameter_list|(
name|int
name|line
parameter_list|)
function_decl|;
DECL|method|changeQueryWindowTitle (String query)
name|String
name|changeQueryWindowTitle
parameter_list|(
name|String
name|query
parameter_list|)
function_decl|;
DECL|method|changeQueryPageTitle (String query)
name|String
name|changeQueryPageTitle
parameter_list|(
name|String
name|query
parameter_list|)
function_decl|;
DECL|method|accountNotFound (String who)
name|String
name|accountNotFound
parameter_list|(
name|String
name|who
parameter_list|)
function_decl|;
DECL|method|changeNotVisibleTo (String who)
name|String
name|changeNotVisibleTo
parameter_list|(
name|String
name|who
parameter_list|)
function_decl|;
DECL|method|anonymousDownload (String protocol)
name|String
name|anonymousDownload
parameter_list|(
name|String
name|protocol
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


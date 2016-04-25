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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|Constants
import|;
end_import

begin_interface
DECL|interface|PatchConstants
specifier|public
interface|interface
name|PatchConstants
extends|extends
name|Constants
block|{
DECL|method|patchBase ()
name|String
name|patchBase
parameter_list|()
function_decl|;
DECL|method|patchSet ()
name|String
name|patchSet
parameter_list|()
function_decl|;
DECL|method|upToChange ()
name|String
name|upToChange
parameter_list|()
function_decl|;
DECL|method|openReply ()
name|String
name|openReply
parameter_list|()
function_decl|;
DECL|method|linePrev ()
name|String
name|linePrev
parameter_list|()
function_decl|;
DECL|method|lineNext ()
name|String
name|lineNext
parameter_list|()
function_decl|;
DECL|method|chunkPrev ()
name|String
name|chunkPrev
parameter_list|()
function_decl|;
DECL|method|chunkNext ()
name|String
name|chunkNext
parameter_list|()
function_decl|;
DECL|method|commentPrev ()
name|String
name|commentPrev
parameter_list|()
function_decl|;
DECL|method|commentNext ()
name|String
name|commentNext
parameter_list|()
function_decl|;
DECL|method|focusSideA ()
name|String
name|focusSideA
parameter_list|()
function_decl|;
DECL|method|focusSideB ()
name|String
name|focusSideB
parameter_list|()
function_decl|;
DECL|method|expandComment ()
name|String
name|expandComment
parameter_list|()
function_decl|;
DECL|method|expandAllCommentsOnCurrentLine ()
name|String
name|expandAllCommentsOnCurrentLine
parameter_list|()
function_decl|;
DECL|method|toggleSideA ()
name|String
name|toggleSideA
parameter_list|()
function_decl|;
DECL|method|toggleIntraline ()
name|String
name|toggleIntraline
parameter_list|()
function_decl|;
DECL|method|showPreferences ()
name|String
name|showPreferences
parameter_list|()
function_decl|;
DECL|method|toggleReviewed ()
name|String
name|toggleReviewed
parameter_list|()
function_decl|;
DECL|method|markAsReviewedAndGoToNext ()
name|String
name|markAsReviewedAndGoToNext
parameter_list|()
function_decl|;
DECL|method|commentEditorSet ()
name|String
name|commentEditorSet
parameter_list|()
function_decl|;
DECL|method|commentInsert ()
name|String
name|commentInsert
parameter_list|()
function_decl|;
DECL|method|commentSaveDraft ()
name|String
name|commentSaveDraft
parameter_list|()
function_decl|;
DECL|method|commentCancelEdit ()
name|String
name|commentCancelEdit
parameter_list|()
function_decl|;
DECL|method|whitespaceIGNORE_NONE ()
name|String
name|whitespaceIGNORE_NONE
parameter_list|()
function_decl|;
DECL|method|whitespaceIGNORE_TRAILING ()
name|String
name|whitespaceIGNORE_TRAILING
parameter_list|()
function_decl|;
DECL|method|whitespaceIGNORE_LEADING_AND_TRAILING ()
name|String
name|whitespaceIGNORE_LEADING_AND_TRAILING
parameter_list|()
function_decl|;
DECL|method|whitespaceIGNORE_ALL ()
name|String
name|whitespaceIGNORE_ALL
parameter_list|()
function_decl|;
DECL|method|previousFileHelp ()
name|String
name|previousFileHelp
parameter_list|()
function_decl|;
DECL|method|nextFileHelp ()
name|String
name|nextFileHelp
parameter_list|()
function_decl|;
DECL|method|download ()
name|String
name|download
parameter_list|()
function_decl|;
DECL|method|edit ()
name|String
name|edit
parameter_list|()
function_decl|;
DECL|method|blame ()
name|String
name|blame
parameter_list|()
function_decl|;
DECL|method|addFileCommentToolTip ()
name|String
name|addFileCommentToolTip
parameter_list|()
function_decl|;
DECL|method|cannedReplyDone ()
name|String
name|cannedReplyDone
parameter_list|()
function_decl|;
DECL|method|sideBySideDiff ()
name|String
name|sideBySideDiff
parameter_list|()
function_decl|;
DECL|method|unifiedDiff ()
name|String
name|unifiedDiff
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


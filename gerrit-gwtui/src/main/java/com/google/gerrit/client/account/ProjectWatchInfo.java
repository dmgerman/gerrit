begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_class
DECL|class|ProjectWatchInfo
specifier|public
class|class
name|ProjectWatchInfo
extends|extends
name|JavaScriptObject
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|NEW_CHANGES
name|NEW_CHANGES
block|,
DECL|enumConstant|NEW_PATCHSETS
name|NEW_PATCHSETS
block|,
DECL|enumConstant|ALL_COMMENTS
name|ALL_COMMENTS
block|,
DECL|enumConstant|SUBMITTED_CHANGES
name|SUBMITTED_CHANGES
block|,
DECL|enumConstant|ABANDONED_CHANGES
name|ABANDONED_CHANGES
block|}
DECL|method|project ()
specifier|public
specifier|final
specifier|native
name|String
name|project
parameter_list|()
comment|/*-{ return this.project; }-*/
function_decl|;
DECL|method|filter ()
specifier|public
specifier|final
specifier|native
name|String
name|filter
parameter_list|()
comment|/*-{ return this.filter; }-*/
function_decl|;
DECL|method|project (String s)
specifier|public
specifier|final
specifier|native
name|void
name|project
parameter_list|(
name|String
name|s
parameter_list|)
comment|/*-{ this.project = s; }-*/
function_decl|;
DECL|method|filter (String s)
specifier|public
specifier|final
specifier|native
name|void
name|filter
parameter_list|(
name|String
name|s
parameter_list|)
comment|/*-{ this.filter = s; }-*/
function_decl|;
DECL|method|notify (ProjectWatchInfo.Type t, Boolean b)
specifier|public
specifier|final
name|void
name|notify
parameter_list|(
name|ProjectWatchInfo
operator|.
name|Type
name|t
parameter_list|,
name|Boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
name|ProjectWatchInfo
operator|.
name|Type
operator|.
name|NEW_CHANGES
condition|)
block|{
name|notifyNewChanges
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|NEW_PATCHSETS
condition|)
block|{
name|notifyNewPatchSets
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|ALL_COMMENTS
condition|)
block|{
name|notifyAllComments
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|SUBMITTED_CHANGES
condition|)
block|{
name|notifySubmittedChanges
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|ABANDONED_CHANGES
condition|)
block|{
name|notifyAbandonedChanges
argument_list|(
name|b
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|notify (ProjectWatchInfo.Type t)
specifier|public
specifier|final
name|Boolean
name|notify
parameter_list|(
name|ProjectWatchInfo
operator|.
name|Type
name|t
parameter_list|)
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|t
operator|==
name|ProjectWatchInfo
operator|.
name|Type
operator|.
name|NEW_CHANGES
condition|)
block|{
name|b
operator|=
name|notifyNewChanges
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|NEW_PATCHSETS
condition|)
block|{
name|b
operator|=
name|notifyNewPatchSets
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|ALL_COMMENTS
condition|)
block|{
name|b
operator|=
name|notifyAllComments
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|SUBMITTED_CHANGES
condition|)
block|{
name|b
operator|=
name|notifySubmittedChanges
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|==
name|Type
operator|.
name|ABANDONED_CHANGES
condition|)
block|{
name|b
operator|=
name|notifyAbandonedChanges
argument_list|()
expr_stmt|;
block|}
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|b
argument_list|)
return|;
block|}
DECL|method|notifyNewChanges ()
specifier|private
specifier|native
name|boolean
name|notifyNewChanges
parameter_list|()
comment|/*-{ return this['notify_new_changes'] ? true : false; }-*/
function_decl|;
DECL|method|notifyNewPatchSets ()
specifier|private
specifier|native
name|boolean
name|notifyNewPatchSets
parameter_list|()
comment|/*-{ return this['notify_new_patch_sets'] ? true : false; }-*/
function_decl|;
DECL|method|notifyAllComments ()
specifier|private
specifier|native
name|boolean
name|notifyAllComments
parameter_list|()
comment|/*-{ return this['notify_all_comments'] ? true : false; }-*/
function_decl|;
DECL|method|notifySubmittedChanges ()
specifier|private
specifier|native
name|boolean
name|notifySubmittedChanges
parameter_list|()
comment|/*-{ return this['notify_submitted_changes'] ? true : false; }-*/
function_decl|;
DECL|method|notifyAbandonedChanges ()
specifier|private
specifier|native
name|boolean
name|notifyAbandonedChanges
parameter_list|()
comment|/*-{ return this['notify_abandoned_changes'] ? true : false; }-*/
function_decl|;
DECL|method|notifyNewChanges (boolean b)
specifier|private
specifier|native
name|void
name|notifyNewChanges
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this['notify_new_changes'] = b ? true : null; }-*/
function_decl|;
DECL|method|notifyNewPatchSets (boolean b)
specifier|private
specifier|native
name|void
name|notifyNewPatchSets
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this['notify_new_patch_sets'] = b ? true : null; }-*/
function_decl|;
DECL|method|notifyAllComments (boolean b)
specifier|private
specifier|native
name|void
name|notifyAllComments
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this['notify_all_comments'] = b ? true : null; }-*/
function_decl|;
DECL|method|notifySubmittedChanges (boolean b)
specifier|private
specifier|native
name|void
name|notifySubmittedChanges
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this['notify_submitted_changes'] = b ? true : null; }-*/
function_decl|;
DECL|method|notifyAbandonedChanges (boolean b)
specifier|private
specifier|native
name|void
name|notifyAbandonedChanges
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this['notify_abandoned_changes'] = b ? true : null; }-*/
function_decl|;
DECL|method|ProjectWatchInfo ()
specifier|protected
name|ProjectWatchInfo
parameter_list|()
block|{    }
block|}
end_class

end_unit


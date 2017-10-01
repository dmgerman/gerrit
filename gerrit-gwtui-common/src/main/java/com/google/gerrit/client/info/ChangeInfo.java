begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.info
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|info
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
name|client
operator|.
name|rpc
operator|.
name|NativeString
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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|SubmitRecord
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
name|extensions
operator|.
name|client
operator|.
name|ReviewerState
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
name|extensions
operator|.
name|client
operator|.
name|SubmitType
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|impl
operator|.
name|ser
operator|.
name|JavaSqlTimestamp_JsonSerializer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
DECL|class|ChangeInfo
specifier|public
class|class
name|ChangeInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|init ()
specifier|public
specifier|final
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|allLabels
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|allLabels
argument_list|()
operator|.
name|copyKeysIntoChildren
argument_list|(
literal|"_name"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|projectNameKey ()
specifier|public
specifier|final
name|Project
operator|.
name|NameKey
name|projectNameKey
parameter_list|()
block|{
return|return
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|project
argument_list|()
argument_list|)
return|;
block|}
DECL|method|legacyId ()
specifier|public
specifier|final
name|Change
operator|.
name|Id
name|legacyId
parameter_list|()
block|{
return|return
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|_number
argument_list|()
argument_list|)
return|;
block|}
DECL|method|created ()
specifier|public
specifier|final
name|Timestamp
name|created
parameter_list|()
block|{
name|Timestamp
name|ts
init|=
name|_getCts
argument_list|()
decl_stmt|;
if|if
condition|(
name|ts
operator|==
literal|null
condition|)
block|{
name|ts
operator|=
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|createdRaw
argument_list|()
argument_list|)
expr_stmt|;
name|_setCts
argument_list|(
name|ts
argument_list|)
expr_stmt|;
block|}
return|return
name|ts
return|;
block|}
DECL|method|hasEditBasedOnCurrentPatchSet ()
specifier|public
specifier|final
name|boolean
name|hasEditBasedOnCurrentPatchSet
parameter_list|()
block|{
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|revList
init|=
name|revisions
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|RevisionInfo
operator|.
name|sortRevisionInfoByNumber
argument_list|(
name|revList
argument_list|)
expr_stmt|;
return|return
name|revList
operator|.
name|get
argument_list|(
name|revList
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|isEdit
argument_list|()
return|;
block|}
DECL|method|_getCts ()
specifier|private
specifier|native
name|Timestamp
name|_getCts
parameter_list|()
comment|/*-{ return this._cts; }-*/
function_decl|;
DECL|method|_setCts (Timestamp ts)
specifier|private
specifier|native
name|void
name|_setCts
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
comment|/*-{ this._cts = ts; }-*/
function_decl|;
DECL|method|updated ()
specifier|public
specifier|final
name|Timestamp
name|updated
parameter_list|()
block|{
return|return
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|updatedRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|submitted ()
specifier|public
specifier|final
name|Timestamp
name|submitted
parameter_list|()
block|{
return|return
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|submittedRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|idAbbreviated ()
specifier|public
specifier|final
name|String
name|idAbbreviated
parameter_list|()
block|{
return|return
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|changeId
argument_list|()
argument_list|)
operator|.
name|abbreviate
argument_list|()
return|;
block|}
DECL|method|status ()
specifier|public
specifier|final
name|Change
operator|.
name|Status
name|status
parameter_list|()
block|{
return|return
name|Change
operator|.
name|Status
operator|.
name|valueOf
argument_list|(
name|statusRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|labels ()
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|labels
parameter_list|()
block|{
return|return
name|allLabels
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|removableReviewerIds ()
specifier|public
specifier|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|removableReviewerIds
parameter_list|()
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|removable
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|removableReviewers
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AccountInfo
name|a
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|removableReviewers
argument_list|()
argument_list|)
control|)
block|{
name|removable
operator|.
name|add
argument_list|(
name|a
operator|.
name|_accountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|removable
return|;
block|}
DECL|method|id ()
specifier|public
specifier|final
specifier|native
name|String
name|id
parameter_list|()
comment|/*-{ return this.id; }-*/
function_decl|;
DECL|method|project ()
specifier|public
specifier|final
specifier|native
name|String
name|project
parameter_list|()
comment|/*-{ return this.project; }-*/
function_decl|;
DECL|method|branch ()
specifier|public
specifier|final
specifier|native
name|String
name|branch
parameter_list|()
comment|/*-{ return this.branch; }-*/
function_decl|;
DECL|method|topic ()
specifier|public
specifier|final
specifier|native
name|String
name|topic
parameter_list|()
comment|/*-{ return this.topic; }-*/
function_decl|;
DECL|method|changeId ()
specifier|public
specifier|final
specifier|native
name|String
name|changeId
parameter_list|()
comment|/*-{ return this.change_id; }-*/
function_decl|;
DECL|method|mergeable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|mergeable
parameter_list|()
comment|/*-{ return this.mergeable ? true : false; }-*/
function_decl|;
DECL|method|insertions ()
specifier|public
specifier|final
specifier|native
name|int
name|insertions
parameter_list|()
comment|/*-{ return this.insertions; }-*/
function_decl|;
DECL|method|deletions ()
specifier|public
specifier|final
specifier|native
name|int
name|deletions
parameter_list|()
comment|/*-{ return this.deletions; }-*/
function_decl|;
DECL|method|statusRaw ()
specifier|private
specifier|native
name|String
name|statusRaw
parameter_list|()
comment|/*-{ return this.status; }-*/
function_decl|;
DECL|method|subject ()
specifier|public
specifier|final
specifier|native
name|String
name|subject
parameter_list|()
comment|/*-{ return this.subject; }-*/
function_decl|;
DECL|method|owner ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|owner
parameter_list|()
comment|/*-{ return this.owner; }-*/
function_decl|;
DECL|method|assignee ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|assignee
parameter_list|()
comment|/*-{ return this.assignee; }-*/
function_decl|;
DECL|method|createdRaw ()
specifier|private
specifier|native
name|String
name|createdRaw
parameter_list|()
comment|/*-{ return this.created; }-*/
function_decl|;
DECL|method|updatedRaw ()
specifier|private
specifier|native
name|String
name|updatedRaw
parameter_list|()
comment|/*-{ return this.updated; }-*/
function_decl|;
DECL|method|submittedRaw ()
specifier|private
specifier|native
name|String
name|submittedRaw
parameter_list|()
comment|/*-{ return this.submitted; }-*/
function_decl|;
DECL|method|submitter ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|submitter
parameter_list|()
comment|/*-{ return this.submitter; }-*/
function_decl|;
DECL|method|starred ()
specifier|public
specifier|final
specifier|native
name|boolean
name|starred
parameter_list|()
comment|/*-{ return this.starred ? true : false; }-*/
function_decl|;
DECL|method|reviewed ()
specifier|public
specifier|final
specifier|native
name|boolean
name|reviewed
parameter_list|()
comment|/*-{ return this.reviewed ? true : false; }-*/
function_decl|;
DECL|method|isPrivate ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isPrivate
parameter_list|()
comment|/*-{ return this.is_private ? true : false; }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|isWorkInProgress ()
name|isWorkInProgress
parameter_list|()
comment|/*-{ return this.work_in_progress ? true : false; }-*/
function_decl|;
DECL|method|allLabels ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|LabelInfo
argument_list|>
name|allLabels
parameter_list|()
comment|/*-{ return this.labels; }-*/
function_decl|;
DECL|method|label (String n)
specifier|public
specifier|final
specifier|native
name|LabelInfo
name|label
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.labels[n]; }-*/
function_decl|;
DECL|method|currentRevision ()
specifier|public
specifier|final
specifier|native
name|String
name|currentRevision
parameter_list|()
comment|/*-{ return this.current_revision; }-*/
function_decl|;
DECL|method|setCurrentRevision (String r)
specifier|public
specifier|final
specifier|native
name|void
name|setCurrentRevision
parameter_list|(
name|String
name|r
parameter_list|)
comment|/*-{ this.current_revision = r; }-*/
function_decl|;
DECL|method|revisions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|RevisionInfo
argument_list|>
name|revisions
parameter_list|()
comment|/*-{ return this.revisions; }-*/
function_decl|;
DECL|method|revision (String n)
specifier|public
specifier|final
specifier|native
name|RevisionInfo
name|revision
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.revisions[n]; }-*/
function_decl|;
DECL|method|messages ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|MessageInfo
argument_list|>
name|messages
parameter_list|()
comment|/*-{ return this.messages; }-*/
function_decl|;
DECL|method|setEdit (EditInfo edit)
specifier|public
specifier|final
specifier|native
name|void
name|setEdit
parameter_list|(
name|EditInfo
name|edit
parameter_list|)
comment|/*-{ this.edit = edit; }-*/
function_decl|;
DECL|method|edit ()
specifier|public
specifier|final
specifier|native
name|EditInfo
name|edit
parameter_list|()
comment|/*-{ return this.edit; }-*/
function_decl|;
DECL|method|hasEdit ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasEdit
parameter_list|()
comment|/*-{ return this.hasOwnProperty('edit') }-*/
function_decl|;
DECL|method|hashtags ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|hashtags
parameter_list|()
comment|/*-{ return this.hashtags; }-*/
function_decl|;
DECL|method|hasPermittedLabels ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasPermittedLabels
parameter_list|()
comment|/*-{ return this.hasOwnProperty('permitted_labels') }-*/
function_decl|;
DECL|method|permittedLabels ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
name|permittedLabels
parameter_list|()
comment|/*-{ return this.permitted_labels; }-*/
function_decl|;
DECL|method|permittedValues (String n)
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|permittedValues
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.permitted_labels[n]; }-*/
function_decl|;
DECL|method|removableReviewers ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
name|removableReviewers
parameter_list|()
comment|/*-{ return this.removable_reviewers; }-*/
function_decl|;
DECL|method|_reviewers ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|_reviewers
parameter_list|()
comment|/*-{ return this.reviewers; }-*/
function_decl|;
DECL|method|reviewers ()
specifier|public
specifier|final
name|Map
argument_list|<
name|ReviewerState
argument_list|,
name|List
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|reviewers
parameter_list|()
block|{
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|reviewers
init|=
name|_reviewers
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ReviewerState
argument_list|,
name|List
argument_list|<
name|AccountInfo
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|k
range|:
name|reviewers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|ReviewerState
name|state
init|=
name|ReviewerState
operator|.
name|valueOf
argument_list|(
name|k
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|accounts
init|=
name|result
operator|.
name|get
argument_list|(
name|state
argument_list|)
decl_stmt|;
if|if
condition|(
name|accounts
operator|==
literal|null
condition|)
block|{
name|accounts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|state
argument_list|,
name|accounts
argument_list|)
expr_stmt|;
block|}
name|accounts
operator|.
name|addAll
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|reviewers
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|hasActions ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasActions
parameter_list|()
comment|/*-{ return this.hasOwnProperty('actions') }-*/
function_decl|;
DECL|method|actions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|()
comment|/*-{ return this.actions; }-*/
function_decl|;
DECL|method|_number ()
specifier|public
specifier|final
specifier|native
name|int
name|_number
parameter_list|()
comment|/*-{ return this._number; }-*/
function_decl|;
DECL|method|_more_changes ()
specifier|public
specifier|final
specifier|native
name|boolean
name|_more_changes
parameter_list|()
comment|/*-{ return this._more_changes ? true : false; }-*/
function_decl|;
DECL|method|submitType ()
specifier|public
specifier|final
name|SubmitType
name|submitType
parameter_list|()
block|{
name|String
name|submitType
init|=
name|_submitType
argument_list|()
decl_stmt|;
if|if
condition|(
name|submitType
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|submitType
argument_list|)
return|;
block|}
DECL|method|_submitType ()
specifier|private
specifier|native
name|String
name|_submitType
parameter_list|()
comment|/*-{ return this.submit_type; }-*/
function_decl|;
DECL|method|submittable ()
specifier|public
specifier|final
name|boolean
name|submittable
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|_submittable
argument_list|()
return|;
block|}
DECL|method|_submittable ()
specifier|private
specifier|native
name|boolean
name|_submittable
parameter_list|()
comment|/*-{ return this.submittable ? true : false; }-*/
function_decl|;
comment|/**    * @return the index of the missing label or -1 if no label is missing, or if more than one label    *     is missing.    */
DECL|method|getMissingLabelIndex ()
specifier|public
specifier|final
name|int
name|getMissingLabelIndex
parameter_list|()
block|{
name|int
name|i
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|ret
init|=
operator|-
literal|1
decl_stmt|;
name|List
argument_list|<
name|LabelInfo
argument_list|>
name|labels
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|allLabels
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|LabelInfo
name|label
range|:
name|labels
control|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
operator|!
name|permittedLabels
argument_list|()
operator|.
name|containsKey
argument_list|(
name|label
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|JsArrayString
name|values
init|=
name|permittedValues
argument_list|(
name|label
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
switch|switch
condition|(
name|label
operator|.
name|status
argument_list|()
condition|)
block|{
case|case
name|NEED
case|:
comment|// Label is required for submit.
if|if
condition|(
name|ret
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// more than one label is missing, so it's unclear which to quick
comment|// approve, return -1
return|return
operator|-
literal|1
return|;
block|}
name|ret
operator|=
name|i
expr_stmt|;
continue|continue;
case|case
name|OK
case|:
comment|// Label already applied.
case|case
name|MAY
case|:
comment|// Label is not required.
continue|continue;
case|case
name|REJECT
case|:
comment|// Submit cannot happen, do not quick approve.
case|case
name|IMPOSSIBLE
case|:
return|return
operator|-
literal|1
return|;
block|}
block|}
return|return
name|ret
return|;
block|}
DECL|method|ChangeInfo ()
specifier|protected
name|ChangeInfo
parameter_list|()
block|{}
DECL|class|LabelInfo
specifier|public
specifier|static
class|class
name|LabelInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|status ()
specifier|public
specifier|final
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
name|status
parameter_list|()
block|{
if|if
condition|(
name|approved
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
return|;
block|}
elseif|else
if|if
condition|(
name|rejected
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|REJECT
return|;
block|}
elseif|else
if|if
condition|(
name|optional
argument_list|()
condition|)
block|{
return|return
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
return|;
block|}
else|else
block|{
return|return
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|NEED
return|;
block|}
block|}
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this._name; }-*/
function_decl|;
DECL|method|approved ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|approved
parameter_list|()
comment|/*-{ return this.approved; }-*/
function_decl|;
DECL|method|rejected ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|rejected
parameter_list|()
comment|/*-{ return this.rejected; }-*/
function_decl|;
DECL|method|recommended ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|recommended
parameter_list|()
comment|/*-{ return this.recommended; }-*/
function_decl|;
DECL|method|disliked ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|disliked
parameter_list|()
comment|/*-{ return this.disliked; }-*/
function_decl|;
DECL|method|all ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|ApprovalInfo
argument_list|>
name|all
parameter_list|()
comment|/*-{ return this.all; }-*/
function_decl|;
DECL|method|forUser (int user)
specifier|public
specifier|final
name|ApprovalInfo
name|forUser
parameter_list|(
name|int
name|user
parameter_list|)
block|{
name|JsArray
argument_list|<
name|ApprovalInfo
argument_list|>
name|all
init|=
name|all
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|all
operator|!=
literal|null
operator|&&
name|i
operator|<
name|all
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|all
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|_accountId
argument_list|()
operator|==
name|user
condition|)
block|{
return|return
name|all
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|_values ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|_values
parameter_list|()
comment|/*-{ return this.values; }-*/
function_decl|;
DECL|method|values ()
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|Natives
operator|.
name|keys
argument_list|(
name|_values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|valueText (String n)
specifier|public
specifier|final
specifier|native
name|String
name|valueText
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.values[n]; }-*/
function_decl|;
DECL|method|optional ()
specifier|public
specifier|final
specifier|native
name|boolean
name|optional
parameter_list|()
comment|/*-{ return this.optional ? true : false; }-*/
function_decl|;
DECL|method|blocking ()
specifier|public
specifier|final
specifier|native
name|boolean
name|blocking
parameter_list|()
comment|/*-{ return this.blocking ? true : false; }-*/
function_decl|;
DECL|method|defaultValue ()
specifier|public
specifier|final
specifier|native
name|short
name|defaultValue
parameter_list|()
comment|/*-{ return this.default_value; }-*/
function_decl|;
DECL|method|_value ()
specifier|public
specifier|final
specifier|native
name|short
name|_value
parameter_list|()
comment|/*-{       if (this.value) return this.value;       if (this.disliked) return -1;       if (this.recommended) return 1;       return 0;     }-*/
function_decl|;
DECL|method|maxValue ()
specifier|public
specifier|final
name|String
name|maxValue
parameter_list|()
block|{
return|return
name|LabelValue
operator|.
name|formatValue
argument_list|(
name|valueSet
argument_list|()
operator|.
name|last
argument_list|()
argument_list|)
return|;
block|}
DECL|method|valueSet ()
specifier|public
specifier|final
name|SortedSet
argument_list|<
name|Short
argument_list|>
name|valueSet
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|Short
argument_list|>
name|values
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
argument_list|()
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|parseValue
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
DECL|method|parseValue (String formatted)
specifier|public
specifier|static
specifier|final
name|short
name|parseValue
parameter_list|(
name|String
name|formatted
parameter_list|)
block|{
if|if
condition|(
name|formatted
operator|.
name|startsWith
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
name|formatted
operator|=
name|formatted
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|formatted
operator|.
name|startsWith
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
name|formatted
operator|=
name|formatted
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|Short
operator|.
name|parseShort
argument_list|(
name|formatted
argument_list|)
return|;
block|}
DECL|method|LabelInfo ()
specifier|protected
name|LabelInfo
parameter_list|()
block|{}
block|}
DECL|class|ApprovalInfo
specifier|public
specifier|static
class|class
name|ApprovalInfo
extends|extends
name|AccountInfo
block|{
DECL|method|hasValue ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasValue
parameter_list|()
comment|/*-{ return this.hasOwnProperty('value'); }-*/
function_decl|;
DECL|method|value ()
specifier|public
specifier|final
specifier|native
name|short
name|value
parameter_list|()
comment|/*-{ return this.value || 0; }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|VotingRangeInfo
DECL|method|permittedVotingRange ()
name|permittedVotingRange
parameter_list|()
comment|/*-{ return this.permitted_voting_range; }-*/
function_decl|;
DECL|method|ApprovalInfo ()
specifier|protected
name|ApprovalInfo
parameter_list|()
block|{}
block|}
DECL|class|VotingRangeInfo
specifier|public
specifier|static
class|class
name|VotingRangeInfo
extends|extends
name|AccountInfo
block|{
DECL|method|min ()
specifier|public
specifier|final
specifier|native
name|short
name|min
parameter_list|()
comment|/*-{ return this.min || 0; }-*/
function_decl|;
DECL|method|max ()
specifier|public
specifier|final
specifier|native
name|short
name|max
parameter_list|()
comment|/*-{ return this.max || 0; }-*/
function_decl|;
DECL|method|VotingRangeInfo ()
specifier|protected
name|VotingRangeInfo
parameter_list|()
block|{}
block|}
DECL|class|EditInfo
specifier|public
specifier|static
class|class
name|EditInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|setName (String n)
specifier|public
specifier|final
specifier|native
name|String
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.name = n; }-*/
function_decl|;
DECL|method|baseRevision ()
specifier|public
specifier|final
specifier|native
name|String
name|baseRevision
parameter_list|()
comment|/*-{ return this.base_revision; }-*/
function_decl|;
DECL|method|commit ()
specifier|public
specifier|final
specifier|native
name|CommitInfo
name|commit
parameter_list|()
comment|/*-{ return this.commit; }-*/
function_decl|;
DECL|method|hasActions ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasActions
parameter_list|()
comment|/*-{ return this.hasOwnProperty('actions') }-*/
function_decl|;
DECL|method|actions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|()
comment|/*-{ return this.actions; }-*/
function_decl|;
DECL|method|hasFetch ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasFetch
parameter_list|()
comment|/*-{ return this.hasOwnProperty('fetch') }-*/
function_decl|;
DECL|method|fetch ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|FetchInfo
argument_list|>
name|fetch
parameter_list|()
comment|/*-{ return this.fetch; }-*/
function_decl|;
DECL|method|hasFiles ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasFiles
parameter_list|()
comment|/*-{ return this.hasOwnProperty('files') }-*/
function_decl|;
DECL|method|files ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|FileInfo
argument_list|>
name|files
parameter_list|()
comment|/*-{ return this.files; }-*/
function_decl|;
DECL|method|EditInfo ()
specifier|protected
name|EditInfo
parameter_list|()
block|{}
block|}
DECL|class|RevisionInfo
specifier|public
specifier|static
class|class
name|RevisionInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|fromEdit (EditInfo edit)
specifier|public
specifier|static
name|RevisionInfo
name|fromEdit
parameter_list|(
name|EditInfo
name|edit
parameter_list|)
block|{
name|RevisionInfo
name|revisionInfo
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|revisionInfo
operator|.
name|takeFromEdit
argument_list|(
name|edit
argument_list|)
expr_stmt|;
return|return
name|revisionInfo
return|;
block|}
DECL|method|forParent (int number, CommitInfo commit)
specifier|public
specifier|static
name|RevisionInfo
name|forParent
parameter_list|(
name|int
name|number
parameter_list|,
name|CommitInfo
name|commit
parameter_list|)
block|{
name|RevisionInfo
name|revisionInfo
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|revisionInfo
operator|.
name|takeFromParent
argument_list|(
name|number
argument_list|,
name|commit
argument_list|)
expr_stmt|;
return|return
name|revisionInfo
return|;
block|}
DECL|method|takeFromEdit (EditInfo edit)
specifier|private
specifier|native
name|void
name|takeFromEdit
parameter_list|(
name|EditInfo
name|edit
parameter_list|)
comment|/*-{       this._number = 0;       this.name = edit.name;       this.commit = edit.commit;       this.edit_base = edit.base_revision;     }-*/
function_decl|;
DECL|method|takeFromParent (int number, CommitInfo commit)
specifier|private
specifier|native
name|void
name|takeFromParent
parameter_list|(
name|int
name|number
parameter_list|,
name|CommitInfo
name|commit
parameter_list|)
comment|/*-{       this._number = number;       this.commit = commit;       this.name = this._number;     }-*/
function_decl|;
DECL|method|_number ()
specifier|public
specifier|final
specifier|native
name|int
name|_number
parameter_list|()
comment|/*-{ return this._number; }-*/
function_decl|;
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|uploader ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|uploader
parameter_list|()
comment|/*-{ return this.uploader; }-*/
function_decl|;
DECL|method|isEdit ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isEdit
parameter_list|()
comment|/*-{ return this._number == 0; }-*/
function_decl|;
DECL|method|commit ()
specifier|public
specifier|final
specifier|native
name|CommitInfo
name|commit
parameter_list|()
comment|/*-{ return this.commit; }-*/
function_decl|;
DECL|method|setCommit (CommitInfo c)
specifier|public
specifier|final
specifier|native
name|void
name|setCommit
parameter_list|(
name|CommitInfo
name|c
parameter_list|)
comment|/*-{ this.commit = c; }-*/
function_decl|;
DECL|method|editBase ()
specifier|public
specifier|final
specifier|native
name|String
name|editBase
parameter_list|()
comment|/*-{ return this.edit_base; }-*/
function_decl|;
DECL|method|hasFiles ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasFiles
parameter_list|()
comment|/*-{ return this.hasOwnProperty('files') }-*/
function_decl|;
DECL|method|files ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|FileInfo
argument_list|>
name|files
parameter_list|()
comment|/*-{ return this.files; }-*/
function_decl|;
DECL|method|hasActions ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasActions
parameter_list|()
comment|/*-{ return this.hasOwnProperty('actions') }-*/
function_decl|;
DECL|method|actions ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|()
comment|/*-{ return this.actions; }-*/
function_decl|;
DECL|method|hasFetch ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasFetch
parameter_list|()
comment|/*-{ return this.hasOwnProperty('fetch') }-*/
function_decl|;
DECL|method|fetch ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|FetchInfo
argument_list|>
name|fetch
parameter_list|()
comment|/*-{ return this.fetch; }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|hasPushCertificate ()
name|hasPushCertificate
parameter_list|()
comment|/*-{ return this.hasOwnProperty('push_certificate'); }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|PushCertificateInfo
DECL|method|pushCertificate ()
name|pushCertificate
parameter_list|()
comment|/*-{ return this.push_certificate; }-*/
function_decl|;
DECL|method|sortRevisionInfoByNumber (JsArray<RevisionInfo> list)
specifier|public
specifier|static
name|void
name|sortRevisionInfoByNumber
parameter_list|(
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|list
parameter_list|)
block|{
specifier|final
name|int
name|editParent
init|=
name|findEditParent
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|list
argument_list|)
argument_list|,
operator|new
name|Comparator
argument_list|<
name|RevisionInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|RevisionInfo
name|a
parameter_list|,
name|RevisionInfo
name|b
parameter_list|)
block|{
return|return
name|num
argument_list|(
name|a
argument_list|)
operator|-
name|num
argument_list|(
name|b
argument_list|)
return|;
block|}
specifier|private
name|int
name|num
parameter_list|(
name|RevisionInfo
name|r
parameter_list|)
block|{
return|return
operator|!
name|r
operator|.
name|isEdit
argument_list|()
condition|?
literal|2
operator|*
operator|(
name|r
operator|.
name|_number
argument_list|()
operator|-
literal|1
operator|)
operator|+
literal|1
else|:
literal|2
operator|*
name|editParent
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|findEditParent (JsArray<RevisionInfo> list)
specifier|public
specifier|static
name|int
name|findEditParent
parameter_list|(
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|list
parameter_list|)
block|{
name|RevisionInfo
name|r
init|=
name|findEditParentRevision
argument_list|(
name|list
argument_list|)
decl_stmt|;
return|return
name|r
operator|==
literal|null
condition|?
operator|-
literal|1
else|:
name|r
operator|.
name|_number
argument_list|()
return|;
block|}
DECL|method|findEditParentRevision (JsArray<RevisionInfo> list)
specifier|public
specifier|static
name|RevisionInfo
name|findEditParentRevision
parameter_list|(
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|// edit under revisions?
name|RevisionInfo
name|editInfo
init|=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|editInfo
operator|.
name|isEdit
argument_list|()
condition|)
block|{
name|String
name|parentRevision
init|=
name|editInfo
operator|.
name|editBase
argument_list|()
decl_stmt|;
comment|// find parent
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|list
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|RevisionInfo
name|parentInfo
init|=
name|list
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|parentInfo
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|parentRevision
argument_list|)
condition|)
block|{
comment|// found parent pacth set number
return|return
name|parentInfo
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|id ()
specifier|public
specifier|final
name|String
name|id
parameter_list|()
block|{
return|return
name|PatchSet
operator|.
name|Id
operator|.
name|toId
argument_list|(
name|_number
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isMerge ()
specifier|public
specifier|final
name|boolean
name|isMerge
parameter_list|()
block|{
return|return
name|commit
argument_list|()
operator|.
name|parents
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|1
return|;
block|}
DECL|method|RevisionInfo ()
specifier|protected
name|RevisionInfo
parameter_list|()
block|{}
block|}
DECL|class|FetchInfo
specifier|public
specifier|static
class|class
name|FetchInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|url ()
specifier|public
specifier|final
specifier|native
name|String
name|url
parameter_list|()
comment|/*-{ return this.url }-*/
function_decl|;
DECL|method|ref ()
specifier|public
specifier|final
specifier|native
name|String
name|ref
parameter_list|()
comment|/*-{ return this.ref }-*/
function_decl|;
DECL|method|commands ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|commands
parameter_list|()
comment|/*-{ return this.commands }-*/
function_decl|;
DECL|method|command (String n)
specifier|public
specifier|final
specifier|native
name|String
name|command
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.commands[n]; }-*/
function_decl|;
DECL|method|FetchInfo ()
specifier|protected
name|FetchInfo
parameter_list|()
block|{}
block|}
DECL|class|CommitInfo
specifier|public
specifier|static
class|class
name|CommitInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|commit ()
specifier|public
specifier|final
specifier|native
name|String
name|commit
parameter_list|()
comment|/*-{ return this.commit; }-*/
function_decl|;
DECL|method|parents ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|CommitInfo
argument_list|>
name|parents
parameter_list|()
comment|/*-{ return this.parents; }-*/
function_decl|;
DECL|method|author ()
specifier|public
specifier|final
specifier|native
name|GitPerson
name|author
parameter_list|()
comment|/*-{ return this.author; }-*/
function_decl|;
DECL|method|committer ()
specifier|public
specifier|final
specifier|native
name|GitPerson
name|committer
parameter_list|()
comment|/*-{ return this.committer; }-*/
function_decl|;
DECL|method|subject ()
specifier|public
specifier|final
specifier|native
name|String
name|subject
parameter_list|()
comment|/*-{ return this.subject; }-*/
function_decl|;
DECL|method|message ()
specifier|public
specifier|final
specifier|native
name|String
name|message
parameter_list|()
comment|/*-{ return this.message; }-*/
function_decl|;
DECL|method|webLinks ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
parameter_list|()
comment|/*-{ return this.web_links; }-*/
function_decl|;
DECL|method|CommitInfo ()
specifier|protected
name|CommitInfo
parameter_list|()
block|{}
block|}
DECL|class|GitPerson
specifier|public
specifier|static
class|class
name|GitPerson
extends|extends
name|JavaScriptObject
block|{
DECL|method|name ()
specifier|public
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name; }-*/
function_decl|;
DECL|method|email ()
specifier|public
specifier|final
specifier|native
name|String
name|email
parameter_list|()
comment|/*-{ return this.email; }-*/
function_decl|;
DECL|method|dateRaw ()
specifier|private
specifier|native
name|String
name|dateRaw
parameter_list|()
comment|/*-{ return this.date; }-*/
function_decl|;
DECL|method|date ()
specifier|public
specifier|final
name|Timestamp
name|date
parameter_list|()
block|{
return|return
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|dateRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|GitPerson ()
specifier|protected
name|GitPerson
parameter_list|()
block|{}
block|}
DECL|class|MessageInfo
specifier|public
specifier|static
class|class
name|MessageInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|author ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|author
parameter_list|()
comment|/*-{ return this.author; }-*/
function_decl|;
DECL|method|message ()
specifier|public
specifier|final
specifier|native
name|String
name|message
parameter_list|()
comment|/*-{ return this.message; }-*/
function_decl|;
DECL|method|_revisionNumber ()
specifier|public
specifier|final
specifier|native
name|int
name|_revisionNumber
parameter_list|()
comment|/*-{ return this._revision_number || 0; }-*/
function_decl|;
DECL|method|tag ()
specifier|public
specifier|final
specifier|native
name|String
name|tag
parameter_list|()
comment|/*-{ return this.tag; }-*/
function_decl|;
DECL|method|dateRaw ()
specifier|private
specifier|native
name|String
name|dateRaw
parameter_list|()
comment|/*-{ return this.date; }-*/
function_decl|;
DECL|method|date ()
specifier|public
specifier|final
name|Timestamp
name|date
parameter_list|()
block|{
return|return
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|dateRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|MessageInfo ()
specifier|protected
name|MessageInfo
parameter_list|()
block|{}
block|}
DECL|class|MergeableInfo
specifier|public
specifier|static
class|class
name|MergeableInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|submitType ()
specifier|public
specifier|final
specifier|native
name|String
name|submitType
parameter_list|()
comment|/*-{ return this.submit_type }-*/
function_decl|;
DECL|method|mergeable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|mergeable
parameter_list|()
comment|/*-{ return this.mergeable }-*/
function_decl|;
DECL|method|MergeableInfo ()
specifier|protected
name|MergeableInfo
parameter_list|()
block|{}
block|}
DECL|class|IncludedInInfo
specifier|public
specifier|static
class|class
name|IncludedInInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|externalNames ()
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|externalNames
parameter_list|()
block|{
return|return
name|Natives
operator|.
name|keys
argument_list|(
name|external
argument_list|()
argument_list|)
return|;
block|}
DECL|method|branches ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|branches
parameter_list|()
comment|/*-{ return this.branches; }-*/
function_decl|;
DECL|method|tags ()
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|tags
parameter_list|()
comment|/*-{ return this.tags; }-*/
function_decl|;
DECL|method|external (String n)
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|external
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.external[n]; }-*/
function_decl|;
DECL|method|external ()
specifier|private
specifier|native
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
name|external
parameter_list|()
comment|/*-{ return this.external; }-*/
function_decl|;
DECL|method|IncludedInInfo ()
specifier|protected
name|IncludedInInfo
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit


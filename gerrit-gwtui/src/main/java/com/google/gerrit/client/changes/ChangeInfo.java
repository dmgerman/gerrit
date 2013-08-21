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
name|account
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
name|diff
operator|.
name|FileInfo
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
name|all_labels
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|all_labels
argument_list|()
operator|.
name|copyKeysIntoChildren
argument_list|(
literal|"_name"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|project_name_key ()
specifier|public
specifier|final
name|Project
operator|.
name|NameKey
name|project_name_key
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
DECL|method|legacy_id ()
specifier|public
specifier|final
name|Change
operator|.
name|Id
name|legacy_id
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
name|_get_cts
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
name|_set_cts
argument_list|(
name|ts
argument_list|)
expr_stmt|;
block|}
return|return
name|ts
return|;
block|}
DECL|method|_get_cts ()
specifier|private
specifier|final
specifier|native
name|Timestamp
name|_get_cts
parameter_list|()
comment|/*-{ return this._cts; }-*/
function_decl|;
DECL|method|_set_cts (Timestamp ts)
specifier|private
specifier|final
specifier|native
name|void
name|_set_cts
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
DECL|method|id_abbreviated ()
specifier|public
specifier|final
name|String
name|id_abbreviated
parameter_list|()
block|{
return|return
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|change_id
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
name|all_labels
argument_list|()
operator|.
name|keySet
argument_list|()
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
DECL|method|change_id ()
specifier|public
specifier|final
specifier|native
name|String
name|change_id
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
comment|/*-{ return this.mergeable; }-*/
function_decl|;
DECL|method|statusRaw ()
specifier|private
specifier|final
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
DECL|method|createdRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|createdRaw
parameter_list|()
comment|/*-{ return this.created; }-*/
function_decl|;
DECL|method|updatedRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|updatedRaw
parameter_list|()
comment|/*-{ return this.updated; }-*/
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
DECL|method|_sortkey ()
specifier|public
specifier|final
specifier|native
name|String
name|_sortkey
parameter_list|()
comment|/*-{ return this._sortkey; }-*/
function_decl|;
DECL|method|all_labels ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|LabelInfo
argument_list|>
name|all_labels
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
DECL|method|current_revision ()
specifier|public
specifier|final
specifier|native
name|String
name|current_revision
parameter_list|()
comment|/*-{ return this.current_revision; }-*/
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
DECL|method|has_permitted_labels ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_permitted_labels
parameter_list|()
comment|/*-{ return this.hasOwnProperty('permitted_labels') }-*/
function_decl|;
DECL|method|permitted_labels ()
specifier|public
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
name|permitted_labels
parameter_list|()
comment|/*-{ return this.permitted_labels; }-*/
function_decl|;
DECL|method|permitted_values (String n)
specifier|public
specifier|final
specifier|native
name|JsArrayString
name|permitted_values
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return this.permitted_labels[n]; }-*/
function_decl|;
DECL|method|removable_reviewers ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|AccountInfo
argument_list|>
name|removable_reviewers
parameter_list|()
comment|/*-{ return this.removable_reviewers; }-*/
function_decl|;
DECL|method|has_actions ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_actions
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
specifier|final
specifier|native
name|int
name|_number
parameter_list|()
comment|/*-{ return this._number; }-*/
function_decl|;
DECL|method|_more_changes ()
specifier|final
specifier|native
name|boolean
name|_more_changes
parameter_list|()
comment|/*-{ return this._more_changes ? true : false; }-*/
function_decl|;
DECL|method|ChangeInfo ()
specifier|protected
name|ChangeInfo
parameter_list|()
block|{   }
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
DECL|method|for_user (int user)
specifier|public
specifier|final
name|ApprovalInfo
name|for_user
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
name|_account_id
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
specifier|final
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
DECL|method|value_text (String n)
specifier|public
specifier|final
specifier|native
name|String
name|value_text
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
DECL|method|_value ()
specifier|final
specifier|native
name|short
name|_value
parameter_list|()
comment|/*-{       if (this.value) return this.value;       if (this.disliked) return -1;       if (this.recommended) return 1;       return 0;     }-*/
function_decl|;
DECL|method|max_value ()
specifier|public
specifier|final
name|String
name|max_value
parameter_list|()
block|{
return|return
name|LabelValue
operator|.
name|formatValue
argument_list|(
name|value_set
argument_list|()
operator|.
name|last
argument_list|()
argument_list|)
return|;
block|}
DECL|method|value_set ()
specifier|public
specifier|final
name|SortedSet
argument_list|<
name|Short
argument_list|>
name|value_set
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
argument_list|<
name|Short
argument_list|>
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
block|{     }
block|}
DECL|class|ApprovalInfo
specifier|public
specifier|static
class|class
name|ApprovalInfo
extends|extends
name|AccountInfo
block|{
DECL|method|has_value ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_value
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
DECL|method|ApprovalInfo ()
specifier|protected
name|ApprovalInfo
parameter_list|()
block|{     }
block|}
DECL|class|RevisionInfo
specifier|public
specifier|static
class|class
name|RevisionInfo
extends|extends
name|JavaScriptObject
block|{
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
DECL|method|draft ()
specifier|public
specifier|final
specifier|native
name|boolean
name|draft
parameter_list|()
comment|/*-{ return this.draft || false; }-*/
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
DECL|method|set_commit (CommitInfo c)
specifier|public
specifier|final
specifier|native
name|void
name|set_commit
parameter_list|(
name|CommitInfo
name|c
parameter_list|)
comment|/*-{ this.commit = c; }-*/
function_decl|;
DECL|method|has_files ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_files
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
DECL|method|has_actions ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_actions
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
argument_list|)
expr_stmt|;
block|}
DECL|method|RevisionInfo ()
specifier|protected
name|RevisionInfo
parameter_list|()
block|{     }
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
DECL|method|CommitInfo ()
specifier|protected
name|CommitInfo
parameter_list|()
block|{     }
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
specifier|final
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
block|{     }
block|}
DECL|class|ActionInfo
specifier|public
specifier|static
class|class
name|ActionInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|id ()
specifier|public
specifier|final
specifier|native
name|String
name|id
parameter_list|()
comment|/*-{ return this.id; }-*/
function_decl|;
DECL|method|method ()
specifier|public
specifier|final
specifier|native
name|String
name|method
parameter_list|()
comment|/*-{ return this.method; }-*/
function_decl|;
DECL|method|label ()
specifier|public
specifier|final
specifier|native
name|String
name|label
parameter_list|()
comment|/*-{ return this.label; }-*/
function_decl|;
DECL|method|title ()
specifier|public
specifier|final
specifier|native
name|String
name|title
parameter_list|()
comment|/*-{ return this.title; }-*/
function_decl|;
DECL|method|enabled ()
specifier|public
specifier|final
specifier|native
name|boolean
name|enabled
parameter_list|()
comment|/*-{ return this.enabled || false; }-*/
function_decl|;
DECL|method|ActionInfo ()
specifier|protected
name|ActionInfo
parameter_list|()
block|{     }
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
DECL|method|dateRaw ()
specifier|private
specifier|final
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
block|{     }
block|}
DECL|class|MergeableInfo
specifier|public
specifier|static
class|class
name|MergeableInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|submit_type ()
specifier|public
specifier|final
specifier|native
name|String
name|submit_type
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
block|{     }
block|}
block|}
end_class

end_unit


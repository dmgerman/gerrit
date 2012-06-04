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
name|Set
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
name|id
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
name|Natives
operator|.
name|keys
argument_list|(
name|labels0
argument_list|()
argument_list|)
return|;
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
DECL|method|id ()
specifier|public
specifier|final
specifier|native
name|String
name|id
parameter_list|()
comment|/*-{ return this.id; }-*/
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
DECL|method|_sortkey ()
specifier|public
specifier|final
specifier|native
name|String
name|_sortkey
parameter_list|()
comment|/*-{ return this._sortkey; }-*/
function_decl|;
DECL|method|labels0 ()
specifier|private
specifier|final
specifier|native
name|JavaScriptObject
name|labels0
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
DECL|class|AccountInfo
specifier|public
specifier|static
class|class
name|AccountInfo
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
DECL|method|AccountInfo ()
specifier|protected
name|AccountInfo
parameter_list|()
block|{     }
block|}
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
DECL|method|LabelInfo ()
specifier|protected
name|LabelInfo
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit


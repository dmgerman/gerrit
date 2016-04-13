begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.groups
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|groups
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
name|info
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

begin_class
DECL|class|GroupAuditEventInfo
specifier|public
class|class
name|GroupAuditEventInfo
extends|extends
name|JavaScriptObject
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|ADD_USER
DECL|enumConstant|REMOVE_USER
DECL|enumConstant|ADD_GROUP
DECL|enumConstant|REMOVE_GROUP
name|ADD_USER
block|,
name|REMOVE_USER
block|,
name|ADD_GROUP
block|,
name|REMOVE_GROUP
block|}
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
DECL|method|type ()
specifier|public
specifier|final
name|Type
name|type
parameter_list|()
block|{
return|return
name|Type
operator|.
name|valueOf
argument_list|(
name|typeRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|user ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|user
parameter_list|()
comment|/*-{ return this.user; }-*/
function_decl|;
DECL|method|memberAsUser ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|memberAsUser
parameter_list|()
comment|/*-{ return this.member; }-*/
function_decl|;
DECL|method|memberAsGroup ()
specifier|public
specifier|final
specifier|native
name|GroupInfo
name|memberAsGroup
parameter_list|()
comment|/*-{ return this.member; }-*/
function_decl|;
DECL|method|dateRaw ()
specifier|private
specifier|native
name|String
name|dateRaw
parameter_list|()
comment|/*-{ return this.date; }-*/
function_decl|;
DECL|method|typeRaw ()
specifier|private
specifier|native
name|String
name|typeRaw
parameter_list|()
comment|/*-{ return this.type; }-*/
function_decl|;
DECL|method|GroupAuditEventInfo ()
specifier|protected
name|GroupAuditEventInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


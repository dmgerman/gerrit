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
name|changes
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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

begin_class
DECL|class|MemberInfo
specifier|public
class|class
name|MemberInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|getAccountId ()
specifier|public
specifier|final
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|account_id
argument_list|()
argument_list|)
return|;
block|}
DECL|method|asAccountInfo ()
specifier|public
specifier|final
name|AccountInfo
name|asAccountInfo
parameter_list|()
block|{
return|return
name|AccountInfo
operator|.
name|create
argument_list|(
name|account_id
argument_list|()
argument_list|,
name|fullName
argument_list|()
argument_list|,
name|preferredEmail
argument_list|()
argument_list|)
return|;
block|}
DECL|method|account_id ()
specifier|private
specifier|final
specifier|native
name|int
name|account_id
parameter_list|()
comment|/*-{ return this.account_id || 0; }-*/
function_decl|;
DECL|method|fullName ()
specifier|public
specifier|final
specifier|native
name|String
name|fullName
parameter_list|()
comment|/*-{ return this.full_name; }-*/
function_decl|;
DECL|method|preferredEmail ()
specifier|public
specifier|final
specifier|native
name|String
name|preferredEmail
parameter_list|()
comment|/*-{ return this.preferred_email; }-*/
function_decl|;
DECL|method|MemberInfo ()
specifier|protected
name|MemberInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


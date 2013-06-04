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
DECL|class|SshKeyInfo
specifier|public
class|class
name|SshKeyInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|seq ()
specifier|public
specifier|final
specifier|native
name|int
name|seq
parameter_list|()
comment|/*-{ return this.seq || 0; }-*/
function_decl|;
DECL|method|sshPublicKey ()
specifier|public
specifier|final
specifier|native
name|String
name|sshPublicKey
parameter_list|()
comment|/*-{ return this.ssh_public_key; }-*/
function_decl|;
DECL|method|encodedKey ()
specifier|public
specifier|final
specifier|native
name|String
name|encodedKey
parameter_list|()
comment|/*-{ return this.encoded_key; }-*/
function_decl|;
DECL|method|algorithm ()
specifier|public
specifier|final
specifier|native
name|String
name|algorithm
parameter_list|()
comment|/*-{ return this.algorithm; }-*/
function_decl|;
DECL|method|comment ()
specifier|public
specifier|final
specifier|native
name|String
name|comment
parameter_list|()
comment|/*-{ return this.comment; }-*/
function_decl|;
DECL|method|isValid ()
specifier|public
specifier|final
specifier|native
name|boolean
name|isValid
parameter_list|()
comment|/*-{ return this['valid'] ? true : false; }-*/
function_decl|;
DECL|method|SshKeyInfo ()
specifier|protected
name|SshKeyInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


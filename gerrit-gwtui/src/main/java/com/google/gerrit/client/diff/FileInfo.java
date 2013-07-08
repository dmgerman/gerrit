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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
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
DECL|class|FileInfo
specifier|public
class|class
name|FileInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|path ()
specifier|public
specifier|final
specifier|native
name|String
name|path
parameter_list|()
comment|/*-{ return this.path; }-*/
function_decl|;
DECL|method|old_path ()
specifier|public
specifier|final
specifier|native
name|String
name|old_path
parameter_list|()
comment|/*-{ return this.old_path; }-*/
function_decl|;
DECL|method|lines_inserted ()
specifier|public
specifier|final
specifier|native
name|int
name|lines_inserted
parameter_list|()
comment|/*-{ return this.lines_inserted || 0; }-*/
function_decl|;
DECL|method|lines_deleted ()
specifier|public
specifier|final
specifier|native
name|int
name|lines_deleted
parameter_list|()
comment|/*-{ return this.lines_deleted || 0; }-*/
function_decl|;
DECL|method|binary ()
specifier|public
specifier|final
specifier|native
name|boolean
name|binary
parameter_list|()
comment|/*-{ return this.binary || false; }-*/
function_decl|;
DECL|method|FileInfo ()
specifier|protected
name|FileInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


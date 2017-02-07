begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|VoidResult
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
name|editor
operator|.
name|EditFileInfo
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
name|GerritCallback
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
name|HttpCallback
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
name|RestApi
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
name|Patch
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
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_comment
comment|/** REST API helpers to remotely edit a change. */
end_comment

begin_class
DECL|class|ChangeEditApi
specifier|public
class|class
name|ChangeEditApi
block|{
comment|/** Get file (or commit message) contents. */
DECL|method|get (PatchSet.Id id, String path, boolean base, HttpCallback<NativeString> cb)
specifier|public
specifier|static
name|void
name|get
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|base
parameter_list|,
name|HttpCallback
argument_list|<
name|NativeString
argument_list|>
name|cb
parameter_list|)
block|{
name|RestApi
name|api
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|get
argument_list|()
operator|!=
literal|0
condition|)
block|{
comment|// Read from a published revision, when change edit doesn't
comment|// exist for the caller, or is not currently active.
name|api
operator|=
name|ChangeApi
operator|.
name|revision
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"files"
argument_list|)
operator|.
name|id
argument_list|(
name|path
argument_list|)
operator|.
name|view
argument_list|(
literal|"content"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|api
operator|=
name|editMessage
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|addParameter
argument_list|(
literal|"base"
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|api
operator|=
name|editFile
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|addParameter
argument_list|(
literal|"base"
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
name|api
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Get file (or commit message) contents of the edit. */
DECL|method|get (PatchSet.Id id, String path, HttpCallback<NativeString> cb)
specifier|public
specifier|static
name|void
name|get
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|HttpCallback
argument_list|<
name|NativeString
argument_list|>
name|cb
parameter_list|)
block|{
name|get
argument_list|(
name|id
argument_list|,
name|path
argument_list|,
literal|false
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Get meta info for change edit. */
DECL|method|getMeta (PatchSet.Id id, String path, AsyncCallback<EditFileInfo> cb)
specifier|public
specifier|static
name|void
name|getMeta
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|AsyncCallback
argument_list|<
name|EditFileInfo
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|id
operator|.
name|get
argument_list|()
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"only supported for edits"
argument_list|)
throw|;
block|}
name|editFile
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|view
argument_list|(
literal|"meta"
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Put message into a change edit. */
DECL|method|putMessage (int id, String m, GerritCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|putMessage
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|m
parameter_list|,
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|editMessage
argument_list|(
name|id
argument_list|)
operator|.
name|put
argument_list|(
name|m
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Put contents into a file or commit message in a change edit. */
DECL|method|put (int id, String path, String content, GerritCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|put
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|content
parameter_list|,
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|putMessage
argument_list|(
name|id
argument_list|,
name|content
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|editFile
argument_list|(
name|id
argument_list|,
name|path
argument_list|)
operator|.
name|put
argument_list|(
name|content
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Delete a file in the pending edit. */
DECL|method|delete (int id, String path, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|delete
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|editFile
argument_list|(
name|id
argument_list|,
name|path
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Rename a file in the pending edit. */
DECL|method|rename (int id, String path, String newPath, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|rename
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|newPath
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|Input
name|in
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|oldPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|in
operator|.
name|newPath
argument_list|(
name|newPath
argument_list|)
expr_stmt|;
name|ChangeApi
operator|.
name|edit
argument_list|(
name|id
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Restore (undo delete/modify) a file in the pending edit. */
DECL|method|restore (int id, String path, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|restore
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|path
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|Input
name|in
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|restorePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|ChangeApi
operator|.
name|edit
argument_list|(
name|id
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|editMessage (int id)
specifier|private
specifier|static
name|RestApi
name|editMessage
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|ChangeApi
operator|.
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"edit:message"
argument_list|)
return|;
block|}
DECL|method|editFile (int id, String path)
specifier|private
specifier|static
name|RestApi
name|editFile
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|ChangeApi
operator|.
name|edit
argument_list|(
name|id
argument_list|)
operator|.
name|id
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|class|Input
specifier|private
specifier|static
class|class
name|Input
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|static
name|Input
name|create
parameter_list|()
block|{
return|return
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
return|;
block|}
DECL|method|restorePath (String p)
specifier|final
specifier|native
name|void
name|restorePath
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ this.restore_path=p }-*/
function_decl|;
DECL|method|oldPath (String p)
specifier|final
specifier|native
name|void
name|oldPath
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ this.old_path=p }-*/
function_decl|;
DECL|method|newPath (String p)
specifier|final
specifier|native
name|void
name|newPath
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ this.new_path=p }-*/
function_decl|;
DECL|method|Input ()
specifier|protected
name|Input
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit


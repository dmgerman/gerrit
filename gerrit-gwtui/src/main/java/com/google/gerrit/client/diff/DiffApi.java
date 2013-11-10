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
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|ChangeApi
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
name|AccountDiffPreference
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
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|DiffApi
specifier|public
class|class
name|DiffApi
block|{
DECL|enum|IgnoreWhitespace
specifier|public
enum|enum
name|IgnoreWhitespace
block|{
DECL|enumConstant|NONE
DECL|enumConstant|TRAILING
DECL|enumConstant|CHANGED
DECL|enumConstant|ALL
name|NONE
block|,
name|TRAILING
block|,
name|CHANGED
block|,
name|ALL
block|}
DECL|method|list (int id, String revision, AsyncCallback<NativeMap<FileInfo>> cb)
specifier|public
specifier|static
name|void
name|list
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|revision
parameter_list|,
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|FileInfo
argument_list|>
argument_list|>
name|cb
parameter_list|)
block|{
name|ChangeApi
operator|.
name|revision
argument_list|(
name|id
argument_list|,
name|revision
argument_list|)
operator|.
name|view
argument_list|(
literal|"files"
argument_list|)
operator|.
name|get
argument_list|(
name|NativeMap
operator|.
name|copyKeysIntoChildren
argument_list|(
literal|"path"
argument_list|,
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|diff (PatchSet.Id id, String path)
specifier|public
specifier|static
name|DiffApi
name|diff
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|DiffApi
argument_list|(
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
literal|"diff"
argument_list|)
argument_list|)
return|;
block|}
DECL|field|call
specifier|private
specifier|final
name|RestApi
name|call
decl_stmt|;
DECL|method|DiffApi (RestApi call)
specifier|private
name|DiffApi
parameter_list|(
name|RestApi
name|call
parameter_list|)
block|{
name|this
operator|.
name|call
operator|=
name|call
expr_stmt|;
block|}
DECL|method|base (PatchSet.Id id)
specifier|public
name|DiffApi
name|base
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"base"
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|ignoreWhitespace (AccountDiffPreference.Whitespace w)
specifier|public
name|DiffApi
name|ignoreWhitespace
parameter_list|(
name|AccountDiffPreference
operator|.
name|Whitespace
name|w
parameter_list|)
block|{
switch|switch
condition|(
name|w
condition|)
block|{
default|default:
case|case
name|IGNORE_NONE
case|:
return|return
name|ignoreWhitespace
argument_list|(
name|IgnoreWhitespace
operator|.
name|NONE
argument_list|)
return|;
case|case
name|IGNORE_SPACE_AT_EOL
case|:
return|return
name|ignoreWhitespace
argument_list|(
name|IgnoreWhitespace
operator|.
name|TRAILING
argument_list|)
return|;
case|case
name|IGNORE_SPACE_CHANGE
case|:
return|return
name|ignoreWhitespace
argument_list|(
name|IgnoreWhitespace
operator|.
name|CHANGED
argument_list|)
return|;
case|case
name|IGNORE_ALL_SPACE
case|:
return|return
name|ignoreWhitespace
argument_list|(
name|IgnoreWhitespace
operator|.
name|ALL
argument_list|)
return|;
block|}
block|}
DECL|method|ignoreWhitespace (IgnoreWhitespace w)
specifier|public
name|DiffApi
name|ignoreWhitespace
parameter_list|(
name|IgnoreWhitespace
name|w
parameter_list|)
block|{
if|if
condition|(
name|w
operator|!=
literal|null
operator|&&
name|w
operator|!=
name|IgnoreWhitespace
operator|.
name|NONE
condition|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"ignore-whitespace"
argument_list|,
name|w
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|intraline (boolean intraline)
specifier|public
name|DiffApi
name|intraline
parameter_list|(
name|boolean
name|intraline
parameter_list|)
block|{
if|if
condition|(
name|intraline
condition|)
block|{
name|call
operator|.
name|addParameterTrue
argument_list|(
literal|"intraline"
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|wholeFile ()
specifier|public
name|DiffApi
name|wholeFile
parameter_list|()
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"context"
argument_list|,
literal|"ALL"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|context (int lines)
specifier|public
name|DiffApi
name|context
parameter_list|(
name|int
name|lines
parameter_list|)
block|{
name|call
operator|.
name|addParameter
argument_list|(
literal|"context"
argument_list|,
name|lines
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|get (AsyncCallback<DiffInfo> cb)
specifier|public
name|void
name|get
parameter_list|(
name|AsyncCallback
argument_list|<
name|DiffInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|call
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|FilenameComparator
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
name|java
operator|.
name|util
operator|.
name|Comparator
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
DECL|method|oldPath ()
specifier|public
specifier|final
specifier|native
name|String
name|oldPath
parameter_list|()
comment|/*-{ return this.old_path; }-*/
function_decl|;
DECL|method|linesInserted ()
specifier|public
specifier|final
specifier|native
name|int
name|linesInserted
parameter_list|()
comment|/*-{ return this.lines_inserted || 0; }-*/
function_decl|;
DECL|method|linesDeleted ()
specifier|public
specifier|final
specifier|native
name|int
name|linesDeleted
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
DECL|method|status ()
specifier|public
specifier|final
specifier|native
name|String
name|status
parameter_list|()
comment|/*-{ return this.status; }-*/
function_decl|;
comment|// JSNI methods cannot have 'long' as a parameter type or a return type and
comment|// it's suggested to use double in this case:
comment|// http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJSNI.html#important
DECL|method|size ()
specifier|public
specifier|final
name|long
name|size
parameter_list|()
block|{
return|return
operator|(
name|long
operator|)
name|_size
argument_list|()
return|;
block|}
DECL|method|_size ()
specifier|private
specifier|native
name|double
name|_size
parameter_list|()
comment|/*-{ return this.size || 0; }-*/
function_decl|;
DECL|method|sizeDelta ()
specifier|public
specifier|final
name|long
name|sizeDelta
parameter_list|()
block|{
return|return
operator|(
name|long
operator|)
name|_sizeDelta
argument_list|()
return|;
block|}
DECL|method|_sizeDelta ()
specifier|private
specifier|native
name|double
name|_sizeDelta
parameter_list|()
comment|/*-{ return this.size_delta || 0; }-*/
function_decl|;
DECL|method|_row ()
specifier|public
specifier|final
specifier|native
name|int
name|_row
parameter_list|()
comment|/*-{ return this._row }-*/
function_decl|;
DECL|method|_row (int r)
specifier|public
specifier|final
specifier|native
name|void
name|_row
parameter_list|(
name|int
name|r
parameter_list|)
comment|/*-{ this._row = r }-*/
function_decl|;
DECL|method|sortFileInfoByPath (JsArray<FileInfo> list)
specifier|public
specifier|static
name|void
name|sortFileInfoByPath
parameter_list|(
name|JsArray
argument_list|<
name|FileInfo
argument_list|>
name|list
parameter_list|)
block|{
name|Natives
operator|.
name|asList
argument_list|(
name|list
argument_list|)
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|FileInfo
operator|::
name|path
argument_list|,
name|FilenameComparator
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileName (String path)
specifier|public
specifier|static
name|String
name|getFileName
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|fileName
decl_stmt|;
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
name|fileName
operator|=
literal|"Commit Message"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Patch
operator|.
name|MERGE_LIST
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|fileName
operator|=
literal|"Merge List"
expr_stmt|;
block|}
else|else
block|{
name|fileName
operator|=
name|path
expr_stmt|;
block|}
name|int
name|s
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
return|return
name|s
operator|>=
literal|0
condition|?
name|fileName
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
else|:
name|fileName
return|;
block|}
DECL|method|FileInfo ()
specifier|protected
name|FileInfo
parameter_list|()
block|{}
block|}
end_class

end_unit


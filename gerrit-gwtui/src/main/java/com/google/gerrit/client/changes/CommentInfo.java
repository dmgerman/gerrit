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
name|CommentRange
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
name|changes
operator|.
name|Side
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
DECL|class|CommentInfo
specifier|public
class|class
name|CommentInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|createRange (String path, Side side, int line, String in_reply_to, String message, CommentRange range)
specifier|public
specifier|static
name|CommentInfo
name|createRange
parameter_list|(
name|String
name|path
parameter_list|,
name|Side
name|side
parameter_list|,
name|int
name|line
parameter_list|,
name|String
name|in_reply_to
parameter_list|,
name|String
name|message
parameter_list|,
name|CommentRange
name|range
parameter_list|)
block|{
name|CommentInfo
name|info
init|=
name|createFile
argument_list|(
name|path
argument_list|,
name|side
argument_list|,
name|in_reply_to
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|info
operator|.
name|setRange
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|info
operator|.
name|setLine
argument_list|(
name|range
operator|==
literal|null
condition|?
name|line
else|:
name|range
operator|.
name|end_line
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|createFile (String path, Side side, String in_reply_to, String message)
specifier|public
specifier|static
name|CommentInfo
name|createFile
parameter_list|(
name|String
name|path
parameter_list|,
name|Side
name|side
parameter_list|,
name|String
name|in_reply_to
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|CommentInfo
name|info
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|info
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|info
operator|.
name|setSide
argument_list|(
name|side
argument_list|)
expr_stmt|;
name|info
operator|.
name|setInReplyTo
argument_list|(
name|in_reply_to
argument_list|)
expr_stmt|;
name|info
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|setId (String id)
specifier|private
specifier|final
specifier|native
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
comment|/*-{ this.id = id; }-*/
function_decl|;
DECL|method|setPath (String path)
specifier|private
specifier|final
specifier|native
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
comment|/*-{ this.path = path; }-*/
function_decl|;
DECL|method|setSide (Side side)
specifier|private
specifier|final
name|void
name|setSide
parameter_list|(
name|Side
name|side
parameter_list|)
block|{
name|setSideRaw
argument_list|(
name|side
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setSideRaw (String side)
specifier|private
specifier|final
specifier|native
name|void
name|setSideRaw
parameter_list|(
name|String
name|side
parameter_list|)
comment|/*-{ this.side = side; }-*/
function_decl|;
DECL|method|setLine (int line)
specifier|private
specifier|final
specifier|native
name|void
name|setLine
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{ this.line = line; }-*/
function_decl|;
DECL|method|setInReplyTo (String in_reply_to)
specifier|private
specifier|final
specifier|native
name|void
name|setInReplyTo
parameter_list|(
name|String
name|in_reply_to
parameter_list|)
comment|/*-{     this.in_reply_to = in_reply_to;   }-*/
function_decl|;
DECL|method|setMessage (String message)
specifier|private
specifier|final
specifier|native
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
comment|/*-{ this.message = message; }-*/
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
DECL|method|path ()
specifier|public
specifier|final
specifier|native
name|String
name|path
parameter_list|()
comment|/*-{ return this.path; }-*/
function_decl|;
DECL|method|side ()
specifier|public
specifier|final
name|Side
name|side
parameter_list|()
block|{
name|String
name|s
init|=
name|sideRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|Side
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|Side
operator|.
name|REVISION
return|;
block|}
DECL|method|sideRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|sideRaw
parameter_list|()
comment|/*-{ return this.side }-*/
function_decl|;
DECL|method|line ()
specifier|public
specifier|final
specifier|native
name|int
name|line
parameter_list|()
comment|/*-{ return this.line; }-*/
function_decl|;
DECL|method|in_reply_to ()
specifier|public
specifier|final
specifier|native
name|String
name|in_reply_to
parameter_list|()
comment|/*-{ return this.in_reply_to; }-*/
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
DECL|method|updated ()
specifier|public
specifier|final
name|Timestamp
name|updated
parameter_list|()
block|{
name|String
name|updatedRaw
init|=
name|updatedRaw
argument_list|()
decl_stmt|;
return|return
name|updatedRaw
operator|==
literal|null
condition|?
literal|null
else|:
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|updatedRaw
argument_list|()
argument_list|)
return|;
block|}
DECL|method|updatedRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|updatedRaw
parameter_list|()
comment|/*-{ return this.updated; }-*/
function_decl|;
DECL|method|author ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|author
parameter_list|()
comment|/*-{ return this.author; }-*/
function_decl|;
DECL|method|has_line ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_line
parameter_list|()
comment|/*-{ return this.hasOwnProperty('line'); }-*/
function_decl|;
DECL|method|range ()
specifier|public
specifier|final
specifier|native
name|CommentRange
name|range
parameter_list|()
comment|/*-{ return this.range; }-*/
function_decl|;
DECL|method|setRange (CommentRange range)
specifier|public
specifier|final
specifier|native
name|void
name|setRange
parameter_list|(
name|CommentRange
name|range
parameter_list|)
comment|/*-{ this.range = range; }-*/
function_decl|;
DECL|method|CommentInfo ()
specifier|protected
name|CommentInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


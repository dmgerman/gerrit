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
name|extensions
operator|.
name|client
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
DECL|method|create (String path, Side side, int line, CommentRange range)
specifier|public
specifier|static
name|CommentInfo
name|create
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
name|CommentRange
name|range
parameter_list|)
block|{
name|CommentInfo
name|n
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|n
operator|.
name|path
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|n
operator|.
name|side
argument_list|(
name|side
argument_list|)
expr_stmt|;
if|if
condition|(
name|range
operator|!=
literal|null
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|range
operator|.
name|endLine
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|range
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|>
literal|0
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
DECL|method|createReply (CommentInfo r)
specifier|public
specifier|static
name|CommentInfo
name|createReply
parameter_list|(
name|CommentInfo
name|r
parameter_list|)
block|{
name|CommentInfo
name|n
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|n
operator|.
name|path
argument_list|(
name|r
operator|.
name|path
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|side
argument_list|(
name|r
operator|.
name|side
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|inReplyTo
argument_list|(
name|r
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|hasRange
argument_list|()
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|r
operator|.
name|range
argument_list|()
operator|.
name|endLine
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|range
argument_list|(
name|r
operator|.
name|range
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|hasLine
argument_list|()
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|r
operator|.
name|line
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
DECL|method|copy (CommentInfo s)
specifier|public
specifier|static
name|CommentInfo
name|copy
parameter_list|(
name|CommentInfo
name|s
parameter_list|)
block|{
name|CommentInfo
name|n
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|n
operator|.
name|path
argument_list|(
name|s
operator|.
name|path
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|side
argument_list|(
name|s
operator|.
name|side
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|id
argument_list|(
name|s
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|inReplyTo
argument_list|(
name|s
operator|.
name|inReplyTo
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|message
argument_list|(
name|s
operator|.
name|message
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|hasRange
argument_list|()
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|s
operator|.
name|range
argument_list|()
operator|.
name|endLine
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|range
argument_list|(
name|s
operator|.
name|range
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|hasLine
argument_list|()
condition|)
block|{
name|n
operator|.
name|line
argument_list|(
name|s
operator|.
name|line
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|n
return|;
block|}
DECL|method|path (String p)
specifier|public
specifier|final
specifier|native
name|void
name|path
parameter_list|(
name|String
name|p
parameter_list|)
comment|/*-{ this.path = p }-*/
function_decl|;
DECL|method|id (String i)
specifier|public
specifier|final
specifier|native
name|void
name|id
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.id = i }-*/
function_decl|;
DECL|method|line (int n)
specifier|public
specifier|final
specifier|native
name|void
name|line
parameter_list|(
name|int
name|n
parameter_list|)
comment|/*-{ this.line = n }-*/
function_decl|;
DECL|method|range (CommentRange r)
specifier|public
specifier|final
specifier|native
name|void
name|range
parameter_list|(
name|CommentRange
name|r
parameter_list|)
comment|/*-{ this.range = r }-*/
function_decl|;
DECL|method|inReplyTo (String i)
specifier|public
specifier|final
specifier|native
name|void
name|inReplyTo
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.in_reply_to = i }-*/
function_decl|;
DECL|method|message (String m)
specifier|public
specifier|final
specifier|native
name|void
name|message
parameter_list|(
name|String
name|m
parameter_list|)
comment|/*-{ this.message = m }-*/
function_decl|;
DECL|method|side (Side side)
specifier|public
specifier|final
name|void
name|side
parameter_list|(
name|Side
name|side
parameter_list|)
block|{
name|sideRaw
argument_list|(
name|side
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|sideRaw (String s)
specifier|private
specifier|final
specifier|native
name|void
name|sideRaw
parameter_list|(
name|String
name|s
parameter_list|)
comment|/*-{ this.side = s }-*/
function_decl|;
DECL|method|path ()
specifier|public
specifier|final
specifier|native
name|String
name|path
parameter_list|()
comment|/*-{ return this.path }-*/
function_decl|;
DECL|method|id ()
specifier|public
specifier|final
specifier|native
name|String
name|id
parameter_list|()
comment|/*-{ return this.id }-*/
function_decl|;
DECL|method|inReplyTo ()
specifier|public
specifier|final
specifier|native
name|String
name|inReplyTo
parameter_list|()
comment|/*-{ return this.in_reply_to }-*/
function_decl|;
DECL|method|patchSet ()
specifier|public
specifier|final
specifier|native
name|int
name|patchSet
parameter_list|()
comment|/*-{ return this.patch_set }-*/
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
DECL|method|updated ()
specifier|public
specifier|final
name|Timestamp
name|updated
parameter_list|()
block|{
name|Timestamp
name|r
init|=
name|updatedTimestamp
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|String
name|s
init|=
name|updatedRaw
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|r
operator|=
name|JavaSqlTimestamp_JsonSerializer
operator|.
name|parseTimestamp
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|updatedTimestamp
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
DECL|method|updatedRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|updatedRaw
parameter_list|()
comment|/*-{ return this.updated }-*/
function_decl|;
DECL|method|updatedTimestamp ()
specifier|private
specifier|final
specifier|native
name|Timestamp
name|updatedTimestamp
parameter_list|()
comment|/*-{ return this._ts }-*/
function_decl|;
DECL|method|updatedTimestamp (Timestamp t)
specifier|private
specifier|final
specifier|native
name|void
name|updatedTimestamp
parameter_list|(
name|Timestamp
name|t
parameter_list|)
comment|/*-{ this._ts = t }-*/
function_decl|;
DECL|method|author ()
specifier|public
specifier|final
specifier|native
name|AccountInfo
name|author
parameter_list|()
comment|/*-{ return this.author }-*/
function_decl|;
DECL|method|line ()
specifier|public
specifier|final
specifier|native
name|int
name|line
parameter_list|()
comment|/*-{ return this.line || 0 }-*/
function_decl|;
DECL|method|hasLine ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasLine
parameter_list|()
comment|/*-{ return this.hasOwnProperty('line') }-*/
function_decl|;
DECL|method|hasRange ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hasRange
parameter_list|()
comment|/*-{ return this.hasOwnProperty('range') }-*/
function_decl|;
DECL|method|range ()
specifier|public
specifier|final
specifier|native
name|CommentRange
name|range
parameter_list|()
comment|/*-{ return this.range }-*/
function_decl|;
DECL|method|message ()
specifier|public
specifier|final
specifier|native
name|String
name|message
parameter_list|()
comment|/*-{ return this.message }-*/
function_decl|;
DECL|method|CommentInfo ()
specifier|protected
name|CommentInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit


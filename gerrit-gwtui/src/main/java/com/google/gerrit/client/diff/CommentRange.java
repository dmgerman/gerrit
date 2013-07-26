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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|LineCharacter
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|TextMarker
operator|.
name|FromTo
import|;
end_import

begin_class
DECL|class|CommentRange
specifier|public
class|class
name|CommentRange
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (int sl, int sc, int el, int ec)
specifier|public
specifier|static
name|CommentRange
name|create
parameter_list|(
name|int
name|sl
parameter_list|,
name|int
name|sc
parameter_list|,
name|int
name|el
parameter_list|,
name|int
name|ec
parameter_list|)
block|{
name|CommentRange
name|r
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|r
operator|.
name|set
argument_list|(
name|sl
argument_list|,
name|sc
argument_list|,
name|el
argument_list|,
name|ec
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|create (FromTo fromTo)
specifier|public
specifier|static
name|CommentRange
name|create
parameter_list|(
name|FromTo
name|fromTo
parameter_list|)
block|{
if|if
condition|(
name|fromTo
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|LineCharacter
name|from
init|=
name|fromTo
operator|.
name|getFrom
argument_list|()
decl_stmt|;
name|LineCharacter
name|to
init|=
name|fromTo
operator|.
name|getTo
argument_list|()
decl_stmt|;
return|return
name|create
argument_list|(
name|from
operator|.
name|getLine
argument_list|()
operator|+
literal|1
argument_list|,
name|from
operator|.
name|getCh
argument_list|()
argument_list|,
name|to
operator|.
name|getLine
argument_list|()
operator|+
literal|1
argument_list|,
name|to
operator|.
name|getCh
argument_list|()
argument_list|)
return|;
block|}
DECL|method|start_line ()
specifier|public
specifier|final
specifier|native
name|int
name|start_line
parameter_list|()
comment|/*-{ return this.start_line; }-*/
function_decl|;
DECL|method|start_character ()
specifier|public
specifier|final
specifier|native
name|int
name|start_character
parameter_list|()
comment|/*-{ return this.start_character; }-*/
function_decl|;
DECL|method|end_line ()
specifier|public
specifier|final
specifier|native
name|int
name|end_line
parameter_list|()
comment|/*-{ return this.end_line; }-*/
function_decl|;
DECL|method|end_character ()
specifier|public
specifier|final
specifier|native
name|int
name|end_character
parameter_list|()
comment|/*-{ return this.end_character; }-*/
function_decl|;
DECL|method|set (int sl, int sc, int el, int ec)
specifier|private
specifier|final
specifier|native
name|void
name|set
parameter_list|(
name|int
name|sl
parameter_list|,
name|int
name|sc
parameter_list|,
name|int
name|el
parameter_list|,
name|int
name|ec
parameter_list|)
comment|/*-{     this.start_line = sl;     this.start_character = sc;     this.end_line = el;     this.end_character = ec;   }-*/
function_decl|;
DECL|method|CommentRange ()
specifier|protected
name|CommentRange
parameter_list|()
block|{   }
block|}
end_class

end_unit


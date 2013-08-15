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
DECL|package|net.codemirror.lib
package|package
name|net
operator|.
name|codemirror
operator|.
name|lib
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_comment
comment|/** Object that represents a text marker within CodeMirror */
end_comment

begin_class
DECL|class|TextMarker
specifier|public
class|class
name|TextMarker
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|TextMarker
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
DECL|method|clear ()
specifier|public
specifier|final
specifier|native
name|void
name|clear
parameter_list|()
comment|/*-{ this.clear(); }-*/
function_decl|;
DECL|method|changed ()
specifier|public
specifier|final
specifier|native
name|void
name|changed
parameter_list|()
comment|/*-{ this.changed(); }-*/
function_decl|;
DECL|method|find ()
specifier|public
specifier|final
specifier|native
name|FromTo
name|find
parameter_list|()
comment|/*-{ return this.find(); }-*/
function_decl|;
DECL|method|TextMarker ()
specifier|protected
name|TextMarker
parameter_list|()
block|{   }
DECL|class|FromTo
specifier|public
specifier|static
class|class
name|FromTo
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (LineCharacter from, LineCharacter to)
specifier|public
specifier|static
name|FromTo
name|create
parameter_list|(
name|LineCharacter
name|from
parameter_list|,
name|LineCharacter
name|to
parameter_list|)
block|{
name|FromTo
name|fromTo
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|fromTo
operator|.
name|setFrom
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|fromTo
operator|.
name|setTo
argument_list|(
name|to
argument_list|)
expr_stmt|;
return|return
name|fromTo
return|;
block|}
DECL|method|create (CommentRange range)
specifier|public
specifier|static
name|FromTo
name|create
parameter_list|(
name|CommentRange
name|range
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|LineCharacter
operator|.
name|create
argument_list|(
name|range
operator|.
name|start_line
argument_list|()
operator|-
literal|1
argument_list|,
name|range
operator|.
name|start_character
argument_list|()
argument_list|)
argument_list|,
name|LineCharacter
operator|.
name|create
argument_list|(
name|range
operator|.
name|end_line
argument_list|()
operator|-
literal|1
argument_list|,
name|range
operator|.
name|end_character
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getFrom ()
specifier|public
specifier|final
specifier|native
name|LineCharacter
name|getFrom
parameter_list|()
comment|/*-{ return this.from; }-*/
function_decl|;
DECL|method|getTo ()
specifier|public
specifier|final
specifier|native
name|LineCharacter
name|getTo
parameter_list|()
comment|/*-{ return this.to; }-*/
function_decl|;
DECL|method|setFrom (LineCharacter from)
specifier|public
specifier|final
specifier|native
name|void
name|setFrom
parameter_list|(
name|LineCharacter
name|from
parameter_list|)
comment|/*-{ this.from = from; }-*/
function_decl|;
DECL|method|setTo (LineCharacter to)
specifier|public
specifier|final
specifier|native
name|void
name|setTo
parameter_list|(
name|LineCharacter
name|to
parameter_list|)
comment|/*-{ this.to = to; }-*/
function_decl|;
DECL|method|FromTo ()
specifier|protected
name|FromTo
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit


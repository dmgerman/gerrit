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
DECL|method|on (String event, Runnable thunk)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|Runnable
name|thunk
parameter_list|)
comment|/*-{ this.on(event, function(){$entry(thunk.@java.lang.Runnable::run()())}) }-*/
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
DECL|method|create (Pos f, Pos t)
specifier|public
specifier|static
specifier|final
specifier|native
name|FromTo
name|create
parameter_list|(
name|Pos
name|f
parameter_list|,
name|Pos
name|t
parameter_list|)
comment|/*-{       return {from: f, to: t}     }-*/
function_decl|;
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
name|Pos
operator|.
name|create
argument_list|(
name|range
operator|.
name|startLine
argument_list|()
operator|-
literal|1
argument_list|,
name|range
operator|.
name|startCharacter
argument_list|()
argument_list|)
argument_list|,
name|Pos
operator|.
name|create
argument_list|(
name|range
operator|.
name|endLine
argument_list|()
operator|-
literal|1
argument_list|,
name|range
operator|.
name|endCharacter
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|from ()
specifier|public
specifier|final
specifier|native
name|Pos
name|from
parameter_list|()
comment|/*-{ return this.from }-*/
function_decl|;
DECL|method|to ()
specifier|public
specifier|final
specifier|native
name|Pos
name|to
parameter_list|()
comment|/*-{ return this.to }-*/
function_decl|;
DECL|method|from (Pos f)
specifier|public
specifier|final
specifier|native
name|void
name|from
parameter_list|(
name|Pos
name|f
parameter_list|)
comment|/*-{ this.from = f }-*/
function_decl|;
DECL|method|to (Pos t)
specifier|public
specifier|final
specifier|native
name|void
name|to
parameter_list|(
name|Pos
name|t
parameter_list|)
comment|/*-{ this.to = t }-*/
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


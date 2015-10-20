begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|dom
operator|.
name|client
operator|.
name|StyleInjector
import|;
end_import

begin_class
DECL|class|LintLine
specifier|public
class|class
name|LintLine
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (String shortMsg, String msg, String sev, Pos line)
specifier|public
specifier|static
name|LintLine
name|create
parameter_list|(
name|String
name|shortMsg
parameter_list|,
name|String
name|msg
parameter_list|,
name|String
name|sev
parameter_list|,
name|Pos
name|line
parameter_list|)
block|{
name|StyleInjector
operator|.
name|inject
argument_list|(
literal|".CodeMirror-lint-marker-"
operator|+
name|sev
operator|+
literal|" {\n"
operator|+
literal|"  visibility: hidden;\n"
operator|+
literal|"  text-overflow: ellipsis;\n"
operator|+
literal|"  white-space: nowrap;\n"
operator|+
literal|"  overflow: hidden;\n"
operator|+
literal|"  position: relative;\n"
operator|+
literal|"}\n"
operator|+
literal|".CodeMirror-lint-marker-"
operator|+
name|sev
operator|+
literal|":after {\n"
operator|+
literal|"  content:'"
operator|+
name|shortMsg
operator|+
literal|"';\n"
operator|+
literal|"  visibility: visible;\n"
operator|+
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|create
argument_list|(
name|msg
argument_list|,
name|sev
argument_list|,
name|line
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|create (String msg, String sev, Pos f, Pos t)
specifier|public
specifier|static
specifier|native
name|LintLine
name|create
parameter_list|(
name|String
name|msg
parameter_list|,
name|String
name|sev
parameter_list|,
name|Pos
name|f
parameter_list|,
name|Pos
name|t
parameter_list|)
comment|/*-{     return {       message : msg,       severity : sev,       from : f,       to : t     };   }-*/
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
DECL|method|detailedMessage ()
specifier|public
specifier|final
specifier|native
name|String
name|detailedMessage
parameter_list|()
comment|/*-{ return this.message; }-*/
function_decl|;
DECL|method|severity ()
specifier|public
specifier|final
specifier|native
name|String
name|severity
parameter_list|()
comment|/*-{ return this.severity; }-*/
function_decl|;
DECL|method|from ()
specifier|public
specifier|final
specifier|native
name|Pos
name|from
parameter_list|()
comment|/*-{ return this.from; }-*/
function_decl|;
DECL|method|to ()
specifier|public
specifier|final
specifier|native
name|Pos
name|to
parameter_list|()
comment|/*-{ return this.to; }-*/
function_decl|;
DECL|method|LintLine ()
specifier|protected
name|LintLine
parameter_list|()
block|{   }
block|}
end_class

end_unit


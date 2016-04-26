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
name|Element
import|;
end_import

begin_comment
comment|/** Object that represents a text marker within CodeMirror */
end_comment

begin_class
DECL|class|MergeView
specifier|public
class|class
name|MergeView
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (Element p, Configuration cfg)
specifier|public
specifier|static
name|MergeView
name|create
parameter_list|(
name|Element
name|p
parameter_list|,
name|Configuration
name|cfg
parameter_list|)
block|{
name|MergeView
name|mv
init|=
name|newMergeView
argument_list|(
name|p
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|Extras
operator|.
name|attach
argument_list|(
name|mv
operator|.
name|leftOriginal
argument_list|()
argument_list|)
expr_stmt|;
name|Extras
operator|.
name|attach
argument_list|(
name|mv
operator|.
name|editor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mv
return|;
block|}
DECL|method|newMergeView (Element p, Configuration cfg)
specifier|private
specifier|static
specifier|native
name|MergeView
name|newMergeView
parameter_list|(
name|Element
name|p
parameter_list|,
name|Configuration
name|cfg
parameter_list|)
comment|/*-{     return $wnd.CodeMirror.MergeView(p, cfg);   }-*/
function_decl|;
DECL|method|leftOriginal ()
specifier|public
specifier|final
specifier|native
name|CodeMirror
name|leftOriginal
parameter_list|()
comment|/*-{     return this.leftOriginal();   }-*/
function_decl|;
DECL|method|editor ()
specifier|public
specifier|final
specifier|native
name|CodeMirror
name|editor
parameter_list|()
comment|/*-{     return this.editor();   }-*/
function_decl|;
DECL|method|setShowDifferences (boolean b)
specifier|public
specifier|final
specifier|native
name|void
name|setShowDifferences
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{     this.setShowDifferences(b);   }-*/
function_decl|;
DECL|method|getGapElement ()
specifier|public
specifier|final
specifier|native
name|Element
name|getGapElement
parameter_list|()
comment|/*-{     return $doc.getElementsByClassName("CodeMirror-merge-gap")[0];   }-*/
function_decl|;
DECL|method|MergeView ()
specifier|protected
name|MergeView
parameter_list|()
block|{   }
block|}
end_class

end_unit


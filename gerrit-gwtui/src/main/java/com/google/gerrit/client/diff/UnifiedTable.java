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
name|GWT
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|resources
operator|.
name|client
operator|.
name|CssResource
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|ui
operator|.
name|HTMLPanel
import|;
end_import

begin_comment
comment|/**  * A table with one row and one column to hold a unified CodeMirror displaying  * the files to be compared.  */
end_comment

begin_class
DECL|class|UnifiedTable
class|class
name|UnifiedTable
extends|extends
name|DiffTable
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|UnifiedTable
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|DiffTableStyle
interface|interface
name|DiffTableStyle
extends|extends
name|CssResource
block|{
DECL|method|intralineInsert ()
name|String
name|intralineInsert
parameter_list|()
function_decl|;
DECL|method|intralineDelete ()
name|String
name|intralineDelete
parameter_list|()
function_decl|;
DECL|method|diffInsert ()
name|String
name|diffInsert
parameter_list|()
function_decl|;
DECL|method|diffDelete ()
name|String
name|diffDelete
parameter_list|()
function_decl|;
DECL|method|unifiedLineNumber ()
name|String
name|unifiedLineNumber
parameter_list|()
function_decl|;
DECL|method|lineNumbersLeft ()
name|String
name|lineNumbersLeft
parameter_list|()
function_decl|;
DECL|method|lineNumbersRight ()
name|String
name|lineNumbersRight
parameter_list|()
function_decl|;
block|}
DECL|field|parent
specifier|private
name|Unified
name|parent
decl_stmt|;
DECL|field|cm
annotation|@
name|UiField
name|Element
name|cm
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
specifier|static
name|DiffTableStyle
name|style
decl_stmt|;
DECL|method|UnifiedTable (Unified parent, PatchSet.Id base, PatchSet.Id revision, String path)
name|UnifiedTable
parameter_list|(
name|Unified
name|parent
parameter_list|,
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|PatchSet
operator|.
name|Id
name|revision
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setHideEmptyPane (boolean hide)
name|void
name|setHideEmptyPane
parameter_list|(
name|boolean
name|hide
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|isVisibleA ()
name|boolean
name|isVisibleA
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getDiffScreen ()
name|Unified
name|getDiffScreen
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
block|}
end_class

end_unit


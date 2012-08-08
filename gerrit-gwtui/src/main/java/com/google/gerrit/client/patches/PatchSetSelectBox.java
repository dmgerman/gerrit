begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|Dispatcher
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
name|Gerrit
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
name|PatchScript
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
name|PatchSetDetail
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
name|DivElement
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
name|Style
operator|.
name|Display
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickHandler
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
name|DOM
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
name|Anchor
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
name|Composite
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
name|Image
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|KeyUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|PatchSetSelectBox
specifier|public
class|class
name|PatchSetSelectBox
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|PatchSetSelectBox
argument_list|>
block|{   }
DECL|field|uiBinder
specifier|private
specifier|static
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
DECL|interface|BoxStyle
interface|interface
name|BoxStyle
extends|extends
name|CssResource
block|{
DECL|method|selected ()
name|String
name|selected
parameter_list|()
function_decl|;
DECL|method|hidden ()
name|String
name|hidden
parameter_list|()
function_decl|;
DECL|method|downloadLink ()
name|String
name|downloadLink
parameter_list|()
function_decl|;
block|}
DECL|enum|Side
specifier|public
enum|enum
name|Side
block|{
DECL|enumConstant|A
DECL|enumConstant|B
name|A
block|,
name|B
block|}
DECL|field|script
name|PatchScript
name|script
decl_stmt|;
DECL|field|patchKey
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|idSideA
name|PatchSet
operator|.
name|Id
name|idSideA
decl_stmt|;
DECL|field|idSideB
name|PatchSet
operator|.
name|Id
name|idSideB
decl_stmt|;
DECL|field|idActive
name|PatchSet
operator|.
name|Id
name|idActive
decl_stmt|;
DECL|field|side
name|Side
name|side
decl_stmt|;
DECL|field|screenType
name|PatchScreen
operator|.
name|Type
name|screenType
decl_stmt|;
DECL|field|links
name|List
argument_list|<
name|Anchor
argument_list|>
name|links
decl_stmt|;
annotation|@
name|UiField
DECL|field|linkPanel
name|HTMLPanel
name|linkPanel
decl_stmt|;
annotation|@
name|UiField
DECL|field|style
name|BoxStyle
name|style
decl_stmt|;
annotation|@
name|UiField
DECL|field|sideMarker
name|DivElement
name|sideMarker
decl_stmt|;
DECL|method|PatchSetSelectBox (Side side, final PatchScreen.Type type)
specifier|public
name|PatchSetSelectBox
parameter_list|(
name|Side
name|side
parameter_list|,
specifier|final
name|PatchScreen
operator|.
name|Type
name|type
parameter_list|)
block|{
name|this
operator|.
name|side
operator|=
name|side
expr_stmt|;
name|this
operator|.
name|screenType
operator|=
name|type
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
block|}
DECL|method|display (final PatchSetDetail detail, final PatchScript script, Patch.Key key, PatchSet.Id idSideA, PatchSet.Id idSideB)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|PatchSetDetail
name|detail
parameter_list|,
specifier|final
name|PatchScript
name|script
parameter_list|,
name|Patch
operator|.
name|Key
name|key
parameter_list|,
name|PatchSet
operator|.
name|Id
name|idSideA
parameter_list|,
name|PatchSet
operator|.
name|Id
name|idSideB
parameter_list|)
block|{
name|this
operator|.
name|script
operator|=
name|script
expr_stmt|;
name|this
operator|.
name|patchKey
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|idSideA
operator|=
name|idSideA
expr_stmt|;
name|this
operator|.
name|idSideB
operator|=
name|idSideB
expr_stmt|;
name|this
operator|.
name|idActive
operator|=
operator|(
name|side
operator|==
name|Side
operator|.
name|A
operator|)
condition|?
name|idSideA
else|:
name|idSideB
expr_stmt|;
name|this
operator|.
name|links
operator|=
operator|new
name|LinkedList
argument_list|<
name|Anchor
argument_list|>
argument_list|()
expr_stmt|;
name|linkPanel
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|screenType
operator|==
name|PatchScreen
operator|.
name|Type
operator|.
name|UNIFIED
condition|)
block|{
name|sideMarker
operator|.
name|setInnerText
argument_list|(
operator|(
name|side
operator|==
name|Side
operator|.
name|A
operator|)
condition|?
literal|"(-)"
else|:
literal|"(+)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sideMarker
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
name|Anchor
name|baseLink
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|detail
operator|.
name|getInfo
argument_list|()
operator|.
name|getParents
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|baseLink
operator|=
name|createLink
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|patchBaseAutoMerge
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|baseLink
operator|=
name|createLink
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|patchBase
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|links
operator|.
name|add
argument_list|(
name|baseLink
argument_list|)
expr_stmt|;
if|if
condition|(
name|screenType
operator|==
name|PatchScreen
operator|.
name|Type
operator|.
name|UNIFIED
operator|||
name|side
operator|==
name|Side
operator|.
name|A
condition|)
block|{
name|linkPanel
operator|.
name|add
argument_list|(
name|baseLink
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|side
operator|==
name|Side
operator|.
name|B
condition|)
block|{
name|links
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|hidden
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Patch
name|patch
range|:
name|script
operator|.
name|getHistory
argument_list|()
control|)
block|{
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|patch
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|Anchor
name|anchor
init|=
name|createLink
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|psId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|psId
argument_list|)
decl_stmt|;
name|links
operator|.
name|add
argument_list|(
name|anchor
argument_list|)
expr_stmt|;
name|linkPanel
operator|.
name|add
argument_list|(
name|anchor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|idActive
operator|==
literal|null
operator|&&
name|side
operator|==
name|Side
operator|.
name|A
condition|)
block|{
name|links
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|selected
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|links
operator|.
name|get
argument_list|(
name|idActive
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|selected
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Anchor
name|downloadLink
init|=
name|createDownloadLink
argument_list|()
decl_stmt|;
if|if
condition|(
name|downloadLink
operator|!=
literal|null
condition|)
block|{
name|linkPanel
operator|.
name|add
argument_list|(
name|downloadLink
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createLink (String label, final PatchSet.Id id)
specifier|private
name|Anchor
name|createLink
parameter_list|(
name|String
name|label
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
specifier|final
name|Anchor
name|anchor
init|=
operator|new
name|Anchor
argument_list|(
name|label
argument_list|)
decl_stmt|;
name|anchor
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|side
operator|==
name|Side
operator|.
name|A
condition|)
block|{
name|idSideA
operator|=
name|id
expr_stmt|;
block|}
else|else
block|{
name|idSideB
operator|=
name|id
expr_stmt|;
block|}
name|Patch
operator|.
name|Key
name|keySideB
init|=
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|idSideB
argument_list|,
name|patchKey
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|screenType
condition|)
block|{
case|case
name|SIDE_BY_SIDE
case|:
name|Gerrit
operator|.
name|display
argument_list|(
name|Dispatcher
operator|.
name|toPatchSideBySide
argument_list|(
name|idSideA
argument_list|,
name|keySideB
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNIFIED
case|:
name|Gerrit
operator|.
name|display
argument_list|(
name|Dispatcher
operator|.
name|toPatchUnified
argument_list|(
name|idSideA
argument_list|,
name|keySideB
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|anchor
return|;
block|}
DECL|method|createDownloadLink ()
specifier|private
name|Anchor
name|createDownloadLink
parameter_list|()
block|{
name|boolean
name|isCommitMessage
init|=
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|script
operator|.
name|getNewName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isCommitMessage
operator|||
operator|(
name|side
operator|==
name|Side
operator|.
name|A
operator|&&
literal|0
operator|>=
name|script
operator|.
name|getA
argument_list|()
operator|.
name|size
argument_list|()
operator|)
operator|||
operator|(
name|side
operator|==
name|Side
operator|.
name|B
operator|&&
literal|0
operator|>=
name|script
operator|.
name|getB
argument_list|()
operator|.
name|size
argument_list|()
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Patch
operator|.
name|Key
name|key
init|=
operator|(
name|idSideA
operator|==
literal|null
operator|)
condition|?
name|patchKey
else|:
operator|(
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|idSideA
argument_list|,
name|patchKey
operator|.
name|get
argument_list|()
argument_list|)
operator|)
decl_stmt|;
name|String
name|sideURL
init|=
operator|(
name|side
operator|==
name|Side
operator|.
name|A
operator|)
condition|?
literal|"1"
else|:
literal|"0"
decl_stmt|;
specifier|final
name|String
name|base
init|=
name|GWT
operator|.
name|getHostPageBaseURL
argument_list|()
operator|+
literal|"cat/"
decl_stmt|;
name|Image
name|image
init|=
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|downloadIcon
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Anchor
name|anchor
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|anchor
operator|.
name|setHref
argument_list|(
name|base
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"^"
operator|+
name|sideURL
argument_list|)
expr_stmt|;
name|anchor
operator|.
name|setTitle
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|download
argument_list|()
argument_list|)
expr_stmt|;
name|anchor
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|downloadLink
argument_list|()
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|insertBefore
argument_list|(
name|anchor
operator|.
name|getElement
argument_list|()
argument_list|,
name|image
operator|.
name|getElement
argument_list|()
argument_list|,
name|DOM
operator|.
name|getFirstChild
argument_list|(
name|anchor
operator|.
name|getElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|anchor
return|;
block|}
block|}
end_class

end_unit


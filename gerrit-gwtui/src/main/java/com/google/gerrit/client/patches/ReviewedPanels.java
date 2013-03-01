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
name|client
operator|.
name|VoidResult
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
name|changes
operator|.
name|PatchTable
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
name|changes
operator|.
name|PatchTable
operator|.
name|PatchValidator
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
name|changes
operator|.
name|Util
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
name|rpc
operator|.
name|RestApi
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
name|ui
operator|.
name|ChangeLink
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
name|ui
operator|.
name|InlineHyperlink
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ValueChangeEvent
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
name|logical
operator|.
name|shared
operator|.
name|ValueChangeHandler
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
name|rpc
operator|.
name|AsyncCallback
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
name|CheckBox
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
name|FlowPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtml
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_class
DECL|class|ReviewedPanels
specifier|public
class|class
name|ReviewedPanels
block|{
DECL|field|top
specifier|public
specifier|final
name|FlowPanel
name|top
decl_stmt|;
DECL|field|bottom
specifier|public
specifier|final
name|FlowPanel
name|bottom
decl_stmt|;
DECL|field|patchKey
specifier|private
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|fileList
specifier|private
name|PatchTable
name|fileList
decl_stmt|;
DECL|field|reviewedLink
specifier|private
name|InlineHyperlink
name|reviewedLink
decl_stmt|;
DECL|field|checkBoxTop
specifier|private
name|CheckBox
name|checkBoxTop
decl_stmt|;
DECL|field|checkBoxBottom
specifier|private
name|CheckBox
name|checkBoxBottom
decl_stmt|;
DECL|method|ReviewedPanels ()
specifier|public
name|ReviewedPanels
parameter_list|()
block|{
name|this
operator|.
name|top
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|this
operator|.
name|bottom
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|this
operator|.
name|bottom
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|reviewedPanelBottom
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|populate (Patch.Key pk, PatchTable pt, int patchIndex, PatchScreen.Type patchScreenType)
specifier|public
name|void
name|populate
parameter_list|(
name|Patch
operator|.
name|Key
name|pk
parameter_list|,
name|PatchTable
name|pt
parameter_list|,
name|int
name|patchIndex
parameter_list|,
name|PatchScreen
operator|.
name|Type
name|patchScreenType
parameter_list|)
block|{
name|patchKey
operator|=
name|pk
expr_stmt|;
name|fileList
operator|=
name|pt
expr_stmt|;
name|reviewedLink
operator|=
name|createReviewedLink
argument_list|(
name|patchIndex
argument_list|,
name|patchScreenType
argument_list|)
expr_stmt|;
name|top
operator|.
name|clear
argument_list|()
expr_stmt|;
name|checkBoxTop
operator|=
name|createReviewedCheckbox
argument_list|()
expr_stmt|;
name|top
operator|.
name|add
argument_list|(
name|checkBoxTop
argument_list|)
expr_stmt|;
name|top
operator|.
name|add
argument_list|(
name|createReviewedAnchor
argument_list|()
argument_list|)
expr_stmt|;
name|bottom
operator|.
name|clear
argument_list|()
expr_stmt|;
name|checkBoxBottom
operator|=
name|createReviewedCheckbox
argument_list|()
expr_stmt|;
name|bottom
operator|.
name|add
argument_list|(
name|checkBoxBottom
argument_list|)
expr_stmt|;
name|bottom
operator|.
name|add
argument_list|(
name|createReviewedAnchor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createReviewedCheckbox ()
specifier|private
name|CheckBox
name|createReviewedCheckbox
parameter_list|()
block|{
specifier|final
name|CheckBox
name|checkBox
init|=
operator|new
name|CheckBox
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|reviewedAnd
argument_list|()
operator|+
literal|" "
argument_list|)
decl_stmt|;
name|checkBox
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onValueChange
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|event
parameter_list|)
block|{
specifier|final
name|boolean
name|value
init|=
name|event
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|setReviewedByCurrentUser
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkBoxTop
operator|.
name|getValue
argument_list|()
operator|!=
name|value
condition|)
block|{
name|checkBoxTop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checkBoxBottom
operator|.
name|getValue
argument_list|()
operator|!=
name|value
condition|)
block|{
name|checkBoxBottom
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|checkBox
return|;
block|}
DECL|method|getValue ()
specifier|public
name|boolean
name|getValue
parameter_list|()
block|{
return|return
name|checkBoxTop
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|setValue (final boolean value)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|boolean
name|value
parameter_list|)
block|{
name|checkBoxTop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|checkBoxBottom
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|setReviewedByCurrentUser (boolean reviewed)
specifier|public
name|void
name|setReviewedByCurrentUser
parameter_list|(
name|boolean
name|reviewed
parameter_list|)
block|{
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|fileList
operator|.
name|updateReviewedStatus
argument_list|(
name|patchKey
argument_list|,
name|reviewed
argument_list|)
expr_stmt|;
block|}
name|PatchSet
operator|.
name|Id
name|ps
init|=
name|patchKey
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|RestApi
name|api
init|=
operator|new
name|RestApi
argument_list|(
literal|"/changes/"
argument_list|)
operator|.
name|id
argument_list|(
name|ps
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|view
argument_list|(
literal|"revisions"
argument_list|)
operator|.
name|id
argument_list|(
name|ps
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|view
argument_list|(
literal|"files"
argument_list|)
operator|.
name|id
argument_list|(
name|patchKey
operator|.
name|getFileName
argument_list|()
argument_list|)
operator|.
name|view
argument_list|(
literal|"reviewed"
argument_list|)
decl_stmt|;
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
init|=
operator|new
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|arg0
parameter_list|)
block|{
comment|// nop
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|VoidResult
name|result
parameter_list|)
block|{
comment|// nop
block|}
block|}
decl_stmt|;
if|if
condition|(
name|reviewed
condition|)
block|{
name|api
operator|.
name|put
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|api
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
if|if
condition|(
name|reviewedLink
operator|!=
literal|null
condition|)
block|{
name|setReviewedByCurrentUser
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|reviewedLink
operator|.
name|go
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createReviewedLink (final int patchIndex, final PatchScreen.Type patchScreenType)
specifier|private
name|InlineHyperlink
name|createReviewedLink
parameter_list|(
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|PatchScreen
operator|.
name|Type
name|patchScreenType
parameter_list|)
block|{
specifier|final
name|PatchValidator
name|unreviewedValidator
init|=
operator|new
name|PatchValidator
argument_list|()
block|{
specifier|public
name|boolean
name|isValid
parameter_list|(
name|Patch
name|patch
parameter_list|)
block|{
return|return
operator|!
name|patch
operator|.
name|isReviewedByCurrentUser
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|InlineHyperlink
name|reviewedLink
init|=
operator|new
name|ChangeLink
argument_list|(
literal|""
argument_list|,
name|patchKey
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|int
name|nextUnreviewedPatchIndex
init|=
name|fileList
operator|.
name|getNextPatch
argument_list|(
name|patchIndex
argument_list|,
literal|true
argument_list|,
name|unreviewedValidator
argument_list|,
name|fileList
operator|.
name|PREFERENCE_VALIDATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextUnreviewedPatchIndex
operator|>
operator|-
literal|1
condition|)
block|{
comment|// Create invisible patch link to change page
name|reviewedLink
operator|=
name|fileList
operator|.
name|createLink
argument_list|(
name|nextUnreviewedPatchIndex
argument_list|,
name|patchScreenType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|reviewedLink
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|reviewedLink
return|;
block|}
DECL|method|createReviewedAnchor ()
specifier|private
name|Anchor
name|createReviewedAnchor
parameter_list|()
block|{
name|SafeHtmlBuilder
name|text
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|text
operator|.
name|append
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|text
operator|.
name|append
argument_list|(
name|SafeHtml
operator|.
name|asis
argument_list|(
name|Util
operator|.
name|C
operator|.
name|nextPatchLinkIcon
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Anchor
name|reviewedAnchor
init|=
operator|new
name|Anchor
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|reviewedAnchor
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|reviewedAnchor
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
name|setReviewedByCurrentUser
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|reviewedLink
operator|.
name|go
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|reviewedAnchor
return|;
block|}
block|}
end_class

end_unit


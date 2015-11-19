begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|resources
operator|.
name|client
operator|.
name|ClientBundle
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
name|ImageResource
import|;
end_import

begin_interface
DECL|interface|Resources
specifier|public
interface|interface
name|Resources
extends|extends
name|ClientBundle
block|{
annotation|@
name|Source
argument_list|(
literal|"addFileComment.png"
argument_list|)
DECL|method|addFileComment ()
name|ImageResource
name|addFileComment
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"arrowDown.png"
argument_list|)
DECL|method|arrowDown ()
name|ImageResource
name|arrowDown
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"arrowRight.png"
argument_list|)
DECL|method|arrowRight ()
name|ImageResource
name|arrowRight
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"arrowUp.png"
argument_list|)
DECL|method|arrowUp ()
name|ImageResource
name|arrowUp
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"deleteHover.png"
argument_list|)
DECL|method|deleteHover ()
name|ImageResource
name|deleteHover
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"deleteNormal.png"
argument_list|)
DECL|method|deleteNormal ()
name|ImageResource
name|deleteNormal
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"diffy26.png"
argument_list|)
DECL|method|gerritAvatar26 ()
name|ImageResource
name|gerritAvatar26
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"downloadIcon.png"
argument_list|)
DECL|method|downloadIcon ()
name|ImageResource
name|downloadIcon
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"draftComments.png"
argument_list|)
DECL|method|draftComments ()
name|ImageResource
name|draftComments
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"editText.png"
argument_list|)
DECL|method|edit ()
name|ImageResource
name|edit
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"editUndo.png"
argument_list|)
DECL|method|editUndo ()
name|ImageResource
name|editUndo
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"gear.png"
argument_list|)
DECL|method|gear ()
name|ImageResource
name|gear
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"goNext.png"
argument_list|)
DECL|method|goNext ()
name|ImageResource
name|goNext
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"goPrev.png"
argument_list|)
DECL|method|goPrev ()
name|ImageResource
name|goPrev
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"goUp.png"
argument_list|)
DECL|method|goUp ()
name|ImageResource
name|goUp
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"greenCheck.png"
argument_list|)
DECL|method|greenCheck ()
name|ImageResource
name|greenCheck
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"info.png"
argument_list|)
DECL|method|info ()
name|ImageResource
name|info
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"listAdd.png"
argument_list|)
DECL|method|listAdd ()
name|ImageResource
name|listAdd
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"mediaFloppy.png"
argument_list|)
DECL|method|save ()
name|ImageResource
name|save
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"merge.png"
argument_list|)
DECL|method|merge ()
name|ImageResource
name|merge
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"queryIcon.png"
argument_list|)
DECL|method|queryIcon ()
name|ImageResource
name|queryIcon
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"readOnly.png"
argument_list|)
DECL|method|readOnly ()
name|ImageResource
name|readOnly
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"redNot.png"
argument_list|)
DECL|method|redNot ()
name|ImageResource
name|redNot
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"sideBySideDiff.png"
argument_list|)
DECL|method|sideBySideDiff ()
name|ImageResource
name|sideBySideDiff
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"starFilled.png"
argument_list|)
DECL|method|starFilled ()
name|ImageResource
name|starFilled
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"starOpen.png"
argument_list|)
DECL|method|starOpen ()
name|ImageResource
name|starOpen
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"undoNormal.png"
argument_list|)
DECL|method|undoNormal ()
name|ImageResource
name|undoNormal
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"unifiedDiff.png"
argument_list|)
DECL|method|unifiedDiff ()
name|ImageResource
name|unifiedDiff
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"warning.png"
argument_list|)
DECL|method|warning ()
name|ImageResource
name|warning
parameter_list|()
function_decl|;
annotation|@
name|Source
argument_list|(
literal|"question.png"
argument_list|)
DECL|method|question ()
name|ImageResource
name|question
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|KeyPressEvent
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
name|Grid
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
name|HasHorizontalAlignment
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
name|HTMLTable
operator|.
name|CellFormatter
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
name|globalkey
operator|.
name|client
operator|.
name|KeyCommand
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
name|globalkey
operator|.
name|client
operator|.
name|KeyCommandSet
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

begin_class
DECL|class|NavLinks
class|class
name|NavLinks
extends|extends
name|Composite
block|{
DECL|enum|Nav
specifier|public
enum|enum
name|Nav
block|{
DECL|enumConstant|PREV
name|PREV
argument_list|(
literal|0
argument_list|,
literal|'['
argument_list|,
name|PatchUtil
operator|.
name|C
operator|.
name|previousFileHelp
argument_list|()
argument_list|,
literal|0
argument_list|)
block|,
DECL|enumConstant|NEXT
name|NEXT
argument_list|(
literal|2
argument_list|,
literal|']'
argument_list|,
name|PatchUtil
operator|.
name|C
operator|.
name|nextFileHelp
argument_list|()
argument_list|,
literal|1
argument_list|)
block|;
DECL|field|col
specifier|public
name|int
name|col
decl_stmt|;
comment|// Table Cell column to display link in
DECL|field|key
specifier|public
name|int
name|key
decl_stmt|;
comment|// key code shortcut to activate link
DECL|field|help
specifier|public
name|String
name|help
decl_stmt|;
comment|// help string for '?' popup
DECL|field|cmd
specifier|public
name|int
name|cmd
decl_stmt|;
comment|// index into cmds array
DECL|method|Nav (int c, int k, String h, int i)
name|Nav
parameter_list|(
name|int
name|c
parameter_list|,
name|int
name|k
parameter_list|,
name|String
name|h
parameter_list|,
name|int
name|i
parameter_list|)
block|{
name|this
operator|.
name|col
operator|=
name|c
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|k
expr_stmt|;
name|this
operator|.
name|help
operator|=
name|h
expr_stmt|;
name|this
operator|.
name|cmd
operator|=
name|i
expr_stmt|;
block|}
block|}
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
DECL|field|keys
specifier|private
specifier|final
name|KeyCommandSet
name|keys
decl_stmt|;
DECL|field|table
specifier|private
specifier|final
name|Grid
name|table
decl_stmt|;
DECL|field|cmds
specifier|private
name|KeyCommand
name|cmds
index|[]
init|=
operator|new
name|KeyCommand
index|[
literal|2
index|]
decl_stmt|;
DECL|method|NavLinks (KeyCommandSet kcs, PatchSet.Id forPatch)
name|NavLinks
parameter_list|(
name|KeyCommandSet
name|kcs
parameter_list|,
name|PatchSet
operator|.
name|Id
name|forPatch
parameter_list|)
block|{
name|patchSetId
operator|=
name|forPatch
expr_stmt|;
name|keys
operator|=
name|kcs
expr_stmt|;
name|table
operator|=
operator|new
name|Grid
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|table
argument_list|)
expr_stmt|;
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|table
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
name|sideBySideScreenLinkTable
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_LEFT
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_CENTER
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_RIGHT
argument_list|)
expr_stmt|;
specifier|final
name|ChangeLink
name|up
init|=
operator|new
name|ChangeLink
argument_list|(
literal|""
argument_list|,
name|patchSetId
argument_list|)
decl_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|up
argument_list|,
name|SafeHtml
operator|.
name|asis
argument_list|(
name|Util
operator|.
name|C
operator|.
name|upToChangeIconLink
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|up
argument_list|)
expr_stmt|;
block|}
DECL|method|display (int patchIndex, PatchScreen.Type type, PatchTable fileList)
name|void
name|display
parameter_list|(
name|int
name|patchIndex
parameter_list|,
name|PatchScreen
operator|.
name|Type
name|type
parameter_list|,
name|PatchTable
name|fileList
parameter_list|)
block|{
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|setupNav
argument_list|(
name|Nav
operator|.
name|PREV
argument_list|,
name|fileList
operator|.
name|getPreviousPatchLink
argument_list|(
name|patchIndex
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|setupNav
argument_list|(
name|Nav
operator|.
name|NEXT
argument_list|,
name|fileList
operator|.
name|getNextPatchLink
argument_list|(
name|patchIndex
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setupNav
argument_list|(
name|Nav
operator|.
name|PREV
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setupNav
argument_list|(
name|Nav
operator|.
name|NEXT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setupNav (final Nav nav, final InlineHyperlink link)
specifier|protected
name|void
name|setupNav
parameter_list|(
specifier|final
name|Nav
name|nav
parameter_list|,
specifier|final
name|InlineHyperlink
name|link
parameter_list|)
block|{
comment|/* setup the cells */
if|if
condition|(
name|link
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
name|nav
operator|.
name|col
argument_list|,
name|link
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|clearCell
argument_list|(
literal|0
argument_list|,
name|nav
operator|.
name|col
argument_list|)
expr_stmt|;
block|}
comment|/* setup the keys */
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cmds
index|[
name|nav
operator|.
name|cmd
index|]
operator|!=
literal|null
condition|)
block|{
name|keys
operator|.
name|remove
argument_list|(
name|cmds
index|[
name|nav
operator|.
name|cmd
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|link
operator|!=
literal|null
condition|)
block|{
name|cmds
index|[
name|nav
operator|.
name|cmd
index|]
operator|=
operator|new
name|KeyCommand
argument_list|(
literal|0
argument_list|,
name|nav
operator|.
name|key
argument_list|,
name|nav
operator|.
name|help
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|link
operator|.
name|go
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
else|else
block|{
name|cmds
index|[
name|nav
operator|.
name|cmd
index|]
operator|=
operator|new
name|UpToChangeCommand
argument_list|(
name|patchSetId
argument_list|,
literal|0
argument_list|,
name|nav
operator|.
name|key
argument_list|)
expr_stmt|;
block|}
name|keys
operator|.
name|add
argument_list|(
name|cmds
index|[
name|nav
operator|.
name|cmd
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|QueryScreen
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
name|PageLinks
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
name|Change
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
name|Project
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
name|Change
operator|.
name|Status
import|;
end_import

begin_comment
comment|/** Link to the open changes of a project. */
end_comment

begin_class
DECL|class|ProjectLink
specifier|public
class|class
name|ProjectLink
extends|extends
name|InlineHyperlink
block|{
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|status
specifier|private
name|Status
name|status
decl_stmt|;
DECL|method|ProjectLink (final Project.NameKey proj, Change.Status stat)
specifier|public
name|ProjectLink
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
name|Change
operator|.
name|Status
name|stat
parameter_list|)
block|{
name|this
argument_list|(
name|proj
operator|.
name|get
argument_list|()
argument_list|,
name|proj
argument_list|,
name|stat
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectLink (final String text, final Project.NameKey proj, Change.Status stat)
specifier|public
name|ProjectLink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
name|Change
operator|.
name|Status
name|stat
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|PageLinks
operator|.
name|toChangeQuery
argument_list|(
name|PageLinks
operator|.
name|projectQuery
argument_list|(
name|proj
argument_list|,
name|stat
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|status
operator|=
name|stat
expr_stmt|;
name|project
operator|=
name|proj
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|getTargetHistoryToken
argument_list|()
argument_list|,
name|createScreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createScreen ()
specifier|private
name|Screen
name|createScreen
parameter_list|()
block|{
return|return
name|QueryScreen
operator|.
name|forQuery
argument_list|(
name|PageLinks
operator|.
name|projectQuery
argument_list|(
name|project
argument_list|,
name|status
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


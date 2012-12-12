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
name|projects
operator|.
name|ProjectInfo
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
name|projects
operator|.
name|ProjectMap
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
name|InlineHTML
import|;
end_import

begin_class
DECL|class|HighlightingProjectsTable
specifier|public
class|class
name|HighlightingProjectsTable
extends|extends
name|ProjectsTable
block|{
DECL|field|toHighlight
specifier|private
name|String
name|toHighlight
decl_stmt|;
DECL|method|display (final ProjectMap projects, final String toHighlight)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|ProjectMap
name|projects
parameter_list|,
specifier|final
name|String
name|toHighlight
parameter_list|)
block|{
name|this
operator|.
name|toHighlight
operator|=
name|toHighlight
expr_stmt|;
name|super
operator|.
name|display
argument_list|(
name|projects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|populate (final int row, final ProjectInfo k)
specifier|protected
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|ProjectInfo
name|k
parameter_list|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
operator|new
name|InlineHTML
argument_list|(
name|Util
operator|.
name|highlight
argument_list|(
name|k
operator|.
name|name
argument_list|()
argument_list|,
name|toHighlight
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|k
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


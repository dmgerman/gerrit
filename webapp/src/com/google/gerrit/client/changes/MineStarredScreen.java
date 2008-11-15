begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|Screen
import|;
end_import

begin_class
DECL|class|MineStarredScreen
specifier|public
class|class
name|MineStarredScreen
extends|extends
name|Screen
block|{
DECL|field|table
specifier|private
name|ChangeTable
name|table
decl_stmt|;
DECL|field|starred
specifier|private
name|ChangeTable
operator|.
name|Section
name|starred
decl_stmt|;
DECL|method|MineStarredScreen ()
specifier|public
name|MineStarredScreen
parameter_list|()
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|starredHeading
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|=
operator|new
name|ChangeTable
argument_list|()
expr_stmt|;
name|starred
operator|=
operator|new
name|ChangeTable
operator|.
name|Section
argument_list|()
expr_stmt|;
name|table
operator|.
name|addSection
argument_list|(
name|starred
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


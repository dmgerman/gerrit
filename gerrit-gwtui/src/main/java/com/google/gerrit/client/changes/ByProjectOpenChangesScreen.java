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
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_class
DECL|class|ByProjectOpenChangesScreen
specifier|public
class|class
name|ByProjectOpenChangesScreen
extends|extends
name|PagedSingleListScreen
block|{
DECL|field|projectKey
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
decl_stmt|;
DECL|method|ByProjectOpenChangesScreen (final Project.NameKey proj, final String positionToken)
specifier|public
name|ByProjectOpenChangesScreen
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
specifier|final
name|String
name|positionToken
parameter_list|)
block|{
name|super
argument_list|(
literal|"project,open,"
operator|+
name|proj
operator|.
name|toString
argument_list|()
argument_list|,
name|positionToken
argument_list|)
expr_stmt|;
name|projectKey
operator|=
name|proj
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|setPageTitle
argument_list|(
name|Util
operator|.
name|M
operator|.
name|changesOpenInProject
argument_list|(
name|projectKey
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadPrev ()
specifier|protected
name|void
name|loadPrev
parameter_list|()
block|{
name|Util
operator|.
name|LIST_SVC
operator|.
name|byProjectOpenPrev
argument_list|(
name|projectKey
argument_list|,
name|pos
argument_list|,
name|pageSize
argument_list|,
name|loadCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadNext ()
specifier|protected
name|void
name|loadNext
parameter_list|()
block|{
name|Util
operator|.
name|LIST_SVC
operator|.
name|byProjectOpenNext
argument_list|(
name|projectKey
argument_list|,
name|pos
argument_list|,
name|pageSize
argument_list|,
name|loadCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


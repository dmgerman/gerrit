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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Dispatcher
operator|.
name|toProjectAdmin
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
name|client
operator|.
name|ui
operator|.
name|MenuScreen
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

begin_class
DECL|class|ProjectScreen
specifier|public
specifier|abstract
class|class
name|ProjectScreen
extends|extends
name|MenuScreen
block|{
DECL|field|INFO
specifier|public
specifier|static
specifier|final
name|String
name|INFO
init|=
literal|"info"
decl_stmt|;
DECL|field|BRANCH
specifier|public
specifier|static
specifier|final
name|String
name|BRANCH
init|=
literal|"branches"
decl_stmt|;
DECL|field|ACCESS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS
init|=
literal|"access"
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|name
decl_stmt|;
DECL|method|ProjectScreen (final Project.NameKey toShow)
specifier|public
name|ProjectScreen
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|toShow
parameter_list|)
block|{
name|name
operator|=
name|toShow
expr_stmt|;
specifier|final
name|boolean
name|isWild
init|=
name|toShow
operator|.
name|equals
argument_list|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getWildProject
argument_list|()
argument_list|)
decl_stmt|;
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectAdminTabGeneral
argument_list|()
argument_list|,
name|toProjectAdmin
argument_list|(
name|name
argument_list|,
name|INFO
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isWild
condition|)
block|{
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectAdminTabBranches
argument_list|()
argument_list|,
name|toProjectAdmin
argument_list|(
name|name
argument_list|,
name|BRANCH
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|link
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectAdminTabAccess
argument_list|()
argument_list|,
name|toProjectAdmin
argument_list|(
name|name
argument_list|,
name|ACCESS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getProjectKey ()
specifier|protected
name|Project
operator|.
name|NameKey
name|getProjectKey
parameter_list|()
block|{
return|return
name|name
return|;
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
name|project
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


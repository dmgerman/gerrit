begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.api.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|config
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|acceptance
operator|.
name|LightweightPluginDaemonTest
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
name|acceptance
operator|.
name|TestPlugin
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|extensions
operator|.
name|webui
operator|.
name|TopMenu
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
name|extensions
operator|.
name|webui
operator|.
name|TopMenu
operator|.
name|MenuEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|AbstractModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|TestPlugin
argument_list|(
name|name
operator|=
literal|"test-topmenus"
argument_list|,
name|sysModule
operator|=
literal|"com.google.gerrit.acceptance.api.config.TopMenusIT$Module"
argument_list|)
DECL|class|TopMenusIT
specifier|public
class|class
name|TopMenusIT
extends|extends
name|LightweightPluginDaemonTest
block|{
DECL|field|TEST_MENU_ENTRY
specifier|static
specifier|final
name|TopMenu
operator|.
name|MenuEntry
name|TEST_MENU_ENTRY
init|=
operator|new
name|TopMenu
operator|.
name|MenuEntry
argument_list|(
literal|"MyMenu"
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|TopMenu
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|TopMenuTest
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TopMenuTest
specifier|public
specifier|static
class|class
name|TopMenuTest
implements|implements
name|TopMenu
block|{
annotation|@
name|Override
DECL|method|getEntries ()
specifier|public
name|List
argument_list|<
name|MenuEntry
argument_list|>
name|getEntries
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_MENU_ENTRY
argument_list|)
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|topMenuShouldReturnOneEntry ()
specifier|public
name|void
name|topMenuShouldReturnOneEntry
parameter_list|()
throws|throws
name|RestApiException
block|{
name|List
argument_list|<
name|MenuEntry
argument_list|>
name|topMenuItems
init|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|topMenus
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|topMenuItems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|TEST_MENU_ENTRY
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


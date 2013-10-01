begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|TopMenu
specifier|public
interface|interface
name|TopMenu
block|{
DECL|class|MenuEntry
specifier|public
class|class
name|MenuEntry
block|{
DECL|field|name
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|items
specifier|public
specifier|final
name|List
argument_list|<
name|MenuItem
argument_list|>
name|items
decl_stmt|;
DECL|method|MenuEntry (GerritTopMenu gerritMenu, List<MenuItem> items)
specifier|public
name|MenuEntry
parameter_list|(
name|GerritTopMenu
name|gerritMenu
parameter_list|,
name|List
argument_list|<
name|MenuItem
argument_list|>
name|items
parameter_list|)
block|{
name|this
argument_list|(
name|gerritMenu
operator|.
name|menuName
argument_list|,
name|items
argument_list|)
expr_stmt|;
block|}
DECL|method|MenuEntry (String name, List<MenuItem> items)
specifier|public
name|MenuEntry
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|MenuItem
argument_list|>
name|items
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|items
operator|=
name|items
expr_stmt|;
block|}
block|}
DECL|class|MenuItem
specifier|public
class|class
name|MenuItem
block|{
DECL|field|url
specifier|public
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|name
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|target
specifier|public
specifier|final
name|String
name|target
decl_stmt|;
DECL|method|MenuItem (String name, String url)
specifier|public
name|MenuItem
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|url
argument_list|,
literal|"_blank"
argument_list|)
expr_stmt|;
block|}
DECL|method|MenuItem (String name, String url, String target)
specifier|public
name|MenuItem
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
block|}
DECL|method|getEntries ()
name|List
argument_list|<
name|MenuEntry
argument_list|>
name|getEntries
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


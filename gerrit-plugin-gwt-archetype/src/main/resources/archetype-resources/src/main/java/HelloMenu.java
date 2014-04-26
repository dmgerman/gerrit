begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

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
name|PluginName
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_class
DECL|class|HelloMenu
specifier|public
class|class
name|HelloMenu
implements|implements
name|TopMenu
block|{
DECL|field|menuEntries
specifier|private
specifier|final
name|List
argument_list|<
name|MenuEntry
argument_list|>
name|menuEntries
decl_stmt|;
annotation|@
name|Inject
DECL|method|HelloMenu (@luginName String pluginName)
specifier|public
name|HelloMenu
parameter_list|(
annotation|@
name|PluginName
name|String
name|pluginName
parameter_list|)
block|{
name|menuEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|menuEntries
operator|.
name|add
argument_list|(
operator|new
name|MenuEntry
argument_list|(
literal|"Hello"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|MenuItem
argument_list|(
literal|"Hello Screen"
argument_list|,
literal|"#/x/"
operator|+
name|pluginName
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|menuEntries
return|;
block|}
block|}
end_class

end_unit


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
name|api
operator|.
name|ExtensionScreen
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
name|plugins
operator|.
name|PluginInfo
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
name|plugins
operator|.
name|PluginMap
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
name|rpc
operator|.
name|Natives
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
name|rpc
operator|.
name|ScreenLoadCallback
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
name|FancyFlexTable
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Anchor
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
name|FlexTable
operator|.
name|FlexCellFormatter
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
name|FlowPanel
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
name|ImageResourceRenderer
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
name|Panel
import|;
end_import

begin_class
DECL|class|PluginListScreen
specifier|public
class|class
name|PluginListScreen
extends|extends
name|PluginScreen
block|{
DECL|field|pluginPanel
specifier|private
name|Panel
name|pluginPanel
decl_stmt|;
DECL|field|pluginTable
specifier|private
name|PluginTable
name|pluginTable
decl_stmt|;
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
name|initPluginList
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|PluginMap
operator|.
name|all
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|PluginMap
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
specifier|final
name|PluginMap
name|result
parameter_list|)
block|{
name|pluginTable
operator|.
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|initPluginList ()
specifier|private
name|void
name|initPluginList
parameter_list|()
block|{
name|pluginTable
operator|=
operator|new
name|PluginTable
argument_list|()
expr_stmt|;
name|pluginTable
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|pluginsTable
argument_list|()
argument_list|)
expr_stmt|;
name|pluginPanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|pluginPanel
operator|.
name|setWidth
argument_list|(
literal|"500px"
argument_list|)
expr_stmt|;
name|pluginPanel
operator|.
name|add
argument_list|(
name|pluginTable
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|pluginPanel
argument_list|)
expr_stmt|;
block|}
DECL|class|PluginTable
specifier|private
specifier|static
class|class
name|PluginTable
extends|extends
name|FancyFlexTable
argument_list|<
name|PluginInfo
argument_list|>
block|{
DECL|method|PluginTable ()
name|PluginTable
parameter_list|()
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnPluginName
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnPluginSettings
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnPluginVersion
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnPluginStatus
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final PluginMap plugins)
name|void
name|display
parameter_list|(
specifier|final
name|PluginMap
name|plugins
parameter_list|)
block|{
while|while
condition|(
literal|1
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|PluginInfo
name|p
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|plugins
operator|.
name|values
argument_list|()
argument_list|)
control|)
block|{
specifier|final
name|int
name|row
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
name|table
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|applyDataRowStyle
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|row
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populate (final int row, final PluginInfo plugin)
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|PluginInfo
name|plugin
parameter_list|)
block|{
if|if
condition|(
name|plugin
operator|.
name|disabled
argument_list|()
operator|||
name|plugin
operator|.
name|indexUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|plugin
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
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
name|Anchor
argument_list|(
name|plugin
operator|.
name|name
argument_list|()
argument_list|,
name|Gerrit
operator|.
name|selfRedirect
argument_list|(
name|plugin
operator|.
name|indexUrl
argument_list|()
argument_list|)
argument_list|,
literal|"_blank"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|new
name|ExtensionScreen
argument_list|(
name|plugin
operator|.
name|name
argument_list|()
operator|+
literal|"/settings"
argument_list|)
operator|.
name|isFound
argument_list|()
condition|)
block|{
name|InlineHyperlink
name|adminScreenLink
init|=
operator|new
name|InlineHyperlink
argument_list|()
decl_stmt|;
name|adminScreenLink
operator|.
name|setHTML
argument_list|(
operator|new
name|ImageResourceRenderer
argument_list|()
operator|.
name|render
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|gear
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|adminScreenLink
operator|.
name|setTargetHistoryToken
argument_list|(
literal|"/x/"
operator|+
name|plugin
operator|.
name|name
argument_list|()
operator|+
literal|"/settings"
argument_list|)
expr_stmt|;
name|adminScreenLink
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|pluginSettingsToolTip
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|adminScreenLink
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
name|plugin
operator|.
name|version
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|plugin
operator|.
name|disabled
argument_list|()
condition|?
name|Util
operator|.
name|C
operator|.
name|pluginDisabled
argument_list|()
else|:
name|Util
operator|.
name|C
operator|.
name|pluginEnabled
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


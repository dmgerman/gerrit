begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.registration
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
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
name|common
operator|.
name|Nullable
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
name|Provider
import|;
end_import

begin_comment
comment|/**  * An extension that is provided by a plugin.  *  *<p>Contains the name of the plugin that provides the extension, the extension point  * implementation and optionally the export name under which the extension was exported.  *  *<p>An export name is only available if this extension is an entry in a {@link DynamicMap}.  *  * @param<T> Type of extension point that this extension implements  */
end_comment

begin_class
DECL|class|Extension
specifier|public
class|class
name|Extension
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|exportName
specifier|private
specifier|final
annotation|@
name|Nullable
name|String
name|exportName
decl_stmt|;
DECL|field|provider
specifier|private
specifier|final
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
decl_stmt|;
DECL|method|Extension (String pluginName, Provider<T> provider)
specifier|public
name|Extension
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|)
block|{
name|this
argument_list|(
name|pluginName
argument_list|,
literal|null
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
DECL|method|Extension (String pluginName, @Nullable String exportName, Provider<T> provider)
specifier|protected
name|Extension
parameter_list|(
name|String
name|pluginName
parameter_list|,
annotation|@
name|Nullable
name|String
name|exportName
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|exportName
operator|=
name|exportName
expr_stmt|;
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
DECL|method|getPluginName ()
specifier|public
name|String
name|getPluginName
parameter_list|()
block|{
return|return
name|pluginName
return|;
block|}
annotation|@
name|Nullable
DECL|method|getExportName ()
specifier|public
name|String
name|getExportName
parameter_list|()
block|{
return|return
name|exportName
return|;
block|}
DECL|method|getProvider ()
specifier|public
name|Provider
argument_list|<
name|T
argument_list|>
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|provider
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit


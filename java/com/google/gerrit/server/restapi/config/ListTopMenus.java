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
DECL|package|com.google.gerrit.server.restapi.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|config
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
name|restapi
operator|.
name|Response
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
name|RestReadView
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigResource
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
name|server
operator|.
name|plugincontext
operator|.
name|PluginSetContext
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ListTopMenus
class|class
name|ListTopMenus
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|extensions
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|TopMenu
argument_list|>
name|extensions
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListTopMenus (PluginSetContext<TopMenu> extensions)
name|ListTopMenus
parameter_list|(
name|PluginSetContext
argument_list|<
name|TopMenu
argument_list|>
name|extensions
parameter_list|)
block|{
name|this
operator|.
name|extensions
operator|=
name|extensions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource resource)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|MenuEntry
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|ConfigResource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|TopMenu
operator|.
name|MenuEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|extensions
operator|.
name|runEach
argument_list|(
name|extension
lambda|->
name|entries
operator|.
name|addAll
argument_list|(
name|extension
operator|.
name|getEntries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|entries
argument_list|)
operator|.
name|caching
argument_list|(
name|ConfigResource
operator|.
name|DEFAULT_CACHE_CONTROL
argument_list|)
return|;
block|}
block|}
end_class

end_unit


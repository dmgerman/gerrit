begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|plugins
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
name|common
operator|.
name|InstallPluginInput
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
name|common
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
name|extensions
operator|.
name|restapi
operator|.
name|NotImplementedException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_interface
DECL|interface|Plugins
specifier|public
interface|interface
name|Plugins
block|{
DECL|method|list ()
name|ListRequest
name|list
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|name (String name)
name|PluginApi
name|name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|install (String name, InstallPluginInput input)
name|PluginApi
name|install
parameter_list|(
name|String
name|name
parameter_list|,
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|class|ListRequest
specifier|abstract
class|class
name|ListRequest
block|{
DECL|field|all
specifier|private
name|boolean
name|all
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|PluginInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|map
init|=
name|getAsMap
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PluginInfo
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getAsMap ()
specifier|public
specifier|abstract
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|getAsMap
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|all ()
specifier|public
name|ListRequest
name|all
parameter_list|()
block|{
name|this
operator|.
name|all
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getAll ()
specifier|public
name|boolean
name|getAll
parameter_list|()
block|{
return|return
name|all
return|;
block|}
DECL|method|limit (int limit)
specifier|public
name|ListRequest
name|limit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|start (int start)
specifier|public
name|ListRequest
name|start
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getStart ()
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|Plugins
block|{
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|ListRequest
name|list
parameter_list|()
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|name (String name)
specifier|public
name|PluginApi
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|install (String name, InstallPluginInput input)
specifier|public
name|PluginApi
name|install
parameter_list|(
name|String
name|name
parameter_list|,
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit


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
DECL|package|com.google.gerrit.server.restapi.change
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
name|change
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Streams
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
name|client
operator|.
name|ListChangesOption
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
name|client
operator|.
name|ListOption
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
name|ChangeInfo
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
name|PluginDefinedInfo
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
name|server
operator|.
name|DynamicOptions
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
name|DynamicOptions
operator|.
name|DynamicBean
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
name|change
operator|.
name|ChangeAttributeFactory
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
name|change
operator|.
name|ChangeJson
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
name|change
operator|.
name|ChangeResource
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
name|change
operator|.
name|PluginDefinedAttributesFactories
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
name|change
operator|.
name|RevisionResource
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|GetChange
specifier|public
class|class
name|GetChange
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
implements|,
name|DynamicOptions
operator|.
name|BeanReceiver
implements|,
name|DynamicOptions
operator|.
name|BeanProvider
block|{
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|json
decl_stmt|;
DECL|field|attrFactories
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ChangeAttributeFactory
argument_list|>
name|attrFactories
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dynamicBeans
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DynamicBean
argument_list|>
name|dynamicBeans
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|usage
operator|=
literal|"Output options"
argument_list|)
DECL|method|addOption (ListChangesOption o)
specifier|public
name|void
name|addOption
parameter_list|(
name|ListChangesOption
name|o
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-O"
argument_list|,
name|usage
operator|=
literal|"Output option flags, in hex"
argument_list|)
DECL|method|setOptionFlagsHex (String hex)
name|void
name|setOptionFlagsHex
parameter_list|(
name|String
name|hex
parameter_list|)
block|{
name|options
operator|.
name|addAll
argument_list|(
name|ListOption
operator|.
name|fromBits
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|hex
argument_list|,
literal|16
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|GetChange (ChangeJson.Factory json, DynamicSet<ChangeAttributeFactory> attrFactories)
name|GetChange
parameter_list|(
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|,
name|DynamicSet
argument_list|<
name|ChangeAttributeFactory
argument_list|>
name|attrFactories
parameter_list|)
block|{
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|attrFactories
operator|=
name|attrFactories
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDynamicBean (String plugin, DynamicOptions.DynamicBean dynamicBean)
specifier|public
name|void
name|setDynamicBean
parameter_list|(
name|String
name|plugin
parameter_list|,
name|DynamicOptions
operator|.
name|DynamicBean
name|dynamicBean
parameter_list|)
block|{
name|dynamicBeans
operator|.
name|put
argument_list|(
name|plugin
argument_list|,
name|dynamicBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDynamicBean (String plugin)
specifier|public
name|DynamicBean
name|getDynamicBean
parameter_list|(
name|String
name|plugin
parameter_list|)
block|{
return|return
name|dynamicBeans
operator|.
name|get
argument_list|(
name|plugin
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
return|return
name|Response
operator|.
name|withMustRevalidate
argument_list|(
name|newChangeJson
argument_list|()
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
DECL|method|apply (RevisionResource rsrc)
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
block|{
return|return
name|Response
operator|.
name|withMustRevalidate
argument_list|(
name|newChangeJson
argument_list|()
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newChangeJson ()
specifier|private
name|ChangeJson
name|newChangeJson
parameter_list|()
block|{
return|return
name|json
operator|.
name|create
argument_list|(
name|options
argument_list|,
name|this
operator|::
name|buildPluginInfo
argument_list|)
return|;
block|}
DECL|method|buildPluginInfo (ChangeData cd)
specifier|private
name|ImmutableList
argument_list|<
name|PluginDefinedInfo
argument_list|>
name|buildPluginInfo
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|PluginDefinedAttributesFactories
operator|.
name|createAll
argument_list|(
name|cd
argument_list|,
name|this
argument_list|,
name|Streams
operator|.
name|stream
argument_list|(
name|attrFactories
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


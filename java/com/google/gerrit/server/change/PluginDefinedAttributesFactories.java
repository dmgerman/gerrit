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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MINUTES
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
name|flogger
operator|.
name|FluentLogger
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
name|Extension
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
name|BeanProvider
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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/** Static helpers for use by {@link PluginDefinedAttributesFactory} implementations. */
end_comment

begin_class
DECL|class|PluginDefinedAttributesFactories
specifier|public
class|class
name|PluginDefinedAttributesFactories
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
annotation|@
name|Nullable
DECL|method|createAll ( ChangeData cd, BeanProvider beanProvider, Stream<Extension<ChangeAttributeFactory>> attrFactories)
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|PluginDefinedInfo
argument_list|>
name|createAll
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|BeanProvider
name|beanProvider
parameter_list|,
name|Stream
argument_list|<
name|Extension
argument_list|<
name|ChangeAttributeFactory
argument_list|>
argument_list|>
name|attrFactories
parameter_list|)
block|{
name|ImmutableList
argument_list|<
name|PluginDefinedInfo
argument_list|>
name|result
init|=
name|attrFactories
operator|.
name|map
argument_list|(
name|e
lambda|->
name|tryCreate
argument_list|(
name|cd
argument_list|,
name|beanProvider
argument_list|,
name|e
operator|.
name|getPluginName
argument_list|()
argument_list|,
name|e
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|?
name|result
else|:
literal|null
return|;
block|}
annotation|@
name|Nullable
DECL|method|tryCreate ( ChangeData cd, BeanProvider beanProvider, String plugin, ChangeAttributeFactory attrFactory)
specifier|private
specifier|static
name|PluginDefinedInfo
name|tryCreate
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|BeanProvider
name|beanProvider
parameter_list|,
name|String
name|plugin
parameter_list|,
name|ChangeAttributeFactory
name|attrFactory
parameter_list|)
block|{
name|PluginDefinedInfo
name|pdi
init|=
literal|null
decl_stmt|;
try|try
block|{
name|pdi
operator|=
name|attrFactory
operator|.
name|create
argument_list|(
name|cd
argument_list|,
name|beanProvider
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|atMostEvery
argument_list|(
literal|1
argument_list|,
name|MINUTES
argument_list|)
operator|.
name|withCause
argument_list|(
name|ex
argument_list|)
operator|.
name|log
argument_list|(
literal|"error populating attribute on change %s from plugin %s"
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pdi
operator|!=
literal|null
condition|)
block|{
name|pdi
operator|.
name|name
operator|=
name|plugin
expr_stmt|;
block|}
return|return
name|pdi
return|;
block|}
DECL|method|PluginDefinedAttributesFactories ()
specifier|private
name|PluginDefinedAttributesFactories
parameter_list|()
block|{}
block|}
end_class

end_unit


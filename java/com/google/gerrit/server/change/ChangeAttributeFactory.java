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

begin_comment
comment|/**  * Interface for plugins to provide additional fields in {@link  * com.google.gerrit.extensions.common.ChangeInfo ChangeInfo}.  *  *<p>Register a {@code ChangeAttributeFactory} in a plugin {@code Module} like this:  *  *<pre>  * DynamicSet.bind(binder(), ChangeAttributeFactory.class).to(YourClass.class);  *</pre>  *  *<p>See the<a  * href="https://gerrit-review.googlesource.com/Documentation/dev-plugins.html#query_attributes">plugin  * developer documentation for more details and examples.  */
end_comment

begin_interface
DECL|interface|ChangeAttributeFactory
specifier|public
interface|interface
name|ChangeAttributeFactory
block|{
comment|/**    * Create a plugin-provided info field.    *    *<p>Typically, implementations will subclass {@code PluginDefinedInfo} to add additional fields.    *    * @param cd change.    * @param beanProvider provider of {@code DynamicBean}s, which may be used for reading options.    * @param plugin plugin name.    * @return the plugin's special change info.    */
DECL|method|create (ChangeData cd, BeanProvider beanProvider, String plugin)
name|PluginDefinedInfo
name|create
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|BeanProvider
name|beanProvider
parameter_list|,
name|String
name|plugin
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


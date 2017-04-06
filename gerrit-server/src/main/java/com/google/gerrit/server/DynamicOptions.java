begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|registration
operator|.
name|DynamicMap
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
name|util
operator|.
name|cli
operator|.
name|CmdLineParser
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

begin_class
DECL|class|DynamicOptions
specifier|public
class|class
name|DynamicOptions
block|{
comment|/**    * To provide additional options, bind a DynamicBean. For example:    *    *<pre>    *   bind(com.google.gerrit.server.DynamicOptions.DynamicBean.class)    *       .annotatedWith(Exports.named(com.google.gerrit.sshd.commands.Query.class))    *       .to(MyOptions.class);    *</pre>    *    * To define the additional options, implement this interface. For example:    *    *<pre>    *   public class MyOptions implements DynamicOptions.DynamicBean {    *     {@literal @}Option(name = "--verbose", aliases = {"-v"}    *             usage = "Make the operation more talkative")    *     public boolean verbose;    *   }    *</pre>    *    * The option will be prefixed by the plugin name. In the example above, if the plugin name was    * my-plugin, then the --verbose option as used by the caller would be --my-plugin--verbose.    */
DECL|interface|DynamicBean
specifier|public
interface|interface
name|DynamicBean
block|{}
comment|/**    * The entity which provided additional options may need a way to receive a reference to the    * DynamicBean it provided. To do so, the existing class should implement BeanReceiver (a setter)    * and then provide some way for the plugin to request its DynamicBean (a getter.) For example:    *    *<pre>    *   public class Query extends SshCommand implements DynamicOptions.BeanReceiver {    *       public void setDynamicBean(String plugin, DynamicOptions.DynamicBean dynamicBean) {    *         dynamicBeans.put(plugin, dynamicBean);    *       }    *    *       public DynamicOptions.DynamicBean getDynamicBean(String plugin) {    *         return dynamicBeans.get(plugin);    *       }    *   ...    *   }    * }    *</pre>    */
DECL|interface|BeanReceiver
specifier|public
interface|interface
name|BeanReceiver
block|{
DECL|method|setDynamicBean (String plugin, DynamicBean dynamicBean)
name|void
name|setDynamicBean
parameter_list|(
name|String
name|plugin
parameter_list|,
name|DynamicBean
name|dynamicBean
parameter_list|)
function_decl|;
block|}
comment|/**    * To include options from DynamicBeans, setup a DynamicMap and call this parse method. For    * example:    *    *<pre>    *   DynamicMap.mapOf(binder(), DynamicOptions.DynamicBean.class);    *    * ...    *    *   protected void parseCommandLine(Object options) throws UnloggedFailure {    *     final CmdLineParser clp = newCmdLineParser(options);    *     DynamicOptions.parse(dynamicBeans, clp, options);    *     ...    *  }    *</pre>    */
DECL|method|parse (DynamicMap<DynamicBean> dynamicBeans, CmdLineParser clp, Object bean)
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|DynamicMap
argument_list|<
name|DynamicBean
argument_list|>
name|dynamicBeans
parameter_list|,
name|CmdLineParser
name|clp
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
for|for
control|(
name|String
name|plugin
range|:
name|dynamicBeans
operator|.
name|plugins
argument_list|()
control|)
block|{
name|Provider
argument_list|<
name|DynamicBean
argument_list|>
name|provider
init|=
name|dynamicBeans
operator|.
name|byPlugin
argument_list|(
name|plugin
argument_list|)
operator|.
name|get
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|DynamicBean
name|dynamicBean
init|=
name|provider
operator|.
name|get
argument_list|()
decl_stmt|;
name|clp
operator|.
name|parseWithPrefix
argument_list|(
name|plugin
argument_list|,
name|dynamicBean
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|BeanReceiver
condition|)
block|{
operator|(
operator|(
name|BeanReceiver
operator|)
name|bean
operator|)
operator|.
name|setDynamicBean
argument_list|(
name|plugin
argument_list|,
name|dynamicBean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit


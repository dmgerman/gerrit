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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|CapabilityScope
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
name|annotations
operator|.
name|RequiresCapability
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
name|AuthException
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
name|CurrentUser
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
name|account
operator|.
name|CapabilityControl
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_class
DECL|class|CapabilityUtils
specifier|public
class|class
name|CapabilityUtils
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CapabilityUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|checkRequiresCapability (Provider<CurrentUser> userProvider, String pluginName, Class<?> clazz)
specifier|public
specifier|static
name|void
name|checkRequiresCapability
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|String
name|pluginName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|AuthException
block|{
name|RequiresCapability
name|rc
init|=
name|getClassAnnotation
argument_list|(
name|clazz
argument_list|,
name|RequiresCapability
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|CurrentUser
name|user
init|=
name|userProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|CapabilityControl
name|ctl
init|=
name|user
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctl
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|capability
init|=
name|rc
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|pluginName
operator|!=
literal|null
operator|&&
operator|!
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|pluginName
argument_list|)
operator|&&
operator|(
name|rc
operator|.
name|scope
argument_list|()
operator|==
name|CapabilityScope
operator|.
name|PLUGIN
operator|||
name|rc
operator|.
name|scope
argument_list|()
operator|==
name|CapabilityScope
operator|.
name|CONTEXT
operator|)
condition|)
block|{
name|capability
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s-%s"
argument_list|,
name|pluginName
argument_list|,
name|rc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rc
operator|.
name|scope
argument_list|()
operator|==
name|CapabilityScope
operator|.
name|PLUGIN
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Class %s uses @%s(scope=%s), but is not within a plugin"
argument_list|,
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|RequiresCapability
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|CapabilityScope
operator|.
name|PLUGIN
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"cannot check capability"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|ctl
operator|.
name|canPerform
argument_list|(
name|capability
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Capability %s is required to access this resource"
argument_list|,
name|capability
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Find an instance of the specified annotation, walking up the inheritance    * tree if necessary.    *    * @param<T> Annotation type to search for    * @param clazz root class to search, may be null    * @param annotationClass class object of Annotation subclass to search for    * @return the requested annotation or null if none    */
DECL|method|getClassAnnotation (Class<?> clazz, Class<T> annotationClass)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getClassAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|annotationClass
parameter_list|)
block|{
for|for
control|(
init|;
name|clazz
operator|!=
literal|null
condition|;
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|T
name|t
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|annotationClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit


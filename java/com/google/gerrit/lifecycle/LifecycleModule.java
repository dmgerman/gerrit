begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.lifecycle
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lifecycle
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
name|config
operator|.
name|FactoryModule
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
name|events
operator|.
name|LifecycleListener
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|binder
operator|.
name|LinkedBindingBuilder
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
name|internal
operator|.
name|UniqueAnnotations
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

begin_comment
comment|/** Module to support registering a unique LifecyleListener. */
end_comment

begin_class
DECL|class|LifecycleModule
specifier|public
specifier|abstract
class|class
name|LifecycleModule
extends|extends
name|FactoryModule
block|{
comment|/**    * @return a unique listener binding.    *<p>To create a listener binding use:    *<pre>    * listener().to(MyListener.class);    *</pre>    *     where {@code MyListener} is a {@link Singleton} implementing the {@link LifecycleListener}    *     interface.    */
DECL|method|listener ()
specifier|protected
name|LinkedBindingBuilder
argument_list|<
name|LifecycleListener
argument_list|>
name|listener
parameter_list|()
block|{
specifier|final
name|Annotation
name|id
init|=
name|UniqueAnnotations
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|bind
argument_list|(
name|LifecycleListener
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|inject
operator|.
name|AbstractModule
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
name|assistedinject
operator|.
name|FactoryModuleBuilder
import|;
end_import

begin_class
DECL|class|FactoryModule
specifier|public
specifier|abstract
class|class
name|FactoryModule
extends|extends
name|AbstractModule
block|{
comment|/**    * Register an assisted injection factory.    *<p>    * This function provides an automatic way to define a factory that creates a    * concrete type through assisted injection. For example to configure the    * following assisted injection case:    *    *<pre>    * public class Foo {    *   public interface Factory {    *     Foo create(int a);    *   }    *&#064;Inject    *   Foo(Logger log, @Assisted int a) {...}    * }    *</pre>    *    * Just pass {@code Foo.Factory.class} to this method. The factory will be    * generated to return its one return type as declared in the creation method.    *    * @param factory interface which specifies the bean factory method.    */
DECL|method|factory (final Class<?> factory)
specifier|protected
name|void
name|factory
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|factory
parameter_list|)
block|{
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


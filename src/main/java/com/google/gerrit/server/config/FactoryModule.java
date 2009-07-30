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
name|FactoryProvider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
comment|/**    * Register an assisted injection factory.    *<p>    * This function provides an automatic way to define a factory that creates a    * concrete type through assited injection. For example to configure the    * following assisted injection case:    *    *<pre>    * public class Foo {    *   public interface Factory {    *     Foo create(int a);    *   }    *&#064;Inject    *   Foo(Logger log, @Assisted int a) {...}    * }    *</pre>    *    * Just pass {@code Foo.Factory.class} to this method. The factory will be    * generated to return its one return type as declared in the creation method.    *    * @param<F>    * @param factory    */
DECL|method|factory (final Class<F> factory)
specifier|protected
parameter_list|<
name|F
parameter_list|>
name|void
name|factory
parameter_list|(
specifier|final
name|Class
argument_list|<
name|F
argument_list|>
name|factory
parameter_list|)
block|{
specifier|final
name|Method
index|[]
name|methods
init|=
name|factory
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|methods
operator|.
name|length
condition|)
block|{
case|case
literal|1
case|:
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
name|methods
index|[
literal|0
index|]
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|isAbstract
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|addError
argument_list|(
literal|"Factory "
operator|+
name|factory
operator|.
name|getName
argument_list|()
operator|+
literal|" returns abstract result."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bind
argument_list|(
name|factory
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|factory
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|0
case|:
name|addError
argument_list|(
literal|"Factory "
operator|+
name|factory
operator|.
name|getName
argument_list|()
operator|+
literal|" has no create method."
argument_list|)
expr_stmt|;
break|break;
default|default:
name|addError
argument_list|(
literal|"Factory "
operator|+
name|factory
operator|.
name|getName
argument_list|()
operator|+
literal|" has more than one create method."
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
DECL|method|isAbstract (final Class<?> result)
specifier|private
specifier|static
name|boolean
name|isAbstract
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|result
parameter_list|)
block|{
return|return
name|result
operator|.
name|isInterface
argument_list|()
operator|||
operator|(
name|result
operator|.
name|getModifiers
argument_list|()
operator|&
name|Modifier
operator|.
name|ABSTRACT
operator|)
operator|==
name|Modifier
operator|.
name|ABSTRACT
return|;
block|}
block|}
end_class

end_unit


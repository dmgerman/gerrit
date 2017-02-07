begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|FIELD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|METHOD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
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
name|base
operator|.
name|MoreObjects
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
name|Iterables
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
name|Lists
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
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
name|Target
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
name|Field
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
name|InvocationTargetException
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Runner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|BlockJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Suite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|FrameworkMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|InitializationError
import|;
end_import

begin_comment
comment|/**  * Suite to run tests with different {@code gerrit.config} values.  *  *<p>For each {@link Config} method in the class and base classes, a new group of tests is created  * with the {@link Parameter} field set to the config.  *  *<pre>  * {@literal @}RunWith(ConfigSuite.class)  * public abstract class MyAbstractTest {  *   {@literal @}ConfigSuite.Parameter  *   protected Config cfg;  *  *   {@literal @}ConfigSuite.Config  *   public static Config firstConfig() {  *     Config cfg = new Config();  *     cfg.setString("gerrit", null, "testValue", "a");  *   }  * }  *  * public class MyTest extends MyAbstractTest {  *   {@literal @}ConfigSuite.Config  *   public static Config secondConfig() {  *     Config cfg = new Config();  *     cfg.setString("gerrit", null, "testValue", "b");  *   }  *  *   {@literal @}Test  *   public void myTest() {  *     // Test using cfg.  *   }  * }  *</pre>  *  * This creates a suite of tests with three groups:  *  *<ul>  *<li><strong>default</strong>: {@code MyTest.myTest}  *<li><strong>firstConfig</strong>: {@code MyTest.myTest[firstConfig]}  *<li><strong>secondConfig</strong>: {@code MyTest.myTest[secondConfig]}  *</ul>  *  * Additionally, config values used by<strong>default</strong> can be set in a method annotated  * with {@code @ConfigSuite.Default}.  *  *<p>The name of the config method corresponding to the currently-running test can be stored in a  * field annotated with {@code @ConfigSuite.Name}.  */
end_comment

begin_class
DECL|class|ConfigSuite
specifier|public
class|class
name|ConfigSuite
extends|extends
name|Suite
block|{
DECL|field|DEFAULT
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT
init|=
literal|"default"
decl_stmt|;
annotation|@
name|Target
argument_list|(
block|{
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|Default
specifier|public
specifier|static
annotation_defn|@interface
name|Default
block|{}
annotation|@
name|Target
argument_list|(
block|{
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|Config
specifier|public
specifier|static
annotation_defn|@interface
name|Config
block|{}
annotation|@
name|Target
argument_list|(
block|{
name|FIELD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|Parameter
specifier|public
specifier|static
annotation_defn|@interface
name|Parameter
block|{}
annotation|@
name|Target
argument_list|(
block|{
name|FIELD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|Name
specifier|public
specifier|static
annotation_defn|@interface
name|Name
block|{}
DECL|class|ConfigRunner
specifier|private
specifier|static
class|class
name|ConfigRunner
extends|extends
name|BlockJUnit4ClassRunner
block|{
DECL|field|configMethod
specifier|private
specifier|final
name|Method
name|configMethod
decl_stmt|;
DECL|field|parameterField
specifier|private
specifier|final
name|Field
name|parameterField
decl_stmt|;
DECL|field|nameField
specifier|private
specifier|final
name|Field
name|nameField
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|ConfigRunner ( Class<?> clazz, Field parameterField, Field nameField, String name, Method configMethod)
specifier|private
name|ConfigRunner
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Field
name|parameterField
parameter_list|,
name|Field
name|nameField
parameter_list|,
name|String
name|name
parameter_list|,
name|Method
name|configMethod
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameterField
operator|=
name|parameterField
expr_stmt|;
name|this
operator|.
name|nameField
operator|=
name|nameField
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|configMethod
operator|=
name|configMethod
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createTest ()
specifier|public
name|Object
name|createTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|test
init|=
name|getTestClass
argument_list|()
operator|.
name|getJavaClass
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|parameterField
operator|.
name|set
argument_list|(
name|test
argument_list|,
name|callConfigMethod
argument_list|(
name|configMethod
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|nameField
operator|!=
literal|null
condition|)
block|{
name|nameField
operator|.
name|set
argument_list|(
name|test
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|test
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|protected
name|String
name|getName
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|name
argument_list|,
name|DEFAULT
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|testName (FrameworkMethod method)
specifier|protected
name|String
name|testName
parameter_list|(
name|FrameworkMethod
name|method
parameter_list|)
block|{
name|String
name|n
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|name
operator|==
literal|null
condition|?
name|n
else|:
name|n
operator|+
literal|"["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|runnersFor (Class<?> clazz)
specifier|private
specifier|static
name|List
argument_list|<
name|Runner
argument_list|>
name|runnersFor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Method
name|defaultConfig
init|=
name|getDefaultConfig
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Method
argument_list|>
name|configs
init|=
name|getConfigs
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|Field
name|parameterField
init|=
name|getOnlyField
argument_list|(
name|clazz
argument_list|,
name|Parameter
operator|.
name|class
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|parameterField
operator|!=
literal|null
argument_list|,
literal|"No @ConfigSuite.Field found"
argument_list|)
expr_stmt|;
name|Field
name|nameField
init|=
name|getOnlyField
argument_list|(
name|clazz
argument_list|,
name|Name
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Runner
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|configs
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ConfigRunner
argument_list|(
name|clazz
argument_list|,
name|parameterField
argument_list|,
name|nameField
argument_list|,
literal|null
argument_list|,
name|defaultConfig
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|configs
control|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ConfigRunner
argument_list|(
name|clazz
argument_list|,
name|parameterField
argument_list|,
name|nameField
argument_list|,
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|InitializationError
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Errors initializing runners:"
argument_list|)
expr_stmt|;
for|for
control|(
name|Throwable
name|t
range|:
name|e
operator|.
name|getCauses
argument_list|()
control|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getDefaultConfig (Class<?> clazz)
specifier|private
specifier|static
name|Method
name|getDefaultConfig
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Method
name|result
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|clazz
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Default
name|ann
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|Default
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
name|result
operator|==
literal|null
argument_list|,
literal|"Multiple methods annotated with @ConfigSuite.Method: %s, %s"
argument_list|,
name|result
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|result
operator|=
name|m
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|getConfigs (Class<?> clazz)
specifier|private
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getConfigs
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|clazz
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Config
name|ann
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|Config
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
operator|!
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|DEFAULT
argument_list|)
argument_list|,
literal|"@ConfigSuite.Config cannot be named %s"
argument_list|,
name|DEFAULT
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|callConfigMethod (Method m)
specifier|private
specifier|static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
name|callConfigMethod
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
argument_list|()
return|;
block|}
name|checkArgument
argument_list|(
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
argument_list|,
literal|"%s must return Config"
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|(
name|m
operator|.
name|getModifiers
argument_list|()
operator|&
name|Modifier
operator|.
name|STATIC
operator|)
operator|!=
literal|0
argument_list|,
literal|"%s must be static"
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
argument_list|,
literal|"%s must take no parameters"
argument_list|,
name|m
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|(
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|IllegalArgumentException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getOnlyField (Class<?> clazz, Class<? extends Annotation> ann)
specifier|private
specifier|static
name|Field
name|getOnlyField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|ann
parameter_list|)
block|{
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|clazz
operator|.
name|getFields
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getAnnotation
argument_list|(
name|ann
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|checkArgument
argument_list|(
name|fields
operator|.
name|size
argument_list|()
operator|<=
literal|1
argument_list|,
literal|"expected 1 @ConfigSuite.%s field, found: %s"
argument_list|,
name|ann
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|fields
argument_list|)
expr_stmt|;
return|return
name|Iterables
operator|.
name|getFirst
argument_list|(
name|fields
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|ConfigSuite (Class<?> clazz)
specifier|public
name|ConfigSuite
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|clazz
argument_list|,
name|runnersFor
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


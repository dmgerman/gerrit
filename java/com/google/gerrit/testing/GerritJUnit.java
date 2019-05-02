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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
package|;
end_package

begin_comment
comment|/** Static JUnit utility methods. */
end_comment

begin_class
DECL|class|GerritJUnit
specifier|public
class|class
name|GerritJUnit
block|{
comment|/**    * Assert that an exception is thrown by a block of code.    *    *<p>This method is source-compatible with<a    * href="https://junit.org/junit4/javadoc/latest/org/junit/Assert.html#assertThrows(java.lang.Class,%20org.junit.function.ThrowingRunnable)">JUnit    * 4.13 beta</a>.    *    *<p>This construction is recommended by the Truth team for use in conjunction with asserting    * over a {@code ThrowableSubject} on the return type:    *    *<pre>    *   MyException e = assertThrows(MyException.class, () -> doSomething(foo));    *   assertThat(e).isInstanceOf(MySubException.class);    *   assertThat(e).hasMessageThat().contains("sub-exception occurred");    *</pre>    *    * @param throwableClass expected exception type.    * @param runnable runnable containing arbitrary code.    * @return exception that was thrown.    */
DECL|method|assertThrows ( Class<T> throwableClass, ThrowingRunnable runnable)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|T
name|assertThrows
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|throwableClass
parameter_list|,
name|ThrowingRunnable
name|runnable
parameter_list|)
block|{
try|try
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
operator|!
name|throwableClass
operator|.
name|isInstance
argument_list|(
name|t
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"expected "
operator|+
name|throwableClass
operator|.
name|getName
argument_list|()
operator|+
literal|" but "
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" was thrown"
argument_list|,
name|t
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|toReturn
init|=
operator|(
name|T
operator|)
name|t
decl_stmt|;
return|return
name|toReturn
return|;
block|}
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"expected "
operator|+
name|throwableClass
operator|.
name|getName
argument_list|()
operator|+
literal|" but no exception was thrown"
argument_list|)
throw|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ThrowingRunnable
specifier|public
interface|interface
name|ThrowingRunnable
block|{
DECL|method|run ()
name|void
name|run
parameter_list|()
throws|throws
name|Throwable
function_decl|;
block|}
DECL|method|GerritJUnit ()
specifier|private
name|GerritJUnit
parameter_list|()
block|{}
block|}
end_class

end_unit


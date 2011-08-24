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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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

begin_comment
comment|/** Performs replacements on strings such as<code>Hello ${user}</code>. */
end_comment

begin_class
DECL|class|ParameterizedString
specifier|public
class|class
name|ParameterizedString
block|{
comment|/** Obtain a string which has no parameters and always produces the value. */
DECL|method|asis (final String constant)
specifier|public
specifier|static
name|ParameterizedString
name|asis
parameter_list|(
specifier|final
name|String
name|constant
parameter_list|)
block|{
return|return
operator|new
name|ParameterizedString
argument_list|(
operator|new
name|Constant
argument_list|(
name|constant
argument_list|)
argument_list|)
return|;
block|}
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
DECL|field|rawPattern
specifier|private
specifier|final
name|String
name|rawPattern
decl_stmt|;
DECL|field|patternOps
specifier|private
specifier|final
name|List
argument_list|<
name|Format
argument_list|>
name|patternOps
decl_stmt|;
DECL|field|parameters
specifier|private
specifier|final
name|List
argument_list|<
name|Parameter
argument_list|>
name|parameters
decl_stmt|;
DECL|method|ParameterizedString ()
specifier|protected
name|ParameterizedString
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Constant
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ParameterizedString (final Constant c)
specifier|private
name|ParameterizedString
parameter_list|(
specifier|final
name|Constant
name|c
parameter_list|)
block|{
name|pattern
operator|=
name|c
operator|.
name|text
expr_stmt|;
name|rawPattern
operator|=
name|c
operator|.
name|text
expr_stmt|;
name|patternOps
operator|=
name|Collections
operator|.
expr|<
name|Format
operator|>
name|singletonList
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|parameters
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
DECL|method|ParameterizedString (final String pattern)
specifier|public
name|ParameterizedString
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|raw
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Parameter
argument_list|>
name|prs
init|=
operator|new
name|ArrayList
argument_list|<
name|Parameter
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Format
argument_list|>
name|ops
init|=
operator|new
name|ArrayList
argument_list|<
name|Format
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|pattern
operator|.
name|length
argument_list|()
condition|)
block|{
specifier|final
name|int
name|b
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|<
literal|0
condition|)
block|{
break|break;
block|}
specifier|final
name|int
name|e
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|,
name|b
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|raw
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|ops
operator|.
name|add
argument_list|(
operator|new
name|Constant
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|b
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expr
init|=
name|pattern
operator|.
name|substring
argument_list|(
name|b
operator|+
literal|2
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|String
name|parameterName
init|=
literal|""
decl_stmt|;
name|List
argument_list|<
name|Function
argument_list|>
name|functions
init|=
operator|new
name|ArrayList
argument_list|<
name|Function
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|expr
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|parameterName
operator|=
name|expr
expr_stmt|;
block|}
else|else
block|{
name|int
name|firstDot
init|=
name|expr
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|parameterName
operator|=
name|expr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstDot
argument_list|)
expr_stmt|;
name|String
name|actionsStr
init|=
name|expr
operator|.
name|substring
argument_list|(
name|firstDot
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
index|[]
name|actions
init|=
name|actionsStr
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|action
range|:
name|actions
control|)
block|{
name|Function
name|function
init|=
name|FUNCTIONS
operator|.
name|get
argument_list|(
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|==
literal|null
condition|)
block|{
name|function
operator|=
name|NOOP
expr_stmt|;
block|}
name|functions
operator|.
name|add
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Parameter
name|p
init|=
operator|new
name|Parameter
argument_list|(
name|parameterName
argument_list|,
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|functions
argument_list|)
argument_list|)
decl_stmt|;
name|raw
operator|.
name|append
argument_list|(
literal|"{"
operator|+
name|prs
operator|.
name|size
argument_list|()
operator|+
literal|"}"
argument_list|)
expr_stmt|;
name|prs
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|ops
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|i
operator|=
name|e
operator|+
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|<
name|pattern
operator|.
name|length
argument_list|()
condition|)
block|{
name|raw
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|ops
operator|.
name|add
argument_list|(
operator|new
name|Constant
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|rawPattern
operator|=
name|raw
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|patternOps
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|ops
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|prs
argument_list|)
expr_stmt|;
block|}
comment|/** Get the original pattern given to the constructor. */
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
comment|/** Get the pattern with variables replaced with {0}, {1}, ... */
DECL|method|getRawPattern ()
specifier|public
name|String
name|getRawPattern
parameter_list|()
block|{
return|return
name|rawPattern
return|;
block|}
comment|/** Get the list of parameter names, ordered by appearance in the pattern. */
DECL|method|getParameterNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|()
block|{
specifier|final
name|ArrayList
argument_list|<
name|String
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Parameter
name|p
range|:
name|parameters
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|p
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|r
argument_list|)
return|;
block|}
comment|/** Convert a map of parameters into a value array for binding. */
DECL|method|bind (final Map<String, String> params)
specifier|public
name|String
index|[]
name|bind
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|r
init|=
operator|new
name|String
index|[
name|parameters
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|r
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|format
argument_list|(
name|b
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|r
index|[
name|i
index|]
operator|=
name|b
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** Format this string by performing the variable replacements. */
DECL|method|replace (final Map<String, String> params)
specifier|public
name|String
name|replace
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Format
name|f
range|:
name|patternOps
control|)
block|{
name|f
operator|.
name|format
argument_list|(
name|r
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|replace (final String name, final String value)
specifier|public
name|Builder
name|replace
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|()
operator|.
name|replace
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getPattern
argument_list|()
return|;
block|}
DECL|class|Builder
specifier|public
specifier|final
class|class
name|Builder
block|{
DECL|field|params
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|replace (final String name, final String value)
specifier|public
name|Builder
name|replace
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ParameterizedString
operator|.
name|this
operator|.
name|replace
argument_list|(
name|params
argument_list|)
return|;
block|}
block|}
DECL|class|Format
specifier|private
specifier|static
specifier|abstract
class|class
name|Format
block|{
DECL|method|format (StringBuilder b, Map<String, String> p)
specifier|abstract
name|void
name|format
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|)
function_decl|;
block|}
DECL|class|Constant
specifier|private
specifier|static
class|class
name|Constant
extends|extends
name|Format
block|{
DECL|field|text
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
DECL|method|Constant (final String text)
name|Constant
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format (StringBuilder b, Map<String, String> p)
name|void
name|format
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|)
block|{
name|b
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Parameter
specifier|private
specifier|static
class|class
name|Parameter
extends|extends
name|Format
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|functions
specifier|private
specifier|final
name|List
argument_list|<
name|Function
argument_list|>
name|functions
decl_stmt|;
DECL|method|Parameter (final String name, final List<Function> functions)
name|Parameter
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|List
argument_list|<
name|Function
argument_list|>
name|functions
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|functions
operator|=
name|functions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format (StringBuilder b, Map<String, String> p)
name|void
name|format
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|)
block|{
name|String
name|v
init|=
name|p
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
literal|""
expr_stmt|;
block|}
for|for
control|(
name|Function
name|function
range|:
name|functions
control|)
block|{
name|v
operator|=
name|function
operator|.
name|apply
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Function
specifier|private
specifier|static
specifier|abstract
class|class
name|Function
block|{
DECL|method|apply (String a)
specifier|abstract
name|String
name|apply
parameter_list|(
name|String
name|a
parameter_list|)
function_decl|;
block|}
DECL|field|FUNCTIONS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
name|FUNCTIONS
init|=
name|initFunctions
argument_list|()
decl_stmt|;
DECL|field|NOOP
specifier|private
specifier|static
specifier|final
name|Function
name|NOOP
init|=
operator|new
name|Function
argument_list|()
block|{
annotation|@
name|Override
name|String
name|apply
parameter_list|(
name|String
name|a
parameter_list|)
block|{
return|return
name|a
return|;
block|}
block|}
decl_stmt|;
DECL|method|initFunctions ()
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
name|initFunctions
parameter_list|()
block|{
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"toLowerCase"
argument_list|,
operator|new
name|Function
argument_list|()
block|{
annotation|@
name|Override
name|String
name|apply
parameter_list|(
name|String
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"toUpperCase"
argument_list|,
operator|new
name|Function
argument_list|()
block|{
annotation|@
name|Override
name|String
name|apply
parameter_list|(
name|String
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"localPart"
argument_list|,
operator|new
name|Function
argument_list|()
block|{
annotation|@
name|Override
name|String
name|apply
parameter_list|(
name|String
name|a
parameter_list|)
block|{
name|int
name|at
init|=
name|a
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
return|return
name|at
operator|<
literal|0
condition|?
name|a
else|:
name|a
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|at
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|m
argument_list|)
return|;
block|}
block|}
end_class

end_unit


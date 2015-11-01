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
name|common
operator|.
name|base
operator|.
name|Preconditions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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
name|Modifier
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|ConfigUtil
specifier|public
class|class
name|ConfigUtil
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|allValuesOf (final T defaultValue)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|allValuesOf
parameter_list|(
specifier|final
name|T
name|defaultValue
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|T
index|[]
operator|)
name|defaultValue
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"values"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|NoSuchMethodException
decl||
name|InvocationTargetException
decl||
name|IllegalAccessException
decl||
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot obtain enumeration values"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parse a Java enumeration from the configuration.    *    * @param<T> type of the enumeration object.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param valueString string value from git Config    * @param all all possible values in the enumeration which should be    *        recognized. This should be {@code EnumType.values()}.    * @return the selected enumeration value, or {@code defaultValue}.    */
DECL|method|getEnum (final String section, final String subsection, final String setting, String valueString, final T[] all)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|T
name|getEnum
parameter_list|(
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|subsection
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
name|String
name|valueString
parameter_list|,
specifier|final
name|T
index|[]
name|all
parameter_list|)
block|{
name|String
name|n
init|=
name|valueString
operator|.
name|replace
argument_list|(
literal|' '
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|T
name|e
range|:
name|all
control|)
block|{
if|if
condition|(
name|e
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|n
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"Value \""
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|valueString
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"\" not recognized in "
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|section
argument_list|)
expr_stmt|;
if|if
condition|(
name|subsection
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|subsection
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|setting
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|"; supported values are: "
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|T
name|e
range|:
name|all
control|)
block|{
name|r
operator|.
name|append
argument_list|(
name|e
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|r
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
throw|;
block|}
comment|/**    * Parse a Java enumeration list from the configuration.    *    * @param<T> type of the enumeration object.    * @param config the configuration file to read.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param defaultValue default value to return if the setting was not set.    *        Must not be null as the enumeration values are derived from this.    * @return the selected enumeration values list, or {@code defaultValue}.    */
DECL|method|getEnumList (final Config config, final String section, final String subsection, final String setting, final T defaultValue)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getEnumList
parameter_list|(
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|subsection
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
specifier|final
name|T
name|defaultValue
parameter_list|)
block|{
specifier|final
name|T
index|[]
name|all
init|=
name|allValuesOf
argument_list|(
name|defaultValue
argument_list|)
decl_stmt|;
return|return
name|getEnumList
argument_list|(
name|config
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|,
name|all
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|/**    * Parse a Java enumeration list from the configuration.    *    * @param<T> type of the enumeration object.    * @param config the configuration file to read.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param all all possible values in the enumeration which should be    *        recognized. This should be {@code EnumType.values()}.    * @param defaultValue default value to return if the setting was not set.    *        This value may be null.    * @return the selected enumeration values list, or {@code defaultValue}.    */
DECL|method|getEnumList (final Config config, final String section, final String subsection, final String setting, final T[] all, final T defaultValue)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getEnumList
parameter_list|(
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|subsection
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
specifier|final
name|T
index|[]
name|all
parameter_list|,
specifier|final
name|T
name|defaultValue
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|String
index|[]
name|values
init|=
name|config
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|string
range|:
name|values
control|)
block|{
if|if
condition|(
name|string
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|getEnum
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|,
name|string
argument_list|,
name|all
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
comment|/**    * Parse a numerical time unit, such as "1 minute", from the configuration.    *    * @param config the configuration file to read.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param defaultValue default value to return if no value was set in the    *        configuration file.    * @param wantUnit the units of {@code defaultValue} and the return value, as    *        well as the units to assume if the value does not contain an    *        indication of the units.    * @return the setting, or {@code defaultValue} if not set, expressed in    *         {@code units}.    */
DECL|method|getTimeUnit (final Config config, final String section, final String subsection, final String setting, final long defaultValue, final TimeUnit wantUnit)
specifier|public
specifier|static
name|long
name|getTimeUnit
parameter_list|(
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|subsection
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
specifier|final
name|long
name|defaultValue
parameter_list|,
specifier|final
name|TimeUnit
name|wantUnit
parameter_list|)
block|{
specifier|final
name|String
name|valueString
init|=
name|config
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueString
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
name|String
name|s
init|=
name|valueString
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
comment|/* negative */
condition|)
block|{
throw|throw
name|notTimeUnit
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|,
name|valueString
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|getTimeUnit
argument_list|(
name|s
argument_list|,
name|defaultValue
argument_list|,
name|wantUnit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|notTime
parameter_list|)
block|{
throw|throw
name|notTimeUnit
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|setting
argument_list|,
name|valueString
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parse a numerical time unit, such as "1 minute", from a string.    *    * @param valueString the string to parse.    * @param defaultValue default value to return if no value was set in the    *        configuration file.    * @param wantUnit the units of {@code defaultValue} and the return value, as    *        well as the units to assume if the value does not contain an    *        indication of the units.    * @return the setting, or {@code defaultValue} if not set, expressed in    *         {@code units}.    */
DECL|method|getTimeUnit (final String valueString, long defaultValue, TimeUnit wantUnit)
specifier|public
specifier|static
name|long
name|getTimeUnit
parameter_list|(
specifier|final
name|String
name|valueString
parameter_list|,
name|long
name|defaultValue
parameter_list|,
name|TimeUnit
name|wantUnit
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(0|[1-9][0-9]*)\\s*(.*)$"
argument_list|)
operator|.
name|matcher
argument_list|(
name|valueString
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
name|String
name|digits
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|unitName
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|TimeUnit
name|inputUnit
decl_stmt|;
name|int
name|inputMul
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|unitName
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|wantUnit
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"ms"
argument_list|,
literal|"milliseconds"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"s"
argument_list|,
literal|"sec"
argument_list|,
literal|"second"
argument_list|,
literal|"seconds"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"m"
argument_list|,
literal|"min"
argument_list|,
literal|"minute"
argument_list|,
literal|"minutes"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|MINUTES
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"h"
argument_list|,
literal|"hr"
argument_list|,
literal|"hour"
argument_list|,
literal|"hours"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|HOURS
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"d"
argument_list|,
literal|"day"
argument_list|,
literal|"days"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|DAYS
expr_stmt|;
name|inputMul
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"w"
argument_list|,
literal|"week"
argument_list|,
literal|"weeks"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|DAYS
expr_stmt|;
name|inputMul
operator|=
literal|7
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"mon"
argument_list|,
literal|"month"
argument_list|,
literal|"months"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|DAYS
expr_stmt|;
name|inputMul
operator|=
literal|30
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|match
argument_list|(
name|unitName
argument_list|,
literal|"y"
argument_list|,
literal|"year"
argument_list|,
literal|"years"
argument_list|)
condition|)
block|{
name|inputUnit
operator|=
name|TimeUnit
operator|.
name|DAYS
expr_stmt|;
name|inputMul
operator|=
literal|365
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|notTimeUnit
argument_list|(
name|valueString
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|wantUnit
operator|.
name|convert
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|digits
argument_list|)
operator|*
name|inputMul
argument_list|,
name|inputUnit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
throw|throw
name|notTimeUnit
argument_list|(
name|valueString
argument_list|)
throw|;
block|}
block|}
DECL|method|getRequired (Config cfg, String section, String name)
specifier|public
specifier|static
name|String
name|getRequired
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|name
parameter_list|)
block|{
specifier|final
name|String
name|v
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No "
operator|+
name|section
operator|+
literal|"."
operator|+
name|name
operator|+
literal|" configured"
argument_list|)
throw|;
block|}
return|return
name|v
return|;
block|}
comment|/**    * Store section by inspecting Java class attributes.    *<p>    * Optimize the storage by unsetting a variable if it is    * being set to default value by the server.    *<p>    * Fields marked with final or transient modifiers are skipped.    *    * @param cfg config in which the values should be stored    * @param section section    * @param sub subsection    * @param s instance of class with config values    * @param defaults instance of class with default values    * @throws ConfigInvalidException    */
DECL|method|storeSection (Config cfg, String section, String sub, T s, T defaults)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|storeSection
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|sub
parameter_list|,
name|T
name|s
parameter_list|,
name|T
name|defaults
parameter_list|)
throws|throws
name|ConfigInvalidException
block|{
try|try
block|{
for|for
control|(
name|Field
name|f
range|:
name|s
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|skipField
argument_list|(
name|f
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|t
init|=
name|f
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|n
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|c
init|=
name|f
operator|.
name|get
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|Object
name|d
init|=
name|f
operator|.
name|get
argument_list|(
name|defaults
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isString
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|d
argument_list|,
literal|"Default cannot be null for: "
operator|+
name|n
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|null
operator|||
name|c
operator|.
name|equals
argument_list|(
name|d
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isString
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|String
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isInteger
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setInt
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Integer
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isLong
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setLong
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Long
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isBoolean
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Boolean
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|cfg
operator|.
name|setEnum
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"type is unknown: "
operator|+
name|t
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
decl||
name|IllegalArgumentException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"cannot save values"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Load section by inspecting Java class attributes.    *<p>    * Config values are stored optimized: no default values are stored.    * The loading is performed eagerly: all values are set.    *<p>    * Fields marked with final or transient modifiers are skipped.    *    * @param cfg config from which the values are loaded    * @param section section    * @param sub subsection    * @param s instance of class in which the values are set    * @param defaults instance of class with default values    * @param i instance to merge during the load. When present, the    * boolean fields are not nullified when their values are false    * @return loaded instance    * @throws ConfigInvalidException    */
DECL|method|loadSection (Config cfg, String section, String sub, T s, T defaults, T i)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|loadSection
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|sub
parameter_list|,
name|T
name|s
parameter_list|,
name|T
name|defaults
parameter_list|,
name|T
name|i
parameter_list|)
throws|throws
name|ConfigInvalidException
block|{
try|try
block|{
for|for
control|(
name|Field
name|f
range|:
name|s
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|skipField
argument_list|(
name|f
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|t
init|=
name|f
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|n
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|d
init|=
name|f
operator|.
name|get
argument_list|(
name|defaults
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isString
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|d
argument_list|,
literal|"Default cannot be null for: "
operator|+
name|n
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isString
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|String
name|v
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
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
operator|(
name|String
operator|)
name|d
expr_stmt|;
block|}
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isInteger
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|cfg
operator|.
name|getInt
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Integer
operator|)
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isLong
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|cfg
operator|.
name|getLong
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Long
operator|)
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isBoolean
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|boolean
name|b
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Boolean
operator|)
name|d
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|||
name|i
operator|!=
literal|null
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|t
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|cfg
operator|.
name|getEnum
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|n
argument_list|,
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"type is unknown: "
operator|+
name|t
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
name|Object
name|o
init|=
name|f
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|s
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
decl||
name|IllegalArgumentException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigInvalidException
argument_list|(
literal|"cannot load values"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|s
return|;
block|}
DECL|method|skipField (Field field)
specifier|private
specifier|static
name|boolean
name|skipField
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
name|int
name|modifiers
init|=
name|field
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
return|return
name|Modifier
operator|.
name|isFinal
argument_list|(
name|modifiers
argument_list|)
operator|||
name|Modifier
operator|.
name|isTransient
argument_list|(
name|modifiers
argument_list|)
return|;
block|}
DECL|method|isString (Class<?> t)
specifier|private
specifier|static
name|boolean
name|isString
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
name|String
operator|.
name|class
operator|==
name|t
return|;
block|}
DECL|method|isBoolean (Class<?> t)
specifier|private
specifier|static
name|boolean
name|isBoolean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|class
operator|==
name|t
operator|||
name|boolean
operator|.
name|class
operator|==
name|t
return|;
block|}
DECL|method|isLong (Class<?> t)
specifier|private
specifier|static
name|boolean
name|isLong
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
name|Long
operator|.
name|class
operator|==
name|t
operator|||
name|long
operator|.
name|class
operator|==
name|t
return|;
block|}
DECL|method|isInteger (Class<?> t)
specifier|private
specifier|static
name|boolean
name|isInteger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|class
operator|==
name|t
operator|||
name|int
operator|.
name|class
operator|==
name|t
return|;
block|}
DECL|method|match (final String a, final String... cases)
specifier|private
specifier|static
name|boolean
name|match
parameter_list|(
specifier|final
name|String
name|a
parameter_list|,
specifier|final
name|String
modifier|...
name|cases
parameter_list|)
block|{
for|for
control|(
specifier|final
name|String
name|b
range|:
name|cases
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|b
operator|.
name|equalsIgnoreCase
argument_list|(
name|a
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|notTimeUnit (final String section, final String subsection, final String setting, final String valueString)
specifier|private
specifier|static
name|IllegalArgumentException
name|notTimeUnit
parameter_list|(
specifier|final
name|String
name|section
parameter_list|,
specifier|final
name|String
name|subsection
parameter_list|,
specifier|final
name|String
name|setting
parameter_list|,
specifier|final
name|String
name|valueString
parameter_list|)
block|{
return|return
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid time unit value: "
operator|+
name|section
operator|+
operator|(
name|subsection
operator|!=
literal|null
condition|?
literal|"."
operator|+
name|subsection
else|:
literal|""
operator|)
operator|+
literal|"."
operator|+
name|setting
operator|+
literal|" = "
operator|+
name|valueString
argument_list|)
return|;
block|}
DECL|method|notTimeUnit (final String val)
specifier|private
specifier|static
name|IllegalArgumentException
name|notTimeUnit
parameter_list|(
specifier|final
name|String
name|val
parameter_list|)
block|{
return|return
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid time unit value: "
operator|+
name|val
argument_list|)
return|;
block|}
DECL|method|ConfigUtil ()
specifier|private
name|ConfigUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit


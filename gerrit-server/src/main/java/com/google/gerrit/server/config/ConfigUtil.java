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
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|equalsIgnoreCase
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
name|reviewdb
operator|.
name|AccountGroup
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
name|reviewdb
operator|.
name|AccountGroupName
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
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
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
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_class
DECL|class|ConfigUtil
specifier|public
class|class
name|ConfigUtil
block|{
comment|/**    * Parse a Java enumeration from the configuration.    *    * @param<T> type of the enumeration object.    * @param config the configuration file to read.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param defaultValue default value to return if the setting was not set.    *        Must not be null as the enumeration values are derived from this.    * @return the selected enumeration value, or {@code defaultValue}.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getEnum (final Config config, final String section, final String subsection, final String setting, final T defaultValue)
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
name|T
name|getEnum
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
decl_stmt|;
try|try
block|{
name|all
operator|=
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
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
catch|catch
parameter_list|(
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
catch|catch
parameter_list|(
name|IllegalAccessException
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
catch|catch
parameter_list|(
name|InvocationTargetException
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
catch|catch
parameter_list|(
name|NoSuchMethodException
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
return|return
name|getEnum
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
comment|/**    * Parse a Java enumeration from the configuration.    *    * @param<T> type of the enumeration object.    * @param config the configuration file to read.    * @param section section the key is in.    * @param subsection subsection the key is in, or null if not in a subsection.    * @param setting name of the setting to read.    * @param all all possible values in the enumeration which should be    *        recognized. This should be {@code EnumType.values()}.    * @param defaultValue default value to return if the setting was not set.    *        This value may be null.    * @return the selected enumeration value, or {@code defaultValue}.    */
DECL|method|getEnum (final Config config, final String section, final String subsection, final String setting, final T[] all, final T defaultValue)
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
name|T
name|getEnum
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
name|equalsIgnoreCase
argument_list|(
name|e
operator|.
name|name
argument_list|()
argument_list|,
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
comment|/**    * Parse a numerical time unit, such as "1 minute", from a string.    *    * @param s the string to parse.    * @param defaultValue default value to return if no value was set in the    *        configuration file.    * @param wantUnit the units of {@code defaultValue} and the return value, as    *        well as the units to assume if the value does not contain an    *        indication of the units.    * @return the setting, or {@code defaultValue} if not set, expressed in    *         {@code units}.    */
DECL|method|getTimeUnit (String s, long defaultValue, TimeUnit wantUnit)
specifier|public
specifier|static
name|long
name|getTimeUnit
parameter_list|(
name|String
name|s
parameter_list|,
name|long
name|defaultValue
parameter_list|,
name|TimeUnit
name|wantUnit
parameter_list|)
block|{
specifier|final
name|String
name|valueString
init|=
name|s
decl_stmt|;
specifier|final
name|String
name|unitName
decl_stmt|;
specifier|final
name|int
name|sp
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp
operator|>
literal|0
condition|)
block|{
name|unitName
operator|=
name|s
operator|.
name|substring
argument_list|(
name|sp
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|char
name|last
init|=
name|s
operator|.
name|charAt
argument_list|(
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
literal|'0'
operator|<=
name|last
operator|&&
name|last
operator|<=
literal|'9'
condition|)
block|{
name|unitName
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|unitName
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|last
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
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
name|s
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
comment|/**    * Resolve groups from group names, via the database. Group names not found in    * the database will be skipped.    *    * @param dbfactory database to resolve from.    * @param groupNames group names to resolve.    * @param log log for any warnings and errors.    * @param groupNotFoundWarning formatted message to output to the log for each    *        group name which is not found in the database.<code>{0}</code> will    *        be replaced with the group name.    * @return the actual groups resolved from the database. If no groups are    *         found, returns an empty {@code Set}, never {@code null}.    */
DECL|method|groupsFor ( SchemaFactory<ReviewDb> dbfactory, String[] groupNames, Logger log, String groupNotFoundWarning)
specifier|public
specifier|static
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groupsFor
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|dbfactory
parameter_list|,
name|String
index|[]
name|groupNames
parameter_list|,
name|Logger
name|log
parameter_list|,
name|String
name|groupNotFoundWarning
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|dbfactory
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|name
range|:
name|groupNames
control|)
block|{
name|AccountGroupName
name|group
init|=
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|groupNotFoundWarning
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|add
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Database error, cannot load groups"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**    * Resolve groups from group names, via the database. Group names not found in    * the database will be skipped.    *    * @param dbfactory database to resolve from.    * @param groupNames group names to resolve.    * @param log log for any warnings and errors.    * @return the actual groups resolved from the database. If no groups are    *         found, returns an empty {@code Set}, never {@code null}.    */
DECL|method|groupsFor ( SchemaFactory<ReviewDb> dbfactory, String[] groupNames, Logger log)
specifier|public
specifier|static
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groupsFor
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|dbfactory
parameter_list|,
name|String
index|[]
name|groupNames
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
return|return
name|groupsFor
argument_list|(
name|dbfactory
argument_list|,
name|groupNames
argument_list|,
name|log
argument_list|,
literal|"Group \"{0}\" not in database, skipping."
argument_list|)
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
name|equalsIgnoreCase
argument_list|(
name|a
argument_list|,
name|b
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


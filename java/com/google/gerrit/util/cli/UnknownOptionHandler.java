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
DECL|package|com.google.gerrit.util.cli
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
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
name|common
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Classes that define command-line options by using the {@link org.kohsuke.args4j.Option}  * annotation can implement this class to accept and handle unknown options.  *  *<p>If a user specifies an unknown option and this unknown option doesn't get accepted, the  * parsing of the command-line options fails and the user gets an error (this is the default  * behavior if classes do not implement this interface).  */
end_comment

begin_interface
DECL|interface|UnknownOptionHandler
specifier|public
interface|interface
name|UnknownOptionHandler
block|{
comment|/**    * Checks whether the given option name matches the naming pattern of options that are defined by    * plugins that were defined by registering a {@link    * com.google.gerrit.server.DynamicOptions.DynamicBean}.    *    * @param optionName name of the option    * @return {@code true} if it's a plugin option, otherwise {@code false}    */
DECL|method|isPluginOption (String optionName)
specifier|public
specifier|static
name|boolean
name|isPluginOption
parameter_list|(
name|String
name|optionName
parameter_list|)
block|{
comment|// Options from plugins have the following name pattern: '--<plugin-name>--<option-name>'
if|if
condition|(
name|optionName
operator|.
name|startsWith
argument_list|(
literal|"--"
argument_list|)
condition|)
block|{
name|optionName
operator|=
name|optionName
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|optionName
operator|.
name|contains
argument_list|(
literal|"--"
argument_list|)
return|;
block|}
comment|/**    * Whether an unknown option should be accepted.    *    *<p>If an unknown option is not accepted, the parsing of the command-line options fails and the    * user gets an error.    *    *<p>This method can be used to ignore unknown options (without failure for the user) or to    * handle them.    *    * @param name the name of an unknown option that was provided by the user    * @param value the value of the unknown option that was provided by the user    * @return whether this unknown options is accepted    */
DECL|method|accept (String name, @Nullable String value)
name|boolean
name|accept
parameter_list|(
name|String
name|name
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.receive
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|receive
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
name|collect
operator|.
name|ImmutableMap
operator|.
name|toImmutableMap
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
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/** Possible values for {@code -o notedb=X} push option. */
end_comment

begin_enum
DECL|enum|NoteDbPushOption
specifier|public
enum|enum
name|NoteDbPushOption
block|{
DECL|enumConstant|DISALLOW
name|DISALLOW
block|,
DECL|enumConstant|ALLOW
name|ALLOW
block|;
DECL|field|OPTION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|OPTION_NAME
init|=
literal|"notedb"
decl_stmt|;
DECL|field|ALL
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|NoteDbPushOption
argument_list|>
name|ALL
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|values
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableMap
argument_list|(
name|NoteDbPushOption
operator|::
name|value
argument_list|,
name|Function
operator|.
name|identity
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Parses an option value from a lowercase string representation.    *    * @param value input value.    * @return parsed value, or empty if no value matched.    */
DECL|method|parse (String value)
specifier|public
specifier|static
name|Optional
argument_list|<
name|NoteDbPushOption
argument_list|>
name|parse
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|ALL
operator|.
name|get
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
block|}
end_enum

end_unit


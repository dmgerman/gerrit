begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Enums
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
name|Optional
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
name|Strings
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
name|ImmutableList
import|;
end_import

begin_enum
DECL|enum|NoteDbMode
specifier|public
enum|enum
name|NoteDbMode
block|{
comment|/** NoteDb is disabled. */
DECL|enumConstant|OFF
name|OFF
block|,
comment|/** Reading and writing all data to NoteDb is enabled. */
DECL|enumConstant|READ_WRITE
name|READ_WRITE
block|,
comment|/**    * Run tests with NoteDb disabled, then convert ReviewDb to NoteDb and check    * that the results match.    */
DECL|enumConstant|CHECK
name|CHECK
block|;
DECL|field|VAR
specifier|private
specifier|static
specifier|final
name|String
name|VAR
init|=
literal|"GERRIT_NOTEDB"
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|static
name|NoteDbMode
name|get
parameter_list|()
block|{
if|if
condition|(
name|isEnvVarTrue
argument_list|(
literal|"GERRIT_ENABLE_NOTEDB"
argument_list|)
condition|)
block|{
comment|// TODO(dborowitz): Remove once GerritForge CI is migrated.
return|return
name|READ_WRITE
return|;
block|}
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|VAR
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|OFF
return|;
block|}
name|value
operator|=
name|value
operator|.
name|toUpperCase
argument_list|()
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|NoteDbMode
argument_list|>
name|mode
init|=
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|NoteDbMode
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|mode
operator|.
name|isPresent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid value for "
operator|+
name|VAR
operator|+
literal|": "
operator|+
name|System
operator|.
name|getenv
argument_list|(
name|VAR
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|mode
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|readWrite ()
specifier|public
specifier|static
name|boolean
name|readWrite
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|==
name|READ_WRITE
return|;
block|}
DECL|method|isEnvVarTrue (String name)
specifier|private
specifier|static
name|boolean
name|isEnvVarTrue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|value
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"yes"
argument_list|,
literal|"y"
argument_list|,
literal|"true"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_enum

end_unit


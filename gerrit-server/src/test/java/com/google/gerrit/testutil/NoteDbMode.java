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
name|Strings
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
comment|/** Writing data to NoteDb is enabled. */
DECL|enumConstant|WRITE
name|WRITE
block|,
comment|/** Reading and writing all data to NoteDb is enabled. */
DECL|enumConstant|READ_WRITE
name|READ_WRITE
block|,
comment|/**    * Run tests with NoteDb disabled, then convert ReviewDb to NoteDb and check    * that the results match.    */
DECL|enumConstant|CHECK
name|CHECK
block|;
DECL|field|ENV_VAR
specifier|private
specifier|static
specifier|final
name|String
name|ENV_VAR
init|=
literal|"GERRIT_NOTEDB"
decl_stmt|;
DECL|field|SYS_PROP
specifier|private
specifier|static
specifier|final
name|String
name|SYS_PROP
init|=
literal|"gerrit.notedb"
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|static
name|NoteDbMode
name|get
parameter_list|()
block|{
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
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
name|value
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
expr_stmt|;
block|}
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
name|NoteDbMode
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
operator|.
name|orNull
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
condition|)
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for env variable %s: %s"
argument_list|,
name|ENV_VAR
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for system property %s: %s"
argument_list|,
name|SYS_PROP
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
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
block|}
end_enum

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|Strings
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
name|common
operator|.
name|Nullable
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
name|extensions
operator|.
name|restapi
operator|.
name|BadRequestException
import|;
end_import

begin_comment
comment|/** Utility functions to manipulate commit messages. */
end_comment

begin_class
DECL|class|CommitMessageUtil
specifier|public
class|class
name|CommitMessageUtil
block|{
DECL|method|CommitMessageUtil ()
specifier|private
name|CommitMessageUtil
parameter_list|()
block|{}
comment|/**    * Checks for invalid (empty or containing \0) commit messages and appends a newline character to    * the commit message.    *    * @throws BadRequestException if the commit message is null or empty    * @returns the trimmed message with a trailing newline character    */
DECL|method|checkAndSanitizeCommitMessage (@ullable String commitMessage)
specifier|public
specifier|static
name|String
name|checkAndSanitizeCommitMessage
parameter_list|(
annotation|@
name|Nullable
name|String
name|commitMessage
parameter_list|)
throws|throws
name|BadRequestException
block|{
name|String
name|trimmed
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Commit message cannot be null or empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|trimmed
operator|.
name|indexOf
argument_list|(
literal|0
argument_list|)
operator|>=
literal|0
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Commit message cannot have NUL character"
argument_list|)
throw|;
block|}
name|trimmed
operator|=
name|trimmed
operator|+
literal|"\n"
expr_stmt|;
return|return
name|trimmed
return|;
block|}
block|}
end_class

end_unit


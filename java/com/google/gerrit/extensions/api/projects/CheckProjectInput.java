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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
package|;
end_package

begin_class
DECL|class|CheckProjectInput
specifier|public
class|class
name|CheckProjectInput
block|{
DECL|field|autoCloseableChangesCheck
specifier|public
name|AutoCloseableChangesCheckInput
name|autoCloseableChangesCheck
decl_stmt|;
DECL|class|AutoCloseableChangesCheckInput
specifier|public
specifier|static
class|class
name|AutoCloseableChangesCheckInput
block|{
comment|/** Whether auto-closeable changes should be fixed by setting their status to MERGED. */
DECL|field|fix
specifier|public
name|Boolean
name|fix
decl_stmt|;
comment|/** Branch that should be checked for auto-closeable changes. */
DECL|field|branch
specifier|public
name|String
name|branch
decl_stmt|;
comment|/** Number of commits to skip. */
DECL|field|skipCommits
specifier|public
name|Integer
name|skipCommits
decl_stmt|;
comment|/** Maximum number of commits to walk. */
DECL|field|maxCommits
specifier|public
name|Integer
name|maxCommits
decl_stmt|;
block|}
block|}
end_class

end_unit


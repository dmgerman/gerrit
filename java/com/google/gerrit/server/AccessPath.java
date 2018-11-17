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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_comment
comment|/** How the {@link CurrentUser} is accessing Gerrit. */
end_comment

begin_enum
DECL|enum|AccessPath
specifier|public
enum|enum
name|AccessPath
block|{
comment|/** An unknown access path, probably should not be special. */
DECL|enumConstant|UNKNOWN
name|UNKNOWN
block|,
comment|/** Access through the REST API. */
DECL|enumConstant|REST_API
name|REST_API
block|,
comment|/** Access by a web cookie. This path is not protected like REST_API. */
DECL|enumConstant|WEB_BROWSER
name|WEB_BROWSER
block|,
comment|/** Access through an SSH command that is not invoked by Git. */
DECL|enumConstant|SSH_COMMAND
name|SSH_COMMAND
block|,
comment|/** Access from a Git client using any Git protocol. */
DECL|enumConstant|GIT
name|GIT
block|}
end_enum

end_unit


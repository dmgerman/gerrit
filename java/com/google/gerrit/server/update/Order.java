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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
package|;
end_package

begin_comment
comment|/** Order of execution of the various phases of a {@link BatchUpdate}. */
end_comment

begin_enum
DECL|enum|Order
specifier|public
enum|enum
name|Order
block|{
comment|/**    * Update the repository and execute all ref updates before touching the database.    *    *<p>The default and most common, as Gerrit does not behave well when a patch set has no    * corresponding ref in the repo.    */
DECL|enumConstant|REPO_BEFORE_DB
name|REPO_BEFORE_DB
block|,
comment|/**    * Update the database before touching the repository.    *    *<p>Generally only used when deleting patch sets, which should be deleted first from the    * database (for the same reason as above.)    */
DECL|enumConstant|DB_BEFORE_REPO
name|DB_BEFORE_REPO
block|; }
end_enum

end_unit


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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
package|;
end_package

begin_enum
DECL|enum|CommitMergeStatus
enum|enum
name|CommitMergeStatus
block|{
comment|/** */
DECL|enumConstant|CLEAN_MERGE
name|CLEAN_MERGE
block|,
comment|/** */
DECL|enumConstant|CLEAN_PICK
name|CLEAN_PICK
block|,
comment|/** */
DECL|enumConstant|ALREADY_MERGED
name|ALREADY_MERGED
block|,
comment|/** */
DECL|enumConstant|PATH_CONFLICT
name|PATH_CONFLICT
block|,
comment|/** */
DECL|enumConstant|MISSING_DEPENDENCY
name|MISSING_DEPENDENCY
block|,
comment|/** */
DECL|enumConstant|NO_PATCH_SET
name|NO_PATCH_SET
block|,
comment|/** */
DECL|enumConstant|REVISION_GONE
name|REVISION_GONE
block|,
comment|/** */
DECL|enumConstant|CRISS_CROSS_MERGE
name|CRISS_CROSS_MERGE
block|; }
end_enum

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_comment
comment|/* Current state within the basic workflow of the change **/
end_comment

begin_enum
DECL|enum|ChangeStatus
specifier|public
enum|enum
name|ChangeStatus
block|{
comment|/**    * Change is open and pending review, or review is in progress.    *    *<p>This is the default state assigned to a change when it is first created in the database. A    * change stays in the NEW state throughout its review cycle, until the change is submitted or    * abandoned.    *    *<p>Changes in the NEW state can be moved to:    *    *<ul>    *<li>{@link #MERGED} - when the Submit Patch Set action is used;    *<li>{@link #ABANDONED} - when the Abandon action is used.    *</ul>    */
DECL|enumConstant|NEW
name|NEW
block|,
comment|/**    * Change is closed, and submitted to its destination branch.    *    *<p>Once a change has been merged, it cannot be further modified by adding a replacement patch    * set. Draft comments however may be published, supporting a post-submit review.    */
DECL|enumConstant|MERGED
name|MERGED
block|,
comment|/**    * Change is closed, but was not submitted to its destination branch.    *    *<p>Once a change has been abandoned, it cannot be further modified by adding a replacement    * patch set, and it cannot be merged. Draft comments however may be published, permitting    * reviewers to send constructive feedback.    *    *<p>Changes in the ABANDONED state can be moved to:    *    *<ul>    *<li>{@link #NEW} - when the Restore action is used.    *</ul>    */
DECL|enumConstant|ABANDONED
name|ABANDONED
block|}
end_enum

end_unit


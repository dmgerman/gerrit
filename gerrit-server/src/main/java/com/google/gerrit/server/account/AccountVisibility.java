begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
package|;
end_package

begin_comment
comment|/** Visibility level of other accounts to a given user. */
end_comment

begin_enum
DECL|enum|AccountVisibility
specifier|public
enum|enum
name|AccountVisibility
block|{
comment|/** All accounts are visible to all users. */
DECL|enumConstant|ALL
name|ALL
block|,
comment|/** Accounts sharing a group with the given user. */
DECL|enumConstant|SAME_GROUP
name|SAME_GROUP
block|,
comment|/** Accounts in a group that is visible to the given user. */
DECL|enumConstant|VISIBLE_GROUP
name|VISIBLE_GROUP
block|,
comment|/**    * Other accounts are not visible to the given user unless they are explicitly    * collaborating on a change.    */
DECL|enumConstant|NONE
name|NONE
block|; }
end_enum

end_unit


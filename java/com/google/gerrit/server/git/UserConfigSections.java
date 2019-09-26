begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
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
package|;
end_package

begin_class
DECL|class|UserConfigSections
specifier|public
class|class
name|UserConfigSections
block|{
comment|/** The general user preferences. */
DECL|field|GENERAL
specifier|public
specifier|static
specifier|final
name|String
name|GENERAL
init|=
literal|"general"
decl_stmt|;
comment|/** The my menu user preferences. */
DECL|field|MY
specifier|public
specifier|static
specifier|final
name|String
name|MY
init|=
literal|"my"
decl_stmt|;
DECL|field|KEY_URL
specifier|public
specifier|static
specifier|final
name|String
name|KEY_URL
init|=
literal|"url"
decl_stmt|;
DECL|field|KEY_TARGET
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TARGET
init|=
literal|"target"
decl_stmt|;
DECL|field|KEY_ID
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ID
init|=
literal|"id"
decl_stmt|;
comment|/** The table column user preferences. */
DECL|field|CHANGE_TABLE
specifier|public
specifier|static
specifier|final
name|String
name|CHANGE_TABLE
init|=
literal|"changeTable"
decl_stmt|;
DECL|field|CHANGE_TABLE_COLUMN
specifier|public
specifier|static
specifier|final
name|String
name|CHANGE_TABLE_COLUMN
init|=
literal|"column"
decl_stmt|;
comment|/** The edit user preferences. */
DECL|field|EDIT
specifier|public
specifier|static
specifier|final
name|String
name|EDIT
init|=
literal|"edit"
decl_stmt|;
comment|/** The diff user preferences. */
DECL|field|DIFF
specifier|public
specifier|static
specifier|final
name|String
name|DIFF
init|=
literal|"diff"
decl_stmt|;
DECL|method|UserConfigSections ()
specifier|private
name|UserConfigSections
parameter_list|()
block|{}
block|}
end_class

end_unit


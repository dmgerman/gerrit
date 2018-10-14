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

begin_comment
comment|/*  * Input for a commentlink configuration on a project.  */
end_comment

begin_class
DECL|class|CommentLinkInput
specifier|public
class|class
name|CommentLinkInput
block|{
comment|/** A JavaScript regular expression to match positions to be replaced with a hyperlink. */
DECL|field|match
specifier|public
name|String
name|match
decl_stmt|;
comment|/** The URL to direct the user to whenever the regular expression is matched. */
DECL|field|link
specifier|public
name|String
name|link
decl_stmt|;
comment|/** Whether the commentlink is enabled. */
DECL|field|enabled
specifier|public
name|Boolean
name|enabled
decl_stmt|;
block|}
end_class

end_unit

